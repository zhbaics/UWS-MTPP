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
import guisanboot.datadup.ui.ADSchedDialog;
import guisanboot.datadup.data.DBSchedule;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.audit.data.Audit;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnChiefADSch;
/**
 * 增加预删除快照调度事件类
 * @author Administrator
 */
public class AddADSchAction extends GeneralActionForMainUi{
    public AddADSchAction(){
        super(
            ResourceCenter.SMALL_ADD_SCH,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.addADSch",
            MenuAndBtnCenterForMainUi.FUNC_ADD_ADSCH
        );
    }

    @Override public void doAction(ActionEvent evt){
        ADSchedDialog dialog;

SanBootView.log.info(getClass().getName(),"########### Entering add scheduler action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        dialog = new ADSchedDialog( view,null, null );


        int width  = 585+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object[] ret = dialog.getValues();
        if( ret == null ) return;

        DBSchedule dbSche = (DBSchedule)ret[0];

        String profName[] = dbSche.getProfName().split(" ");
        int irootid = Integer.parseInt(profName[1]);
        Volume objVol = view.initor.mdb.getVolume(irootid);
        int iclnt_id = 0 ;
        if( objVol != null ){
            String str = objVol.getSnap_name();
            VolumeMap objVM = view.initor.mdb.getVolMapFromVectorOnName(str);
            if(objVM != null){
                iclnt_id = objVM.getVolClntID();
            }
        }
        Audit audit = view.audit.registerAuditRecord( iclnt_id, MenuAndBtnCenterForMainUi.FUNC_ADD_ADSCH );

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
                BrowserTreeNode chiefSchNode = view.getChiefNodeOnRoot( ResourceCenter.TYPE_CHIEF_ADSCH );
                if( chiefSchNode != null ){
                    view.setCurNode( chiefSchNode );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    ProcessEventOnChiefADSch peOnChiefSch = new ProcessEventOnChiefADSch( view );
                    TreePath path = new TreePath( chiefSchNode.getPath() );
                    peOnChiefSch.processTreeSelection( path );
                    peOnChiefSch.controlMenuAndBtnForTreeEvent();
                    view.getTree().setSelectionPath( path );
                    view.getTree().requestFocus();
                }

        }
SanBootView.log.info(getClass().getName(),"########### End of add scheduler action. " );
    }
}
