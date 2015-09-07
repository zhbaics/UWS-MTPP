/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BasicVDisk;
import guisanboot.data.BriefVDisk;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.MirrorGrp;
import guisanboot.data.MirrorJob;
import guisanboot.data.Pool;
import guisanboot.data.SourceAgent;
import guisanboot.data.UWSrvNode;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.*;
import guisanboot.unlimitedIncMj.entity.ChiefCopyJobList;
import guisanboot.unlimitedIncMj.entity.ChiefUnLimitedIncSnapList;
import guisanboot.unlimitedIncMj.entity.UnlimitedIncMirroredSnap;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefCj;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 * BasicCrtMjAction.java
 *
 * Created on 2010-1-26, 12:20:08
 */
public class BasicCrtMjAction extends GeneralActionForMainUi{
    private boolean isCanceled = false;

    public BasicCrtMjAction( int func ){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtMj",
            func
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering create mirror job action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) {
SanBootView.log.info(getClass().getName(),"selobj is null!\n ########### End of create mirror job action. " );
            return;
        }

        realDo( selObj );
    }

    public boolean realDo( Object selObj ){
        Object[] ret;
        String mjName="",type1;
        boolean isOk,isTransferBranch=false;
        int root_id,snap_id=-1;
        ChiefMirrorJobList chiefMj=null;
        ChiefCopyJobList chiefCj=null;
        MirrorGrp mg = null;

        boolean isChiefMJList = ( selObj instanceof ChiefMirrorJobList );
        boolean isUiSnap = ( selObj instanceof UnlimitedIncMirroredSnap );
        if( !isChiefMJList && !isUiSnap ){
SanBootView.log.info(getClass().getName(),"bad type!\n ########### End of create mirror job action. " );
            return false;
        }

        int func = this.funcID;
        if( isChiefMJList ){
            chiefMj = (ChiefMirrorJobList)selObj;
            ret = view.getSomethingOnTreeFromChiefMjListObj( chiefMj );
            if( ret == null ) {
SanBootView.log.info(getClass().getName(),"something not got!\n ########### Endo of create mirror job action. " );
                return false;
            }
            mjName = (String)ret[0];
            root_id = ((Integer)ret[1]).intValue();
            type1 = (String)ret[2];
            isTransferBranch = ((Boolean)ret[3]).booleanValue();

            if( type1.equals("MirrorDiskInfo") ){
                // 对于回滚主机的卷（类型为mirrordiskinfo），要检查一下该卷是否真实存在，
                // 因为有可能回滚前这个卷就没有被镜像完整（第一次镜像没有完整结束，mj就被删除了）。
                if( !view.initor.mdb.isExistThisDisk( root_id ) ){
SanBootView.log.error( getClass().getName(),"this volume maybe not exist, it's rootid is : " + root_id + "\n ########### Endo of create mirror job action. " );
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notexistdisk")
                    );
                    return false;
                }
            }
        }else{
            UnlimitedIncMirroredSnap uisnap = (UnlimitedIncMirroredSnap)selObj;
            BrowserTreeNode chiefUISnapNode = uisnap.getFatherNode();
            if( chiefUISnapNode != null ){
                ChiefUnLimitedIncSnapList chiefUISnap  = (ChiefUnLimitedIncSnapList)chiefUISnapNode.getUserObject();
                BrowserTreeNode mdiNode = chiefUISnap.getFatherNode();
                if( mdiNode != null ){
                    BrowserTreeNode chiefCjNode = view.getChiefCJNodeOnMDI( mdiNode );
                    if( chiefCjNode != null ){
                        chiefCj = (ChiefCopyJobList)chiefCjNode.getUserObject();
                    }
                    MirrorDiskInfo mdi = (MirrorDiskInfo)mdiNode.getUserObject();
                    BrowserTreeNode chiefHVolNode = mdi.getFatherNode();
                    Object chiefHVolObj = chiefHVolNode.getUserObject();
                    ChiefHostVolume chiefHVol = (ChiefHostVolume)chiefHVolObj;
                    BrowserTreeNode hostNode = chiefHVol.getFatherNode();
                    SourceAgent sa = (SourceAgent)hostNode.getUserObject();
                    isTransferBranch = mdi.isCMDPProtect() && mdi.getSrc_agent_mp().toUpperCase().startsWith("C");
                }
            }
            root_id = uisnap.snap.getSnap_root_id();
            snap_id = uisnap.snap.getSnap_local_snapid();

            if( !getMainBranch( root_id ) ){
SanBootView.log.error( getClass().getName(),"failed to get main branch of this volume, it's rootid is : " + root_id + "\n ########### Endo of create mirror job action. " );
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.getMainBranch")
                );
                return false;
            }

            if( !getSnapList( root_id ) ){
SanBootView.log.error( getClass().getName(),"failed to get snapshot list of this volume, it's rootid is : " + root_id + "\n ########### Endo of create mirror job action. " );
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.getSnapList")
                );
                return false;
            }
            
            int id_on_main_branch = getSnapIDOnMainBranch( snap_id );
            if( id_on_main_branch == -1 ){
SanBootView.log.error( getClass().getName(),"failed to get id on main branch of this volume, it's rootid is : " + root_id + "\n ########### Endo of create mirror job action. " );
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.getIdOnMainBranch")
                );
                return false;
            }

            mjName ="CJ-"+uisnap.snap.getSnap_root_id()+"." + snap_id;
        }
        
        if( view.initor.mdb.getUcsDiskCount(root_id) == 0 && func == MenuAndBtnCenterForMainUi.FUNC_CRT_LMJ){
            JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notucsdisk")
                    );
                    return false;
        }

        SimpleGetDestUWSrvThread thread1 = new SimpleGetDestUWSrvThread( view );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.getDestSWU"),
            SanBootView.res.getString("View.pdiagTip.getDestSWU"),
            thread1
        );

        if( !thread1.isOk() ){ return false; }
