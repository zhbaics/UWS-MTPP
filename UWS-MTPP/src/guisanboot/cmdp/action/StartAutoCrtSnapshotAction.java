/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefHostVol;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;
import guisanboot.data.MirrorGrp;

/**
 * StartAutoCrtSnapshotAction.java
 *
 * Created on 2010-11-26, 16:01:44
 */
public class StartAutoCrtSnapshotAction extends GeneralActionForMainUi{
    public StartAutoCrtSnapshotAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.startAutoCrtSnap",
            MenuAndBtnCenterForMainUi.FUNC_PHY_START_AUTO_CRT_SNAP
        );
    }

    @Override public void doAction( ActionEvent evt ){
SanBootView.log.info(getClass().getName(),"########### Entering start auto-crt-snap action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( ( selObj == null) || !( selObj instanceof VolumeMap ) ){
SanBootView.log.info(getClass().getName(),"bad type or sel obj is null \n ########### End of start auto-crt-snap action." );
            return;
        }
        
        VolumeMap volMap = (VolumeMap)selObj;
        BrowserTreeNode chiefHostVolNode = volMap.getFatherNode();
        StartAutoCrtSnapThread thread = new StartAutoCrtSnapThread( view,volMap,chiefHostVolNode );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.stAutoCrtSnap"),
            SanBootView.res.getString("View.pdiagTip.stAutoCrtSnap"),
            thread
        );
SanBootView.log.info(getClass().getName(),"########### End of start auto-crt-snap action." );
    }
}

class StartAutoCrtSnapThread extends BasicGetSomethingThread{
    VolumeMap volMap;
    BrowserTreeNode chiefHostVolNode;
    boolean isSyncOfVolMap;
    boolean isMgStart;

    public StartAutoCrtSnapThread( SanBootView view,VolumeMap volMap,BrowserTreeNode chiefHostVolNode ){
        super( view );
        this.volMap = volMap;
        this.chiefHostVolNode = chiefHostVolNode;
    }

    public boolean realRun(){
        this.updateVolMap();
        if( this.isSyncOfVolMap && this.isMgStart ){
            // 第一次获取最新状态的结果：已经启动，重新读mirror group配置
            view.initor.mdb.sendSigToMg(volMap.getVol_mgid(), MirrorGrp.SIG_SIGHUP);
            return true;
        }else{
            if( !this.isSyncOfVolMap ){
                // 设为“同步”
                view.initor.mdb.setWorkStateOfSync( volMap.getVolName(),volMap.getVol_info(),"1" );
            }
            if( !this.isMgStart ){
                view.initor.mdb.startMg( volMap.getVol_mgid() );
            }

            this.updateVolMap();
            if( this.isSyncOfVolMap && this.isMgStart ){
                // 第二次获取最新状态的结果：已经启动，刷新界面
                this.updateUI();
                return true;
            }else{
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.startAutoCrtSnapFail");
                return false;
            }
        }
    }

    private void updateUI(){
        if( this.chiefHostVolNode != null ){
            // 显示点击 chiefHostVolNode 后的右边tabpane中的内容
            view.setCurNode( chiefHostVolNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefHostVol peOnChiefHostVol = new ProcessEventOnChiefHostVol( view );
            TreePath path = new TreePath( chiefHostVolNode.getPath() );
            peOnChiefHostVol.processTreeSelection( path );
            peOnChiefHostVol.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }
    }

    private void updateVolMap(){
        this.isMgStart = false;
        this.isSyncOfVolMap = false;

        if( !view.initor.mdb.updateOneVolumeMap( volMap.getVolName() ) ){
SanBootView.log.error( getClass().getName(), "can not get latest vol info for "+ volMap.getVolName() );
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_VOLMAP ) +" : " +
                      view.initor.mdb.getErrorMessage();
            return;
        }else{
            VolumeMap newVolMap = view.initor.mdb.getOneVolMap( volMap.getVolName() );
            if( newVolMap != null ){
                view.initor.mdb.replaceVolMap( volMap.getVolName(), newVolMap.getVol_info() );
SanBootView.log.debug(getClass().getName(), "volMap's vol_info when replaceVolMap: " + volMap.getVol_info() );
            }else{
SanBootView.log.error( getClass().getName(), "the latest got volMap is null. "+ volMap.getVolName() );
                errMsg =  ResourceCenter.getCmdString( ResourceCenter.CMD_GET_VOLMAP ) +" : " +
                    view.initor.mdb.getErrorMessage();
                return;
            }
        }

        isSyncOfVolMap = !volMap.isWorkStateASync();
        isMgStart = false;
        if( volMap.getVol_mgid() > 0 ){
            isMgStart = view.initor.mdb.checkMg( volMap.getVol_mgid() );
            if( !isMgStart ){
                errMsg =  ResourceCenter.getCmdString( ResourceCenter.CMD_CHECK_MG_STATUS ) +" : " +
                    view.initor.mdb.getErrorMessage();
            }
        }
    }
}