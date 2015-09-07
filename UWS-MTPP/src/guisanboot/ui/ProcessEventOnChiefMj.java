/*
 * ProcessEventOnChiefMj.java
 *
 * Created on December 10, 2004, 3:25 PM
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
public class ProcessEventOnChiefMj extends GeneralProcessEventForSanBoot {
    BrowserTreeNode curChiefMjNode = null;
    
    /** Creates a new instance of ProcessEventOnChiefMj */
    public ProcessEventOnChiefMj( ){
        this( null );
    }
    public ProcessEventOnChiefMj(SanBootView view ) {
        super( ResourceCenter.TYPE_CHIEF_MIRROR_JOB,view );
    }
    
    private void showMjList( BrowserTreeNode chiefMjNode,int eventType ){
        curChiefMjNode = chiefMjNode;
        
        ChiefMirrorJobList chiefMj = (ChiefMirrorJobList)chiefMjNode.getUserObject();
        BrowserTreeNode fNode = chiefMj.getFatherNode();
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
        
        GetMirrorJobThread thread = new GetMirrorJobThread(
            view, 
            chiefMjNode,
            this,
            eventType, 
            rootid,
            false
        );
        view.startupProcessDiag( 
            SanBootView.res.getString("View.pdiagTitle.getMj"),
            SanBootView.res.getString("View.pdiagTip.getMj"),
            thread
        );
    }
    
    @Override public void processTreeSelection(TreePath path){
        if ( !view.initor.isLogined() ) return;
        
        view.removeRightPane();
        view.addTable();
        
        // 布置table中的内容
        BrowserTreeNode chiefMJNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefMJNode != null ){
            showMjList( chiefMJNode,Browser.TREE_SELECTED_EVENT );   
        } 
    }
   
    @Override public void processTreeExpand(TreePath path){
        // 布置table中的内容
        BrowserTreeNode chiefMjNode = (BrowserTreeNode)path.getLastPathComponent();
        if( chiefMjNode != null ){
            showMjList( chiefMjNode,Browser.TREE_EXPAND_EVENT );
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
            SanBootView.res.getString("View.table.mj.toTransferNum"),
            SanBootView.res.getString("View.table.mj.mjInfo"),
            SanBootView.res.getString("View.table.mj.sch"),
            SanBootView.res.getString("View.table.mj.swu_ip"),
            SanBootView.res.getString("View.table.mj.swu_port"),
            SanBootView.res.getString("View.table.mj.swu_pool"),        
            SanBootView.res.getString("View.table.mj.opt"),
            SanBootView.res.getString("View.table.mj.desc")
        };
        table.setupTitle( title );
        
        int[]widthList = new int[]{
          75,120,85,55,95,160,140,130,130,170,360,120
        };
        table.setupTableColumnWidth( widthList );
        
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed( false );
    }
    
    public void insertSomethingToTable( Object obj ){
        MirrorJobWrapper mj =(MirrorJobWrapper)obj;
        mj.mj.setFatherNode( curChiefMjNode );

        GeneralBrowserTableCell statusCell,mjInfoCell,toTransferNum;
        if( mj.mj.getMj_mg_id() > 0 ){
            boolean isMgStart = view.initor.mdb.checkMg( mj.mj.getMj_mg_id() );
            statusCell = new GeneralBrowserTableCell(
                -1,mj.mj.getMjStatusStr( isMgStart),JLabel.LEFT
            );
        }else{
            statusCell = new GeneralBrowserTableCell(
                -1,mj.mj.getMjStatusStr(),JLabel.LEFT
            );
        }

        Object[] ret = view.getSomtthingOnTreeFromMjObj( mj.mj );
        if( ret == null ){
            toTransferNum = new GeneralBrowserTableCell(
                -1,"N/A",JLabel.CENTER
            );
        }else{
            Object hostObj = ret[0];
            int root_id = ((Integer)ret[1]).intValue();
            String sql_state;
            boolean queryOK;
            int cnt;
            if( hostObj == null ){
                sql_state = "select count(*) from vsnap where "+BasicVDisk.BVDisk_Snap_Root_ID +"="+root_id +" and "+
                        BasicVDisk.BVDisk_Snap_Local_SnapId+">"+mj.mj.getMj_current_snap_id() +" and "+BasicVDisk.BVDisk_Snap_OpenType+"="+
                        BasicVDisk.TYPE_OPENED_SNAP;
                queryOK = view.initor.mdb.queryVSnapDB1( sql_state );
                if( queryOK ){
                    cnt = view.initor.mdb.getQueryResult();
                    if( cnt == 0 ){
                        sql_state = "select count(*) from vsnap where "+BasicVDisk.BVDisk_Snap_Root_ID +"="+root_id +" and "+
                            BasicVDisk.BVDisk_Snap_Local_SnapId+">"+mj.mj.getMj_current_snap_id() +" and "+BasicVDisk.BVDisk_Snap_OpenType+"="+
                            BasicVDisk.TYPE_OPENED_CrtByMirr;
                        queryOK = view.initor.mdb.queryVSnapDB1( sql_state );
                        if( queryOK ){
                            cnt = view.initor.mdb.getQueryResult();
                            toTransferNum = new GeneralBrowserTableCell(
                                -1,cnt+" [ " + mj.mj.getMj_current_process() +"% ] ",JLabel.CENTER
                            );
                        }else{
                            toTransferNum = new GeneralBrowserTableCell(
                                -1,"N/A",JLabel.CENTER
                            );
                        }
                    }else{
                        toTransferNum = new GeneralBrowserTableCell(
                            -1,cnt+ " [ " + mj.mj.getMj_current_process() +"% ] ",JLabel.CENTER
                        );
                    }
                }else{
                    toTransferNum = new GeneralBrowserTableCell(
                        -1,"N/A",JLabel.CENTER
                    );
                }
            }else{
                boolean isMTPP=true;
                if( hostObj instanceof BootHost ){
                    isMTPP = ((BootHost)hostObj).isMTPPProtect();
                }else if( hostObj instanceof SourceAgent ){
                    isMTPP = ((SourceAgent)hostObj).isMTPPProtect();
                }
                sql_state = "select count(*) from vsnap where "+BasicVDisk.BVDisk_Snap_Root_ID +"="+root_id +" and "+
                        BasicVDisk.BVDisk_Snap_Local_SnapId+">"+mj.mj.getMj_current_snap_id() +" and "+BasicVDisk.BVDisk_Snap_OpenType+"="+
                        ( isMTPP? BasicVDisk.TYPE_OPENED_SNAP:BasicVDisk.TYPE_OPENED_CrtByMirr);
                queryOK = view.initor.mdb.queryVSnapDB1( sql_state );
                if( queryOK ){
                    cnt = view.initor.mdb.getQueryResult();
                    toTransferNum = new GeneralBrowserTableCell(
                        -1,cnt+" [ " + mj.mj.getMj_current_process() +"% ] ",JLabel.CENTER
                    );
                }else{
                    toTransferNum = new GeneralBrowserTableCell(
                        -1,"N/A",JLabel.CENTER
                    );
                }
            }
        }

        if( !mj.mj.getMj_info().equals("end") && !mj.mj.getMj_info().trim().equals("") ){
            mjInfoCell = new GeneralBrowserTableCell(
                -1,mj.mj.getMj_info(),JLabel.LEFT
            );
        }else{
            mjInfoCell = new GeneralBrowserTableCell(
                -1,"",JLabel.LEFT
            );
        }

        Object[] one = new Object[]{
            mj.mj,
            new GeneralBrowserTableCell(
                -1,mj.mj.getMj_job_name(),JLabel.LEFT
            ),
            new GeneralBrowserTableCell(
                -1,MirrorJob.getTypeString( mj.mj.getMj_job_type() ),JLabel.LEFT
            ),
            statusCell,   // work status
            toTransferNum,
            mjInfoCell,
            new GeneralBrowserTableCell(
                -1,mj.mj.getMj_scheduler()+"",JLabel.LEFT
            ),
            new GeneralBrowserTableCell(
                -1,mj.mj.getMj_dest_ip(),JLabel.LEFT
            ),
            new GeneralBrowserTableCell(
                -1,mj.mj.getMj_dest_port()+"",JLabel.LEFT
            ),
            new GeneralBrowserTableCell(
                -1,mj.pool_info, JLabel.LEFT
            ),
            new GeneralBrowserTableCell(
                -1,mj.mj.getMj_trans_optStr(),JLabel.LEFT
            ),
            new GeneralBrowserTableCell(
                -1,mj.mj.getMj_desc(),JLabel.LEFT
            )
        };
        
        table.insertRow( one );
    }
}
