/*
 * ProcessEventOnActiveLink.java
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
public class ProcessEventOnActiveLink extends GeneralProcessEventForSanBoot {
    /** Creates a new instance of ProcessEventOnActiveLink */
    public ProcessEventOnActiveLink() {
        this( null );
    }
    public ProcessEventOnActiveLink(SanBootView view) {
        super( ResourceCenter.TYPE_ACTLINK_INDEX,view );
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
