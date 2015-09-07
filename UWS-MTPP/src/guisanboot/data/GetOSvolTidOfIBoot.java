/*
 * GetOSvolTidOfIBoot.java
 *
 * Created on 3 18, 2008, 10:43 AM
 */

package guisanboot.data;

import java.net.*;
import java.io.*;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Odysys
 */
public class GetOSvolTidOfIBoot extends NetworkRunning{
    protected int tid = -1;
    
    public void parser( String line ){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );            
        try{
            tid = Integer.parseInt( s1 );
        }catch(Exception ex){}
    }
     
    /** Creates a new instance of AddCmdNetworkRunning */
    public GetOSvolTidOfIBoot() {
    }
    
    public GetOSvolTidOfIBoot(Socket socket )throws IOException{
        super( socket );
    }
    
    public GetOSvolTidOfIBoot(String cmdLine,Socket socket) throws IOException{
        super( cmdLine,socket );
    }
    
    public boolean realDo( String ip ){
        setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_GET_OSVOLTID ) + ip );
SanBootView.log.info( getClass().getName(), " get os vol tid of iboot cmd : "+ this.getCmdLine()  );     
        try{
            tid = -1;         
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get os vol tid of iboot ret : "+ this.getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get os vol tid of iboot errmsg : "+ this.getErrMsg() );             
        }
        return isOk;
    }
    
    public int getTargetID(){
        return tid;
    }
}
