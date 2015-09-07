/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.ui.sdhm;

import guisanboot.data.sdhm.AgentJobInfo;
import guisanboot.data.sdhm.GetAgentJobInfo;
import guisanboot.data.sdhm.GetAgentMonitorInfo;
import guisanboot.data.sdhm.GetMirrorJobInfo;
import guisanboot.data.sdhm.GetSDHMDiskInfoList;
import guisanboot.data.sdhm.GetSDHMUWSHostList;
import guisanboot.data.sdhm.GetSDHMUWSState;
import guisanboot.data.sdhm.GetAgentState;
import guisanboot.data.sdhm.GetAgentVolumeList;
import guisanboot.data.sdhm.SDHMAgentState;
import guisanboot.data.sdhm.SDHMUWSState;
import guisanboot.data.sdhm.GetMirrorRelation;
import guisanboot.data.sdhm.MirrorJobInfo;
import guisanboot.data.sdhm.MirrorRelation;
import javax.swing.*;

import guisanboot.res.*;
import guisanboot.ui.*;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import jxl.*;
import jxl.write.*;

/**
 *
 * @author Administrator
 */
public class SDHMMirrorReportGetter extends BasicGetSomethingThread {

    private static final String SHEET_HEADER = SanBootView.res.getString("SDHMMirrorReportDialog.templateHeaderSheetName");
    private static final String SHEET_SPACE_MONITOR = SanBootView.res.getString("SDHMMirrorReportDialog.templateSpaceMonitorSheetName");
    private static final String SHEET_STATE_MONITOR = SanBootView.res.getString("SDHMMirrorReportDialog.templateStateMonitorSheetName");
    private static final String SHEET_AGENT_MONITOR = SanBootView.res.getString("SDHMMirrorReportDialog.templateAgentMonitorSheetName");
    private static final String SHEET_AGENT_STATE_MONITOR = SanBootView.res.getString("SDHMMirrorReportDialog.templateAgentStateMonitorSheetName");
    private static final String SHEET_SCHEDULE_MONITOR = SanBootView.res.getString("SDHMMirrorReportDialog.templateScheduleMonitorSheetName");
    private static final String SHEET_TRANSFER_MONITOR = SanBootView.res.getString("SDHMMirrorReportDialog.templateTransferMonitorSheetName");
    private static final String SHEET_RECOVERY_MONITOR = SanBootView.res.getString("SDHMMirrorReportDialog.templateRecoveryMonitorSheetName");
    private static final String IP_ALL = SanBootView.res.getString("SDHMMirrorReportDialog.text.all");
    private BigDecimal byteToGB = new BigDecimal(1024 * 1024 * 1024);
    private BigDecimal byteToMB = new BigDecimal(1024 * 1024);
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
    private SanBootView view;
    private Vector agentList;
    private Vector mirrorUWSList;
    private SDHMUWSState sdhmUWSState; //磁盘总体信息
    private Vector sdhmUWSDiskTime; //磁盘信息采集时间
    private Vector sdhmUWSAllDisk; //整体磁盘空间变化
    private Vector sdhmUWSVisualDisk; //磁盘已分配空间变化
    private Vector sdhmUWSFreeDisk; //磁盘空闲空间变化
    private Vector sdhmUWSLocalDisk; //本地UWS磁盘使用变化
    private Vector sdhmUWSMirrorDisk; //镜像UWS磁盘使用变化
    private Vector sdhmUWSIdleDisk; //空闲卷磁盘空间变化
    private Vector sdhmUWSTransferTime; //UWS远程传输信息采集时间
    private Vector sdhmUWSTransferRead; //UWS远程传输读信息
    private Vector sdhmUWSTransferWrite; //UWS远程传输写信息
    private MirrorRelation mirrorRelation; //UWS远程镜像关系
    private MirrorJobInfo mirrorJobInfoSummary; //UWS远程镜像汇总
    private MirrorJobInfo mirrorJobInfoDetail; //UWS远程镜像详细
    private MirrorJobInfo mirrorJobInfoRead; //UWS远程镜像读IO
    private MirrorJobInfo mirrorJobInfoWrite; //UWS远程镜像写IO
    private ArrayList mirrorUWSTimeList = new ArrayList(); //根据mirrorUWSIP的mirrorUWS磁盘变化(1...n) 时间采集点列表
    private ArrayList mirrorUWSSizeList = new ArrayList(); //根据mirrorUWSIP的mirrorUWS磁盘变化(1...n) 使用空间列表
    private ArrayList mirrorUWSSnapList = new ArrayList(); //根据mirrorUWSIP的mirrorUWS快照变化(1...n) 快照数量列表
    private ArrayList agentTimeList = new ArrayList(); //根据agentIP的Agent磁盘变化(1...n) agent时间采集点列表
    private ArrayList agentDiskList = new ArrayList(); //根据agentIP的Agent磁盘变化(1...n) agent使用空间列表
    private ArrayList agentUWSDiskList = new ArrayList(); //根据agentIP的Agent磁盘变化(1...n) 对应UWS网盘使用空间列表
    private ArrayList agentMirrorDiskList = new ArrayList(); //根据agentIP的Agent磁盘变化(1...n) 对应镜像服务器使用空间列表
    private boolean spaceMonitorFlag;
    private boolean agentMonitorFlag;
    private boolean scheduleMonitorFlag;
//    private boolean transferMonitorFlag;
    private boolean recoveryMonitorFlag;
    private String agentIP;
//    private String mirrorUWSIP;
    private String starttime;
    private String endtime;
    private Object lock = new Object();
    private boolean overed = false;
    private int getCount;
    File targetFile;
    private int retcode;
    public boolean errorFlag = false;

    /** Creates a new instance of SDHMMirrorReportGetter */
    public SDHMMirrorReportGetter(
            SanBootView _view,
            Vector _agentList,
            Vector _mirrorUWSList,
            boolean _spaceMonitorFlag,
            boolean _agentMonitorFlag,
            boolean _scheduleMonitorFlag,
            boolean _recoveryMonitorFlag,
            String _agentIP,
            //        String _mirrorUWSIP,
            String _starttime,
            String _endtime,
            File _targetFile) {
        view = _view;
        agentList = _agentList;
        mirrorUWSList = _mirrorUWSList;
        spaceMonitorFlag = _spaceMonitorFlag;
        agentMonitorFlag = _agentMonitorFlag;
        scheduleMonitorFlag = _scheduleMonitorFlag;
//        transferMonitorFlag = _transferMonitorFlag;
        recoveryMonitorFlag = _recoveryMonitorFlag;
        agentIP = _agentIP;
//        mirrorUWSIP = _mirrorUWSIP;
        starttime = _starttime;
        endtime = _endtime;
        targetFile = _targetFile;
    }
    Runnable setEnable = new Runnable() {

        public void run() {
        }
    };
    Runnable setDisable = new Runnable() {

        public void run() {
        }
    };
    Runnable generateReportRun = new Runnable() {

        public void run() {
            try {
                //获取磁盘当前状态信息
                GetSDHMUWSState getSDHMUWSState = new GetSDHMUWSState(
                        view,
                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_STATE));
                getSDHMUWSState.run();
                retcode = getSDHMUWSState.getRetCode();
                if (retcode != 0) {
                    errorFlag = true;
                    errMsg = getSDHMUWSState.getErrMsg();
                    SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                    setOver(true);
                    return;
                }
                sdhmUWSState = getSDHMUWSState.SDHMUWSState(); //磁盘当前状态

                if (spaceMonitorFlag) {

                    //获取整体磁盘变化信息
                    GetSDHMDiskInfoList getSDHMAllDisk = new GetSDHMDiskInfoList(
                            view,
                            ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_ALLDISK) + starttime + " " + endtime);
                    getSDHMAllDisk.run();
                    retcode = getSDHMAllDisk.getRetCode();
                    if (retcode != 0) {
                        errorFlag = true;
                        errMsg = getSDHMAllDisk.getErrMsg();
                        SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                        setOver(true);
                        return;
                    }
                    sdhmUWSDiskTime = getSDHMAllDisk.SDHMUWSDiskTimeList();//磁盘信息采集时间
                    sdhmUWSAllDisk = getSDHMAllDisk.SDHMUWSDiskInfoList(); //整体磁盘量变化

                    //获取已分配磁盘变化信息
                    GetSDHMDiskInfoList getSDHMVisualDisk = new GetSDHMDiskInfoList(
                            view,
                            ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_VISUALDISK) + starttime + " " + endtime);
                    getSDHMVisualDisk.run();
                    retcode = getSDHMVisualDisk.getRetCode();
                    if (retcode != 0) {
                        errorFlag = true;
                        errMsg = getSDHMVisualDisk.getErrMsg();
                        SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                        setOver(true);
                        return;
                    }
                    sdhmUWSVisualDisk = getSDHMVisualDisk.SDHMUWSDiskInfoList(); //磁盘已分配空间变化

                    //获取空闲磁盘变化信息
                    GetSDHMDiskInfoList getSDHMFreeDisk = new GetSDHMDiskInfoList(
                            view,
                            ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_FREEDISK) + starttime + " " + endtime);
                    getSDHMFreeDisk.run();
                    retcode = getSDHMFreeDisk.getRetCode();
                    if (retcode != 0) {
                        errorFlag = true;
                        errMsg = getSDHMFreeDisk.getErrMsg();
                        SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                        setOver(true);
                        return;
                    }
                    sdhmUWSFreeDisk = getSDHMFreeDisk.SDHMUWSDiskInfoList(); //整体空闲空间变化

                    //获取本地UWS磁盘使用变化信息
                    GetSDHMDiskInfoList getSDHMLocalDisk = new GetSDHMDiskInfoList(
                            view,
                            ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_LOCALDISK) + starttime + " " + endtime);
                    getSDHMLocalDisk.run();
                    retcode = getSDHMLocalDisk.getRetCode();
                    if (retcode != 0) {
                        errorFlag = true;
                        errMsg = getSDHMLocalDisk.getErrMsg();
                        SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                        setOver(true);
                        return;
                    }
                    sdhmUWSLocalDisk = getSDHMLocalDisk.SDHMUWSDiskInfoList(); //本地UWS磁盘使用变化

                    //获取远程镜像UWS磁盘使用变化信息
                    GetSDHMDiskInfoList getSDHMMirrorDisk = new GetSDHMDiskInfoList(
                            view,
                            ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_MIRRORDISK) + starttime + " " + endtime);
                    getSDHMMirrorDisk.run();
                    retcode = getSDHMMirrorDisk.getRetCode();
                    if (retcode != 0) {
                        errorFlag = true;
                        errMsg = getSDHMMirrorDisk.getErrMsg();
                        SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                        setOver(true);
                        return;
                    }
                    sdhmUWSMirrorDisk = getSDHMMirrorDisk.SDHMUWSDiskInfoList(); //镜像UWS磁盘使用变化

                    //获取空闲卷磁盘变化信息
                    GetSDHMDiskInfoList getSDHMIdleDisk = new GetSDHMDiskInfoList(
                            view,
                            ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_IDLEDISK) + starttime + " " + endtime);
                    getSDHMIdleDisk.run();
                    retcode = getSDHMIdleDisk.getRetCode();
                    if (retcode != 0) {
                        errorFlag = true;
                        errMsg = getSDHMIdleDisk.getErrMsg();
                        SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                        setOver(true);
                        return;
                    }
                    sdhmUWSIdleDisk = getSDHMIdleDisk.SDHMUWSDiskInfoList(); //空闲卷磁盘空间变化

                    //获取MirrorUWS变化信息

                    if (mirrorUWSList == null) {
                        GetSDHMUWSHostList getSDHMUWSHostList = new GetSDHMUWSHostList(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_HOST_LIST));
                        getSDHMUWSHostList.run();
                        retcode = getSDHMUWSHostList.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getSDHMUWSHostList.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        mirrorUWSList = getSDHMUWSHostList.SDHMUWSHostList();
                    }
                    for (int i = 0; (mirrorUWSList != null) && (i < mirrorUWSList.size()); i++) {
                        String mirrorUWSIPTemp = mirrorUWSList.get(i).toString();

                        //MirrorUWS磁盘变化信息
                        GetSDHMDiskInfoList getSDHMMirrorUWSSize = new GetSDHMDiskInfoList(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_MIRRORUWS_SIZE) + mirrorUWSIPTemp + " size " + starttime + " " + endtime);
                        getSDHMMirrorUWSSize.run();
                        retcode = getSDHMMirrorUWSSize.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getSDHMMirrorUWSSize.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        mirrorUWSTimeList.add(getSDHMMirrorUWSSize.SDHMUWSDiskTimeList());
                        mirrorUWSSizeList.add(getSDHMMirrorUWSSize.SDHMUWSDiskInfoList());

                        //MirrorUWS快照变化信息
                        GetSDHMDiskInfoList getSDHMMirrorUWSSnap = new GetSDHMDiskInfoList(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_MIRRORUWS_SNAP) + mirrorUWSIPTemp + " snap " + starttime + " " + endtime);
                        getSDHMMirrorUWSSnap.run();
                        retcode = getSDHMMirrorUWSSnap.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getSDHMMirrorUWSSnap.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        mirrorUWSSnapList.add(getSDHMMirrorUWSSnap.SDHMUWSDiskInfoList());

                    }
                }

                //获取Agent磁盘变化信息
