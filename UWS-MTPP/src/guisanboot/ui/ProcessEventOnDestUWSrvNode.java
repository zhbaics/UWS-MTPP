/*
 * ProcessEventOnDestUWSrvNode.java
 *
 * Created on July 13, 2008, 3:25 PM
 */

package guisanboot.ui;

import javax.swing.tree.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnDestUWSrvNode extends GeneralProcessEventForSanBoot {
    /** Creates a new instance of ProcessEventOnDestUWSrvNode */
    public ProcessEventOnDestUWSrvNode(){
        this( null );
    }
    public ProcessEventOnDestUWSrvNode(SanBootView view) {
        super( ResourceCenter.TYPE_DEST_UWS,view );
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
