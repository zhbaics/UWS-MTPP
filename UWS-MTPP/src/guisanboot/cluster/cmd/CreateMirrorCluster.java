/*
 * CreateMirrorCluster.java
 *
 * Created on 8 16, 2011, 10:35 AM
 */

package guisanboot.cluster.cmd;

import guisanboot.data.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class CreateMirrorCluster extends NetworkRunning {
    /** Creates a new instance of CreateMirrorCluster */
    public CreateMirrorCluster(){
    }
    
    public void parser(String line ){   
        parserForCMDP( line );
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( !this.isEqZero() ){
            this.errMsg += line +"\n";
        }
    }
    
    public boolean crtMirrorCluster(){
SanBootView.log.info(getClass().getName()," create mirror cluster cmd: "+ this.getCmdLine() );
        try{
            run();
        }catch(Exception ex){
            setErrMsg( this.getExceptionErrMsg( ex ) );
            setRetCode( this.getExceptionErrCode( ex ) );
        }
SanBootView.log.info(getClass().getName()," create mirror cluster cmd retcode: "+ this.getRetCode() );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error(getClass().getName()," create mirror cluster cmd errmsg: "+ this.getErrMsg() );
        }
        return isOk;
    }
}