//                //TODO
//                if(agentMonitorFlag && (agentIP!=null)){
//                    if(agentIP.equals(IP_ALL)){
//                        for(int k=0;k<agentList.size();k++){
//                            String agentIPTemp = agentList.get(k).toString();
//                            //获取当前agent磁盘总体信息
//                            GetAgentState getAgentState = new GetAgentState(
//                                view,
//                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_INFO) + agentIPTemp + " " + starttime + " " + endtime
//                            );
//                            getAgentState.run();
//                            retcode = getAgentState.getRetCode();
//                            if(retcode != 0) {
//                                errorFlag = true;
//                                errMsg = getAgentState.getErrMsg();
//                                SanBootView.log.error( getClass().getName(), " sdhmMirrorReportError: " +  errMsg );
//                                setOver(true);
//                                return;
//                            }
//                            //获取agent的卷列表
//                        }
//                    }else{
//                            GetSDHMDiskInfoList getSDHMAgentUWSDisk = new GetSDHMDiskInfoList(
//                                view,
//                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_AGENT_LOC) + agentIP + " " + starttime + " " + endtime
//                            );
//                            getSDHMAgentUWSDisk.run();
//                            retcode = getSDHMAgentUWSDisk.getRetCode();
//                            if(retcode != 0) {
//                                errorFlag = true;
//                                errMsg = getSDHMAgentUWSDisk.getErrMsg();
//                                SanBootView.log.error( getClass().getName(), " sdhmMirrorReportError: " +  errMsg );
//                                setOver(true);
//                                return;
//                            }
//                            //agent时间采集点列表
//                            agentTimeList.add(getSDHMAgentUWSDisk.SDHMUWSDiskTimeList());
//                            //对应UWS网盘使用空间列表
//                            agentUWSDiskList.add(getSDHMAgentUWSDisk.SDHMUWSDiskInfoList());
//                            //agent使用空间列表
//                            //agentDiskList
//                            //对应镜像UWS使用空间列表
//                            //agentMirrorDiskList
//                    }
//                }

                //获取UWS远程镜像调度信息
                //TODO
                if (scheduleMonitorFlag) {
                    //获取UWS远程镜像关系
                    GetMirrorRelation getMirrorRelation = new GetMirrorRelation(
                            view,
                            ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_MIRROR_RELATION));
                    getMirrorRelation.run();
                    retcode = getMirrorRelation.getRetCode();
                    if (retcode != 0) {
                        errorFlag = true;
                        errMsg = getMirrorRelation.getErrMsg();
                        SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                        setOver(true);
                        return;
                    }
                    mirrorRelation = getMirrorRelation.MirrorRelation();//UWS远程镜像关系

                    //获取远程镜像任务汇总信息
                    GetMirrorJobInfo getMirrorJobInfoSummary = new GetMirrorJobInfo(
                            view,
                            ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_MIRROR_JOB_SUMMARY) + starttime + " " + endtime);
                    getMirrorJobInfoSummary.run();
                    retcode = getMirrorJobInfoSummary.getRetCode();
                    if (retcode != 0) {
                        errorFlag = true;
                        errMsg = getMirrorJobInfoSummary.getErrMsg();
                        SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                        setOver(true);
                        return;
                    }
                    mirrorJobInfoSummary = getMirrorJobInfoSummary.MirrorJobInfo();//UWS远程镜像任务汇总信息

                    //获取远程镜像任务详细信息
                    GetMirrorJobInfo getMirrorJobInfoDetail = new GetMirrorJobInfo(
                            view,
                            ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_MIRROR_JOB_DETAIL) + starttime + " " + endtime);
                    getMirrorJobInfoDetail.run();
                    retcode = getMirrorJobInfoDetail.getRetCode();
                    if (retcode != 0) {
                        errorFlag = true;
                        errMsg = getMirrorJobInfoDetail.getErrMsg();
                        SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                        setOver(true);
                        return;
                    }
                    mirrorJobInfoDetail = getMirrorJobInfoDetail.MirrorJobInfo();//UWS远程镜像任务详细信息


                    //获取远程镜像任务读IO信息
                    GetMirrorJobInfo getMirrorJobInfoRead = new GetMirrorJobInfo(
                            view,
                            ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_MIRROR_JOB_READ) + starttime + " " + endtime);
                    getMirrorJobInfoRead.run();
                    retcode = getMirrorJobInfoRead.getRetCode();
                    if (retcode != 0) {
                        errorFlag = true;
                        errMsg = getMirrorJobInfoRead.getErrMsg();
                        SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                        setOver(true);
                        return;
                    }
                    mirrorJobInfoRead = getMirrorJobInfoRead.MirrorJobInfo();//UWS远程镜像任务读IO信息


                    //获取远程镜像任务写IO信息
                    GetMirrorJobInfo getMirrorJobInfoWrite = new GetMirrorJobInfo(
                            view,
                            ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_MIRROR_JOB_WRITE) + starttime + " " + endtime);
                    getMirrorJobInfoWrite.run();
                    retcode = getMirrorJobInfoWrite.getRetCode();
                    if (retcode != 0) {
                        errorFlag = true;
                        errMsg = getMirrorJobInfoWrite.getErrMsg();
                        SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                        setOver(true);
                        return;
                    }
                    mirrorJobInfoWrite = getMirrorJobInfoWrite.MirrorJobInfo();//UWS远程镜像任务写IO信息

                    //TODO
                    //获取UWS远程镜像读信息
//                    GetSDHMDiskInfoList getSDHMTransferRead = new GetSDHMDiskInfoList(
//                        view,
//                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_TRANSFER_READ) + starttime + " " + endtime
//                    );
//                    getSDHMTransferRead.run();
//                    retcode = getSDHMTransferRead.getRetCode();
//                    if(retcode != 0) {
//                        errorFlag = true;
//                        errMsg = getSDHMTransferRead.getErrMsg();
//                        SanBootView.log.error( getClass().getName(), " sdhmMirrorReportError: " +  errMsg );
//                        setOver(true);
//                        return;
//                    }
//                    sdhmUWSTransferTime = getSDHMTransferRead.SDHMUWSDiskTimeList();//信息采集时间
//                    sdhmUWSTransferRead = getSDHMTransferRead.SDHMUWSDiskInfoList(); //UWS远程镜像读变化
//
//                    //获取UWS远程镜像写信息
//                    GetSDHMDiskInfoList getSDHMTransferWrite = new GetSDHMDiskInfoList(
//                        view,
//                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_TRANSFER_WRITE) + starttime + " " + endtime
//                    );
//                    getSDHMTransferWrite.run();
//                    retcode = getSDHMTransferWrite.getRetCode();
//                    if(retcode != 0) {
//                        errorFlag = true;
//                        errMsg = getSDHMTransferWrite.getErrMsg();
//                        SanBootView.log.error( getClass().getName(), " sdhmMirrorReportError: " +  errMsg );
//                        setOver(true);
//                        return;
//                    }
//                    sdhmUWSTransferWrite = getSDHMTransferWrite.SDHMUWSDiskInfoList(); //UWS远程镜像写变化

                    //获取远程镜像统计信息


                }

                //获取UWS远程镜像传输信息
                //TODO
//                if(transferMonitorFlag){
//                    //获取UWS远程镜像读信息
//                    GetSDHMDiskInfoList getSDHMTransferRead = new GetSDHMDiskInfoList(
//                        view,
//                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_TRANSFER_READ) + starttime + " " + endtime
//                    );
//                    getSDHMTransferRead.run();
//                    retcode = getSDHMTransferRead.getRetCode();
//                    if(retcode != 0) {
//                        errorFlag = true;
//                        errMsg = getSDHMTransferRead.getErrMsg();
//                        SanBootView.log.error( getClass().getName(), " sdhmMirrorReportError: " +  errMsg );
//                        setOver(true);
//                        return;
//                    }
//                    sdhmUWSTransferTime = getSDHMTransferRead.SDHMUWSDiskTimeList();//信息采集时间
//                    sdhmUWSTransferRead = getSDHMTransferRead.SDHMUWSDiskInfoList(); //UWS远程镜像读变化
//
//                    //获取UWS远程镜像写信息
//                    GetSDHMDiskInfoList getSDHMTransferWrite = new GetSDHMDiskInfoList(
//                        view,
//                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_TRANSFER_WRITE) + starttime + " " + endtime
//                    );
//                    getSDHMTransferWrite.run();
//                    retcode = getSDHMTransferWrite.getRetCode();
//                    if(retcode != 0) {
//                        errorFlag = true;
//                        errMsg = getSDHMTransferWrite.getErrMsg();
//                        SanBootView.log.error( getClass().getName(), " sdhmMirrorReportError: " +  errMsg );
//                        setOver(true);
//                        return;
//                    }
//                    sdhmUWSTransferWrite = getSDHMTransferWrite.SDHMUWSDiskInfoList(); //UWS远程镜像写变化
//
//                }

                //获取UWS紧急恢复一览信息
                //TODO
                if (recoveryMonitorFlag) {
                }

                //TODO
                String templateName = SanBootView.res.getString("SDHMMirrorReportDialog.templateName");
                String headerSheetName = SanBootView.res.getString("SDHMMirrorReportDialog.headerSheetName");
                String spaceMonitorSheetName = SanBootView.res.getString("SDHMMirrorReportDialog.spaceMonitorSheetName");
                String stateMonitorSheetName = SanBootView.res.getString("SDHMMirrorReportDialog.stateMonitorSheetName");
                String agentMonitorSheetName = SanBootView.res.getString("SDHMMirrorReportDialog.agentMonitorSheetName");
                String agentStateMonitorSheetName = SanBootView.res.getString("SDHMMirrorReportDialog.agentStateMonitorSheetName");
                String scheduleMonitorSheetName = SanBootView.res.getString("SDHMMirrorReportDialog.scheduleMonitorSheetName");
                String transferMonitorSheetName = SanBootView.res.getString("SDHMMirrorReportDialog.transferMonitorSheetName");
                String recoveryMonitorSheetName = SanBootView.res.getString("SDHMMirrorReportDialog.recoveryMonitorSheetName");
                String realpath = InitApp.getUserWorkDir() + File.separator + "template" + File.separator + templateName;
                File templateFile = new File(realpath);
                Workbook templateWorkbook = Workbook.getWorkbook(templateFile);
                WritableWorkbook reportWritableWorkbook = Workbook.createWorkbook(targetFile, templateWorkbook);
                WritableSheet wwsHeaderTemplate = reportWritableWorkbook.getSheet(SHEET_HEADER);
                WritableSheet wwsSpaceMonitorTemplate = reportWritableWorkbook.getSheet(SHEET_SPACE_MONITOR);
                WritableSheet wwsStateMonitorTemplate = reportWritableWorkbook.getSheet(SHEET_STATE_MONITOR);
                WritableSheet wwsAgentMonitorTemplate = reportWritableWorkbook.getSheet(SHEET_AGENT_MONITOR);
                WritableSheet wwsAgentStateMonitorTemplate = reportWritableWorkbook.getSheet(SHEET_AGENT_STATE_MONITOR);
                WritableSheet wwsScheduleMonitorTemplate = reportWritableWorkbook.getSheet(SHEET_SCHEDULE_MONITOR);
                WritableSheet wwsTransferMonitorTemplate = reportWritableWorkbook.getSheet(SHEET_TRANSFER_MONITOR);
                WritableSheet wwsRecoveryMonitorTemplate = reportWritableWorkbook.getSheet(SHEET_RECOVERY_MONITOR);
                int templateSheetNum = reportWritableWorkbook.getNumberOfSheets();
                int newSheetNum = templateSheetNum;

                //报表首页信息
                //TODO
//                String uwsID = "UWS001";
//                String uwsIP = "20.20.1.244";

                //报表首页
                reportWritableWorkbook.copySheet(wwsHeaderTemplate.getName(), headerSheetName, ++newSheetNum);
                WritableSheet wwsHeader = reportWritableWorkbook.getSheet(headerSheetName);
                Label uwsIdLabel = (Label) wwsHeader.getWritableCell(3, 21);
//                uwsIdLabel.setString(uwsID);
                uwsIdLabel.setString(sdhmUWSState.getUWSName());
                Label uwsIpLabel = (Label) wwsHeader.getWritableCell(3, 23);
