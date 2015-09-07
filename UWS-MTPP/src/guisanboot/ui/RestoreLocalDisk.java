/*
 * RestoreLocalDisk.java
 *
 * Created on 2007/1/8,�AM 11:30
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
public class RestoreLocalDisk implements Runnable{
    RestoreOrigiDiskable restOrgDiskDiag;
    private boolean overed = false;
     
    /** Creates a new instance of RestoreLocalDisk */
    public RestoreLocalDisk( RestoreOrigiDiskable diag ) {
        restOrgDiskDiag = diag;
    }
    
    Runnable setFinishStatus = new Runnable(){
        public void run(){
            restOrgDiskDiag.enableNextButton( true );
            
            // 一旦到了这一步, 就只能点击"Finish"按钮来结束
            // restOrgDiskDiag.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        }
    };
    
    Runnable setStatus = new Runnable(){
        public void run(){
            restOrgDiskDiag.setProcess();
        }
    };
    
    Runnable restoreLocalDisk = new Runnable(){
        public void run(){
            restOrgDiskDiag.realRestore();
            setOver( true );
        }
    };
    
    public void run(){
        Thread initThread = new Thread( restoreLocalDisk );
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
