/*
 * ProcessEventOnChiefSnap.java
 *
 * Created on December 10, 2004, 3:25 PM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefSnap extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefSnapNode = null;
    
    /** Creates a new instance of ProcessEventOnChiefSnap */
    public ProcessEventOnChiefSnap(){
        this( null );
    }
    public ProcessEventOnChiefSnap(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_SNAP_INDEX,view );
    }
    
    private void showSnapList( BrowserTreeNode chiefSnapNode,int eventType ){
        curChiefSnapNode = chiefSnapNode;
        
        ChiefSnapshot chiefSnap = (ChiefSnapshot)chiefSnapNode.getUserObject();
        BrowserTreeNode fNode = chiefSnap.getFatherNode();
        Object fObj = fNode.getUserObject();
        
        int rootid = 0;
        boolean isCMDP = false;
        if( fObj instanceof Volume ){
            rootid = ((Volume)fObj).getSnap_root_id();
        }else if( fObj instanceof LogicalVol ){
            // logicalvol一定要在volumemap之前，因为前者是从后者继承过来的
            VolumeMap tgt = view.initor.mdb.getTargetOnLV( (LogicalVol)fObj );
            rootid = tgt.getVol_rootid();
        }else if( fObj instanceof VolumeMap ){
            rootid = ((VolumeMap)fObj).getVol_rootid();
            isCMDP = ( ((VolumeMap)fObj).isCMDPProtect() && ((VolumeMap)fObj).getVolDiskLabel().startsWith("C:") );
        }else if( fObj instanceof MirrorDiskInfo ){
            rootid = ((MirrorDiskInfo)fObj).getSnap_rootid();
            isCMDP = ( ((MirrorDiskInfo)fObj).isCMDPProtect() && ((MirrorDiskInfo)fObj).getSrc_agent_mp().startsWith("C") );
        }else if( fObj instanceof CloneDisk ){
            rootid = ((CloneDisk)fObj).getRoot_id();
        }else {
            return ;
        }
        
        if( rootid == 0 ) return;
        
        GetSnapDirectlyUnderDisk thread = new GetSnapDirectlyUnderDisk(
            view, 
            chiefSnapNode,
            this,
            eventType,
            rootid,
            isCMDP
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getSnap"),
            SanBootView.res.getString("View.pdiagTip.getSnap"), 
            thread
        );
    }
    
    @Override public void processTreeBothEvent(TreePath path){
        if ( !view.initor.isLogined() ) return;
         
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefSnapNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefSnapNode != null ){
            showSnapList( chiefSnapNode,Browser.TREE_BOTH_EVENT );   
        }
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
         
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefSnapNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefSnapNode != null ){
            showSnapList( chiefSnapNode,Browser.TREE_SELECTED_EVENT );   
        }
    }
    
    @Override public void processTreeExpand(TreePath path){
        // 布置table中的内容
        BrowserTreeNode chiefSnapNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefSnapNode != null ){
            showSnapList( chiefSnapNode,Browser.TREE_EXPAND_EVENT );
        }  
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        Object[] one;
        
        if( obj instanceof Snapshot ){
            Snapshot snap = (Snapshot)obj;

            snap.setFatherNode( curChiefSnapNode );

            one = new Object[4];
            one[0] = snap;

            one[1] =  new GeneralBrowserTableCell(
                -1,snap.toString(),JLabel.LEFT
            );

            view.initor.mdb.getSnapSize( String.valueOf(snap.getSnap_root_id( )) ,String.valueOf(snap.getSnap_local_snapid()) );
            String size = view.initor.mdb.getSsize();
            one[2] = new GeneralBrowserTableCell(
                -1,size,JLabel.LEFT
            );

            one[3] = new GeneralBrowserTableCell(
                -1,snap.getCreateTimeStr(),JLabel.LEFT
            );
        }else{
            DeletingSnapshot snap1 = (DeletingSnapshot)obj;

            snap1.setFatherNode( curChiefSnapNode );

            one = new Object[3];
            one[0] = snap1;

            one[1] =  new GeneralBrowserTableCell(
                -1,snap1.toString(),JLabel.LEFT
            );
            one[2] = new GeneralBrowserTableCell(
                -1,"N/A",JLabel.LEFT
            );
            one[3] = new GeneralBrowserTableCell(
                -1,snap1.getCreateTimeStr(),JLabel.LEFT
            );
        }
        
        table.insertRow( one );
    }
    
    public synchronized void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.snap.snapid"),
            SanBootView.res.getString("View.table.snap.name"),
            SanBootView.res.getString("View.table.snap.size"),
            SanBootView.res.getString("View.table.snap.crtTime")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,100},  {1,165}, {2,155}, {3,155}
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }    
}
