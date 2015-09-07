/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 2009/12/15   AM 11:15
 */

package guisanboot.unlimitedIncMj.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BasicVDisk;
import guisanboot.data.BootHost;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.SourceAgent;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefHostVolume;
import guisanboot.ui.ChiefRemoteFreeVolume;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.InitProgramDialog;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.entity.ChiefLocalUnLimitedIncMirrorVolList;
import guisanboot.unlimitedIncMj.entity.ChiefUnLimitedIncSnapList;
import guisanboot.unlimitedIncMj.entity.UnlimitedIncMirroredSnap;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import guisanboot.unlimitedIncMj.service.GetCloneDiskProcess;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefCloneDisk;
import guisanboot.unlimitedIncMj.ui.CreateCloneDiskDialog;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zjj
 */
public class CloneDiskAction extends GeneralActionForMainUi{
    BrowserTreeNode fatherNode = null;
    private boolean isOK = false;
    private boolean isCancel = false;

    public CloneDiskAction() {
        super(
          ResourceCenter.MENU_ICON_BLANK,
          ResourceCenter.MENU_ICON_BLANK,
          "View.MenuItem.clonedisk",
          MenuAndBtnCenterForMainUi.FUNC_CLONE_DISK
        );
    }

    @Override public void doAction( ActionEvent evt ){
SanBootView.log.info(getClass().getName(),"########### Entering clone disk action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ){
SanBootView.log.info(getClass().getName(),"sel obj is null. \n########### End of clone disk action. " );
            return;
        }

        if( !( selObj instanceof UnlimitedIncMirroredSnap ) ){
SanBootView.log.info(getClass().getName(),"sel obj is not ui-snap. \n########### End of clone disk action. " );
            return;
        }

        UnlimitedIncMirroredSnap ui_snap = (UnlimitedIncMirroredSnap)selObj;

        CloneDisk newCloneDisk = new CloneDisk();
        newCloneDisk.setSrc_inc_mirvol_snap_local_id( ui_snap.snap.getSnap_local_snapid() );
        newCloneDisk.setCrt_time( ui_snap.snap.getSnap_create_time() );
        BrowserTreeNode fNode = ui_snap.getFatherNode();
        Object obj = fNode.getUserObject();

        if( obj instanceof MirrorDiskInfo ){
            MirrorDiskInfo mdi = (MirrorDiskInfo)obj;
            newCloneDisk.setSrc_inc_mirvol_root_id( mdi.getSnap_rootid() );
            newCloneDisk.setLabel( mdi.getSrc_agent_mp() );
            newCloneDisk.setDefaultDesc();
            BrowserTreeNode fNode1  = mdi.getFatherNode();
            ChiefLocalUnLimitedIncMirrorVolList chiefLUIMVList = (ChiefLocalUnLimitedIncMirrorVolList)fNode1.getUserObject();
            fatherNode = chiefLUIMVList.getFatherNode();
            Object obj1 = fatherNode.getUserObject();
            if( obj1 instanceof VolumeMap ){
                VolumeMap volMap = (VolumeMap)obj1;
                newCloneDisk.setSrc_disk_root_id( volMap.getVol_rootid() );
                BrowserTreeNode fNode3 = volMap.getFatherNode();
                ChiefHostVolume chiefHostVol= (ChiefHostVolume)fNode3.getUserObject();
                BrowserTreeNode hostObjNode = chiefHostVol.getFatherNode();
                Object hostObj = hostObjNode.getUserObject();
                if( hostObj instanceof BootHost ){
                    BootHost bootHost = (BootHost)hostObj;
                    newCloneDisk.setSrc_host_id( bootHost.getID() );
                    newCloneDisk.setSrc_host_type( CloneDisk.IS_BOOTHOST );
                }else{
                    SourceAgent srcAgnt = (SourceAgent)hostObj;
                    newCloneDisk.setSrc_host_id( srcAgnt.getSrc_agnt_id() );
                    newCloneDisk.setSrc_host_type( CloneDisk.IS_SRCAGNT );
                }
             }else if( obj1 instanceof Volume ){
                // 本地空闲卷
                Volume vol = (Volume)obj1;
                newCloneDisk.setSrc_disk_root_id( vol.getSnap_root_id() );
                newCloneDisk.setLabel( vol.getSnap_name() );
                newCloneDisk.setSrc_host_id( -1 );
                newCloneDisk.setSrc_host_type( CloneDisk.IS_FREEVOL );
             }
        }else{
            ChiefUnLimitedIncSnapList chiefUIMSnap = (ChiefUnLimitedIncSnapList)obj;
            BrowserTreeNode mdiNode = chiefUIMSnap.getFatherNode();
            fatherNode = mdiNode;
            MirrorDiskInfo mdi = (MirrorDiskInfo)mdiNode.getUserObject();
            newCloneDisk.setSrc_disk_root_id( mdi.getSnap_rootid() );
            newCloneDisk.setSrc_inc_mirvol_root_id( mdi.getSnap_rootid() );
            //newCloneDisk.setLabel( mdi.getSrc_agent_mp() );
            newCloneDisk.setLabel( mdi.toString() );
            newCloneDisk.setDefaultDesc();
            BrowserTreeNode fNode1  = mdi.getFatherNode();
            Object fObj1 = fNode1.getUserObject();
            if( fObj1 instanceof ChiefHostVolume ){
                ChiefHostVolume chiefHostVol = (ChiefHostVolume)fObj1;
                BrowserTreeNode srcAgtNode = chiefHostVol.getFatherNode();
                SourceAgent srcAgnt = (SourceAgent)srcAgtNode.getUserObject();
                newCloneDisk.setSrc_host_id( srcAgnt.getSrc_agnt_id() );
                newCloneDisk.setSrc_host_type( CloneDisk.IS_SRCAGNT );
            }else{
                ChiefRemoteFreeVolume chiefRemoteFreeVol = (ChiefRemoteFreeVolume)fObj1;
                newCloneDisk.setSrc_host_id(-1);
                newCloneDisk.setSrc_host_type( CloneDisk.IS_REMOTE_FREEVOL );
            }
        }

        this.doAction1( ui_snap,newCloneDisk);
    }

