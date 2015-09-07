/*
 * ProcessEventOnChiefCloneDisk.java
 *
 * Created on December 21, 2009, 1:40 PM
 */

package guisanboot.unlimitedIncMj.service;

import guisanboot.ui.*;
import javax.swing.*;
import javax.swing.tree.*;
import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;
import guisanboot.unlimitedIncMj.entity.ChiefCloneDiskList;
import guisanboot.unlimitedIncMj.model.GetCloneDiskListThread;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;

/**
 *
 * @author  Administrator
 */
public class ProcessEventOnChiefCloneDisk extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefCloneDiskNode = null;
    Object hostObj;

    /** Creates a new instance of ProcessEventOnChiefUnlimitedIncMirrorVol */
    public ProcessEventOnChiefCloneDisk( ){
        this( null );
    }
    public ProcessEventOnChiefCloneDisk(SanBootView view ) {
        super( ResourceCenter.TYPE_CHIEF_CLONE_DISK,view );
    }
    
    private void showCloneDiskList( BrowserTreeNode chiefCloneDiskNode,int eventType ){
        curChiefCloneDiskNode = chiefCloneDiskNode;

        ChiefCloneDiskList chiefCloneDiskList = (ChiefCloneDiskList)chiefCloneDiskNode.getUserObject();
        BrowserTreeNode fNode = chiefCloneDiskList.getFatherNode();
        Object fObj = fNode.getUserObject();

        int src_disk_root_id = 0;
        int src_host_id=-1;
        int src_host_type=-1;
        if( fObj instanceof Volume ){
            src_disk_root_id = ((Volume)fObj).getSnap_root_id();
            src_host_type = CloneDisk.IS_FREEVOL;
        }else if( fObj instanceof MirrorDiskInfo ){
            MirrorDiskInfo mdi = (MirrorDiskInfo)fObj;
            BrowserTreeNode fn = mdi.getFatherNode();
            Object fo = fn.getUserObject();
            if( fo instanceof ChiefHostVolume ){
                hostObj = view.getHostObjFromChiefHostVol( fn );
                if( hostObj != null ){
                    if( hostObj instanceof SourceAgent ){
                        src_host_id = ((SourceAgent)hostObj).getSrc_agnt_id();
                        src_host_type =CloneDisk.IS_SRCAGNT;
                        src_disk_root_id = mdi.getSnap_rootid();
                    }else{
    SanBootView.log.error(getClass().getName(), "This place need srcagent!!!");
                        return;
                    }
                }else{
    SanBootView.log.error(getClass().getName(), "This place need srcagent!!!");
                    return;
                }
            }else{
                src_host_type =CloneDisk.IS_REMOTE_FREEVOL;
                src_disk_root_id = mdi.getSnap_rootid();
            }
        }else if( fObj instanceof LogicalVol ){
            // logicalvol一定要在volumemap之前，因为前者是从后者继承过来的
            LogicalVol lv = (LogicalVol)fObj;
            BrowserTreeNode fn = lv.getFatherNode();
            hostObj = view.getHostObjFromChiefHostVol( fn );
            if( hostObj != null ){
                if( hostObj instanceof BootHost ){
                    src_host_id = ((BootHost)hostObj).getID();
                    src_host_type = CloneDisk.IS_BOOTHOST;
                }else{
SanBootView.log.error(getClass().getName(), "This place need BootHost!!");
                    return;
                }
            }else{
SanBootView.log.error(getClass().getName(), "This place need BootHost!!");
                    return;                
            }
            VolumeMap tgt = view.initor.mdb.getTargetOnLV( lv );
            src_disk_root_id = tgt.getVol_rootid();
        }else if( fObj instanceof VolumeMap ){
            VolumeMap volMap = (VolumeMap)fObj;
            BrowserTreeNode fn = volMap.getFatherNode();
            hostObj = view.getHostObjFromChiefHostVol( fn );
            if( hostObj != null ){
                if( hostObj instanceof BootHost ){
                    src_host_id = ((BootHost)hostObj).getID();
                    src_host_type = CloneDisk.IS_BOOTHOST;
                    src_disk_root_id = volMap.getVol_rootid();
                }else{
SanBootView.log.error(getClass().getName(), "This place need BootHost!!");
                    return;
                }
            }else{
SanBootView.log.error(getClass().getName(), "This place need BootHost!!");
                return;
            }
        }else {
            return ;
        }

        if( src_disk_root_id == -1 ) return;
//System.out.println("<ProcessEventOnChiefUnlimitedIncMirrorVol> rootid: "+ rootid );

        GetCloneDiskListThread thread = new GetCloneDiskListThread(
            view,
            chiefCloneDiskNode,
            this,
            eventType,
            src_host_id,
            src_host_type,
            src_disk_root_id
        );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.getCloneDisk"),
            SanBootView.res.getString("View.pdiagTip.getCloneDisk"),
            thread
        );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefCloneDiskNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefCloneDiskNode != null ){
            this.showCloneDiskList( chiefCloneDiskNode,Browser.TREE_SELECTED_EVENT );
        } 
    }

    @Override public void processTreeExpand(TreePath path){
        // 布置table中的内容
        BrowserTreeNode chiefCloneDiskNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefCloneDiskNode != null ){
            this.showCloneDiskList( chiefCloneDiskNode,Browser.TREE_EXPAND_EVENT );
        }  
    }
    
    public void realDoTableDoubleClick(Object cell){
        // do nothing
    }
    
    public void setupTableList(){
        Object[] title = new Object[]{
            SanBootView.res.getString("View.table.clonedisk.id"),
            SanBootView.res.getString("View.table.clonedisk.label"),
            SanBootView.res.getString("View.table.clonedisk.crttime"),
            SanBootView.res.getString("View.table.clonedisk.tid"),
            SanBootView.res.getString("View.table.clonedisk.desc"),
        };
        table.setupTitle( title );

        int[][] widthList = new int[][]{
            {0,70},{1,85},{2,155},{3,90},{4,250}
        };
        table.setupTableColumnWidth( widthList );
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        CloneDisk cloneDisk = (CloneDisk)obj;
        cloneDisk.setFatherNode( this.curChiefCloneDiskNode );
        Object[] one = new Object[5];
        one[0] = cloneDisk;
        if( cloneDisk.isBelongedFreeVol() || cloneDisk.isBelongedRemoteFreeVol()  ){
            one[1] = new GeneralBrowserTableCell( -1,"",JLabel.LEFT );
        }else{
            one[1] = new GeneralBrowserTableCell( -1,cloneDisk.getLabel(),JLabel.LEFT );
        }
        one[2] = new GeneralBrowserTableCell( -1,cloneDisk.getCrtTimeString(),JLabel.LEFT );
        one[3] = new GeneralBrowserTableCell( -1,cloneDisk.getTarget_id()+"",JLabel.LEFT );
        one[4] = new GeneralBrowserTableCell( -1,cloneDisk.getDesc(),JLabel.LEFT );
        table.insertRow(one);
    }
}
