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
import guisanboot.ui.*;
import guisanboot.res.*;

public class AddView extends NetworkRunning{
    private View curView;
    
    public void parser(String line) {
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
SanBootView.log.debug( getClass().getName(), "(AddView): "+ s1 );
        
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
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Type ) ){
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
            }
        }else{
            if( s1.indexOf( BasicVDisk.BVDisk_Start ) >=0 ){
                curView = new View();
            }
        }
    }
    
    public AddView( String cmd,Socket socket) throws IOException {
        super( cmd,socket );
    }
    
    public AddView( String cmd ){
        super( cmd );
    }

    public boolean realDo( int rootid,int snapid,String vname,String timestamp ){
        String aCmdLine = ResourceCenter.getCmd( ResourceCenter.CMD_ADD_VIEW ) +
                rootid + " "  + snapid + ( vname.equals("")?"":" -v " + vname ) +
                ( timestamp.equals("")?"":" -s " + timestamp );
        this.setCmdLine( aCmdLine );
SanBootView.log.info( getClass().getName()," add view cmd:"+ this.getCmdLine() );

        try{
            curView = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," add view cmd retcode:"+ this.getRetCode()  );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," add view cmd errmsg:"+ this.getErrMsg()  );
        }
        return isOk;
    }

    public boolean realDo( int rootid,int snapid,String vname ){
        String aCmdLine = ResourceCenter.getCmd( ResourceCenter.CMD_ADD_VIEW ) +
                rootid + " "  + snapid +
                ( vname.equals("")?"":" -v " + vname );
        this.setCmdLine( aCmdLine );
SanBootView.log.info( getClass().getName()," add view cmd:"+ this.getCmdLine() );

        try{
            curView = null; 
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," add view cmd retcode:"+ this.getRetCode()  );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," add view cmd errmsg:"+ this.getErrMsg()  );            
        }
        return isOk;
    }
    
    public boolean realDo( int poolid,int rootid,int snapid,String vname ){
        String aCmdLine = ResourceCenter.getCmd( ResourceCenter.CMD_ADD_VIEW ) +
                rootid + " "  + snapid + " -p " + poolid +
                ( vname.equals("")?"":" -v " + vname );
        this.setCmdLine( aCmdLine );
SanBootView.log.info( getClass().getName()," add view cmd:"+ this.getCmdLine() );

        try{
            curView = null; 
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," add view cmd retcode:"+ this.getRetCode()  );

        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," add view cmd errmsg:"+ this.getErrMsg()  );            
        }
        return isOk;
    }
    
    public View getCrtView(){
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