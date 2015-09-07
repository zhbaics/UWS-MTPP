/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror;

import guisanboot.data.BasicVDisk;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.Snapshot;
import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;
import guisanboot.ui.*;

/**
 *
 * @author Administrator
 */
public class RollbackVolThread extends BasicGetSomethingThread {
    MirrorDiskInfo mdi;    
    ArrayList localIdList;
    
    public RollbackVolThread(
        SanBootView view,
        MirrorDiskInfo mdi
    ){
        super( view );
        this.mdi = mdi;
    }
    
    public boolean realRun(){        
        BrowserTreeNode mdiNode = mdi.getTreeNode();
        BrowserTreeNode chiefFreeMdiNode = null;
        if( mdiNode != null ){
            chiefFreeMdiNode = (BrowserTreeNode)mdiNode.getParent();
        }
        int rootid = mdi.getSnap_rootid();            
        if( !getMirrorSnapList( rootid ) ) return false;
        if( !modifyMirrorSnap( rootid ) ) return false;
        
        if( !view.initor.mdb.delMDI( rootid ) ){
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_MIRROR_DISK ) + " : " +
                        view.initor.mdb.getErrorMessage();
            return false;
        }else{
            view.initor.mdb.removeMDIFromCache( mdi );
        }
        
        if( (chiefFreeMdiNode != null) && ( mdiNode != null ) ){
            view.removeNodeFromTree( chiefFreeMdiNode, mdiNode );   
        }
        
        BrowserTreeNode chiefFreeVolNode = view.getChiefNodeOnRoot( ResourceCenter.TYPE_CHIEF_ORPHAN_VOL );
        if( chiefFreeVolNode != null ){            
            // 显示点击 chiefFreeVolNode 后的右边tabpane中的内容
            view.setCurNode( chiefFreeVolNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefOrphanVol peOnChiefFreeVol = new ProcessEventOnChiefOrphanVol( view );
            TreePath path = new TreePath( chiefFreeVolNode.getPath() );
            peOnChiefFreeVol.processTreeSelection( path );
            peOnChiefFreeVol.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }
        
        return true;
    }
    
    public boolean getMirrorSnapList( int rootid ){
        //boolean ok = view.initor.mdb.getBriefVDiskList(
        //    ResourceCenter.getCmd( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + rootid
        //);
        //if( ok ){
            // localIdList = view.initor.mdb.getBriefVDiskList();
            boolean ok = view.initor.mdb.queryVSnapDB(
                "select * from " + ResourceCenter.VSnap_DB +" where " + BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid+";"
            );
            if( !ok ){
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_SNAP ) + " : " +
                        view.initor.mdb.getErrorMessage();
            }
            return ok;

        /*
        }else{
            if( view.initor.mdb.getErrorCode() == 255 ){
                localIdList = new ArrayList( 0 ); // don't exist snaps on rootid
                return true;
            }else{
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_SNAP_Directly_Under_Disk ) + " : " +
                        view.initor.mdb.getErrorMessage();
                return ok;
            }
        }
        */
    }

    // 只对普通镜像的快照和镜像卷进行类型的改变，无限增量镜像不做这样的改变
    public boolean modifyMirrorSnap( int rootid ){
        boolean aIsOk;
        
        ArrayList<BasicVDisk> list = view.initor.mdb.getAllQueryResult();
        int size = list.size();
        for( int i=0; i<size; i++ ){
            BasicVDisk disk = (BasicVDisk)list.get( i );
            if( !disk.isMirroredSnap() && !disk.isMirroredSnapHeader() ) continue;

            if( disk.isMirroredSnap() ){
                aIsOk = view.initor.mdb.modSnapshot( rootid, disk.getSnap_local_snapid(),Snapshot.TYPE_OPENED_SNAP );
            }else{
                // rollback会自动将disk online的
                aIsOk = view.initor.mdb.rollSnapshotToDisk( rootid,disk.getSnap_local_snapid() );
            }

            if( !aIsOk ){
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_SNAP ) + " : " +
                    view.initor.mdb.getErrorMessage();
                return false;
            }
        }

        return true;

        /*
        int size = localIdList.size();
SanBootView.log.debug( getClass().getName()," brief disk number: " + size );    
        for( int i=0; i<size; i++ ){
            BriefVDisk disk = (BriefVDisk)localIdList.get( i );
            if( disk.isMirroredSnap() ){
                snap = view.initor.mdb.getMirroredSnapshotFromQuerySql( disk.getLocal_snap_id() );                
            }else if( disk.isMirroredSnapHeader() ){
                snap = view.initor.mdb.getMirroredSnapshotHeaderFromQuerySql( disk.getLocal_snap_id() );                 
            }else{
                continue;
            }
            
            if( snap != null ){
                if( disk.isMirroredSnap() ){
                    aIsOk = view.initor.mdb.modSnapshot( rootid, snap.getSnap_local_snapid(),Snapshot.TYPE_OPENED_SNAP );
                }else{
                    // rollback会自动将disk online的
                    aIsOk = view.initor.mdb.rollSnapshotToDisk( rootid,snap.getSnap_local_snapid() );                   
                }
                if( !aIsOk ){
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_SNAP ) + " : " +
                        view.initor.mdb.getErrorMessage();
                    return false;
                }
            }else{
SanBootView.log.error( getClass().getName()," Not found snapshot from vsnap db: ["+ rootid + "." + snap.getSnap_local_snapid() +"]" ); 
                errMsg = " Not found snapshot from vsnap db: ["+ rootid + "." + snap.getSnap_local_snapid() +"]";
                return false;
            }
        }
        
        return true;
        */
    }  
}
