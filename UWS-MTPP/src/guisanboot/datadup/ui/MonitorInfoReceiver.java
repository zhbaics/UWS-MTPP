package guisanboot.datadup.ui;

/**
 * Title:        Odysys UWS
 * Description:
 * Copyright:    Copyright (c) 2008
 * Company:      odysys
 * @author
 * @version 3.0
 */
import java.beans.*;
import java.util.Vector;
import guisanboot.ui.SanBootView;

public class MonitorInfoReceiver extends Thread{
    private boolean Continue = true;
    private int     sleepTime  = 4; //4 秒��
    private Vector<PropertyChangeListener>  pListenerList = new Vector<PropertyChangeListener>();
    private PropertyChangeSupport  agent = new PropertyChangeSupport(this);
    private boolean suspendRequested = false; 
    SanBootView view;

    public MonitorInfoReceiver( SanBootView _view ) {
        view = _view;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener I){
        agent.addPropertyChangeListener(I);
        pListenerList.addElement( I );
    }
    
    public void removePropertyChangeListener(PropertyChangeListener I){
        agent.removePropertyChangeListener(I);
        pListenerList.removeElement( I );
    }
    
    public void requestSuspend(){
        suspendRequested = true;
    }
    
    private synchronized void checkSuspended() throws InterruptedException{
        while( suspendRequested ){
            wait();
        }
    }
    
    public synchronized void requestResume() {
        suspendRequested = false;
        notify();
    }
    
    public synchronized boolean isContinue(){
        return Continue;
    }

    public synchronized void setContinue(boolean val){
        Continue = val;
    }

    public void  run(){
        while( isContinue() ){
            try{
                // 检查 "是否要求暂停获取 task info"
                checkSuspended();
                sleep( sleepTime*1000 );
            }catch(InterruptedException e){}
      
            getTaskInfo();
        }
    }

    private void getTaskInfo(){
        boolean isOk = view.initor.mdb.updateTaskList();
        
        if( pListenerList.size()>0 ){ // 通知Listener们去获取新的task info.
            if( isOk ){
                agent.firePropertyChange(
                    "TASKINFO",
                    "old",
                    "new"
                );
            }else{
                // 通知listener们获取task info 失败
                agent.firePropertyChange(
                    view.initor.mdb.getErrorMessage(),
                    "old",
                    "new"
                );
            }
        }
    }
}
