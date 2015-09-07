/*
 * GetSnapDirectlyUnderDisk.java
 *
 * Created on July 14, 2005, 10:18 AM
 */

package guisanboot.ui;

import guisanboot.data.BasicVDisk;
import guisanboot.data.BriefVDisk;
import guisanboot.data.Snapshot;
import mylib.UI.*;
import guisanboot.res.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author  Administrator
 */
public class GetSnapDirectlyUnderDisk extends GeneralGetSomethingThread {
    private int rootid;
    private boolean isCMDP;
    private ArrayList<Snapshot> list = new ArrayList<Snapshot>();
    
    /** Creates a new instance of GetSnapDirectlyUnderDisk */
    public GetSnapDirectlyUnderDisk( 
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
        // 步骤 :
        //(1) listsnap -m rootid 找出直属快照的 local_id list
        //(2) select * from vsnap where type=snap and rootid=<disk.root_id> ===> <snap set>
        //(3) select * from <snap set> where snap.local_id == each local_id in local_id list

        if( eventType == Browser.TREE_EXPAND_EVENT ){
            this.fireClearTree();
        }else if( eventType == Browser.TREE_SELECTED_EVENT ){
            this.fireClearTable();
        }else if( eventType == Browser.TREE_BOTH_EVENT ){        
            this.fireClearTable();
            this.fireClearTree();
        }

        this.list.clear();

        // listsnap -m <root_id>
        boolean ok = view.initor.mdb.getBriefVDiskList( 
            ResourceCenter.getCmd( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + rootid 
        );
        if( ok ){
            ok = view.initor.mdb.queryVSnapDB(
                "select * from " + ResourceCenter.VSnap_DB +" where " +BasicVDisk.BVDisk_Snap_OpenType +"="+
                BasicVDisk.TYPE_OPENED_SNAP +" and "+ BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid+";"
            );
            if( ok ){
                if( !this.isCMDP ){
                    getSnap( 0 );
                }else{
                    this.getCMDPSnap( 0 );
                }
                
                // get deleting-snapshot list
                boolean OK = view.initor.mdb.queryVSnapDB(
                    "select * from " + ResourceCenter.VSnap_DB +" where " +BasicVDisk.BVDisk_Snap_OpenType +"="+
                    BasicVDisk.TYPE_OPEND_DEL_SNAP +" and "+ BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid+";"
                );
                if( OK ){
                    if( !this.isCMDP ){
                        getSnap( 1 );
                    }else{
                        this.getCMDPSnap( 1 );
                    }
                    
                    OK = view.initor.mdb.queryVSnapDB(
                        "select * from " + ResourceCenter.VSnap_DB +" where " +BasicVDisk.BVDisk_Snap_OpenType +"="+
                        BasicVDisk.TYPE_OPENED_CrtByMirr +" and "+ BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid+";"
                    );
                    
                    if( OK ){
                        if( !this.isCMDP ){
                            getSnap( 2 );
                        }else{
                            this.getCMDPSnap( 2 );
                        }
                    }
                }
                
                addToGUI();

                if( eventType == Browser.TREE_EXPAND_EVENT || eventType == Browser.TREE_BOTH_EVENT ){
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
    
    private void getSnap( int mode ){
        Snapshot snap = null;

        ArrayList localIdList = view.initor.mdb.getBriefVDiskList();
        int size = localIdList.size();
SanBootView.log.debug( getClass().getName()," brief disk number: " + size );    
        for( int i=0; i<size; i++ ){
            BriefVDisk disk = (BriefVDisk)localIdList.get(i);
            snap = null;
            if( mode == 0 ){
                if( disk.isSnap() ){
                    snap = view.initor.mdb.getSnapshotFromQuerySql( disk.getLocal_snap_id() );   
                }
            }else if( mode == 1 ){
                if( disk.isDeletingSnap() ){
                    snap = view.initor.mdb.getDelSnapshotFromQuerySql( disk.getLocal_snap_id() );
                }
            }else {
                if( disk.isMirroredSnap() ){
                    snap = view.initor.mdb.getMirroredSnapshotFromQuerySql( disk.getLocal_snap_id() );
                }
            }

            if( snap != null ){
                this.list.add( snap );
            }else{
SanBootView.log.error( getClass().getName()," Not found snapshot from vsnap db: ["+ rootid + "." +
disk.getLocal_snap_id() +"]" );
            }
        }
    }

    private void getCMDPSnap( int mode ){
        Snapshot snap;

        ArrayList localIdList = view.initor.mdb.getAllQueryResult();
        int size = localIdList.size();
SanBootView.log.debug( getClass().getName()," brief disk number: " + size );
        for( int i=0; i<size; i++ ){
            BasicVDisk disk = (BasicVDisk)localIdList.get(i);
            if( isOnMainBranch( disk.getSnap_local_snapid() ) ) continue;

            snap = null;
            if( mode == 0 ){
                if( disk.isSnap() ){
                    snap = view.initor.mdb.getSnapshotFromQuerySql( disk.getSnap_local_snapid() );
                }
            }else if( mode == 1 ){
                if( disk.isDeletingSnap() ){
                    snap = view.initor.mdb.getDelSnapshotFromQuerySql( disk.getSnap_local_snapid() );
                }
            }else {
                if( disk.isMirroredSnap() ){
                    snap = view.initor.mdb.getMirroredSnapshotFromQuerySql( disk.getSnap_local_snapid() );
                }
            }

            if( snap != null ){
                this.list.add( snap );
            }else{
SanBootView.log.error( getClass().getName()," Not found snapshot from vsnap db: ["+ rootid + "." +
disk.getSnap_local_snapid() +"]" );
            }
        }
    }

    private void addToGUI(){
        Object[] objList = list.toArray();
        Arrays.sort( objList );
        //for( int i=0; i<objList.length; i++ ){
        for( int i=objList.length-1; i>=0; i-- ){
            Snapshot snap = (Snapshot)objList[i];
            if( eventType == Browser.TREE_EXPAND_EVENT  ){
                BrowserTreeNode cNode = new BrowserTreeNode( snap,false );
                snap.setTreeNode( cNode );
                snap.setFatherNode( fNode );
                view.addNode( fNode,cNode );
            }else if( eventType == Browser.TREE_SELECTED_EVENT ){
                processEvent.insertSomethingToTable( snap );
            }else {
                BrowserTreeNode cNode = new BrowserTreeNode( snap,false );
                snap.setTreeNode( cNode );
                snap.setFatherNode( fNode );
                view.addNode( fNode,cNode );

                processEvent.insertSomethingToTable( snap );
            }
        }
    }

    private boolean isOnMainBranch( int localId ){
        ArrayList mainBranchIdList = view.initor.mdb.getBriefVDiskList();
        int size = mainBranchIdList.size();
        for( int i=0; i<size; i++ ){
            BriefVDisk disk = (BriefVDisk)mainBranchIdList.get(i);
            if( localId == disk.getLocal_snap_id() ){
                return true;
            }
        }
        return false;
    }
}
