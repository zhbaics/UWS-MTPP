/*
 * GetErrorLog.java
 *
 * Created on 2008/8/11, PM�1:12
 *
 */

package guisanboot.datadup.cmd;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import java.io.*;
import java.net.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author Administrator
 */
public class GetErrorLog extends NetworkRunning{
    StringBuffer buf;
    
    public void parser( String line ){
SanBootView.log.debug( getClass().getName()," =====> " + line );    
        buf.append( line+"\n" );
    }
     
    /** Creates a new instance of GetErrorLog */
    public GetErrorLog(  String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetErrorLog( String cmd ){
        super( cmd );
    }
    
    public GetErrorLog( Socket socket ) throws IOException{
        super( socket );
    }
    
    public boolean viewErrorLog(){
SanBootView.log.info( getClass().getName()," view error log cmd: "+ this.getCmdLine() );           
        try{
            buf = new StringBuffer();
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," view error log cmd retcode: "+ this.getRetCode() );    
        
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," view error log cmd errmsg: "+ this.getErrMsg() );            
        }
        return isOk;
    }
    
    public StringBuffer getContentBuffer(){
        return buf;
    }
}
