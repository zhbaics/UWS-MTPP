package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2011
 * Company:      odysys
 * @author
 * @version 1.0
 */
import java.io.*;
import java.net.*;
import guisanboot.ui.*;
import guisanboot.res.*;

public class AddSnapshot extends NetworkRunning{
    private Snapshot curSnap;
    
    public void parser(String line) {
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
SanBootView.log.debug( getClass().getName(), "(AddSnapshot): "+ s1 );
        
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
            
            if( s1.startsWith( BasicVDisk.BVDisk_Snap_Local_SnapId ) ){
                try{
                    int snapid = Integer.parseInt( value );
                    curSnap.setSnap_local_snapid( snapid );
                }catch(Exception ex){
                    curSnap.setSnap_local_snapid( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Name) ){
                curSnap.setSnap_name( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Root_ID )){
                try{
                    int rootid = Integer.parseInt( value );
                    curSnap.setSnap_root_id( rootid );
                }catch(Exception ex){
                    curSnap.setSnap_root_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Pool_ID )){
                try{
                    int poolid = Integer.parseInt( value );
                    curSnap.setSnap_pool_id( poolid );
                }catch(Exception ex){
                    curSnap.setSnap_pool_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Parent )){
                try{
                    int parent = Integer.parseInt( value );
                    curSnap.setSnap_parent( parent );
                }catch(Exception ex){
                    curSnap.setSnap_parent( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_BlkSize ) ){
                try{
                    int blksize = Integer.parseInt( value );
                    curSnap.setSnap_block_size( blksize );
                }catch(Exception ex){
                    curSnap.setSnap_block_size( 0 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_MaxBlkNo ) ){
                try{
                    int maxblkno = Integer.parseInt( value );
                    curSnap.setSnap_max_block_no( maxblkno );
                }catch(Exception ex){
                    curSnap.setSnap_max_block_no( 0 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_OpenType ) ){
                try{
                    int type = Integer.parseInt( value );
                    curSnap.setSnap_opened_type( type );
                }catch(Exception ex){
                    curSnap.setSnap_opened_type( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Child_List )){
                curSnap.setSnap_child_list( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_TID ) ){
                try{
                    int tid = Integer.parseInt( value );
                    curSnap.setSnap_target_id( tid );
                }catch(Exception ex){
                    curSnap.setSnap_target_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_LocalID ) ){
                try{
                    int localid = Integer.parseInt( value );
                    curSnap.setSnap_localid( localid );
                }catch(Exception ex){
                    curSnap.setSnap_localid( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_CrtTime ) ){
                curSnap.setSnap_create_time( value );
            }
        }else{
            if( s1.indexOf( BasicVDisk.BVDisk_Start ) >=0 ){
                curSnap = new Snapshot();
            }
        }
    }
    
    public AddSnapshot( String cmd,Socket socket) throws IOException {
        super( cmd,socket );
    }
    
    public AddSnapshot( String cmd ){
        super( cmd );
    }

    public boolean realDo( int rootid,int snapid,String name,int poolid ){
        String aCmdLine = ResourceCenter.getCmd( ResourceCenter.CMD_ADD_SNAP1 ) +
                name + " " + rootid + " " + snapid;
        if( poolid != -1 ){
            aCmdLine += " " + poolid;
        }
        this.setCmdLine( aCmdLine );
SanBootView.log.info( getClass().getName()," add snapshot cmd:"+ this.getCmdLine() );

        try{
            curSnap = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," add snapshot cmd retcode:"+ this.getRetCode()  );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," add snapshot cmd errmsg:"+ this.getErrMsg()  );
        }
        return isOk;
    }

    public Snapshot getCrtSnap(){
        return curSnap;
    }
    
    public int getTargetID(){
        if( curSnap!=null ){
            return curSnap.getTargetID();
        }else{
            return -1;
        }
    }

    public int getSnapLocalId(){
        if( curSnap!=null ){
            return curSnap.getSnap_local_snapid();
        }else{
            return -1;
        }
    }
    
    public int getRootID(){
        if( curSnap!=null ){
            return curSnap.getSnap_root_id();
        }else{
            return -1;
        }
    }
}