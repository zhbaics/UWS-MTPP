/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror;

import guisanboot.data.BasicVDisk;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.SourceAgent;
import guisanboot.data.Snapshot;
import guisanboot.data.Volume;
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
public class RollbackThread extends BasicGetSomethingThread {
    SourceAgent sa;    
    ArrayList localIdList;
    
    public RollbackThread(
        SanBootView view,
        SourceAgent sa
    ){
        super( view );
        this.sa = sa;
    }
    
    public boolean realRun(){
        int i,size,rootid;
        MirrorDiskInfo mdi;
        
        BrowserTreeNode saNode = sa.getTreeNode();
        BrowserTreeNode chiefSaNode = null;
        if( saNode != null ){
            chiefSaNode = (BrowserTreeNode)saNode.getParent();
        }

        ArrayList mdiList = view.initor.mdb.getMDIFromCacheOnSrcAgntID( sa.getSrc_agnt_id() );
        size = mdiList.size();
        for( i=0; i<size; i++ ){
            mdi = (MirrorDiskInfo)mdiList.get(i);
            rootid = mdi.getSnap_rootid();
            
            if( !getMirrorSnapList( rootid ) ) return false;
            if( !modifyMirrorSnap( rootid ) ) return false;
            if( !setTargetIDForMDI( mdi,rootid ) ) return false;
        }
        
        // 生成一个新的UUID.当mirror的源和目的都是同一台uws时，如果不生成新的UUID，那么就会出现“回滚的srcagent”和
        // 某个srcagent的UUID相同，从而出现混乱。
        String new_uuid = SanBootView.util.pcgenUUID();
        boolean aIsOK = view.initor.mdb.modSrcAgnt( sa.getSrc_agnt_id(), new_uuid, -1 );
        if( !aIsOK ){
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_SRCAGNT ) + " : " +
                        view.initor.mdb.getErrorMessage();
            return false;
        }else{
            sa.setSrc_agnt_uws_id( -1 );      // 断绝与某个uws的关系,表示是一个rollbacked srcagent
            sa.setSrc_agnt_desc( new_uuid ); // 赋予新的UUId
            sa.resetFuncForTable();
            sa.resetFuncForTree();
        }
        
        if( ( chiefSaNode != null ) && ( saNode != null ) ){
            view.removeNodeFromTree( chiefSaNode, saNode );   
        }
        
