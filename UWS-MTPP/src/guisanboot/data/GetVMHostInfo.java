/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class GetVMHostInfo extends NetworkRunning{
    
    private ArrayList<VMHostInfo> vmhi = new ArrayList<VMHostInfo>();
    private VMHostInfo currVMH = null ;
    
    public GetVMHostInfo(){
        super();
    }
    
    public GetVMHostInfo(String cmd){
        super(cmd);
    }

    public GetVMHostInfo(String cmd , Socket socket) throws IOException{
        super( cmd ,socket );
    }
    
    @Override
    public void parser(String line) {
         String s1 = line.trim();
        SanBootView.log.debug(getClass().getName(),"#####: " + s1 ); 

        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
            SanBootView.log.debug(getClass().getName(),"@@@@@: "+value ); 
            if( s1.startsWith( VMHostInfo.VM_ip ) ){
                currVMH.setVm_ip(value);
            } else if ( s1.startsWith( VMHostInfo.VM_name ) ){
                currVMH.setVm_name(value);
                vmhi.add(currVMH);
            } else if ( s1.startsWith( VMHostInfo.VM_path ) ){
                currVMH.setVm_path(value);
            } else if ( s1.startsWith( VMHostInfo.VM_port ) ){
                currVMH.setVm_port(value);
            } else if ( s1.startsWith( VMHostInfo.VM_snapid ) ){
                currVMH.setVm_snapid(value);
            } else if ( s1.startsWith( VMHostInfo.VM_targetid ) ){
                currVMH.setVm_targetid(value);
            } else if ( s1.startsWith( VMHostInfo.VM_vip ) ){
                currVMH.setVm_vip(value);
            } else if ( s1.startsWith( VMHostInfo.VM_letter ) ){
                currVMH.setVm_letter(value);
            } else if ( s1.startsWith( VMHostInfo.VM_viewid ) ){
                currVMH.setVm_viewid(value);
            } else if ( s1.startsWith( VMHostInfo.VM_clntid ) ){
                currVMH.setVm_clntid(value);
            } else if ( s1.startsWith( VMHostInfo.VM_recoverip ) ){
                try{
                    int tmpint = Integer.parseInt(value);
                    currVMH.setVm_recoverip(tmpint);
                } catch(Exception ex){
                    currVMH.setVm_recoverip(0);
                }
            } else if(s1.startsWith(VMHostInfo.VM_standby_maxtime)){
                currVMH.setVm_maxdisctime(value);
            } else if(s1.startsWith(VMHostInfo.VM_standbyIsCheck)){
                currVMH.setVm_ischeck(value);
            } else if(s1.startsWith(VMHostInfo.VM_standbyCheckIP)){
                currVMH.setVm_pingip(value);
            }
        } else if( s1.startsWith( VMHostInfo.VM_RecordFlag ) ){
            currVMH = new VMHostInfo();
        }

    }

    @Override
    public String getCmdLine() {
        return super.getCmdLine();
    }

    @Override
    public void setCmdLine(String _cmdLine) {
        super.setCmdLine(_cmdLine);
    }
    
    public boolean updateVMHostInfo( String conffile ){
        SanBootView.log.info( getClass().getName(), " get mirror_disk_info cmd: " + getCmdLine()  );        
        try{
            this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_GET_VM_INFO ) + conffile );
            vmhi.clear();
            currVMH = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get mirror_disk_info retcode: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get mirror_disk_info errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
    
    public ArrayList<VMHostInfo> getAllVMHostInfo(){
        int size = vmhi.size();
        ArrayList<VMHostInfo> ret = new ArrayList<VMHostInfo>(size);
        for( int i =0 ; i< size ; i++ ){
            ret.add( vmhi.get(i) );
        }
        return ret;
    }
    
    public VMHostInfo getVMHostInfoByName( String vmname ){
        int size = vmhi.size();
        for ( int i=0 ; i < size ; i++ ){
            VMHostInfo tmpvmhi = ( VMHostInfo )vmhi.get(i);
            if( tmpvmhi.getVm_name().equals( vmname ) ){
                return tmpvmhi;
            }
        }
        return null ;
    }
}
