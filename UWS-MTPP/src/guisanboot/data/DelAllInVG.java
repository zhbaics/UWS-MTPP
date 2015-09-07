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
public class DelAllInVG extends NetworkRunning{  
    private String ret = "";
    
    /** Creates a new instance of GetSystemTime */
    public DelAllInVG( String cmd ){
        super( cmd );
    }
    
    public DelAllInVG(){
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"(DelAllInVG)========> "+ line );         
        ret = line;
    }
    
    public boolean delAllInVg(){ 
SanBootView.log.info( getClass().getName(), " delAllInVg cmd: "+ getCmdLine()  );
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " delAllInVg cmd retcode: "+ getRetCode()  );
        if( getRetCode() != AbstractNetworkRunning.OK ){
SanBootView.log.error( getClass().getName(), " delAllInVg cmd errmsg: "+ getErrMsg()  );    
            return false;
        }else{
            return true;
            //return ret.equals("0");
        }
    }
}