//        boolean isucsvol = false;
//        if( view.initor.mdb.getUcsDiskCount(root_id) == 0) {
//            isucsvol = false;
//        } else {
//            isucsvol = true;
//        }
        EditMirrorJobDialog dialog = new EditMirrorJobDialog( view,null,mjName,func,isTransferBranch );
        int width = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 380+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        ret = dialog.getValues();
        if( ret == null ) {
SanBootView.log.info(getClass().getName(), "cancel to create the copy job.");
            this.isCanceled = true;
            return true;
        }

        String job_name = (String)ret[0];
        String job_desc = (String)ret[1];
        UWSrvNode uws = (UWSrvNode)ret[2];
        Pool pool = (Pool)ret[3];
        int opt = ((Integer)ret[4]).intValue();
        int type = ((Integer)ret[5]).intValue();
        int mgintervaltime = ((Integer)ret[6]).intValue();
        int mglogmaxtime = ((Integer)ret[7]).intValue();
        int mgmaxsnapshot = ((Integer)ret[8]).intValue();

        if( func == MenuAndBtnCenterForMainUi.FUNC_CRT_MJ ){
            //mg = view.initor.mdb.getMGFromVecOnManything( root_id, 1, "MG_"+root_id );
            mg = view.initor.mdb.getMGFromVectorOnRootID( root_id );
            if( mg == null ){
                // generate a mirror group object for this mirror job
                mg = new MirrorGrp(
                    "MG_"+root_id,
                     1,
                     root_id,
                    "MG_"+root_id
                );
                isOk = view.initor.mdb.addMg( mg );
                if( !isOk ){
                    JOptionPane.showMessageDialog(view,
                        ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_MG ) +" : "+view.initor.mdb.getErrorMessage()
                    );
                    return false;
                }else{
                    mg.setMg_id( view.initor.mdb.getNewId() );
                    view.initor.mdb.addMGToVector( mg );
                }
            }
        }

        MirrorJob mj = new MirrorJob(
            job_name,
            uws.getUws_ip(),
            uws.getUws_port(),
            opt,
            pool.getPool_id(),
            pool.getPool_passwd(),
            job_desc
        );

        if( func == MenuAndBtnCenterForMainUi.FUNC_CRT_MJ ){
            mj.setMj_mg_id( mg.getMg_id() );
        }else if( func == MenuAndBtnCenterForMainUi.FUNC_CRT_MJ1 ){
            mj.setMj_job_type( type );
            mj.setMj_track_src_rootid( root_id );
            mj.setMj_track_mode( MirrorJob.MJ_TRACK_MODE_AFT_CRT_SNAP );
            mj.setMj_track_src_type( MirrorJob.MJ_TRACK_SRC_TYPE_LOCALDISK );
        }else if(func == MenuAndBtnCenterForMainUi.FUNC_CRT_LMJ){
            mj.setMj_job_type(type);
            mj.setMg_name(job_name);
            mj.setMg_interval_time(mgintervaltime);
            mj.setMg_log_max_time(mglogmaxtime);
            mj.setMg_max_snapshot(mgmaxsnapshot);
            mj.setMj_track_src_rootid( root_id );
            mj.setMj_track_mode( MirrorJob.MJ_TRACK_MODE_AFT_CRT_SNAP );
            mj.setMj_track_src_type( MirrorJob.MJ_TRACK_SRC_TYPE_LOCALDISK );
        }else {
            mj.setMj_job_type( MirrorJob.MJ_TYPE_REMOTE_COPY_JOB );
            if( ( opt & MirrorJob.MJ_OPT_BRANCH ) != 0 ){
                // 对于copy job来说，如果选择了“传输分支”，则需要给出snap_id对应在主分支上的id
                mj.setMj_copy_src_snapid( getSnapIDOnMainBranch( snap_id ) );
            }else{
                mj.setMj_copy_src_snapid( snap_id );
            }
SanBootView.log.info( getClass().getName(),"mj's copy src snapid : " + mj.getMj_copy_src_snapid() );
            mj.setMj_copy_src_rootid( root_id );
            mj.setMj_copy_src_type( MirrorJob.MJ_TRACK_SRC_TYPE_LOCALDISK );
        }
        
        isOk = view.initor.mdb.addMj( mj );
        if( isOk ){
SanBootView.log.info( getClass().getName(),"new mj id is: " + view.initor.mdb.getNewId() );
            mj.setMj_id( view.initor.mdb.getNewId() );
            view.initor.mdb.monitorMJ( mj.getMj_id() );
            MirrorJob newMj = view.initor.mdb.getMonedMj( mj.getMj_id() );
            if( newMj == null ){
SanBootView.log.error( this.getClass().getName(),"Can't find mj object on mj_id: "+ mj.getMj_id() );
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_MJ ) +" : "+ SanBootView.res.getString("common.unknown")+SanBootView.res.getString("common.error")
                );
