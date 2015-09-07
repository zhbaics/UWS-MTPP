/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.ui.sdhm;

import guisanboot.data.sdhm.GetSDHMReport;
import guisanboot.data.sdhm.SDHMReport;
import javax.swing.*;

import guisanboot.res.*;
import guisanboot.ui.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Vector;
import jxl.*;
import jxl.write.*;

/**
 *
 * @author Administrator
 */
public class SDHMReportGetter extends BasicGetSomethingThread {

    private static final String SHEET_BASE_INFO = "BaseInfo";
    private static final String SHEET_NORMAL_INFO = "NormalInfo";
    private static final String SHEET_TASK_INFO = "TaskInfo";
    private static final String SHEET_NO_TASK_INFO = "NoTaskInfo";
    private static final String SHEET_ERROR_INFO = "ErrorInfo";
    private static final String SHEET_REPORT = "Report";
    private SDHMReportDialog diag;
    private SanBootView view;
    private String ip;
    private String starttime;
    private String endtime;
    private Object lock = new Object();
    private boolean overed = false;
    private int getCount;
    File targetFile;
    private int retcode;
    public boolean errorFlag = false;

    /** Creates a new instance of SDHMReportGetter */
    public SDHMReportGetter(
            SanBootView _view,
            SDHMReportDialog _diag,
            String _ip,
            String _starttime,
            String _endtime,
            File _targetFile) {
        view = _view;
        diag = _diag;
        ip = _ip;
        starttime = _starttime;
        endtime = _endtime;
        targetFile = _targetFile;
    }
    Runnable setEnable = new Runnable() {

        public void run() {
            diag.setEnable(true);
        }
    };
    Runnable setDisable = new Runnable() {

        public void run() {
            diag.setEnable(false);
        }
    };
    Runnable generateReportRun = new Runnable() {

        public void run() {
            try {
                GetSDHMReport getSDHMReportNormal = new GetSDHMReport(
                        view,
                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_REPORT) + ip + " " + starttime + " " + endtime + " all");
                getSDHMReportNormal.run();
                retcode = getSDHMReportNormal.getRetCode();
                if (retcode != 0) {
                    errorFlag = true;
                    errMsg = getSDHMReportNormal.getErrMsg();
                    SanBootView.log.error(getClass().getName(), " sdhmReportError: " + errMsg);
                    setOver(true);
                    return;
                }


                GetSDHMReport getSDHMReportTask = new GetSDHMReport(
                        view,
                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_REPORT) + ip + " " + starttime + " " + endtime + " task");
                getSDHMReportTask.run();
                retcode = getSDHMReportTask.getRetCode();
                if (retcode != 0) {
                    errorFlag = true;
                    errMsg = getSDHMReportTask.getErrMsg();
                    SanBootView.log.error(getClass().getName(), " sdhmReportError: " + errMsg);
                    setOver(true);
                    return;
                }


                GetSDHMReport getSDHMReportNoTask = new GetSDHMReport(
                        view,
                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_REPORT) + ip + " " + starttime + " " + endtime + " notask");
                getSDHMReportNoTask.run();
                retcode = getSDHMReportNoTask.getRetCode();
                if (retcode != 0) {
                    errorFlag = true;
                    errMsg = getSDHMReportNoTask.getErrMsg();
                    SanBootView.log.error(getClass().getName(), " sdhmReportError: " + errMsg);
                    setOver(true);
                    return;
                }

