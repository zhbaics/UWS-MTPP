/*
 * GetMJIntoTableModel.java
 *
 * Created on Aug 18, 2010, 14:05  PM
 */

package guisanboot.remotemirror.cmd;

import guisanboot.data.BasicVDisk;
import guisanboot.data.MirrorJob;
import guisanboot.data.Pool;
import guisanboot.data.PoolWrapper;
import guisanboot.data.SuspendNetworkRunning;
import guisanboot.data.UWSrvNode;
import guisanboot.remotemirror.ui.multiRenderTable.MjOption;
import guisanboot.remotemirror.ui.multiRenderTable.WrapperOfRemoteUWSAndPool;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;

import guisanboot.ui.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetMJIntoTableModel extends SuspendNetworkRunning{
    private ArrayList cache = new ArrayList();
    private SanBootView view;
    private MirrorJob curMJ  = null;
    private DefaultTableModel model;
    private int mode; // 0: for StartOrStopMjInBatchAction 1: ModifyMjInBatchAction
    
    /** Creates a new instance of GetMJIntoTableModel */
    public GetMJIntoTableModel( SanBootView _view,String cmd,int mode ) throws IOException {
        super( cmd, _view.getSocket(),true, 5 );
        this.view = _view;
        this.mode = mode;
    }

    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 );

        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value );

            if( s1.startsWith( MirrorJob.MJ_mj_id ) ){
                try{
                    int id = Integer.parseInt( value );
                    curMJ.setMj_id( id );
                }catch(Exception ex){
                    curMJ.setMj_id( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_mg_id )){
                try{
                    int mg_id = Integer.parseInt( value );
                    curMJ.setMj_mg_id( mg_id );
                }catch(Exception ex){
                    curMJ.setMj_mg_id( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_job_type)){
                try{
                    int job_type = Integer.parseInt( value );
                    curMJ.setMj_job_type( job_type );
                }catch(Exception ex){
                    curMJ.setMj_job_type( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_job_name)){
                curMJ.setMj_job_name( value );
            }else if(s1.startsWith( MirrorJob.MJ_mj_dest_ip)){
                curMJ.setMj_dest_ip( value );
            }else if(s1.startsWith( MirrorJob.MJ_mj_dest_port )){
                try{
                    int dest_port = Integer.parseInt( value );
                    curMJ.setMj_dest_port( dest_port);
                }catch(Exception ex){
                    curMJ.setMj_dest_port( -1 );
                }
            }else if(s1.startsWith( MirrorJob.MJ_mj_transfer_option )){
                try{
                    int opt = Integer.parseInt( value );
                    curMJ.setMj_transfer_option( opt );
                }catch(Exception ex){
                    curMJ.setMj_transfer_option( 0 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_dest_pool_passwd ) ){
                curMJ.setMj_dest_pool_passwd( value );
            }else if( s1.startsWith( MirrorJob.MJ_mj_dest_pool ) ){
                try{
                    int dest_poolid = Integer.parseInt( value );
                    curMJ.setMj_dest_pool( dest_poolid );
                }catch(Exception ex){
                    curMJ.setMj_dest_pool( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_dest_root_id) ){
                try{
                    int dest_rootid = Integer.parseInt( value );
                    curMJ.setMj_dest_root_id( dest_rootid );
                }catch(Exception ex){
                    curMJ.setMj_dest_root_id( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_done_snap_id ) ){
                try{
                    int done_snapid = Integer.parseInt( value );
                    curMJ.setMj_done_snap_id( done_snapid );
                }catch( Exception ex){
                    curMJ.setMj_done_snap_id( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_current_rootid ) ){
                try{
                    int cur_rootid = Integer.parseInt( value );
                    curMJ.setMj_current_rootid( cur_rootid );
                }catch( Exception ex){
                    curMJ.setMj_current_rootid( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_current_snap_id ) ){
                try{
                    int cur_snapid = Integer.parseInt( value );
                    curMJ.setMj_current_snap_id( cur_snapid );
                }catch( Exception ex){
                    curMJ.setMj_current_snap_id( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_current_process) ){
                try{
                    int cur_process = Integer.parseInt( value );
                    curMJ.setMj_current_process( cur_process );
                }catch( Exception ex){
                    curMJ.setMj_current_process( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_job_status )){
                try{
                    int job_status = Integer.parseInt( value );
                    curMJ.setMj_job_status( job_status );
                }catch( Exception ex){
                    curMJ.setMj_job_status( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_job_pid ) ){
                try{
                    int job_pid = Integer.parseInt( value );
                    curMJ.setMj_job_pid( job_pid );
                }catch( Exception ex){
                    curMJ.setMj_job_pid( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_desc ) ){
                curMJ.setMj_desc( value );
            }else if( s1.startsWith( MirrorJob.MJ_mj_current_block ) ){
                try{
                    int cur_blk = Integer.parseInt( value );
                    curMJ.setMj_current_block( cur_blk );
                }catch( Exception ex){
                    curMJ.setMj_current_block( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_info) ){
                curMJ.setMj_info( value );
            }else if( s1.startsWith( MirrorJob.MJ_mj_track_src_rootid ) ){
                try{
                    int rootid = Integer.parseInt( value );
                    curMJ.setMj_track_src_rootid( rootid );
                }catch( Exception ex){
                    curMJ.setMj_track_src_rootid( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_track_mode ) ){
                try{
                    int track_mode = Integer.parseInt( value );
                    curMJ.setMj_track_mode( track_mode );
                }catch( Exception ex){
                    curMJ.setMj_track_mode( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_track_src_type ) ){
                try{
                    int track_src_type = Integer.parseInt( value );
                    curMJ.setMj_track_src_type( track_src_type );
                }catch( Exception ex){
                    curMJ.setMj_track_src_type( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_copy_src_rootid ) ){
                try{
                    int copy_src_rootid = Integer.parseInt( value );
                    curMJ.setMj_copy_src_rootid( copy_src_rootid );
                }catch(Exception ex){
                    curMJ.setMj_copy_src_rootid( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_copy_src_snapid ) ){
                try{
                    int copy_src_snapid = Integer.parseInt( value );
                    curMJ.setMj_copy_src_snapid( copy_src_snapid );
                }catch(Exception ex){
                    curMJ.setMj_copy_src_snapid( -1 );
                }
            }else if( s1.startsWith( MirrorJob.MJ_mj_copy_src_type ) ){
                try{
                    int copy_src_type = Integer.parseInt( value );
                    curMJ.setMj_copy_src_type( copy_src_type );
                }catch(Exception ex){
                    curMJ.setMj_copy_src_type( -1 );
                }
                try{
                    if( mode == 0 ){
                        SwingUtilities.invokeAndWait( insertModel );
                    }else{
                        SwingUtilities.invokeAndWait( insertModel1 );
                    }
                }catch(Exception ex){}
            }
        }else{
            if( s1.startsWith( MirrorJob.MJ_RECFLAG )){
                curMJ = new MirrorJob();
            }
        }
    }

    public void updateMJList(){
        curMJ = null;
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
    }
    
    Runnable insertModel = new Runnable(){
        public void run(){
            Object[] one = new Object[11];
            one[0] = new Boolean( false );
            one[1] = curMJ;
            one[2] = curMJ.getMj_job_name();
            one[3] = MirrorJob.getTypeString( curMJ.getMj_job_type() );

            if( curMJ.getMj_mg_id() > 0 ){
                boolean isMgStart = view.initor.mdb.checkMg( curMJ.getMj_mg_id() );
                one[4] = curMJ.getMjStatusStr( isMgStart);
            }else{
                one[4] = curMJ.getMjStatusStr();
            }

            if( !curMJ.getMj_info().equals("end") && !curMJ.getMj_info().trim().equals("") ){
                one[5] = curMJ.getMj_info();
            }else{
                one[5] = "";
            }
            one[6] = curMJ.getMj_dest_ip();
            one[7] = curMJ.getMj_dest_port();
            String pool_info = getPool( curMJ.getMj_dest_ip(),curMJ.getMj_dest_port(),curMJ.getMj_dest_pool() );
            one[8] = pool_info;
            one[9] = curMJ.getMj_trans_optStr();
            one[10] = curMJ.getMj_desc();
            model.addRow( one );
        }
    };
    
    Runnable insertModel1 = new Runnable(){
        public void run(){
            Object[] one = new Object[7];
            one[0] = new Boolean( false );
            one[1] = curMJ;
            one[2] = curMJ.getMj_job_name();
            one[3] = MirrorJob.getTypeString( curMJ.getMj_job_type() );

            UWSrvNode uwsNode = view.initor.mdb.getOneUWSrvFromVector( curMJ.getMj_dest_ip() );
            PoolWrapper pool = getPool1( curMJ.getMj_dest_ip(),curMJ.getMj_dest_port(),curMJ.getMj_dest_pool() );
            WrapperOfRemoteUWSAndPool wrapper = new WrapperOfRemoteUWSAndPool( uwsNode, pool );
            wrapper.addPoolItem( pool );
            one[4] = wrapper;

            one[5] = new MjOption(
                curMJ.isContinue(),curMJ.isEncrypt(),
                curMJ.isDelView(),curMJ.isZip(),
                curMJ.isBranch()
            );
            one[6] = curMJ.getMj_desc();
            model.addRow( one );
        }
    };

    private String getPool( String uws_ip,int uws_port,int poolid ){
        String pool_name,totalStr,freeStr;

        PoolItem poolItem = hasOne( uws_ip,uws_port,poolid );
        if( poolItem == null ){
            view.initor.mdb.getRemotePool( uws_ip, uws_port, poolid );
            Pool pool = view.initor.mdb.getRemotePool( poolid );
            if( pool == null ){
SanBootView.log.error( getClass().getName()," Not found remote pool: [ server_ip:" + uws_ip  + " server_port:" +
         uws_port + " poolid: "+ poolid + " ]. Maybe mediaserver service not running." );
                pool = new Pool();
                pool_name = poolid + "";
                pool.setPool_name( pool_name );
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

            cache.add(  new PoolItem( uws_ip,uws_port, poolid, pool_name,
                    lFree,freeStr, lTotal,totalStr,lVused,pool.getPool_passwd() ) );

        }else{
            pool_name = poolItem.pool_name;
            freeStr = poolItem.freeStr;
            totalStr = poolItem.totalStr;
        }

        return pool_name+" [ "+freeStr+" / "+totalStr+" ]";
    }

    private PoolWrapper getPool1( String uws_ip,int uws_port,int poolid ){
        String pool_name,totalStr,freeStr;
        Pool pool;

        PoolItem poolItem = hasOne( uws_ip,uws_port,poolid );
        if( poolItem == null ){
            view.initor.mdb.getRemotePool( uws_ip, uws_port, poolid );
            pool = view.initor.mdb.getRemotePool( poolid );
            if( pool == null ){
SanBootView.log.error( getClass().getName()," Not found remote pool: [ server_ip:" + uws_ip  + " server_port:" +
         uws_port + " poolid: "+ poolid + " ]. Maybe mediaserver service not running." );
                pool = new Pool();
                pool_name = poolid + "";
                pool.setPool_name( pool_name );
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
                pool.setUsed( lVused );
                pool.setFreeSize( lFree );
                pool.setTotalSize( lTotal );
            }

            cache.add(  new PoolItem( uws_ip,uws_port, poolid, pool_name,
                    lFree,freeStr, lTotal,totalStr,lVused,pool.getPool_passwd() ) );
        }else{
            pool = new Pool();
            pool.setPool_id( poolItem.key_poolid );
            pool.setPool_name( poolItem.pool_name );
            pool.setTotalSize( poolItem.totalSize );
            pool.setFreeSize( poolItem.freeSize );
            pool.setUsed( poolItem.vused );
            pool.setPool_passwd( poolItem.pool_pwd );
        }
        
        return new PoolWrapper( pool );
    }

    private PoolItem hasOne( String uws_ip,int uws_port,int poolid ){
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            PoolItem poolItem = (PoolItem)cache.get(i);
            if( poolItem.key_ip.equals( uws_ip ) &&
                ( poolItem.key_port == uws_port ) &&
                (poolItem.key_poolid == poolid )
            ){
                return poolItem;
            }
        }
        return null;
    }

    public void setMjTable( JTable table ){
        model = (DefaultTableModel)table.getModel();
    }
}

class PoolItem{
    public String key_ip;
    public int key_port;
    public int key_poolid;

    public String pool_pwd;
    public String pool_name;
    public long freeSize;
    public String freeStr;
    public long totalSize;
    public String totalStr;
    public long vused;

    PoolItem(
        String key_ip,int key_port,int key_poolid,String pool_name,
        long freeSize,String freeStr,long totalSize,
        String totalStr,long vused,
        String pool_pwd
    ){
        this.key_ip = key_ip;
        this.key_port = key_port;
        this.key_poolid = key_poolid;
        this.pool_name = pool_name;
        this.freeSize = freeSize;
        this.freeStr = freeStr;
        this.totalSize = totalSize;
        this.totalStr = totalStr;
        this.vused = vused;
        this.pool_pwd = pool_pwd;
    }
}
