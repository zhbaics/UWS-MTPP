/*
 * GetAgentInfo.java
 *
 * Created on January 28, 2005, 10:35 AM
 */

package guisanboot.data;

import java.io.*;
import java.net.*;

/**
 *
 * @author  Administrator
 */
public class GetHostInfo extends NetworkRunning {
    public final static String OS_NAME= "OS_NAME";
    public final static String OS_RELEASE = "OS_RELEASE";
    public final static String HOSTNAME = "HOSTNAME";
    public final static String ARCH   ="ARCHITECTURE";
    public final static String HOSTID = "HOSTID";
    
    private String os_name = "" ;
    private String os_release = "" ;
    private String hostname = "" ;
    private String machine = "" ;
    private String hostid = "";
    
    /** Creates a new instance of GetHostInfo */
    public GetHostInfo(String cmd, Socket socket) throws IOException {
        super( cmd,socket );
    }
    
    public void parser(String line){
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
        
        if( index>0){
            String value = s1.substring( index+1 ).trim();
            String key = s1.substring(0,index);
//System.out.println("####: "+key);
//System.out.println("@@@@@: "+value);
          
            if( key.equals( OS_NAME ) ){
                os_name = value;
            }else if( key.equals(OS_RELEASE)){
                os_release = value;
            }else if( key.equals(HOSTNAME)){
                hostname = value;
            }else if( key.equals(ARCH) ){
                machine = value;
            }else if( key.equals(HOSTID)){
                hostid = value; 
            }
        }
    }
    
    public void getAgentInfo(){
        try{
            run();
        }catch(Exception ex){
            setErrMsg( this.getExceptionErrMsg( ex ) );
            setRetCode( this.getExceptionErrCode( ex ) );
        }
    }
    
    public String getOSName(){
        return os_name;
    }
    public String getOSRelease(){
        return os_release;
    }
    public String getHostName(){
        return hostname;
    }
    public String getMachine(){
        return machine;
    }
    public String getHostId(){
        return hostid;
    }
}
