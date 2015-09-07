package guisanboot.ui;
import guisanboot.data.MirrorJob;
import java.beans.*;
import java.util.Vector;

public class MonitorInfoReceiver extends Thread{
    private boolean Continue = true;
    private int     sleepTime  = 4; // 4秒
    private Vector  pListenerList = new Vector();
    private PropertyChangeSupport  agent = new PropertyChangeSupport(this);
    private boolean suspendRequested = false; 
    SanBootView view;
    MirrorJob mj = null;
    
    public MonitorInfoReceiver( SanBootView _view,MirrorJob mj ) {
        view = _view;
        this.mj = mj;
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

    public synchronized void setContinue( boolean val ){
        Continue = val;
    }
    
    @Override public void  run(){
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
        boolean isOk = view.initor.mdb.monitorMJ( mj.getMj_id() );
        
        if( pListenerList.size()>0 ){ // 通知Listener们去获取新的task info.
            if( isOk ){
                agent.firePropertyChange(
                    "MJINFO",
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
