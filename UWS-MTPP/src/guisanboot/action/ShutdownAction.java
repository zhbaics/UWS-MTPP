/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author zourishun
 */
public class ShutdownAction extends GeneralActionForMainUi {
    public ShutdownAction() {
        super(
          ResourceCenter.MENU_ICON_BLANK,
          ResourceCenter.MENU_ICON_BLANK,
          "RightCustomDialog.checkbox.shutdown",
          MenuAndBtnCenterForMainUi.FUNC_SHUTDOWN
        );
    }

    @Override public void doAction( ActionEvent evt ){
        int ret = JOptionPane.showConfirmDialog(
            view,
            SanBootView.res.getString("MenuAndBtnCenter.confirm19"),
            SanBootView.res.getString("common.confirm"),  //"Confirm",
            JOptionPane.OK_CANCEL_OPTION
        );
        if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
            return;
        }

        Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_SHUTDOWN );
        audit.setEventDesc( "Shutdown RapidDR server successfully.");
        view.audit.addAuditRecord( audit );

        view.initor.mdb.powerdownUws();
        JOptionPane.showMessageDialog(view,
            SanBootView.res.getString("MenuAndBtnCenter.error.shutdown")
        );
    }
}
