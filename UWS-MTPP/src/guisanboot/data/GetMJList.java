/*
 * GetMJList.java
 *
 * Created on July 8, 2005, 8:15 PM
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.util.Vector;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetMJList extends NetworkRunning {
    private Vector<MirrorJob> mjs = new Vector<MirrorJob>();
    private MirrorJob curMJ = null;
    
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
                mjs.addElement( curMJ );
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
            }else if( s1.startsWith( MirrorJob.MJ_mj_scheduler ) ){
                try{
                    int mj_sch_id = Integer.parseInt( value );
                    curMJ.setMj_scheduler( mj_sch_id );
                }catch(Exception ex){
                    curMJ.setMj_scheduler( -1 );
                }
            } else if( s1.startsWith( MirrorJob.MJ_mg_name ) ){
                curMJ.setMg_name( value );
            } else if( s1.startsWith( MirrorJob.MJ_mg_interval_time ) ){
                try{
                    int mg_interval_time = Integer.parseInt( value );
                    curMJ.setMg_interval_time(mg_interval_time);
                } catch(Exception ex){
                    curMJ.setMg_interval_time(-1);
                }
            } else if( s1.startsWith( MirrorJob.MJ_mg_log_max_time ) ){
                try{
                    int mg_log_max_time = Integer.parseInt( value );
                    curMJ.setMg_log_max_time(mg_log_max_time);
                } catch(Exception ex){
                    curMJ.setMg_log_max_time(-1);
                }
            } else if( s1.startsWith( MirrorJob.MJ_mg_max_snapshot ) ){
                try{
                    int mg_max_snapshot = Integer.parseInt( value );
                    curMJ.setMg_max_snapshot(mg_max_snapshot);
                } catch(Exception ex){
                    curMJ.setMg_max_snapshot(-1);
                }
            }
        }else{
            if( s1.startsWith( MirrorJob.MJ_RECFLAG )){
                curMJ = new MirrorJob();
            }
        }
    }
    
    public GetMJList(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetMJList(String cmd){
        super( cmd );
    }
    
    public boolean updateMj( int mj_id ) {
        setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_GET_MJ_STATUS ) +mj_id );
SanBootView.log.info( getClass().getName(), " get one mj cmd: " + getCmdLine()  );         
        try{
            mjs.removeAllElements();
            curMJ = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get one mj retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get one mj errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
    
    public boolean updateMj() {
SanBootView.log.info( getClass().getName(), " get mj cmd: " + getCmdLine()  ); 
        try{
            mjs.removeAllElements();
            curMJ = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get mj retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get mj errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
  
    public void addMJToVector( MirrorJob mj ){
        mjs.addElement( mj );
    }

    public void removeMJFromVector( MirrorJob mj ){
        int id = mj.getMj_id();
        int size = mjs.size();
        for( int i=0; i<size; i++ ){
            MirrorJob _mj = mjs.elementAt(i);
            if( _mj.getMj_id() == id ){
                mjs.remove(i);
                break;
            }
        }
    }

    public MirrorJob getMJFromVectorOnID( int id ){
        int size = mjs.size();
        for( int i=0; i<size; i++ ){
            MirrorJob mj = mjs.elementAt( i );
            if( mj.getMj_id() == id )
                return mj;
        }
        return null;
    }

    public MirrorJob getIncMjOnDestRootId( int dest_root_id ){
        int size = mjs.size();
        for( int i=0;i<size;i++){
            MirrorJob mj = mjs.elementAt(i);
            if( ( mj.getMj_dest_root_id() == dest_root_id ) && mj.isIncMjType() ){
                return mj;
            }
        }
        return null;
    }

    public ArrayList<MirrorJob> getCjListFromVecOnSrcRootId( int rootid ){
        int size = mjs.size();
        ArrayList<MirrorJob> ret = new ArrayList<MirrorJob>( size );
        for( int i=0;i<size;i++){
            MirrorJob mj = mjs.elementAt(i);
            if( ( mj.getMj_copy_src_rootid() == rootid ) && mj.isCjType() ){
                ret.add( mj );
            }
        }
        return ret;
    }

    public ArrayList<MirrorJob> getIncMjListFromVecOnSrcRootId( int rootid ){
        int size = mjs.size();
        ArrayList<MirrorJob> ret = new ArrayList<MirrorJob>( size );
        for( int i=0;i<size;i++){
            MirrorJob mj = mjs.elementAt(i);
            if( ( mj.getMj_track_src_rootid() == rootid ) && mj.isIncMjType() ){
                ret.add( mj );
            }
        }
        return ret;
    }

    public ArrayList<MirrorJob> getIncMjListFromVecOnSrcRootIdOrMgID( int rootid,int mgid ){
        int size = mjs.size();
        ArrayList<MirrorJob> ret = new ArrayList<MirrorJob>( size );
        for( int i=0;i<size;i++){
            MirrorJob mj = mjs.elementAt(i);
SanBootView.log.debug( getClass().getName(), " mj -> "+ mj.prtMe() );
            if( ( mj.getMj_mg_id() == mgid ) || ( (  mj.getMj_track_src_rootid() == rootid ) && mj.isIncMjType() ) ){
                ret.add( mj );
            }
        }
        return ret;
    }

    public ArrayList<MirrorJob> getMJFromVecOnMgID( int mgID ){
        int size = mjs.size();
        ArrayList<MirrorJob> ret = new ArrayList<MirrorJob>( size );
        for( int i=0;i<size;i++){
            MirrorJob mj = mjs.elementAt(i);        
            if( mj.getMj_mg_id() == mgID ){
                ret.add( mj );
            }
        }
        return ret;
    }
    
    public ArrayList<MirrorJob> getAllMJ( ){
        ArrayList<MirrorJob> list  = new ArrayList<MirrorJob>();
        int size = mjs.size();
        for( int i=0; i<size; i++ ){
            list.add( mjs.elementAt( i ) );
        }
        return list;
    }
    
    public ArrayList<MirrorJob> getLogMjOnRootId( int root_id ){
        ArrayList<MirrorJob> list  = new ArrayList<MirrorJob>();
        int size = mjs.size();
        for( int i=0; i<size; i++ ){
            MirrorJob job = mjs.get(i);
            if( job.getMj_track_src_rootid() == root_id && job.getMj_job_type() == MirrorJob.MJ_TYPE_REMOTE_LOG_JOB ){
                list.add( mjs.elementAt( i ) );
            }
        }
        return list;
    }
    
    public ArrayList<MirrorJob> getAllLogMj(){
        ArrayList<MirrorJob> list  = new ArrayList<MirrorJob>();
        int size = mjs.size();
        for( int i=0; i<size; i++ ){
            MirrorJob job = mjs.get(i);
            if(job.getMj_job_type() == MirrorJob.MJ_TYPE_REMOTE_LOG_JOB ){
                list.add( mjs.elementAt( i ) );
            }
        }
        return list;
    }
    
    public ArrayList<MirrorJob> getAllMJOnDestIP( String dest_ip ){
        int size = mjs.size();
        ArrayList<MirrorJob> list = new ArrayList<MirrorJob>( size );
        for( int i=0; i<size; i++ ){
            MirrorJob job = mjs.get(i);
System.out.println(" mj in mdb'dest ip: "+ job.getMj_dest_ip() ); 
            if( job.getMj_dest_ip().equals( dest_ip ) ){
                list.add( job );
            }
        }
        
        return list;
    }

    public ArrayList<MirrorJob> getMjRelatedWithMjSch( int mjSch_id,boolean start ){
        ArrayList<MirrorJob> ret = new ArrayList<MirrorJob>();
        int size = mjs.size();
        for( int i=0; i<size; i++ ){
            MirrorJob mj = (MirrorJob)mjs.get(i);
            if( mj.getMj_scheduler() == mjSch_id ){
                if( start ){
                    if( mj.isMJStart() ){
                        ret.add( mj );
                    }
                }else{
                    if( mj.isMJStop() ){
                        ret.add( mj );
                    }
                }
            }
        }
        return ret;
    }
    
    public boolean isRelatedWithMj( int mjSch_id ){
        int size = mjs.size();
        for( int i=0; i<size; i++ ){
            MirrorJob mj = (MirrorJob)mjs.get(i);
            if( mj.getMj_scheduler() == mjSch_id ){
                return true;
            }
        }
        return false;
    }

    public int getMJNum(){
        return mjs.size();
    }
}