SanBootView.log.info(getClass().getName(),"########### End of create mirror job action." );
                return false;
            }else{
                view.initor.mdb.addMJToVector( newMj );
                if( chiefMj != null){
                    BrowserTreeNode chiefMjNode = chiefMj.getTreeNode();
                    view.setCurNode( chiefMjNode );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    ProcessEventOnChiefMj peOnChiefMj = new ProcessEventOnChiefMj( view );
                    TreePath path = new TreePath( chiefMjNode.getPath() );
                    peOnChiefMj.processTreeSelection( path );
                    peOnChiefMj.controlMenuAndBtnForTreeEvent();
                    view.getTree().setSelectionPath( path );
                    view.getTree().requestFocus();
                }else if( chiefCj != null ){
                    view.setCurNode( chiefCj.getTreeNode() );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    ProcessEventOnChiefCj peOnChiefCj = new ProcessEventOnChiefCj( view );
                    TreePath path = new TreePath( chiefCj.getTreeNode().getPath() );
                    peOnChiefCj.processTreeSelection( path );
                    peOnChiefCj.controlMenuAndBtnForTreeEvent();
                    view.getTree().setSelectionPath( path );
                    view.getTree().requestFocus();
                }
            }
        }else{
            JOptionPane.showMessageDialog(view,
                ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_MJ ) +" : "+view.initor.mdb.getErrorMessage()
            );
SanBootView.log.info(getClass().getName(),"########### End of create mirror job action. " );
            return false;
        }
        
SanBootView.log.info(getClass().getName(),"########### End of create mirror job action. " );
        return true;
    }
    
    public boolean isCanceled(){
        return this.isCanceled;
    }

    private boolean getMainBranch( int rootid ){
        boolean ok = view.initor.mdb.getBriefVDiskList(
            ResourceCenter.getCmd( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + rootid
        );
        return ok;
    }

    private boolean getSnapList( int rootid ){
        boolean ok = view.initor.mdb.queryVSnapDB(
            "select * from " + ResourceCenter.VSnap_DB +" where " + BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid
        );
        return ok;
    }

    private boolean isInChildList( ArrayList childList,int childId ){
        int size = childList.size();
        for( int i=0; i<size; i++ ){
            int aChildId = ((Integer)childList.get(i)).intValue();
            if( aChildId == childId ){
                return true;
            }
        }
        return false;
    }

    private boolean searchIdOnMainBranch( int localId,int childId ){
        ArrayList<BasicVDisk> list = view.initor.mdb.getAllQueryResult();
        int size = list.size();
        for( int i=0; i<size; i++ ){
            BasicVDisk bv = list.get(i);
            if( bv.getSnap_local_snapid() == localId ){
                if( isInChildList( bv.getChildList(),childId ) ){
                    return true;
                }
            }
        }
        return false;
    }

    private int getSnapIDOnMainBranch( int localId ){
        ArrayList mainBranchIdList = view.initor.mdb.getBriefVDiskList();
        int size = mainBranchIdList.size();
        for( int i=0; i<size; i++ ){
            BriefVDisk disk = (BriefVDisk)mainBranchIdList.get(i);
            if( localId == disk.getLocal_snap_id() ){
                return localId;
            }else{
                if( searchIdOnMainBranch( disk.getLocal_snap_id(),localId ) ){
                    return disk.getLocal_snap_id();
                }
            }
        }

        return -1;
    }
}
