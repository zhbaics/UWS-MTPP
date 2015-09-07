/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.VMHostInfo;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.GetRstVersion;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import guisanboot.vmmanage.service.ProcessEventOnVMHostInfo;
import guisanboot.vmmanage.ui.ModifyVMDialog;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author Administrator
 */
public class ModifyVMHostAction extends GeneralActionForMainUi{
    public ModifyVMHostAction(){
        super(
                ResourceCenter.MENU_ICON_BLANK,
                ResourceCenter.MENU_ICON_BLANK,
                "View.MenuItem.modVMHost",
                MenuAndBtnCenterForMainUi.FUNC_MOD_VMHOST 
           );
    }
    
     @Override public void doAction(ActionEvent evt){
        SanBootView.log.info(getClass().getName(),"########### Entering modify VMHost action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) {
        SanBootView.log.info(getClass().getName(),"selobj is null!\n ########### End of modify mirror job action. " );
            return;
        }
        VMHostInfo vmhost = null ;
        Vector bindlist = null;
        Object[] ret;
        boolean isOk;
        if( selObj instanceof VMHostInfo ){
            vmhost = (VMHostInfo)selObj;
            // 正在准备快照版本
            isOk = view.initor.mdb.isRunningVMHost(vmhost);
            if( !isOk ){
                ProgressDialog initDiag = new ProgressDialog(
                    view,
                    SanBootView.res.getString("View.pdiagTitle.getSnapVer"),
                    SanBootView. res.getString("View.pdiagTip.getSnapVer")
                );

                GetRstVersion getRstVer = new GetRstVersion( initDiag,view,Integer.valueOf(vmhost.getVm_clntid()).intValue(),true,true );
                getRstVer.start();
                initDiag.mySetSize();
                initDiag.setLocation( view.getCenterPoint( initDiag.getDefWidth(),initDiag.getDefHeight() ) );
                initDiag.setVisible( true );
                bindlist = getRstVer.getBindList();                

                ModifyVMDialog dialog = new ModifyVMDialog(view,vmhost,bindlist);
                int width = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 430+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );
                dialog.setVisible( true );

                ret = dialog.getValues();
                if( ret == null ) {
                    SanBootView.log.info(getClass().getName(), "cancel to modify the copy job.");
                    return;
                }
                vmhost = (VMHostInfo)ret[0];
                isOk = view.initor.mdb.changeDiskVMHost(vmhost);
                if( isOk ){
                    isOk = view.initor.mdb.modVMHost(vmhost);
                    if( !isOk ){
                        JOptionPane.showMessageDialog(view,
                            SanBootView.res.getString("CreateVMHost.error.wrongIP")
                        );
                        return;
                    } else {
                        BrowserTreeNode vmhostNode = vmhost.getTreeNode();
                        view.setCurNode( vmhostNode );
                        view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                        TreePath path = new TreePath( vmhostNode.getPath() );
                        ProcessEventOnVMHostInfo processEvent = new ProcessEventOnVMHostInfo( view );
                        processEvent.processTreeSelection( path );
                        processEvent.controlMenuAndBtnForTreeEvent();
                        view.getTree().setSelectionPath( path );
                        view.getTree().requestFocus();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString( "VMHostInfo.mes.powerOnAlready" ) );
                return ;
            }
        }
        
        SanBootView.log.info(getClass().getName(),"########### End of modify vmhost action. " );
    }
}
