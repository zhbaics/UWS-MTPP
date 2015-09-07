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
 * @author Administrator
 */
public class PowerOffVMHostAction extends GeneralActionForMainUi{
    public PowerOffVMHostAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.powerOffVMHost",
            MenuAndBtnCenterForMainUi.FUNC_POWEROFF_VMHOST
        );
    }
@Override public void doAction( ActionEvent evt ){
SanBootView.log.info(getClass().getName(),"########### Entering power on VMHost action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ){
SanBootView.log.info(getClass().getName(),"bad type or sel obj is null \n ########### End of power on VMHost action." );
            return;
        }
        if( selObj instanceof VMHostInfo ){
            VMHostInfo vmhost = (VMHostInfo)selObj;
            boolean isOk = view.initor.mdb.isRunningVMHost(vmhost);
            if( isOk ){
                PowerOffVMHostThread thread = new PowerOffVMHostThread(view , vmhost);
                view.startupProcessDiag(
                    SanBootView.res.getString("View.pdiagTitle.powerOffVMHost"),
                    SanBootView.res.getString("View.pdiagTip.powerOffVMHost"),
                    thread
                );
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString( "VMHostInfo.mes.powerOff" ) );
                return ;
            }
        }
        SanBootView.log.info(getClass().getName(),"########### End of power on VMHost action." );
     }
     
}

class PowerOffVMHostThread extends BasicGetSomethingThread{
    VMHostInfo vmhost;
    public static final int timeout = 59;
    public PowerOffVMHostThread( SanBootView view, VMHostInfo _vmhost){
        super(view);
        this.vmhost = _vmhost;
    }
    
     public boolean realRun(){
         boolean isOk , isRunning ;
         int times = 0 ;
         isOk = view.initor.mdb.shutdownVMHost(vmhost);
         if( isOk ){
             try{
                 while( true ){
                     isRunning = view.initor.mdb.isRunningVMHost(vmhost);
                     if( !isRunning ){
                         break;
                     } else {
                         if( times >timeout){
                             break;
                         }
                         times = times + 1;
                         Thread.sleep(1000*10);
                     }
                 }
             } catch (Exception ex){
                 JOptionPane.showMessageDialog(view, SanBootView.res.getString( "VMHostInfo.error.powerOfffailed" ) );
                 return false;
             }
         } else {
             JOptionPane.showMessageDialog(view, SanBootView.res.getString( "VMHostInfo.error.powerOfffailed" ) );
             return false;
         }
         
         if( isRunning ){
             isOk = view.initor.mdb.powerOffVMHost(this.vmhost);
             if( isOk ) {
                 isOk = view.initor.mdb.suspendVMHost(vmhost);
             } else {
                 JOptionPane.showMessageDialog(view, SanBootView.res.getString( "VMHostInfo.error.powerOfffailed" ) );
                 return false;
             }
             if( isOk ){
                 updateUI();
             } else {
                 JOptionPane.showMessageDialog(view, SanBootView.res.getString( "VMHostInfo.error.powerOfffailed" ) );
                 return false;
             }
         } else {
             updateUI();
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
