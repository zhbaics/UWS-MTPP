/*
 * GetBootHost.java
 *
 * Created on July 8, 2005, 8:15 PM
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
public class GetBootHost extends NetworkRunning {
    private Vector<BootHost> hosts = new Vector<BootHost>();
    private BootHost curHost = null;
    
    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 ); 

        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value ); 

            if( s1.startsWith( BootHost.CLNTID ) ){
                try{
                    int id = Integer.parseInt( value );
                    curHost.setID( id );
                }catch( Exception ex ){
                    curHost.setID( -1 );
                }
                hosts.addElement( curHost ); 
            }else if( s1.startsWith( BootHost.CLNTNAME )){
                curHost.setName( value );
            }else if( s1.startsWith( BootHost.CLNTIP)){
                curHost.setIP( value );
            }else if( s1.startsWith( BootHost.CLNTMACHINE)){
                curHost.setMachine( value );
            }else if( s1.startsWith(BootHost.CLNT_BOOT_PORTNAME ) ){
                curHost.setPortName( value );               
            }else if( s1.startsWith( BootHost.CLNTPORT1 ) ){
                try{
                    int clnt_port1 = Integer.parseInt( value );
                    curHost.setClntPort1( clnt_port1 );
                }catch(Exception ex){
                    curHost.setClntPort1( -1 );
                }
            }else if(s1.startsWith( BootHost.CLNTPORT )){
                try{
                    int port = Integer.parseInt( value );
                    curHost.setPort( port );
                }catch(Exception ex){
                    curHost.setPort( -1 );
                }
            }else if(s1.startsWith( BootHost.CLNTOS )){
                curHost.setOS( value );
            }else if(s1.startsWith( BootHost.CLNTSTATUS )){
                curHost.setStatus( value );
            }else if( s1.startsWith( BootHost.CLNT_CONF_IP ) ){
                curHost.setUUID( value );
            }else if( s1.startsWith( BootHost.CLNT_INIT_FLAG ) ){
                try{
                    int init_flag = Integer.parseInt( value );
                    curHost.setInitFlag( init_flag );
                }catch(Exception ex){
                    curHost.setInitFlag( -1 );
                }
            }else if( s1.startsWith( BootHost.CLNT_AUTO_DR_FLAG )){
                try{
                    int auto_dr_flag = Integer.parseInt( value );
                    curHost.setAutoDRFlag( auto_dr_flag );
                }catch(Exception ex){
                    curHost.setAutoDRFlag( -1 );
                }
            }else if( s1.startsWith( BootHost.CLNT_AUTO_REBOOT_FLAG ) ){
                try{
                    int auto_reboot_flag = Integer.parseInt( value );
                    curHost.setAutoRebootFlag( auto_reboot_flag );
                }catch( Exception ex){
                    curHost.setAutoRebootFlag( -1 );
                }
            }else if( s1.startsWith( BootHost.CLNT_STOP_ALL_BASE_SERV ) ){
                try{
                    int stop_all_base_serv = Integer.parseInt( value );
                    curHost.setStopAllBaseServFlag( stop_all_base_serv );
                }catch( Exception ex){
                    curHost.setStopAllBaseServFlag( -1 );
                }
            }else if( s1.startsWith( BootHost.CLNT_BOOT_MAC ) ) {
                curHost.setBootMac( value );
            }else if( s1.startsWith( BootHost.CLNT_BOOT_MODE ) ){
                try{
                    int mode = Integer.parseInt( value );
                    curHost.setBootMode( mode );
                }catch( Exception ex ){
                    curHost.setBootMode( BootHost.BOOT_MODE_EM );
                }
            }else if( s1.startsWith( BootHost.CLNT_MODE ) ){
                try{
                    int clnt_mode = Integer.parseInt( value );
                    curHost.setClntMode( clnt_mode );
                }catch(Exception ex){
                    curHost.setClntMode( 0 );
                }
            }else if( s1.startsWith( BootHost.CLNT_PROTECT_TYPE ) ){
                try{
                    int protect_type = Integer.parseInt( value );
                    curHost.setProtectType( protect_type );
                }catch(Exception ex){
                    curHost.setProtectType( 0 );
                }
            }else if( s1.startsWith( BootHost.CLNT_PRI_IP ) ){
                curHost.setClnt_pri_ip( value );
            }else if( s1.startsWith( BootHost.CLNT_VIP ) ){
                curHost.setClnt_vip( value );
            }else if( s1.startsWith( BootHost.CLNT_CLUSTER_ID ) ){
                try{
                    int cluster_id = Integer.parseInt( value );
                    curHost.setClnt_cluster_id( cluster_id );
                }catch(Exception ex){
                    curHost.setClnt_cluster_id( 0 );
                }
            }else if( s1.startsWith( BootHost.CLNT_D2D_CID ) ){
                try{
                    int d2d_cid = Integer.parseInt( value );
                    curHost.setClnt_d2d_cid( d2d_cid );
                }catch(Exception ex){
                    curHost.setClnt_d2d_cid( 0 );
                }
            }else if( s1.startsWith( BootHost.CLNT_OPT ) ){
                try{
                    int clnt_opt = Integer.parseInt( value );
                    curHost.setClnt_opt( clnt_opt );
                }catch(Exception ex){
                    curHost.setClnt_opt( 0 );
                }
            }
        }else{
            if( s1.startsWith( BootHost.CLNTRECFLAG )){
                curHost = new BootHost();
            }
        }
    }
    
    public GetBootHost(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetBootHost(String cmd){
        super( cmd );
    }

    public boolean updateBootHost() {
SanBootView.log.info( getClass().getName(), " get BootHost cmd: " + getCmdLine()  );        
        try{
            hosts.removeAllElements();
            curHost = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get BootHost retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get BootHost errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
  
    public void addHostToVector( BootHost host ){
        hosts.addElement( host );
    }

    public void removeHostFromVector( BootHost host ){
        hosts.removeElement( host );
    }
    
    public BootHost getHostFromVectorOnID( long id ){
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            BootHost host = hosts.elementAt(i);
            if( host.getID() == id )
                return host;
        }
        return null;
    }
    
    public BootHost getHostFromVectOnD2DClntID( int d2d_clnt_id ){
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            BootHost host = hosts.get(i);
            if( host.getClnt_d2d_cid() == d2d_clnt_id ){
                return host;
            }
        }
        return null;
    }

    public BootHost getHostFromVectOnD2DClntID1( String d2d_clnt_id ){
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            BootHost host = hosts.get(i);
            if( d2d_clnt_id.equals( host.getClnt_d2d_cid()+"" ) ){
                return host;
            }
        }
        return null;
    }
    
    public BootHost isExistBootHost( String uuid ){
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            BootHost clnt = (BootHost)hosts.get(i);
            if( clnt.getUUID().equals( uuid ) ){
                return clnt;
            }
        }

        return null;
    }

    public BootHost getHostFromVectorOnName( String name ){
        int size = hosts.size();
        for( int i=0;i<size;i++){
            BootHost agt = hosts.elementAt(i);
            if( agt.getName().equals( name ) )
                return agt;
        }
        return null;
    }

    public BootHost getHostFromVecOnIP( String ip ){
        int size = hosts.size();
        for( int i=0;i<size;i++){
            BootHost agt = hosts.elementAt(i);         
            if( agt.getIP().equals( ip ) )
                return agt;
        }
        return null;
    }
    
    public Vector getAllBootHost(){
        BootHost one;
        
        Vector<BootHost> list  = new Vector<BootHost>();
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            one = (BootHost)hosts.elementAt(i);
            list.addElement( one );            
        }
        return list;
    }
    
    public Vector getAllRealBootHost(){
        BootHost one;

        Vector<BootHost> list  = new Vector<BootHost>();
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            one = (BootHost)hosts.elementAt(i);
            if( one.isRealHostInCluster() ){
                list.addElement( one );
            }
        }
        return list;
    }
    
    public ArrayList<BootHost> getAllSingleBootHost(){
        BootHost one;

        ArrayList<BootHost> list  = new ArrayList<BootHost>();
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            one = (BootHost)hosts.elementAt(i);
            if( !one.isCluster() ){
                list.add( one );
            }
        }
        return list;
    }

    public Vector getBootHostOnType( int mode ){
        BootHost host;
        
        Vector<BootHost> list = new Vector<BootHost>();
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            host = (BootHost)hosts.elementAt(i);
            if( mode == 0 ){
                if( !host.isInited() ){
                    list.addElement( host );
                }
            }else{
                if( host.isInited() ){
                    list.addElement( host );
                }
            }
        }
        
        return list;
    }
    
    public int getBootHostNum(){
        return hosts.size();
    }
    
    public BootHost getBootHostOnUUID( String uuid ){
        BootHost host;
        
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            host = hosts.elementAt(i);
            if( host.getUUID().equals(uuid) ){
                return host;
            }
        }
        
        return null;
    }

    public BootHost getBootHostOnUUIDAndIP( String uuid, String ip ){
        BootHost host;

        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            host = hosts.elementAt(i);
            if( host.getUUID().equals(uuid) && host.getIP().equals( ip ) ){
                return host;
            }
        }

        return null;
    }

    public BootHost getBootHostOnUUIDAndIPForRac( String uuid, String ip ){
        BootHost host;
        
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            host = hosts.elementAt(i);
            if( host.getUUID().equals( uuid ) ){
                if( host.getIP().equals( ip ) || host.isContainedInVip( ip ) ){
                    return host;
                }
            }
        }

        return null;
    }

    public ArrayList getMemberForCluster( int cluster_id ){
        ArrayList<BootHost> ret = new ArrayList<BootHost>();
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            BootHost host = hosts.get(i);
            if( host.getClnt_cluster_id() == cluster_id ){
                ret.add( host );
            }
        }
        return ret;
    }
}
