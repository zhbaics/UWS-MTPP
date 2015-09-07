/*
 * ProcessEventOnChiefProfile.java
 *
 * Created on Aug 6, 2008, 3:25 PM
 */

package guisanboot.datadup.ui.ProcessEvent;
import guisanboot.cluster.entity.Cluster;
import javax.swing.*;
import javax.swing.tree.*;

import mylib.UI.*;
import guisanboot.datadup.ui.viewobj.ChiefProfile;
import guisanboot.datadup.data.BackupClient;
import guisanboot.datadup.data.UniProfile;
import guisanboot.res.*;
import guisanboot.ui.GeneralProcessEventForSanBoot;
import guisanboot.datadup.ui.GetProfileThread;
import guisanboot.ui.SanBootView;
import guisanboot.data.BootHost;
import guisanboot.datadup.data.UniProBackup;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefProfile extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefProfNode = null;
    
    /** Creates a new instance of ProcessEventOnChiefProfile */
    public ProcessEventOnChiefProfile(){
        this( null );
    }
    public ProcessEventOnChiefProfile(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_PROF,view );
    }
    
    private void showProfList( BrowserTreeNode chiefProfNode,int eventType ){
        curChiefProfNode = chiefProfNode;
        
        ChiefProfile chiefProf = (ChiefProfile)chiefProfNode.getUserObject();
        BrowserTreeNode hostNode = chiefProf.getFatherNode();
        long cid = -1;
        Cluster cluster = null;
        if( hostNode != null ){
            Object uObj = hostNode.getUserObject();
            if( uObj instanceof BootHost ){
                BootHost bootHost = (BootHost)uObj;
                BackupClient bkClnt = view.initor.mdb.getBkClntOnUUID( bootHost.getUUID()   );
                if( bkClnt != null ){
                    cid = bkClnt.getID();
                }
            }else{
                cid = 0; // 反正不能为-1 (2011.7.20)
                cluster = (Cluster)uObj;
            }
        }
        
        GetProfileThread thread = new GetProfileThread(
            view, 
            chiefProfNode,
            cid,
            cluster,
            ResourceCenter.CMD_TYPE_MTPP,
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
            SanBootView.res.getString("View.table.profile.name"),
            SanBootView.res.getString("View.table.profile.sch"),
            SanBootView.res.getString("View.table.profile.content")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,220},{1,65},{2,475}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        UniProfile prof =(UniProfile)obj;  
        prof.setFatherNode( curChiefProfNode );
        
        Object[] one = new Object[3];
        one[0] = prof;
        
        one[1] =  new GeneralBrowserTableCell(
            -1,view.initor.mdb.getSchNumOnProfName( prof.getProfileName() )+"",JLabel.LEFT
        );
        UniProBackup backup = prof.getUniProBackup();
        one[2] = new GeneralBrowserTableCell(
            -1,backup.getSrc(),JLabel.LEFT
        );
        
        table.insertRow(one);
    }
}
