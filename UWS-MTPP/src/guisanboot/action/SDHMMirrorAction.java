/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import guisanboot.ui.sdhm.SDHMMirrorReportDialog;
import java.awt.event.ActionEvent;

/**
 *
 * @author zourishun
 */
public class SDHMMirrorAction extends GeneralActionForMainUi{
    public SDHMMirrorAction(){
        super(
            ResourceCenter.BTN_ICON_RPT_16,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.sdhmMirror",
            MenuAndBtnCenterForMainUi.FUNC_SDHM_MIRROR
        );
    }

    @Override public void doAction(ActionEvent evt){
        SanBootView.log.info(getClass().getName(),"########### Entering SDHM Mirror action");
        SDHMMirrorReportDialog dialog = new SDHMMirrorReportDialog( view );

//        TaskLogGeter geter = new TaskLogGeter(
//            view,
//            dialog,
//            1,
//            50
//        );
//        geter.start();

        int width  = 450+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 530+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
        SanBootView.log.info(getClass().getName(),"########### End of SDHM Mirror action. " );

	}
}
