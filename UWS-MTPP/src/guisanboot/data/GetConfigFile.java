/*
 * GetConfigFile.java
 *
 * Created on March 28, 2005, 4:56 PM
 */

package guisanboot.data;
import java.io.*;
import java.net.*;

import guisanboot.res.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class GetConfigFile extends NetworkRunning{
    /** Creates a new instance of GetConfigFile */
    public GetConfigFile( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetConfigFile( String cmd ){
        super( cmd );
    }
    
    public GetConfigFile( Socket socket ) throws IOException {
        super( socket );
    }
    
    public void parser(String line){
        if( line.equals("") || line.startsWith("#") ){
            return;
        }
  
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 ); 
        int index = s1.indexOf("=");
        if( index>0 ){
            String val = s1.substring(index+1).trim();
            if( val == null ) return;
                
            if( s1.startsWith(ResourceCenter.CLI_CONF_INSTALLPATH) ){    
                ResourceCenter.CMDBASE = val;
                ResourceCenter.PROFILE_DIR = val+"/server/profile/";
                ResourceCenter.TMP_DIR = val+"/server/tmp/";
                ResourceCenter.MDB_DIR = val +"/server/db";
                ResourceCenter.BIN_DIR = val +"/server/bin/";
                ResourceCenter.SNAP_DIR = val +"/vsnap/";
                ResourceCenter.OUTPUT_DIR = val+"/server/output/";
                ResourceCenter.CLT_IP_CONF = val+"/server/conf/";
            }else if( s1.startsWith(ResourceCenter.CLI_CONF_SNAP_DIR )){
                ResourceCenter.SNAP_DIR = val+"/";
            }else if( s1.startsWith(ResourceCenter.CLI_CONF_ODBROOT )){
                ResourceCenter.ODBROOT = val;
            }else if( s1.startsWith( ResourceCenter.CLI_CONF_IDBROOT )){
                ResourceCenter.IDBROOT = val;
            }
        }
    }
    
    public boolean getContent(){
SanBootView.log.info( getClass().getName(), " get config file cmd: " + this.getCmdLine() );        
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get config file retcode: " + this.getRetCode()  );     
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get config file errmsg: " + this.getErrMsg()  );               
        }
        return isOk;
    }
}
