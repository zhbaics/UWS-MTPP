/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.data.BootHost;
import guisanboot.data.LogicalVol;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.MirrorGrp;
import guisanboot.data.Pool;
import guisanboot.data.Snapshot;
import guisanboot.data.SourceAgent;
import guisanboot.data.View;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefHostVolume;
import guisanboot.ui.ChiefSnapshot;
import guisanboot.ui.CreateSnapDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefSnap;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.entity.ChiefCloneDiskList;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class CrtSnapAction extends GeneralActionForMainUi{
    private int mode; // 1: create sync snap  0: create anti-sync snap

    public CrtSnapAction( int mode ){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtSnap",
            MenuAndBtnCenterForMainUi.FUNC_CRT_SNAP
        );
        this.mode = mode;
    }

    private boolean isInvalidMaxSnapLicense(){
        int maxNum_mtpp = ResourceCenter.MAX_SNAP_NUM;
        int maxNum_cmdp = ResourceCenter.MAX_SNAP_CMDP_NUM;
        if( maxNum_mtpp <=0 && maxNum_cmdp <=0 ){
            JOptionPane.showMessageDialog(view,
                SanBootView.res.getString("MenuAndBtnCenter.error.badSnapNum")
            );
            return true;
        }
        return false;
    }

    private int getMaxSnapNum(){
        int maxNum_mtpp = ResourceCenter.MAX_SNAP_NUM;
        int maxNum_cmdp = ResourceCenter.MAX_SNAP_CMDP_NUM;
        if( maxNum_mtpp > maxNum_cmdp ){
            return maxNum_mtpp;
        }else{
            return maxNum_cmdp;
        }
    }

    private boolean isInvalidMaxSnapLicenseForMTPP(){
        int maxNum = ResourceCenter.MAX_SNAP_NUM;
        if( maxNum <=0 ){
SanBootView.log.error( getClass().getName(),"(CrtSnapAction) bad system allowed max snap num: "+ maxNum );
            JOptionPane.showMessageDialog(view,
                SanBootView.res.getString("MenuAndBtnCenter.error.badSnapNum")
            );
            return true;
        }
        return false;
    }

    private boolean isInvalidMaxSnapLicenseForCMDP(){
        int maxNum = ResourceCenter.MAX_SNAP_CMDP_NUM;
        if( maxNum <= 0 ){
SanBootView.log.error( getClass().getName(),"(CrtSnapAction) bad system allowed max snap num: "+ maxNum );
            JOptionPane.showMessageDialog(view,
                SanBootView.res.getString("MenuAndBtnCenter.error.badSnapNum")
            );
            return true;
        }
        return false;
    }

    @Override public void doAction(ActionEvent evt){
        int maxNum = 0;
        boolean isOk;
        boolean needSendNetbootInfo = false;
        BootHost host = null;
        PPProfile prof = null;

SanBootView.log.info(getClass().getName(),"########### Entering create snap action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !( selObj instanceof ChiefSnapshot ) ){
            return;
        }

        ChiefSnapshot chiefSnap = (ChiefSnapshot)selObj;
        BrowserTreeNode chiefSnapNode = chiefSnap.getTreeNode();
        BrowserTreeNode fNode = chiefSnap.getFatherNode();
        Object volObj = fNode.getUserObject();

        int rootid = -1;
        int snapid = -1;
        int poolid = -1;
        if( volObj instanceof Volume ){
SanBootView.log.info( getClass().getName()," (CrtSnapAction)crt snap for volume" );
            if( isInvalidMaxSnapLicense() )return;
            Volume vol = (Volume)volObj;
            rootid = vol.getSnap_root_id();
            maxNum = getMaxSnapNum();
        }else if( volObj instanceof LogicalVol ){
SanBootView.log.info( getClass().getName()," (CrtSnapAction)crt snap for lv");
            if( isInvalidMaxSnapLicenseForMTPP() )return;
            LogicalVol lv =(LogicalVol)volObj;
            VolumeMap tgt = view.initor.mdb.getTargetOnLV( lv );
            maxNum = tgt.getMaxSnapNum();
            rootid = tgt.getVol_rootid();
        }else if( volObj instanceof View ){
SanBootView.log.info( getClass().getName()," (CrtSnapAction)crt snap for view");
            if( isInvalidMaxSnapLicense() )return;
            // 暂不支持view的快照
            View v = (View)volObj;
            maxNum = getMaxSnapNum();
            rootid = v.getSnap_root_id();
        }else if( volObj instanceof MirrorDiskInfo ){
            if( isInvalidMaxSnapLicense() )return;
            MirrorDiskInfo mdi = (MirrorDiskInfo)volObj;

            BrowserTreeNode chiefHVolNode = mdi.getFatherNode();
            Object chiefHVolObj = chiefHVolNode.getUserObject();
            ChiefHostVolume chiefHVol = (ChiefHostVolume)chiefHVolObj;
            BrowserTreeNode hostNode = chiefHVol.getFatherNode();
            SourceAgent sa = (SourceAgent)hostNode.getUserObject();
            maxNum = sa.isCMDPProtect()?ResourceCenter.MAX_SNAP_CMDP_NUM:ResourceCenter.MAX_SNAP_NUM;
            rootid = mdi.getSnap_rootid();
        }else if( volObj instanceof CloneDisk ){
            if( isInvalidMaxSnapLicense() )return;
            CloneDisk cloneDisk = (CloneDisk)volObj;
            rootid = cloneDisk.getRoot_id();

            BrowserTreeNode chiefCloneDiskNode = cloneDisk.getFatherNode();
            Object chiefCloneDiskObj = chiefCloneDiskNode.getUserObject();
            ChiefCloneDiskList chiefCloneDiskList = (ChiefCloneDiskList)chiefCloneDiskObj;

            BrowserTreeNode generalVolNode = chiefCloneDiskList.getFatherNode();
            Object generalVolObj = generalVolNode.getUserObject();
            if( generalVolObj instanceof MirrorDiskInfo ){
                MirrorDiskInfo mdi = (MirrorDiskInfo)generalVolObj;
                BrowserTreeNode chiefHVolNode = mdi.getFatherNode();
                Object chiefHVolObj = chiefHVolNode.getUserObject();
                ChiefHostVolume chiefHVol = (ChiefHostVolume)chiefHVolObj;
                BrowserTreeNode hostNode = chiefHVol.getFatherNode();
                SourceAgent sa = (SourceAgent)hostNode.getUserObject();
                maxNum = sa.isCMDPProtect()?ResourceCenter.MAX_SNAP_CMDP_NUM:ResourceCenter.MAX_SNAP_NUM;
            }else {
                VolumeMap volMap = (VolumeMap)generalVolObj;
                BrowserTreeNode chiefHVolNode = volMap.getFatherNode();
                Object chiefHVolObj = chiefHVolNode.getUserObject();
                ChiefHostVolume chiefHVol = (ChiefHostVolume)chiefHVolObj;
                BrowserTreeNode hostNode = chiefHVol.getFatherNode();
                BootHost bh = (BootHost)hostNode.getUserObject();
                maxNum = bh.isCMDPProtect()?ResourceCenter.MAX_SNAP_CMDP_NUM:ResourceCenter.MAX_SNAP_NUM;
            }
        }else{ // VolumeMap
SanBootView.log.info( getClass().getName()," (CrtSnapAction)crt snap for volmap");
            VolumeMap volMap = (VolumeMap)volObj;
            rootid = volMap.getVol_rootid();

            BrowserTreeNode chiefHVolNode = volMap.getFatherNode();
            Object chiefHVolObj = chiefHVolNode.getUserObject();
            ChiefHostVolume chiefHVol = (ChiefHostVolume)chiefHVolObj;
            BrowserTreeNode hostNode = chiefHVol.getFatherNode();
            host = (BootHost)hostNode.getUserObject();

            prof = view.initor.mdb.getBelongedDg( host.getID(),volMap.getVolDiskLabel() );

            if( mode == 0 ){
                if( prof != null ){
                    if( prof.isValidDriveGrp() ){
                        if( prof.getItem("C") != null ){
                            if( volMap.getVolDiskLabel().substring(0,1).toUpperCase().equals("C") ){
SanBootView.log.warning( getClass().getName(),"(CrtSnapAction) can't create latest snapshot for dg which include C disk." );
                                JOptionPane.showMessageDialog(view,
                                    SanBootView.res.getString("MenuAndBtnCenter.error.crtLatestSnapForOSDisk")
                                );
                            }else{
                                JOptionPane.showMessageDialog(view,
                                    SanBootView.res.getString("MenuAndBtnCenter.error.crtLatestSnapForOSDisk1")
                                );
                            }
                            return;
                        }
                    }else{
                        if( volMap.getVolDiskLabel().substring(0,1).toUpperCase().equals("C") ){
SanBootView.log.warning( getClass().getName(),"(CrtSnapAction) can't create latest snapshot for C." );
                            // 因为给C盘创建快照前，需要修改注册表，但是“最新快照”只是对target做快照（在服务器上），
                            // 所以无法修改注册表，所以不能给C盘创建"最新快照"
                            JOptionPane.showMessageDialog(view,
                                SanBootView.res.getString("MenuAndBtnCenter.error.crtLatestSnapForOSDisk")
                            );
                            return;
                        }
                    }
                }
            }

            if( host.isMTPPProtect() ){
                if( isInvalidMaxSnapLicenseForMTPP() )return;
                maxNum = volMap.getMaxSnapNum();
            }else{
                if( isInvalidMaxSnapLicenseForCMDP() ) return;
                if( volMap.isCMDPProtect() ){
                    MirrorGrp mg = view.initor.mdb.getMGFromVectorOnRootID( rootid );
                    if( mg != null ){
                        maxNum = mg.getMg_max_snapshot();
                    }
                }else{
                    if( isInvalidMaxSnapLicenseForMTPP() )return;
                    maxNum = volMap.getMaxSnapNum();
                }
            }

            if( volMap.getVolDiskLabel().toUpperCase().equals("C:\\") ){
                // 只有对C盘做快照时才需要发送 netbootinfo 到所有相关的dest uws server上
                needSendNetbootInfo = true;
            }
        }

        if( maxNum <= 0 ){
SanBootView.log.error( getClass().getName(),"(CrtSnapAction) bad user defined max snap num: "+ maxNum );
            JOptionPane.showMessageDialog(view,
                SanBootView.res.getString("MenuAndBtnCenter.error.badSnapNum1")
            );
            return;
        }

        if( rootid == -1 ) {
SanBootView.log.error( getClass().getName()," Can't get root id.");
            return;
        }

        isOk = view.initor.mdb.updateOrphanVol();
        if( !isOk ){
            view.showError1(
                ResourceCenter.CMD_GET_VOL,
                view.initor.mdb.getErrorCode(),
                view.initor.mdb.getErrorMessage()
            );
            return;
        }else{
            Volume vol = view.initor.mdb.getVolume( rootid );
            if( vol == null ){
SanBootView.log.error(getClass().getName(), "can't find volume, rootid: " + rootid );
                return;
            }else{
                snapid = vol.getSnap_local_snapid();
                poolid = vol.getSnap_pool_id();
            }
        }

        Pool pool = view.initor.mdb.getPool( poolid );
        if( pool == null ){
SanBootView.log.error( getClass().getName()," not found pool: poolid: " + poolid );
            JOptionPane.showMessageDialog( view,
                SanBootView.res.getString("MenuAndBtnCenter.error.notFoundPool")
            );
            return;
        }else{
            isOk = view.initor.mdb.getPoolInfo( pool.getPool_id() );
            if( !isOk ){
SanBootView.log.error( getClass().getName()," can't get pool info: poolid: " + pool.getPool_id() );
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_POOLINFO )+" : "+
                        view.initor.mdb.getErrorMessage()
                );
                return;
            }

            long total = view.initor.mdb.getPoolTotalCap();
            long vused = view.initor.mdb.getPoolVUsed();
            long avail = total - vused;
            if( avail <=0 ){
SanBootView.log.error( getClass().getName()," There is no available capacity in pool: "+ pool.getPool_id() );
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("CreateOrphanVol.error.noSpaceOnPool1")+ " " +pool.getPool_name()+"\n"+
                        SanBootView.res.getString("MenuAndBtnCenter.error.crtSnap")
                );
                return;
            }
        }

        CreateSnapDialog dialog = new CreateSnapDialog( view,mode );
        int width = 355+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 165+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object[] ret = dialog.getValues();
        if( ret == null || ret.length <=0 ) return;

        String name = (String)ret[0];
        if( mode == 0 ){ // create async snapshot for cmdp
            if( name.length() >= 241 ){
                name = name.substring( 0,241 ) + "[flawed_snap]";
            }else{
                name = name + "[flawed_snap]";
            }
        }

        ProgressDialog crtSnapDiag = new ProgressDialog(
            view,
            SanBootView.res.getString("View.pdiagTitle.crtSnap"),
            SanBootView.res.getString("View.pdiagTip.crtSnap")
        );
        CrtSnapshot crtSnap = new CrtSnapshot(
            crtSnapDiag,rootid,snapid,pool.getPool_id(),
            name,maxNum,needSendNetbootInfo,
            host,prof,mode
        );
        crtSnap.start();
        crtSnapDiag.mySetSize();
        crtSnapDiag.setLocation( view.getCenterPoint( crtSnapDiag.getDefWidth(),crtSnapDiag.getDefHeight() ) );
        crtSnapDiag.setVisible( true );

        Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_CRT_SNAP );

        if( crtSnap.isOK() && !crtSnap.isCancelOp()){
            audit.setEventDesc( "Create snapshot: " + name + " successfully." );
            view.audit.addAuditRecord( audit );

            // 显示点击 chiefSnapNode 后的右边tabpane中的内容
            view.setCurNode( chiefSnapNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefSnap peOnChiefSnap = new ProcessEventOnChiefSnap( view );
            TreePath path = new TreePath( chiefSnapNode.getPath() );
            peOnChiefSnap.processTreeSelection( path );
            peOnChiefSnap.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }else{
            if( !crtSnap.isOK() ){
                audit.setEventDesc( "Failed to create snapshot: " + name );
                view.audit.addAuditRecord( audit );

                view.showError1(
                    crtSnap.getErrCmd(),
                    crtSnap.getErrCode(),
                    crtSnap.getErrMsg()
                );
            }
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of create snap action. " );
    }

    class CrtSnapshot extends Thread{
        ProgressDialog pdiag;
        int rootid,snapid,poolid;
        String name;
        int maxNum;
        boolean needSendNetbootInfo;
        BootHost host;
        boolean isOk;
        boolean cancelOp;
        int errcode;
        String errmsg;
        int cmd;
        MirrorGrp mg;
        PPProfile prof;
        int mode;   // 1: create sync snap  0: create anti-sync snap

        public CrtSnapshot(
            ProgressDialog pdiag,
            int rootid,
            int snapid,
            int poolid,
            String name,
            int maxNum,
            boolean needSendNetbootInfo,
            BootHost host,
            PPProfile prof,
            int mode
        ){
            this.pdiag = pdiag;
            this.rootid = rootid;
            this.snapid = snapid;
            this.poolid = poolid;
            this.name = name;
            this.maxNum = maxNum;
            this.needSendNetbootInfo = needSendNetbootInfo;
            this.host = host;
            this.prof = prof;
            this.mode = mode;
        }

        Runnable close = new Runnable(){
            public void run(){
                pdiag.dispose();
            }
        };

        @Override public void run(){
            cancelOp = false;
            isOk = view.initor.mdb.getSnapshot( rootid );
            if( isOk ){
                int num = view.initor.mdb.getSnapshotNum();
SanBootView.log.info( getClass().getName()," Snap num on disk: "+ num  );
                if( num >= maxNum ){
                    Snapshot snap = view.initor.mdb.getLastSnapshot();
SanBootView.log.info( getClass().getName(), " last snap crt time: "+ snap.getSnap_create_time() );

                    int ret = JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm11"),
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
                        cancelOp = true;
                    }

                    if( !cancelOp ){
                        view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
                        isOk = view.initor.mdb.delSnapshot( snap.getSnap_root_id(),snap.getSnap_local_snapid() );
                        view.initor.mdb.restoreOldTimeOut();
                        if( !isOk ){
                            this.cmd = ResourceCenter.CMD_DEL_SNAP;
                            this.errcode = view.initor.mdb.getErrorCode();
                            this.errmsg = view.initor.mdb.getErrorMessage();
                        }
                    }
                }
            }else{
                this.cmd = ResourceCenter.CMD_GET_SNAP;
                this.errcode = view.initor.mdb.getErrorCode();
                this.errmsg = view.initor.mdb.getErrorMessage();
            }

            if( isOk && !cancelOp ){
                view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
                if( prof == null ){
                    isOk = view.initor.mdb.addSnapshot(
                        rootid, snapid, poolid, name
                    );
                }else{
                    String dg_name = "";
                    int isGrp = 0;
                    if( prof.isValidDriveGrp() ){
                        dg_name = prof.getDriveGrpName();
                        isGrp = 1;
                    }else{
                        dg_name = prof.getMainDiskItem().getVolMap().getVolName();
                    }
                    isOk = view.initor.mdb.addSnapshotForCMDP(
                        host.getIP(),host.getPort(),dg_name ,isGrp,name,mode
                    );
                }
                view.initor.mdb.restoreOldTimeOut();
                if( !isOk ){
                    this.cmd = ResourceCenter.CMD_ADD_SNAP;
                    this.errcode = view.initor.mdb.getErrorCode();
                    this.errmsg = view.initor.mdb.getErrorMessage();
                }else{
                    // 给mg发信号, 通知它：卷上创建了一个新快照
                    view.initor.mdb.sendSigToMgOnRootID( rootid,MirrorGrp.SIG_SIGUSR2 );
                }
            }

            /*
            // 发送 netbootinfo 到所有的dest uws server中
            if( needSendNetbootInfo ){
                // 不管成功与否
                view.initor.mdb.sendNetbootInfoToDestUWS( rootid, host );
            }
            */

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
    }
}
