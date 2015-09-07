/*
 * ProcessEventOnChiefSch.java
 *
 * Created on July 25, 2008, 3:25 PM
 */

package guisanboot.datadup.ui.ProcessEvent;

import guisanboot.datadup.data.*;
import guisanboot.datadup.ui.GetSchThread;
import javax.swing.*;
import javax.swing.tree.*;

import mylib.UI.*;
import guisanboot.res.*;
import guisanboot.ui.GeneralProcessEventForSanBoot;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefSch extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefSchNode = null;

    /** Creates a new instance of ProcessEventOnChiefSch */
    public ProcessEventOnChiefSch(){
        this( null );
    }
    public ProcessEventOnChiefSch(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_SCH,view );
    }
    
    private void showSchList( BrowserTreeNode chiefSchNode,int eventType ){        
        curChiefSchNode = chiefSchNode;
        
        GetSchThread thread = new GetSchThread(
            view, 
            chiefSchNode,
            null,
            this,
            eventType
        );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.getSch"),
            SanBootView.res.getString("View.pdiagTip.getSch"),
            thread
        );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefSchNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefSchNode != null ){
            showSchList( chiefSchNode,Browser.TREE_SELECTED_EVENT );   
        } 
    }
   
    @Override public void processTreeExpand(TreePath path){
        // 布置table中的内容�
        BrowserTreeNode chiefSchNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefSchNode != null ){
            showSchList( chiefSchNode,Browser.TREE_EXPAND_EVENT );
        }  
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.scheduler.name"),
            SanBootView.res.getString("View.table.scheduler.enable"),
            SanBootView.res.getString("View.table.scheduler.startTime"),
            SanBootView.res.getString("View.table.scheduler.profileName"),
            SanBootView.res.getString("View.table.scheduler.client")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,165}, {1,65},{2,130},{3,155},{4,250}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        DBSchedule sch =(DBSchedule)obj; 
        sch.setFatherNode( curChiefSchNode );
        
        Object[] one = new Object[5];
        one[0] = sch;

        one[1] = new GeneralBrowserTableCell(
            -1, sch.isEnable()?"YES":"NO" ,JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,sch.getStartTime(),JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,sch.getSimpleProfName(),JLabel.LEFT
        );
        
        UniProfile prof = view.initor.mdb.getOneProfile( sch.getProfName() );
        String host="N/A";
        if( prof != null ){
            UniProIdentity id = prof.getUniProIdentity();
            String clt_id = id.getClntID();
            BackupClient clnt = view.initor.mdb.getClientFromVectorOnID( clt_id );
            if( clnt!= null)
                host = clnt.getHostName() + " [ " + clnt.getIP() + " ]";
        }
        one[4] = new GeneralBrowserTableCell(
            -1,host,JLabel.LEFT
        );
        table.insertRow(one);
    }
}
