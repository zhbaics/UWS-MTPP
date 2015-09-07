/*
 * TaskLogGeter.java
 *
 * Created on Aug 11, 2008, 11:58 AM
 */

package guisanboot.datadup.ui;

import javax.swing.*;

import guisanboot.datadup.cmd.*;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class TaskLogGeter extends Thread{
    private BackupHistoryDialog diag;
    private SanBootView view;
    private int begin;
    private int num;
    private final Object lock = new Object();
    private boolean overed = false;
    private int getCount;
    
    /** Creates a new instance of TaskLogGeter */
    public TaskLogGeter(
        SanBootView _view,
        BackupHistoryDialog _diag,
        int _begin,
        int _num
    ){
        view = _view;
        diag = _diag;
        begin = _begin;
        num = _num;
    }
    
    Runnable setBtn = new Runnable(){
        public void run(){
            diag.enableButton( true );
        }
    };
    
    Runnable addListener = new Runnable(){
        public void run(){
            diag.addListSelectionListener();
        }
    };
    
    Runnable getInfoRun = new Runnable(){
        public void run(){
            try{
                GetTaskLog getTskLog = new GetTaskLog(
                    view, 
                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_TSKLOG )
                );
                getTskLog.setTableModel( diag.getTableModel() );
      
                getTskLog.updateTaskLog( begin,num );
                
                getCount = getTskLog.getCount();
                
                setOver(true);
            }catch( Exception ex ){
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

        diag.setBeforeGet( false );
        try{
            SwingUtilities.invokeAndWait( setBtn );
            SwingUtilities.invokeAndWait( addListener );
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
    
    public int getTskLogCount(){
        return getCount;
    }
}
