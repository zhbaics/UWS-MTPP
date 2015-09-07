/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.remotemirror.ui.CreateMjInBatchDialog;
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
 * CreateMjInBatchAction.java
 *
 * Created on 2010-8-27, 16:12
 */
public class CreateMjInBatchAction extends GeneralActionForMainUi{
    public CreateMjInBatchAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtMjInBatch",
            MenuAndBtnCenterForMainUi.FUNC_BATCH_CREATE_MJ
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering create mirror job in batch action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;
        if( !(selObj instanceof ChiefMJManage) ) return;

        CreateMjInBatchDialog dialog = new CreateMjInBatchDialog( view );
        int width  = 1000+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 540+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object retVal = dialog.getRetVal();
        if( retVal == null ) {
SanBootView.log.info(getClass().getName(),"########### End1 of creating mirror job in batch action. " );
            return;
        }

        if( ((Integer)retVal).intValue() == 1 ){
            ChiefMJManage chiefMjManage =(ChiefMJManage)selObj;
            BrowserTreeNode chiefMjManageNode = chiefMjManage.getTreeNode();
            view.setCurNode( chiefMjManageNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            TreePath path = new TreePath( chiefMjManageNode.getPath() );
            ProcessEventOnChiefMjMg peOnChiefMjMg = new ProcessEventOnChiefMjMg( view );
            peOnChiefMjMg.processTreeSelection( path );
            peOnChiefMjMg.controlMenuAndBtnForTreeEvent();
        }
SanBootView.log.info(getClass().getName(),"########### End of creating mirror job in batch action. " );
    }
}
