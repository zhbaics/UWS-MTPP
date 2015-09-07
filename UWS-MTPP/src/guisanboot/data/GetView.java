/*
 * GetView.java
 *
 * Created on July 8, 2005, 8:15 PM
 */

package guisanboot.data;

import java.net.*;
import java.io.*;
import java.util.*;

import guisanboot.ui.*;
import mylib.UI.*;
import mylib.tool.*;

/**
 *
 * @author  Administrator
 */
public class GetView extends NetworkRunning implements TreeProcessable{
    private SanBootView view;
    private BrowserTreeNode fNode;
    private boolean addTable;
    private boolean addTree;
    private boolean addCache;
    private GeneralProcessEventForSanBoot processEvent;
    private View curView = null;
    private ArrayList<View> viewList = new ArrayList<View>();
    private int rootid=-1;
    private int parentid=-1;
 
    public void parser(String line){
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );  
        
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
            }else if( s1.startsWith( BasicVDisk.BVDisk_Snap_Desc ) ){
                curView.setSnap_desc( value );
                
                if( curView.isView() ){
                    if( rootid == -1 && parentid == -1 ){
                        if( addTable ){
                            processEvent.insertSomethingToTable( curView );
                        }
                        
                        if( addTree ){
                            BrowserTreeNode cNode = new BrowserTreeNode( curView,false );
                            curView.setTreeNode( cNode );
                            curView.setFatherNode( fNode );
                            view.addNode( fNode,cNode );
                        }
                        
                        if( addCache ){
                            viewList.add( curView );
                        }
                    }else{
                        if( parentid == -1 ){
                            if( curView.getSnap_root_id() == rootid ){
                                if( addTable ){
                                    processEvent.insertSomethingToTable( curView );
                                }

                                if( addTree ){
                                    BrowserTreeNode cNode = new BrowserTreeNode( curView,false );
                                    curView.setTreeNode( cNode );
                                    curView.setFatherNode( fNode );
                                    view.addNode( fNode,cNode );
                                }

                                if( addCache ){
                                    viewList.add( curView );
                                }
                            }
                        }else{
                            if ( ( curView.getSnap_root_id() == rootid ) &&
                                 ( curView.getSnap_parent() == parentid ) ){
                                if( addTable ){
                                    processEvent.insertSomethingToTable( curView );
                                }

                                if( addTree ){
                                    BrowserTreeNode cNode = new BrowserTreeNode( curView,false );
                                    curView.setTreeNode( cNode );
                                    curView.setFatherNode( fNode );
                                    view.addNode( fNode,cNode );
                                }

                                if( addCache ){
                                    viewList.add( curView );
                                }
                            }
                        }
                    }
                }
            }
        }else{
            if( s1.indexOf( BasicVDisk.BVDisk_Start) >=0 ){
                curView = new View();
            }
        }
    }
    
    public GetView(String cmd, Socket socket,SanBootView _view) throws IOException{
        super( cmd ,socket );
        view = _view;
    }
    
    public GetView(String cmd, Socket socket,SanBootView view,int rootid,int parentid) throws IOException{
        this( cmd,socket,view);
        this.rootid = rootid;
        this.parentid = parentid;
    }
    
    public GetView(String cmd){
        super( cmd );
    }
    
    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " get view cmd: "+this.getCmdLine() );     
        try{
            curView = null;
            viewList.clear();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get view cmd retcode: "+this.getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get view cmd errMsg: "+this.getErrMsg() );             
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
    
    public void setRootId( int rootid ){
        this.rootid = rootid;
    }
    
    public void setParentId( int parentid ){
        this.parentid = parentid;
    }
    
    public ArrayList<View> getAllViewList(){
        int size = viewList.size();
        ArrayList<View> ret = new ArrayList<View>( size );
        for( int i=size-1; i>=0; i-- ){
            ret.add( viewList.get(i) );
        }
        return ret;
    }
    
    public ArrayList<ViewWrapper> getAllViewWrapperList(){
        ViewWrapper view;
        
        int size = viewList.size();
        ArrayList<ViewWrapper> ret = new ArrayList<ViewWrapper>( size );
        for( int i=size-1; i>=0; i-- ){
            view = new ViewWrapper( viewList.get(i));
            ret.add( view );
        }
        return ret;
    }
    
    public ArrayList<ViewWrapper> getAllViewWrapperList( int rootid,int parentid ){
        ViewWrapper view;
        View v;
        
        int size = viewList.size();
        ArrayList<ViewWrapper> ret = new ArrayList<ViewWrapper>( size );
        for( int i=size-1; i>=0; i-- ){
            v = (View)viewList.get(i);
            if( ( v.getSnap_root_id() == rootid ) && ( v.getSnap_parent() == parentid ) ){
                view = new ViewWrapper( (View)viewList.get(i));
                ret.add( view );
            }
        }
        return ret;
    }
}
