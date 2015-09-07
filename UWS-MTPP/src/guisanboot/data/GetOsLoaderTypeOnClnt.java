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
import guisanboot.ui.*;

public class GetOsLoaderTypeOnClnt extends NetworkRunning {
    private String osLoaderType="";
    
    public void parser(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );             
        osLoaderType = line;
    }

    public GetOsLoaderTypeOnClnt( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetOsLoaderTypeOnClnt( String cmd ){
        super( cmd );
    }

    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " get osLoaderType cmd: "+ getCmdLine() );         
        try{
            osLoaderType ="";  
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get osLoaderType retcode: "+ getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get osLoaderType errmsg: "+ getErrMsg() );             
        }
        return isOk;
    }
    
    public String getOsLoaderType(){
        return osLoaderType;
    }
}
