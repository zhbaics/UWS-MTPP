/*
 * InitProgressGeter.java
 *
 * Created on June 23, 2010, 9:00 AM
 */

package guisanboot.cmdp.service;

import guisanboot.cluster.entity.Cluster;
import javax.swing.*;
import java.io.*;

import guisanboot.cmdp.ui.QueryInitProgressDialog;
import guisanboot.data.BootHost;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class InitProgressGeter extends Thread{
    private QueryInitProgressDialog queryInitProgressDiag;
    private SanBootView view;
    private BootHost host = null;
    private Cluster cluster = null;
    private final Object lock = new Object();
    private boolean overed = false;
      
    /** Creates a new instance of InitProgressGeter */
    public InitProgressGeter( 
        QueryInitProgressDialog _diag,
        SanBootView _view,
        BootHost host,
        Cluster cluster
    ) {
        queryInitProgressDiag = _diag;
        view = _view;
        this.host = host;
        this.cluster = cluster;
    }
    
    Runnable setBtn = new Runnable(){
        public void run(){
            queryInitProgressDiag.enableButton( true );
        }
    };
  
    Runnable getInfoRun = new Runnable(){
        public void run(){
            try{
                GetInitProgressIntoTableModel getTask = new GetInitProgressIntoTableModel(
                    view,
                    host,
                    cluster
                );
                
                getTask.setTaskTable( queryInitProgressDiag.getTable() );
            
                getTask.updateInitProgress();
            
                setOver(true);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    };
    
    @Override public void run(){
        Thread getInfoThread = new Thread( getInfoRun );
        getInfoThread.start();

        while( !isOver() ){
            try{
                Thread.sleep(100);
            } catch( Exception e){
                e.printStackTrace();
            }
        }

        try{
            SwingUtilities.invokeAndWait( setBtn );
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    boolean isOver(){
        synchronized ( lock ){
            return overed;
        }
    }

    void setOver(boolean val){
        synchronized( lock ){
            overed = val;
        }
    }
}
