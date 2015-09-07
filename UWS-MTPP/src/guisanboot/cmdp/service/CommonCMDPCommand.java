/*
 * CommonCMDPCommand.java
 *
 * Created on June 23, 2010, 15:21 PM
 */

package guisanboot.cmdp.service;

import guisanboot.data.NetworkRunning;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class CommonCMDPCommand extends NetworkRunning{
    /** Creates a new instance of CommonCMDPCommand */
    public CommonCMDPCommand( ) {
        super();
    }
    
    public void parser( String line ){
SanBootView.log.debug(getClass().getName(),"*****" + this.getCmdLine()+ "    [ ret: " + line +" ] *****" );
        retForCMDP = line.trim();
        try{
            this.retCode = Integer.parseInt( retForCMDP );
        }catch( Exception ex ){}
    }

    public boolean execCMDPcmd1( String cmd_desc,String cmd ){
        this.setCmdLine( cmd );
SanBootView.log.info( getClass().getName(),cmd_desc + cmd );
        retForCMDP = "";
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

        return this.isEqZero();
    }

    public boolean execCMDPcmd( String cmd_desc,String cmd ){
        this.setCmdLine( cmd );
SanBootView.log.info( getClass().getName(),cmd_desc + cmd );
        retForCMDP = "1";
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

        return this.isEqZero();
    }

    public boolean isNotConnectToHost(){
        return this.errMsg.contains("connect to server failed,");
    }
}
