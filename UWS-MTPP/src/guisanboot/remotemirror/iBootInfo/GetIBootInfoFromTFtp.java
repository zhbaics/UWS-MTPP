/*
 * GetIBootInfoFromTFtp.java
 *
 * Created on Aug 13, 2009, 4:56 PM
 */

package guisanboot.remotemirror.iBootInfo;
import guisanboot.data.*;
import java.io.*;
import java.net.*;
import guisanboot.ui.SanBootView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author  Administrator
 */
public class GetIBootInfoFromTFtp extends NetworkRunning{
    private StringBuffer contents;
    
    /** Creates a new instance of GetIBootInfoFromTFtp */
    public GetIBootInfoFromTFtp( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetIBootInfoFromTFtp( String cmd ){
        super( cmd );
    }
    
    public GetIBootInfoFromTFtp( Socket socket ) throws IOException {
        super( socket );
    }
    
    public void parser(String line){
        if( line.equals("") || line.startsWith("#") ){
            return;
        }
  
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 ); 
        contents.append( s1+"\n" );
    }
    
    public boolean getContent(){
SanBootView.log.info( getClass().getName(), " get ibootInfo config cmd: " + this.getCmdLine() );
        contents = new StringBuffer();
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get ibootInfo config retcode: " + this.getRetCode()  );     
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get ibootInfo config errmsg: " + this.getErrMsg()  );               
        }
        return isOk;
    }
    
    public StringBuffer getIBootInfoContents(){
        return contents;
    }
}
