/*
 * GetMirrorSnapProgress.java
 *
 * Created on July 14, 2005, 10:18 AM
 */

package guisanboot.ui;

import guisanboot.data.BasicVDisk;
import guisanboot.data.BriefVDisk;
import guisanboot.data.MirrorJob;
import guisanboot.data.Snapshot;
import javax.swing.*;

import mylib.UI.*;
import guisanboot.res.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetMirrorSnapProgress extends GeneralGetSomethingThread {
    private ArrayList localIdList1;
    private int rootid;
    private int mj_id;
    private int mj_type;
    
    /** Creates a new instance of GetMirrorSnapProgress */
    public GetMirrorSnapProgress( 
        SanBootView view, 
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType,
        int rootid,
        int mj_id,
        int mj_type
    ){
        super( view,fNode, processEvent, eventType );
        this.rootid = rootid;
        this.mj_id = mj_id;
        this.mj_type = mj_type;
    }
    
    public boolean realRun(){ 
        // 步骤 :
        //(1) listsnap -m rootid 找出直属快照的 local_id list
        //(2) select * from vsnap where type=snap and rootid=<disk.root_id> ===> <snap set>
        //(3) select * from <snap set> where snap.local_id == each local_id
        
        if( eventType == Browser.TREE_EXPAND_EVENT ){
            try{
                SwingUtilities.invokeAndWait( clearTree );
            }catch( Exception ex ){
                ex.printStackTrace();
            }
        }else if( eventType == Browser.TREE_SELECTED_EVENT ){
            try{
                SwingUtilities.invokeAndWait( clearTable );
            }catch( Exception ex ){
                ex.printStackTrace();
            }
        }
        
        boolean ok = view.initor.mdb.monitorMJ( mj_id );
        if( ok ){
            MirrorJob newMJ = view.initor.mdb.getMonedMj( mj_id );
            if( newMJ != null ){
                if( ( mj_type == MirrorJob.MJ_TYPE_LOCAL ) || ( mj_type == MirrorJob.MJ_TYPE_REMOTE ) ){
                    ok = view.initor.mdb.getBriefVDiskList(
                        ResourceCenter.getCmd( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + rootid
                    );
                }else{
                    ok = view.initor.mdb.getBriefVDiskList(
                        ResourceCenter.getCmd( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + rootid +
                        " -b "+ 0 + " -e "+ 32
                    );
                }
                if( ok ){
                    if( ( mj_type == MirrorJob.MJ_TYPE_LOCAL ) || ( mj_type == MirrorJob.MJ_TYPE_REMOTE ) ){
                        ok = view.initor.mdb.queryVSnapDB(
                            "select * from " + ResourceCenter.VSnap_DB +" where (" +BasicVDisk.BVDisk_Snap_OpenType +"="+
                            BasicVDisk.TYPE_OPENED_SNAP + " or "+BasicVDisk.BVDisk_Snap_OpenType +"="+
                            BasicVDisk.TYPE_OPENED_CrtByMirr +") and "+ BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid+";"
                        );
                    }else{
                        localIdList1 = view.initor.mdb.getBriefVDiskList();
                        ok = view.initor.mdb.queryVSnapDB(
                            "select * from " + ResourceCenter.VSnap_DB +" where " + BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid+
                            " and "+BasicVDisk.BVDisk_Snap_Local_SnapId + " in (" + this.getLocalSnapIdString() + ");"
                        );
                    }

                    if( ok ){
                        int done_snap_id = newMJ.getMj_done_snap_id();
                        ArrayList localIdList = view.initor.mdb.getLimitedDiskList( done_snap_id );
                        int size = localIdList.size();
SanBootView.log.debug( getClass().getName()," brief disk number: " + size );    
                        for( int i=0; i<size; i++ ){
                            BriefVDisk disk = (BriefVDisk)localIdList.get(i);
                            if( disk.isSnap() || disk.isMirroredSnap() || disk.isUIMirroredSnap() ){
                                Snapshot snap = view.initor.mdb.getGeneralSnapshotFromQuerySql( disk.getLocal_snap_id() );
                                if( snap != null ){                        
                                     MirroredSnapView msv = new MirroredSnapView( snap );
                                    if( eventType == Browser.TREE_EXPAND_EVENT  ){
                                        BrowserTreeNode cNode = new BrowserTreeNode( msv,false );
                                        msv.setTreeNode( cNode );
                                        msv.setFatherNode( fNode );
                                        view.addNode( fNode,cNode );
                                    }else{
                                        processEvent.insertSomethingToTable( msv );
                                    }
                                }else{
SanBootView.log.error( getClass().getName()," Not found snapshot from vsnap db: ["+ rootid + "." +
                 disk.getLocal_snap_id() +"]" );                            
                                }
                            }
                        }

                        if( eventType == Browser.TREE_EXPAND_EVENT ){
                            view.reloadTreeNode( fNode );
                        }
                    }else{
                        errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_SNAP ) + " : " +
                                view.initor.mdb.getErrorMessage();
                    }
                }else{
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + " : " +
                            view.initor.mdb.getErrorMessage();
                }
            }else{
SanBootView.log.error( getClass().getName(),"Can't find mj: [ id: " + mj_id +" ]" );                
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_MJ ) + " : " +
                             view.initor.mdb.getErrorMessage();
                ok = false;
            }
        }else{
              errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_MJ ) + " : " +
                            view.initor.mdb.getErrorMessage();
        }
        
        return ok;
    }

    private String getLocalSnapIdString(){
        StringBuffer buf = new StringBuffer();
        boolean isFirst = true;

        int size = this.localIdList1.size();
        for( int i=0; i<size; i++ ){
            BriefVDisk disk = (BriefVDisk)localIdList1.get(i);
            if( isFirst ){
                buf.append( disk.getLocal_snap_id() );
                isFirst = false;
            }else{
                buf.append(","+disk.getLocal_snap_id() );
            }
        }

        return buf.toString();
    }
}
