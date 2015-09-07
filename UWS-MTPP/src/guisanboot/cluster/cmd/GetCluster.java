/*
 * GetCluster.java
 *
 * Created on July 11, 2011, 3:32 PM
 */

package guisanboot.cluster.cmd;

import guisanboot.cluster.entity.Cluster;
import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetCluster extends NetworkRunning {
    private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    private Cluster curCluster = null;
    
    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 ); 

        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value ); 

            if( s1.startsWith( Cluster.CLUSTER_ID ) ){
                try{
                    int id = Integer.parseInt( value );
                    curCluster.setCluster_id( id );
                }catch( Exception ex ){
                    curCluster.setCluster_id( 0 );
                }
                clusters.add( curCluster );
            }else if( s1.startsWith( Cluster.CLUSTER_NAME )){
                curCluster.setCluster_name( value );
            }else if( s1.startsWith( Cluster.CLUSTER_INIT_FLAG ) ){
                try{
                    int init_flag = Integer.parseInt( value );
                    curCluster.setCluster_inited_flag( init_flag );
                }catch(Exception ex){
                    curCluster.setCluster_inited_flag( 0 );
                }
            }else if( s1.startsWith( Cluster.CLUSTER_AUTO_DR_FLAG )){
                try{
                    int auto_dr_flag = Integer.parseInt( value );
                    curCluster.setCluster_auto_dr_flag( auto_dr_flag );
                }catch(Exception ex){
                    curCluster.setCluster_auto_dr_flag( -1 );
                }
            }else if( s1.startsWith( Cluster.CLUSTER_AUTO_REBOOT_FLAG ) ){
                try{
                    int auto_reboot_flag = Integer.parseInt( value );
                    curCluster.setCluster_auto_reboot_flag( auto_reboot_flag );
                }catch( Exception ex){
                    curCluster.setCluster_auto_reboot_flag( -1 );
                }
            }else if( s1.startsWith( Cluster.CLUSTER_STOP_ALL_BASE_SERV ) ){
                try{
                    int stop_all_base_serv = Integer.parseInt( value );
                    curCluster.setCluster_stop_base_service( stop_all_base_serv );
                }catch( Exception ex){
                    curCluster.setCluster_stop_base_service( -1 );
                }
            }else if( s1.startsWith( Cluster.CLUSTER_BOOT_MAC ) ) {
                curCluster.setCluster_mac_address( value );
            }else if( s1.startsWith( Cluster.CLUSTER_BOOT_MODE ) ){
                try{
                    int mode = Integer.parseInt( value );
                    curCluster.setCluster_boot_mode( mode );
                }catch( Exception ex ){
                    curCluster.setCluster_boot_mode( BootHost.BOOT_MODE_EM );
                }
            }else if( s1.startsWith( Cluster.CLUSTER_PROTECT_TYPE ) ){
                try{
                    int protect_type = Integer.parseInt( value );
                    curCluster.setCluster_protect_type( protect_type );
                }catch(Exception ex){
                    curCluster.setCluster_protect_type( 0 );
                }
            }else if( s1.startsWith( Cluster.CLUSTER_TYPE ) ){
                try{
                    int type = Integer.parseInt( value );
                    curCluster.setCluster_type( type );
                }catch(Exception ex){
                    curCluster.setCluster_type( ResourceCenter.TYPE_CLUS_NOT_RAC_INT );
                }
            }
        }else{
            if( s1.startsWith( Cluster.CLUSTERECFLAG )){
                curCluster = new Cluster();
            }
        }
    }
    
    public GetCluster(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetCluster(String cmd){
        super( cmd );
    }

    public boolean updateCluster() {
SanBootView.log.info( getClass().getName(), " get cluster cmd: " + getCmdLine()  );
        try{
            clusters.clear();
            curCluster = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get cluster retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get cluster errmsg: "+getErrMsg() );
        }
        return isOk;
    }
  
    public void addClusterToVector( Cluster one ){
        clusters.add( one );
    }

    public void removeClusterFromVector( Cluster one ){
        clusters.remove( one );
    }

    public Cluster getClusterFromVectorOnID( long id ){
        int size = clusters.size();
        for( int i=0; i<size; i++ ){
            Cluster cluster = clusters.get(i);
            if( cluster.getCluster_id() == id )
                return cluster;
        }
        return null;
    }
    
    public Cluster getClusterFromVectorOnName( String name ){
        int size = clusters.size();
        for( int i=0;i<size;i++){
            Cluster cluster = clusters.get(i);
            if( cluster.getCluster_name().equals( name ) )
                return cluster;
        }
        return null;
    }

    public ArrayList<Cluster> getAllCluster(){
        int size = clusters.size();
        ArrayList<Cluster> list  = new ArrayList<Cluster>(size);
        for( int i=0; i<size; i++ ){
            Cluster one = (Cluster)clusters.get(i);
            list.add( one );
        }
        return list;
    }
    
    public int getClusterNum(){
        return clusters.size();
    }
}
