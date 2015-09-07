/*
 * GetBootHost.java
 *
 * Created on July 8, 2005, 8:15 PM
 */

package guisanboot.data;

import java.net.*;
import java.io.*;
import java.util.*;

import guisanboot.ui.*;
import mylib.UI.*;


/**
 *
 * @author  Administrator
 */
public class GetSnapshot extends NetworkRunning implements TreeProcessable{
    private SanBootView view;
    private BrowserTreeNode fNode;
    private boolean addTable;
    private boolean addTree;
    private boolean addCache;
    private GeneralProcessEventForSanBoot processEvent;
    private Snapshot curSnap = null;
    private ArrayList<Snapshot> snapList = new ArrayList<Snapshot>();
 
    public void parser(String line){
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );         
        
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
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Desc ) ){
                curSnap.setSnap_desc( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Owner ) ){
                curSnap.setSnap_owner( value );

                if( curSnap.isSnap() ){
                    if( addTable ){
                        processEvent.insertSomethingToTable( curSnap );
                    }

                    if( addTree ){
                        BrowserTreeNode cNode = new BrowserTreeNode( curSnap,false );
                        curSnap.setTreeNode( cNode );
                        curSnap.setFatherNode( fNode );
                        view.addNode( fNode,cNode );
                    }

                    if( addCache ){
                        snapList.add( curSnap );
                    }
                }
            }
        }else{
            if( s1.startsWith( BasicVDisk.BVDisk_RECFLAG ) ){
                curSnap = new Snapshot();
            }
        }
    }
    
    public GetSnapshot(String cmd, Socket socket,SanBootView _view) throws IOException{
        super( cmd ,socket );
        view = _view;
    }
  
    public GetSnapshot(String cmd){
        super( cmd );
    }
    
    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " getsnapshot cmd: "+this.getCmdLine() ); 
        try{
            curSnap = null;
            snapList.clear();
            
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " getsnapshot cmd retcode: "+this.getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " getsnapshot cmd errmsg: "+this.getErrMsg() );            
        }
        return isOk;
    }
    
    public void setFatherTreeNode( BrowserTreeNode _fNode){
        fNode = _fNode;
    }
    
    public void setProcessEvent( GeneralProcessEventForSanBoot _processEvent ){
        processEvent = _processEvent;
    }
    
    public void setAddTableFlag( boolean _addTable ){
        addTable = _addTable;
    }
    
    public void setAddTreeFlag( boolean _addTree ){
        addTree = _addTree;
    }
    
    public void setAddCacheFlag( boolean val ){
        addCache = val;
    }
    
    public ArrayList<Snapshot> getAllSnapList(){
        int size = snapList.size();
        ArrayList<Snapshot> ret = new ArrayList<Snapshot>( size );
        for( int i=size-1; i>=0; i-- ){
            ret.add( snapList.get(i) );
        }
        return ret;
    }
    
    public ArrayList<SnapWrapper> getAllSnapWrapperList(){
        SnapWrapper wap;
        
        int size = snapList.size();
        ArrayList<SnapWrapper> ret = new ArrayList<SnapWrapper>( size );
        for( int i=size-1; i>=0; i-- ){
            wap = new SnapWrapper( snapList.get(i));
            ret.add( wap );
        }
        return ret;
    }
    
    public ArrayList<SnapWrapper> getAllSnapWrapperList( int rootid ){
        SnapWrapper wap;
        Snapshot snap;
        
        int size = snapList.size();
        ArrayList<SnapWrapper> ret = new ArrayList<SnapWrapper>( size );
        for( int i=size-1; i>=0; i-- ){
            snap = snapList.get(i);
            if( snap.getSnap_root_id() == rootid ){
                wap = new SnapWrapper( (Snapshot)snapList.get(i));
                ret.add( wap );
            }
        }
        return ret;
    }
    
    public int getSnapshotNum(){
        return snapList.size();
    }
    
    public Snapshot getLastSnapshot(){
        int size = snapList.size();
        return snapList.get( 0 );
    }
}
