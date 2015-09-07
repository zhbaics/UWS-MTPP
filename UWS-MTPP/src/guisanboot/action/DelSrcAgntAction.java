/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.SourceAgent;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author zourishun
 */
public class DelSrcAgntAction extends GeneralActionForMainUi{
    public DelSrcAgntAction(){
        super(
            ResourceCenter.SMALL_DEL_HOST,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delHost",
            MenuAndBtnCenterForMainUi.FUNC_DEL_SRCAGNT
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering delete sourceagent action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !(selObj instanceof SourceAgent) ) return;
        SourceAgent selHost = (SourceAgent)selObj;

        int ret = JOptionPane.showConfirmDialog(
            view,
            SanBootView.res.getString("MenuAndBtnCenter.confirm3"),
            SanBootView.res.getString("common.confirm"),  //"Confirm",
            JOptionPane.OK_CANCEL_OPTION
        );
        if(  ( ret == JOptionPane.CANCEL_OPTION ) || (  ret == JOptionPane.CLOSED_OPTION) ){
            return;
        }

        guisanboot.remotemirror.DelSrcAgntThread thread = new guisanboot.remotemirror.DelSrcAgntThread( view,selHost,false );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.delHost"),
            SanBootView.res.getString("View.pdiagTip.delHost"),
            thread
        );
SanBootView.log.info(getClass().getName(),"########### End of delete SourceAgent action. " );
    }
}
