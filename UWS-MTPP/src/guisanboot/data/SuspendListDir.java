/*
 * SuspendListDir.java
 *
 * Created on April 6, 2005, 11:20 AM
 */

package guisanboot.data;

import java.io.*;
import java.net.*;
import javax.swing.*;

import mylib.tool.*;
import guisanboot.exception.*;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class SuspendListDir extends SuspendNetworkRunning{
    private DefaultListModel model;
    private String curFile;
    private boolean onlyDir = true;
    private long agentId = -1L; 
    private boolean isSyncNode = true; // 是获取sync node上的目录，还是restore agent上的目录
    private boolean filterFatherPath = false; // 是否过滤掉".."目录
    
    /** Creates a new instance of SuspendListDir */
    public SuspendListDir(
        String cmd,
        Socket socket,
        boolean yield,
        int nestNum,
        boolean _onlyDir,
        long _agentId,
        boolean _isSyncNode,
        boolean _filterFatherPath
    ) throws IOException
    {
        super( cmd,socket,yield,nestNum );
        onlyDir = _onlyDir;
        agentId = _agentId;
        isSyncNode = _isSyncNode;
        filterFatherPath = _filterFatherPath;
    }
    
    private void parseSrv( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line ); 
        
        SplitString sp = new SplitString( line );
        String type = sp.getNextToken();
        sp.getNextTokenN(6);
        curFile = sp.getRest();
        
        if( curFile == null  || curFile.equals("")  ) return;
        if( curFile.equals(".") ) return;
        
        if( filterFatherPath ){
            if( curFile.equals("../") ){
                return;
            }
        }
        
        if(  type.startsWith("d") ){
            curFile = curFile+"/";
        }
        
        if( onlyDir ){
            if( type.startsWith("d") ){
                try{
                    SwingUtilities.invokeAndWait( insertModel );
                }catch(Exception ex){
                }
            }
        }else{
            try{
                SwingUtilities.invokeAndWait( insertModel );
            }catch(Exception ex){
            }
        }
    }
    
    private void parseAgent( String line ){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );     
        
        if( s1 != null && !s1.equals("") ){
            if( s1.equals("./") ){
                return;
            }
            
            if( filterFatherPath ){
                if( s1.equals("../") ){
                    return;
                }
            }
            
            curFile = s1;
            
            if( onlyDir ){
                if( isDir( line ) ){
                    try{
                        SwingUtilities.invokeAndWait( insertModel );
                    }catch(Exception ex){}
                }
            }else{
                try{
                    SwingUtilities.invokeAndWait( insertModel );
                }catch(Exception ex){}
            }
        }
    }
    
    private boolean isDir( String line ){
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        if( line == null || line.equals("") ) return false;
        
        int length = line.length();
        if( line.charAt( length-1 ) == '/' )
            return true;
        else
            return false;
    }
      
    @Override public void parserErr( String line ) {
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );     

        if( s1!=null && !s1.equals("") ){
            curFile = s1;
            
            try{
                SwingUtilities.invokeAndWait( insertModel );
            }catch(Exception ex){
            }
        }
    }
     
    public void parser( String line ) throws InterruptedException {
SanBootView.log.debug(getClass().getName(),"========> "+ line ); 
        
        if( line == null || line.equals( "" ) ) return;
        if(  agentId == -1L ){
            parseSrv( line );
        }else{
            parseAgent( line );
        }
    }
    
    public void updateFileList( String beginPath )throws IOException,
                                                            InterruptedException,
                                                            BadMagicException,
                                                            BadVersionException,
                                                            BadPackageLenException
    {
        curFile = null;
        
        if( agentId == -1L ){
            setCmdLine(
                ResourceCenter.getCmd( ResourceCenter.CMD_NORMAL_LIST ) + 
                "\""+beginPath+"\""
            );
        }else{
            if( isSyncNode ){
                setCmdLine(
                    ResourceCenter.getCmd( ResourceCenter.CMD_GET_FILE_LIST )+
                    agentId+" "+
                    "\""+beginPath+"\""
                );
            }else{
                setCmdLine(
                    ResourceCenter.getCmd( ResourceCenter.CMD_GET_FILE_LIST1 )+
                    agentId+" "+
                    "\""+beginPath+"\""
                );
            }
        }
SanBootView.log.info( getClass().getName(), "cmd(SuspendListDir): "+this.getCmdLine() ); 
        run();
    }
    
    Runnable insertModel = new Runnable(){
        public void run(){
            model.addElement( curFile );
        }
    };
    
    public void setListModel(DefaultListModel _model){
        model = _model;
    }
}
