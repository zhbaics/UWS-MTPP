/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.LogicalVol;
import guisanboot.data.Snapshot;
import guisanboot.data.View;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefHostVolume;
import guisanboot.ui.ChiefOrphanVolume;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.OnlineOrOfflineDeviceThread;
import guisanboot.ui.ProcessEventOnChiefHostVol;
import guisanboot.ui.ProcessEventOnChiefOrphanVol;
import guisanboot.ui.ProcessEventOnSnapshot;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class OnlineDevAction extends GeneralActionForMainUi{
    public OnlineDevAction(){
        super(
            ResourceCenter.BTN_ICON_ONLINE,
            ResourceCenter.BTN_ICON_ONLINE,
            "View.MenuItem.onlineDev",
            MenuAndBtnCenterForMainUi.FUNC_ONLINE
        );
    }

    @Override public void doAction(ActionEvent evt){
        int rootid,local_snap_id=-1;
        BrowserTreeNode fNode;
SanBootView.log.info(getClass().getName(),"########### Entering online device action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( selObj instanceof Volume ){
SanBootView.log.info( getClass().getName()," (OnlineDevAction)doing online for volume" );
            Volume vol = (Volume)selObj;
            fNode = vol.getFatherNode();
            rootid = vol.getSnap_root_id();
            local_snap_id = vol.getSnap_local_snapid();
        }else if( selObj instanceof LogicalVol ){
SanBootView.log.info( getClass().getName()," (OnlineDevAction)doing online for lv");
            LogicalVol lv = (LogicalVol)selObj;
            fNode = lv.getFatherNode();
            VolumeMap tgt = view.initor.mdb.getTargetOnLV( lv );
            if( tgt == null ){
SanBootView.log.error( this.getClass().getName(),"Can't find volmap for lv: "+ lv.getLVName() );
                return;
            }
            rootid = tgt.getVol_rootid();
        }else if( selObj instanceof View  ){
SanBootView.log.info( getClass().getName()," (OnlineDevAction)doing online for view");
            View v =(View)selObj;
            fNode = v.getFatherNode();
            rootid = v.getSnap_root_id();
            local_snap_id = v.getSnap_local_snapid();
        }else if( selObj instanceof VolumeMap ){
SanBootView.log.info( getClass().getName()," (OnlineDevAction)doing online for volmapMap");
            VolumeMap volMap = (VolumeMap)selObj;
            fNode = volMap.getFatherNode();
            rootid = volMap.getVol_rootid();
        }else{
SanBootView.log.info( getClass().getName()," (OnlineDevAction)doing online for other obj,exit.");
            return;
        }

        OnlineOrOfflineDeviceThread online = new OnlineOrOfflineDeviceThread( view, rootid,local_snap_id, OnlineOrOfflineDeviceThread.ACT_ONLINE );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.onlineDev"),
            SanBootView.res.getString("View.pdiagTip.onlineDev"),
            online
        );

        if( online.isOk() ){
            if( fNode != null ){
                view.setCurNode( fNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                TreePath path = new TreePath( fNode.getPath() );

                Object fObj = fNode.getUserObject();
                if( fObj instanceof ChiefHostVolume ){
                    ProcessEventOnChiefHostVol peOnChiefHVol = new ProcessEventOnChiefHostVol( view );
                    peOnChiefHVol.processTreeSelection( path );
                    peOnChiefHVol.controlMenuAndBtnForTreeEvent();
                }else if( fObj instanceof ChiefOrphanVolume ){
                    ProcessEventOnChiefOrphanVol peOnChiefFreeVol = new ProcessEventOnChiefOrphanVol( view );
                    peOnChiefFreeVol.processTreeSelection( path );
                    peOnChiefFreeVol.controlMenuAndBtnForTreeEvent();
                }else if( fObj instanceof Snapshot ) {
                    ProcessEventOnSnapshot peOnSnap = new ProcessEventOnSnapshot( view );
                    peOnSnap.processTreeSelection( path );
                    peOnSnap.controlMenuAndBtnForTreeEvent();
                }

                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }
        }else{
            if( !online.isOk() ){
                view.showError1(
                    online.getErrCmd(),
                    online.getErrCode(),
                    online.getErrMsg()
                );
            }
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of online device action. " );
    }
}
