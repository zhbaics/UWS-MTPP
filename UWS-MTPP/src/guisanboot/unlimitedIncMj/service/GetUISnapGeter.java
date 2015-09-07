/*
 * GetUISnapGeter.java
 *
 * Created on Jan 05, 2010, 4:38 PM
 */

package guisanboot.unlimitedIncMj.service;

import javax.swing.*;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.model.GetSnapDirectlyUnderUnlimitedIncMirrorVol;
import guisanboot.unlimitedIncMj.ui.QueryUnlimitedIncSnapDialog;

/**
 *
 * @author  Administrator
 */
public class GetUISnapGeter extends Thread{
    private QueryUnlimitedIncSnapDialog diag;
    private SanBootView view;
    private int rootid;
    private int begin;
    private int end;
    private final Object lock = new Object();
    private boolean overed = false;
    private int getCount;
    private int type;
    private boolean isCmdp;
    
    /** Creates a new instance of GetUISnapGeter */
    public GetUISnapGeter(
        SanBootView _view,
        QueryUnlimitedIncSnapDialog _diag,
        int _rootid,
        int _begin,
        int _end,
        int _type,
        boolean isCmdp
    ){
        view = _view;
        diag = _diag;
        rootid = _rootid;
        begin = _begin;
        end = _end;
        type = _type;
        this.isCmdp = isCmdp;
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
                GetSnapDirectlyUnderUnlimitedIncMirrorVol getSnap = new GetSnapDirectlyUnderUnlimitedIncMirrorVol(
                    view,null,null,1,rootid,begin,end,true,type,isCmdp
                );
                getSnap.setTableModel( diag.getTableModel() );
                getSnap.realRun();
                getCount = getSnap.getCount();

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
