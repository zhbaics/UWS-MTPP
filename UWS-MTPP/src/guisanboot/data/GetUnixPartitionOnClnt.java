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

public class GetUnixPartitionOnClnt extends NetworkRunning {
    private SystemPartitionForUnix curPart = null;
    private Vector<SystemPartitionForUnix> cache = new Vector<SystemPartitionForUnix>();
    
    public void parser(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        
        try{
            String[] list = Pattern.compile(";").split( line,-1 );
            
            if( list[0].equals("") ){ //必须有 mp
                return;
            }
             
            curPart = new SystemPartitionForUnix();
            curPart.mp = list[0];
            curPart.dev_path = list[1];
            curPart.fsType = list[2];
            curPart.size = list[3];
            curPart.used = list[4];
            
            if( list.length >=6 ){
                curPart.devType = list[5];
            }
            
            if( list.length >=7 ){
                curPart.vender = list[6];
            }
            
            cache.addElement( curPart );
        }catch(Exception ex){
            //ex.printStackTrace();
            curPart = null;
        }
    }

    public GetUnixPartitionOnClnt( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetUnixPartitionOnClnt( String cmd ){
        super( cmd );
    }

    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " get unix partition (confile) cmd: "+ getCmdLine() ); 
        try{
            curPart = null;
            cache.removeAllElements();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get unix partition (confile) retcode: "+ getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get unix partition (confile) errmsg: "+ getErrMsg() );             
        }
        return isOk;
    }
    
    public Vector<SystemPartitionForUnix> getAllPartition(){
        int size = cache.size();
        Vector<SystemPartitionForUnix> ret = new Vector<SystemPartitionForUnix>( size );
        for( int i=0; i<size; i++ ){
            ret.addElement( cache.elementAt(i) );
        }
        return ret;
    }
    
    public ArrayList<String> getFsList(){
        String fs;
        
        int size = cache.size();
        ArrayList<String> ret = new ArrayList<String>( size );
        for( int i=0; i<size; i++ ){
            fs = ((SystemPartitionForUnix)cache.elementAt(i)).mp;
            if( !fs.substring( fs.length()-1, fs.length() ).equals("/") ){
                fs +="/";
            }
            ret.add(  fs );
        }
        return ret;
    }
    
    public SystemPartitionForUnix getSysPartStatistic( String mp ){
        SystemPartitionForUnix part;
        
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            part = cache.elementAt(i);
            if( part.mp.equals( mp ) ){
                return part;
            }
        }
        
        return null;
    }
    
    public String prtMe(){
        boolean isFirst = true;
        String tmp;
        SystemPartitionForUnix part;
        StringBuffer buf = new StringBuffer();
        
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            part = cache.elementAt( i );
            tmp = part.mp+";"+part.dev_path+";"+part.fsType+";"+part.size+
                  ";"+part.used+";"+part.devType+";"+part.vender+";";
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
