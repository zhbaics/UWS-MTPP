/*
 * GetSrcAgnt.java
 *
 * Created on July 17, 2008, 8:15 PM
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetSrcAgnt extends NetworkRunning {
    private ArrayList<SourceAgent> hosts = new ArrayList<SourceAgent>();
    private SourceAgent curHost = null;
    
    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 ); 

        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value ); 

            if( s1.startsWith( SourceAgent.SRCAGNT_ID ) ){
                try{
                    int id = Integer.parseInt( value );
                    curHost.setSrc_agnt_id( id );
                }catch(Exception ex){
                    curHost.setSrc_agnt_id( -1 );
                }
                hosts.add( curHost ); 
            }else if( s1.startsWith( SourceAgent.SRCAGNT_IP )){
                curHost.setSrc_agnt_ip( value );
            }else if( s1.startsWith( SourceAgent.SRCAGNT_OS ) ){
                curHost.setSrc_agnt_os( value );
            }else if( s1.startsWith( SourceAgent.SRCAGNT_MACHINE )){
                curHost.setMachine( value );
            }else if( s1.startsWith( SourceAgent.SRCAGNT_DESC ) ){
                curHost.setSrc_agnt_desc( value );
            }else if(s1.startsWith( SourceAgent.SRCAGNT_UWS_ID )){
                try{
                    int uws_id = Integer.parseInt( value );
                    curHost.setSrc_agnt_uws_id( uws_id );
                }catch(Exception ex){
                    curHost.setSrc_agnt_uws_id( -1 );
                }
            }else if( s1.startsWith( SourceAgent.SRCAGNT_PORT1 ) ){
                try{
                    int port1 = Integer.parseInt( value );
                    curHost.setSrc_agnt_port1( port1 );
                }catch(Exception ex){
                    //curHost.setSrc_agnt_port1( ResourceCenter.MTPP_AGENT_PORT );
                }
            }else if( s1.startsWith(SourceAgent.SRCAGNT_PORT) ){
                try{
                    int port = Integer.parseInt( value );
                    curHost.setSrc_agnt_port( port );
                }catch(Exception ex){
                    //curHost.setSrc_agnt_port( ResourceCenter.MTPP_AGENT_PORT );
                }
            }else if( s1.startsWith(SourceAgent.SRCAGNT_PSN ) ){
                curHost.setSrc_agnt_psn( value );
            }else if( s1.startsWith(SourceAgent.SRCAGNT_BOOT_MODE ) ){
                try{
                    int mode = Integer.parseInt( value );
                    curHost.setSrc_agnt_boot_mode( mode );
                }catch(Exception ex){
                    curHost.setSrc_agnt_boot_mode( 0 );
                }            
            }else if( s1.startsWith( SourceAgent.SRCAGNT_PROTECT_MODE ) ){
                try{
                    int ptype = Integer.parseInt( value );
                    curHost.setProtectType( ptype );
                    if( curHost.isCMDPProtect() ){
                        if( curHost.getSrc_agnt_port() == -1 ){
                            curHost.setSrc_agnt_port( ResourceCenter.CMDP_AGENT_PORT );
                        }
                    }else{
                        if( curHost.getSrc_agnt_port() == -1 ){
                            curHost.setSrc_agnt_port( ResourceCenter.MTPP_AGENT_PORT );
                        }
                    }
                }catch(Exception ex){
                    curHost.setProtectType( BootHost.PROTECT_TYPE_MTPP );
                    curHost.setSrc_agnt_port( ResourceCenter.MTPP_AGENT_PORT );
                }
            }else if( s1.startsWith( SourceAgent.SRCAGNT_PRI_IP ) ){
                curHost.setSrc_agnt_pri_ip( value );
            }else if( s1.startsWith( SourceAgent.SRCAGNT_VIP ) ){
                curHost.setSrc_agnt_vip( value );
            }else if( s1.startsWith( SourceAgent.SRCAGNT_CLNT_OPT ) ){
                try{
                    int opt = Integer.parseInt( value );
                    curHost.setClnt_opt( opt );
                }catch(Exception ex){
                    curHost.setClnt_opt( 0 );
                }
            }else if( s1.startsWith( SourceAgent.SRCAGNT_CLUSTER_ID ) ){
                try{
                    int cluster_id = Integer.parseInt( value );
                    curHost.setClnt_cluster_id( cluster_id );
                }catch(Exception ex){
                    curHost.setClnt_cluster_id( 0 );
                }
            }
        }else{
            if( s1.startsWith( SourceAgent.SRCAGNT_FLAG )){
                curHost = new SourceAgent();
            }
        }
    }
    
    public GetSrcAgnt(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetSrcAgnt(String cmd){
        super( cmd );
    }
    
    public boolean updateSrcAgent() {
SanBootView.log.info( getClass().getName(), " get src_agent cmd: " + getCmdLine()  );        
        try{
            hosts.clear();
            curHost = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get src_agent retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get src_agent errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
    
    public void addSrcAgentToVector( SourceAgent host ){
        hosts.add( host );
    }
    
    public void removeSrcAgentFromVector( SourceAgent host ){
        hosts.remove( host );
    }
    
    public SourceAgent getSrcAgntFromVectorOnID( int id ){
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            SourceAgent host = hosts.get(i);
            if( host.getSrc_agnt_id() == id )
                return host;
        }
        return null;
    }
    
    public ArrayList<SourceAgent> getSrcAgntFromVecOnUWSID( int uws_id ){
        int size = hosts.size();
        ArrayList<SourceAgent> ret = new ArrayList<SourceAgent>( size );
        for( int i=0;i<size;i++){
            SourceAgent agt = hosts.get(i);         
            if( agt.getSrc_agnt_uws_id() == uws_id ){
                ret.add( agt );
            }
        }
        return ret;
    }
    
    public ArrayList<SourceAgent> getSrcAgntForRealAgtOnUWSrvID( int src_uws_id ){
        SourceAgent srcAgnt;
        
        int size = hosts.size();
        ArrayList<SourceAgent> ret = new ArrayList<SourceAgent>(size);
        for( int i=0; i<size; i++ ){
            srcAgnt = hosts.get(i);
            if( !srcAgnt.getSrc_agnt_desc().equals("") ){
                if( srcAgnt.getSrc_agnt_uws_id() == src_uws_id ){
                    ret.add( hosts.get(i) );
                }
            }
        }
        return ret;
    }
    
    public ArrayList<SourceAgent> getSrcAgntForRealAgent(){
        SourceAgent srcAgnt;
        
        int size = hosts.size();
        ArrayList<SourceAgent> ret = new ArrayList<SourceAgent>(size);
        for( int i=0; i<size; i++ ){
            srcAgnt = hosts.get(i);
            if( !srcAgnt.getSrc_agnt_desc().equals("") ){
                ret.add( hosts.get(i) );
            }
        }
        return ret;
    }
    
    // 应该只有一个，这个SourceAgent是代表某个源端UWS服务器的
    public SourceAgent getSrcAgntForSrcUWSOnUWSrvID( int src_uws_id ){
        SourceAgent srcAgnt;
        
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            srcAgnt = hosts.get(i);
            if( srcAgnt.getSrc_agnt_desc().equals("") ){
                if( srcAgnt.getSrc_agnt_uws_id() == src_uws_id ){
                     return srcAgnt;
                }
            }
        }
        return null;
    }

    public SourceAgent getSrcAgntForSrcUWSOnPSN( String psn ){
        SourceAgent srcAgnt;

        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            srcAgnt = hosts.get(i);
            if( srcAgnt.getSrc_agnt_desc().equals("") && srcAgnt.getSrc_agnt_os().equals( psn) ){
                return srcAgnt;
            }
        }
        return null;
    }

    public ArrayList<SourceAgent> getSrcAgntForSrcUWS(){
        SourceAgent srcAgnt;
        
        int size = hosts.size();
        ArrayList<SourceAgent> ret = new ArrayList<SourceAgent>(size);
        for( int i=0; i<size; i++ ){
            srcAgnt = hosts.get(i);
            if( srcAgnt.getSrc_agnt_desc().equals("") ){
                ret.add( hosts.get(i) );
            }
        }
        return ret;
    }
    
    public SourceAgent isExistSrcAgnt( String uuid ){
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            SourceAgent srcAgnt = (SourceAgent)hosts.get(i);
            if( srcAgnt.getSrc_agnt_desc().equals(uuid) ){
                return srcAgnt;
            }
        }
        
        return null;
    }
    
    public SourceAgent isExistSrcAgnt( String uuid,int src_uws_id ){
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            SourceAgent srcAgnt = (SourceAgent)hosts.get(i);
            if( srcAgnt.getSrc_agnt_desc().equals( uuid ) &&
                srcAgnt.getSrc_agnt_uws_id() == src_uws_id    
            ){
                return srcAgnt;
            }
        }
        
        return null;
    }
    
    // 代表源端UWS服务器的srcagent
    public SourceAgent isExistUWSSrcAgnt( String psn ){
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            SourceAgent srcAgnt = (SourceAgent)hosts.get(i);
            if( srcAgnt.getSrc_agnt_os().equals( psn ) ){
                return srcAgnt;
            }
        }
        
        return null;
    }
    
    // 代表源端UWS服务器的srcagent
    public SourceAgent isExistUWSSrcAgnt1( String ip ){
        int size = hosts.size();
        for( int i=0; i<size; i++ ){
            SourceAgent srcAgnt = (SourceAgent)hosts.get(i);
            if( srcAgnt.getSrc_agnt_ip().equals( ip ) ){
                return srcAgnt;
            }
        }
        
        return null;
    }
    
    public ArrayList<SourceAgent> getAllSrcAgnt(){
        int size = hosts.size();
        ArrayList<SourceAgent> ret = new ArrayList<SourceAgent>(size);
        for( int i=0; i<size; i++ ){
            ret.add( hosts.get(i) );
        }
        return ret;
    }
    
    public ArrayList<SourceAgent> getAllNormalSrcAgnt(){
        SourceAgent sa;
        
        int size = hosts.size();
        ArrayList<SourceAgent> ret = new ArrayList<SourceAgent>(size);
        for( int i=0; i<size; i++ ){
            sa = hosts.get(i);
            if( sa.isNormalHost() ){
                ret.add( sa );
            }
        }
        return ret;
    }
    
    public ArrayList<SourceAgent> getAllRollbackSrcAgnt(){
        SourceAgent sa;
        
        int size = hosts.size();
        ArrayList<SourceAgent> ret = new ArrayList<SourceAgent>(size);
        for( int i=0; i<size; i++ ){
            sa = hosts.get(i);
            if( sa.isRollbackedHost() ){
                ret.add( sa );
            }
        }
        return ret;
    }
    
    public int getSrcAgntNum(){
        return hosts.size();
    }
}
