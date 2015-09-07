/*
 * Login.java
 *
 * Created on April 2, 2005, 4:24 PM
 */

package guisanboot.data;

import java.io.*;
import java.net.*;

import guisanboot.res.*;
import guisanboot.ui.*;
import guisanboot.exception.*;

/**
 *
 * @author  Administrator
 */
public class Login extends AbstractNetworkRunning{
    private String user;
    private String passwd;
    
    /** Creates a new instance of Login */
    public Login() {
    }
    
    public Login( Socket socket ) throws IOException{
        super( socket );
    }
    
    public void run() throws IOException,
                                BadMagicException,
                                BadPackageLenException,
                                BadVersionException
    {
        byte[] pader;
        
        synchronized( ResourceCenter.comLock ){
            // 组装包头
            int userlen = user.getBytes().length;
            int passwdlen = passwd.getBytes().length;

            assembleMessageHeader( 
                ResourceCenter.C_S_LOGIN, 
                0, 
                64,
                ResourceCenter.C_S_LOGIN_STATUS
            );

            if( userlen < 32 ){
                pader = new byte[ 32-userlen-1 ];
                String newuser = user + new String( pader );
                out.writeString( newuser );
            }else{
                out.writeString( user );
            }

            if( passwdlen < 32 ){
                pader = new byte[ 32-passwdlen-1 ];
                String newpasswd = passwd + new String( pader );
                out.writeString( newpasswd );
            }else{
                out.writeString( passwd );
            }

            out.flush();

            // 开始接受并分析返回包
            checkMessageHeader();
        }
        
        // 检查返回的状态
        retCode = status - 7021 ;
        
        if( retCode == 1 ){
            errMsg = SanBootView.res.getString("common.error.loginerr");
        }else if( retCode == 2 ){
            errMsg = SanBootView.res.getString("common.error.loginLimit");
        }
    }
       
    public boolean login( String _user,String _passwd ){
        user = _user;
        passwd = _passwd;
        
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        
        return ( getRetCode() == AbstractNetworkRunning.OK );
    }
}
