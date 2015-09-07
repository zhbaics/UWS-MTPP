/*
 * ProcessEventOnChiefUnlimitedIncMirrorVol.java
 *
 * Created on December 14, 2009, 11:44 AM
 */

package guisanboot.unlimitedIncMj.service;

import guisanboot.ui.*;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;
import guisanboot.unlimitedIncMj.entity.ChiefLocalUnLimitedIncMirrorVolList;
import guisanboot.unlimitedIncMj.model.GetUnlimitedIncMirrorVol;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefUnlimitedIncMirrorVol extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefUnlimitedIncMirrorVolNode = null;
    Object hostObj;

    /** Creates a new instance of ProcessEventOnChiefUnlimitedIncMirrorVol */
    public ProcessEventOnChiefUnlimitedIncMirrorVol( ){
        this( null );
    }
    public ProcessEventOnChiefUnlimitedIncMirrorVol(SanBootView view ) {
        super( ResourceCenter.TYPE_CHIEF_UNLIMITED_INC_MIRROR_VOL,view );
    }
    
    private void showUnlimitedIncMirrorVolList( BrowserTreeNode chiefUnlimitedIncMirrorVolNode,int eventType ){
        curChiefUnlimitedIncMirrorVolNode = chiefUnlimitedIncMirrorVolNode;

        ChiefLocalUnLimitedIncMirrorVolList chiefUnlimitedIncMirrorVol = (ChiefLocalUnLimitedIncMirrorVolList)chiefUnlimitedIncMirrorVolNode.getUserObject();
        BrowserTreeNode fNode = chiefUnlimitedIncMirrorVol.getFatherNode();
        Object fObj = fNode.getUserObject();
        
        int rootid = 0;
        int src_agnt_id=-1;
        if( fObj instanceof Volume ){
            rootid = ((Volume)fObj).getSnap_root_id();
        }else if( fObj instanceof LogicalVol ){
            // logicalvol一定要在volumemap之前，因为前者是从后者继承过来的
            LogicalVol lv = (LogicalVol)fObj;
            BrowserTreeNode fn = lv.getFatherNode();
            hostObj = view.getHostObjFromChiefHostVol( fn );
            if( hostObj instanceof BootHost ){
                src_agnt_id = ((BootHost)hostObj).getID();
            }else{
SanBootView.log.error(getClass().getName(), "This place need BootHost!!");
                return;
            }
            VolumeMap tgt = view.initor.mdb.getTargetOnLV( lv );
            rootid = tgt.getVol_rootid();
        }else if( fObj instanceof VolumeMap ){
            VolumeMap volMap = (VolumeMap)fObj;
            BrowserTreeNode fn = volMap.getFatherNode();
            hostObj = view.getHostObjFromChiefHostVol( fn );
            if( hostObj instanceof BootHost ){
                src_agnt_id = ((BootHost)hostObj).getID();
            }else{
SanBootView.log.error(getClass().getName(), "This place need BootHost!!");
                return;
            }
            rootid = volMap.getVol_rootid();
        }else {
SanBootView.log.error(getClass().getName(), "unknown volume type.");
            return ;
        }
        
        if( rootid == 0 ) return;
System.out.println("<ProcessEventOnChiefUnlimitedIncMirrorVol> rootid: "+ rootid + " src_agnt_id: "+ src_agnt_id );

        GetUnlimitedIncMirrorVol thread = new GetUnlimitedIncMirrorVol(
            view,
            chiefUnlimitedIncMirrorVolNode,
            this,
            eventType,
            src_agnt_id,
            rootid
        );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.getUnlitIncMirrorVol"),
            SanBootView.res.getString("View.pdiagTip.getUnlitIncMirrorVol"),
            thread
        );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefUnlimitedIncMirrorVolNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefUnlimitedIncMirrorVolNode != null ){
            showUnlimitedIncMirrorVolList( chiefUnlimitedIncMirrorVolNode,Browser.TREE_SELECTED_EVENT );
        } 
    }

    @Override public void processTreeExpand(TreePath path){
        // 布置table中的内容
        BrowserTreeNode chiefUnlimitedIncMirrorVolNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefUnlimitedIncMirrorVolNode != null ){
            showUnlimitedIncMirrorVolList( chiefUnlimitedIncMirrorVolNode,Browser.TREE_EXPAND_EVENT );
        }  
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.volmap.id"),
            SanBootView.res.getString("View.table.volmap.fs"),
            SanBootView.res.getString("View.table.volmap.name"),
            SanBootView.res.getString("View.table.volmap.type"),
        };
        table.setupTitle( title );

        int[][] widthList = new int[][]{
            {0,70},{1,85},{2,150},{3,100}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        MirrorDiskInfo mdi = (MirrorDiskInfo)obj;
        mdi.setFatherNode( this.curChiefUnlimitedIncMirrorVolNode );
        Object[] one = new Object[4];
        one[0] = mdi;
        one[1] = new GeneralBrowserTableCell( -1,mdi.getSrc_agent_mp(),JLabel.LEFT );
        one[2] = new GeneralBrowserTableCell( -1,mdi.getSrc_snap_name(),JLabel.LEFT );
        one[3] = new GeneralBrowserTableCell( -1,MirrorJob.getTypeString( mdi.getVolumeType() ),JLabel.LEFT );
        table.insertRow(one);
    }
}
