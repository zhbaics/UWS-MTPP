/*
 * ProcessEventOnLunMap.java
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
public class ProcessEventOnLunMap extends GeneralProcessEventForSanBoot {
    /** Creates a new instance of ProcessEventOnLunMap */
    public ProcessEventOnLunMap(){
        this( null );
    }
    public ProcessEventOnLunMap(SanBootView view) {
        super( ResourceCenter.TYPE_LUNMAP_INDEX,view );
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
