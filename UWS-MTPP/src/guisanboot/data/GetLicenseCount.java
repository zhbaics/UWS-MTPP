/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class GetLicenseCount extends NetworkRunning{

    private LicenseCount lc= null;
    
    public GetLicenseCount(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetLicenseCount(String cmd){
        super( cmd );
    }
    @Override
    public void parser(String line) {
        String s1 = line.trim();
        int index = s1.indexOf("=");
        if( index>0){
        String value = s1.substring( index+1 ).trim();
        if( s1.startsWith( LicenseCount.SYSTEM) ){
                try{
                    int sys = Integer.parseInt( value );
                    lc.setSystemLicense(sys);
                }catch( Exception ex ){
                    lc.setSystemLicense(0);;
                }
            } else if( s1.startsWith(LicenseCount.DATABASE) ){
                try{
                    int data = Integer.parseInt(value);
                    lc.setDatabaseLicense(data);
                }catch(Exception ex){
                    lc.setDatabaseLicense(0);
                } 
            }else if( s1.startsWith(LicenseCount.COUNT) ){
                    lc = new LicenseCount();
                }
        }
    }
    
    public boolean updateLicenseCount() {
SanBootView.log.info( getClass().getName(), " get LicenseCount cmd: " + getCmdLine()  );        
        try{
            lc = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get LicenseCount retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get LicenseCount errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
    
    public boolean hasSystemLicense(){
        boolean ret = false;
        if(lc != null){
            if(lc.getSystemLicense() != 0) ret = true;
        }
        return ret;
    }
    
    public boolean hasDatabaseLicense(){
        boolean ret = false;
        if(lc != null){
            if(lc.getDatabaseLicense() != 0) ret = true;
        }
        return ret;
    }
}
