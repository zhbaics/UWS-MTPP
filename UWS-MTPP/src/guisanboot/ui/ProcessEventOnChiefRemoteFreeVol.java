/*
 * ProcessEventOnChiefRemoteFreeVol.java
 *
 * Created on July 14, 2008, 3:25 PM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefRemoteFreeVol extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefFreeMirrorVolNode = null; 
            
    /** Creates a new instance of ProcessEventOnChiefRemoteFreeVol */
    public ProcessEventOnChiefRemoteFreeVol(){
        this( null );
    }
    public ProcessEventOnChiefRemoteFreeVol(SanBootView view) {
        super( ResourceCenter.TYPE_CHIEF_REMOTE_VOL,view );
    }
    
    private void showFreeMirrorVolList( BrowserTreeNode chiefFreeMirrorVolNode,int eventType ){
        curChiefFreeMirrorVolNode = chiefFreeMirrorVolNode;
        
        ChiefRemoteFreeVolume chiefRFreeVol = (ChiefRemoteFreeVolume)chiefFreeMirrorVolNode.getUserObject();
        BrowserTreeNode fNode = (BrowserTreeNode)chiefRFreeVol.getFatherNode();
        SrcUWSrvNodeWrapper srcUWSrv =(SrcUWSrvNodeWrapper)fNode.getUserObject();
        int src_uws_id = srcUWSrv.getMetaData().getUws_id();
        
        GetFreeMirrorVolNodeUnderChiefRMirrorVol thread = new GetFreeMirrorVolNodeUnderChiefRMirrorVol(
            view, 
            chiefFreeMirrorVolNode,
            this,
            eventType, 
            src_uws_id
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getFreeMirrorVol"),
            SanBootView.res.getString("View.pdiagTip.getFreeMirrorVol"),
            thread
        );
    }
     
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
         
        view.removeRightPane();
        view.addTable( false );
        
        // 布置table中的内容
        BrowserTreeNode chiefFreeMirrorVolNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefFreeMirrorVolNode != null ){
            showFreeMirrorVolList( chiefFreeMirrorVolNode,Browser.TREE_SELECTED_EVENT );   
        }
    }
    
    @Override public void processTreeExpand(TreePath path){
        // 布置table中的内容
        BrowserTreeNode chiefFreeMirrorVolNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefFreeMirrorVolNode != null ){
            showFreeMirrorVolList( chiefFreeMirrorVolNode,Browser.TREE_EXPAND_EVENT );
        }  
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void insertSomethingToTable( Object obj ){
        MirrorDiskInfo mdi = (MirrorDiskInfo)obj;
        mdi.setFatherNode( curChiefFreeMirrorVolNode );
        
        Object[] one = new Object[3];
        one[0] = mdi;
        one[1] =  new GeneralBrowserTableCell(
            -1,mdi.getSrc_snap_name(),JLabel.LEFT
        );
        one[2] = new GeneralBrowserTableCell(
            -1,MirrorJob.getTypeString( mdi.getVolumeType() ),JLabel.LEFT
        );

        table.insertRow( one );
    }
    
    public void setupTableList(){    
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.vol.id"),
            SanBootView.res.getString("View.table.vol.name"),
            SanBootView.res.getString("View.table.volmap.type")
        };
        table.setupTitle( title );
        
        int[][] widthList = new int[][]{
            {0,70},{1,160},{2,135}
        };
        table.setupTableColumnWidth(widthList);
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }    
}
