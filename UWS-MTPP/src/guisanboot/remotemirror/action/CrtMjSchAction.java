/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.remotemirror.ChiefMJScheduler;
import guisanboot.remotemirror.entity.MirrorJobSch;
import guisanboot.remotemirror.service.ProcessEventOnChiefMjScheduler;
import guisanboot.remotemirror.ui.EditMjSchDialog;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 * CrtMjSchAction.java
 *
 * Created on 2010-9-8, 10:29:20
 */
public class CrtMjSchAction extends GeneralActionForMainUi{
    public CrtMjSchAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtMjSch",
            MenuAndBtnCenterForMainUi.FUNC_CRT_MJ_SCH
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering crt mirror job scheduler action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;
        if( !(selObj instanceof ChiefMJScheduler) ) return;

        EditMjSchDialog dialog = new EditMjSchDialog( view,null );
        int width  = 583+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 602+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object ret  = dialog.getRetVal();
        if( ret == null ) return;

        MirrorJobSch mjSch = (MirrorJobSch)ret;
SanBootView.log.info(getClass().getName(), "mjSch to create: " + mjSch.prtMe() );
 
        boolean isOk = view.initor.mdb.addMjSch( mjSch );
        if( isOk ){
            mjSch.setScheduler_id( view.initor.mdb.getNewId() );
            view.initor.mdb.addMjSchIntoCache( mjSch );

            ChiefMJScheduler chiefMjSch =(ChiefMJScheduler)selObj;
            BrowserTreeNode chiefMjSchNode = chiefMjSch.getTreeNode();
            view.setCurNode( chiefMjSchNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            TreePath path = new TreePath( chiefMjSchNode.getPath() );
            ProcessEventOnChiefMjScheduler peOnChiefMjSch = new ProcessEventOnChiefMjScheduler( view );
            peOnChiefMjSch.processTreeSelection( path );
            peOnChiefMjSch.controlMenuAndBtnForTreeEvent();
        }else{
            JOptionPane.showMessageDialog(
                view,ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_MJ_SCH ) +" : " + view.initor.mdb.getErrorMessage()
            );
        }

SanBootView.log.info(getClass().getName(),"########### End of crt mirror job scheduler action." );
    }
}
