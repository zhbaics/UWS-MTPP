/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;

import guisanboot.data.NetworkRunning;
import javax.swing.table.*;
import java.io.IOException;
import guisanboot.ui.*;

/**
 *
 * @author Administrator
 */
public class GetSDHMReport extends NetworkRunning {

    private SDHMReport sdhmReport;
    private DefaultTableModel model;
    private SanBootView view;
    private int count;

    /** Creates a new instance of GetSDHMReport */
    public GetSDHMReport(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetSDHMReport(String cmd) {
        super(cmd);
    }

    public SDHMReport SDHMReport() {
        //test data
//        sdhmReport.addHostip("198.1.1.1");
//        sdhmReport.addHostname("mzw");
//        sdhmReport.addOs("Microsoft Windows XP Professional");
//        sdhmReport.addOsversion("Service Pack 2");
//        sdhmReport.addDisklabel("PHYSICALDRIVE0");
//        sdhmReport.addDisksize("74 GB");
//        sdhmReport.addPartitionlabel("C");
//        sdhmReport.addPartitionsize("45 GB");
//        sdhmReport.addPartitionlabel("D");
//        sdhmReport.addPartitionsize("29 GB");
//        sdhmReport.addCpuname("Intel(R) Atom(TM) CPU  330   @ 1.60GHz");
//        sdhmReport.addCpudeviceid("CPU0");
//        sdhmReport.addCpusocketdesignation("U1PR");
//        sdhmReport.addCpucurrentclockspeed("1596 MHZ");
//        sdhmReport.addCpul2cachesize("512 Kb_L2");
//        sdhmReport.addCpuname("Intel(R) Atom(TM) CPU  330   @ 1.60GHz");
//        sdhmReport.addCpudeviceid("CPU1");
//        sdhmReport.addCpusocketdesignation("U1PR");
//        sdhmReport.addCpucurrentclockspeed("1596 MHZ");
//        sdhmReport.addCpul2cachesize("512 Kb_L2");
//        sdhmReport.addCpuname("Intel(R) Atom(TM) CPU  330   @ 1.60GHz");
//        sdhmReport.addCpudeviceid("CPU2");
//        sdhmReport.addCpusocketdesignation("U1PR");
//        sdhmReport.addCpucurrentclockspeed("1596 MHZ");
//        sdhmReport.addCpul2cachesize("512 Kb_L2");
//        sdhmReport.addCpuname("Intel(R) Atom(TM) CPU  330   @ 1.60GHz");
//        sdhmReport.addCpudeviceid("CPU3");
//        sdhmReport.addCpusocketdesignation("U1PR");
//        sdhmReport.addCpucurrentclockspeed("1596 MHZ");
//        sdhmReport.addCpul2cachesize("512 Kb_L2");
//        sdhmReport.addMemorydeviceid("Physical Memory 0");
//        sdhmReport.addMemorycapacity("1024 MB");
//        sdhmReport.addAdaptermac("00:1C:C0:C2:26:A2");
//        sdhmReport.addAdaptername("Realtek RTL8168C(P)/8111C(P) PCI-E Gigabit Ethernet NIC");
//        sdhmReport.addAdapternetconnectionstatus("Connected");
//        sdhmReport.addAdaptermanufacturer("Realtek Semiconductor Corp.");
//        sdhmReport.addVideodeviceid("VideoController1");
//        sdhmReport.addVideoname("Intel(R) 82945G Express Chipset Family");
//        sdhmReport.addBoardmanufacturer("Intel Corporation");
//        sdhmReport.addBoardproduct("D945GCLF2D");
//        
//        sdhmReport.addThrmemoryused("70%");
//        sdhmReport.addAvgmemoryused("60%");
//        sdhmReport.addMaxmemoryused("80%");
//        sdhmReport.addMinmemoryused("50%");
//        sdhmReport.addCreatetimememoryused("20090814161006");
//        sdhmReport.addCreatetimememoryused("20090819161123");
//        sdhmReport.addCreatetimememoryused("20090901161239");
//        sdhmReport.addCreatetimememoryused("20090914161357");
//        sdhmReport.addCreatetimememoryused("20090814161006");
//        sdhmReport.addCreatetimememoryused("20090819161123");
//        sdhmReport.addCreatetimememoryused("20090901161239");
//        sdhmReport.addCreatetimememoryused("20090914161357");
//        sdhmReport.addThrcpuused("80%");
//        sdhmReport.addAvgcpuused("60%");
//        sdhmReport.addMaxcpuused("70%");
//        sdhmReport.addMincpuused("50%");
////        sdhmReport.addCreatetimecpuused("");
//        sdhmReport.addThrnetio("9999 B/S");
//        sdhmReport.addAvgnetio("0 B/S");
//        sdhmReport.addMaxnetio("0 B/S");
//        sdhmReport.addMinnetio("0 B/S");
////        sdhmReport.addCreatetimenetio("");
//        sdhmReport.addThrdiskio("200000 B/S");
//        sdhmReport.addAvgdiskio("130999.0 B/S");
//        sdhmReport.addMaxdiskio("429782 B/S");
//        sdhmReport.addMindiskio("82361 B/S");
//        sdhmReport.addCreatetimediskio("20090814161006");
//        sdhmReport.addCreatetimediskio("20090819161123");
//        sdhmReport.addCreatetimediskio("20090901161239");
//        sdhmReport.addCreatetimediskio("20090914161357");
//        sdhmReport.addCreatetimediskio("20090814161006");
//        sdhmReport.addCreatetimediskio("20090819161123");
//        sdhmReport.addCreatetimediskio("20090901161239");
//        sdhmReport.addCreatetimediskio("20090914161357");
//        sdhmReport.addThrdiskused("90%");
//        sdhmReport.addAvgdiskused("80%");
//        sdhmReport.addMaxdiskused("97%");
//        sdhmReport.addMindiskused("75%");
//        sdhmReport.addCreatetimediskused("20090814161006");
//        sdhmReport.addCreatetimediskused("20090819161123");
//        sdhmReport.addCreatetimediskused("20090901161239");
//        sdhmReport.addCreatetimediskused("20090914161357");
//        sdhmReport.addCreatetimediskused("20090814161006");
//        sdhmReport.addCreatetimediskused("20090819161123");
//        sdhmReport.addCreatetimediskused("20090901161239");
//        sdhmReport.addCreatetimediskused("20090914161357");

        //test data
        return sdhmReport;
    }

    public void parser(String line) {
        if (sdhmReport == null) {
            sdhmReport = new SDHMReport();
        }
        String s1 = line.trim();

        SanBootView.log.debug(getClass().getName(), "output:" + s1);

        int index = s1.indexOf("=");

        if (index > 0) {
            String value = s1.substring(index + 1).trim();

            if (s1.startsWith(SDHMReport.HOST_IP)) {
                sdhmReport.addHostip(value);
            } else if (s1.startsWith(SDHMReport.HOST_NAME)) {
                sdhmReport.addHostname(value);
            } else if (s1.startsWith(SDHMReport.OS)) {
                sdhmReport.addOs(value);
            } else if (s1.startsWith(SDHMReport.OS_VERSION)) {
                sdhmReport.addOsversion(value);
            } else if (s1.startsWith(SDHMReport.DISK_LABEL)) {
                sdhmReport.addDisklabel(value);
            } else if (s1.startsWith(SDHMReport.DISK_SIZE)) {
                sdhmReport.addDisksize(value);
            } else if (s1.startsWith(SDHMReport.PARTITION_LABEL)) {
                sdhmReport.addPartitionlabel(value);
            } else if (s1.startsWith(SDHMReport.PARITION_SIZE)) {
                sdhmReport.addPartitionsize(value);
            } else if (s1.startsWith(SDHMReport.CPU_NAME)) {
                sdhmReport.addCpuname(value);
            } else if (s1.startsWith(SDHMReport.CPU_DIVICE_ID)) {
                sdhmReport.addCpudeviceid(value);
            } else if (s1.startsWith(SDHMReport.CPU_SOCKET_DESIGNATION)) {
                sdhmReport.addCpusocketdesignation(value);
            } else if (s1.startsWith(SDHMReport.CPU_CURRENT_CLOCK_SPEED)) {
                sdhmReport.addCpucurrentclockspeed(value);
            } else if (s1.startsWith(SDHMReport.CPU_L2_CACHE_SIZE)) {
                sdhmReport.addCpul2cachesize(value);
            } else if (s1.startsWith(SDHMReport.MEMORY_DEVICE_ID)) {
                sdhmReport.addMemorydeviceid(value);
            } else if (s1.startsWith(SDHMReport.MEMORY_CAPACITY)) {
                sdhmReport.addMemorycapacity(value);
            } else if (s1.startsWith(SDHMReport.ADAPTER_MAC)) {
                sdhmReport.addAdaptermac(value);
            } else if (s1.startsWith(SDHMReport.ADAPTER_NAME)) {
                sdhmReport.addAdaptername(value);
            } else if (s1.startsWith(SDHMReport.ADAPTER_CONNECTION_STATUS)) {
                sdhmReport.addAdapternetconnectionstatus(value);
            } else if (s1.startsWith(SDHMReport.ADAPTER_MANUFACTURER)) {
                sdhmReport.addAdaptermanufacturer(value);
            } else if (s1.startsWith(SDHMReport.VIDEO_DEVICE_ID)) {
                sdhmReport.addVideodeviceid(value);
            } else if (s1.startsWith(SDHMReport.VIDEO_NAME)) {
                sdhmReport.addVideoname(value);
            } else if (s1.startsWith(SDHMReport.BOARD_MANUFACTURER)) {
                sdhmReport.addBoardmanufacturer(value);
            } else if (s1.startsWith(SDHMReport.BOARD_PRODUCT)) {
                sdhmReport.addBoardproduct(value);
            } else if (s1.startsWith(SDHMReport.THR_MEMORY_USED)) {
                sdhmReport.addThrmemoryused(value);
            } else if (s1.startsWith(SDHMReport.AVG_MEMORY_USED)) {
                sdhmReport.addAvgmemoryused(value);
            } else if (s1.startsWith(SDHMReport.MAX_MEMORY_USED)) {
                sdhmReport.addMaxmemoryused(value);
            } else if (s1.startsWith(SDHMReport.MIN_MEMORY_USED)) {
                sdhmReport.addMinmemoryused(value);
            } else if (s1.startsWith(SDHMReport.CREATETIME_MEMORY_USED)) {
                sdhmReport.addCreatetimememoryused(value);
            } else if (s1.startsWith(SDHMReport.THR_CPU_USED)) {
                sdhmReport.addThrcpuused(value);
            } else if (s1.startsWith(SDHMReport.AVG_CPU_USED)) {
                sdhmReport.addAvgcpuused(value);
            } else if (s1.startsWith(SDHMReport.MAX_CPU_USED)) {
                sdhmReport.addMaxcpuused(value);
            } else if (s1.startsWith(SDHMReport.MIN_CPU_USED)) {
                sdhmReport.addMincpuused(value);
            } else if (s1.startsWith(SDHMReport.CREATETIME_CPU_USED)) {
                sdhmReport.addCreatetimecpuused(value);
            } else if (s1.startsWith(SDHMReport.THR_NET_IO)) {
                sdhmReport.addThrnetio(value);
            } else if (s1.startsWith(SDHMReport.AVG_NET_IO)) {
                sdhmReport.addAvgnetio(value);
            } else if (s1.startsWith(SDHMReport.MAX_NET_IO)) {
                sdhmReport.addMaxnetio(value);
            } else if (s1.startsWith(SDHMReport.MIN_NET_IO)) {
                sdhmReport.addMinnetio(value);
            } else if (s1.startsWith(SDHMReport.CREATETIME_NET_IO)) {
                sdhmReport.addCreatetimenetio(value);
            } else if (s1.startsWith(SDHMReport.THR_DISK_IO)) {
                sdhmReport.addThrdiskio(value);
            } else if (s1.startsWith(SDHMReport.AVG_DISK_IO)) {
                sdhmReport.addAvgdiskio(value);
            } else if (s1.startsWith(SDHMReport.MAX_DISK_IO)) {
                sdhmReport.addMaxdiskio(value);
            } else if (s1.startsWith(SDHMReport.MIN_DISK_IO)) {
                sdhmReport.addMindiskio(value);
            } else if (s1.startsWith(SDHMReport.CREATETIME_DISK_IO)) {
                sdhmReport.addCreatetimediskio(value);
            } else if (s1.startsWith(SDHMReport.THR_DISK_USED)) {
                sdhmReport.addThrdiskused(value);
            } else if (s1.startsWith(SDHMReport.AVG_DISK_USED)) {
                sdhmReport.addAvgdiskused(value);
            } else if (s1.startsWith(SDHMReport.MAX_DISK_USED)) {
                sdhmReport.addMaxdiskused(value);
            } else if (s1.startsWith(SDHMReport.MIN_DISK_USED)) {
                sdhmReport.addMindiskused(value);
            } else if (s1.startsWith(SDHMReport.CREATETIME_DISK_USED)) {
                sdhmReport.addCreatetimediskused(value);
            } else if (s1.startsWith(SDHMReport.ERROR_CREATE_TIME)) {
                sdhmReport.addErrorCreateTime(value);
            }
        } else {
        }
    }
}
