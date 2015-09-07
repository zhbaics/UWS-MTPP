/*
 * GetSystemTime.java
 *
 * Created on April 22, 2005, 1:12 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.util.regex.*;


/**
 *
 * @author  Administrator
 */
public class GenerateIBootInitrd extends NetworkRunning{
    private String initrdPath="";
    
    /** Creates a new instance of GetSystemTime */
    public GenerateIBootInitrd( String cmd ){
        super( cmd );
    }
    
    public GenerateIBootInitrd(){
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName()," =====> "+ line ); 

        String[] ret = Pattern.compile("\\s+").split( line,-1 );
        try{
            if( ret[1].equals("created") ){
                initrdPath = ret[0];
            }
        }catch(Exception ex){
            initrdPath = "";
        }
    }
    
    public String genIbootInitrd(){ 
SanBootView.log.info(getClass().getName()," genIbootInitrd cmd: "+ this.getCmdLine() );
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info(getClass().getName()," genIbootInitrd retcode: "+ this.getRetCode() );
        
        if( getRetCode() != AbstractNetworkRunning.OK ){
SanBootView.log.error(getClass().getName()," genIbootInitrd errmsg: "+ this.getErrMsg() );    
            return "";
        }else{
            // initrdPath可能为空
            return initrdPath;
        }
    }
}
