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
public class GetClntIDFromObjID extends NetworkRunning{
    private int clntId = -1;
    
    /** Creates a new instance of GetSystemTime */
    public GetClntIDFromObjID( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );
        
        int indx = line.indexOf("=");
        if( indx >0 ){
            if( line.startsWith("bkobj_client") ){
                try{
                    clntId = Integer.parseInt( line.substring(indx+1) );
                }catch(Exception ex){
                    clntId = -1;
                }
            }
        }
    }
    
    public int getClntID( String cmd ){
SanBootView.log.info( getClass().getName(), " (get clnt id from obj id in sch obj) cmd : "+cmd  );
        setCmdLine( cmd );
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " (get clnt id from obj id in sch obj) retcode : "+getRetCode()  );       
        if( getRetCode() != AbstractNetworkRunning.OK ){
SanBootView.log.error( getClass().getName(), " (get clnt id from obj id in sch obj) errmsg : "+getErrMsg()  );       
            return -1;
        }else{
            return clntId;
        }
    }
}
