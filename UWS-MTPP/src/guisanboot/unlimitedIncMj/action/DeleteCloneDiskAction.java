/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 2009.12.21 PM 1:04
 */

package guisanboot.unlimitedIncMj.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author zjj
 */
public class DeleteCloneDiskAction extends GeneralActionForMainUi{
    public DeleteCloneDiskAction() {
        super(
          ResourceCenter.MENU_ICON_BLANK,
          ResourceCenter.MENU_ICON_BLANK,
          "View.MenuItem.delCloneDisk",
          MenuAndBtnCenterForMainUi.FUNC_DEL_CLONE_DISK
        );
    }

    @Override public void doAction( ActionEvent evt ){
SanBootView.log.info(getClass().getName(),"########### Entering destory clone disk action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !( selObj instanceof CloneDisk ) ){
            return;
        }

        CloneDisk disk = (CloneDisk)selObj;

        int retVal =  JOptionPane.showConfirmDialog(
            view,
            SanBootView.res.getString("MenuAndBtnCenter.confirm22"),
            SanBootView.res.getString("common.confirm"),  //"Confirm",
            JOptionPane.OK_CANCEL_OPTION
        );
        if( ( retVal == JOptionPane.CANCEL_OPTION ) || ( retVal == JOptionPane.CLOSED_OPTION ) ){
            return;
        }

        ProgressDialog pdiag = new ProgressDialog(
            view,
            SanBootView.res.getString("View.pdiagTitle.delCloneDisk"),
            SanBootView.res.getString("View.pdiagTip.delCloneDisk")
        );
        DeleteCloneDisk delvol = new DeleteCloneDisk( pdiag,disk,view,false );
        delvol.start();
        pdiag.mySetSize();
        pdiag.setLocation( view.getCenterPoint( pdiag.getDefWidth(),pdiag.getDefHeight() ) );
        pdiag.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of destory clonedisk action." );
    }
}
