/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;
import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import guisanboot.datadup.ui.MonitorDialog;
import guisanboot.datadup.ui.TaskListGeter;
/**
 *
 * @author zourishun
 */
public class MonitorAction extends GeneralActionForMainUi{
    public MonitorAction(){
        super(
            ResourceCenter.BTN_ICON_MON_16,
            ResourceCenter.BTN_ICON_MON_50,
            "View.MenuItem.mon",
            MenuAndBtnCenterForMainUi.FUNC_MONITOR_DD
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering task monitor action. " );
        MonitorDialog dialog = new MonitorDialog( view );
        TaskListGeter geter = new TaskListGeter(
            dialog,
            view
        );
        geter.start();

        int width  = 765+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 385+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of task monitor action. " );
    }
}