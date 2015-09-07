/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class GetLinuxDevName extends NetworkRunning{
    private ArrayList<String> devlist = new ArrayList<String>();
    @Override
    public void parser(String line) {
        if( line == null || line.equals("") ) return;
        devlist.add(line);
    }
    
    public GetLinuxDevName( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetLinuxDevName( String cmd ){
        super( cmd );
    }
    
    public ArrayList<String> getDevNameList(){
        ArrayList<String> ret = new ArrayList<String>();
        if( devlist.size() > 0){
            for( int i = 0 ; i < devlist.size() ; i++){
                ret.add(devlist.get(i));
            }
        }
        return ret;
    }
    
    public boolean realDo(){
        SanBootView.log.info( getClass().getName(), " get devname for unix cmd: " + this.getCmdLine() );         
        try{
            devlist.clear();
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get devname for unix retcode: " + this.getRetCode() );         
        
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get devname for unix errmsg: " + this.getErrMsg() );                     
        }
        return isOk;
    }
}
