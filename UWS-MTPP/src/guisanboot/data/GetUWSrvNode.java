/*
 * GetUWSrvNode.java
 *
 * Created on July 8, 2005, 8:15 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import java.net.*;
import java.io.*;

/**
 *
 * @author  Administrator
 */
public class GetUWSrvNode extends NetworkRunning {
    private ArrayList<UWSrvNode> hosts = new ArrayList<UWSrvNode>();
    private UWSrvNode curSrv = null;
    
    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 ); 

        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value ); 
            
            if( s1.startsWith( UWSrvNode.UWS_uws_id) ){
                try{
                    int id = Integer.parseInt( value );
                    curSrv.setUws_id( id );
                }catch(Exception ex){
                    curSrv.setUws_id( -1 );
                }
            }else if( s1.startsWith( UWSrvNode.UWS_uws_ip )){
                curSrv.setUws_ip( value );
            }else if( s1.startsWith( UWSrvNode.UWS_uws_port) ){
                try{
                    int port = Integer.parseInt( value );
                    curSrv.setUws_port( port );
                }catch(Exception ex){
                    curSrv.setUws_port( UWSrvNode.MEDIA_SERVER_PORT );
                }
            }else if(s1.startsWith( UWSrvNode.UWS_uws_psn )){
                curSrv.setUws_psn( value );
                hosts.add( curSrv );
            }
        }else{
            if( s1.startsWith( UWSrvNode.UWS_RECFLAG )){
                curSrv = new UWSrvNode();
            }
        }
    }
    
    public GetUWSrvNode(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetUWSrvNode(String cmd){
        super( cmd );
    }

    public boolean updateUWSrvNode() {
SanBootView.log.info( getClass().getName(), " get SWU server cmd: " + getCmdLine()  );        
        try{
            hosts.clear();
            curSrv = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get SWU server retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get SWU server errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
    
    public void addUWSrvToVector( UWSrvNode srv ){
        hosts.add( srv );
    }
    
    public void removeUWSrvFromVector( UWSrvNode srv ){
        hosts.remove( srv );
    }
    
    public UWSrvNode getUWSrvFromVectorOnID( int id ){
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            UWSrvNode srv = hosts.get(i);
            if( srv.getUws_id() == id )
                return srv;
        }
        return null;
    }
    
    public UWSrvNode getUWSrvFromVecOnIP( String ip ){
        int size = hosts.size();
        for( int i=0;i<size;i++ ){
            UWSrvNode srv = hosts.get( i );
SanBootView.log.debug(getClass().getName(),"##### swu server ip: " + srv.getUws_ip() );
            if( srv.getUws_ip().equals( ip ) )
                return srv;
        }
        return null;
    }
    
    public UWSrvNode isExistThisUWSrv( String psn ){
        int size = hosts.size();
        for( int i=0;i<size;i++){
            UWSrvNode srv = hosts.get(i);        
            if( srv.getUws_psn().equals( psn ) ){
                return srv;
            }
        }
        return null;
    }
    
    public UWSrvNode isExistThisUWSrv( String psn,String ip ){
        int size = hosts.size();
        for( int i=0;i<size;i++){
            UWSrvNode srv = hosts.get(i);        
            if( srv.getUws_psn().equals( psn ) &&
                srv.getUws_ip().equals( ip ) 
            ){
                return srv;
            }
        }
        return null;
    }
    
    public ArrayList<UWSrvNode> getAllUWSrv(){
        int size = hosts.size();
        ArrayList<UWSrvNode> list  = new ArrayList<UWSrvNode>( size );
        for( int i=0; i<size; i++ ){
            list.add( hosts.get(i) );
        }
        return list;
    }
    
    public int getUWSrvNum(){
        return hosts.size();
    }
}
