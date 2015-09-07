/*
 * ProcessEventOnDeletingSnapshot.java
 *
 * Created on July 11, 2008, 3:25 PM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnDeletingSnapshot extends GeneralProcessEventForSanBoot {    
    /** Creates a new instance of ProcessEventOnDeletingSnapshot */
    public ProcessEventOnDeletingSnapshot(){
        this( null );
    }
    public ProcessEventOnDeletingSnapshot(SanBootView view) {
        super( ResourceCenter.TYPE_DEL_SNAP_INDEX,view );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
         
        view.removeRightPane();
        view.addTable( false );
        
        setupTableList();
    }
    
    @Override public void processTreeExpand(TreePath path){
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
    }
    
    public void setupTableList(){    
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.snap.viewid"),
            SanBootView.res.getString("View.table.snap.name"),
            SanBootView.res.getString("View.table.snap.crtTime"),
            SanBootView.res.getString("View.table.snap.status"),
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,155},  {1,165}, {2,155}, {3,65}
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }    
}
