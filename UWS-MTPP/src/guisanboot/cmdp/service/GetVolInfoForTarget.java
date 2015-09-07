/*
 * GetVolInfoForTarget.java
 *
 * Created on July 14, 2010, 11:33 AM
 */

package guisanboot.cmdp.service;

import guisanboot.data.NetworkRunning;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class GetVolInfoForTarget extends NetworkRunning{
    public final static String  BM_RET = "ret";
    public final static String  BM_NAME = "name";

    private String volInfo = "";
    
    /** Creates a new instance of GetVolInfoForTarget */
    public GetVolInfoForTarget( String cmd ) {
        super( cmd );
    }

    public void parser(String line){
        if( !this.isContinueToParserRetValueForCMDP(line) ) return;

        if( this.isEqZero() ){
            volInfo = line;
        }else{
            this.errMsg += line +"\n";
        }
    }
    
    public boolean getVolInfoForTarget( String clntIP,int port,String targetName ){
        this.volInfo = "";

        this.setCmdLine( ResourceCenter.getCmdpS2A_CmdPath_FOR_LIST_CMD( clntIP, port) +
            " volondisk -desttarget " + targetName
        );
SanBootView.log.info( getClass().getName(), " get volinfo for target cmd: "+ getCmdLine() );
        
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

SanBootView.log.info( getClass().getName(), " get volinfo for target cmd retcode: "+ getRetCode() );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get volinfo for target errmsg: "+ getErrMsg() );
        }
        return isOk;
    }
    
    public String getVolInfo(){
        return this.volInfo;
    }
}
