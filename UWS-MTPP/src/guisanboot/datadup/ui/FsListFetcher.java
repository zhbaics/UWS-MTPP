/*
 * FsListFetcher.java
 *
 * Created on Aug 14, 2008, 13:31 PM
 */

package guisanboot.datadup.ui;

import guisanboot.datadup.data.BackupClient;
import guisanboot.datadup.cmd.GetLinuxFS;
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
public class FsListFetcher extends Thread implements FetchFileable{
    private String beginPath;
    private DefaultListModel model;
    private Vector listVector;
    private BrowseBakedFile diag; 
    private Socket socket;
    private BackupClient cli; 
    private boolean overed = false;
    private GetLinuxFS getFsList; 
    private boolean yield = false;
    private int nestNum=0;
    private boolean filterFatherPath = false; // 是否过滤掉".."目录
    
    /** Creates a new instance of FsListFetcher */
    public FsListFetcher( 
        BackupClient _cli,
        String _beginPath,
        DefaultListModel _model,
        Vector _listVector,
        BrowseBakedFile _diag,
        Socket _socket,
        boolean _yield,
        int _nestNum,
        boolean _filterFatherPath
    ) {
        cli = _cli;
        beginPath = _beginPath;
        model = _model;
        listVector = _listVector;
        diag = _diag;
        socket = _socket;
        yield = _yield;
        nestNum = _nestNum;
        filterFatherPath = _filterFatherPath;
    }
    
    Runnable setTF1Enabled = new Runnable(){
        public void run(){
            diag.setEnabledTF1();
        }
    };
    
    public void run(){
        try{
            diag.setWaitCursor( );
            
            getFsList = new GetLinuxFS(
                ResourceCenter.getCmd( ResourceCenter.CMD_GET_LINUX_FS ),
                socket,
                yield,
                nestNum,
                filterFatherPath
            );
            getFsList.setListModel( model );
            getFsList.setListVector( listVector );
            
            getFsList.updateFileList( cli, beginPath ); 
            
            try{
                SwingUtilities.invokeAndWait( setTF1Enabled );
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            diag.saveRoots(); 
            diag.resotreCursor();
            
            setOver(true);
        }catch(IOException ex1){
            ex1.printStackTrace();
            JOptionPane.showMessageDialog((JDialog)diag,
                SanBootView.res.getString("common.error.timeout")
            );
        }catch(Exception ex2){
            ex2.printStackTrace();
        }
    }
    
    public void setDestroyFlag(boolean val){
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
