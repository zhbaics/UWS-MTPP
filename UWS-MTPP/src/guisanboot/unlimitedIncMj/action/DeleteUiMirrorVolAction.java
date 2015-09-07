/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 2009/12/17   AM 11:35
 */

package guisanboot.unlimitedIncMj.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BasicVDisk;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.MirrorJob;
import guisanboot.data.SourceAgent;
import guisanboot.data.Volume;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefHostVolume;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefRemoteFreeVol;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.entity.ChiefLocalUnLimitedIncMirrorVolList;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefUnlimitedIncMirrorVol;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zjj
 */
public class DeleteUiMirrorVolAction extends GeneralActionForMainUi{
    public DeleteUiMirrorVolAction() {
        super(
          ResourceCenter.MENU_ICON_BLANK,
          ResourceCenter.MENU_ICON_BLANK,
          "View.MenuItem.deluimirvol",
          MenuAndBtnCenterForMainUi.FUNC_DEL_UI_MIRROR_VOL
        );
    }

    @Override public void doAction( ActionEvent evt ){
SanBootView.log.info(getClass().getName(),"########### Entering delete ui-mirror-volume action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !( selObj instanceof MirrorDiskInfo ) ){
            return;
        }

        MirrorDiskInfo disk = (MirrorDiskInfo)selObj;
        ArrayList cjList = null;
        if( disk.isLocalMirrorVol() ){ // 3
            // check if there is a mirror job related with this disk to delete
            MirrorJob mj = view.initor.mdb.getIncMjOnDestRootId( disk.getSnap_rootid() );
            if( mj != null ){
                if( mj.isMJStart() ){
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.delUiMirVol")
                    );
                    return;
                }else{
                    int retVal =  JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm21"),
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if( ( retVal == JOptionPane.CANCEL_OPTION ) || ( retVal == JOptionPane.CLOSED_OPTION ) ){
                        return;
                    }
                }
            }
        }else{ // 4 or 6
            int ret = JOptionPane.showConfirmDialog(
                view,
                SanBootView.res.getString("MenuAndBtnCenter.confirm19"),
                SanBootView.res.getString("common.confirm"),  //"Confirm",
                JOptionPane.OK_CANCEL_OPTION
            );
            if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
                return;
            }