        BrowserTreeNode chiefRollbackHostNode = view.getChiefNodeOnRoot( ResourceCenter.TYPE_CHIEF_ROLLBACK_HOST );
        if( chiefRollbackHostNode != null ){
            addToTree( chiefRollbackHostNode );
            
            // 显示点击 chiefRollbackHostNode 后的右边tabpane中的内容
            view.setCurNode( chiefRollbackHostNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefRollbackHost peOnChiefRollbackHost = new ProcessEventOnChiefRollbackHost( view );
            TreePath path = new TreePath( chiefRollbackHostNode.getPath() );
            peOnChiefRollbackHost.processTreeSelection( path );
            peOnChiefRollbackHost.controlMenuAndBtnForTreeEvent();
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
            //localIdList = view.initor.mdb.getBriefVDiskList();
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
                localIdList = new ArrayList( 0 ); // don't exist vsnap on rootid
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
        BasicVDisk header = null;

        ArrayList<BasicVDisk> list = view.initor.mdb.getAllQueryResult();
        int size = list.size();
        for( int i=0; i<size; i++ ){
            BasicVDisk disk = (BasicVDisk)list.get( i );
            if( !disk.isMirroredSnap() && !disk.isMirroredSnapHeader() ) continue;
            
            if( disk.isMirroredSnap() ){
                aIsOk = view.initor.mdb.modSnapshot( rootid, disk.getSnap_local_snapid(),Snapshot.TYPE_OPENED_SNAP );
            }else{
                aIsOk = true;
                header = disk;
            }
            
            if( !aIsOk ){
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_SNAP ) + " : " +
                    view.initor.mdb.getErrorMessage();
                return false;
            }
        }

        if( header != null ){
            // 将header disk变成类型为2的对象
            // rollback会自动将header disk online的
            aIsOk = view.initor.mdb.rollSnapshotToDisk( rootid,header.getSnap_local_snapid() );
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
    
    private void addToTree( BrowserTreeNode chiefRollbackHostNode ){      
        BrowserTreeNode hostNode = new BrowserTreeNode( sa,false );
        sa.setTreeNode( hostNode );
        sa.setFatherNode( chiefRollbackHostNode );
        chiefRollbackHostNode.add( hostNode );

        ChiefHostVolume chiefHostVol = new ChiefHostVolume();
        BrowserTreeNode chiefHVolNode = new BrowserTreeNode( chiefHostVol,false );
        chiefHostVol.setTreeNode( chiefHVolNode );
        chiefHostVol.setFatherNode( hostNode );

        ChiefNetBootHost chiefNBootHost = new ChiefNetBootHost();
        BrowserTreeNode chiefNBHNode = new BrowserTreeNode( chiefNBootHost,false );
        chiefNBootHost.setTreeNode( chiefNBHNode );
        chiefNBootHost.setFatherNode( hostNode );

        hostNode.add( chiefHVolNode );
        hostNode.add( chiefNBHNode );

        ArrayList list = view.initor.mdb.getMDIFromCacheOnSrcAgntID( sa.getSrc_agnt_id() );
        int size = list.size();
        for( int j=0; j<size;j++ ){
            MirrorDiskInfo mdi = (MirrorDiskInfo)list.get(j);
            BrowserTreeNode volNode = new BrowserTreeNode( mdi, false );
            mdi.setTreeNode( volNode );
            mdi.setFatherNode( chiefHVolNode );

            // 准备lunmap list node
            ChiefLunMap chiefLm = new ChiefLunMap();
            BrowserTreeNode chiefLmNode = new BrowserTreeNode( chiefLm, true );
            chiefLm.setTreeNode( chiefLmNode );
            chiefLm.setFatherNode( volNode );
            volNode.add( chiefLmNode );

            // 准备snapshot list node
            ChiefSnapshot chiefSnap = new ChiefSnapshot();
            BrowserTreeNode chiefSnapNode = new BrowserTreeNode( chiefSnap,false );
            chiefSnap.setTreeNode( chiefSnapNode );
            chiefSnap.setFatherNode( volNode );
            volNode.add( chiefSnapNode );

            // 准备mj list node
            ChiefMirrorJobList chiefMjList = new ChiefMirrorJobList();
            BrowserTreeNode chiefMjNode = new BrowserTreeNode( chiefMjList,false );
            chiefMjList.setTreeNode( chiefMjNode );
            chiefMjList.setFatherNode( volNode );
            volNode.add( chiefMjNode );

            chiefHVolNode.add( volNode ); 
        } 
        
        view.reloadTreeNode( chiefRollbackHostNode );
    }
    
    private boolean setTargetIDForMDI( MirrorDiskInfo mdi,int rootid ){    
        boolean ok = view.initor.mdb.queryVSnapDB(
            "select * from " + ResourceCenter.VSnap_DB +" where " +
            BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid+
            " and " +
            // 回滚的镜像卷的类型只可能为2
            BasicVDisk.BVDisk_Snap_OpenType + "=" +BasicVDisk.TYPE_OPENED_DISK+
            ";"
        );
        if( !ok ){
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_SNAP ) + " : " +
                    view.initor.mdb.getErrorMessage();
            return false;
        }else{
            Volume vol = view.initor.mdb.getQueryedVolume( rootid ); 
            if( vol != null ){
                mdi.setTargetID( vol.getSnap_target_id() );
                ok = view.initor.mdb.modMDI( rootid, vol.getSnap_target_id() );
                if( !ok ){
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_SNAP ) + " : " +
                        view.initor.mdb.getErrorMessage();
                }
                return ok;
            }else{
                return true;
            }
        }        
    }
}
