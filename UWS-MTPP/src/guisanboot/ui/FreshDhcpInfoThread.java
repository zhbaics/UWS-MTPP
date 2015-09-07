/*
 * FreshDhcpInfoThread.java
 *
 * Created on 2007/12/13, PM 3:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.data.DHCPdb;
import guisanboot.res.ResourceCenter;

/**
 *
 * @author Administrator
 */
public class FreshDhcpInfoThread implements SlowerLaunch{
    String status="";
    String errormsg="";
    int cnt=0;
    DhcpDialog dialog;
    SanBootView view;
    String dhcpIp;
    int dhcp_port;
    String dhcp_acct;
    String dhcp_passwd;
    DHCPdb dhcpdb;
    
    /** Creates a new instance of FreshDhcpInfoThread */
    public FreshDhcpInfoThread( DhcpDialog _dialog,SanBootView _view,String _dhcpIp,int port,String acct,String passwd  ) {
        dialog = _dialog;
        view = _view;
        dhcpdb = view.initor.dhcpdb;
        
        dhcpIp = _dhcpIp;
        dhcp_port = port;
        dhcp_acct = acct;
        dhcp_passwd = passwd;
    }
    
    public boolean init(){
        boolean ret = true;
        StringBuffer errBuf = new StringBuffer();
        
        // 先与dhcp server通信,并获取dhcp info
        boolean ok = view.initor.realLogin( dhcpIp,dhcp_port,dhcp_acct,dhcp_passwd,1 );
        if( ok ){
            cnt++;
            status = SanBootView.res.getString("common.loadstatus.isDHCPStart");
            boolean dhcpEnable = false; 
            boolean viewFile = dhcpdb.viewFileContents("/etc/sysconfig/customdrv/dhcpenable");
            if( viewFile ){
                dhcpEnable = true;
            }else{
                // 通信错误
                if( dhcpdb.getErrorCode() <= -1000 ){
                    errBuf.append(
                        "\n"+ResourceCenter.getCmdString( ResourceCenter.CMD_DHCP_START )+" : "+
                        dhcpdb.getErrorMessage()
                    );
                    ret = false;
                }
            }
            dhcpdb.setDHCPStartFlag( dhcpEnable );
            
            cnt++;
            status = SanBootView.res.getString("common.loadstatus.global");
            if( !dhcpdb.getGlobalOptFromDhcp() ){
                errBuf.append(
                    "\n"+ResourceCenter.getCmdString( ResourceCenter.CMD_GET_DHCP_OPT )+" : "+
                    dhcpdb.getErrorMessage()
                );
                ret = false;
            }
            
            cnt++;
            status = SanBootView.res.getString("common.loadstatus.subnet");
            if( !dhcpdb.getSubnetFromDhcp() ){
                errBuf.append(
                    "\n"+ResourceCenter.getCmdString( ResourceCenter.CMD_GET_SUBNET )+" : "+
                    dhcpdb.getErrorMessage()
                );
                ret = false;
            }
            
            cnt++;
            status = SanBootView.res.getString("common.loadstatus.ibootsrv");
            if( !dhcpdb.getIBootSrvFromDhcp() ){
                errBuf.append(
                    "\n"+ResourceCenter.getCmdString( ResourceCenter.CMD_GET_DHCP_IBOOT )+" : "+
                    dhcpdb.getErrorMessage()
                );
                ret = false;
            }
            
            cnt++;
            status = SanBootView.res.getString("common.loadstatus.clnt");
            if( !dhcpdb.getClientFromDhcp() ){
                errBuf.append(
                    "\n"+ResourceCenter.getCmdString( ResourceCenter.CMD_GET_CLT_DHCP )+" : "+
                    dhcpdb.getErrorMessage()
                );
                ret = false;
            }
            dialog.init1();
            dialog.enableButton( true );
            dhcpdb.setLoginOKFlag( true );
            
        }else{
            dhcpdb.setLoginOKFlag( false );
            dhcpdb.clearDb();
            dialog.init1();
            dialog.enableButton( false );
            errBuf.append( "\n"+ view.initor.errormsg );
            ret = false;
        }
        
        errormsg = SanBootView.res.getString("DhcpDialog.error.initfail") + errBuf.toString();
        return ret;
    }
    
    public String getLoadingStatus(){
        return status;
    }
    
    public int getLoadingProcessVal(){
        return (cnt*100)/5;
    }
     
    public String getInitErrMsg(){
        return errormsg;
    }
    public String getSrvIP(){
        return "";
    }
    public boolean isCrtVG(){
        return true;
    }
}
