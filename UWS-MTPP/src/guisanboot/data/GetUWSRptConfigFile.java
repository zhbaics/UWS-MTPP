/*
 * GetUWSRptConfigFile.java
 *
 * Created on March 28, 2005, 4:56 PM
 */

package guisanboot.data;
import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;


/**
 *
 * @author  Administrator
 */
public class GetUWSRptConfigFile extends NetworkRunning{
    private UWSRptConf conf;
    
    /** Creates a new instance of GetConfigFile */
    public GetUWSRptConfigFile( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetUWSRptConfigFile( String cmd ){
        super( cmd );
    }
    
    public GetUWSRptConfigFile( Socket socket ) throws IOException {
        super( socket );
    }
    
    public void parser(String line){
        if( line == null || line.equals("") || line.startsWith("#") ){
            return;
        }
  
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 ); 
        int index = s1.indexOf("=");
        if( index>0 ){
            String val = s1.substring(index+1).trim();
            if( val == null ) return;
            
            if( s1.startsWith(UWSRptConf.RPT_MON_UWS ) ){    
                conf.setMonUWSFlag( val.toUpperCase().equals("YES")  );
            }else if(s1.startsWith(UWSRptConf.RPT_SEND_MAIL )){
                conf.setSendMailFlag( val.toUpperCase().equals("YES") );
            }else if( s1.startsWith(UWSRptConf.RPT_MAILTO )){
                conf.setMailTo( val );
            }else if( s1.startsWith( UWSRptConf.RPT_SEND_WHEN_ERR ) ){
                conf.setSendWhenErrOcuredFlag( !val.toUpperCase().equals("YES") );
            }else if( s1.startsWith( UWSRptConf.RPT_SEND_TIME) ){
                conf.setSendTime( val );
            }else if( s1.startsWith( UWSRptConf.RPT_SEND_FREQ) ){
                conf.setSendFreq( val );
            }else if( s1.startsWith( UWSRptConf.RPT_REMAIN_DAYS ) ){
                try{
                    int day = Integer.parseInt( val );
                    conf.setRemainDays( day );
                }catch(Exception ex){
                    conf.setRemainDays( 10 );
                }
            }else if(s1.startsWith( UWSRptConf.RPT_OK_REC_NUM) ){
                try{
                    int num = Integer.parseInt( val );
                    conf.setOkRecNum( num );
                }catch( Exception ex){
                    conf.setOkRecNum( 20 );
                }
            }else if( s1.startsWith( UWSRptConf.RPT_FAILED_REC_NUM ) ){
                try{
                    int num1 = Integer.parseInt( val );
                    conf.setFailedRecNum( num1 );
                }catch( Exception ex){
                    conf.setFailedRecNum( 50 );
                }
            }
        }
    }
    
    public boolean getContent(){
SanBootView.log.info( getClass().getName(), " get uws report confile cmd: "+ getCmdLine() ); 
        try{
            conf = new UWSRptConf();
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get uws report confile retcode: "+ getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get uws report confile errmsg: "+ getErrMsg() );             
        }
        return isOk;
    }
    
    public UWSRptConf getConf(){
        return conf;
    }
}
