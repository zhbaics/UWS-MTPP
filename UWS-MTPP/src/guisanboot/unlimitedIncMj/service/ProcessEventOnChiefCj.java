/*
 * ProcessEventOnChiefCj.java
 *
 * Created on Janu 25, 2010, 5:41 PM
 */

package guisanboot.unlimitedIncMj.service;

import guisanboot.ui.*;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;
import guisanboot.unlimitedIncMj.entity.ChiefCopyJobList;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefCj extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefCjNode = null;
    
    /** Creates a new instance of ProcessEventOnChiefCj */
    public ProcessEventOnChiefCj( ){
        this( null );
    }
    public ProcessEventOnChiefCj(SanBootView view ) {
        super( ResourceCenter.TYPE_CHIEF_COPY_JOB,view );
    }
    
    private void showCjList( BrowserTreeNode chiefCjNode,int eventType ){
        curChiefCjNode = chiefCjNode;
        
        ChiefCopyJobList chiefCj = (ChiefCopyJobList)chiefCjNode.getUserObject();
        BrowserTreeNode fNode = chiefCj.getFatherNode();
        Object fObj = fNode.getUserObject();
        
        int rootid = 0;
        if( fObj instanceof Volume ){
            rootid = ((Volume)fObj).getSnap_root_id();
        }else if( fObj instanceof LogicalVol ){
            // logicalvol一定要在volumemap之前，因为前者是从后者继承过来的
            VolumeMap tgt = view.initor.mdb.getTargetOnLV( (LogicalVol)fObj );
            rootid = tgt.getVol_rootid();
        }else if( fObj instanceof VolumeMap ){
            rootid = ((VolumeMap)fObj).getVol_rootid();
        }else if( fObj instanceof MirrorDiskInfo ){
            rootid = ((MirrorDiskInfo)fObj).getSnap_rootid();
        }else {
            return ;
        }
        
        if( rootid == 0 ) return;
System.out.println("<ProcessEventOnChiefMj> rootid: "+ rootid );
        
        GetMirrorJobThread thread = new GetMirrorJobThread(
            view, 
            chiefCjNode,
            this,
            eventType, 
            rootid,
            true
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getCj"),
            SanBootView.res.getString("View.pdiagTip.getCj"),
            thread
        );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefCJNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefCJNode != null ){
            showCjList( chiefCJNode,Browser.TREE_SELECTED_EVENT );
        } 
    }
   
    @Override public void processTreeExpand(TreePath path){
        // 布置table中的内容
        BrowserTreeNode chiefCjNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefCjNode != null ){
            showCjList( chiefCjNode,Browser.TREE_EXPAND_EVENT );
        }  
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.mj.id"),
            SanBootView.res.getString("View.table.mj.name"),
            SanBootView.res.getString("View.table.mj.type"),
            SanBootView.res.getString("View.table.mj.status"),
            SanBootView.res.getString("View.table.mj.swu_ip"),
            SanBootView.res.getString("View.table.mj.swu_port"),
            SanBootView.res.getString("View.table.mj.swu_pool"),        
            SanBootView.res.getString("View.table.mj.opt"),
            SanBootView.res.getString("View.table.mj.desc")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
          {0,75},{1,120}, {2,85},{3,55}, {4,160},
          {5,160},{6,160},{7,360},{8,120}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        MirrorJobWrapper mj =(MirrorJobWrapper)obj;
        
        mj.mj.setFatherNode( curChiefCjNode );
        
        Object[] one = new Object[9];
        one[0] = mj.mj;

        one[1] = new GeneralBrowserTableCell(
            -1,mj.mj.getMj_job_name(),JLabel.LEFT
        );

        one[2] = new GeneralBrowserTableCell(
            -1,MirrorJob.getTypeString( mj.mj.getMj_job_type() ),JLabel.LEFT
        );

        one[3] = new GeneralBrowserTableCell(
            -1,mj.mj.getMjStatusStr(),JLabel.LEFT
        );
        one[4] = new GeneralBrowserTableCell(
            -1,mj.mj.getMj_dest_ip(),JLabel.LEFT
        );
        one[5] = new GeneralBrowserTableCell(
            -1,mj.mj.getMj_dest_port()+"",JLabel.LEFT
        );
        one[6] = new GeneralBrowserTableCell(
            -1,mj.pool_info, JLabel.LEFT
        );
        one[7] = new GeneralBrowserTableCell(
            -1,mj.mj.getMj_trans_optStr(),JLabel.LEFT
        );
        one[8] = new GeneralBrowserTableCell(
            -1,mj.mj.getMj_desc(),JLabel.LEFT
        );
     
        table.insertRow(one);
    }
}
