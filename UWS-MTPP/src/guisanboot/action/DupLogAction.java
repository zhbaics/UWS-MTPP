/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import java.awt.event.ActionEvent;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import guisanboot.datadup.ui.BackupHistoryDialog;
import guisanboot.datadup.ui.TaskLogGeter;
/**
 *
 * @author zourishun
 */
public class DupLogAction extends GeneralActionForMainUi{
    public DupLogAction(){
        super(
            ResourceCenter.ICON_DUP_LOG_16,
            ResourceCenter.ICON_DUP_LOG_50,
            "View.MenuItem.duplog",
            MenuAndBtnCenterForMainUi.FUNC_DUP_LOG
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering dup-log browser action");
        BackupHistoryDialog dialog = new BackupHistoryDialog( view );

        TaskLogGeter geter = new TaskLogGeter(
            view,
            dialog,
            1,
            50
        );
        geter.start();

        int width  = 725+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 550+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of dup-log browser action. " );
    }
}
