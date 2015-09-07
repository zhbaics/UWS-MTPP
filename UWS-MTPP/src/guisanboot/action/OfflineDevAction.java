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
public class OfflineDevAction extends GeneralActionForMainUi{
    public OfflineDevAction(){
        super(
            ResourceCenter.BTN_ICON_OFFLINE,
            ResourceCenter.BTN_ICON_OFFLINE,
            "View.MenuItem.offlineDev",
            MenuAndBtnCenterForMainUi.FUNC_OFFLINE
        );
    }

    @Override public void doAction(ActionEvent evt){

    }
}
