package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */
import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class GetSubnetFromDHCP extends NetworkRunning {
    private SubNetInDHCPConf curSubnet = null;
    private Vector<SubNetInDHCPConf> cache = new Vector<SubNetInDHCPConf>();
    
    public void parser(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        
        try{
            String[] list = Pattern.compile("\\s+").split( line );
            curSubnet = new SubNetInDHCPConf();
            curSubnet.subnet = list[0];
            curSubnet.netmask =list[1];
            curSubnet.start = list[2];
            curSubnet.end = list[3];
            int lease_time = -1;
            try{
                lease_time = Integer.parseInt( list[4] );
                curSubnet.def_lease_time = lease_time;
            }catch(Exception ex){}
            
            curSubnet.allUnknonClnt =( list[5].equals("1") );
            cache.addElement( curSubnet );

            int len = list.length;
            for(int i=6; i<len; i=i+2){
                SubNetInDHCPConf moreSubnet = new SubNetInDHCPConf();
                moreSubnet.subnet = list[0];
                moreSubnet.netmask =list[1];
                moreSubnet.start = list[i];
                moreSubnet.end = list[i+1];
                moreSubnet.def_lease_time = lease_time;
                moreSubnet.allUnknonClnt =( list[5].equals("1") );
                cache.addElement( moreSubnet );
            }

        }catch(Exception ex){
            curSubnet = null;
        }
    }

    public GetSubnetFromDHCP( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetSubnetFromDHCP( String cmd ){
        super( cmd );
    }
    
    public GetSubnetFromDHCP(){
    }
    
    public void clearCache(){
        cache.removeAllElements();
    }
    
    public boolean realDo(){
SanBootView.log.info( getClass().getName(), "getSubnet from dhcp cmd: "+this.getCmdLine()  );        
        try{
            curSubnet = null;
            cache.removeAllElements();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), "getSubnet from dhcp retcode: "+ getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), "getSubnet from dhcp errmsg: "+ getErrMsg() );             
        }
        return isOk;
    }
    
    public void addSubnet( SubNetInDHCPConf conf ){
        cache.addElement( conf );
    }
    
    public void delSubnet( SubNetInDHCPConf conf ){
        cache.removeElement( conf );
    }
    
    public Vector<SubNetInDHCPConf> getAllSubnet(){
        Vector<SubNetInDHCPConf> ret = new Vector<SubNetInDHCPConf>();
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            ret.addElement( cache.elementAt(i) );
        }
        
        return ret;
    }
}
