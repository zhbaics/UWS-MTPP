/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import guisanboot.datadup.data.DBSchedule;
import guisanboot.audit.data.Audit;
import guisanboot.datadup.data.UniProfile;
import guisanboot.datadup.data.UniProIdentity;
import guisanboot.datadup.data.BackupClient;
import guisanboot.data.BootHost;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnChiefSch;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnChiefProfile;
import guisanboot.datadup.ui.SchedDialog;
/**
 *
 * @author zourishun
 */
public class AddSchAction extends GeneralActionForMainUi{
    public AddSchAction(){
        super(
            ResourceCenter.SMALL_ADD_SCH,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.addSch",
            MenuAndBtnCenterForMainUi.FUNC_ADD_SCH
        );
    }

    @Override public void doAction(ActionEvent evt){
        SchedDialog dialog;
        UniProfile _prof=null,prof = null;

SanBootView.log.info(getClass().getName(),"########### Entering add scheduler action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ){
            dialog = new SchedDialog( view,null, null );
        }else{
            if( selObj instanceof UniProfile ){
                _prof = (UniProfile)selObj;

                 // get real profile from cache. Contents of profile on GUI maybe older.
                prof = view.initor.mdb.getOneProfile( _prof.getProfileName() );
SanBootView.log.info( getClass().getName(),"profile to add scheduler: " + _prof.getProfileName() );
                if( prof == null ) return;

//System.out.println(" prof : \n "+ prof.prtMe() );
                if( !view.initor.mdb.checkProfile( prof ) ){
                    JOptionPane.showMessageDialog( view,
                        view.initor.mdb.getProfErrMsg()
                    );
                    return;
                }
                dialog = new SchedDialog( view,null,prof );
            }else{
                dialog = new SchedDialog( view,null,null );
            }
        }

        int width  = 585+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object[] ret = dialog.getValues();
        if( ret == null ) return;

        DBSchedule dbSche = (DBSchedule)ret[0];

        String profName = dbSche.getProfName();
        UniProfile prof1 = view.initor.mdb.getOneProfile( profName );
        UniProIdentity identity = prof1.getUniProIdentity();
        String cltid = identity.getClntID();
        BackupClient bk_clnt = view.initor.mdb.getClientFromVectorOnID( cltid );
        int clnt_id = 0;
        if( bk_clnt != null ){
            BootHost host = view.initor.mdb.getHostFromCacheOnUUID( bk_clnt.getUUID() );
            clnt_id = host.getID();
        }

        Audit audit = view.audit.registerAuditRecord( clnt_id, MenuAndBtnCenterForMainUi.FUNC_ADD_SCH );

        boolean ok = view.initor.mdb.addOneScheduler( dbSche );
        if( ok ){
            dbSche.setID( view.initor.mdb.getNewId() );
            view.initor.mdb.addSchIntoCache( dbSche );

            // must be after setting newid to dbsche
            audit.setEventDesc( "Add scheduler: " + dbSche.getName() + " successfully.");
            view.audit.addAuditRecord( audit );
        }else{
            audit.setEventDesc( "Failed to add scheduler: " + dbSche.getName() );
            view.audit.addAuditRecord( audit );

            JOptionPane.showMessageDialog(view,
                ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_DB_SCHEDULER )+
                ": "+
                view.initor.mdb.getErrorMessage()
            );
            return;
        }

        if( ok ){
            if( _prof != null ){
                BrowserTreeNode chiefProfNode = _prof.getFatherNode();
                if( chiefProfNode != null ){
                    view.setCurNode( chiefProfNode );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    ProcessEventOnChiefProfile peOnChiefProf = new ProcessEventOnChiefProfile( view );
                    TreePath path = new TreePath( chiefProfNode.getPath() );
                    peOnChiefProf.processTreeSelection( path );
                    peOnChiefProf.controlMenuAndBtnForTreeEvent();
                    view.getTree().setSelectionPath( path );
                    view.getTree().requestFocus();
                }
            }else{
                BrowserTreeNode chiefSchNode = view.getChiefNodeOnRoot( ResourceCenter.TYPE_CHIEF_SCH );
                if( chiefSchNode != null ){
                    view.setCurNode( chiefSchNode );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    ProcessEventOnChiefSch peOnChiefSch = new ProcessEventOnChiefSch( view );
                    TreePath path = new TreePath( chiefSchNode.getPath() );
                    peOnChiefSch.processTreeSelection( path );
                    peOnChiefSch.controlMenuAndBtnForTreeEvent();
                    view.getTree().setSelectionPath( path );
                    view.getTree().requestFocus();
                }
            }
        }
SanBootView.log.info(getClass().getName(),"########### End of add scheduler action. " );
    }
}
