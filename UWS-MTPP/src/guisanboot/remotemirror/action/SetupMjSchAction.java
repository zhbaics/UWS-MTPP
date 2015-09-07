/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.remotemirror.ui.SetupMjSchDialog;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefMJManage;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefMjMg;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 * SetupMjSchAction.java
 *
 * Created on 2010-9-9, 11:19:20
 */
public class SetupMjSchAction extends GeneralActionForMainUi{
    public SetupMjSchAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.setupMjSch",
            MenuAndBtnCenterForMainUi.FUNC_SETUP_MJ_SCH
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering setup mirror-job-scheduler action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;
        if( !(selObj instanceof ChiefMJManage) ) return;

        SetupMjSchDialog dialog = new SetupMjSchDialog( view );
        int width  = 583+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 450+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object ret  = dialog.getRetVal();
        if( ret == null ) {
SanBootView.log.info(getClass().getName(),"########### End1 of setup mirror-job-scheduler action. " );
            return;
        }
        
        if( ((Integer)ret).intValue() == 1 ){
            ChiefMJManage chiefMjManage =(ChiefMJManage)selObj;
            BrowserTreeNode chiefMjManageNode = chiefMjManage.getTreeNode();
            view.setCurNode( chiefMjManageNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            TreePath path = new TreePath( chiefMjManageNode.getPath() );
            ProcessEventOnChiefMjMg peOnChiefMjMg = new ProcessEventOnChiefMjMg( view );
            peOnChiefMjMg.processTreeSelection( path );
            peOnChiefMjMg.controlMenuAndBtnForTreeEvent();
        }
        
SanBootView.log.info(getClass().getName(),"########### End of setup-mirror-job-scheduler action." );
    }
}
