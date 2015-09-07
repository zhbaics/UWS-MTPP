/*
 * CheckVolAftRegister.java
 *
 * Created on 4 11, 2011, 10:35 AM
 */

package guisanboot.cmdp.service;

import guisanboot.data.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class CheckVolAftRegister extends NetworkRunning {
    /** Creates a new instance of CheckVolAftRegister */
    public CheckVolAftRegister(){
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
    
    public boolean checkVolAftRegister(){
SanBootView.log.info(getClass().getName()," check vol aft registerring cmd: "+ this.getCmdLine() );
        try{
            run();
        }catch(Exception ex){
            setErrMsg( this.getExceptionErrMsg( ex ) );
            setRetCode( this.getExceptionErrCode( ex ) );
        }
SanBootView.log.info(getClass().getName()," check vol aft registerring cmd retcode: "+ this.getRetCode() );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error(getClass().getName()," check vol aft registerring  cmd errmsg: "+ this.getErrMsg() );
        }
        return isOk;
    }
}
