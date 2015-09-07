/*
 * GetAgentInfo.java
 *
 * Created on January 28, 2005, 10:35 AM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;

/**
 *
 * @author  Administrator
 */
public class GetAgentInfo extends NetworkRunning {
    public final static String OS_NAME = "OS_NAME";
    public final static String OS_RELEASE = "OS_RELEASE";
    public final static String OS_BIT = "OS_BIT";
    public final static String HOSTNAME = "HOSTNAME";
    public final static String ARCH   = "ARCHITECTURE";
    public final static String HOSTID = "HOSTID";
    
    private String os_name = "" ;
    private String os_release = "" ;
    private String os_bit = "";
    private String hostname = "" ;
    private String machine = "" ;
    private String hostid = "";

    private String uuid = "";
    private String ip="";
    private String pri_ip="";
    private String vip="";
    private int port = 0;
    private int mtpp_port = 0;
    
    /** Creates a new instance of GetAgentInfo */
    public GetAgentInfo( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }

    public void parser(String line ){
        if( this.isMTPPCmd() ){
            parserForMTPP( line );
        }else{
            parserForCMDP( line );
        }
    }

    public void parserForMTPP(String line){
SanBootView.log.debug(getClass().getName(),"####: " + line );
        String s1 = line.trim();

        int index = s1.indexOf("=");
        
        if( index>0){
          String value = s1.substring( index+1 ).trim();
          String key = s1.substring( 0,index );
          
          if( key.equals( OS_NAME ) ){
            os_name = value;
          }else if( key.equals(OS_RELEASE)){
            os_release = value;
          }else if( key.equals(OS_BIT ) ){
            os_bit = value;
          }else if( key.equals(HOSTNAME)){
            this.hostname = value;
          }else if( key.equals(ARCH) ){
            this.machine = value;
          }else if( key.equals(HOSTID)){
            this.hostid = value; 
          }
        }
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line );
        }else{
            this.errMsg += line +"\n";
        }
    }
    
    public boolean getAgentInfo(){
SanBootView.log.info(getClass().getName()," get agt cmd: "+ this.getCmdLine() ); 
        try{
            run();
        }catch(Exception ex){
            setErrMsg( this.getExceptionErrMsg( ex ) );
            setRetCode( this.getExceptionErrCode( ex ) );
        }
SanBootView.log.info(getClass().getName()," get agt cmd retcode: "+ this.getRetCode() ); 
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error(getClass().getName()," get agt cmd errmsg: "+ this.getErrMsg() );             
        }
        return isOk;
    }
    
    public String getOSName(){
        if( os_name.toUpperCase().startsWith("WIN") ){
            return "Win"+os_release;
        }else{
            return os_name;
        }
    }
    public boolean isWin(){
        return os_name.toUpperCase().startsWith("WIN");
    }
     
    public String getOSRelease(){
        return os_release.toUpperCase();
    }
    public String getHostName(){
        return hostname;
    }
    public String getMachine(){
        if( !this.os_bit.equals("") ){
            if( this.os_bit.equals("64") ){
                return machine+"_64";
            }else{
                return machine;
            }
        }else{
            return machine;
        }
    }
    public String getHostId(){
        return hostid;
    }
    public String getBit(){
        return this.os_bit;
    }

    public String getUUID(){
        return this.uuid;
    }
    public void setUUID( String val ){
        this.uuid = val;
    }

    public String getIP(){
        return this.ip;
    }
    public void setIP( String val ){
        this.ip = val;
    }

    public int getPort(){
        return this.port;
    }
    public void setPort( int val ){
        this.port = val;
    }

    public int getMtppPort(){
        return this.mtpp_port;
    }
    public void setMtppPort( int val ){
        this.mtpp_port = val;
    }

    /**
     * @return the pri_ip
     */
    public String getPri_ip() {
        return pri_ip;
    }

    /**
     * @param pri_ip the pri_ip to set
     */
    public void setPri_ip(String pri_ip) {
        this.pri_ip = pri_ip;
    }

    /**
     * @return the vip
     */
    public String getVip() {
        return vip;
    }

    /**
     * @param vip the vip to set
     */
    public void setVip(String vip) {
        this.vip = vip;
    }
}