//                uwsIpLabel.setString(uwsIP);
                uwsIpLabel.setString(sdhmUWSState.getUWSIP());
                DateTime reportStartLabel = (DateTime) wwsHeader.getWritableCell(3, 25);
                reportStartLabel.setDate(sdfDate.parse(starttime));
                DateTime reportEndLabel = (DateTime) wwsHeader.getWritableCell(3, 27);
                reportEndLabel.setDate(sdfDate.parse(endtime));

                //本地UWS报表
                if (spaceMonitorFlag) {
                    reportWritableWorkbook.copySheet(wwsSpaceMonitorTemplate.getName(), spaceMonitorSheetName, ++newSheetNum);
                    WritableSheet wwsSpaceMonitor = reportWritableWorkbook.getSheet(spaceMonitorSheetName);
                    //UWS当前磁盘使用情况
                    //UWS磁盘总量
                    jxl.write.Number uwsTotalDiskNumber = new jxl.write.Number(1, 2, bigDecimalToDouble(sdhmUWSState.getAllDisk(), byteToGB));
                    wwsSpaceMonitor.addCell(uwsTotalDiskNumber);
                    //UWS磁盘已分配量
                    jxl.write.Number uwsVisualDiskNumber = new jxl.write.Number(1, 3, bigDecimalToDouble(sdhmUWSState.getVisualDisk(), byteToGB));
                    wwsSpaceMonitor.addCell(uwsVisualDiskNumber);
                    //UWS未使用磁盘量
                    jxl.write.Number uwsFreeDiskNumber = new jxl.write.Number(1, 5, bigDecimalToDouble(sdhmUWSState.getFreeDisk(), byteToGB));
                    wwsSpaceMonitor.addCell(uwsFreeDiskNumber);
                    //UWS磁盘未分配量 删除
//                    jxl.write.Number uwsUnVisualDiskNumber = new jxl.write.Number(1, 5, bigDecimalToDouble(sdhmUWSState.getAllDisk().subtract(sdhmUWSState.getVisualDisk()), byteToGB));
//                    wwsSpaceMonitor.addCell(uwsUnVisualDiskNumber);
                    //UWS本地使用量
                    jxl.write.Number uwsLocalDiskNumber = new jxl.write.Number(1, 6, bigDecimalToDouble(sdhmUWSState.getLocalDisk(), byteToGB));
                    wwsSpaceMonitor.addCell(uwsLocalDiskNumber);
                    //UWS镜像使用量
                    jxl.write.Number uwsMirrorDiskNumber = new jxl.write.Number(1, 7, bigDecimalToDouble(sdhmUWSState.getMirrorDisk(), byteToGB));
                    wwsSpaceMonitor.addCell(uwsMirrorDiskNumber);
                    //UWS分配未使用量 删除
//                    jxl.write.Number uwsFreeVisualDiskNumber = new jxl.write.Number(1, 8, bigDecimalToDouble(sdhmUWSState.getVisualDisk().subtract(sdhmUWSState.getLocalDisk()).subtract(sdhmUWSState.getMirrorDisk()), byteToGB));
//                    wwsSpaceMonitor.addCell(uwsFreeVisualDiskNumber);
                    //UWS空闲卷
                    jxl.write.Number uwsIdleDiskNumber = new jxl.write.Number(1, 8, bigDecimalToDouble(sdhmUWSState.getIdleDisk(), byteToGB));
                    wwsSpaceMonitor.addCell(uwsIdleDiskNumber);

                    //UWS磁盘增量情况
                    //UWS磁盘信息采集时间
                    for (int indexSpace = 0; indexSpace < sdhmUWSDiskTime.size(); indexSpace++) {
                        DateTime diskInfo = new DateTime(indexSpace + 1, 18, sdfDate.parse(sdhmUWSDiskTime.get(indexSpace).toString()), DateTime.GMT8);
                        wwsSpaceMonitor.addCell(diskInfo);
                    }
                    //UWS磁盘已分配空间
                    for (int indexSpace = 0; indexSpace < sdhmUWSVisualDisk.size(); indexSpace++) {
                        jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 19, objectToDouble(sdhmUWSVisualDisk.get(indexSpace), byteToGB));
                        wwsSpaceMonitor.addCell(diskInfo);
                    }
                    //UWS磁盘未使用空间
                    for (int indexSpace = 0; indexSpace < sdhmUWSFreeDisk.size(); indexSpace++) {
                        jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 20, objectToDouble(sdhmUWSFreeDisk.get(indexSpace), byteToGB));
                        wwsSpaceMonitor.addCell(diskInfo);
                    }
                    //UWS磁盘本地使用空间
                    for (int indexSpace = 0; indexSpace < sdhmUWSLocalDisk.size(); indexSpace++) {
                        jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 21, objectToDouble(sdhmUWSLocalDisk.get(indexSpace), byteToGB));
                        wwsSpaceMonitor.addCell(diskInfo);
                    }
                    //UWS磁盘镜像使用空间
                    for (int indexSpace = 0; indexSpace < sdhmUWSMirrorDisk.size(); indexSpace++) {
                        jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 22, objectToDouble(sdhmUWSMirrorDisk.get(indexSpace), byteToGB));
                        wwsSpaceMonitor.addCell(diskInfo);
                    }

                    //UWS镜像服务器空间变化信息
                    for (int i = 0; (mirrorUWSList != null) && (i < mirrorUWSList.size()); i++) {
                        //UWS镜像服务器IP地址
                        String mirrorUWSIPTemp = mirrorUWSList.get(i).toString();
                        Label mirrorUWSIPLabel = new Label(0, 44 + i, mirrorUWSIPTemp);
                        wwsSpaceMonitor.addCell(mirrorUWSIPLabel);

                        //指定IP对应的磁盘变化时间采集点信息
                        Vector mirrorUWSTimeTemp = (Vector) mirrorUWSTimeList.get(i);
                        for (int indexSpace = 0; indexSpace < mirrorUWSTimeTemp.size(); indexSpace++) {
                            DateTime diskInfo = new DateTime(indexSpace + 1, 43, sdfDate.parse(mirrorUWSTimeTemp.get(indexSpace).toString()), DateTime.GMT8);
                            wwsSpaceMonitor.addCell(diskInfo);
                        }
                        //指定IP对应的磁盘变化磁盘大小信息
                        Vector mirrorUWSSizeTemp = (Vector) mirrorUWSSizeList.get(i);
                        for (int indexSpace = 0; indexSpace < mirrorUWSSizeTemp.size(); indexSpace++) {
                            jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 44 + i, objectToDouble(mirrorUWSSizeTemp.get(indexSpace), byteToGB));
                            wwsSpaceMonitor.addCell(diskInfo);
                        }
                    }


                }

                //客户服务器UWS报表
                if (agentMonitorFlag && (agentIP != null)) {
                    if (agentIP.equals(IP_ALL)) {
                        for (int k = 0; k < agentList.size(); k++) {
                            String agentIPTemp = agentList.get(k).toString();
                            reportWritableWorkbook.copySheet(wwsAgentMonitorTemplate.getName(), agentMonitorSheetName + "-" + agentIPTemp, ++newSheetNum);
                            WritableSheet wwsAgentMonitor = reportWritableWorkbook.getSheet(agentMonitorSheetName + "-" + agentIPTemp);
                            reportWritableWorkbook.copySheet(wwsAgentStateMonitorTemplate.getName(), agentStateMonitorSheetName + "-" + agentIPTemp, ++newSheetNum);
                            WritableSheet wwsAgentStateMonitor = reportWritableWorkbook.getSheet(agentStateMonitorSheetName + "-" + agentIPTemp);
                            //Agent服务器IP地址
                            Label agentIPLabel = new Label(1, 2, agentIPTemp);
                            wwsAgentMonitor.addCell(agentIPLabel);
                            Label stateIPLabel = new Label(1, 2, agentIPTemp);
                            wwsAgentStateMonitor.addCell(stateIPLabel);

                            //获取当前agent磁盘总体信息
                            GetAgentState getAgentDiskInfo = new GetAgentState(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_DISK_INFO) + agentIPTemp + " all ");
                            getAgentDiskInfo.run();
                            retcode = getAgentDiskInfo.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentDiskInfo.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            SDHMAgentState sdhmAgentDiskInfo = getAgentDiskInfo.SDHMAgentState();
                            //TODO Excel输出
                            //快照最大数
//                            jxl.write.Number agentMaxSnapLabel = new jxl.write.Number(1, 11, objectToDouble(sdhmAgentState.getMaxSnap(), BigDecimal.ONE));
//                            wwsAgentMonitor.addCell(agentMaxSnapLabel);
                            //磁盘总量
                            jxl.write.Number agentTotalDiskNumber = new jxl.write.Number(1, 12, objectToDouble(sdhmAgentDiskInfo.getTotaldisk(), byteToGB));
                            wwsAgentMonitor.addCell(agentTotalDiskNumber);
                            //磁盘未使用量
                            jxl.write.Number agentFreeDiskNumber = new jxl.write.Number(1, 13, objectToDouble(sdhmAgentDiskInfo.getFreedisk(), byteToGB));
                            wwsAgentMonitor.addCell(agentFreeDiskNumber);

                            //获取agent的卷列表
                            GetAgentVolumeList getAgentVolumeList = new GetAgentVolumeList(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_VOLUME) + agentIPTemp);
                            getAgentVolumeList.run();
                            retcode = getAgentVolumeList.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentVolumeList.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            Vector agentVolumeList = getAgentVolumeList.AgentVolumeList();

                            //获取agent卷磁盘信息
                            for (int m = 0; m < agentVolumeList.size(); m++) {
                                String agentVolumeName = agentVolumeList.get(m).toString();
                                //agent卷名
                                Label agentVolumeNameLabel = new Label(1 + m, 3, agentVolumeName);
                                wwsAgentMonitor.addCell(agentVolumeNameLabel);
                                GetAgentState getAgentVolInfo = new GetAgentState(
                                        view,
                                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_DISK_INFO) + agentIPTemp + " " + agentVolumeName);
                                getAgentVolInfo.run();
                                retcode = getAgentVolInfo.getRetCode();
                                if (retcode != 0) {
                                    errorFlag = true;
                                    errMsg = getAgentVolInfo.getErrMsg();
                                    SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                    setOver(true);
                                    return;
                                }
                                SDHMAgentState sdhmAgentVolInfo = getAgentVolInfo.SDHMAgentState();
                                //TODO Excel输出
                                //卷总量
                                jxl.write.Number agentVolDiskNumber = new jxl.write.Number(m + 1, 4, objectToDouble(sdhmAgentVolInfo.getTotaldisk(), byteToGB));
                                wwsAgentMonitor.addCell(agentVolDiskNumber);
                                //卷未使用量
                                jxl.write.Number agentVolFreeDiskNumber = new jxl.write.Number(m + 1, 5, objectToDouble(sdhmAgentVolInfo.getFreedisk(), byteToGB));
                                wwsAgentMonitor.addCell(agentVolFreeDiskNumber);
                            }

                            //获取agent的网盘列表
                            GetAgentVolumeList getAgentTargetList = new GetAgentVolumeList(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_TARGET) + agentIPTemp);
                            getAgentTargetList.run();
                            retcode = getAgentTargetList.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentTargetList.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            Vector agentTargetList = getAgentTargetList.AgentVolumeList();

                            //获取agent网盘磁盘信息
                            for (int m = 0; m < agentTargetList.size(); m++) {
                                String agentTargetName = agentTargetList.get(m).toString();
                                //agent卷名
                                Label agentTargetNameLabel = new Label(1 + m, 6, agentTargetName);
                                wwsAgentMonitor.addCell(agentTargetNameLabel);
                                GetAgentState getAgentVolInfo = new GetAgentState(
                                        view,
                                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_TARGET_DISK_INFO) + agentIPTemp + " " + agentTargetName);
                                getAgentVolInfo.run();
                                retcode = getAgentVolInfo.getRetCode();
                                if (retcode != 0) {
                                    errorFlag = true;
                                    errMsg = getAgentVolInfo.getErrMsg();
                                    SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                    setOver(true);
                                    return;
                                }
                                SDHMAgentState sdhmAgentVolInfo = getAgentVolInfo.SDHMAgentState();
                                //TODO Excel输出
                                //网盘总量
                                jxl.write.Number agentVolDiskNumber = new jxl.write.Number(m + 1, 7, objectToDouble(sdhmAgentVolInfo.getTotaldisk(), byteToGB));
                                wwsAgentMonitor.addCell(agentVolDiskNumber);
                                //网盘未使用量
                                jxl.write.Number agentVolFreeDiskNumber = new jxl.write.Number(m + 1, 8, objectToDouble(sdhmAgentVolInfo.getFreedisk(), byteToGB));
                                wwsAgentMonitor.addCell(agentVolFreeDiskNumber);
                                //快照数量
                                jxl.write.Number agentUsedSnapLabel = new jxl.write.Number(m + 1, 9, objectToDouble(sdhmAgentVolInfo.getUsedsnap(), BigDecimal.ONE));
                                wwsAgentMonitor.addCell(agentUsedSnapLabel);
                                //快照最大数
                                jxl.write.Number agentMaxSnapLabel = new jxl.write.Number(m + 1, 10, objectToDouble(sdhmAgentVolInfo.getMaxsnap(), BigDecimal.ONE));
                                wwsAgentMonitor.addCell(agentMaxSnapLabel);
                            }

                            //获取agent总空闲磁盘采集信息
                            GetSDHMDiskInfoList getAgentFreeDisk = new GetSDHMDiskInfoList(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_FREEDISK) + agentIPTemp + " freedisk " + starttime + " " + endtime);
                            getAgentFreeDisk.run();
                            retcode = getAgentFreeDisk.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentFreeDisk.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            Vector agentFreeDiskTime = getAgentFreeDisk.SDHMUWSDiskTimeList();//agent磁盘信息采集时间
                            Vector agentFreeDiskList = getAgentFreeDisk.SDHMUWSDiskInfoList(); //agent磁盘量变化
                            //Excel输出
                            for (int indexSpace = 0; indexSpace < agentFreeDiskTime.size(); indexSpace++) {
                                DateTime diskInfo = new DateTime(indexSpace + 1, 21, sdfDate.parse(agentFreeDiskTime.get(indexSpace).toString()), DateTime.GMT8);
                                wwsAgentMonitor.addCell(diskInfo);
                            }
                            for (int indexSpace = 0; indexSpace < agentFreeDiskList.size(); indexSpace++) {
                                jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 22, objectToDouble(agentFreeDiskList.get(indexSpace), byteToGB));
                                wwsAgentMonitor.addCell(diskInfo);
                            }

                            //获取agent每个卷空闲磁盘采集信息
                            for (int indexVol = 0; indexVol < agentVolumeList.size(); indexVol++) {
                                String volNameTemp = agentVolumeList.get(indexVol).toString();
                                Label agentVolNameLabel = new Label(0, 23 + indexVol, "卷未使用量-" + volNameTemp);
                                wwsAgentMonitor.addCell(agentVolNameLabel);
                                GetSDHMDiskInfoList getAgentVolFreeDisk = new GetSDHMDiskInfoList(
                                        view,
                                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_VOL_FREEDISK) + agentIPTemp + " " + volNameTemp + " freevol " + starttime + " " + endtime);
                                getAgentVolFreeDisk.run();
                                retcode = getAgentVolFreeDisk.getRetCode();
                                if (retcode != 0) {
                                    errorFlag = true;
                                    errMsg = getAgentVolFreeDisk.getErrMsg();
                                    SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                    setOver(true);
                                    return;
                                }
                                Vector agentVolFreeDiskList = getAgentVolFreeDisk.SDHMUWSDiskInfoList(); //agent每个卷磁盘量变化
                                //Excel输出
                                for (int indexSpace = 0; indexSpace < agentVolFreeDiskList.size(); indexSpace++) {
                                    jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 23 + indexVol, objectToDouble(agentVolFreeDiskList.get(indexSpace), byteToGB));
                                    wwsAgentMonitor.addCell(diskInfo);
                                }
                            }

                            int agentVolumeLength = agentVolumeList.size();
                            //获取agent每个网盘空闲磁盘采集信息
                            for (int indexVol = 0; indexVol < agentTargetList.size(); indexVol++) {
                                if (indexVol == 0){
                                    Label dateNameLabel = new Label(0, 24 + agentVolumeLength + indexVol, "年月日");
                                    wwsAgentMonitor.addCell(dateNameLabel);
                                }
                                String targetNameTemp = agentTargetList.get(indexVol).toString();
                                Label agentTargetNameLabel = new Label(0, 25 + agentVolumeLength + indexVol, "网盘未使用量-" + targetNameTemp);
                                wwsAgentMonitor.addCell(agentTargetNameLabel);
                                GetSDHMDiskInfoList getAgentTargetFreeDisk = new GetSDHMDiskInfoList(
                                        view,
                                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_TARGET_FREEDISK) + agentIPTemp + " " + targetNameTemp + " freesize " + starttime + " " + endtime);
                                getAgentTargetFreeDisk.run();
                                retcode = getAgentTargetFreeDisk.getRetCode();
                                if (retcode != 0) {
                                    errorFlag = true;
                                    errMsg = getAgentTargetFreeDisk.getErrMsg();
                                    SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                    setOver(true);
                                    return;
                                }
                                Vector agentTargetFreeTimeList = getAgentTargetFreeDisk.SDHMUWSDiskTimeList();//agent每个卷采集时间
                                Vector agentTargetFreeDiskList = getAgentTargetFreeDisk.SDHMUWSDiskInfoList(); //agent每个卷磁盘量变化
                                //Excel输出
                                if (indexVol == 0){
                                    for (int indexSpace = 0; indexSpace < agentTargetFreeTimeList.size(); indexSpace++) {
                                        DateTime diskInfo = new DateTime(indexSpace + 1, 24 + agentVolumeLength + indexVol, sdfDate.parse(agentTargetFreeTimeList.get(indexSpace).toString()), DateTime.GMT8);
                                        wwsAgentMonitor.addCell(diskInfo);
                                    }
                                }

                                for (int indexSpace = 0; indexSpace < agentTargetFreeDiskList.size(); indexSpace++) {
                                    jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 25 + agentVolumeLength + indexVol, objectToDouble(agentTargetFreeDiskList.get(indexSpace), byteToGB));
                                    wwsAgentMonitor.addCell(diskInfo);
                                }
                            }

                            //Agent状态信息
                            //有任务时信息
                            //Memory信息
                            GetAgentMonitorInfo getAgentMonitorInfoMemTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task memused " + starttime + " " + endtime);
                            getAgentMonitorInfoMemTask.run();
                            retcode = getAgentMonitorInfoMemTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoMemTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal memusedTask = getAgentMonitorInfoMemTask.AvgMonitorInfo();
                            //CPU信息
                            GetAgentMonitorInfo getAgentMonitorInfoCpuTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task cpuused " + starttime + " " + endtime);
                            getAgentMonitorInfoCpuTask.run();
                            retcode = getAgentMonitorInfoCpuTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoCpuTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal cpuusedTask = getAgentMonitorInfoCpuTask.AvgMonitorInfo();
                            //磁盘读IO信息
                            GetAgentMonitorInfo getAgentMonitorInfoDiskrioTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task diskrio " + starttime + " " + endtime);
                            getAgentMonitorInfoDiskrioTask.run();
                            retcode = getAgentMonitorInfoDiskrioTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoDiskrioTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal diskrioTask = getAgentMonitorInfoDiskrioTask.AvgMonitorInfo();
                            //磁盘写IO信息
                            GetAgentMonitorInfo getAgentMonitorInfoDiskwioTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task diskwio " + starttime + " " + endtime);
                            getAgentMonitorInfoDiskwioTask.run();
                            retcode = getAgentMonitorInfoDiskwioTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoDiskwioTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal diskwioTask = getAgentMonitorInfoDiskwioTask.AvgMonitorInfo();
                            //网络读IO信息
                            GetAgentMonitorInfo getAgentMonitorInfoNetrioTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task netrio " + starttime + " " + endtime);
                            getAgentMonitorInfoNetrioTask.run();
                            retcode = getAgentMonitorInfoNetrioTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoNetrioTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal netrioTask = getAgentMonitorInfoNetrioTask.AvgMonitorInfo();
                            //网络写IO信息
                            GetAgentMonitorInfo getAgentMonitorInfoNetwioTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task netwio " + starttime + " " + endtime);
                            getAgentMonitorInfoNetwioTask.run();
                            retcode = getAgentMonitorInfoNetwioTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoNetwioTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal netwioTask = getAgentMonitorInfoNetwioTask.AvgMonitorInfo();

                            //无任务时信息
                            //Memory信息
                            GetAgentMonitorInfo getAgentMonitorInfoMemNoTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask memused " + starttime + " " + endtime);
                            getAgentMonitorInfoMemNoTask.run();
                            retcode = getAgentMonitorInfoMemNoTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoMemNoTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal memusedNoTask = getAgentMonitorInfoMemNoTask.AvgMonitorInfo();
                            //CPU信息
                            GetAgentMonitorInfo getAgentMonitorInfoCpuNoTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask cpuused " + starttime + " " + endtime);
                            getAgentMonitorInfoCpuNoTask.run();
                            retcode = getAgentMonitorInfoCpuNoTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoCpuNoTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal cpuusedNoTask = getAgentMonitorInfoCpuNoTask.AvgMonitorInfo();
                            //磁盘读IO信息
                            GetAgentMonitorInfo getAgentMonitorInfoDiskrioNoTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask diskrio " + starttime + " " + endtime);
                            getAgentMonitorInfoDiskrioNoTask.run();
                            retcode = getAgentMonitorInfoDiskrioNoTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoDiskrioNoTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal diskrioNoTask = getAgentMonitorInfoDiskrioNoTask.AvgMonitorInfo();
                            //磁盘写IO信息
                            GetAgentMonitorInfo getAgentMonitorInfoDiskwioNoTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask diskwio " + starttime + " " + endtime);
                            getAgentMonitorInfoDiskwioNoTask.run();
                            retcode = getAgentMonitorInfoDiskwioNoTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoDiskwioNoTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal diskwioNoTask = getAgentMonitorInfoDiskwioNoTask.AvgMonitorInfo();
                            //网络读IO信息
                            GetAgentMonitorInfo getAgentMonitorInfoNetrioNoTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask netrio " + starttime + " " + endtime);
                            getAgentMonitorInfoNetrioNoTask.run();
                            retcode = getAgentMonitorInfoNetrioNoTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoNetrioNoTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal netrioNoTask = getAgentMonitorInfoNetrioNoTask.AvgMonitorInfo();
                            //网络写IO信息
                            GetAgentMonitorInfo getAgentMonitorInfoNetwioNoTask = new GetAgentMonitorInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask netwio " + starttime + " " + endtime);
                            getAgentMonitorInfoNetwioNoTask.run();
                            retcode = getAgentMonitorInfoNetwioNoTask.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentMonitorInfoNetwioNoTask.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            BigDecimal netwioNoTask = getAgentMonitorInfoNetwioNoTask.AvgMonitorInfo();

                            //Excel写入
                            //有任务
                            //磁盘读
                            jxl.write.Number diskrioTaskNumber = new jxl.write.Number(1, 5, objectToDouble(diskrioTask, byteToMB));
                            wwsAgentStateMonitor.addCell(diskrioTaskNumber);
                            //磁盘写
                            jxl.write.Number diskwioTaskNumber = new jxl.write.Number(1, 6, objectToDouble(diskwioTask, byteToMB));
                            wwsAgentStateMonitor.addCell(diskwioTaskNumber);
                            //网络读
                            jxl.write.Number netrioTaskNumber = new jxl.write.Number(1, 7, objectToDouble(netrioTask, byteToMB));
                            wwsAgentStateMonitor.addCell(netrioTaskNumber);
                            //网络写
                            jxl.write.Number netwioTaskNumber = new jxl.write.Number(1, 8, objectToDouble(netwioTask, byteToMB));
                            wwsAgentStateMonitor.addCell(netwioTaskNumber);
                            //CPU
                            jxl.write.Number cpuusedTaskNumber = new jxl.write.Number(1, 9, objectToDouble(cpuusedTask, BigDecimal.ONE));
                            wwsAgentStateMonitor.addCell(cpuusedTaskNumber);
                            //内存
                            jxl.write.Number memusedTaskNumber = new jxl.write.Number(1, 10, objectToDouble(memusedTask, BigDecimal.ONE));
                            wwsAgentStateMonitor.addCell(memusedTaskNumber);
                            //无任务
                            //磁盘读
                            jxl.write.Number diskrioNoTaskNumber = new jxl.write.Number(2, 5, objectToDouble(diskrioNoTask, byteToMB));
                            wwsAgentStateMonitor.addCell(diskrioNoTaskNumber);
                            //磁盘写
                            jxl.write.Number diskwioNoTaskNumber = new jxl.write.Number(2, 6, objectToDouble(diskwioNoTask, byteToMB));
                            wwsAgentStateMonitor.addCell(diskwioNoTaskNumber);
                            //网络读
                            jxl.write.Number netrioNoTaskNumber = new jxl.write.Number(2, 7, objectToDouble(netrioNoTask, byteToMB));
                            wwsAgentStateMonitor.addCell(netrioNoTaskNumber);
                            //网络写
                            jxl.write.Number netwioNoTaskNumber = new jxl.write.Number(2, 8, objectToDouble(netwioNoTask, byteToMB));
                            wwsAgentStateMonitor.addCell(netwioNoTaskNumber);
                            //CPU
                            jxl.write.Number cpuusedNoTaskNumber = new jxl.write.Number(2, 9, objectToDouble(cpuusedNoTask, BigDecimal.ONE));
                            wwsAgentStateMonitor.addCell(cpuusedNoTaskNumber);
                            //内存
                            jxl.write.Number memusedNoTaskNumber = new jxl.write.Number(2, 10, objectToDouble(memusedNoTask, BigDecimal.ONE));
                            wwsAgentStateMonitor.addCell(memusedNoTaskNumber);

                            //获取任务汇总信息
                            GetAgentJobInfo getAgentJobInfoSummary = new GetAgentJobInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_TASK_INFO) + agentIPTemp + " summary " + starttime + " " + endtime);
                            getAgentJobInfoSummary.run();
                            retcode = getAgentJobInfoSummary.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentJobInfoSummary.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            AgentJobInfo agentJobInfoSummary = getAgentJobInfoSummary.AgentTaskInfo();
                            //TODO Excel输出
                            //任务总数
                            jxl.write.Number agentJobInfoTotal = new jxl.write.Number(1, 18, objectToDouble(agentJobInfoSummary.getSumtask(), BigDecimal.ONE));
                            wwsAgentStateMonitor.addCell(agentJobInfoTotal);
                            //失败数量
                            jxl.write.Number agentJobInfoError = new jxl.write.Number(1, 19, objectToDouble(agentJobInfoSummary.getErrortask(), BigDecimal.ONE));
                            wwsAgentStateMonitor.addCell(agentJobInfoError);

                            //获取任务详细信息
                            GetAgentJobInfo getAgentJobInfoDetail = new GetAgentJobInfo(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_TASK_INFO) + agentIPTemp + " details " + starttime + " " + endtime);
                            getAgentJobInfoDetail.run();
                            retcode = getAgentJobInfoDetail.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentJobInfoDetail.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            AgentJobInfo agentJobInfoDetail = getAgentJobInfoDetail.AgentTaskInfo();
                            Vector agentTaskStartTime = agentJobInfoDetail.getAgentTaskStartTime();
                            Vector agentTaskEndTime = agentJobInfoDetail.getAgentTaskEndTime();
                            Vector agentTaskType = agentJobInfoDetail.getAgentTaskType();
                            Vector agentTaskResult = agentJobInfoDetail.getAgentTaskResult();
                            Vector agentTaskMessage = agentJobInfoDetail.getAgentTaskMessage();
                            //TODO Excel输出
                            for (int indexTask = 0; indexTask < agentTaskStartTime.size(); indexTask++) {
                                String agentTaskStartTimeTemp = agentTaskStartTime.get(indexTask).toString();
                                String agentTaskEndTimeTemp = agentTaskEndTime.get(indexTask).toString();
                                String agentTaskTypeTemp = agentTaskType.get(indexTask).toString();
                                String agentTaskResultTemp = agentTaskResult.get(indexTask).toString();
                                String agentTaskMessageTemp = agentTaskMessage.get(indexTask).toString();
                                //开始时间
                                DateTime agentTaskStartTimeDate = new DateTime(indexTask + 1, 21, sdfDate.parse(agentTaskStartTimeTemp), DateTime.GMT8);
                                wwsAgentStateMonitor.addCell(agentTaskStartTimeDate);
                                //结束时间
                                DateTime agentTaskEndTimeDate = new DateTime(indexTask + 1, 22, sdfDate.parse(agentTaskEndTimeTemp), DateTime.GMT8);
                                wwsAgentStateMonitor.addCell(agentTaskEndTimeDate);
                                //任务类型
                                Label agentTaskTypeTempLabel = new Label(indexTask + 1, 23, agentTaskTypeTemp);
                                wwsAgentStateMonitor.addCell(agentTaskTypeTempLabel);
                                //任务结果
                                Label agentTaskResultTempLabel = new Label(indexTask + 1, 24, agentTaskResultTemp);
                                wwsAgentStateMonitor.addCell(agentTaskResultTempLabel);
                                //任务信息
                                Label agentTaskMessageTempLabel = new Label(indexTask + 1, 25, agentTaskMessageTemp);
                                wwsAgentStateMonitor.addCell(agentTaskMessageTempLabel);

                            }




