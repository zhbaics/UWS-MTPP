/*
 * GetPXEInfoFromTFtp.java
 *
 * Created on June 24, 2009, 4:56 PM
 */

package guisanboot.data;
import java.io.*;
import java.net.*;
import guisanboot.ui.SanBootView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author  Administrator
 */
public class GetPXEInfoFromTFtp extends NetworkRunning{
    private StringBuffer contents;
    
    /** Creates a new instance of GetDhcpConfig */
    public GetPXEInfoFromTFtp( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetPXEInfoFromTFtp( String cmd ){
        super( cmd );
    }
    
    public GetPXEInfoFromTFtp( Socket socket ) throws IOException {
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
SanBootView.log.info( getClass().getName(), " get pxe config cmd: " + this.getCmdLine() );
        contents = new StringBuffer();
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get pxe config retcode: " + this.getRetCode()  );     
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get pxe config errmsg: " + this.getErrMsg()  );               
        }
        return isOk;
    }
    
    public String replaceMAC( String oldMac,String newMac ){
        Pattern pattern = Pattern.compile( oldMac );
        Matcher matcher = pattern.matcher( contents.toString() );
        return matcher.replaceAll( newMac );
    }
    
    public String relaceTftp( String contents,String newTftp ){
        int indx1 = contents.indexOf("TFTP");
        int indx2 = contents.indexOf("CFG");
        if( indx1>0 && indx2 >0 ){
            String old_tftp = contents.substring( indx1+5,indx2).trim();
            Pattern pattern = Pattern.compile( old_tftp );
            Matcher matcher = pattern.matcher( contents );
            return matcher.replaceAll( newTftp );
        }else{
            return contents; // return original contents; Maybe manual modification is needed in the future.
        }
    }
}
