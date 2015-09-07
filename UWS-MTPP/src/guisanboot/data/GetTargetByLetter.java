/*
 * GetTargetByLetter.java
 *
 * Created on June 2, 2009, 1:12 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class GetTargetByLetter extends NetworkRunning{
    private String tgtInfo = "";
    
    /** Creates a new instance of GetSystemTime */
    public GetTargetByLetter( String cmd ){
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
SanBootView.log.debug(getClass().getName(),"========> "+ line );  
        if( line == null || line.equals("") ) return;
        tgtInfo = line.trim();
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line );
        }else{
            this.errMsg += line +"\n";
        }
    }

    public int getTargetIDByLetter( ){  
SanBootView.log.info( getClass().getName(), " get target info by letter cmd: " + getCmdLine() );
        try{
            tgtInfo ="";
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " get target info by letter cmd retcode: " + getRetCode());  
        
        if( !this.isOKForExcuteThisCmd() ){
SanBootView.log.error( getClass().getName(), " get target info by letter cmd errmsg: " + getErrMsg()  );      
            return -1;
        }else{
            int index = tgtInfo.indexOf(":");
            if( index>=0 ){
                try{
                    String tgtID = tgtInfo.substring( index+1 );
                    return Integer.parseInt( tgtID );
                }catch( Exception ex1 ){
SanBootView.log.error( getClass().getName(), " bad target info: " + tgtInfo );
                    return -1;
                }
            }else{
SanBootView.log.error( getClass().getName(), " bad target info: " + tgtInfo );                
                return -1;
            }
        }
    }
}
