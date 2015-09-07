/*
 * ProcessEventOnScheduler.java
 *
 * Created on Aug 7, 2008, 11:33 AM
 */

package guisanboot.datadup.ui.ProcessEvent;
import javax.swing.tree.*;
import guisanboot.res.*;
import guisanboot.ui.GeneralProcessEventForSanBoot;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnScheduler extends GeneralProcessEventForSanBoot {
    /** Creates a new instance of ProcessEventOnScheduler */
    public ProcessEventOnScheduler(){
        this( null );
    }
    public ProcessEventOnScheduler(SanBootView view) {
        super( ResourceCenter.TYPE_SCH,view );
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
