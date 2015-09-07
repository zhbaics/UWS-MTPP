/*
 * GetViewDirectlyUnderSnap.java
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
public class GetViewDirectlyUnderSnap extends GeneralGetSomethingThread {
    private Snapshot snap;
    private ArrayList viewList = new ArrayList();
    
    /** Creates a new instance of GetViewDirectlyUnderSnap */
    public GetViewDirectlyUnderSnap( 
        SanBootView view,  
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType,
        Snapshot _snap
    ){
        super( view,fNode,processEvent,eventType );
        
        snap = _snap;
    }
    
    public boolean realRun(){
        int i,j,size,local_snap_id;
        View viewObj; 
        BasicVDisk directChild;
        
        // 步骤 :
        // 1) 递归获取从该快照开始的所有view 节点
        // 2) 根据快照的子节点来判断直属于它的所有view
        
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
        
        boolean ok = view.initor.mdb.queryVSnapDB(
            "select * from " + ResourceCenter.VSnap_DB +" where "+ 
                BasicVDisk.BVDisk_Snap_Root_ID + "=" + snap.getSnap_root_id() +";"
        );
        
        if( ok ){
            // 先更新一下snap's child list
            Snapshot newSnap = view.initor.mdb.getCommonSnapshotFromQuerySql( snap.getSnap_local_snapid() );
            if( newSnap != null ){
                snap.setSnap_child_list( newSnap.getSnap_child_list() );
            }else{
SanBootView.log.error( getClass().getName(),"Not found snapshot from vsnap db: ["+snap.getSnap_root_id() +
        "."+snap.getSnap_local_snapid()+"]" );                
            }
            
            // 获取树上从该快照开始所有的 view 节点
            getViewChild( snap );
            size = viewList.size();
SanBootView.log.debug( getClass().getName()," Child of view from this snap node on tree: ["+snap.getSnap_root_id()+
        "."+snap.getSnap_local_snapid() +"]" );
SanBootView.log.debug( getClass().getName(),"===========================");
            for( i=0; i<size; i++ ){
                viewObj = (View)viewList.get(i);
SanBootView.log.debug( getClass().getName()," view local_snap_id: "+viewObj.getSnap_local_snapid() +
                    " view target_id: "+ viewObj.getSnap_target_id()
                );            
            }
SanBootView.log.debug( getClass().getName(),"===========================\r\n");            
            
            // 根据该快照的子节点来判断直属于它的view
            ArrayList directChildList = snap.getChildList();
            size = directChildList.size();
            for( i=0; i<size; i++ ){
                local_snap_id = ((Integer)directChildList.get(i)).intValue();
                directChild = view.initor.mdb.getVDisk( local_snap_id );
                if( directChild != null ){
                    viewObj = null;
                    if( directChild.isSnap() ){
                        viewObj = searchViewList( directChild.getSnap_target_id() );
                    }else{ // direct child is view or disk
                        if( directChild.isView() ){
                            if( directChild.getSnap_target_id() != snap.getSnap_target_id() ){
                                viewObj = new View( directChild );
                            }
                        }
                    }
                    
                    if( viewObj != null ){
                        if( eventType == Browser.TREE_EXPAND_EVENT  ){
                            BrowserTreeNode cNode = new BrowserTreeNode( viewObj,false );
                            viewObj.setTreeNode( cNode );
                            viewObj.setFatherNode( fNode );
                            view.addNode( fNode,cNode );
                        }else{
                            processEvent.insertSomethingToTable( viewObj );
                        }
SanBootView.log.debug( getClass().getName()," Found direct view: ["+ viewObj.getSnap_root_id()+
            "." + viewObj.getSnap_local_snapid() +"]");                                                
                    }
                }else{
SanBootView.log.error( getClass().getName()," Not found disk from vsnap db: ["+ snap.getSnap_root_id() +"."+
        local_snap_id +"]");                    
                }
            }
            
            if( eventType == Browser.TREE_EXPAND_EVENT ){
                view.reloadTreeNode( fNode );
            }
        }else{
             errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_VIEW ) + " : " +
                view.initor.mdb.getErrorMessage();
        }
             
        return ok;
    }   
    
    private void getViewChild( BasicVDisk beginDisk ){
        int i,size;
        
        ArrayList childIdList = beginDisk.getChildList();
        ArrayList childDiskList = view.initor.mdb.getDiskFromQuerySql( childIdList );
        
        size = childDiskList.size();
        for( i=0; i<size; i++ ){
            BasicVDisk disk = (BasicVDisk)childDiskList.get(i);
            if( !disk.isView() && !disk.isOriDisk() && !disk.isMirroredSnapHeader() ){
                getViewChild( disk );
            }else{
                if( disk.isView() ){
                    viewList.add( new View( disk ) );
                }
            }
        }
    }
    
    private View searchViewList( int target_id ){
        View viewObj;
        
        int size = viewList.size();
        for( int j=0; j<size; j++ ){
            viewObj = (View)viewList.get(j);
            if( viewObj.getSnap_target_id() == target_id ){
                return viewObj;
            }
        }
        return null;
    }
}
