/*
 * GetFileLastModTime.java
 *
 * Created on April 23, 2005, 1:34 PM
 */

package guisanboot.data;

import mylib.tool.*;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class GetFileLastModTime extends NetworkRunning{
    String lastModTime = "";
    
    /** Creates a new instance of GetFileLastModTime */
    public GetFileLastModTime( String cmd ) {
        super( cmd );
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line ); 
        
        SplitString sp = new SplitString( line );
        lastModTime = sp.getNextToken()+"/"+sp.getNextToken()+"/"+
                        sp.getNextToken()+"  "+sp.getNextToken()+":"+
                        sp.getNextToken()+":"+sp.getNextToken();
    }
    
    public String getLastModTime( String file ){
        try{
            setCmdLine(
                ResourceCenter.getCmd( ResourceCenter.CMD_GET_FILE_MODTIME ) +
                file
            );
SanBootView.log.info( getClass().getName(), " get lastmodTime cmd: " + this.getCmdLine() );              
            run();
        }catch( Exception ex ){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " get lastmodTime cmd: " + this.getRetCode() );           
        if( getRetCode() != AbstractNetworkRunning.OK ){
SanBootView.log.error( getClass().getName(), " get lastmodTime errmsg: " + this.getErrMsg() );            
            return "";
        }else{
            return lastModTime;
        }
    }
}
