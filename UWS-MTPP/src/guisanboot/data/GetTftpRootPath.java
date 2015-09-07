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
public class GetTftpRootPath extends NetworkRunning{
    private String root="";
    
    /** Creates a new instance of GetSystemTime */
    public GetTftpRootPath( String cmd ){
        super( cmd );
    }
    
    public GetTftpRootPath(){
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        root=line;
    }
    
    public String getTftpRootPath(){ 
SanBootView.log.info( getClass().getName(), " get tftp root path cmd: "+ getCmdLine() ); 
        try{
            root="";
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " get tftp root path retcode: "+ getRetCode()  ); 
        
        if( getRetCode() != AbstractNetworkRunning.OK ){
SanBootView.log.error( getClass().getName(), " get tftp root path errmsg: "+ getErrMsg()  );     
            return "";
        }else{
            // 可能还是为空,说明tftp服务没有起来
            return root;
        }
    }
}
