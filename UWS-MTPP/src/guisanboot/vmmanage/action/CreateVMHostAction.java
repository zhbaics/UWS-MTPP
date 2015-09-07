/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.SourceAgent;
import guisanboot.data.VMHostInfo;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefVMHost;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.GetRstVersion;
import guisanboot.ui.ProcessEventOnChiefVMHost;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import guisanboot.vmmanage.service.SetViewVersionThread;
import guisanboot.vmmanage.ui.CreateVMDialog;
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
public class CreateVMHostAction extends GeneralActionForMainUi{
    public CreateVMHostAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtVMHost",
            MenuAndBtnCenterForMainUi.FUNC_CRT_VMHOST
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
        Vector bindlist = null ;
        Object[] ret;
        boolean isOk;
        isOk = view.initor.mdb.hasVMServiceInfo();
        if( !isOk ){
            JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("CreateVMHost.error.noServiceInfo")
                    );
                    return;
        }
        if( selObj instanceof ChiefVMHost ){
            ChiefVMHost cvmhost = (ChiefVMHost)selObj;
            BrowserTreeNode fNode = cvmhost.getFatherNode();
            Object fObj = fNode.getUserObject();
            if( fObj instanceof BootHost ){
                BootHost bhost = (BootHost)fObj;
                vmhost = new VMHostInfo();
                vmhost.setVm_clntid(bhost.getID()+"");
                isOk = view.initor.mdb.updateVMServerInfo();
                if( isOk ){
                    vmhost.setVm_ip( view.initor.mdb.getVMServerIP() );
                    vmhost.setVm_port( view.initor.mdb.getVMServerPort() +"");
                } else {
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("CreateVMHost.error.wrongServer")
                    );
                    return;
                }
                // 正在准备快照版本
                ProgressDialog initDiag = new ProgressDialog(
                    view,
                    SanBootView.res.getString("View.pdiagTitle.getSnapVer"),
                    SanBootView.res.getString("View.pdiagTip.getSnapVer")
                );

                GetRstVersion getRstVer = new GetRstVersion( initDiag,view,bhost.getID(),true,true );
                getRstVer.start();
                initDiag.mySetSize();
                initDiag.setLocation( view.getCenterPoint( initDiag.getDefWidth(),initDiag.getDefHeight() ) );
                initDiag.setVisible( true );
                bindlist = getRstVer.getBindList();
                
                CreateVMDialog dialog = new CreateVMDialog(view, CreateVMDialog.OP_CREATE ,vmhost ,bindlist);
                int width = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 350+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );
                dialog.setVisible( true );
                
                ret = dialog.getValues();
                if( ret == null ) {
                    SanBootView.log.info(getClass().getName(), "cancel to create VMHost.");
                    return;
                }
                vmhost = (VMHostInfo)ret[0];
                SetViewVersionThread thread = new SetViewVersionThread(view , vmhost ,cvmhost);
                view.startupProcessDiag(
                    SanBootView.res.getString("View.pdiagTitle.createVMHost"),
                    SanBootView.res.getString("View.pdiagTip.createVMHost"),
                    thread
                );
            } else if( fObj instanceof SourceAgent ){
                SourceAgent srchost = (SourceAgent)fObj;
                vmhost = new VMHostInfo();
                vmhost.setVm_clntid(srchost.getSrc_agnt_id()+"");
                isOk = view.initor.mdb.updateVMServerInfo();
                if( isOk ){
                    vmhost.setVm_ip( view.initor.mdb.getVMServerIP() );
                    vmhost.setVm_port( view.initor.mdb.getVMServerPort() +"");
                } else {
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("CreateVMHost.error.wrongServer")
                    );
                    return;
                }
                // 正在准备快照版本
                ProgressDialog initDiag = new ProgressDialog(
                    view,
                    SanBootView.res.getString("View.pdiagTitle.getSnapVer"),
                    SanBootView.res.getString("View.pdiagTip.getSnapVer")
                );

                GetRstVersion getRstVer = new GetRstVersion( initDiag,view,srchost.getSrc_agnt_id(),true,true );
                getRstVer.start();
                initDiag.mySetSize();
                initDiag.setLocation( view.getCenterPoint( initDiag.getDefWidth(),initDiag.getDefHeight() ) );
                initDiag.setVisible( true );
                bindlist = getRstVer.getBindList();
                
                CreateVMDialog dialog = new CreateVMDialog(view, CreateVMDialog.OP_CREATE ,vmhost ,bindlist);
                int width = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 350+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );
                dialog.setVisible( true );
                
                ret = dialog.getValues();
                if( ret == null ) {
                    SanBootView.log.info(getClass().getName(), "cancel to create VMHost.");
                    return;
                }
                vmhost = (VMHostInfo)ret[0];
                SetViewVersionThread thread = new SetViewVersionThread(view , vmhost ,cvmhost);
                view.startupProcessDiag(
                    SanBootView.res.getString("View.pdiagTitle.createVMHost"),
                    SanBootView.res.getString("View.pdiagTip.createVMHost"),
                    thread
                );
            }
        }
        SanBootView.log.info(getClass().getName(),"########### End of create vmhost action. " );
    }
    
    
}
