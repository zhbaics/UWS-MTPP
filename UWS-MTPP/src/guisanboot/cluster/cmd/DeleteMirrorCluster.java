/*
 * DeleteMirrorCluster.java
 *
 * Created on 8 16, 2011, 10:35 AM
 */

package guisanboot.cluster.cmd;

import guisanboot.data.*;

/**
 *
 * @author  Administrator
 */
public class DeleteMirrorCluster extends NetworkRunning {
    /** Creates a new instance of DeleteMirrorCluster */
    public DeleteMirrorCluster(){
    }
    
    public void parser(String line ){   
        parserForCMDP( line );
    }

    // driver failed, err=21
    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( !this.isEqZero() ){
            this.errMsg += line +"\n";
        }
    }
    
    public boolean delMirrorCluster(){
        try{
            run();
        }catch(Exception ex){
            setErrMsg( this.getExceptionErrMsg( ex ) );
            setRetCode( this.getExceptionErrCode( ex ) );
        }
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
            if( this.getErrMsg().contains("err=21") ){
                isOk = true;
            }else{
                isOk = false;
            }
        }
        return isOk;
    }
}
