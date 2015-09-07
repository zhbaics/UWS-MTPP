/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.AddUserDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;

/**
 *
 * @author zourishun
 */
public class CrtUserAction extends GeneralActionForMainUi{
    public CrtUserAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtUser",
            MenuAndBtnCenterForMainUi.FUNC_CRT_USER
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering create user action. " );
        AddUserDialog dialog = new AddUserDialog( view,null,-1 );
        int width  = 270+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 265+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of create user action. " );
    }
}
