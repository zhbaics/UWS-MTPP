/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;


import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class SDHMUWSState {

    public static final String UWS_ALL_DISK = "alldisk";
    public static final String UWS_FREE_DISK = "freedisk";
    public static final String UWS_VISUAL_DISK = "visualdisk";
    public static final String UWS_USED_DISK = "useddisk";
    public static final String UWS_LOCAL_DISK = "localdisk";
    public static final String UWS_MIRROR_DISK = "mirrordisk";
    public static final String UWS_IDLE_DISK = "idledisk";
    public static final String UWS_MEM_USED = "memoryused";
    public static final String UWS_CPU_USED = "cpuused";
    public static final String UWS_NET_READ = "netreadio";
    public static final String UWS_NET_WRITE = "netwriteio";
    public static final String UWS_IP = "ody_monitor_host_ip";
    public static final String UWS_NAME = "ody_monitor_host_name";
    private BigDecimal alldisk = BigDecimal.ZERO;
    private BigDecimal freedisk = BigDecimal.ZERO;
    private BigDecimal visualdisk = BigDecimal.ZERO;
    private BigDecimal useddisk = BigDecimal.ZERO;
    private BigDecimal localdisk = BigDecimal.ZERO;
    private BigDecimal mirrordisk = BigDecimal.ZERO;
    private BigDecimal idledisk = BigDecimal.ZERO;
    private BigDecimal memoryused = BigDecimal.ZERO;
    private BigDecimal cpuused = BigDecimal.ZERO;
    private BigDecimal receive = BigDecimal.ZERO;
    private BigDecimal send = BigDecimal.ZERO;
    private String uwsip = "";
    private String uwsname = "";

    /** Creates a new instance of SDHMUWSState */
    public SDHMUWSState() {
    }

    public BigDecimal getAllDisk() {
        return alldisk;
    }

    public void setAllDisk(BigDecimal _alldisk) {
        alldisk = _alldisk;
    }

    public BigDecimal getFreeDisk() {
        return freedisk;
    }

    public void setFreeDisk(BigDecimal _freedisk) {
        freedisk = _freedisk;
    }

    public BigDecimal getVisualDisk() {
        return visualdisk;
    }

    public void setVisualDisk(BigDecimal _visualdisk) {
        visualdisk = _visualdisk;
    }

    public BigDecimal getUsedDisk() {
        return useddisk;
    }

    public void setUsedDisk(BigDecimal _useddisk) {
        useddisk = _useddisk;
    }

    public BigDecimal getLocalDisk() {
        return localdisk;
    }

    public void setLocalDisk(BigDecimal _localdisk) {
        localdisk = _localdisk;
    }

    public BigDecimal getMirrorDisk() {
        return mirrordisk;
    }

    public void setMirrorDisk(BigDecimal _mirrordisk) {
        mirrordisk = _mirrordisk;
    }

    public BigDecimal getIdleDisk() {
        return idledisk;
    }

    public void setIdleDisk(BigDecimal _idledisk) {
        idledisk = _idledisk;
    }

    public BigDecimal getMemoryUsed() {
        return memoryused;
    }

    public void setMemoryUsed(BigDecimal _memoryused) {
        memoryused = _memoryused;
    }

    public BigDecimal getCpuUsed() {
        return cpuused;
    }

    public void setCpuUsed(BigDecimal _cpuused) {
        cpuused = _cpuused;
    }

    public BigDecimal getNetReceive() {
        return receive;
    }

    public void setNetReceive(BigDecimal _receive) {
        receive = _receive;
    }

    public BigDecimal getNetSend() {
        return send;
    }

    public void setNetSend(BigDecimal _send) {
        send = _send;
    }

    public String getUWSIP() {
        return uwsip;
    }

    public void setUWSIP(String _uwsip) {
        uwsip = _uwsip;
    }

    public String getUWSName() {
        return uwsname;
    }

    public void setUWSName(String _uwsname) {
        uwsname = _uwsname;
    }
}
