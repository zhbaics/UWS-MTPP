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
public class GenerateIBootKernelImg extends NetworkRunning{
    private String imgpath="";
    
    /** Creates a new instance of GetSystemTime */
    public GenerateIBootKernelImg( String cmd ){
        super( cmd );
    }
    
    public GenerateIBootKernelImg(){
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"=======> "+ line );         
        imgpath = line;
    }
    
    public String genIbootKernelImg(){ 
SanBootView.log.info(getClass().getName()," genIbootKernelImg cmd: "+ this.getCmdLine() ); 
        try{
            imgpath="";
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info(getClass().getName()," genIbootKernelImg retcode: "+ this.getRetCode() );
        
        if( getRetCode() != AbstractNetworkRunning.OK ){
SanBootView.log.error(getClass().getName()," genIbootKernelImg errmsg: "+ this.getErrMsg() );    
            return "";
        }else{
            // imgpath有可能为空
            return imgpath;
        }
    }
}
