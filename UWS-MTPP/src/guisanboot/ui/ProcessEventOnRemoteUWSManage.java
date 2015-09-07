/*
 * ProcessEventOnRemoteUWSManage.java
 *
 * Created on July 18, 2008, 9:40 PM
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
public class ProcessEventOnRemoteUWSManage extends GeneralProcessEventForSanBoot{
    BrowserTreeNode curRemoteUWSmgNode;
    
    public ProcessEventOnRemoteUWSManage(){
        this( null );
    }
    public ProcessEventOnRemoteUWSManage( SanBootView view ){
        super( ResourceCenter.TYPE_REMOTE_UWS_MANAGE,view );
    }
    
    private void showSrcUWSNodeList( BrowserTreeNode remoteUWSMgNode,int eventType ){ 
        curRemoteUWSmgNode = remoteUWSMgNode;
        
        GetSrcUWSrvNodeUnderRemoteUWSMG thread = new GetSrcUWSrvNodeUnderRemoteUWSMG(
            view, 
            remoteUWSMgNode,
            this,
            eventType
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getSrcSWU"),
            SanBootView.res.getString("View.pdiagTip.getSrcSWU"), 
            thread
        );
    }
    
    @Override public void processTreeSelection( TreePath path ){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
       
        BrowserTreeNode remoteUWSMgNode = (BrowserTreeNode)path.getLastPathComponent();
        if( remoteUWSMgNode != null ){
            showSrcUWSNodeList( remoteUWSMgNode,Browser.TREE_SELECTED_EVENT );   
        }
    }
    
    @Override public void processTreeExpand(TreePath path){
        BrowserTreeNode remoteUWSMgNode = (BrowserTreeNode)path.getLastPathComponent();
        if( remoteUWSMgNode != null ){
            showSrcUWSNodeList( remoteUWSMgNode,Browser.TREE_EXPAND_EVENT );
        }  
    }
     
    public void realDoTableDoubleClick(Object cell){}
    
    public void setupTableList(){   
       Object[] title = new Object[]{
            SanBootView.res.getString("View.table.swu.id"),
            SanBootView.res.getString("View.table.swu.ip"),
            SanBootView.res.getString("View.table.swu.port"),
            SanBootView.res.getString("View.table.swu.psn")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
          {0,75},  {1,145}, {2,145}, {3,105}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        SrcUWSrvNodeWrapper uws =(SrcUWSrvNodeWrapper)obj;
        uws.setFatherNode( curRemoteUWSmgNode );
        
        Object[] one = new Object[4];
        one[0] = uws;

        one[1] = new GeneralBrowserTableCell(
            -1,uws.getMetaData().getUws_ip(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,uws.getMetaData().getUws_port()+"",JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,uws.getMetaData().getUws_psn(),JLabel.LEFT
        );
        table.insertRow(one);
    }
}
