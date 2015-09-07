/*
 * IsDir.java
 *
 * Created on September 16, 2005, 12:38 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;


/**
 *
 * @author  Administrator
 */
public class IsDir extends NetworkRunning{
    private boolean isDir = false;
    private boolean isFile = false;
    private boolean isNone = false;
    private boolean isUnKnown = false;
    private boolean isOtherErr = false;
    
    public void parser( String line ){
        String str = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ str );         
        if( str.equals("dir") ){
            isDir = true;
        }else if( str.equals("file") ){
            isFile = true;
        }else if( str.equals("none") ){
            isNone = true;
        }else if( str.startsWith("unknown") ){
            isUnKnown = true;
        }else{
            isOtherErr = true;
        }
    }
    
    /** Creates a new instance of IsDir */
    public IsDir( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public IsDir( String cmd ){
        super( cmd );
    }
    
    public boolean getFileStatus(){ 
SanBootView.log.info( getClass().getName(), " get filestatus cmd: "+ getCmdLine() ); 
        try{
            isDir = false;
            isFile = false;
            isNone = false;
            isUnKnown = false;
            isOtherErr = false;
    
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get filestatus retcode: "+ getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get filestatus errmsg: "+ getErrMsg() );             
        }
        return isOk;
    }
    
    // 下面 4 个函数是Agent返回的错误
    public boolean isdir(){
        return isDir;
    }
    
    public boolean isFile(){
        return isFile;
    }
    
    public boolean isNotExist(){
        return isNone;
    }
    
    public boolean isUnKnown(){
        return isUnKnown;
    }
    
    // 这个函数是的server返回的错误，比如agent连接失败等
    public boolean isOther(){
        return isOtherErr;
    }
}
