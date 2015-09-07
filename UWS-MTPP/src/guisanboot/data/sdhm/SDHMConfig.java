/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;

/**
 *
 * @author Administrator
 */
public class SDHMConfig {

    public static final String MONITOR_UWS = "monitor_uws";
    public static final String HOST_PORT = "hostport";
    public static final String GET_DATA_TIME = "getdata_time";
    public static final String REP_SAVE_DIR = "rep_save_dir";
    public static final String HOST_IP = "hostip";
    public static final String THR_CPU = "cused_value";
    public static final String THR_DISK = "dused_value";
    public static final String THR_MEMORY = "mused_value";
    public static final String THR_DISKIO = "dio_value";
    public static final String THR_NETIO = "nio_value";
    public static final String INTERVAL = "intervalmin";
    private String monitor_uws;
    private String hostport;
    private String getdata_time;
    private String rep_save_dir;
    private String hostip;
    private String thrcpu;
    private String thrdisk;
    private String thrmemory;
    private String thrdiskio;
    private String thrnetio;
    private String interval;

    /** Creates a new instance of SDHMConfig */
    public SDHMConfig() {
    }

    public String getMonitorUWS() {
        return monitor_uws;
    }

    public void setMonitorUWS(String _monitor_uws) {
        monitor_uws = _monitor_uws;
    }

    public String getHostport() {
        return hostport;
    }

    public void setHostport(String _hostport) {
        hostport = _hostport;
    }

    public String getGetdataTime() {
        return getdata_time;
    }

    public void setGetdataTime(String _getdata_time) {
        getdata_time = _getdata_time;
    }

    public String getRepSaveDir() {
        return rep_save_dir;
    }

    public void setRepSaveDir(String _rep_save_dir) {
        rep_save_dir = _rep_save_dir;
    }

    public String getHostip() {
        return hostip;
    }

    public void setHostip(String _hostip) {
        hostip = _hostip;
    }

    public String getThrcpu() {
        return thrcpu;
    }

    public void setThrcpu(String _thrcpu) {
        thrcpu = _thrcpu;
    }

    public String getThrdisk() {
        return thrdisk;
    }

    public void setThrdisk(String _thrdisk) {
        thrdisk = _thrdisk;
    }

    public String getThrmemory() {
        return thrmemory;
    }

    public void setThrmemory(String _thrmemory) {
        thrmemory = _thrmemory;
    }

    public String getThrdiskio() {
        return thrdiskio;
    }

    public void setThrdiskio(String _thrdiskio) {
        thrdiskio = _thrdiskio;
    }

    public String getThrnetio() {
        return thrnetio;
    }

    public void setThrnetio(String _thrnetio) {
        thrnetio = _thrnetio;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String _interval) {
        interval = _interval;
    }
}
