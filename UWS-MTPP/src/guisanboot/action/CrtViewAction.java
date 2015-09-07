/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.data.BasicVDisk;
import guisanboot.data.BootHost;
import guisanboot.data.LogicalVol;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.Pool;
import guisanboot.data.Snapshot;
import guisanboot.data.SourceAgent;
import guisanboot.data.View;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefSnapshot;
import guisanboot.ui.CreateViewDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.MirroredSnap;
import guisanboot.ui.ProcessEventOnMirroredSnap;
import guisanboot.ui.ProcessEventOnSnapshot;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class CrtViewAction extends GeneralActionForMainUi{
    public CrtViewAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtView",
            MenuAndBtnCenterForMainUi.FUNC_CRT_VIEW
        );
    }

    @Override public void doAction(ActionEvent evt){
        Object volObj = null;
        BrowserTreeNode snapFNode,snapNode;
        int snap_pool_id;
        int snap_rootid;
        int snap_local_id;
        boolean needZeroUUID = false;

SanBootView.log.info(getClass().getName(),"########### Entering create view action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isSnapshot = ( selObj instanceof Snapshot );
        boolean isMirroredSnap = ( selObj instanceof MirroredSnap );
        if( !isSnapshot && !isMirroredSnap ){
            return;
        }

        int maxNum = ResourceCenter.MAX_SNAP_NUM;
        if( maxNum <=0 ){
SanBootView.log.error( getClass().getName(),"(CrtViewAction) bad system allowed max snap num: "+ maxNum );
            JOptionPane.showMessageDialog(view,
                SanBootView.res.getString("MenuAndBtnCenter.error.badSnapNum")
            );
            return;
        }

        if( selObj instanceof Snapshot ){
            Snapshot snap = (Snapshot)selObj;
            snapNode = snap.getTreeNode();
            snap_pool_id = snap.getSnap_pool_id();
            snap_rootid = snap.getSnap_root_id();
            snap_local_id = snap.getSnap_local_snapid();
            snapFNode = snap.getFatherNode();
        }else{
            MirroredSnap ms = (MirroredSnap)selObj;
            snapNode = ms.getTreeNode();
            snap_pool_id = ms.snap.getSnap_pool_id();
            snap_rootid = ms.snap.getSnap_root_id();
            snap_local_id = ms.snap.getSnap_local_snapid();
            snapFNode = ms.getFatherNode();
        }

        Object snapFObj = snapFNode.getUserObject();
        if( snapFObj instanceof ChiefSnapshot ){
            ChiefSnapshot chiefSnap = (ChiefSnapshot)snapFObj;
            BrowserTreeNode fNode = chiefSnap.getFatherNode();
            volObj = fNode.getUserObject();
        }else if( snapFObj instanceof MirrorDiskInfo ){
            volObj = snapFObj;
        }else{
            return;
        }

        int rootid = -1;
  //      int snapid = -1;
  //      int poolid = -1;
        if( volObj instanceof Volume ){
SanBootView.log.info( getClass().getName()," (CrtViewAction)crt view for volume" );
            Volume vol = (Volume)volObj;
            rootid = vol.getSnap_root_id();
        }else if( volObj instanceof LogicalVol ){
SanBootView.log.info( getClass().getName()," (CrtViewAction)crt view for lv");
            LogicalVol lv =(LogicalVol)volObj;
            VolumeMap tgt = view.initor.mdb.getTargetOnLV( lv );
            maxNum = tgt.getMaxSnapNum();
            rootid = tgt.getVol_rootid();
        }else if( volObj instanceof View ){
SanBootView.log.info( getClass().getName()," (CrtViewAction)crt view for view");
            View v = (View)volObj;
            // 暂不支持对view的快照再作view
            rootid = v.getSnap_root_id();
        }else if( volObj instanceof VolumeMap ){
SanBootView.log.info( getClass().getName()," (CrtViewAction)crt view for volmap");
            VolumeMap volMap = (VolumeMap)volObj;
            BootHost host = view.initor.mdb.getBootHostFromVector( volMap.getVolClntID() );
            if( host != null ){
                needZeroUUID = host.isCMDPProtect();
            }else{
                needZeroUUID  = true; // 保险起见，需要zero uuid on sector for view
            }

            maxNum = 250;
            rootid = volMap.getVol_rootid();
        }else if( volObj instanceof CloneDisk ){
            CloneDisk cloneDisk =(CloneDisk)volObj;
            rootid = cloneDisk.getRoot_id();
        }else{
            MirrorDiskInfo mdi =(MirrorDiskInfo)volObj;
            SourceAgent srcAgnt = view.initor.mdb.getSrcAgntFromVectorOnID( mdi.getSrc_agnt_id() );
            if( srcAgnt != null ){
                needZeroUUID = srcAgnt.isCMDPProtect();
            }else{
                needZeroUUID = true; // 保险起见，需要zero uuid on sector for view
            }
            rootid = mdi.getSnap_rootid();
        }

        if( rootid == -1 ) {
SanBootView.log.error( getClass().getName()," Can't get root id.");
            return;
        }

        Pool pool = view.initor.mdb.getPool( snap_pool_id );
        if( pool == null ){
            JOptionPane.showMessageDialog( view,
                SanBootView.res.getString("MenuAndBtnCenter.error.notFoundPool")
            );
            return;
        }else{
            boolean isOk = view.initor.mdb.getPoolInfo( pool.getPool_id() );
            if( !isOk ){
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_POOLINFO )+" : "+
                        view.initor.mdb.getErrorMessage()
                );
                return;
            }

            long total = view.initor.mdb.getPoolTotalCap();
            long vused = view.initor.mdb.getPoolVUsed();
            long avail = total-vused;
            if( avail <=0 ){
//                JOptionPane.showMessageDialog(view,
//                        SanBootView.res.getString("CreateOrphanVol.error.noSpaceOnPool2")+ " " +pool.getPool_name()+"\n"+
//                        SanBootView.res.getString("MenuAndBtnCenter.error.crtView")
//                );
//                return;
            }
        }

        CreateViewDialog  dialog = new CreateViewDialog( view,snap_rootid,snap_local_id );
        int width = 275+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 165+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object[] ret = dialog.getValues();
        if( ret == null || ret.length <= 0 ) return;
        String name = (String)ret[0];

        ProgressDialog pdiag = new ProgressDialog(
            view,
            SanBootView.res.getString("View.pdiagTitle.crtView"),
            SanBootView.res.getString("View.pdiagTip.crtView")
        );

        CrtView crtView = new CrtView( pdiag,snap_rootid, snap_local_id,pool.getPool_id(),name,maxNum,needZeroUUID );
        crtView.start();
        pdiag.mySetSize();
        pdiag.setLocation( view.getCenterPoint( pdiag.getDefWidth(),pdiag.getDefHeight() ) );
        pdiag.setVisible( true );

        Audit audit = view.audit.registerAuditRecord( 0,MenuAndBtnCenterForMainUi.FUNC_CRT_VIEW );

        if( crtView.isOK() && !crtView.isCancelOp() ){
            audit.setEventDesc( "Create view: " + name + " successfully." );
            view.audit.addAuditRecord( audit );

            if( isSnapshot ){
                // 显示点击 snapNode 后的右边tabpane中的内容
                // 精确地获取snapNode ！！！！！！！！！！！！！！
                if( snapNode == null ) {
SanBootView.log.info(getClass().getName(),"########### End of create view action. " );
                    return;
                }

                view.setCurNode( snapNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                ProcessEventOnSnapshot peOnSnap = new ProcessEventOnSnapshot( view );
                TreePath path = new TreePath( snapNode.getPath() );
                peOnSnap.processTreeSelection( path );
                peOnSnap.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }else{
                // 显示点击 snapNode 后的右边tabpane中的内容
                // 精确地获取snapNode ！！！！！！！！！！！！！！！
                if( snapNode == null ) {
SanBootView.log.info(getClass().getName(),"########### End of create view action. " );
                    return;
                }

                view.setCurNode( snapNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                ProcessEventOnMirroredSnap peOnMSnap = new ProcessEventOnMirroredSnap( view );
                TreePath path = new TreePath( snapNode.getPath() );
                peOnMSnap.processTreeSelection( path );
                peOnMSnap.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }

        }else{
            if( !crtView.isOK() ){
                audit.setEventDesc( "Failed to create view: " + name );
                view.audit.addAuditRecord( audit );

                view.showError1(
                    crtView.getErrCmd(),
                    view.initor.mdb.getErrorCode(),
                    view.initor.mdb.getErrorMessage()
                );
            }
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of create view action. " );
    }

    class CrtView extends Thread{
        ProgressDialog pdiag;
        int rootid,snapid,poolid;
        String name;
        int maxNum;
        boolean needZeroUUID;
        boolean isOk;
        boolean cancelOp;
        int errcode;
        String errmsg;
        int cmd;
        ArrayList<View> viewList = new ArrayList<View>();

        public CrtView(
            ProgressDialog pdiag,
            int rootid,
            int snapid,
            int poolid,
            String name,
            int maxNum,
            boolean needZeroUUID
        ){
            this.pdiag = pdiag;
            this.rootid = rootid;
            this.snapid = snapid;
            this.poolid = poolid;
            this.name = name;
            this.maxNum = maxNum;
            this.needZeroUUID = needZeroUUID;
        }

        Runnable close = new Runnable(){
            public void run(){
                pdiag.dispose();
            }
        };

        @Override public void run(){
            int i,j,size,local_snap_id,size1,cnt;
            Snapshot snap,newSnap;
            View viewObj;
            BasicVDisk directChild;
            cancelOp = false;

            isOk = view.initor.mdb.getSnapshot( rootid );
            if( isOk ){
                int num = view.initor.mdb.getSnapshotNum();
SanBootView.log.info( getClass().getName()," Snap num on disk: "+ num  );
                if( num >= maxNum ){
                    // 检查其他快照(除要做view的快照)是否都有view
                    isOk = view.initor.mdb.queryVSnapDB(
                        "select * from " + ResourceCenter.VSnap_DB +" where "+
                            BasicVDisk.BVDisk_Snap_Root_ID + "=" + rootid +";"
                    );

                    if( isOk ){
                        cnt = 0;
                        ArrayList snapList = view.initor.mdb.getAllSnapList();
                        size = snapList.size();
                        for( i=0; i<size; i++ ){
                            snap = (Snapshot)snapList.get(i);
                            if( snap.getSnap_local_snapid() != snapid ){
                                // 先更新一下snap's child list
                                newSnap = view.initor.mdb.getSnapshotFromQuerySql( snap.getSnap_local_snapid() );
                                if( newSnap != null ){
                                    snap.setSnap_child_list( newSnap.getSnap_child_list() );
                                }else{
SanBootView.log.error( getClass().getName(),"Not found snapshot from vsnap db: ["+snap.getSnap_root_id() +
                            "."+snap.getSnap_local_snapid()+"]" );
                                }

                                // 获取树上从该快照开始所有的 view 节点
                                viewList.clear();
                                getViewChild( snap );
                                size1 = viewList.size();
SanBootView.log.debug( getClass().getName()," Child of view from this snap node on tree: ["+snap.getSnap_root_id()+
                            "."+snap.getSnap_local_snapid() +"]" );
SanBootView.log.debug( getClass().getName(),"===========================");
                                for( j=0; j<size1; j++ ){
                                    viewObj = (View)viewList.get(j);
SanBootView.log.debug( getClass().getName()," view local_snap_id: "+viewObj.getSnap_local_snapid() +
                                        " view target_id: "+ viewObj.getSnap_target_id()
                                    );
                                }
SanBootView.log.debug( getClass().getName(),"===========================\r\n");

                                // 根据该快照的子节点来判断直属于它的view
                                ArrayList directChildList = snap.getChildList();
                                size1 = directChildList.size();
                                for( j=0; j<size1; j++ ){
                                    local_snap_id = ((Integer)directChildList.get(j)).intValue();
                                    directChild = view.initor.mdb.getVDisk( local_snap_id );
                                    if( directChild != null ){
                                        viewObj = null;
                                        if( directChild.isSnap() ){
                                            viewObj = searchViewList( directChild.getSnap_target_id() );
                                        }else{ // direct child is view or disk
                                            if( directChild.isView() ){
                                                if( directChild.getSnap_target_id() != snap.getSnap_target_id() ){
                                                    viewObj = new View( directChild );
                                                }
                                            }
                                        }

                                        if( viewObj != null ){
                                            cnt+=1;
SanBootView.log.debug( getClass().getName()," Found direct view: ["+ viewObj.getSnap_root_id()+
                                "." + viewObj.getSnap_local_snapid() +"]");
                                            break;
                                        }
                                    }else{
SanBootView.log.error( getClass().getName()," Not found disk from vsnap db: ["+ snap.getSnap_root_id() +"."+
                            local_snap_id +"]");
                                    }
                                }
                            }
                        }

                        if( cnt >= (size-1) ){
                            // 说明除了要创建view的快照外所有的快照都有view了，直接报告该错误。
                            JOptionPane.showMessageDialog(view,
                                SanBootView.res.getString("MenuAndBtnCenter.error.beyondMaxSnapNum")
                            );
                            cancelOp = true;
                        }
                    }else{
                        this.cmd = ResourceCenter.CMD_GET_VIEW;
                        this.errcode = view.initor.mdb.getErrorCode();
                        this.errmsg = view.initor.mdb.getErrorMessage();
                    }
                }
            }else{
                this.cmd = ResourceCenter.CMD_GET_SNAP;
                this.errcode = view.initor.mdb.getErrorCode();
                this.errmsg = view.initor.mdb.getErrorMessage();
            }

            if( isOk && !cancelOp ){
                view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
                isOk = view.initor.mdb.addView( name, rootid, snapid,poolid );
                view.initor.mdb.restoreOldTimeOut();
                if( !isOk ){
                    this.cmd = ResourceCenter.CMD_ADD_VIEW;
                    this.errcode = view.initor.mdb.getErrorCode();
                    this.errmsg = view.initor.mdb.getErrorMessage();
                }else{
                    if( needZeroUUID ){
                        View newView = view.initor.mdb.getCrtView();
                        isOk = view.initor.mdb.zeroUUIDOnSector( newView.getSnap_root_id(),newView.getSnap_local_snapid() );
                        if( !isOk ){
SanBootView.log.error(getClass().getName(), "failed to zero uuid of sector for view(pdmc): "+ newView.getSnap_root_id()+"/"+newView.getSnap_local_snapid() );
                            this.cmd = ResourceCenter.CMD_ADD_VIEW;
                            this.errcode = view.initor.mdb.getErrorCode();
                            this.errmsg = view.initor.mdb.getErrorMessage();
                            view.initor.mdb.delView( newView ); // 将它删除，不管结果
                        }
                    }
                }
            }

            try{
                SwingUtilities.invokeAndWait( close );
            }catch( Exception ex ){
                ex.printStackTrace();
            }
        }

        public boolean isOK(){
            return isOk;
        }

        public boolean isCancelOp(){
            return cancelOp;
        }

        public int getErrCmd(){
            return cmd;
        }

        public int getErrCode(){
            return errcode;
        }

        public String getErrMsg(){
            return errmsg;
        }

        private void getViewChild( BasicVDisk beginDisk ){
            ArrayList childIdList = beginDisk.getChildList();
            ArrayList childDiskList = view.initor.mdb.getDiskFromQuerySql( childIdList );
            int size = childDiskList.size();
            for( int i=0; i<size; i++ ){
                BasicVDisk disk = (BasicVDisk)childDiskList.get(i);
                if( !disk.isView() && !disk.isOriDisk() ){
                    getViewChild( disk );
                }else{
                    if( disk.isView() ){
                        viewList.add( new View( disk ) );
                    }
                }
            }
        }

        private View searchViewList( int target_id ){
            View viewObj;
            int size = viewList.size();
            for( int j=0; j<size; j++ ){
                viewObj = (View)viewList.get(j);
                if( viewObj.getSnap_target_id() == target_id ){
                    return viewObj;
                }
            }
            return null;
        }
    }
}
