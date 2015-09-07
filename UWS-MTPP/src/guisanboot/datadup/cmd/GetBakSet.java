/*
 * GetBakSet.java
 *
 * Created on November 15, 2004, 4:27 PM
 */

package guisanboot.datadup.cmd;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import java.io.*;
import java.net.*;
import guisanboot.res.*;
import guisanboot.datadup.data.BakSet;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class GetBakSet extends NetworkRunning{
    protected BakSet curBakSet  = null;
    
    /** Creates a new instance of GetBakSet */
    public GetBakSet( String cmd ) {
        super( cmd );
    }
    
    public void parser(String line) {
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(), "======> " + s1);
        
        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
            String key = s1.substring(0,index);
//System.out.println("####: "+key);
//System.out.println("@@@@@: "+value);
            if( key.equals( BakSet.BAKSET_ID ) ){
                try{
                    long id = Long.parseLong( value );
                    curBakSet.setID( id );
                }catch(Exception ex){
                    curBakSet.setID( -1L );
                }
            }else if( key.equals( BakSet.BAKSET_OBJ )){
                try{
                    int objId = Integer.parseInt( value );
                    curBakSet.setObj( objId );
                }catch(Exception ex){
                    curBakSet.setObj( -1 );
                }
            }else if(key.equals( BakSet.BAKSET_STIME )){
                curBakSet.setStarttime( value );
            }else if(key.equals( BakSet.BAKSET_STATUS )){
                curBakSet.setStatus( value ); 
            }
        }else{
            if( s1.startsWith( BakSet.BAKSET_RECFLAG )){
                curBakSet = new BakSet();
            }
        }
    }
    
    public GetBakSet( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    // 获取某个bkst的内容
    public boolean updateBakSet( long bks_id ){
        try{
            curBakSet = null;
            
            setCmdLine(
                ResourceCenter.getCmd( ResourceCenter.CMD_GET_BKSET ) + bks_id
            );
SanBootView.log.info(getClass().getName(), " get bkst cmd: "+this.getCmdLine());
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info(getClass().getName(), " get bkst cmd retcode: "+this.getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error(getClass().getName(), " get bkst cmd errmsg: "+this.getErrMsg() );             
        }
        return isOk;
    }
    
    public BakSet getBakSet(){
        return curBakSet;
    }
}
