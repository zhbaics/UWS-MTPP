/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.ui.AuditLogDialog;
import guisanboot.mjob.ui.MjobLogGeter;
import guisanboot.mjob.ui.MjobLogDialog;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;

/**
 *
 * @author zourishun
 */
public class MjobAction extends GeneralActionForMainUi{
    public MjobAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.mjob",
            MenuAndBtnCenterForMainUi.FUNC_USER_MJOB
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering mjob action. " );
        MjobLogDialog dialog = new MjobLogDialog( view );

        MjobLogGeter geter = new MjobLogGeter(
            view,
            dialog,
            1,
            50
        );
        geter.start();
        
        int width  = 750;
        int height = 450;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of mjob action. " );
    }
}
