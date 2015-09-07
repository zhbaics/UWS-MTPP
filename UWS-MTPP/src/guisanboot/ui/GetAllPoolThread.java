/*
 * GetAllPhyVolThread.java
 *
 * Created on 2007/5/18,PM 4:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;
import java.util.ArrayList;
import guisanboot.data.*;


/**
 *
 * @author Administrator
 */
public class GetAllPoolThread extends BasicGetSomethingThread{
    private ArrayList list = null;
    private String remote_uws_ip ="";
    private int remote_uws_port = UWSrvNode.MEDIA_SERVER_PORT;
    
    /** Creates a new instance of BackupMDBThread */
    public GetAllPoolThread(
        SanBootView view,
        String _remote_uws_ip,
        int _remote_uws_port
    ) {
        super( view );
        
        remote_uws_ip = _remote_uws_ip;
        remote_uws_port = _remote_uws_port;
    }
    
    /** Creates a new instance of BackupMDBThread */
    public GetAllPoolThread(
        SanBootView view
    ) {
        this( view,"", -1 );
    }
    
    public boolean realRun(){ 
        boolean aIsOk;
        if( !remote_uws_ip.equals("") ){
            aIsOk = view.initor.mdb.updateRemotePool( remote_uws_ip,remote_uws_port );
        }else{
            aIsOk = view.initor.mdb.updatePool();
        }
        if( aIsOk ){
            if( !remote_uws_ip.equals("") ){
                list = view.initor.mdb.getRemotePoolList();
            }else{
                list = view.initor.mdb.getPoolList();
            }
            int size = list.size();
            for( int i=0; i<size; i++ ){
                Pool pool = (Pool)list.get(i);
                if( !remote_uws_ip.equals("") ){
                    aIsOk = view.initor.mdb.getRemotePoolInfo( remote_uws_ip, remote_uws_port, pool.getPool_id() );
                }else{
                    aIsOk = view.initor.mdb.getPoolInfo( pool.getPool_id() );
                }
                if( aIsOk ){
                    if( !remote_uws_ip.equals("") ){
                        pool.setTotalSize( view.initor.mdb.getRemotePoolTotalCap() );
                        pool.setUsed( view.initor.mdb.getRemotePoolVUsed() );
                        pool.setFreeSize( view.initor.mdb.getRemotePoolAvailCap() );
                    }else{
                        pool.setTotalSize( view.initor.mdb.getPoolTotalCap() );
                        pool.setUsed( view.initor.mdb.getPoolVUsed() );
                        pool.setFreeSize( view.initor.mdb.getPoolAvailCap() );
                    }
                }else{
                    pool.setTotalSize( -1 );
                    pool.setUsed( -1 );
                    pool.setFreeSize( -1 );
                }
            }
        }
        
        return true;
    }
    
    public ArrayList getRet(){
        return list;
    }
    
    public ArrayList getWraperRet(){
        if( !remote_uws_ip.equals("") ){
            return view.initor.mdb.getRemotePoolWrapList( false );
        }else{
            return view.initor.mdb.getPoolWrapList( false );
        }
    }
}