/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.ui.AuditLogDialog;
import guisanboot.audit.ui.AuditLogGeter;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;

/**
 *
 * @author zourishun
 */
public class AuditAction extends GeneralActionForMainUi{
    public AuditAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.autid",
            MenuAndBtnCenterForMainUi.FUNC_USER_AUDIT
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering audit action. " );
        AuditLogDialog dialog = new AuditLogDialog( view );

        AuditLogGeter geter = new AuditLogGeter(
            view,
            dialog,
            1,
            50
        );
        geter.start();

        int width  = 605;
        int height = 575;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of audit action. " );
    }
}
