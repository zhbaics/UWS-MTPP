/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.entity.SubCluster;
import guisanboot.data.BootHost;
import guisanboot.data.GetAgentInfo;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.DelHost;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author zourishun
 */
public class DelHostAction extends GeneralActionForMainUi{
    boolean isCluster = false;

    public DelHostAction(){
        super(
            ResourceCenter.SMALL_DEL_HOST,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delHost",
            MenuAndBtnCenterForMainUi.FUNC_DEL_HOST
        );
    }

    private Object[] isConnectedToThisHost( BootHost selHost,String delConfirmStr ){
        int option;

        boolean isConnect = true;
        Object[] ret = new Object[2];
        if( selHost.isWinHost() ){
            if( selHost.isCMDPProtect() ){
                try{
                    // 获取主机信息,
                    GetAgentInfo getAgentInfo = new GetAgentInfo(
                        ResourceCenter.getCmdpS2A_CmdPath1( selHost.getIP(), selHost.getPort() ) + "getsysinfo 2>/dev/null",
                        view.getSocket()
                    );
                    getAgentInfo.setCmdType( ResourceCenter.CMD_TYPE_CMDP );
                    boolean isOk = getAgentInfo.getAgentInfo();
                    if( !isOk ){
                        option = JOptionPane.showConfirmDialog(
                            view,
                            delConfirmStr,
                            SanBootView.res.getString("common.confirm"),  //"Confirm",
                            JOptionPane.OK_CANCEL_OPTION
                        );
                        if( ( option == JOptionPane.CANCEL_OPTION ) || ( option == JOptionPane.CLOSED_OPTION) ){
                            ret[0] = new Boolean( true );
                            ret[1] = new Boolean( false );
                            return ret;
                        }

                        ret[0] = new Boolean( false );
                        isConnect = false;
                    }else{
                        if( !this.isCluster ){
                            option = JOptionPane.showConfirmDialog(
                                view,
                                SanBootView.res.getString("MenuAndBtnCenter.confirm3"),
                                SanBootView.res.getString("common.confirm"),  //"Confirm",
                                JOptionPane.OK_CANCEL_OPTION
                            );
                            if(  ( option == JOptionPane.CANCEL_OPTION ) || (  option == JOptionPane.CLOSED_OPTION) ){
                                ret[0] = new Boolean( true );
                                ret[1] = new Boolean( false );
                                return ret;
                            }
                        }
                        ret[0] = new Boolean( false );
                    }
                }catch(Exception ex){
                    option = JOptionPane.showConfirmDialog(
                        view,
                        delConfirmStr,
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if( ( option == JOptionPane.CANCEL_OPTION ) || ( option == JOptionPane.CLOSED_OPTION) ){
                        ret[0] = new Boolean( true );
                        ret[1] = new Boolean( false );
                        return ret;
                    }

                    ret[0] = new Boolean( false );
                    isConnect = false;
                }
            }else{ // mtpp
                option = JOptionPane.showConfirmDialog(
                    view,
                    delConfirmStr,
                    SanBootView.res.getString("common.confirm"),  //"Confirm",
                    JOptionPane.OK_CANCEL_OPTION
                );
                if(  ( option == JOptionPane.CANCEL_OPTION ) || (  option == JOptionPane.CLOSED_OPTION) ){
                    ret[0] = new Boolean( true );
                    ret[1] = new Boolean( false );
                    return ret;
                }
                ret[0] = new Boolean( false );
            }
        }else{ // linux
            if( !view.initor.mdb.addIscsiInitorDriver( selHost.getIP(),selHost.getPort() ) ){
                option = JOptionPane.showConfirmDialog(
                    view,
                    delConfirmStr,
                    SanBootView.res.getString("common.confirm"),  //"Confirm",
                    JOptionPane.OK_CANCEL_OPTION
                );
                if( ( option == JOptionPane.CANCEL_OPTION ) || ( option == JOptionPane.CLOSED_OPTION) ){
                    ret[0] = new Boolean( true );
                    ret[1] = new Boolean( false );
                    return ret;
                }

                ret[0] = new Boolean( false );
                isConnect = false;
            }else{
                if( !this.isCluster ){
                    option = JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm3"),
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if(  ( option == JOptionPane.CANCEL_OPTION ) || (  option == JOptionPane.CLOSED_OPTION) ){
                        ret[0] = new Boolean( true );
                        ret[1] = new Boolean( false );
                        return ret;
                    }
                }
                ret[0] = new Boolean( false );
            }
        }

        ret[1] = new Boolean( isConnect );
        return ret;
    }

    @Override public void doAction(ActionEvent evt){
        Object[] ret;
        boolean isConnect = true;

SanBootView.log.info(getClass().getName(),"########### Entering delete boothost or cluster action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isHost = (selObj instanceof BootHost );
        isCluster = ( selObj instanceof Cluster );
        if( !isHost && !isCluster ) return;

        BootHost selHost = null;
        Cluster selCluster = null;
        String delConfirmStr;
        if( isHost ){
            selHost = (BootHost)selObj;
        }else{
            selCluster = (Cluster)selObj;
        }

        if( selCluster != null ){
            delConfirmStr = SanBootView.res.getString("MenuAndBtnCenter.confirm32");
        }else{
            delConfirmStr = SanBootView.res.getString("MenuAndBtnCenter.confirm10");
        }

        if( selHost != null ){
            ret = this.isConnectedToThisHost( selHost,delConfirmStr );
            if( ((Boolean)ret[0]).booleanValue() ) return;
            isConnect = ((Boolean)ret[1]).booleanValue();
        }else{
            boolean isTotalConnect=false,isFirst = true;
            ArrayList<SubCluster> realHostList = selCluster.getRealSubCluster();
            int size = realHostList.size();
            for( int i=0; i<size; i++ ){
                SubCluster subc = realHostList.get(i);
                ret = this.isConnectedToThisHost( subc.getHost(),delConfirmStr );
                if( ((Boolean)ret[0]).booleanValue() ) return;
                isConnect = ((Boolean)ret[1]).booleanValue();
                if( isFirst ){
                    isTotalConnect = isConnect;
                    isFirst = false;
                }else{
                    isTotalConnect &= isConnect;
                }
                subc.setIsConnected( isConnect );
            }

            if( this.isCluster && isTotalConnect ){
                int option = JOptionPane.showConfirmDialog(
                    view,
                    SanBootView.res.getString("MenuAndBtnCenter.confirm31"),
                    SanBootView.res.getString("common.confirm"),  //"Confirm",
                    JOptionPane.OK_CANCEL_OPTION
                );
                if(  ( option == JOptionPane.CANCEL_OPTION ) || (  option == JOptionPane.CLOSED_OPTION) ){
                    return;
                }
            }
        }

        DelHost thread = new DelHost( view,selHost,isConnect,selCluster );
        String title,tip;
        if( this.isCluster ){
            title = SanBootView.res.getString("View.pdiagTitle.delCluster");
            tip = SanBootView.res.getString("View.pdiagTip.delCluster");
        }else{
            title = SanBootView.res.getString("View.pdiagTitle.delHost");
            tip = SanBootView.res.getString("View.pdiagTip.delHost");
        }
        view.startupProcessDiag(
            title,
            tip,
            thread
        );
SanBootView.log.info(getClass().getName(),"########### End of delete host(BootHost) action. " );
    }
}
