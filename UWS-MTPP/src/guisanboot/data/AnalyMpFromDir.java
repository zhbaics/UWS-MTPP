/*
 * GetAgentInfo.java
 *
 * Created on January 28, 2005, 10:35 AM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;
import java.util.regex.*;

/**
 *
 * @author  Administrator
 */
public class AnalyMpFromDir extends NetworkRunning {  
    private String src  = "";
    private BindOfFSAndDevNo binder; // fs's mp and its devno 
    
    /** Creates a new instance of GetAgentInfo */
    public AnalyMpFromDir( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    public AnalyMpFromDir(){}
    
    public void parser(String line){
        if( line == null || line.equals("") ) return;
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(), "(AnalyMpFromDir====> " + s1 ); 
        
        String[] list = Pattern.compile("\\s+").split( s1, -1 );
        try{
            src  = list[0];
            binder = new BindOfFSAndDevNo();
            binder.mp = list[3];
            binder.devno = Integer.parseInt( list[4] );
            if( binder.devno == -1 ){
                binder = null;
            }
        }catch(Exception ex){
            binder = null;
        }
    }
    
    public BindOfFSAndDevNo getMpFromDir(){
SanBootView.log.info(getClass().getName(), "getFsMpFromDir cmd: "+getCmdLine() );
        try{
            run();
        }catch(Exception ex){
            setErrMsg( this.getExceptionErrMsg( ex ) );
            setRetCode( this.getExceptionErrCode( ex ) );
        }
        
SanBootView.log.info(getClass().getName(), "getFsMpFromDir cmd retcode: "+this.getRetCode() );
        if( getRetCode() == AbstractNetworkRunning.OK ){
            return binder;
        }else{
SanBootView.log.error(getClass().getName(), "getFsMpFromDir cmd errmsg: "+this.getErrMsg() );    
            return null;
        }
    }   
}
