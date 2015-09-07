/*
 * MirrorJobGeter.java
 *
 * Created on Aug 18, 2010, 13:59 PM
 */

package guisanboot.remotemirror.ui;

import guisanboot.remotemirror.cmd.GetMJIntoTableModel;
import javax.swing.*;
import java.io.*;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class MirrorJobGeter extends Thread{
    private BatchedOpForMjInterface diag;
    private SanBootView view;
    private final Object lock = new Object();
    private boolean overed = false;
    private int mode;

    /** Creates a new instance of MirrorJobGeter */
    public MirrorJobGeter(
        BatchedOpForMjInterface _diag,
        SanBootView _view
    ) {
        diag = _diag;
        view = _view;
        this.mode = 0;
    }

    /** Creates a new instance of MirrorJobGeter */
    public MirrorJobGeter( 
        BatchedOpForMjInterface _diag,
        SanBootView _view,
        int mode
    ) {
        diag = _diag;
        view = _view;
        this.mode = mode;
    }
    
    Runnable setBtn = new Runnable(){
        public void run(){
            diag.enableButton( true );
        }
    };
  
    Runnable getInfoRun = new Runnable(){
        public void run(){
            try{
                GetMJIntoTableModel getMJList = new GetMJIntoTableModel(
                    view,
                    ResourceCenter.getCmd( ResourceCenter.CMD_GET_MJ ),
                    mode
                );
                getMJList.setMjTable( diag.getTable() );
                getMJList.updateMJList();
                setOver( true );
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
