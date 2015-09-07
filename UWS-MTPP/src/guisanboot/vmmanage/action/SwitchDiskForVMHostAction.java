/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.vmmanage.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.ams.ui.AmsPhyFailoverWizardDialog;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.data.BootHost;
import guisanboot.data.VMHostInfo;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import guisanboot.vmmanage.ui.PhyFailoverWizardDialog1;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 * FailoverByPhyProtectAction.java
 *
 * Created on 2010-6-7, 13:46:19
 */
public class SwitchDiskForVMHostAction extends GeneralActionForMainUi{
    private boolean isSwitchDisk = false;

    public SwitchDiskForVMHostAction(){
        this( false );
    }
    
    public SwitchDiskForVMHostAction( boolean isSwitch ){
        super(
            ResourceCenter.BTN_ICON_DR_16,
            ResourceCenter.BTN_ICON_DR_50,
            "View.MenuItem.failover",
            MenuAndBtnCenterForMainUi.FUNC_WIN_VMHOST_SWITCHNETDISK
        );
        this.isSwitchDisk = isSwitch;
    }

    @Override public void doAction(ActionEvent evt){
        // 网络磁盘切换目前只能发生在boothost上(2010.11.15)
        
        if( this.isSwitchDisk ){
SanBootView.log.info(getClass().getName(),"########### Entering phy-switch-to-netdisk action. " );
        }else{
 SanBootView.log.info(getClass().getName(),"########### Entering phy-failover action. " );
        }
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isVMHost = ( selObj instanceof VMHostInfo );
        if( isVMHost ){
            VMHostInfo vmh = (VMHostInfo)selObj;
            BootHost host = view.initor.mdb.getBootHostFromVector( Integer.parseInt( vmh.getVm_clntid() ) );
            String tempIp = host.getIP(); 
            if( !vmh.getVm_vip().equals( host.getIP() ) ){
                host.setIP( vmh.getVm_vip() );
            }
            if( !host.isInited() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notInited")
                );
                return;
            }
            
            boolean isOk = view.initor.mdb.isRunningVMHost(vmh);
            if( !isOk ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("VMHostInfo.error.notpoweronVMHost")
                );
                return ;
            }

            if( !host.isWinHost() ){
//                JOptionPane.showMessageDialog( view,
//                    SanBootView.res.getString("MenuAndBtnCenter.error.notWinHost")
//                );
                
                AmsPhyFailoverWizardDialog dialog = new AmsPhyFailoverWizardDialog( view,host,isSwitchDisk );
                int width  = 560+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; //380;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );
                dialog.setVisible( true );

                return;
            }

            int ret;
            if( !this.isSwitchDisk ){
                ret = JOptionPane.showConfirmDialog(
                    view,
                    SanBootView.res.getString("MenuAndBtnCenter.confirm6"),
                    SanBootView.res.getString("common.confirm"),  //"Confirm",
                    JOptionPane.OK_CANCEL_OPTION
                );
                if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
SanBootView.log.info(getClass().getName(),"cancel this operation! \n########### End of switchdisk. " );
                    return;
                }
            }else{
                // 检查C盘是否为dg的组成部分
                PPProfile dg = view.initor.mdb.getBelongedDg( host.getID(), "C" );
                if( dg !=null && dg.isValidDriveGrp1() ){
                    ret = JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm29"),
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
SanBootView.log.info(getClass().getName(),"cancel this operation! \n########### End of phy-switch-to-netdisk action. " );
                        return;
                    }
                }else{
                    ret = JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm27"),
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
SanBootView.log.info(getClass().getName(),"cancel this operation! \n########### End of phy-switch-to-netdisk action. " );
                        return;
                    }
                }
            }

            PhyFailoverWizardDialog1 dialog = new PhyFailoverWizardDialog1( view,host,isSwitchDisk );
            int width  = 560+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; //380;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
            
            host.setIP(tempIp);
        }else{
            return;
        }
        if( !this.isSwitchDisk ){
SanBootView.log.info(getClass().getName(),"########### End of phy-failover action. " );
        }else{
SanBootView.log.info(getClass().getName(),"########### End of phy-switch-to-netdisk action. " );
        }
    }
}
