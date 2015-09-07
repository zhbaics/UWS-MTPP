/*
 * ProcessEventOnCluster.java
 *
 * Created on July 14, 2011, 10:32 AM
 */

package guisanboot.cluster.ui;

import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.entity.MemberNodeService;
import guisanboot.ui.*;
import guisanboot.datadup.ui.viewobj.HostProfileService;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnCluster extends GeneralProcessEventForSanBoot {   
    /** Creates a new instance of ProcessEventOnCluster */
    public ProcessEventOnCluster(){
        this( null );
    }
    public ProcessEventOnCluster(SanBootView view) {
        super( ResourceCenter.TYPE_CLUSTER,view );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        setupTableList();
        BrowserTreeNode clusterNode = (BrowserTreeNode)path.getLastPathComponent();
        Cluster cluster = (Cluster)clusterNode.getUserObject();
        insertSomethingToTable( cluster );
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
      
    public void insertSomethingToTable( Object obj ){
        Cluster cluster = (Cluster)obj;

        Object[] one = new Object[2];
        MemberNodeService member = new MemberNodeService();
        one[0] = member;
        one[1] = new GeneralBrowserTableCell(
            -1, SanBootView.res.getString("common.desc.memberNodeList"),JLabel.LEFT
        );
        table.insertRow( one  );

        HostVolumeService service = new HostVolumeService();
        one[0] = service;
        one[1] = new GeneralBrowserTableCell(
            -1, SanBootView.res.getString("common.desc.hostVolList1"),JLabel.LEFT
        );
        table.insertRow( one  );

        if( cluster.isCMDPProtect() ){
            HostProfileService service1 = new HostProfileService( 1 );
            one[0] = service1;
            one[1] = new GeneralBrowserTableCell(
                -1, SanBootView.res.getString("common.desc.pprofList1"),JLabel.LEFT
            );
            table.insertRow( one  );
        }

        HostProfileService service2 = new HostProfileService();
        one[0] = service2;
        one[1] = new GeneralBrowserTableCell(
            -1, SanBootView.res.getString("common.desc.profList1"),JLabel.LEFT
        );
        table.insertRow( one  );
        
        NetbootedHostService service3 = new NetbootedHostService();
        one[0] = service3;
        one[1] = new GeneralBrowserTableCell(
            -1,SanBootView.res.getString("common.desc.netbootedHostList1"),JLabel.LEFT
        );
        table.insertRow( one ); 
    }
    
    public void setupTableList(){    
        Object[] title = new Object[]{
            SanBootView.res.getString("common.table.item"),
            SanBootView.res.getString("common.table.desc")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,150},  {1,375}
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    } 
}
