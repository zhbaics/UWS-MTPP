/*
 * ViewTextFile.java
 *
 * Created on March 31, 2005, 5:33 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;

/**
 *
 * @author  Administrator
 */
public class ViewTextFile extends NetworkRunning{
    StringBuffer buf;
    
    /** Creates a new instance of ViewTextFile */
    public ViewTextFile( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public ViewTextFile( String cmd ){
        super( cmd );
    }
    
    public ViewTextFile(Socket socket ) throws IOException{
        super( socket );
    }
    
    public void parser( String line ){
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        buf.append( line+"\n" );
    }
    
    public boolean viewFile(){
SanBootView.log.info(this.getClass().getName(), "viewFile cmd: "+ getCmdLine()   );        
        try{
            buf = new StringBuffer();
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info(this.getClass().getName(), "viewFile cmd retcode: "+ getRetCode()   );         
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error(this.getClass().getName(), "viewFile cmd errmsg: "+ getErrMsg()   );               
        }
        return isOk;
    }
    
    public StringBuffer getContentBuffer(){
        return buf;
    }
}
