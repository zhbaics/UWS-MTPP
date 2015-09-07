/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.ui;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.data.BootHost;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.MirrorGrp;
import guisanboot.data.Snapshot;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.unlimitedIncMj.entity.ChiefUnLimitedIncSnapList;
import guisanboot.unlimitedIncMj.entity.UnlimitedIncMirroredSnap;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefUIMSnap;
import guisanboot.unlimitedIncMj.service.ProcessEventOnUnlimitedIncMirrorVol;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 * DelSnapAction.java
 *
 * Created on 2010-1-5, 14:37:53
 */
public class DelSnapAction extends GeneralActionForMainUi{
    private boolean isOk = true;
    private DefaultTableModel model = null;
    private int row;

    public DelSnapAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delSnap",
            MenuAndBtnCenterForMainUi.FUNC_DEL_SNAP
        );
    }

    @Override public void doAction(ActionEvent evt ){
SanBootView.log.info(getClass().getName(),"########### Entering delete snap action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) {
SanBootView.log.info(getClass().getName(),"sel obj is null. \n########### End of delete snap action. " );
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
SanBootView.log.info(getClass().getName(),"bad type. \n########### End of delete snap action. " );
            return;
        }

        int ret = JOptionPane.showConfirmDialog(
            view,
            SanBootView.res.getString("MenuAndBtnCenter.confirm2"),
            SanBootView.res.getString("common.confirm"),  //"Confirm",
            JOptionPane.OK_CANCEL_OPTION
        );

        if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
SanBootView.log.info(getClass().getName(),"cancel this action. \n########### End of delete snap action. " );
            return;
        }

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

        /**
         * 为永久快照，删除需要密码验证
         */
        if( snap_opened_type == 0){
            SnapPassword dialog = new SnapPassword( view, true);
            int width = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 150+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );

            Object[] retsult = dialog.getValues();
            if( retsult == null || retsult.length <= 0 ){
                SanBootView.log.info(getClass().getName(),"cancel this action. \n########### End of delete snap action. " );
                return;
            }

            /**
             *             记录当前登录的UWS信息
             *             view.initor.serverIp = (String)ret[0];
             *             view.initor.user     = (String)ret[1];
             *             view.initor.passwd   = (String)ret[2];
             *             view.initor.port     = ((Integer)ret[3]).intValue();
             */

            //  与 iboot server 相 连
            //boolean ok = view.initor.realLogin( view.initor.serverIp, view.initor.port, view.initor.user, view.initor.passwd, 0 );
            boolean ok = view.initor.rightCheck( view.initor.serverIp, view.initor.port, (String)retsult[0], (String)retsult[1], 0 );
            if( !ok ){
                //JOptionPane.showMessageDialog( view, view.initor.getInitErrMsg() );
                view.showError1(  ResourceCenter.CMD_DEL_SNAP, 255,  view.initor.errormsg );
                SanBootView.log.info(getClass().getName(),"check user&&password fail. \n########### End of delete snap action. " );
                return;
            }

        }

        ProgressDialog delSnapDiag = new ProgressDialog(
            view,
            SanBootView.res.getString("View.pdiagTitle.delSnap"),
            SanBootView.res.getString("View.pdiagTip.delSnap")
        );
        DelSnapshot delSnap = new DelSnapshot( delSnapDiag,root_id, local_snap_id,prof,snap_name );
        delSnap.start();
        delSnapDiag.mySetSize();
        delSnapDiag.setLocation( view.getCenterPoint( delSnapDiag.getDefWidth(),delSnapDiag.getDefHeight() ) );
        delSnapDiag.setVisible( true );

        Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_DEL_SNAP );

        this.isOk = delSnap.isOK();
        if( delSnap.isOK() ){
            audit.setEventDesc( "Delete snapshot: " + snap.getSnap_name() + " successfully." );
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
            audit.setEventDesc( "Failed to delete snapshot: " + snap.getSnap_name() );
            view.audit.addAuditRecord( audit );

            view.showError1(
                ResourceCenter.CMD_DEL_SNAP,
                delSnap.getErrCode(),
                delSnap.getErrMsg()
            );
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of delete snap action. " );
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

    class DelSnapshot extends Thread{
        ProgressDialog pdiag;
        int rootid,snapid;
        PPProfile prof;
        String snap_name;
        boolean isOk;
        int errcode;
        String errmsg;

        public DelSnapshot(
            ProgressDialog pdiag,
            int rootid,
            int snapid,
            PPProfile prof,
            String snap_name
        ){
            this.pdiag = pdiag;
            this.rootid = rootid;
            this.snapid = snapid;
            this.prof = prof;
            this.snap_name = snap_name;
        }

        Runnable close = new Runnable(){
            public void run(){
                pdiag.dispose();
            }
        };

        @Override public void run(){
            view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
            if( prof == null ){
                isOk = view.initor.mdb.delSnapshot( rootid,snapid );
            }else{
                int clntID = prof.getHostID();
                BootHost host = view.initor.mdb.getBootHostFromVector( clntID );
                if( host == null ){
                    isOk = false;
                    this.errcode = -1;
                    this.errmsg = "Can't find host which id is : "+ clntID;
                }else{
                    String dg_name = "";
                    int isGrp = 0;
                    if( prof.isValidDriveGrp() ){
                        dg_name = prof.getDriveGrpName();
                        isGrp = 1;
                    }else{
                        dg_name = prof.getMainDiskItem().getVolMap().getVolName();
                    }
                    if( snap_name.startsWith("os_delegate") ){
                        isOk = view.initor.mdb.delSnapshot( rootid,snapid );
                    }else{
                        isOk = view.initor.mdb.delSnapshotForCMDP( host.getIP(),host.getPort(),dg_name, snapid, isGrp );
                    }
                }
            }
            view.initor.mdb.restoreOldTimeOut();
            if( !isOk ){
                this.errcode = view.initor.mdb.getErrorCode();
                this.errmsg = view.initor.mdb.getErrorMessage();
            }else{
                view.initor.mdb.sendSigToMgOnRootID( rootid, MirrorGrp.SIG_SIGRTMIN );
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
