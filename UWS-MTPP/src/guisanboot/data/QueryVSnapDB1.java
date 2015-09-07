/*
 * QueryVSnapDB1.java
 *
 * Created on Sep 6, 2010, 11:14 AM
 */

package guisanboot.data;

import java.net.*;
import java.io.*;

import guisanboot.ui.*;

/**
 *
 * @author  Administrator
 */
public class QueryVSnapDB1 extends NetworkRunning{
    private int count = 0;
    
    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );
        int indx = s1.indexOf("=");
        if( indx > 0 ){
            try{
                int intCnt = Integer.parseInt( s1.substring( indx + 1 ) );
                count = intCnt;
            }catch(Exception ex){
                count = 0;
            }
        }
    }
    
    public QueryVSnapDB1( String cmd, Socket socket ) throws IOException{
        super( cmd ,socket );
    }
  
    public QueryVSnapDB1(String cmd){
        super( cmd );
    }
    
    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " query vsnap db cmd: "+this.getCmdLine() ); 
        try{
            count = 0;
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " query vsnap cmd retcode: "+this.getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " query vsnap cmd errmsg: "+this.getErrMsg() );            
        }
        return isOk;
    }

    public int getQueryResult(){
        return this.count;
    }
}
