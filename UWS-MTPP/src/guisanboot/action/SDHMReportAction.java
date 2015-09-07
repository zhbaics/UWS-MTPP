/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import guisanboot.ui.sdhm.SDHMReportDialog;
import java.awt.event.ActionEvent;

/**
 *
 * @author zourishun
 */
public class SDHMReportAction extends GeneralActionForMainUi{
    public SDHMReportAction(){
        super(
            ResourceCenter.SMALL_ADD_HOST,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.sdhmInfo",
            MenuAndBtnCenterForMainUi.FUNC_SDHM_INFO
        );
    }

    @Override public void doAction(ActionEvent evt){
        SanBootView.log.info(getClass().getName(),"########### Entering SDHM Report action");
        SDHMReportDialog dialog = new SDHMReportDialog( view );

//        TaskLogGeter geter = new TaskLogGeter(
//            view,
//            dialog,
//            1,
//            50
//        );
//        geter.start();

        int width  = 340+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 280+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
        SanBootView.log.info(getClass().getName(),"########### End of SDHM Report action. " );
    }
}
