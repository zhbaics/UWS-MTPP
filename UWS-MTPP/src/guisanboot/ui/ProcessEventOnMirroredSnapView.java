/*
 * ProcessEventOnMirroredSnapView.java
 *
 * Created on December 10, 2004, 3:25 PM
 */

package guisanboot.ui;

import javax.swing.tree.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnMirroredSnapView extends GeneralProcessEventForSanBoot {
    /** Creates a new instance of ProcessEventOnMirroredSnapView */
    public ProcessEventOnMirroredSnapView(){
        this( null );
    }
    public ProcessEventOnMirroredSnapView(SanBootView view) {
        super( ResourceCenter.TYPE_MIRROR_SNAP_VIEW,view );
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
