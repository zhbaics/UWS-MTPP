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
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GetServerNetInterface extends NetworkRunning {
    private ArrayList<IFConfObj> objs = new ArrayList<IFConfObj>();
    private IFConfObj curIf = null;
    
    public void parser( String line ){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1);
        
        if( s1.startsWith("#")  ){
            // do nothing
        }else{
            try{
                String[] list = Pattern.compile("\\s+").split( s1,-1 );
                curIf = new IFConfObj();
                
                // ifconfig eth0 20.20.1.144 netmask 255.255.255.0 broadcast 20.20.1.255 up
                curIf.setIfname( list[1] );
                curIf.setIp( list[2] );
                curIf.setMask( list[4] );
                curIf.setBroadcast( list[6]);
                curIf.setStatus( list[7] );
                
                objs.add( curIf );
SanBootView.log.debug(getClass().getName(),curIf.prtMe() );
            }catch(Exception ex){
SanBootView.log.debug(getClass().getName(), ex.getMessage() );
            }
        }
    }
    
    public GetServerNetInterface(String cmd,Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetServerNetInterface( String cmd ){
        super( cmd );
    }

    public boolean updateIflist() {
SanBootView.log.info( getClass().getName()," get ifconfig cmd: "+this.getCmdLine());
        try{
            objs.clear();
            curIf = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," get ifconfig cmd retcode: "+getRetCode()  );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," get ifconfig cmd errmsg: "+getErrMsg()  );            
        }
        return isOk;
    }
  
    public void addToCache(IFConfObj obj){
        objs.add( obj );
    }

    public void removeFromCache(IFConfObj obj){
        objs.remove( obj );
    }
    
    public IFConfObj getFirstFromCache(){
        int size = objs.size();
        if( size > 0 ){
            return objs.get(0);
        }else{
            return null;
        }
    }
    
    public IFConfObj getFromVectorOnIfName( String ifname ){
        int size = objs.size();
        for( int i=0;i<size;i++ ){
            IFConfObj obj = objs.get(i);
            if( obj.getIfname().equals( ifname ) )
                return obj;
        }
        
        return null;
    }
    
    public ArrayList getAll(){
        int size = objs.size();
        ArrayList<IFConfObj> ret  = new ArrayList<IFConfObj>( size );
        for( int i=0;i<size;i++ ){
            ret.add( objs.get(i) );
        }
        return ret;
    }
    
    public void clearCache(){
        objs.clear();
    }
}
