/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.service;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;

/**
 *
 * @author Administrator
 */
public class HasVMServiceInfoOrNot extends NetworkRunning{

    private String hasOrNot="";
    public final static String INFO_YES="0";
    public final static String INFO_NO="1";
    
    @Override
    public void parser(String line) {
        hasOrNot = line.trim();
    }
    
    public HasVMServiceInfoOrNot(String _cmd){
        super(_cmd);
    }
    
    public boolean updateInfo(){
        try{
            this.setCmdLine(ResourceCenter.getCmd( ResourceCenter.CMD_HAS_VMSERVICE_INFO_OR_NOT));
            SanBootView.log.info( getClass().getName(), " hasvmserviceinfo cmd: " + getCmdLine()  );
            this.hasOrNot = "";
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        SanBootView.log.info( getClass().getName(), " get hasvmserviceinfo retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get hasvmserviceinfo errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
    
    public boolean hasVMServiceInfo(){
        boolean isOk = updateInfo();
        if( isOk ){
            if( INFO_YES.equals(this.hasOrNot) ){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
