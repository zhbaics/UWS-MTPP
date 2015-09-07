/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 2009/12/17   AM 11:35
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
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.entity.ChiefLocalUnLimitedIncMirrorVolList;
import guisanboot.unlimitedIncMj.entity.ChiefUnLimitedIncSnapList;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import guisanboot.unlimitedIncMj.service.GetUISnapGeter;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefUnlimitedIncMirrorVol;
import guisanboot.unlimitedIncMj.ui.QueryUnlimitedIncSnapDialog;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zjj
 */
public class QueryUISnapAction extends GeneralActionForMainUi{
    public QueryUISnapAction() {
        super(
          ResourceCenter.MENU_ICON_BLANK,
          ResourceCenter.MENU_ICON_BLANK,
          "View.MenuItem.queryUISnap",
          MenuAndBtnCenterForMainUi.FUNC_QUEYR_UISNAP
        );
    }

    @Override public void doAction( ActionEvent evt ){
        int type = ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP;

SanBootView.log.info(getClass().getName(),"########### Enter into query unlimited incremental snap  action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ){
SanBootView.log.info(getClass().getName(),"sel obj is null!\n########### End of query unlimited incremental snap action. " );
            return;
        }
        boolean isMDI = (selObj instanceof MirrorDiskInfo );
        boolean isChiefUIMSnap = (selObj instanceof ChiefUnLimitedIncSnapList );

        if( !isMDI && !isChiefUIMSnap ){
SanBootView.log.info(getClass().getName(),"sel obj is bad type!\n########### End of query unlimited incremental snap action. " );
            return;
        }

        MirrorDiskInfo mdi;
        if( isMDI ){
            mdi = (MirrorDiskInfo)selObj;
            type = ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP1;
        }else{
            ChiefUnLimitedIncSnapList chiefUIMSnap = (ChiefUnLimitedIncSnapList)selObj;
            BrowserTreeNode fNode = chiefUIMSnap.getFatherNode();
            mdi = (MirrorDiskInfo)fNode.getUserObject();
            if( mdi.isRemoteCjVol() ){
                type = ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP1;
            }
        }

        // 为clone操作做准备
        CloneDisk newCloneDisk = new CloneDisk();
        newCloneDisk.setSrc_inc_mirvol_root_id( mdi.getSnap_rootid() );
        newCloneDisk.setLabel( mdi.toString() );
        newCloneDisk.setDefaultDesc();

        boolean isCmdp = false;
        if( isMDI ){
            BrowserTreeNode fNode1  = mdi.getFatherNode();
            ChiefLocalUnLimitedIncMirrorVolList chiefLUIMVList = (ChiefLocalUnLimitedIncMirrorVolList)fNode1.getUserObject();
            BrowserTreeNode fatherNode = chiefLUIMVList.getFatherNode();
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
                    isCmdp = mdi.isCMDPProtect() && mdi.getSrc_agent_mp().substring(0,1).toUpperCase().equals( "C" );
                    newCloneDisk.setSrc_host_id( bootHost.getID() );
                    newCloneDisk.setSrc_host_type( CloneDisk.IS_BOOTHOST );
                }else{
                    SourceAgent srcAgnt = (SourceAgent)hostObj;
                    isCmdp = mdi.isCMDPProtect() && mdi.getSrc_agent_mp().substring(0,1).toUpperCase().equals( "C" );
                    newCloneDisk.setSrc_host_id( srcAgnt.getSrc_agnt_id() );
                    newCloneDisk.setSrc_host_type( CloneDisk.IS_SRCAGNT );
                }
            }else{
                Volume vol = (Volume)obj1;
                newCloneDisk.setSrc_disk_root_id( vol.getSnap_root_id() );
                newCloneDisk.setSrc_host_type( CloneDisk.IS_FREEVOL );
                newCloneDisk.setSrc_host_id( -1 );
            }
        }else{ // isChiefUIMSnap
            newCloneDisk.setSrc_disk_root_id( mdi.getSnap_rootid() );
            BrowserTreeNode fNode1  = mdi.getFatherNode();
            Object fObj1 = fNode1.getUserObject();
            if( fObj1 instanceof ChiefHostVolume ){
                ChiefHostVolume chiefHostVol = (ChiefHostVolume)fObj1;
                BrowserTreeNode hostObjNode = chiefHostVol.getFatherNode();
                Object hostObj = hostObjNode.getUserObject();
                SourceAgent srcAgnt = (SourceAgent)hostObj;
                isCmdp = mdi.isCMDPProtect() && mdi.getSrc_agent_mp().substring(0,1).toUpperCase().equals( "C" );
                newCloneDisk.setSrc_host_id( srcAgnt.getSrc_agnt_id() );
                newCloneDisk.setSrc_host_type( CloneDisk.IS_SRCAGNT );
            }else{ // ChiefRemoteFreeVolume
                newCloneDisk.setLabel( mdi.toTreeString() );
                newCloneDisk.setSrc_host_id( -1 );
                newCloneDisk.setSrc_host_type( CloneDisk.IS_REMOTE_FREEVOL );
            }
        }

        boolean isOk = view.initor.mdb.getUISnapSum( "",0,0,"",mdi.getSnap_rootid() );
        if( !isOk ){
            view.showError1(
                ResourceCenter.CMD_GET_UI_SNAP_SUM,
                view.initor.mdb.getErrorCode(),
                view.initor.mdb.getErrorMessage()
            );
SanBootView.log.info(getClass().getName(),"########### End of query unlimited incremental snap action. " );
            return;
        }
        int ui_snap_sum = view.initor.mdb.getNewId();
        if( ui_snap_sum <=0 ){
            JOptionPane.showMessageDialog( view, SanBootView.res.getString("MenuAndBtnCenter.error.getUISnapSum"));
SanBootView.log.info(getClass().getName(),"########### End of query unlimited incremental snap action. " );
            return;
        }
SanBootView.log.info( getClass().getName()," UI-Snap sum is " + ui_snap_sum );

        int begin = 0;
        int end = QueryUnlimitedIncSnapDialog.PAGE_SIZE-1;
        QueryUnlimitedIncSnapDialog dialog = new QueryUnlimitedIncSnapDialog( view,mdi,ui_snap_sum,begin,end,newCloneDisk,type,isCmdp );

        GetUISnapGeter geter = new GetUISnapGeter(
            view,
            dialog,
            mdi.getSnap_rootid(),
            begin,
            end,
            type,
            isCmdp
        );
        geter.start();

        int width  = 645+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 455+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of query unlimited incremental snap action. " );
    }

    class DelUIMirVol extends Thread{
        ProgressDialog pdiag;
        MirrorDiskInfo disk;
        SanBootView view;

        public DelUIMirVol( ProgressDialog pdiag,MirrorDiskInfo disk,SanBootView view ){
            this.pdiag = pdiag;
            this.disk = disk;
            this.view = view;
        }

        Runnable close = new Runnable(){
            public void run(){
                pdiag.dispose();
            }
        };

        @Override public void run(){
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
                    ProcessEventOnChiefUnlimitedIncMirrorVol peOnChiefUIMirVol = new ProcessEventOnChiefUnlimitedIncMirrorVol( view );
                    peOnChiefUIMirVol.processTreeSelection( path );
                    peOnChiefUIMirVol.controlMenuAndBtnForTreeEvent();
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
    }
}
