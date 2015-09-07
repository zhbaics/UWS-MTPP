/*
 * GetMirrorDiskInfo.java
 *
 * Created on July 17, 2008, 8:0 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetMirrorDiskInfo extends NetworkRunning {
    private ArrayList<MirrorDiskInfo> mdisks = new ArrayList<MirrorDiskInfo>();
    private MirrorDiskInfo curMdi = null;
    
    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 ); 

        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value ); 

            if( s1.startsWith( MirrorDiskInfo.MDI_SNAP_ROOTID ) ){
                try{
                    int rootid = Integer.parseInt( value );
                    curMdi.setSnap_rootid( rootid );
                }catch(Exception ex){
                    curMdi.setSnap_rootid( -1 );
                }
                mdisks.add( curMdi );
            }else if( s1.startsWith( MirrorDiskInfo.MDI_SRCAGNT_ID ) ){
                try{
                    int srcagnt_id = Integer.parseInt( value );
                    curMdi.setSrc_agnt_id( srcagnt_id );
                }catch(Exception ex){
                    curMdi.setSrc_agnt_id( -1 );
                }
            }else if( s1.startsWith( MirrorDiskInfo.MDI_SRC_SNAP_ROOTID ) ){
                try{
                    int src_rootid = Integer.parseInt( value );
                    curMdi.setSrc_snap_rootid( src_rootid );
                }catch(Exception ex){
                    curMdi.setSrc_snap_rootid( -1 );
                }
            }else if( s1.startsWith( MirrorDiskInfo.MDI_SRC_VOL_NAME ) ){
                curMdi.setSrc_snap_name( value );
            }else if(s1.startsWith( MirrorDiskInfo.MDI_SRC_DISK_LABEL )){
                curMdi.setSrc_agent_mp( value );
            }else if(s1.startsWith( MirrorDiskInfo.MDI_SRCAGNT_FLAG ) ){
                try{
                    int flag = Integer.parseInt( value );
                    curMdi.setSrc_agent_info( flag );
                }catch(Exception ex){
                    curMdi.setSrc_agent_info( -1 );
                }                
            }else if( s1.startsWith(MirrorDiskInfo.MDI_SRC_GRPDETAIL ) ){
            }else if( s1.startsWith( MirrorDiskInfo.MDI_SRC_GRPINFO ) ){
                try{
                    int tid = Integer.parseInt( value );
                    curMdi.setTargetID( tid );
                }catch(Exception ex){
                    curMdi.setTargetID( -1 );
                }  
            }else if( s1.startsWith( MirrorDiskInfo.MDI_SRC_VOL_INFO ) ){
                try{
                    int vol_type = Integer.parseInt( value );
                    curMdi.setVolumeType( vol_type );
                }catch(Exception ex){
                    curMdi.setVolumeType( 2 );
                }
            }else if( s1.startsWith( MirrorDiskInfo.MDI_SRC_VOL_PROTECT_TYPE ) ){
                try{
                    int ptype = Integer.parseInt( value );
                    curMdi.setSrc_vol_protect_type( ptype );
                }catch(Exception ex){
                    curMdi.setSrc_vol_protect_type( BootHost.PROTECT_TYPE_UNKNOWN );
                }
            }
        }else{
            if( s1.startsWith( MirrorDiskInfo.MDI_FLAG )){
                curMdi = new MirrorDiskInfo();
            }
        }
    }
    
    public GetMirrorDiskInfo(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetMirrorDiskInfo(String cmd){
        super( cmd );
    }

    public boolean updateMirrorDiskInfo() {
SanBootView.log.info( getClass().getName(), " get mirror_disk_info cmd: " + getCmdLine()  );        
        try{
            mdisks.clear();
            curMdi = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get mirror_disk_info retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get mirror_disk_info errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
  
    public void addMDIToVector( MirrorDiskInfo mdi ){
        mdisks.add( mdi );
    }
    
    public void removeMDIFromVector( MirrorDiskInfo mdi ){
        int id = mdi.getSnap_rootid();
        int size = mdisks.size();
        for( int i=0; i<size; i++ ){
            MirrorDiskInfo _mdi = (MirrorDiskInfo)mdisks.get( i );
            if( _mdi.getSnap_rootid() == id ){
                mdisks.remove( i );
                break;
            }
        }
    }
    
    public MirrorDiskInfo getMDIFromCacheOnRootID( int rootid ){
        int size = mdisks.size();
        for( int i=0; i<size; i++ ){
            MirrorDiskInfo mdi = mdisks.get(i);
            if( mdi.getSnap_rootid() == rootid ){
                return mdi;
            }
        }
        return null;
    }
    
    // mirrordiskinfo'target is not (=0 or <0), it is a rollbacked disk
    public MirrorDiskInfo getMDIFromCacheOnTID( int tid ){
        int size = mdisks.size();
        for( int i=0; i<size; i++ ){
            MirrorDiskInfo mdi = mdisks.get(i);
            if( mdi.getTargetID() == tid ){
                return mdi;
            }
        }
        return null;
    }
    
    public MirrorDiskInfo isExistMDI( int rootid ){
        int size = mdisks.size();
        for( int i=0; i<size; i++ ){
            MirrorDiskInfo mdi = mdisks.get(i);
            if( mdi.getSnap_rootid() == rootid ){
                return mdi;
            }
        }
        return null;
    }
    
    public ArrayList isExistMDIList( int rootid ){
        ArrayList ret = new ArrayList();
        int size = mdisks.size();
        for( int i=0; i<size; i++ ){
            MirrorDiskInfo mdi = mdisks.get(i);
            if( mdi.getSnap_rootid() == rootid ){
                ret.add( mdi );
            }
        }
        return ret;
    }
    
    public ArrayList<MirrorDiskInfo> getMDIFromCacheOnSrcAgntID( int srcagntID ){
        int size = mdisks.size();
        ArrayList<MirrorDiskInfo> ret = new ArrayList<MirrorDiskInfo>( size );
        for( int i=0; i<size; i++ ){
            MirrorDiskInfo mdi = mdisks.get(i);
            if( mdi.getSrc_agnt_id() == srcagntID ){
                ret.add( mdi );
            }
        }
        return ret;
    }

    public ArrayList<MirrorDiskInfo> getMDIFromCacheOnHostIDandRootID( int hostId,int rootId ){
        int size = mdisks.size();
        ArrayList<MirrorDiskInfo> ret = new ArrayList<MirrorDiskInfo>( size );
        for( int i=0; i<size; i++ ){
            MirrorDiskInfo mdi = mdisks.get(i);
            if( ( ( mdi.getSrc_agnt_id() == hostId ) || ( hostId == -1 ) )&&
                ( mdi.getSrc_snap_rootid() == rootId )
            ){
                ret.add( mdi );
            }
        }
        return ret;
    }

    public ArrayList<MirrorDiskInfo> getAllMDIs(){
        int size = mdisks.size();
        ArrayList<MirrorDiskInfo> ret = new ArrayList<MirrorDiskInfo>(size);
        for( int i=0; i<size; i++ ){
            ret.add( mdisks.get(i) );
        }
        return ret;
    }
    
    
    public int getMDINum(){
        return mdisks.size();
    }
}
