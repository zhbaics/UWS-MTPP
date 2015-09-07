/*
 * CreateVersion.java
 *
 * Created on July 23, 2010, 11:28 AM
 */

package guisanboot.cmdp.service;

import guisanboot.data.NetworkRunning;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class CreateVersion extends NetworkRunning{
    /** Creates a new instance of CreateVersion */
    public CreateVersion( String cmd ) {
        super( cmd );
    }

    public void parser(String line){
        String s1 = line.trim();

        int index = s1.indexOf("=");
        if( index > 0 ){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(this.getClass().getName(), "(CreateVersion)===> "+ s1);

            if( s1.startsWith("ret") ){
                this.retForCMDP = value;
                try{
                    this.retCode = Integer.parseInt( value );
                }catch(Exception ex){
                    this.retCode = -1;
                }
            }
        }
    }
    
    public boolean createVersion( String clntIP,int port,String name,int isgrp, String desc,int mode ){
        this.retForCMDP = "1";
        
        this.setCmdLine(
            ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_ADD_SNAP ) + clntIP + "," + port + "," + name + "," +desc +","+ isgrp+","+mode
        );
SanBootView.log.info( getClass().getName(), " create phy-version cmd: " + getCmdLine() );
        this.setCmdType( ResourceCenter.CMD_TYPE_CMDP );
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }

SanBootView.log.info( getClass().getName(), " create phy-version cmd retcode: "+ getRetCode() );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " create phy-version cmd errmsg: "+ getErrMsg() );
        }
        return isOk;
    }
}