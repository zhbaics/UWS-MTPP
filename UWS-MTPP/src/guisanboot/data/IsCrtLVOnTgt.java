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
public class IsCrtLVOnTgt extends NetworkRunning{  
    private String ret = "";
    
    /** Creates a new instance of GetSystemTime */
    public IsCrtLVOnTgt( String cmd ){
        super( cmd );
    }
    
    public IsCrtLVOnTgt(){
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"(IsCrtLVOnTgt)========> "+ line );         
        ret = line;
    }
    
    public boolean isCrtLVOnTgt(){ 
SanBootView.log.info( getClass().getName(), " get isCrtLVOnTgt cmd: "+ this.getCmdLine() ); 
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " get isCrtLVOnTgt retcode: "+ this.getRetCode() ); 
        if( getRetCode() != AbstractNetworkRunning.OK ){
SanBootView.log.error( getClass().getName(), " get isCrtLVOnTgt errmsg: "+ this.getErrMsg() );     
            return false;
        }else{
            return ret.equals("0");
        }
    }
}
