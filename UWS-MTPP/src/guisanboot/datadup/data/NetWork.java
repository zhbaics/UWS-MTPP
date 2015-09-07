/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.datadup.data;

/**
 *
 * @author Administrator
 */
public class NetWork {

    private String hostId ;
    private String hostIp ;
    private boolean isNetWorkOk ;

    public NetWork(String hostId, String hostIp, boolean isNetWorkOk) {
        this.hostId = hostId;
        this.hostIp = hostIp;
        this.isNetWorkOk = isNetWorkOk;
    }

    public NetWork() {
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public boolean getIsNetWorkOk() {
        return isNetWorkOk;
    }

    public void setIsNetWorkOk(boolean isNetWorkOk) {
        this.isNetWorkOk = isNetWorkOk;
    }

    public String prtMe(){
        StringBuffer buf = new StringBuffer();
       
        buf.append("[network]\n");
        buf.append("host_ip="+getHostIp()+"\n");
        buf.append("host_id="+getHostId()+"\n");
        buf.append("is_net_work_ok="+getIsNetWorkOk()+"\n");
        
        return buf.toString();
    }
    

    

}
