/*
 * NotParseOutputCmd.java
 *
 * Created on Aug 6, 2008, 10:07 AM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;
import mylib.tool.*;

/**
 *
 * @author  Administrator
 */
public class NotParseOutputCmd extends NetworkRunning {
    protected StringBuffer buf;
    
    /** Creates a new instance of NotParseOutputCmd */
    public NotParseOutputCmd( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public NotParseOutputCmd( String cmd ){
        super( cmd );
    }
    
    public NotParseOutputCmd( Socket socket ) throws IOException{
        super( socket );
    }
    
    public void parser(String line){
        if( line.equals("") ) return;
SanBootView.log.debug( getClass().getName(),"========> "+ line );
        buf.append( line+"\n" );
    }
    
    public boolean exec(){      
        try{
            buf = new StringBuffer();
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        return ( getRetCode() == AbstractNetworkRunning.OK );
    }
    
    public String getExecResult(){
        return buf.toString();
    }
}
