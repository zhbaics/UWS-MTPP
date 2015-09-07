/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.ams.ui.AmsPhyFailoverWizardDialog;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.cmdp.ui.PhyFailoverForSrcAgntWizardDialog;
import guisanboot.cmdp.ui.PhyFailoverWizardDialog;
import guisanboot.data.BootHost;
import guisanboot.data.SourceAgent;
import guisanboot.remotemirror.ChiefRollbackHost;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;

/**
 * FailoverByPhyProtectAction.java
 *
 * Created on 2010-6-7, 13:46:19
 */
public class FailoverByPhyProtectAction extends GeneralActionForMainUi{
    private boolean isSwitchDisk = false;

    public FailoverByPhyProtectAction(){
        this( false );
    }
    
    public FailoverByPhyProtectAction( boolean isSwitch ){
        super(
            ResourceCenter.BTN_ICON_DR_16,
            ResourceCenter.BTN_ICON_DR_50,
            "View.MenuItem.failover",
            MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER
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

        boolean isBootHost = ( selObj instanceof BootHost );
        boolean isSrvAgent = ( selObj instanceof SourceAgent );
        if( isBootHost ){
            BootHost host = (BootHost)selObj;

            if( !host.isInited() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notInited")
                );
                return;
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
SanBootView.log.info(getClass().getName(),"cancel this operation! \n########### End of phy-failover action. " );
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

            PhyFailoverWizardDialog dialog = new PhyFailoverWizardDialog( view,host,isSwitchDisk );
            int width  = 560+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; //380;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
        }else if( isSrvAgent ){
            boolean mustCheckDiskExist =false;
            SourceAgent host = (SourceAgent)selObj;
            BrowserTreeNode fNode = host.getFatherNode();
            Object fObj = fNode.getUserObject();
            if( fObj instanceof ChiefRollbackHost ){
                mustCheckDiskExist = true;
            }

            host.getTreeNode();
            if( !host.isWinHost() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notWinHost")
                );
                return;
            }

            int ret = JOptionPane.showConfirmDialog(
                view,
                SanBootView.res.getString("MenuAndBtnCenter.confirm6"),
                SanBootView.res.getString("common.confirm"),  //"Confirm",
                JOptionPane.OK_CANCEL_OPTION
            );
            if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
SanBootView.log.info(getClass().getName(),"cancel this operation! \n########### End of phy-failover action. " );
                return;
            }

            PhyFailoverForSrcAgntWizardDialog dialog = new PhyFailoverForSrcAgntWizardDialog( view,host,mustCheckDiskExist );
            int width  = 560+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; // 380;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
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
