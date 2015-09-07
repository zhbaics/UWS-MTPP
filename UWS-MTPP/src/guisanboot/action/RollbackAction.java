/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.SourceAgent;
import guisanboot.remotemirror.RollbackThread;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author zourishun
 */
public class RollbackAction extends GeneralActionForMainUi {
    public RollbackAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.rollback",
            MenuAndBtnCenterForMainUi.FUNC_ROLLBACK
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering rollback win host action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !(selObj instanceof SourceAgent)  ) return;
        SourceAgent sa =(SourceAgent )selObj;

        int ret = JOptionPane.showConfirmDialog(
            view,
            SanBootView.res.getString("MenuAndBtnCenter.confirm19"),
            SanBootView.res.getString("common.confirm"),  //"Confirm",
            JOptionPane.OK_CANCEL_OPTION
        );
        if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
            return;
        }

        RollbackThread thread = new RollbackThread( view,sa );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.rollback"),
            SanBootView.res.getString("View.pdiagTip.rollback"),
            thread
        );
SanBootView.log.info(getClass().getName(),"########### End of rollback win host action. " );
    }
}
