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
public class GetLVMType extends NetworkRunning{
    public final static String LVM_TYPE_NONE="NONE";
    public final static String LVM_TYPE_LVM1="LVM1";
    public final static String LVM_TYPE_LVM2="LVM2";
    
    private String type = LVM_TYPE_NONE;
    
    /** Creates a new instance of GetSystemTime */
    public GetLVMType( String cmd ){
        super( cmd );
    }
    
    public GetLVMType(){
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );        
        type = line.trim();
    }
    
    public String getLVMType(){ 
SanBootView.log.info( getClass().getName(), " get lvmtype cmd: "+ this.getCmdLine() );
        try{
            type = LVM_TYPE_NONE;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " get lvmtype retcode: "+ this.getRetCode() ); 
        if( getRetCode() != AbstractNetworkRunning.OK ){
SanBootView.log.error( getClass().getName(), " get lvmtype errmsg: "+ this.getErrMsg() );     
            return LVM_TYPE_NONE;
        }else{
            return type;
        }
    }
}
