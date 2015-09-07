/*
 * ProcessEventOnMjScheduler.java
 *
 * Created on Seq 9, 2010, 10:28 AM
 */

package guisanboot.remotemirror.service;

import guisanboot.ui.*;
import javax.swing.tree.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnMjScheduler extends GeneralProcessEventForSanBoot {   
    /** Creates a new instance of ProcessEventOnMjScheduler */
    public ProcessEventOnMjScheduler(){
        this( null );
    }
    public ProcessEventOnMjScheduler(SanBootView view) {
        super( ResourceCenter.TYPE_MJ_SCHEDULER,view );
    }
    
    @Override public void processTreeSelection(TreePath path){
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }

    public void insertSomethingToTable( Object obj ){
    }
    
    public void setupTableList(){
    } 
}
