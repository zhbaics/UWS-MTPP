/*
 * StartorStopService.java
 *
 * Created on April 22, 2005, 1:12 PM
 */

package guisanboot.data;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class StartorStopService extends NetworkRunning{
    /** Creates a new instance of StartorStopService */
    public StartorStopService( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        if( this.isCMDPCmd() ){
            parserForCMDP( line );
        }
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( !this.isEqZero() ){
            this.errMsg += line +"\n";
        }
    }
    
    public boolean stopOrStartService( String cltIP,int cltPort,String mode,String service,int cmdType){
        if( cmdType == ResourceCenter.CMD_TYPE_MTPP ){
            setCmdLine(
                ResourceCenter.getCmd( ResourceCenter.CMD_STOP_START_SERVICE ) + cltIP + " " + cltPort +
                    //" services_process.exe " + mode + " " + service ( chenlianwu's )
                    " ib_win_service.exe " + mode + " " + service
            );
        }else{
            setCmdLine(
                ResourceCenter.getCmdpS2A_CmdPath( cltIP, cltPort ) + " ib_win_service.exe " + mode + " " + service
            );
        }
        this.setCmdType( cmdType );
SanBootView.log.info( getClass().getName(), " stoporstart service cmd: " + getCmdLine() );                                 
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " stoporstart service cmd retcode: " + getRetCode() );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " stoporstart service cmd errmsg: " + getErrMsg() );
        }
        return isOk;
    }
}
