package guisanboot.unlimitedIncMj.model;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * 2009/12/16 PM 3:43
 * @author
 * @version 1.0
 */
import guisanboot.data.*;
import java.io.*;
import java.net.*;
import guisanboot.ui.*;
import guisanboot.res.*;

public class ClonediskCmd extends NetworkRunning{
    private BasicVDisk curView;

    public void parser(String line) {
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
SanBootView.log.debug( getClass().getName(), "(Clonedisk): "+ s1 );
        
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
            
            if( s1.startsWith( BasicVDisk.BVDisk_Snap_Local_SnapId ) ){
                try{
                    int snapid = Integer.parseInt( value );
                    curView.setSnap_local_snapid( snapid );
                }catch(Exception ex){
                    curView.setSnap_local_snapid( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Name) ){
                curView.setSnap_name( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Root_ID )){
                try{
                    int rootid = Integer.parseInt( value );
                    curView.setSnap_root_id( rootid );
                }catch(Exception ex){
                    curView.setSnap_root_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Pool_ID )){
                try{
                    int poolid = Integer.parseInt( value );
                    curView.setSnap_pool_id( poolid );
                }catch(Exception ex){
                    curView.setSnap_pool_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Parent )){
                try{
                    int parent = Integer.parseInt( value );
                    curView.setSnap_parent( parent );
                }catch(Exception ex){
                    curView.setSnap_parent( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Orig_ID ) ){
                try{
                    int orig_id = Integer.parseInt( value );
                    curView.setSnap_orig_id( orig_id);
                }catch(Exception ex){
                    curView.setSnap_orig_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_BlkSize ) ){
                try{
                    int blksize = Integer.parseInt( value );
                    curView.setSnap_block_size( blksize );
                }catch(Exception ex){
                    curView.setSnap_block_size( 0 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_MaxBlkNo ) ){
                try{
                    int maxblkno = Integer.parseInt( value );
                    curView.setSnap_max_block_no( maxblkno );
                }catch(Exception ex){
                    curView.setSnap_max_block_no( 0 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_OpenType ) ){
                try{
                    int type = Integer.parseInt( value );
                    curView.setSnap_opened_type( type );
                }catch(Exception ex){
                    curView.setSnap_opened_type( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Child_List )){
                curView.setSnap_child_list( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_TID ) ){
                try{
                    int tid = Integer.parseInt( value );
                    curView.setSnap_target_id( tid );
                }catch(Exception ex){
                    curView.setSnap_target_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_LocalID ) ){
                try{
                    int localid = Integer.parseInt( value );
                    curView.setSnap_localid( localid );
                }catch(Exception ex){
                    curView.setSnap_localid( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_CrtTime ) ){
                curView.setSnap_create_time( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Desc )){
                curView.setSnap_desc( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Owner ) ){
                curView.setSnap_owner( value );
            }
        }else{
            if( s1.indexOf( BasicVDisk.BVDisk_Start ) >=0 ){
                curView = new View();
            }
        }
    }
    
    public ClonediskCmd( String cmd,Socket socket) throws IOException {
        super( cmd,socket );
    }
    
    public ClonediskCmd( String cmd ){
        super( cmd );
    }

    public boolean realDoInFront( int rootid,int snapid,String name,String desc ){
        String aCmdLine = ResourceCenter.getCmd( ResourceCenter.CMD_CLONE_DISK )+" -o MTPP "+
                ( name.equals("") ?"":" -v "+name ) +
                ( desc.equals("")?"":" -d "+desc )+" " +
                rootid + " "  + snapid;
                
        return this.realDo1( aCmdLine );
    }

    // clone disk in background
    public boolean realDo( int rootid,int snapid,String name,String desc ){
        String aCmdLine = ResourceCenter.getCmd( ResourceCenter.CMD_CLONE_DISK ) +" -b -o MTPP " +
                ( name.equals("") ?"":" -v "+name ) +
                ( desc.equals("")?"":" -d "+desc )+" " +
                rootid + " "  + snapid;

        return realDo1( aCmdLine );
    }

    public boolean realDo1( String aCmdLine ){
        this.setCmdLine( aCmdLine );
SanBootView.log.info( getClass().getName()," clone disk cmd:"+ this.getCmdLine() );

        try{
            curView = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," clone disk cmd retcode:"+ this.getRetCode()  );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," clone disk cmd errmsg:"+ this.getErrMsg()  );
        }
        return isOk;
    }

    public BasicVDisk getCloneDisk(){
        return curView;
    }
    
    public int getTargetID(){
        if( curView!=null ){
            return curView.getTargetID();
        }else{
            return -1;
        }
    }
    
    public int getRootID(){
        if( curView!=null ){
            return curView.getSnap_root_id();
        }else{
            return -1;
        }
    }
}