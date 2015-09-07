/*
 * ProcessEventOnMirroredSnap.java
 *
 * Created on December 10, 2004, 3:25 PM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnMirroredSnap extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curSnapNode = null; 
    
    /** Creates a new instance of ProcessEventOnMirroredSnap */
    public ProcessEventOnMirroredSnap(){
        this( null );
    }
    public ProcessEventOnMirroredSnap(SanBootView view) {
        super( ResourceCenter.TYPE_MIRROR_SNAP,view );
    }
    
    private void showViewList( BrowserTreeNode snapNode,int eventType ){
        curSnapNode = snapNode;
        MirroredSnap snap = (MirroredSnap)snapNode.getUserObject();
        
        GetViewDirectlyUnderSnap thread = new GetViewDirectlyUnderSnap(
            view, 
            curSnapNode,
            this,
            eventType,
            snap.snap
        );  
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getView"),
            SanBootView.res.getString("View.pdiagTip.getView"), 
            thread
        );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
         
        view.removeRightPane();
        view.addTable( false );

        BrowserTreeNode snapNode = (BrowserTreeNode)path.getLastPathComponent();
        if( snapNode != null ){
            showViewList( snapNode,Browser.TREE_SELECTED_EVENT );   
        }
    }
    
    @Override public void processTreeExpand(TreePath path){
        BrowserTreeNode snapNode = (BrowserTreeNode)path.getLastPathComponent();
        if( snapNode != null ){
            showViewList( snapNode,Browser.TREE_EXPAND_EVENT );
        }  
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        View aView = (View)obj;
        aView.setFatherNode( curSnapNode );
        
        Object[] one = new Object[4];
        one[0] = aView;
        
        one[1] =  new GeneralBrowserTableCell(
            -1,aView.toString(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,aView.getCreateTimeStr(),JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,aView.getStatus() ,JLabel.LEFT
        );   
        table.insertRow( one );
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
