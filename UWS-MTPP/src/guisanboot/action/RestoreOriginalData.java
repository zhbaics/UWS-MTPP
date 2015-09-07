/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.DestAgent;
import guisanboot.data.DhcpClientInfo;
import guisanboot.data.NetCard;
import guisanboot.data.SourceAgent;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefNetBootHost;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.RestoreOriginalDiskWizardDialog;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class RestoreOriginalData extends GeneralActionForMainUi {
    private boolean isSwitchDisk = false;

    public RestoreOriginalData(){
        this( false );
    }

    public RestoreOriginalData( boolean isSwitchDisk ){
        super(
            ResourceCenter.BTN_ICON_RD_16,
            ResourceCenter.BTN_ICON_RD_50,
            "View.MenuItem.rstOriData",
            MenuAndBtnCenterForMainUi.FUNC_RESTORE_DISK
        );
        this.isSwitchDisk = isSwitchDisk;
    }

    @Override public void doAction(ActionEvent evt){
        boolean isOk,needModUUID = false;

        if( this.isSwitchDisk ){
SanBootView.log.info(getClass().getName(),"########### Entering switch-restore original disk action. " );
        }else{
SanBootView.log.info(getClass().getName(),"########### Entering restore original disk action. " );
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

            // 判断是否成功初始化了
            if( !host.isInited() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notInited")
                );
                return;
            }

            if( !this.isSwitchDisk ){
                // 判断是否从 network 启动
                isOk = view.initor.mdb.isStartupfromSAN( host.getIP(),host.getPort(),"C" );
                if( isOk ){
                    if( !view.initor.mdb.isStartupFromSAN() ){
                        JOptionPane.showMessageDialog( view,
                            SanBootView.res.getString("MenuAndBtnCenter.error.notStartFromNet")
                        );
                        return;
                    }
                }else{
                    JOptionPane.showMessageDialog( view,
                        ResourceCenter.getCmdString( ResourceCenter.CMD_IS_STARTUP_FROM_NET )+" : "+
                            view.initor.mdb.getErrorMessage()
                    );
                    return;
                }
            }

            String targetSrvName = view.initor.mdb.getHostName();
            if( targetSrvName.equals("") ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("EditProfileDialog.error.getHostNameFailed")
                );
                return;
            }

            String windir = view.initor.mdb.getWinDir( host.getIP(),host.getPort() );
SanBootView.log.info(getClass().getName(), "windir: " + windir );
            if( windir.equals("") ){
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_WINDIR )+
                    ": "+
                    view.initor.mdb.getErrorMessage()
                );
                return;
            }

            DestAgent da = new DestAgent(
                -1,
                host.getIP(),
                host.getPort(),
                host.getOS(),
                "", // mac
                "",  // desc
                BootHost.PROTECT_TYPE_MTPP
            );
            da.setHostType( DestAgent.TYPE_ORI_HOST );
            da.setSrc_Agnt_id( host.getID() );
            da.setHostUUID( host.getUUID() );
            da.setBootMode( host.getBootMode() );
            da.setMachine( host.getMachine() );

            RestoreOriginalDiskWizardDialog dialog = new RestoreOriginalDiskWizardDialog(
                    windir,view,da,targetSrvName,needModUUID,isSwitchDisk );
            int width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 370+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
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

            // 判断是否从 network 启动
            isOk = view.initor.mdb.isStartupfromSAN( da.getDst_agent_ip(),da.getDst_agent_port(), "C" );
            if( isOk ){
                if( !view.initor.mdb.isStartupFromSAN() ){
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notStartFromNet")
                    );
                    return;
                }
            }else{
                JOptionPane.showMessageDialog( view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_IS_STARTUP_FROM_NET )+" : "+
                        view.initor.mdb.getErrorMessage()
                );
                return;
            }

            String targetSrvName = view.initor.mdb.getHostName();
            if( targetSrvName.equals("") ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("EditProfileDialog.error.getHostNameFailed")
                );
                return;
            }

            String windir = view.initor.mdb.getWinDir( da.getDst_agent_ip(),da.getDst_agent_port() );
SanBootView.log.info(getClass().getName(), "windir: " + windir );
            if( windir.equals("") ){
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_WINDIR )+
                    ": "+
                    view.initor.mdb.getErrorMessage()
                );
                return;
            }

            if( hostObj instanceof BootHost ){
                BootHost bh = (BootHost)hostObj;
                da.setSrc_Agnt_id( bh.getID() );
                da.setHostType( DestAgent.TYPE_DST_AGNT );
                da.setHostUUID( bh.getUUID() );
                da.setBootMode( bh.getBootMode() );
                da.setMachine( bh.getMachine() );

                // 获取bh的网卡信息
                isOk = view.initor.mdb.getIPInfoFromSrv(  ResourceCenter.CLT_IP_CONF + "/" + bh.getID() + ResourceCenter.CONF_IP );
                if( !isOk ){
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("InitBootHostWizardDialog.log.getIpInfo")+" "+
                        SanBootView.res.getString("common.failed")
                    );
                    return;
                }else{
                    ArrayList netCardList = view.initor.mdb.getAllNetCardinfoFromSrv();
                    if( !this.isContainedInSet( netCardList, da.getDst_agent_mac() ) ){
                        needModUUID = true;
                    }
                }

            }else{
                SourceAgent sa = (SourceAgent)hostObj;
                da.setSrc_Agnt_id( sa.getSrc_agnt_id() );
                da.setHostType( DestAgent.TYPE_SRC_AGNT );
                da.setRealBootHostFlag( false );
                da.setBootMode( sa.getSrc_agnt_boot_mode() );
                da.setMachine( sa.getMachine() );
                needModUUID = true;
            }

            RestoreOriginalDiskWizardDialog dialog = new RestoreOriginalDiskWizardDialog(
                    windir,view,da,targetSrvName,needModUUID,isSwitchDisk );
            int width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 370+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
        }

        if( !this.isSwitchDisk ){
SanBootView.log.info(getClass().getName(),"########### End of restore original disk action." );
        }else{
SanBootView.log.info(getClass().getName(),"########### End of switch-restore original disk action." );
        }
    }

    private boolean isContainedInSet( ArrayList<NetCard> list,String mac ){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            NetCard nc = list.get(i);
            String smac1 = DhcpClientInfo.getSimpleMac( nc.mac );
            String smac2 = DhcpClientInfo.getSimpleMac( mac );
            if( smac1.equals( smac2) ){
                return true;
            }
        }
        return false;
    }
}
