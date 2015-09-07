/*
 * ProcessEventOnPool.java
 *
 * Created on Mar 21, 2008, 11:33 AM
 */

package guisanboot.ui;

import javax.swing.tree.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnPool extends GeneralProcessEventForSanBoot {
    /** Creates a new instance of ProcessEventOnPool */
    public ProcessEventOnPool(){
        this( null );
    }
    public ProcessEventOnPool(SanBootView view) {
        super( ResourceCenter.TYPE_POOL,view );
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
