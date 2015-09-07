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

public class GetPartitionByCatConf extends NetworkRunning {
    private SystemPartitionForWin curPart = null;
    private Vector<SystemPartitionForWin> cache = new Vector<SystemPartitionForWin>();

    public void parser( String line ){
        if( this.isMTPPCmd() ){
            this.parserMTPP( line );
        }else{
           this.parserCMDP( line );
        }
    }

    public void parserCMDP( String line ){
        if( line == null || line.equals("") ) return;
        line = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ line );
        try{
            String[] list = Pattern.compile(";").split( line,-1 );

            if( list[0].equals("") ){ // ������
                return;
            }

            if( list[1].equals("") ){ // ������
                return;
            }

            if( list[2].equals("") ){ // ������
                return;
            }

            curPart = new SystemPartitionForWin();
            curPart.disklabel = list[0];
            curPart.uuid = list[1];
            curPart.volInfo = list[2];
            curPart.fsType = list[3];
            curPart.size = list[4];
            curPart.layoutcount = list[5];
            curPart.layouts = list[6];

            cache.addElement( curPart );
        }catch(Exception ex){
            curPart = null;
        }
    }

    public void parserMTPP(String line)  {
        if( line == null || line.equals("") ) return;
        line = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ line );          
        try{
            String[] list = Pattern.compile(";").split( line,-1 );
            
            if( list[0].equals("") ){ // ������
                return;
            }
            
            if( list[3].equals("") ){ // ������
                return;
            }
            
            if( list[7].equals("") ){ // ������
                return;
            }
            
            curPart = new SystemPartitionForWin();
            curPart.disklabel = list[0];
            curPart.isActive = list[1];
            curPart.label = list[2];
            curPart.volInfo = list[3];
            curPart.fsType = list[4];
            curPart.size = list[5];
            curPart.available = list[6];
            curPart.vender = list[7];
            curPart.iscsiVar = list[8];
                        
            cache.addElement( curPart );
        }catch(Exception ex){
            curPart = null;
        }
    }

    public GetPartitionByCatConf( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetPartitionByCatConf( String cmd ){
        super( cmd );
    }

    public boolean realDo(){ 
SanBootView.log.info( getClass().getName(), " get partition (confile) cmd: "+ getCmdLine() );         
        try{
            curPart = null;
            cache.removeAllElements();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get partition (confile) retcode: "+ getRetCode() );     
        boolean isOk = ( this.getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get partition (confile) errmsg: "+ getErrMsg() );              
        }
        return isOk;
    }
    
    public Vector<SystemPartitionForWin> getAllPartition(){
        int size = cache.size();
        Vector<SystemPartitionForWin> ret = new Vector<SystemPartitionForWin>( size );
        for( int i=0; i<size; i++ ){
            ret.addElement( cache.elementAt(i) );
        }
        return ret;
    }

    public SystemPartitionForWin getSysPartStatistic( String driver ){
        SystemPartitionForWin part;
        
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            part = cache.elementAt(i);
            if( part.getDiskLabel().toUpperCase().equals( driver.toUpperCase() ) ){
                return part;
            }
        }
        
        return null;
    }

    public String prtMe(){
        if( this.isCMDPCmd() ){
            return prtMeForMTPP();
        }else{
            return prtMeForCMDP();
        }
    }

    public String prtMeForCMDP(){
        boolean isFirst = true;
        String tmp;
        SystemPartitionForWin part;
        StringBuffer buf = new StringBuffer();

        int size = cache.size();
        for( int i=0; i<size; i++ ){
            part = cache.elementAt( i );
            tmp = part.getDiskLabel()+";"+part.uuid+";"+part.volInfo+";"+part.fsType+
                  ";"+part.size+";"+part.layoutcount+";"+part.layouts;
            if( isFirst ){
                buf.append( tmp );
                isFirst = false;
            }else{
                buf.append( "\n" + tmp );
            }
        }

        return buf.toString();
    }

    public String prtMeForMTPP(){
        boolean isFirst = true;
        String tmp;
        SystemPartitionForWin part;
        StringBuffer buf = new StringBuffer();
        
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            part = cache.elementAt( i );
            tmp = part.getDiskLabel()+";"+part.isActive+";"+part.label+";"+part.getVolInfo()+";"+part.fsType+
                  ";"+part.size+";"+part.available+";"+part.vender+";"+part.iscsiVar;
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
