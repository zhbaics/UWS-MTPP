/*
 * GetLunMap1.java
 *
 * Created on 2007/4/26, AM�9:53
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.util.Vector;
import java.net.*;
import java.io.*;

import mylib.tool.*;

/**
 *
 * @author Administrator
 */
public class GetLunMap1 extends NetworkRunning  {
    Vector<LunMap> list = new Vector<LunMap>();
    private LunMap curLM = null;
     
    public void parser(String line){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );           
        String s1 = line.trim();
        SplitString sp = new SplitString( s1 );
        String lm_ip = sp.getNextToken();
        String lm_mask = sp.getNextToken();
        String lm_right = sp.getNextToken();
        String lm_srvusr = sp.getNextToken();
        String lm_clntusr = sp.getNextToken();
        
        curLM= new LunMap(
            lm_ip,
            lm_mask,
            lm_right,
            lm_srvusr,
            "",
            lm_clntusr,
            ""
        );
        list.addElement( curLM );  
    }
    
    public GetLunMap1(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
    
    /** Creates a new instance of GetLunMap1 */
    public GetLunMap1(String cmd ) {
        super( cmd );
    }
    
    public boolean realDo(){ 
SanBootView.log.info( getClass().getName(), "get lunmap cmd: " + getCmdLine() );             
        try{
            list.removeAllElements();
            curLM = null;
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), "get lunmap retcode: " + getRetCode() ); 
        boolean isOk =( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), "get lunmap errmsg: " + getErrMsg() );             
        }
        return isOk;
    }
    
    public Vector<LunMap> getAllLMs(){
        Vector<LunMap> ret = new Vector<LunMap>();
        int size = list.size();
        for( int i=0; i<size; i++ ){
            ret.addElement( list.elementAt(i) );
        }
        return ret;
    }
}