//                            //指定IP对应的磁盘变化时间采集点信息
//                            Vector agentTimeTemp = (Vector)agentTimeList.get(k);
//                            for (int indexSpace = 0; indexSpace < agentTimeTemp.size(); indexSpace++ ){
//                                DateTime diskInfo = new DateTime(indexSpace+1, 23, sdfDate.parse(agentTimeTemp.get(indexSpace).toString()));
//                                wwsAgentMonitor.addCell(diskInfo);
//                            }
//                            //指定IP对应的磁盘变化磁盘大小信息
//                            Vector agentSizeTemp = (Vector)agentUWSDiskList.get(k);
//                            for(int indexSpace = 0; indexSpace < agentSizeTemp.size(); indexSpace++ ){
//                                jxl.write.Number diskInfo = new jxl.write.Number(indexSpace+1, 24, objectToDouble(agentSizeTemp.get(indexSpace), byteToGB));
//                                wwsAgentMonitor.addCell(diskInfo);
//                            }
                        }
                    } else {
                        String agentIPTemp = agentIP;
                        reportWritableWorkbook.copySheet(wwsAgentMonitorTemplate.getName(), agentMonitorSheetName + "-" + agentIPTemp, ++newSheetNum);
                        WritableSheet wwsAgentMonitor = reportWritableWorkbook.getSheet(agentMonitorSheetName + "-" + agentIPTemp);
                        reportWritableWorkbook.copySheet(wwsAgentStateMonitorTemplate.getName(), agentStateMonitorSheetName + "-" + agentIPTemp, ++newSheetNum);
                        WritableSheet wwsAgentStateMonitor = reportWritableWorkbook.getSheet(agentStateMonitorSheetName + "-" + agentIPTemp);
                        //Agent服务器IP地址
                        Label agentIPLabel = new Label(1, 2, agentIPTemp);
                        wwsAgentMonitor.addCell(agentIPLabel);
                        Label stateIPLabel = new Label(1, 2, agentIPTemp);
                        wwsAgentStateMonitor.addCell(stateIPLabel);

                        //获取当前agent磁盘总体信息
                        GetAgentState getAgentDiskInfo = new GetAgentState(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_DISK_INFO) + agentIPTemp + " all ");
                        getAgentDiskInfo.run();
                        retcode = getAgentDiskInfo.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentDiskInfo.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        SDHMAgentState sdhmAgentDiskInfo = getAgentDiskInfo.SDHMAgentState();
                        //TODO Excel输出
                        //快照最大数
