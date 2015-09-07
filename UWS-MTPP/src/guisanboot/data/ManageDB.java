package guisanboot.data;

/**
 * Title: Odysys UWS Description: Copyright: Copyright (c) 2006 Company: Odysys
 *
 * @author Odysys
 * @version 1.0
 */
import java.io.*;
import java.util.*;

import mylib.tool.*;
import guisanboot.res.*;
import guisanboot.ui.*;
import guisanboot.datadup.cmd.*;
import guisanboot.datadup.data.*;
import guisanboot.datadup.ui.EditProfileDialog;
import guisanboot.datadup.ui.MonitorInfoReceiver;
import guisanboot.remotemirror.iBootInfo.GetIBootInfoFromTFtp;
import guisanboot.unlimitedIncMj.model.GetCloneDiskListCmd;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import guisanboot.cmdp.entity.*;
import guisanboot.cmdp.service.*;
import guisanboot.remotemirror.cmd.GetMJScheduler;
import guisanboot.remotemirror.entity.MirrorJobSch;
import guisanboot.audit.data.*;
import guisanboot.cluster.cmd.GetCluster;
import guisanboot.cluster.cmd.GetHeartbeatDskInfo;
import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.entity.ClusterVolume;
import guisanboot.cluster.entity.SubCluster;
import guisanboot.cluster.cmd.CreateMirrorCluster;
import guisanboot.cluster.cmd.DeleteMirrorCluster;
import guisanboot.cluster.cmd.GetMirrorClusterInfo;
import guisanboot.cluster.entity.ClusterConfigInfo;
import guisanboot.ams.service.AmsBuildMirrorOnClnt;
import guisanboot.ams.service.AmsGetDiskPathFromTgtId;
import guisanboot.ams.service.InitAmsProgressInfoReceiver;
import guisanboot.lvm.service.*;
import guisanboot.ams.service.GetInitAmsProgress;
import guisanboot.ams.service.AmsGetRealDiskPathFromTgtId;
import guisanboot.vmmanage.service.*;

public class ManageDB {
    // 文件操作

    GetDhcpConfig getDhcp;
    GetConfigFile getConf;
    GetUWSRptConfigFile getUWSRptConf;
    NetworkRunningWithoutOutput rptMailTest;
    NetworkRunningWithoutOutput reloadUWSConf;
    IsDir isDir;
    ListDir listDir;
    ViewTextFile viewFile;
    GetErrorLog getErrLog;
    SendFileToSrv sendFileToSrv;
    GetSystemTime getSysTime;
    GetWindir getWindir;
    GetTargetServerName getTargetSrvName;
    GetFileLastModTime getLastModTime;
    NetworkRunningWithoutOutput mvFile;
    NetworkRunningWithoutOutput cpFile;
    NetworkRunningWithoutOutput delFile;
    NetworkRunningWithoutOutput findFile;
    NetworkRunningWithoutOutput mkdir;
    NetworkRunningWithoutOutput chmod;
    NetworkRunningWithoutOutput touchFile;
    NetworkRunningWithoutOutput diff;
    NetworkRunningWithoutOutput ln;
    NetworkRunningWithoutOutput mountIAHidenFs;
    GetPartitionOnClnt getPartition;
    GetPartitionByCatConf getPartitionByCatConf;
    GetVolInfoOnClnt getVolInfoForCMDP;
    GetMirrorClusterInfo getMirrorClusterInfo;
    GetUnixPartitionOnClnt getUnixPart;
    GetUnixNetCardOnClnt getUnixNetCard;
    GetUnixNetCardOnClnt getUnixNetCardFromSrv;
    GetServiceOnClnt getService;
    GetRstMapOnClnt getRstMapper;
    GetDiskPartitionTableForWinOnClnt getDiskPartForWin;
    GetDiskPartitionTableForWinOnClnt getOldDiskInfoForWin;
    GetDiskPartitionTableForWinOnClnt getNewDiskInfoForWin;
    GetDiskPartitionTableForUnixOnClnt getDiskPartForUnix;
    GetDiskPartitionTableForUnixOnClnt getOldDiskInfoForUnix;
    GetDiskPartitionTableForUnixOnClnt getNewDiskInfoForUnix;
    FormatPartitionOnDiskForUnix formatDiskPartForUnix;
    GetUnixRstMapOnClnt getUnixRstMapper;
    GetOsLoaderTypeOnClnt getOsLoaderTypeOnClnt;
    NetworkRunningWithoutOutput formatVol;
    NetworkRunningWithoutOutput set_efi_partition_flag;
    NetworkRunningWithoutOutput addPortal;
    NetworkRunningWithoutOutput loginTarget;
    NetworkRunningWithoutOutput logoutTarget;
    ListTargetDiskOnClnt listTDisk;
    GetIpInfoOnClnt getIpInfo;
    GetIpInfoOnClnt getNetCardFromSrv;
    SyncOpCmdWithNoOutput modyHostBoot;
    NetworkRunningWithoutOutput copyOS;
    NetworkRunningWithoutOutput regOS;
    GetTargetDriver getTargetDrv;
    IsStartupFromSAN isStartupFromSAN;
    IsStartupFromNetBoot isStartupFromNetBoot;
    GetIdleDriveLetter getIdleDriveLetter;
    StartorStopService startService;
    NetworkRunningWithoutOutput loadInfo;
    NetworkRunningWithoutOutput assignDriver;
    NetworkRunningWithoutOutput mntDriver;
    SetPartitionActive setPartitionActive;
    GetNetInfoFromMDB getNetInfo;
    GetLVMInfoFromPXELinux getLVMInfo;
    GetUnixNetCardInfo getUnixNetCardInfo;
    NetworkRunningWithoutOutput bindTgtVolAndMac;
    GetPersistentList getPersistentTarget;
    NetworkRunningWithoutOutput addPersistentTarget;
    NetworkRunningWithoutOutput delPersistentTarget;
    NetworkRunningWithoutOutput rebootHost;
    NetworkRunningWithoutOutput sendNetConf;
    GetWinPlatform getWinPlatform;
    GetLVMType getLVMType;
    IsCrtVGOnTgt isCrtVgOnTgt;
    IsCrtLVOnTgt isCrtLVOnTgt;
    DelAllInVG delLVMVG;
    MountLV mntLV;
    GetTftpRootPath getTftpRootPath;
    GetIScsiCmdPath getIscsiCmdPath;
    NetworkRunningWithoutOutput sendFileFromAgtToSrv;
    NetworkRunningWithoutOutput umountFS;
    NetworkRunningWithoutOutput mountPool;
    NetworkRunningWithoutOutput mkfs;
    NetworkRunningWithoutOutput setBootInfoEnvVar;
    NetworkRunningWithoutOutput getBootInfoVar;
    NetworkRunningWithoutOutput setIBootConfigure;
    GenerateIBootKernelImg genKernelImg;
    GenerateIBootInitrd genInitrd;
    GenerateIBootPxeLinux genPxeLinux;
    NetworkRunningWithoutOutput chgFsLabel;
    NetworkRunningWithoutOutput vgOnline;
    NetworkRunningWithoutOutput vgOffline;
    NetworkRunningWithoutOutput modBootConf;
    SetUnixActive setUnixActive;
    GetOsLoaderType getOsLoader;
    NetworkRunningWithoutOutput addIscsiInitDriver;
    GetClntIDFromObjID getClntIDFromObjId;
    NetworkRunningWithoutOutput changeRegisterForIP;
    GetKernelVer getKernelVer;
    GetPool getPool;
    GetPool getPoolofRemoteUWS;
    GetPoolInfo getPoolInfo;
    GetPoolInfo getPoolInfoOfRemoteUWS;
    GetIdleDisk getIdleDisk;
    GetUCSDiskInfo getUcsDiskInfo;
    GetUcsDiskCountByView getucsdiskcount;
    GetOSvolTidOfIBoot getOsVolTid;
    GetOrphanVol getorphanvol;
    CreateResVol crtResVol;
    NetworkRunningWithoutOutput delResVol;
    CreateUcsPool crtUcsPool;
    NetworkRunningWithoutOutput delUcsPool;
    NetworkRunningWithoutOutput expandVG;
    GetShareName getShareName;
    GetLicence getLic;
    AnalyMpFromDir anlyMp;
    GetBriefVDisk getBrVdisk;
    GetAgentUUID getAgntUUID;
    NetworkRunningWithoutOutput reCrtUUID;
    NetworkRunningWithoutOutput setUUID;
    GetIScsiSessionObj getIScsiSession;
    GetServerNetInterface getIf;
    NetworkRunningWithoutOutput adjustDriver;
    ListProfile listProf;
    GetBakSet getBkset;
    GetTaskLog getOneTaskLog;
    GetTargetByLetter getTgtByLetter;
    GetPXEInfoFromTFtp getPxeInfo;
    GetIBootInfoFromTFtp getIBootInfo;
    Get3rdDhcpInfo get3rdDhcpInfo;
    GetHeartbeatDskInfo getHbDskInfo;
    NetworkRunningWithoutOutput ziporunzip;
    NetworkRunningWithoutOutput touchAFile;
    GetHostIDOnMac getHostIDOnMac;
    NetworkRunningWithoutOutput delTaskLogInfo;
    NetworkRunningWithoutOutput powerdown;
    GetServiceOnVolume getServiceOnVol;
    NetworkRunningWithoutOutput ib_driver_comp;
    GetIscsiIP getIscsiIP;
    // 自动为新盘分区格式化
    NetworkRunningWithoutOutput rstPartition;
    ListPartitionForWinOnDisk listPartition;
    FormatPartitionOnDiskForWin formatPartition;
    FormatPartitionOnDiskForWin lvFormatPartition;
    NetworkRunningWithoutOutput mntPartition;
    // iboot setup
    GetIbootObjList getIbootList;
    NetworkRunningWithoutOutput delIboot;
    NetworkRunningWithoutOutput addIboot;
    // user 
    GetBackupUser getBakUser;
    AddCmdNetworkRunning addUser;
    NetworkRunningWithoutOutput modUser;
    NetworkRunningWithoutOutput delUser;
    // volume map
    GetVolumeMap getAllVolMap;
    GetVolumeMap getOneVolMap;
    GetVolumeMap queryVolMapOnDestUWSrv;
    NetworkRunningWithoutOutput addVolMap;
    NetworkRunningWithoutOutput modVolMap;
    NetworkRunningWithoutOutput delVolMap;
    // volume 
    AddOrphanVol addVol;
    NetworkRunningWithoutOutput delVol;
    GetVolSize getVolSize;
    GetVGSize getVgSize;
    IsCreatedVg isCrtVg;
    NetworkRunningWithoutOutput onlineVol;
    NetworkRunningWithoutOutput offlineVol;
    // snapshot
    QueryVSnapDB queryVSnapDB;
    QueryVSnapDB1 queryVSnapDB1;
    GetSnapshot getSnap;
    NetworkRunningWithoutOutput addSnap;
    AddSnapshot addSnap1;
    CreateVersion addSnapForCmdp;
    NetworkRunningWithoutOutput delSnap;
    CommonCMDPCommand delSnapForCmdp;
    NetworkRunningWithoutOutput expSnap;
    NetworkRunningWithoutOutput modSnap;
    NetworkRunningWithoutOutput rolldisk; // rollback from snapshot to disk
    GetSnapName getSnapName;
    GetSnapSize getSnapSize;//by hwh
    // view
    GetView getView;
    AddView addView;
    NetworkRunningWithoutOutput delView;
    UcsForwordView ucsForwordView;
    // lunmap
    GetLunMap1 getLunMap;
    NetworkRunningWithoutOutput delLM;
    NetworkRunningWithoutOutput addLM;
    // client 
    GetBootHost getBootHost;
    GetBootHost getOneBootHost;
    GetBootHost queryBootHostOnDestUWSrv;
    AddBootHostNetworkRunning addBootHost;
    NetworkRunningWithoutOutput modBootHost;
    NetworkRunningWithoutOutput delBootHost;
    // uws server
    GetUWSrvNode getUWSrv;
    GetUWSrvNode queryUWSrvOnDestUWSrv;
    AddBootHostNetworkRunning addUWSrv;
    NetworkRunningWithoutOutput modUWSrv;
    NetworkRunningWithoutOutput delUWSrv;
    // mg
    GetMGList getMgList;
    GetMGList getOneMg;
    AddBootHostNetworkRunning addMg;
    NetworkRunningWithoutOutput modMg;
    NetworkRunningWithoutOutput delMg;
    NetworkRunningWithoutOutput checkMg;
    NetworkRunningWithoutOutput startMg;
    NetworkRunningWithoutOutput stopMg;
    NetworkRunningWithoutOutput sendSigToMg;
    // mj
    GetMJList getMjList;
    GetMJList monMj;
    AddCmdNetworkRunning addMj;
    NetworkRunningWithoutOutput modMj;
    NetworkRunningWithoutOutput delMj;
    NetworkRunningWithoutOutput switchMj;
    NetworkRunningWithoutOutput msputfile;
    GetMJScheduler getMjSch;
    AddCmdNetworkRunning addMjSch;
    NetworkRunningWithoutOutput modMjSch;
    NetworkRunningWithoutOutput delMjSch;
    // srcagent
    GetSrcAgnt getSrcAgnt;
    GetSrcAgnt querySrcAgntOnDestUWSrv;
    AddCmdNetworkRunning addSrcAgnt;
    NetworkRunningWithoutOutput delSrcAgnt;
    NetworkRunningWithoutOutput modSrcAgnt;
    // mirrorDiskInfo
    GetMirrorDiskInfo getMdi;
    GetMirrorDiskInfo queryMdiOnDestUWSrv;
    AddCmdNetworkRunning addMdi;
    NetworkRunningWithoutOutput modMdi;
    NetworkRunningWithoutOutput delMdi;
    NetworkRunningWithoutOutput destroyDisk;
    // clone disk
    GetCloneDiskListCmd updateCloneDisk;
    GetCloneDiskListCmd getCloneDiskListCmd;
    AddCmdNetworkRunning addCloneDisk;
    NetworkRunningWithoutOutput delCloneDisk;
    NetworkRunningWithoutOutput modCloneDisk;
    AddCmdNetworkRunning getUISnapSum;
    // mirrorSnapUsage
    GetMirrorSnapusage getMSU;
    AddCmdNetworkRunning addMSU;
    NetworkRunningWithoutOutput delMSU;
    NetworkRunningWithoutOutput modMSU;
    // netbootedHost
    GetNetBootedHost getNBH;
    AddCmdNetworkRunning addNBH;
    NetworkRunningWithoutOutput delNBH;
    NetworkRunningWithoutOutput modNBH;
    // service map
    GetServMap getServMap;
    AddCmdNetworkRunning addServMap;
    NetworkRunningWithoutOutput delServMap;
    // report
    AddCmdNetworkRunning addReport;
    // lvm op
    NetworkRunningWithoutOutput addVg;
    NetworkRunningWithoutOutput delVg;
    NetworkRunningWithoutOutput addLV;
    NetworkRunningWithoutOutput delLV;
    /*
     ** 备份和恢复管理数据
     */
    NetworkRunningWithoutOutput bakMdb;
    NetworkRunningWithoutOutput rstMdb;
    /*
     ** 管理数据库中的调度信息
     */
    GetSchList getSchList;
    AddCmdNetworkRunning addSch;
    NetworkRunningWithoutOutput modSch;
    NetworkRunningWithoutOutput delSch;
    /*
     ** pool
     */
    AddCmdNetworkRunning addPool;
    NetworkRunningWithoutOutput delPool;
    /*
     ** task
     */
    GetBakTask getTaskList;
    NetworkRunningWithoutOutput delTask;
    NetworkRunningWithoutOutput killTask;
    NetworkRunningWithoutOutput delTaskLog;
    NetworkRunningWithoutOutput delAllTskLog;
    NetworkRunningWithoutOutput activeTask;

    /*
     ** Audit
     */
    AddCmdNetworkRunning addAudit;
    NetworkRunningWithoutOutput delAudit;
    NetworkRunningWithoutOutput delAllAudit;

    /**
     *  Mjob
     */
    NetworkRunningWithoutOutput delAllMjob;
    /*
     * bakset
     */
    NetworkRunningWithoutOutput modBakset;

    /*
     * Cluster
     */
    GetCluster getCluster;
    AddCmdNetworkRunning addCluster;
    NetworkRunningWithoutOutput modCluster;
    NetworkRunningWithoutOutput delCluster;

    /*
     ** login and logout
     */
    Logout logout;
    Login login;
//    SyncOpCmdWithoutput syncOPWithOutput;
    SyncOpCmdWithNoOutput syncOPWithNoOutput;
    GetCPProcess getCPProcess;
    GetUnixCPProcess getUnixCPProcess;
    Map<Long, Object> tskSummaryMap = new HashMap<Long, Object>();
    /*
     ** duplicate profile，详细描述一次duplicate的信息
     */
    private ArrayList<UniProfile> profileList = new ArrayList<UniProfile>();

    /*
     ** physical protect strategy, 描述自动创建版本的信息
     */
    private ArrayList<PPProfile> ppprofileList = new ArrayList<PPProfile>();

    /*
     ** 备份对象���ݶ���
     */
    GetBakObjList getBakObjList;
    GetBakObjList getOneBakObjList;
    AddCmdNetworkRunning addBakObj;
    NetworkRunningWithoutOutput modBakObj;
    NetworkRunningWithoutOutput delBakObj;
    /*
     ** d2d client
     */
    GetBakClient getBakClnt;
    AddCmdNetworkRunning addNode;
    NetworkRunningWithoutOutput modNode;
    NetworkRunningWithoutOutput delNode;

    /*
     ** CMDP command
     */
    GetDriveGroup getDriveGrp;
    GetDatabaseInfoOnVolume getDbInfo;
    NetworkRunningWithoutOutput crtDG;
    NetworkRunningWithoutOutput delDG;
    NetworkRunningWithoutOutput addDiskIntoDG;
    NetworkRunningWithoutOutput delDiskFromDG;
    NetworkRunningWithoutOutput modAutoCrtSnapParameter;
    NetworkRunningWithoutOutput modServicesOnVol;
    NetworkRunningWithoutOutput modDbinfoOnVol;
    ModVolInfo modVolInfo;
    GetInitProgress getCurrentSyncState;
    CommonCMDPCommand setCurrentStateOfSync;
    CommonCMDPCommand getWorkStateOfSync;
    CommonCMDPCommand setWorkStateOfSync;
    CommonCMDPCommand getHAInfo;
    CommonCMDPCommand setHAInfo;
    BuildMirrorOnClnt buildMirror;
    CommonCMDPCommand delMirrorOnClnt;
    CommonCMDPCommand destoryMirror;
    SetNetBootVersion setNetBootVersion;
    NetworkRunningWithoutOutput getFileFromS2A;  // 利用tftp机制
    CommonCMDPCommand recoverInvalidMirror;
    CommonCMDPCommand buildRestoreMirror;
    CommonCMDPCommand buildMirrorAftRestore;
    CommonCMDPCommand chkdiskOnWin;
    GetVolInfoForTarget getVolInfoForTgt;
    GetMirrorringInfo getMirroringInfo;
    CommonCMDPCommand setInitType;
    CommonCMDPCommand flushDisk;
    CommonCMDPCommand modDBAftRestoreOriDisk;
    CommonCMDPCommand zero_uuid_sector;
    CreateMirrorCluster createMirrorCluster;
    DeleteMirrorCluster deleteMirrorCluster;
    CheckVolAftRegister checkVolumeAftRegister;
    /**
     * ams
     */
    AmsBuildMirrorOnClnt amsbuildMirror;
    LvmCmd lvmCmd;
    AmsGetDiskPathFromTgtId amsGetDiskPath;
    AmsGetRealDiskPathFromTgtId amsGetRealDiskPath;
    GetInitAmsProgress getCurrentAmsSyncState;
    NetworkRunningWithoutOutput delAmsProtect;
    NetworkRunningWithoutOutput setAmsWorkState;
    NetworkRunningWithoutOutput amsSwitchNetDisk;
    NetworkRunningWithoutOutput restoreNetDisk;
    NetworkRunningWithoutOutput amsSwitchLocal;
    NetworkRunningWithoutOutput amsparted;
    NetworkRunningWithoutOutput amsRebuildMirror;
    NetworkRunningWithoutOutput forwordUcsSnap;
    //license count
    GetLicenseCount getLicenseCount;
    //txip get
    GetTxIPFromIP getTxIPFromIP;
    //VMmanange
    GetVMHostInfo getVMHostInfo;
    GetVMServerInfo getVMServerInfo;
    GetVMHostIPConfig getVMHostIPConfig;
    GetIdleAndUsedVip getIdleAndUsedVip;
    NetworkRunningWithoutOutput addVMHost;
    NetworkRunningWithoutOutput modVMHost;
    NetworkRunningWithoutOutput delVMHost;
    NetworkRunningWithoutOutput vmHostOper;
    NetworkRunningWithoutOutput enbaleIboot;
    GetUWSDefaultIP getUWSDefaultIp;
    GetVMInfoFromHost getVMInfoFromHost;
    NetworkRunningWithoutOutput crtVMMachine;
    HasVMServiceInfoOrNot hasVMserviceInfoOrNot;
    NetworkRunningWithoutOutput crtVMServiceInfo;
    NetworkRunningWithoutOutput bindviewMac;
    GetLinuxDevName getLinuxDevName;
    NetworkRunningWithoutOutput setupIbootLinux64;
//    VMHostOperationService vmHostOPS;
    boolean debug = true;
    SanBootView view;
    int oldTimeOut = 300000; // 300 sec.
    String errorMsg = "";

    public String getErrorMessage() {
        if (errorMsg.equals("")) {
            return SanBootView.res.getString("common.failed");
        } else {
            if (errorMsg.length() > 80) {
                return errorMsg.substring(0, 76) + "....";
            } else {
                return errorMsg;
            }
        }
    }
    int errorCode;

    public int getErrorCode() {
        return errorCode;
    }
    int newId;

    public int getNewId() {
        return newId;
    }

    private void recordNewId(AddCmdNetworkRunning running) {
        newId = running.getNewId();
    }

    public boolean finished(AbstractNetworkRunning running) {
        boolean ret = running.isOKForExcuteThisCmd();
        if (!ret) {
            this.errorMsg = running.getErrMsg();
            this.errorCode = running.getRetCode();
        }
        return ret;
    }

    public void recordException(AbstractNetworkRunning running, Exception ex) {
        running.setExceptionErrMsg(ex);
        running.setExceptionRetCode(ex);
    }

    public String getGeneralErrorMsg(int errcode) {
        if (errcode == ResourceCenter.ERRCODE_BadMagic
                || errcode == ResourceCenter.ERRCODE_BadLen
                || errcode == ResourceCenter.ERRCODE_BadVer) {
            return SanBootView.res.getString("common.errmsg.format");
        } else if (errcode == ResourceCenter.ERRCODE_TIMEOUT) {
            return SanBootView.res.getString("common.errmsg.timeout");
        } else if (errcode == ResourceCenter.ERRCODE_NETIO) {
            return SanBootView.res.getString("common.error.io");
        } else { // general errors(unknows)
            return SanBootView.res.getString("common.errcode.unknown");
        }
    }

    public void addOne(long tsk_id, Object obj) {
        this.tskSummaryMap.put(new Long(tsk_id), obj);
    }

    public Object getOne(long tsk_id) {
        return tskSummaryMap.get(new Long(tsk_id));
    }

    public void rmOne(long tsk_id) {
        tskSummaryMap.remove(new Long(tsk_id));
    }

    public boolean restoreOldTimeOut() {
        try {
            view.getSocket().setSoTimeout(oldTimeOut);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int getOldTimeOut() {
        return oldTimeOut;
    }

    public int getCurTimeOut() {
        try {
            return view.getSocket().getSoTimeout();
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public boolean setNewTimeOut(int val) {
        try {
            oldTimeOut = view.getSocket().getSoTimeout();
            view.getSocket().setSoTimeout(val);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isMTPPCmd(int cmdType) {
        return (cmdType == ResourceCenter.CMD_TYPE_MTPP);
    }

    public ManageDB(SanBootView _view) {
        view = _view;

        // file op
        getDhcp = new GetDhcpConfig(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        getConf = new GetConfigFile(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_CONF));
        getUWSRptConf = new GetUWSRptConfigFile(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + ResourceCenter.CONF_UWS_REPORT);
        rptMailTest = new NetworkRunningWithoutOutput();
        reloadUWSConf = new NetworkRunningWithoutOutput();

        isDir = new IsDir(
                ResourceCenter.getCmd(ResourceCenter.CMD_IS_DIR));
        listDir = new ListDir(
                ResourceCenter.getCmd(ResourceCenter.CMD_LIST_DIR));
        viewFile = new ViewTextFile(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        getErrLog = new GetErrorLog(
                ResourceCenter.getCmd(ResourceCenter.CMD_VIEW_ERRLOG));
        getSysTime = new GetSystemTime(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_SYSTEM_TIME));
        getWindir = new GetWindir(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_WINDIR));
        getTargetSrvName = new GetTargetServerName(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_HOST_NAME));
        getLastModTime = new GetFileLastModTime(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_FILE_MODTIME));
        sendFileToSrv = new SendFileToSrv();
        mvFile = new NetworkRunningWithoutOutput();
        cpFile = new NetworkRunningWithoutOutput();
        delFile = new NetworkRunningWithoutOutput();
        findFile = new NetworkRunningWithoutOutput();
        mkdir = new NetworkRunningWithoutOutput();
        chmod = new NetworkRunningWithoutOutput();
        touchFile = new NetworkRunningWithoutOutput();
        diff = new NetworkRunningWithoutOutput();
        ln = new NetworkRunningWithoutOutput();
        mountIAHidenFs = new NetworkRunningWithoutOutput();

        getPartition = new GetPartitionOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_PARTITION));
        getPartitionByCatConf = new GetPartitionByCatConf(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_PARTITION));
        getVolInfoForCMDP = new GetVolInfoOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_PARTITION));
        getMirrorClusterInfo = new GetMirrorClusterInfo(
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_MIRROR_CLUSTER_GET));
        getUnixPart = new GetUnixPartitionOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_UNIX_PART));

        getRstMapper = new GetRstMapOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        getDiskPartForWin = new GetDiskPartitionTableForWinOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD));
        getOldDiskInfoForWin = new GetDiskPartitionTableForWinOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        getNewDiskInfoForWin = new GetDiskPartitionTableForWinOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_PARTITION));
        getDiskPartForUnix = new GetDiskPartitionTableForUnixOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD));
        getOldDiskInfoForUnix = new GetDiskPartitionTableForUnixOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        getNewDiskInfoForUnix = new GetDiskPartitionTableForUnixOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        formatDiskPartForUnix = new FormatPartitionOnDiskForUnix(
                ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD));
        getUnixRstMapper = new GetUnixRstMapOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        getOsLoaderTypeOnClnt = new GetOsLoaderTypeOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        addIscsiInitDriver = new NetworkRunningWithoutOutput();

        getClntIDFromObjId = new GetClntIDFromObjID("");
        getBakClnt = new GetBakClient(ResourceCenter.getCmd(ResourceCenter.CMD_GET_CLIENT));
        addNode = new AddCmdNetworkRunning();
        modNode = new NetworkRunningWithoutOutput();
        delNode = new NetworkRunningWithoutOutput();
        changeRegisterForIP = new NetworkRunningWithoutOutput();
        getKernelVer = new GetKernelVer("");
        getPool = new GetPool(ResourceCenter.getCmd(ResourceCenter.CMD_GET_POOL));
        getPoolofRemoteUWS = new GetPool("");
        getPoolInfo = new GetPoolInfo(ResourceCenter.getCmd(ResourceCenter.CMD_GET_POOLINFO));
        getPoolInfoOfRemoteUWS = new GetPoolInfo("");
        getIdleDisk = new GetIdleDisk("");
        getUcsDiskInfo = new GetUCSDiskInfo("");
        getucsdiskcount = new GetUcsDiskCountByView("");
        getOsVolTid = new GetOSvolTidOfIBoot();
        getorphanvol = new GetOrphanVol(ResourceCenter.getCmd(ResourceCenter.CMD_GET_VOL));
        getView = new GetView(ResourceCenter.getCmd(ResourceCenter.CMD_GET_VIEW));
        addView = new AddView(ResourceCenter.getCmd(ResourceCenter.CMD_ADD_VIEW));
        ucsForwordView = new UcsForwordView(ResourceCenter.getCmd(ResourceCenter.CMD_FORWORD_UCS_VIEW));
        delView = new NetworkRunningWithoutOutput();
        crtResVol = new CreateResVol(ResourceCenter.getCmd(ResourceCenter.CMD_CRT_RESVOL));
        delResVol = new NetworkRunningWithoutOutput();
        crtUcsPool = new CreateUcsPool(ResourceCenter.getCmd(ResourceCenter.CMD_CRT_UCS_POOL));
        delUcsPool = new NetworkRunningWithoutOutput();
        expandVG = new NetworkRunningWithoutOutput();
        getShareName = new GetShareName(ResourceCenter.getCmd(ResourceCenter.CMD_GET_SHARENAME));
        getLic = new GetLicence(ResourceCenter.getCmd(ResourceCenter.CMD_GET_LICENCE));
        anlyMp = new AnalyMpFromDir();
        getBrVdisk = new GetBriefVDisk("");
        addPool = new AddCmdNetworkRunning();
        delPool = new NetworkRunningWithoutOutput();
        getAgntUUID = new GetAgentUUID();
        reCrtUUID = new NetworkRunningWithoutOutput();
        setUUID = new NetworkRunningWithoutOutput();
        getTgtByLetter = new GetTargetByLetter(ResourceCenter.getCmd(ResourceCenter.CMD_GET_TARGET_BY_LETTER));
        getPxeInfo = new GetPXEInfoFromTFtp("");
        getIBootInfo = new GetIBootInfoFromTFtp("");
        get3rdDhcpInfo = new Get3rdDhcpInfo("");
        getHbDskInfo = new GetHeartbeatDskInfo("");
        ziporunzip = new NetworkRunningWithoutOutput();
        touchAFile = new NetworkRunningWithoutOutput();
        getHostIDOnMac = new GetHostIDOnMac("");
        delTaskLogInfo = new NetworkRunningWithoutOutput();
        powerdown = new NetworkRunningWithoutOutput();
        ib_driver_comp = new NetworkRunningWithoutOutput();
        getIscsiIP = new GetIscsiIP("");
        getServiceOnVol = new GetServiceOnVolume("");
        getIScsiSession = new GetIScsiSessionObj(ResourceCenter.getCmd(ResourceCenter.CMD_CHECK_ISCSI_SESSION));
        getIf = new GetServerNetInterface(ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        adjustDriver = new NetworkRunningWithoutOutput();
        listProf = new ListProfile("");
        getBkset = new GetBakSet(ResourceCenter.getCmd(ResourceCenter.CMD_GET_BKSET));
        getOneTaskLog = new GetTaskLog(ResourceCenter.getCmd(ResourceCenter.CMD_QUERY_TSKLOG));

        rstPartition = new NetworkRunningWithoutOutput();
        listPartition = new ListPartitionForWinOnDisk(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD));
        formatPartition = new FormatPartitionOnDiskForWin(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD));
        mntPartition = new NetworkRunningWithoutOutput();

        getIbootList = new GetIbootObjList(ResourceCenter.getCmd(ResourceCenter.CMD_LIST_IBOOT));
        addIboot = new NetworkRunningWithoutOutput();
        delIboot = new NetworkRunningWithoutOutput();

        //getNetCard = new GetNetCardOnClnt(
        //    ResourceCenter.getCmd( ResourceCenter.CMD_GET_NETCARD )
        //);
        //getNetCardFromSrv = new GetNetCardOnClnt(
        //    ResourceCenter.getCmd( ResourceCenter.CMD_GET_NETCARD )
        //);
        getUnixNetCard = new GetUnixNetCardOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_NETCARD));
        getUnixNetCardFromSrv = new GetUnixNetCardOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_NETCARD));

        getService = new GetServiceOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_OS_SERVICE), ResourceCenter.DEFAULT_CHARACTER_SET);
        formatVol = new NetworkRunningWithoutOutput();
        set_efi_partition_flag = new NetworkRunningWithoutOutput();
        addPortal = new NetworkRunningWithoutOutput();
        loginTarget = new NetworkRunningWithoutOutput();
        logoutTarget = new NetworkRunningWithoutOutput();
        listTDisk = new ListTargetDiskOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_LIST_TDISK));
        getIpInfo = new GetIpInfoOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_IP_INFO));
        getNetCardFromSrv = new GetIpInfoOnClnt(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        copyOS = new NetworkRunningWithoutOutput();
        modyHostBoot = new SyncOpCmdWithNoOutput();
        regOS = new NetworkRunningWithoutOutput();
        getTargetDrv = new GetTargetDriver(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_TARGET_DRIVER));
        isStartupFromSAN = new IsStartupFromSAN(
                ResourceCenter.getCmd(ResourceCenter.CMD_IS_STARTUP_FROM_NET));
        isStartupFromNetBoot = new IsStartupFromNetBoot();
        getIdleDriveLetter = new GetIdleDriveLetter("");

        startService = new StartorStopService(
                ResourceCenter.getCmd(ResourceCenter.CMD_STOP_START_SERVICE));
        loadInfo = new NetworkRunningWithoutOutput();
        assignDriver = new NetworkRunningWithoutOutput();
        mntDriver = new NetworkRunningWithoutOutput();
        setPartitionActive = new SetPartitionActive();
        getNetInfo = new GetNetInfoFromMDB(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        getLVMInfo = new GetLVMInfoFromPXELinux(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT));
        getUnixNetCardInfo = new GetUnixNetCardInfo();

        bindTgtVolAndMac = new NetworkRunningWithoutOutput();
        addPersistentTarget = new NetworkRunningWithoutOutput();
        getPersistentTarget = new GetPersistentList("");
        delPersistentTarget = new NetworkRunningWithoutOutput();
        rebootHost = new NetworkRunningWithoutOutput();
        sendNetConf = new NetworkRunningWithoutOutput();
        getWinPlatform = new GetWinPlatform();
        getLVMType = new GetLVMType();
        isCrtVgOnTgt = new IsCrtVGOnTgt();
        onlineVol = new NetworkRunningWithoutOutput();
        offlineVol = new NetworkRunningWithoutOutput();
        isCrtLVOnTgt = new IsCrtLVOnTgt();
        delLVMVG = new DelAllInVG();
        mntLV = new MountLV();
        getTftpRootPath = new GetTftpRootPath();
        getIscsiCmdPath = new GetIScsiCmdPath();
        sendFileFromAgtToSrv = new NetworkRunningWithoutOutput();
        umountFS = new NetworkRunningWithoutOutput();
        mountPool = new NetworkRunningWithoutOutput();
        mkfs = new NetworkRunningWithoutOutput();
        setBootInfoEnvVar = new NetworkRunningWithoutOutput();
        setIBootConfigure = new NetworkRunningWithoutOutput();
        getBootInfoVar = new NetworkRunningWithoutOutput();
        genKernelImg = new GenerateIBootKernelImg();
        genInitrd = new GenerateIBootInitrd();
        genPxeLinux = new GenerateIBootPxeLinux();
        chgFsLabel = new NetworkRunningWithoutOutput();
        vgOnline = new NetworkRunningWithoutOutput();
        vgOffline = new NetworkRunningWithoutOutput();
        modBootConf = new NetworkRunningWithoutOutput();
        setUnixActive = new SetUnixActive();
        getOsLoader = new GetOsLoaderType();

        // sch
        getSchList = new GetSchList(ResourceCenter.getCmd(ResourceCenter.CMD_GET_DB_SCHEDULER));
        addSch = new AddCmdNetworkRunning();
        modSch = new NetworkRunningWithoutOutput();
        delSch = new NetworkRunningWithoutOutput();

        // user op
        getBakUser = new GetBackupUser(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_USER));
        addUser = new AddCmdNetworkRunning();
        modUser = new NetworkRunningWithoutOutput();
        delUser = new NetworkRunningWithoutOutput();

        // boot host
        getBootHost = new GetBootHost(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_BOOT_HOST));
        getOneBootHost = new GetBootHost(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_BOOT_HOST));
        queryBootHostOnDestUWSrv = new GetBootHost(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_BOOT_HOST));
        addBootHost = new AddBootHostNetworkRunning();
        modBootHost = new NetworkRunningWithoutOutput();
        delBootHost = new NetworkRunningWithoutOutput();

        // uws server
        getUWSrv = new GetUWSrvNode(ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_SRV));
        queryUWSrvOnDestUWSrv = new GetUWSrvNode(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD));
        addUWSrv = new AddBootHostNetworkRunning();
        modUWSrv = new NetworkRunningWithoutOutput();
        delUWSrv = new NetworkRunningWithoutOutput();

        // mg
        getMgList = new GetMGList(ResourceCenter.getCmd(ResourceCenter.CMD_GET_MG));
        getOneMg = new GetMGList(ResourceCenter.getCmd(ResourceCenter.CMD_GET_MG));
        addMg = new AddBootHostNetworkRunning();
        modMg = new NetworkRunningWithoutOutput();
        delMg = new NetworkRunningWithoutOutput();
        checkMg = new NetworkRunningWithoutOutput();
        startMg = new NetworkRunningWithoutOutput();
        stopMg = new NetworkRunningWithoutOutput();
        sendSigToMg = new NetworkRunningWithoutOutput();

        // mj
        getMjList = new GetMJList(ResourceCenter.getCmd(ResourceCenter.CMD_GET_MJ));
        monMj = new GetMJList(ResourceCenter.getCmd(ResourceCenter.CMD_GET_MJ));
        addMj = new AddCmdNetworkRunning();
        modMj = new NetworkRunningWithoutOutput();
        delMj = new NetworkRunningWithoutOutput();
        switchMj = new NetworkRunningWithoutOutput();
        msputfile = new NetworkRunningWithoutOutput();
        getMjSch = new GetMJScheduler(ResourceCenter.getCmd(ResourceCenter.CMD_GET_MJ_SCH));
        addMjSch = new AddCmdNetworkRunning();
        modMjSch = new NetworkRunningWithoutOutput();
        delMjSch = new NetworkRunningWithoutOutput();

        // srcagent
        getSrcAgnt = new GetSrcAgnt(ResourceCenter.getCmd(ResourceCenter.CMD_GET_SRCAGNT));
        querySrcAgntOnDestUWSrv = new GetSrcAgnt(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD));
        addSrcAgnt = new AddCmdNetworkRunning();
        delSrcAgnt = new NetworkRunningWithoutOutput();
        modSrcAgnt = new NetworkRunningWithoutOutput();

        // mirrorDiskInfo
        getMdi = new GetMirrorDiskInfo(ResourceCenter.getCmd(ResourceCenter.CMD_GET_MIRROR_DISK));
        queryMdiOnDestUWSrv = new GetMirrorDiskInfo(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD));
        modMdi = new NetworkRunningWithoutOutput();
        addMdi = new AddCmdNetworkRunning();
        delMdi = new NetworkRunningWithoutOutput();
        destroyDisk = new NetworkRunningWithoutOutput();

        // clone disk
        getCloneDiskListCmd = new GetCloneDiskListCmd("");
        updateCloneDisk = new GetCloneDiskListCmd("");
        addCloneDisk = new AddCmdNetworkRunning();
        delCloneDisk = new NetworkRunningWithoutOutput();
        modCloneDisk = new NetworkRunningWithoutOutput();
        getUISnapSum = new AddCmdNetworkRunning();

        // mirrorSnapUsage
        getMSU = new GetMirrorSnapusage(ResourceCenter.getCmd(ResourceCenter.CMD_GET_SNAPUSAGE));
        addMSU = new AddCmdNetworkRunning();
        delMSU = new NetworkRunningWithoutOutput();
        modMSU = new NetworkRunningWithoutOutput();

        // netbootedHost
        getNBH = new GetNetBootedHost(ResourceCenter.getCmd(ResourceCenter.CMD_GET_NETBOOTED_HOST));
        addNBH = new AddCmdNetworkRunning();
        delNBH = new NetworkRunningWithoutOutput();
        modNBH = new NetworkRunningWithoutOutput();

        // service
        getServMap = new GetServMap(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_SERVMAP));
        addServMap = new AddCmdNetworkRunning();
        delServMap = new NetworkRunningWithoutOutput();

        // report
        addReport = new AddCmdNetworkRunning();

        // volume op
        getAllVolMap = new GetVolumeMap(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_VOLMAP));
        getOneVolMap = new GetVolumeMap(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_VOLMAP));
        queryVolMapOnDestUWSrv = new GetVolumeMap(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_VOLMAP));
        addVolMap = new NetworkRunningWithoutOutput();
        modVolMap = new NetworkRunningWithoutOutput();
        delVolMap = new NetworkRunningWithoutOutput();

        // volume 
        addVol = new AddOrphanVol();
        delVol = new NetworkRunningWithoutOutput();
        getVolSize = new GetVolSize(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_VOL_SIZE));
        getVgSize = new GetVGSize(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_VG_SIZE));
        isCrtVg = new IsCreatedVg(
                ResourceCenter.getCmd(ResourceCenter.CMD_IS_CRT_VG));

        // snapshot
        queryVSnapDB = new QueryVSnapDB(ResourceCenter.getCmd(ResourceCenter.CMD_SNAPDB_SQL_QUEUE));
        queryVSnapDB1 = new QueryVSnapDB1(ResourceCenter.getCmd(ResourceCenter.CMD_SNAPDB_SQL_QUEUE));
        getSnap = new GetSnapshot(ResourceCenter.getCmd(ResourceCenter.CMD_GET_SNAP));
        addSnap = new NetworkRunningWithoutOutput();
        addSnap1 = new AddSnapshot(ResourceCenter.getCmd(ResourceCenter.CMD_ADD_SNAP));
        addSnapForCmdp = new CreateVersion("");
        delSnap = new NetworkRunningWithoutOutput();
        delSnapForCmdp = new CommonCMDPCommand();
        expSnap = new NetworkRunningWithoutOutput();
        modSnap = new NetworkRunningWithoutOutput();
        rolldisk = new NetworkRunningWithoutOutput();
        getSnapName = new GetSnapName(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_SNAP_NAME));
        getSnapSize = new GetSnapSize("");

        // lunmap
        getLunMap = new GetLunMap1(ResourceCenter.getCmd(ResourceCenter.CMD_GET_LUNMAP));
        delLM = new NetworkRunningWithoutOutput();
        addLM = new NetworkRunningWithoutOutput();

        // vg 
        addVg = new NetworkRunningWithoutOutput();
        delVg = new NetworkRunningWithoutOutput();
        addLV = new NetworkRunningWithoutOutput();
        delLV = new NetworkRunningWithoutOutput();

        // mdb op
        bakMdb = new NetworkRunningWithoutOutput();
        rstMdb = new NetworkRunningWithoutOutput();

        // login and logout
        logout = new Logout();
        login = new Login();
//        syncOPWithOutput = new SyncOpCmdWithoutput();
        syncOPWithNoOutput = new SyncOpCmdWithNoOutput();
        getCPProcess = new GetCPProcess();
        getUnixCPProcess = new GetUnixCPProcess();

        // bak object op
        getBakObjList = new GetBakObjList(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_BAKOBJECT));
        getOneBakObjList = new GetBakObjList(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_BAKOBJECT));
        addBakObj = new AddCmdNetworkRunning();
        modBakObj = new NetworkRunningWithoutOutput();
        delBakObj = new NetworkRunningWithoutOutput();

        // task
        getTaskList = new GetBakTask(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_TASK));
        delTask = new NetworkRunningWithoutOutput();
        killTask = new NetworkRunningWithoutOutput();
        activeTask = new NetworkRunningWithoutOutput();
        delTaskLog = new NetworkRunningWithoutOutput();
        delAllTskLog = new NetworkRunningWithoutOutput();

        //VMmanage
        getVMHostInfo = new GetVMHostInfo(ResourceCenter.getCmd(ResourceCenter.CMD_GET_VM_INFO));
        getVMServerInfo = new GetVMServerInfo(ResourceCenter.getCmd(ResourceCenter.CMD_GET_VM_SERVER_INFO));
        getVMHostIPConfig = new GetVMHostIPConfig("", "GB2312");
        getIdleAndUsedVip = new GetIdleAndUsedVip("");

        //license count
        getLicenseCount = new GetLicenseCount(ResourceCenter.getCmd(ResourceCenter.CMD_GET_LICENSE_COUNT));
        getTxIPFromIP = new GetTxIPFromIP();
        // audit
        addAudit = new AddCmdNetworkRunning();
        delAudit = new NetworkRunningWithoutOutput();
        delAllAudit = new NetworkRunningWithoutOutput();

        // bakset
        modBakset = new NetworkRunningWithoutOutput();

        // cluster
        getCluster = new GetCluster(ResourceCenter.getCmd(ResourceCenter.CMD_GET_CLUSTER));
        addCluster = new AddCmdNetworkRunning();
        modCluster = new NetworkRunningWithoutOutput();
        delCluster = new NetworkRunningWithoutOutput();

        // cmdp
        // dg
        getDriveGrp = new GetDriveGroup(ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_GET_DG));

        // database info
        getDbInfo = new GetDatabaseInfoOnVolume(ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_GET_DBINFO));
        crtDG = new NetworkRunningWithoutOutput();
        delDG = new NetworkRunningWithoutOutput();
        addDiskIntoDG = new NetworkRunningWithoutOutput();
        delDiskFromDG = new NetworkRunningWithoutOutput();
        modAutoCrtSnapParameter = new NetworkRunningWithoutOutput();
        modServicesOnVol = new NetworkRunningWithoutOutput();
        modDbinfoOnVol = new NetworkRunningWithoutOutput();

        getCurrentSyncState = new GetInitProgress("");
        setCurrentStateOfSync = new CommonCMDPCommand();
        getWorkStateOfSync = new CommonCMDPCommand();
        modVolInfo = new ModVolInfo("");
        setWorkStateOfSync = new CommonCMDPCommand();
        getHAInfo = new CommonCMDPCommand();
        setHAInfo = new CommonCMDPCommand();
        buildMirror = new BuildMirrorOnClnt();
        delMirrorOnClnt = new CommonCMDPCommand();
        destoryMirror = new CommonCMDPCommand();
        setNetBootVersion = new SetNetBootVersion("");
        getFileFromS2A = new NetworkRunningWithoutOutput();
        recoverInvalidMirror = new CommonCMDPCommand();
        buildRestoreMirror = new CommonCMDPCommand();
        buildMirrorAftRestore = new CommonCMDPCommand();
        chkdiskOnWin = new CommonCMDPCommand();
        getVolInfoForTgt = new GetVolInfoForTarget("");
        getMirroringInfo = new GetMirrorringInfo("");
        setInitType = new CommonCMDPCommand();
        flushDisk = new CommonCMDPCommand();
        modDBAftRestoreOriDisk = new CommonCMDPCommand();
        zero_uuid_sector = new CommonCMDPCommand();
        createMirrorCluster = new CreateMirrorCluster();
        deleteMirrorCluster = new DeleteMirrorCluster();
        checkVolumeAftRegister = new CheckVolAftRegister();

        amsbuildMirror = new AmsBuildMirrorOnClnt();
        lvmCmd = new LvmCmd();
        amsGetDiskPath = new AmsGetDiskPathFromTgtId();
        amsGetRealDiskPath = new AmsGetRealDiskPathFromTgtId();
        getCurrentAmsSyncState = new GetInitAmsProgress("");
        delAmsProtect = new NetworkRunningWithoutOutput();
        setAmsWorkState = new NetworkRunningWithoutOutput();
        amsSwitchNetDisk = new NetworkRunningWithoutOutput();
        amsSwitchLocal = new NetworkRunningWithoutOutput();
        amsparted = new NetworkRunningWithoutOutput();
        amsRebuildMirror = new NetworkRunningWithoutOutput();
        restoreNetDisk = new NetworkRunningWithoutOutput();
        forwordUcsSnap = new NetworkRunningWithoutOutput();

        //vmmanager
        addVMHost = new NetworkRunningWithoutOutput();
        modVMHost = new NetworkRunningWithoutOutput();
        delVMHost = new NetworkRunningWithoutOutput();
        vmHostOper = new NetworkRunningWithoutOutput();
        enbaleIboot = new NetworkRunningWithoutOutput();
        getUWSDefaultIp = new GetUWSDefaultIP(ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_DEFAULT_IP));
        getVMInfoFromHost = new GetVMInfoFromHost();
        crtVMMachine = new NetworkRunningWithoutOutput();
        hasVMserviceInfoOrNot = new HasVMServiceInfoOrNot("");
        crtVMServiceInfo = new NetworkRunningWithoutOutput();
        bindviewMac = new NetworkRunningWithoutOutput();
        getLinuxDevName = new GetLinuxDevName(ResourceCenter.getCmd(ResourceCenter.CMD_GET_LINUX_DEV));
        setupIbootLinux64 = new NetworkRunningWithoutOutput();
//        vmHostOPS = new VMHostOperationService("");
    }

    public void setCmdPath() {
        getBakObjList.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_BAKOBJECT));
        getBkset.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_BKSET));
        getTaskList.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_TASK));
        getSchList.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_DB_SCHEDULER));
        getBakClnt.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_CLIENT));
        getBootHost.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_BOOT_HOST));
        getAllVolMap.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_VOLMAP));
        getVgSize.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_VG_SIZE));
        isCrtVg.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_IS_CRT_VG));
        getServMap.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_SERVMAP));
        getBakUser.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_USER));
        getLic.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_LICENCE));
    }

    public void closeStreamOnCurSocket() {
        try {
            getDhcp.closeSocketStream();
            getConf.closeSocketStream();
            getUWSRptConf.closeSocketStream();
            rptMailTest.closeSocketStream();
            reloadUWSConf.closeSocketStream();
            isDir.closeSocketStream();
            listDir.closeSocketStream();
            viewFile.closeSocketStream();
            getErrLog.closeSocketStream();
            sendFileToSrv.closeSocketStream();
            mvFile.closeSocketStream();
            cpFile.closeSocketStream();
            delFile.closeSocketStream();
            findFile.closeSocketStream();
            mkdir.closeSocketStream();
            chmod.closeSocketStream();
            touchFile.closeSocketStream();
            diff.closeSocketStream();
            ln.closeSocketStream();
            mountIAHidenFs.closeSocketStream();
            getPartition.closeSocketStream();
            getPartitionByCatConf.closeSocketStream();
            getVolInfoForCMDP.closeSocketStream();
            getMirrorClusterInfo.closeSocketStream();
            getUnixPart.closeSocketStream();
            getRstMapper.closeSocketStream();
            getDiskPartForWin.closeSocketStream();
            getOldDiskInfoForWin.closeSocketStream();
            getNewDiskInfoForWin.closeSocketStream();
            getDiskPartForUnix.closeSocketStream();
            getOldDiskInfoForUnix.closeSocketStream();
            getNewDiskInfoForUnix.closeSocketStream();
            formatDiskPartForUnix.closeSocketStream();
            getUnixRstMapper.closeSocketStream();
            getOsLoaderTypeOnClnt.closeSocketStream();
            addIscsiInitDriver.closeSocketStream();
            getClntIDFromObjId.closeSocketStream();
            getBakClnt.closeSocketStream();
            addNode.closeSocketStream();
            modNode.closeSocketStream();
            delNode.closeSocketStream();
            changeRegisterForIP.closeSocketStream();
            getKernelVer.closeSocketStream();
            getPool.closeSocketStream();
            getPoolofRemoteUWS.closeSocketStream();
            getPoolInfo.closeSocketStream();
            getPoolInfoOfRemoteUWS.closeSocketStream();
            getIdleDisk.closeSocketStream();
            getUcsDiskInfo.closeSocketStream();
            getucsdiskcount.closeSocketStream();
            getOsVolTid.closeSocketStream();
            getorphanvol.closeSocketStream();
            getView.closeSocketStream();
            addView.closeSocketStream();
            ucsForwordView.closeSocketStream();
            delView.closeSocketStream();
            crtResVol.closeSocketStream();
            delResVol.closeSocketStream();
            crtUcsPool.closeSocketStream();
            delUcsPool.closeSocketStream();
            expandVG.closeSocketStream();
            getShareName.closeSocketStream();
            getLic.closeSocketStream();
            anlyMp.closeSocketStream();
            getBrVdisk.closeSocketStream();
            addPool.closeSocketStream();
            delPool.closeSocketStream();
            getAgntUUID.closeSocketStream();
            reCrtUUID.closeSocketStream();
            setUUID.closeSocketStream();
            getTgtByLetter.closeSocketStream();
            getPxeInfo.closeSocketStream();
            getIBootInfo.closeSocketStream();
            get3rdDhcpInfo.closeSocketStream();
            getHbDskInfo.closeSocketStream();
            ziporunzip.closeSocketStream();
            touchAFile.closeSocketStream();
            getHostIDOnMac.closeSocketStream();
            delTaskLogInfo.closeSocketStream();
            powerdown.closeSocketStream();
            ib_driver_comp.closeSocketStream();
            getIscsiIP.closeSocketStream();
            getServiceOnVol.closeSocketStream();
            getIScsiSession.closeSocketStream();
            getIf.closeSocketStream();
            adjustDriver.closeSocketStream();
            listProf.closeSocketStream();
            getBkset.closeSocketStream();
            getOneTaskLog.closeSocketStream();
            rstPartition.closeSocketStream();
            listPartition.closeSocketStream();
            formatPartition.closeSocketStream();
            mntPartition.closeSocketStream();
            getIbootList.closeSocketStream();
            addIboot.closeSocketStream();
            addIboot.closeSocketStream();
            getSchList.closeSocketStream();
            addSch.closeSocketStream();
            modSch.closeSocketStream();
            delSch.closeSocketStream();
            getUnixNetCard.closeSocketStream();
            getUnixNetCardFromSrv.closeSocketStream();
            getService.closeSocketStream();
            getSysTime.closeSocketStream();
            getWindir.closeSocketStream();
            getTargetSrvName.closeSocketStream();
            getLastModTime.closeSocketStream();
            formatVol.closeSocketStream();
            set_efi_partition_flag.closeSocketStream();
            addPortal.closeSocketStream();
            loginTarget.closeSocketStream();
            logoutTarget.closeSocketStream();
            listTDisk.closeSocketStream();
            getIpInfo.closeSocketStream();
            getNetCardFromSrv.closeSocketStream();
            copyOS.closeSocketStream();
            modyHostBoot.closeSocketStream();
            regOS.closeSocketStream();
            getTargetDrv.closeSocketStream();
            isStartupFromSAN.closeSocketStream();
            getIdleDriveLetter.closeSocketStream();
            isStartupFromNetBoot.closeSocketStream();
            startService.closeSocketStream();
            loadInfo.closeSocketStream();
            assignDriver.closeSocketStream();
            mntDriver.closeSocketStream();
            setPartitionActive.closeSocketStream();
            getNetInfo.closeSocketStream();
            getLVMInfo.closeSocketStream();
            getUnixNetCardInfo.closeSocketStream();
            bindTgtVolAndMac.closeSocketStream();
            getPersistentTarget.closeSocketStream();
            addPersistentTarget.closeSocketStream();
            delPersistentTarget.closeSocketStream();
            rebootHost.closeSocketStream();
            sendNetConf.closeSocketStream();
            getWinPlatform.closeSocketStream();
            getLVMType.closeSocketStream();
            isCrtVgOnTgt.closeSocketStream();
            onlineVol.closeSocketStream();
            offlineVol.closeSocketStream();
            isCrtLVOnTgt.closeSocketStream();
            delLVMVG.closeSocketStream();
            mntLV.closeSocketStream();
            getTftpRootPath.closeSocketStream();
            getIscsiCmdPath.closeSocketStream();
            sendFileFromAgtToSrv.closeSocketStream();
            umountFS.closeSocketStream();
            mountPool.closeSocketStream();
            mkfs.closeSocketStream();
            setBootInfoEnvVar.closeSocketStream();
            setIBootConfigure.closeSocketStream();
            getBootInfoVar.closeSocketStream();
            genKernelImg.closeSocketStream();
            genInitrd.closeSocketStream();
            genPxeLinux.closeSocketStream();
            chgFsLabel.closeSocketStream();
            vgOffline.closeSocketStream();
            vgOnline.closeSocketStream();
            modBootConf.closeSocketStream();
            setUnixActive.closeSocketStream();
            getOsLoader.closeSocketStream();

            getBakUser.closeSocketStream();
            addUser.closeSocketStream();
            modUser.closeSocketStream();
            delUser.closeSocketStream();

            getBootHost.closeSocketStream();
            getOneBootHost.closeSocketStream();
            queryBootHostOnDestUWSrv.closeSocketStream();
            addBootHost.closeSocketStream();
            modBootHost.closeSocketStream();
            delBootHost.closeSocketStream();

            getUWSrv.closeSocketStream();
            queryUWSrvOnDestUWSrv.closeSocketStream();
            addUWSrv.closeSocketStream();
            modUWSrv.closeSocketStream();
            delUWSrv.closeSocketStream();

            getMgList.closeSocketStream();
            getOneMg.closeSocketStream();
            addMg.closeSocketStream();
            modMg.closeSocketStream();
            delMg.closeSocketStream();
            checkMg.closeSocketStream();
            startMg.closeSocketStream();
            stopMg.closeSocketStream();
            sendSigToMg.closeSocketStream();

            getMjList.closeSocketStream();
            monMj.closeSocketStream();
            addMj.closeSocketStream();
            modMj.closeSocketStream();
            delMj.closeSocketStream();
            switchMj.closeSocketStream();
            msputfile.closeSocketStream();
            getMjSch.closeSocketStream();
            addMjSch.closeSocketStream();
            modMjSch.closeSocketStream();
            delMjSch.closeSocketStream();

            // srcagent
            getSrcAgnt.closeSocketStream();
            querySrcAgntOnDestUWSrv.closeSocketStream();
            addSrcAgnt.closeSocketStream();
            delSrcAgnt.closeSocketStream();
            modSrcAgnt.closeSocketStream();

            // mirrorDiskInfo
            getMdi.closeSocketStream();
            queryMdiOnDestUWSrv.closeSocketStream();
            modMdi.closeSocketStream();
            addMdi.closeSocketStream();
            delMdi.closeSocketStream();
            destroyDisk.closeSocketStream();

            // clone disk
            getCloneDiskListCmd.closeSocketStream();
            updateCloneDisk.closeSocketStream();
            this.addCloneDisk.closeSocketStream();
            this.delCloneDisk.closeSocketStream();
            this.modCloneDisk.closeSocketStream();
            getUISnapSum.closeSocketStream();

            // mirrorSnapUsage
            getMSU.closeSocketStream();
            addMSU.closeSocketStream();
            delMSU.closeSocketStream();
            modMSU.closeSocketStream();

            // netbooted host
            getNBH.closeSocketStream();
            addNBH.closeSocketStream();
            delNBH.closeSocketStream();
            modNBH.closeSocketStream();

            getServMap.closeSocketStream();
            addServMap.closeSocketStream();
            delServMap.closeSocketStream();

            addReport.closeSocketStream();

            addVolMap.closeSocketStream();
            delVolMap.closeSocketStream();
            modVolMap.closeSocketStream();
            getAllVolMap.closeSocketStream();
            getOneVolMap.closeSocketStream();
            queryVolMapOnDestUWSrv.closeSocketStream();

            addVol.closeSocketStream();
            delVol.closeSocketStream();
            getVolSize.closeSocketStream();
            getVgSize.closeSocketStream();
            isCrtVg.closeSocketStream();

            queryVSnapDB.closeSocketStream();
            queryVSnapDB1.closeSocketStream();
            getSnap.closeSocketStream();
            addSnap.closeSocketStream();
            addSnap1.closeSocketStream();
            addSnapForCmdp.closeSocketStream();
            delSnap.closeSocketStream();
            delSnapForCmdp.closeSocketStream();
            expSnap.closeSocketStream();
            modSnap.closeSocketStream();
            rolldisk.closeSocketStream();
            getSnapName.closeSocketStream();
            getSnapSize.closeSocketStream();

            getLunMap.closeSocketStream();
            delLM.closeSocketStream();
            addLM.closeSocketStream();

            addVg.closeSocketStream();
            delVg.closeSocketStream();
            addLV.closeSocketStream();
            delLV.closeSocketStream();

            bakMdb.closeSocketStream();
            rstMdb.closeSocketStream();

            logout.closeSocketStream();
            login.closeSocketStream();
//            syncOPWithOutput.closeSocketStream();
            syncOPWithNoOutput.closeSocketStream();
            getCPProcess.closeSocketStream();
            getUnixCPProcess.closeSocketStream();

            getBakObjList.closeSocketStream();
            getOneBakObjList.closeSocketStream();
            addBakObj.closeSocketStream();
            modBakObj.closeSocketStream();
            delBakObj.closeSocketStream();

            getTaskList.closeSocketStream();
            delTask.closeSocketStream();
            killTask.closeSocketStream();
            activeTask.closeSocketStream();
            delTaskLog.closeSocketStream();
            delAllTskLog.closeSocketStream();
            modBakset.closeSocketStream();
            getCluster.closeSocketStream();
            addCluster.closeSocketStream();
            modCluster.closeSocketStream();
            delCluster.closeSocketStream();

            addAudit.closeSocketStream();
            delAudit.closeSocketStream();
            delAllAudit.closeSocketStream();

            getDriveGrp.closeSocketStream();
            getDbInfo.closeSocketStream();
            crtDG.closeSocketStream();
            delDG.closeSocketStream();
            addDiskIntoDG.closeSocketStream();
            delDiskFromDG.closeSocketStream();
            modAutoCrtSnapParameter.closeSocketStream();
            modServicesOnVol.closeSocketStream();
            modDbinfoOnVol.closeSocketStream();
            getCurrentSyncState.closeSocketStream();
            getCurrentAmsSyncState.closeSocketStream();
            getWorkStateOfSync.closeSocketStream();
            modVolInfo.closeSocketStream();
            setCurrentStateOfSync.closeSocketStream();
            setWorkStateOfSync.closeSocketStream();
            getHAInfo.closeSocketStream();
            setHAInfo.closeSocketStream();
            buildMirror.closeSocketStream();
            amsbuildMirror.closeSocketStream();
            lvmCmd.closeSocketStream();
            delAmsProtect.closeSocketStream();
            setAmsWorkState.closeSocketStream();
            amsSwitchNetDisk.closeSocketStream();
            amsSwitchLocal.closeSocketStream();
            amsparted.closeSocketStream();
            amsRebuildMirror.closeSocketStream();
            forwordUcsSnap.closeSocketStream();
            restoreNetDisk.closeSocketStream();
            amsGetDiskPath.closeSocketStream();
            amsGetRealDiskPath.closeSocketStream();
            delMirrorOnClnt.closeSocketStream();
            destoryMirror.closeSocketStream();
            setNetBootVersion.closeSocketStream();
            getFileFromS2A.closeSocketStream();
            recoverInvalidMirror.closeSocketStream();
            buildRestoreMirror.closeSocketStream();
            buildMirrorAftRestore.closeSocketStream();
            chkdiskOnWin.closeSocketStream();
            getVolInfoForTgt.closeSocketStream();
            getMirroringInfo.closeSocketStream();
            setInitType.closeSocketStream();
            flushDisk.closeSocketStream();
            modDBAftRestoreOriDisk.closeSocketStream();
            zero_uuid_sector.closeSocketStream();
            createMirrorCluster.closeSocketStream();
            deleteMirrorCluster.closeSocketStream();
            checkVolumeAftRegister.closeSocketStream();
            getVMHostInfo.closeSocketStream();

            //vmmanager
            getVMServerInfo.closeSocketStream();
            getVMHostIPConfig.closeSocketStream();
            getIdleAndUsedVip.closeSocketStream();
            addVMHost.closeSocketStream();
            modVMHost.closeSocketStream();
            delVMHost.closeSocketStream();
            vmHostOper.closeSocketStream();
            enbaleIboot.closeSocketStream();
            getUWSDefaultIp.closeSocketStream();
            getVMInfoFromHost.closeSocketStream();
            crtVMMachine.closeSocketStream();
            hasVMserviceInfoOrNot.closeSocketStream();
            crtVMServiceInfo.closeSocketStream();
            bindviewMac.closeSocketStream();
            getLinuxDevName.closeSocketStream();
            setupIbootLinux64.closeSocketStream();
            //license count
            getLicenseCount.closeSocketStream();
            getTxIPFromIP.closeSocketStream();

//            vmHostOPS.closeSocketStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setCurSocket() {
        try {
            getDhcp.setSocket(view.getSocket());
            getConf.setSocket(view.getSocket());
            getUWSRptConf.setSocket(view.getSocket());
            rptMailTest.setSocket(view.getSocket());
            reloadUWSConf.setSocket(view.getSocket());
            isDir.setSocket(view.getSocket());
            listDir.setSocket(view.getSocket());
            viewFile.setSocket(view.getSocket());
            getErrLog.setSocket(view.getSocket());
            sendFileToSrv.setSocket(view.getSocket());
            mvFile.setSocket(view.getSocket());
            cpFile.setSocket(view.getSocket());
            delFile.setSocket(view.getSocket());
            findFile.setSocket(view.getSocket());
            mkdir.setSocket(view.getSocket());
            chmod.setSocket(view.getSocket());
            touchFile.setSocket(view.getSocket());
            diff.setSocket(view.getSocket());
            ln.setSocket(view.getSocket());
            mountIAHidenFs.setSocket(view.getSocket());
            getPartition.setSocket(view.getSocket());
            getPartitionByCatConf.setSocket(view.getSocket());
            getVolInfoForCMDP.setSocket(view.getSocket());
            getMirrorClusterInfo.setSocket(view.getSocket());
            getUnixPart.setSocket(view.getSocket());
            getRstMapper.setSocket(view.getSocket());
            getDiskPartForWin.setSocket(view.getSocket());
            getOldDiskInfoForWin.setSocket(view.getSocket());
            getNewDiskInfoForWin.setSocket(view.getSocket());
            getDiskPartForUnix.setSocket(view.getSocket());
            getOldDiskInfoForUnix.setSocket(view.getSocket());
            getNewDiskInfoForUnix.setSocket(view.getSocket());
            formatDiskPartForUnix.setSocket(view.getSocket());
            getUnixRstMapper.setSocket(view.getSocket());
            getOsLoaderTypeOnClnt.setSocket(view.getSocket());
            addIscsiInitDriver.setSocket(view.getSocket());
            getClntIDFromObjId.setSocket(view.getSocket());
            getBakClnt.setSocket(view.getSocket());
            addNode.setSocket(view.getSocket());
            modNode.setSocket(view.getSocket());
            delNode.setSocket(view.getSocket());
            changeRegisterForIP.setSocket(view.getSocket());
            getKernelVer.setSocket(view.getSocket());
            getPool.setSocket(view.getSocket());
            getPoolofRemoteUWS.setSocket(view.getSocket());
            getPoolInfo.setSocket(view.getSocket());
            getPoolInfoOfRemoteUWS.setSocket(view.getSocket());
            getIdleDisk.setSocket(view.getSocket());
            getUcsDiskInfo.setSocket(view.getSocket());
            getucsdiskcount.setSocket(view.getSocket());
            getOsVolTid.setSocket(view.getSocket());
            getorphanvol.setSocket(view.getSocket());
            getView.setSocket(view.getSocket());
            addView.setSocket(view.getSocket());
            ucsForwordView.setSocket(view.getSocket());
            delView.setSocket(view.getSocket());
            crtResVol.setSocket(view.getSocket());
            delResVol.setSocket(view.getSocket());
            crtUcsPool.setSocket(view.getSocket());
            delUcsPool.setSocket(view.getSocket());
            expandVG.setSocket(view.getSocket());
            getShareName.setSocket(view.getSocket());
            getLic.setSocket(view.getSocket());
            anlyMp.setSocket(view.getSocket());
            getBrVdisk.setSocket(view.getSocket());
            addPool.setSocket(view.getSocket());
            delPool.setSocket(view.getSocket());
            getAgntUUID.setSocket(view.getSocket());
            reCrtUUID.setSocket(view.getSocket());
            setUUID.setSocket(view.getSocket());
            getTgtByLetter.setSocket(view.getSocket());
            getPxeInfo.setSocket(view.getSocket());
            getIBootInfo.setSocket(view.getSocket());
            get3rdDhcpInfo.setSocket(view.getSocket());
            getHbDskInfo.setSocket(view.getSocket());
            ziporunzip.setSocket(view.getSocket());
            touchAFile.setSocket(view.getSocket());
            getHostIDOnMac.setSocket(view.getSocket());
            delTaskLogInfo.setSocket(view.getSocket());
            powerdown.setSocket(view.getSocket());
            ib_driver_comp.setSocket(view.getSocket());
            getIscsiIP.setSocket(view.getSocket());
            getServiceOnVol.setSocket(view.getSocket());
            getIScsiSession.setSocket(view.getSocket());
            getIf.setSocket(view.getSocket());
            adjustDriver.setSocket(view.getSocket());
            listProf.setSocket(view.getSocket());
            getBkset.setSocket(view.getSocket());
            getOneTaskLog.setSocket(view.getSocket());
            rstPartition.setSocket(view.getSocket());
            listPartition.setSocket(view.getSocket());
            formatPartition.setSocket(view.getSocket());
            mntPartition.setSocket(view.getSocket());
            getIbootList.setSocket(view.getSocket());
            addIboot.setSocket(view.getSocket());
            delIboot.setSocket(view.getSocket());
            getSchList.setSocket(view.getSocket());
            addSch.setSocket(view.getSocket());
            modSch.setSocket(view.getSocket());
            delSch.setSocket(view.getSocket());
            getUnixNetCard.setSocket(view.getSocket());
            getUnixNetCardFromSrv.setSocket(view.getSocket());
            getService.setSocket(view.getSocket());
            getSysTime.setSocket(view.getSocket());
            getWindir.setSocket(view.getSocket());
            getTargetSrvName.setSocket(view.getSocket());
            getLastModTime.setSocket(view.getSocket());
            formatVol.setSocket(view.getSocket());
            set_efi_partition_flag.setSocket(view.getSocket());
            addPortal.setSocket(view.getSocket());
            loginTarget.setSocket(view.getSocket());
            logoutTarget.setSocket(view.getSocket());
            listTDisk.setSocket(view.getSocket());
            getIpInfo.setSocket(view.getSocket());
            getNetCardFromSrv.setSocket(view.getSocket());
            copyOS.setSocket(view.getSocket());
            modyHostBoot.setSocket(view.getSocket());
            regOS.setSocket(view.getSocket());
            getTargetDrv.setSocket(view.getSocket());
            isStartupFromSAN.setSocket(view.getSocket());
            getIdleDriveLetter.setSocket(view.getSocket());
            isStartupFromNetBoot.setSocket(view.getSocket());
            startService.setSocket(view.getSocket());
            loadInfo.setSocket(view.getSocket());
            assignDriver.setSocket(view.getSocket());
            mntDriver.setSocket(view.getSocket());
            setPartitionActive.setSocket(view.getSocket());
            getNetInfo.setSocket(view.getSocket());
            getLVMInfo.setSocket(view.getSocket());
            getUnixNetCardInfo.setSocket(view.getSocket());
            bindTgtVolAndMac.setSocket(view.getSocket());
            getPersistentTarget.setSocket(view.getSocket());
            addPersistentTarget.setSocket(view.getSocket());
            delPersistentTarget.setSocket(view.getSocket());
            rebootHost.setSocket(view.getSocket());
            sendNetConf.setSocket(view.getSocket());
            getWinPlatform.setSocket(view.getSocket());
            getLVMType.setSocket(view.getSocket());
            isCrtVgOnTgt.setSocket(view.getSocket());
            onlineVol.setSocket(view.getSocket());
            offlineVol.setSocket(view.getSocket());
            isCrtLVOnTgt.setSocket(view.getSocket());
            delLVMVG.setSocket(view.getSocket());
            mntLV.setSocket(view.getSocket());
            getTftpRootPath.setSocket(view.getSocket());
            getIscsiCmdPath.setSocket(view.getSocket());
            sendFileFromAgtToSrv.setSocket(view.getSocket());
            umountFS.setSocket(view.getSocket());
            mountPool.setSocket(view.getSocket());
            mkfs.setSocket(view.getSocket());
            setBootInfoEnvVar.setSocket(view.getSocket());
            setIBootConfigure.setSocket(view.getSocket());
            getBootInfoVar.setSocket(view.getSocket());
            genKernelImg.setSocket(view.getSocket());
            genInitrd.setSocket(view.getSocket());
            genPxeLinux.setSocket(view.getSocket());
            chgFsLabel.setSocket(view.getSocket());
            vgOnline.setSocket(view.getSocket());
            vgOffline.setSocket(view.getSocket());
            modBootConf.setSocket(view.getSocket());
            setUnixActive.setSocket(view.getSocket());
            getOsLoader.setSocket(view.getSocket());

            getBakUser.setSocket(view.getSocket());
            addUser.setSocket(view.getSocket());
            modUser.setSocket(view.getSocket());
            delUser.setSocket(view.getSocket());

            getAllVolMap.setSocket(view.getSocket());
            getOneVolMap.setSocket(view.getSocket());
            queryVolMapOnDestUWSrv.setSocket(view.getSocket());
            addVolMap.setSocket(view.getSocket());
            delVolMap.setSocket(view.getSocket());
            modVolMap.setSocket(view.getSocket());

            addVol.setSocket(view.getSocket());
            delVol.setSocket(view.getSocket());
            getVolSize.setSocket(view.getSocket());
            getVgSize.setSocket(view.getSocket());
            isCrtVg.setSocket(view.getSocket());

            queryVSnapDB.setSocket(view.getSocket());
            queryVSnapDB1.setSocket(view.getSocket());
            getSnap.setSocket(view.getSocket());
            addSnap.setSocket(view.getSocket());
            addSnap1.setSocket(view.getSocket());
            addSnapForCmdp.setSocket(view.getSocket());
            delSnap.setSocket(view.getSocket());
            delSnapForCmdp.setSocket(view.getSocket());
            expSnap.setSocket(view.getSocket());
            modSnap.setSocket(view.getSocket());
            rolldisk.setSocket(view.getSocket());
            getSnapName.setSocket(view.getSocket());
            getSnapSize.setSocket(view.getSocket());

            getLunMap.setSocket(view.getSocket());
            delLM.setSocket(view.getSocket());
            addLM.setSocket(view.getSocket());

            addVg.setSocket(view.getSocket());
            delVg.setSocket(view.getSocket());
            addLV.setSocket(view.getSocket());
            delLV.setSocket(view.getSocket());

            getBootHost.setSocket(view.getSocket());
            getOneBootHost.setSocket(view.getSocket());
            queryBootHostOnDestUWSrv.setSocket(view.getSocket());
            addBootHost.setSocket(view.getSocket());
            modBootHost.setSocket(view.getSocket());
            delBootHost.setSocket(view.getSocket());

            getUWSrv.setSocket(view.getSocket());
            queryUWSrvOnDestUWSrv.setSocket(view.getSocket());
            addUWSrv.setSocket(view.getSocket());
            delUWSrv.setSocket(view.getSocket());
            modUWSrv.setSocket(view.getSocket());

            getMgList.setSocket(view.getSocket());
            getOneMg.setSocket(view.getSocket());
            addMg.setSocket(view.getSocket());
            delMg.setSocket(view.getSocket());
            modMg.setSocket(view.getSocket());
            checkMg.setSocket(view.getSocket());
            startMg.setSocket(view.getSocket());
            stopMg.setSocket(view.getSocket());
            sendSigToMg.setSocket(view.getSocket());

            getMjList.setSocket(view.getSocket());
            monMj.setSocket(view.getSocket());
            addMj.setSocket(view.getSocket());
            delMj.setSocket(view.getSocket());
            modMj.setSocket(view.getSocket());
            switchMj.setSocket(view.getSocket());
            msputfile.setSocket(view.getSocket());
            getMjSch.setSocket(view.getSocket());
            addMjSch.setSocket(view.getSocket());
            delMjSch.setSocket(view.getSocket());
            modMjSch.setSocket(view.getSocket());

            getSrcAgnt.setSocket(view.getSocket());
            querySrcAgntOnDestUWSrv.setSocket(view.getSocket());
            addSrcAgnt.setSocket(view.getSocket());
            delSrcAgnt.setSocket(view.getSocket());
            modSrcAgnt.setSocket(view.getSocket());

            // mirrorDiskInfo
            getMdi.setSocket(view.getSocket());
            queryMdiOnDestUWSrv.setSocket(view.getSocket());
            modMdi.setSocket(view.getSocket());
            addMdi.setSocket(view.getSocket());
            delMdi.setSocket(view.getSocket());
            destroyDisk.setSocket(view.getSocket());

            // clonedisk
            getCloneDiskListCmd.setSocket(view.getSocket());
            updateCloneDisk.setSocket(view.getSocket());
            this.addCloneDisk.setSocket(view.getSocket());
            this.delCloneDisk.setSocket(view.getSocket());
            this.modCloneDisk.setSocket(view.getSocket());
            this.getUISnapSum.setSocket(view.getSocket());

            // mirrorSnapUsage
            getMSU.setSocket(view.getSocket());
            addMSU.setSocket(view.getSocket());
            delMSU.setSocket(view.getSocket());
            modMSU.setSocket(view.getSocket());

            // netbooted Host 
            getNBH.setSocket(view.getSocket());
            addNBH.setSocket(view.getSocket());
            delNBH.setSocket(view.getSocket());
            modNBH.setSocket(view.getSocket());

            getServMap.setSocket(view.getSocket());
            addServMap.setSocket(view.getSocket());
            delServMap.setSocket(view.getSocket());

            addReport.setSocket(view.getSocket());

            bakMdb.setSocket(view.getSocket());
            rstMdb.setSocket(view.getSocket());

            getBakObjList.setSocket(view.getSocket());
            getOneBakObjList.setSocket(view.getSocket());
            addBakObj.setSocket(view.getSocket());
            modBakObj.setSocket(view.getSocket());
            delBakObj.setSocket(view.getSocket());

            getTaskList.setSocket(view.getSocket());
            delTask.setSocket(view.getSocket());
            killTask.setSocket(view.getSocket());
            activeTask.setSocket(view.getSocket());
            delTaskLog.setSocket(view.getSocket());
            delAllTskLog.setSocket(view.getSocket());
            modBakset.setSocket(view.getSocket());
            getCluster.setSocket(view.getSocket());
            addCluster.setSocket(view.getSocket());
            modCluster.setSocket(view.getSocket());
            delCluster.setSocket(view.getSocket());

            addAudit.setSocket(view.getSocket());
            delAudit.setSocket(view.getSocket());
            delAllAudit.setSocket(view.getSocket());

            logout.setSocket(view.getSocket());
            login.setSocket(view.getSocket());
//            syncOPWithOutput.setSocket( view.getSocket() );
            syncOPWithNoOutput.setSocket(view.getSocket());
            getCPProcess.setSocket(view.getSocket());
            getUnixCPProcess.setSocket(view.getSocket());

            getDriveGrp.setSocket(view.getSocket());
            getDbInfo.setSocket(view.getSocket());
            crtDG.setSocket(view.getSocket());
            delDG.setSocket(view.getSocket());
            addDiskIntoDG.setSocket(view.getSocket());
            delDiskFromDG.setSocket(view.getSocket());
            modAutoCrtSnapParameter.setSocket(view.getSocket());
            modServicesOnVol.setSocket(view.getSocket());
            modDbinfoOnVol.setSocket(view.getSocket());
            getCurrentSyncState.setSocket(view.getSocket());
            getCurrentAmsSyncState.setSocket(view.getSocket());
            getWorkStateOfSync.setSocket(view.getSocket());
            modVolInfo.setSocket(view.getSocket());
            setCurrentStateOfSync.setSocket(view.getSocket());
            setWorkStateOfSync.setSocket(view.getSocket());
            getHAInfo.setSocket(view.getSocket());
            setHAInfo.setSocket(view.getSocket());
            buildMirror.setSocket(view.getSocket());
            amsbuildMirror.setSocket(view.getSocket());
            lvmCmd.setSocket(view.getSocket());
            delAmsProtect.setSocket(view.getSocket());
            setAmsWorkState.setSocket(view.getSocket());
            amsSwitchNetDisk.setSocket(view.getSocket());
            amsSwitchLocal.setSocket(view.getSocket());
            amsparted.setSocket(view.getSocket());
            amsRebuildMirror.setSocket(view.getSocket());
            forwordUcsSnap.setSocket(view.getSocket());
            restoreNetDisk.setSocket(view.getSocket());
            amsGetDiskPath.setSocket(view.getSocket());
            amsGetRealDiskPath.setSocket(view.getSocket());
            delMirrorOnClnt.setSocket(view.getSocket());
            destoryMirror.setSocket(view.getSocket());
            setNetBootVersion.setSocket(view.getSocket());
            getFileFromS2A.setSocket(view.getSocket());
            recoverInvalidMirror.setSocket(view.getSocket());
            buildRestoreMirror.setSocket(view.getSocket());
            buildMirrorAftRestore.setSocket(view.getSocket());
            chkdiskOnWin.setSocket(view.getSocket());
            getVolInfoForTgt.setSocket(view.getSocket());
            getMirroringInfo.setSocket(view.getSocket());
            setInitType.setSocket(view.getSocket());
            flushDisk.setSocket(view.getSocket());
            modDBAftRestoreOriDisk.setSocket(view.getSocket());
            zero_uuid_sector.setSocket(view.getSocket());
            createMirrorCluster.setSocket(view.getSocket());
            deleteMirrorCluster.setSocket(view.getSocket());
            checkVolumeAftRegister.setSocket(view.getSocket());

            //vmmanager
            getVMHostInfo.setSocket(view.getSocket());
            getVMServerInfo.setSocket(view.getSocket());
            getVMHostIPConfig.setSocket(view.getSocket());
            getIdleAndUsedVip.setSocket(view.getSocket());
            addVMHost.setSocket(view.getSocket());
            modVMHost.setSocket(view.getSocket());
            delVMHost.setSocket(view.getSocket());
            vmHostOper.setSocket(view.getSocket());
            enbaleIboot.setSocket(view.getSocket());
            getUWSDefaultIp.setSocket(view.getSocket());
            getVMInfoFromHost.setSocket(view.getSocket());
            crtVMMachine.setSocket(view.getSocket());
            hasVMserviceInfoOrNot.setSocket(view.getSocket());
            crtVMServiceInfo.setSocket(view.getSocket());
            bindviewMac.setSocket(view.getSocket());
            getLinuxDevName.setSocket(view.getSocket());
            setupIbootLinux64.setSocket(view.getSocket());
            //license count
            getLicenseCount.setSocket(view.getSocket());
            getTxIPFromIP.setSocket(view.getSocket());
//            vmHostOPS.setSocket( view.getSocket() );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    MonitorInfoReceiver monitor;

    public void setMonitor(MonitorInfoReceiver _mon) {
        monitor = _mon;
    }

    public MonitorInfoReceiver getMonitor() {
        return monitor;
    }
    InitProgressInfoReceiver initProgressMonitor;

    public void setInitProgressMonitor(InitProgressInfoReceiver mon) {
        this.initProgressMonitor = mon;
    }

    public InitProgressInfoReceiver getInitProgressMonitor() {
        return this.initProgressMonitor;
    }
    InitAmsProgressInfoReceiver initAmsProgressMonitor;

    public void setInitAmsProgressMonitor(InitAmsProgressInfoReceiver mon) {
        this.initAmsProgressMonitor = mon;
    }

    public InitAmsProgressInfoReceiver getInitAmsProgressMonitor() {
        return this.initAmsProgressMonitor;
    }

    ////////////////////////////////////////////////
    //
    //   Get Config file
    //
    ////////////////////////////////////////////////
    public boolean getConfig() {
        boolean ret = getConf.getContent();
        if (!ret) {
            errorCode = getConf.getRetCode();
            errorMsg = getConf.getErrMsg();
        }
        return ret;
    }

    public boolean getDhcpConfig() {
        getDhcp.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CAT)
                + ResourceCenter.CLT_IP_CONF + ResourceCenter.DHCP_CONF_FILE);

        boolean ret = getDhcp.getContent();
        if (!ret) {
            errorCode = getDhcp.getRetCode();
            errorMsg = getDhcp.getErrMsg();
        }
        return ret;
    }

    // 管理IP就是dhcp conf中的title，每个管理IP对应一个section
    public DHCPOpt getCurrentDhcp(String admin_ip) {
        return getDhcp.getCurrentDhcp(admin_ip);
    }

    public void addOneDhcp(DHCPOpt one) {
        this.getDhcp.addOneDhcp(one);
    }

    public void removeOneDhcp(DHCPOpt one) {
        this.getDhcp.removeOneDhcp(one);
    }

    public String prtAllDhcp() {
        return getDhcp.prtMe();
    }

    public boolean getUWSRptConf() {
        boolean ret = getUWSRptConf.getContent();
        if (!ret) {
            errorCode = getUWSRptConf.getRetCode();
            errorMsg = getUWSRptConf.getErrMsg();
        }
        return ret;
    }

    public UWSRptConf getUWSRptConfContent() {
        return getUWSRptConf.getConf();
    }

    public boolean rptMailTest() {
        rptMailTest.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_SEND_UWS_RPT));
        SanBootView.log.info(getClass().getName(), "send rpt mail test cmd: " + rptMailTest.getCmdLine());

        try {
            rptMailTest.run();
        } catch (Exception ex) {
            recordException(rptMailTest, ex);
        }
        SanBootView.log.info(getClass().getName(), "send rpt mail test retcode: " + rptMailTest.getRetCode());
        boolean isOk = finished(rptMailTest);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "send rpt mail test errmsg: " + rptMailTest.getErrMsg());
        }
        return isOk;
    }

    public boolean reloadUWSConf() {
        reloadUWSConf.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_RELOAD_UWS_RPT));
        SanBootView.log.info(getClass().getName(), "reload system conf cmd: " + reloadUWSConf.getCmdLine());

        try {
            reloadUWSConf.run();
        } catch (Exception ex) {
            recordException(reloadUWSConf, ex);
        }
        SanBootView.log.info(getClass().getName(), "reload system conf retcode: " + reloadUWSConf.getRetCode());
        boolean isOk = finished(reloadUWSConf);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "reload system conf errmsg: " + reloadUWSConf.getErrMsg());
        }
        return isOk;
    }

    public boolean listProfile(String basePath) {
        listDir.setBasePath(basePath);
        listDir.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GREP) + basePath);
        boolean ret = listDir.listDirectory();
        if (!ret) {
            errorMsg = listDir.getErrMsg();
            errorCode = listDir.getRetCode();
        }
        return ret;
    }

    public boolean listDir(String basePath) {
        listDir.setBasePath(basePath);
        listDir.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_LIST_DIR) + basePath);
        boolean ret = listDir.listDirectory();
        if (!ret) {
            errorMsg = listDir.getErrMsg();
            errorCode = listDir.getRetCode();
        }
        return ret;
    }

    public boolean listDirOnRemoteHost(int dest_poolid, String dest_pool_passwd, String dest_uws_ip, int dest_uws_port, String basePath) {
        listDir.setBasePath(basePath);
        listDir.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + " -p " + dest_poolid + " -P " + dest_pool_passwd
                + " " + dest_uws_ip + " " + dest_uws_port + " listDir " + basePath);
        boolean ret = listDir.listDirectory();
        if (!ret) {
            errorMsg = listDir.getErrMsg();
            errorCode = listDir.getRetCode();
        }
        return ret;
    }

    public Vector<UnixFileObj> getFileList(String filter) {
        return listDir.getFileList(filter);
    }

    public ArrayList<UnixFileObj> getFileList() {
        return listDir.getFileList();
    }

    public Vector<UnixFileObj> getMDBFile() {
        return listDir.getFileList(ResourceCenter.DBName);
    }

    public boolean isFile(String filename) {
        return listDir.isFile(filename);
    }

    public boolean isExistFile(String filename) {
        return listDir.isExist(filename);
    }

    public ArrayList<String> getProfileFile() {
        return listProf.getFileList();
    }

    public Vector getBkLogFile(String level) {
        return listDir.getFileList("bks_info_" + level + ".log");
    }

    public boolean viewFileContents(String file) {
        viewFile.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + file);
        boolean ret = viewFile.viewFile();
        if (!ret) {
            errorCode = viewFile.getRetCode();
            errorMsg = viewFile.getErrMsg();
        }
        return ret;
    }

    public boolean viewDiagInfo(long id) {
        viewFile.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT_DIAG) + id);
        boolean ret = viewFile.viewFile();
        if (!ret) {
            errorCode = viewFile.getRetCode();
            errorMsg = viewFile.getErrMsg();
        }
        return ret;
    }

    public StringBuffer getContentBuf() {
        return viewFile.getContentBuffer();
    }

    public boolean getBakSet(long bkst_id) {
        boolean ret = getBkset.updateBakSet(bkst_id);
        if (!ret) {
            errorCode = getBkset.getRetCode();
            errorMsg = getBkset.getErrMsg();
        }
        return ret;
    }

    public BakSet getBakSet() {
        return getBkset.getBakSet();
    }

    public boolean getOneBakTask(String profName) {
        boolean ret = getOneTaskLog.updateTaskLog(profName);
        if (!ret) {
            errorCode = getOneTaskLog.getRetCode();
            errorMsg = getOneTaskLog.getErrMsg();
        }
        return ret;
    }

    public BakTask getQueryBakTask() {
        return getOneTaskLog.getQueryTask();
    }

    public boolean getFileStatus(long cltId, String file) {
        isDir.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_IS_DIR) + cltId
                + " \"" + file + "\"");
        boolean ret = isDir.getFileStatus();
        if (!ret) {
            errorMsg = isDir.getErrMsg();
            errorCode = isDir.getRetCode();
        }
        return ret;
    }

    // 必须先调用 getFileStatus
    public boolean isDir() {
        return isDir.isdir();
    }

    public boolean isFile() {
        return isDir.isFile();
    }

    public boolean isNotExist() {
        return isDir.isNotExist();
    }

    public boolean isUnknownErr() {
        return isDir.isUnKnown();
    }

    public boolean getErrorLog(long task_id) {
        getErrLog.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_VIEW_ERRLOG) + task_id);
        boolean ret = getErrLog.viewErrorLog();
        if (!ret) {
            errorMsg = getErrLog.getErrMsg();
            errorCode = getErrLog.getRetCode();
        }
        return ret;
    }

    public StringBuffer getErrLogContentBuf() {
        return getErrLog.getContentBuffer();
    }

    public boolean moveFile(String src, String dest) {
        mvFile.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_MV) + src + " " + dest);
        SanBootView.log.info(getClass().getName(), " mv cmd: " + mvFile.getCmdLine());
        try {
            mvFile.run();
        } catch (Exception ex) {
            recordException(mvFile, ex);
        }
        SanBootView.log.info(getClass().getName(), " mv cmd retcode: " + mvFile.getRetCode());
        boolean isOk = finished(mvFile);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mv cmd errmsg: " + mvFile.getErrMsg());
        }
        return isOk;
    }

    public boolean copyFiles(String src, String dest) {
        cpFile.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CP) + src + " " + dest);
        SanBootView.log.info(getClass().getName(), " copy file cmd: " + cpFile.getCmdLine());
        try {
            cpFile.run();
        } catch (Exception ex) {
            recordException(cpFile, ex);
        }
        SanBootView.log.info(getClass().getName(), " copy file retcode: " + cpFile.getRetCode());
        boolean isOk = finished(cpFile);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " copy file errmsg: " + cpFile.getErrMsg());
        }
        return isOk;
    }

    public boolean delFile(String file) {
        delFile.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_DEL_FILE) + file);
        SanBootView.log.info(getClass().getName(), " del file cmd: " + delFile.getCmdLine());
        try {
            delFile.run();
        } catch (Exception ex) {
            recordException(delFile, ex);
        }
        SanBootView.log.info(getClass().getName(), " del file retcode: " + delFile.getRetCode());
        boolean isOk = finished(delFile);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " del file errmsg: " + delFile.getErrMsg());
        }
        return isOk;
    }

    public boolean findFile(String file) {
        findFile.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_FIND_FILE) + file);
        SanBootView.log.info(getClass().getName(), " find file cmd: " + findFile.getCmdLine());
        try {
            findFile.run();
            
            
        } catch (Exception ex) {
            recordException(findFile, ex);
        }
        SanBootView.log.info(getClass().getName(), " find file retcode: " + findFile.getRetCode());
        boolean isOk = finished(findFile);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " find file errmsg: " + findFile.getErrMsg());
        }
        return isOk;
    }

    public boolean mkdir(String dir) {
        mkdir.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_MKDIR) + " -p " + dir);
        SanBootView.log.info(getClass().getName(), " mkdir cmd: " + mkdir.getCmdLine());
        try {
            mkdir.run();
        } catch (Exception ex) {
            recordException(mkdir, ex);
        }
        SanBootView.log.info(getClass().getName(), " mkdir cmd retcode: " + mkdir.getRetCode());
        boolean isOk = finished(mkdir);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mkdir cmd errmsg: " + mkdir.getErrMsg());
        }
        return isOk;
    }

    public boolean mkdirOnRemoteHost(int dest_poolid, String dest_pool_passwd, String dest_uws_ip, int dest_uws_port, String dir) {
        mkdir.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + " -p " + dest_poolid + " -P " + dest_pool_passwd
                + " " + dest_uws_ip + " " + dest_uws_port + " makeDir " + dir);
        SanBootView.log.info(getClass().getName(), " mkdir cmd: " + mkdir.getCmdLine());
        try {
            mkdir.run();
        } catch (Exception ex) {
            recordException(mkdir, ex);
        }
        SanBootView.log.info(getClass().getName(), " mkdir cmd retcode: " + mkdir.getRetCode());
        boolean isOk = finished(mkdir);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mkdir cmd errmsg: " + mkdir.getErrMsg());
        }
        return isOk;
    }

    public boolean chmod(String mode, String file) {
        chmod.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CHMOD) + " " + mode + " " + file);
        SanBootView.log.info(getClass().getName(), " chmod cmd: " + chmod.getCmdLine());
        try {
            chmod.run();
        } catch (Exception ex) {
            recordException(chmod, ex);
        }
        SanBootView.log.info(getClass().getName(), " chmod cmd retcode: " + chmod.getRetCode());
        boolean isOk = finished(chmod);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " chmod cmd errmsg: " + chmod.getErrMsg());
        }
        return isOk;
    }

    public boolean touchFile(String file) {
        touchFile.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_TOUCH_FILE) + " " + file);
        SanBootView.log.info(getClass().getName(), " touch file cmd: " + touchFile.getCmdLine());

        try {
            touchFile.run();
        } catch (Exception ex) {
            recordException(touchFile, ex);
        }
        SanBootView.log.info(getClass().getName(), " touch file cmd retcode: " + touchFile.getRetCode());
        boolean isOk = finished(touchFile);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " touch file cmd errmsg: " + touchFile.getErrMsg());
        }
        return isOk;
    }

    public boolean diff(String ip, int port, String src, String dest) {
        diff.setCmdLine(
                " mysystem.sh diff " + src + " " + dest);
        SanBootView.log.info(getClass().getName(), " diff cmd: " + diff.getCmdLine());

        try {
            diff.run();
        } catch (Exception ex) {
            recordException(diff, ex);
        }
        SanBootView.log.info(getClass().getName(), " diff retcode: " + diff.getRetCode());
        boolean isOk = finished(diff);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " diff errmsg: " + diff.getErrMsg());
        }
        return isOk;
    }

    public boolean ln(String lnName, String linked) {
        ln.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_LINK) + linked + " " + lnName);
        SanBootView.log.info(getClass().getName(), " ln cmd: " + ln.getCmdLine());

        try {
            ln.run();
        } catch (Exception ex) {
            recordException(ln, ex);
        }
        SanBootView.log.info(getClass().getName(), " ln cmd retcode: " + ln.getRetCode());
        boolean isOk = finished(ln);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " ln cmd errmsg: " + ln.getErrMsg());
        }
        return isOk;
    }

    public boolean mountIAHidenFs(String clntIP, int port, String option) {
        mountIAHidenFs.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + clntIP + " " + port + " ib_ia_deal_hide_prt.sh " + option);
        SanBootView.log.info(getClass().getName(), " mount IA hiden fs cmd: " + mountIAHidenFs.getCmdLine());

        try {
            mountIAHidenFs.run();
        } catch (Exception ex) {
            recordException(mountIAHidenFs, ex);
        }
        SanBootView.log.info(getClass().getName(), " mount IA hiden fs cmd retcode: " + mountIAHidenFs.getRetCode());
        boolean isOk = finished(mountIAHidenFs);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mount IA hiden fs cmd errmsg: " + mountIAHidenFs.getErrMsg());
        }
        return isOk;
    }

    public Day getCurSystemTime() {
        return getSysTime.getCurSystemTime();
    }

    public String getWinDir(String ip, int port) {
        return getWinDir(ip, port, ResourceCenter.CMD_TYPE_MTPP);
    }

    public String getWinDir(String ip, int port, int mode) {
        if (this.isMTPPCmd(mode)) {
            getWindir.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + ip + " " + port + " ib_get_win_dir.exe");
        } else {
            getWindir.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " ib_get_win_dir.exe");
        }
        getWindir.setCmdType(mode);

        String ret = getWindir.getWindowDir();
        this.errorMsg = getWindir.getErrMsg();
        this.errorCode = getWindir.getRetCode();
        return ret;
    }

    public int getClntIDFromObjIdInSchObj(String cmd) {
        return getClntIDFromObjId.getClntID(cmd);
    }
    public String targetSrvName = null;

    public String getHostName() {
        // 放弃cache,每次都重新获取，否则会出现混乱（2010.4.12）
        this.targetSrvName = "";
        return getHostName("", 0, 0, "");
    }

    public String getHostName(String dest_uws_ip, int dest_uws_port, int pool_id, String pool_passwd) {
        if (targetSrvName == null || targetSrvName.equals("")) {
            if (dest_uws_ip.equals("")) {
                getTargetSrvName.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_HOST_NAME));
            } else {
                getTargetSrvName.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + " -p " + pool_id + " -P " + pool_passwd + " " + dest_uws_ip + " " + dest_uws_port
                        + " getHostName");
            }
            targetSrvName = getTargetSrvName.getTargetSrvName();
            this.errorCode = getTargetSrvName.getRetCode();
            this.errorMsg = getTargetSrvName.getErrMsg();
        }
        return targetSrvName;
    }

    public String getLastModTime(String file) {
        String ret = getLastModTime.getLastModTime(file);
        this.errorMsg = getLastModTime.getErrMsg();
        this.errorCode = getLastModTime.getRetCode();
        return ret;
    }

    public boolean getSystemPart(String ip, int port, String args) {
        return getSystemPart(ip, port, args, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean getSystemPart(String ip, int port, String args, int mode) {
        if (this.isMTPPCmd(mode)) {
            getPartition.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_PARTITION) + " " + ip + " " + port + " " + args);
        } else {
            getPartition.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(ip, port) + args);
        }
        getPartition.setCmdType(mode);
        boolean isOk = getPartition.realDo();
        if (!isOk) {
            this.errorMsg = getPartition.getErrMsg();
            this.errorCode = getPartition.getRetCode();
        }
        return isOk;
    }

    public boolean getVolInfoForCMDP(String ip, int port) {
        this.getVolInfoForCMDP.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_GET_PARTITION) + ip + "," + port);
        getVolInfoForCMDP.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        boolean isOk = getVolInfoForCMDP.realDo();
        if (!isOk) {
            this.errorMsg = getVolInfoForCMDP.getErrMsg();
            this.errorCode = getVolInfoForCMDP.getRetCode();
        }
        return isOk;
    }

    public boolean getMirrorClusterInfo(String ip, int port) {
        this.getMirrorClusterInfo.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_MIRROR_CLUSTER_GET) + ip + "," + port);
        getMirrorClusterInfo.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        boolean isOk = getMirrorClusterInfo.realDo();
        if (!isOk) {
            this.errorMsg = getMirrorClusterInfo.getErrMsg();
            this.errorCode = getMirrorClusterInfo.getRetCode();
        }
        return isOk;
    }

    public ClusterConfigInfo getCCI() {
        return this.getMirrorClusterInfo.getCCI();
    }

    public boolean isMasterNode() {
        return this.getMirrorClusterInfo.isMasterNode();
    }

    public boolean getUnixPart(String ip, int port, String args) {
        getUnixPart.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_UNIX_PART) + " " + ip + " " + port + " " + args);
        boolean isOk = getUnixPart.realDo();
        if (!isOk) {
            this.errorMsg = getUnixPart.getErrMsg();
            this.errorCode = getUnixPart.getRetCode();
        }
        return isOk;
    }

    public boolean getUnixPart1(String conf) {
        getUnixPart.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + " " + conf);
        boolean isOk = getUnixPart.realDo();
        if (!isOk) {
            this.errorMsg = getUnixPart.getErrMsg();
            this.errorCode = getUnixPart.getRetCode();
        }
        return isOk;
    }

    public boolean getSystemPartFromMDB(String conf, int mode) {
        getPartitionByCatConf.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        getPartitionByCatConf.setCmdType(mode);
        boolean isOk = getPartitionByCatConf.realDo();
        if (!isOk) {
            this.errorMsg = getPartitionByCatConf.getErrMsg();
            this.errorCode = getPartitionByCatConf.getRetCode();
        }
        return isOk;
    }

    public Vector getSysPartByCatConf() {
        return getPartitionByCatConf.getAllPartition();
    }

    public SystemPartitionForWin getSysPartStatistic(String driver) {
        return getPartition.getSysPartStatistic(driver);
    }

    public Vector getSysPart() {
        return getPartition.getAllPartition();
    }

    public Object[] getLocakDiskFromVolInfoForCMDP() {
        return this.getVolInfoForCMDP.getAllLocalDiskPartition();
    }

    public Vector getVolInfoForCMDP() {
        return this.getVolInfoForCMDP.getAllPartition();
    }

    public String getVolInfoContentsForCMDP() {
        return this.getVolInfoForCMDP.prtMeForCMDP();
    }

    public Vector getUnixSysPart() {
        return getUnixPart.getAllPartition();
    }

    public ArrayList getFsList() {
        return getUnixPart.getFsList();
    }

    public SystemPartitionForUnix getSysPartStatistic1(String mp) {
        return getUnixPart.getSysPartStatistic(mp);
    }

    public String getSysPartContentsFromCmd() {
        return getPartition.prtMe();
    }

    public String getSysPartStrContents() {
        return this.getPartitionByCatConf.prtMe();
    }

    public String getUnixSysPartStrContents() {
        return getUnixPart.prtMe();
    }

    public boolean getUnixNetCard(String ip, int port, String args) {
        getUnixNetCard.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_NETCARD) + " " + ip + " " + port + " " + args);
        boolean isOk = getUnixNetCard.realDo();
        if (!isOk) {
            this.errorMsg = getUnixNetCard.getErrMsg();
            this.errorCode = getUnixNetCard.getRetCode();
        }
        return isOk;
    }

    public ArrayList getUnixNetCard() {
        return getUnixNetCard.getAllNetCard();
    }

    public boolean getUnixNetCardFromSrv(String conf) {
        getUnixNetCardFromSrv.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getUnixNetCardFromSrv.realDo();
        if (!isOk) {
            this.errorMsg = getUnixNetCardFromSrv.getErrMsg();
            errorCode = getUnixNetCardFromSrv.getRetCode();
        }
        return isOk;
    }

    public ArrayList getUnixNetCardFromSrv() {
        return getUnixNetCardFromSrv.getAllNetCard();
    }

    public boolean getDiskPartForWin(String ip, int port, String args) {
        return this.getDiskPartForWin(ip, port, args, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean getDiskPartForWin(String ip, int port, String args, int mode) {
        if (this.isMTPPCmd(mode)) {
            getDiskPartForWin.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + ip + " " + port + " " + args);
        } else {
            getDiskPartForWin.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " " + args);
        }
        getDiskPartForWin.setCmdType(mode);

        boolean isOk = getDiskPartForWin.realDo();
        if (!isOk) {
            this.errorMsg = getDiskPartForWin.getErrMsg();
            this.errorCode = getDiskPartForWin.getRetCode();
        }
        return isOk;
    }

    public String getDiskPartForWin() {
        return getDiskPartForWin.getContents();
    }

    public boolean getOldDiskPartitionTableForWin(String conf) {
        getOldDiskInfoForWin.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getOldDiskInfoForWin.realDo();
        if (!isOk) {
            this.errorMsg = getOldDiskInfoForWin.getErrMsg();
            this.errorCode = getOldDiskInfoForWin.getRetCode();
        }
        return isOk;
    }

    public ArrayList getAllOldDiskPartList() {
        return getOldDiskInfoForWin.getAllDiskPartInfo();
    }

    public String getAllOldDiskPartContents() {
        return getOldDiskInfoForWin.getContents();
    }

    public boolean getDiskPartForUnix(String ip, int port, String args) {
        getDiskPartForUnix.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + ip + " " + port + " " + args);
        boolean isOk = getDiskPartForUnix.realDo();
        if (!isOk) {
            this.errorMsg = getDiskPartForUnix.getErrMsg();
            this.errorCode = getDiskPartForUnix.getRetCode();
        }
        return isOk;
    }

    public String getDiskPartForUnix() {
        return getDiskPartForUnix.getContents();
    }

    public ArrayList<SystemPartitionForUnix> getIAHidenPartition() {
        return getDiskPartForUnix.getIAHidenPartition();
    }

    public Vector<SystemPartitionForUnix> getIAHidenPartition2() {
        return getDiskPartForUnix.getAllDiskPartInfo2();
    }

    public boolean getNewDiskPartitionTableForWin(String ip, int port, String args) {
        return this.getNewDiskPartitionTableForWin(ip, port, args, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean getNewDiskPartitionTableForWin(String ip, int port, String args, int mode) {
        if (this.isMTPPCmd(mode)) {
            getNewDiskInfoForWin.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_PARTITION) + " " + ip + " " + port + " " + args);
        } else {
            getNewDiskInfoForWin.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " " + args);
        }
        getNewDiskInfoForWin.setCmdType(mode);
        boolean isOk = getNewDiskInfoForWin.realDo();
        if (!isOk) {
            this.errorMsg = getNewDiskInfoForWin.getErrMsg();
            this.errorCode = getNewDiskInfoForWin.getRetCode();
        }
        return isOk;
    }

    public ArrayList getAllNewDiskPartList() {
        return getNewDiskInfoForWin.getAllDiskPartInfo();
    }

    public boolean getOldDiskPartitionTableForUnix(String conf) {
        getOldDiskInfoForUnix.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getOldDiskInfoForUnix.realDo();
        if (!isOk) {
            this.errorMsg = getOldDiskInfoForUnix.getErrMsg();
            this.errorCode = getOldDiskInfoForUnix.getRetCode();
        }
        return isOk;
    }

    public ArrayList getAllOldDiskPartListForUnix() {
        return getOldDiskInfoForUnix.getAllDiskPartInfo();
    }

    public String getAllOldDiskPartContentsForUnix() {
        return getOldDiskInfoForUnix.getContents();
    }

    public ArrayList<SystemPartitionForUnix> getIAHidenFsFromOldDiskPartForUnix() {
        return getOldDiskInfoForUnix.getIAHidenPartition();
    }

    public boolean getNewDiskPartitionTableForUnix(String ip, int port, String args) {
        getNewDiskInfoForUnix.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_PARTITION) + " " + ip + " " + port + " " + args);
        boolean isOk = getNewDiskInfoForUnix.realDo();
        if (!isOk) {
            this.errorMsg = getNewDiskInfoForUnix.getErrMsg();
            this.errorCode = getNewDiskInfoForUnix.getRetCode();
        }
        return isOk;
    }

    public ArrayList getAllNewDiskPartListForUnix() {
        return getNewDiskInfoForUnix.getAllDiskPartInfo();
    }

    public boolean formatDiskPartForUnix(String ip, int port, String conf, String oldDevName, String newDevName) {
        formatDiskPartForUnix.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + ip + " " + port + " auto_mkfs.sh -f " + conf + " " + oldDevName + " " + newDevName);
        boolean isOk = formatDiskPartForUnix.realDo();
        if (!isOk) {
            this.errorMsg = formatDiskPartForUnix.getErrMsg();
            this.errorCode = formatDiskPartForUnix.getRetCode();
        }
        return isOk;
    }

    public DiskForUnix getMpFromFormatPart1() {
        // 获取本地盘的mp
        return formatDiskPartForUnix.getMpList();
    }

    public DiskForUnix getMpFromFormatPart2() {
        // 获取lv的mp
        return formatDiskPartForUnix.getLVMpList();
    }

    public boolean formatDiskPartForUnix1(String ip, int port, String conf, String diskParm) {
        formatDiskPartForUnix.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + ip + " " + port + " vg_restore.sh -f " + conf + " " + diskParm);
        boolean isOk = formatDiskPartForUnix.realDo();
        if (!isOk) {
            this.errorMsg = formatDiskPartForUnix.getErrMsg();
            this.errorCode = formatDiskPartForUnix.getRetCode();
        }
        return isOk;
    }

    public boolean getRstMapper(String conf) {
        getRstMapper.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getRstMapper.realDo();
        if (!isOk) {
            this.errorMsg = getRstMapper.getErrMsg();
            this.errorCode = getRstMapper.getRetCode();
        }
        return isOk;
    }

    public HashMap getRstMapperList() {
        return getRstMapper.getAllMapper();
    }

    public Vector getRstMapperList1() {
        return getRstMapper.getAllMapper1();
    }

    public boolean getUnixRstMapper(String conf) {
        getUnixRstMapper.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getUnixRstMapper.realDo();
        if (!isOk) {
            this.errorMsg = getUnixRstMapper.getErrMsg();
            this.errorCode = getUnixRstMapper.getRetCode();
        }
        return isOk;
    }

    public HashMap getUnixRstMapperList() {
        return getUnixRstMapper.getAllMapper();
    }

    public Vector getUnixRstMapperList1() {
        return getUnixRstMapper.getAllMapper1();
    }

    public boolean getOsLoaderTypeOnClnt(String conf) {
        getOsLoaderTypeOnClnt.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getOsLoaderTypeOnClnt.realDo();
        if (!isOk) {
            this.errorMsg = getOsLoaderTypeOnClnt.getErrMsg();
            this.errorCode = getOsLoaderTypeOnClnt.getRetCode();
        }
        return isOk;
    }

    public String getOsLoaderType() {
        return getOsLoaderTypeOnClnt.getOsLoaderType();
    }

    public boolean getOSService(String ip, int port, String args) {
        return getOSService(ip, port, args, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean getOSService(String ip, int port, String args, int mode) {
        if (this.isMTPPCmd(mode)) {
            getService.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_OS_SERVICE) + " " + ip + " " + port + " " + args);
        } else {
            getService.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " services_process.exe list 2>/dev/null");
        }
        getService.setEncoding(ResourceCenter.DEFAULT_CHARACTER_SET);
        getService.setCmdType(mode);

        boolean isOk = getService.realDo();
        if (!isOk) {
            this.errorMsg = getService.getErrMsg();
            this.errorCode = getService.getRetCode();
        }
        return isOk;
    }

    public boolean getOSService(String conf) {
        return getOSService(conf, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean getOSService(String conf, int mode) {
        getService.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + " " + conf);
        getService.setEncoding("");
        if (mode == ResourceCenter.CMD_TYPE_MTPP) {
            getService.setCmdType(ResourceCenter.CMD_TYPE_MTPP);
        } else {
            getService.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        }
        boolean isOk = getService.realDo();
        if (!isOk) {
            this.errorMsg = getService.getErrMsg();
            this.errorCode = getService.getRetCode();
        }
        return isOk;
    }

    public Vector getOSService() {
        return getService.getAllServices();
    }

    public String getOSServiceContents() {
        return getOSServiceContents(ResourceCenter.CMD_TYPE_MTPP);
    }

    public String getOSServiceContents(int mode) {
        return getService.getAllServiceContents(mode);
    }

    public boolean formatVol(String cltIP, int cltPort, String vol, String fstype, String label, String active) {
        return this.formatVol(cltIP, cltPort, vol, fstype, label, active, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean formatVol(String cltIP, int cltPort, String vol, String fstype, String label, String active, int mode) {
        if (this.isMTPPCmd(mode)) {
            formatVol.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_FORMAT_VOL) + cltIP + " "
                    + cltPort + " ib_format_disk.exe " + vol + " " + fstype + " " + label + " " + active);
        } else {
            formatVol.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_format_disk.exe " + vol + " " + fstype + " " + label + " " + active);
        }
        formatVol.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "formatVol cmd: " + formatVol.getCmdLine());
        try {
            SanBootView.log.info(getClass().getName(), "format time out: " + formatVol.socket.getSoTimeout());
        } catch (Exception ex) {
        }
        try {
            formatVol.run();
        } catch (Exception ex) {
            recordException(formatVol, ex);
        }
        SanBootView.log.info(getClass().getName(), "formatVol cmd retcode: " + formatVol.getRetCode());
        boolean isOk = finished(formatVol);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "formatVol cmd errmsg: " + formatVol.getErrMsg());
        }
        return isOk;
    }

    public boolean formatUnixVol(String cltIP, int cltPort, String args) {
        formatVol.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_FORMAT_VOL) + cltIP + " "
                + cltPort + " formatlv.sh " + args);
        SanBootView.log.info(getClass().getName(), "formatVol cmd: " + formatVol.getCmdLine());
        try {
            SanBootView.log.info(getClass().getName(), "format time out: " + formatVol.socket.getSoTimeout());
        } catch (Exception ex) {
        }

        try {
            formatVol.run();
        } catch (Exception ex) {
            recordException(formatVol, ex);
        }
        SanBootView.log.info(getClass().getName(), "formatVol cmd retcode: " + formatVol.getRetCode());
        boolean isOk = finished(formatVol);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "formatVol cmd errmsg: " + formatVol.getErrMsg());
        }
        return isOk;
    }

    public boolean setEfiPartitionFlag(String cltIP, int cltPort, String args) {
        set_efi_partition_flag.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_SET_EFI_PART_FLAG) + cltIP + " "
                + cltPort + " ib_set_efi_partition_flag.sh " + args);
        SanBootView.log.info(getClass().getName(), "set efi partition flag cmd: " + set_efi_partition_flag.getCmdLine());

        try {
            set_efi_partition_flag.run();
        } catch (Exception ex) {
            recordException(set_efi_partition_flag, ex);
        }
        SanBootView.log.info(getClass().getName(), "set efi partition flag cmd retcode: " + set_efi_partition_flag.getRetCode());
        boolean isOk = finished(set_efi_partition_flag);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "set efi partition flag cmd errmsg: " + set_efi_partition_flag.getErrMsg());
        }
        return isOk;
    }

    public boolean addPortal(String clntIP, int cltPort, String srvIP, int srvPort) {
        return this.addPortal(clntIP, cltPort, srvIP, srvPort, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean addPortal(String clntIP, int cltPort, String srvIP, int srvPort, int mode) {
        if (this.isMTPPCmd(mode)) {
            addPortal.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADDPORTAL) + clntIP + " " + cltPort + " iscsi_demo.exe AddPortal " + srvIP + " " + srvPort);
        } else {
            addPortal.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(clntIP, cltPort) + " iscsi_demo.exe AddPortal " + srvIP + " " + srvPort);
        }
        addPortal.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "addPortal cmd: " + addPortal.getCmdLine());
        try {
            addPortal.run();
        } catch (Exception ex) {
            recordException(addPortal, ex);
        }
        SanBootView.log.info(getClass().getName(), "addPortal cmd retcode: " + addPortal.getRetCode());
        boolean isOk = finished(addPortal);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "addPortal cmd errmsg: " + addPortal.getErrMsg());
        }
        return isOk;
    }

    public boolean loginTarget(String clntIP, int cltPort, String tName) {
        return this.loginTarget(clntIP, cltPort, tName, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean loginTarget(String clntIP, int cltPort, String tName, int mode) {
        if (this.isMTPPCmd(mode)) {
            loginTarget.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_LOGINTARGET) + clntIP + " " + cltPort + " iscsi_demo.exe LoginTarget " + tName);
        } else {
            loginTarget.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(clntIP, cltPort) + " iscsi_demo.exe LoginTarget " + tName);
        }
        loginTarget.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "loginTarget cmd: " + loginTarget.getCmdLine());
        try {
            loginTarget.run();
        } catch (Exception ex) {
            recordException(loginTarget, ex);
        }
        SanBootView.log.info(getClass().getName(), "loginTarget cmd retcode: " + loginTarget.getRetCode());
        boolean isOk = finished(loginTarget);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "loginTarget cmd errmsg: " + loginTarget.getErrMsg());
        }
        return isOk;
    }

    public boolean loginUnixTarget(String clntIP, int cltPort, String args) {
        loginTarget.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_LOGINTARGET) + clntIP + " " + cltPort + " iscsi_cmd.sh login " + args);
        SanBootView.log.info(getClass().getName(), "loginTarget cmd: " + loginTarget.getCmdLine());
        try {
            loginTarget.run();
        } catch (Exception ex) {
            recordException(loginTarget, ex);
        }
        SanBootView.log.info(getClass().getName(), "loginTarget cmd retcode: " + loginTarget.getRetCode());
        boolean isOk = finished(loginTarget);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "loginTarget cmd errmsg: " + loginTarget.getErrMsg());
        }
        return isOk;
    }

    public boolean logoutTargetWithoutUmount(String clntIP, int cltPort, String tName) {
        return this.logoutTargetWithoutUmount(clntIP, cltPort, tName, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean logoutTargetWithoutUmount(String clntIP, int cltPort, String tName, int mode) {
        if (this.isMTPPCmd(mode)) {
            logoutTarget.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_LOGOUTTARGET) + clntIP + " " + cltPort + " iscsi_demo.exe LogoutTarget " + tName);
        } else {
            logoutTarget.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(clntIP, cltPort) + " iscsi_demo.exe LogoutTarget " + tName);
        }
        logoutTarget.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "logoutTarget cmd: " + logoutTarget.getCmdLine());
        try {
            logoutTarget.run();
        } catch (Exception ex) {
            recordException(logoutTarget, ex);
        }
        SanBootView.log.info(getClass().getName(), "logoutTarget cmd retcode: " + logoutTarget.getRetCode());
        boolean isOk = finished(logoutTarget);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "logoutTarget cmd errmsg: " + logoutTarget.getErrMsg());
        }
        return isOk;
    }

    public boolean logoutTarget(String clntIP, int cltPort, String tName, String drvLetter) {
        return this.logoutTarget(clntIP, cltPort, tName, drvLetter, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean logoutTarget(String clntIP, int cltPort, String tName, String drvLetter, int cmdType) {
        if (this.isMTPPCmd(cmdType)) {
            logoutTarget.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_LOGOUTTARGET) + clntIP + " " + cltPort + " iscsi_demo.exe LogoutTargetNew " + tName + " " + drvLetter);
        } else {
            logoutTarget.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(clntIP, cltPort) + " iscsi_demo.exe LogoutTargetNew " + tName + " " + drvLetter);
        }
        logoutTarget.setCmdType(cmdType);
        SanBootView.log.info(getClass().getName(), "logoutTarget cmd: " + logoutTarget.getCmdLine());
        try {
            logoutTarget.run();
        } catch (Exception ex) {
            recordException(logoutTarget, ex);
        }
        SanBootView.log.info(getClass().getName(), "logoutTarget cmd retcode: " + logoutTarget.getRetCode());
        boolean isOk = finished(logoutTarget);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "logoutTarget cmd errmsg: " + logoutTarget.getErrMsg());
        }
        return isOk;
    }

    public boolean logoutUnixTarget(String clntIP, int cltPort, String args) {
        logoutTarget.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_LOGOUTTARGET) + clntIP + " " + cltPort + " iscsi_cmd.sh logout " + args);
        SanBootView.log.info(getClass().getName(), "logoutTarget cmd: " + logoutTarget.getCmdLine());
        try {
            logoutTarget.run();
        } catch (Exception ex) {
            recordException(logoutTarget, ex);
        }
        SanBootView.log.info(getClass().getName(), "logoutTarget cmd retcode: " + logoutTarget.getRetCode());
        boolean isOk = finished(logoutTarget);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "logoutTarget cmd errmsg: " + logoutTarget.getErrMsg());
        }
        return isOk;
    }

    public boolean listTargetDisk(String cltIP, int cltPort) {
        return listTargetDisk(cltIP, cltPort, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean listTargetDisk(String cltIP, int cltPort, int cmdType) {
        if (this.isMTPPCmd(cmdType)) {
            listTDisk.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_LIST_TDISK) + " " + cltIP + " " + cltPort + " iscsi_demo.exe ListTargets");
        } else {
            listTDisk.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " iscsi_demo.exe ListTargets");
        }
        listTDisk.setCmdType(cmdType);

        boolean ret = listTDisk.realDo();
        if (!ret) {
            errorMsg = listTDisk.getErrMsg();
            errorCode = listTDisk.getRetCode();
        }
        return ret;
    }

    public boolean hasThisTargetDisk(String tName) {
        return listTDisk.hasThisTarget(tName);
    }

    public boolean getIPInfoFromClnt(String cltIP, int cltPort, String mac) {
        return getIPInfoFromClnt(cltIP, cltPort, mac, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean getIPInfoFromClnt(String cltIP, int cltPort, String mac, int mode) {
        if (this.isMTPPCmd(mode)) {
            getIpInfo.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_IP_INFO) + " " + cltIP + " " + cltPort + " ib_get_net_info.exe -a " + mac);
        } else {
            getIpInfo.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_get_net_info.exe -a " + mac);
        }
        getIpInfo.setCmdType(mode);
        boolean ret = getIpInfo.realDo();
        if (!ret) {
            errorMsg = getIpInfo.getErrMsg();
            errorCode = getIpInfo.getRetCode();
        }
        return ret;
    }

    public String getIpContents() {
        return getIpInfo.getAllContent();
    }

    public ArrayList getAllNetCardinfo() {
        return getIpInfo.getAllNetCard();
    }

    public boolean getIPInfoFromSrv(String conf) {
        getNetCardFromSrv.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean ret = getNetCardFromSrv.realDo();
        if (!ret) {
            errorMsg = getNetCardFromSrv.getErrMsg();
            errorCode = getNetCardFromSrv.getRetCode();
        }
        return ret;
    }

    public String getIpContentsFromSrv() {
        return getNetCardFromSrv.getAllContent();
    }


    public ArrayList getAllNetCardinfoFromSrv() {
        return getNetCardFromSrv.getAllNetCard();
    }

    public String getUnixIPContents(String mac) {
        return getUnixNetCard.getAllContent(mac);
    }

    public boolean copyOS(String cmdLine) {
        copyOS.setCmdLine(cmdLine);
        SanBootView.log.info(getClass().getName(), "copyOS cmd: " + copyOS.getCmdLine());
        try {
            copyOS.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            recordException(copyOS, ex);
        }
        SanBootView.log.info(getClass().getName(), "copyOS cmd retcode: " + copyOS.getRetCode());
        boolean isOk = finished(copyOS);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "copyOS cmd errmsg: " + copyOS.getErrMsg());
        }
        return isOk;
    }

    public boolean modyHostBoot(String cmdLine) {
        modyHostBoot.setCmdLine(cmdLine);
        SanBootView.log.info(getClass().getName(), "modyHostBoot cmd: " + modyHostBoot.getCmdLine());
        try {
            modyHostBoot.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            recordException(modyHostBoot, ex);
        }
        SanBootView.log.info(getClass().getName(), "modyHostBoot cmd retcode: " + modyHostBoot.getRetCode());
        boolean isOk = finished(modyHostBoot);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "modyHostBoot cmd errmsg: " + modyHostBoot.getErrMsg());
        }
        return isOk;
    }

    public boolean regOS(String cltIP, int cltPort, String regMode, String windir, String mac) {
        return regOS(cltIP, cltPort, regMode, windir, mac, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean regOS(String cltIP, int cltPort, String regMode, String windir, String mac, int mode) {
        if (this.isMTPPCmd(mode)) {
            regOS.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_REG_OS) + cltIP + " " + cltPort + " excutecmd.exe startbootreg.exe 480 " + windir + " " + regMode + " " + mac);
        } else {
            regOS.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " excutecmd.exe startbootreg.exe 480 " + windir + " " + regMode + " " + mac);
        }
        regOS.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "regOS cmd: " + regOS.getCmdLine());
        try {
            regOS.run();
        } catch (Exception ex) {
            recordException(regOS, ex);
        }
        SanBootView.log.info(getClass().getName(), "regOS cmd retcode: " + regOS.getRetCode());
        boolean isOk = finished(regOS);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "regOS cmd errmsg: " + regOS.getErrMsg());
        }
        return isOk;
    }

    public boolean regOS1(String cltIP, int cltPort, String windir) {
        return this.regOS1(cltIP, cltPort, windir, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean regOS1(String cltIP, int cltPort, String windir, int mode) {
        if (this.isMTPPCmd(mode)) {
            regOS.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_REG_OS) + cltIP + " " + cltPort + " ib_bcdedit_boot.exe " + windir);
        } else {
            regOS.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_bcdedit_boot.exe " + windir);
        }
        regOS.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "regOS cmd: " + regOS.getCmdLine());
        try {
            regOS.run();
        } catch (Exception ex) {
            recordException(regOS, ex);
        }
        SanBootView.log.info(getClass().getName(), "regOS cmd retcode: " + regOS.getRetCode());
        boolean isOk = finished(regOS);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "regOS cmd errmsg: " + regOS.getErrMsg());
        }
        return isOk;
    }

    public boolean regOS2(String cltIP, int cltPort, String regMode, String windir, String mac) {
        return regOS2(cltIP, cltPort, regMode, windir, mac, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean regOS2(String cltIP, int cltPort, String regMode, String windir, String mac, int mode) {
        if (this.isMTPPCmd(mode)) {
            regOS.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_REG_OS) + cltIP + " " + cltPort + " excutecmd.exe setreg.bat 480 " + windir + " " + regMode + " " + mac);
        } else {
            regOS.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " excutecmd.exe setreg.bat 480 " + windir + " " + regMode + " " + mac);
        }
        regOS.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "regOS cmd: " + regOS.getCmdLine());
        try {
            regOS.run();
        } catch (Exception ex) {
            recordException(regOS, ex);
        }
        SanBootView.log.info(getClass().getName(), "regOS cmd retcode: " + regOS.getRetCode());
        boolean isOk = finished(regOS);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "regOS cmd errmsg: " + regOS.getErrMsg());
        }
        return isOk;
    }

    public boolean regOS3(String cltIP, int cltPort, String windir) {
        return regOS3(cltIP, cltPort, windir, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean regOS3(String cltIP, int cltPort, String windir, int mode) {
        if (this.isMTPPCmd(mode)) {
            regOS.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_REG_OS) + cltIP + " " + cltPort + " ib_hba_reg.exe " + windir);
        } else {
            regOS.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_hba_reg.exe " + windir);
        }
        regOS.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "regOS cmd: " + regOS.getCmdLine());
        try {
            regOS.run();
        } catch (Exception ex) {
            recordException(regOS, ex);
        }
        SanBootView.log.info(getClass().getName(), "regOS cmd retcode: " + regOS.getRetCode());
        boolean isOk = finished(regOS);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "regOS cmd errmsg: " + regOS.getErrMsg());
        }
        return isOk;
    }

    public String getTargetDriver(String cltIP, int cltPort, String targetName) {
        return getTargetDriver(cltIP, cltPort, targetName, ResourceCenter.CMD_TYPE_MTPP);
    }

    public String getTargetDriver(String cltIP, int cltPort, String targetName, int cmdType) {
        if (this.isMTPPCmd(cmdType)) {
            getTargetDrv.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_TARGET_DRIVER) + cltIP + " " + cltPort + " ib_get_target_driver.exe " + targetName);
        } else {
            getTargetDrv.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_get_target_driver.exe " + targetName);
        }
        getTargetDrv.setCmdType(cmdType);
        String ret = getTargetDrv.getTargetDriver(targetName);
        this.errorCode = getTargetDrv.getRetCode();
        this.errorMsg = getTargetDrv.getErrMsg();
        return ret;
    }

    public boolean isStartupfromSAN(String cltIP, int cltPort, String driver) {
        return this.isStartupfromSAN(cltIP, cltPort, driver, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean isStartupfromSAN(String cltIP, int cltPort, String driver, int mode) {
        if (this.isMTPPCmd(mode)) {
            isStartupFromSAN.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IS_STARTUP_FROM_NET) + cltIP + " " + cltPort + " ib_get_disk_type.exe " + driver);
        } else {
            isStartupFromSAN.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_get_disk_type.exe " + driver);
        }
        isStartupFromSAN.setCmdType(mode);

        try {
            SanBootView.log.info(getClass().getName(), "isStartupfromSAN cmd: " + isStartupFromSAN.getCmdLine());
            isStartupFromSAN.run();
        } catch (Exception ex) {
            recordException(isStartupFromSAN, ex);
        }
        SanBootView.log.info(getClass().getName(), "isStartupFromSAN cmd retcode: " + isStartupFromSAN.getRetCode());
        boolean isOk = finished(isStartupFromSAN);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "isStartupFromSAN cmd errmsg: " + isStartupFromSAN.getErrMsg());
        }
        return isOk;
    }

    public boolean isStartupFromSAN() {
        return isStartupFromSAN.isStartupFromSAN();
    }

    public boolean isStartupfromNetBoot(String cltIP, int cltPort) {
        isStartupFromNetBoot.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_IS_STARTUP_FROM_NET) + cltIP + " " + cltPort + " judge_netboot.sh");
        try {
            SanBootView.log.info(getClass().getName(), "isStartupFromNetBoot cmd: " + isStartupFromNetBoot.getCmdLine());
            isStartupFromNetBoot.run();
        } catch (Exception ex) {
            recordException(isStartupFromNetBoot, ex);
        }
        SanBootView.log.info(getClass().getName(), "isStartupFromNetBoot cmd retcode: " + isStartupFromNetBoot.getRetCode());
        boolean isOk = finished(isStartupFromNetBoot);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "isStartupFromNetBoot cmd errmsg: " + isStartupFromNetBoot.getErrMsg());
        }
        return isOk;
    }

    public boolean isStartupFromNetBoot() {
        return isStartupFromNetBoot.isStartupFromNetBoot();
    }

    public boolean getIdleDriveLetter(String cltIP, int cltPort) {
        return this.getIdleDriveLetter(cltIP, cltPort, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean getIdleDriveLetter(String cltIP, int cltPort, int mode) {
        if (this.isMTPPCmd(mode)) {
            getIdleDriveLetter.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + cltIP + " " + cltPort + " ib_get_idle_drivers.exe");
        } else {
            getIdleDriveLetter.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_get_idle_drivers.exe");
        }
        getIdleDriveLetter.setCmdType(mode);
        try {
            SanBootView.log.info(getClass().getName(), "getIdleDriveLetter cmd: " + getIdleDriveLetter.getCmdLine());
            getIdleDriveLetter.cleanList();
            getIdleDriveLetter.run();
        } catch (Exception ex) {
            recordException(getIdleDriveLetter, ex);
        }
        SanBootView.log.info(getClass().getName(), "getIdleDriveLetter cmd retcode: " + getIdleDriveLetter.getRetCode());
        boolean isOk = finished(getIdleDriveLetter);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "getIdleDriveLetter cmd errmsg: " + getIdleDriveLetter.getErrMsg());
        }
        return isOk;
    }

    public String getIdleDrvLetter() {
        return getIdleDriveLetter.getIdelDriveLetter();
    }

    public ArrayList getIdleDrvLetterList() {
        return getIdleDriveLetter.getAllIdelDrvLetter();
    }

    public boolean doStartService(String cltIP, int cltPort, String mode, String service) {
        return startService.stopOrStartService(cltIP, cltPort, mode, service, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean doStartService(String cltIP, int cltPort, String mode, String service, int cmdtype) {
        return startService.stopOrStartService(cltIP, cltPort, mode, service, cmdtype);
    }

    public boolean loadInfo(String cltIP, int cltPort, String confile) {
        return this.loadInfo(cltIP, cltPort, confile, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean loadInfo(String cltIP, int cltPort, String confile, int mode) {
        if (this.isMTPPCmd(mode)) {
            loadInfo.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_LOAD_INFO) + cltIP + " " + cltPort + " ib_set_net_info.exe -f " + confile);
        } else {
            loadInfo.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_set_net_info.exe -f " + confile);
        }
        loadInfo.setCmdType(mode);

        try {
            SanBootView.log.info(getClass().getName(), "loadInfo cmd: " + loadInfo.getCmdLine());
            loadInfo.run();
        } catch (Exception ex) {
            recordException(loadInfo, ex);
        }
        SanBootView.log.info(getClass().getName(), "loadInfo cmd retcode: " + loadInfo.getRetCode());
        boolean isOk = finished(loadInfo);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "loadInfo cmd errmsg: " + loadInfo.getErrMsg());
        }
        return isOk;
    }

    public boolean assignDriver(String cltIP, int cltPort, String ip, String port, String volInfo, String driver) {
        return this.assignDriver(cltIP, cltPort, ip, port, volInfo, driver, ResourceCenter.CMD_TYPE_MTPP);
    }

    // volInfo可以是一个形如iqn.2005-05.cn.com.odysys.iscsi.uws181:32768的字符串
    public boolean assignDriver(String cltIP, int cltPort, String ip, String port, String volInfo, String driver, int cmdType) {
        SanBootView.log.debug(getClass().getName(), "getIscsiIP cmd before: " + ip);
        if (!ip.equals("NULL")) {
            boolean getipflag = getIscsiIP(cltIP);
            if (getipflag) {
                ip = getIscsiIP();
            }
        }
        SanBootView.log.debug(getClass().getName(), "getIscsiIP cmd after: " + ip);

        if (this.isMTPPCmd(cmdType)) {
            assignDriver.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ASSIGN_DRIVER) + cltIP + " " + cltPort + " ib_set_partition.exe "
                    + "\"" + ip + "\""
                    + " "
                    + "\"" + port + "\""
                    + " "
                    + driver + "=" + volInfo + " otherremain errorexit");
        } else {
            assignDriver.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_set_partition.exe "
                    + "\"" + ip + "\""
                    + " "
                    + "\"" + port + "\""
                    + " "
                    + driver + "=" + volInfo + " otherremain errorexit");
        }
        assignDriver.setCmdType(cmdType);
        SanBootView.log.info(getClass().getName(), "assignDriver cmd: " + assignDriver.getCmdLine());
        try {
            assignDriver.run();
        } catch (Exception ex) {
            recordException(assignDriver, ex);
        }
        SanBootView.log.info(getClass().getName(), "assignDriver cmd retcode: " + assignDriver.getRetCode());
        boolean isOk = finished(assignDriver);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "assignDriver cmd errmsg: " + assignDriver.getErrMsg());
        }
        return isOk;
    }

    // volInfo可以是一个形如iqn.2005-05.cn.com.odysys.iscsi.uws181:32768的字符串
    public boolean assignDriver1(String cltIP, int cltPort, String ip, String port, String volInfo, String driver, int cmdType) {
        SanBootView.log.debug(getClass().getName(), "getIscsiIP cmd before: " + ip);
//        if( !ip.equals("NULL")){
//            boolean getipflag = getIscsiIP(cltIP);
//            if( getipflag )
//                ip = getIscsiIP();
//        }
        SanBootView.log.debug(getClass().getName(), "getIscsiIP cmd after: " + ip);

        if (this.isMTPPCmd(cmdType)) {
            assignDriver.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ASSIGN_DRIVER) + cltIP + " " + cltPort + " ib_set_partition.exe "
                    + "\"" + ip + "\""
                    + " "
                    + "\"" + port + "\""
                    + " "
                    + driver + "=" + volInfo + " otherremain errorexit");
        } else {
            assignDriver.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_set_partition.exe "
                    + "\"" + ip + "\""
                    + " "
                    + "\"" + port + "\""
                    + " "
                    + driver + "=" + volInfo + " otherremain errorexit");
        }
        assignDriver.setCmdType(cmdType);
        SanBootView.log.info(getClass().getName(), "assignDriver cmd: " + assignDriver.getCmdLine());
        try {
            assignDriver.run();
        } catch (Exception ex) {
            recordException(assignDriver, ex);
        }
        SanBootView.log.info(getClass().getName(), "assignDriver cmd retcode: " + assignDriver.getRetCode());
        boolean isOk = finished(assignDriver);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "assignDriver cmd errmsg: " + assignDriver.getErrMsg());
        }
        return isOk;
    }

    /**
     * 切换网络盘时，通过agent的ip，取得需要的服务器的ip
     *
     * @param ip
     * @return
     */
    public boolean getIscsiIP(String ip) {
        getIscsiIP.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_GETISCSIIP) + ip);
        SanBootView.log.info(getClass().getName(), "getIscsiIP cmd: " + getIscsiIP.getCmdLine());
        try {
            getIscsiIP.run();
        } catch (Exception ex) {
            recordException(mntDriver, ex);
        }
        SanBootView.log.info(getClass().getName(), "getIscsiIP cmd retcode: " + getIscsiIP.getRetCode());
        boolean isOk = finished(getIscsiIP);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "getIscsiIP cmd errmsg: " + getIscsiIP.getErrMsg());
        }
        return isOk;
    }

    public String getIscsiIP() {
        return getIscsiIP.getIP();
    }

    public boolean mntDevice(String cltIP, int cltPort, String devName, String mp) {
        mntDriver.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + cltIP + " " + cltPort + " auto_mount.sh " + devName + " " + mp);
        SanBootView.log.info(getClass().getName(), "mount dev cmd: " + mntDriver.getCmdLine());
        try {
            mntDriver.run();
        } catch (Exception ex) {
            recordException(mntDriver, ex);
        }
        SanBootView.log.info(getClass().getName(), "mount dev cmd retcode: " + mntDriver.getRetCode());
        boolean isOk = finished(mntDriver);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mount dev cmd errmsg: " + mntDriver.getErrMsg());
        }
        return isOk;
    }

    public boolean setPartitionActive(String cltIP, int cltPort, String drvLetter, String activeFlag) {
        return this.setPartitionActive(cltIP, cltPort, drvLetter, activeFlag, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean setPartitionActive(String cltIP, int cltPort, String drvLetter, String activeFlag, int mode) {
        if (this.isMTPPCmd(mode)) {
            setPartitionActive.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_SET_PART_ACTIVE) + cltIP + " " + cltPort + " ib_set_active.exe "
                    + drvLetter + " " + activeFlag);
        } else {
            setPartitionActive.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_set_active.exe "
                    + drvLetter + " " + activeFlag);
        }
        setPartitionActive.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "setPartitionActive cmd: " + setPartitionActive.getCmdLine());
        try {
            setPartitionActive.run();
        } catch (Exception ex) {
            recordException(setPartitionActive, ex);
        }
        SanBootView.log.info(getClass().getName(), "setPartitionActive cmd retcode: " + setPartitionActive.getRetCode());
        boolean isOk = finished(setPartitionActive);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "setPartitionActive cmd errmsg: " + setPartitionActive.getErrMsg());
        }
        return isOk;
    }

    public boolean isRight() {
        return setPartitionActive.isRight();
    }

    public boolean getNetInfoFromMDB1(String conf) {
        getNetInfo.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getNetInfo.realDo1();
        if (!isOk) {
            this.errorMsg = getNetInfo.getErrMsg();
            this.errorCode = getNetInfo.getRetCode();
        }
        return isOk;
    }

    public boolean getNetInfoFromMDB(String conf) {
        getNetInfo.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getNetInfo.realDo();
        if (!isOk) {
            this.errorMsg = getNetInfo.getErrMsg();
            this.errorCode = getNetInfo.getRetCode();
        }
        return isOk;
    }

    public String getBootMac() {
        return getNetInfo.getBootMac();
    }

    public String getNetInfoErr() {
        return getNetInfo.getErrMsg();
    }

    public boolean getLVMInfoFromPXELinux(String conf) {
        getLVMInfo.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getLVMInfo.realDo();
        if (!isOk) {
            this.errorMsg = getLVMInfo.getErrMsg();
            this.errorCode = getLVMInfo.getRetCode();
        }
        return isOk;
    }

    public ArrayList getLVMInfo() {
        return getLVMInfo.parse();
    }

    public String getLVMInfoString() {
        return getLVMInfo.prtInfo();
    }

    public boolean getUnixNetCardInfo1(String conf) {
        getUnixNetCardInfo.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getUnixNetCardInfo.realDo1();
        if (!isOk) {
            this.errorMsg = getUnixNetCardInfo.getErrMsg();
            errorCode = getUnixNetCardInfo.getRetCode();
        }
        return isOk;
    }

    public boolean getUnixNetCardInfo(String conf) {
        getUnixNetCardInfo.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getUnixNetCardInfo.realDo();
        if (!isOk) {
            this.errorMsg = getUnixNetCardInfo.getErrMsg();
            errorCode = getUnixNetCardInfo.getRetCode();
        }
        return isOk;
    }

    public String getUnixBootMac() {
        return getUnixNetCardInfo.getBootMac();
    }

    public String getUnixNetCardInifoErr() {
        return getUnixNetCardInfo.getErrMsg();
    }

    public boolean getPersistentTarget(String ip, int port) {
        return getPersistentTarget(ip, port, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean getPersistentTarget(String ip, int port, int cmdType) {
        if (this.isMTPPCmd(cmdType)) {
            getPersistentTarget.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + "  iscsi_demo.exe ListPersistentTargets");
        } else {
            getPersistentTarget.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(ip, port) + "  iscsi_demo.exe ListPersistentTargets");
        }
        getPersistentTarget.setCmdType(cmdType);
        boolean isOk = getPersistentTarget.realDo();
        if (!isOk) {
            this.errorCode = getPersistentTarget.getRetCode();
            this.errorMsg = getPersistentTarget.getErrMsg();
        }
        return isOk;
    }

    public ArrayList getPersistentTgtList() {
        return getPersistentTarget.getAllPersistent();
    }

    public boolean addPersistentTarget(String cltIP, int cltPort, String iscsiVar) {
        return this.addPersistentTarget(cltIP, cltPort, iscsiVar, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean addPersistentTarget(String cltIP, int cltPort, String iscsiVar, int mode) {
        if (this.isMTPPCmd(mode)) {
            addPersistentTarget.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_LONGTIME_TARGET) + cltIP + " " + cltPort + " iscsi_demo.exe AddPersitentTarget " + iscsiVar);
        } else {
            addPersistentTarget.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " iscsi_demo.exe AddPersitentTarget " + iscsiVar);
        }
        addPersistentTarget.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "addPersistentTarget cmd: " + addPersistentTarget.getCmdLine());
        try {
            addPersistentTarget.run();
        } catch (Exception ex) {
            recordException(addPersistentTarget, ex);
        }
        SanBootView.log.info(getClass().getName(), "addPersistentTarget cmd retcode: " + addPersistentTarget.getRetCode());
        boolean isOk = finished(addPersistentTarget);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "addPersistentTarget cmd errmsg: " + addPersistentTarget.getErrMsg());
        }
        return isOk;
    }

    public boolean addIscsiInitorDriver(String cltIP, int cltPort) {
        addIscsiInitDriver.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + cltIP + " " + cltPort + " load_iscsi_module.sh");
        SanBootView.log.info(getClass().getName(), "addIscsiInitDriver cmd: " + addIscsiInitDriver.getCmdLine());
        try {
            addIscsiInitDriver.run();
        } catch (Exception ex) {
            recordException(addIscsiInitDriver, ex);
        }
        SanBootView.log.info(getClass().getName(), "addIscsiInitDriver cmd retcode: " + addIscsiInitDriver.getRetCode());
        boolean isOk = finished(addIscsiInitDriver);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "addIscsiInitDriver cmd errmsg: " + addIscsiInitDriver.getErrMsg());
        }
        return isOk;
    }

    public boolean delPersistentTarget(String cltIP, int cltPort, String iscsiVar) {
        return this.delPersistentTarget(cltIP, cltPort, iscsiVar, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean delPersistentTarget(String cltIP, int cltPort, String iscsiVar, int mode) {
        if (this.isMTPPCmd(mode)) {
            delPersistentTarget.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_LONGTIME_TARGET) + cltIP + " " + cltPort + " iscsi_demo.exe RemovePersitentTarget " + iscsiVar);
        } else {
            delPersistentTarget.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " iscsi_demo.exe RemovePersitentTarget " + iscsiVar);
        }
        delPersistentTarget.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "delPersistentTarget cmd: " + delPersistentTarget.getCmdLine());
        try {
            delPersistentTarget.run();
        } catch (Exception ex) {
            recordException(delPersistentTarget, ex);
        }
        SanBootView.log.info(getClass().getName(), "delPersistentTarget cmd retcode: " + delPersistentTarget.getRetCode());
        boolean isOk = finished(delPersistentTarget);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delPersistentTarget cmd errmsg: " + delPersistentTarget.getErrMsg());
        }
        return isOk;
    }

    public boolean rebootHost(String cltIP, int cltPort) {
        return this.rebootHost(cltIP, cltPort, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean rebootHost(String cltIP, int cltPort, int mode) {
        if (this.isMTPPCmd(mode)) {
            rebootHost.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_REBOOT_HOST) + cltIP + " " + cltPort + " ib_shutdown.exe -r");
        } else {
            rebootHost.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_shutdown.exe -r");
        }
        rebootHost.setCmdType(mode);
        SanBootView.log.info(getClass().getName(), "rebootHost cmd: " + rebootHost.getCmdLine());
        try {
            rebootHost.run();
        } catch (Exception ex) {
            recordException(rebootHost, ex);
        }
        SanBootView.log.info(getClass().getName(), "rebootHost cmd retcode: " + rebootHost.getRetCode());
        boolean isOk = finished(rebootHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "rebootHost cmd errmsg: " + rebootHost.getErrMsg());
        }
        return isOk;
    }

    public boolean sendNetConf(String cltIP, int cltPort, String confile) {
        sendNetConf.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_SEND_INFO) + cltIP + " " + cltPort + " " + confile);
        SanBootView.log.info(getClass().getName(), "sendNetConf cmd: " + sendNetConf.getCmdLine());
        try {
            sendNetConf.run();
        } catch (Exception ex) {
            recordException(sendNetConf, ex);
        }
        SanBootView.log.info(getClass().getName(), "sendNetConf cmd retcode: " + sendNetConf.getRetCode());
        boolean isOk = finished(sendNetConf);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "sendNetConf cmd errmsg: " + sendNetConf.getErrMsg());
        }
        return isOk;
    }

    public String getLVMType(String cltIP, int cltPort) {
        getLVMType.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_WINDIR) + cltIP + " " + cltPort + " getvgtype.sh");
        String ret = getLVMType.getLVMType();
        this.errorCode = getLVMType.getRetCode();
        this.errorMsg = getLVMType.getErrMsg();
        return ret;
    }

    public boolean getBakClientList() {
        boolean ret = getBakClnt.updateClient();
        if (!ret) {
            this.errorMsg = getBakClnt.getErrMsg();
            this.errorCode = getBakClnt.getRetCode();
        }
        return ret;
    }

    public Vector getBalClntList() {
        return getBakClnt.getAllBackupClient();
    }

    public BackupClient getClientFromVector(long id) {
        return getBakClnt.getClientFromVector(id);
    }

    public BackupClient getClientFromVectorOnID(String id) {
        return getBakClnt.getClientFromVectorOnID(id);
    }

    public BackupClient getClientFromVectorOnName(String name) {
        return getBakClnt.getClientFromVectorOnName(name);
    }

    public void addBakClntIntoCache(BackupClient client) {
        getBakClnt.addClientToVector(client);
    }

    public void removeClientFromVector(BackupClient client) {
        getBakClnt.removeClientFromVector(client);
    }

    public BackupClient getBkClntOnUUID(String uuid) {
        return getBakClnt.getBkClntOnUUID(uuid);
    }

    public BackupClient getBkClntOnUUIDAndIP(String uuid, String ip) {
        return getBakClnt.getBkClntOnUUIDAndIP(uuid, ip);
    }

    public BackupClient getBkClntForRestOriDisk(String ip, int port) {
        return getBakClnt.getBkClntForRestOriDisk(ip, port);
    }

    public boolean ModOneClient1(BackupClient cli) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" clnt_id=" + cli.getID());
            buf.append(" clnt_name=" + cli.getHostName());
            buf.append(" clnt_ip=" + cli.getIP());
            buf.append(" clnt_machine=" + cli.getMachineType());
            buf.append(" clnt_port=" + cli.getPort());
            buf.append(" clnt_os=" + cli.getOsType());
            buf.append(" clnt_uuid=" + cli.getUUID());

            modNode.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_CLIENT)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), " mod client cmd: " + modNode.getCmdLine());
            modNode.run();

        } catch (Exception ex) {
            recordException(modNode, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod client cmd retcode: " + modNode.getRetCode());
        boolean isOk = finished(modNode);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod client cmd errmsg: " + modNode.getErrMsg());
        }
        return isOk;
    }

    public boolean ModOneClient(BackupClient cli) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" clnt_id=" + cli.getID());
            buf.append(" clnt_name=" + cli.getHostName());
            buf.append(" clnt_ip=" + cli.getIP());
            buf.append(" clnt_machine=" + cli.getMachineType());
            buf.append(" clnt_port=" + cli.getPort());
            buf.append(" clnt_os=" + cli.getOsType());
            buf.append(" clnt_status=" + cli.getStatus());
            buf.append(" clnt_uuid=" + cli.getUUID());
            buf.append(" clnt_account_id=" + cli.getAcctID());

            modNode.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_CLIENT)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), " mod client cmd: " + modNode.getCmdLine());
            modNode.run();

        } catch (Exception ex) {
            recordException(modNode, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod client cmd retcode: " + modNode.getRetCode());
        boolean isOk = finished(modNode);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod client cmd errmsg: " + modNode.getErrMsg());
        }
        return isOk;
    }

    public boolean addOneClient(BackupClient cli) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" clnt_name=" + cli.getHostName());
            buf.append(" clnt_ip=" + cli.getIP());
            buf.append(" clnt_machine=" + cli.getMachineType());
            buf.append(" clnt_port=" + cli.getPort());
            buf.append(" clnt_os=" + cli.getOsType());
            buf.append(" clnt_status=" + cli.getStatus());
            buf.append(" clnt_uuid=" + cli.getUUID());
            buf.append(" clnt_account_id=" + cli.getAcctID());

            addNode.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_CLIENT)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), " add clnt cmd: " + addNode.getCmdLine());
            addNode.run();
        } catch (Exception ex) {
            recordException(addNode, ex);
        }

        recordNewId(addNode);
        SanBootView.log.info(getClass().getName(), " add clnt retcode: " + addNode.getRetCode());
        boolean isOk = finished(addNode);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " add clnt errmsg: " + addNode.getErrMsg());
        }
        return isOk;
    }

    public boolean delClient(BackupClient cli) {
        try {
            delNode.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_CLIENT)
                    + cli.getID());
            SanBootView.log.info(getClass().getName(), " delete clnt cmd: " + delNode.getCmdLine());
            delNode.run();
        } catch (Exception ex) {
            recordException(delNode, ex);
        }
        SanBootView.log.info(getClass().getName(), " delete clnt cmd retcode: " + delNode.getRetCode());
        boolean isOk = finished(delNode);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " delete clnt cmd errmsg: " + delNode.getErrMsg());
        }
        return isOk;
    }

    public String getKernelVer(String ip, int port, String args) {
        getKernelVer.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " linux_iboot_cmd.sh " + args);
        String ret = getKernelVer.getKernelVer();
        this.errorCode = getKernelVer.getRetCode();
        this.errorMsg = getKernelVer.getErrMsg();
        return ret;
    }

    public boolean isCrtVGOnTgt(String ip, int port, String args) {
        isCrtVgOnTgt.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IS_CRT_VG_ON_TGT) + ip + " " + port + " vgcmd.sh iscrtvg " + args);
        boolean isOk = isCrtVgOnTgt.isCrtVgOnTgt();
        if (!isOk) {
            this.errorCode = isCrtVgOnTgt.getRetCode();
            this.errorMsg = isCrtVgOnTgt.getErrMsg();
        }
        return isOk;
    }

    public boolean isCrtLVOnTgt(String ip, int port, String args) {
        isCrtLVOnTgt.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IS_CRT_LV_ON_TGT) + ip + " " + port + " vgcmd.sh iscrtlv " + args);
        boolean isOk = isCrtLVOnTgt.isCrtLVOnTgt();
        if (!isOk) {
            this.errorCode = isCrtLVOnTgt.getRetCode();
            this.errorMsg = isCrtLVOnTgt.getErrMsg();
        }
        return isOk;
    }

    public boolean delVg(String ip, int port, String args) {
        delLVMVG.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_DEL_LVMVG) + ip + " " + port + " vgcmd.sh rmvg_all " + args);
        boolean isOk = delLVMVG.delAllInVg();
        if (!isOk) {
            this.errorCode = delLVMVG.getRetCode();
            this.errorMsg = delLVMVG.getErrMsg();
        }
        return isOk;
    }

    public String getMp() {
        return mntLV.getMountPoint();
    }

    public String getTftpRootPath() {
        return getTftpRootPath(0, 0, "", "", 0);
    }

    public String getTftpRootPath(int where, int dest_poolid, String dest_pool_passwd, String dest_uws_ip, int dest_uws_port) {
        if (where == 0) { // get tftp root path on src_UWS server
            getTftpRootPath.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_TFTP_ROOT));
            String ret = getTftpRootPath.getTftpRootPath();
            this.errorCode = getTftpRootPath.getRetCode();
            this.errorMsg = getTftpRootPath.getErrMsg();
            return ret;
        } else { // get tftp root path on dest_UWS server
            getTftpRootPath.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + " -p " + dest_poolid + " -P " + dest_pool_passwd
                    + " " + dest_uws_ip + " " + dest_uws_port + " getTftpRoot");
            String ret1 = getTftpRootPath.getTftpRootPath();
            this.errorCode = getTftpRootPath.getRetCode();
            this.errorMsg = getTftpRootPath.getErrMsg();
            return ret1;
        }
    }

    public String getIscsiCmdPath(String ip, int port) {
        getIscsiCmdPath.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " get_iboot_path.sh");
        String ret = getIscsiCmdPath.getIScsiCmdPath();
        this.errorCode = getIscsiCmdPath.getRetCode();
        this.errorMsg = getIscsiCmdPath.getErrMsg();
        return ret;
    }

    public boolean sendFileFromAgtToSrv(String ip, int port, String src, String dest) {
        sendFileFromAgtToSrv.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_SEND_FILE) + ip + ":" + port + " " + src + " " + dest);
        SanBootView.log.info(getClass().getName(), "sendFileFromAgtToSrv cmd: " + sendFileFromAgtToSrv.getCmdLine());
        try {
            sendFileFromAgtToSrv.run();
        } catch (Exception ex) {
            recordException(sendFileFromAgtToSrv, ex);
        }
        SanBootView.log.info(getClass().getName(), "sendFileFromAgtToSrv cmd retcode: " + sendFileFromAgtToSrv.getRetCode());
        boolean isOk = finished(sendFileFromAgtToSrv);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "sendFileFromAgtToSrv cmd errmsg: " + sendFileFromAgtToSrv.getErrMsg());
        }
        return isOk;
    }

    public boolean umountFs(String ip, int port, String mp) {
        umountFS.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " myumount.sh " + mp);
        SanBootView.log.info(getClass().getName(), "umountFS cmd: " + umountFS.getCmdLine());
        try {
            umountFS.run();
        } catch (Exception ex) {
            recordException(umountFS, ex);
        }
        SanBootView.log.info(getClass().getName(), "umountFS cmd retcode: " + umountFS.getRetCode());
        boolean isOk = finished(umountFS);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "umountFS cmd errmsg: " + umountFS.getErrMsg());
        }
        return isOk;
    }

    public boolean umountFs(String mp) {
        umountFS.setCmdLine("umount " + mp);
        SanBootView.log.info(getClass().getName(), "umountFS cmd: " + umountFS.getCmdLine());
        try {
            umountFS.run();
        } catch (Exception ex) {
            recordException(umountFS, ex);
        }
        SanBootView.log.info(getClass().getName(), "umountFS cmd retcode: " + umountFS.getRetCode());
        boolean isOk = finished(umountFS);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "umountFS cmd errmsg: " + umountFS.getErrMsg());
        }
        return isOk;
    }

    public boolean mountPool(String dev, String mp) {
        mountPool.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_MOUNT_POOL) + dev + " " + mp);
        SanBootView.log.info(getClass().getName(), "mountPool cmd: " + mountPool.getCmdLine());
        try {
            mountPool.run();
        } catch (Exception ex) {
            recordException(mountPool, ex);
        }
        SanBootView.log.info(getClass().getName(), "mountPool cmd retcode: " + mountPool.getRetCode());
        boolean isOk = finished(mountPool);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mountPool cmd errmsg: " + mountPool.getErrMsg());
        }
        return isOk;
    }

    public boolean mkFSForPool(String dev) {
        mkfs.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_MKFS_FORPOOL) + dev);
        SanBootView.log.info(getClass().getName(), "mkfs cmd: " + mkfs.getCmdLine());
        try {
            SanBootView.log.info(getClass().getName(), "format time out: " + mkfs.socket.getSoTimeout());
        } catch (Exception ex) {
        }
        try {
            mkfs.run();
        } catch (Exception ex) {
            recordException(mkfs, ex);
        }
        SanBootView.log.info(getClass().getName(), "mkfs cmd retcode: " + mkfs.getRetCode());
        boolean isOk = finished(mkfs);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mkfs cmd errmsg: " + mkfs.getErrMsg());
        }
        return isOk;
    }

    public boolean rstPartition(String cltIP, int cltPort, String confile, int old_disk, int new_disk) {
        return this.rstPartition(cltIP, cltPort, confile, old_disk, new_disk, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean rstPartition(String cltIP, int cltPort, String confile, int old_disk, int new_disk, int mode) {
        if (this.isMTPPCmd(mode)) {
            rstPartition.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + cltIP + " " + cltPort + " ib_restore_partition.exe -restore -f " + confile + " " + old_disk + " " + new_disk);
        } else {
            rstPartition.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_restore_partition.exe -restore -f " + confile + " " + old_disk + " " + new_disk);
        }
        rstPartition.setCmdType(mode);
        try {
            SanBootView.log.info(getClass().getName(), "restore partition cmd: " + rstPartition.getCmdLine());
            rstPartition.run();
        } catch (Exception ex) {
            recordException(rstPartition, ex);
        }
        SanBootView.log.info(getClass().getName(), "rstPartition cmd retcode: " + rstPartition.getRetCode());
        boolean isOk = finished(rstPartition);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "rstPartition cmd errmsg: " + rstPartition.getErrMsg());
        }
        return isOk;
    }

    public boolean rstPartition(String cltIP, int cltPort, String confile, String oldDevName, String newDevName) {
        rstPartition.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + cltIP + " " + cltPort + " restore_partition.sh -f " + confile + " " + oldDevName + " " + newDevName);

        try {
            SanBootView.log.info(getClass().getName(), "restore partition cmd: " + rstPartition.getCmdLine());
            rstPartition.run();
        } catch (Exception ex) {
            recordException(rstPartition, ex);
        }
        SanBootView.log.info(getClass().getName(), "rstPartition cmd retcode: " + rstPartition.getRetCode());
        boolean isOk = finished(rstPartition);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "rstPartition cmd errmsg: " + rstPartition.getErrMsg());
        }
        return isOk;
    }

    public boolean rstPartition1(String cltIP, int cltPort, String confile, String oldDevName, String newDevName) {
        rstPartition.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + cltIP + " " + cltPort + " pv_vg_restore.sh -f " + confile + " " + oldDevName + " " + newDevName);

        try {
            SanBootView.log.info(getClass().getName(), "pv_vg restore partition cmd: " + rstPartition.getCmdLine());
            rstPartition.run();
        } catch (Exception ex) {
            recordException(rstPartition, ex);
        }
        SanBootView.log.info(getClass().getName(), "pv_vg rstPartition cmd retcode: " + rstPartition.getRetCode());
        boolean isOk = finished(rstPartition);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "pv_vg rstPartition cmd errmsg: " + rstPartition.getErrMsg());
        }
        return isOk;
    }

    public boolean listPartition(String ip, int port, String conf, int old_disk, int new_disk) {
        return this.listPartition(ip, port, conf, old_disk, new_disk, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean listPartition(String ip, int port, String conf, int old_disk, int new_disk, int mode) {
        if (this.isMTPPCmd(mode)) {
            listPartition.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " ib_auto_mkfs.exe -list -f " + conf + " " + old_disk + " " + new_disk);
        } else {
            listPartition.setCmdLine(
                    ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " ib_auto_mkfs.exe -list -f " + conf + " " + old_disk + " " + new_disk);
        }
        listPartition.setCmdType(mode);
        boolean isOk = listPartition.realDo();
        if (!isOk) {
            this.errorMsg = listPartition.getErrMsg();
            this.errorCode = listPartition.getRetCode();
        }
        return isOk;
    }

    public String getListPartContents() {
        return listPartition.getListContents();
    }

    public boolean formatPartition(String ip, int port, String conf, int old_disk, int new_disk) {
        return this.formatPartition(ip, port, conf, old_disk, new_disk, "", ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean formatPartition(String ip, int port, String conf, int old_disk, int new_disk, String noFormat, int mode) {
        String cmdLine;

        if (this.isMTPPCmd(mode)) {
            cmdLine = ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " ib_auto_mkfs.exe -restore -f " + conf + " " + old_disk + " " + new_disk;
            formatPartition.setCmdLine(
                    noFormat.equals("") ? cmdLine : cmdLine + " " + noFormat);
        } else {
            cmdLine = ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " ib_auto_mkfs.exe -restore -f " + conf + " " + old_disk + " " + new_disk;
            formatPartition.setCmdLine(
                    noFormat.equals("") ? cmdLine : cmdLine + " " + noFormat);
        }
        formatPartition.setCmdType(mode);
        boolean isOk = formatPartition.realDo();
        if (!isOk) {
            this.errorMsg = formatPartition.getErrMsg();
            this.errorCode = formatPartition.getRetCode();
        }
        return isOk;
    }

    public DiskForWin getMpFromFormatPart() {
        return formatPartition.getMpList();
    }

    // 适用于下列命令:
    // set LVMTYPE
    // set MAC
    // set DST_KERNEL_IMG
    // set CONFIGFILE
    // initiator dir
    public boolean setBootInfoEnvVar(String ip, int port, String args) {
        setBootInfoEnvVar.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " linux_iboot_cmd.sh " + args);
        SanBootView.log.info(getClass().getName(), "setBootInfoEnvVar cmd: " + setBootInfoEnvVar.getCmdLine());
        try {
            setBootInfoEnvVar.run();
        } catch (Exception ex) {
            recordException(setBootInfoEnvVar, ex);
        }
        SanBootView.log.info(getClass().getName(), "setBootInfoEnvVar cmd retcode: " + setBootInfoEnvVar.getRetCode());
        boolean isOk = finished(setBootInfoEnvVar);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "setBootInfoEnvVar cmd errmsg: " + setBootInfoEnvVar.getErrMsg());
        }
        return isOk;
    }

    public boolean setIBootConfigure(String ip, int port, String args) {
        this.setIBootConfigure.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " iboot_set_configure.sh " + args);
        SanBootView.log.info(getClass().getName(), "setIBootConfigure cmd: " + setIBootConfigure.getCmdLine());
        try {
            setIBootConfigure.run();
        } catch (Exception ex) {
            recordException(setIBootConfigure, ex);
        }
        SanBootView.log.info(getClass().getName(), "setIBootConfigure cmd retcode: " + setIBootConfigure.getRetCode());
        boolean isOk = finished(setIBootConfigure);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "setIBootConfigure cmd errmsg: " + setIBootConfigure.getErrMsg());
        }
        return isOk;
    }

    public String genKernelImg(String ip, int port, String args) {
        genKernelImg.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " linux_iboot_cmd.sh " + args);
        String ret = genKernelImg.genIbootKernelImg();
        this.errorCode = genKernelImg.getRetCode();
        this.errorMsg = genKernelImg.getErrMsg();
        return ret;
    }

    public String genInitrd(String ip, int port, String args) {
        genInitrd.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " linux_iboot_cmd.sh " + args);
        String ret = genInitrd.genIbootInitrd();
        this.errorCode = genInitrd.getRetCode();
        this.errorMsg = genInitrd.getErrMsg();
        return ret;
    }

    public StringBuffer genPxeLinux(String ip, int port, String args) {
        genPxeLinux.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " linux_iboot_cmd.sh " + args);
        StringBuffer ret = genPxeLinux.genIbootPxeLinux();
        this.errorCode = genPxeLinux.getRetCode();
        this.errorMsg = genPxeLinux.getErrMsg();
        return ret;
    }

    public boolean getBootInfoVar(String ip, int port, String args) {
        getBootInfoVar.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " linux_iboot_cmd.sh " + args);
        SanBootView.log.info(getClass().getName(), "getBootInfoVar cmd: " + getBootInfoVar.getCmdLine());
        try {
            getBootInfoVar.run();
        } catch (Exception ex) {
            recordException(getBootInfoVar, ex);
        }
        SanBootView.log.info(getClass().getName(), "getBootInfoVar cmd retcode: " + getBootInfoVar.getRetCode());
        boolean isOk = finished(getBootInfoVar);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "getBootInfoVar cmd errmsg: " + getBootInfoVar.getErrMsg());
        }
        return isOk;
    }

    public boolean changeFsLabel(String ip, int port, String args) {
        chgFsLabel.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " mye2label.sh " + args);
        SanBootView.log.info(getClass().getName(), "chgFsLabel cmd: " + chgFsLabel.getCmdLine());
        try {
            chgFsLabel.run();
        } catch (Exception ex) {
            recordException(chgFsLabel, ex);
        }
        SanBootView.log.info(getClass().getName(), "chgFsLabel cmd retcode: " + chgFsLabel.getRetCode());
        boolean isOk = finished(chgFsLabel);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "chgFsLabel cmd errmsg: " + chgFsLabel.getErrMsg());
        }
        return isOk;
    }

    public boolean vgOnline(String ip, int port, String args) {
        vgOnline.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " vgcmd.sh vgonline " + args);
        SanBootView.log.info(getClass().getName(), "vgOnline cmd: " + vgOnline.getCmdLine());
        try {
            vgOnline.run();
        } catch (Exception ex) {
            recordException(vgOnline, ex);
        }
        SanBootView.log.info(getClass().getName(), "vgOnline cmd retcode: " + vgOnline.getRetCode());
        boolean isOk = finished(vgOnline);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vgOnline cmd errmsg: " + vgOnline.getErrMsg());
        }
        return isOk;
    }

    public boolean vgOffline(String ip, int port, String args) {
        vgOffline.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " vgcmd.sh vgoffline " + args);
        SanBootView.log.info(getClass().getName(), "vgOffline cmd: " + vgOffline.getCmdLine());
        try {
            vgOffline.run();
        } catch (Exception ex) {
            recordException(vgOffline, ex);
        }
        SanBootView.log.info(getClass().getName(), "vgOffline cmd retcode: " + vgOffline.getRetCode());
        boolean isOk = finished(vgOffline);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vgOffline cmd errmsg: " + vgOffline.getErrMsg());
        }
        return isOk;
    }

    public boolean modBootConf(String ip, int port, String args) {
        modBootConf.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + args);
        SanBootView.log.info(getClass().getName(), "modBootConf cmd: " + modBootConf.getCmdLine());
        try {
            modBootConf.run();
        } catch (Exception ex) {
            recordException(modBootConf, ex);
        }
        SanBootView.log.info(getClass().getName(), "modBootConf cmd retcode: " + modBootConf.getRetCode());
        boolean isOk = finished(modBootConf);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "modBootConf cmd errmsg: " + modBootConf.getErrMsg());
        }
        return isOk;
    }

    public boolean getUnixActiveFlag(String ip, int port, String args) {
        setUnixActive.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + args);
        SanBootView.log.info(getClass().getName(), "setUnixActive cmd: " + setUnixActive.getCmdLine());
        try {
            setUnixActive.run();
        } catch (Exception ex) {
            recordException(setUnixActive, ex);
        }
        SanBootView.log.info(getClass().getName(), "setUnixActive cmd retcode: " + setUnixActive.getRetCode());
        boolean isOk = finished(setUnixActive);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "setUnixActive cmd errmsg: " + setUnixActive.getErrMsg());
        }
        return isOk;
    }

    public boolean isActive() {
        return setUnixActive.isSetActive();
    }

    public boolean getOsLoaderType(String ip, int port, String args) {
        getOsLoader.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + args);
        boolean isOk = getOsLoader.getOsLoaderType();
        if (!isOk) {
            this.errorCode = getOsLoader.getRetCode();
            this.errorMsg = getOsLoader.getErrMsg();
        }
        return isOk;
    }

    public boolean isGrub() {
        return getOsLoader.isGrub();
    }

    public boolean isLilo() {
        return getOsLoader.isLilo();
    }

    public String getOsLoader() {
        return getOsLoader.getosLoaderType();
    }

    /////////////////////////////////////////////////
    //
    //           Iboot operation
    //
    /////////////////////////////////////////////////
    public boolean listIboot() {
        boolean ret = getIbootList.updateIbootlist();
        if (!ret) {
            errorMsg = getIbootList.getErrMsg();
            errorCode = getIbootList.getRetCode();
        }
        return ret;
    }

    public ArrayList getAllIboot() {
        return getIbootList.getAll();
    }

    public boolean isStartupIboot() {
        return getIbootList.isStartIboot();
    }

    public IBootObj getFromVectorOnMac(String mac) {
        return getIbootList.getFromVectorOnMac(mac);
    }

    public boolean delIboot(String mac) {
        try {
            delIboot.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_IBOOT) + mac);
            SanBootView.log.info(getClass().getName(), " del iboot cmd: " + delIboot.getCmdLine());
            delIboot.run();
        } catch (Exception ex) {
            recordException(delIboot, ex);
        }
        SanBootView.log.info(getClass().getName(), " del iboot cmd retcode: " + delIboot.getRetCode());
        boolean isOk = finished(delIboot);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " del iboot cmd errmsg: " + delIboot.getErrMsg());
        }
        return isOk;
    }

    public boolean addIboot(String mac, String pip, int tid) {
        try {
            addIboot.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_IBOOT) + mac + " " + pip + " " + tid);
            SanBootView.log.info(getClass().getName(), " add iboot cmd: " + addIboot.getCmdLine());
            addIboot.run();
        } catch (Exception ex) {
            recordException(addIboot, ex);
        }
        SanBootView.log.info(getClass().getName(), " add iboot cmd retcode: " + addIboot.getRetCode());
        boolean isOk = finished(addIboot);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " add iboot cmd errmsg: " + addIboot.getErrMsg());
        }
        return isOk;
    }

    public boolean modIboot(String mac, String name, String value) {
        try {
            addIboot.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_IBOOT) + mac + " " + name + " " + value);
            SanBootView.log.info(getClass().getName(), " mod iboot cmd: " + addIboot.getCmdLine());
            addIboot.run();
        } catch (Exception ex) {
            recordException(addIboot, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod iboot cmd retcode: " + addIboot.getRetCode());
        boolean isOk = finished(addIboot);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod iboot cmd errmsg: " + addIboot.getErrMsg());
        }
        return isOk;
    }

    /////////////////////////////////////////////////
    //
    //              Login and Logout
    //
    /////////////////////////////////////////////////
    public boolean logout() {
        SanBootView.log.info(getClass().getName(), "begin to logout server.");
        boolean ret = logout.logout();
        if (!ret) {
            errorMsg = logout.getErrMsg();
            SanBootView.log.error(getClass().getName(), "logout server error: " + errorMsg);
        } else {
            SanBootView.log.info(getClass().getName(), "logout server successfully.");
        }
        return ret;
    }

    public boolean login(String user, String pass) {
        SanBootView.log.info(getClass().getName(), "begin to login server.");
        boolean ret = login.login(user, pass);
        if (!ret) {
            errorMsg = login.getErrMsg();
            SanBootView.log.error(getClass().getName(), "login server error: " + errorMsg);
        } else {
            SanBootView.log.info(getClass().getName(), "login server successfully.");
        }
        return ret;
    }

    public boolean syncOPWithNoOutput(String cmd) {
        boolean ret = syncOPWithNoOutput.execCmd(cmd);
        if (!ret) {
            errorMsg = syncOPWithNoOutput.getErrMsg();
            errorCode = syncOPWithNoOutput.getRetCode();
        }
        return ret;
    }

//    public boolean syncOPWithoutput( String cmd ){
//        boolean ret = syncOPWithOutput.execCmd( cmd );
//        if( !ret ){
//            errorMsg = syncOPWithOutput.getErrMsg();
//            errorCode = syncOPWithOutput.getRetCode();
//        }
//        return ret;
//    }
//    
//    public int getSyncOPPID(){
//        return syncOPWithOutput.getPID();
//    }
    public boolean getCPProcess(int pid) {
        getCPProcess.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_PROCESS) + pid);
        SanBootView.log.info(getClass().getName(), "getCPProcess cmd: " + getCPProcess.getCmdLine());

        try {
            getCPProcess.clear();
            getCPProcess.run();
        } catch (Exception ex) {
            recordException(getCPProcess, ex);
        }
        SanBootView.log.info(getClass().getName(), "getCPProcess cmd retcode: " + getCPProcess.getRetCode());
        boolean isOk = finished(getCPProcess);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "getCPProcess cmd errmsg: " + getCPProcess.getErrMsg());
        }
        return isOk;
    }

    public boolean isCPFinished() {
        return getCPProcess.isFinished();
    }

    public boolean isCPOK() {
        return getCPProcess.isCopyOK();
    }

    public boolean parseProcess() {
        return getCPProcess.parseProcess();
    }

    public String getProcess() {
        return getCPProcess.getProcess();
    }

    public String getCPSrc() {
        return getCPProcess.getSrc();
    }

    public String getCPDest() {
        return getCPProcess.getDest();
    }

    public boolean getUnixCPProcess(String ip, int port, String args) {
        getUnixCPProcess.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_UNIX_CP_PROCESS)
                + ip + " " + port + " syscopy.sh -c " + args);
        SanBootView.log.info(getClass().getName(), "getUnixCPProcess cmd: " + getUnixCPProcess.getCmdLine());

        try {
            getUnixCPProcess.clear();
            getUnixCPProcess.run();
        } catch (Exception ex) {
            recordException(getUnixCPProcess, ex);
        }
        SanBootView.log.info(getClass().getName(), "getUnixCPProcess cmd retcode: " + getUnixCPProcess.getRetCode());
        boolean isOk = finished(getUnixCPProcess);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "getUnixCPProcess cmd errmsg: " + getUnixCPProcess.getErrMsg());
        }
        return isOk;
    }

    public boolean parseUnixCpTotal() {
        return getUnixCPProcess.parseTotal();
    }

    public long getUnixCpTotal() {
        return getUnixCPProcess.getTotal();
    }

    public boolean parseUnixCpProcess() {
        return getUnixCPProcess.parseProcess();
    }

    public long getUnixCpProcess() {
        return getUnixCPProcess.getProcess();
    }

    public String getUnixCpSrcDir() {
        return getUnixCPProcess.getSrcDir();
    }

    public String getUnixCpDestDir() {
        return getUnixCPProcess.getDestDir();
    }

    public String getUnixCpSnapDir() {
        return getUnixCPProcess.getSnapDir();
    }

    ///////////////////////////////////////////////
    //
    //              UWSrvNode
    //
    //////////////////////////////////////////////
    public boolean updateUWSrv() {
        boolean ret = getUWSrv.updateUWSrvNode();
        if (!ret) {
            errorMsg = getUWSrv.getErrMsg();
            errorCode = getUWSrv.getRetCode();
        }
        return ret;
    }

    public boolean queryUWSrvOnDestUWSrv(String dest_uws_ip, int dest_uws_port, String psn) {
        queryUWSrvOnDestUWSrv.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + dest_uws_ip + " " + dest_uws_port
                + " \"vdbmgr -uws -prt uws_sn=" + psn + "\"");
        boolean ret = queryUWSrvOnDestUWSrv.updateUWSrvNode();
        if (!ret) {
            errorMsg = queryUWSrvOnDestUWSrv.getErrMsg();
            errorCode = queryUWSrvOnDestUWSrv.getRetCode();
        }
        return ret;
    }

    public UWSrvNode isExistUWSrvOnDestUWSrv(String psn) {
        return queryUWSrvOnDestUWSrv.isExistThisUWSrv(psn);
    }

    public UWSrvNode getUWSrvFromVecOnIP(String uws_ip) {
        return queryUWSrvOnDestUWSrv.getUWSrvFromVecOnIP(uws_ip);
    }

    public UWSrvNode isExistUWSrvOnDestUWSrv(String psn, String uws_ip) {
        return queryUWSrvOnDestUWSrv.isExistThisUWSrv(psn, uws_ip);
    }

    public boolean modUWSrv(UWSrvNode node) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" uws_id=" + node.getUws_id());
            buf.append(" uws_ip=" + node.getUws_ip());
            buf.append(" uws_port=" + node.getUws_port());
            buf.append(" uws_sn=" + node.getUws_psn());

            modUWSrv.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_UWS_SRV)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), "mod SWU server: " + modUWSrv.getCmdLine());

            modUWSrv.run();
        } catch (Exception ex) {
            recordException(modUWSrv, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod SWU server retcode: " + modUWSrv.getRetCode());
        boolean isOk = finished(modUWSrv);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod SWU server errmsg: " + modUWSrv.getErrMsg());
        }
        return isOk;
    }

    public boolean addUWSrv(String dest_uws_ip, int dest_uws_port, int poolid, String passwd, UWSrvNode node) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" uws_ip=" + node.getUws_ip());
            buf.append(" uws_port=" + node.getUws_port());
            buf.append(" uws_sn=" + node.getUws_psn());

            if (dest_uws_ip.equals("")) {
                addUWSrv.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_ADD_UWS_SRV)
                        + buf.toString());
            } else {
                addUWSrv.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + "-p " + poolid + " -P " + passwd + " "
                        + dest_uws_ip + " " + dest_uws_port + " \"vdbmgr -uws -add" + buf.toString() + "\"");
            }

            SanBootView.log.info(getClass().getName(), "add SWU server: " + addUWSrv.getCmdLine());

            addUWSrv.run();
        } catch (Exception ex) {
            recordException(addUWSrv, ex);
        }

        recordNewId(addUWSrv);
        SanBootView.log.info(getClass().getName(), "add SWU server retcode: " + addUWSrv.getRetCode());

        boolean isOk = finished(addUWSrv);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add SWU server errmsg: " + addUWSrv.getErrMsg());
        }
        return isOk;
    }

    public boolean delUWSrv(int id) {
        try {
            delUWSrv.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_UWS_SRV)
                    + id);
            SanBootView.log.info(getClass().getName(), "del SWU server cmd: " + delUWSrv.getCmdLine());
            delUWSrv.run();
        } catch (Exception ex) {
            recordException(delUWSrv, ex);
        }
        SanBootView.log.info(getClass().getName(), "del SWU server cmd retcode: " + delUWSrv.getRetCode());
        boolean isOk = finished(delUWSrv);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del SWU server cmd errmsg: " + delUWSrv.getErrMsg());
        }
        return isOk;
    }

    public ArrayList getAllUWSrv() {
        return getUWSrv.getAllUWSrv();
    }

    public void removeUWSrvFromVector(UWSrvNode srv) {
        getUWSrv.removeUWSrvFromVector(srv);
    }

    public UWSrvNode getOneUWSrvFromVector(String uws_ip) {
        return getUWSrv.getUWSrvFromVecOnIP(uws_ip);
    }

    public void addUWSrvToVector(UWSrvNode srv) {
        getUWSrv.addUWSrvToVector(srv);
    }

    public UWSrvNode getUWSrvFromVector(int id) {
        return getUWSrv.getUWSrvFromVectorOnID(id);
    }

    public UWSrvNode getUWSrvFromVector(String psn) {
        return getUWSrv.isExistThisUWSrv(psn);
    }

    public ArrayList getSrcUWSrvNode() {
        int i, j, size, size1, uws_id;
        SourceAgent srcAgnt;
        UWSrvNode uws;
        boolean found;

        ArrayList srcAgntList = getAllSrcAgnt();

        ArrayList uwsList = getAllUWSrv();
        size = uwsList.size();
        System.out.println("uws server size: " + size);
        ArrayList<UWSrvNode> ret = new ArrayList<UWSrvNode>(size);
        for (i = 0; i < size; i++) {
            found = false;
            uws = (UWSrvNode) uwsList.get(i);
            System.out.println("uws id: " + uws.getUws_id());

            size1 = srcAgntList.size();
            for (j = 0; j < size1; j++) {
                srcAgnt = (SourceAgent) srcAgntList.get(j);
                uws_id = srcAgnt.getSrc_agnt_uws_id();
                System.out.println("src agent's uws id: " + uws_id);
                if (uws_id == uws.getUws_id()) {
                    found = true;
                    break;
                }
            }

            if (found) {
                ret.add(uws);
            }
        }

        return ret;
    }

    public ArrayList getDestUWSrvNode() {
        int i, size, uws_id;
        Integer key;
        SourceAgent srcAgnt;
        UWSrvNode uws;
        boolean found;

        ArrayList srcAgntList = getAllSrcAgnt();
        HashSet<Integer> set = new HashSet<Integer>();

        size = srcAgntList.size();
        for (i = 0; i < size; i++) {
            srcAgnt = (SourceAgent) srcAgntList.get(i);
            uws_id = srcAgnt.getSrc_agnt_uws_id();
            key = new Integer(uws_id);
            if (!set.contains(key)) {
                set.add(key);
            }
        }

        Iterator<Integer> iterator = set.iterator();
        ArrayList uwsList = getAllUWSrv();
        size = uwsList.size();
        ArrayList<UWSrvNode> ret = new ArrayList<UWSrvNode>(size);
        for (i = 0; i < size; i++) {
            found = false;
            uws = (UWSrvNode) uwsList.get(i);
            while (iterator.hasNext()) {
                uws_id = ((Integer) iterator.next()).intValue();
                if (uws_id == uws.getUws_id()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                ret.add(uws);
            }
        }
        return ret;
    }

    ///////////////////////////////////////////////
    //
    //    Mirror Group
    //
    //////////////////////////////////////////////
    public boolean updateMg() {
        boolean ret = getMgList.updateMg();
        if (!ret) {
            errorMsg = getMgList.getErrMsg();
            errorCode = getMgList.getRetCode();
        }
        return ret;
    }

    public boolean updateOneMg(int mg_id) {
        boolean ret = this.getOneMg.updateOneMg(mg_id);
        if (!ret) {
            errorMsg = getOneMg.getErrMsg();
            errorCode = getOneMg.getRetCode();
        }
        return ret;
    }

    public MirrorGrp getOneMgFromCache() {
        return getOneMg.getOneMgFromCache();
    }

    public boolean modMg(MirrorGrp mg) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" mg_id=" + mg.getMg_id());
            buf.append(" mg_name=" + mg.getMg_name());
            buf.append(" mg_type=" + mg.getMg_type());
            buf.append(" mg_src_ip=" + mg.getMg_src_ip());
            buf.append(" mg_src_port=" + mg.getMg_src_port());
            buf.append(" mg_src_disk_uuid=" + mg.getMg_src_disk_uuid());
            buf.append(" mg_src_root_id=" + mg.getMg_src_root_id());
            buf.append(" mg_interval_time=" + mg.getMg_interval_time());
            buf.append(" mg_min_snap_size=" + mg.getMg_min_snap_size());
            buf.append(" mg_max_snapshot=" + mg.getMg_max_snapshot());
            buf.append(" mg_pid=" + mg.getMg_pid());
            buf.append(" mg_before_cmd=" + mg.getMg_before_cmd());
            buf.append(" mg_after_cmd=" + mg.getMg_after_cmd());
            buf.append(" mg_desc=" + mg.getMg_desc());

            modMg.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_MG)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), "mod mg: " + modMg.getCmdLine());

            modMg.run();
        } catch (Exception ex) {
            recordException(modMg, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod mg retcode: " + modMg.getRetCode());
        boolean isOk = finished(modMg);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod mg errmsg: " + modMg.getErrMsg());
        }
        return isOk;
    }

    public boolean addMg(MirrorGrp mg) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" mg_name=" + mg.getMg_name());
            buf.append(" mg_type=" + mg.getMg_type());
            buf.append(" mg_src_ip=" + mg.getMg_src_ip());
            buf.append(" mg_src_port=" + mg.getMg_src_port());
            buf.append(" mg_src_disk_uuid=" + mg.getMg_src_disk_uuid());
            buf.append(" mg_src_root_id=" + mg.getMg_src_root_id());
            buf.append(" mg_interval_time=" + mg.getMg_interval_time());
            buf.append(" mg_min_snap_size=" + mg.getMg_min_snap_size());
            buf.append(" mg_max_snapshot=" + mg.getMg_max_snapshot());
            buf.append(" mg_before_cmd=" + mg.getMg_before_cmd());
            buf.append(" mg_after_cmd=" + mg.getMg_after_cmd());
            buf.append(" mg_desc=" + mg.getMg_desc());
            buf.append(" mg_log_max_size=" + mg.getMg_log_max_size());
            buf.append(" mg_log_max_time=" + mg.getMg_log_max_time());

            addMg.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_MG)
                    + buf.toString());

            SanBootView.log.info(getClass().getName(), "add mg: " + addMg.getCmdLine());

            addMg.run();
        } catch (Exception ex) {
            recordException(addMg, ex);
        }

        recordNewId(addMg);
        SanBootView.log.info(getClass().getName(), "add mg retcode: " + addMg.getRetCode());

        boolean isOk = finished(addMg);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add mg errmsg: " + addMg.getErrMsg());
        }
        return isOk;
    }

    public boolean delMg(int id) {
        try {
            delMg.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_MG)
                    + id);
            SanBootView.log.info(getClass().getName(), "del mg cmd: " + delMg.getCmdLine());
            delMg.run();
        } catch (Exception ex) {
            recordException(delMg, ex);
        }
        SanBootView.log.info(getClass().getName(), "del mg cmd retcode: " + delMg.getRetCode());
        boolean isOk = finished(delMg);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del mg cmd errmsg: " + delMg.getErrMsg());
        }
        return isOk;
    }

    public boolean checkMg(int mgid) {
        try {
            checkMg.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_CHECK_MG_STATUS) + mgid);
            SanBootView.log.info(getClass().getName(), "check mg status cmd: " + checkMg.getCmdLine());
            checkMg.run();
        } catch (Exception ex) {
            recordException(checkMg, ex);
        }

        int retcode = checkMg.getRetCode();
        SanBootView.log.info(getClass().getName(), "check mg status cmd retcode: " + retcode);

        boolean isOk = (retcode != AbstractNetworkRunning.OK);
        if (!isOk) {
            this.errorMsg = checkMg.getErrMsg();
            this.errorCode = retcode;
        }
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "check mg status cmd errmsg: " + checkMg.getErrMsg());
        }
        return isOk;
    }

    public boolean startMg(int mgid) {
        String cmd = ResourceCenter.getCmd(ResourceCenter.CMD_START_MG) + mgid;
        return syncOPWithNoOutput(cmd);
    }

    public boolean stopMg(int mgid) {
        String cmd = ResourceCenter.getCmd(ResourceCenter.CMD_STOP_MG) + mgid;
        return syncOPWithNoOutput(cmd);
    }

    public boolean sendSigToMgOnRootID(int rootid, int sig) {
        MirrorGrp mg = this.getMGFromVectorOnRootID(rootid);
        if (mg != null) {
            // 不管结果
            return this.sendSigToMg(mg.getMg_id(), sig);
        } else {
            return true;
        }
    }

    public boolean sendSigToMg(int mgid, int sig) {
        try {
            sendSigToMg.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_SEND_SIG_MG) + mgid + " " + sig);
            SanBootView.log.info(getClass().getName(), "send singal to mg cmd: " + sendSigToMg.getCmdLine());
            sendSigToMg.run();
        } catch (Exception ex) {
            recordException(sendSigToMg, ex);
        }

        SanBootView.log.info(getClass().getName(), "send signal to mg retcode: " + sendSigToMg.getRetCode());
        boolean isOk = finished(sendSigToMg);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "send signal to mg errmsg: " + sendSigToMg.getErrMsg());
        }
        return isOk;
    }

    public ArrayList getAllMg() {
        return getMgList.getAllMG();
    }

    public void removeMGFromVector(MirrorGrp mg) {
        getMgList.removeMGFromVector(mg);
    }

    public void addMGToVector(MirrorGrp mg) {
        getMgList.addMGToVector(mg);
    }

    public MirrorGrp getMGFromVectorOnID(int id) {
        return getMgList.getMGFromVectorOnID(id);
    }

    public MirrorGrp getMGFromVectorOnRootID(int rootid) {
        return getMgList.getMGFromVecOnRootid(rootid);
    }

    public MirrorGrp getMGFromVecOnManything(int rootid, int type, String mg_name) {
        return getMgList.getMGFromVecOnManything(rootid, type, mg_name);
    }

    ///////////////////////////////////////////////
    //
    //    Mirror Job Scheduler
    //
    //////////////////////////////////////////////
    public boolean updateMjSch() {
        boolean ret = this.getMjSch.realDo();
        if (!ret) {
            errorMsg = getMjSch.getErrMsg();
            errorCode = getMjSch.getRetCode();
        }
        return ret;
    }

    public ArrayList getAllMjSch() {
        return this.getMjSch.getMJSchList();
    }

    public void addMjSchIntoCache(MirrorJobSch mjSch) {
        this.getMjSch.addMjSch(mjSch);
    }

    public void removeMjSchFromCache(MirrorJobSch mjSch) {
        this.getMjSch.removeMjSch(mjSch);
    }

    public boolean addMjSch(MirrorJobSch mjSch) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" scheduler_type=" + mjSch.getScheduler_type());
            buf.append(" scheduler_mon=" + mjSch.getScheduler_mon());
            buf.append(" scheduler_tue=" + mjSch.getScheduler_tue());
            buf.append(" scheduler_wed=" + mjSch.getScheduler_wed());
            buf.append(" scheduler_thu=" + mjSch.getScheduler_thu());
            buf.append(" scheduler_fri=" + mjSch.getScheduler_fri());
            buf.append(" scheduler_sat=" + mjSch.getScheduler_sat());
            buf.append(" scheduler_sun=" + mjSch.getScheduler_sun());

            this.addMjSch.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_MJ_SCH)
                    + buf.toString());

            SanBootView.log.info(getClass().getName(), "add mj sch: " + addMjSch.getCmdLine());

            addMjSch.run();
        } catch (Exception ex) {
            recordException(addMjSch, ex);
        }

        recordNewId(addMjSch);
        SanBootView.log.info(getClass().getName(), "add mj sch retcode: " + addMjSch.getRetCode());

        boolean isOk = finished(addMjSch);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add mj sch errmsg: " + addMjSch.getErrMsg());
        }
        return isOk;
    }

    public boolean delMjSch(int id) {
        try {
            this.delMjSch.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_MJ_SCH)
                    + id);
            SanBootView.log.info(getClass().getName(), "del mj-sch cmd: " + delMjSch.getCmdLine());
            delMjSch.run();
        } catch (Exception ex) {
            recordException(delMjSch, ex);
        }
        SanBootView.log.info(getClass().getName(), "del mj-sch cmd retcode: " + delMjSch.getRetCode());
        boolean isOk = finished(delMjSch);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del mj-sch cmd errmsg: " + delMjSch.getErrMsg());
        }
        return isOk;
    }

    public boolean modMjSch(MirrorJobSch mjSch) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" scheduler_id=" + mjSch.getScheduler_id());
            buf.append(" scheduler_mon=" + mjSch.getScheduler_mon());
            buf.append(" scheduler_tue=" + mjSch.getScheduler_tue());
            buf.append(" scheduler_wed=" + mjSch.getScheduler_wed());
            buf.append(" scheduler_thu=" + mjSch.getScheduler_thu());
            buf.append(" scheduler_fri=" + mjSch.getScheduler_fri());
            buf.append(" scheduler_sat=" + mjSch.getScheduler_sat());
            buf.append(" scheduler_sun=" + mjSch.getScheduler_sun());

            modMjSch.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_MJ_SCH)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), "mod mj-sch: " + modMjSch.getCmdLine());

            modMjSch.run();
        } catch (Exception ex) {
            recordException(modMjSch, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod mj-sch retcode: " + modMjSch.getRetCode());
        boolean isOk = finished(modMjSch);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod mj-sch errmsg: " + modMjSch.getErrMsg());
        }
        return isOk;
    }

    ///////////////////////////////////////////////
    //
    //    Mirror Job
    //
    //////////////////////////////////////////////
    public boolean updateMj() {
        boolean ret = getMjList.updateMj();
        if (!ret) {
            errorMsg = getMjList.getErrMsg();
            errorCode = getMjList.getRetCode();
        }
        return ret;
    }

    public boolean monitorMJ(int mj_id) {
        boolean ret = monMj.updateMj(mj_id);
        if (!ret) {
            errorMsg = monMj.getErrMsg();
            errorCode = monMj.getRetCode();
        }
        return ret;
    }

    public boolean modMj1(MirrorJob mj) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" mj_id=" + mj.getMj_id());
            buf.append(" mj_job_name=" + mj.getMj_job_name());
            buf.append(" mj_dest_ip=" + mj.getMj_dest_ip());
            buf.append(" mj_dest_port=" + mj.getMj_dest_port());
            buf.append(" mj_transfer_option=" + mj.getMj_transfer_option());
            buf.append(" mj_dest_pool=" + mj.getMj_dest_pool());
            buf.append(" mj_dest_pool_passwd=" + mj.getMj_dest_pool_passwd());
            buf.append(" mj_desc=" + mj.getMj_desc());

            modMj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_MJ)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), "mod mj: " + modMj.getCmdLine());

            modMj.run();
        } catch (Exception ex) {
            recordException(modMj, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod mj retcode: " + modMj.getRetCode());
        boolean isOk = finished(modMj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod mj errmsg: " + modMj.getErrMsg());
        }
        return isOk;
    }

    public boolean modMj2(int mid, String dest_ip, int dest_port) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" mj_id=" + mid);
            buf.append(" mj_dest_ip=" + dest_ip);
            buf.append(" mj_dest_port=" + dest_port);

            modMj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_MJ)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), "mod mj: " + modMj.getCmdLine());

            modMj.run();
        } catch (Exception ex) {
            recordException(modMj, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod mj retcode: " + modMj.getRetCode());
        boolean isOk = finished(modMj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod mj errmsg: " + modMj.getErrMsg());
        }
        return isOk;
    }

    public boolean modMjStatus(int mid, int status) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append(" mj_id=" + mid);
            buf.append(" mj_job_status=" + status);

            modMj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_MJ)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), "mod mj status: " + modMj.getCmdLine());

            modMj.run();
        } catch (Exception ex) {
            recordException(modMj, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod mj status retcode: " + modMj.getRetCode());
        boolean isOk = finished(modMj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod mj status errmsg: " + modMj.getErrMsg());
        }
        return isOk;
    }

    public boolean modMjScheduler(int mid, int sch_id) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append(" mj_id=" + mid);
            buf.append(" mj_scheduler=" + sch_id);

            modMj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_MJ)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), "mod mj's scheduler: " + modMj.getCmdLine());

            modMj.run();
        } catch (Exception ex) {
            recordException(modMj, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod mj's scheduler retcode: " + modMj.getRetCode());
        boolean isOk = finished(modMj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod mj's scheduler errmsg: " + modMj.getErrMsg());
        }
        return isOk;
    }

    public boolean modMj(MirrorJob mj) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" mj_id=" + mj.getMj_id());
            buf.append(" mj_mg_id=" + mj.getMj_mg_id());
            buf.append(" mj_job_type=" + mj.getMj_job_type());
            buf.append(" mj_job_name=" + mj.getMj_job_name());
            buf.append(" mj_dest_ip=" + mj.getMj_dest_ip());
            buf.append(" mj_dest_port=" + mj.getMj_dest_port());
            buf.append(" mj_transfer_option=" + mj.getMj_transfer_option());
            buf.append(" mj_dest_pool=" + mj.getMj_dest_pool());
            buf.append(" mj_dest_pool_passwd=" + mj.getMj_dest_pool_passwd());
            buf.append(" mj_desc=" + mj.getMj_desc());

            modMj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_MJ)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), "mod mj: " + modMj.getCmdLine());

            modMj.run();
        } catch (Exception ex) {
            recordException(modMj, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod mj retcode: " + modMj.getRetCode());
        boolean isOk = finished(modMj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod mj errmsg: " + modMj.getErrMsg());
        }
        return isOk;
    }

    public boolean addMj(MirrorJob mj) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" mj_mg_id=" + mj.getMj_mg_id());
            if (mj.isIncMjType()) {
                buf.append(" mj_track_src_rootid=" + mj.getMj_track_src_rootid());
                buf.append(" mj_track_mode=" + mj.getMj_track_mode());
                buf.append(" mj_track_src_type=" + mj.getMj_track_src_type());
            } else if (mj.isCjType()) {
                buf.append(" mj_copy_src_rootid=" + mj.getMj_copy_src_rootid());
                buf.append(" mj_copy_src_snapid=" + mj.getMj_copy_src_snapid());
                buf.append(" mj_copy_src_type=" + mj.getMj_copy_src_type());
            }
            if (mj.isRemoteLjType()) {
                buf.append(" mj_track_src_rootid=" + mj.getMj_track_src_rootid());
                buf.append(" mj_track_mode=" + mj.getMj_track_mode());
                buf.append(" mj_track_src_type=" + mj.getMj_track_src_type());
                buf.append(" mg_name=" + mj.getMg_name());
                buf.append(" mg_interval_time=" + (mj.getMg_interval_time() != 0 ? mj.getMg_interval_time() : ""));
                buf.append(" mg_log_max_time=" + (mj.getMg_log_max_time() != 0 ? mj.getMg_log_max_time() : ""));
                buf.append(" mg_max_snapshot=" + (mj.getMg_max_snapshot() != 0 ? mj.getMg_max_snapshot() : ""));
            }
            buf.append(" mj_job_status=" + mj.getMj_job_status());
            buf.append(" mj_job_type=" + mj.getMj_job_type());
            buf.append(" mj_job_name=" + mj.getMj_job_name());
            buf.append(" mj_dest_ip=" + mj.getMj_dest_ip());
            buf.append(" mj_dest_port=" + mj.getMj_dest_port());
            buf.append(" mj_transfer_option=" + mj.getMj_transfer_option());
            buf.append(" mj_dest_pool=" + mj.getMj_dest_pool());
            buf.append(" mj_dest_pool_passwd=" + mj.getMj_dest_pool_passwd());
            buf.append(" \"mj_desc=" + mj.getMj_desc() + "\"");

            addMj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_MJ)
                    + buf.toString());

            SanBootView.log.info(getClass().getName(), "add mj: " + addMj.getCmdLine());

            addMj.run();
        } catch (Exception ex) {
            recordException(addMj, ex);
        }

        recordNewId(addMj);
        SanBootView.log.info(getClass().getName(), "add mj retcode: " + addMj.getRetCode());

        boolean isOk = finished(addMj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add mj errmsg: " + addMj.getErrMsg());
        }
        return isOk;
    }

    public boolean delMj(int id) {
        try {
            delMj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_MJ)
                    + id);
            SanBootView.log.info(getClass().getName(), "del mj cmd: " + delMj.getCmdLine());
            delMj.run();
        } catch (Exception ex) {
            recordException(delMj, ex);
        }
        SanBootView.log.info(getClass().getName(), "del mj cmd retcode: " + delMj.getRetCode());
        boolean isOk = finished(delMj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del mj cmd errmsg: " + delMj.getErrMsg());
        }
        return isOk;
    }

    public boolean startMj(int mjid) {
        try {
            switchMj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_START_MJ) + mjid);
            SanBootView.log.info(getClass().getName(), "start mj cmd: " + switchMj.getCmdLine());
            switchMj.run();
        } catch (Exception ex) {
            recordException(switchMj, ex);
        }
        SanBootView.log.info(getClass().getName(), "start mj cmd retcode: " + switchMj.getRetCode());
        boolean isOk = finished(switchMj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "start mj cmd errmsg: " + switchMj.getErrMsg());
        }
        return isOk;
    }

    public boolean stopMj(int mjid) {
        try {
            switchMj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_STOP_MJ) + mjid);
            SanBootView.log.info(getClass().getName(), "stop mj cmd: " + switchMj.getCmdLine());
            switchMj.run();
        } catch (Exception ex) {
            recordException(switchMj, ex);
        }
        SanBootView.log.info(getClass().getName(), "stop mj cmd retcode: " + switchMj.getRetCode());
        boolean isOk = finished(switchMj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "stop mj cmd errmsg: " + switchMj.getErrMsg());
        }
        return isOk;
    }

    public boolean msputfile(String localfile, String dest_uws_ip, int dest_uws_port, int poolid, String pool_pass, String destpath) {
        try {
            msputfile.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MS_PUTFILE) + localfile + " -p " + poolid + " -P " + pool_pass + " " + dest_uws_ip
                    + " " + dest_uws_port + " " + destpath);
            SanBootView.log.info(getClass().getName(), "putfile to dest server cmd: " + msputfile.getCmdLine());
            msputfile.run();
        } catch (Exception ex) {
            recordException(msputfile, ex);
        }
        SanBootView.log.info(getClass().getName(), "putfile to dest server retcode: " + msputfile.getRetCode());
        boolean isOk = finished(msputfile);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "putfile to dest server cmd errmsg: " + msputfile.getErrMsg());
        }
        return isOk;
    }

    public void sendWinNetBootInfoToDestUWS(int src_host_id, String dest_uws_ip, int dest_uws_port, int poolid, String pool_pass, int dest_host_id) {
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + src_host_id + ResourceCenter.CONF_IP,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_IP);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + src_host_id + ResourceCenter.CONF_OLDDISK,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_OLDDISK);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + src_host_id + ResourceCenter.CONF_SERVICE,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_SERVICE);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + src_host_id + ResourceCenter.CONF_NORMAL_DISK,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_NORMAL_DISK);

        this.sendServicesOnVolumeConf(src_host_id, dest_uws_ip, dest_uws_port, poolid, pool_pass, dest_host_id);
    }

    private void sendServicesOnVolumeConf(int src_host_id, String dest_uws_ip, int dest_uws_port, int poolid, String pool_pass, int dest_host_id) {
        ArrayList<PPProfile> ppp_list = this.getPPProfile(src_host_id);
        int size = ppp_list.size();
        StringBuffer buf = new StringBuffer();
        boolean isFirst = true;
        for (int i = 0; i < size; i++) {
            PPProfile ppp = ppp_list.get(i);
            ServicesOnVolume sv = ppp.convertToServicesOnVolume();
            if (isFirst) {
                buf.append(sv.prtMe());
                isFirst = false;
            } else {
                buf.append("\n");
                buf.append(sv.prtMe());
            }
        }

        File tmpFile = this.createTmpFile(ResourceCenter.PREFIX, ResourceCenter.SUFFIX_USWTMP);
        if (tmpFile == null) {
            SanBootView.log.error(getClass().getName(), "failed to send service_on_volume file to server. it's contents:  \n" + buf.toString());
            return;
        }

        // 发送buf中的内容到服务器的tmp目录下
        if (!view.initor.mdb.sendFileToServer(tmpFile.getName(), buf.toString())) {
            SanBootView.log.error(getClass().getName(), "failed to send service_on_volume file to server. it's contents:  \n" + buf.toString());
            tmpFile.delete();
            return;
        }
        tmpFile.delete();

        msputfile(ResourceCenter.TMP_DIR + tmpFile.getName(),
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_SERVICE_RELATED_WITH_VOLUME);
    }

    // for rollbacked sourceagent
    public void sendWinNetBootInfoToDestUWS1(int src_host_id, String dest_uws_ip, int dest_uws_port, int poolid, String pool_pass, int dest_host_id) {
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + src_host_id + ResourceCenter.CONF_IP,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_IP);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + src_host_id + ResourceCenter.CONF_OLDDISK,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_OLDDISK);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + src_host_id + ResourceCenter.CONF_SERVICE,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_SERVICE);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + src_host_id + ResourceCenter.CONF_NORMAL_DISK,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_NORMAL_DISK);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + src_host_id + ResourceCenter.CONF_SERVICE_RELATED_WITH_VOLUME,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_SERVICE_RELATED_WITH_VOLUME);
    }

    public void sendLinuxNetBootInfoToDestUWS(int src_host_id, String dest_uws_ip, int dest_uws_port, int poolid, String pool_pass, int dest_host_id) {
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + src_host_id + ResourceCenter.CONF_IP,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_IP);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + src_host_id + ResourceCenter.CONF_MP,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_MP);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + src_host_id + ResourceCenter.CONF_OS_LOADER,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_OS_LOADER);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + src_host_id + ResourceCenter.CONF_OLDDISK,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_OLDDISK);
    }

    // for rollbacked sourceagent
    public void sendLinuxNetBootInfoToDestUWS1(int src_host_id, String dest_uws_ip, int dest_uws_port, int poolid, String pool_pass, int dest_host_id) {
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + src_host_id + ResourceCenter.CONF_IP,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_IP);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + src_host_id + ResourceCenter.CONF_MP,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_MP);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + src_host_id + ResourceCenter.CONF_OS_LOADER,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_OS_LOADER);
        msputfile(ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + src_host_id + ResourceCenter.CONF_OLDDISK,
                dest_uws_ip, dest_uws_port, poolid, pool_pass,
                ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + dest_host_id + ResourceCenter.CONF_OLDDISK);
    }

    public ArrayList<MirrorJob> getAllMj() {
        return getMjList.getAllMJ();
    }

    public ArrayList<MirrorJob> getLogMjOnRootId(int root_id) {
        return getMjList.getLogMjOnRootId(root_id);
    }

    public ArrayList<MirrorJob> getAllLogMj() {
        return getMjList.getAllLogMj();
    }

    public MirrorJob getIncMjOnDestRootId(int dest_root_id) {
        return getMjList.getIncMjOnDestRootId(dest_root_id);
    }

    public boolean isRelatedWithMj(int mjSch_id) {
        return this.getMjList.isRelatedWithMj(mjSch_id);
    }

    public ArrayList<MirrorJob> getMjRelatedWithMjSch(int mjSch_id, boolean start) {
        return this.getMjList.getMjRelatedWithMjSch(mjSch_id, start);
    }

    public ArrayList<MirrorJob> getIncMjListFromVecOnSrcRootId(int rootid) {
        return getMjList.getIncMjListFromVecOnSrcRootId(rootid);
    }

    public ArrayList<MirrorJob> getCjListFromVecOnSrcRootId(int rootid) {
        return getMjList.getCjListFromVecOnSrcRootId(rootid);
    }

    public ArrayList<MirrorJob> getIncMjListFromVecOnSrcRootIdOrMgID(int rootid, int mgid) {
        return getMjList.getIncMjListFromVecOnSrcRootIdOrMgID(rootid, mgid);
    }

    public ArrayList<MirrorJob> getMjListFromVecOnMgID(int mgid) {
        return getMjList.getMJFromVecOnMgID(mgid);
    }

    public ArrayList<MirrorJob> getAllMJOnDestIP(String dest_ip) {
        return getMjList.getAllMJOnDestIP(dest_ip);
    }

    public void removeMJFromVector(MirrorJob mj) {
        getMjList.removeMJFromVector(mj);
    }

    public void addMJToVector(MirrorJob mj) {
        getMjList.addMJToVector(mj);
    }

    public MirrorJob getMJFromVectorOnID(int id) {
        return getMjList.getMJFromVectorOnID(id);
    }

    public MirrorJob getMonedMj(int id) {
        return monMj.getMJFromVectorOnID(id);
    }

    public Object[] getSomethingFromMjObj(MirrorJob mj) {
        int rootid, ptype = BootHost.PROTECT_TYPE_MTPP;
        String vol_name = "", vol_mp = "";
        Object hostObj = null;

        int mg_id = mj.getMj_mg_id();
        MirrorGrp mg = getMGFromVectorOnID(mg_id);
        if (mg == null) {
            if (mj.isIncMjType()) {
                rootid = mj.getMj_track_src_rootid();
            } else if (mj.isCjType()) {
                rootid = mj.getMj_copy_src_rootid();
            } else {
                SanBootView.log.error(getClass().getName(), "Cann't find mg or track_src_rootid from mj : " + mj.getMj_id());
                return null;
            }
        } else {
            rootid = mg.getMg_src_root_id();
        }

        // 寻找rootid对应的卷的情况
        VolumeMap volMap1 = getVolMapOnRootID(rootid);
        if (volMap1 != null) {
            vol_name = volMap1.getVolName();
            vol_mp = volMap1.getVolDiskLabel();
            hostObj = getBootHostFromVector((long) volMap1.getVolClntID());
            if (hostObj == null) {
                SanBootView.log.warning(getClass().getName(), "Cann't find clint(BootHost) on cltID: " + volMap1.getVolClntID());
                return null;
            }
            ptype = volMap1.getVol_protect_type();
        } else {
            MirrorDiskInfo mdi = getMDIFromCacheOnRootID(rootid);
            if (mdi != null) {
                vol_name = mdi.getSrc_snap_name();
                vol_mp = mdi.getSrc_agent_mp();
                hostObj = getSrcAgntFromVectorOnID(mdi.getSrc_agnt_id());
                if (hostObj == null) {
                    SanBootView.log.warning(getClass().getName(), "Cann't find client(SourceAgent) on cltID: " + mdi.getSrc_agnt_id());
                }
                ptype = mdi.getSrc_vol_protect_type();
            } else {
                SanBootView.log.warning(getClass().getName(), "Cann't find volmap on ROOTID: " + rootid + ". Maybe it's a free vol.");
                // 在StartorStopMjThread中找出这个freevol的名字
            }
        }

        Object[] ret = new Object[5];
        ret[0] = hostObj;
        ret[1] = new Integer(rootid);
        ret[2] = vol_name;
        ret[3] = vol_mp;
        ret[4] = new Integer(ptype);
        return ret;
    }

    ///////////////////////////////////////////////
    //
    //               SourceAgent
    //
    //////////////////////////////////////////////
    public boolean updateSrcAgnt() {
        boolean ret = getSrcAgnt.updateSrcAgent();
        if (!ret) {
            errorMsg = getSrcAgnt.getErrMsg();
            errorCode = getSrcAgnt.getRetCode();
        }
        return ret;
    }

    public boolean querySrcAgntOnDestUWSrv(String dest_uws_ip, int dest_uws_port, String uuid) {
        querySrcAgntOnDestUWSrv.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + dest_uws_ip + " " + dest_uws_port
                + " \"idbmgr -srcagnt -prt src_agnt_desc=" + uuid + "\"");
        boolean ret = querySrcAgntOnDestUWSrv.updateSrcAgent();
        if (!ret) {
            errorMsg = querySrcAgntOnDestUWSrv.getErrMsg();
            errorCode = querySrcAgntOnDestUWSrv.getRetCode();
        }
        return ret;
    }

    public SourceAgent isThisSrcAgntOnDestUWSrv(String uuid) {
        return querySrcAgntOnDestUWSrv.isExistSrcAgnt(uuid);
    }

    public SourceAgent isThisSrcAgntOnDestUWSrv(String uuid, int src_uws_id) {
        return querySrcAgntOnDestUWSrv.isExistSrcAgnt(uuid, src_uws_id);
    }

    public boolean queryUWSSrcAgntOnDestUWSrv(String dest_uws_ip, int dest_uws_port, String psn) {
        querySrcAgntOnDestUWSrv.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + dest_uws_ip + " " + dest_uws_port
                + " \"idbmgr -srcagnt -prt src_agnt_os=" + psn + "\"");
        boolean ret = querySrcAgntOnDestUWSrv.updateSrcAgent();
        if (!ret) {
            errorMsg = querySrcAgntOnDestUWSrv.getErrMsg();
            errorCode = querySrcAgntOnDestUWSrv.getRetCode();
        }
        return ret;
    }

    public SourceAgent isThisUWSSrcAgntOnDestUWSrv(String psn) {
        return querySrcAgntOnDestUWSrv.isExistUWSSrcAgnt(psn);
    }

    public SourceAgent isThisUWSSrcAgntOnDestUWSrv1(String ip) {
        return querySrcAgntOnDestUWSrv.isExistUWSSrcAgnt1(ip);
    }

    public boolean addSrcAgnt(String dest_uws_ip, int dest_uws_port, int poolid, String passwd, SourceAgent srcAgnt) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" src_agnt_ip=" + srcAgnt.getSrc_agnt_ip());
            buf.append(" src_agnt_os=" + srcAgnt.getSrc_agnt_os());
            buf.append(" src_agnt_machine=" + srcAgnt.getMachine());
            buf.append(" src_agnt_desc=" + srcAgnt.getSrc_agnt_desc());
            buf.append(" src_agnt_uws_id=" + srcAgnt.getSrc_agnt_uws_id());
            buf.append(" src_agnt_psn=" + srcAgnt.getSrc_agnt_psn());
            buf.append(" src_agnt_port=" + srcAgnt.getSrc_agnt_port());
            buf.append(" src_agnt_port1=" + srcAgnt.getSrc_agnt_port1());
            buf.append(" src_agnt_boot_mode=" + srcAgnt.getSrc_agnt_boot_mode());
            buf.append(" src_agnt_protect_type=" + srcAgnt.getProtectType());

            if (dest_uws_ip.equals("")) {
                addSrcAgnt.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_ADD_SRCAGNT) + buf.toString());
            } else {
                addSrcAgnt.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + "-p " + poolid + " -P " + passwd + " " + dest_uws_ip + " " + dest_uws_port
                        + " \"idbmgr -srcagnt -add " + buf.toString() + "\"");
            }

            SanBootView.log.info(getClass().getName(), "add src_agent: " + addSrcAgnt.getCmdLine());

            addSrcAgnt.run();
        } catch (Exception ex) {
            recordException(addSrcAgnt, ex);
        }

        recordNewId(addSrcAgnt);
        SanBootView.log.info(getClass().getName(), "add src_agent retcode: " + addSrcAgnt.getRetCode());

        boolean isOk = finished(addSrcAgnt);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add src_agent errmsg: " + addSrcAgnt.getErrMsg());
        }
        return isOk;
    }

    public boolean delSrcAgnt(String dest_uws_ip, int dest_uws_port, int poolid, String passwd, int id) {
        try {
            if (!dest_uws_ip.equals("")) {
                delSrcAgnt.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + "-p " + poolid + " -P " + passwd + " "
                        + dest_uws_ip + " " + dest_uws_port + " \"" + "idbmgr -srcagnt -del src_agnt_id=" + id + "\"");
            } else {
                delSrcAgnt.setCmdLine(ResourceCenter.SNAP_DIR + "idbmgr -srcagnt -del src_agnt_id=" + id);
            }
            SanBootView.log.info(getClass().getName(), "del src_agent cmd: " + delSrcAgnt.getCmdLine());
            delSrcAgnt.run();
        } catch (Exception ex) {
            recordException(delSrcAgnt, ex);
        }
        SanBootView.log.info(getClass().getName(), "del src_agent cmd retcode: " + delSrcAgnt.getRetCode());
        boolean isOk = finished(delSrcAgnt);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del src_agent cmd errmsg: " + delSrcAgnt.getErrMsg());
        }
        return isOk;
    }

    public boolean modSrcAgnt(int id, String uuid, int uws_id) {
        try {
            modSrcAgnt.setCmdLine(ResourceCenter.SNAP_DIR + "idbmgr -srcagnt -mod src_agnt_id=" + id + " src_agnt_uws_id=" + uws_id + "  src_agnt_desc=" + uuid);
            SanBootView.log.info(getClass().getName(), "mod src_agent cmd: " + modSrcAgnt.getCmdLine());
            modSrcAgnt.run();
        } catch (Exception ex) {
            recordException(modSrcAgnt, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod src_agent cmd retcode: " + modSrcAgnt.getRetCode());
        boolean isOk = finished(modSrcAgnt);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod src_agent cmd errmsg: " + modSrcAgnt.getErrMsg());
        }
        return isOk;
    }

    public void addSrcAgntIntoCache(SourceAgent one) {
        getSrcAgnt.addSrcAgentToVector(one);
    }

    public void removeSrcAgntFromCache(SourceAgent one) {
        getSrcAgnt.removeSrcAgentFromVector(one);
    }

    public SourceAgent getSrcAgntFromVectorOnID(int id) {
        return getSrcAgnt.getSrcAgntFromVectorOnID(id);
    }

    public ArrayList getSrcAgntFromVecOnUWSID(int uws_id) {
        return getSrcAgnt.getSrcAgntFromVecOnUWSID(uws_id);
    }

    public ArrayList getAllSrcAgnt() {
        return getSrcAgnt.getAllSrcAgnt();
    }

    public ArrayList getAllRollbackSrcAgnt() {
        return getSrcAgnt.getAllRollbackSrcAgnt();
    }

    public ArrayList getSrcAgntForRealAgent() {
        return getSrcAgnt.getSrcAgntForRealAgent();
    }

    public ArrayList getSrcAgntForRealAgtOnUWSrvID(int src_uws_id) {
        return getSrcAgnt.getSrcAgntForRealAgtOnUWSrvID(src_uws_id);
    }

    public SourceAgent getSrcAgntForSrcUWSOnUWSrvID(int src_uws_id) {
        return getSrcAgnt.getSrcAgntForSrcUWSOnUWSrvID(src_uws_id);
    }

    public SourceAgent getSrcAgntForSrcUWSOnPSN(String psn) {
        return getSrcAgnt.getSrcAgntForSrcUWSOnPSN(psn);
    }

    public ArrayList getSrcAgntForSrcUWS() {
        return getSrcAgnt.getSrcAgntForSrcUWS();
    }

    ///////////////////////////////////////////////
    //
    //    MirrorDiskInfo
    //
    ///////////////////////////////////////////////
    public boolean updateMDI() {
        boolean ret = getMdi.updateMirrorDiskInfo();
        if (!ret) {
            errorMsg = getMdi.getErrMsg();
            errorCode = getMdi.getRetCode();
        }
        return ret;
    }

    // 为了兼容“真正融合版本”之前的版本，之前的版本mirrorDiskInfo中没有protect type字段;
    public void updatePTypeForMirrorDiskInfo() {
        int i, size, clntID;

        ArrayList<MirrorDiskInfo> list = this.getAllMDI();
        size = list.size();
        for (i = 0; i < size; i++) {
            MirrorDiskInfo one = list.get(i);
            if (one.isUnknownProtectType()) {
                clntID = one.getSrc_agnt_id();
                SourceAgent host = this.getSrcAgntFromVectorOnID(clntID);
                if (host != null) {
                    one.setSrc_vol_protect_type(host.getProtectType());
                }
            }
        }
    }

    public boolean queryMdiOnDestUWSrv(String dest_uws_ip, int dest_uws_port, int rootid) {
        if (dest_uws_ip.equals("")) {
            queryMdiOnDestUWSrv.setCmdLine(
                    ResourceCenter.BIN_DIR + "idbmgr -diskinfo -prt snap_rootid=" + rootid);
        } else {
            queryMdiOnDestUWSrv.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + dest_uws_ip + " " + dest_uws_port
                    + " \"idbmgr -diskinfo -prt snap_rootid=" + rootid + "\"");
        }
        boolean ret = queryMdiOnDestUWSrv.updateMirrorDiskInfo();
        if (!ret) {
            errorMsg = queryMdiOnDestUWSrv.getErrMsg();
            errorCode = queryMdiOnDestUWSrv.getRetCode();
        }
        return ret;
    }

    public MirrorDiskInfo isExistMdiOnDestUWSrv(int rootid) {
        return queryMdiOnDestUWSrv.isExistMDI(rootid);
    }

    public ArrayList isExistMDIListOnDestUWSrv(int rootid) {
        return queryMdiOnDestUWSrv.isExistMDIList(rootid);
    }

    public boolean modMDI(int snap_rootid, int targetID) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append(" snap_rootid=" + snap_rootid);
            buf.append(" src_groupinfo=" + targetID);
            modMdi.setCmdLine(
                    ResourceCenter.SNAP_DIR + "idbmgr -diskinfo -mod " + buf.toString());

            SanBootView.log.info(getClass().getName(), "modify mirror_disk_info: " + modMdi.getCmdLine());

            modMdi.run();
        } catch (Exception ex) {
            recordException(modMdi, ex);
        }

        SanBootView.log.info(getClass().getName(), "modify mirror_disk_info retcode: " + modMdi.getRetCode());
        boolean isOk = finished(modMdi);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "modify mirror_disk_info errmsg: " + modMdi.getErrMsg());
        }
        return isOk;
    }

    public boolean modMDI(String dest_uws_ip, int dest_uws_port, int poolid, String passwd, int snap_rootid, int srcAgntID, String vol_mp) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" snap_rootid=" + snap_rootid);
            buf.append(" src_agnt_id=" + srcAgntID);
            buf.append(" src_agent_mp=" + vol_mp);

            modMdi.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + "-p " + poolid + " -P " + passwd + " " + dest_uws_ip + " " + dest_uws_port
                    + " \"idbmgr -diskinfo -mod " + buf.toString() + "\"");

            SanBootView.log.info(getClass().getName(), "modify mirror_disk_info: " + modMdi.getCmdLine());

            modMdi.run();
        } catch (Exception ex) {
            recordException(modMdi, ex);
        }

        SanBootView.log.info(getClass().getName(), "modify mirror_disk_info retcode: " + modMdi.getRetCode());
        boolean isOk = finished(modMdi);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "modify mirror_disk_info errmsg: " + modMdi.getErrMsg());
        }
        return isOk;
    }

    public boolean modMDI(String dest_uws_ip, int dest_uws_port, int poolid, String passwd, int snap_rootid, int srcAgntID) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" snap_rootid=" + snap_rootid);
            buf.append(" src_agnt_id=" + srcAgntID);

            if (dest_uws_ip.equals("")) {
                modMdi.setCmdLine(
                        ResourceCenter.BIN_DIR + "idbmgr -diskinfo -mod " + buf.toString());
            } else {
                modMdi.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + "-p " + poolid + " -P " + passwd + " " + dest_uws_ip + " " + dest_uws_port
                        + " \"idbmgr -diskinfo -mod " + buf.toString() + "\"");
            }
            SanBootView.log.info(getClass().getName(), "modify mirror_disk_info: " + modMdi.getCmdLine());

            modMdi.run();
        } catch (Exception ex) {
            recordException(modMdi, ex);
        }

        SanBootView.log.info(getClass().getName(), "modify mirror_disk_info retcode: " + modMdi.getRetCode());
        boolean isOk = finished(modMdi);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "modify mirror_disk_info errmsg: " + modMdi.getErrMsg());
        }
        return isOk;
    }

    public boolean addMDI(String dest_uws_ip, int dest_uws_port, int poolid, String passwd, MirrorDiskInfo mdi) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" snap_rootid=" + mdi.getSnap_rootid());
            buf.append(" src_agnt_id=" + mdi.getSrc_agnt_id());
            buf.append(" src_snap_rootid=" + mdi.getSrc_snap_rootid());
            buf.append(" src_snap_name=" + mdi.getSrc_snap_name());
            buf.append(" src_agent_mp=" + mdi.getSrc_agent_mp());
            buf.append(" src_agent_info=" + mdi.getSrc_agent_info());
            buf.append(" src_vol_info=" + mdi.getVolumeType());
            buf.append(" src_vol_protect_type=" + mdi.getSrc_vol_protect_type());
            /*
             * buf.append(" crsnap_services=" ); buf.append("
             * changever_services=" ); buf.append(" database_instances=" );
             * buf.append(" others=" ); buf.append(" src_vol_capacity=0" );
             * buf.append(" src_groupinfo=-1" ); buf.append("
             * src_groupinfodetail=" ); buf.append(" src_vol_info=-1" );
             * buf.append(" src_groupname=");
             */
            if (dest_uws_ip.equals("")) {
                addMdi.setCmdLine(
                        ResourceCenter.BIN_DIR + "idbmgr -diskinfo -add " + buf.toString());
            } else {
                addMdi.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + "-p " + poolid + " -P " + passwd + " " + dest_uws_ip + " " + dest_uws_port
                        + " \"idbmgr -diskinfo -add " + buf.toString() + "\"");
            }

            SanBootView.log.info(getClass().getName(), "add mirror_disk_info: " + addMdi.getCmdLine());

            addMdi.run();
        } catch (Exception ex) {
            recordException(addMdi, ex);
        }

        recordNewId(addMdi);
        SanBootView.log.info(getClass().getName(), "add mirror_disk_info retcode: " + addMdi.getRetCode());

        boolean isOk = finished(addMdi);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add mirror_disk_info errmsg: " + addMdi.getErrMsg());
        }
        return isOk;
    }

    public boolean delMDI(String uws_ip, int uws_port, int poolid, String passwd, int rootid) {
        try {
            delMdi.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + "-p " + poolid + " -P " + passwd + " " + uws_ip + " " + uws_port
                    + " \"idbmgr -diskinfo -del snap_rootid=" + rootid + "\"");
            SanBootView.log.info(getClass().getName(), "del mirror_disk_info cmd: " + delMdi.getCmdLine());
            delMdi.run();
        } catch (Exception ex) {
            recordException(delMdi, ex);
        }
        SanBootView.log.info(getClass().getName(), "del mirror_disk_info cmd retcode: " + delMdi.getRetCode());
        boolean isOk = finished(delMdi);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del mirror_disk_info cmd errmsg: " + delMdi.getErrMsg());
        }
        return isOk;
    }

    public boolean destroyDisk(int rootid) {
        try {
            destroyDisk.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DESTORY_DISK) + rootid);
            SanBootView.log.info(getClass().getName(), "destroy disk cmd: " + destroyDisk.getCmdLine());
            destroyDisk.run();
        } catch (Exception ex) {
            recordException(destroyDisk, ex);
        }
        SanBootView.log.info(getClass().getName(), "destroy disk cmd retcode: " + destroyDisk.getRetCode());
        boolean isOk = finished(destroyDisk);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "destroy disk cmd errmsg: " + destroyDisk.getErrMsg());
        }
        return isOk;
    }

    public boolean updateCloneDiskList() {
        String cmdLine = ResourceCenter.getCmd(ResourceCenter.CMD_GET_CLONE_DISK) + " all ";
        boolean isOk = updateCloneDisk.realDo(cmdLine);
        if (!isOk) {
            errorCode = updateCloneDisk.getRetCode();
            errorMsg = updateCloneDisk.getErrMsg();
        }
        return isOk;
    }

    public ArrayList getAllCloneDiskList() {
        return updateCloneDisk.getCloneDiskList();
    }

    public CloneDisk getCloneDiskOnTid(int tid) {
        return updateCloneDisk.getCloneDiskOnTid(tid);
    }

    public boolean getCloneDiskList(int src_host_id, int src_host_type, int src_disk_root_id) {
        String cmdLine = ResourceCenter.getCmd(ResourceCenter.CMD_GET_CLONE_DISK)
                + " src_host_id=" + src_host_id
                + " src_host_type=" + src_host_type
                + " src_disk_root_id=" + src_disk_root_id;
        boolean isOk = getCloneDiskListCmd.realDo(cmdLine);
        if (!isOk) {
            errorCode = getCloneDiskListCmd.getRetCode();
            errorMsg = getCloneDiskListCmd.getErrMsg();
        }
        return isOk;
    }

    public ArrayList getCloneDiskList() {
        return this.getCloneDiskListCmd.getCloneDiskList();
    }

    public boolean addCloneInfo(String dest_uws_ip, int dest_uws_port, int poolid, String passwd, CloneDisk cloneDisk) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" src_host_type=" + cloneDisk.getSrc_host_type());
            buf.append(" src_host_id=" + cloneDisk.getSrc_host_id());
            buf.append(" src_disk_root_id=" + cloneDisk.getSrc_disk_root_id());
            buf.append(" src_inc_mirvol_root_id=" + cloneDisk.getSrc_inc_mirvol_root_id());
            buf.append(" src_inc_mirvol_snap_local_id=" + cloneDisk.getSrc_inc_mirvol_snap_local_id());
            buf.append(" clone_disk_crt_time=" + cloneDisk.getCrt_time());
            buf.append(" clone_disk_root_id=" + cloneDisk.getRoot_id());
            buf.append(" \"clone_disk_label=" + cloneDisk.getLabel() + "\"");
            buf.append(" \"clone_disk_desc=" + cloneDisk.getDesc() + "\"");
            buf.append(" clone_disk_target_id=" + cloneDisk.getTarget_id());

            if (dest_uws_ip.equals("")) {
                this.addCloneDisk.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_ADD_CLONE_DISK) + buf.toString());
            } else {
                this.addCloneDisk.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + "-p " + poolid + " -P " + passwd + " " + dest_uws_ip + " " + dest_uws_port
                        + " \"idbmgr -clonedisk -add " + buf.toString() + "\"");
            }

            SanBootView.log.info(getClass().getName(), "add clone info cmd : " + this.addCloneDisk.getCmdLine());

            this.addCloneDisk.run();
        } catch (Exception ex) {
            recordException(this.addCloneDisk, ex);
        }

        recordNewId(addCloneDisk);
        SanBootView.log.info(getClass().getName(), "add clone info retcode: " + addCloneDisk.getRetCode());

        boolean isOk = finished(addCloneDisk);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add clone info errmsg: " + addCloneDisk.getErrMsg());
        }
        return isOk;
    }

    public boolean getUISnapSum(String dest_uws_ip, int dest_uws_port, int poolid, String passwd, int rootid) {
        try {
            StringBuffer buf = new StringBuffer();

            if (dest_uws_ip.equals("")) {
                this.getUISnapSum.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_UI_SNAP_SUM) + rootid + " -s ");
            } else {
                this.getUISnapSum.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + "-p " + poolid + " -P " + passwd + " " + dest_uws_ip + " " + dest_uws_port
                        + " listsnap -m " + rootid + " -s ");
            }

            SanBootView.log.info(getClass().getName(), "get UI-Snap sum cmd : " + this.getUISnapSum.getCmdLine());

            this.getUISnapSum.run();
        } catch (Exception ex) {
            recordException(this.getUISnapSum, ex);
        }

        recordNewId(getUISnapSum);
        SanBootView.log.info(getClass().getName(), "get UI-snap retcode: " + getUISnapSum.getRetCode());

        boolean isOk = finished(getUISnapSum);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "get UI-snap errmsg: " + getUISnapSum.getErrMsg());
        }
        return isOk;
    }

    public boolean delCloneDisk(String uws_ip, int uws_port, int poolid, String passwd, int cloneDiskId) {
        try {
            if (uws_ip.equals("")) {
                this.delCloneDisk.setCmdLine(
                        ResourceCenter.SNAP_DIR + "idbmgr -cloneinfo -del clone_disk_id=" + cloneDiskId);
            } else {
                this.delCloneDisk.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + "-p " + poolid + " -P " + passwd + " " + uws_ip + " " + uws_port
                        + " \"idbmgr -cloneinfo -del clone_disk_id=" + cloneDiskId + "\"");
            }
            SanBootView.log.info(getClass().getName(), "del cloneinfo cmd: " + delCloneDisk.getCmdLine());
            delCloneDisk.run();
        } catch (Exception ex) {
            recordException(delCloneDisk, ex);
        }
        SanBootView.log.info(getClass().getName(), "del cloneinfo cmd retcode: " + delCloneDisk.getRetCode());
        boolean isOk = finished(delCloneDisk);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del cloneinfo cmd errmsg: " + delCloneDisk.getErrMsg());
        }
        return isOk;
    }

    public boolean modCloneDisk(String uws_ip, int uws_port, int poolid, String passwd, int cloneDiskId, int hostid, int hosttype) {
        try {
            if (uws_ip.equals("")) {
                this.modCloneDisk.setCmdLine(
                        ResourceCenter.SNAP_DIR + "idbmgr -cloneinfo -mod clone_disk_id=" + cloneDiskId + " src_host_type=" + hosttype + " src_host_id=" + hostid);
            } else {
                this.modCloneDisk.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + "-p " + poolid + " -P " + passwd + " " + uws_ip + " " + uws_port
                        + " \"idbmgr -cloneinfo -mod clone_disk_id=" + cloneDiskId + " src_host_type=" + hosttype + " src_host_id=" + hostid + "\"");
            }
            SanBootView.log.info(getClass().getName(), "mod cloneinfo cmd: " + modCloneDisk.getCmdLine());
            modCloneDisk.run();
        } catch (Exception ex) {
            recordException(modCloneDisk, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod cloneinfo cmd retcode: " + modCloneDisk.getRetCode());
        boolean isOk = finished(modCloneDisk);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod cloneinfo cmd errmsg: " + modCloneDisk.getErrMsg());
        }
        return isOk;
    }

    public boolean delMDI(int snap_rootid) {
        try {
            delMdi.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_MIRROR_DISK) + snap_rootid);
            SanBootView.log.info(getClass().getName(), "del mirror_disk_info cmd: " + delMdi.getCmdLine());
            delMdi.run();
        } catch (Exception ex) {
            recordException(delMdi, ex);
        }
        SanBootView.log.info(getClass().getName(), "del mirror_disk_info cmd retcode: " + delMdi.getRetCode());
        boolean isOk = finished(delMdi);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del mirror_disk_info cmd errmsg: " + delMdi.getErrMsg());
        }
        return isOk;
    }

    public void addMDIIntoCache(MirrorDiskInfo one) {
        getMdi.addMDIToVector(one);
    }

    public void removeMDIFromCache(MirrorDiskInfo one) {
        getMdi.removeMDIFromVector(one);
    }

    public ArrayList getMDIFromCacheOnSrcAgntID(int srcAgntID) {
        return getMdi.getMDIFromCacheOnSrcAgntID(srcAgntID);
    }

    public ArrayList getMDIFromCacheOnHostIDandRootID(int hostId, int rootId) {
        return getMdi.getMDIFromCacheOnHostIDandRootID(hostId, rootId);
    }

    public MirrorDiskInfo getMDIFromCacheOnRootID(int rootid) {
        return getMdi.getMDIFromCacheOnRootID(rootid);
    }

    public MirrorDiskInfo getMDIFromCacheOnTID(int tid) {
        return getMdi.getMDIFromCacheOnTID(tid);
    }

    public ArrayList<MirrorDiskInfo> getAllMDI() {
        return getMdi.getAllMDIs();
    }

    ///////////////////////////////////////////////
    //
    //    MirrorSnapUsage
    //
    //////////////////////////////////////////////
    public boolean updateMSU() {
        boolean ret = getMSU.updateMirrorSnapusage();
        if (!ret) {
            errorMsg = getMSU.getErrMsg();
            errorCode = getMSU.getRetCode();
        }
        return ret;
    }

    public boolean addMSU(SnapUsage msu) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" dst_agent_id=" + msu.getDst_agent_id());
            buf.append(" snap_rootid=" + msu.getSnap_rootid());
            buf.append(" snap_local_id=" + msu.getSnap_local_id());
            buf.append(" snap_view_local_id=" + msu.getSnap_view_local_id());
            buf.append(" export_mp=" + msu.getExport_mp());
            buf.append(" snap_target_id=" + msu.getSnapTid());
            buf.append(" snap_crt_time=" + msu.getCrtTime());

            addMSU.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_SNAPUSAGE) + buf.toString());

            SanBootView.log.info(getClass().getName(), "add mirror_snap_usage: " + addMSU.getCmdLine());

            addMSU.run();
        } catch (Exception ex) {
            recordException(addMSU, ex);
        }

        recordNewId(addMSU);
        SanBootView.log.info(getClass().getName(), "add mirror_snap_usage retcode: " + addMSU.getRetCode());

        boolean isOk = finished(addMSU);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add mirror_snap_usage errmsg: " + addMSU.getErrMsg());
        }
        return isOk;
    }

    public boolean modMSU(int us_id, int snapid, int viewid, int tid, String crtime) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append(" usage_id=" + us_id);
            buf.append(" snap_local_id=" + snapid);
            buf.append(" snap_view_local_id=" + viewid);
            buf.append(" snap_target_id=" + tid);
            buf.append(" snap_crt_time=" + crtime);

            modMSU.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_SNAPUSAGE) + buf.toString());

            SanBootView.log.info(getClass().getName(), "modify mirror_snap_usage: " + modMSU.getCmdLine());

            modMSU.run();
        } catch (Exception ex) {
            recordException(modMSU, ex);
        }

        SanBootView.log.info(getClass().getName(), "modify mirror_snap_usage retcode: " + modMSU.getRetCode());

        boolean isOk = finished(modMSU);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "modify mirror_snap_usage errmsg: " + modMSU.getErrMsg());
        }
        return isOk;
    }

    public boolean delMSU(int id) {
        try {
            delMSU.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_SNAPUSAGE) + id);
            SanBootView.log.info(getClass().getName(), "del mirror_snap_usage cmd: " + delMSU.getCmdLine());
            delMSU.run();
        } catch (Exception ex) {
            recordException(delMSU, ex);
        }
        SanBootView.log.info(getClass().getName(), "del mirror_snap_usage cmd retcode: " + delMSU.getRetCode());
        boolean isOk = finished(delMSU);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del mirror_snap_usage cmd errmsg: " + delMSU.getErrMsg());
        }
        return isOk;
    }

    public void addMSUIntoCache(SnapUsage one) {
        getMSU.addMSUToVector(one);
    }

    public void removeMSUFromCache(SnapUsage one) {
        getMSU.removeMSUFromVector(one);
    }

    public ArrayList getMSUFromCacheOnDstAgntID(int dstAgntID) {
        return getMSU.getMSUFromCacheOnDstAgntID(dstAgntID);
    }

    public Vector getMSUVolMapFromCacheOnDstAgntID(int dstAgntID) {
        return getMSU.getMSUVolMapFromCacheOnDstAgntID(dstAgntID);
    }

    public ArrayList getSnapUsageOnRootID(int rootid) {
        return getMSU.getSnapUsageOnRootID(rootid);
    }

    public SnapUsage getSnapUsageOnSomething(int da_id, int rootid, String mp) {
        return getMSU.getSnapUsageOnSomething(da_id, rootid, mp);
    }

    public SnapUsage getSnapUsageOnSomething(int da_id, String mp) {
        return getMSU.getSnapUsageOnSomething(da_id, mp);
    }

    public ArrayList getSnapUsageOnSomething(int da_id, int rootid) {
        return getMSU.getSnapUsageOnSomething(da_id, rootid);
    }

    public ArrayList getAllMSU() {
        return getMSU.getAllMSUs();
    }

    //////////////////////////////////////////////
    //
    //   NetbootedHost
    //
    //////////////////////////////////////////////
    public boolean updateNBH() {
        boolean ret = getNBH.updateDestAgent();
        if (!ret) {
            errorMsg = getNBH.getErrMsg();
            errorCode = getNBH.getRetCode();
        }
        return ret;
    }

    public boolean addNBH(DestAgent da) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" dst_agent_ip=" + da.getDst_agent_ip());
            buf.append(" dst_agent_port=" + da.getDst_agent_port());
            buf.append(" dst_agent_ostype=" + da.getDst_agent_ostype());
            buf.append(" dst_agent_mac=" + da.getDst_agent_mac());
            buf.append(" dst_agent_desc=" + da.getDst_agent_desc());
            buf.append(" dst_agent_protect_type=" + da.getProtectType());

            addNBH.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_NETBOOTED_HOST) + buf.toString());

            SanBootView.log.info(getClass().getName(), "add netbooted_host: " + addNBH.getCmdLine());

            addNBH.run();
        } catch (Exception ex) {
            recordException(addNBH, ex);
        }

        recordNewId(addNBH);
        SanBootView.log.info(getClass().getName(), "add netbooted_host retcode: " + addNBH.getRetCode());

        boolean isOk = finished(addNBH);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add netbooted_host errmsg: " + addNBH.getErrMsg());
        }
        return isOk;
    }

    public boolean modNBH(int id, String ip) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append(id);
            buf.append(" dst_agent_ip=" + ip);

            modNBH.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_NETBOOTED_HOST) + buf.toString());

            SanBootView.log.info(getClass().getName(), "mod netbooted_host: " + modNBH.getCmdLine());

            modNBH.run();
        } catch (Exception ex) {
            recordException(modNBH, ex);
        }

        SanBootView.log.info(getClass().getName(), "mod netbooted_host retcode: " + modNBH.getRetCode());

        boolean isOk = finished(modNBH);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod netbooted_host errmsg: " + modNBH.getErrMsg());
        }
        return isOk;
    }

    public boolean delNBH(int id) {
        try {
            delNBH.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_NETBOOTED_HOST) + id);
            SanBootView.log.info(getClass().getName(), "del netbooted_host cmd: " + delNBH.getCmdLine());
            delNBH.run();
        } catch (Exception ex) {
            recordException(delNBH, ex);
        }
        SanBootView.log.info(getClass().getName(), "del netbooted_host cmd retcode: " + delNBH.getRetCode());
        boolean isOk = finished(delNBH);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del netbooted_host cmd errmsg: " + delNBH.getErrMsg());
        }
        return isOk;
    }

    public void addNBHIntoCache(DestAgent one) {
        getNBH.addDestAgntToVector(one);
    }

    public void removeNBHFromCache(DestAgent one) {
        getNBH.removeDestAgntFromVector(one);
    }

    public DestAgent getNBHFromCacheOnDestAgntID(int dstAgntID) {
        return getNBH.getDAFromCacheOnDestAgntID(dstAgntID);
    }

    public ArrayList getAllNBH() {
        return getNBH.getAllDAs();
    }

    ///////////////////////////////////////////////
    //
    //                BootHost
    //
    //////////////////////////////////////////////
    public boolean updateBootHost() {
        boolean ret = getBootHost.updateBootHost();
        if (!ret) {
            errorMsg = getBootHost.getErrMsg();
            errorCode = getBootHost.getRetCode();
        }
        return ret;
    }

    public boolean queryBootHostOnDestUWSrv(String dest_uws_ip, int dest_uws_port, String uuid) {
        queryBootHostOnDestUWSrv.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + dest_uws_ip + " " + dest_uws_port
                + " \"idbmgr -clnt -prt clnt_conf=" + uuid + "\"");
        boolean ret = queryBootHostOnDestUWSrv.updateBootHost();
        if (!ret) {
            errorMsg = queryBootHostOnDestUWSrv.getErrMsg();
            errorCode = queryBootHostOnDestUWSrv.getRetCode();
        }
        return ret;
    }

    public boolean getOneBootHost(int hid) {
        getOneBootHost.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_ONE_HOST) + hid);
        boolean ret = getOneBootHost.updateBootHost();
        if (!ret) {
            errorMsg = getOneBootHost.getErrMsg();
            errorCode = getOneBootHost.getRetCode();
        }
        return ret;
    }

    public ArrayList<BootHost> getMemberForCluster(int cluster_id) {
        return getBootHost.getMemberForCluster(cluster_id);
    }

    public ArrayList<BackupClient> getD2DMemberForCluster(int cluster_id) {
        ArrayList<BackupClient> ret = new ArrayList<BackupClient>();
        ArrayList<BootHost> members = this.getMemberForCluster(cluster_id);
        int size = members.size();
        for (int i = 0; i < size; i++) {
            BootHost member = members.get(i);
            if (member.getClnt_d2d_cid() > 0) {
                BackupClient bkClnt = this.getClientFromVector(member.getClnt_d2d_cid());
                if (bkClnt != null) {
                    ret.add(bkClnt);
                }
            }
        }
        return ret;
    }

    public BootHost getOneBootHostInfo(int hid) {
        return getOneBootHost.getHostFromVectorOnID(hid);
    }

    public BootHost getBootHostOnRootId(int rootid) {
        VolumeMap volMap = this.getVolMapOnRootID(rootid);
        if (volMap != null) {
            return this.getBootHostFromVector(volMap.getVolClntID());
        } else {
            return null;
        }
    }

    public BootHost isThisBootHostOnDestUWSrv(String uuid) {
        return queryBootHostOnDestUWSrv.isExistBootHost(uuid);
    }

    public BootHost getBootHostOnName(String name) {
        return getBootHost.getHostFromVectorOnName(name);
    }

    public BootHost getHostFromVecOnIP(String ip) {
        return getBootHost.getHostFromVecOnIP(ip);
    }

    public BootHost getHostFromVectorOnID(long id) {
        return getBootHost.getHostFromVectorOnID(id);
    }

    public BootHost getHostFromVectOnD2DClntID(int d2d_clnt_id) {
        return getBootHost.getHostFromVectOnD2DClntID(d2d_clnt_id);
    }

    public BootHost getHostFromVectOnD2DClntID1(String d2d_clnt_id) {
        return getBootHost.getHostFromVectOnD2DClntID1(d2d_clnt_id);
    }

    public BootHost getHostFromCacheOnUUID(String uuid) {
        return getBootHost.getBootHostOnUUID(uuid);
    }

    public BootHost getBootHostOnUUIDAndIP(String uuid, String ip) {
        return getBootHost.getBootHostOnUUIDAndIP(uuid, ip);
    }

    public BootHost getBootHostOnUUIDAndIPForRac(String uuid, String ip) {
        return getBootHost.getBootHostOnUUIDAndIPForRac(uuid, ip);
    }

    public Vector getAllBootHost() {
        return getBootHost.getAllBootHost();
    }

    public Vector getAllRealBootHost() {
        return getBootHost.getAllRealBootHost();
    }

    public ArrayList<BootHost> getAllSingleBootHost() {
        return getBootHost.getAllSingleBootHost();
    }

    public Vector getAllUninitedHost() {
        return getBootHost.getBootHostOnType(0);
    }

    public Vector getAllInitedHost() {
        return getBootHost.getBootHostOnType(1);
    }

    public boolean modOneBootHost1(int cltID, int initFlag, int auto_dr, int auto_reboot, int stop_allserv, String mac, int bootMode) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" clnt_id=" + cltID);
            buf.append(" clnt_inited_flag=" + initFlag);
            buf.append(" clnt_auto_dr_flag=" + auto_dr);
            buf.append(" clnt_auto_reboot_flag=" + auto_reboot);
            SanBootView.log.info(getClass().getName(), "stop_allserv: " + stop_allserv);
            buf.append(" clnt_stop_base_service=" + stop_allserv);
            buf.append(" clnt_mac_address=" + mac);
            buf.append(" clnt_boot_mode=" + bootMode);

            modBootHost.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_BOOT_HOST)
                    + buf.toString());

            SanBootView.log.info(getClass().getName(), "mod boot host: " + modBootHost.getCmdLine());

            modBootHost.run();
        } catch (Exception ex) {
            recordException(modBootHost, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod boot host retcode: " + modBootHost.getRetCode());
        boolean isOk = finished(modBootHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod boot host errmsg: " + modBootHost.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneBootHost2(int cltID, int stop_allserv) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" clnt_id=" + cltID);
            SanBootView.log.info(getClass().getName(), "stop_allserv: " + stop_allserv);
            buf.append(" clnt_stop_base_service=" + stop_allserv);

            modBootHost.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_BOOT_HOST)
                    + buf.toString());

            SanBootView.log.info(getClass().getName(), "mod boot host: " + modBootHost.getCmdLine());

            modBootHost.run();
        } catch (Exception ex) {
            recordException(modBootHost, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod boot host retcode: " + modBootHost.getRetCode());
        boolean isOk = finished(modBootHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod boot host errmsg: " + modBootHost.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneBootHost3(int cltID, int auto_switch_disk) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append(" clnt_id=" + cltID);
            buf.append(" clnt_auto_switch_disk=" + auto_switch_disk);

            modBootHost.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_BOOT_HOST)
                    + buf.toString());

            SanBootView.log.info(getClass().getName(), "mod boot host: " + modBootHost.getCmdLine());

            modBootHost.run();
        } catch (Exception ex) {
            recordException(modBootHost, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod boot host retcode: " + modBootHost.getRetCode());
        boolean isOk = finished(modBootHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod boot host errmsg: " + modBootHost.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneBootHost4(BootHost node) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" clnt_id=" + node.getID());
            buf.append(" clnt_ip=" + node.getIP());
            buf.append(" clnt_port=" + node.getPort());
            buf.append(" clnt_port1=" + node.getMtppPort());
            buf.append(" clnt_name=" + node.getName());
            buf.append(" \"" + "clnt_os=" + node.getOS() + "\"");
            buf.append(" clnt_machine=" + node.getMachine());
            buf.append(" clnt_pri_ip=" + node.getClnt_pri_ip());
            buf.append(" clnt_vip=" + node.getClnt_vip());

            modBootHost.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_BOOT_HOST)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), "mod boot host: " + modBootHost.getCmdLine());

            modBootHost.run();
        } catch (Exception ex) {
            recordException(modBootHost, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod boot host retcode: " + modBootHost.getRetCode());
        boolean isOk = finished(modBootHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod boot host errmsg: " + modBootHost.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneBootHost5(BootHost node) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" clnt_id=" + node.getID());
            buf.append(" clnt_ip=" + node.getIP());
            buf.append(" clnt_port=" + node.getPort());
            buf.append(" clnt_port1=" + node.getMtppPort());
            buf.append(" clnt_name=" + node.getName());
            buf.append(" \"" + "clnt_os=" + node.getOS() + "\"");
            buf.append(" clnt_machine=" + node.getMachine());
            buf.append(" clnt_pri_ip=" + node.getClnt_pri_ip());
            buf.append(" clnt_vip=" + node.getClnt_vip());
            buf.append(" clnt_conf=" + node.getUUID());

            modBootHost.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_BOOT_HOST)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), "mod boot host: " + modBootHost.getCmdLine());

            modBootHost.run();
        } catch (Exception ex) {
            recordException(modBootHost, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod boot host retcode: " + modBootHost.getRetCode());
        boolean isOk = finished(modBootHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod boot host errmsg: " + modBootHost.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneBootHost(BootHost node) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" clnt_id=" + node.getID());
            buf.append(" clnt_ip=" + node.getIP());
            buf.append(" clnt_port=" + node.getPort());
            buf.append(" clnt_port1=" + node.getMtppPort());
            buf.append(" clnt_name=" + node.getName());
            buf.append(" \"" + "clnt_os=" + node.getOS() + "\"");
            buf.append(" clnt_machine=" + node.getMachine());
            buf.append(" clnt_status=" + node.getStatus());
            buf.append(" clnt_conf=" + node.getUUID());
            buf.append(" clnt_inited_flag=" + node.getInitFlag());
            buf.append(" clnt_auto_dr_flag=" + node.getAutoDRFlag());
            buf.append(" clnt_auto_reboot_flag=" + node.getAutoRebootFlag());
            buf.append(" clnt_stop_base_service=" + node.getStopAllBaseServFlag());
            buf.append(" clnt_protect_type=" + node.getProtectType());
            buf.append(" clnt_cluster_id=" + node.getClnt_cluster_id());
            buf.append(" clnt_d2d_cid=" + node.getClnt_d2d_cid());
            buf.append(" clnt_pri_ip=" + node.getClnt_pri_ip());
            buf.append(" clnt_vip=" + node.getClnt_vip());
            buf.append(" clnt_opt=" + node.getClnt_opt());

            modBootHost.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_BOOT_HOST)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), "mod boot host: " + modBootHost.getCmdLine());

            modBootHost.run();
        } catch (Exception ex) {
            recordException(modBootHost, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod boot host retcode: " + modBootHost.getRetCode());
        boolean isOk = finished(modBootHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod boot host errmsg: " + modBootHost.getErrMsg());
        }
        return isOk;
    }

    public boolean addOneBootHost(BootHost node) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" clnt_ip=" + node.getIP());
            buf.append(" clnt_port=" + node.getPort());
            buf.append(" clnt_port1=" + node.getMtppPort());
            buf.append(" clnt_name=" + node.getName());
            buf.append(" \"" + "clnt_os=" + node.getOS() + "\"");
            buf.append(" clnt_machine=" + node.getMachine());
            buf.append(" clnt_status=" + node.getStatus());
            buf.append(" clnt_conf=" + node.getUUID());
            buf.append(" clnt_inited_flag=" + node.getInitFlag());
            buf.append(" clnt_auto_dr_flag=" + node.getAutoDRFlag());
            buf.append(" clnt_auto_reboot_flag=" + node.getAutoRebootFlag());
            buf.append(" clnt_stop_base_service=" + node.getStopAllBaseServFlag());
            buf.append(" clnt_boot_mode=" + node.getBootMode());
            buf.append(" clnt_mode=" + node.getClntMode());
            buf.append(" clnt_protect_type=" + node.getProtectType());
            buf.append(" clnt_cluster_id=" + node.getClnt_cluster_id());
            buf.append(" clnt_d2d_cid=" + node.getClnt_d2d_cid());
            buf.append(" clnt_pri_ip=" + node.getClnt_pri_ip());
            buf.append(" clnt_vip=" + node.getClnt_vip());
            buf.append(" clnt_opt=" + node.getClnt_opt());

            addBootHost.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_BOOT_HOST)
                    + buf.toString());

            SanBootView.log.info(getClass().getName(), "add boot host: " + addBootHost.getCmdLine());

            addBootHost.run();
        } catch (Exception ex) {
            recordException(addBootHost, ex);
        }

        recordNewId(addBootHost);
        SanBootView.log.info(getClass().getName(), "add boot host retcode: " + addBootHost.getRetCode());

        boolean isOk = finished(addBootHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add boot host errmsg: " + addBootHost.getErrMsg());
        }
        return isOk;
    }

    public String getIpConf() {
        return addBootHost.getIPConf();
    }

    public boolean delBootHost(int id) {
        try {
            delBootHost.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_BOOT_HOST)
                    + id);
            SanBootView.log.info(getClass().getName(), "del boot host cmd: " + delBootHost.getCmdLine());
            delBootHost.run();
        } catch (Exception ex) {
            recordException(delBootHost, ex);
        }
        SanBootView.log.info(getClass().getName(), "del boot host cmd retcode: " + delBootHost.getRetCode());
        boolean isOk = finished(delBootHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del boot host cmd errmsg: " + delBootHost.getErrMsg());
        }
        return isOk;
    }

    public void removeBootHostFromVector(BootHost host) {
        getBootHost.removeHostFromVector(host);
    }

    public void removeBootHostFromVector(long id) {
        BootHost aHost = getBootHostFromVector(id);
        if (aHost != null) {
            this.removeBootHostFromVector(aHost);
        }
    }

    public void addBootHostToVector(BootHost host) {
        getBootHost.addHostToVector(host);
    }

    public BootHost getBootHostFromVector(long id) {
        return getBootHost.getHostFromVectorOnID(id);
    }

    //////////////////////////////////////////////////
    //
    //                ServiceMap
    //
    //////////////////////////////////////////////////
    public boolean updateServMap() {
        boolean ret = getServMap.updateServMap();
        if (!ret) {
            errorMsg = getServMap.getErrMsg();
            errorCode = getServMap.getRetCode();
        }
        return ret;
    }

    public void insertOneServMapIntoVect(ServiceMap servMap) {
        getServMap.addServMapToVector(servMap);
    }

    public void removeOneServMapFromVect(ServiceMap servMap) {
        getServMap.removeServMapFromVector(servMap);
    }

    public boolean addServMap(ServiceMap servMap) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" clnt_id=" + servMap.getClntID());
            buf.append(
                    " " + "\"" + "service_name=" + servMap.getServName() + "\"");
            buf.append(
                    " " + "\"" + "service_desc=" + servMap.getServDesc() + "\"");

            addServMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_SERVMAP)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), " add service map cmd: " + addServMap.getCmdLine());
            addServMap.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            recordException(addServMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " add service cmd retcode: " + addServMap.getRetCode());
        recordNewId(addServMap);
        boolean isOk = finished(addServMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " add service cmd errmsg: " + addServMap.getErrMsg());
        }
        return isOk;
    }

    public boolean delServMap(int id) {
        try {
            delServMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_SERVMAP) + id);
            SanBootView.log.info(getClass().getName(), "delServMap cmd: " + delServMap.getCmdLine());
            delServMap.run();
        } catch (Exception ex) {
            recordException(delServMap, ex);
        }
        SanBootView.log.info(getClass().getName(), "delServMap cmd retcode: " + delServMap.getRetCode());
        boolean isOk = finished(delServMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delServMap cmd errmsg: " + delServMap.getErrMsg());
        }
        return isOk;
    }

    public boolean addReport(String fileName) {
        try {
            addReport.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_UWS_RPT)
                    + fileName);
            SanBootView.log.info(getClass().getName(), " add swu report: " + addReport.getCmdLine());

            addReport.run();
        } catch (Exception ex) {
            recordException(addReport, ex);
        }
        recordNewId(addReport);
        SanBootView.log.info(getClass().getName(), " add swu report retcode: " + addReport.getRetCode());
        boolean isOk = finished(addReport);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " add swu report errmsg: " + addReport.getErrMsg());
        }
        return isOk;
    }

    public Vector getAllServMapOnClntID(int id) {
        return getServMap.getAllServMapOnClntID(id);
    }

    public ServiceMap getServiceMap(int id, String name, String desc) {
        return getServMap.getServiceMap(id, name, desc);
    }

    public ServiceMap getServiceMap(String name, String desc) {
        return getServMap.getServiceMap(name, desc);
    }

    ///////////////////////////////////////////////////
    //
    //           Backup User
    //
    ///////////////////////////////////////////////////
    public boolean updateAllBakUser() {
        boolean ret = getBakUser.updateBakUser();
        if (!ret) {
            errorMsg = getBakUser.getErrMsg();
            errorCode = getBakUser.getRetCode();
        }

        return ret;
    }

    public Vector getAllBakUser() {
        return getBakUser.getAllBackupUser();
    }

    public void removeUserFromVector(BackupUser user) {
        getBakUser.removeBakUserFromVector(user);
    }

    public void AddUserToVector(BackupUser user) {
        getBakUser.addBakUserToVector(user);
    }

    public boolean modBackupUser(long id, String name, String passwd) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" account_id=" + id);
            buf.append(" \"account_name=" + name + "\"");
            buf.append(" \"account_pass=" + passwd + "\"");

            modUser.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_USER)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), " mod user cmd : " + modUser.getCmdLine());
            modUser.run();
        } catch (Exception ex) {
            recordException(modUser, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod user retcode: " + modUser.getRetCode());

        boolean isOk = finished(modUser);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod user errmsg: " + modUser.getErrMsg());
        }
        return isOk;
    }

    public boolean modBackupUser(long id, int mode) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" account_id=" + id);
            buf.append(" account_priv=" + mode);

            modUser.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_USER)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), " mod user's right cmd : " + modUser.getCmdLine());
            modUser.run();
        } catch (Exception ex) {
            recordException(modUser, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod user's right retcode: " + modUser.getRetCode());

        boolean isOk = finished(modUser);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod user's right errmsg: " + modUser.getErrMsg());
        }
        return isOk;
    }

    public boolean addOrModBackupUser(BackupUser user, int mode) {
        NetworkRunning running = null;

        try {
            StringBuffer buf = new StringBuffer();

            if (mode != 0) {
                buf.append(" account_id=" + user.getID());
            }
            buf.append(" account_name=" + user.getUserName());
            buf.append(" account_pass=" + user.getPasswd());
            buf.append(" account_priv=" + user.getRight());

            if (mode == 0) {
                addUser.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_ADD_USER)
                        + buf.toString());
                running = addUser;
            } else {
                modUser.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_MOD_USER)
                        + buf.toString());
                running = modUser;
            }

            running.run();

        } catch (Exception ex) {
            recordException(running, ex);
        }

        if (mode == 0) {
            recordNewId(addUser);
        }
        SanBootView.log.info(getClass().getName(), " add(mod) user retcode: " + running.getRetCode());

        boolean isOk = finished(running);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " add(mod) user errmsg: " + running.getErrMsg());
        }
        return isOk;
    }

    public boolean deleteBakUser(String username) {
        try {
            delUser.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_USER)
                    + username);
            SanBootView.log.info(getClass().getName(), " del user cmd line: " + delUser.getCmdLine());
            delUser.run();
        } catch (Exception ex) {
            recordException(delUser, ex);
        }
        SanBootView.log.info(getClass().getName(), " del user retcode: " + delUser.getRetCode());
        boolean isOk = finished(delUser);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " del user errmsg: " + delUser.getErrMsg());
        }
        return isOk;
    }

    public BackupUser getBakUserOnName(String name) {
        return getBakUser.getBakUserFromVectorOnName(name);
    }

    public BackupUser getBakUserFromVectorOnID(int id) {
        return getBakUser.getBakUserFromVectorOnID(id);
    }

    public boolean isLastAdminUser(String name) {
        return getBakUser.isLastAdminUser(name);
    }

    public boolean restoreMDB(String src) {
        return copyFiles(src, ResourceCenter.MDB_DIR);
    }

    public File createTmpFile(String prefix, String suffix) {
        try {
            File file = File.createTempFile(prefix, suffix);
            return file;
        } catch (IOException ex) {
            SanBootView.log.error(getClass().getName(), "create temp file failed: " + ex.getMessage());
            return null;
        }
    }

    public boolean isLoginUsrIsAdmin() {
        BackupUser user = getBakUserOnName(view.initor.user);
        if (user != null) {
            return user.isAdminRight();
        } else {
            return false;
        }
    }

    /////////////////////////////////////////////////
    //
    //            send file to server
    //
    /////////////////////////////////////////////////
    public boolean sendFileToServer(String filename, String contents) {
        SanBootView.log.info(getClass().getName(), " send this file to server: " + filename);
        if (contents.equals("")) {
            SanBootView.log.warning(getClass().getName(), " Contents to send is null,so assign a string with white space to it.");
            contents = "                         ";
        }
        boolean ret = sendFileToSrv.sendFileToSrv(filename, contents);
        if (!ret) {
            errorMsg = sendFileToSrv.getErrMsg();
            errorCode = sendFileToSrv.getRetCode();
        }
        SanBootView.log.info(getClass().getName(), " send file to server retcode: " + sendFileToSrv.getRetCode());
        if (!ret) {
            SanBootView.log.error(getClass().getName(), " send file to server errmsg: " + sendFileToSrv.getErrMsg());
        }
        return ret;
    }

    ////////////////////////////////////////////////
    //
    //           Volume  Map
    //
    ////////////////////////////////////////////////
    public boolean updateVolumeMap() {
        boolean ret = getAllVolMap.updateVolMap();
        if (!ret) {
            this.errorMsg = getAllVolMap.getErrMsg();
            this.errorCode = getAllVolMap.getRetCode();
        }
        return ret;
    }

    // 为了兼容“真正融合版本”之前的版本，之前的版本volumemap中没有protect type字段;
    public void updatePTypeForVolumeMap() {
        int i, size, clntID;

        Vector<VolumeMap> list = getAllVolMaps();
        size = list.size();
        for (i = 0; i < size; i++) {
            VolumeMap one = list.get(i);
            if (one.isUnknownProtect()) {
                clntID = one.getVolClntID();
                BootHost host = this.getBootHostFromVector(clntID);
                if (host != null) {
                    one.setVol_protect_type(host.getProtectType());
                }
            }
        }
    }

    public boolean updateOneVolumeMap(String volName) {
        boolean ret = getOneVolMap.updateVolMap(volName);
        if (!ret) {
            this.errorMsg = getOneVolMap.getErrMsg();
            this.errorCode = getOneVolMap.getRetCode();
        }
        return ret;
    }

    public VolumeMap getOneVolMap(String volName) {
        return getOneVolMap.getVolMapFromVecOnName(volName);
    }

    public boolean queryVolMapOnDestUWSrv(String dest_uws_ip, int dest_uws_port, int clntid, String mp) {
        queryVolMapOnDestUWSrv.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + dest_uws_ip + " " + dest_uws_port
                + " \"idbmgr -vol -prt vol_clntid=" + clntid + " \"vol_disklabel=" + mp + "\"\"");
        boolean ret = queryVolMapOnDestUWSrv.updateVolMap();
        if (!ret) {
            errorMsg = queryVolMapOnDestUWSrv.getErrMsg();
            errorCode = queryVolMapOnDestUWSrv.getRetCode();
        }
        return ret;
    }

    public VolumeMap isThisVolMapOnDestUWSrv(int clntid, String mp) {
        return queryVolMapOnDestUWSrv.isExistVolMap(clntid, mp);
    }

    public VolumeMap isVolExist(int clntid, String mp) {
        return getAllVolMap.isExistVolMap(clntid, mp);
    }

    public void insertOneVolMapIntoVec(VolumeMap volMap) {
        getAllVolMap.addVolMapToVector(volMap);
    }

    public void removeOneVolMapFromVec(VolumeMap volMap) {
        getAllVolMap.removeVolMapFromVector(volMap);
    }

    public void replaceVolMap(String volName, String vol_info) {
        getAllVolMap.replaceVolumeMap(volName, vol_info);
    }

    public VolumeMap generalGetVolMapFromVecOnClntandLabel(int clntid, String label) {
        return getAllVolMap.generalGetVolMapFromVecOnClntandLabel(clntid, label);
    }

    public VolumeMap getVolMapFromVecOnClntandLabel(int clntid, String label) {
        return getAllVolMap.getVolMapFromVecOnClntandLabel(clntid, label);
    }

    public VolumeMap getVolMapFromVecOnClusterAndLabel(int cluster_id, String label) {
        return getAllVolMap.getVolMapFromVecOnClusterandLabel(cluster_id, label);
    }

    public boolean modOneVolumeMap(String volName, int maxSnap, String desc) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " vol_max_snap=" + maxSnap
                    + " \"" + "vol_desc=" + desc + "\"");

            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap(String volName, int maxSnap) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " vol_max_snap=" + maxSnap);
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap1(String volName, int view_tid, String crttime, int disk_type) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " vol_view_targetid=" + view_tid + " vol_last_good_boot_info=" + crttime
                    + " last_good_boot_disk_type=" + disk_type);
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap5(String volName, int view_tid, String crttime, int disk_type) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " vol_switch_last_good_version=" + view_tid + " vol_switch_last_good_info=" + crttime
                    + " vol_switch_last_good_disk_type=" + disk_type);
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap6(String volName, int maxSnap, String desc, int protect_type) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " vol_max_snap=" + maxSnap
                    + " vol_protect_type=" + protect_type
                    + " \"" + "vol_desc=" + desc + "\"");
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap7(VolumeMap volMap) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volMap.getVolName()
                    + " vol_info=" + volMap.getVol_info());
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap7(VolumeMap volMap, String volinfo) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volMap.getVolName()
                    + " vol_info=" + volinfo);
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap7(String volName, int vol_opt, int cluster_id) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " vol_opt=" + vol_opt
                    + " vol_cluster_id=" + cluster_id);
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap8(String volName, int mgid) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " vol_mgid=" + mgid);
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap2(String volName, String localId, int view_tid, String crttime, int disk_type) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " setversion=" + localId
                    + " vol_last_sel_boot_version=" + view_tid + " vol_last_sel_boot_info=" + crttime
                    + " last_sel_boot_disk_type=" + disk_type);
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap3(String volName, String setVersion) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " setversion=" + setVersion);
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap4(String volName, int view_tid, String crttime, int disk_type) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " vol_switch_last_sel_version=" + view_tid + " vol_switch_last_sel_info=" + crttime
                    + " vol_switch_last_sel_disk_type=" + disk_type);
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap(String volName, String mp) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " vol_disklabel=" + mp);
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneVolumeMap3(String volName, int tid) {
        try {
            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP) + volName
                    + " vol_target_id=" + tid);
            SanBootView.log.info(getClass().getName(), " mod volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modWholeVolumeMap(VolumeMap volMap) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(volMap.getVolName());
            buf.append(" vol_clntid=" + volMap.getVolClntID());
            buf.append(" vol_disklabel=" + volMap.getVolDiskLabel());
            buf.append(" vol_target_id=" + volMap.getVolTargetID());
            buf.append(" vol_max_snap=" + volMap.getMaxSnapNum());
            buf.append(" \"" + "vol_desc=" + volMap.getVolDesc() + "\"");
            buf.append(" vol_rootid=" + volMap.getVol_rootid());
            buf.append(" \"" + "rollback_orgi_volname=" + volMap.getRollbackOrgiVolname() + "\"");
            buf.append(" \"" + "rollback_orgi_uws=" + volMap.getRollbackOrgiUws() + "\"");

            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), " modify volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " modify volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " modify volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean modMirrorInfoOnVolumeMap(String volName, String uuid, String others) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append(volName);
            buf.append(" vol_uuid=" + uuid);
            buf.append(" others=" + others);

            modVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VOLMAP)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), " modify volmap cmd: " + modVolMap.getCmdLine());
            modVolMap.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            recordException(modVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " modify volmap cmd retcode: " + modVolMap.getRetCode());
        boolean isOk = finished(modVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " modify volmap cmd errmsg: " + modVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean AddOneVolumeMap(VolumeMap volMap) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" vol_name=" + volMap.getVolName());
            buf.append(" vol_clntid=" + volMap.getVolClntID());
            buf.append(" vol_disklabel=" + volMap.getVolDiskLabel());
            buf.append(" vol_target_id=" + volMap.getVolTargetID());
            buf.append(" vol_max_snap=" + volMap.getMaxSnapNum());
            buf.append(" \"" + "vol_desc=" + volMap.getVolDesc() + "\"");
            buf.append(" vol_rootid=" + volMap.getVol_rootid());
            buf.append(" \"" + "rollback_orgi_volname=" + volMap.getRollbackOrgiVolname() + "\"");
            buf.append(" \"" + "rollback_orgi_uws=" + volMap.getRollbackOrgiUws() + "\"");
            buf.append(" vol_protect_type=" + volMap.getVol_protect_type());
            buf.append(" vol_opt=" + volMap.getVol_opt());
            buf.append(" vol_cluster_id=" + volMap.getVol_cluster_id());

            addVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_VOLMAP)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), " add volmap cmd: " + addVolMap.getCmdLine());
            addVolMap.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            recordException(addVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " add volmap cmd retcode: " + addVolMap.getRetCode());
        boolean isOk = finished(addVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " add volmap cmd errmsg: " + addVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean delVolumeMap(VolumeMap volMap) {
        try {
            delVolMap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_VOLMAP)
                    + volMap.getVolName());
            SanBootView.log.info(getClass().getName(), " del volmap cmd: " + delVolMap.getCmdLine());
            delVolMap.run();
        } catch (Exception ex) {
            recordException(delVolMap, ex);
        }
        SanBootView.log.info(getClass().getName(), " del volmap cmd retcode: " + delVolMap.getRetCode());
        boolean isOk = finished(delVolMap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " del volmap cmd errmsg: " + delVolMap.getErrMsg());
        }
        return isOk;
    }

    public boolean addOrphVol(String volName, int bksize, int bkNum, int poolid) {
        try {
            addVol.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_VOL)
                    + bksize + " -n " + bkNum + " -p " + poolid + " -o " + ResourceCenter.OWNER_MTPP + " -v " + volName);
            SanBootView.log.info(getClass().getName(), "addVol cmd: " + addVol.getCmdLine());
            addVol.run();
        } catch (Exception ex) {
            recordException(addVol, ex);
        }
        SanBootView.log.info(getClass().getName(), "addVol cmd retcode: " + addVol.getRetCode());
        this.newId = addVol.getTargetID();
        boolean isOk = finished(addVol);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "addVol cmd errMsg: " + addVol.getErrMsg());
        }
        return isOk;
    }

    public boolean addOrphVolAmsUcs(String volName, int bksize, int bkNum, int poolid,
            int oldestPoolId, int latestPoolId, int logPoolId, int logNum) {
        try {
            addVol.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_VOL)
                    + bksize + " -n " + bkNum + " -p " + poolid + " -o " + ResourceCenter.OWNER_CMDP + " -v " + volName
                    + " -l " + oldestPoolId + ":" + latestPoolId + ":" + logPoolId + ":" + logNum);
            SanBootView.log.info(getClass().getName(), "addVol cmd: " + addVol.getCmdLine());
            addVol.run();
        } catch (Exception ex) {
            recordException(addVol, ex);
        }
        SanBootView.log.info(getClass().getName(), "addVol cmd retcode: " + addVol.getRetCode());
        this.newId = addVol.getTargetID();
        boolean isOk = finished(addVol);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "addVol cmd errMsg: " + addVol.getErrMsg());
        }
        return isOk;
    }

    public boolean addOrphVolAms(String volName, int bksize, int bkNum, int poolid) {
        try {
            addVol.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_VOL)
                    + bksize + " -n " + bkNum + " -p " + poolid + " -o " + ResourceCenter.OWNER_CMDP + " -v " + volName + " -m");
            SanBootView.log.info(getClass().getName(), "addVol cmd: " + addVol.getCmdLine());
            addVol.run();
        } catch (Exception ex) {
            recordException(addVol, ex);
        }
        SanBootView.log.info(getClass().getName(), "addVol cmd retcode: " + addVol.getRetCode());
        this.newId = addVol.getTargetID();
        boolean isOk = finished(addVol);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "addVol cmd errMsg: " + addVol.getErrMsg());
        }
        return isOk;
    }

    public Volume getCrtVolume() {
        return addVol.getCrtVolume();
    }

    public int getRootID() {
        return addVol.getRootID();
    }

    public boolean delVolume(Volume vol) {
        try {
            delVol.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_VOL)
                    + vol.getSnap_root_id());
            SanBootView.log.info(getClass().getName(), "delvol cmd: " + delVol.getCmdLine());
            delVol.run();
        } catch (Exception ex) {
            recordException(delVol, ex);
        }
        SanBootView.log.info(getClass().getName(), "delvol cmd retcode: " + delVol.getRetCode());
        boolean isOk = finished(delVol);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delvol cmd errmsg: " + delVol.getErrMsg());
        }
        return isOk;
    }

    public boolean onlineVolume(int rootid, int snapid) {
        try {
            onlineVol.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ONLINE_DEV) + rootid + " " + snapid);
            SanBootView.log.info(getClass().getName(), "online vol cmd: " + onlineVol.getCmdLine());
            onlineVol.run();
        } catch (Exception ex) {
            recordException(onlineVol, ex);
        }
        SanBootView.log.info(getClass().getName(), "online vol cmd retcode: " + onlineVol.getRetCode());
        boolean isOk = finished(onlineVol);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "online vol cmd errmsg: " + onlineVol.getErrMsg());
        }
        return isOk;
    }

    public boolean offlineVolume(int rootid, int snapid) {
        try {
            offlineVol.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_OFFLINE_DEV) + rootid + " " + snapid);
            SanBootView.log.info(getClass().getName(), "offline vol cmd: " + offlineVol.getCmdLine());
            offlineVol.run();
        } catch (Exception ex) {
            recordException(offlineVol, ex);
        }
        SanBootView.log.info(getClass().getName(), "offline vol cmd retcode: " + offlineVol.getRetCode());
        boolean isOk = finished(offlineVol);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "offline vol cmd errmsg: " + offlineVol.getErrMsg());
        }
        return isOk;
    }

    public boolean getVolumeSize(String volName) {
        try {
            getVolSize.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_VOL_SIZE)
                    + volName);
            SanBootView.log.info(getClass().getName(), "getVolSize cmd: " + getVolSize.getCmdLine());
            getVolSize.run();
        } catch (Exception ex) {
            recordException(getVolSize, ex);
        }
        SanBootView.log.info(getClass().getName(), "getVolSize cmd retcode: " + getVolSize.getRetCode());
        boolean isOk = finished(getVolSize);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "getVolSize cmd errmsg: " + getVolSize.getErrMsg());
        }
        return isOk;
    }

    public float getRealVolSize() {
        return getVolSize.getRealVolSize();
    }

    public boolean isCreatedVolGrp() {
        try {
            SanBootView.log.info(getClass().getName(), "isCrtVg cmd: " + isCrtVg.getCmdLine());
            isCrtVg.run();
        } catch (Exception ex) {
            recordException(isCrtVg, ex);
        }
        SanBootView.log.info(getClass().getName(), "isCrtVg cmd retcode: " + isCrtVg.getRetCode());
        boolean isOk = finished(isCrtVg);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "isCrtVg cmd errmsg: " + isCrtVg.getErrMsg());
        }
        return isOk;
    }

    public boolean hasVG() {
        return isCrtVg.isCreatedVG();
    }

    public boolean getVolGrpSize() {
        try {
            SanBootView.log.info(getClass().getName(), "getVgSize cmd: " + getVgSize.getCmdLine());
            getVgSize.run();
        } catch (Exception ex) {
            recordException(getVgSize, ex);
        }
        SanBootView.log.info(getClass().getName(), "getVgSize cmd retcode: " + getVgSize.getRetCode());
        boolean isOk = finished(getVgSize);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "getVgSize cmd errmsg: " + getVgSize.getErrMsg());
        }
        return isOk;
    }

    public float getRealVGSize() {
        return getVgSize.getRealVGSize();
    }

    public void AddVolumeMapToVector(VolumeMap volMap) {
        getAllVolMap.addVolMapToVector(volMap);
    }

    public VolumeMap getVolMapFromVecOnTID(int tid) {
        return getAllVolMap.getVolMapFromVecOnTID(tid);
    }

    public VolumeMap getVolMapFromVecOnMgID(int mgId) {
        return getAllVolMap.getVolMapOnMgid(mgId);
    }

    public VolumeMap getVolMapFromVectorOnName(String name) {
        return getAllVolMap.getVolMapFromVecOnName(name);
    }

    public void removeVolMapFromVector(VolumeMap volMap) {
        getAllVolMap.removeVolMapFromVector(volMap);
    }

    public Vector<VolumeMap> getAllVolMaps() {
        return getAllVolMap.getAllVolumeMap();
    }

    public Vector<VolumeMap> getVolMapOnClntID(int cid) {
        return getAllVolMap.getVolMapOnClntID(cid);
    }

    public ArrayList<VolumeMap> getVolMapOnClntID1(int cid) {
        return getAllVolMap.getVolMapOnClntID1(cid);
    }

    public Vector<VolumeMap> getVolMapOnClntIDAndPType(int cid, int ptype) {
        return getAllVolMap.getVolMapOnClntIDAndPType(cid, ptype);
    }

    public Vector<VolumeMap> getVolMapOnClusterIDAndPType(int cluster_id, int ptype) {
        return getAllVolMap.getVolMapOnClusterIDAndPType(cluster_id, ptype);
    }

    public Vector<VolumeMap> getVolMapWithoutOSDiskOnClntID(int cid) {
        return getAllVolMap.getVolMapWithoutOSDiskOnClntID(cid);
    }

    public boolean isExistThisServiceOnVolMap(int cid, String service) {
        return getAllVolMap.isExistThisServiceOnVolMap(cid, service);
    }

    public Vector getServiceOfVolMapOnClntID(int cid) {
        return getAllVolMap.getServiceOfVolMapOnClntID(cid);
    }

    public HashMap<Integer, VolumeMap> getVolMapHashMapOnClntID(int cid) {
        return getAllVolMap.getVolMapHashMapOnClntID(cid);
    }

    public VolumeMap getRollbackedVolMap(String volname, String uws, int cid) {
        return getAllVolMap.getRollbackedVolMap(volname, uws, cid);
    }

    public Vector<VolumeMapWrapper> getVolMapWrapOnClntID(int id, int type) {
        return getAllVolMap.getVolMapWrapOnClntID(id, type);
    }

    public VolumeMap getVolMapOnRootID(int rootid) {
        return getAllVolMap.getVolMapOnRootID(rootid);
    }

    // 下列这些方法只在非windows 主机上使用,因为跟它
    // 相关的volmap需要分析具体类型(vg/lv/tgt).
    public Vector<VolumeMap> getVgListOnClntID(int clntid) {
        Vector<VolumeMap> list = getVolMapOnClntID(clntid);
        return getVgListOnClntID(list, clntid);
    }

    public Vector getTgtListOnClntID(int clntid) {
        Vector<VolumeMap> list = getVolMapOnClntID(clntid);
        return getTgtListOnClntIDAndVg(list, null, clntid);
    }

    public Vector getTgtListOnClntIDAndVgName(int clntid, String vgname) {
        Vector<VolumeMap> list = getVolMapOnClntID(clntid);
        return getTgtListOnClntIDAndVg(list, vgname, clntid);
    }
    // 正常情况下,lv和target并没有1:1的对应关系，但是UWS规定一个vg只能由一个target组成,一个vg只有一个lv，
    // 使得lv和target之间具有了1:1的关系

    public int getTargetIDOnLV(LogicalVol lv) {
        return getTgtIDOnVGname(lv.getVgName(), lv.getVolClntID());
    }

    public VolumeMap getTargetOnLV(LogicalVol lv) {
        return getTgtOnVGname(lv.getVgName(), lv.getVolClntID());
    }

    public int getTgtIDOnVGname(String vgname, int clntid) {
        VolumeMap tgt = getTgtOnVGname(vgname, clntid);
        if (tgt != null) {
            return tgt.getVolTargetID();
        } else {
            return -1;
        }
    }

    public VolumeMap getVGOnVGName(String vgname, int clntid) {
        Vector<VolumeMap> vgList = getVgListOnClntID(clntid);
        int size = vgList.size();
        for (int i = 0; i < size; i++) {
            VolumeMap vg = vgList.elementAt(i);
            if (vg.getVolName().equals(vgname)) {
                return vg;
            }
        }
        return null;
    }

    public boolean hasThisVgOnTid(int tid) {
        VolumeMap volMap;

        Vector<VolumeMap> list = getAllVolMaps();

        int size = list.size();
        for (int i = 0; i < size; i++) {
            volMap = list.elementAt(i);
            if (volMap.isVG()) {
                if (volMap.getVolName().indexOf(tid + "") >= 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public VolumeMap getVgOnTid(int clntID, int tid) {
        VolumeMap volMap;

        Vector<VolumeMap> list = getVolMapOnClntID(clntID);

        int size = list.size();
        for (int i = 0; i < size; i++) {
            volMap = list.elementAt(i);
            if (volMap.isVG()) {
                if (volMap.getVolName().equals("vg_" + clntID + "_" + tid)) {
                    return volMap;
                }
            }
        }

        return null;
    }

    public VolumeMap getLVOnTid(int tid) {
        VolumeMap volMap;

        Vector<VolumeMap> list = getAllVolMaps();

        int size = list.size();
        for (int i = 0; i < size; i++) {
            volMap = list.elementAt(i);
            if (volMap.getVolDiskLabel().startsWith("/") && volMap.getVolTargetID() == 0) { // is a LV
                if (volMap.getVolDesc().indexOf(tid + "") >= 0) {
                    return volMap;
                }
            }
        }

        return null;
    }

    public VolumeMap getTgtOnVGname(String vgname, int clntid) {
        Vector<VolumeMap> list = getVolMapOnClntID(clntid);
        Vector<VolumeMap> tgtList = getTgtListOnClntIDAndVg(list, vgname, clntid);
        if (tgtList.size() > 0) {
            return tgtList.elementAt(0);
        } else {
            return null;
        }
    }

    public Vector<VolumeMap> getLVListOnClntID(int clntid) {
        Vector<VolumeMap> list = getVolMapOnClntID(clntid);
        return getLVListOnClntID(list, null);
    }

    public Vector<VolumeMap> getLVListOnClntIDAndVgname(int clntid, String vgName) {
        Vector<VolumeMap> list = getVolMapOnClntID(clntid);
        return getLVListOnClntID(list, vgName);
    }

    public VolumeMap getLVOnClntIDAndMP(int clntid, String mp) {
        VolumeMap lv;

        Vector<VolumeMap> list = getLVListOnClntID(clntid);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            lv = list.elementAt(i);
            if (lv.getVolDiskLabel().equals(mp)) {
                return lv;
            }
        }

        return null;
    }

    public VolumeMap getRealLVOnClntIDAndMP(int clntid, String mp) {
        VolumeMap lv;

        Vector<VolumeMap> list = getRealLVListOnClntID(clntid);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            lv = list.elementAt(i);
            if (lv.getVolDiskLabel().equals(mp)) {
                return lv;
            }
        }

        return null;
    }

    public VolumeMap getLVOnClntAndVG(int clntid, String vg) {
        VolumeMap lv;

        Vector<VolumeMap> list = getLVListOnClntID(clntid);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            lv = list.elementAt(i);
            if (lv.getVolDesc().equals(vg)) {
                return lv;
            }
        }

        return null;
    }

    public Vector<VolumeMap> getVgListOnClntID(Vector<VolumeMap> list, int clntid) {
        VolumeMap volMap;
        Vector<VolumeMap> ret = new Vector<VolumeMap>();

        int size = list.size();
        for (int i = 0; i < size; i++) {
            volMap = list.elementAt(i);
            if (volMap.isVG()) {
                ret.addElement(volMap);
            }
        }

        return ret;
    }

    public Vector<VolumeMap> getTgtListOnClntIDAndVg(Vector<VolumeMap> list, String vgname, int clntid) {
        VolumeMap volMap;
        Vector<VolumeMap> ret = new Vector<VolumeMap>();

        int size = list.size();
        for (int i = 0; i < size; i++) {
            volMap = list.elementAt(i);
            if (volMap.isTGT()) {
                if (vgname != null) {
                    if (volMap.getVolDiskLabel().equals(vgname)) {
                        ret.addElement(volMap);
                    }
                } else {
                    ret.addElement(volMap);
                }
            }
        }

        return ret;
    }

    public Vector<VolumeMap> getLVListOnClntID(Vector<VolumeMap> list, String vgname) {
        VolumeMap volMap;
        Vector<VolumeMap> ret = new Vector<VolumeMap>();

        int size = list.size();
        for (int i = 0; i < size; i++) {
            volMap = list.elementAt(i);
            if (volMap.isLV()) {
                if (vgname != null) {
                    if (volMap.getVolDesc().equals(vgname)) {
                        ret.addElement(volMap);
                    }
                } else {
                    ret.addElement(volMap);
                }
            }
        }

        return ret;
    }

    public Vector<VolumeMap> getRealLVListOnClntID(int clntId) {
        VolumeMap volMap;
        int tid;

        Vector<VolumeMap> ret = new Vector<VolumeMap>();

        Vector<VolumeMap> list = getVolMapOnClntID(clntId);
        Vector<VolumeMap> lvList = getLVListOnClntID(list, null);
        int size = lvList.size();
        for (int i = 0; i < size; i++) {
            volMap = lvList.elementAt(i);
            tid = getTgtIDOnVGname(volMap.getVolDesc(), clntId);
            if (tid != -1) {
                ret.addElement(volMap);
            }
        }

        return ret;
    }

    public Vector<VolumeMapWrapper> getVolMapWrapperForRealLVOnClntID(int clntId, int type) {
        VolumeMap volMap;
        int tid;

        Vector<VolumeMapWrapper> ret = new Vector<VolumeMapWrapper>();

        Vector<VolumeMap> list = getVolMapOnClntID(clntId);
        Vector<VolumeMap> lvList = getLVListOnClntID(list, null);
        int size = lvList.size();
        for (int i = 0; i < size; i++) {
            volMap = lvList.elementAt(i);
            tid = getTgtIDOnVGname(volMap.getVolDesc(), clntId);
            if (tid != -1) {
                VolumeMapWrapper wrap = new VolumeMapWrapper(type);
                wrap.volMap = volMap;
                ret.addElement(wrap);
            }
        }

        return ret;
    }

    // 小曹需要这样的方法，即尽量找出真正的lvm类型
    public String getRealLVMType(int clntID) {
        int i, size;
        String lvmType;
        VolumeMap vg;
        Vector<VolumeMap> vgList;

        Vector<VolumeMap> volMapList = getVolMapOnClntID(clntID);
        vgList = getVgListOnClntID(volMapList, clntID);
        size = vgList.size();
        for (i = 0; i < size; i++) {
            vg = vgList.elementAt(i);
            lvmType = vg.getVolDiskLabel();
            if (lvmType.startsWith("LVM")) {
                return lvmType;
            }
        }

        return "NONE";
    }

    public ArrayList<VolumeMap> getVolFromCluster(int cluster_id) {
        return getAllVolMap.getVolFromCluster(cluster_id);
    }

    public Vector<VolumeMap> getVolFromCluster1(int cluster_id) {
        return getAllVolMap.getVolFromCluster1(cluster_id);
    }

    public Vector<VolumeMapWrapper> getVolFromCluster(int cluster_id, int type) {
        return getAllVolMap.getVolFromCluster(cluster_id, type);
    }

    //////////////////////////////////////////////////////
    //
    //                snapshot
    //
    //////////////////////////////////////////////////////
    public boolean queryVSnapDB(String sql) {
        queryVSnapDB.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_SNAPDB_SQL_QUEUE) + "\"" + sql + "\"");
        boolean isOk = queryVSnapDB.realDo();
        if (!isOk) {
            this.errorMsg = queryVSnapDB.getErrMsg();
            this.errorCode = queryVSnapDB.getRetCode();
        }
        return isOk;
    }

    public boolean queryVSnapDB1(String sql) {
        queryVSnapDB1.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_SNAPDB_SQL_QUEUE) + "\"" + sql + "\"");
        boolean isOk = queryVSnapDB1.realDo();
        if (!isOk) {
            this.errorMsg = queryVSnapDB1.getErrMsg();
            this.errorCode = queryVSnapDB1.getRetCode();
        }
        return isOk;
    }

    public int getQueryResult() {
        return queryVSnapDB1.getQueryResult();
    }

    public ArrayList<BasicVDisk> getAllQueryResult() {
        return queryVSnapDB.getAllResult();
    }

    public int getAllQueryResultNum() {
        return queryVSnapDB.getAllResultNum();
    }

    public ArrayList getSnapListFromQuerySql() {
        return queryVSnapDB.getAllSnapList();
    }

    public Snapshot getCommonSnapshotFromQuerySql(int local_snap_id) {
        return queryVSnapDB.getCommonSnapshotFromQuerySql(local_snap_id);
    }

    public Snapshot getSnapshotFromQuerySql(int local_snap_id) {
        return queryVSnapDB.getSnapshotFromQuerySql(local_snap_id);
    }

    public Snapshot getGeneralSnapshotFromQuerySql(int local_snap_id) {
        return queryVSnapDB.getGeneralSnapshotFromQuerySql(local_snap_id);
    }

    public DeletingSnapshot getDelSnapshotFromQuerySql(int local_snap_id) {
        return queryVSnapDB.getDelSnapshotFromQuerySql(local_snap_id);
    }

    public Snapshot getMirroredSnapshotFromQuerySql(int local_snap_id) {
        return queryVSnapDB.getMirroredSnapshotFromQuerySql(local_snap_id);
    }

    public Snapshot getUIMirroredSnapshotFromQuerySql(int local_snap_id) {
        return queryVSnapDB.getUIMirroredSnapshotFromQuerySql(local_snap_id);
    }

    public Snapshot getMirroredSnapshotFromQuerySql(int rootid, int local_snap_id) {
        return queryVSnapDB.getMirroredSnapshotFromQuerySql(rootid, local_snap_id);
    }

    public Snapshot getUIMirroredSnapshotFromQuerySql(int rootid, int local_snap_id) {
        return queryVSnapDB.getUIMirroredSnapshotFromQuerySql(rootid, local_snap_id);
    }

    public Snapshot getMirroredSnapshotHeaderFromQuerySql(int local_snap_id) {
        return queryVSnapDB.getMirroredSnapshotHeaderFromQuerySql(local_snap_id);
    }

    public Snapshot getUIMirroredSnapshotHeaderFromQuerySql(int local_snap_id) {
        return queryVSnapDB.getUIMirroredSnapshotHeaderFromQuerySql(local_snap_id);
    }

    public Snapshot getMirroredSnapshotHeaderFromQuerySql(int rootid, int local_snap_id) {
        return queryVSnapDB.getMirroredSnapshotHeaderFromQuerySql(rootid, local_snap_id);
    }

    public Snapshot getUIMirroredSnapshotHeaderFromQuerySql(int rootid, int local_snap_id) {
        return queryVSnapDB.getUIMirroredSnapshotHeaderFromQuerySql(rootid, local_snap_id);
    }

    public Snapshot getAvailableMirroredSnapshotFromQuerySql(int rootid, int local_snap_id) {
        return queryVSnapDB.getAvailableMirroredSnapshotFromQuerySql(rootid, local_snap_id);
    }

    public Snapshot getSnapshotFromQuerySql(int rootid, int local_snap_id) {
        return queryVSnapDB.getSnapshotFromQuerySql(rootid, local_snap_id);
    }

    public BasicVDisk getVDisk(int local_snap_id) {
        return queryVSnapDB.getVDisk(local_snap_id);
    }

    public BasicVDisk getVDisk(int rootid, int local_snap_id) {
        return queryVSnapDB.getVDisk(rootid, local_snap_id);
    }

    public Volume getQueryedVolume(int rootid) {
        return queryVSnapDB.getVolume(rootid);
    }

    public Volume getQueryedUIMirVol(int rootid) {
        return queryVSnapDB.getUIMirVol(rootid);
    }

    public View getView(int rootid) {
        return queryVSnapDB.getView(rootid);
    }

    public ArrayList<BasicVDisk> getViewListFromQuerySql() {
        return queryVSnapDB.getAllViewList();
    }

    public ArrayList<View> getViewFromQuerySql(int local_snap_id) {
        return queryVSnapDB.getViewFromQuerySql(local_snap_id);
    }

    public ArrayList<BasicVDisk> getDiskFromQuerySql(ArrayList local_snap_list) {
        return queryVSnapDB.getDiskFromQuerySql(local_snap_list);
    }

    public ArrayList<BasicVDisk> getDiskFromQuerySql(int rootid, ArrayList local_snap_list) {
        return queryVSnapDB.getDiskFromQuerySql(rootid, local_snap_list);
    }

    public ArrayList<SnapWrapper> getSnapWrapperListFromQuerySql(int rootid) {
        return queryVSnapDB.getSnapWrapperListFromQuerySql(rootid);
    }

    public ArrayList<Snapshot> getSnapListFromQuerySql(int rootid) {
        return queryVSnapDB.getSnapListFromQuerySql(rootid);
    }

    public int getSnapNumFromQuerySql(int rootid) {
        return getSnapListFromQuerySql(rootid).size();
    }

    public ArrayList<Snapshot> getUISnapListFromQuerySql(int rootid) {
        return queryVSnapDB.getUISnapListFromQuerySql(rootid);
    }

    public boolean isExistThisDisk(int rootid) {
        boolean ok = view.initor.mdb.queryVSnapDB(
                "select * from " + ResourceCenter.VSnap_DB + " where (" + BasicVDisk.BVDisk_Snap_OpenType + "="
                + BasicVDisk.TYPE_OPENED_DISK + " or " + BasicVDisk.BVDisk_Snap_OpenType + "="
                + BasicVDisk.TYPE_OPENED_AppMirr + " or " + BasicVDisk.BVDisk_Snap_OpenType + "="
                + BasicVDisk.TYPE_OPENED_UCSDISK + ") " + " and " + BasicVDisk.BVDisk_Snap_Root_ID + "=" + rootid + ";");

        if (ok) {
            return (view.initor.mdb.getAllQueryResultNum() > 0);
        } else {
            return ok;
        }
    }

    public boolean getSnapshot(int rootid) {
        getSnap.setAddCacheFlag(true);
        getSnap.setAddTableFlag(false);
        getSnap.setAddTreeFlag(false);
        getSnap.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_SNAP) + rootid);
        boolean isOk = getSnap.realDo();
        if (!isOk) {
            this.errorMsg = getSnap.getErrMsg();
            this.errorCode = getSnap.getRetCode();
        }
        return isOk;
    }

    public int getSnapshotNum() {
        return getSnap.getSnapshotNum();
    }

    public Snapshot getLastSnapshot() {
        return getSnap.getLastSnapshot();
    }

    public ArrayList getAllSnapList() {
        return getSnap.getAllSnapList();
    }

    // 使用文剑在服务器端的命令创建快照，该命令同时给相应的mg发信号
    public boolean addSnapshot(int tid, String maxSnap, int rootid) {
        MirrorGrp mg = view.initor.mdb.getMGFromVectorOnRootID(rootid);
        int mgid = (mg != null) ? mg.getMg_id() : -1;

        try {
            addSnap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_SNAP) + tid + " -m " + maxSnap + " -g " + mgid);
            addSnap.setCmdType(ResourceCenter.CMD_TYPE_MTPP);
            SanBootView.log.info(getClass().getName(), "create Snap cmd: " + addSnap.getCmdLine());
            addSnap.run();
        } catch (Exception ex) {
            recordException(addSnap, ex);
        }
        SanBootView.log.info(getClass().getName(), "create Snap cmd retcode: " + addSnap.getRetCode());
        boolean isOk = finished(addSnap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "create Snap cmd errmsg: " + addSnap.getErrMsg());
        }
        return isOk;
    }

    public boolean addSnapshot(int rootid, int snapid, int poolid, String name) {
        try {
            addSnap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_SNAP1) + name + " " + rootid + " " + snapid + " " + poolid);
            addSnap.setCmdType(ResourceCenter.CMD_TYPE_MTPP);
            SanBootView.log.info(getClass().getName(), "addSnap cmd: " + addSnap.getCmdLine());
            addSnap.run();
        } catch (Exception ex) {
            recordException(addSnap, ex);
        }
        SanBootView.log.info(getClass().getName(), "addSnap cmd retcode: " + addSnap.getRetCode());
        boolean isOk = finished(addSnap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "addSnap cmd errmsg: " + addSnap.getErrMsg());
        }
        return isOk;
    }

    public boolean addSnapshot1(int rootid, int snapid, int poolid, String name) {
        boolean ret = addSnap1.realDo(rootid, snapid, name, poolid);
        if (!ret) {
            this.errorMsg = addSnap1.getErrMsg();
            this.errorCode = addSnap1.getRetCode();
        }

        this.newId = addSnap1.getSnapLocalId();
        return ret;
    }

    public Snapshot getCrtSnapshot() {
        return addSnap1.getCrtSnap();
    }

    public boolean addSnapshotForCMDP(String clntIP, int port, String name, int isgrp, String desc, int mode) {
        boolean isOk = addSnapForCmdp.createVersion(clntIP, port, name, isgrp, desc, mode);
        if (!isOk) {
            if (addSnapForCmdp.getRet().equals("3")) {
                this.errorMsg = SanBootView.res.getString("common.error.crtSyncSnap");
            } else if (addSnapForCmdp.getRet().equals("5")) {
                this.errorMsg = SanBootView.res.getString("common.error.postProcess");
            } else {
                this.errorMsg = SanBootView.res.getString("common.error");
            }
            this.errorCode = addSnapForCmdp.getRetCode();
        }
        return isOk;
    }

    public boolean delSnapshot(int rootid, int snapid) {
        try {
            delSnap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_SNAP)
                    + rootid + " " + snapid);
            SanBootView.log.info(getClass().getName(), "delsnap cmd: " + delSnap.getCmdLine());
            delSnap.run();
        } catch (Exception ex) {
            recordException(delSnap, ex);
        }
        SanBootView.log.info(getClass().getName(), "delsnap cmd retcode: " + delSnap.getRetCode());
        boolean isOk = finished(delSnap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delsnap cmd errmsg: " + delSnap.getErrMsg());
        }
        return isOk;
    }

    public boolean delSnapshotForCMDP(String clntIP, int port, String name, int localSnapId, int isgrp) {
        boolean isOk = delSnapForCmdp.execCMDPcmd("del phy_snap cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_DEL_SNAP) + clntIP + "," + port + "," + name + "," + localSnapId + "," + isgrp);
        SanBootView.log.info(getClass().getName(), "del phy_snap cmd retcode: " + delSnapForCmdp.getRetCode());
        if (!isOk) {
            errorMsg = delSnapForCmdp.getErrMsg();
            errorCode = delSnapForCmdp.getRetCode();
            SanBootView.log.error(getClass().getName(), "del phy_snap cmd errmsg: " + delSnapForCmdp.getErrMsg());
        }
        return isOk;
    }

    public boolean expSnapshot(String snapName, String srcVol, float val) {
        try {
            expSnap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_EXP_SSPACE)
                    + snapName + " " + srcVol + " " + val);
            SanBootView.log.info(getClass().getName(), "expSnap cmd: " + expSnap.getCmdLine());
            expSnap.run();
        } catch (Exception ex) {
            recordException(expSnap, ex);
        }
        SanBootView.log.info(getClass().getName(), "expSnap cmd retcode: " + expSnap.getRetCode());
        boolean isOk = finished(expSnap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "expSnap cmd errmsg: " + expSnap.getErrMsg());
        }
        return isOk;
    }

    public boolean modSnapshot(int rootid, int local_id, int opened_type) {
        try {
            modSnap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_SNAP) + rootid
                    + " snap_local_snapid=" + local_id + "  snap_opened_type=" + opened_type);
            SanBootView.log.info(getClass().getName(), "modSnap cmd: " + modSnap.getCmdLine());
            modSnap.run();
        } catch (Exception ex) {
            recordException(modSnap, ex);
        }
        SanBootView.log.info(getClass().getName(), "modSnap cmd retcode: " + modSnap.getRetCode());
        boolean isOk = finished(modSnap);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "modSnap cmd errmsg: " + modSnap.getErrMsg());
        }
        return isOk;
    }

    public boolean rollSnapshotToDisk(int rootid, int local_id) {
        try {
            rolldisk.setCmdLine(
                    ResourceCenter.SNAP_DIR + "rollback  -t 2 " + rootid + " " + local_id);
            SanBootView.log.info(getClass().getName(), "rollback2disk cmd: " + rolldisk.getCmdLine());
            rolldisk.run();
        } catch (Exception ex) {
            recordException(rolldisk, ex);
        }
        SanBootView.log.info(getClass().getName(), "rollback2disk cmd retcode: " + rolldisk.getRetCode());
        boolean isOk = finished(rolldisk);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "rollback2disk cmd errmsg: " + rolldisk.getErrMsg());
        }
        return isOk;
    }

    public boolean getSnapName(String volName) {
        try {
            getSnapName.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_GET_SNAP_NAME)
                    + volName);
            SanBootView.log.info(getClass().getName(), "getSnapName cmd: " + getSnapName.getCmdLine());
            getSnapName.run();
        } catch (Exception ex) {
            recordException(getSnapName, ex);
        }
        SanBootView.log.info(getClass().getName(), "getSnapName cmd retcode: " + getSnapName.getRetCode());
        boolean isOk = finished(getSnapName);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "getSnapName cmd errmsg: " + getSnapName.getErrMsg());
        }
        return isOk;
    }

    public String getSname() {
        return getSnapName.getName();
    }

    public boolean getSnapSize(String snap_root_id, String snap_locl_snapid) {
        try {
            getSnapSize.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_SNAP_SIZE) + snap_root_id + " " + snap_locl_snapid);
            SanBootView.log.info(getClass().getName(), " get snapsize cmd: " + getSnapSize.getCmdLine());
            getSnapSize.run();
        } catch (Exception ex) {
            recordException(modSnap, ex);
        }

        SanBootView.log.info(getClass().getName(), " get snapsize retcode: " + getSnapSize.getRetCode());
        boolean isOk = finished(getSnapSize);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " get snapsize errmsg: " + getSnapSize.getErrMsg());
        }
        return isOk;
    }

    public String getSsize() {
        return getSnapSize.getSize();
    }

    ///////////////////////////////////////////////////////
    // 
    //    lunmap
    //
    ///////////////////////////////////////////////////////
    public boolean getLunMapForTID(int tid) {
        getLunMap.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_LUNMAP) + tid);
        boolean ret = getLunMap.realDo();
        if (!ret) {
            this.errorMsg = getLunMap.getErrMsg();
            this.errorCode = getLunMap.getRetCode();
        }
        return ret;
    }

    public Vector<LunMap> getAllLunMapForTid() {
        return getLunMap.getAllLMs();
    }

    public boolean delLunMap(int tid, String ip, String mask, String rwset) {
        try {
            delLM.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_LUNMAP)
                    + tid + " " + ip + " " + mask + " " + rwset);
            SanBootView.log.info(getClass().getName(), "delLM cmd: " + delLM.getCmdLine());
            delLM.run();
        } catch (Exception ex) {
            recordException(delLM, ex);
        }
        SanBootView.log.info(getClass().getName(), "delLM cmd retcode: " + delLM.getRetCode());
        boolean isOk = finished(delLM);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delLM cmd errmsg: " + delLM.getErrMsg());
        }
        return isOk;
    }

    public boolean addLunMap(int tid, String ip, String mask,
            String rwset, String suser, String spass, String cuser, String cpass) {
        try {
            addLM.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_LUNMAP)
                    + tid + " " + ip + " " + mask + " " + rwset + " " + suser + " "
                    + spass + " " + cuser + " " + cpass);
            SanBootView.log.info(getClass().getName(), "addLM cmd: " + addLM.getCmdLine());
            addLM.run();
        } catch (Exception ex) {
            recordException(addLM, ex);
        }
        SanBootView.log.info(getClass().getName(), "addLM cmd retcode: " + addLM.getRetCode());
        boolean isOk = finished(addLM);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "addLM cmd errmsg: " + addLM.getErrMsg());
        }
        return isOk;
    }

    public boolean addVg(String ip, int port, String args) {
        try {
            addVg.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_VG) + " " + ip + " " + port + " crtvg.sh " + args);
            SanBootView.log.info(getClass().getName(), "addVg cmd: " + addVg.getCmdLine());
            addVg.run();
        } catch (Exception ex) {
            recordException(addVg, ex);
        }
        SanBootView.log.info(getClass().getName(), "addVg cmd retcode: " + addVg.getRetCode());
        boolean isOk = finished(addVg);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "addVg cmd errmsg: " + addVg.getErrMsg());
        }
        return isOk;
    }

    public boolean delVg(String args) {
        try {
            delVg.setCmdLine(args);
            SanBootView.log.info(getClass().getName(), "dleVg cmd: " + delVg.getCmdLine());
            delVg.run();
        } catch (Exception ex) {
            recordException(delVg, ex);
        }
        SanBootView.log.info(getClass().getName(), "delVg cmd retcode: " + delVg.getRetCode());
        boolean isOk = finished(delVg);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delVg cmd errmsg: " + delVg.getErrMsg());
        }
        return isOk;
    }

    public boolean addLV(String ip, int port, String args) {
        try {
            addLV.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_VG) + " " + ip + " " + port + " crtlv.sh " + args);
            SanBootView.log.info(getClass().getName(), "addLV cmd: " + addLV.getCmdLine());
            addLV.run();
        } catch (Exception ex) {
            recordException(addLV, ex);
        }
        SanBootView.log.info(getClass().getName(), "addLV cmd retcode: " + addLV.getRetCode());
        boolean isOk = finished(addLV);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "addLV cmd errmsg: " + addLV.getErrMsg());
        }
        return isOk;
    }

    public boolean delLV(String args) {
        try {
            delLV.setCmdLine(args);
            SanBootView.log.info(getClass().getName(), "delLV cmd: " + delLV.getCmdLine());
            delLV.run();
        } catch (Exception ex) {
            recordException(delLV, ex);
        }
        SanBootView.log.info(getClass().getName(), "delLV cmd retcode: " + delLV.getRetCode());
        boolean isOk = finished(delLV);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delLV cmd errmsg: " + delLV.getErrMsg());
        }
        return isOk;
    }

    ///////////////////////////////////////////////////////
    // 
    //    backup and restore mdb
    //
    ///////////////////////////////////////////////////////
    public boolean backupMDB(String path) {
        bakMdb.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_BAKALL) + path);
        try {
            SanBootView.log.info(getClass().getName(), "bak mdb cmd: " + bakMdb.getCmdLine());
            bakMdb.run();
        } catch (Exception ex) {
            recordException(bakMdb, ex);
        }
        SanBootView.log.info(getClass().getName(), "bak mdb ret code: " + bakMdb.getRetCode());
        boolean isOk = finished(bakMdb);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "bak mdb ret errmsg: " + bakMdb.getErrMsg());
        }
        return isOk;
    }

    public boolean restMDB(String path) {
        rstMdb.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_RSTALL) + path);
        SanBootView.log.info(getClass().getName(), "rst mdb cmd: " + rstMdb.getCmdLine());
        try {
            rstMdb.run();
        } catch (Exception ex) {
            recordException(rstMdb, ex);
        }
        SanBootView.log.info(getClass().getName(), "rst mdb retcode: " + rstMdb.getRetCode());
        boolean isOk = finished(rstMdb);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "rst mdb errmsg: " + rstMdb.getErrMsg());


        }
        return isOk;
    }

    ///////////////////////////////////////////////////
    //
    //                  Profile
    //
    ///////////////////////////////////////////////////
    public boolean updateProfile() {
        profileList.clear();

        ArrayList<String> fileList = getProfileFile();
        int size = fileList.size();
        boolean ret = true;
        for (int i = 0; i < size; i++) {
            String file = fileList.get(i);
            UniProfile profile = new UniProfile(file);

            if (!viewFileContents(file)) {
                ret = false;
                break;
            }

            try {
                profile.parserProfile(getContentBuf());
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }

            if (profile.hasIBootSection()) {
                SanBootView.log.info(getClass().getName(), profile.getProfileName() + " is a SWU profile.");
                SanBootView.log.debug(getClass().getName(), profile.prtMe());
                profileList.add(profile);
            }
        }

        return ret;
    }

    public void removeProfileFromVector(UniProfile profile) {
        profileList.remove(profile);
    }

    public void removeProfFromCache(UniProfile profile) {
        int size = profileList.size();
        for (int i = 0; i < size; i++) {
            UniProfile one = (UniProfile) profileList.get(i);
            if (one.toString().equals(profile.toString())) {
                System.out.println(" remove profile name: " + profile.toString());
                profileList.remove(i);
                break;
            }
        }
    }

    public void addProfileToVector(UniProfile profile) {
        profileList.add(0, profile);
    }

    public ArrayList<UniProfile> getAllProfile(long bkObjId) {
        ArrayList<UniProfile> ret = new ArrayList<UniProfile>();
        int size = profileList.size();
        for (int i = 0; i < size; i++) {
            UniProfile one = profileList.get(i);
            UniProIdentity identity = one.getUniProIdentity();
            if (identity.getBkObj_ID().trim().equals(bkObjId + "")) {
                ret.add(one);
            }
        }
        return ret;
    }

    public ArrayList<UniProfile> getAllProfileOnClntID(long cid) {
        ArrayList<UniProfile> ret = new ArrayList<UniProfile>();
        int size = profileList.size();
        for (int i = 0; i < size; i++) {
            UniProfile one = profileList.get(i);
            UniProIdentity identity = one.getUniProIdentity();
            if (identity.getClntID().trim().equals(cid + "")) {
                ret.add(one);
            }
        }
        return ret;
    }

    public ArrayList<UniProfile> getAllProfileForCluster(int cluster_id) {
        ArrayList<BackupClient> d2d_members = this.getD2DMemberForCluster(cluster_id);
        int size1 = d2d_members.size();

        ArrayList<UniProfile> ret = new ArrayList<UniProfile>();
        int size = profileList.size();
        for (int i = 0; i < size; i++) {
            UniProfile one = profileList.get(i);
            UniProIdentity identity = one.getUniProIdentity();

            for (int j = 0; j < size1; j++) {
                BackupClient d2d_clnt = d2d_members.get(j);
                if (identity.getClntID().trim().equals(d2d_clnt.getID() + "")) {
                    ret.add(one);
                    break;
                }
            }
        }
        return ret;
    }

    public ArrayList<UniProfile> getAllProfile() {
        ArrayList<UniProfile> ret = new ArrayList<UniProfile>();
        int size = profileList.size();
        for (int i = 0; i < size; i++) {
            ret.add(profileList.get(i));
        }
        return ret;
    }

    public ArrayList<UniProfile> getTypeProfile(int type) {
        int fType, realType;

        ArrayList<UniProfile> ret = new ArrayList<UniProfile>();
        int size = profileList.size();
        for (int i = 0; i < size; i++) {
            UniProfile prof = profileList.get(i);
            UniProBackup bak = prof.getUniProBackup();
            try {
                fType = Integer.parseInt(bak.getSrcType());
                realType = (fType & ResourceCenter.BAK_TYPE_MASK);
                if (realType == type) {
                    ret.add(prof);
                }
            } catch (Exception ex) {
                continue;
            }
        }

        return ret;
    }

    public UniProfile getOneProfile(String name) {
        int size = profileList.size();
        for (int i = 0; i < size; i++) {
            UniProfile profile = profileList.get(i);
            if (profile.getProfileName().equals(name)) {
                return profile;
            }
        }
        return null;
    }
    String profErrMsg = "";

    public boolean checkProfile(UniProfile prof) {
        UniProIdentity identity = prof.getUniProIdentity();
        UniProBackup backup = prof.getUniProBackup();
        UniProHeader header = prof.getUniProHeader();
        UniProDrive drive = prof.getUniProDrive1();
        UniProIBoot iboot = prof.getUniProIBoot();
        String profName = prof.toString();
        profErrMsg = "";

        String bkobjId = identity.getBkObj_ID();
        BakObject bkObj = view.initor.mdb.getBakObjFromVector(bkobjId);
        if (bkObj == null) {
            SanBootView.log.error(getClass().getName(), " Missing bkobj in profile: " + profName);
            profErrMsg = SanBootView.res.getString("common.errcode.inconsistentProf") + "\n"
                    + SanBootView.res.getString("common.errcode.errProfReason") + " "
                    + SanBootView.res.getString("EditProfileDialog.error.lostBakObj");
            return false;
        }

        String bkClntId = identity.getClntID();
        BackupClient bkClnt = view.initor.mdb.getClientFromVectorOnID(bkClntId);
        if (bkClnt == null) {
            SanBootView.log.error(getClass().getName(), "Missing d2d client in profile: " + profName);
            profErrMsg = SanBootView.res.getString("common.errcode.inconsistentProf") + "\n"
                    + SanBootView.res.getString("common.errcode.errProfReason") + " "
                    + SanBootView.res.getString("common.errcode.profile.noBkClnt");
            return false;
        }

        String dest_src_map = header.getSource_dest_reference();
        if (dest_src_map.equals("")) {
            SanBootView.log.error(getClass().getName(), "Missing source_dest_reference in profile: " + profName);
            profErrMsg = SanBootView.res.getString("common.errcode.inconsistentProf") + "\n"
                    + SanBootView.res.getString("common.errcode.errProfReason") + " "
                    + SanBootView.res.getString("common.errcode.profile.noDest");
            return false;
        }

        String path = drive.getPath();
        if (path.equals("")) {
            SanBootView.log.error(getClass().getName(), "Missing path in profile: " + profName);
            profErrMsg = SanBootView.res.getString("common.errcode.inconsistentProf") + "\n"
                    + SanBootView.res.getString("common.errcode.errProfReason") + " "
                    + SanBootView.res.getString("common.errcode.profile.noDest");
            return false;
        }

        if (!bkClnt.isWin()) {
            String lvm_refer = header.getLinux_lvm_reference();
            if (lvm_refer.equals("")) {
                SanBootView.log.error(getClass().getName(), "Missing lvm_reference in profile: " + profName);
                profErrMsg = SanBootView.res.getString("common.errcode.inconsistentProf") + "\n"
                        + SanBootView.res.getString("common.errcode.errProfReason")
                        + " " + SanBootView.res.getString("common.errcode.profile.noDest");
                return false;
            }
        }

        String src = backup.getSrc();
        String exclude = backup.getExcludeFromBak();
        String iboot_aft_cmd;
        if (bkClnt.isWin()) {
            String aWindir = view.initor.mdb.getWinDir(bkClnt.getIP(), bkClnt.getPort());
            if (aWindir.equals("")) {
                SanBootView.log.error(getClass().getName(), "Not found windows dir (mdb:CheckProfile).");
                profErrMsg = SanBootView.res.getString("common.errcode.inconsistentProf") + "\n"
                        + SanBootView.res.getString("common.errcode.errProfReason") + " "
                        + SanBootView.res.getString("common.errcode.profile.notFoundWindir");
                return false;
            }

            String windir = "/C:/" + aWindir;
            if (EditProfileDialog.isOsDupForWin(src, exclude, windir)) {
                iboot_aft_cmd = iboot.getIboot_af_cmd();
                if (iboot_aft_cmd.equals("")) {
                    SanBootView.log.error(getClass().getName(), "Missing iboot_aft_cmd(win) in profile: " + profName);
                    profErrMsg = SanBootView.res.getString("common.errcode.inconsistentProf") + "\n"
                            + SanBootView.res.getString("common.errcode.errProfReason")
                            + " " + SanBootView.res.getString("common.errcode.profile.noCmd");
                    return false;
                }
            }
        } else {
            if (EditProfileDialog.isOsDupForLinux(src)) {
                iboot_aft_cmd = iboot.getIboot_af_cmd();
                if (iboot_aft_cmd.equals("")) {
                    SanBootView.log.error(getClass().getName(), "Missing iboot_aft_cmd(linux) in profile: " + profName);
                    profErrMsg = SanBootView.res.getString("common.errcode.inconsistentProf") + "\n"
                            + SanBootView.res.getString("common.errcode.errProfReason")
                            + " " + SanBootView.res.getString("common.errcode.profile.noCmd");
                    return false;
                }
            }
        }

        return true;
    }

    public String getProfErrMsg() {
        return profErrMsg;
    }

    public boolean isSameProfileName(String newName) {
        UniProfile prof;

        int size = profileList.size();
        for (int i = 0; i < size; i++) {
            prof = (UniProfile) profileList.get(i);
            System.out.println(" sched profname: " + prof.toString() + " new prof name: " + newName);

            if (prof.toString().equals(newName)) {
                return true;
            }
        }

        return false;
    }

    /////////////////////////////////////////////////
    //
    //                  Scheduler
    //
    //////////////////////////////////////////////////
    public boolean updateCronScheduler() {
        boolean ret = getSchList.updateScheduleList();
        if (!ret) {
            errorMsg = getSchList.getErrMsg();
            errorCode = getSchList.getRetCode();
        }
        return ret;
    }

    public ArrayList getSchList() {
        return getSchList.getAllSchedule();
    }

    public int getSchNum() {
        return getSchList.getSchNum();
    }

    public ArrayList<DBSchedule> getNormalSch() {
        return getSchList.getNormalBackupSch();
    }

    public ArrayList<DBSchedule> getAutoDelSch() {
        return getSchList.getAutoDelSch();
    }

    public ArrayList<DBSchedule> getSchOnProfName(String profName) {
        return getSchList.getSchOnProfName(profName);
    }

    public int getSchNumOnProfName(String profName) {
        return getSchList.getSchOnProfName(profName).size();
    }

    public void addSchIntoCache(DBSchedule sch) {
        getSchList.addScheduleToCache(sch);
    }

    public void removeSchFromCache(DBSchedule sch) {
        getSchList.removeSchedulerFromCache(sch);
    }

    public void removeSch(DBSchedule sch) {
        getSchList.removeSch(sch);
    }

    public boolean addOneScheduler(DBSchedule sch) {
        NetworkRunning running = null;

        try {
            StringBuffer buf = new StringBuffer();

            if (sch.getID() > 0) {
                buf.append(" sch_id=" + sch.getID());
            }
            buf.append(" sch_name=" + sch.getName());
            buf.append(" sch_type=" + sch.getSchType());
            buf.append(" sch_min=" + sch.getMin());
            buf.append(" sch_hour=" + sch.getHour());
            buf.append(" sch_day=" + sch.getDay());
            buf.append(" sch_month=" + sch.getMonth());
            buf.append(" sch_week=" + sch.getWeek());
            buf.append(" sch_level=" + sch.getLevel());
            buf.append(" sch_prof=" + sch.getProfId());
            buf.append(" sch_dev=" + sch.getDevId());
            buf.append(" sch_obj=" + sch.getObjId());
            buf.append(
                    " " + "\"" + "sch_profname=" + sch.getProfName() + "\"");

            if (sch.getID() > 0) {
                modSch.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_MOD_DB_SCHEDULER)
                        + buf.toString());
                SanBootView.log.info(getClass().getName(), " mod sch cmd: " + modSch.getCmdLine());
                running = modSch;
            } else {
                addSch.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_ADD_DB_SCHEDULER)
                        + buf.toString());
                SanBootView.log.info(getClass().getName(), " add sch cmd: " + addSch.getCmdLine());
                running = addSch;
            }
            running.run();
        } catch (Exception ex) {
            recordException(running, ex);
        }

        if (sch.getID() <= 0) {
            recordNewId((AddCmdNetworkRunning) running);
        }
        SanBootView.log.info(getClass().getName(), " add(mod) sch cmd retcode: " + running.getRetCode());

        boolean isOk = finished(running);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " add(mod) sch errmsg: " + running.getErrMsg());
        }
        return isOk;
    }

    public boolean deleteOneScheduler(DBSchedule sch) {
        try {
            delSch.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_DB_SCHEDULER)
                    + sch.getID());
            SanBootView.log.info(getClass().getName(), " del sch cmd: " + delSch.getCmdLine());
            delSch.run();
        } catch (Exception ex) {
            recordException(delSch, ex);
        }
        SanBootView.log.info(getClass().getName(), " del sch cmd retcode: " + delSch.getRetCode());
        boolean isOk = finished(delSch);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " del sch cmd errmsg: " + delSch.getErrMsg());
        }
        return isOk;
    }

    ///////////////////////////////////////////////////
    //
    //                Backup object 
    //
    ////////////////////////////////////////////////////
    public boolean updateBakObjList() {
        boolean ret = getBakObjList.updateBakObjList();
        if (!ret) {
            this.errorCode = getBakObjList.getRetCode();
            this.errorMsg = getBakObjList.getErrMsg();
        }
        return ret;
    }

    public ArrayList getBakObjOnClient(long cliId) {
        return getBakObjList.getBakObjectListFromVector(cliId);
    }

    public ArrayList getBakObjOnClientUidAndType(long cliId, int uid, int type) {
        return getBakObjList.getBakObjectListFromVector(cliId, uid, type);
    }

    public void addBakObjIntoVector(BakObject bakObj) {
        getBakObjList.addBakObjectToVector(bakObj);
    }

    public void modBakObjInVector(long bkobjId, String fileList, String excludeList, String filetype) {
        getBakObjList.modBakObjInVector(bkobjId, fileList, excludeList, filetype);
    }

    public BakObject getBakObjectFromVector(long cliId, String fileName) {
        return getBakObjList.getBakObjectFromVector(cliId, fileName);
    }

    public BakObject getBakObjFromVector(long bakObjId) {
        return getBakObjList.getBakObjectFromVector(bakObjId);
    }

    public BakObject getBakObjFromVector(String bkObjId) {
        return getBakObjList.getBakObjectFromVector(bkObjId);
    }

    public BakObject getBakObjFromVector(long bkObjId, int uid) {
        return getBakObjList.getBakObjectListFromVector(bkObjId, uid);
    }

    public void removeBakObjFromVector(BakObject bakObj) {
        getBakObjList.removeBakObjectFromVector(bakObj);
    }

    public void removeBakObjFromVector(long bkObjId) {
        BakObject bkobj = getBakObjList.getBakObjectFromVector(bkObjId);
        if (bkobj != null) {
            removeBakObjFromVector(bkobj);
        }
    }

    public boolean updateBakObjList(String bkObjId) {
        String cmd = ResourceCenter.getCmd(ResourceCenter.CMD_GET_ONE_BKOBJ) + bkObjId;
        boolean ret = getOneBakObjList.updateBakObjList(cmd);
        if (!ret) {
            this.errorCode = getOneBakObjList.getRetCode();
            this.errorMsg = getOneBakObjList.getErrMsg();
        }
        return ret;
    }

    public BakObject getOneBakObject() {
        return getOneBakObjList.getOneBakObj();
    }

    public boolean addBakObj(String fileName) {
        try {
            addBakObj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_BAKOBJECT)
                    + fileName);
            SanBootView.log.info(getClass().getName(), "add bakobj cmd: " + addBakObj.getCmdLine());
            addBakObj.run();
        } catch (Exception ex) {
            recordException(addBakObj, ex);
        }

        recordNewId(addBakObj);
        SanBootView.log.info(getClass().getName(), "add bakobj cmd retcode: " + addBakObj.getRetCode());
        boolean isOk = finished(addBakObj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add bakobj cmd errmsg: " + addBakObj.getErrMsg());
        }
        return isOk;
    }

    public boolean modBakObj(long bkObjId, String desc, int maxBkNum) {
        try {
            modBakObj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_BAKOBJECT)
                    + "bkobj_id=" + bkObjId
                    + " \"" + "bkobj_desc=" + desc + "\""
                    + " bkobj_max_backuplevel=" + maxBkNum);
            SanBootView.log.info(getClass().getName(), " mod bakobj cmd: " + modBakObj.getCmdLine());

            modBakObj.run();
        } catch (Exception ex) {
            recordException(modBakObj, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod bakobj cmd retcode: " + modBakObj.getRetCode());
        boolean isOk = finished(modBakObj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod bakobj cmd errmsg: " + modBakObj.getErrMsg());
        }
        return isOk;
    }

    public boolean modBakObj(long bkObjId, String bkContent, String excludeList, String filetype) {
        try {
            modBakObj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_BAKOBJECT)
                    + "bkobj_id=" + bkObjId
                    + " bkobj_filetype=" + filetype
                    + " \"" + "bkobj_filename=" + bkContent + "\""
                    + " \"" + "bkobj_exclude=" + excludeList + "\"");
            SanBootView.log.info(getClass().getName(), " mod bakobj cmd: " + modBakObj.getCmdLine());

            modBakObj.run();
        } catch (Exception ex) {
            recordException(modBakObj, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod bakobj cmd retcode: " + modBakObj.getRetCode());
        boolean isOk = finished(modBakObj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod bakobj cmd errmsg: " + modBakObj.getErrMsg());
        }
        return isOk;
    }

    public boolean modBakObj(long bkObjId, int maxBkNum) {
        try {
            modBakObj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_BAKOBJECT)
                    + "bkobj_id=" + bkObjId
                    + " bkobj_max_backuplevel=" + maxBkNum);
            SanBootView.log.info(getClass().getName(), " mod bakobj cmd: " + modBakObj.getCmdLine());

            modBakObj.run();
        } catch (Exception ex) {
            recordException(modBakObj, ex);
        }
        SanBootView.log.info(getClass().getName(), " mod bakobj cmd retcode: " + modBakObj.getRetCode());
        boolean isOk = finished(modBakObj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " mod bakobj cmd errmsg: " + modBakObj.getErrMsg());
        }
        return isOk;
    }

    public boolean deleteBakObj(long bakObjID) {
        try {
            delBakObj.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_BAKOBJECT)
                    + bakObjID);
            SanBootView.log.info(getClass().getName(), " del bakobj cmd: " + delBakObj.getCmdLine());
            delBakObj.run();
        } catch (Exception ex) {
            recordException(delBakObj, ex);
        }
        SanBootView.log.info(getClass().getName(), " del bakobj cmd retcode: " + delBakObj.getRetCode());
        boolean isOk = finished(delBakObj);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " del bakobj cmd errmsg: " + delBakObj.getErrMsg());
        }
        return isOk;
    }

    /**
     * ********* UCS IDLE DISK ********
     */
    public boolean getIdelDisk() {
        getIdleDisk.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_IDLE_DISK));
        SanBootView.log.info(getClass().getName(), "getIdelDisk cmd :" + getIdleDisk.getCmdLine());
        try {
            getIdleDisk.removeAll();
            getIdleDisk.run();
        } catch (Exception ex) {
            recordException(mntDriver, ex);
        }
        SanBootView.log.info(getClass().getName(), "getIdleDisk cmd retCode :" + getIdleDisk.getRetCode());
        boolean isOk = finished(getIdleDisk);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "getIdleDisk errorMsg :" + getIdleDisk.getErrMsg());
        }
        return isOk;
    }

    public ArrayList<IdleDisk> getAllIdleDisk() {
        return getIdleDisk.getAllIdleDisk();
    }

    /**
     * **************UCS Disk info*******************
     */
    public boolean getUcsDiskInfo(int root_id) {
        getUcsDiskInfo.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_UCS_DISK_INFO) + root_id);
        SanBootView.log.info(getClass().getName(), "getUcsDiskInfo cmd :" + getUcsDiskInfo.getCmdLine());
        try {
            getUcsDiskInfo.run();
        } catch (Exception ex) {
            recordException(mntDriver, ex);
        }
        SanBootView.log.info(getClass().getName(), "getUcsDiskInfo cmd retCode :" + getUcsDiskInfo.getRetCode());
        boolean isOk = finished(getUcsDiskInfo);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "getUcsDiskInfo errorMsg :" + getUcsDiskInfo.getErrMsg());
        }
        return isOk;
    }

    public int getUcsDiskCount(int root_id) {
        getucsdiskcount.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_UCS_COUNT) + root_id);
        SanBootView.log.info(getClass().getName(), "getUcsDiskCount cmd :" + getucsdiskcount.getCmdLine());
        try {
            getucsdiskcount.run();
        } catch (Exception ex) {
            recordException(mntDriver, ex);
        }
        SanBootView.log.info(getClass().getName(), "getUcsDiskCount cmd retCode :" + getucsdiskcount.getRetCode());
        boolean isOk = finished(getucsdiskcount);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "getucsdiskcount errorMsg :" + getucsdiskcount.getErrMsg());
        }
        return getucsdiskcount.getCount();
    }

    public int getLatestPoolId() {
        return getUcsDiskInfo.getLatestpoolid();
    }

    public int getOldestPoolId() {
        return getUcsDiskInfo.getOldestpoolid();
    }

    public int getLogPoolId() {
        return getUcsDiskInfo.getLogpoolid();
    }

    public int getLogNum() {
        return getUcsDiskInfo.getLognum();
    }

    public int getLogMaxSize() {
        return getUcsDiskInfo.getLogmaxsize();
    }

    public int getLogMaxTime() {
        return getUcsDiskInfo.getLogmaxtime();
    }

    ////////////////////////////////////////////////////
    //
    //                    Pool
    //
    /////////////////////////////////////////////////////
    public boolean updatePool() {
        boolean ret = getPool.updatePool();
        if (!ret) {
            this.errorMsg = getPool.getErrMsg();
            this.errorCode = getPool.getRetCode();
        }
        return ret;
    }

    public boolean updateRemotePool(String ip, int port) {
        getPoolofRemoteUWS.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_POOL_REMOTE_UWS) + " " + ip + " " + port
                + " \"" + "vdbmgr -pool -prt all" + "\"");
        boolean ret = getPoolofRemoteUWS.updatePool();
        if (!ret) {
            this.errorMsg = getPoolofRemoteUWS.getErrMsg();
            this.errorCode = getPoolofRemoteUWS.getRetCode();
        }
        return ret;
    }

    public boolean getPoolInfo(int poolid) {
        boolean ret = getPoolInfo.getPoolInfo(ResourceCenter.getCmd(ResourceCenter.CMD_GET_POOLINFO) + poolid);
        if (!ret) {
            this.errorMsg = getPool.getErrMsg();
            this.errorCode = getPool.getRetCode();
        }
        return ret;
    }

    public boolean getRemotePool(String ip, int port, int poolid) {
        getPoolofRemoteUWS.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_POOL_REMOTE_UWS) + " " + ip + " " + port
                + " \"" + "vdbmgr -pool -prt pool_id=" + poolid + "\"");
        boolean ret = getPoolofRemoteUWS.updatePool();
        if (!ret) {
            this.errorMsg = getPoolofRemoteUWS.getErrMsg();
            this.errorCode = getPoolofRemoteUWS.getRetCode();
        }
        return ret;
    }

    public boolean getRemotePoolInfo(String ip, int port, int poolid) {
        String cmdLine = ResourceCenter.getCmd(ResourceCenter.CMD_GET_POOLINFO_REMOTE_UWS) + " " + ip + " " + port
                + " \"" + "poolinfo " + poolid + "\"";
        boolean ret = getPoolInfoOfRemoteUWS.getPoolInfo(cmdLine);
        if (!ret) {
            this.errorMsg = getPoolInfoOfRemoteUWS.getErrMsg();
            this.errorCode = getPoolInfoOfRemoteUWS.getRetCode();
        }
        return ret;
    }

    public long getPoolTotalCap() {
        return getPoolInfo.getTotalCap();
    }

    public long getRemotePoolTotalCap() {
        return getPoolInfoOfRemoteUWS.getTotalCap();
    }

    public long getPoolAvailCap() {
        return getPoolInfo.getAvailCap();
    }

    public long getRemotePoolAvailCap() {
        return getPoolInfoOfRemoteUWS.getAvailCap();
    }

    public long getPoolVUsed() {
        return getPoolInfo.getVUsed();
    }

    public long getRemotePoolVUsed() {
        return getPoolInfoOfRemoteUWS.getVUsed();
    }

    public ArrayList getPoolList() {
        return getPool.getAllPool();
    }

    public ArrayList getNormalPoolList() {
        return getPool.getAllNormalPool();
    }

    public ArrayList getUcsPoolList() {
        return getPool.getUcsPool();
    }

    public ArrayList getUcsPoolWrapList(boolean hasSpace) {
        return getPool.getUcsPoolWrapper(hasSpace);
    }

    public ArrayList getRemotePoolList() {
        return getPoolofRemoteUWS.getAllPool();
    }

    public long parsePool() {
        long max = 0;

        ArrayList pList = getPoolList();
        int psize = pList.size();
        for (int i = 0; i < psize; i++) {
            Pool pool = (Pool) pList.get(i);
            if (getPoolInfo(pool.getPool_id())) {
                long total = this.getPoolTotalCap();
                if (total > max) {
                    max = total;
                }
            }
        }

        return max;
    }

    public ArrayList getPoolWrapList(boolean hasSpace) {
        return getPool.getAllPoolWraper(hasSpace);
    }

    public ArrayList getNormalPoolWrapList(boolean hasSpace) {
        return getPool.getAllNormalPoolWraper(hasSpace);
    }

    public ArrayList getRemotePoolWrapList(boolean hasSpace) {
        return getPoolofRemoteUWS.getAllPoolWraper(hasSpace);
    }

    public int getPoolNum() {
        return getPool.getPoolNum();
    }

    public int getRemotePoolNum() {
        return getPoolofRemoteUWS.getPoolNum();
    }

    public Pool getPool(int pid) {
        return getPool.getPoolOnID(pid);
    }

    public Pool getRemotePool(int pid) {
        return getPoolofRemoteUWS.getPoolOnID(pid);
    }

    public PoolWrapper getPoolWrapper(int pid) {
        return getPool.getPoolWrapOnID(pid);
    }

    public PoolWrapper getRemotePoolWrapper(int pid) {
        return getPoolofRemoteUWS.getPoolWrapOnID(pid);
    }

    public void addPoolIntoCache(Pool pool) {
        getPool.addPool(pool);
    }

    public void addRemotePoolIntoCache(Pool pool) {
        getPoolofRemoteUWS.addPool(pool);
    }

    public void delPoolFromCache(Pool pool) {
        getPool.removePool(pool);
    }

    public void delRemotePoolFromCache(Pool pool) {
        getPoolofRemoteUWS.removePool(pool);
    }

    public boolean getHostUUID(String ip, int port, boolean isWin) {
        return this.getHostUUID(ip, port, isWin, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean getHostUUID(String ip, int port, boolean isWin, int mode) {
        if (isWin) {
            if (this.isMTPPCmd(mode)) {
                getAgntUUID.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_UUID_WIN) + " " + ip + " " + port + " pc_gen_uuid.exe " + ResourceCenter.VERIFY_CODE + " -get");
            } else {
                getAgntUUID.setCmdLine(ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " pc_gen_uuid.exe " + ResourceCenter.VERIFY_CODE + " -get");
            }
        } else {
            getAgntUUID.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_UUID_LINUX) + " " + ip + " " + port + " pc_gen_uuid.sh " + ResourceCenter.VERIFY_CODE + " -get");
        }
        getAgntUUID.setCmdType(mode);
        boolean ret = getAgntUUID.getAgentUUID();
        if (!ret) {
            this.errorMsg = getAgntUUID.getErrMsg();
            this.errorCode = getAgntUUID.getRetCode();
        }
        return ret;
    }

    public String getUUID() {
        return getAgntUUID.getUUID();
    }

    public boolean reCrtUUID(String ip, int port, int mode, boolean isWin) {
        try {
            if (isWin) {
                if (this.isMTPPCmd(mode)) {
                    this.reCrtUUID.setCmdLine(
                            ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + ip + " " + port + " pc_gen_uuid.exe " + ResourceCenter.VERIFY_CODE + " -recreate");
                } else {
                    this.reCrtUUID.setCmdLine(
                            ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " pc_gen_uuid.exe " + ResourceCenter.VERIFY_CODE + " -recreate");
                }
            } else {
                this.reCrtUUID.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + ip + " " + port + " pc_gen_uuid.sh " + ResourceCenter.VERIFY_CODE + " -recreate");
            }

            reCrtUUID.setCmdType(mode);
            SanBootView.log.info(getClass().getName(), "re-create uuid cmd: " + reCrtUUID.getCmdLine());
            reCrtUUID.run();
        } catch (Exception ex) {
            recordException(reCrtUUID, ex);
        }
        SanBootView.log.info(getClass().getName(), "re-create uuid cmd retcode: " + reCrtUUID.getRetCode());
        boolean isOk = finished(reCrtUUID);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "re-create uuid cmd errmsg: " + reCrtUUID.getErrMsg());
        }
        return isOk;
    }

    public boolean setUUID(String ip, int port, String uuid, int mode, boolean isWin) {
        try {
            if (isWin) {
                if (this.isMTPPCmd(mode)) {
                    this.setUUID.setCmdLine(
                            ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + ip + " " + port + " pc_gen_uuid.exe " + ResourceCenter.VERIFY_CODE + " -set " + uuid);
                } else {
                    this.setUUID.setCmdLine(
                            ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " pc_gen_uuid.exe " + ResourceCenter.VERIFY_CODE + " -set " + uuid);
                }
            } else {
                this.setUUID.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + ip + " " + port + " pc_gen_uuid.sh " + ResourceCenter.VERIFY_CODE + " -set " + uuid);
            }
            setUUID.setCmdType(mode);
            SanBootView.log.info(getClass().getName(), "set uuid cmd: " + setUUID.getCmdLine());
            setUUID.run();
        } catch (Exception ex) {
            recordException(setUUID, ex);
        }
        SanBootView.log.info(getClass().getName(), "set uuid cmd retcode: " + setUUID.getRetCode());
        boolean isOk = finished(setUUID);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "set uuid cmd errmsg: " + setUUID.getErrMsg());
        }
        return isOk;
    }

    public int getTargetByLetter(String ip, int port, String letter) {
        return this.getTargetByLetter(ip, port, letter, ResourceCenter.CMD_TYPE_MTPP);
    }

    public int getTargetByLetter(String ip, int port, String letter, int mode) {
        if (this.isMTPPCmd(mode)) {
            getTgtByLetter.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_TARGET_BY_LETTER) + " " + ip + " " + port
                    + " iscsi_demo.exe GetTargetByDiskLetter  " + letter);
        } else {
            getTgtByLetter.setCmdLine(ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " iscsi_demo.exe GetTargetByDiskLetter  " + letter);
        }
        getTgtByLetter.setCmdType(mode);
        int tgtID = getTgtByLetter.getTargetIDByLetter();
        if (tgtID < 0) {
            this.errorMsg = getTgtByLetter.getErrMsg();
            this.errorCode = getTgtByLetter.getRetCode();
        }
        return tgtID;
    }

    public boolean getPXEInfoFromTFtp(String pxefile) {
        getPxeInfo.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + pxefile);
        boolean isOk = getPxeInfo.getContent();
        if (!isOk) {
            errorMsg = getPxeInfo.getErrMsg();
            errorCode = getPxeInfo.getRetCode();
        }
        return isOk;
    }

    public String replacePXEInfo(String oldMac, String newMac) {
        return getPxeInfo.replaceMAC(oldMac, newMac);
    }

    public String replaceTftpInfo(String contents, String newTftp) {
        return getPxeInfo.relaceTftp(contents, newTftp);
    }

    public boolean getIBootInfoFromTFtp(String ibootInfofile) {
        getIBootInfo.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + ibootInfofile);
        boolean isOk = getIBootInfo.getContent();
        if (!isOk) {
            errorMsg = getIBootInfo.getErrMsg();
            errorCode = getIBootInfo.getRetCode();
        }
        return isOk;
    }

    public StringBuffer getIBootInfoContents() {
        return getIBootInfo.getIBootInfoContents();
    }

    public boolean get3rdDhcpInfo(String the_3rdfile) {
        get3rdDhcpInfo.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + the_3rdfile);
        boolean isOk = get3rdDhcpInfo.realDo();
        if (!isOk) {
            errorMsg = get3rdDhcpInfo.getErrMsg();
            errorCode = get3rdDhcpInfo.getRetCode();
        }
        return isOk;
    }

    public String getIpFrom3rdDhcpInfo() {
        return get3rdDhcpInfo.getIP();
    }

    public boolean getHeartbeatDiskInfo(String conf) {
        this.getHbDskInfo.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);
        boolean isOk = getHbDskInfo.realDo();
        if (!isOk) {
            errorMsg = getHbDskInfo.getErrMsg();
            errorCode = getHbDskInfo.getRetCode();
        }
        return isOk;
    }

    public String getHeartBeatDisk() {
        return getHbDskInfo.getHbDsk();
    }

    public String getNextServerFrom3rdDhcpInfo() {
        return get3rdDhcpInfo.getNextServer();
    }

    public boolean zipORunzip(int op, String tozip, String zipfile) {
        if (op == 0) { // zip
            this.ziporunzip.setCmdLine("tar -czf " + zipfile + " " + tozip);
            SanBootView.log.info(getClass().getName(), "zip cmd: " + ziporunzip.getCmdLine());
        } else { // unzip
            this.ziporunzip.setCmdLine("tar -xzf" + zipfile);
            SanBootView.log.info(getClass().getName(), "unzip cmd: " + ziporunzip.getCmdLine());
        }

        try {
            ziporunzip.run();
        } catch (Exception ex) {
            recordException(ziporunzip, ex);
        }
        if (op == 0) {
            SanBootView.log.info(getClass().getName(), "zip cmd retcode: " + ziporunzip.getRetCode());
        } else {
            SanBootView.log.info(getClass().getName(), "unzip cmd retcode: " + ziporunzip.getRetCode());
        }
        boolean isOk = finished(ziporunzip);
        if (!isOk) {
            if (op == 0) {
                SanBootView.log.error(getClass().getName(), "zip cmd errmsg: " + ziporunzip.getErrMsg());
            } else {
                SanBootView.log.error(getClass().getName(), "unzip cmd errmsg: " + ziporunzip.getErrMsg());
            }
        }
        return isOk;
    }

    public boolean touchAFile(int dest_poolid, String dest_pool_passwd, String dest_uws_ip, int dest_uws_port, String file) {
        touchAFile.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_REMOTE_CMD) + " -p " + dest_poolid + " -P " + dest_pool_passwd
                + " " + dest_uws_ip + " " + dest_uws_port + " touchAFile " + file);
        SanBootView.log.info(getClass().getName(), "touchAFile cmd: " + touchAFile.getCmdLine());
        try {
            touchAFile.run();
        } catch (Exception ex) {
            recordException(touchAFile, ex);
        }
        SanBootView.log.info(getClass().getName(), "touchAFile cmd retcode: " + touchAFile.getRetCode());
        boolean isOk = finished(touchAFile);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "touchAFile cmd errmsg: " + touchAFile.getErrMsg());
        }
        return isOk;
    }

    public boolean getHostIDOnMac(String mac) {
        boolean isOk = this.getHostIDOnMac.realDo(mac);
        if (!isOk) {
            this.errorCode = this.getHostIDOnMac.getRetCode();
            if (errorCode == 1) { // grep 没有匹配上
                isOk = true;
            }
            this.errorMsg = this.getHostIDOnMac.getErrMsg();
        }
        return isOk;
    }

    public ArrayList<Integer> getHostIDOnMac() {
        return this.getHostIDOnMac.getHostIDList();
    }

    public boolean delTaskLogInfo(long id) {
        this.delTaskLogInfo.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_DEL_TASKLOG_INFO)
                + ResourceCenter.OUTPUT_DIR + "  " + id);

        SanBootView.log.info(getClass().getName(), "del Tasklog info cmd: " + delTaskLogInfo.getCmdLine());
        try {
            delTaskLogInfo.run();
        } catch (Exception ex) {
            recordException(delTaskLogInfo, ex);
        }
        SanBootView.log.info(getClass().getName(), "del Tasklog info cmd retcode: " + delTaskLogInfo.getRetCode());
        boolean isOk = finished(delTaskLogInfo);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del Tasklog info cmd errmsg: " + delTaskLogInfo.getErrMsg());
        }
        return isOk;
    }

    public boolean getIscsiSessionList() {
        boolean ret = getIScsiSession.updateSessionlist();
        if (!ret) {
            this.errorMsg = getIScsiSession.getErrMsg();
            this.errorCode = getIScsiSession.getRetCode();
        }
        return ret;
    }

    public boolean getUWSIf() {
        getIf.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + " /etc/sysconfig/startnet.sh");
        boolean ret = getIf.updateIflist();
        if (!ret) {
            this.errorMsg = getIf.getErrMsg();
            this.errorCode = getIf.getRetCode();
        }
        return ret;
    }

    public ArrayList getAllIf() {
        return getIf.getAll();
    }

    public IFConfObj getIF(String ifname) {
        return getIf.getFromVectorOnIfName(ifname);
    }

    public IFConfObj getFirstIF() {
        return getIf.getFirstFromCache();
    }

    public boolean getIscsiSessionIsReallyOk() {
        return getIScsiSession.getSessionListOk();
    }

    public ISCSISessionObj isExistActSessionOnTid(int tid, String ip) {
        return getIScsiSession.getFromCacheOnTid(tid, ip);
    }

    // 只用在源盘恢复向导中，其他地方没有用该方法；该命令采用“tmp file记录盘符”
    // 的方式来mount磁盘。 最早通过volinfo来mount，发现volinfo有时会变化，造成mount失败
    public boolean adjustDriver(String ip, int port, String letter) {
        return this.adjustDriver(ip, port, letter, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean adjustDriver(String ip, int port, String letter, int mode) {
        try {
            if (letter.equals("")) {
                if (this.isMTPPCmd(mode)) {
                    adjustDriver.setCmdLine(
                            ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " ib_adjust_driver.exe -f_2_d");
                } else {
                    adjustDriver.setCmdLine(
                            ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " ib_adjust_driver.exe -f_2_d");
                }
            } else {
                if (this.isMTPPCmd(mode)) {
                    adjustDriver.setCmdLine(
                            ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " ib_adjust_driver.exe -d_2_f " + letter);
                } else {
                    adjustDriver.setCmdLine(
                            ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " ib_adjust_driver.exe -d_2_f " + letter);
                }
            }
            adjustDriver.setCmdType(mode);
            SanBootView.log.info(getClass().getName(), "adjust driver cmd: " + adjustDriver.getCmdLine());
            adjustDriver.run();
        } catch (Exception ex) {
            recordException(adjustDriver, ex);
        }
        SanBootView.log.info(getClass().getName(), "adjust driver cmd retcode: " + adjustDriver.getRetCode());
        boolean isOk = finished(adjustDriver);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "adjust driver cmd errmsg: " + adjustDriver.getErrMsg());
        }
        return isOk;
    }

    public boolean delAdjustDriverTmpFile(String ip, int port, String letter) {
        return this.delAdjustDriverTmpFile(ip, port, letter, ResourceCenter.CMD_TYPE_MTPP);
    }

    public boolean delAdjustDriverTmpFile(String ip, int port, String letter, int mode) {
        try {
            if (this.isMTPPCmd(mode)) {
                adjustDriver.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " ib_adjust_driver.exe -delete_tmp_file " + letter);
            } else {
                adjustDriver.setCmdLine(
                        ResourceCenter.getCmdpS2A_CmdPath(ip, port) + " ib_adjust_driver.exe -delete_tmp_file " + letter);
            }
            adjustDriver.setCmdType(mode);
            SanBootView.log.info(getClass().getName(), "del adjust-driver-tmp-file cmd: " + adjustDriver.getCmdLine());
            adjustDriver.run();
        } catch (Exception ex) {
            recordException(adjustDriver, ex);
        }
        SanBootView.log.info(getClass().getName(), "del adjust-driver-tmp-file cmd retcode: " + adjustDriver.getRetCode());
        boolean isOk = finished(adjustDriver);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del adjust-driver-tmp-file cmd errmsg: " + adjustDriver.getErrMsg());
        }
        return isOk;
    }

    public boolean listProfile() {
        listProf.setCmdLine(
                ResourceCenter.BIN_DIR + "listprofile");
        boolean ret = listProf.listProfile();
        if (!ret) {
            errorMsg = listProf.getErrMsg();
            errorCode = listProf.getRetCode();
        }
        return ret;
    }

    public ArrayList getProfileNameList() {
        return listProf.getFileList();
    }

    ////////////////////////////////////////////////////
    //
    //                    orphvol
    //
    /////////////////////////////////////////////////////
    public boolean updateOrphanVol() {
        this.getorphanvol.setAddCacheFlag(true);
        this.getorphanvol.setAddTableFlag(false);
        this.getorphanvol.setAddTreeFlag(false);
        this.getorphanvol.setFilterFlag(false);

        boolean ret = getorphanvol.realDo();
        if (!ret) {
            this.errorMsg = getorphanvol.getErrMsg();
            this.errorCode = getorphanvol.getRetCode();
        }
        return ret;
    }

    public Volume getVolume(int rootid) {
        return getorphanvol.getVolume(rootid);
    }

    public boolean getAllView(int rootid) {
        this.getView.setAddCacheFlag(true);
        this.getView.setAddTableFlag(false);
        this.getView.setAddTreeFlag(false);
        this.getView.setRootId(rootid);
        this.getView.setParentId(-1);

        getView.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_VIEW));
        boolean ret = getView.realDo();
        if (!ret) {
            this.errorMsg = getView.getErrMsg();
            this.errorCode = getView.getRetCode();
        }
        return ret;
    }

    public ArrayList getViewList() {
        return getView.getAllViewList();
    }

    public boolean addView(String timestamp, String viewName, int rootid, int snapid) {
        boolean ret = addView.realDo(rootid, snapid, viewName, timestamp);
        if (!ret) {
            this.errorMsg = addView.getErrMsg();
            this.errorCode = addView.getRetCode();
        }

        this.newId = addView.getTargetID();
        return ret;
    }

    public boolean addView(String viewName, int rootid, int snapid) {
        boolean ret = addView.realDo(rootid, snapid, viewName);
        if (!ret) {
            this.errorMsg = addView.getErrMsg();
            this.errorCode = addView.getRetCode();
        }

        this.newId = addView.getTargetID();
        return ret;
    }

    public boolean addView(String viewName, int rootid, int snapid, int poolid) {
        boolean ret = addView.realDo(poolid, rootid, snapid, viewName);
        if (!ret) {
            this.errorMsg = addView.getErrMsg();
            this.errorCode = addView.getRetCode();
        }

        this.newId = addView.getTargetID();
        return ret;
    }

    public int getRootIDFromView() {
        return addView.getRootID();
    }

    public View getCrtView() {
        return addView.getCrtView();
    }

    /**
     * ******************** UCS        *********************
     */
    public boolean forwordViewByTime(int rootid, int localId, String timestamp) {
        boolean ret = ucsForwordView.realDo(rootid, localId, timestamp);
        if (!ret) {
            this.errorMsg = ucsForwordView.getErrMsg();
            this.errorCode = ucsForwordView.getRetCode();
        }
        return ret;
    }

    public boolean forwordViewByIO(int rootid, int localId, int IOnum) {
        boolean ret = ucsForwordView.realDo(rootid, localId, IOnum);
        if (!ret) {
            this.errorMsg = ucsForwordView.getErrMsg();
            this.errorCode = ucsForwordView.getRetCode();
        }
        return ret;
    }

    public View getForwordView() {
        return ucsForwordView.getCrtView();
    }

    public boolean delView(View vol) {
        try {
            delView.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_VIEW)
                    + vol.getSnap_root_id() + " " + vol.getSnap_local_snapid());
            SanBootView.log.info(getClass().getName(), "delView cmd: " + delView.getCmdLine());
            delView.run();
        } catch (Exception ex) {
            recordException(delView, ex);
        }
        SanBootView.log.info(getClass().getName(), "delView cmd retcode: " + delView.getRetCode());
        boolean isOk = finished(delView);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delView cmd errmsg: " + delView.getErrMsg());
        }
        return isOk;
    }

    public boolean crtResVol(String name, long size) {
        crtResVol.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CRT_RESVOL) + size + " bkop " + name + " -share=no");
        boolean isOk = crtResVol.realDo();
        if (!isOk) {
            this.errorMsg = getOsVolTid.getErrMsg();
            this.errorCode = getOsVolTid.getRetCode();
        }
        return isOk;
    }

    public boolean delResVol(String shname) {
        try {
            delResVol.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_RESVOL)
                    + shname);
            SanBootView.log.info(getClass().getName(), "delResVol cmd: " + delResVol.getCmdLine());
            delResVol.run();
        } catch (Exception ex) {
            recordException(delResVol, ex);
        }
        SanBootView.log.info(getClass().getName(), "delResVol cmd retcode: " + delResVol.getRetCode());
        boolean isOk = finished(delResVol);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delResVol cmd errmsg: " + delResVol.getErrMsg());
        }
        return isOk;
    }

    public boolean crtUcsPool(String name, String dev) {
        crtUcsPool.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CRT_UCS_POOL) + name + " 0 " + dev);
        boolean isOk = crtUcsPool.realDo();
        if (!isOk) {
            this.errorMsg = crtUcsPool.getErrMsg();
            this.errorCode = crtUcsPool.getRetCode();
        }
        return isOk;
    }

    public boolean expandVG(String dev) {
        try {
            expandVG.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_EXPANDVG) + " vg " + dev);
            SanBootView.log.info(getClass().getName(), "expandVG cmd: " + expandVG.getCmdLine());
            expandVG.run();
        } catch (Exception ex) {
            recordException(expandVG, ex);
        }
        SanBootView.log.info(getClass().getName(), "expandVG cmd retcode: " + expandVG.getRetCode());
        boolean isOk = finished(expandVG);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "expandVG cmd errmsg: " + expandVG.getErrMsg());
        }
        return isOk;
    }

    public boolean delUcsPool(String ucsname) {
        try {
            delUcsPool.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_UCS_POOL)
                    + ucsname);
            SanBootView.log.info(getClass().getName(), "delUcsPool cmd: " + delUcsPool.getCmdLine());
            delUcsPool.run();
        } catch (Exception ex) {
            recordException(delUcsPool, ex);
        }
        SanBootView.log.info(getClass().getName(), "delUcsPool cmd retcode: " + delUcsPool.getRetCode());
        boolean isOk = finished(delUcsPool);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delUcsPool cmd errmsg: " + delUcsPool.getErrMsg());
        }
        return isOk;
    }

    public String getResDevName() {
        return crtResVol.getDevName();
    }

    public String getResMp() {
        return crtResVol.getMp();
    }

    public String getShname() {
        return crtResVol.getShname();
    }

    public boolean getOsVolTargetIDOfIBoot(String cltIP) {
        boolean isOk = getOsVolTid.realDo(cltIP);
        if (!isOk) {
            this.errorMsg = getOsVolTid.getErrMsg();
            this.errorCode = getOsVolTid.getRetCode();
        }
        return isOk;
    }

    public int getOsVolTid() {
        return getOsVolTid.getTargetID();
    }

    public boolean getShareName() {
        boolean isOk = getShareName.realDo();
        if (!isOk) {
            this.errorMsg = getShareName.getErrMsg();
            this.errorCode = getShareName.getRetCode();
        }
        return isOk;
    }

    public ArrayList getSharenameList() {
        return getShareName.getShNameList();
    }

    public BindOfFSAndDevNo getMp(String ip, int port, String src) {
        anlyMp.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " anly_mp.sh -d " + src);
        BindOfFSAndDevNo ret = anlyMp.getMpFromDir();
        this.errorCode = anlyMp.getRetCode();
        this.errorMsg = anlyMp.getErrMsg();
        return ret;
    }

    public boolean getBriefVDiskList(String cmdLine) {
        getBrVdisk.setCmdLine(cmdLine);
        boolean isOk = getBrVdisk.realDo();
        if (!isOk) {
            this.errorMsg = getBrVdisk.getErrMsg();
            this.errorCode = getBrVdisk.getRetCode();
        }
        return isOk;
    }

    public ArrayList getBriefVDiskList() {
        return getBrVdisk.getAllDiskList();
    }

    public ArrayList getBriefVDiskList1() {
        return getBrVdisk.getAllDiskList1();
    }

    public ArrayList getLimitedDiskList(int done_snap_id) {
        return getBrVdisk.getLimitedDiskList(done_snap_id);
    }

    public boolean getLicense() {
        boolean isOk = getLic.getLicence();
        if (!isOk) {
            this.errorMsg = getLic.getErrMsg();
            this.errorCode = getLic.getRetCode();
        }
        return isOk;
    }

    public boolean getPesudoLicense() {
        this.getLic.setCmdLine("cat /tmp/lic");
        boolean isOk = getLic.getLicence();
        if (!isOk) {
            this.errorMsg = getLic.getErrMsg();
            this.errorCode = getLic.getRetCode();
        }
        return isOk;
    }

    public int getSnapInLicOfMTPP() {
        return getLic.getMTPPSnapInLic();
    }

    public int getPointOfMTPP() {
        return getLic.getMTPPPointInLic();
    }

    public boolean isSupportMTPP() {
        return getLic.isSupportMTPP();
    }

    public int getSnapInLicOfCMDP() {
        return getLic.getCMDPSnapInLic();
    }

    public int getPointOfCMDP() {
        return getLic.getCMDPPointInLic();
    }

    public boolean isSupportCMDP() {
        return getLic.isSupportCMDP();
    }

    public boolean addPool(Pool pool) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append(" pool_name=" + pool.getPool_name());
            buf.append(
                    " " + "\"" + "pool_path=" + pool.getPool_path() + "\"");
            buf.append(
                    " " + "\"" + "pool_vol_name=" + pool.getPool_vol_name() + "\"");
            buf.append(
                    " " + "\"" + "pool_passwd=" + pool.getPool_passwd() + "\"");
            buf.append(
                    " pool_flag=" + pool.getPoolFlag());
            buf.append(
                    " pool_melt_move_data=" + pool.getPoolMeltMoveData());
            buf.append(" pool_type=" + pool.getPool_Type());

            addPool.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_POOL)
                    + buf.toString());
            SanBootView.log.info(getClass().getName(), " add pool map cmd: " + addPool.getCmdLine());
            addPool.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            recordException(addPool, ex);
        }
        SanBootView.log.info(getClass().getName(), " add pool cmd retcode: " + addPool.getRetCode());
        recordNewId(addPool);
        boolean isOk = finished(addPool);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " add pool cmd errmsg: " + addPool.getErrMsg());
        }
        return isOk;
    }

    public boolean delPool(int id) {
        try {
            delPool.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_POOL) + id);
            SanBootView.log.info(getClass().getName(), "delPool cmd: " + delPool.getCmdLine());
            delPool.run();
        } catch (Exception ex) {
            recordException(delPool, ex);
        }
        SanBootView.log.info(getClass().getName(), "delPool cmd retcode: " + delPool.getRetCode());
        boolean isOk = finished(delPool);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delPool cmd errmsg: " + delPool.getErrMsg());
        }
        return isOk;
    }

    ///////////////////////////////////////////////////
    //
    //                  Task list
    //
    ///////////////////////////////////////////////////
    public boolean updateTaskList() {
        boolean ret = getTaskList.updateTaskList();
        if (!ret) {
            errorMsg = getTaskList.getErrMsg();
            errorCode = getTaskList.getRetCode();
        }
        return ret;
    }

    public ArrayList<BakTask> getAllTaskList(int mode) {
        return getTaskList.getAllTaskList(mode);
    }

    public void removeOneTask(BakTask one) {
        getTaskList.removeTask(one);
    }

    public boolean deleteTask(long task_id) {
        try {
            delTask.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_TASK)
                    + task_id);
            SanBootView.log.info(getClass().getName(), " delete task from MDB cmd: " + delTask.getCmdLine());
            delTask.run();
        } catch (Exception ex) {
            recordException(delTask, ex);
        }
        SanBootView.log.info(getClass().getName(), " delete task from MDB retcode: " + delTask.getRetCode());
        boolean isOk = finished(delTask);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " delete task from MDB cmd errmsg: " + delTask.getErrMsg());
        }
        return isOk;
    }

    public boolean killTask(String cid, long jid) {
        try {
            killTask.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_KILL_TASK)
                    + "-cid=" + cid + " -jid=" + jid);
            SanBootView.log.info(getClass().getName(), " kill task cmd: " + killTask.getCmdLine());
            killTask.run();
        } catch (Exception ex) {
            recordException(killTask, ex);
        }
        SanBootView.log.info(getClass().getName(), " kill task cmd retcode: " + killTask.getRetCode());

        boolean isOk = finished(killTask);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " kill task cmd errmsg: " + killTask.getErrMsg());
        }
        return isOk;
    }

    public boolean activeTask(int pid) {
        try {
            activeTask.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ACTIVE_TASK) + pid);
            SanBootView.log.info(getClass().getName(), "Active task(reload mg config) cmd: " + activeTask.getCmdLine());

            activeTask.run();
        } catch (Exception ex) {
            recordException(activeTask, ex);
        }
        SanBootView.log.info(getClass().getName(), "Active task(reload mg config) cmd retcode: " + activeTask.getRetCode());
        boolean isOk = finished(activeTask);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "Active task(reload mg config) cmd errmsg: " + activeTask.getErrMsg());
        }
        return isOk;
    }

    public boolean removeTaskLog(long task_id) {
        try {
            delTaskLog.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_TSKLOG)
                    + task_id);
            SanBootView.log.info(getClass().getName(), " del task log cmd: " + delTaskLog.getCmdLine());
            delTaskLog.run();
        } catch (Exception ex) {
            recordException(delTaskLog, ex);
        }
        SanBootView.log.info(getClass().getName(), " del task log cmd retcode: " + delTaskLog.getRetCode());
        boolean isOk = finished(delTaskLog);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " del task log cmd errmsg: " + delTaskLog.getErrMsg());
        }
        return isOk;
    }

    public boolean removeAllTskLog() {
        try {
            delAllTskLog.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_ALLLOG));
            SanBootView.log.info(getClass().getName(), " del all task log cmd: " + delAllTskLog.getCmdLine());
            delAllTskLog.run();
        } catch (Exception ex) {
            recordException(delAllTskLog, ex);
        }
        SanBootView.log.info(getClass().getName(), " del all task log cmd retcode: " + delAllTskLog.getRetCode());
        boolean isOk = finished(delAllTskLog);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " del all task log cmd errmsg: " + delAllTskLog.getErrMsg());
        }
        return isOk;
    }

    // audit
    public boolean addAudit(Audit audit) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" audit_user_id=" + audit.getUid());
            buf.append(" audit_time=" + audit.getEventtime());
            buf.append(" audit_clnt_id=" + audit.getCID());
            buf.append(" audit_event_id=" + audit.getEID());
            buf.append(
                    "  \"" + "audit_event_desc=" + audit.getEventDesc() + "\"");

            addAudit.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_AUDIT) + buf.toString());
            SanBootView.log.info(getClass().getName(), " add audit log cmd: " + addAudit.getCmdLine());
            addAudit.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            recordException(addAudit, ex);
        }
        SanBootView.log.info(getClass().getName(), " add audit log retcode: " + addAudit.getRetCode());
        recordNewId(addAudit);
        boolean isOk = finished(addAudit);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " add audit log errmsg: " + addAudit.getErrMsg());
        }
        return isOk;
    }

    public boolean removeAuditLog(long audit_id) {
        try {
            delAudit.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_AUDIT)
                    + audit_id);
            SanBootView.log.info(getClass().getName(), " del audit log cmd: " + delAudit.getCmdLine());
            delAudit.run();
        } catch (Exception ex) {
            recordException(delAudit, ex);
        }
        SanBootView.log.info(getClass().getName(), " del audit log cmd retcode: " + delAudit.getRetCode());
        boolean isOk = finished(delAudit);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " del audit log cmd errmsg: " + delAudit.getErrMsg());
        }
        return isOk;
    }

    public boolean removeAllAuditLog() {
        try {
            delAllAudit.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_ALLAUDIT));
            SanBootView.log.info(getClass().getName(), " del all audit log cmd: " + delAllAudit.getCmdLine());
            delAllAudit.run();
        } catch (Exception ex) {
            recordException(delAllAudit, ex);
        }
        SanBootView.log.info(getClass().getName(), " del all audit log cmd retcode: " + delAllAudit.getRetCode());
        boolean isOk = finished(delAllAudit);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " del all audit log cmd errmsg: " + delAllAudit.getErrMsg());
        }
        return isOk;
    }

    public boolean removeAllMjobLog() {
        try {
            delAllMjob.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_FILE) + ResourceCenter.MJOB_DIR + "mjob.log");
            SanBootView.log.info(getClass().getName(), " del all mjob log cmd: " + delAllMjob.getCmdLine());
            delAllMjob.run();
        } catch (Exception ex) {
            recordException(delAllMjob, ex);
        }
        SanBootView.log.info(getClass().getName(), " del all mjob log cmd retcode: " + delAllMjob.getRetCode());
        boolean isOk = finished(delAllAudit);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " del all mjob log cmd errmsg: " + delAllMjob.getErrMsg());
        }
        return isOk;
    }

    public boolean modBakSet(long bakSetId) {
        try {
            modBakset.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_BAKSET)
                    + bakSetId + " bks_status=ABORT");
            SanBootView.log.info(getClass().getName(), " modify bkst cmd: " + modBakset.getCmdLine());
            modBakset.run();
        } catch (Exception ex) {
            recordException(modBakset, ex);
        }
        SanBootView.log.info(getClass().getName(), " modify bkst cmd retcode: " + modBakset.getRetCode());
        boolean isOk = finished(modBakset);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " modify bkst cmd errMsg: " + modBakset.getErrMsg());
        }
        return isOk;
    }

    /////////////////////////////////////////////////////////////////////
    //
    //            Cluster
    //
    /////////////////////////////////////////////////////////////////////
    public boolean updateCluster() {
        boolean ret = this.getCluster.updateCluster();
        if (!ret) {
            errorMsg = getCluster.getErrMsg();
            errorCode = getCluster.getRetCode();
        }
        return ret;
    }

    public void addClusterToCache(Cluster one) {
        getCluster.addClusterToVector(one);
    }

    public void removeClusterFromVector(Cluster one) {
        getCluster.removeClusterFromVector(one);
    }

    public ArrayList<Cluster> getAllCluster() {
        return this.getCluster.getAllCluster();
    }

    public Cluster getClusterOnID(int cluster_id) {
        return this.getCluster.getClusterFromVectorOnID(cluster_id);
    }

    public Cluster getOneCluster(String member_uuid) {
        BootHost host = this.getHostFromCacheOnUUID(member_uuid);
        if (host != null) {
            return this.getClusterOnID(host.getClnt_cluster_id());
        } else {
            return null;
        }
    }

    public ArrayList getClusterUUID(int cluster_id) {
        ArrayList<String> ret = new ArrayList<String>();
        ArrayList<BootHost> hosts = this.getMemberForCluster(cluster_id);
        int size = hosts.size();
        for (int i = 0; i < size; i++) {
            BootHost host = hosts.get(i);
            if (!host.getUUID().equals("")) {
                ret.add(host.getUUID());
            }
        }
        return ret;
    }

    public boolean isWinCluster(int cluster_id) {
        ArrayList<BootHost> hosts = this.getMemberForCluster(cluster_id);
        if (hosts.size() > 0) {
            return hosts.get(0).isWinHost();
        } else {
            return false;
        }
    }

    public void assembleCluster() {
        int i, j, l, size, size1, size2;

        ArrayList<Cluster> list = this.getAllCluster();
        size = list.size();
        for (i = 0; i < size; i++) {
            Cluster clus = list.get(i);
            clus.removeAllSubCluster();
            ArrayList<BootHost> members = this.getMemberForCluster(clus.getCluster_id());
            size1 = members.size();
            for (j = 0; j < size1; j++) {
                BootHost member = members.get(j);
                BackupClient bkClnt = this.getClientFromVector(member.getClnt_d2d_cid());
                Vector<VolumeMap> vols = this.getVolMapOnClntID(member.getID());
                SubCluster subc = new SubCluster();
                subc.setHost(member);
                subc.setD2d_host(bkClnt);
                size2 = vols.size();
                for (l = 0; l < size2; l++) {
                    VolumeMap vol = vols.elementAt(l);
                    ClusterVolume cv = new ClusterVolume(vol);
                    subc.addDisk(cv);
                }
                clus.addSubCluster(subc);
            }
        }
    }

    public boolean addOneCluster(Cluster cluster) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" cluster_name=" + cluster.getCluster_name());
            buf.append(" cluster_inited_flag=" + cluster.getCluster_inited_flag());
            buf.append(" cluster_auto_dr_flag=" + cluster.getCluster_auto_dr_flag());
            buf.append(" cluster_auto_reboot_flag=" + cluster.getCluster_auto_reboot_flag());
            buf.append(" cluster_stop_base_service=" + cluster.getCluster_stop_base_service());
            buf.append(" cluster_mac_address=" + cluster.getCluster_mac_address());
            buf.append(" cluster_boot_mode=" + cluster.getCluster_boot_mode());
            buf.append(" cluster_protect_type=" + cluster.getCluster_protect_type());
            buf.append(" cluster_type=" + cluster.getCluster_type());

            addCluster.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_ADD_CLUSTER)
                    + buf.toString());

            SanBootView.log.info(getClass().getName(), "add cluster: " + addCluster.getCmdLine());

            addCluster.run();
        } catch (Exception ex) {
            recordException(addCluster, ex);
        }

        recordNewId(addCluster);
        SanBootView.log.info(getClass().getName(), "add cluster retcode: " + addCluster.getRetCode());

        boolean isOk = finished(addCluster);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add cluster errmsg: " + addCluster.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneCluster1(int cluster_id, int initFlag, int auto_dr, int auto_reboot, int stop_allserv, String mac, int bootMode) {
        try {
            StringBuffer buf = new StringBuffer();

            buf.append(" cluster_id=" + cluster_id);
            buf.append(" cluster_inited_flag=" + initFlag);
            buf.append(" cluster_auto_dr_flag=" + auto_dr);
            buf.append(" cluster_auto_reboot_flag=" + auto_reboot);
            buf.append(" cluster_stop_base_service=" + stop_allserv);
            buf.append(" cluster_mac_address=" + mac);
            buf.append(" cluster_boot_mode=" + bootMode);

            modCluster.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_CLUSTER)
                    + buf.toString());

            SanBootView.log.info(getClass().getName(), "mod one cluster: " + modCluster.getCmdLine());

            modCluster.run();
        } catch (Exception ex) {
            recordException(modCluster, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod one cluster retcode: " + modCluster.getRetCode());
        boolean isOk = finished(modCluster);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod one cluster errmsg: " + modCluster.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneCluster2(int cluster_id, int stop_allserv) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append(" cluster_id=" + cluster_id);
            SanBootView.log.info(getClass().getName(), "stop_allserv: " + stop_allserv);
            buf.append(" cluster_stop_base_service=" + stop_allserv);

            modCluster.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_CLUSTER)
                    + buf.toString());

            SanBootView.log.info(getClass().getName(), "mod one cluster: " + modCluster.getCmdLine());

            modCluster.run();
        } catch (Exception ex) {
            recordException(modCluster, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod one cluster retcode: " + modCluster.getRetCode());
        boolean isOk = finished(modCluster);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod one cluster errmsg: " + modCluster.getErrMsg());
        }
        return isOk;
    }

    public boolean modOneCluster2(int cluster_id, String name) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append(" cluster_id=" + cluster_id);
            buf.append(" cluster_name=" + name);

            modCluster.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_MOD_CLUSTER)
                    + buf.toString());

            SanBootView.log.info(getClass().getName(), "mod one cluster: " + modCluster.getCmdLine());

            modCluster.run();
        } catch (Exception ex) {
            recordException(modCluster, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod one cluster retcode: " + modCluster.getRetCode());
        boolean isOk = finished(modCluster);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod one cluster errmsg: " + modCluster.getErrMsg());
        }
        return isOk;
    }

    public boolean delCluster(Cluster cluster) {
        try {
            delCluster.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_DEL_CLUSTER)
                    + cluster.getCluster_id());
            SanBootView.log.info(getClass().getName(), " delete cluster cmd: " + delCluster.getCmdLine());
            delCluster.run();
        } catch (Exception ex) {
            recordException(delCluster, ex);
        }
        SanBootView.log.info(getClass().getName(), " delete cluster cmd retcode: " + delCluster.getRetCode());
        boolean isOk = finished(delCluster);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " delete cluster cmd errmsg: " + delCluster.getErrMsg());
        }
        return isOk;
    }

    /////////////////////////////////////////////////////////////////////////////
    //
    //                   common functions in ManageDB
    //
    /////////////////////////////////////////////////////////////////////////////
    public void sendNetbootInfoToDestUWS(int rootid, BootHost host) {
        String uuid, dest_uws_ip, dest_pool_pass;
        int dest_uws_port, dest_poolid;
        int mg_id = -1;
        int src_host_id;
        boolean isOk;

        MirrorGrp mg = this.getMGFromVectorOnRootID(rootid);
        if (mg != null) {
            mg_id = mg.getMg_id();
        } else {
            SanBootView.log.error(getClass().getName(), "Not found mg related rootid: " + rootid + " for host: " + host.getIP());
        }

        if (mg_id != -1) {
            uuid = host.getUUID();
            src_host_id = host.getID();
            ArrayList mjList = view.initor.mdb.getMjListFromVecOnMgID(mg_id);
            int size = mjList.size();
            for (int i = 0; i < size; i++) {
                MirrorJob mj = (MirrorJob) mjList.get(i);
                dest_uws_ip = mj.getMj_dest_ip();
                dest_uws_port = mj.getMj_dest_port();
                dest_poolid = mj.getMj_dest_pool();
                dest_pool_pass = mj.getMj_dest_pool_passwd();
                isOk = this.querySrcAgntOnDestUWSrv(dest_uws_ip, dest_uws_port, uuid);
                if (isOk) {
                    SourceAgent srcAgnt = this.isThisSrcAgntOnDestUWSrv(uuid);
                    if (srcAgnt != null) {
                        this.sendWinNetBootInfoToDestUWS(src_host_id, dest_uws_ip, dest_uws_port, dest_poolid, dest_pool_pass, srcAgnt.getSrc_agnt_id());
                    }
                }
            }
        }
    }

    //////////////////////////////////////////////
    //
    //         Physical protect command
    //
    //////////////////////////////////////////////
    public boolean updateDg() {
        boolean ret = this.getDriveGrp.realDo();
        if (!ret) {
            errorMsg = getNBH.getErrMsg();
            errorCode = getNBH.getRetCode();
        }
        return ret;
    }

    public ArrayList<DriveGroup> getAllDG() {
        return getDriveGrp.getDgList();
    }

    public DriveGroup findDgOnRootId(int rootid) {
        return getDriveGrp.findDgOnRootId(rootid);
    }

    public DriveGroup findDgOnDgName(String dgname) {
        return getDriveGrp.findDgOnDgName(dgname);
    }

    public PPProfile hasThisPPProfile(String dgName) {
        int size = this.ppprofileList.size();
        for (int i = 0; i < size; i++) {
            PPProfile ppprof = ppprofileList.get(i);
            if (ppprof.getDriveGrpName().equals(dgName)) {
                return ppprof;
            }
        }
        return null;
    }

    public void prtAllPPProfile() {
        int size = ppprofileList.size();
        for (int i = 0; i < size; i++) {
            ppprofileList.get(i).PrtMe();
        }
    }

    public boolean isBelongedToSameDg(int clntId, String label1, String label2) {
        PPProfile prof1 = this.getBelongedDg(clntId, label1);
        PPProfile prof2 = this.getBelongedDg(clntId, label2);
        if ((prof1 != null) && (prof2 != null)) {
            return prof1.getDiskList().equals(prof2.getDiskList());
        } else {
            return false;
        }
    }

    public boolean isBelongedToSameDg1(int cluster_id, String label1, String label2) {
        PPProfile prof1 = this.getBelongedDg1(cluster_id, label1);
        PPProfile prof2 = this.getBelongedDg1(cluster_id, label2);
        if ((prof1 != null) && (prof2 != null)) {
            return prof1.getDiskList().equals(prof2.getDiskList());
        } else {
            return false;
        }
    }

    public ArrayList<PPProfile> getPPProfile(long clntId) {
        int size = ppprofileList.size();
        ArrayList<PPProfile> ret = new ArrayList<PPProfile>();
        for (int i = 0; i < size; i++) {
            PPProfile prof = ppprofileList.get(i);
            if (prof.getHostID() == clntId) {
                ret.add(prof);
            }
        }
        return ret;
    }

    public ArrayList<PPProfile> clonePPProfile(long clntId) {
        int size = ppprofileList.size();
        ArrayList<PPProfile> ret = new ArrayList<PPProfile>();
        for (int i = 0; i < size; i++) {
            PPProfile prof = ppprofileList.get(i);
            if (prof.getHostID() == clntId) {
                PPProfile newProf = prof.cloneMyself();
                ret.add(newProf);
            }
        }
        return ret;
    }

    public ArrayList<PPProfile> getAllPPProfileForCluster(int cluster_id) {
        int size = ppprofileList.size();
        ArrayList<PPProfile> ret = new ArrayList<PPProfile>();
        for (int i = 0; i < size; i++) {
            PPProfile prof = ppprofileList.get(i);
            BootHost member = view.initor.mdb.getHostFromVectorOnID(prof.getHostID());
            if (member == null) {
                continue;
            }

//            if( member.getClnt_cluster_id() == cluster_id ){
            ret.add(prof);
//            }
        }
        return ret;
    }

    public ArrayList<PPProfile> clonePPProfileForCluster(int cluster_id) {
        int size = ppprofileList.size();
        ArrayList<PPProfile> ret = new ArrayList<PPProfile>();
        for (int i = 0; i < size; i++) {
            PPProfile prof = ppprofileList.get(i);
            BootHost member = view.initor.mdb.getHostFromVectorOnID(prof.getHostID());
            if (member == null) {
                continue;
            }

            if (member.getClnt_cluster_id() == cluster_id) {
                PPProfile newProf = prof.cloneMyself();
                newProf.setHost_ip(member.getIP());
                newProf.setHost_port(member.getPort());
                newProf.setHost_id(member.getID());
                newProf.setHost_uuid(member.getUUID());
                ret.add(newProf);
            }
        }
        return ret;
    }

    public ArrayList<PPProfileItem> getPPProfileItem(long clntId) {
        int size = ppprofileList.size();
        ArrayList<PPProfileItem> ret = new ArrayList<PPProfileItem>();
        for (int i = 0; i < size; i++) {
            PPProfile prof = ppprofileList.get(i);
            if (prof.getHostID() == clntId) {
                ArrayList<PPProfileItem> ele = prof.getElements();
                int size1 = ele.size();
                for (int j = 0; j < size1; j++) {
                    ret.add(ele.get(j));
                }
            }
        }
        return ret;
    }

    public ArrayList<PPProfileItem> getPPProfileItem1(int cluster_id) {
        ArrayList<PPProfile> pppList = this.getAllPPProfileForCluster(cluster_id);
        int size = pppList.size();
        ArrayList<PPProfileItem> ret = new ArrayList<PPProfileItem>();
        for (int i = 0; i < size; i++) {
            PPProfile prof = pppList.get(i);
            ArrayList<PPProfileItem> ele = prof.getElements();
            int size1 = ele.size();
            for (int j = 0; j < size1; j++) {
                ret.add(ele.get(j));
            }
        }
        return ret;
    }

    // 根据diskList来找出对应的PPProfile
    public PPProfile getPPProfile(long clntID, String diskList) {
        ArrayList<PPProfile> profList = this.getPPProfile(clntID);
        int size = profList.size();
        for (int i = 0; i < size; i++) {
            PPProfile prof = profList.get(i);
            if (prof.getDiskList().equals(diskList)) {
                return prof;
            }
        }
        return null;
    }

    // 根据diskList来找出对应的PPProfile
    public PPProfile getPPProfile1(int cluster_id, String diskList) {
        ArrayList<PPProfile> profList = this.getAllPPProfileForCluster(cluster_id);
        int size = profList.size();
        for (int i = 0; i < size; i++) {
            PPProfile prof = profList.get(i);
            if (prof.getDiskList().equals(diskList)) {
                return prof;
            }
        }
        return null;
    }

    public PPProfile getBelongedDg1(int cluster_id, String diskLabel) {
        ArrayList<PPProfile> profList = this.getAllPPProfileForCluster(cluster_id);
        int size = profList.size();
        for (int i = 0; i < size; i++) {
            PPProfile prof = profList.get(i);
            if (prof.isBelongedToThisDG(diskLabel)) {
                return prof;
            }
        }
        return null;
    }

    public PPProfile getBelongedDg(long clntID, String diskLabel) {
        ArrayList<PPProfile> profList = this.getPPProfile(clntID);
        int size = profList.size();
        for (int i = 0; i < size; i++) {
            PPProfile prof = profList.get(i);
            if (prof.isBelongedToThisDG(diskLabel)) {
                return prof;
            }
        }
        return null;
    }

    public boolean reloadMgConfig(long clntID) {
        boolean ok = true;

        ArrayList<PPProfile> profList = this.getPPProfile(clntID);
        int size = profList.size();
        for (int i = 0; i < size; i++) {
            PPProfile prof = profList.get(i);
            ArrayList<PPProfileItem> disks = prof.getElements();
            int size1 = disks.size();
            for (int j = 0; j < size1; j++) {
                PPProfileItem item = disks.get(j);
                if (!sendSigToMg(item.getMg().getMg_id(), MirrorGrp.SIG_SIGHUP)) {
                    ok = false;
                }
                /*
                 * if( item.getMg().getMg_pid() > 0 ){ if(
                 * !view.initor.mdb.activeTask( item.getMg().getMg_pid() ) ){ ok
                 * = false; } }
                 */
            }
        }

        return ok;
    }

    public boolean reloadMgConfig(int cluster_id) {
        boolean ok = true;

        ArrayList<PPProfile> profList = this.getAllPPProfileForCluster(cluster_id);
        int size = profList.size();
        for (int i = 0; i < size; i++) {
            PPProfile prof = profList.get(i);
            ArrayList<PPProfileItem> disks = prof.getElements();
            int size1 = disks.size();
            for (int j = 0; j < size1; j++) {
                PPProfileItem item = disks.get(j);
                if (!sendSigToMg(item.getMg().getMg_id(), MirrorGrp.SIG_SIGHUP)) {
                    ok = false;
                }
                /*
                 * if( item.getMg().getMg_pid() > 0 ){ if(
                 * !view.initor.mdb.activeTask( item.getMg().getMg_pid() ) ){ ok
                 * = false; } }
                 */
            }
        }

        return ok;
    }

    public boolean updatePPProfileList() {
        int i, j, size, size1, rootid;
        Vector volMapList;
        VolumeMap volMap;
        MirrorGrp mg;
        DriveGroup dg;
        PPProfile ppprof;
        PPProfileItem item;
        boolean ret = true;

        ppprofileList.clear();

        Vector hostList = this.getAllBootHost();
        size = hostList.size();
        for (i = 0; i < size; i++) {
            BootHost host = (BootHost) hostList.elementAt(i);
            if (host.isMTPPProtect()) {
                continue;
            }

            volMapList = this.getVolMapOnClntIDAndPType(host.getID(), BootHost.PROTECT_TYPE_CMDP);
            size1 = volMapList.size();
            for (j = 0; j < size1; j++) {
                volMap = (VolumeMap) volMapList.elementAt(j);

                rootid = volMap.getVol_rootid();
                mg = this.getMGFromVectorOnRootID(rootid);
                if (mg == null) {
                    SanBootView.log.error(getClass().getName(), "this mg of the vol is missing, the vol'rootid: " + rootid);
                    //ret = false;
                    continue;
                }

                if (volMap.isBelongDG()) {
                    dg = this.findDgOnRootId(rootid);
                    if (dg != null) { // 这说明volmap属于主盘
                        volMap.setDBType(volMap.parseDBType());
                        SanBootView.log.info(getClass().getName(), "db type: " + volMap.getDBType() + " for " + volMap.getVolName());

                        ppprof = this.hasThisPPProfile(dg.dg_name);
                        if (ppprof == null) {
                            ppprof = new PPProfile(dg.dg_name, rootid);
                            ppprof.setHost_uuid(host.getUUID());
                            ppprof.setHost_id(host.getID());
                            ppprof.setHost_ip(host.getIP());
                            ppprof.setHost_port(host.getPort());
                            item = new PPProfileItem(mg, volMap);
                            ppprof.addItem(item);

                            this.ppprofileList.add(ppprof);
                        } else {
                            ppprof.setMasterDiskRootid(rootid);
                            item = new PPProfileItem(mg, volMap);
                            ppprof.addItem(item);
                        }

                    } else { // 这说明volmap属于从盘
                        ppprof = this.hasThisPPProfile(volMap.getGroupname());
                        if (ppprof == null) {
                            ppprof = new PPProfile(volMap.getGroupname(), -1);
                            ppprof.setHost_uuid(host.getUUID());
                            ppprof.setHost_id(host.getID());
                            ppprof.setHost_ip(host.getIP());
                            ppprof.setHost_port(host.getPort());
                            item = new PPProfileItem(mg, volMap);
                            ppprof.addItem(item);

                            this.ppprofileList.add(ppprof);
                        } else {
                            item = new PPProfileItem(mg, volMap);
                            ppprof.addItem(item);
                        }
                    }
                } else { // 该主机卷不属于任何磁盘组
                    volMap.setDBType(volMap.parseDBType());
                    SanBootView.log.info(getClass().getName(), "db type: " + volMap.getDBType() + " for " + volMap.getVolName());

                    item = new PPProfileItem(mg, volMap);
                    ppprof = new PPProfile();
                    ppprof.setHost_uuid(host.getUUID());
                    ppprof.setHost_id(host.getID());
                    ppprof.setHost_ip(host.getIP());
                    ppprof.setHost_port(host.getPort());
                    ppprof.addItem(item);

                    this.ppprofileList.add(ppprof);
                }
            }
        }

        return ret;
    }

    // 这个命令被废弃。因为可以直接分析volmap.vol_info来得到db type (2010.11.4)
    public boolean getDatabaseInfoOnVol(String volname) {
        boolean ret = this.getDbInfo.realDo(volname);
        if (!ret) {
            errorMsg = getDbInfo.getErrMsg();
            errorCode = getDbInfo.getRetCode();
        }
        return ret;
    }

    public int getDBtype() {
        return getDbInfo.getDBType();
    }

    public boolean crtDG(String dgname, String rootids) {
        try {
            this.crtDG.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_CRT_DRIVEGRP) + dgname + "," + rootids);
            SanBootView.log.info(getClass().getName(), "crtDG cmd: " + crtDG.getCmdLine());
            crtDG.run();
        } catch (Exception ex) {
            recordException(crtDG, ex);
        }
        SanBootView.log.info(getClass().getName(), "crtDG cmd retcode: " + crtDG.getRetCode());
        boolean isOk = finished(crtDG);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "crtDG cmd errmsg: " + crtDG.getErrMsg());
        }
        return isOk;
    }

    public boolean delDG(String dgname, String master_disk_rootid, String master_disk_grpdetail) {
        try {
            this.delDG.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_DEL_DRIVEGRP) + dgname + "," + master_disk_rootid + "," + master_disk_grpdetail);
            SanBootView.log.info(getClass().getName(), "delDG cmd: " + delDG.getCmdLine());
            delDG.run();
        } catch (Exception ex) {
            recordException(delDG, ex);
        }
        SanBootView.log.info(getClass().getName(), "delDG cmd retcode: " + delDG.getRetCode());
        boolean isOk = finished(delDG);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delDG cmd errmsg: " + delDG.getErrMsg());
        }
        return isOk;
    }

    public boolean addDiskIntoDG(String dgname, String rootid) {
        try {
            this.addDiskIntoDG.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_ADD_DISK_INTO_DG) + dgname + "," + rootid);
            SanBootView.log.info(getClass().getName(), "add disk into dg cmd: " + addDiskIntoDG.getCmdLine());
            addDiskIntoDG.run();
        } catch (Exception ex) {
            recordException(addDiskIntoDG, ex);
        }
        SanBootView.log.info(getClass().getName(), "add disk into dg cmd retcode: " + addDiskIntoDG.getRetCode());
        boolean isOk = finished(addDiskIntoDG);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "add disk into dg cmd errmsg: " + addDiskIntoDG.getErrMsg());
        }
        return isOk;
    }

    public boolean delDiskFromDG(String volname, String rootid, String grpinfo, String grpdetail, String dgname) {
        try {
            this.delDiskFromDG.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_DEL_DISK_FROM_DG) + "\"" + volname + "," + rootid + "," + grpinfo + "," + grpdetail + "," + dgname + "\"");
            SanBootView.log.info(getClass().getName(), "del disk from DG cmd: " + delDiskFromDG.getCmdLine());
            delDiskFromDG.run();
        } catch (Exception ex) {
            recordException(delDiskFromDG, ex);
        }
        SanBootView.log.info(getClass().getName(), "del disk from DG cmd retcode: " + delDiskFromDG.getRetCode());
        boolean isOk = finished(delDiskFromDG);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "del disk from DG cmd errmsg: " + delDiskFromDG.getErrMsg());
        }
        return isOk;
    }

    public boolean modAutoCrtSnapParameter(int mg_id, int sch_type, int interval_time, int max_snap, int min_inc_data) {
        try {
            this.modAutoCrtSnapParameter.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_MOD_MG) + mg_id
                    + " mg_schedule_type=" + sch_type + " mg_interval_time=" + interval_time
                    + " mg_max_snapshot=" + max_snap + " mg_min_snap_size=" + min_inc_data);
            SanBootView.log.info(getClass().getName(), "mod auto-crt-snap parameter cmd: " + modAutoCrtSnapParameter.getCmdLine());
            modAutoCrtSnapParameter.run();
        } catch (Exception ex) {
            recordException(modAutoCrtSnapParameter, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod auto-crt-snap parameter cmd retcode: " + modAutoCrtSnapParameter.getRetCode());
        boolean isOk = finished(modAutoCrtSnapParameter);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod auto-crt-snap parameter cmd errmsg: " + modAutoCrtSnapParameter.getErrMsg());
        }
        return isOk;
    }

    public boolean modAutoCrtSnapParameter1(int mg_id, int sch_type, String sch_minute, String sch_hour, String sch_day,
            String sch_month, String sch_week, String sch_clock_zone, String sch_hour1, String clock_set, int max_snap, int min_inc_data) {
        try {
            this.modAutoCrtSnapParameter.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_MOD_MG) + mg_id
                    + " mg_schedule_type=" + sch_type + " mg_schedule_minute=" + sch_minute
                    + " mg_schedule_hour=" + sch_hour + " mg_schedule_day=" + sch_day
                    + " mg_schedule_month=" + sch_month + " mg_schedule_week=" + sch_week
                    + " mg_schedule_clock_zone=" + sch_clock_zone + " mg_schedule_hour1=" + sch_hour1
                    + " mg_schedule_clock_set=" + clock_set
                    + " mg_max_snapshot=" + max_snap + " mg_min_snap_size=" + min_inc_data);
            SanBootView.log.info(getClass().getName(), "mod auto-crt-snap parameter cmd: " + modAutoCrtSnapParameter.getCmdLine());
            modAutoCrtSnapParameter.run();
        } catch (Exception ex) {
            recordException(modAutoCrtSnapParameter, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod auto-crt-snap parameter cmd retcode: " + modAutoCrtSnapParameter.getRetCode());
        boolean isOk = finished(modAutoCrtSnapParameter);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod auto-crt-snap parameter cmd errmsg: " + modAutoCrtSnapParameter.getErrMsg());
        }
        return isOk;
    }

    public boolean modAutoCrtSnapParameter2(int mg_id, int sch_type, int interval_time) {
        try {
            this.modAutoCrtSnapParameter.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_MOD_MG) + mg_id
                    + " mg_schedule_type=" + sch_type + " mg_interval_time=" + interval_time
                    + " mg_src_ip=   mg_src_disk_uuid=  ");
            SanBootView.log.info(getClass().getName(), "mod auto-crt-snap parameter cmd: " + modAutoCrtSnapParameter.getCmdLine());
            modAutoCrtSnapParameter.run();
        } catch (Exception ex) {
            recordException(modAutoCrtSnapParameter, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod auto-crt-snap parameter cmd retcode: " + modAutoCrtSnapParameter.getRetCode());
        boolean isOk = finished(modAutoCrtSnapParameter);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod auto-crt-snap parameter cmd errmsg: " + modAutoCrtSnapParameter.getErrMsg());
        }
        return isOk;
    }

    public boolean modAutoCrtSnapParameter3(int mg_id, int max_snap, int min_inc_data) {
        try {
            this.modAutoCrtSnapParameter.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_MOD_MG) + mg_id
                    + " mg_max_snapshot=" + max_snap + " mg_min_snap_size=" + min_inc_data);
            SanBootView.log.info(getClass().getName(), "mod auto-crt-snap parameter cmd: " + modAutoCrtSnapParameter.getCmdLine());
            modAutoCrtSnapParameter.run();
        } catch (Exception ex) {
            recordException(modAutoCrtSnapParameter, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod auto-crt-snap parameter cmd retcode: " + modAutoCrtSnapParameter.getRetCode());
        boolean isOk = finished(modAutoCrtSnapParameter);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod auto-crt-snap parameter cmd errmsg: " + modAutoCrtSnapParameter.getErrMsg());
        }
        return isOk;
    }

    public boolean modServiceOnVolume(String vol_name, String serviceStr) {
        try {
            this.modServicesOnVol.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_MOD_SERVICES) + vol_name + " changever_services=" + serviceStr);
            SanBootView.log.info(getClass().getName(), "mod services-on-vol cmd: " + modServicesOnVol.getCmdLine());
            modServicesOnVol.run();
        } catch (Exception ex) {
            recordException(modServicesOnVol, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod services-on-vol cmd retcode: " + modServicesOnVol.getRetCode());
        boolean isOk = finished(modServicesOnVol);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod services-on-vol cmd errmsg: " + modServicesOnVol.getErrMsg());
        }
        return isOk;
    }

    public boolean modDbinfoOnVolume(String master_disk_vol_name, int dbtype, String instances) {
        try {
            this.modDbinfoOnVol.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_SET_DBINFO) + master_disk_vol_name + ",0," + dbtype + "," + instances);
            SanBootView.log.info(getClass().getName(), "mod db-info-on-vol cmd: " + modDbinfoOnVol.getCmdLine());
            modDbinfoOnVol.run();
        } catch (Exception ex) {
            recordException(modDbinfoOnVol, ex);
        }
        SanBootView.log.info(getClass().getName(), "mod db-info-on-vol cmd retcode: " + modDbinfoOnVol.getRetCode());
        boolean isOk = finished(modDbinfoOnVol);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "mod db-info-on-vol cmd errmsg: " + modDbinfoOnVol.getErrMsg());
        }
        return isOk;
    }

    public boolean getCurrentSyncState1(String clntIP, int port, String uuid, String letter) {
        boolean ret = this.getCurrentSyncState.updateBuildProgress(clntIP, port, uuid, letter);
        if (!ret) {
            errorMsg = getCurrentSyncState.getErrMsg();
            errorCode = getCurrentSyncState.getRetCode();
        }
        return ret;
    }

    public boolean getCurrentSyncState(String clntIP, int port, String volname, String letter) {
        boolean ret = this.getCurrentSyncState.updateInitProgress(clntIP, port, volname, letter);
        if (!ret) {
            errorMsg = getCurrentSyncState.getErrMsg();
            errorCode = getCurrentSyncState.getRetCode();
        }
        return ret;
    }

    public boolean getCurrentSyncState(String clntIP, int port, String volname) {
        boolean ret = this.getCurrentSyncState.updateInitProgress(clntIP, port, volname, "");
        if (!ret) {
            errorMsg = getCurrentSyncState.getErrMsg();
            errorCode = getCurrentSyncState.getRetCode();
        }
        return ret;
    }

    public boolean currentStateIsConnectError() {
        return this.getCurrentSyncState.isNotConnectToHost();
    }

    public boolean currentStateIsSync() {
        return this.getCurrentSyncState.isSync();
    }

    public boolean currentStateIsUnknown() {
        return this.getCurrentSyncState.isUnknow();
    }

    public boolean currentStateIsAsync() {
        return this.getCurrentSyncState.isSync();
    }

    public boolean currentStateIsAsync2Sync() {
        return this.getCurrentSyncState.isAsync2Sync();
    }

    public boolean currentStateIsInit() {
        return this.getCurrentSyncState.isIniting();
    }

    public boolean currentStateIsTimeout() {
        return this.getCurrentSyncState.isTimeout();
    }

    public String getCurrentState() {
        return this.getCurrentSyncState.getInitRecord().getState();
    }

    public InitProgressRecord getCurRec() {
        return this.getCurrentSyncState.getInitRecord();
    }

    public boolean getWorkStateOfSync(String volInfo) {
        this.getWorkStateOfSync.execCMDPcmd("get workstate of sync cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_GET_CURSTATE) + volInfo);
        boolean isOk = !this.getWorkStateOfSync.isNull(); // 判断成功与否的条件很特殊
        SanBootView.log.info(getClass().getName(), "get workstate of sync cmd retcode: " + getWorkStateOfSync.getRetCode());
        if (!isOk) {
            errorMsg = getWorkStateOfSync.getErrMsg();
            errorCode = getWorkStateOfSync.getRetCode();
            SanBootView.log.error(getClass().getName(), "get workstate of sync cmd errmsg: " + getWorkStateOfSync.getErrMsg());
        }
        return isOk;
    }

    public boolean modVolInfo(String volInfo, int dbtype) {
        boolean isOk = modVolInfo.execCMDPcmd("mod vol info cmd: ", ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_MOD_VOLINFO) + volInfo + "," + dbtype);
        SanBootView.log.info(getClass().getName(), "mod vol info befor cmd: " + modVolInfo.getRetCode());
        if (!isOk) {
            errorMsg = modVolInfo.getErrMsg();
            errorCode = modVolInfo.getRetCode();
            SanBootView.log.error(getClass().getName(), "mod vol info after cmd: " + modVolInfo.getErrMsg());
        }
        return isOk;
    }

    public String getVolInfo() {
        return String.valueOf(modVolInfo.getRetCode());
    }

    public boolean workStateIsASync() {
        return !this.getWorkStateOfSync.isEqZero();
    }

    public boolean setWorkStateOfSync(String volName, String volInfo, String stateVal) {
        boolean isOk = setWorkStateOfSync.execCMDPcmd("set workstate of sync cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_MOD_WORKSTATE) + volName + "," + volInfo + "," + stateVal);
        SanBootView.log.info(getClass().getName(), "set workstate of sync cmd retcode: " + setWorkStateOfSync.getRetCode());
        if (!isOk) {
            errorMsg = setWorkStateOfSync.getErrMsg();
            errorCode = setWorkStateOfSync.getRetCode();
            SanBootView.log.error(getClass().getName(), "set workstate of sync cmd errmsg: " + setWorkStateOfSync.getErrMsg());
        }
        return isOk;
    }

    public boolean setWorkStateIsOk() {
        return this.setWorkStateOfSync.isEqZero();
    }

    public boolean setCurrentStateOfSync(String clntIP, int port, String drive, String syncType) {
        boolean isOk = this.setCurrentStateOfSync.execCMDPcmd("set currentstate of sync cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_MOD_CURSTATE) + clntIP + "," + port + "," + drive + "," + syncType + ",aaaa");
        SanBootView.log.info(getClass().getName(), "set currentstate of sync cmd retcode: " + setCurrentStateOfSync.getRetCode());
        if (!isOk) {
            errorMsg = setCurrentStateOfSync.getErrMsg();
            errorCode = setCurrentStateOfSync.getRetCode();
            SanBootView.log.error(getClass().getName(), "set workstate of sync cmd errmsg: " + setCurrentStateOfSync.getErrMsg());
        }
        return isOk;
    }

    public boolean setCurrentStateIsOk() {
        return this.setCurrentStateOfSync.isEqZero();
    }

    public boolean getHAInfo(String clntIP, int port, String letter) {
        this.getHAInfo.execCMDPcmd1("get HA info cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_GET_HAINFO) + clntIP + "," + port + "," + letter + ",drive");
        // callfun在界面上的输出和返回值是一样的，所以只能这样判断:
        // 1.  输出为空肯定是错的
        // 2.  errorCode为0或1，也表示成功；其他值表示错误
        int aErrCode = getHAInfo.getRetCode();
        //boolean isOk = !this.getHAInfo.isNull() || ( errorCode == 0 ) || ( errorCode == 1 );
        boolean isOk = !this.getHAInfo.isNull() || (aErrCode == 0) || (aErrCode == 1) || (aErrCode == 2) || (aErrCode == 255);
        SanBootView.log.info(getClass().getName(), "get HA info cmd retcode: " + getHAInfo.getRetCode());
        if (!isOk) {
            errorMsg = getHAInfo.getErrMsg();
            errorCode = getHAInfo.getRetCode();
            SanBootView.log.error(getClass().getName(), "get HA info cmd errmsg: " + getHAInfo.getErrMsg());
        }
        return isOk;
    }

    public boolean isConnectErrorWhenGetHaInfo() {
        return this.getHAInfo.isNotConnectToHost();
    }

    public boolean isSupportHA() {
        // return !this.getHAInfo.isEqZero() || ( this.getHAInfo.getRetCode() == 1 );

        // 发现有时会返回-1,从clw的web界面上看，-1表示“不支持双机”（ 2010.11.15 ）
        // 所以只有为1或2时才认为“支持双机”，否则就是“不支持双机” （ 2010.11.15 ）
        return (this.getHAInfo.getRetCode() == 1) || (this.getHAInfo.getRetCode() == 2);
    }

    public boolean setHAInfo(String clntIP, int port, String drive, String supportHA) {
        boolean isOk = this.setHAInfo.execCMDPcmd("set HA info cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_SET_HAINFO) + clntIP + "," + port + "," + drive + ",drive," + supportHA);
        SanBootView.log.info(getClass().getName(), "set HA info cmd retcode: " + setHAInfo.getRetCode());
        if (!isOk) {
            errorMsg = setHAInfo.getErrMsg();
            errorCode = setHAInfo.getRetCode();
            SanBootView.log.error(getClass().getName(), "set HA info cmd errmsg: " + setHAInfo.getErrMsg());
        }
        return isOk;
    }

    public boolean setHAInfoOk() {
        return setHAInfo.isEqZero();
    }

    public boolean buildMirroring(String clntIP, int port, String letter,
            String interval_time, String min_size, int blk_size, int max_snap,
            String desc, String poolName, int worktype, String pub_ip1, String pub_ip2, String src_type) {
        this.buildMirror.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        boolean ret = this.buildMirror.buildMirror(clntIP, port, letter, interval_time, min_size,
                blk_size, max_snap, desc, poolName, worktype, pub_ip1, pub_ip2, src_type);
        if (!ret) {
            errorMsg = buildMirror.getErrMsg();
            errorCode = buildMirror.getRetCode();
        }
        return ret;
    }

    /**
     * ############################## AMS-BEGIN
     * #######################################################
     */
    public boolean getCurrentAmsSyncState(String clntIP, int port, String mount) {
        boolean ret = this.getCurrentAmsSyncState.updateInitProgressByMount(clntIP, port, mount);
        if (!ret) {
            errorMsg = getCurrentAmsSyncState.getErrMsg();
            errorCode = getCurrentAmsSyncState.getRetCode();
        }
        return ret;
    }

    public String getCurrentAmsState(String clntIP, int port, String mount) {
        return getCurrentAmsSyncState.getInitRecord().getState();
    }

    public boolean workAmsStateIsASync(String clntIP, int port, String mount) {
        boolean ret = this.getCurrentAmsSyncState.updateInitProgressByMount(clntIP, port, mount);
        if (!ret) {
            errorMsg = getCurrentAmsSyncState.getErrMsg();
            errorCode = getCurrentAmsSyncState.getRetCode();
            return ret;
        }
        return this.getCurrentAmsSyncState.isASync();
    }

//    public boolean currentAmsStateIsConnectError(){
//        return this.getCurrentAmsSyncState.isNotConnectToHost( String clntIP,int port,String mount );
//    }
    public boolean workAmsStateIsSync(String clntIP, int port, String mount) {
        boolean ret = this.getCurrentAmsSyncState.updateInitProgressByMount(clntIP, port, mount);
        if (!ret) {
            errorMsg = getCurrentAmsSyncState.getErrMsg();
            errorCode = getCurrentAmsSyncState.getRetCode();
            return ret;
        }
        return this.getCurrentAmsSyncState.isSync();
    }

    public boolean workAmsStateIsFalse(String clntIP, int port, String mount) {
        boolean ret = this.getCurrentAmsSyncState.updateInitProgressByMount(clntIP, port, mount);
        if (!ret) {
            errorMsg = getCurrentAmsSyncState.getErrMsg();
            errorCode = getCurrentAmsSyncState.getRetCode();
            return ret;
        }
        return this.getCurrentAmsSyncState.isFalse();
    }

    public boolean amsBuildMirroring(String clntIP, int port, String args) {
        try {
            amsbuildMirror.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + clntIP + " " + port + " ams_protected.sh " + args);
            amsbuildMirror.buildMirror();
        } catch (Exception ex) {
            recordException(amsbuildMirror, ex);
        }
        boolean isOk = finished(amsbuildMirror);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "ams build cmd errmsg: " + amsbuildMirror.getErrMsg());
        }
        return isOk;
    }

    public boolean lvmCmd(String clntIP, int port, String cmd) {
        try {
            lvmCmd.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + clntIP + " " + port + " " + cmd);
            lvmCmd.cmd();
        } catch (Exception ex) {
            recordException(lvmCmd, ex);
        }
        boolean isOk = finished(lvmCmd);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "lvm cmd errmsg: " + lvmCmd.getErrMsg());
        }
        return isOk;
    }

    /**
     * 删除ams主机时调用相关脚本来重新挂载源盘
     */
    public boolean delAmsProtect(String clntIP, int port, String args) {
        try {
            delAmsProtect.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + clntIP + " " + port + " ams_del_protected.sh " + args);
            SanBootView.log.info(getClass().getName(), "del ams cmd: " + delAmsProtect.getCmdLine());
            delAmsProtect.run();
        } catch (Exception ex) {
            recordException(delAmsProtect, ex);
        }
        SanBootView.log.info(getClass().getName(), "del ams cmd retcode: " + delAmsProtect.getRetCode());
        boolean isOk = finished(delAmsProtect);
        if (!isOk) {
            errorMsg = getWorkStateOfSync.getErrMsg();
            errorCode = getWorkStateOfSync.getRetCode();
            SanBootView.log.error(getClass().getName(), "delams cmd errmsg: " + delAmsProtect.getErrMsg());
        }
        return isOk;
    }

    public boolean setAmsWorkState(String clntIP, int port, String args) {
        try {
            setAmsWorkState.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + clntIP + " " + port + " ams_stat_set.sh " + args);
            SanBootView.log.info(getClass().getName(), "set ams stat cmd: " + setAmsWorkState.getCmdLine());
            setAmsWorkState.run();
        } catch (Exception ex) {
            recordException(setAmsWorkState, ex);
        }
        SanBootView.log.info(getClass().getName(), "set ams stat cmd: " + setAmsWorkState.getRetCode());
        boolean isOk = finished(setAmsWorkState);
        if (!isOk) {
            errorMsg = setAmsWorkState.getErrMsg();
            errorCode = setAmsWorkState.getRetCode();
            SanBootView.log.error(getClass().getName(), "set ams stat errmsg: " + setAmsWorkState.getErrMsg());
        }
        return isOk;
    }

    public boolean amsSwitchNetDisk(String clntIP, int port, String args) {
        try {
            amsSwitchNetDisk.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + clntIP + " " + port + " ams_switch_netdisk.sh " + args);
            SanBootView.log.info(getClass().getName(), "ams switch to netdisk cmd: " + amsSwitchNetDisk.getCmdLine());
            amsSwitchNetDisk.run();
        } catch (Exception ex) {
            recordException(amsSwitchNetDisk, ex);
        }
        SanBootView.log.info(getClass().getName(), "ams switch to netdisk cmd: " + amsSwitchNetDisk.getRetCode());
        boolean isOk = finished(amsSwitchNetDisk);
        if (!isOk) {
            errorMsg = amsSwitchNetDisk.getErrMsg();
            errorCode = amsSwitchNetDisk.getRetCode();
            SanBootView.log.error(getClass().getName(), "ams switch to netdisk errmsg: " + amsSwitchNetDisk.getErrMsg());
        }
        return isOk;
    }

    public boolean amsSwitchLocal(String clntIP, int port, String args) {
        try {
            amsSwitchLocal.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + clntIP + " " + port + " ams_switch_local.sh " + args);
            SanBootView.log.info(getClass().getName(), "ams switch to local cmd: " + amsSwitchLocal.getCmdLine());
            amsSwitchLocal.run();
        } catch (Exception ex) {
            recordException(amsSwitchLocal, ex);
        }
        SanBootView.log.info(getClass().getName(), "ams switch to local cmd: " + amsSwitchLocal.getRetCode());
        boolean isOk = finished(amsSwitchLocal);
        if (!isOk) {
            errorMsg = amsSwitchLocal.getErrMsg();
            errorCode = amsSwitchLocal.getRetCode();
            SanBootView.log.error(getClass().getName(), "ams switch to local errmsg: " + amsSwitchLocal.getErrMsg());
        }
        return isOk;
    }

    public boolean amsparted(String clntIP, int port, String args) {
        try {
            amsparted.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + clntIP + " " + port + " ams_parted.sh " + args);
            SanBootView.log.info(getClass().getName(), "ams amsparted cmd: " + amsparted.getCmdLine());
            amsparted.run();
        } catch (Exception ex) {
            recordException(amsparted, ex);
        }
        SanBootView.log.info(getClass().getName(), "ams amsparted cmd: " + amsparted.getRetCode());
        boolean isOk = finished(amsparted);
        if (!isOk) {
            errorMsg = amsparted.getErrMsg();
            errorCode = amsparted.getRetCode();
            SanBootView.log.error(getClass().getName(), "ams amsparted errmsg: " + amsparted.getErrMsg());
        }
        return isOk;
    }

    public boolean amsRebuildMirror(String clntIP, int port, String args) {
        try {
            amsRebuildMirror.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + clntIP + " " + port + " ams_rebuild.sh " + args);
            SanBootView.log.info(getClass().getName(), "ams amsRebuildMirror cmd: " + amsRebuildMirror.getCmdLine());
            amsRebuildMirror.run();
        } catch (Exception ex) {
            recordException(amsRebuildMirror, ex);
        }
        SanBootView.log.info(getClass().getName(), "ams amsRebuildMirror cmd: " + amsRebuildMirror.getRetCode());
        boolean isOk = finished(amsRebuildMirror);
        if (!isOk) {
            errorMsg = amsRebuildMirror.getErrMsg();
            errorCode = amsRebuildMirror.getRetCode();
            SanBootView.log.error(getClass().getName(), "ams amsRebuildMirror errmsg: " + amsRebuildMirror.getErrMsg());
        }
        return isOk;
    }

    public boolean restoreNetDisk(String clntIP, int port, String args) {
        try {
            restoreNetDisk.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + clntIP + " " + port + " ams_restore_netdisk.sh " + args);
            SanBootView.log.info(getClass().getName(), "ams restoreNetDisk to netdisk cmd: " + restoreNetDisk.getCmdLine());
            restoreNetDisk.run();
        } catch (Exception ex) {
            recordException(restoreNetDisk, ex);
        }
        SanBootView.log.info(getClass().getName(), "ams restoreNetDisk to netdisk cmd: " + restoreNetDisk.getRetCode());
        boolean isOk = finished(restoreNetDisk);
        if (!isOk) {
            errorMsg = restoreNetDisk.getErrMsg();
            errorCode = restoreNetDisk.getRetCode();
            SanBootView.log.error(getClass().getName(), "ams restoreNetDisk to netdisk errmsg: " + restoreNetDisk.getErrMsg());
        }
        return isOk;
    }

    /**
     * 通过targetID取得被保护disk的disk_path路径
     */
    public boolean amsGetDiskPath(String clntIP, int port, String args) {
        try {
            amsGetDiskPath.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + clntIP + " " + port + " ams_protected_disk_devpath.sh " + args);
            SanBootView.log.info(getClass().getName(), "ams switch to netdisk cmd: " + amsGetDiskPath.getCmdLine());
            amsGetDiskPath.buildMirror();
        } catch (Exception ex) {
            recordException(amsGetDiskPath, ex);
        }
        SanBootView.log.info(getClass().getName(), "ams switch to netdisk cmd: " + amsGetDiskPath.getRetCode());
        boolean isOk = finished(amsGetDiskPath);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "am amsGetDiskPath: " + amsGetDiskPath.getErrMsg());
        }
        return isOk;
    }

    public String getDiskPath() {
        return this.amsGetDiskPath.getDiskpath();
    }

    /**
     * 取得targetid对应的disk分区，/dev/xxx1
     */
    public boolean amsGetRealDiskPath(String clntIP, int port, String args) {
        try {
            amsGetRealDiskPath.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + " " + clntIP + " " + port + " ams_getnetdev_by_targetid.sh " + args);
            amsGetRealDiskPath.buildMirror();
        } catch (Exception ex) {
            recordException(amsGetRealDiskPath, ex);
        }
        boolean isOk = finished(amsGetRealDiskPath);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "am amsGetRealDiskPath: " + amsGetRealDiskPath.getErrMsg());
        }
        return isOk;
    }

    public String getRealDiskPath() {
        return this.amsGetRealDiskPath.getDiskpath();
    }

    /**
     * ############################## AMS-END
     * #######################################################
     */
    public boolean buildMirroring(String clntIP, int port, String letter,
            String interval_time, String min_size, int blk_size, int max_snap,
            String desc, String poolName, int worktype) {
        this.buildMirror.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
//        boolean ret = this.buildMirror.buildMirror( clntIP, port, letter,interval_time,min_size,
//            blk_size,max_snap,desc,poolName
//        );
        //boolean ret = this.buildMirror.buildMirror( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,0,"","","" );
        boolean ret = this.buildMirror.buildMirror(clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName, worktype, "", "", "");
        if (!ret) {
            errorMsg = buildMirror.getErrMsg();
            errorCode = buildMirror.getRetCode();
        }
        return ret;
    }

    //no label
    public boolean buildMirroringNoLabel(String clntIP, int port, String letter,
            String interval_time, String min_size, int blk_size, int max_snap,
            String desc, String poolName, int worktype) {
        this.buildMirror.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        //boolean ret = this.buildMirror.buildMirror( clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName,0,"","","uuid" );
        boolean ret = this.buildMirror.buildMirror(clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName, worktype, "", "", "uuid");
        if (!ret) {
            errorMsg = buildMirror.getErrMsg();
            errorCode = buildMirror.getRetCode();
        }
        return ret;
    }

    // ################   ucs  ####################################
    public boolean buildMirroringNoLabelByUcs(String clntIP, int port, String letter,
            String interval_time, String min_size, int blk_size, int max_snap,
            String desc, String poolName,
            int diskType, String LogMaxSize, String logMaxTime, int latestPool, int oldestPool, int LogPool, int logNum, int worktype) {
        this.buildMirror.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        boolean ret = this.buildMirror.buildMirrorNoLabelByUcs(clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName, "uuid", diskType, LogMaxSize, logMaxTime, oldestPool, latestPool, LogPool, logNum, worktype);
        if (!ret) {
            errorMsg = buildMirror.getErrMsg();
            errorCode = buildMirror.getRetCode();
        }
        return ret;
    }

    public boolean buildMirroringByUcs(String clntIP, int port, String letter,
            String interval_time, String min_size, int blk_size, int max_snap,
            String desc, String poolName,
            int diskType, String LogMaxSize, String logMaxTime, int latestPool, int oldestPool, int LogPool, int logNum, int worktype) {
        this.buildMirror.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        boolean ret = this.buildMirror.buildMirrorbyUcs(clntIP, port, letter, interval_time, min_size, blk_size, max_snap, desc, poolName, diskType, LogMaxSize, logMaxTime, oldestPool, latestPool, LogPool, logNum, worktype);
        if (!ret) {
            errorMsg = buildMirror.getErrMsg();
            errorCode = buildMirror.getRetCode();
        }
        return ret;
    }

    //######################
    public boolean isExistMirroringRelationship() {
        return this.buildMirror.isExitMirrorRelationshipOnClnt();
    }

    public String getRetVal() {
        return this.buildMirror.getRetVal();
    }

    public String getVolName() {
        return this.buildMirror.getVolName();
    }

    public boolean modDBAftRestoreOriDisk(String clntIP, int port, String drive, String uuid) {
        boolean isOk = this.modDBAftRestoreOriDisk.execCMDPcmd("modify db after restore original disk cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_MODIFY_DB_AFT_RECOVER) + clntIP + "," + port + "," + drive + "," + uuid);
        SanBootView.log.info(getClass().getName(), "modify db after restore original disk cmd retcode: " + modDBAftRestoreOriDisk.getRetCode());
        if (!isOk) {
            errorMsg = modDBAftRestoreOriDisk.getErrMsg();
            errorCode = modDBAftRestoreOriDisk.getRetCode();
            SanBootView.log.error(getClass().getName(), "modify db after restore original disk cmd errmsg: " + modDBAftRestoreOriDisk.getErrMsg());
        }
        return isOk;
    }

    public boolean zeroUUIDOnSector(int rootid, int snap_localid) {
        boolean isOk = this.zero_uuid_sector.execCMDPcmd("zero uuid on sector cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_ZERO_UUID_SECTOR) + rootid + "," + snap_localid);
        SanBootView.log.info(getClass().getName(), "zero uuid on sector cmd retcode: " + zero_uuid_sector.getRetCode());
        if (!isOk) {
            errorMsg = zero_uuid_sector.getErrMsg();
            errorCode = zero_uuid_sector.getRetCode();
            SanBootView.log.error(getClass().getName(), "zero uuid on sector cmd errmsg: " + zero_uuid_sector.getErrMsg());
        }
        return isOk;
    }

    public boolean createMirrorCluster(String ip, int port, String pri_ip1, String pri_ip2, String diskguid, int cluster_port) {
        this.createMirrorCluster.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        this.createMirrorCluster.setCmdLine(
                ResourceCenter.getCmdpS2A_CmdPath_For_Cluster_CMD(ip, port) + " create -thisip " + pri_ip1 + " -otherip " + pri_ip2 + " -sharediskguid "
                + diskguid + " -clusterport " + cluster_port);
        boolean isOk = this.createMirrorCluster.crtMirrorCluster();
        if (!isOk) {
            errorMsg = createMirrorCluster.getErrMsg();
            errorCode = createMirrorCluster.getRetCode();
        }
        return isOk;
    }

    public boolean delMirrorCluster(String ip, int port) {
        this.deleteMirrorCluster.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        this.deleteMirrorCluster.setCmdLine(
                ResourceCenter.getCmdpS2A_CmdPath_For_Cluster_CMD(ip, port) + "del");
        SanBootView.log.info(getClass().getName(), "delete mirror cluster  cmd: " + deleteMirrorCluster.getCmdLine());
        boolean isOk = this.deleteMirrorCluster.delMirrorCluster();
        SanBootView.log.info(getClass().getName(), "delete mirror cluster cmd retcode: " + deleteMirrorCluster.getRetCode());
        if (!isOk) {
            errorMsg = deleteMirrorCluster.getErrMsg();
            errorCode = deleteMirrorCluster.getRetCode();
            SanBootView.log.error(getClass().getName(), "delete mirror cluster cmd errmsg: " + deleteMirrorCluster.getErrMsg());
        }
        return isOk;
    }

    public boolean checkVolumeAftRegister(String ip, int port, String destLetter) {
        this.checkVolumeAftRegister.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        this.checkVolumeAftRegister.setCmdLine(
                ResourceCenter.getCmdpS2A_CmdPath1(ip, port) + " checkvolume -srcdrive " + destLetter);

        boolean isOk = this.checkVolumeAftRegister.checkVolAftRegister();
        if (!isOk) {
            errorMsg = checkVolumeAftRegister.getErrMsg();
            errorCode = checkVolumeAftRegister.getRetCode();
        }
        return isOk;
    }

    public boolean delMirrorOnClnt(String clntIP, int port, String drive) {
        boolean isOk = this.delMirrorOnClnt.execCMDPcmd("del mirror on client cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_DEL_MIRROR_ON_CLNT) + clntIP + "," + port + "," + drive);
        SanBootView.log.info(getClass().getName(), "del mirror on client cmd retcode: " + delMirrorOnClnt.getRetCode());
        if (!isOk) {
            errorMsg = delMirrorOnClnt.getErrMsg();
            errorCode = delMirrorOnClnt.getRetCode();
            SanBootView.log.error(getClass().getName(), "del mirror on client cmd errmsg: " + delMirrorOnClnt.getErrMsg());
        }
        return isOk;
    }

    public boolean delMirrorOnClnt1(String clntIP, int port, String uuid) {
        boolean isOk = this.delMirrorOnClnt.execCMDPcmd("del mirror on client cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_DEL_MIRROR_ON_CLNT1) + clntIP + "," + port + "," + uuid);
        SanBootView.log.info(getClass().getName(), "del mirror on client cmd retcode: " + delMirrorOnClnt.getRetCode());
        if (!isOk) {
            errorMsg = delMirrorOnClnt.getErrMsg();
            errorCode = delMirrorOnClnt.getRetCode();
            SanBootView.log.error(getClass().getName(), "del mirror on client cmd errmsg: " + delMirrorOnClnt.getErrMsg());
        }
        return isOk;
    }

    public boolean destoryMirror(String clntIP, int port, String volName) {
        boolean isOk = this.destoryMirror.execCMDPcmd("destory mirror on client cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_DEL_PROTECT) + clntIP + "," + port + "," + volName + ",0");
        SanBootView.log.info(getClass().getName(), "destory mirror on client cmd retcode: " + destoryMirror.getRetCode());
        if (!isOk) {
            errorMsg = destoryMirror.getErrMsg();
            errorCode = destoryMirror.getRetCode();
            SanBootView.log.error(getClass().getName(), "destory mirror on client cmd errmsg: " + destoryMirror.getErrMsg());
        }
        return isOk;
    }

    public boolean setNetBootVersion(String clntIP, int port, String rootIdStr, String snaplocalIdStr, int num) {
        boolean isOk = setNetBootVersion.setNetBootVersion(clntIP, port, rootIdStr, snaplocalIdStr, num);
        if (!isOk) {
            errorMsg = setNetBootVersion.getErrMsg();
            errorCode = setNetBootVersion.getRetCode();
        }
        return isOk;
    }

    public boolean isOkForSetNetbootVersion() {
        return this.setNetBootVersion.isOkForSetNetBootVer();
    }

    // 只适用于cmdp的agent
    public boolean getFileFromS2A(String cltIP, int cltPort, String ser_ip, String file, int mode) {
        try {
            if (this.isMTPPCmd(mode)) {
                this.getFileFromS2A.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + cltIP + " " + cltPort + " tftp " + ser_ip + " get " + file);
            } else {
                this.getFileFromS2A.setCmdLine(
                        ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " tftp " + ser_ip + " get " + file);
            }
            getFileFromS2A.setCmdType(mode);
            SanBootView.log.info(getClass().getName(), " get file from server to agent cmd: " + getFileFromS2A.getCmdLine());
            getFileFromS2A.run();
        } catch (Exception ex) {
            recordException(getFileFromS2A, ex);
        }
        SanBootView.log.info(getClass().getName(), " get file from server to agent cmd retcode: " + getFileFromS2A.getRetCode());
        boolean isOk = finished(getFileFromS2A);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), " get file from server to agent cmd errmsg: " + getFileFromS2A.getErrMsg());
        }
        return isOk;
    }

    public boolean recoverInvalidMirror(String clntIP, int port, String voluuid, int tid, String vol_info) {
        boolean isOk = this.recoverInvalidMirror.execCMDPcmd("recover invalid mirror cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_RECOVER_INVALID_MIRRROR) + clntIP + "," + port + "," + voluuid + "," + tid + "," + vol_info);
        SanBootView.log.info(getClass().getName(), "recover invalid mirror cmd retcode: " + recoverInvalidMirror.getRetCode());
        if (!isOk) {
            errorMsg = recoverInvalidMirror.getErrMsg();
            errorCode = recoverInvalidMirror.getRetCode();
            SanBootView.log.error(getClass().getName(), "recover invalid mirror cmd errmsg: " + recoverInvalidMirror.getErrMsg());
        }
        return isOk;
    }

    public boolean buildRestoreMirror(String clntIP, int port, String srcdrive, String destdrive, String destVolLen) {
        boolean isOk = this.buildRestoreMirror.execCMDPcmd("build rest-mirror cmd: ",
                // 0: 全量  2: 增量（有效容量）
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_CRT_RESTORE_PROTECT) + clntIP + "," + port + "," + srcdrive + "," + destdrive + ",sync," + ResourceCenter.CMDP_RST_MIRROR_TYPE + "," + destVolLen);
        SanBootView.log.info(getClass().getName(), "build rest-mirror cmd retcode: " + buildRestoreMirror.getRetCode());
        if (!isOk) {
            errorMsg = buildRestoreMirror.getErrMsg();
            errorCode = buildRestoreMirror.getRetCode();
            SanBootView.log.error(getClass().getName(), "build rest-mirror cmd errmsg: " + buildRestoreMirror.getErrMsg());
        }
        return isOk;
    }

    public boolean buildMirrorAftRestore(String clntIP, int port, String srcuuid, String destVolInfo, int tid) {
        boolean isOk = this.buildMirrorAftRestore.execCMDPcmd("build mirror aft restore cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_CRT_PROTECT_AFT_RESTORE) + clntIP + "," + port + "," + srcuuid + "," + destVolInfo + "," + tid);
        SanBootView.log.info(getClass().getName(), "build mirror aft restore cmd retcode: " + buildMirrorAftRestore.getRetCode());
        if (!isOk) {
            errorMsg = buildMirrorAftRestore.getErrMsg();
            errorCode = buildMirrorAftRestore.getRetCode();
            SanBootView.log.error(getClass().getName(), "build mirror aft restore cmd errmsg: " + buildMirrorAftRestore.getErrMsg());
        }
        return isOk;
    }

    // setLabelType:   set 0, label is "local";  set 1, label is "iscsi"
    public boolean checkDiskOnWin(String clntIP, int port, String checktime, String driveLetter, String setLabelType) {
        boolean isOk = chkdiskOnWin.execCMDPcmd("check disk cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_CHECK_DISK) + clntIP + "," + port + "," + driveLetter + "," + setLabelType + "," + checktime);
        SanBootView.log.info(getClass().getName(), "check disk cmd retcode: " + chkdiskOnWin.getRetCode());
        if (!isOk) {
            errorMsg = chkdiskOnWin.getErrMsg();
            errorCode = chkdiskOnWin.getRetCode();
            SanBootView.log.error(getClass().getName(), "check disk cmd errmsg: " + chkdiskOnWin.getErrMsg());
        }
        return isOk;
    }

    public String getVolInfoForTarget(String clntIP, int port, String targetName) {
        this.getVolInfoForTgt.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        boolean ret = this.getVolInfoForTgt.getVolInfoForTarget(clntIP, port, targetName);
        if (!ret) {
            errorMsg = getVolInfoForTgt.getErrMsg();
            errorCode = getVolInfoForTgt.getRetCode();
            return "";
        } else {
            return this.getVolInfoForTgt.getVolInfo();
        }
    }

    public boolean getMirrorInfo(String clntIP, int port, String drive) {
        this.getMirroringInfo.setCmdType(ResourceCenter.CMD_TYPE_CMDP);
        boolean ret = this.getMirroringInfo.realDo(clntIP, port, drive);
        if (!ret) {
            errorMsg = getMirroringInfo.getErrMsg();
            errorCode = getMirroringInfo.getRetCode();
        }
        return ret;
    }

    public String getMirrorTargetID() {
        return this.getMirroringInfo.getTid();
    }

    public boolean mirrorStateIsSync() {
        return this.getMirroringInfo.isSync();
    }

    public boolean mirrorStateIsAsync() {
        return this.getMirroringInfo.isASync();
    }

    public boolean mirrorStateIsA2S() {
        return this.getMirroringInfo.isA2S();
    }

    public boolean mirrorStateIsInit() {
        return this.getMirroringInfo.isIniting();
    }

    public boolean mirrorStateIsUnknown() {
        return this.getMirroringInfo.isUnknown();
    }

    public boolean mirrorStateIsConnectError() {
        return this.getMirroringInfo.isNotConnectToHost();
    }

    public String getMirrorState() {
        return this.getMirroringInfo.getStateString();
    }

    public boolean setInitType(String clntIP, int port, String status, int flag) {
        boolean isOk = this.setInitType.execCMDPcmd("set init type cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_SET_INIT_STAT) + clntIP + "," + port + "," + status + "," + flag);
        SanBootView.log.info(getClass().getName(), "set init type cmd retcode: " + setInitType.getRetCode());
        if (!isOk) {
            errorMsg = setInitType.getErrMsg();
            errorCode = setInitType.getRetCode();
            SanBootView.log.error(getClass().getName(), "set init type cmd errmsg: " + setInitType.getErrMsg());
        }
        return isOk;
    }

    public boolean flushDisk(String clntIP, int port, String drive) {
        boolean isOk = this.flushDisk.execCMDPcmd("flush disk cmd: ",
                ResourceCenter.getCmd(ResourceCenter.CMD_CMDP_FLUSH_DISK) + clntIP + "," + port + "," + drive);
        SanBootView.log.info(getClass().getName(), "flush disk cmd retcode: " + flushDisk.getRetCode());
        if (!isOk) {
            errorMsg = flushDisk.getErrMsg();
            errorCode = flushDisk.getRetCode();
            SanBootView.log.error(getClass().getName(), "flush disk cmd errmsg: " + flushDisk.getErrMsg());
        }
        return isOk;
    }

    public boolean powerdownUws() {
        powerdown.setCmdLine(
                "shell powerdown");
        SanBootView.log.info(getClass().getName(), "powerdown cmd: " + powerdown.getCmdLine());
        try {
            powerdown.run();
        } catch (Exception ex) {
            recordException(powerdown, ex);
        }
        SanBootView.log.info(getClass().getName(), "powerdown cmd retcode: " + powerdown.getRetCode());
        boolean isOk = finished(powerdown);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "powerdown cmd errmsg: " + powerdown.getErrMsg());
        }
        return isOk;
    }

    public boolean getServicesOnVolume(String conf) {
        this.getServiceOnVol.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_CAT) + conf);

        boolean ret = getServiceOnVol.realDo();
        if (!ret) {
            errorMsg = getServiceOnVol.getErrMsg();
            errorCode = getServiceOnVol.getRetCode();
        }
        return ret;
    }

    public ArrayList<ServicesOnVolume> getAllServiceListOnVol() {
        return getServiceOnVol.getAllServiceListOnVol();
    }

    // 恢复跟symantec相关的注册表信息. 2011.4.14
    public boolean ib_driver_comp(String cltIP, int cltPort, String destDrv, int mode) {
        try {
            if (this.isMTPPCmd(mode)) {
                this.ib_driver_comp.setCmdLine(
                        ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + cltIP + " " + cltPort + " ib_driver_comp.exe -restore " + destDrv);
            } else {
                this.ib_driver_comp.setCmdLine(
                        ResourceCenter.getCmdpS2A_CmdPath(cltIP, cltPort) + " ib_driver_comp.exe -restore " + destDrv);
            }
            ib_driver_comp.setCmdType(mode);
            SanBootView.log.info(getClass().getName(), " ib_driver_comp(symantec) cmd: " + ib_driver_comp.getCmdLine());
            ib_driver_comp.run();
        } catch (Exception ex) {
            recordException(ib_driver_comp, ex);
        }
        SanBootView.log.info(getClass().getName(), " ib_driver_comp(symantec) cmd retcode: " + ib_driver_comp.getRetCode());
        boolean isOk = finished(ib_driver_comp);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "ib_driver_comp(symantec) cmd errmsg: " + ib_driver_comp.getErrMsg());
        }
        return isOk;
    }

    public boolean forwordUcsSnapByTime(int rootId, int localId, String timestamp) {
        try {
            forwordUcsSnap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_FORWORD_UCS_VIEW) + " -t " + timestamp
                    + " " + rootId + " " + localId);
            SanBootView.log.info(getClass().getName(), "ams forwordUcsSnap cmd: " + forwordUcsSnap.getCmdLine());
            forwordUcsSnap.run();
        } catch (Exception ex) {
            recordException(forwordUcsSnap, ex);
        }
        SanBootView.log.info(getClass().getName(), "ams forwordUcsSnap cmd: " + forwordUcsSnap.getRetCode());
        boolean isOk = finished(forwordUcsSnap);
        if (!isOk) {
            errorMsg = forwordUcsSnap.getErrMsg();
            errorCode = forwordUcsSnap.getRetCode();
            SanBootView.log.error(getClass().getName(), "ams forwordUcsSnap errmsg: " + forwordUcsSnap.getErrMsg());
        }

        return true;
    }

    public boolean forwordUcsSnapByIO(int rootId, int localId, int ioNum) {
        try {
            forwordUcsSnap.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_FORWORD_UCS_VIEW) + " -n " + ioNum
                    + " " + rootId + " " + localId);
            SanBootView.log.info(getClass().getName(), "ams forwordUcsSnap cmd: " + forwordUcsSnap.getCmdLine());
            forwordUcsSnap.run();
        } catch (Exception ex) {
            recordException(forwordUcsSnap, ex);
        }
        SanBootView.log.info(getClass().getName(), "ams forwordUcsSnap cmd: " + forwordUcsSnap.getRetCode());
        boolean isOk = finished(forwordUcsSnap);
        if (!isOk) {
            errorMsg = forwordUcsSnap.getErrMsg();
            errorCode = forwordUcsSnap.getRetCode();
            SanBootView.log.error(getClass().getName(), "ams forwordUcsSnap errmsg: " + forwordUcsSnap.getErrMsg());
        }

        return true;
    }

    public boolean updateVMHostInfo(String conffile) {
        return getVMHostInfo.updateVMHostInfo(conffile);
    }

    public ArrayList<VMHostInfo> getAllVMHostInfo() {
        return getVMHostInfo.getAllVMHostInfo();
    }

    public VMHostInfo getVMHostInfoByName(String vmname) {
        return getVMHostInfo.getVMHostInfoByName(vmname);
    }

    public boolean updateVMServerInfo() {
        return getVMServerInfo.updateVMServer();
    }

    public String getVMServerIP() {
        return getVMServerInfo.getServerip();
    }

    public int getVMServerPort() {
        return getVMServerInfo.getPort();
    }

    /**
     * ******************************VMHostManager ****************************
     */
    public boolean addVMHost(VMHostInfo vmhost) {
        try {
            StringBuffer args = new StringBuffer();
            String configfile = vmhost.getVm_clntid() + "." + vmhost.getVm_name() + ".conf";
            args.append(" " + configfile);
            args.append(" vm_clntid=" + vmhost.getVm_clntid());
            args.append(" vm_name=" + vmhost.getVm_name());
            args.append(" \"vm_path=" + vmhost.getVm_path() + "\"");
            args.append(" vm_ip=" + vmhost.getVm_ip());
            args.append(" vm_port=" + vmhost.getVm_port());
            args.append(" vm_vip=" + vmhost.getVm_vip());
            args.append(" vm_targetid=" + (vmhost.getVm_targetid() == null ? "" : vmhost.getVm_targetid()));
            args.append(" vm_snapid=" + (vmhost.getVm_snapid() == null ? "" : vmhost.getVm_snapid()));
            args.append(" vm_viewid=" + (vmhost.getVm_viewid() == null ? "" : vmhost.getVm_viewid()));
            args.append(" vm_letter=" + (vmhost.getVm_letter() == null ? "" : vmhost.getVm_letter()));
            args.append(" vm_recoverip=" + vmhost.getVm_recoverip());
            args.append(" vm_maxdisctime=" + vmhost.getVm_maxdisctime());
            args.append(" vm_pingip=" + vmhost.getVm_pingip());
            args.append(" vm_ischeck=" + vmhost.getVm_ischeck());
            this.addVMHost.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_ADD_VM_HOST) + args.toString());
            SanBootView.log.info(getClass().getName(), "addVMHost cmd: " + addVMHost.getCmdLine());
            addVMHost.run();
        } catch (Exception ex) {
            recordException(addVMHost, ex);
        }
        SanBootView.log.info(getClass().getName(), "addVMHost cmd retcode: " + addVMHost.getRetCode());
        boolean isOk = finished(addVMHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "addVMHost cmd errmsg: " + addVMHost.getErrMsg());
        }
        return isOk;
    }

    public boolean modVMHost(VMHostInfo vmhost) {
        try {
            StringBuffer args = new StringBuffer();
            String configfile = vmhost.getVm_clntid() + "." + vmhost.getVm_name() + ".conf";
            args.append(" " + configfile);
            args.append(" vm_clntid=" + vmhost.getVm_clntid());
            args.append(" vm_name=" + vmhost.getVm_name());
            args.append(" \"vm_path=" + vmhost.getVm_path() + "\"");
            args.append(" vm_ip=" + vmhost.getVm_ip());
            args.append(" vm_port=" + vmhost.getVm_port());
            args.append(" vm_vip=" + vmhost.getVm_vip());
            args.append(" vm_targetid=" + (vmhost.getVm_targetid() == null ? "" : vmhost.getVm_targetid()));
            args.append(" vm_snapid=" + (vmhost.getVm_snapid() == null ? "" : vmhost.getVm_snapid()));
            args.append(" vm_viewid=" + (vmhost.getVm_viewid() == null ? "" : vmhost.getVm_viewid()));
            args.append(" vm_letter=" + (vmhost.getVm_letter() == null ? "" : vmhost.getVm_letter()));
            args.append(" vm_recoverip=" + vmhost.getVm_recoverip());
            args.append(" vm_maxdisctime=" + vmhost.getVm_maxdisctime());
            args.append(" vm_pingip=" + vmhost.getVm_pingip());
            args.append(" vm_ischeck=" + vmhost.getVm_ischeck());
            this.modVMHost.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_MOD_VM_HOST) + args.toString());
            SanBootView.log.info(getClass().getName(), "modVMHost cmd: " + modVMHost.getCmdLine());
            modVMHost.run();
        } catch (Exception ex) {
            recordException(modVMHost, ex);
        }
        SanBootView.log.info(getClass().getName(), "modVMHost cmd retcode: " + modVMHost.getRetCode());
        boolean isOk = finished(modVMHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "modVMHost cmd errmsg: " + modVMHost.getErrMsg());
        }
        return isOk;
    }

    public boolean delVMHost(VMHostInfo vmhost) {
        try {
            String configfile = vmhost.getVm_clntid() + "." + vmhost.getVm_name() + ".conf";
            this.delVMHost.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_DEL_VM_HOST) + configfile);
            SanBootView.log.info(getClass().getName(), "delVMHost cmd: " + delVMHost.getCmdLine());
            delVMHost.run();
        } catch (Exception ex) {
            recordException(delVMHost, ex);
        }
        SanBootView.log.info(getClass().getName(), "delVMHost cmd retcode: " + delVMHost.getRetCode());
        boolean isOk = finished(delVMHost);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "delVMHost cmd errmsg: " + delVMHost.getErrMsg());
        }
        return isOk;
    }

    public boolean powerOnVMHost(VMHostInfo vmhost) {
        try {
            StringBuffer args = new StringBuffer();
            args.append(" \"poweron,");
            args.append(vmhost.getVm_ip() + ",");
            args.append(vmhost.getVm_port() + ",,,");
            args.append(vmhost.getVm_path() + "\"");
            this.vmHostOper.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_POWERON_VMHOST) + args.toString());
            SanBootView.log.info(getClass().getName(), "VMHostOper cmd: " + vmHostOper.getCmdLine());
            vmHostOper.run();
        } catch (Exception ex) {
            recordException(vmHostOper, ex);
        }
        SanBootView.log.info(getClass().getName(), "vmHostOper cmd retcode: " + vmHostOper.getRetCode());
        boolean isOk = finished(vmHostOper);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vmHostOper cmd errmsg: " + vmHostOper.getErrMsg());
        }
        return isOk;
    }

    public boolean powerOffVMHost(VMHostInfo vmhost) {
        try {
            StringBuffer args = new StringBuffer();
            args.append(" \"poweroff,");
            args.append(vmhost.getVm_ip() + ",");
            args.append(vmhost.getVm_port() + ",,,");
            args.append(vmhost.getVm_path() + "\"");
            this.vmHostOper.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_POWEROFF_VMHOST) + args.toString());
            SanBootView.log.info(getClass().getName(), "VMHostOper cmd: " + vmHostOper.getCmdLine());
            vmHostOper.run();
        } catch (Exception ex) {
            recordException(vmHostOper, ex);
        }
        SanBootView.log.info(getClass().getName(), "vmHostOper cmd retcode: " + vmHostOper.getRetCode());
        boolean isOk = finished(vmHostOper);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vmHostOper cmd errmsg: " + vmHostOper.getErrMsg());
        }
        return isOk;
    }

    public boolean suspendVMHost(VMHostInfo vmhost) {
        try {
            StringBuffer args = new StringBuffer();
            args.append(" \"suspend,");
            args.append(vmhost.getVm_ip() + ",");
            args.append(vmhost.getVm_port() + ",");
            args.append(vmhost.getVm_letter() + ",");
            args.append(vmhost.getVm_targetid() + ",");
            args.append(vmhost.getVm_path() + "\"");
            this.vmHostOper.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_SUSPEND_VMHOST) + args.toString());
            SanBootView.log.info(getClass().getName(), "VMHostOper cmd: " + vmHostOper.getCmdLine());
            vmHostOper.run();
        } catch (Exception ex) {
            recordException(vmHostOper, ex);
        }
        SanBootView.log.info(getClass().getName(), "vmHostOper cmd retcode: " + vmHostOper.getRetCode());
        boolean isOk = finished(vmHostOper);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vmHostOper cmd errmsg: " + vmHostOper.getErrMsg());
        }
        return isOk;
    }

    public boolean shutdownVMHost(VMHostInfo vmhost) {
        try {
            StringBuffer args = new StringBuffer();
            args.append(" shutdownguest,");
            args.append(vmhost.getVm_vip() + ",");
            args.append(vmhost.getVm_port());
            this.vmHostOper.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_SHUTDOWN_VMHOST) + args.toString());
            SanBootView.log.info(getClass().getName(), "VMHostOper cmd: " + vmHostOper.getCmdLine());
            vmHostOper.run();
        } catch (Exception ex) {
            recordException(vmHostOper, ex);
        }
        SanBootView.log.info(getClass().getName(), "vmHostOper cmd retcode: " + vmHostOper.getRetCode());
        boolean isOk = finished(vmHostOper);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vmHostOper cmd errmsg: " + vmHostOper.getErrMsg());
        }
        return isOk;
    }

    public boolean removeVMHost(VMHostInfo vmhost) {
        try {
            StringBuffer args = new StringBuffer();
            args.append(" \"remove,");
            args.append(vmhost.getVm_ip() + ",");
            args.append(vmhost.getVm_port() + ",,,");
            args.append(vmhost.getVm_path() + "\"");
            this.vmHostOper.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_ISRUNNING_VMHOST) + args.toString());
            SanBootView.log.info(getClass().getName(), "VMHostOper cmd: " + vmHostOper.getCmdLine());
            vmHostOper.run();
        } catch (Exception ex) {
            recordException(vmHostOper, ex);
        }
        SanBootView.log.info(getClass().getName(), "vmHostOper cmd retcode: " + vmHostOper.getRetCode());
        boolean isOk = finished(vmHostOper);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vmHostOper cmd errmsg: " + vmHostOper.getErrMsg());
        }
        return isOk;       
    }

    public boolean isRunningVMHost(VMHostInfo vmhost) {
        try {
            StringBuffer args = new StringBuffer();
            args.append(" \"isruning,");
            args.append(vmhost.getVm_ip() + ",");
            args.append(vmhost.getVm_port() + ",,,");
            args.append(vmhost.getVm_path() + "\"");
            this.vmHostOper.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_ISRUNNING_VMHOST) + args.toString());
            SanBootView.log.info(getClass().getName(), "VMHostOper cmd: " + vmHostOper.getCmdLine());
            vmHostOper.run();
        } catch (Exception ex) {
            recordException(vmHostOper, ex);
        }
        SanBootView.log.info(getClass().getName(), "vmHostOper cmd retcode: " + vmHostOper.getRetCode());
        boolean isOk = finished(vmHostOper);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vmHostOper cmd errmsg: " + vmHostOper.getErrMsg());
        }
        return isOk;
    }

    public boolean changeDiskVMHost(VMHostInfo vmhost) {
        try {
            StringBuffer args = new StringBuffer();
            args.append(" \"changedisk,");
            args.append(vmhost.getVm_ip() + ",");
            args.append(vmhost.getVm_port() + ",");
            args.append(vmhost.getVm_letter() + ",");
            args.append(vmhost.getVm_targetid() + ",");
            args.append(vmhost.getVm_path() + ",");
            args.append(vmhost.getVm_vip() + ",");
            args.append(vmhost.getVm_recoverip() + "\"");
            args.append(" vm_maxdisctime=" + vmhost.getVm_maxdisctime());
            args.append(" vm_pingip=" + vmhost.getVm_pingip());
            args.append(" vm_ischeck=" + vmhost.getVm_ischeck());
            this.vmHostOper.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CHANGEDISK_VMHOST) + args.toString());
            SanBootView.log.info(getClass().getName(), "VMHostOper cmd: " + vmHostOper.getCmdLine());
            vmHostOper.run();
        } catch (Exception ex) {
            recordException(vmHostOper, ex);
        }
        SanBootView.log.info(getClass().getName(), "vmHostOper cmd retcode: " + vmHostOper.getRetCode());
        boolean isOk = finished(vmHostOper);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vmHostOper cmd errmsg: " + vmHostOper.getErrMsg());
        }
        return isOk;
    }

    public boolean deleteVMHostIP(VMHostInfo vmhost) {
        try {
            this.vmHostOper.setCmdLine(ResourceCenter.getCmdpS2A_CmdPath_DeleteVMHostIP(vmhost.getVm_vip(), 2015));
            SanBootView.log.info(getClass().getName(), "VMHostOper cmd: " + vmHostOper.getCmdLine());
            vmHostOper.run();
        } catch (Exception ex) {
            recordException(vmHostOper, ex);
        }
        SanBootView.log.info(getClass().getName(), "vmHostOper cmd retcode: " + vmHostOper.getRetCode());
        boolean isOk = finished(vmHostOper);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vmHostOper cmd errmsg: " + vmHostOper.getErrMsg());
        }
        return isOk;
    }

    public boolean recoverVMHostIP(VMHostInfo vmhost) {
        try {
            this.vmHostOper.setCmdLine(ResourceCenter.getCmdpS2A_CmdPath_RecoverVMHostIP(vmhost.getVm_vip(), 2015));
            SanBootView.log.info(getClass().getName(), "VMHostOper cmd: " + vmHostOper.getCmdLine());
            vmHostOper.run();
        } catch (Exception ex) {
            recordException(vmHostOper, ex);
        }
        SanBootView.log.info(getClass().getName(), "vmHostOper cmd retcode: " + vmHostOper.getRetCode());
        boolean isOk = finished(vmHostOper);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vmHostOper cmd errmsg: " + vmHostOper.getErrMsg());
        }
        return isOk;
    }

    public boolean bindViewMac(String ip, int port, String mac, int pType) {
        try {
            if (pType == ResourceCenter.CMD_TYPE_CMDP) {
                this.bindviewMac.setCmdLine(ResourceCenter.getCmdpS2A_CmdPath_BindMacAddrs(ip, port, mac));
            } else {
                this.bindviewMac.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_IBOOTCMD) + ip + " " + port + " bindview_cmd.exe type=3 mac=" + mac);
            }
            SanBootView.log.info(getClass().getName(), "bindviewMac cmd: " + bindviewMac.getCmdLine());
            this.bindviewMac.run();
        } catch (Exception ex) {
            recordException(bindviewMac, ex);
        }
        SanBootView.log.info(getClass().getName(), "bindviewMac cmd: " + bindviewMac.getRetCode());
        boolean isOk = finished(bindviewMac);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "bindviewMac cmd errmsg: " + bindviewMac.getErrMsg());
        }
        return isOk;
    }

    public boolean isAlreadyStartSystem(String ip, int port) {
        try {
            this.vmHostOper.setCmdLine(ResourceCenter.getCmdpS2A_CmdStartedSystem(ip, port));
            SanBootView.log.info(getClass().getName(), "vmHostOper cmd: " + vmHostOper.getCmdLine());
            this.vmHostOper.run();
        } catch (Exception ex) {
            recordException(vmHostOper, ex);
        }
        SanBootView.log.info(getClass().getName(), "vmHostOper cmd: " + vmHostOper.getRetCode());
        boolean isOk = finished(vmHostOper);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "vmHostOper cmd errmsg: " + vmHostOper.getErrMsg());
        }
        return isOk;
    }

    public boolean enableIbootDiskView(String args) {
        try {

            this.enbaleIboot.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_ENABELIBOOT_VMHOST_DISK) + " enableiboot," + args);
            SanBootView.log.info(getClass().getName(), "enbaleIboot cmd: " + enbaleIboot.getCmdLine());
            enbaleIboot.run();
        } catch (Exception ex) {
            recordException(enbaleIboot, ex);
        }
        SanBootView.log.info(getClass().getName(), "enbaleIboot cmd retcode: " + enbaleIboot.getRetCode());
        boolean isOk = finished(enbaleIboot);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "enbaleIboot cmd errmsg: " + enbaleIboot.getErrMsg());
        }
        return isOk;
    }

    public boolean updateIPConfigInfo(String ip, int port) {
        getVMHostIPConfig.setCmdLine(ResourceCenter.getCmdpS2A_CmdPath_SeeVMHostIPInfo(ip, port));
        return getVMHostIPConfig.updateVMHostIPConfig();
    }

    public ArrayList<String> getVMHostIPInfo() {
        return getVMHostIPConfig.getIPConfigInfo();
    }

    public ArrayList<String> getVMHostIPInfo1() {
        return getVMHostIPConfig.getIPConfigInfo1();
    }

    public ArrayList<String> getIdleVip() {
        boolean isOk = getIdleAndUsedVip.updateIdleVip();
        if (isOk) {
            return getIdleAndUsedVip.getIdleVip();
        } else {
            return new ArrayList<String>();
        }
    }

    public ArrayList<String> getUsedVip() {
        boolean isOk = getIdleAndUsedVip.updateUsedVip();
        if (isOk) {
            return getIdleAndUsedVip.getUsedVip();
        } else {
            return new ArrayList<String>();
        }
    }

    public String getUWSDefaultIp() {
        boolean isOk = getUWSDefaultIp.updateDefaultIp();
        if (isOk) {
            return getUWSDefaultIp.getUWSDefaultIp();
        } else {
            return "";
        }
    }

    public boolean updateVMinfoFromHost(String ip) {
        return getVMInfoFromHost.updateVMinfoFromHost(ip);
    }

    public String getOsType() {
        return getVMInfoFromHost.getOsType();
    }

    public String getArch() {
        return getVMInfoFromHost.getArch();
    }

    public long getDiskSize() {
        return getVMInfoFromHost.getDiskSize();
    }

    /**
     *
     * @param args
     * (198.88.88.91,2015,d:/test172,test172,2003,x86,1,16102850560,1024)
     * @return
     */
    public boolean createVMMachine(String args) {
        try {
            crtVMMachine.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CRT_VM_MACHINE) + args);
            SanBootView.log.info(getClass().getName(), "crtVMMachine cmd :" + crtVMMachine.getCmdLine());
            crtVMMachine.run();
        } catch (Exception ex) {
            recordException(crtVMMachine, ex);
        }
        SanBootView.log.info(getClass().getName(), "crtVMMachine cmd retcode :" + crtVMMachine.getRetCode());
        boolean isOk = finished(crtVMMachine);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "crtVMMachine cmd errmsg :" + crtVMMachine.getErrMsg());
        }
        return isOk;
    }

    public boolean setVMServiceInfo(String ip, String port) {
        try {
            crtVMServiceInfo.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_CRT_VMSERVICE_INFO) + ip + " " + port);
            SanBootView.log.info(getClass().getName(), "crtVMServiceInfo cmd :" + crtVMServiceInfo.getCmdLine());
            crtVMServiceInfo.run();
        } catch (Exception ex) {
            recordException(crtVMServiceInfo, ex);
        }
        SanBootView.log.info(getClass().getName(), "crtVMServiceInfo cmd retcode :" + crtVMServiceInfo.getRetCode());
        boolean isOk = finished(crtVMServiceInfo);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "crtVMServiceInfo cmd errmsg :" + crtVMServiceInfo.getErrMsg());
        }
        return isOk;
    }

    public boolean hasVMServiceInfo() {
        return hasVMserviceInfoOrNot.hasVMServiceInfo();
    }

    public boolean updateLicenseCount() {
        return getLicenseCount.updateLicenseCount();
    }

    public boolean hasSystemLicense() {
        return getLicenseCount.hasSystemLicense();
    }

    public boolean hasDatabaseLicense() {
        return getLicenseCount.hasDatabaseLicense();
    }

    public String getTXIP(String ip) {
        try {
            getTxIPFromIP.setCmdLine(ResourceCenter.getCmd(ResourceCenter.CMD_GET_TXIP) + ip);
            getTxIPFromIP.run();
        } catch (Exception ex) {
            recordException(getTxIPFromIP, ex);
        }
        boolean isOk = finished(getTxIPFromIP);
        if (!isOk) {
            return null;
        } else {
            return getTxIPFromIP.getTxIp();
        }
    }

    public boolean getLinuxDevName(String ip, int port, String args) {
        getLinuxDevName.setCmdLine(
                ResourceCenter.getCmd(ResourceCenter.CMD_GET_LINUX_DEV) + " " + ip + " " + port + " " + args);
        boolean isOk = getLinuxDevName.realDo();
        if (!isOk) {
            this.errorMsg = getLinuxDevName.getErrMsg();
            this.errorCode = getLinuxDevName.getRetCode();
        }
        return isOk;
    }

    public ArrayList<String> getLinuxDevNameList() {
        return getLinuxDevName.getDevNameList();
    }

    public boolean setupIbootLinux64(String ip, int port, String args) {
        try {
            setupIbootLinux64.setCmdLine(
                    ResourceCenter.getCmd(ResourceCenter.CMD_SETUP_IBOOT_LINUX64) + ip + " " + port + " iboot/iscsi-boot.sh " + args);
            SanBootView.log.info(getClass().getName(), "send setupIbootLinux64 cmd: " + setupIbootLinux64.getCmdLine());
            setupIbootLinux64.run();
        } catch (Exception ex) {
            recordException(setupIbootLinux64, ex);
        }

        SanBootView.log.info(getClass().getName(), "send setupIbootLinux64 retcode: " + setupIbootLinux64.getRetCode());
        boolean isOk = finished(setupIbootLinux64);
        if (!isOk) {
            SanBootView.log.error(getClass().getName(), "send setupIbootLinux64 errmsg: " + setupIbootLinux64.getErrMsg());
        }
        return isOk;
    }

    public Boolean getDhcpStatus(){
        String cmd = ResourceCenter.BIN_DIR + "phydup_fun.sh IsDchpEnable";
        Boolean isStart = false;
        try{
             isStart = this.getDhcp.getDhcpStatus(cmd);
        }catch(Exception ex){
            recordException( this.getDhcp, ex );
        }
        return isStart;
    }
}