/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 2009/12/16 PM 1:49
 */

package guisanboot.tool.ui;

import guisanboot.ui.InitProgramDialog;
import guisanboot.ui.SlowerLaunch;
import java.awt.Cursor;
import javax.swing.SwingUtilities;

/**
 *
 * @author zjj
 */
public abstract class BasicProcessor implements Runnable{
    protected InitProgramDialog diag;
    protected SlowerLaunch launcher;
    protected boolean overed = false;

    public BasicProcessor( InitProgramDialog _diag ){
        diag = _diag;
    }

    /** Creates a new instance of LaunchSomething */
    public BasicProcessor( InitProgramDialog _diag, SlowerLaunch _launcher ) {
        diag = _diag;
        launcher = _launcher;
    }

    public void setSlowerLaunch( SlowerLaunch launcher ){
        this.launcher = launcher;
    }

    Runnable setStatus = new Runnable(){
        public void run(){
            diag.setProcess(
                launcher.getLoadingProcessVal(),
                launcher.getLoadingStatus()
            );
            diag.setCursor(
                Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR )
            );
        }
    };

    Runnable close = new Runnable(){
        public void run(){
            diag.dispose();
        }
    };

    public abstract void doSomething();

    Runnable doSomething = new Runnable(){
        public void run(){
            doSomething();
            setOver( true );
        }
    };

    public void run(){
        Thread initThread = new Thread( doSomething );
        initThread.start();

        while( !isOver() ){
            try{
                SwingUtilities.invokeAndWait( setStatus );

                Thread.sleep(200);
            } catch( Exception e){
                e.printStackTrace();
            }
        }

        try{
            SwingUtilities.invokeAndWait( close );
        }catch(Exception ex1){
            ex1.printStackTrace();
        }
    }

    synchronized boolean isOver(){
        return overed;
    }

    synchronized void setOver(boolean val){
        overed = val;
    }
}
