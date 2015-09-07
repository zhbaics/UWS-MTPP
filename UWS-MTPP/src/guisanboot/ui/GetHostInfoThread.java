/*
 * GetHostInfoThread.java
 *
 * Created on January 28, 2005, 11:46 AM
 */

package guisanboot.ui;

import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class GetHostInfoThread extends BasicGetSomethingThread{
    private String ip;
    private int port;
    private boolean isWin = true;
    private int ptype = BootHost.PROTECT_TYPE_MTPP;
    private GetAgentInfo getAgentInfo;
    private int retCode=0;
    private String uuid="";
    
    /**
     * Creates a new instance of GetHostInfoThread 
     */
    public GetHostInfoThread(
        SanBootView view,
        String _ip,
        int _port 
    ) {
        super( view,false );
        
        ip = _ip;
        port = _port;
    }
    
    public GetHostInfoThread(
        SanBootView view,
        String _ip,
        int _port,
        boolean isWin,
        int ptype
    ) {
        super( view,false );
        
        ip = _ip;
        port = _port;
        this.isWin = isWin;
        this.ptype = ptype;
    }

    public boolean realRun(){
        boolean isOK;
        
        try{
            String cmdline;
            int cmdtype = ResourceCenter.CMD_TYPE_MTPP;
            if( this.isWin ){
                if( this.ptype == BootHost.PROTECT_TYPE_CMDP ){
                    cmdline = ResourceCenter.getCmdpS2A_CmdPath1(ip, port)+" getsysinfo 2>/dev/null";
                    cmdtype = ResourceCenter.CMD_TYPE_CMDP;
                }else{
                    cmdline = ResourceCenter.getCmd( ResourceCenter.CMD_GET_HOST_INFO1 ) + ip + " " + port+" ib_get_osinfo.exe";
                }
            }else{
                cmdline =  ResourceCenter.getCmd( ResourceCenter.CMD_GET_HOST_INFO )+
                ip+" "+port;
            }

            getAgentInfo = new GetAgentInfo( 
                cmdline,
                view.getSocket()
            );
            getAgentInfo.setCmdType( cmdtype );
            isOK = getAgentInfo.getAgentInfo();
            if( isOK ){
                if( getAgentInfo.getOSName().toUpperCase().startsWith("WIN") ){
                    isOK = view.initor.mdb.getHostUUID( ip, port, true, cmdtype );
                }else{
                    isOK = view.initor.mdb.getHostUUID( ip,port,false );
                }
                
                if( !isOK ){
                    errMsg = view.initor.mdb.getErrorMessage();
                    retCode = view.initor.mdb.getErrorCode();
                }else{
                    uuid = view.initor.mdb.getUUID();
                }
            }else{
                errMsg = getAgentInfo.getErrMsg();
                retCode = getAgentInfo.getRetCode();
            }
        }catch(Exception ex){
            isOK = false;
            ex.printStackTrace();
        }
        
        return isOK;
    }
    
    public String getErrMsgFromGetAgentInfo(){
        return errMsg;
    }
    public int getErrCodeFromGetAgentInfo(){
        return retCode;
    }
    public String getOSName(){
        return getAgentInfo.getOSName();
    }
    public String getWinPlatForm(){
        try{
            String release = getAgentInfo.getOSName().substring(3);
            return release.toUpperCase();
        }catch(Exception ex){
            return BootHost.W_2003;
        }
    }
    public boolean isWinHost(){
        return BootHost.isWinHost( getAgentInfo.getOSName() );
    }
    public String getOSRelease(){
        return getAgentInfo.getOSRelease();
    }
    public String getHostName(){
        return getAgentInfo.getHostName();
    }
    public String getMachine(){
        return getAgentInfo.getMachine();
    }
    public String getHostId(){
        return getAgentInfo.getHostId();
    }
    public String getUUID(){
        return uuid;
    }
}
