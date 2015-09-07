package guisanboot.cluster.cmd;

/**
 * GetMirrorClusterInfo 2011.08.25 AM 10:04
 */
import guisanboot.cluster.entity.ClusterConfigInfo;
import guisanboot.data.*;
import java.io.*;
import java.net.*;
import guisanboot.ui.*;

public class GetMirrorClusterInfo extends NetworkRunning {
    private ClusterConfigInfo cci = null;
    
    public void parser( String line ){
        this.parserCMDP( line );
    }
    
    public void parserCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP1(line) )return;
        
        if( !this.isEqZero() ){
            this.errMsg += line +"\n";
        }else{
            String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 );

            int index = s1.indexOf("=");
            if( index>0){
                String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value );
                
                if( s1.startsWith( ClusterConfigInfo.CCI_ClusterDiskLength ) ){
                    cci.setClusterDiskLength( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_diskinterval )){
                    cci.setDiskinterval( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_sharediskguid ) ){
                    cci.setSharediskguid( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_ErrorCode ) ){
                    cci.setErrorCode( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_isSocketStarted ) ){
                    cci.setIsSocketStarted( value );
                }else if(s1.startsWith( ClusterConfigInfo.CCI_ret)){
                    cci.setRet( value );
                }else if(s1.startsWith( ClusterConfigInfo.CCI_OtherStatus )){
                    cci.setOtherStatus( value );
                }else if(s1.startsWith( ClusterConfigInfo.CCI_isEnd )){
                    cci.setIsEnd( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_OtherClusterDiskStartoffset ) ){
                    cci.setOtherClusterDiskStartoffset( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_port ) ){
                    cci.setPort( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_isNetWorkError )){
                    cci.setIsNetWorkError( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_ClusterType ) ){
                    cci.setClusterType( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_HasReadDiskTag ) ){
                    cci.setHasReadDiskTag( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_ifCluster ) ){
                    cci.setIfCluster( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_WRDiskFailedTimes ) ){
                    cci.setWRDiskFailedTimes( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_SendErrorTimes ) ){
                    cci.setSendErrorTimes( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_thisip) ){
                    cci.setThisip( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_ThisClusterDiskStartoffset) ){
                    cci.setThisClusterDiskStartoffset( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_hostguid) ){
                    cci.setHostguid( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_OtherClusterDiskLength) ){
                    cci.setOtherClusterDiskLength( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_ClusterDiskStartoffset ) ){
                    cci.setClusterDiskStartoffset( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_otherip ) ){
                    cci.setOtherip( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_return ) ){
                    cci.setReturn( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_maxlisten ) ){
                    cci.setMaxlisten( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_ErrorType ) ){
                    cci.setErrorType( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_socketinterval ) ){
                    cci.setSocketinterval( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_isMaster ) ){
                    cci.setIsMaster( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_isExit ) ){
                    cci.setIsExit( value );
                }else if( s1.startsWith( ClusterConfigInfo.CCI_ThisClusterDiskLength ) ){
                    cci.setThisClusterDiskLength( value );
                }
            }else{
                if( s1.startsWith( BootHost.CMDPRECFLAG )){
                    this.cci = new ClusterConfigInfo();
                }
            }
        }
    }

    public GetMirrorClusterInfo( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetMirrorClusterInfo( String cmd ){
        super( cmd );
    }

    public boolean realDo(){ 
SanBootView.log.info( getClass().getName(), " get mirror cluster info cmd: "+ getCmdLine() );
        try{
            cci = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get mirror cluster info cmd retcode: "+ getRetCode() );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get mirror cluster info cmd errmsg: "+ getErrMsg() );
        }
        return isOk;
    }

    public ClusterConfigInfo getCCI(){
        return this.cci;
    }
    
    public boolean isMasterNode(){
        return this.cci.isMasterNode();
    }
}