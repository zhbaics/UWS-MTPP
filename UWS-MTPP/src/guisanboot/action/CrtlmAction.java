/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.data.LogicalVol;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.View;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.AddLunMapDialog;
import guisanboot.ui.ChiefLunMap;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefLunMap;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.awt.event.ActionEvent;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class CrtlmAction extends GeneralActionForMainUi{
    public CrtlmAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtlm",
            MenuAndBtnCenterForMainUi.FUNC_LUNMAP
        );
    }

    @Override public void doAction(ActionEvent evt){
        int tid;
        ChiefLunMap chiefLm;
        BrowserTreeNode chiefLMNode=null,diskNode=null;
        Object volObj;

SanBootView.log.info(getClass().getName(),"########### Entering create lunmap action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !( selObj instanceof ChiefLunMap ) &&
            !( selObj instanceof View ) &&
            !( selObj instanceof VolumeMap ) &&
            !( selObj instanceof Volume ) &&
            !( selObj instanceof LogicalVol ) &&
            !( selObj instanceof MirrorDiskInfo ) &&
            !( selObj instanceof CloneDisk  )
        ){
            return;
        }

        if( selObj instanceof ChiefLunMap ){
            chiefLm = (ChiefLunMap)selObj;
            chiefLMNode = chiefLm.getTreeNode();
            BrowserTreeNode volNode = chiefLm.getFatherNode();
            volObj = volNode.getUserObject();
        }else if( selObj instanceof View ){
            View viewObj = (View)selObj;
            diskNode = viewObj.getTreeNode();
            volObj = selObj;
        }else if( selObj instanceof VolumeMap ){
            VolumeMap volMapObj = (VolumeMap)selObj;
            diskNode = volMapObj.getTreeNode();
            volObj = selObj;
        }else if( selObj instanceof Volume ){
            Volume vObj = (Volume)selObj;
            diskNode = vObj.getTreeNode();
            volObj = selObj;
        }else if( selObj instanceof LogicalVol ){
            LogicalVol lvObj = (LogicalVol)selObj;
            diskNode = lvObj.getTreeNode();
            volObj = selObj;
        }else if( selObj instanceof MirrorDiskInfo ){
            MirrorDiskInfo mdi =(MirrorDiskInfo)selObj;
            diskNode = mdi.getTreeNode();
            volObj = selObj;
        }else{ // CloneDisk
            CloneDisk cd  = (CloneDisk)selObj;
            diskNode = cd.getTreeNode();
            volObj = selObj;
        }

        if( diskNode != null ){
            chiefLMNode = view.getChiefLunMapNodeOnViewNode( diskNode );
        }

        AddLunMapDialog dialog = new AddLunMapDialog( view );
        int width = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 320+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object[] ret = dialog.getValues();
        if( ret == null || ret.length <= 0 ) return;

        if( volObj instanceof Volume ){
            Volume vol = (Volume)volObj;
            tid = vol.getTargetID();
        }else if( volObj instanceof LogicalVol ){
            LogicalVol lv = (LogicalVol)volObj;
            tid = view.initor.mdb.getTargetIDOnLV( lv );
        }else if( volObj instanceof View){
            View viewObj = (View)volObj;
            tid = viewObj.getTargetID();
        }else if( volObj instanceof MirrorDiskInfo ){
            MirrorDiskInfo mdi = (MirrorDiskInfo)volObj;
            tid = mdi.getTargetID();
        }else if( volObj instanceof CloneDisk ){
            CloneDisk cloneDisk =(CloneDisk)volObj;
            tid = cloneDisk.getTarget_id();
        }else{
            VolumeMap volMap = (VolumeMap)volObj;
            tid = volMap.getVolTargetID();
        }

        Audit audit = view.audit.registerAuditRecord( 0,MenuAndBtnCenterForMainUi.FUNC_LUNMAP );

        boolean isOk = view.initor.mdb.addLunMap(
            tid,
            (String)ret[0], // ip
            (String)ret[1], // mask
            (String)ret[2], // rwset
            (String)ret[3], // sev user
            (String)ret[4], // ser passwd
            (String)ret[5], //client user
            (String)ret[6] // client passwd
        );

        if( isOk ){
            audit.setEventDesc( "Add lunmap for target:" + tid +" on " + ret[0] + " successfully." );
            view.audit.addAuditRecord( audit );

            // 显示点击 chiefLMNode 后的右边tabpane中的内容
            if( chiefLMNode != null ){
                view.setCurNode( chiefLMNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                ProcessEventOnChiefLunMap peOnChiefLM= new ProcessEventOnChiefLunMap( view );
                TreePath path = new TreePath( chiefLMNode.getPath() );
                peOnChiefLM.processTreeSelection( path );
                peOnChiefLM.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }
        }else{
            audit.setEventDesc( "Failed to add lunmap for target: " + tid +" on " + ret[0] );
            view.audit.addAuditRecord( audit );

            view.showError1(
                ResourceCenter.CMD_ADD_LUNMAP,
                view.initor.mdb.getErrorCode(),
                //ResourceCenter.getGeneralErr( view.initor.mdb.getErrorCode() )
                view.initor.mdb.getErrorMessage()
            );
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of create lunmap action. " );
    }
}
