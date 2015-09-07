/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;

/**
 *
 * @author zourishun
 */
public class QuickStartMjAction extends StartMjAction{
    public QuickStartMjAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.qstartMj",
            MenuAndBtnCenterForMainUi.FUNC_QUICK_START_MJ,
            true
        );
    }
}
