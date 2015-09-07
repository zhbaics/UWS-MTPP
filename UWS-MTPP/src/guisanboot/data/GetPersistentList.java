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

public class GetPersistentList extends NetworkRunning {
    private ArrayList<Integer> cache = new ArrayList<Integer>();

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
        int indx = line.indexOf(":");
        if( indx >=0 ){
            try{
                int tid = Integer.parseInt( line.substring(indx+1) );
                cache.add( new Integer( tid ) );
            }catch(Exception ex){}
        }
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line );
        }else{
            this.errMsg += line +"\n";
        }
    }

    public GetPersistentList( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetPersistentList( String cmd ){
        super( cmd );
    }

    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " get persistent list cmd: "+ getCmdLine() );         
        try{
            cache.clear();
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get persistent list retcode: "+ getRetCode() ); 
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get persistent list errmsg: "+ getErrMsg() );             
        }
        return isOk;
    }
    
    public ArrayList<Integer> getAllPersistent(){
        ArrayList<Integer> ret = new ArrayList<Integer>();
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            ret.add( cache.get(i) );
        }
        return ret;
    }
}
