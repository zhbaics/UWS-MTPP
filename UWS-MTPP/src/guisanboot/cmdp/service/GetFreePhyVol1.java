/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.service;

import guisanboot.cmdp.entity.VolumeWrapper2;
import guisanboot.data.BasicVDisk;
import guisanboot.data.GetFreePhyVol;
import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.net.Socket;
import mylib.UI.BrowserTreeNode;

/**
 * GetFreePhyVol1.java
 *
 * Created on 2010-6-24, 11:40:48
 */
public class GetFreePhyVol1 extends GetFreePhyVol{
    public GetFreePhyVol1( String cmd,Socket socket,SanBootView view) throws IOException {
        super( cmd,socket,view );
    }

    @Override public void parser(String line) {
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

                if( addTable ){
                    if( filter ){
                        if( ( view.initor.mdb.getVolMapFromVecOnTID( curVol.getSnap_target_id() ) == null ) &&
                           ( view.initor.mdb.getMDIFromCacheOnTID( curVol.getSnap_target_id() ) == null ) &&
                           ( view.initor.mdb.getCloneDiskOnTid( curVol.getSnap_target_id() ) == null )
                        ){
                            processEvent.insertSomethingToTable( curVol );
                        }
                    }else{
                        processEvent.insertSomethingToTable( curVol );
                    }
                }

                if( addTree ){
                    if( filter ){
                        if( ( view.initor.mdb.getVolMapFromVecOnTID( curVol.getSnap_target_id() ) == null ) &&
                            ( view.initor.mdb.getMDIFromCacheOnTID( curVol.getSnap_target_id() ) == null )  &&
                            ( view.initor.mdb.getCloneDiskOnTid( curVol.getSnap_target_id() ) == null )
                        ){
                            BrowserTreeNode cNode = new BrowserTreeNode( curVol,false );
                            curVol.setTreeNode( cNode );
                            curVol.setFatherNode( fNode );
                            view.addNode( fNode,cNode );
                        }
                    }else{
                        BrowserTreeNode cNode = new BrowserTreeNode( curVol,false );
                        curVol.setTreeNode( cNode );
                        curVol.setFatherNode( fNode );
                        view.addNode( fNode,cNode );
                    }
                }

                if( addCache ){
                    if( filter ){
                        if( ( view.initor.mdb.getVolMapFromVecOnTID( curVol.getTargetID() ) == null ) &&
                            ( view.initor.mdb.getMDIFromCacheOnTID( curVol.getSnap_target_id() ) == null ) &&
                            ( view.initor.mdb.getCloneDiskOnTid( curVol.getSnap_target_id() ) == null )
                        ){
                            cache.add( curVol );
                        }
                    }else{
                        cache.add( curVol );
                    }
                }
            }
        }else{
            //if( s1.indexOf( BasicVDisk.BVDisk_Start ) >=0 ){
            // for cmdp (2010.6.18)
            if( s1.indexOf( BasicVDisk.BVDisk_RECFLAG ) >=0 ){
                curVol = new VolumeWrapper2();
            }
        }
    }
}
