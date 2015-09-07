/*
 * ProcessEventOnChiefNetBootHost.java
 *
 * Created on July 28, 2008, 3:25 PM
 */

package guisanboot.ui;

import guisanboot.cluster.entity.Cluster;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;

import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefNetBootHost extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefNBHNode;
    
    /** Creates a new instance of ProcessEventOnChiefNetBootHost */
    public ProcessEventOnChiefNetBootHost(){
        this( null );
    }
    public ProcessEventOnChiefNetBootHost(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_NETBOOT_HOST,view );
    }
    
    private void showNetBootHostList( BrowserTreeNode chiefNBHNode,int eventType ){
        curChiefNBHNode = chiefNBHNode;
        
        ChiefNetBootHost chiefNBH = (ChiefNetBootHost)chiefNBHNode.getUserObject();
        BrowserTreeNode hostNode = chiefNBH.getFatherNode();
        if( hostNode != null ){
            Object hostObj = hostNode.getUserObject();
            boolean isBootHost = ( hostObj instanceof BootHost );
            boolean isCluster = (hostObj instanceof Cluster );
            if( isBootHost || isCluster ){
                if( eventType == Browser.TREE_SELECTED_EVENT ){
                    removeAllRow();
                    setupTableList();
                }else{
                    view.removeAllData( chiefNBHNode );
                }
                
                ArrayList daList = view.getNetbootedHostOnHost( hostNode ); // get dest agent list
                int size = daList.size();
                for( int i=0; i<size; i++ ){
                    Object daObj = daList.get(i);
                    if( eventType == Browser.TREE_SELECTED_EVENT ){
                        this.insertSomethingToTable( daObj );
                    }else{
                        DestAgent da = (DestAgent)daObj;
                        BrowserTreeNode cNode = new BrowserTreeNode( da,false );
                        da.setTreeNode( cNode );
                        da.setFatherNode( chiefNBHNode );
                        view.addNode( chiefNBHNode,cNode );
                    }
                }

                if( eventType == Browser.TREE_EXPAND_EVENT ){
                    view.reloadTreeNode( chiefNBHNode );
                }
            }else{
                removeAllRow();
                setupTableList();
                
                SourceAgent srcAgnt = (SourceAgent)hostObj;
                int srcAgntID = srcAgnt.getSrc_agnt_id();
                GetNetbootedHostUnderSrcAgnt thread = new GetNetbootedHostUnderSrcAgnt(
                    view, 
                    chiefNBHNode,
                    this,
                    eventType, 
                    srcAgntID
                );
                view.startupProcessDiag( 
                    SanBootView.res.getString("View.pdiagTitle.getNetbootedHost"),
                    SanBootView.res.getString("View.pdiagTip.getNetbootedHost"), 
                    thread
                );
            }
        }
    }
    
    @Override public void processTreeExpand( TreePath path ){    
        // 布置table中的内容
        BrowserTreeNode chiefNBHNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefNBHNode != null ){
            showNetBootHostList( chiefNBHNode, Browser.TREE_EXPAND_EVENT );
        }
    }
    
    @Override public void processTreeSelection(TreePath path){        
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefNBHNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefNBHNode != null ){
            showNetBootHostList( chiefNBHNode, Browser.TREE_SELECTED_EVENT );
        }
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        DestAgent da = (DestAgent)obj;
        da.setFatherNode( curChiefNBHNode );
        
        Object[] one = new Object[3];
        one[0] = da;

        one[1] = new GeneralBrowserTableCell(
            -1,da.getDst_agent_ip(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,da.getDst_agent_mac(),JLabel.LEFT
        );
        
        table.insertRow( one );
    }
    
    public void setupTableList(){    
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.host.id"),
            SanBootView.res.getString("View.table.host.ip"),
            SanBootView.res.getString("View.table.host.mac")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
          {0,70}, {1,95}, {2,225}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }    
}
