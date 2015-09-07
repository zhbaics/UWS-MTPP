/*
 * RecoveringBootHost.java
 *
 * Created on 2007/1/8,��AM 11:30
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;
import javax.swing.*;
/**
 *
 * @author Administrator
 */
public class RecoveringBootHost implements Runnable{
    FailoverInterface failover;
    private boolean overed = false;
     
    /** Creates a new instance of RecoveringBootHost */
    public RecoveringBootHost( FailoverInterface failover ) {
        this.failover = failover;
    }
    
    Runnable setFinishStatus = new Runnable(){
        public void run(){
            failover.enableNextButton( true );
            
            // 下面语句测试： 点击view后显示其直属的snap
            // failoverDiag.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        }
    };
    
    Runnable setStatus = new Runnable(){
        public void run(){
        }
    };
    
    Runnable recoverBootHost = new Runnable(){
        public void run(){
            failover.realDRRecover();
            setOver( true );
        }
    };
    
    public void run(){
        Thread initThread = new Thread( recoverBootHost );
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
            SwingUtilities.invokeAndWait( setFinishStatus );
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
