/*
 * TaskListGeter.java
 *
 * Created on Aug 11, 2008, 11:29 AM
 */

package guisanboot.datadup.ui;

import guisanboot.datadup.cmd.GetBakTaskIntoTableModel;
import javax.swing.*;
import java.io.*;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class TaskListGeter extends Thread{
    private MonitorDialog monitorDiag;
    private SanBootView view;
    private final Object lock = new Object();
    private boolean overed = false;
      
    /** Creates a new instance of TaskListGeter */
    public TaskListGeter( 
        MonitorDialog _diag,
        SanBootView _view
    ) {
        monitorDiag = _diag;
        view = _view;
    }
    
    Runnable setBtn = new Runnable(){
        public void run(){
            monitorDiag.enableButton( true );
        }
    };
  
    Runnable getInfoRun = new Runnable(){
        public void run(){
            try{
                GetBakTaskIntoTableModel getTask = new GetBakTaskIntoTableModel( 
                    view,
                    ResourceCenter.getCmd( ResourceCenter.CMD_GET_TASK ) 
                );
                getTask.setTaskTable( monitorDiag.getTaskTable() );
                getTask.updateTaskList();
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