                GetSDHMReport getSDHMReportError = new GetSDHMReport(
                        view,
                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_REPORT) + ip + " " + starttime + " " + endtime + " error");
                getSDHMReportError.run();
                retcode = getSDHMReportError.getRetCode();
                if (retcode != 0) {
                    errorFlag = true;
                    errMsg = getSDHMReportError.getErrMsg();
                    SanBootView.log.error(getClass().getName(), " sdhmReportError: " + errMsg);
                    setOver(true);
                    return;
                }

                SDHMReport sdhmReportNormal = getSDHMReportNormal.SDHMReport();
                SDHMReport sdhmReportTask = getSDHMReportTask.SDHMReport();
                SDHMReport sdhmReportNoTask = getSDHMReportNoTask.SDHMReport();
                SDHMReport sdhmReportError = getSDHMReportError.SDHMReport();

                String templateName = SanBootView.res.getString("SDHMReportDialog.templateName");
                String realpath = InitApp.getUserWorkDir() + File.separator + "template" + File.separator + templateName;
                File f = new File(realpath);
                Workbook wb = Workbook.getWorkbook(f);
                WritableWorkbook wwb = Workbook.createWorkbook(targetFile, wb);
                WritableSheet wwsBaseInfo = wwb.getSheet(SHEET_BASE_INFO);
                WritableSheet wwsNormalInfo = wwb.getSheet(SHEET_NORMAL_INFO);
                WritableSheet wwsTaskInfo = wwb.getSheet(SHEET_TASK_INFO);
                WritableSheet wwsNoTaskInfo = wwb.getSheet(SHEET_NO_TASK_INFO);
                WritableSheet wwsErrorInfo = wwb.getSheet(SHEET_ERROR_INFO);
                WritableSheet wwsReport = wwb.getSheet(SHEET_REPORT);

                //ErrorInfo Sheet
                Vector errorCreateTimeVector = sdhmReportError.getErrorCreateTime();
                for (int i = 0; i < errorCreateTimeVector.size(); i++) {
                    if (i < 26) {
                        Label errorCreateTimeLabel = (Label) wwsErrorInfo.getWritableCell(1, i + 1);
                        errorCreateTimeLabel.setString(formatString(errorCreateTimeVector.get(i).toString()));
                    } else {
                        Label errorCreateTimeLabel = new Label(1, i + 1, formatString(errorCreateTimeVector.get(i).toString()));
                        wwsErrorInfo.addCell(errorCreateTimeLabel);
                    }
                }
                //Report Sheet 
                SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat reportDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Label reportHostIPLabel = (Label) wwsReport.getWritableCell(1, 1);
                reportHostIPLabel.setString(ip);
                Label reportStartTime = (Label) wwsReport.getWritableCell(1, 2);
                reportStartTime.setString(reportDateFormat.format(inDateFormat.parse(starttime)));
                Label reportEndTime = (Label) wwsReport.getWritableCell(1, 3);
                reportEndTime.setString(reportDateFormat.format(inDateFormat.parse(endtime)));

                //BaseInfo Sheet
                Vector hostIPVector = sdhmReportNormal.getHostip();
                for (int i = 0; i < hostIPVector.size(); i++) {
                    Label hostIPLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 1);
                    hostIPLabel.setString(hostIPVector.get(i).toString());
                }

                Vector hostNameVector = sdhmReportNormal.getHostname();
                for (int i = 0; i < hostNameVector.size(); i++) {
                    Label hostNameLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 2);
                    hostNameLabel.setString(hostNameVector.get(i).toString());
                }

                Vector osVector = sdhmReportNormal.getOs();
                for (int i = 0; i < osVector.size(); i++) {
                    Label osLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 3);
                    osLabel.setString(osVector.get(i).toString());
                }

                Vector osVersionVector = sdhmReportNormal.getOsversion();
                for (int i = 0; i < osVersionVector.size(); i++) {
                    Label osVersionLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 4);
                    osVersionLabel.setString(osVersionVector.get(i).toString());
                }

                Vector diskLabelVector = sdhmReportNormal.getDisklabel();
                for (int i = 0; i < diskLabelVector.size(); i++) {
                    Label diskLabelLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 5);
                    diskLabelLabel.setString(diskLabelVector.get(i).toString());
                }

                Vector diskSizeVector = sdhmReportNormal.getDisksize();
                for (int i = 0; i < diskSizeVector.size(); i++) {
                    Label diskSizeLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 6);
                    diskSizeLabel.setString(diskSizeVector.get(i).toString());
                }

                Vector partitionLabelVector = sdhmReportNormal.getPartitionlabel();
                for (int i = 0; i < partitionLabelVector.size(); i++) {
                    Label partitionLabelLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 7);
                    partitionLabelLabel.setString(partitionLabelVector.get(i).toString());
                }

                Vector partitionSizeVector = sdhmReportNormal.getPartitionsize();
                for (int i = 0; i < partitionSizeVector.size(); i++) {
                    Label partitionSizeLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 8);
                    partitionSizeLabel.setString(partitionSizeVector.get(i).toString());
                }

                Vector cpuNameVector = sdhmReportNormal.getCpuname();
                for (int i = 0; i < cpuNameVector.size(); i++) {
                    Label cpuNameLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 9);
                    cpuNameLabel.setString(cpuNameVector.get(i).toString());
                }

                Vector cpuDeviceIDVector = sdhmReportNormal.getCpudeviceid();
                for (int i = 0; i < cpuDeviceIDVector.size(); i++) {
                    Label cpuDeviceIDLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 10);
                    cpuDeviceIDLabel.setString(cpuDeviceIDVector.get(i).toString());
                }

                Vector cpuSocketDesignationVector = sdhmReportNormal.getCpusocketdesignation();
                for (int i = 0; i < cpuSocketDesignationVector.size(); i++) {
                    Label cpuSocketDesignationLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 11);
                    cpuSocketDesignationLabel.setString(cpuSocketDesignationVector.get(i).toString());
                }

                Vector cpuCurrentClockSpeedVector = sdhmReportNormal.getCpucurrentclockspeed();
                for (int i = 0; i < cpuCurrentClockSpeedVector.size(); i++) {
                    Label cpuCurrentClockSpeedLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 12);
                    cpuCurrentClockSpeedLabel.setString(cpuCurrentClockSpeedVector.get(i).toString());
                }

                Vector cpuL2CacheSizeVector = sdhmReportNormal.getCpul2cachesize();
                for (int i = 0; i < cpuL2CacheSizeVector.size(); i++) {
                    Label cpuL2CacheSizeLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 13);
                    cpuL2CacheSizeLabel.setString(cpuL2CacheSizeVector.get(i).toString());
                }

                Vector memoryDeviceIDVector = sdhmReportNormal.getMemorydeviceid();
                for (int i = 0; i < memoryDeviceIDVector.size(); i++) {
                    Label memoryDeviceIDLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 14);
                    memoryDeviceIDLabel.setString(memoryDeviceIDVector.get(i).toString());
                }

                Vector memoryCapacityVector = sdhmReportNormal.getMemorycapacity();
                for (int i = 0; i < memoryCapacityVector.size(); i++) {
                    Label memoryCapacityLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 15);
                    memoryCapacityLabel.setString(memoryCapacityVector.get(i).toString());
                }

                Vector adapterMacVector = sdhmReportNormal.getAdaptermac();
                for (int i = 0; i < adapterMacVector.size(); i++) {
                    Label adapterMacLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 16);
                    adapterMacLabel.setString(adapterMacVector.get(i).toString());
                }

                Vector adapterNameVector = sdhmReportNormal.getAdaptername();
                for (int i = 0; i < adapterNameVector.size(); i++) {
                    Label adapterNameLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 17);
                    adapterNameLabel.setString(adapterNameVector.get(i).toString());
                }

                Vector adapterNetConnectionStatusVector = sdhmReportNormal.getAdapternetconnectionstatus();
                for (int i = 0; i < adapterNetConnectionStatusVector.size(); i++) {
                    Label adapterNetConnectionStatusLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 18);
                    adapterNetConnectionStatusLabel.setString(adapterNetConnectionStatusVector.get(i).toString());
                }

                Vector adapterManufacturerVector = sdhmReportNormal.getAdaptermanufacturer();
                for (int i = 0; i < adapterManufacturerVector.size(); i++) {
                    Label adapterManufacturerLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 19);
                    adapterManufacturerLabel.setString(adapterManufacturerVector.get(i).toString());
                }

                Vector videoDeviceIDVector = sdhmReportNormal.getVideodeviceid();
                for (int i = 0; i < videoDeviceIDVector.size(); i++) {
                    Label videoDeviceIDLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 20);
                    videoDeviceIDLabel.setString(videoDeviceIDVector.get(i).toString());
                }

                Vector videoNameVector = sdhmReportNormal.getVideoname();
                for (int i = 0; i < videoNameVector.size(); i++) {
                    Label videoNameLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 21);
                    videoNameLabel.setString(videoNameVector.get(i).toString());
                }

                Vector boardManufacturerVector = sdhmReportNormal.getBoardmanufacturer();
                for (int i = 0; i < boardManufacturerVector.size(); i++) {
                    Label boardManufacturerLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 22);
                    boardManufacturerLabel.setString(boardManufacturerVector.get(i).toString());
                }

                Vector boardProductVector = sdhmReportNormal.getBoardproduct();
                for (int i = 0; i < boardProductVector.size(); i++) {
                    Label boardProductLabel = (Label) wwsBaseInfo.getWritableCell(i + 1, 23);
                    boardProductLabel.setString(boardProductVector.get(i).toString());
                }

                for (int sheetindex = 0; sheetindex < 3; sheetindex++) {
                    SDHMReport sdhmReport;
                    WritableSheet wwsInfo;
                    if (sheetindex == 0) {
                        sdhmReport = sdhmReportNormal;
                        wwsInfo = wwsNormalInfo;
                    } else if (sheetindex == 1) {
                        sdhmReport = sdhmReportTask;
                        wwsInfo = wwsTaskInfo;
                    } else {
                        sdhmReport = sdhmReportNoTask;
                        wwsInfo = wwsNoTaskInfo;
                    }
                    //Info Sheet
                    Vector thrMemoryUsedVector = sdhmReport.getThrmemoryused();
                    for (int i = 0; i < thrMemoryUsedVector.size(); i++) {
                        jxl.write.Number thrMemoryUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(1, 2);
                        double doubleValue = stringToDouble(thrMemoryUsedVector.get(i).toString());
                        thrMemoryUsedLabel.setValue(doubleValue);
                    }

                    Vector avgMemoryUsedVector = sdhmReport.getAvgmemoryused();
                    for (int i = 0; i < avgMemoryUsedVector.size(); i++) {
                        jxl.write.Number avgMemoryUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(1, 3);
                        double doubleValue = stringToDouble(avgMemoryUsedVector.get(i).toString());
                        avgMemoryUsedLabel.setValue(doubleValue);
                    }

                    Vector maxMemoryUsedVector = sdhmReport.getMaxmemoryused();
                    for (int i = 0; i < maxMemoryUsedVector.size(); i++) {
                        jxl.write.Number maxMemoryUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(1, 4);
                        double doubleValue = stringToDouble(maxMemoryUsedVector.get(i).toString());
                        maxMemoryUsedLabel.setValue(doubleValue);
                    }

                    Vector minMemoryUsedVector = sdhmReport.getMinmemoryused();
                    for (int i = 0; i < minMemoryUsedVector.size(); i++) {
                        jxl.write.Number mixMemoryUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(1, 5);
                        double doubleValue = stringToDouble(minMemoryUsedVector.get(i).toString());
                        mixMemoryUsedLabel.setValue(doubleValue);
                    }

                    Vector createTimeMemoryUsedVector = sdhmReport.getCreatetimememoryused();
                    Label timesMemoryUsedLabel = (Label) wwsInfo.getWritableCell(1, 6);
                    timesMemoryUsedLabel.setString(String.valueOf(createTimeMemoryUsedVector.size()));
                    for (int i = 0; i < createTimeMemoryUsedVector.size(); i++) {
                        if (i < 20) {
                            Label createTimeMemoryUsedLabel = (Label) wwsInfo.getWritableCell(1, 7 + i);
                            createTimeMemoryUsedLabel.setString(formatString(createTimeMemoryUsedVector.get(i).toString()));
                        } else {
                            Label createTimeMemoryUsedLabel = new Label(1, 7 + i, formatString(createTimeMemoryUsedVector.get(i).toString()));
                            wwsInfo.addCell(createTimeMemoryUsedLabel);
                        }
                    }

                    Vector thrCpuUsedVector = sdhmReport.getThrcpuused();
                    for (int i = 0; i < thrCpuUsedVector.size(); i++) {
                        jxl.write.Number thrCpuUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(2, 2);
                        double doubleValue = stringToDouble(thrCpuUsedVector.get(i).toString());
                        thrCpuUsedLabel.setValue(doubleValue);
                    }

                    Vector avgCpuUsedVector = sdhmReport.getAvgcpuused();
                    for (int i = 0; i < avgCpuUsedVector.size(); i++) {
                        jxl.write.Number avgCpuUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(2, 3);
                        double doubleValue = stringToDouble(avgCpuUsedVector.get(i).toString());
                        avgCpuUsedLabel.setValue(doubleValue);
                    }

                    Vector maxCpuUsedVector = sdhmReport.getMaxcpuused();
                    for (int i = 0; i < maxCpuUsedVector.size(); i++) {
                        jxl.write.Number maxCpuUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(2, 4);
                        double doubleValue = stringToDouble(maxCpuUsedVector.get(i).toString());
                        maxCpuUsedLabel.setValue(doubleValue);
                    }

                    Vector minCpuUsedVector = sdhmReport.getMincpuused();
                    for (int i = 0; i < minCpuUsedVector.size(); i++) {
                        jxl.write.Number mixCpuUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(2, 5);
                        double doubleValue = stringToDouble(minCpuUsedVector.get(i).toString());
                        mixCpuUsedLabel.setValue(doubleValue);
                    }

                    Vector createTimeCpuUsedVector = sdhmReport.getCreatetimecpuused();
                    Label timesCpuUsedLabel = (Label) wwsInfo.getWritableCell(2, 6);
                    timesCpuUsedLabel.setString(String.valueOf(createTimeCpuUsedVector.size()));
                    for (int i = 0; i < createTimeCpuUsedVector.size(); i++) {
                        if (i < 20) {
                            Label createTimeCpuUsedLabel = (Label) wwsInfo.getWritableCell(2, 7 + i);
                            createTimeCpuUsedLabel.setString(formatString(createTimeCpuUsedVector.get(i).toString()));
                        } else {
                            Label createTimeCpuUsedLabel = new Label(2, 7 + i, formatString(createTimeCpuUsedVector.get(i).toString()));
                            wwsInfo.addCell(createTimeCpuUsedLabel);
                        }
                    }

                    Vector thrDiskUsedVector = sdhmReport.getThrdiskused();
                    for (int i = 0; i < thrDiskUsedVector.size(); i++) {
                        jxl.write.Number thrDiskUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(3, 2);
                        double doubleValue = stringToDouble(thrDiskUsedVector.get(i).toString());
                        thrDiskUsedLabel.setValue(doubleValue);
                    }

                    Vector avgDiskUsedVector = sdhmReport.getAvgdiskused();
                    for (int i = 0; i < avgDiskUsedVector.size(); i++) {
                        jxl.write.Number avgDiskUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(3, 3);
                        double doubleValue = stringToDouble(avgDiskUsedVector.get(i).toString());
                        avgDiskUsedLabel.setValue(doubleValue);
                    }

                    Vector maxDiskUsedVector = sdhmReport.getMaxdiskused();
                    for (int i = 0; i < maxDiskUsedVector.size(); i++) {
                        jxl.write.Number maxDiskUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(3, 4);
                        double doubleValue = stringToDouble(maxDiskUsedVector.get(i).toString());
                        maxDiskUsedLabel.setValue(doubleValue);
                    }

                    Vector minDiskUsedVector = sdhmReport.getMindiskused();
                    for (int i = 0; i < minDiskUsedVector.size(); i++) {
                        jxl.write.Number mixDiskUsedLabel = (jxl.write.Number) wwsInfo.getWritableCell(3, 5);
                        double doubleValue = stringToDouble(minDiskUsedVector.get(i).toString());
                        mixDiskUsedLabel.setValue(doubleValue);
                    }

                    Vector createTimeDiskUsedVector = sdhmReport.getCreatetimediskused();
                    Label timesDiskUsedLabel = (Label) wwsInfo.getWritableCell(3, 6);
                    timesDiskUsedLabel.setString(String.valueOf(createTimeDiskUsedVector.size()));
                    for (int i = 0; i < createTimeDiskUsedVector.size(); i++) {
                        if (i < 20) {
                            Label createTimeDiskUsedLabel = (Label) wwsInfo.getWritableCell(3, 7 + i);
                            createTimeDiskUsedLabel.setString(formatString(createTimeDiskUsedVector.get(i).toString()));
                        } else {
                            Label createTimeDiskUsedLabel = new Label(3, 7 + i, formatString(createTimeDiskUsedVector.get(i).toString()));
                            wwsInfo.addCell(createTimeDiskUsedLabel);
                        }
                    }

                    Vector thrDiskIOVector = sdhmReport.getThrdiskio();
                    for (int i = 0; i < thrDiskIOVector.size(); i++) {
                        jxl.write.Number thrDiskIOLabel = (jxl.write.Number) wwsInfo.getWritableCell(4, 2);
                        double doubleValue = stringToDouble(thrDiskIOVector.get(i).toString());
                        thrDiskIOLabel.setValue(doubleValue);
                    }

                    Vector avgDiskIOVector = sdhmReport.getAvgdiskio();
                    for (int i = 0; i < avgDiskIOVector.size(); i++) {
                        jxl.write.Number avgDiskIOLabel = (jxl.write.Number) wwsInfo.getWritableCell(4, 3);
                        double doubleValue = stringToDouble(avgDiskIOVector.get(i).toString());
                        avgDiskIOLabel.setValue(doubleValue);
                    }

                    Vector maxDiskIOVector = sdhmReport.getMaxdiskio();
                    for (int i = 0; i < maxDiskIOVector.size(); i++) {
                        jxl.write.Number maxDiskIOLabel = (jxl.write.Number) wwsInfo.getWritableCell(4, 4);
                        double doubleValue = stringToDouble(maxDiskIOVector.get(i).toString());
                        maxDiskIOLabel.setValue(doubleValue);
                    }

                    Vector minDiskIOVector = sdhmReport.getMindiskio();
                    for (int i = 0; i < minDiskIOVector.size(); i++) {
                        jxl.write.Number mixDiskIOLabel = (jxl.write.Number) wwsInfo.getWritableCell(4, 5);
                        double doubleValue = stringToDouble(minDiskIOVector.get(i).toString());
                        mixDiskIOLabel.setValue(doubleValue);
                    }

                    Vector createTimeDiskIOVector = sdhmReport.getCreatetimediskio();
                    Label timesDiskIOLabel = (Label) wwsInfo.getWritableCell(4, 6);
                    timesDiskIOLabel.setString(String.valueOf(createTimeDiskIOVector.size()));
                    for (int i = 0; i < createTimeDiskIOVector.size(); i++) {
                        if (i < 20) {
                            Label createTimeDiskIOLabel = (Label) wwsInfo.getWritableCell(4, 7 + i);
                            createTimeDiskIOLabel.setString(formatString(createTimeDiskIOVector.get(i).toString()));
                        } else {
                            Label createTimeDiskIOLabel = new Label(4, 7 + i, formatString(createTimeDiskIOVector.get(i).toString()));
                            wwsInfo.addCell(createTimeDiskIOLabel);
                        }
                    }

                    Vector thrNetIOVector = sdhmReport.getThrnetio();
                    for (int i = 0; i < thrNetIOVector.size(); i++) {
                        jxl.write.Number thrNetIOLabel = (jxl.write.Number) wwsInfo.getWritableCell(5, 2);
                        double doubleValue = stringToDouble(thrNetIOVector.get(i).toString());
                        thrNetIOLabel.setValue(doubleValue);
                    }

                    Vector avgNetIOVector = sdhmReport.getAvgnetio();
                    for (int i = 0; i < avgNetIOVector.size(); i++) {
                        jxl.write.Number avgNetIOLabel = (jxl.write.Number) wwsInfo.getWritableCell(5, 3);
                        double doubleValue = stringToDouble(avgNetIOVector.get(i).toString());
                        avgNetIOLabel.setValue(doubleValue);
                    }

                    Vector maxNetIOVector = sdhmReport.getMaxnetio();
                    for (int i = 0; i < maxNetIOVector.size(); i++) {
                        jxl.write.Number maxNetIOLabel = (jxl.write.Number) wwsInfo.getWritableCell(5, 4);
                        double doubleValue = stringToDouble(maxNetIOVector.get(i).toString());
                        maxNetIOLabel.setValue(doubleValue);
                    }

                    Vector minNetIOVector = sdhmReport.getMinnetio();
                    for (int i = 0; i < minNetIOVector.size(); i++) {
                        jxl.write.Number mixNetIOLabel = (jxl.write.Number) wwsInfo.getWritableCell(5, 5);
                        double doubleValue = stringToDouble(minNetIOVector.get(i).toString());
                        mixNetIOLabel.setValue(doubleValue);
                    }

                    Vector createTimeNetIOVector = sdhmReport.getCreatetimenetio();
                    Label timesNetIOLabel = (Label) wwsInfo.getWritableCell(5, 6);
                    timesNetIOLabel.setString(String.valueOf(createTimeNetIOVector.size()));
                    for (int i = 0; i < createTimeNetIOVector.size(); i++) {
                        if (i < 20) {
                            Label createTimeNetIOLabel = (Label) wwsInfo.getWritableCell(5, 7 + i);
                            createTimeNetIOLabel.setString(formatString(createTimeNetIOVector.get(i).toString()));
                        } else {
                            Label createTimeNetIOLabel = new Label(5, 7 + i, formatString(createTimeNetIOVector.get(i).toString()));
                            wwsInfo.addCell(createTimeNetIOLabel);
                        }
                    }

                    if (sheetindex == 0) {
                        wwsNormalInfo = wwsInfo;
                    } else if (sheetindex == 1) {
                        wwsTaskInfo = wwsInfo;
                    } else {
                        wwsNoTaskInfo = wwsInfo;
                    }
                }

                wwb.write();
                wwb.close();
                wb.close();

                setOver(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmReportError: " + ex.getMessage());
                errorFlag = true;
                errMsg = ex.getMessage();
                setOver(true);
                if (targetFile.exists()) {
                    targetFile.delete();
                }
            }
        }
    };

    public boolean realRun() {
        Thread getInfoThread = new Thread(generateReportRun);
        getInfoThread.start();
        while (!isOver()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmReportError: " + e.getMessage());
                errorFlag = true;
                errMsg = e.getMessage();
            }
        }
        try {
            SwingUtilities.invokeAndWait(setEnable);
        } catch (Exception ex) {
            ex.printStackTrace();
            SanBootView.log.error(getClass().getName(), " sdhmReportError: " + ex.getMessage());
            errorFlag = true;
            errMsg = ex.getMessage();
        }
        return true;
    }

    boolean isOver() {
        synchronized (lock) {
            return overed;
        }
    }

    void setOver(boolean val) {
        synchronized (lock) {
            overed = val;
        }
    }

    public int getTskLogCount() {
        return getCount;
    }

    public boolean getRetVal() {
        return !errorFlag;
    }

    public String getErrorMsg() {
        return errMsg;
    }

    public String formatString(String s) {
        SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat outDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss#");
        String value = "";
        int index = s.indexOf(":");
        if (index > 0) {
            String tempdate = s.substring(0, index).trim();
            try {
                value = outDateFormat.format(inDateFormat.parse(tempdate)) + s.substring(index + 1).trim();
            } catch (Exception ex) {
                ex.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmReportError: " + ex.getMessage());
                errorFlag = true;
                errMsg = ex.getMessage();
            }
        } else {
            value = s;
        }
        return value;
    }

    public String formatErrorMessage(String s) {
        SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat outDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        String value = "";
        int index = s.indexOf(":");
        if (index > 0) {
            String tempdate = s.substring(0, index).trim();
            String actionValue = "";
            String actionString = s.substring(index + 1).trim();
            int indexAction = actionString.indexOf(":");
            if (indexAction > 0) {
                actionValue = "action:" + actionString.substring(0, indexAction).trim() + " status:" + actionString.substring(indexAction + 1).trim();
            } else {
                actionValue = actionString;
            }

            try {
                value = outDateFormat.format(inDateFormat.parse(tempdate)) + actionValue;
            } catch (Exception ex) {
                ex.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmReportError: " + ex.getMessage());
                errorFlag = true;
                errMsg = ex.getMessage();
            }
        } else {
            value = s;
        }
        return value;
    }

    public double stringToDouble(String s) {
        double value = 0;
        try {
            value = Double.parseDouble(s);
        } catch (Exception ex) {
            value = 0;
        }
        return value;
    }
}
