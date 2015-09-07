package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import mylib.tool.*;
import mylib.UI.*;
import guisanboot.ui.*;
import guisanboot.exception.*;
import guisanboot.res.*;

public class AddOrphanVol extends SyncOpCmdWithoutput{
    private Volume curVol;
    
    public void parser(String line) {
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
SanBootView.log.debug(getClass().getName(), "(AddOrphanVol): "+ s1 );
        
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
            
            if( s1.startsWith( BasicVDisk.BVDisk_Snap_Local_SnapId ) ){
                try{
                    int snapid = Integer.parseInt( value );
                    curVol.setSnap_local_snapid( snapid );
                }catch(Exception ex){
                    curVol.setSnap_local_snapid( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Name) ){
                curVol.setSnap_name( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Root_ID )){
                try{
                    int rootid = Integer.parseInt( value );
                    curVol.setSnap_root_id( rootid );
                }catch(Exception ex){
                    curVol.setSnap_root_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Pool_ID )){
                try{
                    int poolid = Integer.parseInt( value );
                    curVol.setSnap_pool_id( poolid );
                }catch(Exception ex){
                    curVol.setSnap_pool_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Parent )){
                try{
                    int parent = Integer.parseInt( value );
                    curVol.setSnap_parent( parent );
                }catch(Exception ex){
                    curVol.setSnap_parent( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_BlkSize ) ){
                try{
                    int blksize = Integer.parseInt( value );
                    curVol.setSnap_block_size( blksize );
                }catch(Exception ex){
                    curVol.setSnap_block_size( 0 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_MaxBlkNo ) ){
                try{
                    int maxblkno = Integer.parseInt( value );
                    curVol.setSnap_max_block_no( maxblkno );
                }catch(Exception ex){
                    curVol.setSnap_max_block_no( 0 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_OpenType ) ){
                try{
                    int type = Integer.parseInt( value );
                    curVol.setSnap_opened_type( type );
                }catch(Exception ex){
                    curVol.setSnap_opened_type( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Child_List )){
                curVol.setSnap_child_list( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_TID ) ){
                try{
                    int tid = Integer.parseInt( value );
                    curVol.setSnap_target_id( tid );
                }catch(Exception ex){
                    curVol.setSnap_target_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_LocalID ) ){
                try{
                    int localid = Integer.parseInt( value );
                    curVol.setSnap_localid( localid );
                }catch(Exception ex){
                    curVol.setSnap_localid( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_CrtTime ) ){
                curVol.setSnap_create_time( value );
            }
        }else{
            if( s1.indexOf( BasicVDisk.BVDisk_Start ) >=0 ){
                curVol = new Volume();
            }
        }
    }
    
//    public AddOrphanVol( String cmd,Socket socket) throws IOException {
//        super( cmd,socket );
//    }
    
//    public AddOrphanVol( String cmd ){
//        super( cmd );
//    }

    public boolean realDo(){ 
SanBootView.log.info( getClass().getName()," add disk cmd: "+ this.getCmdLine() );
        try{
            curVol = null; 
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," add disk cmd retcode: "+ this.getRetCode() );
        boolean isOk =( getRetCode() == AbstractNetworkRunning.OK );
        if ( !isOk ){
SanBootView.log.error( getClass().getName()," add disk cmd errmsg: "+ this.getErrMsg() );            
        }
        return isOk;
    }
    
    public Volume getCrtVolume(){
        return curVol;
    }
    
    public int getTargetID(){
        if( curVol!=null ){
            return curVol.getTargetID();
        }else{
            return -1;
        }
    }
    
    public int getRootID(){
        if( curVol!=null ){
            return curVol.getSnap_root_id();
        }else{
            return -1;
        }
    }
}