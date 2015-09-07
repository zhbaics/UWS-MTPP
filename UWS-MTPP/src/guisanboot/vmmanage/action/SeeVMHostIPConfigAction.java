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
import guisanboot.vmmanage.ui.ShowVMHostIPConfigDialog;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author zourishun
 */
public class SeeVMHostIPConfigAction extends GeneralActionForMainUi{
    public SeeVMHostIPConfigAction(){
        super(  ResourceCenter.MENU_ICON_BLANK,
                ResourceCenter.MENU_ICON_BLANK,
                "View.MenuItem.seeVMHostIP",
                MenuAndBtnCenterForMainUi.FUNC_SEE_VMHOST_IPCONFIG
            );
    }

    @Override public void doAction( ActionEvent evt ){
        SanBootView.log.info(getClass().getName(),"########### Entering getipconfig action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ){
SanBootView.log.info(getClass().getName(),"bad type or sel obj is null \n ########### End of getipconfig action." );
            return;
        }
        
        if( selObj instanceof VMHostInfo ){
            VMHostInfo vmhost = (VMHostInfo)selObj;
            boolean isOk = view.initor.mdb.isRunningVMHost(vmhost);
            if( isOk ){
                GetVMHostIPThread thread = new GetVMHostIPThread(view , vmhost);
                view.startupProcessDiag(
                    SanBootView.res.getString("View.pdiagTitle.seeVMHostIP"),
                    SanBootView.res.getString("View.pdiagTip.seeVMHostIP"),
                    thread
                );
                isOk = thread.isOk();
                if( isOk ){
                    ArrayList<String> list = view.initor.mdb.getVMHostIPInfo1();
                    ShowVMHostIPConfigDialog dialog = new ShowVMHostIPConfigDialog( view ,list );
                    int width = 440+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                    int height = 350+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                    dialog.setSize( width,height );
                    dialog.setLocation( view.getCenterPoint( width,height ) );
                    dialog.setVisible( true );
                }
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString( "VMHostInfo.mes.powerOff" ) );
                return ;
            }
        }
        SanBootView.log.info(getClass().getName(),"########### End of seeVMHostIP action." );
     }
     
}
class GetVMHostIPThread extends BasicGetSomethingThread{
    VMHostInfo vmhost;
    public GetVMHostIPThread( SanBootView view, VMHostInfo _vmhost){
        super(view);
        this.vmhost = _vmhost;
    }
    
     public boolean realRun(){
         boolean isOk;
         isOk = view.initor.mdb.updateIPConfigInfo(vmhost.getVm_vip(), ResourceCenter.CMDP_AGENT_PORT);
         if( !isOk ){
             JOptionPane.showMessageDialog(view, SanBootView.res.getString( "VMHostInfo.error.seeVMHostIP" ) );
             return false;
         }
         return isOk;
     }
}