            // 检查有无还在传输数据的copy job
            if( !view.initor.mdb.updateMj() ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.updateMj")
                );
                return;
            }

            cjList = this.checkCopyJob( disk );
            if( cjList == null ){
                JOptionPane.showMessageDialog( view, this.errMsg );
                return;
            }
        }

        ProgressDialog initDiag = new ProgressDialog(
            view,
            SanBootView.res.getString("View.pdiagTitle.destroyDisk"),
            SanBootView.res.getString("View.pdiagTip.destroyDisk")
        );
        DelUIMirVol delvol = new DelUIMirVol( initDiag,disk,view,cjList );
        delvol.start();
        initDiag.mySetSize();
        initDiag.setLocation( view.getCenterPoint( initDiag.getDefWidth(),initDiag.getDefHeight() ) );
        initDiag.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of delete ui-mirror-volume action. " );
    }

    class DelUIMirVol extends Thread{
        ProgressDialog pdiag;
        MirrorDiskInfo disk;
        SanBootView view;
        ArrayList cjList;
        String errorStr = "";

        public DelUIMirVol( ProgressDialog pdiag,MirrorDiskInfo disk,SanBootView view,ArrayList cjList ){
            this.pdiag = pdiag;
            this.disk = disk;
            this.view = view;
            this.cjList = cjList;
        }

        Runnable close = new Runnable(){
            public void run(){
                pdiag.dispose();
            }
        };

        @Override public void run(){
            // delete its clone disk
            ArrayList cdList = getAllCloneDisk( disk );
            if( cdList == null ){
                JOptionPane.showMessageDialog( view, this.errorStr );
                return;
            }
            int size = cdList.size();
            for( int i=0; i<size; i++ ){
                CloneDisk cd = (CloneDisk)cdList.get(i);
                DeleteCloneDisk delCd = new DeleteCloneDisk( null,cd,view,true );
                if ( !delCd.realDo() ){
                    return;
                }
            }

            // clear its copy job
            if( cjList != null ){
                size = cjList.size();
                for( int i=0; i<size; i++ ){
                    MirrorJob mj = (MirrorJob)cjList.get(i);
                    if( view.initor.mdb.delMj( mj.getMj_id() ) ){
                        view.initor.mdb.removeMJFromVector( mj );
                    }
                }
            }
            
            boolean isOk = view.initor.mdb.queryVSnapDB(
                "select * from " + ResourceCenter.VSnap_DB +" where "+
                BasicVDisk.BVDisk_Snap_Root_ID + "=" + disk.getSnap_rootid() +";"
            );
            if( !isOk ){
                view.showError1(
                    ResourceCenter.CMD_QUERY_MDB,
                    view.initor.mdb.getErrorCode(),
                    view.initor.mdb.getErrorMessage()
                );
                return;
            }

            Volume volume = view.initor.mdb.getQueryedUIMirVol( disk.getSnap_rootid() );
            if( volume != null ){
                view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
                isOk = view.initor.mdb.destroyDisk( disk.getSnap_rootid() );
                view.initor.mdb.restoreOldTimeOut();
                if( !isOk && ( view.initor.mdb.getErrorCode() == 100 )  ){
                    // disk.getSnap_rootid() 代表的disk本身就不存在
                    isOk = true;
                }
                
                if( !isOk ){
                    view.showError1(
                        ResourceCenter.CMD_DESTORY_DISK,
                        view.initor.mdb.getErrorCode(),
                        view.initor.mdb.getErrorMessage()
                    );
                    return;
                }
            }

            if( isOk ){
                isOk = view.initor.mdb.delMDI( disk.getSnap_rootid() );
                if( isOk ){
                    BrowserTreeNode fNode = disk.getFatherNode();
                    BrowserTreeNode selDiskNode = view.getMDINodeOnChiefUIMirVol( fNode,disk.getSnap_rootid() );
                    if( selDiskNode !=null ){
                        view.removeNodeFromTree( fNode,selDiskNode );
                    }

                    view.initor.mdb.removeMDIFromCache( disk );

                    view.setCurNode( fNode );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    TreePath path = new TreePath( fNode.getPath() );

                    Object fObj = fNode.getUserObject();
                    if( fObj instanceof ChiefLocalUnLimitedIncMirrorVolList ){
                        ProcessEventOnChiefUnlimitedIncMirrorVol peOnChiefUIMirVol = new ProcessEventOnChiefUnlimitedIncMirrorVol( view );
                        peOnChiefUIMirVol.processTreeSelection( path );
                        peOnChiefUIMirVol.controlMenuAndBtnForTreeEvent();
                    }else{
                        ProcessEventOnChiefRemoteFreeVol peOnChiefRemoteFreeVol = new ProcessEventOnChiefRemoteFreeVol( view );
                        peOnChiefRemoteFreeVol.processTreeSelection( path );
                        peOnChiefRemoteFreeVol.controlMenuAndBtnForTreeEvent();
                    }
                    view.getTree().setSelectionPath( path );
                    view.getTree().requestFocus();
                }else{
                    view.showError1(
                        ResourceCenter.CMD_DEL_MIRROR_DISK,
                        view.initor.mdb.getErrorCode(),
                        view.initor.mdb.getErrorMessage()
                    );
                }
            }
            
            try{
                SwingUtilities.invokeAndWait( close );
            }catch( Exception ex ){
                ex.printStackTrace();
            }
        }
        
        private ArrayList getAllCloneDisk( MirrorDiskInfo mdi ){
            int src_disk_root_id = 0;
            int src_host_id  = -1;
            int src_host_type = -1;

            BrowserTreeNode fn = mdi.getFatherNode();
            Object fo = fn.getUserObject();
            if( fo instanceof ChiefHostVolume ){
                Object hostObj = view.getHostObjFromChiefHostVol( fn );
                if( hostObj != null ){
                    if( hostObj instanceof SourceAgent ){
                        src_host_id = ((SourceAgent)hostObj).getSrc_agnt_id();
                        src_host_type =CloneDisk.IS_SRCAGNT;
                        src_disk_root_id = mdi.getSnap_rootid();
                    }else{
SanBootView.log.error(getClass().getName(), "This place need srcagent!!!");
                        errorStr = "host info related with MirrorDiskInfo must be SourceAgent";
                        return null;
                    }
                }else{
SanBootView.log.error(getClass().getName(), "This place need srcagent!!!");
                    errorStr = "host info must exist.";
                    return null;
                }
            }else{
                src_host_type =CloneDisk.IS_REMOTE_FREEVOL;
                src_disk_root_id = mdi.getSnap_rootid();
            }

            boolean ok = view.initor.mdb.getCloneDiskList( src_host_id,src_host_type,src_disk_root_id );
            if( !ok ){
                errorStr = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_CLONE_DISK ) + " : " +
                        view.initor.mdb.getErrorMessage();
                return null;
            }else{
                return view.initor.mdb.getCloneDiskList();
            }
        }
    }
    
    String errMsg="";
    private ArrayList checkCopyJob( MirrorDiskInfo disk ){
        int rootid = disk.getSnap_rootid();

        ArrayList mjList = view.initor.mdb.getCjListFromVecOnSrcRootId( rootid );
        int size = mjList.size();
        for( int i=0; i<size; i++ ){
            MirrorJob mj = (MirrorJob)mjList.get(i);
            if( ( mj.getMj_current_snap_id() != mj.getMj_copy_src_snapid() ) || ( mj.getMj_current_process() != 100 ) ){
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.runningMj") + " : " + mj.getMj_id() ;
                return null;
            }
        }
        
        return mjList;
    }
}
