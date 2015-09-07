/*
 * ProcessEventOnService.java
 *
 * Created on February 25, 2005, 9:40 AM
 */

package guisanboot.ui;

import javax.swing.tree.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnService extends GeneralProcessEventForSanBoot{
    public ProcessEventOnService(){
        this( null );
    }
    public ProcessEventOnService( SanBootView view ){
        super( ResourceCenter.TYPE_SERVICE_INDEX,view );
    }
    
    @Override public void processTreeSelection( TreePath path ){
    }
    
    public void realDoTableDoubleClick(Object cell){}
    
    public void setupTableList(){   
    }
    
    public void insertSomethingToTable( Object obj ){
    }
}
