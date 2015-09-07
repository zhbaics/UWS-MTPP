/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.remotemirror.entity.MirrorJobSch;
import guisanboot.remotemirror.service.ProcessEventOnChiefMjScheduler;
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
public class DelMjSchAction extends GeneralActionForMainUi{
    public DelMjSchAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delMjSch",
            MenuAndBtnCenterForMainUi.FUNC_DEL_MJ_SCH
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering del mirror job scheduler action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;
        if( !(selObj instanceof MirrorJobSch ) ) return;

        MirrorJobSch mjSch =(MirrorJobSch)selObj;
SanBootView.log.info(getClass().getName(), "mjSch to del: " + mjSch.prtMe() );

        int retVal =  JOptionPane.showConfirmDialog(
            view,
            SanBootView.res.getString("MenuAndBtnCenter.confirm25"),
            SanBootView.res.getString("common.confirm"),  //"Confirm",
            JOptionPane.OK_CANCEL_OPTION
        );
        if( ( retVal == JOptionPane.CANCEL_OPTION ) || ( retVal == JOptionPane.CLOSED_OPTION ) ){
            return;
        }

        // check whether some mirror jobs are still related with this mirror Job scheduler
        if( view.initor.mdb.isRelatedWithMj( mjSch.getScheduler_id() ) ){
            JOptionPane.showMessageDialog(view, SanBootView.res.getString("EditMjSchDialog.error.isUsed") );
            return;
        }

        boolean isOk = view.initor.mdb.delMjSch( mjSch.getScheduler_id()  );
        if( isOk ){
            this.view.initor.mdb.removeMjSchFromCache( mjSch );

            BrowserTreeNode chiefMjSchNode = view.getChiefNodeOnRoot( ResourceCenter.TYPE_CHIEF_MJ_SCHEDULER );
            if( chiefMjSchNode != null ){
                view.setCurNode( chiefMjSchNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                TreePath path = new TreePath( chiefMjSchNode.getPath() );
                ProcessEventOnChiefMjScheduler peOnChiefMjSch = new ProcessEventOnChiefMjScheduler( view );
                peOnChiefMjSch.processTreeSelection( path );
                peOnChiefMjSch.controlMenuAndBtnForTreeEvent();
            }
        }else{
            JOptionPane.showMessageDialog(
                view,ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_MJ_SCH ) +" : " + view.initor.mdb.getErrorMessage()
            );
        }
SanBootView.log.info(getClass().getName(),"########### End of del mirror job scheduler action." );
    }
}
