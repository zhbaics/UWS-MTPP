/*
 * ProcessEventOnProfile.java
 *
 * Created on Aug 7, 2008, 14:33 PM
 */

package guisanboot.datadup.ui.ProcessEvent;
import guisanboot.datadup.data.DBSchedule;
import guisanboot.datadup.data.UniProfile;
import guisanboot.datadup.ui.GetSchThread;
import javax.swing.tree.*;
import guisanboot.res.*;
import guisanboot.ui.GeneralProcessEventForSanBoot;
import guisanboot.ui.SanBootView;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;
import mylib.UI.GeneralBrowserTableCell;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnProfile extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curProfileNode = null;
    
    /** Creates a new instance of ProcessEventOnProfile */
    public ProcessEventOnProfile(){
        this( null );
    }
    public ProcessEventOnProfile(SanBootView view) {
        super( ResourceCenter.TYPE_PROF,view );
    }
    
    private void showSchList( BrowserTreeNode profileNode,int eventType ){        
        curProfileNode = profileNode;
        
        UniProfile prof = (UniProfile)profileNode.getUserObject();
        GetSchThread thread = new GetSchThread(
            view, 
            profileNode, 
            prof.getProfileName(), 
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
        BrowserTreeNode profileNode = (BrowserTreeNode)path.getLastPathComponent();
        if( profileNode != null ){
            showSchList( profileNode,Browser.TREE_SELECTED_EVENT );   
        } 
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        DBSchedule sch =(DBSchedule)obj; 
        sch.setFatherNode( curProfileNode );
        
        Object[] one = new Object[3];
        one[0] = sch;

        one[1] = new GeneralBrowserTableCell(
            -1, sch.isEnable()?"YES":"NO" ,JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,sch.getStartTime(),JLabel.LEFT
        );
        table.insertRow(one);
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.scheduler.name"),
            SanBootView.res.getString("View.table.scheduler.enable"),
            SanBootView.res.getString("View.table.scheduler.startTime")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,165}, {1,65},{2,130}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
}
