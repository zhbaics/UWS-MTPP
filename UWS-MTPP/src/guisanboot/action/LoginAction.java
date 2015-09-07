/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.data.GUIAdminOptUWS;
import guisanboot.res.ResourceCenter;
import guisanboot.tool.UWSSockConnectManager;
import guisanboot.ui.ConnectDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author zourishun
 */
public class LoginAction extends GeneralActionForMainUi{
    public LoginAction(){
        super(
            ResourceCenter.BTN_ICON_QCONNECT_16,
            ResourceCenter.BTN_ICON_QCONNECT_50,
            "View.MenuItem.connect",
            MenuAndBtnCenterForMainUi.FUNC_LOGIN
        );
    }

    @Override public void doAction(ActionEvent evt){
        ConnectDialog dialog;
        int width,height;
        int fail_cnt = 0;
        boolean ok = false;
        GUIAdminOptUWS selUWS,uws;

SanBootView.log.info(getClass().getName(),"########### Entering login action. " );

        if( view.initor.lastUWS != null ){
            uws = view.initor.lastUWS;
System.out.println(" last swu server: "+ uws.getServerIp() );
        }else{
            uws = view.initor.adminOpt.getFirstUWS();
            if( uws != null ){
System.out.println(" new swu server: "+ uws.getServerIp() );
            }else{
System.out.println(" none swu server" );
            }
        }
        dialog = new ConnectDialog(
            view,
            uws
        );
        width  = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        height = 210+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint(width,height) );

        while( fail_cnt<3 && !ok ){
            dialog.setVisible( true );

            Object[] ret = dialog.getValues();
            if( ret == null ) return;

            // 记录当前登录的UWS信息
            view.initor.serverIp = (String)ret[0];
            view.initor.user     = (String)ret[1];
            view.initor.passwd   = (String)ret[2];
            view.initor.port     = ((Integer)ret[3]).intValue();

            selUWS               = (GUIAdminOptUWS)ret[4];
            view.initor.txIp     = selUWS.getTxIP();
            view.initor.uws      = selUWS;

            //  与 iboot server 相 连
            ok = view.initor.realLogin( view.initor.serverIp,view.initor.port,view.initor.user,view.initor.passwd,0 );
            if( !ok ){
                fail_cnt++;
                JOptionPane.showMessageDialog( view,
                    view.initor.getInitErrMsg()
                );
            }
    	}// end of while

    	if( !ok ){
            view.initor.dealLoginFailure();
            return;
   	}else{
            view.initor.setLoginedFlag( true );
    	}

        view.mbCenter.setEnabledOnLogin( false );

        view.setTitle( SanBootView.res.getString( "View.frameTitle" )+"[ "+view.initor.serverIp+" ]" );

        // 开始真正地初始化 iboot server
        view.initor.initAppWithGraphy();

        // 增加登陆的新UWS到conf中
        view.initor.addUWSConf();
        view.initor.saveConf();
        view.mbCenter.setupConnectButtonStatus( view.initor.isLogined() );

        view.mbCenter.setEnabledOnMainMenu( MenuAndBtnCenterForMainUi.INDEX_LOGICAL_PROTECT, view.initor.mdb.isSupportMTPP() );
        view.mbCenter.setEnabledOnMainMenu( MenuAndBtnCenterForMainUi.INDEX_PHYSICAL_PROTECT,view.initor.mdb.isSupportCMDP() );

        UWSSockConnectManager conThread = new UWSSockConnectManager( view );
        conThread.start();

        Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_LOGIN );
        audit.setEventDesc( "Logon to system successfully.");
        view.audit.addAuditRecord( audit );

SanBootView.log.info(getClass().getName(),"########### End of login action. " );
    }
}
