/*
 * SetNetBootVersion.java
 *
 * Created on July 1, 2010, 11:14 AM
 */

package guisanboot.cmdp.service;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class SetNetBootVersion extends NetworkRunning{
    private String ret = "";
    
    /** Creates a new instance of SetNetBootVersion */
    public SetNetBootVersion( String cmd ) {
        super( cmd );
    }

/*
successfully!
count=1
---index 0----
ret=0
*/
    public void parser(String line){
        String s1 = line.trim();
        
        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(this.getClass().getName(), "(SetNetBootVersion)===> "+ s1);

            if( s1.startsWith( "ret" ) ){
                this.ret = value;
            }
        }
    }
    
    public boolean setNetBootVersion( String clntIP,int port,String rootIDStr,String localIDStr,int num ){
        this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_SET_NETBOOT_VERSION ) +
            clntIP+","+port+","+rootIDStr+","+localIDStr+","+num
        );
SanBootView.log.info( getClass().getName(), " set netboot version cmd: "+ getCmdLine() );
        this.ret = "";
        
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

SanBootView.log.info( getClass().getName(), " set netboot version retcode: "+ getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " set netboot version errmsg: "+ getErrMsg() );
        }
        return isOk;
    }

    public boolean isOkForSetNetBootVer(){
        return this.ret.equals("0");
    }
}
