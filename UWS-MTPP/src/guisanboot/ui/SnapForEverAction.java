package guisanboot.ui;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.Snapshot;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.unlimitedIncMj.entity.ChiefUnLimitedIncSnapList;
import guisanboot.unlimitedIncMj.entity.UnlimitedIncMirroredSnap;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefUIMSnap;
import guisanboot.unlimitedIncMj.service.ProcessEventOnUnlimitedIncMirrorVol;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

public class SnapForEverAction extends GeneralActionForMainUi{
    private boolean isOk = true;
    private DefaultTableModel model = null;
    private int row;

    public SnapForEverAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modSnap",
            MenuAndBtnCenterForMainUi.FUNC_SNAP_EVER
        );
    }

    @Override public void doAction(ActionEvent evt ){
SanBootView.log.info(getClass().getName(),"########### Entering set snap for-ever action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) {
SanBootView.log.info(getClass().getName(),"sel obj is null. \n########### End of set snap for-ever action. " );
            return;
        }
        this.doAction1( selObj );
    }

    public void doAction1( Object selObj ){
        int root_id;
        int local_snap_id;
        int snap_opened_type;
        Snapshot snap = null;
        UnlimitedIncMirroredSnap ui_snap = null;
        PPProfile prof = null;
        String snap_name;

        boolean isNormalSnap = ( selObj instanceof Snapshot );
        boolean isUiSnap = ( selObj instanceof UnlimitedIncMirroredSnap );

        if( !isNormalSnap && !isUiSnap ) {
            SanBootView.log.info(getClass().getName(),"bad type. \n########### End of set snap for-ever action. " );
            return;
        }

//        int ret = JOptionPane.showConfirmDialog(
//            view,
//            SanBootView.res.getString("MenuAndBtnCenter.confirm2"),
//            SanBootView.res.getString("common.confirm"),  //"Confirm",
//            JOptionPane.OK_CANCEL_OPTION
//        );
//
//        if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
//            SanBootView.log.info(getClass().getName(),"cancel this action. \n########### End of set snap for-ever action. " );
//            return;
//        }

        if( isNormalSnap ){
            snap = (Snapshot)selObj;
            root_id = snap.getSnap_root_id();
            local_snap_id = snap.getSnap_local_snapid();
            snap_name = snap.getSnap_desc();
            snap_opened_type = snap.getSnap_opened_type();

            VolumeMap volMap = view.initor.mdb.getVolMapOnRootID( root_id );
            if( volMap != null ){
                int clntID = volMap.getVolClntID();
                prof = view.initor.mdb.getBelongedDg( clntID,volMap.getVolDiskLabel() );
            }
        }else{
            ui_snap = (UnlimitedIncMirroredSnap)selObj;
            root_id = ui_snap.snap.getSnap_root_id();
            local_snap_id = ui_snap.snap.getSnap_local_snapid();
            snap_name = ui_snap.snap.getSnap_desc();
            snap_opened_type = ui_snap.snap.getSnap_opened_type();
        }

        ProgressDialog modSnapDiag = new ProgressDialog(
            view,
            SanBootView.res.getString("View.pdiagTitle.modSnap"),
            SanBootView.res.getString("View.pdiagTip.modSnap")
        );
        ModSnapshot modSnap = new ModSnapshot( modSnapDiag,root_id, local_snap_id,prof,snap_name, snap_opened_type );
        modSnap.start();
        modSnapDiag.mySetSize();
        modSnapDiag.setLocation( view.getCenterPoint( modSnapDiag.getDefWidth(),modSnapDiag.getDefHeight() ) );
        modSnapDiag.setVisible( true );

        Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_SNAP_EVER );

        this.isOk = modSnap.isOK();
        if( modSnap.isOK() ){
            audit.setEventDesc( "set snap for-ever: " + snap.getSnap_name() + " successfully." );
            view.audit.addAuditRecord( audit );

            if( this.model != null ){
                model.removeRow( row );
            }else{
                if( isNormalSnap ){
                    BrowserTreeNode fNode = snap.getFatherNode();
                    if( fNode == null ) return;

                    ChiefSnapshot chiefSnap = (ChiefSnapshot)fNode.getUserObject();
                    BrowserTreeNode chiefSnapNode = chiefSnap.getTreeNode();

                    BrowserTreeNode selSnapNode = view.getSnapNodeOnOrphVol( chiefSnapNode,snap.toTreeString() );
                    if( selSnapNode!= null ){
                        view.removeNodeFromTree( chiefSnapNode, selSnapNode );
                    }

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
                    BrowserTreeNode fNode = ui_snap.getFatherNode();
                    if( fNode == null ) return;

                    Object fobj = fNode.getUserObject();
                    if( fobj instanceof MirrorDiskInfo ){
                        MirrorDiskInfo mdi = (MirrorDiskInfo)fobj;
                        BrowserTreeNode mdiNode = mdi.getTreeNode();

                        BrowserTreeNode selSnapNode = view.getSnapNodeOnOrphVol( mdiNode,ui_snap.toTreeString() );
                        if( selSnapNode!= null ){
                            view.removeNodeFromTree( mdiNode, selSnapNode );
                        }

                        view.setCurNode( mdiNode );
                        view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                        ProcessEventOnUnlimitedIncMirrorVol peOnUiMirVol = new ProcessEventOnUnlimitedIncMirrorVol( view );
                        TreePath path = new TreePath( mdiNode.getPath() );
                        peOnUiMirVol.processTreeSelection( path );
                        peOnUiMirVol.controlMenuAndBtnForTreeEvent();
                        view.getTree().setSelectionPath( path );
                        view.getTree().requestFocus();
                    }else if( fobj instanceof ChiefUnLimitedIncSnapList ){
                        ChiefUnLimitedIncSnapList chiefUIMSnap = (ChiefUnLimitedIncSnapList)fobj;

                        BrowserTreeNode selSnapNode = view.getSnapNodeOnOrphVol( fNode,ui_snap.toTreeString() );
                        if( selSnapNode!= null ){
                            view.removeNodeFromTree( fNode, selSnapNode );
                        }
                        
                        view.setCurNode( fNode );
                        view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                        ProcessEventOnChiefUIMSnap peOnChiefUIMSnap = new ProcessEventOnChiefUIMSnap( view );
                        TreePath path = new TreePath( fNode.getPath() );
                        peOnChiefUIMSnap.processTreeSelection( path );
                        peOnChiefUIMSnap.controlMenuAndBtnForTreeEvent();
                        view.getTree().setSelectionPath( path );
                        view.getTree().requestFocus();
                    }
                }
            }
        }else{
            audit.setEventDesc( "Failed to set snap for-ever : " + snap.getSnap_name() );
            view.audit.addAuditRecord( audit );

            view.showError1(
                ResourceCenter.CMD_DEL_SNAP,
                modSnap.getErrCode(),
                modSnap.getErrMsg()
            );
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of set snap for-ever action. " );
    }

    public void setRow( int row ){
        this.row = row;
    }

    public void setTableModel( DefaultTableModel model ){
        this.model = model;
    }

    public boolean isOK(){
        return this.isOk;
    }

    class ModSnapshot extends Thread{
        ProgressDialog pdiag;
        int rootid,snapid,snap_opened_type;
        PPProfile prof;
        String snap_name;
        boolean isOk;
        int errcode;
        String errmsg;

        public ModSnapshot(
            ProgressDialog pdiag,
            int rootid,
            int snapid,
            PPProfile prof,
            String snap_name,
            int snap_opened_type
        ){
            this.pdiag = pdiag;
            this.rootid = rootid;
            this.snapid = snapid;
            this.prof = prof;
            this.snap_name = snap_name;
            this.snap_opened_type = snap_opened_type;
        }

        Runnable close = new Runnable(){
            public void run(){
                pdiag.dispose();
            }
        };

        @Override public void run(){
            view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
//            if( prof == null ){
                /**
                 * 设为永久快照
                 * 相当于设为手动快照，不会自动删除
                 */
                 if( snap_opened_type == 0){
                    isOk = true;
                 }
                 else{
                    isOk = view.initor.mdb.modSnapshot( rootid, snapid, 0);
                 }
//            }else{
//                int clntID = prof.getHostID();
//                BootHost host = view.initor.mdb.getBootHostFromVector( clntID );
//                if( host == null ){
//                    isOk = false;
//                    this.errcode = -1;
//                    this.errmsg = "Can't find host which id is : "+ clntID;
//                }else{
//                    String dg_name = "";
//                    int isGrp = 0;
//                    if( prof.isValidDriveGrp() ){
//                        dg_name = prof.getDriveGrpName();
//                        isGrp = 1;
//                    }else{
//                        dg_name = prof.getMainDiskItem().getVolMap().getVolName();
//                    }
//                    if( snap_name.startsWith("os_delegate") ){
//                        isOk = view.initor.mdb.delSnapshot( rootid,snapid );
//                    }else{
//                        isOk = view.initor.mdb.delSnapshotForCMDP( host.getIP(),host.getPort(),dg_name, snapid, isGrp );
//                    }
//                }
//            }
            view.initor.mdb.restoreOldTimeOut();
            if( !isOk ){
                this.errcode = view.initor.mdb.getErrorCode();
                this.errmsg = view.initor.mdb.getErrorMessage();
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
        public int getErrCode(){
            return errcode;
        }
        public String getErrMsg(){
            return errmsg;
        }
    }
}
