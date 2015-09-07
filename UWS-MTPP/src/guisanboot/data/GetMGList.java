/*
 * GetMGList.java
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
public class GetMGList extends NetworkRunning {
    private Vector<MirrorGrp> mgs = new Vector<MirrorGrp>();
    private MirrorGrp curMG = null;
    
    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 ); 
        
        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value ); 

            if( s1.startsWith( MirrorGrp.MG_mg_id) ){
                try{
                    int id = Integer.parseInt( value );
                    curMG.setMg_id( id );
                }catch(Exception ex){
                    curMG.setMg_id( -1 );
                }
                mgs.addElement( curMG );
            }else if( s1.startsWith( MirrorGrp.MG_mg_name )){
                curMG.setMg_name( value );
            }else if( s1.startsWith( MirrorGrp.MG_mg_type)){
                try{
                    int type = Integer.parseInt( value );
                    curMG.setMg_type( type );
                }catch(Exception ex){
                    curMG.setMg_type( -1 );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_src_ip)){
                curMG.setMg_src_ip( value );
            }else if(s1.startsWith( MirrorGrp.MG_mg_src_port)){
                try{
                    int port = Integer.parseInt( value );
                    curMG.setMg_src_port( port );
                }catch(Exception ex){
                    curMG.setMg_src_port( -1 );
                }
            }else if(s1.startsWith( MirrorGrp.MG_mg_src_disk_uuid )){
                curMG.setMg_src_disk_uuid( value );
            }else if(s1.startsWith( MirrorGrp.MG_mg_src_root_id )){
                try{
                    int rootid = Integer.parseInt( value );
                    curMG.setMg_src_root_id( rootid );
                }catch(Exception ex){
                    curMG.setMg_src_root_id( -1 );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_interval_time ) ){
                try{
                    int interval = Integer.parseInt( value );
                    curMG.setMg_interval_time( interval );
                }catch(Exception ex){
                    curMG.setMg_interval_time( -1 );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_min_snap_size ) ){
                try{
                    int min = Integer.parseInt( value );
                    curMG.setMg_min_snap_size( min );
                }catch(Exception ex){
                    curMG.setMg_min_snap_size( -1 );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_max_snapshot )){
                try{
                    int max = Integer.parseInt( value );
                    curMG.setMg_max_snapshot( max );
                }catch(Exception ex){
                    curMG.setMg_max_snapshot( -1 );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_pid ) ){
                try{
                    int pid = Integer.parseInt( value );
                    curMG.setMg_pid( pid );
                }catch( Exception ex){
                    curMG.setMg_pid( -1 );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_before_cmd ) ){
                curMG.setMg_before_cmd( value );
            }else if( s1.startsWith( MirrorGrp.MG_mg_after_cmd ) ){
                curMG.setMg_after_cmd( value );
            }else if( s1.startsWith( MirrorGrp.MG_mg_desc) ){
                curMG.setMg_desc( value );
            }else if( s1.startsWith( MirrorGrp.MG_mg_schedule_type ) ){
                try{
                    int sch_type = Integer.parseInt( value );
                    curMG.setMg_schedule_type( sch_type );
                }catch(Exception ex){
                    curMG.setMg_schedule_type( MirrorGrp.MG_SCH_TYPE_ROTATE );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_schedule_minute ) ){
                if( value.equals("") ){
                    curMG.setMg_schedule_minute( "0" );
                }else{
                    curMG.setMg_schedule_minute( value );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_schedule_hour1 ) ){
                if( value.equals("") ){
                    curMG.setMg_schedule_hour1( "0" );
                }else{
                    curMG.setMg_schedule_hour1( value );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_schedule_hour ) ){
                if( value.equals("") ){
                    curMG.setMg_schedule_hour( "0" );
                }else{
                    curMG.setMg_schedule_hour( value );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_schedule_day ) ){
                if( value.equals("") ){
                    curMG.setMg_schedule_day( "*" );
                }else{
                    curMG.setMg_schedule_day( value );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_schedule_month ) ){
                if( value.equals("") ){
                    curMG.setMg_schedule_month( "*" );
                }else{
                    curMG.setMg_schedule_month( value );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_schedule_week ) ){
                if( value.equals("") ){
                    curMG.setMg_schedule_week( "*" );
                }else{
                    curMG.setMg_schedule_week( value );
                }
            }else if( s1.startsWith( MirrorGrp.MG_mg_schedule_clock_zone) ){
                curMG.setMg_schedule_clock_zone( value );
            }else if( s1.startsWith( MirrorGrp.MG_mg_schedule_clock_set ) ){
                curMG.setMg_schedule_clock_set( value );
            }
        }else{
            if( s1.startsWith( MirrorGrp.MG_RECFLAG )){
                curMG = new MirrorGrp();
            }
        }
    }
    
    public GetMGList(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetMGList(String cmd){
        super( cmd );
    }

    public boolean updateOneMg( int mg_id ) {
        this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_GET_ONE_MG ) + mg_id );
SanBootView.log.info( getClass().getName(), " get one mg cmd: " + getCmdLine()  );
        try{
            mgs.removeAllElements();
            curMG = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get one mg retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get one mg errmsg: "+getErrMsg() );
        }
        return isOk;
    }

    public boolean updateMg() {
SanBootView.log.info( getClass().getName(), " get mg cmd: " + getCmdLine()  );        
        try{
            mgs.removeAllElements();
            curMG = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get mg retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get mg errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
  
    public void addMGToVector( MirrorGrp mg ){
        mgs.addElement( mg );
    }

    public void removeMGFromVector( MirrorGrp mg ){
        mgs.removeElement( mg );
    }

    public MirrorGrp getMGFromVectorOnID( int id ){
        int size = mgs.size();
        for( int i=0; i<size; i++ ){
            MirrorGrp mg = mgs.elementAt(i);
            if( mg.getMg_id() == id )
                return mg;
        }
        return null;
    }
    
    public MirrorGrp getMGFromVecOnRootid( int rootid ){
        int size = mgs.size();
        for( int i=0;i<size;i++ ){
            MirrorGrp mg = mgs.elementAt( i );
            if( mg.getMg_src_root_id() == rootid )
                return mg;
        }
        return null;
    }
    
    public MirrorGrp getMGFromVecOnManything( int rootid,int type,String name ){
        int size = mgs.size();
        for( int i=0;i<size;i++){
            MirrorGrp mg = mgs.elementAt(i);        
            if( ( mg.getMg_src_root_id() == rootid) &&
                  ( mg.getMg_type() == type) &&
                  ( mg.getMg_name().equals( name) )
            ){
                return mg;
            }
        }
        return null;
    }
    
    public ArrayList<MirrorGrp> getAllMG(){
        ArrayList<MirrorGrp> list  = new ArrayList<MirrorGrp>();
        int size = mgs.size();
        for( int i=0; i<size; i++ ){
            list.add( mgs.elementAt( i ) );
        }
        return list;
    }

    public MirrorGrp getOneMgFromCache(){
        if( this.mgs.size() > 0 ){
            return ( MirrorGrp )this.mgs.elementAt(0);
        }else{
            return null;
        }
    }

    public int getMGNum(){
        return mgs.size();
    }
}
