/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.cluster.entity.Cluster;
import guisanboot.data.BootHost;
import guisanboot.datadup.data.DBSchedule;
import guisanboot.datadup.data.UniProfile;
import guisanboot.datadup.ui.InputNameDialog;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnChiefProfile;
import guisanboot.datadup.ui.viewobj.ChiefProfile;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class RenameProfAction extends GeneralActionForMainUi{
    UniProfile prof;
    String oldName;

    public RenameProfAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.renameProf",
            MenuAndBtnCenterForMainUi.FUNC_RENAME_PROF
        );
    }

    @Override public void doAction(ActionEvent evt){
        BootHost host = null;
        Cluster cluster = null;

SanBootView.log.info(getClass().getName(),"########### Entering rename profile action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isProf = ( selObj instanceof UniProfile );
        if( isProf ){
            UniProfile aprof = (UniProfile)selObj;
            BrowserTreeNode chiefProfNode = aprof.getFatherNode();
            ChiefProfile chiefProf = (ChiefProfile)chiefProfNode.getUserObject();
            BrowserTreeNode hostNode = chiefProf.getFatherNode();
            Object obj = hostNode.getUserObject();
            if( obj instanceof BootHost ){
                host = (BootHost)obj;
            }else{
                cluster = (Cluster)obj;
            }

            prof = view.initor.mdb.getOneProfile( aprof.getProfileName() );
SanBootView.log.info( getClass().getName(),"profile to rename: " + aprof.getProfileName() );
            if( prof == null ) return;

            if( cluster != null ){
                host = view.initor.mdb.getHostFromVectOnD2DClntID1( prof.getUniProIdentity().getClntID() );
                if( host == null ){
SanBootView.log.error( getClass().getName()," not find boot host according to d2d_clnt_id:"+ prof.getUniProIdentity().getClntID() );
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("common.errcode.inconsistentProf")
                    );
                    return;
                }
            }

            oldName = prof.getProfileName();
            ArrayList schList = view.initor.mdb.getSchOnProfName( oldName );

            InputNameDialog dialog = new InputNameDialog( view,prof.toString() );
            int width = 275+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 165+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );

            Object[] ret = dialog.getValues();
            if( ret == null || ret.length <=0 ) return;

            String newName = ResourceCenter.PROFILE_DIR + (String)ret[0];

            File tmpFile = view.initor.mdb.createTmpFile( ResourceCenter.PREFIX,ResourceCenter.SUFFIX_PROF );
            if( tmpFile == null ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("common.errmsg.crtTmpFileLocallyFailed")
                );
                return;
            }

            prof.setProfileName( newName );
            prof.setHeaderProfileName();
            prof.setIdentityProfileName();

            // 发送profile的内容
            if( !view.initor.mdb.sendFileToServer( tmpFile.getName(),prof.prtMe() ) ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("common.errmsg.sendFileFailed")+" : "+
                    view.initor.mdb.getErrorMessage()
                );
                // 失败, 回退
                undo_rename_profile();

                tmpFile.delete();
                return;
            }
            tmpFile.delete();

            Audit audit = view.audit.registerAuditRecord( host.getID(), MenuAndBtnCenterForMainUi.FUNC_RENAME_PROF );

            boolean ok = view.initor.mdb.moveFile(
                ResourceCenter.TMP_DIR + tmpFile.getName(),
                newName
            );
            if( ok ){
                audit.setEventDesc( "Rename profile from " + oldName + " to " + newName + " successfully." );
                view.audit.addAuditRecord( audit );

                // del older profile
                view.initor.mdb.delFile( oldName );

                // modify scheduler related with older profile
                int size = schList.size();
                for( int i=0; i<size; i++ ){
                    DBSchedule sch = (DBSchedule)schList.get(i);
                    sch.setProfName( newName );
                    // 不管结果
                    view.initor.mdb.addOneScheduler( sch );
                }

                // refresh chiefProfile
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
                audit.setEventDesc( "Failed to rename profile from " + oldName + " to " + newName );
                view.audit.addAuditRecord( audit );

                // 失败, 回退
                undo_rename_profile();

                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_RENAME_PROFILE )+
                    ": "+
                    view.initor.mdb.getErrorMessage()
                );
                return;
            }
        }
SanBootView.log.info(getClass().getName(),"########### Entering rename profile action. " );
    }

    private void undo_rename_profile(){
        prof.setProfileName( oldName );
        prof.setHeaderProfileName();
        prof.setIdentityProfileName();
    }
}