//                            jxl.write.Number agentMaxSnapLabel = new jxl.write.Number(1, 11, objectToDouble(sdhmAgentState.getMaxSnap(), BigDecimal.ONE));
//                            wwsAgentMonitor.addCell(agentMaxSnapLabel);
                        //磁盘总量
                        jxl.write.Number agentTotalDiskNumber = new jxl.write.Number(1, 12, objectToDouble(sdhmAgentDiskInfo.getTotaldisk(), byteToGB));
                        wwsAgentMonitor.addCell(agentTotalDiskNumber);
                        //磁盘未使用量
                        jxl.write.Number agentFreeDiskNumber = new jxl.write.Number(1, 13, objectToDouble(sdhmAgentDiskInfo.getFreedisk(), byteToGB));
                        wwsAgentMonitor.addCell(agentFreeDiskNumber);

                        //获取agent的卷列表
                        GetAgentVolumeList getAgentVolumeList = new GetAgentVolumeList(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_VOLUME) + agentIPTemp);
                        getAgentVolumeList.run();
                        retcode = getAgentVolumeList.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentVolumeList.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        Vector agentVolumeList = getAgentVolumeList.AgentVolumeList();

                        //获取agent卷磁盘信息
                        for (int m = 0;  m < agentVolumeList.size(); m++) {
                            String agentVolumeName = agentVolumeList.get(m).toString();
                            //agent卷名
                            Label agentVolumeNameLabel = new Label(1 + m, 3, agentVolumeName);
                            wwsAgentMonitor.addCell(agentVolumeNameLabel);
                            GetAgentState getAgentVolInfo = new GetAgentState(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_DISK_INFO) + agentIPTemp + " " + agentVolumeName);
                            getAgentVolInfo.run();
                            retcode = getAgentVolInfo.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentVolInfo.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            SDHMAgentState sdhmAgentVolInfo = getAgentVolInfo.SDHMAgentState();
                            //TODO Excel输出
                            //卷总量
                            jxl.write.Number agentVolDiskNumber = new jxl.write.Number(m + 1, 4, objectToDouble(sdhmAgentVolInfo.getTotaldisk(), byteToGB));
                            wwsAgentMonitor.addCell(agentVolDiskNumber);
                            //卷未使用量
                            jxl.write.Number agentVolFreeDiskNumber = new jxl.write.Number(m + 1, 5, objectToDouble(sdhmAgentVolInfo.getFreedisk(), byteToGB));
                            wwsAgentMonitor.addCell(agentVolFreeDiskNumber);
                        }

                        //获取agent的网盘列表
                        GetAgentVolumeList getAgentTargetList = new GetAgentVolumeList(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_TARGET) + agentIPTemp);
                        getAgentTargetList.run();
                        retcode = getAgentTargetList.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentTargetList.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        Vector agentTargetList = getAgentTargetList.AgentVolumeList();

                        //获取agent网盘磁盘信息
                        for (int m = 0; m < agentTargetList.size(); m++) {
                            String agentTargetName = agentTargetList.get(m).toString();
                            //agent卷名
                            Label agentTargetNameLabel = new Label(1 + m, 6, agentTargetName);
                            wwsAgentMonitor.addCell(agentTargetNameLabel);
                            GetAgentState getAgentVolInfo = new GetAgentState(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_TARGET_DISK_INFO) + agentIPTemp + " " + agentTargetName);
                            getAgentVolInfo.run();
                            retcode = getAgentVolInfo.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentVolInfo.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            SDHMAgentState sdhmAgentVolInfo = getAgentVolInfo.SDHMAgentState();
                            //TODO Excel输出
                            //卷总量
                            jxl.write.Number agentVolDiskNumber = new jxl.write.Number(m + 1, 7, objectToDouble(sdhmAgentVolInfo.getTotaldisk(), byteToGB));
                            wwsAgentMonitor.addCell(agentVolDiskNumber);
                            //卷未使用量
                            jxl.write.Number agentVolFreeDiskNumber = new jxl.write.Number(m + 1, 8, objectToDouble(sdhmAgentVolInfo.getFreedisk(), byteToGB));
                            wwsAgentMonitor.addCell(agentVolFreeDiskNumber);
                            //快照数量
                            jxl.write.Number agentUsedSnapLabel = new jxl.write.Number(m + 1, 9, objectToDouble(sdhmAgentVolInfo.getUsedsnap(), BigDecimal.ONE));
                            wwsAgentMonitor.addCell(agentUsedSnapLabel);
                            //快照最大数
                            jxl.write.Number agentMaxSnapLabel = new jxl.write.Number(m + 1, 10, objectToDouble(sdhmAgentVolInfo.getMaxsnap(), BigDecimal.ONE));
                            wwsAgentMonitor.addCell(agentMaxSnapLabel);
                        }

                        //获取agent总空闲磁盘采集信息
                        GetSDHMDiskInfoList getAgentFreeDisk = new GetSDHMDiskInfoList(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_FREEDISK) + agentIPTemp + " freedisk " + starttime + " " + endtime);
                        getAgentFreeDisk.run();
                        retcode = getAgentFreeDisk.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentFreeDisk.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        Vector agentFreeDiskTime = getAgentFreeDisk.SDHMUWSDiskTimeList();//agent磁盘信息采集时间
                        Vector agentFreeDiskList = getAgentFreeDisk.SDHMUWSDiskInfoList(); //agent磁盘量变化
                        //Excel输出
                        for (int indexSpace = 0; indexSpace < agentFreeDiskTime.size(); indexSpace++) {
                            DateTime diskInfo = new DateTime(indexSpace + 1, 21, sdfDate.parse(agentFreeDiskTime.get(indexSpace).toString()), DateTime.GMT8);
                            wwsAgentMonitor.addCell(diskInfo);
                        }
                        for (int indexSpace = 0; indexSpace < agentFreeDiskList.size(); indexSpace++) {
                            jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 22, objectToDouble(agentFreeDiskList.get(indexSpace), byteToGB));
                            wwsAgentMonitor.addCell(diskInfo);
                        }

                        //获取agent每个卷空闲磁盘采集信息
                        for (int indexVol = 0; indexVol < agentVolumeList.size(); indexVol++) {
                            String volNameTemp = agentVolumeList.get(indexVol).toString();
                            Label agentVolNameLabel = new Label(0, 23 + indexVol, "卷未使用量-" + volNameTemp);
                            wwsAgentMonitor.addCell(agentVolNameLabel);
                            GetSDHMDiskInfoList getAgentVolFreeDisk = new GetSDHMDiskInfoList(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_VOL_FREEDISK) + agentIPTemp + " " + volNameTemp + " freevol " + starttime + " " + endtime);
                            getAgentVolFreeDisk.run();
                            retcode = getAgentVolFreeDisk.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentVolFreeDisk.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            Vector agentVolFreeDiskList = getAgentVolFreeDisk.SDHMUWSDiskInfoList(); //agent每个卷磁盘量变化
                            //Excel输出
                            for (int indexSpace = 0; indexSpace < agentVolFreeDiskList.size(); indexSpace++) {
                                jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 23 + indexVol, objectToDouble(agentVolFreeDiskList.get(indexSpace), byteToGB));
                                wwsAgentMonitor.addCell(diskInfo);
                            }
                        }

                        int agentVolumeLength = agentVolumeList.size();
                        //获取agent每个网盘空闲磁盘采集信息
                        for (int indexVol = 0; indexVol < agentTargetList.size(); indexVol++) {
                            if (indexVol == 0){
                                Label dateNameLabel = new Label(0, 24 + agentVolumeLength + indexVol, "年月日");
                                wwsAgentMonitor.addCell(dateNameLabel);
                            }
                            String targetNameTemp = agentTargetList.get(indexVol).toString();
                            Label agentTargetNameLabel = new Label(0, 25 + agentVolumeLength + indexVol, "网盘未使用量-" + targetNameTemp);
                            wwsAgentMonitor.addCell(agentTargetNameLabel);
                            GetSDHMDiskInfoList getAgentTargetFreeDisk = new GetSDHMDiskInfoList(
                                    view,
                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_TARGET_FREEDISK) + agentIPTemp + " " + targetNameTemp + " freesize " + starttime + " " + endtime);
                            getAgentTargetFreeDisk.run();
                            retcode = getAgentTargetFreeDisk.getRetCode();
                            if (retcode != 0) {
                                errorFlag = true;
                                errMsg = getAgentTargetFreeDisk.getErrMsg();
                                SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                                setOver(true);
                                return;
                            }
                            Vector agentTargetFreeTimeList = getAgentTargetFreeDisk.SDHMUWSDiskTimeList();//agent每个卷采集时间
                            Vector agentTargetFreeDiskList = getAgentTargetFreeDisk.SDHMUWSDiskInfoList(); //agent每个卷磁盘量变化
                            //Excel输出
                            if (indexVol == 0){
                                for (int indexSpace = 0; indexSpace < agentTargetFreeTimeList.size(); indexSpace++) {
                                    DateTime diskInfo = new DateTime(indexSpace + 1, 24 + agentVolumeLength + indexVol, sdfDate.parse(agentTargetFreeTimeList.get(indexSpace).toString()), DateTime.GMT8);
                                    wwsAgentMonitor.addCell(diskInfo);
                                }
                            }
                            for (int indexSpace = 0; indexSpace < agentTargetFreeDiskList.size(); indexSpace++) {
                                jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 25 + agentVolumeLength + indexVol, objectToDouble(agentTargetFreeDiskList.get(indexSpace), byteToGB));
                                wwsAgentMonitor.addCell(diskInfo);
                            }
                        }
                        //Agent状态信息
                        //有任务时信息
                        //Memory信息
                        GetAgentMonitorInfo getAgentMonitorInfoMemTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task memused " + starttime + " " + endtime);
                        getAgentMonitorInfoMemTask.run();
                        retcode = getAgentMonitorInfoMemTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoMemTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal memusedTask = getAgentMonitorInfoMemTask.AvgMonitorInfo();
                        //CPU信息
                        GetAgentMonitorInfo getAgentMonitorInfoCpuTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task cpuused " + starttime + " " + endtime);
                        getAgentMonitorInfoCpuTask.run();
                        retcode = getAgentMonitorInfoCpuTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoCpuTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal cpuusedTask = getAgentMonitorInfoCpuTask.AvgMonitorInfo();
                        //磁盘读IO信息
                        GetAgentMonitorInfo getAgentMonitorInfoDiskrioTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task diskrio " + starttime + " " + endtime);
                        getAgentMonitorInfoDiskrioTask.run();
                        retcode = getAgentMonitorInfoDiskrioTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoDiskrioTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal diskrioTask = getAgentMonitorInfoDiskrioTask.AvgMonitorInfo();
                        //磁盘写IO信息
                        GetAgentMonitorInfo getAgentMonitorInfoDiskwioTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task diskwio " + starttime + " " + endtime);
                        getAgentMonitorInfoDiskwioTask.run();
                        retcode = getAgentMonitorInfoDiskwioTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoDiskwioTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal diskwioTask = getAgentMonitorInfoDiskwioTask.AvgMonitorInfo();
                        //网络读IO信息
                        GetAgentMonitorInfo getAgentMonitorInfoNetrioTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task netrio " + starttime + " " + endtime);
