/*
 * ProcessEventOnSrcUWSrvNode.java
 *
 * Created on July 10, 2008, 3:25 PM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnSrcUWSrvNode extends GeneralProcessEventForSanBoot {
    /** Creates a new instance of ProcessEventOnSrcUWSrvNode */
    public ProcessEventOnSrcUWSrvNode(){
        this( null );
    }
    public ProcessEventOnSrcUWSrvNode(SanBootView view) {
        super( ResourceCenter.TYPE_SRC_UWSSRV,view );
    }
    
    @Override public void processTreeSelection( TreePath path ){
        if ( !view.initor.isLogined() ) return;

        //2. remove other component
        view.removeRightPane();

        //3. add Table 
        view.addTable();
        
        setupTableList();
        insertSomethingToTable( null );
    }
      
    @Override public void processTreeExpand(TreePath path){
        BrowserTreeNode srcUWSrvNode = (BrowserTreeNode)path.getLastPathComponent();
        if( srcUWSrvNode != null ){
            view.removeAllData( srcUWSrvNode ); 
            
            ChiefRemoteHost chiefRHost = new ChiefRemoteHost();
            BrowserTreeNode chiefRHostNode = new BrowserTreeNode( chiefRHost, false );
            chiefRHost.setTreeNode( chiefRHostNode );
            chiefRHost.setFatherNode( srcUWSrvNode );
            srcUWSrvNode.add( chiefRHostNode );
            
            ChiefRemoteFreeVolume chiefRFreeVol = new ChiefRemoteFreeVolume();
            BrowserTreeNode chiefRFreeVolNode = new BrowserTreeNode( chiefRFreeVol, false );
            chiefRFreeVol.setTreeNode( chiefRFreeVolNode );
            chiefRFreeVol.setFatherNode( srcUWSrvNode );
            srcUWSrvNode.add( chiefRFreeVolNode );
            
            view.reloadTreeNode( srcUWSrvNode );
        }
    }
    
    public void realDoTableDoubleClick(Object cell){
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("common.table.item"),
            SanBootView.res.getString("common.table.desc")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,120},  {1,375}
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        Object[] one = new Object[2];
        RemoteHostService service = new RemoteHostService();
        one[0] = service;
        one[1] = new GeneralBrowserTableCell(
            -1, SanBootView.res.getString("common.desc.rhostList"),JLabel.LEFT
        );
        table.insertRow( one  );
        
        RemoteFreeVolService service1 = new RemoteFreeVolService();
        one[0] = service1;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.rFreeVolList"),JLabel.LEFT
        );
        table.insertRow( one );   
    }
}
