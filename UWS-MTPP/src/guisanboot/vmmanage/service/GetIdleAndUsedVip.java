/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.service;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;

/**
 *
 * @author zourishun
 */
public class GetIdleAndUsedVip extends NetworkRunning{

    public static final int IDLE_IP_TYPE = 0;
    public static final int USED_IP_TYPE = 1;
    
    ArrayList<String> idleiplist = new ArrayList<String>();
    ArrayList<String> usediplist = new ArrayList<String>();
    private int type = 0;
    @Override
    public void parser(String line) {
        String s1 = line.trim();
        if( s1 != null && !s1.equals("") ){
            if( this.type == IDLE_IP_TYPE ){
                idleiplist.add(s1);
            } else {
                usediplist.add(s1);
            }
        }
    }
    
    public  GetIdleAndUsedVip(String _cmd){
        super( _cmd );
    }
    
    public boolean updateIdleVip(){
        try{
            SanBootView.log.error( getClass().getName(), " get idlevip errmsg: "+getCmdLine() );
            this.type = IDLE_IP_TYPE;
            this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_GET_IDLE_VIP ) );
            idleiplist.clear();
            run();
        } catch( Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        SanBootView.log.error( getClass().getName(), " get idlevip errmsg: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
            SanBootView.log.error( getClass().getName(), " get idlevip errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
    
    public boolean updateUsedVip(){
        try{
            SanBootView.log.error( getClass().getName(), " get usedvip errmsg: "+getCmdLine() );
            this.type = USED_IP_TYPE ;
            this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_GET_USED_VIP ) );
            usediplist.clear();
            run();
        } catch( Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        SanBootView.log.error( getClass().getName(), " get usedvip errmsg: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
            SanBootView.log.error( getClass().getName(), " get usedvip errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
    
    public  ArrayList<String> getIdleVip(){
        return this.idleiplist;
    }
    
    public ArrayList<String> getUsedVip(){
        return this.usediplist;
    }
}
