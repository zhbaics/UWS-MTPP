/*
 * GetSnapDirectlyUnderView.java
 *
 * Created on July 14, 2005, 10:18 AM
 */

package guisanboot.ui;
import javax.swing.*;

import mylib.UI.*;
import guisanboot.data.*;
import guisanboot.res.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetSnapDirectlyUnderView extends GeneralGetSomethingThread{
    private int rootid;
    private int local_snap_id;
    
    /** Creates a new instance of GetSnapDirectlyUnderView */
    public GetSnapDirectlyUnderView( 
        SanBootView view,  
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType,
        int _rootid,
        int _local_snap_id
    ){
        super( view, fNode, processEvent, eventType );
        
        rootid = _rootid;
        local_snap_id = _local_snap_id;
    }
    
    public boolean realRun(){
        // 步骤 :
        //(1) listsnap -t <view.root_id> <view.local_snap_id>找出直属快照的local_id list
        //(2) select * from vsnap where type=snap and rootid=<view.root_id> ====> <snap set>
        //(3) select * from <snap set> where snap.local_id= each local_id
        
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
        
        boolean ok = view.initor.mdb.getBriefVDiskList( 
            ResourceCenter.getCmd( ResourceCenter.CMD_GET_SNAP_Directly_Under_View ) + rootid +" -t "+ local_snap_id
        );
        if( ok ){
            ok = view.initor.mdb.queryVSnapDB(
                "select * from " + ResourceCenter.VSnap_DB +" where " +BasicVDisk.BVDisk_Snap_OpenType +"="+
                BasicVDisk.TYPE_OPENED_SNAP +" and "+ BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid+";"
            );
            if( ok ){
                ArrayList localIdList = view.initor.mdb.getBriefVDiskList();
                int size = localIdList.size();
SanBootView.log.debug( getClass().getName()," brief disk number: " + size );    
                for( int i=0; i<size; i++ ){
                    BriefVDisk disk = (BriefVDisk)localIdList.get(i);
                    if( disk.isSnap() ){                     
                        Snapshot snap = view.initor.mdb.getSnapshotFromQuerySql( disk.getLocal_snap_id() );
SanBootView.log.debug( getClass().getName(),"Found snap of view: ["+rootid+"."+disk.getLocal_snap_id() +"]" );                           
                        if( snap != null ){                        
                            if( eventType == Browser.TREE_EXPAND_EVENT  ){
                                BrowserTreeNode cNode = new BrowserTreeNode( snap,false );
                                snap.setTreeNode( cNode );
                                snap.setFatherNode( fNode );
                                view.addNode( fNode,cNode );
                            }else{
                                processEvent.insertSomethingToTable( snap );
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
        return ok;
    }
}
