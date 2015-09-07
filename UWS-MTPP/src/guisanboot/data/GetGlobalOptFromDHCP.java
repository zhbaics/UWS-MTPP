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
import java.util.regex.*;
import guisanboot.ui.*;

public class GetGlobalOptFromDHCP extends NetworkRunning {
    private DhcpGlobalOpt globalOpt = null;
    
    public void parser(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        
        try{
            String[] list = Pattern.compile("\\s+").split( line, -1 );
            if( list[0].equals("domain") ){
                globalOpt = new DhcpGlobalOpt();
                globalOpt.setDNS( list[1] );
            }else if( list[0].equals("gateway") ){
                globalOpt.setDefGw( list[1] );
            }else if( list[0].equals("os") ){
                globalOpt.setDefStartOS( list[1] );
            }
        }catch(Exception ex){
            globalOpt = null;
        }
    }
    
    public GetGlobalOptFromDHCP( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetGlobalOptFromDHCP( String cmd ){
        super( cmd );
    }
    
    public GetGlobalOptFromDHCP(){
    }
    
    public void clearCache(){
        globalOpt = null;
    }
    
    public boolean realDo(){
SanBootView.log.info( getClass().getName(), "getGlobalOptFromDHCP cmd: "+ getCmdLine() ); 
        try{
            globalOpt = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), "getGlobalOptFromDHCP retcode: "+ getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), "getGlobalOptFromDHCP errmsg: "+ getErrMsg() );             
        }
        return isOk;
    }
    
    public DhcpGlobalOpt getDhcpGlobalOpt(){
        return globalOpt;
    }
    
    public void setDns( String dns ){
        if( globalOpt != null ){
            this.globalOpt.setDNS( dns );
        }
    }
    public void setDefgw( String defgw ){
        if( globalOpt != null ){
            this.globalOpt.setDefGw( defgw );
        }
    }
    public void setDefOS( String defos ){
        if( globalOpt != null ){
            this.globalOpt.setDefStartOS( defos );
        }
    }
}
