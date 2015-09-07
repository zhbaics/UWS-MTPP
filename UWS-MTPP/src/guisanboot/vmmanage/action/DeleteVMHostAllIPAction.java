/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.VMHostInfo;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import guisanboot.vmmanage.service.ProcessEventOnVMHostInfo;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class DeleteVMHostAllIPAction extends GeneralActionForMainUi{
    public DeleteVMHostAllIPAction(){
        super(
                ResourceCenter.MENU_ICON_BLANK,
                ResourceCenter.MENU_ICON_BLANK,
                "View.MenuItem.delVMHostIP",
                MenuAndBtnCenterForMainUi.FUNC_DEL_VMHOST_IP 
           );
    }
    
        @Override public void doAction( ActionEvent evt ){
SanBootView.log.info(getClass().getName(),"########### Entering delVMHostIP action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ){
SanBootView.log.info(getClass().getName(),"bad type or sel obj is null \n ########### End of delVMHostIP action." );
            return;
        }
        
        int ret = JOptionPane.showConfirmDialog(
                    view,
                    SanBootView.res.getString("MenuAndBtnCenter.confirm.delVMHostIP"),
                    SanBootView.res.getString("common.confirm"),  //"Confirm",
                    JOptionPane.OK_CANCEL_OPTION
                );
        if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
            SanBootView.log.info(getClass().getName(),"cancel this operation! \n########### End of phy-switch-to-netdisk action. " );
            return;
        }
        
        if( selObj instanceof VMHostInfo ){
            VMHostInfo vmhost = (VMHostInfo)selObj;
            boolean isOk = view.initor.mdb.isRunningVMHost(vmhost);
            if( isOk ){
                DelVMHostIPThread thread = new DelVMHostIPThread(view , vmhost);
                view.startupProcessDiag(
                    SanBootView.res.getString("View.pdiagTitle.delVMHostIP"),
                    SanBootView.res.getString("View.pdiagTip.delVMHostIP"),
                    thread
                );
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString( "VMHostInfo.mes.powerOff" ) );
                return ;
            }
        }
        SanBootView.log.info(getClass().getName(),"########### End of delVMHostIP action." );
     }
     
}

class DelVMHostIPThread extends BasicGetSomethingThread{
    VMHostInfo vmhost;
    public DelVMHostIPThread( SanBootView view, VMHostInfo _vmhost){
        super(view);
        this.vmhost = _vmhost;
    }
    
     public boolean realRun(){
         boolean isOk;
             isOk = view.initor.mdb.deleteVMHostIP(vmhost);
             if( isOk ){
                 updateUI();
             } else {
                 JOptionPane.showMessageDialog(view, SanBootView.res.getString( "VMHostInfo.error.delVMHostIP" ) );
                 return false;
             }
         return isOk;
     }
     
     public void updateUI(){
         BrowserTreeNode vmHostNode = this.vmhost.getTreeNode();
         if( vmHostNode != null ){
             view.setCurNode( vmHostNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnVMHostInfo peOnVMHostInfo = new ProcessEventOnVMHostInfo( view );
            TreePath path = new TreePath( vmHostNode.getPath() );
            peOnVMHostInfo.processTreeSelection( path );
            peOnVMHostInfo.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
         }
     }
}
