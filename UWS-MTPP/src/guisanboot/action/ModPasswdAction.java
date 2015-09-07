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
public class ModPasswdAction extends GeneralActionForMainUi{
    public ModPasswdAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modPasswd",
            MenuAndBtnCenterForMainUi.FUNC_MOD_PASSWD
        );
    }

    @Override public void doAction(ActionEvent evt){
    }
}
