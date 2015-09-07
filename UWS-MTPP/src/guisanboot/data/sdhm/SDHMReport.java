/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;


import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class SDHMReport {
    //system info

    public static final String HOST_IP = "hostip";
    public static final String HOST_NAME = "hostname";
    public static final String OS = "os";
    public static final String OS_VERSION = "version";
    public static final String DISK_LABEL = "disklable";
    public static final String DISK_SIZE = "disksize";
    public static final String PARTITION_LABEL = "partitionlable";
    public static final String PARITION_SIZE = "partitionsize";
    public static final String CPU_NAME = "cpuname";
    public static final String CPU_DIVICE_ID = "cpudeviceid";
    public static final String CPU_SOCKET_DESIGNATION = "cpusocketdesignation";
    public static final String CPU_CURRENT_CLOCK_SPEED = "cpucurrentclockspeed";
    public static final String CPU_L2_CACHE_SIZE = "cpul2cachesize";
    public static final String MEMORY_DEVICE_ID = "memorydeviceid";
    public static final String MEMORY_CAPACITY = "memorycapacity";
    public static final String ADAPTER_MAC = "adaptermac";
    public static final String ADAPTER_NAME = "adaptername";
    public static final String ADAPTER_CONNECTION_STATUS = "adapternetconnectionstatus";
    public static final String ADAPTER_MANUFACTURER = "adaptermanufacturer";
    public static final String VIDEO_DEVICE_ID = "videodeviceid";
    public static final String VIDEO_NAME = "videoname";
    public static final String BOARD_MANUFACTURER = "boardmanufacturer";
    public static final String BOARD_PRODUCT = "boardproduct";
    //
    public static final String THR_MEMORY_USED = "THR(memoryused)";
    public static final String AVG_MEMORY_USED = "AVG(memoryused)";
    public static final String MAX_MEMORY_USED = "MAX(memoryused)";
    public static final String MIN_MEMORY_USED = "MIN(memoryused)";
    public static final String CREATETIME_MEMORY_USED = "Memorycreatetime";
    public static final String THR_CPU_USED = "THR(cpuused)";
    public static final String AVG_CPU_USED = "AVG(cpuused)";
    public static final String MAX_CPU_USED = "MAX(cpuused)";
    public static final String MIN_CPU_USED = "MIN(cpuused)";
    public static final String CREATETIME_CPU_USED = "Cpucreatetime";
    public static final String THR_NET_IO = "THR(totalnetio)";
    public static final String AVG_NET_IO = "AVG(totalnetio)";
    public static final String MAX_NET_IO = "MAX(totalnetio)";
    public static final String MIN_NET_IO = "MIN(totalnetio)";
    public static final String CREATETIME_NET_IO = "NetIOcreatetime";
    public static final String THR_DISK_IO = "THR(totaldiskio)";
    public static final String AVG_DISK_IO = "AVG(totaldiskio)";
    public static final String MAX_DISK_IO = "MAX(totaldiskio)";
    public static final String MIN_DISK_IO = "MIN(totaldiskio)";
    public static final String CREATETIME_DISK_IO = "DiskIOcreatetime";
    public static final String THR_DISK_USED = "THR(totaldiskused)";
    public static final String AVG_DISK_USED = "AVG(totaldiskused)";
    public static final String MAX_DISK_USED = "MAX(totaldiskused)";
    public static final String MIN_DISK_USED = "MIN(totaldiskused)";
    public static final String CREATETIME_DISK_USED = "Diskcreatetime";
    public static final String ERROR_CREATE_TIME = "ErrorCreateTime";
    private Vector hostip = new Vector();
    private Vector hostname = new Vector();
    private Vector os = new Vector();
    private Vector osversion = new Vector();
    private Vector disklabel = new Vector();
    private Vector disksize = new Vector();
    private Vector partitionlabel = new Vector();
    private Vector partitionsize = new Vector();
    private Vector cpuname = new Vector();
    private Vector cpudeviceid = new Vector();
    private Vector cpusocketdesignation = new Vector();
    private Vector cpucurrentclockspeed = new Vector();
    private Vector cpul2cachesize = new Vector();
    private Vector memorydeviceid = new Vector();
    private Vector memorycapacity = new Vector();
    private Vector adaptermac = new Vector();
    private Vector adaptername = new Vector();
    private Vector adapternetconnectionstatus = new Vector();
    private Vector adaptermanufacturer = new Vector();
    private Vector videodeviceid = new Vector();
    private Vector videoname = new Vector();
    private Vector boardmanufacturer = new Vector();
    private Vector boardproduct = new Vector();
    private Vector thr_memoryused = new Vector();
    private Vector avg_memoryused = new Vector();
    private Vector max_memoryused = new Vector();
    private Vector min_memoryused = new Vector();
    private Vector createtime_memoryused = new Vector();
    private Vector thr_cpuused = new Vector();
    private Vector avg_cpuused = new Vector();
    private Vector max_cpuused = new Vector();
    private Vector min_cpuused = new Vector();
    private Vector createtime_cpuused = new Vector();
    private Vector thr_netio = new Vector();
    private Vector avg_netio = new Vector();
    private Vector max_netio = new Vector();
    private Vector min_netio = new Vector();
    private Vector createtime_netio = new Vector();
    private Vector thr_diskio = new Vector();
    private Vector avg_diskio = new Vector();
    private Vector max_diskio = new Vector();
    private Vector min_diskio = new Vector();
    private Vector createtime_diskio = new Vector();
    private Vector thr_diskused = new Vector();
    private Vector avg_diskused = new Vector();
    private Vector max_diskused = new Vector();
    private Vector min_diskused = new Vector();
    private Vector createtime_diskused = new Vector();
    private Vector errorcreate_time = new Vector();

    /** Creates a new instance of SDHMReport */
    public SDHMReport() {
    }

    public Vector getHostip() {
        return hostip;
    }

    public void addHostip(String _hostip) {
        hostip.add(_hostip);
    }

    public Vector getHostname() {
        return hostname;
    }

    public void addHostname(String _hostname) {
        hostname.add(_hostname);
    }

    public Vector getOs() {
        return os;
    }

    public void addOs(String _os) {
        os.add(_os);
    }

    public Vector getOsversion() {
        return osversion;
    }

    public void addOsversion(String _osversion) {
        osversion.add(_osversion);
    }

    public Vector getDisklabel() {
        return disklabel;
    }

    public void addDisklabel(String _disklabel) {
        disklabel.add(_disklabel);
    }

    public Vector getDisksize() {
        return disksize;
    }

    public void addDisksize(String _disksize) {
        disksize.add(_disksize);
    }

    public Vector getPartitionlabel() {
        return partitionlabel;
    }

    public void addPartitionlabel(String _partitionlabel) {
        partitionlabel.add(_partitionlabel);
    }

    public Vector getPartitionsize() {
        return partitionsize;
    }

    public void addPartitionsize(String _partitionsize) {
        partitionsize.add(_partitionsize);
    }

    public Vector getCpuname() {
        return cpuname;
    }

    public void addCpuname(String _cpuname) {
        cpuname.add(_cpuname);
    }

    public Vector getCpudeviceid() {
        return cpudeviceid;
    }

    public void addCpudeviceid(String _cpudeviceid) {
        cpudeviceid.add(_cpudeviceid);
    }

    public Vector getCpusocketdesignation() {
        return cpusocketdesignation;
    }

    public void addCpusocketdesignation(String _cpusocketdesignation) {
        cpusocketdesignation.add(_cpusocketdesignation);
    }

    public Vector getCpucurrentclockspeed() {
        return cpucurrentclockspeed;
    }

    public void addCpucurrentclockspeed(String _cpucurrentclockspeed) {
        cpucurrentclockspeed.add(_cpucurrentclockspeed);
    }

    public Vector getCpul2cachesize() {
        return cpul2cachesize;
    }

    public void addCpul2cachesize(String _cpul2cachesize) {
        cpul2cachesize.add(_cpul2cachesize);
    }

    public Vector getMemorydeviceid() {
        return memorydeviceid;
    }

    public void addMemorydeviceid(String _memorydeviceid) {
        memorydeviceid.add(_memorydeviceid);
    }

    public Vector getMemorycapacity() {
        return memorycapacity;
    }

    public void addMemorycapacity(String _memorycapacity) {
        memorycapacity.add(_memorycapacity);
    }

    public Vector getAdaptermac() {
        return adaptermac;
    }

    public void addAdaptermac(String _adaptermac) {
        adaptermac.add(_adaptermac);
    }

    public Vector getAdaptername() {
        return adaptername;
    }

    public void addAdaptername(String _adaptername) {
        adaptername.add(_adaptername);
    }

    public Vector getAdapternetconnectionstatus() {
        return adapternetconnectionstatus;
    }

    public void addAdapternetconnectionstatus(String _adapternetconnectionstatus) {
        adapternetconnectionstatus.add(_adapternetconnectionstatus);
    }

    public Vector getAdaptermanufacturer() {
        return adaptermanufacturer;
    }

    public void addAdaptermanufacturer(String _adaptermanufacturer) {
        adaptermanufacturer.add(_adaptermanufacturer);
    }

    public Vector getVideodeviceid() {
        return videodeviceid;
    }

    public void addVideodeviceid(String _videodeviceid) {
        videodeviceid.add(_videodeviceid);
    }

    public Vector getVideoname() {
        return videoname;
    }

    public void addVideoname(String _videoname) {
        videoname.add(_videoname);
    }

    public Vector getBoardmanufacturer() {
        return boardmanufacturer;
    }

    public void addBoardmanufacturer(String _boardmanufacturer) {
        boardmanufacturer.add(_boardmanufacturer);
    }

    public Vector getBoardproduct() {
        return boardproduct;
    }

    public void addBoardproduct(String _boardproduct) {
        boardproduct.add(_boardproduct);
    }

    public Vector getThrmemoryused() {
        return thr_memoryused;
    }

    public void addThrmemoryused(String _thr_memoryused) {
        thr_memoryused.add(_thr_memoryused);
    }

    public Vector getAvgmemoryused() {
        return avg_memoryused;
    }

    public void addAvgmemoryused(String _avg_memoryused) {
        avg_memoryused.add(_avg_memoryused);
    }

    public Vector getMaxmemoryused() {
        return max_memoryused;
    }

    public void addMaxmemoryused(String _max_memoryused) {
        max_memoryused.add(_max_memoryused);
    }

    public Vector getMinmemoryused() {
        return min_memoryused;
    }

    public void addMinmemoryused(String _min_memoryused) {
        min_memoryused.add(_min_memoryused);
    }

    public Vector getCreatetimememoryused() {
        return createtime_memoryused;
    }

    public void addCreatetimememoryused(String _createtime_memoryused) {
        createtime_memoryused.add(_createtime_memoryused);
    }

    public Vector getThrcpuused() {
        return thr_cpuused;
    }

    public void addThrcpuused(String _thr_cpuused) {
        thr_cpuused.add(_thr_cpuused);
    }

    public Vector getAvgcpuused() {
        return avg_cpuused;
    }

    public void addAvgcpuused(String _avg_cpuused) {
        avg_cpuused.add(_avg_cpuused);
    }

    public Vector getMaxcpuused() {
        return max_cpuused;
    }

    public void addMaxcpuused(String _max_cpuused) {
        max_cpuused.add(_max_cpuused);
    }

    public Vector getMincpuused() {
        return min_cpuused;
    }

    public void addMincpuused(String _min_cpuused) {
        min_cpuused.add(_min_cpuused);
    }

    public Vector getCreatetimecpuused() {
        return createtime_cpuused;
    }

    public void addCreatetimecpuused(String _createtime_cpuused) {
        createtime_cpuused.add(_createtime_cpuused);
    }

    public Vector getThrnetio() {
        return thr_netio;
    }

    public void addThrnetio(String _thr_netio) {
        thr_netio.add(_thr_netio);
    }

    public Vector getAvgnetio() {
        return avg_netio;
    }

    public void addAvgnetio(String _avg_netio) {
        avg_netio.add(_avg_netio);
    }

    public Vector getMaxnetio() {
        return max_netio;
    }

    public void addMaxnetio(String _max_netio) {
        max_netio.add(_max_netio);
    }

    public Vector getMinnetio() {
        return min_netio;
    }

    public void addMinnetio(String _min_netio) {
        min_netio.add(_min_netio);
    }

    public Vector getCreatetimenetio() {
        return createtime_netio;
    }

    public void addCreatetimenetio(String _createtime_netio) {
        createtime_netio.add(_createtime_netio);
    }

    public Vector getThrdiskio() {
        return thr_diskio;
    }

    public void addThrdiskio(String _thr_diskio) {
        thr_diskio.add(_thr_diskio);
    }

    public Vector getAvgdiskio() {
        return avg_diskio;
    }

    public void addAvgdiskio(String _avg_diskio) {
        avg_diskio.add(_avg_diskio);
    }

    public Vector getMaxdiskio() {
        return max_diskio;
    }

    public void addMaxdiskio(String _max_diskio) {
        max_diskio.add(_max_diskio);
    }

    public Vector getMindiskio() {
        return min_diskio;
    }

    public void addMindiskio(String _min_diskio) {
        min_diskio.add(_min_diskio);
    }

    public Vector getCreatetimediskio() {
        return createtime_diskio;
    }

    public void addCreatetimediskio(String _createtime_diskio) {
        createtime_diskio.add(_createtime_diskio);
    }

    public Vector getThrdiskused() {
        return thr_diskused;
    }

    public void addThrdiskused(String _thr_diskused) {
        thr_diskused.add(_thr_diskused);
    }

    public Vector getAvgdiskused() {
        return avg_diskused;
    }

    public void addAvgdiskused(String _avg_diskused) {
        avg_diskused.add(_avg_diskused);
    }

    public Vector getMaxdiskused() {
        return max_diskused;
    }

    public void addMaxdiskused(String _max_diskused) {
        max_diskused.add(_max_diskused);
    }

    public Vector getMindiskused() {
        return min_diskused;
    }

    public void addMindiskused(String _min_diskused) {
        min_diskused.add(_min_diskused);
    }

    public Vector getCreatetimediskused() {
        return createtime_diskused;
    }

    public void addCreatetimediskused(String _createtime_diskused) {
        createtime_diskused.add(_createtime_diskused);
    }

    public Vector getErrorCreateTime() {
        return errorcreate_time;
    }

    public void addErrorCreateTime(String _errorcreate_time) {
        errorcreate_time.add(_errorcreate_time);
    }
}
