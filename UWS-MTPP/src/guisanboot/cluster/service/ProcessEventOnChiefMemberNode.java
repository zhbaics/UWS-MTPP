/*
 * ProcessEventOnChiefMemberNode.java
 *
 * Created on Aug 4, 2011, 16:26 PM
 */

package guisanboot.cluster.service;

import guisanboot.cluster.entity.ChiefMemberNode;
import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.entity.SubCluster;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.res.*;
import guisanboot.ui.GeneralProcessEventForSanBoot;
import guisanboot.ui.SanBootView;
import guisanboot.data.BootHost;
import java.util.ArrayList;


/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefMemberNode extends GeneralProcessEventForSanBoot {    
    /** Creates a new instance of ProcessEventOnChiefMemberNode */
    public ProcessEventOnChiefMemberNode(){
        this( null );
    }
    
    public ProcessEventOnChiefMemberNode(SanBootView view) {
        super( ResourceCenter.TYPE_CLUSTER_CHIEF_MEMBER_NODE,view );
    }
    
    private void showMemberNodeList( BrowserTreeNode chiefMembersNode,int eventType ){
        removeAllRow();
        setupTableList();

        ChiefMemberNode chiefMembers = (ChiefMemberNode)chiefMembersNode.getUserObject();
        BrowserTreeNode cNode = chiefMembers.getFatherNode();
        Cluster cluster = (Cluster)cNode.getUserObject();
        ArrayList<SubCluster> subcList = cluster.getRealSubCluster();
        int size = subcList.size();
        for( int i=0; i<size; i++ ){
            insertSomethingToTable( subcList.get(i).getHost() );
        }
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        BrowserTreeNode chiefMembersNode = (BrowserTreeNode)path.getLastPathComponent();
        showMemberNodeList( chiefMembersNode,Browser.TREE_SELECTED_EVENT );
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.host.id"),
            SanBootView.res.getString("View.table.host.name"),
            SanBootView.res.getString("View.table.host.ip"),
            SanBootView.res.getString("View.table.host.priip"),
            SanBootView.res.getString("View.table.host.vip"),
            SanBootView.res.getString("View.table.host.port"),
            SanBootView.res.getString("View.table.host.os"),
            SanBootView.res.getString("View.table.host.machine"),
            SanBootView.res.getString("View.table.host.uuid")
        };
        table.setupTitle( title );

        int[][] widthList = new int[][]{
            {0,55},{1,160},{2,115},{3,115},{4,115},{5,55},
            {6,100},{7,100},{8,250}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        BootHost host = (BootHost)obj;
        Object[] one = new Object[9];
        one[0] = host;
        one[1] = new GeneralBrowserTableCell(
            -1,host.getName(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,host.getIP(),JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,host.getClnt_pri_ip(),JLabel.LEFT
        );
        one[4] = new GeneralBrowserTableCell(
            -1,host.getClnt_vip(),JLabel.LEFT
        );
        one[5] = new GeneralBrowserTableCell(
            -1,host.getPort()+"",JLabel.LEFT
        );
        one[6] = new GeneralBrowserTableCell(
            -1,host.getOS(),JLabel.LEFT
        );
        one[7] = new GeneralBrowserTableCell(
            -1,host.getMachine(),JLabel.LEFT
        );
        one[8] = new GeneralBrowserTableCell(
            -1,host.getUUID(),JLabel.LEFT
        );
        table.insertRow(one);
    }
}
