/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.service;

import guisanboot.data.NetworkRunning;
import guisanboot.res.ResourceCenter;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author zourishun
 */
public class VMHostOperationService extends NetworkRunning{

    @Override
    public void parser(String line) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public VMHostOperationService(String cmd){
        super(cmd);
    }
    
    public VMHostOperationService(Socket socket , String cmd) throws IOException{
        super( cmd , socket );
    }
    
    public boolean powerOnVMHost(String args){
        try{
            this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_POWERON_VMHOST ) + args );
            run();
        }catch (Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        return true;
    }
    
    public boolean powerOffVMHost(String args){
        try{
            this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_POWEROFF_VMHOST ) + args );
            run();
        } catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        return true;
    }
    
    public boolean isRunningVMHots(String args){
        try{
            this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_ISRUNNING_VMHOST ) + args );
            run();
        } catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        return true;
    }
    
    public boolean changeVMHostDisk(String args){
        try{
            this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CHANGEDISK_VMHOST ) + args );
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        return true;
    }
}
