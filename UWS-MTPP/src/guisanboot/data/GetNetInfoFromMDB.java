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
import java.util.regex.*;
import guisanboot.ui.*;

public class GetNetInfoFromMDB extends NetworkRunning {
    private String mac="";
    private boolean founed = false;
    private boolean skip = false;
    
    public void parser(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );      
        String tmp = line.trim();
        if( tmp.startsWith("Adapter") ){
            if( !skip ){
                int idx = tmp.indexOf("(*)");
                if( idx >=0 ){
                    founed = true;
                }
            }else{
                founed = true;
            }
        }else if( tmp.startsWith("Mac") ){
            if( founed ){
                String[] list = Pattern.compile(":").split(tmp,-1);
                mac = list[1].trim();
                founed = false;
            }
        }
    }

    public GetNetInfoFromMDB( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetNetInfoFromMDB( String cmd ){
        super( cmd );
    }

    public boolean realDo1(){ // 随便获取一个mac即可
SanBootView.log.info( getClass().getName(), " get netinfo confile cmd: "+ getCmdLine() );
        try{
            this.skip = true;
            founed = false;
            mac ="";

            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get netinfo confile retcode: "+ getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get netinfo confile errmsg: "+ getErrMsg() );
        }
        return isOk;
    }

    public boolean realDo(){        
SanBootView.log.info( getClass().getName(), " get netinfo confile cmd: "+ getCmdLine() );         
        try{
            skip = false;
            founed = false;
            mac ="";
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get netinfo confile retcode: "+ getRetCode() );    
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get netinfo confile errmsg: "+ getErrMsg() );              
        }
        return isOk;
    }
    
    public String getBootMac(){
        return mac;
    }
    
}
