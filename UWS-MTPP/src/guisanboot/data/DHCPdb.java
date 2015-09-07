/*
 * DHCPdb.java
 *
 * Created on 2007/12/12,�PM�8:26
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class DHCPdb {
    GetTargetServerName getTargetSrvName;
    GetClientFromDHCP getClnt;
    GetGlobalOptFromDHCP getOpt;
    GetIBootSrvFromDHCP getIbootSrv;
    GetSubnetFromDHCP getSubnet;
    Logout logout;
    Login login;
    SyncOpCmdWithNoOutput dhcpOp;
    ViewTextFile viewFile;
    SendFileToSrv sendFileToSrv;
    GetServerNetInterface getIf;
    NetworkRunningWithoutOutput touchFile;
    NetworkRunningWithoutOutput delFile;
    NetworkRunningWithoutOutput mvFile;
    private boolean isLoginOk = true;
    private boolean isDHCPStart = false;
    
    /** Creates a new instance of DHCPdb */
    public DHCPdb() {
        getTargetSrvName = new GetTargetServerName(
            ResourceCenter.getCmd( ResourceCenter.CMD_GET_HOST_NAME )
        );
        getClnt = new GetClientFromDHCP();
        getOpt = new GetGlobalOptFromDHCP();
        getIbootSrv = new GetIBootSrvFromDHCP();
        getSubnet = new GetSubnetFromDHCP();
        dhcpOp= new SyncOpCmdWithNoOutput();
        viewFile = new ViewTextFile(
            ResourceCenter.getCmd( ResourceCenter.CMD_CAT )
        );
        getIf = new GetServerNetInterface("");
        sendFileToSrv = new SendFileToSrv( );
        touchFile = new NetworkRunningWithoutOutput();
        delFile = new NetworkRunningWithoutOutput();
        mvFile = new NetworkRunningWithoutOutput();
        logout = new Logout();
        login = new Login();
    }
    
    String errorMsg ="";
    public String getErrorMessage(){
        if( errorMsg.equals("") ){
            return SanBootView.res.getString("common.failed");
        }else{
            if( errorMsg.length() > 80 ){
                return errorMsg.substring(0, 76)+"....";
            }else{
                return errorMsg;
            }
        }
    }
    
    public boolean isLoginOK(){
        return isLoginOk;
    }
    public void setLoginOKFlag( boolean val ){
        isLoginOk = val;
    }
    
    public boolean isDHCPStart(){
        return this.isDHCPStart;
    }
    public void setDHCPStartFlag( boolean val ){
        isDHCPStart = val;
    }
    
    int errorCode;
    public int getErrorCode(){
        return errorCode;
    }
    
    int newId;
    public int getNewId(){
        return newId;
    }
    
    public boolean finished( AbstractNetworkRunning running ){
        boolean ret = ( running.getRetCode() == AbstractNetworkRunning.OK );
        if( !ret ){
            this.errorMsg = running.getErrMsg();
            this.errorCode = running.getRetCode();
        }
        return ret;
    }
  
    public void recordException( AbstractNetworkRunning running,Exception ex ){
        running.setExceptionErrMsg( ex );
        running.setExceptionRetCode( ex );
    }
    
    public void setSocket( Socket socket ){
        try{
            getTargetSrvName.setSocket( socket );
            getClnt.setSocket( socket );
            getOpt.setSocket( socket );
            getIbootSrv.setSocket( socket );
            getSubnet.setSocket( socket );
            dhcpOp.setSocket( socket );
            delFile.setSocket( socket );
            mvFile.setSocket( socket );
            viewFile.setSocket( socket );
            sendFileToSrv.setSocket( socket );
            touchFile.setSocket( socket );
            getIf.setSocket( socket );
            login.setSocket( socket );
            logout.setSocket( socket );
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void closeStreamOnCurSocket(){
        try{
            getTargetSrvName.closeSocketStream();
            getClnt.closeSocketStream(  );
            getOpt.closeSocketStream(  );
            getIbootSrv.closeSocketStream(  );
            getSubnet.closeSocketStream(  );
            dhcpOp.closeSocketStream();
            delFile.closeSocketStream();
            mvFile.closeSocketStream();
            viewFile.closeSocketStream();
            sendFileToSrv.closeSocketStream();
            touchFile.closeSocketStream();
            getIf.closeSocketStream();
            login.closeSocketStream();
            logout.closeSocketStream();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void clearDb(){
        getClnt.clearCache();
        getOpt.clearCache();
        getIbootSrv.clearCache();
        getSubnet.clearCache();
        getIf.clearCache();
    }
    
    public boolean delFile( String file ){
        delFile.setCmdLine( 
            ResourceCenter.getCmd( ResourceCenter.CMD_DEL_FILE ) + file
        );
SanBootView.log.info(this.getClass().getName(), " del file cmd: " + delFile.getCmdLine()  );        
        try{
            delFile.run();
        }catch( Exception ex ){
            recordException( delFile,ex );
        }
SanBootView.log.info(this.getClass().getName(), " del file retcode: " + delFile.getRetCode()  );          
        boolean isOk =  finished( delFile );
        if( !isOk ){
SanBootView.log.error(this.getClass().getName(), " del file errmsg: " + delFile.getErrMsg()  );               
        }
        return isOk;
    }
    
    public boolean touchFile( String file ){
        touchFile.setCmdLine(
            ResourceCenter.getCmd( ResourceCenter.CMD_TOUCH_FILE ) + " " + file
        );
SanBootView.log.info(this.getClass().getName(), " touch file cmd: " + touchFile.getCmdLine()  );
        try{
            touchFile.run();
        }catch( Exception ex ){
            recordException( touchFile,ex );
        }
SanBootView.log.info(this.getClass().getName(), " touch file cmd retcode: "+touchFile.getRetCode() );      
        boolean isOk = finished( touchFile );
        if( !isOk ){
SanBootView.log.error(this.getClass().getName(), " touch file cmd errmsg: "+touchFile.getErrMsg() );                
        }
        return isOk;
    }
    
    public boolean moveFile( String src,String dest ){
        mvFile.setCmdLine(
            ResourceCenter.getCmd( ResourceCenter.CMD_MV ) + src+" "+dest
        );
SanBootView.log.info( getClass().getName(), " mv cmd: "+mvFile.getCmdLine() ); 
        try{
            mvFile.run();
        }catch(Exception ex){
            recordException( mvFile, ex );
        }
SanBootView.log.info( getClass().getName(), " mv cmd retcode: "+mvFile.getRetCode() );      
        boolean isOk  = finished( mvFile );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " mv cmd errmsg: "+mvFile.getErrMsg() ) ;           
        }
        return isOk;
    }
    
    public boolean sendFileToServer( String filename,String contents ){
SanBootView.log.info(getClass().getName(), " send this file to server: " + filename ); 
        if( contents.equals("") ){
SanBootView.log.warning( getClass().getName()," Contents to send is null,so assign a string with white space to it.");
            contents = "                         ";
        }
        boolean ret = sendFileToSrv.sendFileToSrv( filename,contents ); 
        if( !ret ){
            errorMsg = sendFileToSrv.getErrMsg();
            errorCode = sendFileToSrv.getRetCode();
        }
SanBootView.log.info(getClass().getName()," send file to server retcode: "+sendFileToSrv.getRetCode() );    
        if( !ret ){
SanBootView.log.error(getClass().getName()," send file to server errmsg: "+sendFileToSrv.getErrMsg() );    
        }
        return ret;
    }
    
    public boolean viewFileContents( String file ){
        viewFile.setCmdLine(
            ResourceCenter.getCmd( ResourceCenter.CMD_CAT )+file
        );
        boolean ret = viewFile.viewFile();
        if( !ret ){
            errorMsg = viewFile.getErrMsg();
            errorCode = viewFile.getRetCode();
        }      
        return ret;
    }
    
    public boolean getUWSIf(){
        getIf.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CAT ) + " /etc/sysconfig/startnet.sh" );
        boolean ret = getIf.updateIflist();
        if( !ret ){
            this.errorMsg = getIf.getErrMsg();
            this.errorCode = getIf.getRetCode();
        }
        return ret;
    }
    
    public ArrayList getAllIf(){
        return getIf.getAll();
    }
    
    public IFConfObj getIF( String ifname ){
        return getIf.getFromVectorOnIfName( ifname );
    }
    
    public IFConfObj getFirstIF(){
        return getIf.getFirstFromCache();
    }
    
    public boolean getGlobalOptFromDhcp(){
        getOpt.setCmdLine(  ResourceCenter.getCmd( ResourceCenter.CMD_GET_DHCP_OPT ) );       
        boolean isOk = getOpt.realDo();
        if( !isOk ){
            this.errorMsg = getOpt.getErrMsg();
            errorCode = getOpt.getRetCode();
        }        
        return isOk;
    }
    public DhcpGlobalOpt getGlobalOpt(){
        return getOpt.getDhcpGlobalOpt();
    }
    public void setGlobalDns( String dns ){
        getOpt.setDns( dns );
    }
    public void setGlobalDefgw( String defgw ){
        getOpt.setDefgw( defgw );
    }
    public void setGlobalDefos( String defos ){
        getOpt.setDefOS( defos );
    }
    public boolean getSubnetFromDhcp(){
        getSubnet.setCmdLine(  ResourceCenter.getCmd( ResourceCenter.CMD_GET_SUBNET ) );         
        boolean isOk = getSubnet.realDo();
        if( !isOk ){
            this.errorMsg = getSubnet.getErrMsg();
            errorCode = getSubnet.getRetCode();
        }        
        return isOk;
    }
    
    public Vector<SubNetInDHCPConf> getSubnetListFromDhcp(){
        return getSubnet.getAllSubnet();
    }
    
    public void addSubnet( SubNetInDHCPConf conf ){
        getSubnet.addSubnet( conf );
    }
    
    public void delSubnet( SubNetInDHCPConf conf ){
        getSubnet.delSubnet( conf );
    }
    
    public boolean getClientFromDhcp(){
        getClnt.setCmdLine(  ResourceCenter.getCmd( ResourceCenter.CMD_GET_CLT_DHCP ) );        
        boolean isOk = getClnt.realDo();
        if( !isOk ){
            this.errorMsg = getClnt.getErrMsg();
            errorCode = getClnt.getRetCode();
        }         
        return isOk;
    }
    public Vector<DhcpClientInfo> getAllClient(){
        return getClnt.getAllClnt();
    }
    public DhcpClientInfo getSelClntOnMac( String mac ){
        return getClnt.getSelClntOnMac( mac );
    }
    public void addClnt( DhcpClientInfo val ){
        getClnt.addClnt( val );
    }
    public void removeClnt( DhcpClientInfo val ){
        getClnt.removeClnt( val );
    }
    public boolean getIBootSrvFromDhcp(){
        getIbootSrv.setCmdLine(  ResourceCenter.getCmd( ResourceCenter.CMD_GET_DHCP_IBOOT ) );
        boolean isOk = getIbootSrv.realDo();
        if( !isOk ){
            this.errorMsg = getIbootSrv.getErrMsg();
            errorCode = getIbootSrv.getRetCode();
        }
        return isOk;
    }
    
    public Vector<DhcpIBootSrv> getIbootSrvListFromDhcp(){
        return getIbootSrv.getAllIBootServer();
    }
    
    public void addIBootSrv( DhcpIBootSrv val ){
        getIbootSrv.addIbootSrv( val );
    }
    public void delIBootSrv( DhcpIBootSrv val ){
        getIbootSrv.delIbootSrv( val );
    }
    public void clearDefFlag(){
        getIbootSrv.clearDefFlag(); 
    }
    public boolean hasDefIbootSrv( String besideIP ){
        return getIbootSrv.hasDefIbootSrv( besideIP ); 
    }
    public boolean dhcpOperation( String cmd ){        
        boolean ret = dhcpOp.execCmd( cmd );       
        if( !ret ){
            errorMsg  = dhcpOp.getErrMsg();
            errorCode = dhcpOp.getRetCode();
        }
        return ret;
    }
    
    public boolean logout(){
        boolean ret = logout.logout();
        if( !ret ){
            errorMsg = logout.getErrMsg();
        }
        return ret;
    }
  
    public boolean login( String user,String pass ){
        boolean ret = login.login( user,pass );
        if( !ret ){
            errorMsg = login.getErrMsg();
        }
        return ret;
    }
    
    public String getHostName(){
        return getTargetSrvName.getTargetSrvName();
    }
}
