/*
 * AuditLogGeter.java
 *
 * Created on Aug 11, 2008, 11:58 AM
 */

package guisanboot.audit.ui;

import guisanboot.audit.cmd.*;
import javax.swing.*;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class AuditLogGeter extends Thread{
    private AuditLogDialog diag;
    private SanBootView view;
    private int begin;
    private int num;
    private Object lock = new Object();
    private boolean overed = false;
    private int getCount;
    
    /** Creates a new instance of AuditLogGeter */
    public AuditLogGeter(
        SanBootView _view,
        AuditLogDialog _diag,
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
                GetAuditLog getAuditLog = new GetAuditLog(
                    view, 
                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AUDIT )
                );
                getAuditLog.setTableModel( diag.getTableModel() );
      
                getAuditLog.updateAuditLog( begin,num );
                
                getCount = getAuditLog.getCount();
                
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
