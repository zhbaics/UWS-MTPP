package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import guisanboot.ui.*;

public class GetClientFromDHCP extends NetworkRunning {
    private DhcpClientInfo curClnt = null;
    private Vector<DhcpClientInfo> cache = new Vector<DhcpClientInfo>();
    
    public void parser(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"======>" + line ); 
        
        try{
            String[] list = Pattern.compile("\\s+").split( line, -1 );
            curClnt = new DhcpClientInfo();
            curClnt.ip = list[0];
            curClnt.mac = list[1];
            curClnt.subnet = list[2];
            curClnt.nextServer = list[3];
            
            if( list.length >=5 ){
                if( list[4]!=null && !list[4].equals("") ){
                    curClnt.dns = list[4];
                }
            }
            
            if( list.length >=6 ){
                if( list[5]!=null && !list[5].equals("") ){
                    curClnt.defgw = list[5];
                }
            }
            
            if( list.length >=7 ){
                if( list[6]!=null && !list[6].equals("") ){
                    curClnt.ostype = list[6];
                }
            }

            if( list.length >=8 ){
                if( list[7]!=null && !list[7].equals("") ){
                    curClnt.boottype = list[7];
                }
            }
            
            cache.addElement( curClnt );
        }catch(Exception ex){
            curClnt = null;
        }
    }

    public GetClientFromDHCP( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetClientFromDHCP( String cmd ){
        super( cmd );
    }
    
    public GetClientFromDHCP(){
    }
    
    public void clearCache(){
        cache.removeAllElements();
    }
    
    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " getClntFromDhcp cmd: "+ getCmdLine()  ); 
        try{
            curClnt = null;
            cache.removeAllElements();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " getClntFromDhcp retcode: "+ getRetCode()  ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " getClntFromDhcp errmsg: "+ getErrMsg()  );             
        }
        return isOk;
    }
    
    public Vector<DhcpClientInfo> getAllClnt(){
        Vector<DhcpClientInfo> ret = new Vector<DhcpClientInfo>();
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            ret.addElement( cache.elementAt(i) );
        }
        
        return ret;
    }
    
    public DhcpClientInfo getSelClntOnMac( String mac ){
        int i,size;
        
        mac = mac.toUpperCase();
        size = cache.size();
SanBootView.log.debug(getClass().getName()," size: "+size + " mac: "+mac );

        for( i=0; i<size; i++ ){
            DhcpClientInfo clnt = cache.elementAt(i);        
            if( clnt.mac.toUpperCase().equals( mac ) ){
SanBootView.log.debug(getClass().getName()," find mac in dhcpinfo list "); 
SanBootView.log.debug(getClass().getName()," dns: "+clnt.getDNS() +" defgw: "+clnt.getDefGw() +" os: "+clnt.getOSType());
                return clnt;
            }
        }
        
        return null;
    }
    
    public void addClnt( DhcpClientInfo clnt ){
        cache.addElement( clnt );
    }
    public void removeClnt( DhcpClientInfo clnt ){
        cache.removeElement( clnt );
    }
}
