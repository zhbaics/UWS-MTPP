/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;

import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;

import guisanboot.ui.AboutDialog;
/**
 *
 * @author zourishun
 */
public class AboutAction extends GeneralActionForMainUi {
    public AboutAction() {
        super(
          ResourceCenter.BTN_ICON_INFO_16,
          ResourceCenter.BTN_ICON_ABOUT_20,
          "View.MenuItem.about",
          MenuAndBtnCenterForMainUi.FUNC_ABOUT
        );
    }

    @Override public void doAction( ActionEvent evt ){
SanBootView.log.info(getClass().getName(),"########### Entering about-help action. " );
        JDialog dialog = new AboutDialog(
            view,
            SanBootView.res.getString("common.product") +" Manager GUI",
            ResourceCenter.BTN_ICON_LOGO
        );
        int width  = 380+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 190+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setTitle( SanBootView.res.getString("AboutDialog.dialog_title"));
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of about-help action. " );
    }
}
