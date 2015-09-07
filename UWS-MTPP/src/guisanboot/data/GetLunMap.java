/*
 * GetBootHost.java
 *
 * Created on July 8, 2005, 8:15 PM
 */

package guisanboot.data;

import java.util.Vector;
import java.net.*;
import java.io.*;

import guisanboot.ui.*;
import mylib.UI.*;
import mylib.tool.*;

/**
 *
 * @author  Administrator
 */
public class GetLunMap extends NetworkRunning implements TreeProcessable{
    private SanBootView view;
    private BrowserTreeNode fNode;
    private boolean addTable=false;
    private boolean addTree=false;
    private GeneralProcessEventForSanBoot processEvent;
    private LunMap curLM = null;
 
    public void parser(String line){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );  

        String s1 = line.trim();
        SplitString sp = new SplitString( s1 );
        String lm_ip = sp.getNextToken();
        String lm_mask = sp.getNextToken();
        String lm_right = sp.getNextToken();
        String lm_srvusr = sp.getNextToken();
        String lm_clntusr = sp.getNextToken();
        
        curLM= new LunMap(
            lm_ip,
            lm_mask,
            lm_right,
            lm_srvusr,
            "",
            lm_clntusr,
            ""
        );
       
        if( addTable ){
            processEvent.insertSomethingToTable( curLM );
        }
    }
    
    public GetLunMap(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetLunMap(String cmd){
        super( cmd );
    }
    
    public boolean realDo(){
SanBootView.log.info( getClass().getName(), "get lunmap cmd: " + getCmdLine() );             
        try{
            curLM = null;
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), "get lunmap retcode: " + getRetCode() );  
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), "get lunmap errmsg: " + getErrMsg() );              
        }
        return isOk;
    }
    
    public boolean puesdoUpdataActlink(){
        curLM = null;
        
        curLM= new LunMap(
            "20.20.1.62",
            "255.255.255.0",
            "rw",
            "srv_user1",
            "", 
            "clnt_user1",
            ""
        );
       
        if( addTable ){
            processEvent.insertSomethingToTable( curLM );
        }
        
        curLM = new LunMap(
            "20.20.1.60",
            "255.255.255.0",
            "--",
            "srv_user2",
            "",
            "clnt_user2",
            ""
        );
        if( addTable ){
            processEvent.insertSomethingToTable( curLM );
        }
        
        return true;
    }
    
    public void setFatherTreeNode( BrowserTreeNode _fNode){
        fNode = _fNode;
    }
    
    public void setProcessEvent( GeneralProcessEventForSanBoot _processEvent ){
        processEvent = _processEvent;
    }
    
    public void setAddTableFlag( boolean _addTable ){
        addTable = _addTable;
    }
    
    public void setAddTreeFlag( boolean _addTree ){
        addTree = _addTree;
    }
}
