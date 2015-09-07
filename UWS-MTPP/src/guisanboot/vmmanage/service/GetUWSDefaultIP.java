/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.service;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import guisanboot.ui.SanBootView;

/**
 *
 * @author Administrator
 */
public class GetUWSDefaultIP extends NetworkRunning{

    private String defaultIp="";
    
    @Override
    public void parser(String line) {
        String s1 = line.trim();
        if(s1 != null && !"".equals(s1)){
            defaultIp = s1;
        } else {
            defaultIp = "";
        }
    }
    
    public GetUWSDefaultIP(String _cmd){
        super(_cmd);
    }
    
    public String getUWSDefaultIp(){
        return defaultIp;
    }
    
    public boolean updateDefaultIp(){
        try{
            defaultIp = "";
            run();
        } catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        SanBootView.log.error( getClass().getName(), " get defaultip errmsg: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
            SanBootView.log.error( getClass().getName(), " get defaultip errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
}
