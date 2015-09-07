/*
 * GetWindir.java
 *
 * Created on April 22, 2005, 1:12 PM
 */

package guisanboot.data;
import guisanboot.ui.SanBootView;
import guisanboot.tool.*;

/**
 *
 * @author  Administrator
 */
public class GetWindir extends NetworkRunning{
    private String path="";
    
    /** Creates a new instance of GetWindir */
    public GetWindir( String cmd ){
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
        String tmp = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ tmp );  
        try{
            if( tmp.length()<4 ){
SanBootView.log.error( getClass().getName(), "bad windir: "+tmp);
                path="";
            }else{
                // path is "windows" or "winnt"
                path = tmp.substring( 3 );
            }
        }catch(Exception ex){
            Tool.prtExceptionMsg( ex, "GetWindir class" );
            path="";
        }
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line );
        }else{
            this.errMsg += line +"\n";
        }
    }

    public String getWindowDir(){ 
SanBootView.log.info( getClass().getName(), " get windir cmd: " + getCmdLine()  );
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
        
SanBootView.log.info( getClass().getName(), " get windir cmd retcode: " + getRetCode()   );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get windir cmd errmsg: " + getErrMsg()   );    
            return "";
        }else{
            return path;
        }
    }
}