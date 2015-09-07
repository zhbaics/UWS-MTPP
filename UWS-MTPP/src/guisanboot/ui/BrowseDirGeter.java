/*
 * BrowseSrvDirGeter.java
 *
 * Created on April 6, 2005, 11:03 AM
 */

package guisanboot.ui;

import javax.swing.*;
import java.net.*;

import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class BrowseDirGeter extends Thread implements Threadable{
    private String beginPath;
    private DefaultListModel model;
    private BrowseDirDialog diag;
    private Socket socket;
    private boolean overed = false;
    private SuspendListDir  listDir;
    private boolean yield = false;
    private int nestNum = 0;
    private boolean onlyDir = true;
    private long agentId = -1L;
    private boolean isSyncNode = true; // 是获取sync node上的目录，还是restore agent上的目录
    private boolean filterFatherPath = false;// 是否过滤掉".."目录
    
    public BrowseDirGeter(
        String beginPath, 
        DefaultListModel model, 
        BrowseDirDialog diag, 
        Socket socket, 
        boolean yield, 
        int nestNum, 
        boolean onlyDir
    ){
        this( beginPath,model,diag,socket,yield,nestNum,onlyDir,-1L,false,false );
    }
    
    /** Creates a new instance of BrowseSrvDirGeter */
    public BrowseDirGeter(
        String beginPath, 
        DefaultListModel model, 
        BrowseDirDialog diag, 
        Socket socket, 
        boolean yield, 
        int nestNum, 
        boolean onlyDir,
        long agentId
    ) {
        this( beginPath,model,diag,socket,yield,nestNum,onlyDir,agentId, true,false );
    }
    
    /** Creates a new instance of BrowseSrvDirGeter */
    public BrowseDirGeter(
        String _beginPath, 
        DefaultListModel _model, 
        BrowseDirDialog _diag, 
        Socket _socket, 
        boolean _yield, 
        int _nestNum, 
        boolean _onlyDir,
        long _agentId,
        boolean _isSyncNode,
        boolean _filterFatherPath
    ) {
        beginPath = _beginPath;
        model = _model;
        diag = _diag;
        yield = _yield;
        socket = _socket;
        nestNum = _nestNum;
        onlyDir = _onlyDir;
        agentId = _agentId;
        isSyncNode = _isSyncNode;
        filterFatherPath = _filterFatherPath;
    }
    
    Runnable setTF1Enabled = new Runnable(){
        public void run(){
            diag.setEnabledTF1();
        }
    };
    
    public void run(){
        try{
            listDir = new SuspendListDir(
                ResourceCenter.getCmd( ResourceCenter.CMD_NORMAL_LIST ),
                socket,
                yield,
                nestNum,
                onlyDir,
                agentId,
                isSyncNode,
                filterFatherPath
            );
            listDir.setListModel( model );

            listDir.updateFileList( beginPath );    
        }catch(InterruptedException ex1){
            ex1.printStackTrace();
        }catch(Exception ex2){
            listDir.setExceptionErrMsg( ex2);
            listDir.setExceptionRetCode( ex2);
        }

        if( listDir.getRetCode() != AbstractNetworkRunning.OK ){
            JOptionPane.showMessageDialog( diag,
                SanBootView.res.getString("common.error.getsrvdir")+" : "+
                listDir.getErrMsg()
            );
        }
        
        try{
            SwingUtilities.invokeAndWait( setTF1Enabled );
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        setOver(true);
    }
    
    public void setDestroyFlag(boolean val){
        listDir.setDestroy( val );
    }
    
    public synchronized boolean isOver(){
        return overed;
    }

    public synchronized void setOver(boolean val){
        overed = val;
    }
    
    public void startRun(){
        this.start();
    }
}
