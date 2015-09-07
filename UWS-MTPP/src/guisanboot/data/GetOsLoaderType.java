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
public class GetOsLoaderType extends NetworkRunning{
    private String osLoaderType="";
    
    /** Creates a new instance of GetSystemTime */
    public GetOsLoaderType( String cmd ){
        super( cmd );
    }
    
    public GetOsLoaderType(){
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line ); 
        osLoaderType = line.trim();
    }
    
    public boolean getOsLoaderType(){ 
SanBootView.log.info( getClass().getName(), " get os loader cmd: "+ this.getCmdLine() ); 
        try{
            osLoaderType="";
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " get os loader retcode: "+ this.getRetCode()  ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get os loader errmsg: "+ this.getErrMsg()  );             
        }
        return isOk;
    }
    
    public String getosLoaderType(){
        return osLoaderType;
    }
    
    public boolean isGrub(){
        return osLoaderType.toUpperCase().equals("GRUB");
    }
    
    public boolean isLilo(){
        return osLoaderType.toUpperCase().equals("LILO");
    }
}
