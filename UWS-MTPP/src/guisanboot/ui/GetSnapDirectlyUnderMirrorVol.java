/*
 * GetSnapDirectlyUnderMirrorVol.java
 *
 * Created on July 18, 2008, 10:18 AM
 */

package guisanboot.ui;

import guisanboot.data.BasicVDisk;
import guisanboot.data.BriefVDisk;
import guisanboot.data.Snapshot;
import javax.swing.*;

import mylib.UI.*;
import guisanboot.res.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author  Administrator
 */
public class GetSnapDirectlyUnderMirrorVol extends GeneralGetSomethingThread {
    private int rootid;
    private boolean isCMDP;
    private ArrayList localIdList_m;
    
    /** Creates a new instance of GetSnapDirectlyUnderMirrorVol */
    public GetSnapDirectlyUnderMirrorVol( 
        SanBootView view, 
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType,
        int rootid,
        boolean isCMDP
    ){
        super( view,fNode, processEvent, eventType );
        this.rootid = rootid;
        this.isCMDP = isCMDP;
    }
    
    public boolean realRun(){ 
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
        
        // 步骤 :
        //(1) listsnap -m rootid 找出直属快照的 local_id list
        //(2) select * from vsnap where type=snap and rootid=<disk.root_id> ===> <snap set>
        //(3) select * from <snap set> where snap.local_id == each local_id in local_id list

        // listsnap -m rootid
        boolean ok = view.initor.mdb.getBriefVDiskList( 
            ResourceCenter.getCmd( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + rootid 
        );
        if( ok ){
            localIdList_m = view.initor.mdb.getBriefVDiskList();
            
            ok = view.initor.mdb.queryVSnapDB(
                "select * from " + ResourceCenter.VSnap_DB +" where " + BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid+";"
            );    
            if( ok ){
                if( this.isCMDP ){
                    searchMirroredSnapHeaderForCMDP();
                    searchMirroredSnapForCMDP( );
                }else{
                    searchMirroredSnapHeader();
                    searchMirroredSnap( );
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
    
    private void searchMirroredSnap(){
        Object[] objList = localIdList_m.toArray();
        Arrays.sort( objList );
SanBootView.log.debug( getClass().getName()," brief disk number: " + objList.length );
        for( int i=objList.length-1; i>=0; i-- ){
            BriefVDisk disk = (BriefVDisk)objList[i];

            if( disk.isMirroredSnap() ){ 
                Snapshot snap = view.initor.mdb.getMirroredSnapshotFromQuerySql( disk.getLocal_snap_id() );
                if( snap != null ){
                    MirroredSnap ms = new MirroredSnap( snap );
                    if( eventType == Browser.TREE_EXPAND_EVENT  ){
                        BrowserTreeNode cNode = new BrowserTreeNode( ms, false );
                        ms.setTreeNode( cNode );
                        ms.setFatherNode( fNode );
                        view.addNode( fNode,cNode );
                    }else{
                        processEvent.insertSomethingToTable( ms );
                    }
                }else{
SanBootView.log.error( getClass().getName()," Not found snapshot from vsnap db: ["+ rootid + "." +
 disk.getLocal_snap_id() +"]" );                            
                }
            }
        }
    }
    
    private void searchMirroredSnapHeader(){
        Object[] objList = localIdList_m.toArray();
        Arrays.sort( objList );
SanBootView.log.debug( getClass().getName()," brief disk number: " + objList.length );
        for( int i=objList.length-1; i>=0; i-- ){
            BriefVDisk disk = (BriefVDisk)objList[i];
            
            if( disk.isMirroredSnapHeader() ){
                Snapshot snap = view.initor.mdb.getMirroredSnapshotHeaderFromQuerySql( disk.getLocal_snap_id() );
                if( snap != null ){    
                    MirroredSnap ms = new MirroredSnap( snap );
                    if( eventType == Browser.TREE_EXPAND_EVENT  ){
                        BrowserTreeNode cNode = new BrowserTreeNode( ms,false );
                        ms.setTreeNode( cNode );
                        ms.setFatherNode( fNode );
                        view.addNode( fNode,cNode );
                    }else{
                        processEvent.insertSomethingToTable( ms );
                    }
                }else{
SanBootView.log.error( getClass().getName()," Not found snapshot from vsnap db: ["+ rootid + "." +
 disk.getLocal_snap_id() +"]" );                            
                }
            }
        }
    }

    private void searchMirroredSnapForCMDP(){
        ArrayList localIdList_al1 = view.initor.mdb.getAllQueryResult();
        Object[] objList = localIdList_al1.toArray();
        Arrays.sort( objList );
SanBootView.log.debug( getClass().getName()," brief disk number: " + objList.length );
        for( int i=objList.length-1; i>=0; i--){
            BasicVDisk disk = (BasicVDisk)objList[i];
            if( isOnMainBranch( disk.getSnap_local_snapid() ) ) continue;

             if( disk.isMirroredSnap() ){
                Snapshot snap = view.initor.mdb.getMirroredSnapshotFromQuerySql( disk.getSnap_local_snapid() );
                if( snap != null ){
                    MirroredSnap ms = new MirroredSnap( snap );
                    if( eventType == Browser.TREE_EXPAND_EVENT  ){
                        BrowserTreeNode cNode = new BrowserTreeNode( ms, false );
                        ms.setTreeNode( cNode );
                        ms.setFatherNode( fNode );
                        view.addNode( fNode,cNode );
                    }else{
                        processEvent.insertSomethingToTable( ms );
                    }
                }else{
SanBootView.log.error( getClass().getName()," Not found snapshot from vsnap db: ["+ rootid + "." +
 disk.getSnap_local_snapid() +"]" );
                }
            }
        }
    }

    private void searchMirroredSnapHeaderForCMDP(){
        ArrayList localIdList_al1 = view.initor.mdb.getAllQueryResult();
        Object[] objList = localIdList_al1.toArray();
        Arrays.sort( objList );
SanBootView.log.debug( getClass().getName()," brief disk number: " + objList.length );
        for( int i=objList.length-1; i>=0; i--){
            BasicVDisk disk = (BasicVDisk)objList[i];
            if( isOnMainBranch( disk.getSnap_local_snapid() ) ) continue;
            
            if( disk.isMirroredSnapHeader() ){
                Snapshot snap = view.initor.mdb.getMirroredSnapshotHeaderFromQuerySql( disk.getSnap_local_snapid() );
                if( snap != null ){
                    MirroredSnap ms = new MirroredSnap( snap );
                    if( eventType == Browser.TREE_EXPAND_EVENT  ){
                        BrowserTreeNode cNode = new BrowserTreeNode( ms,false );
                        ms.setTreeNode( cNode );
                        ms.setFatherNode( fNode );
                        view.addNode( fNode,cNode );
                    }else{
                        processEvent.insertSomethingToTable( ms );
                    }
                }else{
SanBootView.log.error( getClass().getName()," Not found snapshot from vsnap db: ["+ rootid + "." +
 disk.getSnap_local_snapid() +"]" );
                }
            }
        }
    }

    private boolean isOnMainBranch( int localId ){
        //ArrayList mainBranchIdList = view.initor.mdb.getBriefVDiskList();
        //int size = mainBranchIdList.size();
        int size = this.localIdList_m.size();
        for( int i=0; i<size; i++ ){
            //BriefVDisk disk = (BriefVDisk)mainBranchIdList.get(i);
            BriefVDisk disk = (BriefVDisk)this.localIdList_m.get( i );
            if( localId == disk.getLocal_snap_id() ){
                return true;
            }
        }
        return false;
    }
}
