/*
 * GetNetBootedHost.java
 *
 * Created on July 30, 2008, 8:30 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetNetBootedHost extends NetworkRunning {
    private ArrayList<DestAgent> dstAgentList = new ArrayList<DestAgent>();
    private DestAgent curDstAgnt = null;
    
    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 ); 

        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value ); 
            
            if( s1.startsWith( DestAgent.DST_AGNT_ID) ){
                try{
                    int id = Integer.parseInt( value );
                    curDstAgnt.setDst_agent_id( id );
                }catch(Exception ex){
                    curDstAgnt.setDst_agent_id( -1 );
                }
                dstAgentList.add( curDstAgnt );
            }else if( s1.startsWith( DestAgent.DST_AGNT_IP )){
                curDstAgnt.setDst_agent_ip( value );
            }else if( s1.startsWith( DestAgent.DST_AGNT_PORT ) ){
                try{
                    int port = Integer.parseInt( value );
                    curDstAgnt.setDst_agent_port( port );
                }catch(Exception ex){
                    curDstAgnt.setDst_agent_port( -1 );
                }
            }else if( s1.startsWith( DestAgent.DST_AGNT_OSTYPE ) ){
                curDstAgnt.setDst_agent_ostype( value );
            }else if(s1.startsWith( DestAgent.DST_AGNT_MAC )){
                curDstAgnt.setDst_agent_mac( value );
            }else if(s1.startsWith( DestAgent.DST_AGNT_DESC ) ){
                curDstAgnt.setDst_agent_desc( value );  
            }else if( s1.startsWith( DestAgent.DST_PROTECT_TYPE ) ){
                try{
                    int ptype = Integer.parseInt( value );
                    curDstAgnt.setProtectType( ptype );
                }catch(Exception ex){
                    curDstAgnt.setProtectType( 0 );
                }
            }
        }else{
            if( s1.startsWith( DestAgent.DST_REC_FLAG )){
                curDstAgnt = new DestAgent();
            }
        }
    }
    
    public GetNetBootedHost(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetNetBootedHost(String cmd){
        super( cmd );
    }

    public boolean updateDestAgent() {
SanBootView.log.info( getClass().getName(), " get netbooted-host cmd: " + getCmdLine()  );        
        try{
            dstAgentList.clear();
            curDstAgnt = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get netbooted-host retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get netbooted-host errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
  
    public void addDestAgntToVector( DestAgent dstAgnt ){
        dstAgentList.add( dstAgnt );
    }
    
    public void removeDestAgntFromVector( DestAgent dstAgnt ){
        dstAgentList.remove( dstAgnt );
    }
    
    public DestAgent getDAFromCacheOnDestAgntID( int dstAgntID ){
        int size = dstAgentList.size();
        for( int i=0; i<size; i++ ){
            DestAgent dstAgnt = dstAgentList.get(i);
            if( dstAgnt.getDst_agent_id() == dstAgntID ){
                return dstAgnt;
            }
        }
        return null;
    }
    
    public ArrayList<DestAgent> getAllDAs(){
        int size = dstAgentList.size();
        ArrayList<DestAgent> ret = new ArrayList<DestAgent>(size);
        for( int i=0; i<size; i++ ){
            ret.add( dstAgentList.get(i) );
        }
        return ret;
    }
    
    public int getDANum(){
        return dstAgentList.size();
    }
}
