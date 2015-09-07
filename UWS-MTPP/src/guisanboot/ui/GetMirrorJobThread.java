/*
 * GetMirrorJobThread.java
 *
 * Created on July 12, 2008, 12:10 AM
 */

package guisanboot.ui;

import guisanboot.data.BasicVDisk;
import guisanboot.data.MirrorGrp;
import guisanboot.data.MirrorJob;
import guisanboot.data.MirrorJobWrapper;
import guisanboot.data.Pool;
import java.util.ArrayList;
import javax.swing.*;
import mylib.UI.*;

/**
 *
 * @author  Administrator
 */
public class GetMirrorJobThread extends GeneralGetSomethingThread{
    private ArrayList cache = new ArrayList();
    private int rootid;
    private boolean isCj;  // is copy job
    
    /** Creates a new instance of GetMirrorJobThread */
    public GetMirrorJobThread( 
        SanBootView view, 
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType,
        int _rootid,
        boolean isCj
    ){
        super( view, fNode, processEvent, eventType );
        rootid = _rootid;
        this.isCj = isCj;
    }
    
    public boolean realRun(){ 
        String pool_name,totalStr,freeStr;
        PoolItem poolItem;
        int mg_id;
        ArrayList mjList;
        
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

        view.initor.mdb.updateMj();  // 刷新一下mj,因为不知道copy job什么时候会结束

        if( rootid != -1 ){
            MirrorGrp mg = view.initor.mdb.getMGFromVectorOnRootID( rootid );
            if( mg == null ){
SanBootView.log.info( getClass().getName(),"Not found mg related rootid: " + rootid +". Maybe  it is a unlimited incremental mj or copy job.");
                if( !this.isCj ){
                    mjList = view.initor.mdb.getIncMjListFromVecOnSrcRootId( rootid );
                }else{
                    mjList = view.initor.mdb.getCjListFromVecOnSrcRootId( rootid );
                }
            }else{
                mg_id = mg.getMg_id();
                mjList = view.initor.mdb.getIncMjListFromVecOnSrcRootIdOrMgID( rootid,mg_id );
            }
        }else{
            mjList = view.initor.mdb.getAllMj();
        }

        int size = mjList.size();
        for( int i=0; i<size; i++ ){
            MirrorJob mj = (MirrorJob)mjList.get(i); 
            String uws_ip = mj.getMj_dest_ip();
            int uws_port = mj.getMj_dest_port();
            int poolid = mj.getMj_dest_pool();
            
            poolItem = hasOne( uws_ip,uws_port,poolid );
            if( poolItem == null ){
                view.initor.mdb.getRemotePool( uws_ip, uws_port, poolid );
                Pool pool = view.initor.mdb.getRemotePool( poolid );
                if( pool == null ){
SanBootView.log.error( getClass().getName()," Not found remote pool: [ server_ip:" + uws_ip  + " server_port:" +
             uws_port + " poolid: "+ poolid + " ]. Maybe mediaserver service not running." ); 
                    pool_name = poolid + "";
                }else{
                    pool_name = pool.getPool_name();
                }

                view.initor.mdb.getRemotePoolInfo( uws_ip, uws_port, poolid );
                long lFree  = view.initor.mdb.getRemotePoolAvailCap();
                long lVused = view.initor.mdb.getRemotePoolVUsed();
                long lTotal = view.initor.mdb.getRemotePoolTotalCap();
                if( ( lFree == -1 ) && ( lVused == -1 ) && ( lTotal == -1 ) ){
SanBootView.log.error( getClass().getName()," Not found remote pool info: [ server_ip:" + uws_ip  + " server_port:" +
             uws_port + " poolid: "+ poolid + " ]. Maybe mediaserver service not running." ); 
                    totalStr = "N/A";
                    freeStr  = "N/A";                
                }else{
                    totalStr = BasicVDisk.getCapStr( lTotal );

                    // 严格的实际可用空间
                    lFree = lTotal - lVused;
                    if( lFree <= 0 ){
                        freeStr = "0";
                    }else{
                        freeStr = BasicVDisk.getCapStr(  lFree );
                    }
                }
                
                cache.add(  new PoolItem( uws_ip,uws_port, poolid, pool_name,freeStr, totalStr ) );
                
            }else{
                pool_name = poolItem.pool_name;
                freeStr = poolItem.freeStr;
                totalStr = poolItem.totalStr;
            }
            
            if( eventType == Browser.TREE_EXPAND_EVENT  ){
                BrowserTreeNode cNode = new BrowserTreeNode( mj,true );
                mj.setTreeNode( cNode );
                mj.setFatherNode( fNode );
                view.addNode( fNode,cNode );
            }else{
                MirrorJobWrapper mjWrapper = new MirrorJobWrapper( mj, pool_name+" [ "+freeStr+" / "+totalStr+" ]" );
                processEvent.insertSomethingToTable( mjWrapper );
            }
            
            if( eventType == Browser.TREE_EXPAND_EVENT ){
                view.reloadTreeNode( fNode );
            }
        }
        
        return true;
    }   
    
    private PoolItem hasOne( String uws_ip,int uws_port,int poolid ){
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            PoolItem poolItem = (PoolItem)cache.get(i);
            if( poolItem.key_ip.equals(uws_ip) && 
                ( poolItem.key_port == uws_port ) &&
                (poolItem.key_poolid == poolid )
            ){
                return poolItem;
            }
        }
        return null;
    }
}

class PoolItem{
    public String key_ip;
    public int key_port;
    public int key_poolid;
    
    public String pool_name;
    public String freeStr;
    public String totalStr;
    
    PoolItem( String key_ip,int key_port,int key_poolid,String pool_name,String freeStr,String totalStr){
        this.key_ip = key_ip;
        this.key_port = key_port;
        this.key_poolid = key_poolid;
        this.pool_name = pool_name;
        this.freeStr = freeStr;
        this.totalStr = totalStr;
    }
}
