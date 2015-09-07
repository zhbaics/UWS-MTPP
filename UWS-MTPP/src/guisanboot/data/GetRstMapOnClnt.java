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

public class GetRstMapOnClnt extends NetworkRunning {
    private RestoreMapper curMapper = null;
    private HashMap<String,RestoreMapper> cache = new HashMap<String,RestoreMapper>();
    private Vector<RestoreMapper> cache1 = new Vector<RestoreMapper>();
    
    public void parser(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        try{
            String[] list = Pattern.compile(";").split( line,-1 );
            
            if( list.length < 3 ){
                return;
            }
            
            curMapper = new RestoreMapper();
            curMapper.setSrc( list[0] );
            DestDevice dest = new DestDevice( list[1],list[2] );
            curMapper.setDest( dest );
            
            cache.put( list[0],curMapper );
            cache1.addElement( curMapper );
        }catch( Exception ex ){
            curMapper = null;
        }
    }

    public GetRstMapOnClnt( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetRstMapOnClnt( String cmd ){
        super( cmd );
    }

    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " get rstmap cmd: "+ getCmdLine() );         
        try{
            curMapper = null;
            cache.clear();
            cache1.removeAllElements();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get rstmap retcode: "+ getRetCode() );  
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get rstmap errmsg: "+ getErrMsg() );              
        }
        return isOk;
    }
    
    public RestoreMapper getMapper( String src ){
        return cache.get( src );
    }
    
    public HashMap<String,RestoreMapper> getAllMapper(){
        return cache;
    }
    
    public Vector<RestoreMapper> getAllMapper1(){
        int size = cache1.size();
        Vector<RestoreMapper> ret = new Vector<RestoreMapper>( size );
        for( int i=0; i<size; i++ ){
            ret.addElement( cache1.elementAt(i) );
        }
        return ret;
    }
    
    public String prtMe(){
        boolean isFirst = true;
        String tmp;
        RestoreMapper mapper;
        StringBuffer buf = new StringBuffer();
        
        Set<String> set  = cache.keySet();
        Iterator<String> iterator = set.iterator();
        while( iterator.hasNext() ){
            mapper = cache.get( iterator.next() );
            tmp = mapper.getSrc() + ";" + mapper.getDestMp()+";"+mapper.getVolName();
            if( isFirst ){
                buf.append( tmp );
                isFirst = false;
            }else{
                buf.append( "\n" + tmp );
            }
        } 
        return buf.toString();
    }
}
