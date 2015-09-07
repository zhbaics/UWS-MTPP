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
public class GenerateIBootPxeLinux extends NetworkRunning{
    private StringBuffer buf;
    private boolean isFirst = true;
    
    /** Creates a new instance of GetSystemTime */
    public GenerateIBootPxeLinux( String cmd ){
        super( cmd );
    }
    
    public GenerateIBootPxeLinux(){
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"=======> "+ line ); 
        if( isFirst ){
            buf.append( line );
            isFirst = false;
        }else{
            buf.append( "\n"+line );
        }
    }
    
    public StringBuffer genIbootPxeLinux(){ 
SanBootView.log.info(getClass().getName(), " genIbootPxeLinux cmd: "+ this.getCmdLine()  );
        buf = new StringBuffer();
        isFirst = true;
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info(getClass().getName(), " genIbootPxeLinux retcode: "+ this.getRetCode()  ); 
        
        if( getRetCode() != AbstractNetworkRunning.OK ){
SanBootView.log.error(getClass().getName(), " genIbootPxeLinux errmsg: "+ this.getErrMsg()  );     
            return null;
        }else{
            // buf内容有可能为空
            return buf;
        }
    }
}
