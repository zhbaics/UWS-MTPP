/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.VMHostInfo;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefVMHost;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author Administrator
 */
public class DeleteVMHostAction extends GeneralActionForMainUi{
    public DeleteVMHostAction(){
        super(
                ResourceCenter.MENU_ICON_BLANK,
                ResourceCenter.MENU_ICON_BLANK,
                "View.MenuItem.delVMHost",
                MenuAndBtnCenterForMainUi.FUNC_DEL_VMHOST 
           );
    }
    
    @Override public void doAction(ActionEvent evt){
        SanBootView.log.info(getClass().getName(),"########### Entering create VMHost action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) {
        SanBootView.log.info(getClass().getName(),"selobj is null!\n ########### End of create mirror job action. " );
            return;
        }
        VMHostInfo vmhost = null ;
        Object[] ret;
        boolean isOk;
        if( selObj instanceof VMHostInfo ){
            vmhost = (VMHostInfo)selObj;
            isOk = view.initor.mdb.isRunningVMHost(vmhost);
            if( !isOk ){
                isOk = view.initor.mdb.delVMHost(vmhost);
                if( !isOk ){
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("View.error.delVMHost")
                    );
                    return;
                } else {
                    BrowserTreeNode vmhostNode = vmhost.getFatherNode();
                    view.setCurNode( vmhostNode );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    TreePath path = new TreePath( vmhostNode.getPath() );
                    ProcessEventOnChiefVMHost processEvent = new ProcessEventOnChiefVMHost( view );
                    processEvent.processTreeSelection( path );
                    processEvent.processTreeExpand(path);
                    processEvent.controlMenuAndBtnForTreeEvent();
                    view.getTree().setSelectionPath( path );
                    view.getTree().requestFocus();
                    view.initor.mdb.removeVMHost(vmhost);
                }
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString( "VMHostInfo.mes.powerOnAlready" ) );
                return ;
            }
        }
        SanBootView.log.info(getClass().getName(),"########### End of create vmhost action. " );
    }
}
