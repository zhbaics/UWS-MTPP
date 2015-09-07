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

import mylib.UI.*;
import guisanboot.ui.*;

public class GetOrphanVol extends NetworkRunning implements TreeProcessable{
    protected SanBootView view;
    protected BrowserTreeNode fNode;
    protected boolean addTable = false;
    protected boolean addTree = false;
    protected boolean addCache = false;
    protected boolean filter = true;
    protected GeneralProcessEventForSanBoot processEvent;
    protected Volume curVol = null;
    protected ArrayList<Volume> cache = new ArrayList<Volume>();
    
    public void parser(String line) {
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );             
        
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
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Desc ) ){
                curVol.setSnap_desc( value );
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Owner ) ){
                curVol.setSnap_owner( value );
                
                if( addTable ){
                    if( filter ){
                        if( ( view.initor.mdb.getVolMapFromVecOnTID( curVol.getSnap_target_id() ) == null ) &&
                           ( view.initor.mdb.getMDIFromCacheOnTID( curVol.getSnap_target_id() ) == null ) &&
                           ( view.initor.mdb.getCloneDiskOnTid( curVol.getSnap_target_id() ) == null )
                        ){
                            if( curVol.isUWSDisk() ) processEvent.insertSomethingToTable( curVol );
                        } 
                    }else{
                        if( curVol.isUWSDisk() ) processEvent.insertSomethingToTable( curVol );
                    }
                }
                
                if( addTree ){
                    if( filter ){
                        if( ( view.initor.mdb.getVolMapFromVecOnTID( curVol.getSnap_target_id() ) == null ) &&
                            ( view.initor.mdb.getMDIFromCacheOnTID( curVol.getSnap_target_id() ) == null )  &&
                            ( view.initor.mdb.getCloneDiskOnTid( curVol.getSnap_target_id() ) == null )
                        ){
                            if( curVol.isUWSDisk() ){
                                BrowserTreeNode cNode = new BrowserTreeNode( curVol,false );
                                curVol.setTreeNode( cNode );
                                curVol.setFatherNode( fNode );
                                view.addNode( fNode,cNode );
                            }
                        }
                    }else{
                        if( curVol.isUWSDisk() ){
                            BrowserTreeNode cNode = new BrowserTreeNode( curVol,false );
                            curVol.setTreeNode( cNode );
                            curVol.setFatherNode( fNode );
                            view.addNode( fNode,cNode );
                        }
                    }
                }

                if( addCache ){
                    if( filter ){
                        if( ( view.initor.mdb.getVolMapFromVecOnTID( curVol.getTargetID() ) == null ) &&
                            ( view.initor.mdb.getMDIFromCacheOnTID( curVol.getSnap_target_id() ) == null ) &&
                            ( view.initor.mdb.getCloneDiskOnTid( curVol.getSnap_target_id() ) == null )
                        ){
                            if( curVol.isUWSDisk() ) cache.add( curVol );
                        }
                    }else{
                        if( curVol.isUWSDisk() ) cache.add( curVol );
                    }
                }
            }
        }else{
            //if( s1.indexOf( BasicVDisk.BVDisk_Start ) >=0 ){
            // for cmdp (2010.6.18)
            if( s1.indexOf( BasicVDisk.BVDisk_RECFLAG ) >=0 ){
                curVol = new Volume();
            }
        }
    }
    
    public GetOrphanVol( String cmd,Socket socket,SanBootView _view) throws IOException {
        super( cmd,socket );
        view = _view;
    }
    
    public GetOrphanVol( String cmd ){
        super( cmd );
    }
    
    public boolean realDo(){ 
SanBootView.log.info( getClass().getName(), "listdisk cmd: "+getCmdLine() ); 
System.out.println("(GetOrphanVol)addcache: "+ addCache );
System.out.println("(GetOrphanVol)filter: "+this.filter );

        try{
            curVol = null;
            if( addCache ){
                cache.clear();
            }
            
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), "listdisk cmd retcode: "+getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), "listdisk cmd errmsg: "+getErrMsg() );             
        }
        return isOk;
    }
    
    public ArrayList<Volume> getAllVolFromCache(){
        int size = cache.size();
        ArrayList<Volume> ret = new ArrayList<Volume>( size );
        for( int i=0; i<size; i++ ){
            ret.add( cache.get(i) );
        }
        return ret;
    }

    public ArrayList<Volume> getAllVolAndFreeDiskFromCache(){
        int size = cache.size();
        ArrayList<Volume> ret = new ArrayList<Volume>( size );
        for( int i=0; i<size; i++ ){
            Volume vol = cache.get(i);
            if ( view.initor.mdb.getCloneDiskOnTid( vol.getSnap_target_id() ) == null ){
                ret.add( cache.get(i) );
            }
        }
        return ret;
    }

    public int getFreeVolNum(){
        return cache.size();
    }

    public Volume getVolume( int rootid ){
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            Volume v = cache.get(i);
            if( v.getSnap_root_id() == rootid ){
                return v;
            }
        }
        
        return null;
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
    
    public void setAddCacheFlag( boolean _addCache ){
        addCache = _addCache;
    }
    
    public void setFilterFlag( boolean val ){
        filter = val;
    }
}
