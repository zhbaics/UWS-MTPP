/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.cluster.entity.Cluster;
import guisanboot.data.BootHost;
import guisanboot.datadup.data.BakObject;
import guisanboot.datadup.data.DBSchedule;
import guisanboot.datadup.data.UniProfile;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnChiefProfile;
import guisanboot.datadup.ui.viewobj.ChiefProfile;
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
 *
 * @author zourishun
 */
public class DelProfAction extends GeneralActionForMainUi{
    public DelProfAction(){
        super(
            ResourceCenter.ICON_DEL_PROFILE,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delProf",
            MenuAndBtnCenterForMainUi.FUNC_DEL_PROF
        );
    }

    @Override public void doAction(ActionEvent evt){
        BootHost host = null;
        Cluster cluster=null;

SanBootView.log.info(getClass().getName(),"########### Entering delete profile action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isProf = ( selObj instanceof UniProfile );
        if( isProf ){
            UniProfile _prof = (UniProfile)selObj;
            BrowserTreeNode chiefProfNode = _prof.getFatherNode();
            ChiefProfile chiefProf = (ChiefProfile)chiefProfNode.getUserObject();
            BrowserTreeNode hostNode = chiefProf.getFatherNode();
            Object obj = hostNode.getUserObject();
            if( obj instanceof BootHost ){
                host =(BootHost)obj;
            }else{
                cluster = (Cluster)obj;
            }
            UniProfile prof = view.initor.mdb.getOneProfile( _prof.getProfileName() );
SanBootView.log.info( getClass().getName(),"profile to delete: " + _prof.getProfileName() );
            if( prof == null ) return;

            int ret = JOptionPane.showConfirmDialog(
                view,
                SanBootView.res.getString("MenuAndBtnCenter.confirm15"),
                SanBootView.res.getString("common.confirm"),  //"Confirm",
                JOptionPane.OK_CANCEL_OPTION
            );
            if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
                return;
            }

            String bkobjId = prof.getUniProIdentity().getBkObj_ID();
SanBootView.log.info( getClass().getName(),"profile to delete: " + prof.getProfileName() );
            BakObject bkObj = view.initor.mdb.getBakObjFromVector( bkobjId );
            if( bkObj != null ){
                if( !view.initor.mdb.deleteBakObj( bkObj.getBakObjID() ) ){
                    JOptionPane.showMessageDialog(view,
                        ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_BAKOBJECT )+
                        ": "+
                        view.initor.mdb.getErrorMessage()
                    );
                    return;
                }else{
                    view.initor.mdb.removeBakObjFromVector( bkObj );
                }
            }

            // 不要删除d2d client,因为要与FIVStorEX共享这个client.

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

            ArrayList schList = view.initor.mdb.getSchOnProfName( prof.getProfileName() );
            int size = schList.size();
            for( int i=0; i<size; i++ ){
                DBSchedule sch = (DBSchedule)schList.get(i);
                if( !view.initor.mdb.deleteOneScheduler( sch ) ){
                    JOptionPane.showMessageDialog(view,
                        ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_DB_SCHEDULER ) +
                        ": "+
                        view.initor.mdb.getErrorMessage()
                    );
                    return;
                }else{
                    view.initor.mdb.removeSch( sch );
                }
            }

            Audit audit = view.audit.registerAuditRecord( host.getID(), MenuAndBtnCenterForMainUi.FUNC_DEL_PROF );

            if( !view.initor.mdb.delFile( prof.getProfileName()  ) ){
                audit.setEventDesc( "Failed to delete profile: "+ prof.getProfileNameWithoutExtName() );
                view.audit.addAuditRecord( audit );

                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_FILE ) +
                    ": "+
                    view.initor.mdb.getErrorMessage()
                );
            }else{
                audit.setEventDesc( "Delete profile: "+ prof.getProfileNameWithoutExtName() + " successfully." );
                view.audit.addAuditRecord( audit );

                view.initor.mdb.removeProfFromCache( prof );

                // refresh chiefProfile
                if( chiefProfNode != null ){
                    BrowserTreeNode selProfNode = view.getProfNodeOnChiefProf( chiefProfNode,prof.toString() );
                    if( selProfNode!= null ){
                        view.removeNodeFromTree( chiefProfNode, selProfNode );
                    }
                    view.setCurNode( chiefProfNode );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    ProcessEventOnChiefProfile peOnChiefProf = new ProcessEventOnChiefProfile( view );
                    TreePath path = new TreePath( chiefProfNode.getPath() );
                    peOnChiefProf.processTreeSelection( path );
                    peOnChiefProf.controlMenuAndBtnForTreeEvent();
                    view.getTree().setSelectionPath( path );
                    view.getTree().requestFocus();
                }
            }
        }
SanBootView.log.info(getClass().getName(),"########### End of delete profile action. " );
    }
}
