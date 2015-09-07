/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import java.awt.event.ActionEvent;

/**
 *
 * @author zourishun
 */
public class DelUserAction extends GeneralActionForMainUi{
    public DelUserAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delUser",
            MenuAndBtnCenterForMainUi.FUNC_DEL_USER
        );
    }

    @Override public void doAction(ActionEvent evt){
    }
}
