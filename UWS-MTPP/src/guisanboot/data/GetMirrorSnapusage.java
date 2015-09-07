/*
 * GetMirrorSnapusage.java
 *
 * Created on July 17, 2008, 8:30 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.util.Vector;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetMirrorSnapusage extends NetworkRunning {
    private ArrayList<SnapUsage> us = new ArrayList<SnapUsage>();
    private SnapUsage curMSU = null;
    
    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 ); 

        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value ); 
            
            if( s1.startsWith( SnapUsage.SU_ID ) ){
                try{
                    int id = Integer.parseInt( value );
                    curMSU.setUsage_id( id );
                }catch(Exception ex){
                    curMSU.setUsage_id( -1 );
                }
            }else if( s1.startsWith( SnapUsage.SU_SRCAGNT_ID )){
                try{
                    int srcagnt_id = Integer.parseInt( value );
                    curMSU.setDst_agent_id( srcagnt_id );
                }catch(Exception ex){
                    curMSU.setDst_agent_id( -1 );
                }
            }else if( s1.startsWith( SnapUsage.SU_SNAP_ROOTID ) ){
                try{
                    int snap_rootid = Integer.parseInt( value );
                    curMSU.setSnap_rootid( snap_rootid );
                }catch(Exception ex){
                    curMSU.setSnap_rootid( -1 );
                }
            }else if( s1.startsWith( SnapUsage.SU_SNAP_LOCALID ) ){
                try{
                    int snap_localid = Integer.parseInt( value );
                    curMSU.setSnap_local_id( snap_localid );
                }catch(Exception ex){
                    curMSU.setSnap_local_id( -1 );
                }
            }else if(s1.startsWith( SnapUsage.SU_VIEW_ID )){
                try{
                    int view_localid = Integer.parseInt( value );
                    curMSU.setSnap_view_local_id( view_localid );
                }catch(Exception ex){
                    curMSU.setSnap_view_local_id( -1 );
                }
            }else if(s1.startsWith( SnapUsage.SU_EXP_MP ) ){
                curMSU.setExport_mp( value );
            }else if( s1.startsWith( SnapUsage.SU_TID ) ){
                try{
                    int tid = Integer.parseInt( value );
                    curMSU.setSnapTid( tid );
                }catch(Exception ex){
                    curMSU.setSnapTid( -1 );
                }
            }else if( s1.startsWith( SnapUsage.SU_CRT_TIME) ){
                curMSU.setCrtTime( value );
                us.add( curMSU );
            }
        }else{
            if( s1.startsWith( SnapUsage.SU_FLAG )){
                curMSU = new SnapUsage();
            }
        }
    }
    
    public GetMirrorSnapusage(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetMirrorSnapusage(String cmd){
        super( cmd );
    }

    public boolean updateMirrorSnapusage() {
SanBootView.log.info( getClass().getName(), " get mirror_snap_usage cmd: " + getCmdLine()  );        
        try{
            us.clear();
            curMSU = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get mirror_snap_usage retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get mirror_snap_usage errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
  
    public void addMSUToVector( SnapUsage msu ){
        us.add( msu );
    }
    
    public void removeMSUFromVector( SnapUsage msu ){
        us.remove( msu );
    }
    
    public ArrayList<SnapUsage> getMSUFromCacheOnDstAgntID( int dstagntID ){
        int size = us.size();
        ArrayList<SnapUsage> ret = new ArrayList<SnapUsage>( size );
        for( int i=0; i<size; i++ ){
            SnapUsage msu = us.get(i);
            if( msu.getDst_agent_id() == dstagntID ){
                ret.add( msu );
            }
        }
        return ret;
    }
    
    public Vector<VolumeMap> getMSUVolMapFromCacheOnDstAgntID( int dstagntID ){
        int size = us.size();
        Vector<VolumeMap> ret = new Vector<VolumeMap>( size );
        for( int i=0; i<size; i++ ){
            SnapUsage msu = us.get(i);
            if( msu.getDst_agent_id() == dstagntID ){
                VolumeMap volMap = new VolumeMap( msu.getExport_mp() );
                ret.addElement( volMap );
            }
        }
        return ret;
    }
    
    public ArrayList<SnapUsage> getAllMSUs(){
        int size = us.size();
        ArrayList<SnapUsage> ret = new ArrayList<SnapUsage>(size);
        for( int i=0; i<size; i++ ){
            ret.add( us.get(i) );
        }
        return ret;
    }
    
    public SnapUsage getSnapUsageOnSomething( int da_id,int rootid,String mp ){
        int size = us.size();
        for( int i=0; i<size; i++ ){
            SnapUsage one = us.get(i);
            if( ( one.getDst_agent_id() == da_id ) &&
                ( one.getSnap_rootid() == rootid ) &&
                ( one.getExport_mp().equals( mp) ) 
            ){
                return one;
            }
        }
        return null;
    }

    public SnapUsage getSnapUsageOnSomething( int da_id,String mp ){
        int size = us.size();
        for( int i=0; i<size; i++ ){
            SnapUsage one = us.get(i);
            if( ( one.getDst_agent_id() == da_id ) &&
                ( one.getExport_mp().substring(0,1).toUpperCase().equals( mp.substring(0,1).toUpperCase() ) )
            ){
                return one;
            }
        }
        return null;
    }

    public ArrayList<SnapUsage> getSnapUsageOnSomething( int da_id,int rootid ){
        int size = us.size();
        ArrayList<SnapUsage> ret = new ArrayList<SnapUsage>( size );
        for( int i=0; i<size; i++ ){
            SnapUsage one = us.get(i);
            if( ( one.getDst_agent_id() == da_id ) &&
                ( one.getSnap_rootid() == rootid ) 
            ){
                ret.add( one );
            }
        }
        return ret;
    }
    
    public ArrayList<SnapUsage> getSnapUsageOnRootID( int rootid ){
        int size = us.size();
        ArrayList<SnapUsage> ret = new ArrayList<SnapUsage>( size );
        for( int i=0; i<size; i++ ){
            SnapUsage one = us.get(i);
            if( one.getSnap_rootid() == rootid ){
                ret.add( one );
            }
        }
        return ret;
    }
    
    public int getMSUNum(){
        return us.size();
    }
}
