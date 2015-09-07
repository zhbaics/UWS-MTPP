/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.OptionDialog;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;

/**
 *
 * @author zourishun
 */
public class LoginSetup extends GeneralActionForMainUi {
    public LoginSetup() {
        super(
          ResourceCenter.SMALL_ADMIN_OPTION,
          ResourceCenter.SMALL_ADMIN_OPTION,
          "View.MenuItem.adminOpt",
          MenuAndBtnCenterForMainUi.FUNC_ADMINOPT
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering login setup action. " );
        OptionDialog dialog = new OptionDialog( view );
        int width  = 430+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 265+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of login setup action. " );
    }
}
