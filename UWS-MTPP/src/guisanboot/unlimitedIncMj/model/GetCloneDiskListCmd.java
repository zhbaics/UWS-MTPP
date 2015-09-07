/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.unlimitedIncMj.model;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author zjj
 */
public class GetCloneDiskListCmd extends NetworkRunning{
    ArrayList<CloneDisk> list = new ArrayList<CloneDisk>() ;
    private CloneDisk curCloneDisk = null;

    public void parser(String line) {
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 );

        int index = s1.indexOf("=");
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value );

            if( s1.startsWith( CloneDisk.CloneDisk_ID ) ){
                try{
                    int id = Integer.parseInt( value );
                    curCloneDisk.setId( id );
                }catch( Exception ex ){
                    curCloneDisk.setId( -1 );
                }
                list.add( curCloneDisk );
            }else if( s1.startsWith( CloneDisk.CloneDisk_SRC_HOST_TYPE ) ){
                try{
                    int src_host_type = Integer.parseInt( value );
                    curCloneDisk.setSrc_host_type( src_host_type );
                }catch(Exception ex){
                    curCloneDisk.setSrc_host_type( -1 );
                }
            }else if( s1.startsWith( CloneDisk.CloneDisk_SRC_HOST_ID ) ){
                try{
                    int src_host_id = Integer.parseInt( value );
                    curCloneDisk.setSrc_host_id( src_host_id );
                }catch(Exception ex){
                    curCloneDisk.setSrc_host_id( -1 );
                }
            }else if( s1.startsWith( CloneDisk.CloneDisk_SRC_DISK_ROOT_ID ) ){
                try{
                    int src_disk_root_id = Integer.parseInt( value );
                    curCloneDisk.setSrc_disk_root_id( src_disk_root_id );
                }catch(Exception ex){
                    curCloneDisk.setSrc_disk_root_id( -1 );
                }
            }else if( s1.startsWith(CloneDisk.CloneDisk_SRC_INC_MIRVOL_ROOT_ID ) ){
                try{
                    int src_inc_mirvol_root_id = Integer.parseInt( value );
                    curCloneDisk.setSrc_inc_mirvol_root_id( src_inc_mirvol_root_id );
                }catch(Exception ex){
                    curCloneDisk.setSrc_inc_mirvol_root_id( -1 );
                }
            }else if(s1.startsWith( CloneDisk.CloneDisk_SRC_INC_MIRVOL_SNAP_LOCAL_ID )){
                try{
                    int src_inc_mirvol_snap_local_id = Integer.parseInt( value );
                    curCloneDisk.setSrc_inc_mirvol_snap_local_id( src_inc_mirvol_snap_local_id );
                }catch(Exception ex){
                    curCloneDisk.setSrc_inc_mirvol_snap_local_id( -1 );
                }
            }else if(s1.startsWith( CloneDisk.CloneDisk_CRT_TIME )){
                curCloneDisk.setCrt_time( value );
            }else if(s1.startsWith( CloneDisk.CloneDisk_ROOT_ID ) ){
                try{
                    int root_id = Integer.parseInt( value );
                    curCloneDisk.setRoot_id( root_id );
                }catch(Exception ex){
                    curCloneDisk.setRoot_id( -1 );
                }
            }else if( s1.startsWith( CloneDisk.CloneDisk_Label ) ){
                curCloneDisk.setLabel( value );
            }else if( s1.startsWith( CloneDisk.CloneDisk_Desc ) ){
                curCloneDisk.setDesc( value );
            }else if( s1.startsWith( CloneDisk.CloneDisk_Tid )){
                try{
                    int target_id = Integer.parseInt( value );
                    curCloneDisk.setTarget_id( target_id );
                }catch(Exception ex){
                    curCloneDisk.setTarget_id( -1 );
                }
            }
        }else{
            if( s1.startsWith( CloneDisk.CloneDisk_FLAG )){
                curCloneDisk = new CloneDisk();
            }
        }
    }

    public GetCloneDiskListCmd( String cmd,Socket socket) throws IOException {
        super( cmd,socket );
    }

    public GetCloneDiskListCmd( String cmd ){
        super( cmd );
    }

    public boolean realDo( String aCmdLine ){
        this.setCmdLine( aCmdLine );
SanBootView.log.info( getClass().getName()," get clone disk list cmd:"+ this.getCmdLine() );

        try{
            this.list.clear();
            this.curCloneDisk = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," get clone disk list cmd retcode:"+ this.getRetCode()  );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," get clone disk list cmd errmsg:"+ this.getErrMsg()  );
        }
        return isOk;
    }

    public ArrayList getCloneDiskList(){
        int size = this.list.size();
        ArrayList ret = new ArrayList( size );
        for( int i=0; i<size; i++ ){
            ret.add( list.get(i) );
        }
        return ret;
    }

    public CloneDisk getCloneDiskOnTid( int tid ){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            CloneDisk disk = (CloneDisk)list.get(i);
            if( disk.getTarget_id() == tid ){
                return disk;
            }
        }
        
        return null;
    }
}
