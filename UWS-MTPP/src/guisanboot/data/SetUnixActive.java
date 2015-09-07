/*
 * GetSystemTime.java
 *
 * Created on April 22, 2005, 1:12 PM
 */

package guisanboot.data;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class SetUnixActive extends NetworkRunning{
    private String activeFlag="";
    
    /** Creates a new instance of GetSystemTime */
    public SetUnixActive( String cmd ){
        super( cmd );
    }
    
    public SetUnixActive(){
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        activeFlag = line;
    }
    
    public boolean getActiveFlag(){ 
SanBootView.log.info( getClass().getName()," get unix active flag cmd: "+ this.getCmdLine() ); 
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName()," get unix active flag retcode: "+ this.getRetCode() );         
        boolean isOk =  ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," get unix active flag errmsg: "+ this.getErrMsg() );               
        }
        return isOk;
    }
    
    public boolean isSetActive(){
        return activeFlag.toUpperCase().equals("ACTIVE");
    }
}
