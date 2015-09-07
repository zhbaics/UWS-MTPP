/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.SourceAgent;
import guisanboot.datadup.data.BackupClient;
import guisanboot.datadup.data.BakTask;
import guisanboot.datadup.data.UniProfile;
import guisanboot.remotemirror.ChiefRollbackHost;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.FailoverForSrcAgntWizardDialog;
import guisanboot.ui.FailoverWizardDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;

/**
 * function 网络启动入口
 * @author zourishun
 */
public class FailoverAction extends GeneralActionForMainUi {
    boolean isSwitchDisk = false;

    public FailoverAction(){
       this( false );
    }

    public FailoverAction( boolean isSwitchDisk ){
        super(
            ResourceCenter.BTN_ICON_DR_16,
            ResourceCenter.BTN_ICON_DR_50,
            "View.MenuItem.failover",
            MenuAndBtnCenterForMainUi.FUNC_FAILOVER
        );
        this.isSwitchDisk = isSwitchDisk;
    }

    @Override public void doAction(ActionEvent evt){
        if( this.isSwitchDisk ){
SanBootView.log.info(getClass().getName(),"########### Entering switch-to-netdisk action. " );
        }else{
SanBootView.log.info(getClass().getName(),"########### Entering failover action. " );
        }
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isBootHost = ( selObj instanceof BootHost );
        boolean isSrvAgent = ( selObj instanceof SourceAgent );
        if( isBootHost ){
SanBootView.log.info(getClass().getName(),"selected host is boothost. " );
            BootHost host = (BootHost)selObj;
            if( !host.isWinHost() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notWinHost")
                );
                return;
            }

            if( !host.isInited() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notInited")
                );
                return;
            }

            int ret;

            if( this.isSwitchDisk ){
                // 检查是否有正在进行的数据复制任务，若有则报告错误。用户取消掉该任务后，方可进行该操作。
                if( !this.checkTask( host ) ){
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.TaskIsRunning")
                    );
                    return;
                }

                ret = JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm27"),
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
SanBootView.log.info(getClass().getName(),"cancel this operation! \n########### End of switch-to-netdisk action. " );
                        return;
                    }
            }else{
                ret = JOptionPane.showConfirmDialog(
                    view,
                    SanBootView.res.getString("MenuAndBtnCenter.confirm6"),
                    SanBootView.res.getString("common.confirm"),  //"Confirm",
                    JOptionPane.OK_CANCEL_OPTION
                );
                if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
SanBootView.log.info(getClass().getName(),"cancel this operation! \n########### End of failover action. " );
                    return;
                }
            }

            FailoverWizardDialog dialog = new FailoverWizardDialog( view,host,isSwitchDisk );
            int width  = 560+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; // 380;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
        }else if( isSrvAgent ){
SanBootView.log.info(getClass().getName(),"selected host is srcagnt. " );
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
SanBootView.log.info(getClass().getName(),"cancel this operation! \n########### End of failover action. " );
                return;
            }

            FailoverForSrcAgntWizardDialog dialog = new FailoverForSrcAgntWizardDialog( view,host,mustCheckDiskExist );
            int width  = 560+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 400+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; // 380;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
        }else{
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of failover action. " );
    }

    private boolean checkTask( BootHost host ){
        BackupClient bkClnt = view.initor.mdb.getBkClntOnUUID( host.getUUID()   );
        if( bkClnt == null ) return true;

        ArrayList profList = view.initor.mdb.getAllProfileOnClntID( bkClnt.getID() );
        if( profList.size() <= 0 ){
            return true;
        }

        boolean isOk = view.initor.mdb.updateTaskList();
        if( isOk ){
            ArrayList taskList = view.initor.mdb.getAllTaskList( 0 );
            for( int i=0; i<taskList.size(); i++ ){
                BakTask task = (BakTask)taskList.get(i);
                if( this.isContained( task.getProfileName(), profList ) ){
SanBootView.log.error( getClass().getName(), "This task still is running: " + task.getProfileName() +".  So can't begin to switch-net-disk.");
                    return false;
                }
            }
            return true;
        }else{
            return true;
        }
    }

    private boolean isContained( String profName,ArrayList profList ){
        int size = profList.size();
        for( int i=0; i<size; i++ ){
            UniProfile prof =(UniProfile)profList.get(i);
            if( prof.toString().equals( profName ) ){
                return true;
            }
        }
        return false;
    }
}
