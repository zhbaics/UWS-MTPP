/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.MirrorJob;
import guisanboot.data.StartorStopMjThread;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefMirrorJobList;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefMj;
import guisanboot.ui.ProcessEventOnChiefMjMg;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.entity.ChiefCopyJobList;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefCj;
import guisanboot.unlimitedIncMj.service.StartorStopCjThread;
import guisanboot.unlimitedIncMj.service.StartorStopUnlimitedIncMjThread;
import java.awt.event.ActionEvent;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class StopMjAction extends GeneralActionForMainUi{
    public StopMjAction(){
        super(
            ResourceCenter.stopIcon,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.stopMj",
            MenuAndBtnCenterForMainUi.FUNC_STOP_MJ
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering stop mirror job action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) {
SanBootView.log.info(getClass().getName(),"selobj is null!\n ########### Entering stop mirror job action. " );
            return;
        }

        if( !( selObj instanceof MirrorJob ) ){
SanBootView.log.info(getClass().getName(),"bad type ! \n ########### Entering stop mirror job action. " );
            return;
        }

        MirrorJob mj = (MirrorJob)selObj;
        boolean isOk;
        if( mj.isNormalMjType() ){
            StartorStopMjThread thread = new StartorStopMjThread( view, mj, 1,null, 0, "" ,"","",false,BootHost.PROTECT_TYPE_MTPP );
            view.startupProcessDiag(
                SanBootView.res.getString("View.pdiagTitle.stopMj")+" [ "+mj.getMj_job_name() +" ] ",
                SanBootView.res.getString("View.pdiagTip.stopMj"),
                thread
            );
            isOk = thread.isOk();
        }else if( mj.isIncMjType() ){
            StartorStopUnlimitedIncMjThread thread1 = new StartorStopUnlimitedIncMjThread( view, mj, 1,null, 0, "" ,"","",false,BootHost.PROTECT_TYPE_MTPP );
            view.startupProcessDiag(
                SanBootView.res.getString("View.pdiagTitle.stopMj")+" [ "+mj.getMj_job_name() +" ] ",
                SanBootView.res.getString("View.pdiagTip.stopMj"),
                thread1
            );
            isOk = thread1.isOk();
        }else{
            StartorStopCjThread thread2 = new StartorStopCjThread( view,mj,1,null,0,"","","",false,BootHost.PROTECT_TYPE_MTPP );
            view.startupProcessDiag(
                SanBootView.res.getString("View.pdiagTitle.stopCj")+" [ "+mj.getMj_job_name() +" ] ",
                SanBootView.res.getString("View.pdiagTip.stopCj"),
                thread2
            );
            isOk = thread2.isOk();
        }

        if( isOk ){
            mj.setMj_job_status( MirrorJob.MJ_STATUS_STOP );

            BrowserTreeNode mjFNode = mj.getFatherNode();
            Object fMjObj = mjFNode.getUserObject();
            view.setCurNode( mjFNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            TreePath path = new TreePath( mjFNode.getPath() );
            if( fMjObj instanceof ChiefMirrorJobList ){
                ProcessEventOnChiefMj peOnChiefMj = new ProcessEventOnChiefMj( view );
                peOnChiefMj.processTreeSelection( path );
                peOnChiefMj.controlMenuAndBtnForTreeEvent();
            }else if( fMjObj instanceof ChiefCopyJobList ){
                ProcessEventOnChiefCj peOnChiefCj = new ProcessEventOnChiefCj( view );
                peOnChiefCj.processTreeSelection( path );
                peOnChiefCj.controlMenuAndBtnForTreeEvent();
            }else{
                ProcessEventOnChiefMjMg peOnChiefMjMg = new ProcessEventOnChiefMjMg( view );
                peOnChiefMjMg.processTreeSelection( path );
                peOnChiefMjMg.controlMenuAndBtnForTreeEvent();
            }
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }
SanBootView.log.info(getClass().getName(),"########### End of stop mirror job action. " );
    }
}
