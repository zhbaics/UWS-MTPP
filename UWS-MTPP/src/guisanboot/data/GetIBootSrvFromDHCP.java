package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2008
 * Company:      odysys
 * @author
 * @version 1.0
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import guisanboot.ui.*;
import mylib.tool.Check;

public class GetIBootSrvFromDHCP extends NetworkRunning {
    private DhcpIBootSrv curIbootSrv = null;
    
    // 有时陈连武的listnextserver会输出相同ip的nextserver，所以用hashmap来屏蔽掉相同的ip
    private HashMap<String,DhcpIBootSrv> cache = new HashMap<String,DhcpIBootSrv>();
    
    public void parser(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );             
        try{
            String[] list = Pattern.compile("\\s+").split( line );
            curIbootSrv = new DhcpIBootSrv();
            curIbootSrv.setIP( list[0] );
            curIbootSrv.setDefServer( list[1] );
	    if( Check.ipCheck( list[0] ) ){
                cache.put(list[0], curIbootSrv);
            }
        }catch(Exception ex){
            curIbootSrv = null;
        }
    }

    public GetIBootSrvFromDHCP( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetIBootSrvFromDHCP( String cmd ){
        super( cmd );
    }
    
    public GetIBootSrvFromDHCP(){
    }
    
    public void clearCache(){
        cache.clear();
    }
    
    public boolean realDo(){
SanBootView.log.info( getClass().getName(), "getIBootSrvFromDHCP cmd: "+ getCmdLine() );     
        try{
            curIbootSrv = null;
            cache.clear();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), "getIBootSrvFromDHCP retcode: "+ getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), "getIBootSrvFromDHCP errmsg: "+ getErrMsg() );            
        }
        return isOk;
    }
    
    public void addIbootSrv( DhcpIBootSrv val ){
        cache.put( val.getIP(), val );
    }
    public void delIbootSrv( DhcpIBootSrv val ){
        cache.remove( val.getIP() );
    }
    public void clearDefFlag(){
        Iterator iterator = cache.keySet().iterator();
        while( iterator.hasNext() ){
            DhcpIBootSrv ibootSrv = cache.get( (String)iterator.next() );
            ibootSrv.defServer = "0";
        }
    }
    public boolean hasDefIbootSrv( String besideIP ){
        Iterator iterator = cache.keySet().iterator();
        while( iterator.hasNext() ){
            DhcpIBootSrv ibootSrv = cache.get( (String)iterator.next() );
            if( ibootSrv.ip.equals( besideIP ) ){
                continue;
            }

            if( ibootSrv.isDefaultServer() ){
                return true;
            }
        }

        return false;
    }
    
    public Vector<DhcpIBootSrv> getAllIBootServer(){
        Vector<DhcpIBootSrv> ret = new Vector<DhcpIBootSrv>();

        Iterator iterator = cache.keySet().iterator();
        while( iterator.hasNext() ){
            DhcpIBootSrv ibootSrv = cache.get( (String)iterator.next() );
            ret.addElement( ibootSrv );
        }

        return ret;
    }
}
