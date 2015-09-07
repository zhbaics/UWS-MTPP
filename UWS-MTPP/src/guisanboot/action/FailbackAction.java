/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.DestAgent;
import guisanboot.data.SourceAgent;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.BootVerList;
import guisanboot.ui.ChiefNetBootHost;
import guisanboot.ui.FailbackWizardDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class FailbackAction extends GeneralActionForMainUi {
    private boolean isSwitchDisk = false;

    public FailbackAction(){
        this( false );
    }

    public FailbackAction( boolean isSwitchDisk ){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.failback",
            MenuAndBtnCenterForMainUi.FUNC_FAILBACK
        );
        this.isSwitchDisk = isSwitchDisk;
    }

    @Override public void doAction(ActionEvent evt){
        BootVerList bootVer;

        if( this.isSwitchDisk ){
SanBootView.log.info(getClass().getName(),"########### Entering switch-to-localdisk action." );
        }else{
SanBootView.log.info(getClass().getName(),"########### Entering failback action. " );
        }

        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( selObj instanceof BootHost ){
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

            if( !isStartFromLocal( host.getIP(),host.getPort() ) ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notStartFromLocal")
                );
                return;
            }

            int ret;
            if( this.isSwitchDisk ){
                ret = JOptionPane.showConfirmDialog(
                    view,
                    SanBootView.res.getString("MenuAndBtnCenter.confirm28"),
                    SanBootView.res.getString("common.confirm"),  //"Confirm",
                    JOptionPane.OK_CANCEL_OPTION
                );
                if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
                    return;
                }
            }else{
                ret = JOptionPane.showConfirmDialog(
                    view,
                    SanBootView.res.getString("MenuAndBtnCenter.confirm5"),
                    SanBootView.res.getString("common.confirm"),  //"Confirm",
                    JOptionPane.OK_CANCEL_OPTION
                );
                if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
                    return;
                }
            }

            bootVer= view.getBootVer( host.getID() );

            DestAgent da = new DestAgent(
                -1,
                host.getIP(),
                host.getPort(),
                host.getOS(),
                "",  // mac
                "",  // desc
                BootHost.PROTECT_TYPE_MTPP
            );
            da.setSrc_Agnt_id( host.getID() );
            da.setHostType( DestAgent.TYPE_ORI_HOST );
            da.setStopAllServFlag( host.isStopAllBaseServ() );

            FailbackWizardDialog dialog = new FailbackWizardDialog( view,da,bootVer,isSwitchDisk );
            int width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 370+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );

            if( this.isSwitchDisk ){
SanBootView.log.info(getClass().getName(),"########### End of switch-to-localdisk action." );
            }else{
SanBootView.log.info(getClass().getName(),"########### End of phy-failback action." );
            }
        }else{
            DestAgent da = (DestAgent)selObj;
            BrowserTreeNode chiefNBHNode = da.getFatherNode();
            ChiefNetBootHost chiefNBH = (ChiefNetBootHost)chiefNBHNode.getUserObject();
            BrowserTreeNode hostNode = chiefNBH.getFatherNode();
            Object hostObj = hostNode.getUserObject();

            if( !da.isWinHost() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notWinHost")
                );
                return;
            }

            if( !isStartFromLocal( da.getIP(),da.getPort() ) ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notStartFromLocal")
                );
                return;
            }

            int ret = JOptionPane.showConfirmDialog(
                view,
                SanBootView.res.getString("MenuAndBtnCenter.confirm5"),
                SanBootView.res.getString("common.confirm"),  //"Confirm",
                JOptionPane.OK_CANCEL_OPTION
            );
            if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
                return;
            }

            bootVer= view.getBootVerForDestAgent( da.getID() );

            if( hostObj instanceof BootHost ){
                BootHost bh = (BootHost)hostObj;
                da.setSrc_Agnt_id( bh.getID() );
                da.setHostType( DestAgent.TYPE_DST_AGNT );
                da.setStopAllServFlag( bh.isStopAllBaseServ() );
            }else{
                SourceAgent sa = (SourceAgent)hostObj;
                da.setSrc_Agnt_id( sa.getSrc_agnt_id() );
                da.setHostType( DestAgent.TYPE_SRC_AGNT );
                da.setRealBootHostFlag( false );
            }

            FailbackWizardDialog dialog = new FailbackWizardDialog( view,da,bootVer,isSwitchDisk );
            int width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 370+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of failback action. " );
        }
    }

    private boolean isStartFromLocal( String ip,int port ){
        // 判断是否从 network 启动��
        boolean isOk = view.initor.mdb.isStartupfromSAN( ip,port,"C" );
        if( isOk ){
            return !view.initor.mdb.isStartupFromSAN();
        }else{
            return false;
        }
    }
}
