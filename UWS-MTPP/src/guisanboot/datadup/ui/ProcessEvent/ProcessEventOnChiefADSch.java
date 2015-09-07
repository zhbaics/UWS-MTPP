

package guisanboot.datadup.ui.ProcessEvent;

import guisanboot.data.BootHost;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.datadup.data.*;
import guisanboot.datadup.ui.GetADSchThread;
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
public class ProcessEventOnChiefADSch extends GeneralProcessEventForSanBoot {
    public static final String AUTO_DEL_SCHEDULER_PROF ="merge_snap_util.pl";
    
    BrowserTreeNode curChiefSchNode = null;

    /** Creates a new instance of ProcessEventOnChiefADSch */
    public ProcessEventOnChiefADSch(){
        this( null );
    }
    public ProcessEventOnChiefADSch(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_ADSCH,view );
    }
    
    private void showSchList( BrowserTreeNode chiefSchNode,int eventType ){        
        curChiefSchNode = chiefSchNode;
        
        GetADSchThread thread = new GetADSchThread(
            view, 
            chiefSchNode,
            null,
            this,
            eventType
        );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.getADSch"),
            SanBootView.res.getString("View.pdiagTip.getADSch"),
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
            SanBootView.res.getString("View.table.adscheduler.name"),
            SanBootView.res.getString("View.table.adscheduler.enable"),
            SanBootView.res.getString("View.table.adscheduler.startTime"),
            SanBootView.res.getString("View.table.adscheduler.volume"),
            SanBootView.res.getString("View.table.adscheduler.client"),
            SanBootView.res.getString("View.table.adscheduler.keepnum")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,165}, {1,65},{2,130},{3,155},{4,250},{5,100}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        DBSchedule sch =(DBSchedule)obj; 
        sch.setFatherNode( curChiefSchNode );

        //必须为提前删除快照的调度才显示
        if( !sch.getSimpleProfName().trim().startsWith(AUTO_DEL_SCHEDULER_PROF) )
            return;
        String tmpstr[]   = sch.getSimpleProfName().split(" ");
        
        Object[] one = new Object[6];
        one[0] = sch;

        one[1] = new GeneralBrowserTableCell(
            -1, sch.isEnable()?"YES":"NO" ,JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,sch.getStartTime(),JLabel.LEFT
        );
    
        String host="N/A";
        String strvol = "N/A";
        int tmpvolid = Integer.parseInt(tmpstr[1]);
        Volume objVol = view.initor.mdb.getVolume(tmpvolid);
        if( objVol != null ){
            String str = objVol.getSnap_name();
            strvol = str;
            VolumeMap objVM = view.initor.mdb.getVolMapFromVectorOnName(str);
            if(objVM != null){
                long iclnt_id = objVM.getVolClntID();
                BootHost objBH = view.initor.mdb.getBootHostFromVector(iclnt_id);
                if(objBH != null){
                    host = objBH.getName();
                }
            }
        }
        one[3] = new GeneralBrowserTableCell(
            -1,strvol,JLabel.LEFT
        );
        one[4] = new GeneralBrowserTableCell(
            -1,host,JLabel.LEFT
        );
        one[5] = new GeneralBrowserTableCell(
            -1,tmpstr[2],JLabel.LEFT     
        );
        table.insertRow(one);
    }
}
