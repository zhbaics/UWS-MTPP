package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */
import java.io.*;
import java.net.*;
import guisanboot.ui.*;

public class Get3rdDhcpInfo extends NetworkRunning {
    private String ip="";
    private String next_server="";
    
    public void parser(String line)  {
        // ip:40.40.1.132 nextsrv:40.40.1.211
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );      
        String tmp = line.trim();
        int indx = tmp.indexOf("nextsrv");
        if( indx >0 ){
            ip = tmp.substring(3, indx).trim();
            next_server=tmp.substring( indx+8 );
        }
    }

    public Get3rdDhcpInfo( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public Get3rdDhcpInfo( String cmd ){
        super( cmd );
    }

    public boolean realDo(){        
SanBootView.log.info( getClass().getName(), " get the 3rd dhcp info cmd: "+ getCmdLine() );         
        try{            
            ip ="";
            next_server="";
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get the 3rd dhcp info retcode: "+ getRetCode() );    
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get the 3rd dhcp info errmsg: "+ getErrMsg() );              
        }
        return isOk;
    }
    
    public String getIP(){
        return ip;
    }
    
    public String getNextServer(){
        return next_server;
    }
}
