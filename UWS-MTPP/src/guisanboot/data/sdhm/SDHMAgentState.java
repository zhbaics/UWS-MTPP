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
public class SDHMAgentState {

    public static final String AGENT_HOST_NAME = "hostname";
    public static final String AGENT_HOST_MACHINE = "hostmachine";
    public static final String AGENT_HOST_OS = "hostos";
    public static final String AGENT_HOST_MAC = "hostmac";
    public static final String AGENT_MEMORY_USED = "memoryused";
    public static final String AGENT_DISK_READ_IO = "diskreadio";
    public static final String AGENT_DISK_WRITE_IO = "diskwriteio";
    public static final String AGENT_NET_READ_IO = "netreadio";
    public static final String AGENT_NET_WRITE_IO = "netwriteio";
    public static final String AGENT_TOTAL_DISK = "totaldisk";
    public static final String AGENT_FREE_DISK = "freedisk";
    public static final String AGENT_MAX_SNAP = "maxsnap";
    public static final String AGENT_USED_SNAP = "usedsnap";
    private String hostname = "";
    private String hostmachine = "";
    private String hostos = "";
    private String hostmac = "";
    private BigDecimal memoryused = BigDecimal.ZERO;
    private BigDecimal diskreadio = BigDecimal.ZERO;
    private BigDecimal diskwriteio = BigDecimal.ZERO;
    private BigDecimal netreadio = BigDecimal.ZERO;
    private BigDecimal netwriteio = BigDecimal.ZERO;
    private BigDecimal totaldisk = BigDecimal.ZERO;
    private BigDecimal freedisk = BigDecimal.ZERO;
    private BigDecimal maxsnap = BigDecimal.ZERO;
    private BigDecimal usedsnap = BigDecimal.ZERO;

    public BigDecimal getMaxsnap() {
        return maxsnap;
    }

    public void setMaxsnap(BigDecimal maxsnap) {
        this.maxsnap = maxsnap;
    }

    public BigDecimal getUsedsnap() {
        return usedsnap;
    }

    public void setUsedsnap(BigDecimal usedsnap) {
        this.usedsnap = usedsnap;
    }


    /** Creates a new instance of SDHMAgentState */
    public SDHMAgentState() {
    }

    public BigDecimal getDiskreadio() {
        return diskreadio;
    }

    public void setDiskreadio(BigDecimal diskreadio) {
        this.diskreadio = diskreadio;
    }

    public BigDecimal getDiskwriteio() {
        return diskwriteio;
    }

    public void setDiskwriteio(BigDecimal diskwriteio) {
        this.diskwriteio = diskwriteio;
    }

    public BigDecimal getFreedisk() {
        return freedisk;
    }

    public void setFreedisk(BigDecimal freedisk) {
        this.freedisk = freedisk;
    }

    public String getHostmac() {
        return hostmac;
    }

    public void setHostmac(String hostmac) {
        this.hostmac = hostmac;
    }

    public String getHostmachine() {
        return hostmachine;
    }

    public void setHostmachine(String hostmachine) {
        this.hostmachine = hostmachine;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostos() {
        return hostos;
    }

    public void setHostos(String hostos) {
        this.hostos = hostos;
    }

    public BigDecimal getMemoryused() {
        return memoryused;
    }

    public void setMemoryused(BigDecimal memoryused) {
        this.memoryused = memoryused;
    }

    public BigDecimal getNetreadio() {
        return netreadio;
    }

    public void setNetreadio(BigDecimal netreadio) {
        this.netreadio = netreadio;
    }

    public BigDecimal getNetwriteio() {
        return netwriteio;
    }

    public void setNetwriteio(BigDecimal netwriteio) {
        this.netwriteio = netwriteio;
    }

    public BigDecimal getTotaldisk() {
        return totaldisk;
    }

    public void setTotaldisk(BigDecimal totaldisk) {
        this.totaldisk = totaldisk;
    }
}
