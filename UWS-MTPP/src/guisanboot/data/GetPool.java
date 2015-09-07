/*
 * GetPool.java
 *
 * Created on 2008/2/19,�PM 15:25
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import java.net.*;
import java.io.*;


/**
 *
 * @author Administrator
 */
public class GetPool extends NetworkRunning{
    private ArrayList<Pool> poolList = new ArrayList<Pool>();
    private Pool curPool = null;
    
    public void parser(String line){
        String s1 = line.trim();

        int index = s1.indexOf("=");
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );             
        
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
            
            if( s1.startsWith( Pool.POOL_ID ) ){
                try{
                    int id = Integer.parseInt( value );
                    curPool.setPool_id( id );
                }catch(Exception ex){
                    curPool.setPool_id( -1 );
                }
            }else if( s1.startsWith( Pool.POOL_NAME )){
                curPool.setPool_name( value );
            }else if( s1.startsWith( Pool.POOL_PATH )){
                curPool.setPool_path( value );
            }else if( s1.startsWith( Pool.POOL_VOLNAME)){
                curPool.setPool_vol_name( value );
            }else if( s1.startsWith( Pool.POOL_PASSWD ) ){
                curPool.setPool_passwd( value );
                poolList.add( curPool );
            }else if( s1.startsWith( Pool.POOL_FLAG ) ){
                try{
                    int flag = Integer.parseInt( value );
                    curPool.setPoolFlag( flag );
                }catch( Exception ex ){
                    curPool.setPoolFlag( 1 );
                }
            }else if( s1.startsWith( Pool.POOL_MELT_MOVE_DATA ) ){
                try{
                    int melt_flag = Integer.parseInt( value );
                    curPool.setPoolMeltMoveData(  melt_flag );
                }catch( Exception ex ){
                    curPool.setPoolMeltMoveData( 0 );
                }
            }else if( s1.startsWith( Pool.POOL_TYPE ) ){
                try{
                    int poolType = Integer.parseInt( value );
                    curPool.setPool_Type(poolType);
                } catch( Exception ex ){
                    curPool.setPool_Type(0);
                }
            }
        }else{
            if( s1.startsWith( Pool.POOL_RECFLAG ) ){
                curPool = new Pool();
            }
        }
    }
    
    public GetPool( String cmd,Socket socket ) throws IOException{
        super( cmd ,socket );
    }
    
    public GetPool( String cmd ){
        super( cmd );
    }
    
    public boolean updatePool() {
SanBootView.log.info( getClass().getName(), " getpool cmd: "+ getCmdLine() );         
        try{
            poolList.clear();
            curPool = null;
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " getpool retcode: "+ getRetCode() );   
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " getpool errmsg: "+ getErrMsg() );               
        }
        return isOk;
    }
    
    public void addPool( Pool pool ){
        poolList.add( pool );
    }

    public void removePool( Pool pool ){
        poolList.remove( pool );
    }
    
    public Pool getPoolOnID( int id ){
        int size = poolList.size();
        for( int i=0; i<size; i++ ){
            Pool pool =  poolList.get(i);
            if( pool.getPool_id() == id ){
                return pool;
            }
        }
        return null;
    }
    
    public PoolWrapper getPoolWrapOnID( int id ){
        int size = poolList.size();
        for( int i=0; i<size; i++ ){
            Pool pool = poolList.get(i);
            if( pool.getPool_id() == id ){
                return new PoolWrapper( pool );
            }
        }
        return null;
    }
    
    public ArrayList<Pool> getAllPool(){
        int size = poolList.size();
        ArrayList<Pool> ret  = new ArrayList<Pool>( size );
        for( int i=0;i<size;i++ ){
            ret.add( poolList.get(i) );
        }
        return ret;
    }
    
    public ArrayList<Pool> getAllNormalPool(){
        int size = poolList.size();
        ArrayList<Pool> ret  = new ArrayList<Pool>( size );
        for( int i=0;i<size;i++ ){
            if( poolList.get(i).isNormalPool()){
                ret.add( poolList.get(i) );
            }
        }
        return ret;
    }

    public ArrayList<PoolWrapper> getAllPoolWraper( boolean hasSpace ){
        Pool pool;
        PoolWrapper wrap;
        
        int size = poolList.size();
        ArrayList<PoolWrapper> ret  = new ArrayList<PoolWrapper>( size );
        for( int i=0;i<size;i++ ){
            pool = (Pool)poolList.get(i);
            if( hasSpace ){
                if( pool.getRealFreeSize() > 0 ){
                    wrap = new PoolWrapper( pool );
                    ret.add( wrap );
                }
            }else{
                wrap = new PoolWrapper( pool );
                ret.add( wrap );
            }
        }
        return ret;
    }
    
    public ArrayList<PoolWrapper> getAllNormalPoolWraper( boolean hasSpace ){
        Pool pool;
        PoolWrapper wrap;
        
        int size = poolList.size();
        ArrayList<PoolWrapper> ret  = new ArrayList<PoolWrapper>( size );
        for( int i=0;i<size;i++ ){
            pool = (Pool)poolList.get(i);
            if( pool.isNormalPool() ){
                if( hasSpace ){
                    if( pool.getRealFreeSize() > 0 ){
                        wrap = new PoolWrapper( pool );
                        ret.add( wrap );
                    }
                }else{
                    wrap = new PoolWrapper( pool );
                    ret.add( wrap );
                }
            }
        }
        return ret;
    }
    
    public int getPoolNum(){
        return poolList.size();
    }

    public ArrayList<PoolWrapper> getUcsPoolWrapper(boolean hasSpace){
        Pool pool;
        PoolWrapper wrap;

        int size = poolList.size();
        ArrayList<PoolWrapper> ret  = new ArrayList<PoolWrapper>( size );
        for( int i=0;i<size;i++ ){
            pool = (Pool)poolList.get(i);
            if( hasSpace ){
                if( pool.getRealFreeSize() > 0 ){
                    if(pool.isUCSPool()){
                        wrap = new PoolWrapper( pool );
                        ret.add( wrap );
                    }
                }
            }
        }
        return ret;
    }

     public ArrayList<Pool> getUcsPool(){
         Pool pool;
        int size = poolList.size();
        ArrayList<Pool> ret  = new ArrayList<Pool>( size );
        for( int i=0;i<size;i++ ){
            pool = (Pool)poolList.get(i);
            if(pool.isUCSPool()){
                ret.add( poolList.get(i) );
            }
        }
        return ret;
    }
}
