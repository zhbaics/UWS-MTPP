/*
 * FileListFetcher.java
 *
 * Created on July 28, 2008, 18:22 PM
 */

package guisanboot.datadup.ui;

import guisanboot.datadup.data.BackupClient;
import guisanboot.datadup.cmd.GetFileList;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import guisanboot.res.*;
import guisanboot.ui.*;

/**
 *
 * @author  Administrator
 */
public class FileListFetcher extends Thread implements FetchFileable{
    private String beginPath;
    private DefaultListModel model;
    private Vector<String> listVector;
    private BrowseBakedFile diag; 
    private Socket socket;
    private BackupClient cli; 
    private boolean overed = false;
    private GetFileList getFileList; 
    private boolean yield = false;
    private int nestNum=0;
    private boolean filterFatherPath = false; // 检查file属于哪个fs
     
    public FileListFetcher(
        BackupClient _cli,
        String _beginPath,
        DefaultListModel _model,
        Vector _listVector,
        BrowseBakedFile _diag,
        Socket _socket,
        boolean _yield,
        int _nestNum,
        boolean _filterFatherPath
    ){
        cli = _cli;
        beginPath = _beginPath;
        model = _model;
        listVector = _listVector;
        diag = _diag;
        yield = _yield;
        socket = _socket;
        nestNum = _nestNum;
        filterFatherPath = _filterFatherPath;
    }
    
    /** Creates a new instance of FileListFetcher */
    public FileListFetcher( 
        BackupClient _cli,
        String _beginPath,
        DefaultListModel _model,
        Vector _listVector,
        BrowseBakedFile _diag,
        Socket _socket,
        boolean _yield,
        int _nestNum
    ) {
        cli = _cli;
        beginPath = _beginPath;
        model = _model;
        listVector = _listVector;
        diag = _diag;
        yield = _yield;
        socket = _socket;
        nestNum = _nestNum;
    }
    
    Runnable setTF1Enabled = new Runnable(){
        public void run(){
            diag.setEnabledTF1();
        }
    };
    
    @Override public void run(){
        try{
            diag.setWaitCursor( );
            
            getFileList = new GetFileList(
                ResourceCenter.getCmd( ResourceCenter.CMD_GET_FILE_LIST ),
                socket,
                yield,
                nestNum,
                filterFatherPath
            );
            getFileList.setListModel( model );
            getFileList.setListVector( listVector );
            
            getFileList.updateFileList( cli, beginPath ); 
            
            try{
                SwingUtilities.invokeAndWait( setTF1Enabled );
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            diag.resotreCursor();
            
            setOver(true);
        }catch(IOException ex1){
            ex1.printStackTrace();
            JOptionPane.showMessageDialog((JDialog)diag,
                SanBootView.res.getString("common.error.timeout")
            );
        }catch(InterruptedException ex2){
            ex2.printStackTrace();
        }catch(Exception ex3){
            ex3.printStackTrace();
        }
    }
    
    public void setDestroyFlag(boolean val){
        getFileList.setDestroy( val );
    }
    
    public synchronized boolean isOver(){
        return overed;
    }

    public synchronized void setOver(boolean val){
        overed = val;
    }
    
    public void startup(){
        this.start();
    }
}
