/*
 * QueryVSnapDB.java
 *
 * Created on July 8, 2005, 8:15 PM
 */

package guisanboot.data;

import java.net.*;
import java.io.*;
import java.util.*;

import guisanboot.ui.*;

/**
 *
 * @author  Administrator
 */
public class QueryVSnapDB extends NetworkRunning{
    private BasicVDisk vdisk = null;
    private ArrayList<BasicVDisk> vdiskList = new ArrayList<BasicVDisk>();
    
    public void parser(String line){
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
SanBootView.log.debug(getClass().getName(),"========> "+ s1 ); 
        
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
            
            if( s1.startsWith( BasicVDisk.BVDisk_Snap_Local_SnapId ) ){
                try{
                    int snapid = Integer.parseInt( value );
                    vdisk.setSnap_local_snapid( snapid );
                }catch(Exception ex){
                    vdisk.setSnap_local_snapid( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Name) ){
                vdisk.setSnap_name( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Root_ID )){
                try{
                    int rootid = Integer.parseInt( value );
                    vdisk.setSnap_root_id( rootid );
                }catch(Exception ex){
                    vdisk.setSnap_root_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Pool_ID )){
                try{
                    int poolid = Integer.parseInt( value );
                    vdisk.setSnap_pool_id( poolid );
                }catch(Exception ex){
                    vdisk.setSnap_pool_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Parent )){
                try{
                    int parent = Integer.parseInt( value );
                    vdisk.setSnap_parent( parent );
                }catch(Exception ex){
                    vdisk.setSnap_parent( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_BlkSize ) ){
                try{
                    int blksize = Integer.parseInt( value );
                    vdisk.setSnap_block_size( blksize );
                }catch(Exception ex){
                    vdisk.setSnap_block_size( 0 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_MaxBlkNo ) ){
                try{
                    int maxblkno = Integer.parseInt( value );
                    vdisk.setSnap_max_block_no( maxblkno );
                }catch(Exception ex){
                    vdisk.setSnap_max_block_no( 0 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_OpenType ) ){
                try{
                    int type = Integer.parseInt( value );
                    vdisk.setSnap_opened_type( type );
                }catch(Exception ex){
                    vdisk.setSnap_opened_type( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Child_List )){
                vdisk.setSnap_child_list( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_TID ) ){
                try{
                    int tid = Integer.parseInt( value );
                    vdisk.setSnap_target_id( tid );
                }catch(Exception ex){
                    vdisk.setSnap_target_id( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_LocalID ) ){
                try{
                    int localid = Integer.parseInt( value );
                    vdisk.setSnap_localid( localid );
                }catch(Exception ex){
                    vdisk.setSnap_localid( -1 );
                }
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_CrtTime ) ){
                vdisk.setSnap_create_time( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Desc ) ){
                vdisk.setSnap_desc( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Owner ) ){
                vdisk.setSnap_owner( value );
                vdiskList.add( vdisk );
            }
        }else{
            if( s1.startsWith( BasicVDisk.BVDisk_RECFLAG ) ){
                vdisk = new BasicVDisk();
            }
        }
    }
    
    public QueryVSnapDB( String cmd, Socket socket ) throws IOException{
        super( cmd ,socket );
    }
  
    public QueryVSnapDB(String cmd){
        super( cmd );
    }
    
    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " query vsnap db cmd: "+this.getCmdLine() ); 
        try{
            vdisk = null;
            vdiskList.clear();
            
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " query vsnap cmd retcode: "+this.getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " query vsnap cmd errmsg: "+this.getErrMsg() );            
        }
        return isOk;
    }
    
    public ArrayList<BasicVDisk> getAllResult(){
        int size = vdiskList.size();
        ArrayList<BasicVDisk> ret = new ArrayList<BasicVDisk>( size );
        for( int i=0; i<size; i++ ){
            ret.add( vdiskList.get(i) );
        }
        return ret;
    }
    
    public int getAllResultNum(){
        return this.getAllResult().size();
    }

    public ArrayList<BasicVDisk> getAllSnapList(){
        BasicVDisk aVdisk;
        
        int size = vdiskList.size();
        ArrayList<BasicVDisk> ret = new ArrayList<BasicVDisk>( size );
        for( int i=size-1; i>=0; i-- ){
            aVdisk = vdiskList.get( i );
            if( aVdisk.isSnap() ){
                ret.add( aVdisk );
            }
        }
        return ret;
    }
    
    public BasicVDisk getVDisk( int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.getSnap_local_snapid() == local_snap_id ){
                return vdisk;
            }
        }
        return null;
    }
    
    public BasicVDisk getVDisk( int rootid,int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( ( vdisk.getSnap_root_id() == rootid ) && 
                ( vdisk.getSnap_local_snapid() == local_snap_id )
            ){
                return vdisk;
            }
        }
        return null;
    }

    public Volume getUIMirVol( int rootid ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isUIMirroredSnapHeader() ){
                if( vdisk.getSnap_root_id() == rootid ){
                    Volume vol = new Volume( vdisk );
                    return vol;
                }
            }
        }
        return null;
    }

    public Volume getVolume( int rootid ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isOriDisk() || vdisk.isCmdpDisk() ){
                if( vdisk.getSnap_root_id() == rootid ){
                    Volume vol = new Volume( vdisk ); 
                    return vol;
                }
            }
        }
        return null;
    }
    
    public View getView( int rootid ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isView() ){
                if( vdisk.getSnap_root_id() == rootid ){
                    View view = new View( vdisk ); 
                    return view;
                }
            }
        }
        return null;
    }
    
    public Snapshot getCommonSnapshotFromQuerySql( int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isSnap() || vdisk.isMirroredSnap() || vdisk.isMirroredSnapHeader() ){
                if( vdisk.getSnap_local_snapid() == local_snap_id ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }
    
    public Snapshot getSnapshotFromQuerySql( int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isSnap() ){
                if( vdisk.getSnap_local_snapid() == local_snap_id ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }

    public Snapshot  getGeneralSnapshotFromQuerySql( int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isSnap() || vdisk.isMirroredSnap() || vdisk.isUIMirroredSnap() ){
                if( vdisk.getSnap_local_snapid() == local_snap_id ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }

    public DeletingSnapshot getDelSnapshotFromQuerySql( int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isDeletingSnap() ){ 
                if( vdisk.getSnap_local_snapid() == local_snap_id ){
                    DeletingSnapshot snap = new DeletingSnapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }
    
    public Snapshot getMirroredSnapshotFromQuerySql( int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isMirroredSnap() ){ 
                if( vdisk.getSnap_local_snapid() == local_snap_id ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }

    public Snapshot getUIMirroredSnapshotFromQuerySql( int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isUIMirroredSnap()){
                if( vdisk.getSnap_local_snapid() == local_snap_id ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }

    public Snapshot getMirroredSnapshotFromQuerySql( int rootid,int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isMirroredSnap() ){ 
                if( ( vdisk.getSnap_root_id() == rootid ) &&
                    ( vdisk.getSnap_local_snapid() == local_snap_id )
                ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }

    public Snapshot getUIMirroredSnapshotFromQuerySql( int rootid,int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isUIMirroredSnap() ){
                if( ( vdisk.getSnap_root_id() == rootid ) &&
                    ( vdisk.getSnap_local_snapid() == local_snap_id )
                ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }

    public Snapshot getMirroredSnapshotHeaderFromQuerySql( int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isMirroredSnapHeader() ){ 
                if( vdisk.getSnap_local_snapid() == local_snap_id ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }

    public Snapshot getUIMirroredSnapshotHeaderFromQuerySql( int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isUIMirroredSnapHeader() ){
                if( vdisk.getSnap_local_snapid() == local_snap_id ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }

    public Snapshot getMirroredSnapshotHeaderFromQuerySql( int rootid,int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isMirroredSnapHeader() ){ 
                if( ( vdisk.getSnap_root_id() == rootid ) && 
                    ( vdisk.getSnap_local_snapid() == local_snap_id ) 
                ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }

    public Snapshot getUIMirroredSnapshotHeaderFromQuerySql( int rootid,int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isUIMirroredSnapHeader() ){
                if( ( vdisk.getSnap_root_id() == rootid ) &&
                    ( vdisk.getSnap_local_snapid() == local_snap_id )
                ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }

    public Snapshot getAvailableMirroredSnapshotFromQuerySql( int rootid,int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isMirroredSnap() || vdisk.isMirroredSnapHeader() || vdisk.isSnap()
            ){
                if( ( vdisk.getSnap_root_id() == rootid ) &&
                    ( vdisk.getSnap_local_snapid() == local_snap_id ) 
                ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }

    public Snapshot getSnapshotFromQuerySql( int rootid,int local_snap_id ){
        int size = vdiskList.size();
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isSnap() || vdisk.isMirroredSnap() ){
                if( ( vdisk.getSnap_root_id() == rootid ) &&
                    ( vdisk.getSnap_local_snapid() == local_snap_id ) 
                ){
                    Snapshot snap = new Snapshot( vdisk );
                    return snap;
                }
            }
        }
        return null;
    }
    
    public ArrayList<BasicVDisk> getAllViewList(){
        BasicVDisk aVDisk;
        
        int size = vdiskList.size();
        ArrayList<BasicVDisk> ret = new ArrayList<BasicVDisk>( size );
        for( int i=size-1; i>=0; i-- ){
            aVDisk = vdiskList.get(i);
            if( aVDisk.isView() ){
                ret.add( aVDisk );
            }
        }
        return ret;
    }
    
    public ArrayList<View> getViewFromQuerySql( int local_snap_id ){
        int size = vdiskList.size();
        ArrayList<View> ret = new ArrayList<View>( size );
        for( int i=size-1; i>=0; i-- ){
            vdisk = vdiskList.get( i );
            if( vdisk.isView() ){
                if( vdisk.getSnap_parent() == local_snap_id ){
                    View view = new View( vdisk );
                    ret.add( view );
                }
            }
        }
        return ret;
    }
    
    public ArrayList<BasicVDisk> getDiskFromQuerySql( ArrayList local_snap_list ){
        int size = local_snap_list.size();
        ArrayList<BasicVDisk> ret = new ArrayList<BasicVDisk>( size );
        for( int i=0; i<size; i++ ){
            int local_snap_id = ((Integer)local_snap_list.get(i)).intValue();
            BasicVDisk disk = getVDisk( local_snap_id );
            if( disk != null ){
                ret.add( disk );
            }
        }
        return ret;
    }
    
    public ArrayList<BasicVDisk> getDiskFromQuerySql( int rootid,ArrayList local_snap_list ){
        int size = local_snap_list.size();
        ArrayList<BasicVDisk> ret = new ArrayList<BasicVDisk>( size );
        for( int i=0; i<size; i++ ){
            int local_snap_id = ((Integer)local_snap_list.get(i)).intValue();
            BasicVDisk disk = getVDisk( rootid,local_snap_id );
            if( disk != null ){
                ret.add( disk );
            }
        }
        return ret;
    }
    
    public ArrayList<SnapWrapper> getSnapWrapperListFromQuerySql( int rootid ){
        SnapWrapper wap;
        
        int size = vdiskList.size();
        ArrayList<SnapWrapper> ret = new ArrayList<SnapWrapper>( size );
        for( int i=size-1; i>=0; i-- ){
            BasicVDisk disk = vdiskList.get(i);
            if( disk.isSnap() || disk.isMirroredSnap() ){
                if( disk.getSnap_root_id() == rootid ){
                    wap = new SnapWrapper( new Snapshot( disk ) );
                    ret.add( wap );
                }
            }
        }
        return ret;
    }

    public ArrayList<Snapshot> getSnapListFromQuerySql( int rootid ){
        Snapshot snap;
        
        int size = vdiskList.size();
        ArrayList<Snapshot> ret = new ArrayList<Snapshot>( size );
        for( int i=size-1; i>=0; i-- ){
            BasicVDisk disk = vdiskList.get(i);
            if( disk.isSnap() || disk.isMirroredSnap() || disk.isMirroredSnapHeader() ){
                if( disk.getSnap_root_id() == rootid ){
                    snap = new Snapshot( disk );
                    ret.add( snap );
                }
            }
        }
        return ret;
    }

    public ArrayList<Snapshot> getUISnapListFromQuerySql( int rootid ){
        Snapshot snap;

        int size = vdiskList.size();
        ArrayList<Snapshot> ret = new ArrayList<Snapshot>( size );
        for( int i=size-1; i>=0; i-- ){
            BasicVDisk disk = vdiskList.get(i);
            if( disk.isUIMirroredSnap() || disk.isUIMirroredSnapHeader() ){
                if( disk.getSnap_root_id() == rootid ){
                    snap = new Snapshot( disk );
                    ret.add( snap );
                }
            }
        }
        return ret;
    }
}
