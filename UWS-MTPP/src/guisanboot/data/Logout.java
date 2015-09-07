/*
 * Logout.java
 *
 * Created on April 2, 2005, 4:25 PM
 */

package guisanboot.data;

import java.io.*;
import java.net.*;
import guisanboot.res.*;
import guisanboot.exception.*;

/**
 *
 * @author  Administrator
 */
public class Logout extends AbstractNetworkRunning {
    /** Creates a new instance of Logout */
    public Logout() {
        super();
    }
   
    public Logout( Socket socket ) throws IOException{
        super( socket );
    }
    
    public void run() throws IOException,
                                BadMagicException,
                                BadPackageLenException,
                                BadVersionException
    {
        
        synchronized( ResourceCenter.comLock ){
            // 组装包头
            assembleMessageHeader( 
                ResourceCenter.C_S_LOGOUT,  
                0, 
                0,
                ResourceCenter.C_S_LOGOUT_STATUS
            ); 

            out.flush();
        }
    }
    
    public boolean logout(){
        try{
            run();
        }catch( Exception ex ){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        
        return ( getRetCode() == AbstractNetworkRunning.OK );
    }
}
