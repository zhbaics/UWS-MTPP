/*
 * ProcessEventOnChiefPPProfile.java
 *
 * Created on June 10, 2010, 10:27 AM
 */

package guisanboot.cmdp.service;

import guisanboot.cluster.entity.Cluster;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.cmdp.entity.PPProfileItem;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.datadup.ui.viewobj.ChiefProfile;
import guisanboot.res.*;
import guisanboot.ui.GeneralProcessEventForSanBoot;
import guisanboot.datadup.ui.GetProfileThread;
import guisanboot.ui.SanBootView;
import guisanboot.data.BootHost;
import guisanboot.data.VolumeMap;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefPPProfile extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefProfNode = null;
    
    /** Creates a new instance of ProcessEventOnChiefPPProfile */
    public ProcessEventOnChiefPPProfile(){
        this( null );
    }

    public ProcessEventOnChiefPPProfile(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_PPPROF,view );
    }
    
    private void showProfList( BrowserTreeNode chiefProfNode,int eventType ){
        curChiefProfNode = chiefProfNode;
        
        ChiefProfile chiefProf = (ChiefProfile)chiefProfNode.getUserObject();
        BrowserTreeNode hostNode = chiefProf.getFatherNode();
        int clntId = -1;
        Cluster cluster = null;
        if( hostNode != null ){
            Object uObj = hostNode.getUserObject();
            if( uObj instanceof BootHost ){
                BootHost bootHost = (BootHost)uObj;
                clntId = bootHost.getID();
            }else{
                clntId = 0;
                cluster = (Cluster)uObj;
            }
        }
        
        GetProfileThread thread = new GetProfileThread(
            view, 
            chiefProfNode,
            clntId,
            cluster,
            ResourceCenter.CMD_TYPE_CMDP,
            this,
            eventType
        );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.getProf"),
            SanBootView.res.getString("View.pdiagTip.getProf"),
            thread
        );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        BrowserTreeNode chiefProfNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefProfNode != null ){
            showProfList( chiefProfNode,Browser.TREE_SELECTED_EVENT );   
        } 
    }
   
    @Override public void processTreeExpand(TreePath path){
        /// 布置table中的内容�
        BrowserTreeNode chiefProfNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefProfNode != null ){
            showProfList( chiefProfNode,Browser.TREE_EXPAND_EVENT );
        }  
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.profile.content1"),
            SanBootView.res.getString("View.table.profile.autocrtversion"),
            SanBootView.res.getString("View.table.profile.minSnapSize"),
            SanBootView.res.getString("View.table.profile.dbtype"),
            SanBootView.res.getString("View.table.profile.dbinstances"),
            SanBootView.res.getString("View.table.profile.services")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,200},{1,420},{2,150},{3,150},{4,200},{5,350}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        PPProfile prof =(PPProfile)obj;
        prof.setFatherNode( curChiefProfNode );
        
        Object[] one = new Object[6];
        one[0] = prof;
        one[1] =  new GeneralBrowserTableCell(
            -1,prof.getIntervalString(),JLabel.LEFT
        );
        PPProfileItem master_disk = prof.getMainDiskItem();
        one[2] =  new GeneralBrowserTableCell(
            -1,master_disk.getMg().getMg_min_snap_size()+"",JLabel.LEFT
        );
SanBootView.log.debug(getClass().getName(), "dbtype: "+ master_disk.getVolMap().getDBType() );
        one[3] = new GeneralBrowserTableCell(
            -1,VolumeMap.getDbString( master_disk.getVolMap().getDBType() ),JLabel.LEFT
        );
        one[4] = new GeneralBrowserTableCell(
            -1,master_disk.getVolMap().getDatabase_instances(),JLabel.LEFT
        );
        one[5] = new GeneralBrowserTableCell(
            -1,master_disk.getVolMap().getChangeVerService(),JLabel.LEFT
        );
        
        table.insertRow(one);
    }
}
