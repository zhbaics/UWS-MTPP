/*
 * IFConfObj.java
 *
 * Created on 2008/7/10, PM�2:21
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

/**
 *
 * @author zjj
 */
public class IFConfObj {
    private String ifname;
    private String ip;
    private String mask;
    private String broadcast;
    private String status;
             
    /** Creates a new instance of IFConfObj */
    public IFConfObj() {
    }
    
    public IFConfObj(
        String ifname,
        String ip,
        String mask,
        String broadcast,
        String status
    ){
        this.ifname = ifname;
        this.ip = ip;
        this.mask = mask;
        this.broadcast = broadcast;
        this.status = status;
    }
    
    public String getIfname() {
        return ifname;
    }

    public void setIfname(String ifname) {
        this.ifname = ifname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String prtMe(){
        return "ifconfig "+this.ifname +" "+this.ip +" "+this.mask +" "+this.broadcast +" "+this.status;
    }
}
