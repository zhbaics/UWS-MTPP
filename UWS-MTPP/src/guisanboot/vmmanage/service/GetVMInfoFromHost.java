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
public class GetVMInfoFromHost extends NetworkRunning{

    public static final String OS_TYPE = "ostype";
    public static final String ARCH = "arch";
    public static final String BLOCK_SIZE = "block_size";
    public static final String MAX_BLOCK_NO = "max_block_no";
    
    private String osType = "";
    private String arch = "";
    private int block_size = 0;
    private int max_block_no = 0;
    
    
    @Override
    public void parser(String line) {
        String s1 = line.trim();
        SanBootView.log.debug(getClass().getName(),"#####: " + s1 ); 
        int index = s1.indexOf("=");
        if(index > 0 ){
            String value = s1.substring( index+1 ).trim();
            if( s1.startsWith(OS_TYPE) ){
                this.osType = value;
            } else if( s1.startsWith( ARCH ) ){
                this.arch = value;
            } else if( s1.startsWith( BLOCK_SIZE ) ){
                try{
                    this.block_size = Integer.parseInt(value);
                } catch(Exception ex){
                    this.block_size = 0;
                }
            } else if( s1.startsWith( MAX_BLOCK_NO ) ){
                try{
                    this.max_block_no = Integer.parseInt(value);
                }catch(Exception ex){
                    this.max_block_no = 0;
                }
            }
        }
    }
    
    public long getDiskSize(){
        return (long)(2<<(block_size-1))*max_block_no;
    }
    
    public String getArch(){
        return this.arch;
    }
    
    public String getOsType(){
        return this.osType.substring( this.osType.indexOf("2") );
    }
    
    private void clean(){
        this.osType = "";
        this.arch = "";
        this.block_size = 0;
        this.max_block_no = 0;
    }
    
    public boolean updateVMinfoFromHost(String ip){
        try{
            clean();
            this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_GET_INFO_FROM_HOST ) + ip);
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
        SanBootView.log.error( getClass().getName(), " get vminfofromhost errmsg: "+getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
            SanBootView.log.error( getClass().getName(), " get vminfofromhost errmsg: "+getErrMsg() );            
        }
        return isOk;
    }
}
