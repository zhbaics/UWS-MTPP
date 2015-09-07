/*
 * ProcessEventOnChiefHost.java
 *
 * Created on December 10, 2004, 3:25 PM
 */

package guisanboot.ui;

import guisanboot.cluster.entity.Cluster;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefHost extends GeneralProcessEventForSanBoot {

    /** Creates a new instance of ProcessEventOnChiefHost */
    public ProcessEventOnChiefHost() {
        this( null );
    }
    
    public ProcessEventOnChiefHost(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_HOST_INDEX,view );
    }
    
    // 将树上的host列表显示在table中. 因为树上的host节点的user object
    // 中保留了相应的树节点和父节点的信息,所以当点击table中的某个对象进行
    // 操作时，就可以得到相关的所有信息，从而保证操作(在MenuAndBtnCenter中)
    // 能够顺利进行。
    private void showHostList( BrowserTreeNode chiefHostNode ){
        removeAllRow();
        setupTableList();
        
        int childCnt = chiefHostNode.getChildCount();
        for( int i=0;i<childCnt;i++ ){
            BrowserTreeNode node = (BrowserTreeNode)chiefHostNode.getChildAt(i);
            insertSomethingToTable( node.getUserObject() );
        }
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefHostNode = (BrowserTreeNode)path.getLastPathComponent();
        showHostList( chiefHostNode );   
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.host.id"),
            SanBootView.res.getString("View.table.host.name"),
            SanBootView.res.getString("View.table.host.ip"),
            SanBootView.res.getString("View.table.host.port"),
            SanBootView.res.getString("View.table.host.protecttype"),
            SanBootView.res.getString("View.table.host.os"),
            SanBootView.res.getString("View.table.host.machine"),
            SanBootView.res.getString("View.table.host.inited"),
            SanBootView.res.getString("View.table.host.bootMode"),
            SanBootView.res.getString("View.table.host.uuid")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,55}, {1,160}, {2,115},{3,55},
            {4,70},{5,100},{6,100},{7,80}, {8,90},  {9,250}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        Object[] one;

        if( obj instanceof BootHost ){
            BootHost hostUsrObj =(BootHost)obj;

            one = new Object[10];
            one[0] = hostUsrObj;

            one[1] = new GeneralBrowserTableCell(
                -1,hostUsrObj.getName(),JLabel.LEFT
            );
            one[2] = new GeneralBrowserTableCell(
                -1,hostUsrObj.getIP(),JLabel.LEFT
            );
            one[3] = new GeneralBrowserTableCell(
                -1,hostUsrObj.getPort()+"",JLabel.LEFT
            );
            one[4] = new GeneralBrowserTableCell(
                -1,hostUsrObj.getProtectString(),JLabel.LEFT
            );
            one[5] = new GeneralBrowserTableCell(
                -1,hostUsrObj.getOS(),JLabel.LEFT
            );
            one[6] = new GeneralBrowserTableCell(
                -1,hostUsrObj.getMachine() ,JLabel.LEFT
            );
            one[7] = new GeneralBrowserTableCell(
                -1,hostUsrObj.isInited()?"YES":"NO", JLabel.LEFT
            );
            one[8] = new GeneralBrowserTableCell(
                -1,hostUsrObj.getBootModeString(),JLabel.LEFT
            );
            one[9] = new GeneralBrowserTableCell(
                -1,hostUsrObj.getUUID(),JLabel.LEFT
            );
        }else{
            Cluster cluster = (Cluster)obj;

            one = new Object[10];
            one[0] = cluster;

            one[1] = new GeneralBrowserTableCell(
                -1,cluster.getCluster_name(),JLabel.LEFT
            );
            one[2] = new GeneralBrowserTableCell(
                -1,cluster.getClusterIP(),JLabel.LEFT
            );
            one[3] = new GeneralBrowserTableCell(
                -1,cluster.getClusterPort(),JLabel.LEFT
            );
            one[4] = new GeneralBrowserTableCell(
                -1,cluster.getProtectString(),JLabel.LEFT
            );
            one[5] = new GeneralBrowserTableCell(
                -1,cluster.getClusterOS(),JLabel.LEFT
            );
            one[6] = new GeneralBrowserTableCell(
                -1,cluster.getClusterMachine() ,JLabel.LEFT
            );
            one[7] = new GeneralBrowserTableCell(
                -1,cluster.isInited()?"YES":"NO", JLabel.LEFT
            );
            one[8] = new GeneralBrowserTableCell(
                -1,cluster.getBootModeString(),JLabel.LEFT
            );
            one[9] = new GeneralBrowserTableCell(
                -1,cluster.getClusterUUID(),JLabel.LEFT
            );
        }
        table.insertRow( one );
    }
}
