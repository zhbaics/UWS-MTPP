/*
 * GetMirrorJobThread.java
 *
 * Created on July 12, 2008, 12:10 AM
 */

package guisanboot.ui;

import guisanboot.data.BasicVDisk;
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
public class GetLogMirrorJobThread extends GeneralGetSomethingThread{
    private ArrayList cache = new ArrayList();
    private int rootid;
    
    /** Creates a new instance of GetMirrorJobThread */
    public GetLogMirrorJobThread( 
        SanBootView view, 
        BrowserTreeNode fNode,
        GeneralProcessEventForSanBoot processEvent,
        int eventType,
        int _rootid
    ){
        super( view, fNode, processEvent, eventType );
        rootid = _rootid;
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
            mjList = view.initor.mdb.getLogMjOnRootId(rootid);
            
        }else{
            mjList = view.initor.mdb.getAllLogMj();
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
