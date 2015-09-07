/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.ams.ui.AmsPhyFailbackWizardDialog;
import guisanboot.cluster.entity.Cluster;
import guisanboot.cmdp.ui.PhyFailbackWizardDialog;
import guisanboot.data.BootHost;
import guisanboot.data.DestAgent;
import guisanboot.data.DhcpClientInfo;
import guisanboot.data.NetCard;
import guisanboot.data.SourceAgent;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.BootVerList;
import guisanboot.ui.ChiefNetBootHost;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;

/**
 * FailbackByPhyProtectAction.java
 *
 * Created on 2010-6-7, 13:47:49
 */
public class FailbackByPhyProtectAction extends GeneralActionForMainUi{
    private boolean isSwitchDisk = false;

    public FailbackByPhyProtectAction(){
        this( false );
    }
    
    public FailbackByPhyProtectAction( boolean isSwitchDisk ){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.failback",
            MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILBACK
        );
        this.isSwitchDisk = isSwitchDisk;
    }

    @Override public void doAction(ActionEvent evt){
        BootVerList bootVer;
        boolean isOk,needFixMirror = true;

        if( this.isSwitchDisk ){
SanBootView.log.info(getClass().getName(),"########### Entering phy-switch-to-localdisk action." );
        }else{
SanBootView.log.info(getClass().getName(),"########### Entering phy-failback action." );
        }
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( selObj instanceof BootHost ){
            BootHost host = (BootHost)selObj;
            Cluster cluster = null;
            if( host.isCluster() ){
                cluster = view.initor.mdb.getClusterOnID( host.getClnt_cluster_id() );
                if( !cluster.isWinHost() ){
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notWinHost")
                    );
                    return;
                }
                if( !cluster.isInited() ){
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notInited")
                    );
                    return;
                }
            }else{
                if( !host.isInited() ){
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notInited")
                    );
                    return;
                }
                if( !host.isWinHost() ){
                    //############################# AMS-BEGIN #############################
                    DestAgent da = new DestAgent(
                        -1,
                        host.getIP(),
                        host.getPort(),
                        host.getOS(),
                        "",  // mac
                        "",  // desc
                        BootHost.PROTECT_TYPE_CMDP
                    );
                    da.setHostType( DestAgent.TYPE_ORI_HOST );
                    da.setSrc_Agnt_id( host.getID() );
                    da.setStopAllServFlag( host.isStopAllBaseServ() );

                    bootVer= view.getBootVer( host.getID() );

                    AmsPhyFailbackWizardDialog dialog = new AmsPhyFailbackWizardDialog( view,da,bootVer,needFixMirror,false );
                    int width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                    int height = 370+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                    dialog.setSize( width,height );
                    dialog.setLocation( view.getCenterPoint( width,height ) );
                    dialog.setVisible( true );

                    return;
                    //############################# AMS-END #############################
                }

            }
            
            if( !isStartFromLocal( host.getIP(),host.getPort() ) ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notStartFromLocal")
                );
                return;
            }
            
            int ret;
            if( this.isSwitchDisk  ){
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

            if( host.isCluster() ){
                bootVer= view.getBootVerForWinCluster( cluster.getCluster_id() );
            }else{
                bootVer= view.getBootVer( host.getID() );
            }

            DestAgent da = new DestAgent(
                -1,
                host.getIP(),
                host.getPort(),
                host.getOS(),
                "",  // mac
                "",  // desc
                BootHost.PROTECT_TYPE_CMDP
            );
            da.setHostType( DestAgent.TYPE_ORI_HOST );
            da.setSrc_Agnt_id( host.getID() );
            da.setStopAllServFlag( host.isStopAllBaseServ() );

            PhyFailbackWizardDialog dialog = new PhyFailbackWizardDialog( view,da,bootVer,needFixMirror,isSwitchDisk );
            int width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 395+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
            
            if( this.isSwitchDisk ){
SanBootView.log.info(getClass().getName(),"########### End of phy-switch-to-localdisk action." );
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
                        needFixMirror = false;
                    }
                }
            }else{
                SourceAgent sa = (SourceAgent)hostObj;
                da.setSrc_Agnt_id( sa.getSrc_agnt_id() );
                da.setHostType( DestAgent.TYPE_SRC_AGNT );
                da.setRealBootHostFlag( false );
                needFixMirror = false;
            }

            PhyFailbackWizardDialog dialog = new PhyFailbackWizardDialog( view,da,bootVer,needFixMirror,false );
            int width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 370+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of phy-failback action. " );
        }
    }
    
    private boolean isStartFromLocal( String ip,int port ){
        // 判断是否从 network 启动��
        boolean isOk = view.initor.mdb.isStartupfromSAN( ip,port,"C",ResourceCenter.CMD_TYPE_CMDP );
        if( isOk ){
            return !view.initor.mdb.isStartupFromSAN();
        }else{
            return false;
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
