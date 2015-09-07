/*
 * BasicGetSomethingThread.java
 *
 * Created on 2008/6/20, 2:50 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author zjj
 */
public abstract class BasicGetSomethingThread extends Thread implements StartupProgress{
    protected JDialog pdiag = null;
    protected SanBootView view;
    protected String errMsg = "";
    protected int errcode = 0;
    protected int cmd = -1;
    protected boolean isOk = true;
    private boolean showErrDiag = true;
    
    Runnable close = new Runnable(){
        public void run(){
            pdiag.dispose();
        }
    };
    
    public void setProcessDialg( JDialog diag ){
        pdiag = diag;
    } 
    public void startProcessing(){
        this.start();
    }
    
    /** Creates a new instance of BasicGetSomethingThread */
    public BasicGetSomethingThread() {
    }
    
    /** Creates a new instance of BasicGetSomethingThread */
    public BasicGetSomethingThread(
        SanBootView view
    ) {
        this( view,true );
    }
    
    public BasicGetSomethingThread(
        SanBootView view,
        boolean showErrDiag
    ){
        this.view = view;
        this.showErrDiag = showErrDiag;
    }
    
    @Override public void run(){
        if( !(isOk = realRun()) ){
            if( showErrDiag ){
                JOptionPane.showMessageDialog( view, errMsg );
            }
        }
        
        closePDialog();
    }
    
    public void closePDialog(){
        if( pdiag != null ){
            try{
                SwingUtilities.invokeAndWait( close );
            }catch( Exception ex ){
                //ex.printStackTrace();
            }
        }
    }
    
    public abstract boolean realRun();
    
    public boolean isOk(){
        return isOk;
    }
    
    public String getErrMsg(){
        return errMsg;
    }
    
    public int getErrCode(){
        return errcode;
    }
    
    public int getErrCmd(){
        return cmd;
    }
}
