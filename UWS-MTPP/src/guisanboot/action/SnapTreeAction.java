/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import java.awt.event.ActionEvent;

import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.res.ResourceCenter;

import guisanboot.ui.snaptree.SnapTreeDialog;
/**
 *
 * @author zourishun
 */
public class SnapTreeAction extends GeneralActionForMainUi{
    public SnapTreeAction(){
        super(
            ResourceCenter.BTN_ICON_MON_16,
            ResourceCenter.BTN_ICON_MON_50,
            "View.MenuItem.mon",
            MenuAndBtnCenterForMainUi.FUNC_SNAP_TREE
        );
    }

    @Override public void doAction(ActionEvent evt){
        SnapTreeDialog dialog = new SnapTreeDialog( view );
        int width  = 600+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 480+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setTitle( "Snapshot Tree");
        dialog.setVisible( true );
    }
}