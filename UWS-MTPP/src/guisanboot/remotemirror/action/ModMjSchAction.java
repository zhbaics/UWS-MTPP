/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.MirrorJob;
import guisanboot.remotemirror.entity.MirrorJobSch;
import guisanboot.remotemirror.service.ProcessEventOnChiefMjScheduler;
import guisanboot.remotemirror.ui.EditMjSchDialog;
import guisanboot.remotemirror.ui.StartOrStopMjInBatchDialog;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 * ModMjSchAction.java
 *
 * Created on 2010-9-8, 10:29:20
 */
public class ModMjSchAction extends GeneralActionForMainUi{
    public ModMjSchAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modMjSch",
            MenuAndBtnCenterForMainUi.FUNC_MOD_MJ_SCH
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering mod mirror job scheduler action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;
        if( !(selObj instanceof MirrorJobSch ) ) return;

        MirrorJobSch mjSch =(MirrorJobSch)selObj;
        EditMjSchDialog dialog = new EditMjSchDialog( view,mjSch );
        int width  = 583+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 620+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
        
        Object ret  = dialog.getRetVal();
        if( ret == null ) return;
        
        MirrorJobSch newMjSch = (MirrorJobSch)ret;
        newMjSch.setScheduler_id( mjSch.getScheduler_id() );
SanBootView.log.info( getClass().getName(), "modified mjsch: " + newMjSch.prtMe() );

        boolean isOk = view.initor.mdb.modMjSch( newMjSch );
        if( isOk ){
            mjSch.setScheduler_mon( newMjSch.getScheduler_mon() );
            mjSch.setScheduler_tue( newMjSch.getScheduler_tue() );
            mjSch.setScheduler_wed( newMjSch.getScheduler_wed() );
            mjSch.setScheduler_thu( newMjSch.getScheduler_thu() );
            mjSch.setScheduler_fri( newMjSch.getScheduler_fri() );
            mjSch.setScheduler_sat( newMjSch.getScheduler_sat() );
            mjSch.setScheduler_sun( newMjSch.getScheduler_sun() );

            BrowserTreeNode chiefMjSchNode = view.getChiefNodeOnRoot( ResourceCenter.TYPE_CHIEF_MJ_SCHEDULER );
            if( chiefMjSchNode != null ){
                view.setCurNode( chiefMjSchNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                TreePath path = new TreePath( chiefMjSchNode.getPath() );
                ProcessEventOnChiefMjScheduler peOnChiefMjSch = new ProcessEventOnChiefMjScheduler( view );
                peOnChiefMjSch.processTreeSelection( path );
                peOnChiefMjSch.controlMenuAndBtnForTreeEvent();
            }

            // 将受影响的、并且已经启动的mj重新启/停一下
            ArrayList<MirrorJob> mjList = view.initor.mdb.getMjRelatedWithMjSch( mjSch.getScheduler_id(),true );
            if( mjList.size() > 0 ){
                StartOrStopMjInBatchDialog do_start_stop = new StartOrStopMjInBatchDialog( view );
                do_start_stop.setMjList( mjList );

                do_start_stop.doWithGraph( StartOrStopMjInBatchDialog.what_stop );
                String stop_errmsg = do_start_stop.getErrorMsg();
                if( stop_errmsg.equals("") ){ // stop successfully,then start
                    do_start_stop.doWithGraph( StartOrStopMjInBatchDialog.what_qstart );
                    String start_errmsg = do_start_stop.getErrorMsg();
                    if( !start_errmsg.equals("") ){
                        JOptionPane.showMessageDialog(
                            view,SanBootView.res.getString("EditMjSchDialog.error.stopMj")
                        );
                    }
                }else{
                    JOptionPane.showMessageDialog(
                        view,SanBootView.res.getString("EditMjSchDialog.error.stopMj")
                    );
                }
            }
        }else{
            JOptionPane.showMessageDialog(
                view,ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_MJ_SCH ) +" : " + view.initor.mdb.getErrorMessage()
            );
        }
SanBootView.log.info(getClass().getName(),"########### End of mod mirror job scheduler action." );
    }
}
