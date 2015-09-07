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
import guisanboot.res.ResourceCenter;
import java.util.regex.Pattern;

public class GetServiceOnClnt extends NetworkRunning {
    private Service curServ = null;
    private Vector<Service> cache = new Vector<Service>();
    private int id = 0;

    public void parser( String line ){
        if( this.isMTPPCmd() ){
            this.parserMTPP( line );
        }else{
            this.parserCMDP( line );
        }
    }

    // name=MSSQL$DEMO;disp=SQL Server (DEMO);path="C:\Program Files\Microsoft SQL Server\MSSQL.1\MSSQL\Binn\sqlservr.exe" -sDEMO;
    public void parserCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;
        //if( !this.isContinueToParserRetValueForCMDP1(line) ) return;

        if( !this.isEqZero() ){
            this.errMsg += line +"\n";
        }else{
            String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 );

            String[] lines = Pattern.compile(";").split( s1 );
            if( lines.length >= 3  ){
                curServ = new Service();

                int index = lines[0].indexOf("=");
                if( index >= 0 ){
                    curServ.setServName( lines[0].substring( index+1 ) );
                }

                index = lines[1].indexOf("=");
                if( index >= 0 ){
                    curServ.setServDesc( lines[1].substring( index+1 ) );
                }

                index = lines[2].indexOf("=");
                if( index >= 0 ){
                    curServ.setServPath( lines[2].substring(index+1) );
                }
                cache.addElement( curServ );
            }
        }
    }

    public void parserMTPP(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );          
        if( line.equals("NoneService") ) return; // �˵�NoneService
        
        String s1 = line.trim();
        int idx = s1.indexOf(" ");
        if( idx >=0 ){
            String name = s1.substring( 0,idx );
            String desc = s1.substring( idx+1 );
            id++;

            curServ = new Service(
                id,
                name,
                desc
            );
            cache.addElement( curServ );
        }
    }

    public GetServiceOnClnt( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }

    public GetServiceOnClnt( String cmd,String encoding ){
        super( cmd,encoding );
    }

    public GetServiceOnClnt( String cmd ){
        super( cmd );
    }

    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " get service (confile) cmd: "+ getCmdLine() ); 
        try{
            id = 0;
            curServ = null;
            cache.removeAllElements();
            
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get service (confile) retcode: "+ getRetCode() ); 
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get service (confile) errmsg: "+ getErrMsg() );             
        }
        return isOk;
    }

    public Vector<Service> getAllServices(){
        Vector<Service> ret1 = new Vector<Service>();
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            ret1.addElement( cache.elementAt(i) );
        }
        return ret1;
    }
    
    public String getAllServiceContents( int mode ){
        boolean isFirst = true;
        
        StringBuffer buf = new StringBuffer();
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            if( isFirst ){
                if( mode == ResourceCenter.CMD_TYPE_MTPP ){
                    buf.append( cache.elementAt(i).prtMe() );
                }else{
                    buf.append("*isEnd=1 *return=0");
                    buf.append("\n" + cache.elementAt(i).prtMeByCMDP() );
                }
                isFirst = false;
            }else{
                if( mode == ResourceCenter.CMD_TYPE_MTPP ){
                    buf.append("\n"+ cache.elementAt(i).prtMe() );
                }else{
                    buf.append("\n"+cache.elementAt(i).prtMeByCMDP() );
                }
            }
        }
        
        String str = buf.toString();
        if( str.equals("") ){
            buf.append("NoneService");
            return buf.toString();
        }else{
            return str;
        }
    }
}
