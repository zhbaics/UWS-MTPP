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
public class GetActiveLink extends NetworkRunning implements TreeProcessable{
    private SanBootView view;
    private BrowserTreeNode fNode;
    private boolean addTable;
    private boolean addTree;
    private GeneralProcessEventForSanBoot processEvent;
    private ActiveLink curLink = null;
 
    public void parser(String line){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName()," =======> "+ line );          
        String s1 = line.trim();
        SplitString sp = new SplitString( s1 );
        String link_ip = sp.getNextToken();
        String link_initor = sp.getNextToken();
        int tid= -1;
        String link_tid = sp.getNextToken();
        try{
            tid = Integer.parseInt( link_tid ); 
        }catch(Exception ex){
            tid = -1;
        }
        String link_send = sp.getNextToken();
        String link_rec = sp.getNextToken();
        String link_pid = sp.getNextToken();
        
        curLink = new ActiveLink(
            link_ip,
            link_initor,
            tid,
            link_send,
            link_rec,
            link_pid
        );
       
        if( addTable ){
            processEvent.insertSomethingToTable( curLink );
        }   
    }
    
    public GetActiveLink(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetActiveLink(String cmd){
        super( cmd );
    }
    
    public boolean realDo(){
SanBootView.log.info(getClass().getName(),"get Active link cmd: " + getCmdLine() );       
        try{
            curLink = null;
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info(getClass().getName(),"get Active link retcode: " + getRetCode() );  
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error(getClass().getName(),"get Active link errmsg: " + getErrMsg() );              
        }
        return isOk;
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
