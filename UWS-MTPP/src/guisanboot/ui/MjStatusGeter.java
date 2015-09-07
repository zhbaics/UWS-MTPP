/*
 * MjStatusGeter.java
 *
 * Created on July 20, 2008, 9:29 AM
 */

package guisanboot.ui;

import guisanboot.data.MirrorJob;
import javax.swing.*;

/**
 *
 * @author  Administrator
 */
public class MjStatusGeter extends Thread{
    private MonitorMJDialog monitorDiag;
    private SanBootView view;
    private MirrorJob mj;
    private final static Object lock = new Object();
    private boolean overed = false;
      
    /** Creates a new instance of MjStatusGeter */
    public MjStatusGeter( 
        MonitorMJDialog _diag,
        MirrorJob mj,
        SanBootView _view
    ) {
        monitorDiag = _diag;
        this.mj = mj;
        view = _view;
    }
    
    Runnable setBtn = new Runnable(){
        public void run(){
            monitorDiag.enableButton( true );
        }
    };
    
    Runnable getInfoRun = new Runnable(){
        public void run(){
            view.initor.mdb.monitorMJ( mj.getMj_id() );     
            MirrorJob newMj = view.initor.mdb.getMonedMj( mj.getMj_id() );
            if( newMj != null ){
                monitorDiag.setLocalSnapID( newMj.getMj_current_snap_id() );
                monitorDiag.setProgress( newMj.getMj_current_process() );
                if( !newMj.getMj_info().equals("end") && !newMj.getMj_info().trim().equals("") )
                    monitorDiag.setMsg( newMj.getMj_info() );
            }
            
            setOver(true);
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
