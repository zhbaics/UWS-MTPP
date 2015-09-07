/*
 * GetTargetDriver.java
 *
 * Created on April 22, 2005, 1:12 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import mylib.tool.*;


/**
 *
 * @author  Administrator
 */
public class GetTargetDriver extends NetworkRunning{
    private String driver="";
    
    /** Creates a new instance of GetTargetDriver */
    public GetTargetDriver( String cmd ){
        super( cmd );
    }

    public void parser( String line ){
        if( this.isMTPPCmd() ){
            parserForMTPP( line );
        }else{
            parserForCMDP( line );
        }
    }

    public void parserForMTPP( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        String tmp = line.trim();
        SplitString sp = new SplitString( tmp );
        driver = sp.getNextTokenN( 2 );
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line );
        }else{
            this.errMsg += line +"\n";
        }
    }

    public String getTargetDriver( String targetName ){
SanBootView.log.info( getClass().getName(), " get target driver cmd: " + getCmdLine() );         
        try{
            driver ="";
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " get target driver cmd retcode: " + getRetCode());  
        
        if( !this.isOKForExcuteThisCmd() ){
SanBootView.log.error( getClass().getName(), " get target driver cmd errmsg: " + getErrMsg()  );      
            return "";
        }else{
            return driver;
        }
    }
}