//                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task netio " + starttime + " " + endtime);
                        getAgentMonitorInfoNetrioTask.run();
                        retcode = getAgentMonitorInfoNetrioTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoNetrioTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal netrioTask = getAgentMonitorInfoNetrioTask.AvgMonitorInfo();
                        //网络写IO信息
                        GetAgentMonitorInfo getAgentMonitorInfoNetwioTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task netwio " + starttime + " " + endtime);
//                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " task netio " + starttime + " " + endtime);
                        getAgentMonitorInfoNetwioTask.run();
                        retcode = getAgentMonitorInfoNetwioTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoNetwioTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal netwioTask = getAgentMonitorInfoNetwioTask.AvgMonitorInfo();

                        //无任务时信息
                        //Memory信息
                        GetAgentMonitorInfo getAgentMonitorInfoMemNoTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask memused " + starttime + " " + endtime);
                        getAgentMonitorInfoMemNoTask.run();
                        retcode = getAgentMonitorInfoMemNoTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoMemNoTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal memusedNoTask = getAgentMonitorInfoMemNoTask.AvgMonitorInfo();
                        //CPU信息
                        GetAgentMonitorInfo getAgentMonitorInfoCpuNoTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask cpuused " + starttime + " " + endtime);
                        getAgentMonitorInfoCpuNoTask.run();
                        retcode = getAgentMonitorInfoCpuNoTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoCpuNoTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal cpuusedNoTask = getAgentMonitorInfoCpuNoTask.AvgMonitorInfo();
                        //磁盘读IO信息
                        GetAgentMonitorInfo getAgentMonitorInfoDiskrioNoTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask diskrio " + starttime + " " + endtime);
                        getAgentMonitorInfoDiskrioNoTask.run();
                        retcode = getAgentMonitorInfoDiskrioNoTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoDiskrioNoTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal diskrioNoTask = getAgentMonitorInfoDiskrioNoTask.AvgMonitorInfo();
                        //磁盘写IO信息
                        GetAgentMonitorInfo getAgentMonitorInfoDiskwioNoTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask diskwio " + starttime + " " + endtime);
                        getAgentMonitorInfoDiskwioNoTask.run();
                        retcode = getAgentMonitorInfoDiskwioNoTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoDiskwioNoTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal diskwioNoTask = getAgentMonitorInfoDiskwioNoTask.AvgMonitorInfo();
                        //网络读IO信息
                        GetAgentMonitorInfo getAgentMonitorInfoNetrioNoTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask netrio " + starttime + " " + endtime);
//                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask netio " + starttime + " " + endtime);
                        getAgentMonitorInfoNetrioNoTask.run();
                        retcode = getAgentMonitorInfoNetrioNoTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoNetrioNoTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal netrioNoTask = getAgentMonitorInfoNetrioNoTask.AvgMonitorInfo();
                        //网络写IO信息
                        GetAgentMonitorInfo getAgentMonitorInfoNetwioNoTask = new GetAgentMonitorInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask netwio " + starttime + " " + endtime);
//                                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_MONITOR_INFO) + agentIPTemp + " notask netio " + starttime + " " + endtime);
                        getAgentMonitorInfoNetwioNoTask.run();
                        retcode = getAgentMonitorInfoNetwioNoTask.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentMonitorInfoNetwioNoTask.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        BigDecimal netwioNoTask = getAgentMonitorInfoNetwioNoTask.AvgMonitorInfo();

                        //Excel写入
                        //有任务
                        //磁盘读
                        jxl.write.Number diskrioTaskNumber = new jxl.write.Number(1, 5, objectToDouble(diskrioTask, byteToMB));
                        wwsAgentStateMonitor.addCell(diskrioTaskNumber);
                        //磁盘写
                        jxl.write.Number diskwioTaskNumber = new jxl.write.Number(1, 6, objectToDouble(diskwioTask, byteToMB));
                        wwsAgentStateMonitor.addCell(diskwioTaskNumber);
                        //网络读
                        jxl.write.Number netrioTaskNumber = new jxl.write.Number(1, 7, objectToDouble(netrioTask, byteToMB));
                        wwsAgentStateMonitor.addCell(netrioTaskNumber);
                        //网络写
                        jxl.write.Number netwioTaskNumber = new jxl.write.Number(1, 8, objectToDouble(netwioTask, byteToMB));
                        wwsAgentStateMonitor.addCell(netwioTaskNumber);
                        //CPU
                        jxl.write.Number cpuusedTaskNumber = new jxl.write.Number(1, 9, objectToDouble(cpuusedTask, BigDecimal.ONE));
                        wwsAgentStateMonitor.addCell(cpuusedTaskNumber);
                        //内存
                        jxl.write.Number memusedTaskNumber = new jxl.write.Number(1, 10, objectToDouble(memusedTask, BigDecimal.ONE));
                        wwsAgentStateMonitor.addCell(memusedTaskNumber);
                        //无任务
                        //磁盘读
                        jxl.write.Number diskrioNoTaskNumber = new jxl.write.Number(2, 5, objectToDouble(diskrioNoTask, byteToMB));
                        wwsAgentStateMonitor.addCell(diskrioNoTaskNumber);
                        //磁盘写
                        jxl.write.Number diskwioNoTaskNumber = new jxl.write.Number(2, 6, objectToDouble(diskwioNoTask, byteToMB));
                        wwsAgentStateMonitor.addCell(diskwioNoTaskNumber);
                        //网络读
                        jxl.write.Number netrioNoTaskNumber = new jxl.write.Number(2, 7, objectToDouble(netrioNoTask, byteToMB));
                        wwsAgentStateMonitor.addCell(netrioNoTaskNumber);
                        //网络写
                        jxl.write.Number netwioNoTaskNumber = new jxl.write.Number(2, 8, objectToDouble(netwioNoTask, byteToMB));
                        wwsAgentStateMonitor.addCell(netwioNoTaskNumber);
                        //CPU
                        jxl.write.Number cpuusedNoTaskNumber = new jxl.write.Number(2, 9, objectToDouble(cpuusedNoTask, BigDecimal.ONE));
                        wwsAgentStateMonitor.addCell(cpuusedNoTaskNumber);
                        //内存
                        jxl.write.Number memusedNoTaskNumber = new jxl.write.Number(2, 10, objectToDouble(memusedNoTask, BigDecimal.ONE));
                        wwsAgentStateMonitor.addCell(memusedNoTaskNumber);

                        //获取任务汇总信息
                        GetAgentJobInfo getAgentJobInfoSummary = new GetAgentJobInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_TASK_INFO) + agentIPTemp + " summary " + starttime + " " + endtime);
                        getAgentJobInfoSummary.run();
                        retcode = getAgentJobInfoSummary.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentJobInfoSummary.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        AgentJobInfo agentJobInfoSummary = getAgentJobInfoSummary.AgentTaskInfo();
                        //TODO Excel输出
                        //任务总数
                        jxl.write.Number agentJobInfoTotal = new jxl.write.Number(1, 18, objectToDouble(agentJobInfoSummary.getSumtask(), BigDecimal.ONE));
                        wwsAgentStateMonitor.addCell(agentJobInfoTotal);
                        //失败数量
                        jxl.write.Number agentJobInfoError = new jxl.write.Number(1, 19, objectToDouble(agentJobInfoSummary.getErrortask(), BigDecimal.ONE));
                        wwsAgentStateMonitor.addCell(agentJobInfoError);

                        //获取任务详细信息
                        GetAgentJobInfo getAgentJobInfoDetail = new GetAgentJobInfo(
                                view,
                                ResourceCenter.getCmd(ResourceCenter.CMD_GET_AGENT_TASK_INFO) + agentIPTemp + " details " + starttime + " " + endtime);
                        getAgentJobInfoDetail.run();
                        retcode = getAgentJobInfoDetail.getRetCode();
                        if (retcode != 0) {
                            errorFlag = true;
                            errMsg = getAgentJobInfoDetail.getErrMsg();
                            SanBootView.log.error(getClass().getName(), " sdhmMirrorReportError: " + errMsg);
                            setOver(true);
                            return;
                        }
                        AgentJobInfo agentJobInfoDetail = getAgentJobInfoDetail.AgentTaskInfo();
                        Vector agentTaskStartTime = agentJobInfoDetail.getAgentTaskStartTime();
                        Vector agentTaskEndTime = agentJobInfoDetail.getAgentTaskEndTime();
                        Vector agentTaskType = agentJobInfoDetail.getAgentTaskType();
                        Vector agentTaskResult = agentJobInfoDetail.getAgentTaskResult();
                        Vector agentTaskMessage = agentJobInfoDetail.getAgentTaskMessage();
                        //TODO Excel输出
                        for (int indexTask = 0; indexTask < agentTaskStartTime.size(); indexTask++) {
                            String agentTaskStartTimeTemp = agentTaskStartTime.get(indexTask).toString();
                            String agentTaskEndTimeTemp = agentTaskEndTime.get(indexTask).toString();
                            String agentTaskTypeTemp = agentTaskType.get(indexTask).toString();
                            String agentTaskResultTemp = agentTaskResult.get(indexTask).toString();
                            String agentTaskMessageTemp = agentTaskMessage.get(indexTask).toString();
                            //开始时间
                            DateTime agentTaskStartTimeDate = new DateTime(indexTask + 1, 21, sdfDate.parse(agentTaskStartTimeTemp), DateTime.GMT8);
                            wwsAgentStateMonitor.addCell(agentTaskStartTimeDate);
                            //结束时间
                            DateTime agentTaskEndTimeDate = new DateTime(indexTask + 1, 22, sdfDate.parse(agentTaskEndTimeTemp), DateTime.GMT8);
                            wwsAgentStateMonitor.addCell(agentTaskEndTimeDate);
                            //任务类型
                            Label agentTaskTypeTempLabel = new Label(indexTask + 1, 23, agentTaskTypeTemp);
                            wwsAgentStateMonitor.addCell(agentTaskTypeTempLabel);
                            //任务结果
                            Label agentTaskResultTempLabel = new Label(indexTask + 1, 24, agentTaskResultTemp);
                            wwsAgentStateMonitor.addCell(agentTaskResultTempLabel);
                            //任务信息
                            Label agentTaskMessageTempLabel = new Label(indexTask + 1, 25, agentTaskMessageTemp);
                            wwsAgentStateMonitor.addCell(agentTaskMessageTempLabel);

                        }



                    }
                }

                //远程镜像调度
                if (scheduleMonitorFlag &&(mirrorRelation!=null) ){
                    reportWritableWorkbook.copySheet(wwsScheduleMonitorTemplate.getName(), scheduleMonitorSheetName, ++newSheetNum);
                    reportWritableWorkbook.copySheet(wwsTransferMonitorTemplate.getName(), transferMonitorSheetName, ++newSheetNum);
                    WritableSheet wwsScheduleMonitor = reportWritableWorkbook.getSheet(scheduleMonitorSheetName);
                    WritableSheet wwsTransferMonitor = reportWritableWorkbook.getSheet(transferMonitorSheetName);
                    //镜像调度关系
                    Vector localUWSIp = mirrorRelation.getLocalUWSIp();
                    Vector localUWSPort = mirrorRelation.getLocalUWSPort();
                    Vector mirrorUWSIp = mirrorRelation.getMirrorUWSIp();
                    Vector mirrorUWSPort = mirrorRelation.getMirrorUWSPort();
                    for (int k = 0; (localUWSIp != null) && (k < localUWSIp.size()); k++) {
                        String localUWSIpTemp = localUWSIp.get(k).toString();
                        Label localUWSIpLabel = new Label(k + 1, 2, localUWSIpTemp);
                        wwsScheduleMonitor.addCell(localUWSIpLabel);
                        String localUWSPortTemp = localUWSPort.get(k).toString();
                        Label localUWSPortLabel = new Label(k + 1, 3, localUWSPortTemp);
                        wwsScheduleMonitor.addCell(localUWSPortLabel);
                    }
                    for (int k = 0; (mirrorUWSIp != null) && (k < mirrorUWSIp.size()); k++) {
                        String mirrorUWSIpTemp = mirrorUWSIp.get(k).toString();
                        Label mirrorUWSIpLabel = new Label(k + 1, 4, mirrorUWSIpTemp);
                        wwsScheduleMonitor.addCell(mirrorUWSIpLabel);
                        String mirrorUWSPortTemp = mirrorUWSPort.get(k).toString();
                        Label mirrorUWSPortLabel = new Label(k + 1, 5, mirrorUWSPortTemp);
                        wwsScheduleMonitor.addCell(mirrorUWSPortLabel);
                    }
                    //镜像任务信息
                    String mirrorJobSum = mirrorJobInfoSummary.getMirrorJobSum();
                    String mirrorJobErr = mirrorJobInfoSummary.getMirrorJobErr();
                    Vector mirrorJobDetailTime = mirrorJobInfoDetail.getMirrorJobDetailTime();
                    Vector mirrorJobDetailResult = mirrorJobInfoDetail.getMirrorJobDetailResult();
                    Vector mirrorJobDetailVersion = mirrorJobInfoDetail.getMirrorJobDetailVersion();
                    Vector mirrorJobReadTime = mirrorJobInfoRead.getMirrorJobReadTime();
                    Vector mirrorJobReadIO = mirrorJobInfoRead.getMirrorJobReadIO();
                    Vector mirrorJobWriteIO = mirrorJobInfoWrite.getMirrorJobWriteIO();

                    for (int indexSpace = 0; (mirrorJobReadTime != null) && (indexSpace < mirrorJobReadTime.size()); indexSpace++) {
                        DateTime diskInfo = new DateTime(indexSpace + 1, 2, sdfDate.parse(mirrorJobReadTime.get(indexSpace).toString()), DateTime.GMT8);
                        wwsTransferMonitor.addCell(diskInfo);
                    }
                    for (int indexSpace = 0; (mirrorJobReadIO != null) && (indexSpace < mirrorJobReadIO.size()); indexSpace++) {
                        jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 3, objectToDouble(mirrorJobReadIO.get(indexSpace), BigDecimal.ONE));
                        wwsTransferMonitor.addCell(diskInfo);
                    }
                    for (int indexSpace = 0; (mirrorJobWriteIO != null) && (indexSpace < mirrorJobWriteIO.size()); indexSpace++) {
                        jxl.write.Number diskInfo = new jxl.write.Number(indexSpace + 1, 4, objectToDouble(mirrorJobWriteIO.get(indexSpace), BigDecimal.ONE));
                        wwsTransferMonitor.addCell(diskInfo);
                    }

                    for (int indexSpace = 0; (mirrorJobDetailTime != null) && (indexSpace < mirrorJobDetailTime.size()); indexSpace++) {
                        DateTime diskInfo = new DateTime(indexSpace + 1, 22, sdfDate.parse(mirrorJobDetailTime.get(indexSpace).toString()), DateTime.GMT8);
                        wwsTransferMonitor.addCell(diskInfo);
                    }
                    for (int indexSpace = 0; (mirrorJobDetailResult != null) && (indexSpace < mirrorJobDetailResult.size()); indexSpace++) {
                        Label diskInfo = new Label(indexSpace + 1, 23, mirrorJobDetailResult.get(indexSpace).toString());
                        wwsTransferMonitor.addCell(diskInfo);
                    }
                    for (int indexSpace = 0; (mirrorJobDetailVersion != null) && (indexSpace < mirrorJobDetailVersion.size()); indexSpace++) {
                        Label diskInfo = new Label(indexSpace + 1, 24, mirrorJobDetailVersion.get(indexSpace).toString());
                        wwsTransferMonitor.addCell(diskInfo);
                    }

                    jxl.write.Number mirrorJobSumNumber = new jxl.write.Number(1, 26, objectToDouble(mirrorJobSum, BigDecimal.ONE));
                    wwsTransferMonitor.addCell(mirrorJobSumNumber);

                    jxl.write.Number mirrorJobErrNumber = new jxl.write.Number(1, 27, objectToDouble(mirrorJobErr, BigDecimal.ONE));
                    wwsTransferMonitor.addCell(mirrorJobErrNumber);


                }

                //远程镜像传输
