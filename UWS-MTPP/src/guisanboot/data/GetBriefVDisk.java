/*
 * GetBriefVDisk.java
 *
 * Created on July 20, 2008, 8:15 PM
 */

package guisanboot.data;

import java.net.*;
import java.io.*;
import java.util.*;
import guisanboot.ui.*;
import java.util.regex.Pattern;

/**
 *
 * @author  Administrator
 */
public class GetBriefVDisk extends NetworkRunning {
    private BriefVDisk curVDisk = null;
    private ArrayList<BriefVDisk> cache = new ArrayList<BriefVDisk>();
    
    public void parser(String line){
// id=7    poolid=1        status=2        create_time=20080429150321      0 bytes
// id=3    poolid=1        status=0        create_time=20080429135432      393216 bytes        
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"=======> "+ s1 );
        try{
            String[] list = Pattern.compile("\\s+").split( s1,-1 );
SanBootView.log.debug( getClass().getName()," items in line is : "+ list.length );            
            if( list.length >=6 ){
                curVDisk = new BriefVDisk();
                
                int local_id = Integer.parseInt( list[0].trim().substring( 3 ) );
                curVDisk.setLocal_snap_id( local_id );
                
                int pool_id = Integer.parseInt( list[1].trim().substring( 7 ) );
                curVDisk.setPool_id( pool_id );
                
                int aStatus = Integer.parseInt( list[2].trim().substring( 7 ) );
                curVDisk.setOpened_type( aStatus );
                
                curVDisk.setCreate_time( list[3].trim().substring(12) );
                
                long size = Long.parseLong( list[4].trim() );
                curVDisk.setSize( size );
                
                curVDisk.setUnit( list[5].trim() );
                cache.add( curVDisk );
            }
        }catch( Exception ex ){
SanBootView.log.error( getClass().getName(), ex.getMessage() );            
            ex.printStackTrace();
            curVDisk = null;
        }
    }
    
    public GetBriefVDisk (String cmd, Socket socket ) throws IOException{
        super( cmd ,socket );
    }
    
    public GetBriefVDisk(String cmd){
        super( cmd );
    }
    
    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " get brief disk cmd: "+this.getCmdLine() ); 
        try{
            curVDisk = null;
            cache.clear();
            
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get brief disk cmd retcode: "+this.getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get brief disk cmd errmsg: "+this.getErrMsg() );            
        }
        return isOk;
    }
    
    public ArrayList<BriefVDisk> getAllDiskList1(){
        int size = cache.size();
        ArrayList<BriefVDisk> ret = new ArrayList<BriefVDisk>( size );
        for( int i=0; i<size; i++ ){
            ret.add( cache.get(i) );
        }
        return ret;
    }
    
    public ArrayList<BriefVDisk> getAllDiskList(){
        int size = cache.size();
        ArrayList<BriefVDisk> ret = new ArrayList<BriefVDisk>( size );
        for( int i=size-1; i>=0; i-- ){
            ret.add( cache.get(i) );
        }
        return ret;
    }
    
    public ArrayList<BriefVDisk> getLimitedDiskList( int done_snap_id ){
        BriefVDisk bdisk;
        
        int size = cache.size();
        ArrayList<BriefVDisk> ret = new ArrayList<BriefVDisk>( size );
        for( int i=size-1; i>=0; i-- ){
            bdisk = (BriefVDisk)cache.get( i );
SanBootView.log.debug(getClass().getName(), " localid of disk:  "+bdisk.getLocal_snap_id() +"  mj done_snap_id : "+done_snap_id );            
            if( bdisk.getLocal_snap_id() <= done_snap_id ){
                ret.add( bdisk );
            }
        }
        return ret;
    }
}
