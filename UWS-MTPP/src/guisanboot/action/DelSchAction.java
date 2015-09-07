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
import mylib.UI.ProcessEvent;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import guisanboot.datadup.data.DBSchedule;
import guisanboot.audit.data.Audit;
import guisanboot.datadup.data.UniProfile;
import guisanboot.datadup.data.UniProIdentity;
import guisanboot.datadup.data.BackupClient;
import guisanboot.data.BootHost;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.datadup.ui.viewobj.ChiefADScheduler;
import guisanboot.datadup.ui.viewobj.ChiefScheduler;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnProfile;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnChiefADSch;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnChiefSch;
/**
 * 删除调度事件类
 * @author Administrator
 */
public class DelSchAction extends GeneralActionForMainUi{
    public DelSchAction(){
        super(
            ResourceCenter.SMALL_DEL_SCH,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delSch",
            MenuAndBtnCenterForMainUi.FUNC_DEL_SCH
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering delete scheduler action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;
        boolean isSch = ( selObj instanceof DBSchedule );
        if( isSch ){
            DBSchedule sch = (DBSchedule)selObj;

            int ret = JOptionPane.showConfirmDialog(
                view,
                SanBootView.res.getString("MenuAndBtnCenter.confirm14"),
                SanBootView.res.getString("common.confirm"),  //"Confirm",
                JOptionPane.OK_CANCEL_OPTION
            );
            if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
                return;
            }

            String profName = sch.getProfName();
            int clnt_id = 0;
            Audit audit;
            int itype = sch.getSchType();
            if( itype == DBSchedule.SCH_NORMAL_BACKUP){
                UniProfile prof1 = view.initor.mdb.getOneProfile( profName );
                UniProIdentity identity = prof1.getUniProIdentity();
                String cltid = identity.getClntID();
                BackupClient bk_clnt = view.initor.mdb.getClientFromVectorOnID( cltid );

                if( bk_clnt != null ){
                    BootHost host = view.initor.mdb.getHostFromCacheOnUUID( bk_clnt.getUUID() );
                    if( host != null ){
                        clnt_id = host.getID();
                    }
                }
            } else {
                String strName[] = profName.split(" ");
                int irootid = Integer.parseInt(strName[1]);
                Volume objVol = view.initor.mdb.getVolume(irootid);
                if( objVol != null ){
                    String str = objVol.getSnap_name();
                    VolumeMap objVM = view.initor.mdb.getVolMapFromVectorOnName(str);
                    if(objVM != null){
                        clnt_id = objVM.getVolClntID();
                    }
                }
            }
            audit = view.audit.registerAuditRecord( clnt_id, MenuAndBtnCenterForMainUi.FUNC_DEL_SCH );

            boolean ok = view.initor.mdb.deleteOneScheduler( sch );
            if( ok ){
                audit.setEventDesc( "Delete scheduler: " + sch.getName() + " successfully." );
                view.audit.addAuditRecord( audit );

                view.initor.mdb.removeSchFromCache( sch );
            }else{
                audit.setEventDesc( "Failed to delete scheduler: " + sch.getName() );
                view.audit.addAuditRecord( audit );

                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_DB_SCHEDULER )+
                    ": "+
                    view.initor.mdb.getErrorMessage()
                );
                return;
            }

            if( ok ){
                BrowserTreeNode fNode = sch.getFatherNode();
                ProcessEvent processEvent = null;
                if( fNode != null ){
                    view.setCurNode( fNode );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    Object userObj = fNode.getUserObject();
                    if( userObj instanceof UniProfile ){ // is a profile tree node
                        processEvent = new ProcessEventOnProfile( view );
                    }else if( userObj instanceof ChiefScheduler ){
                        processEvent = new ProcessEventOnChiefSch( view );
                    }else if ( userObj instanceof ChiefADScheduler){
                        processEvent = new ProcessEventOnChiefADSch( view );
                    }
                }else{
                    fNode = view.getChiefNodeOnRoot( ResourceCenter.TYPE_CHIEF_SCH );
                    view.setCurNode( fNode );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    processEvent = new ProcessEventOnChiefSch( view );
                }
                TreePath path = new TreePath( fNode.getPath() );
                processEvent.processTreeSelection( path );
                processEvent.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }
        }
SanBootView.log.info(getClass().getName(),"########### End of delete scheduler action. " );
    }
}