    public void doAction1( UnlimitedIncMirroredSnap ui_snap,CloneDisk newCloneDisk ){
        CreateCloneDiskDialog dialog = new CreateCloneDiskDialog( view,ui_snap );
        int width  = 300+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 145+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object[] ret = dialog.getValues();
        if( ret == null ) {
SanBootView.log.info(getClass().getName(),"########### canceled to clone disk. End of clone disk action. " );
            this.isOK = true;
            this.isCancel = true;
            return;
        }

        String name = (String)ret[0];
        String desc = (String)ret[1];

        InitProgramDialog initDiag = new InitProgramDialog(
            view,
            SanBootView.res.getString("View.pdiagTitle.clonedisk"),
            SanBootView.res.getString("View.pdiagTip.clonedisk")
        );

        GetCloneDiskProcess getCloneDiskProcess = new GetCloneDiskProcess( view,initDiag,ui_snap,name,desc );
        Thread initThread = new Thread( getCloneDiskProcess );
        initThread.start();
        width  = 300+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        height = 120+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        initDiag.setSize( width, height );
        initDiag.setLocation(  view.getCenterPoint(width,height) );
        initDiag.setVisible( true );

        BasicVDisk vdisk = getCloneDiskProcess.getResultVDisk();
        if( vdisk == null ){
SanBootView.log.error(getClass().getName(), "vdisk is null. Clone disk failed.");
SanBootView.log.info(getClass().getName(),"########### End of clone disk action. " );
            return;
        }

        newCloneDisk.setRoot_id( vdisk.getSnap_root_id() );
        newCloneDisk.setTarget_id( vdisk.getSnap_target_id() );
        newCloneDisk.prtMe();

        if( !view.initor.mdb.addCloneInfo( "", 5010, 0, "", newCloneDisk ) ){
            JOptionPane.showMessageDialog( view ,
                ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_CLONE_DISK ) + ":" +
                view.initor.mdb.getErrorMessage()
            );
SanBootView.log.info(getClass().getName(),"########### End of clone disk action. " );
            return;
        }

        this.isOK = true;
        newCloneDisk.setId( view.initor.mdb.getNewId() );

        if( fatherNode == null ) {
SanBootView.log.info(getClass().getName(),"########### End of clone disk action. " );
            return;
        }

        // 显示点击 chiefCloneDiskNode 后的右边tabpane中的内容
        BrowserTreeNode chiefCloneDiskNode = view.getChiefCloneDiskNodeOnHostVolNode( fatherNode );
        if( chiefCloneDiskNode != null ){
            view.setCurNode( chiefCloneDiskNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefCloneDisk peOnChiefCloneDisk= new ProcessEventOnChiefCloneDisk( view );
            TreePath path = new TreePath( chiefCloneDiskNode.getPath() );
            peOnChiefCloneDisk.processTreeSelection( path );
            peOnChiefCloneDisk.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }
SanBootView.log.info(getClass().getName(),"########### End of clone disk action. " );
    }

    public boolean isOK(){
        return this.isOK;
    }
    
    public boolean isCanceled(){
        return this.isCancel;
    }
}
