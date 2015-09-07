/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.service;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;

/**
 *
 * @author zourishun
 */
public class GetVMServerInfo extends NetworkRunning{
    
    public static final String VM_server_ip = "server_ip";
    public static final String VM_server_port = "server_port";
    
    private String serverip;
    private int port;
    public GetVMServerInfo(String cmd){
        super(cmd);
    }
    
    @Override
    public void parser(String line) {
        String s1 = line.trim();
        SanBootView.log.debug(getClass().getName(),"#####: " + s1 ); 
        int index = s1.indexOf("=");
        if(index > 0 ){
            String value = s1.substring( index+1 ).trim();
            SanBootView.log.debug(getClass().getName(),"@@@@@: "+value ); 
            if ( s1.startsWith( this.VM_server_ip ) ){
                this.serverip = value;
            } else if( s1.startsWith( this.VM_server_port ) ){
                try{
                    this.port = Integer.parseInt(value);
                } catch( Exception ex){
                    this.port = 2015;
                }
            }
        }
    }
    
    public boolean updateVMServer(){
        SanBootView.log.info( getClass().getName(), " getvmserverinfo cmd: " + getCmdLine()  );        
        try{
            this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_GET_VM_SERVER_INFO ) );
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get vmserverinfo retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get vmserverinfo errmsg: "+getErrMsg() );            
        }
        return isOk;
    }

    public int getPort() {
        return port;
    }

    public String getServerip() {
        return serverip;
    }
    
}
