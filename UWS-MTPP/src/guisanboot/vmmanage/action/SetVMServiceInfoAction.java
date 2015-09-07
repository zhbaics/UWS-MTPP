/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import guisanboot.vmmanage.ui.SetVMServiceInfoDialog;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class SetVMServiceInfoAction extends GeneralActionForMainUi {

    public SetVMServiceInfoAction() {
        super(
                ResourceCenter.MENU_ICON_BLANK,
                ResourceCenter.MENU_ICON_BLANK,
                "View.MenuItem.setVMServiceInfo",
                MenuAndBtnCenterForMainUi.FUNC_SET_VMSERVICE_INFO);
    }

    @Override
    public void doAction(ActionEvent evt) {
        SanBootView.log.info(getClass().getName(), "*********Start of SetVMServiceInfoAction   **********");
        Object[] ret;
        SetVMServiceInfoDialog dialog = new SetVMServiceInfoDialog(view);
        int width = 400 + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 150 + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize(width, height);
        dialog.setLocation(view.getCenterPoint(width, height));
        dialog.setVisible(true);

        ret = dialog.getValues();
        if (ret == null) {
            return;
        }
        String ip = ret[0].toString();
        String port = ret[1].toString();
        boolean isOk = view.initor.mdb.setVMServiceInfo(ip, port);
        if (isOk) {
            JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("SetVMServiceInfoAction.success.successful"));
        } else {
            JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("SetVMServiceInfoAction.error.failed"));
        }
        SanBootView.log.info(getClass().getName(), "*********End of SetVMServiceInfoAction   **********");
    }
}
