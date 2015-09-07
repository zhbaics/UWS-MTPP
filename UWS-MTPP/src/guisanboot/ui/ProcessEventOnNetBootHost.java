/*
 * ProcessEventOnNetBootHost.java
 *
 * Created on July 28, 2008, 3:25 PM
 */

package guisanboot.ui;
import guisanboot.data.BasicVDisk;
import guisanboot.data.DestAgent;
import guisanboot.data.SnapUsage;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.res.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnNetBootHost extends GeneralProcessEventForSanBoot {
    /** Creates a new instance of ProcessEventOnNetBootHost */
    public ProcessEventOnNetBootHost(){
        this( null );
    }
    public ProcessEventOnNetBootHost( SanBootView view ) {
        super( ResourceCenter.TYPE_DEST_AGENT,view );
    }
    
    private void showSnapUsageList( BrowserTreeNode nbhNode,int eventType ){
        setupTableList();
        
        DestAgent da = (DestAgent)nbhNode.getUserObject();
        ArrayList list = view.initor.mdb.getMSUFromCacheOnDstAgntID( da.getDst_agent_id() );
        int size = list.size();
        for( int i=0; i<size; i++ ){
            SnapUsage su = (SnapUsage)list.get(i);
            insertSomethingToTable( su );
        }
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        BrowserTreeNode nbhNode = (BrowserTreeNode)path.getLastPathComponent();
        if( nbhNode != null ){
            showSnapUsageList( nbhNode, Browser.TREE_SELECTED_EVENT );
        }
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        SnapUsage su =(SnapUsage)obj;
        
        Object[] one = new Object[2];
        one[0] = su;
        
        int tid  = su.getSnapTid();
        if( tid == -1  ){
            one[1] = new GeneralBrowserTableCell(
                -1, SanBootView.res.getString("common.noprotected"), JLabel.LEFT
            );
        }else if( su.getSnap_view_local_id() == -1 ){
            one[1] = new GeneralBrowserTableCell(
                -1, SanBootView.res.getString("common.vol") + ":" + tid, JLabel.LEFT
            );
        }else if( su.getSnap_view_local_id() == -2 ){
            one[1] = new GeneralBrowserTableCell(
                -1, SanBootView.res.getString("common.cdisk") + ":" +tid,JLabel.LEFT
            );
        }else{
            String crttime = BasicVDisk.getCreateTimeStr( su.getCrtTime() );
            if( !crttime.equals("") ){
                one[1] = new GeneralBrowserTableCell(
                    -1,SanBootView.res.getString("common.view") + ":" + tid + " [" + crttime + "]",JLabel.LEFT
                );
            }else{
                one[1] = new GeneralBrowserTableCell(
                    -1,SanBootView.res.getString("common.view") + ":" + tid ,JLabel.LEFT
                );
            }
        }

        table.insertRow( one );
    }
    
    public void setupTableList(){    
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.volmap.fs"),
            SanBootView.res.getString("View.table.volmap.bootVer")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,135}, {1,285}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }    
}
