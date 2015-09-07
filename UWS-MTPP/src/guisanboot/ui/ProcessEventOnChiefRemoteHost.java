/*
 * ProcessEventOnChiefRemoteHost.java
 *
 * Created on July 14, 2008, 3:25 PM
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
public class ProcessEventOnChiefRemoteHost extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefRHostNode;
    
    /** Creates a new instance of ProcessEventOnChiefRemoteHost */
    public ProcessEventOnChiefRemoteHost(){
        this( null );
    }
    public ProcessEventOnChiefRemoteHost(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_REMOTE_HOST,view );
    }
    
    private void showSrvAgntList( BrowserTreeNode chiefRHostNode,int eventType ){  
        curChiefRHostNode = chiefRHostNode;
        
        ChiefRemoteHost chiefRHost = (ChiefRemoteHost)chiefRHostNode.getUserObject();
        BrowserTreeNode fNode = (BrowserTreeNode)chiefRHost.getFatherNode();
        SrcUWSrvNodeWrapper srcUWSrv =(SrcUWSrvNodeWrapper)fNode.getUserObject();
        int src_uws_id = srcUWSrv.getMetaData().getUws_id();
        
        // SourceAgent就是remote host
        GetSrcAgntNodeUnderChiefRHost thread = new GetSrcAgntNodeUnderChiefRHost(
            view, 
            chiefRHostNode,
            this,
            eventType, 
            src_uws_id
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getRHost"),
            SanBootView.res.getString("View.pdiagTip.getRHost"), 
            thread
        );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefRHostNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefRHostNode != null ){
            showSrvAgntList( chiefRHostNode,Browser.TREE_SELECTED_EVENT );   
        }
    }
    
    @Override public void processTreeExpand(TreePath path){
        BrowserTreeNode chiefRHostNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefRHostNode != null ){
            showSrvAgntList( chiefRHostNode,Browser.TREE_EXPAND_EVENT );
        }
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.host.id"),
            SanBootView.res.getString("View.table.host.ip"),
            SanBootView.res.getString("View.table.host.port"),
            SanBootView.res.getString("View.table.host.protecttype"),
            SanBootView.res.getString("View.table.host.os"),
            SanBootView.res.getString("View.table.host.machine"),
            SanBootView.res.getString("View.table.host.bootMode"),
            SanBootView.res.getString("View.table.host.uuid")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
          {0,75},  {1,145}, {2,75}, {3,70},{4,145},{5,100},{6,90},{7,245}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        SourceAgent srcAgnt =(SourceAgent)obj;
        srcAgnt.setFatherNode( curChiefRHostNode );
        
        Object[] one = new Object[8];
        one[0] = srcAgnt;
        one[1] = new GeneralBrowserTableCell(
            -1,srcAgnt.getSrc_agnt_ip(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,srcAgnt.getSrc_agnt_port()+"",JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,srcAgnt.getProtectString(),JLabel.LEFT
        );
        one[4] = new GeneralBrowserTableCell(
            -1,srcAgnt.getSrc_agnt_os()+"",JLabel.LEFT
        );
        one[5] = new GeneralBrowserTableCell(
            -1,srcAgnt.getMachine(),JLabel.LEFT
        );
        one[6] = new GeneralBrowserTableCell(
            -1,srcAgnt.getBootModeString(),JLabel.LEFT
        );
        one[7] = new GeneralBrowserTableCell(
            -1,srcAgnt.getUUID(),JLabel.LEFT
        );
        table.insertRow(one);
    }
}
