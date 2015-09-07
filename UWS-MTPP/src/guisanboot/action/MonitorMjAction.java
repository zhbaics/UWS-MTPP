/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.MirrorJob;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.MjStatusGeter;
import guisanboot.ui.MonitorMJDialog;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;

/**
 *
 * @author zourishun
 */
public class MonitorMjAction extends GeneralActionForMainUi{
    public MonitorMjAction(){
        super(
            ResourceCenter.monitorIcon,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.monMj",
            MenuAndBtnCenterForMainUi.FUNC_MONITOR_MJ
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering monitor mirror job action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) {
SanBootView.log.info(getClass().getName(),"selobj is null!\n########### End of monitor mirror job action. " );
            return;
        }

        if( !( selObj instanceof MirrorJob ) ){
SanBootView.log.info(getClass().getName(),"bad type !\n ########### End of monitor mirror job action. " );
            return;
        }
        MirrorJob mj = (MirrorJob)selObj;

        MonitorMJDialog diag = new MonitorMJDialog( view,mj );
        MjStatusGeter geter = new MjStatusGeter(
            diag,
            mj,
            view
        );
        geter.start();

        int width  = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 275+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        diag.setSize( width, height );
        diag.setLocation( view.getCenterPoint( width,height ) );
        diag.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of monitor mirror job action. " );
    }
}
