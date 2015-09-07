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
public class GetIScsiCmdPath extends NetworkRunning{
    private String path = "";
    
    /** Creates a new instance of GetSystemTime */
    public GetIScsiCmdPath( String cmd ){
        super( cmd );
    }
    
    public GetIScsiCmdPath(){
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line ); 
        String[] list = Pattern.compile("\n").split( line.trim(),-1 );
        try{
            path = list[0];
        }catch(Exception ex){
            path = "";
        }
    }
    
    public String getIScsiCmdPath(){ 
SanBootView.log.info( getClass().getName(), " get iscsi-cmd path cmd: "+ getCmdLine() ); 
        try{
            path ="";
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " get iscsi-cmd  path retcode: "+ getRetCode() ); 
        
        if( getRetCode() != AbstractNetworkRunning.OK ){
SanBootView.log.error( getClass().getName(), " get iscsi-cmd  path errmsg: "+ getErrMsg() );     
            return "";
        }else{
            // 可能还是为空,说明tftp服务没有起来
            return path;
        }
    }
}
