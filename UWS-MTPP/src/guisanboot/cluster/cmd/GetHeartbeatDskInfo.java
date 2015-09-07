package guisanboot.cluster.cmd;

/**
 * Title:        uws
 * Description:
 * Copyright:    Copyright (c) 2011
 * Company:      odysys
 * @author
 * @version 6.0
 */
import guisanboot.data.*;
import java.io.*;
import java.net.*;
import guisanboot.ui.*;

public class GetHeartbeatDskInfo extends NetworkRunning {
    private String hbdsk="";
    
    public void parser(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line ); 
        String tmp = line.trim();
        hbdsk = tmp;
    }

    public GetHeartbeatDskInfo( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetHeartbeatDskInfo( String cmd ){
        super( cmd );
    }

    public boolean realDo(){        
SanBootView.log.info( getClass().getName(), " get heartbeatdsk info cmd: "+ getCmdLine() );
        try{            
            hbdsk="";
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get heartbeatdsk info retcode: "+ getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get heartbeatdsk info errmsg: "+ getErrMsg() );
        }
        return isOk;
    }

    public String getHbDsk(){
        return hbdsk;
    }
}
