/*
 * ProcessEventOnLocalUnlimitedIncMirroredSnap.java
 *
 * Created on Janu 27, 2010, 2:15 PM
 */

package guisanboot.unlimitedIncMj.service;

import guisanboot.ui.*;
import javax.swing.tree.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnLocalUnlimitedIncMirroredSnap extends GeneralProcessEventForSanBoot { 
    /** Creates a new instance of ProcessEventOnLocalUnlimitedIncMirroredSnap */
    public ProcessEventOnLocalUnlimitedIncMirroredSnap(){
        this( null );
    }
    public ProcessEventOnLocalUnlimitedIncMirroredSnap(SanBootView view) {
        super( ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP1,view );
    }
    
    
    @Override public void processTreeSelection(TreePath path){
    }
    
    @Override public void processTreeExpand(TreePath path){
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }

    public void insertSomethingToTable( Object obj ){
    }

    public void setupTableList(){
    }   
}
