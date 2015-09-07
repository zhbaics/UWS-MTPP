/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.data.BootHost;
import guisanboot.data.Volume;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefOrphanVolume;
import guisanboot.ui.CreateOrphanVolDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefOrphanVol;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class CrtVolAction extends GeneralActionForMainUi{
    public CrtVolAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtVol",
            MenuAndBtnCenterForMainUi.FUNC_CRT_VOL
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering create vol action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isChiefOrphVol = ( selObj instanceof ChiefOrphanVolume );
        boolean isBootHost = ( selObj instanceof BootHost );

        if( !isChiefOrphVol && !isBootHost ){
            return;
        }

        ArrayList poolList = view.initor.mdb.getPoolList();

        CreateOrphanVolDialog  dialog = new CreateOrphanVolDialog( view,poolList );
        int width = 330+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 180+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object[] ret = dialog.getValues();
        if( ret == null || ret.length <= 0 ) return;

        Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_CRT_VOL );

        String name = (String)ret[0];
        int bksize = ((Integer)ret[1]).intValue();
        int bknum = ((Integer)ret[2]).intValue();
        int pid=((Integer)ret[3]).intValue();

        boolean isOk = view.initor.mdb.addOrphVol( name, bksize,bknum,pid );
        if( isOk ){
            audit.setEventDesc( "Create volume: " + name + " successfully." );
            view.audit.addAuditRecord( audit );

            if( isChiefOrphVol ){
                ChiefOrphanVolume chiefOrphVol = (ChiefOrphanVolume)selObj;
                BrowserTreeNode chiefOrphVolNode = chiefOrphVol.getTreeNode();

                Volume newVolume = view.initor.mdb.getCrtVolume();
                if( newVolume != null ){
                    BrowserTreeNode newVolNode = new BrowserTreeNode( newVolume,false );
                    newVolume.setTreeNode( newVolNode );
                    newVolume.setFatherNode( chiefOrphVolNode );
                    view.insertNodeToTree( chiefOrphVolNode,newVolNode );
                }

                // 显示点击 chiefOrphVolNode 后的右边tabpane中的内容
                view.setCurNode( chiefOrphVolNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                ProcessEventOnChiefOrphanVol peOnChiefOrphanVol = new ProcessEventOnChiefOrphanVol( view );
                TreePath path = new TreePath( chiefOrphVolNode.getPath() );
                peOnChiefOrphanVol.processTreeSelection( path );
                peOnChiefOrphanVol.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }else{
            }
        }else{
            audit.setEventDesc( "Failed to create volume: " + name );
            view.audit.addAuditRecord( audit );

            view.showError1(
                ResourceCenter.CMD_ADD_VOL,
                view.initor.mdb.getErrorCode(),
                //ResourceCenter.getCrVolErrStr( view.initor.mdb.getErrorCode() )
                view.initor.mdb.getErrorMessage()
            );
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of create vol action. " );
    }
}
