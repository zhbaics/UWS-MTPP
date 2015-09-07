/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BindOfDiskLabelAndTid;
import guisanboot.data.BootHost;
import guisanboot.data.SourceAgent;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.GetUnixRstVer;
import guisanboot.ui.SanBootView;
import guisanboot.ui.SelectBootVerDialog;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author zourishun
 */
public class SelectBootVerAction extends GeneralActionForMainUi {
    public SelectBootVerAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.BTN_ICON_DR_50,
            "View.MenuItem.selBootVer",
            MenuAndBtnCenterForMainUi.FUNC_SELECT_BOOT_VER
        );
    }

    @Override public void doAction(ActionEvent evt){
        boolean isOk;
        String bootMac,targetSrvName;

SanBootView.log.info( getClass().getName(),"########### Entering select boot version action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( selObj instanceof BootHost ){
            BootHost host = (BootHost)selObj;

            if( host.isWinHost() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.isWinHost")
                );
                return;
            }

            if( !host.isInited() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notInited")
                );
                return;
            }

            String status = isStartFromNet( host.getIP(),host.getPort() );
            if(  !status.toUpperCase().equals("UNKNOW") ){
                if( status.toUpperCase().equals("YES") ){
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notChgVerWhenNetboot")
                    );
                    return;
                }
            }else{
                int ret = JOptionPane.showConfirmDialog(
                    view,
                    SanBootView.res.getString("MenuAndBtnCenter.confirm16"),
                    SanBootView.res.getString("common.confirm"),  //"Confirm",
                    JOptionPane.OK_CANCEL_OPTION
                );
                if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
                    return;
                }
            }

            isOk = view.initor.mdb.getUnixNetCardInfo( ResourceCenter.CLT_IP_CONF+"/"+host.getID()+ ResourceCenter.CONF_IP );
            if( isOk ){
                bootMac = view.initor.mdb.getUnixBootMac();
                 if( bootMac.equals("") ){
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("FailoverWizardDialog.error.notFoundBootMac")
                    );
                    return;
                 }
            }else{
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("FailoverWizardDialog.error.notFoundBootMac")
                );
                return;
            }

            targetSrvName = view.initor.mdb.getHostName();
            if( targetSrvName.equals("") ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("InitBootHostWizardDialog.log.getHostNameFailed")
                );
                return;
            }

            isOk = view.initor.mdb.getUnixPart1( ResourceCenter.CLT_IP_CONF + "/" + host.getID() + ResourceCenter.CONF_MP );
            if( !isOk ){
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_UNIX_PART )+" : "+
                        view.initor.mdb.getErrorMessage()
                );
                return;
            }
            Vector partList = view.initor.mdb.getUnixSysPart();

            ArrayList<BindOfDiskLabelAndTid> oldBootVerList = view.getUnixBootVer( host.getID() );

            // 正在准备快照版本
            GetUnixRstVer getRstVer = new GetUnixRstVer( view,host.getID() );
            view.startupProcessDiag(
                SanBootView.res.getString("View.pdiagTitle.getSnapVer"),
                SanBootView.res.getString("View.pdiagTip.getSnapVer"),
                getRstVer
            );
            Vector bindList = getRstVer.getBindList();

            SelectBootVerDialog dialog = new SelectBootVerDialog( view,bindList,oldBootVerList,bootMac,targetSrvName,host,partList );
            int width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 370+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
        }else if( selObj instanceof SourceAgent ){

            //////////////////////////////////////////////////////////////////////////
            //
            // ��对于SourceAgent来说，直接在目的端UWS上选择网启版本没有意义。因为该操作是对���Ƕ�
            // Դ�源端uws上的主机设计的,而目的端UWS上不存在源端UWS的环境.
            //
            //                          2009-09-23,zjj
            //
            //////////////////////////////////////////////////////////////////////////

/*
            SourceAgent sa = (SourceAgent)selObj;

            if( sa.isWinHost() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.isWinHost")
                );
                return;
            }

            isOk = view.initor.mdb.getUnixNetCardInfo( ResourceCenter.CLT_IP_CONF+"/" + ResourceCenter.PREFIX_SRC_AGNT + sa.getSrc_agnt_id() + ResourceCenter.CONF_IP );
            if( isOk ){
                bootMac = view.initor.mdb.getUnixBootMac();
                 if( bootMac.equals("") ){
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("FailoverWizardDialog.error.notFoundBootMac")
                    );
                    return;
                 }
            }else{
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("FailoverWizardDialog.error.notFoundBootMac")
                );
                return;
            }

            targetSrvName = view.initor.mdb.getHostName();
            if( targetSrvName.equals("") ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("InitBootHostWizardDialog.log.getHostNameFailed")
                );
                return;
            }

            isOk = view.initor.mdb.getUnixPart1( ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + sa.getSrc_agnt_id() + ResourceCenter.CONF_MP );
            if( !isOk ){
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_UNIX_PART )+" : "+
                        view.initor.mdb.getErrorMessage()
                );
                return;
            }
            Vector partList = view.initor.mdb.getUnixSysPart();

            // 正在准备快照版本
            GetUnixRstVerForSrcAgent getRstVer = new GetUnixRstVerForSrcAgent( view,sa.getSrc_agnt_id() );
            view.startupProcessDiag(
                SanBootView.res.getString("View.pdiagTitle.getSnapVer"),
                SanBootView.res.getString("View.pdiagTip.getSnapVer"),
                getRstVer
            );
            if( getRstVer.isNoVersion() ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.noVersion")
                );
                return;
            }
            Vector bindList = getRstVer.getBindList();

            SelectBootVerForSrcAgntDialog dialog = new SelectBootVerForSrcAgntDialog( view,bindList,null,bootMac,targetSrvName,sa,partList );
            int width  = 525+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 370+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
 */
        }
SanBootView.log.info(getClass().getName(),"########### End of select boot version action. " );
    }

    private String isStartFromNet( String ip,int port ){
        // 判断是否从 network 启动
        boolean isOk = view.initor.mdb.isStartupfromNetBoot( ip,port );
        if( isOk ){
            String ret = view.initor.mdb.isStartupFromNetBoot()?"yes":"no";
            return ret;
        }else{
            return "unknow";
        }
    }
}
