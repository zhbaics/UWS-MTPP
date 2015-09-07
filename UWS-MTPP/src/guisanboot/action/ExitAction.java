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
import javax.swing.Icon;

/**
 *
 * @author zourishun
 */
public class ExitAction extends GeneralActionForMainUi {
    public ExitAction(Icon menuIcon,Icon btnIcon,String text,int fID) {
        super(menuIcon,btnIcon,text,fID);
    }

    public ExitAction(){
        super(
          ResourceCenter.BTN_ICON_EXIT_16,
          ResourceCenter.BTN_ICON_EXIT_50,
          "View.MenuItem.exit",
          MenuAndBtnCenterForMainUi.FUNC_EXIT
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering exit action." );
        Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_LOGOUT );
        audit.setEventDesc( "Logout from system successfully.");
        view.audit.addAuditRecord( audit );

        view.dispose();
        System.exit(0);
SanBootView.log.info(getClass().getName(),"########### End of exit action." );
    }
}
