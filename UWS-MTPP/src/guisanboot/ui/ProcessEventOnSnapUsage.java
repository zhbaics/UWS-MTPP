/*
 * ProcessEventOnSnapUsage.java
 *
 * Created on July 28, 2008, 3:25 PM
 */

package guisanboot.ui;

import javax.swing.tree.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnSnapUsage extends GeneralProcessEventForSanBoot {
    /** Creates a new instance of ProcessEventOnSnapUsage */
    public ProcessEventOnSnapUsage(){
        this( null );
    }
    public ProcessEventOnSnapUsage(SanBootView view) {
        super( ResourceCenter.TYPE_SNAP_USAGE,view );
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
