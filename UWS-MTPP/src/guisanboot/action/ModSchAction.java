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
import guisanboot.datadup.ui.SchedDialog;
import guisanboot.datadup.ui.ADSchedDialog;
/**
 * 修改调度事件类
 * @author Administrator
 */
public class ModSchAction extends GeneralActionForMainUi{
    public ModSchAction(){
        super(
            ResourceCenter.SMALL_MOD_SCH,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modSch",
            MenuAndBtnCenterForMainUi.FUNC_MOD_SCH
        );
    }

    @Override public void doAction(ActionEvent evt){
        SchedDialog dialog;
        ADSchedDialog addialog;
        BrowserTreeNode fNode;
        Object[] ret;
SanBootView.log.info(getClass().getName(),"########### Entering modify scheduler action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;
        boolean isSch = ( selObj instanceof DBSchedule );
        if( isSch ){
            DBSchedule sch = (DBSchedule)selObj;
            int itype = sch.getSchType();
            if(itype == DBSchedule.SCH_NORMAL_BACKUP){
                fNode = sch.getFatherNode();
                if( fNode == null ){
                    dialog = new SchedDialog( view,sch,null );
                }else{
                    Object usrObj = fNode.getUserObject();
                    if( usrObj instanceof UniProfile ){
                        UniProfile prof = (UniProfile)usrObj;
                        dialog = new SchedDialog( view,sch, prof );
                    }else{
                        dialog = new SchedDialog( view,sch,null );
                    }
                }

                int width  = 585+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );
                dialog.setVisible( true );

                ret = dialog.getValues();
            } else if( itype == DBSchedule.SCH_AUTO_DEL_SNAP){
                String[] strList = sch.getProfName().split(" ");
                Volume objVol = view.initor.mdb.getVolume(Integer.parseInt(strList[1]));
                addialog = new ADSchedDialog( view , sch , null );
                int width  = 585+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                addialog.setSize( width,height );
                addialog.setLocation( view.getCenterPoint( width,height ) );
                addialog.setVisible( true );

                ret = addialog.getValues();
            } else {
                ret = null ;
            }
            if( ret == null ) return;
            Audit audit;
            DBSchedule dbSche = (DBSchedule)ret[0];
            dbSche.setID( sch.getID() );
            int clnt_id = 0;
            if(itype == DBSchedule.SCH_NORMAL_BACKUP){
                String profName = dbSche.getProfName();
                UniProfile prof1 = view.initor.mdb.getOneProfile( profName );
                UniProIdentity identity = prof1.getUniProIdentity();
                String cltid = identity.getClntID();
                BackupClient bk_clnt = view.initor.mdb.getClientFromVectorOnID( cltid );

                if( bk_clnt != null ){
                    BootHost host = view.initor.mdb.getHostFromCacheOnUUID( bk_clnt.getUUID() );
                    clnt_id = host.getID();
                }
            } else {
                String profName[] = dbSche.getProfName().split(" ");
                int irootid = Integer.parseInt(profName[1]);
                Volume objVol = view.initor.mdb.getVolume(irootid);
                if( objVol != null ){
                    String str = objVol.getSnap_name();
                    VolumeMap objVM = view.initor.mdb.getVolMapFromVectorOnName(str);
                    if(objVM != null){
                        clnt_id = objVM.getVolClntID();
                    }
                }
            }
            audit = view.audit.registerAuditRecord( clnt_id, MenuAndBtnCenterForMainUi.FUNC_MOD_SCH );
            boolean ok = view.initor.mdb.addOneScheduler( dbSche );
            if( ok ){
                audit.setEventDesc( "Modify scheduler: " + dbSche.getName() + " successfully.");
                view.audit.addAuditRecord( audit );

                view.initor.mdb.removeSchFromCache( sch );
                view.initor.mdb.addSchIntoCache( dbSche );
            }else{
                audit.setEventDesc( "Failed to modify scheduler: " + dbSche.getName() );
                view.audit.addAuditRecord( audit );

                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_DB_SCHEDULER )+
                    ": "+
                    view.initor.mdb.getErrorMessage()
                );
                return;
            }

            if( ok ){
                fNode = sch.getFatherNode();
                ProcessEvent processEvent = null;
                if( fNode != null ){
                    view.setCurNode( fNode );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    Object userObj = fNode.getUserObject();
                    if( userObj instanceof UniProfile ){ // is a profile tree node
                        processEvent = new ProcessEventOnProfile( view );
                    }else if( userObj instanceof ChiefScheduler ){
                        processEvent = new ProcessEventOnChiefSch( view );
                    } else if( userObj instanceof ChiefADScheduler){
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
SanBootView.log.info(getClass().getName(),"########### End of modify scheduler action. " );
    }
}
