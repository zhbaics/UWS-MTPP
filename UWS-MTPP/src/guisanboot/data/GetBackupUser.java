/*
 * GetBackupUser.java
 *
 * Created on April 2, 2005, 5:33 AM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.util.Vector;
import java.net.*;
import java.io.*;
import guisanboot.audit.data.*;


/**
 *
 * @author  Administrator
 */
public class GetBackupUser extends NetworkRunning {
    private Vector<BackupUser> userList = new Vector<BackupUser>();
    private BackupUser curUser = null;
    
    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName()," ======> "+ s1 );

        int index = s1.indexOf("=");
        if( index>0 ){
            String value = s1.substring( index+1 );

            if( s1.startsWith( BackupUser.BAKUSER_ID ) ){
                try{
                    long id = Long.parseLong( value );
                    curUser.setID( id );
                }catch(Exception ex){
                    curUser.setID( -1L );
                }
            }else if( s1.startsWith( BackupUser.BAKUSER_NAME ) ){
                curUser.setUserName( value );
            }else if( s1.startsWith( BackupUser.BAKUSER_PASS )){
                curUser.setPasswd( value );
            }else if( s1.startsWith( BackupUser.BAKUSER_PRIV )){
                try{
                    int right = Integer.parseInt( value );
                    curUser.setRight( right );
                }catch(Exception ex){
                    curUser.setRight( 0 );
                }
                userList.addElement( curUser );
            }
        }else{
            if( s1.startsWith( BackupUser.BAKUSER_RECFLAG ) ){
                curUser = new BackupUser( );
            }
        }
    }
    
    /** Creates a new instance of GetBackupUser */
    public GetBackupUser( String cmd,Socket socket ) throws IOException{
        super( cmd ,socket );
    }
    
    public GetBackupUser( String cmd ){
        super( cmd );
    }
    
    public boolean updateBakUser() {
SanBootView.log.info(getClass().getName()," get bak user cmd: "+ this.getCmdLine() );        
        try{
            userList.removeAllElements();
            curUser = null;
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info(getClass().getName()," get bak user cmd retcode: "+ this.getRetCode() );       
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error(getClass().getName()," get bak user cmd errmsg: "+ this.getErrMsg() );                   
        }
        return isOk;
    }
    
    public void addBakUserToVector(BackupUser user){
        userList.addElement( user );
    }

    public void removeBakUserFromVector(BackupUser user){
        userList.removeElement( user );
    }
    
    public BackupUser getBakUserFromVectorOnName( String name ){
        int size = userList.size();
        for( int i=0;i<size;i++){
            BackupUser user = userList.elementAt(i);
            if( user.getUserName().equals( name ) )
                return user;
        }
        
        return null;
    }

    public BackupUser getBakUserFromVectorOnID( int id ){
        int size = userList.size();
        for( int i=0;i<size;i++){
            BackupUser user = userList.elementAt(i);
            if( user.getID() == id )
                return user;
        }

        return null;
    }

    public boolean isLastAdminUser( String name ){
        int size = userList.size();
        for( int i=0;i<size;i++ ){
            BackupUser user = userList.elementAt(i);
            if( user.getUserName().equals( name ) ){
                continue;
            }else{
                if( user.isAdminRight() ){
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public Vector getAllBackupUser(){
        Vector<BackupUser> list  = new Vector<BackupUser>();
        int size = userList.size();
        for( int i=0;i<size;i++ ){
            list.addElement( userList.elementAt(i) );
        }
        return list;
    }
}
