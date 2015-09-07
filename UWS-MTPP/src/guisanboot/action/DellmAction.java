/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.data.LogicalVol;
import guisanboot.data.LunMap;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.View;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefLunMap;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefLunMap;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class DellmAction extends GeneralActionForMainUi{
    public DellmAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.dellm",
            MenuAndBtnCenterForMainUi.FUNC_CANCEL_LM
        );
    }

    @Override public void doAction(ActionEvent evt){
        boolean isOk;
SanBootView.log.info(getClass().getName(),"########### Entering delete lunmap action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !( selObj instanceof LunMap ) ){
            return;
        }

        int ret = JOptionPane.showConfirmDialog(
            view,
            SanBootView.res.getString("MenuAndBtnCenter.confirm4"),
            SanBootView.res.getString("common.confirm"),  //"Confirm",
            JOptionPane.OK_CANCEL_OPTION
        ) ;
        if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
            return;
        }

        Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_CANCEL_LM );

        LunMap lm = (LunMap)selObj;
        BrowserTreeNode chiefLMNode = lm.getFatherNode();
        ChiefLunMap chiefLM = (ChiefLunMap)chiefLMNode.getUserObject();
        BrowserTreeNode volNode = chiefLM.getFatherNode();
        Object volObj = volNode.getUserObject();
        int tid;
        if( volObj instanceof Volume ){
            Volume vol = (Volume)volObj;
            tid = vol.getTargetID();
            isOk = view.initor.mdb.delLunMap( tid, lm.getIP(), lm.getMask(),lm.getAccessMode() );
        }else if( volObj instanceof LogicalVol ){
            LogicalVol lv = (LogicalVol)volObj;
            tid = view.initor.mdb.getTargetIDOnLV( lv );
            isOk = view.initor.mdb.delLunMap( tid, lm.getIP(),lm.getMask(),lm.getAccessMode() );
        }else if( volObj instanceof VolumeMap ) {
            VolumeMap volMap = (VolumeMap)volObj;
            tid = volMap.getVolTargetID();
            isOk = view.initor.mdb.delLunMap( tid ,lm.getIP(),lm.getMask(),lm.getAccessMode() );
        }else if( volObj instanceof MirrorDiskInfo ){
            MirrorDiskInfo mdi = (MirrorDiskInfo)volObj;
            tid = mdi.getTargetID();
            isOk = view.initor.mdb.delLunMap( tid ,lm.getIP(),lm.getMask(),lm.getAccessMode() );
        }else if( volObj instanceof CloneDisk ){
            CloneDisk cloneDisk = (CloneDisk)volObj;
            tid = cloneDisk.getTarget_id();
            isOk = view.initor.mdb.delLunMap( tid, lm.getIP(), lm.getMask(),lm.getAccessMode() );
        }else{
            View v = (View)volObj;
            tid = v.getTargetID();
            isOk = view.initor.mdb.delLunMap( tid,lm.getIP(),lm.getMask(),lm.getAccessMode() );
        }

        if( isOk ){
            audit.setEventDesc( "Delete lunmap for target:" + tid + " on " + lm.getIP() + " successfully." );
            view.audit.addAuditRecord( audit);

            // 显示点击 chiefLMNode 后的右边tabpane中的内容
            view.setCurNode( chiefLMNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefLunMap peOnChiefLM= new ProcessEventOnChiefLunMap( view );
            TreePath path = new TreePath( chiefLMNode.getPath() );
            peOnChiefLM.processTreeSelection( path );
            peOnChiefLM.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }else{
            audit.setEventDesc( "Failed to delete lunmap for target:" + tid + " on " + lm.getIP() );
            view.audit.addAuditRecord( audit);

            view.showError1(
                ResourceCenter.CMD_DEL_LUNMAP,
                view.initor.mdb.getErrorCode(),
                //ResourceCenter.getGeneralErr( view.initor.mdb.getErrorCode() )
                view.initor.mdb.getErrorMessage()
            );
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of delete lunmap action. " );
    }
}
