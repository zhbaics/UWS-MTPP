/*
 * ProcessEventOnMJ.java
 *
 * Created on July 13, 2008, 3:25 PM
 */

package guisanboot.ui;

import guisanboot.data.LogicalVol;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.MirrorJob;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnMJ extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curMjNode;
    
    /** Creates a new instance of ProcessEventOnMJ */
    public ProcessEventOnMJ(){
        this( null );
    }
    public ProcessEventOnMJ(SanBootView view) {
        super( ResourceCenter.TYPE_MIRROR_JOB,view );
    }
    
    private void showSnapList( BrowserTreeNode mjNode,int eventType ){ 
        int root_id;
        
        MirrorJob mj = (MirrorJob)mjNode.getUserObject();
        BrowserTreeNode chiefMjNode = mj.getFatherNode();
        ChiefMirrorJobList chiefMj = (ChiefMirrorJobList)chiefMjNode.getUserObject();
        BrowserTreeNode volNode = chiefMj.getFatherNode();
        Object volObj = volNode.getUserObject();
                
        if( volObj instanceof Volume ){
            Volume vol = (Volume)volObj;
            root_id = vol.getSnap_root_id();
        }else if( volObj instanceof LogicalVol ){
            LogicalVol lv =(LogicalVol)volObj;
            VolumeMap tgt = view.initor.mdb.getTargetOnLV( lv );
            root_id = tgt.getVol_rootid();
        }else if( volObj instanceof MirrorDiskInfo ){
            MirrorDiskInfo mdi = (MirrorDiskInfo)volObj;
            root_id = mdi.getSnap_rootid();
        }else if( volObj instanceof VolumeMap ){
            VolumeMap volMap = (VolumeMap)volObj;
            root_id = volMap.getVol_rootid();
        }else{
            return;
        }
          
        GetMirrorSnapProgress thread = new GetMirrorSnapProgress( 
            view, 
            mjNode,
            this,
            eventType,
            root_id,
            mj.getMj_id(),
            mj.getMj_job_type()
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getMirrorProcess"),
            SanBootView.res.getString("View.pdiagTip.getMirrorProcess"), 
            thread
        );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
         
        view.removeRightPane();
        view.addTable();
        
        BrowserTreeNode mjNode = (BrowserTreeNode)path.getLastPathComponent();
        if( mjNode != null ){
            showSnapList( mjNode,Browser.TREE_SELECTED_EVENT );   
        }
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        MirroredSnapView snap = (MirroredSnapView)obj;
        snap.setFatherNode( curMjNode );
        
        Object[] one = new Object[4];
        one[0] = snap;
        
        one[1] =  new GeneralBrowserTableCell(
            -1,snap.snap.toString(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,snap.snap.getCreateTimeStr(),JLabel.LEFT
        );
        one[3] = new GeneralBrowserTableCell(
            -1,"100%" ,JLabel.LEFT
        );
        table.insertRow( one );
    }
    
    public void setupTableList(){    
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.snap.snapid"),
            SanBootView.res.getString("View.table.snap.name"),
            SanBootView.res.getString("View.table.snap.crtTime"),
            SanBootView.res.getString("View.table.snap.finish"),
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,100},  {1,165}, {2,155}, {3,65}
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }    
}