//                if(transferMonitorFlag && (mirrorUWSIP!=null)){
//                    if(mirrorUWSIP.equals(IP_ALL)){
//                        for(int k=0;k<mirrorUWSList.size();k++){
//                            reportWritableWorkbook.copySheet(wwsTransferMonitorTemplate.getName(), transferMonitorSheetName+String.valueOf(k+1), ++newSheetNum);
//                        }
//                    }else{
//                        reportWritableWorkbook.copySheet(wwsTransferMonitorTemplate.getName(), transferMonitorSheetName, ++newSheetNum);
//                    }
//                }

//                if(transferMonitorFlag){
//                    reportWritableWorkbook.copySheet(wwsTransferMonitorTemplate.getName(), transferMonitorSheetName, ++newSheetNum);
//                    WritableSheet wwsTransferMonitor = reportWritableWorkbook.getSheet(transferMonitorSheetName);
//                    //UWS镜像服务器传输变化信息
//                    //UWS磁盘传输采集时间
//                    for (int indexSpace = 0; indexSpace < sdhmUWSTransferTime.size(); indexSpace++ ){
//                        DateTime diskInfo = new DateTime(indexSpace+1, 2, sdfDate.parse(sdhmUWSTransferTime.get(indexSpace).toString()));
//                        wwsTransferMonitor.addCell(diskInfo);
//                    }
//                    //UWS磁盘传输读变化
//                    for (int indexSpace = 0; indexSpace < sdhmUWSTransferRead.size(); indexSpace++ ){
//                        jxl.write.Number diskInfo = new jxl.write.Number(indexSpace+1, 3, objectToDouble(sdhmUWSTransferRead.get(indexSpace), BigDecimal.ONE));
//                        wwsTransferMonitor.addCell(diskInfo);
//                    }
//                    //UWS磁盘传输写变化
//                    for (int indexSpace = 0; indexSpace < sdhmUWSTransferWrite.size(); indexSpace++ ){
//                        jxl.write.Number diskInfo = new jxl.write.Number(indexSpace+1, 4, objectToDouble(sdhmUWSTransferWrite.get(indexSpace), BigDecimal.ONE));
//                        wwsTransferMonitor.addCell(diskInfo);
//                    }
//
//                }

                //应急恢复
                if (recoveryMonitorFlag) {
                    reportWritableWorkbook.copySheet(wwsRecoveryMonitorTemplate.getName(), recoveryMonitorSheetName, ++newSheetNum);
                }

                //删除模板
                for (int templateIndex = 0; templateIndex < templateSheetNum; templateIndex++) {
                    reportWritableWorkbook.removeSheet(0);
                }
                reportWritableWorkbook.write();
                reportWritableWorkbook.close();
                templateWorkbook.close();


                //Header Sheet
//                Vector errorCreateTimeVector = sdhmReportError.getErrorCreateTime();
//                for (int i=0; i<errorCreateTimeVector.size(); i++){
//                    if(i < 26){
//                        Label errorCreateTimeLabel = (Label)wwsErrorInfo.getWritableCell(1,i+1); 
//                        errorCreateTimeLabel.setString(formatString(errorCreateTimeVector.get(i).toString()));                     
//                    }else{
//                        Label errorCreateTimeLabel = new Label(1,i+1,formatString(errorCreateTimeVector.get(i).toString()));  
//                        wwsErrorInfo.addCell(errorCreateTimeLabel);
//                    }
//                }                  
                //SpaceMonitor Sheet 
//                SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
//                SimpleDateFormat reportDateFormat = new SimpleDateFormat("yyyy-MM-dd");                  
//                Label reportHostIPLabel = (Label)wwsReport.getWritableCell(1,1);
//                reportHostIPLabel.setString(ip);    
//                Label reportStartTime = (Label)wwsReport.getWritableCell(1,2);
//                reportStartTime.setString(reportDateFormat.format(inDateFormat.parse(starttime)));    
//                Label reportEndTime = (Label)wwsReport.getWritableCell(1,3);
//                reportEndTime.setString(reportDateFormat.format(inDateFormat.parse(endtime)));    

                //BaseInfo Sheet
