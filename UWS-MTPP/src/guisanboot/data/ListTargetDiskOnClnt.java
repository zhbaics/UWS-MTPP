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
import guisanboot.ui.*;

public class ListTargetDiskOnClnt extends NetworkRunning {
    private String curTDisk = null;
    private Vector<String> cache = new Vector<String>();

    public void parser(String line ){
        if( this.isMTPPCmd() ){
            parserForMTPP( line );
        }else{
            parserForCMDP( line );
        }
    }

    public void parserForMTPP(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );      
        
        curTDisk = line.trim();
        cache.addElement( curTDisk );
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line );
        }else{
            this.errMsg += line +"\n";
        }
    }

    public ListTargetDiskOnClnt( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public ListTargetDiskOnClnt( String cmd ){
        super( cmd );
    }

    public boolean realDo(){ 
SanBootView.log.info( getClass().getName(), " list target disk on client cmd: "+ getCmdLine() ); 
        try{
            curTDisk = null;
            cache.removeAllElements();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " list target disk on client retcode: "+ getRetCode() );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " list target disk on client errmsg: "+ getErrMsg() );            
        }
        return isOk;
    }
    
    public Vector<String> getAllTargetDisk(){
        Vector<String> ret = new Vector<String>();
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            ret.addElement( cache.elementAt(i) );
        }
        return ret;
    }
    
    public boolean hasThisTarget( String tdisk ){
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            String disk = cache.elementAt(i);
            if( disk.equals( tdisk ) ){
                return true;
            }
        }
        
        return false;
    }
}