//                Vector hostIPVector = sdhmReportNormal.getHostip();
//                for (int i=0; i<hostIPVector.size(); i++){
//                    Label hostIPLabel = (Label)wwsBaseInfo.getWritableCell(i+1,1); 
//                    hostIPLabel.setString(hostIPVector.get(i).toString());                     
//                }
//
//                Vector hostNameVector = sdhmReportNormal.getHostname();
//                for (int i=0; i<hostNameVector.size(); i++){
//                    Label hostNameLabel = (Label)wwsBaseInfo.getWritableCell(i+1,2); 
//                    hostNameLabel.setString(hostNameVector.get(i).toString());                     
//                }     
//
//                Vector osVector = sdhmReportNormal.getOs();
//                for (int i=0; i<osVector.size(); i++){
//                    Label osLabel = (Label)wwsBaseInfo.getWritableCell(i+1,3); 
//                    osLabel.setString(osVector.get(i).toString());                     
//                }     
//
//                Vector osVersionVector = sdhmReportNormal.getOsversion();
//                for (int i=0; i<osVersionVector.size(); i++){
//                    Label osVersionLabel = (Label)wwsBaseInfo.getWritableCell(i+1,4); 
//                    osVersionLabel.setString(osVersionVector.get(i).toString());                     
//                }     
//
//                Vector diskLabelVector = sdhmReportNormal.getDisklabel();
//                for (int i=0; i<diskLabelVector.size(); i++){
//                    Label diskLabelLabel = (Label)wwsBaseInfo.getWritableCell(i+1,5); 
//                    diskLabelLabel.setString(diskLabelVector.get(i).toString());                     
//                }     
//
//                Vector diskSizeVector = sdhmReportNormal.getDisksize();
//                for (int i=0; i<diskSizeVector.size(); i++){
//                    Label diskSizeLabel = (Label)wwsBaseInfo.getWritableCell(i+1,6); 
//                    diskSizeLabel.setString(diskSizeVector.get(i).toString());                     
//                }     
//
//                Vector partitionLabelVector = sdhmReportNormal.getPartitionlabel();
//                for (int i=0; i<partitionLabelVector.size(); i++){
//                    Label partitionLabelLabel = (Label)wwsBaseInfo.getWritableCell(i+1,7); 
//                    partitionLabelLabel.setString(partitionLabelVector.get(i).toString());                     
//                }     
//
//                Vector partitionSizeVector = sdhmReportNormal.getPartitionsize();
//                for (int i=0; i<partitionSizeVector.size(); i++){
//                    Label partitionSizeLabel = (Label)wwsBaseInfo.getWritableCell(i+1,8); 
//                    partitionSizeLabel.setString(partitionSizeVector.get(i).toString());                     
//                }     
//
//                Vector cpuNameVector = sdhmReportNormal.getCpuname();
//                for (int i=0; i<cpuNameVector.size(); i++){
//                    Label cpuNameLabel = (Label)wwsBaseInfo.getWritableCell(i+1,9); 
//                    cpuNameLabel.setString(cpuNameVector.get(i).toString());                     
//                }     
//
//                Vector cpuDeviceIDVector = sdhmReportNormal.getCpudeviceid();
//                for (int i=0; i<cpuDeviceIDVector.size(); i++){
//                    Label cpuDeviceIDLabel = (Label)wwsBaseInfo.getWritableCell(i+1,10); 
//                    cpuDeviceIDLabel.setString(cpuDeviceIDVector.get(i).toString());                     
//                }     
//
//                Vector cpuSocketDesignationVector = sdhmReportNormal.getCpusocketdesignation();
//                for (int i=0; i<cpuSocketDesignationVector.size(); i++){
//                    Label cpuSocketDesignationLabel = (Label)wwsBaseInfo.getWritableCell(i+1,11); 
//                    cpuSocketDesignationLabel.setString(cpuSocketDesignationVector.get(i).toString());                     
//                }     
//
//                Vector cpuCurrentClockSpeedVector = sdhmReportNormal.getCpucurrentclockspeed();
//                for (int i=0; i<cpuCurrentClockSpeedVector.size(); i++){
//                    Label cpuCurrentClockSpeedLabel = (Label)wwsBaseInfo.getWritableCell(i+1,12); 
//                    cpuCurrentClockSpeedLabel.setString(cpuCurrentClockSpeedVector.get(i).toString());                     
//                }     
//
//                Vector cpuL2CacheSizeVector = sdhmReportNormal.getCpul2cachesize();
//                for (int i=0; i<cpuL2CacheSizeVector.size(); i++){
//                    Label cpuL2CacheSizeLabel = (Label)wwsBaseInfo.getWritableCell(i+1,13); 
//                    cpuL2CacheSizeLabel.setString(cpuL2CacheSizeVector.get(i).toString());                     
//                }     
//
//                Vector memoryDeviceIDVector = sdhmReportNormal.getMemorydeviceid();
//                for (int i=0; i<memoryDeviceIDVector.size(); i++){
//                    Label memoryDeviceIDLabel = (Label)wwsBaseInfo.getWritableCell(i+1,14); 
//                    memoryDeviceIDLabel.setString(memoryDeviceIDVector.get(i).toString());                     
//                }     
//
//                Vector memoryCapacityVector = sdhmReportNormal.getMemorycapacity();
//                for (int i=0; i<memoryCapacityVector.size(); i++){
//                    Label memoryCapacityLabel = (Label)wwsBaseInfo.getWritableCell(i+1,15); 
//                    memoryCapacityLabel.setString(memoryCapacityVector.get(i).toString());                     
//                }     
//
//                Vector adapterMacVector = sdhmReportNormal.getAdaptermac();
//                for (int i=0; i<adapterMacVector.size(); i++){
//                    Label adapterMacLabel = (Label)wwsBaseInfo.getWritableCell(i+1,16); 
//                    adapterMacLabel.setString(adapterMacVector.get(i).toString());                     
//                }     
//
//                Vector adapterNameVector = sdhmReportNormal.getAdaptername();
//                for (int i=0; i<adapterNameVector.size(); i++){
//                    Label adapterNameLabel = (Label)wwsBaseInfo.getWritableCell(i+1,17); 
//                    adapterNameLabel.setString(adapterNameVector.get(i).toString());                     
//                }     
//
//                Vector adapterNetConnectionStatusVector = sdhmReportNormal.getAdapternetconnectionstatus();
//                for (int i=0; i<adapterNetConnectionStatusVector.size(); i++){
//                    Label adapterNetConnectionStatusLabel = (Label)wwsBaseInfo.getWritableCell(i+1,18); 
//                    adapterNetConnectionStatusLabel.setString(adapterNetConnectionStatusVector.get(i).toString());                     
//                }     
//
//                Vector adapterManufacturerVector = sdhmReportNormal.getAdaptermanufacturer();
//                for (int i=0; i<adapterManufacturerVector.size(); i++){
//                    Label adapterManufacturerLabel = (Label)wwsBaseInfo.getWritableCell(i+1,19); 
//                    adapterManufacturerLabel.setString(adapterManufacturerVector.get(i).toString());                     
//                }     
//
//                Vector videoDeviceIDVector = sdhmReportNormal.getVideodeviceid();
//                for (int i=0; i<videoDeviceIDVector.size(); i++){
//                    Label videoDeviceIDLabel = (Label)wwsBaseInfo.getWritableCell(i+1,20); 
//                    videoDeviceIDLabel.setString(videoDeviceIDVector.get(i).toString());                     
//                }     
//
//                Vector videoNameVector = sdhmReportNormal.getVideoname();
//                for (int i=0; i<videoNameVector.size(); i++){
//                    Label videoNameLabel = (Label)wwsBaseInfo.getWritableCell(i+1,21); 
//                    videoNameLabel.setString(videoNameVector.get(i).toString());                     
//                }     
//
//                Vector boardManufacturerVector = sdhmReportNormal.getBoardmanufacturer();
//                for (int i=0; i<boardManufacturerVector.size(); i++){
//                    Label boardManufacturerLabel = (Label)wwsBaseInfo.getWritableCell(i+1,22); 
//                    boardManufacturerLabel.setString(boardManufacturerVector.get(i).toString());                     
//                }     
//
//                Vector boardProductVector = sdhmReportNormal.getBoardproduct();
//                for (int i=0; i<boardProductVector.size(); i++){
//                    Label boardProductLabel = (Label)wwsBaseInfo.getWritableCell(i+1,23); 
//                    boardProductLabel.setString(boardProductVector.get(i).toString());                     
//                } 
//                
//                for (int sheetindex=0; sheetindex<3; sheetindex++){
//                    SDHMReport sdhmReport;
//                    WritableSheet wwsInfo;
//                    if (sheetindex==0){
//                        sdhmReport = sdhmReportNormal;
//                        wwsInfo = wwsNormalInfo;
//                    }else if(sheetindex==1){
//                        sdhmReport = sdhmReportTask;
//                        wwsInfo = wwsTaskInfo;
//                    }else{
//                        sdhmReport = sdhmReportNoTask;
//                        wwsInfo = wwsNoTaskInfo;
//                    }
//                    //Info Sheet
//                    Vector thrMemoryUsedVector = sdhmReport.getThrmemoryused();
//                    for (int i=0; i<thrMemoryUsedVector.size(); i++){
//                        jxl.write.Number thrMemoryUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(1,2); 
//                        double doubleValue = stringToDouble(thrMemoryUsedVector.get(i).toString());
//                        thrMemoryUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector avgMemoryUsedVector = sdhmReport.getAvgmemoryused();
//                    for (int i=0; i<avgMemoryUsedVector.size(); i++){
//                        jxl.write.Number avgMemoryUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(1,3);
//                        double doubleValue = stringToDouble(avgMemoryUsedVector.get(i).toString());
//                        avgMemoryUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector maxMemoryUsedVector = sdhmReport.getMaxmemoryused();
//                    for (int i=0; i<maxMemoryUsedVector.size(); i++){
//                        jxl.write.Number maxMemoryUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(1,4); 
//                        double doubleValue = stringToDouble(maxMemoryUsedVector.get(i).toString());
//                        maxMemoryUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector minMemoryUsedVector = sdhmReport.getMinmemoryused();
//                    for (int i=0; i<minMemoryUsedVector.size(); i++){
//                        jxl.write.Number mixMemoryUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(1,5);
//                        double doubleValue = stringToDouble(minMemoryUsedVector.get(i).toString());
//                        mixMemoryUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector createTimeMemoryUsedVector = sdhmReport.getCreatetimememoryused();
//                    Label timesMemoryUsedLabel = (Label)wwsInfo.getWritableCell(1,6);
//                    timesMemoryUsedLabel.setString(String.valueOf(createTimeMemoryUsedVector.size()));   
//                    for (int i=0; i<createTimeMemoryUsedVector.size(); i++){                   
//                        if (i < 20){
//                            Label createTimeMemoryUsedLabel = (Label)wwsInfo.getWritableCell(1,7+i); 
//                            createTimeMemoryUsedLabel.setString(formatString(createTimeMemoryUsedVector.get(i).toString()));                     
//                        }else{
//                            Label createTimeMemoryUsedLabel = new Label(1,7+i,formatString(createTimeMemoryUsedVector.get(i).toString()));  
//                            wwsInfo.addCell(createTimeMemoryUsedLabel);
//                        }
//                    }     
//
//                    Vector thrCpuUsedVector = sdhmReport.getThrcpuused();
//                    for (int i=0; i<thrCpuUsedVector.size(); i++){
//                        jxl.write.Number thrCpuUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(2,2); 
//                        double doubleValue = stringToDouble(thrCpuUsedVector.get(i).toString());
//                        thrCpuUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector avgCpuUsedVector = sdhmReport.getAvgcpuused();
//                    for (int i=0; i<avgCpuUsedVector.size(); i++){
//                        jxl.write.Number avgCpuUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(2,3); 
//                        double doubleValue = stringToDouble(avgCpuUsedVector.get(i).toString());
//                        avgCpuUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector maxCpuUsedVector = sdhmReport.getMaxcpuused();
//                    for (int i=0; i<maxCpuUsedVector.size(); i++){
//                        jxl.write.Number maxCpuUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(2,4); 
//                        double doubleValue = stringToDouble(maxCpuUsedVector.get(i).toString());
//                        maxCpuUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector minCpuUsedVector = sdhmReport.getMincpuused();
//                    for (int i=0; i<minCpuUsedVector.size(); i++){
//                        jxl.write.Number mixCpuUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(2,5); 
//                        double doubleValue = stringToDouble(minCpuUsedVector.get(i).toString());
//                        mixCpuUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector createTimeCpuUsedVector = sdhmReport.getCreatetimecpuused();
//                    Label timesCpuUsedLabel = (Label)wwsInfo.getWritableCell(2,6);
//                    timesCpuUsedLabel.setString(String.valueOf(createTimeCpuUsedVector.size()));   
//                    for (int i=0; i<createTimeCpuUsedVector.size(); i++){
//                        if (i < 20){
//                            Label createTimeCpuUsedLabel = (Label)wwsInfo.getWritableCell(2,7+i); 
//                            createTimeCpuUsedLabel.setString(formatString(createTimeCpuUsedVector.get(i).toString()));                     
//                        }else{
//                            Label createTimeCpuUsedLabel = new Label(2,7+i,formatString(createTimeCpuUsedVector.get(i).toString()));  
//                            wwsInfo.addCell(createTimeCpuUsedLabel);
//                        }
//                    }     
//
//                    Vector thrDiskUsedVector = sdhmReport.getThrdiskused();
//                    for (int i=0; i<thrDiskUsedVector.size(); i++){
//                        jxl.write.Number thrDiskUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(3,2); 
//                        double doubleValue = stringToDouble(thrDiskUsedVector.get(i).toString());
//                        thrDiskUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector avgDiskUsedVector = sdhmReport.getAvgdiskused();
//                    for (int i=0; i<avgDiskUsedVector.size(); i++){
//                        jxl.write.Number avgDiskUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(3,3);
//                        double doubleValue = stringToDouble(avgDiskUsedVector.get(i).toString());
//                        avgDiskUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector maxDiskUsedVector = sdhmReport.getMaxdiskused();
//                    for (int i=0; i<maxDiskUsedVector.size(); i++){
//                        jxl.write.Number maxDiskUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(3,4);
//                        double doubleValue = stringToDouble(maxDiskUsedVector.get(i).toString());
//                        maxDiskUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector minDiskUsedVector = sdhmReport.getMindiskused();
//                    for (int i=0; i<minDiskUsedVector.size(); i++){
//                        jxl.write.Number mixDiskUsedLabel = (jxl.write.Number)wwsInfo.getWritableCell(3,5); 
//                        double doubleValue = stringToDouble(minDiskUsedVector.get(i).toString());
//                        mixDiskUsedLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector createTimeDiskUsedVector = sdhmReport.getCreatetimediskused();
//                    Label timesDiskUsedLabel = (Label)wwsInfo.getWritableCell(3,6);
//                    timesDiskUsedLabel.setString(String.valueOf(createTimeDiskUsedVector.size()));   
//                    for (int i=0; i<createTimeDiskUsedVector.size(); i++){
//                        if (i < 20){
//                            Label createTimeDiskUsedLabel = (Label)wwsInfo.getWritableCell(3,7+i); 
//                            createTimeDiskUsedLabel.setString(formatString(createTimeDiskUsedVector.get(i).toString()));                     
//                        }else{
//                            Label createTimeDiskUsedLabel = new Label(3,7+i,formatString(createTimeDiskUsedVector.get(i).toString()));  
//                            wwsInfo.addCell(createTimeDiskUsedLabel);
//                        }
//                    }     
//
//                    Vector thrDiskIOVector = sdhmReport.getThrdiskio();
//                    for (int i=0; i<thrDiskIOVector.size(); i++){
//                        jxl.write.Number thrDiskIOLabel = (jxl.write.Number)wwsInfo.getWritableCell(4,2); 
//                        double doubleValue = stringToDouble(thrDiskIOVector.get(i).toString());
//                        thrDiskIOLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector avgDiskIOVector = sdhmReport.getAvgdiskio();
//                    for (int i=0; i<avgDiskIOVector.size(); i++){
//                        jxl.write.Number avgDiskIOLabel = (jxl.write.Number)wwsInfo.getWritableCell(4,3); 
//                        double doubleValue = stringToDouble(avgDiskIOVector.get(i).toString());
//                        avgDiskIOLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector maxDiskIOVector = sdhmReport.getMaxdiskio();
//                    for (int i=0; i<maxDiskIOVector.size(); i++){
//                        jxl.write.Number maxDiskIOLabel = (jxl.write.Number)wwsInfo.getWritableCell(4,4); 
//                        double doubleValue = stringToDouble(maxDiskIOVector.get(i).toString());
//                        maxDiskIOLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector minDiskIOVector = sdhmReport.getMindiskio();
//                    for (int i=0; i<minDiskIOVector.size(); i++){
//                        jxl.write.Number mixDiskIOLabel = (jxl.write.Number)wwsInfo.getWritableCell(4,5); 
//                        double doubleValue = stringToDouble(minDiskIOVector.get(i).toString());
//                        mixDiskIOLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector createTimeDiskIOVector = sdhmReport.getCreatetimediskio();
//                    Label timesDiskIOLabel = (Label)wwsInfo.getWritableCell(4,6);
//                    timesDiskIOLabel.setString(String.valueOf(createTimeDiskIOVector.size()));   
//                    for (int i=0; i<createTimeDiskIOVector.size(); i++){
//                        if (i < 20){
//                            Label createTimeDiskIOLabel = (Label)wwsInfo.getWritableCell(4,7+i); 
//                            createTimeDiskIOLabel.setString(formatString(createTimeDiskIOVector.get(i).toString()));                     
//                        }else{
//                            Label createTimeDiskIOLabel = new Label(4,7+i,formatString(createTimeDiskIOVector.get(i).toString()));  
//                            wwsInfo.addCell(createTimeDiskIOLabel);
//                        }
//                    }     
//
//                    Vector thrNetIOVector = sdhmReport.getThrnetio();
//                    for (int i=0; i<thrNetIOVector.size(); i++){
//                        jxl.write.Number thrNetIOLabel = (jxl.write.Number)wwsInfo.getWritableCell(5,2); 
//                        double doubleValue = stringToDouble(thrNetIOVector.get(i).toString());
//                        thrNetIOLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector avgNetIOVector = sdhmReport.getAvgnetio();
//                    for (int i=0; i<avgNetIOVector.size(); i++){
//                        jxl.write.Number avgNetIOLabel = (jxl.write.Number)wwsInfo.getWritableCell(5,3); 
//                        double doubleValue = stringToDouble(avgNetIOVector.get(i).toString());
//                        avgNetIOLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector maxNetIOVector = sdhmReport.getMaxnetio();
//                    for (int i=0; i<maxNetIOVector.size(); i++){
//                        jxl.write.Number maxNetIOLabel = (jxl.write.Number)wwsInfo.getWritableCell(5,4); 
//                        double doubleValue = stringToDouble(maxNetIOVector.get(i).toString());
//                        maxNetIOLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector minNetIOVector = sdhmReport.getMinnetio();
//                    for (int i=0; i<minNetIOVector.size(); i++){
//                        jxl.write.Number mixNetIOLabel = (jxl.write.Number)wwsInfo.getWritableCell(5,5); 
//                        double doubleValue = stringToDouble(minNetIOVector.get(i).toString());
//                        mixNetIOLabel.setValue(doubleValue);                     
//                    }     
//
//                    Vector createTimeNetIOVector = sdhmReport.getCreatetimenetio();
//                    Label timesNetIOLabel = (Label)wwsInfo.getWritableCell(5,6);
//                    timesNetIOLabel.setString(String.valueOf(createTimeNetIOVector.size()));   
//                    for (int i=0; i<createTimeNetIOVector.size(); i++){
//                        if (i < 20){
//                            Label createTimeNetIOLabel = (Label)wwsInfo.getWritableCell(5,7+i); 
//                            createTimeNetIOLabel.setString(formatString(createTimeNetIOVector.get(i).toString()));                     
//                        }else{
//                            Label createTimeNetIOLabel = new Label(5,7+i,formatString(createTimeNetIOVector.get(i).toString()));  
//                            wwsInfo.addCell(createTimeNetIOLabel);
//                        }
//                    } 
//                    
//                    if (sheetindex==0){
//                        wwsNormalInfo = wwsInfo;
//                    }else if(sheetindex==1){
//                        wwsTaskInfo = wwsInfo;
//                    }else{
//                        wwsNoTaskInfo = wwsInfo;
//                    }  
//                }

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

    public double bigDecimalToDouble(BigDecimal in, BigDecimal conversion) {
        double value = 0;
        int scale = 2;
        try {
            BigDecimal conversionIn = in.divide(conversion, scale, BigDecimal.ROUND_HALF_UP);
            value = conversionIn.doubleValue();
        } catch (Exception ex) {
            value = 0;
        }

        return value;
    }

    public double objectToDouble(Object in, BigDecimal conversion) {
        double value = 0;
        int scale = 2;
        try {
            BigDecimal temp = new BigDecimal(in.toString());
            BigDecimal conversionIn = temp.divide(conversion, scale, BigDecimal.ROUND_HALF_UP);
            value = conversionIn.doubleValue();
        } catch (Exception ex) {
            value = 0;
        }

        return value;
    }
}
