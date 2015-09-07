package guisanboot;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import mylib.UI.*; 
import guisanboot.ui.*;
import guisanboot.data.*;
import guisanboot.datadup.ui.viewobj.*;
import guisanboot.res.*;
import guisanboot.unlimitedIncMj.action.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import guisanboot.ams.action.QueryAmsInitProgressAction;
import guisanboot.cmdp.action.*;
import guisanboot.remotemirror.action.CreateMjInBatchAction;
import guisanboot.remotemirror.action.CrtMjSchAction;
import guisanboot.remotemirror.action.DelMjSchAction;
import guisanboot.remotemirror.action.ModMjSchAction;
import guisanboot.remotemirror.action.ModifyMjInBatchAction;
import guisanboot.remotemirror.action.SetupMjSchAction;
import guisanboot.remotemirror.action.StartOrStopMjInBatchAction;
import guisanboot.ams.action.InitHostByAmsAction;
import guisanboot.audit.data.*;
import guisanboot.cluster.action.FailoverClusterForWinByPhyProtectAction;
import guisanboot.cluster.action.InitClusterForWinByPhyProtectAction;
import guisanboot.cluster.entity.Cluster;
import guisanboot.action.*;
import guisanboot.vmmanage.action.*;

import guisanboot.lvm.action.*;

/**
 * 菜单选项
 * @author Administrator
 */
public class MenuAndBtnCenterForMainUi {
    // 控制 menu 和 button的行为时用到的常数
    public static final int  LEVEL_ROOT         = 0;
    public static final int  LEVEL_HOSTLIST     = 1;
    public static final int  LEVEL_BOOT_HOST    = 2;
    public static final int  LEVEL_VOL          = 3;
    public static final int  LEVEL_LV           = 4;
    public static final int  LEVEL_ACTLINK      = 5;
    public static final int  LEVEL_LMLIST       = 6;
    public static final int  LEVEL_LM           = 7;
    public static final int  LEVEL_SNAPLIST     = 8;
    public static final int  LEVEL_SNAP         = 9;
    public static final int  LEVEL_VIEW         = 10;
    public static final int  LEVEL_ORPH_VOLLIST = 11;
    public static final int  LEVEL_ORPH_VOL     = 12;
    public static final int  LEVEL_USER_LIST    = 13;
    public static final int  LEVEL_USER         = 14;
    public static final int  LEVEL_POOLLIST     = 15;
    public static final int  LEVEL_POOL         = 16;
    public static final int  LEVEL_UWSLIST      = 17;
    public static final int  LEVEL_DEST_UWS_SRV = 18;
    public static final int  LEVEL_MJLIST       = 19;
    public static final int  LEVEL_MJ           = 20;
    public static final int  LEVEL_MIRROR_SNAP  = 21;
    public static final int  LEVEL_SRC_UWS_SRV  = 22;
    public static final int  LEVEL_MIRROR_SNAP_VIEW = 23;
    public static final int  LEVEL_NETBOOTED_HOST = 24;  // 网络启动主机
    public static final int  LEVEL_SRC_AGENT    = 25;    // 远程主机（目的端用来表示源端主机的对象）
    public static final int  LEVEL_DEL_SNAP     = 26;
    public static final int  LEVEL_PROFLIST     = 27;
    public static final int  LEVEL_PROF         = 28;
    public static final int  LEVEL_SCHLIST      = 29;
    public static final int  LEVEL_SCH          = 30;
    public static final int  LEVEL_CLONE_DISK   = 31;   // 克隆盘
    public static final int  LEVEL_MIRROR_VOL   = 32;   // 普通镜像卷
    public static final int  LEVEL_UIM_VOL      = 33;   // 无限增量镜像卷
    public static final int  LEVEL_UIM_SNAP_LIST = 34;
    public static final int  LEVEL_UIM_SNAP     = 35;   // 无限增量快照
    public static final int  LEVEL_PPPROF       = 36;   // physical protect profile
    public static final int  LEVEL_ADSCHLIST    = 37;

    public static final int  LEVEL_UNKNOWN      = 99;

    public final static  int FUNC_SEPARATOR    = 0;

    // file
    public final static  int FUNC_EXIT        = 1;
    public final static  int FUNC_LOGIN       = 2;
    public final static  int FUNC_LOGOUT      = 3;
    public final static  int FUNC_PROPERTY    = 4;

    // win op(logical protect)
    public final static  int FUNC_INIT          = 10;
    public final static  int FUNC_FAILOVER      = 11;
    public final static  int FUNC_FAILBACK      = 12;
    public final static  int FUNC_RESTORE_DISK  = 13;
    public final static  int FUNC_ABOUT         = 16;
    public final static  int FUNC_ROLLBACK      = 17;

    // volume
    public final static  int FUNC_CRT_VOL   = 20;
    public final static  int FUNC_DEL_VOL   = 21;
    public final static  int FUNC_ONLINE    = 22;
    public final static  int FUNC_OFFLINE   = 23;
    public final static  int FUNC_ROLLBACK_VOL = 24;

    // snapshot
    public final static  int FUNC_CRT_SNAP   = 30;
    public final static  int FUNC_DEL_SNAP   = 31;
    public final static  int FUNC_MNT_SNAP   = 32;
    public final static  int FUNC_CRT_SYNC_SNAP  = 33;   // for cmdp
    public final static  int FUNC_CRT_ASYNC_SNAP = 34;   // for cmdp
    public final static  int FUNC_SNAP_EVER = 35;
    public final static  int FUNC_CRT_UCS_SNAP = 36;    // for ucs 

    // host lummap
    public final static  int FUNC_LUNMAP    = 40;
    public final static  int FUNC_CANCEL_LM = 41;

    public final static  int FUNC_EXPORTLM    = 67;

    // host
    public final static  int FUNC_CRT_HOST  = 50;
    public final static  int FUNC_DEL_HOST  = 51;
    public final static  int FUNC_MOD_HOST  = 52;
    public final static  int FUNC_MOD_HOSTBOOT  = 66;
    public final static  int FUNC_DEL_CLUSTER = 49;
    public final static  int FUNC_MOD_CLUSTER = 48;

    // sdhm
    public final static  int FUNC_SDHM_INFO  = 131;
    public final static  int FUNC_SDHM_SET  = 132;
    public final static  int FUNC_SDHM_REPORT  = 133;
    public final static  int FUNC_SDHM_MIRROR  = 134;

    // setup
    public final static  int FUNC_DHCP = 53;
    public final static  int FUNC_MSG_REPORT = 54;
    public final static  int FUNC_ADMINOPT = 55;
    public final static  int FUNC_MOD_PASS = 56;
    

    // dest UWS server
    public final static  int FUNC_CRT_UWS_SRV = 57;
    public final static  int FUNC_DEL_UWS_SRV = 58;
    public final static  int FUNC_MOD_UWS_SRV = 59;

    // user
    public final static  int FUNC_CRT_USER  = 60;
    public final static  int FUNC_DEL_USER  = 61;
    public final static  int FUNC_MOD_USER  = 62;
    public final static  int FUNC_MOD_PASSWD  = 63;

    // del netboot host
    public final static  int FUNC_DEL_DSTAGNT = 64;

    // del srcagent <==> del rollbacked host
    public final static  int FUNC_DEL_SRCAGNT = 65;

    // non-win op(logical protect)
    public final static  int FUNC_INIT_NWIN  = 70;
    public final static  int FUNC_INIT_NWIN_AMS = 162;
    public final static  int FUNC_INIT_NWIN_LVM = 164;
    public final static  int FUNC_REST_DISK_NWIN  = 71;
    public final static  int FUNC_SELECT_BOOT_VER = 72;
    public final static  int FUNC_IBOOT_WIZARD = 73;
    public final static  int FUNC_ROLLBACK_NWIN = 74;

    // view
    public final static int FUNC_CRT_VIEW = 80;
    public final static int FUNC_DEL_VIEW = 81;
    public final static int FUNC_MNT_VIEW = 82;
    public final static int FUNC_UMNT_VIEW = 83;

    // pool
    public final static int FUNC_CRT_POOL = 90;
    public final static int FUNC_DEL_POOL = 91;

    // mj
    public final static int FUNC_CRT_MJ = 100;
    public final static int FUNC_DEL_MJ = 101;
    public final static int FUNC_MOD_MJ = 102;
    public final static int FUNC_START_MJ = 103;
    public final static int FUNC_STOP_MJ = 104;
    public final static int FUNC_MONITOR_MJ = 105;
    public final static int FUNC_CRT_MJ1 = 106;
    public final static int FUNC_QUICK_START_MJ = 107;
    public final static int FUNC_BATCH_START_STOP_MJ = 108;
    public final static int FUNC_BATCH_CREATE_MJ = 109;
    public final static int FUNC_CRT_MJ_SCH = 142;
    public final static int FUNC_MOD_MJ_SCH = 143;   // 修改镜像时间计划
    public final static int FUNC_DEL_MJ_SCH = 144;   // 删除镜像时间计划
    public final static int FUNC_SETUP_MJ_SCH = 145;
    public final static int FUNC_BATCH_MOD_MJ = 1090;
    public final static int FUNC_CRT_LMJ = 1000;

    // src uws node
    public final static  int FUNC_MOD_SRC_UWS_SRV = 110;

    // copy job (cj)
    public final static int FUNC_CRT_CJ = 111;
    public final static int FUNC_DEL_CJ = 112;
    public final static int FUNC_MOD_CJ = 113;
    public final static int FUNC_START_CJ = 114;
    public final static int FUNC_STOP_CJ = 115;
    public final static int FUNC_MONITOR_CJ = 116;
    public final static int FUNC_QUICK_START_CJ = 117;

    // data dup
    public final static int FUNC_ADD_PROF = 120;
    public final static int FUNC_MOD_PROF = 121;
    public final static int FUNC_DEL_PROF = 122;
    public final static int FUNC_RENAME_PROF = 123;
    public final static int FUNC_RUN_PROF    = 124;
    public final static int FUNC_VERIFY_PROF = 125;

    public final static int FUNC_ADD_SCH = 126;
    public final static int FUNC_ADD_ADSCH = 1260;
    public final static int FUNC_MOD_SCH = 127;
    public final static int FUNC_DEL_SCH = 128;
    public final static int FUNC_DUP_LOG = 129;
    public final static int FUNC_MONITOR_DD = 130;

    // unlimited incremental snapshot
    public final static int FUNC_CLONE_DISK = 131;
    public final static int FUNC_DEL_UI_MIRROR_VOL = 132;
    public final static int FUNC_DEL_CLONE_DISK = 133;
    public final static int FUNC_QUEYR_UISNAP = 134;
    // public final static int FUNC_DEL_CJ_VOL = 135;  // 删除copy job产生的镜像卷及其克隆盘

    // win op(physical protect)
    public final static  int FUNC_WIN_PHY_INIT          = 135;
    public final static  int FUNC_WIN_PHY_FAILOVER      = 136;
    public final static  int FUNC_WIN_PHY_FAILBACK      = 137;
    public final static  int FUNC_WIN_PHY_RESTORE_DISK  = 138;

    // modify profile( physical protect )
    public final static  int FUNC_PHY_MOD_PROFILE  = 139;

    public final static  int FUNC_PHY_QUERY_INIT_PROGRESS = 140;
    public final static  int FUNC_PHY_QUERY_SYNC_STATE = 141;
    public final static  int FUNC_WIN_PHY_SWITCH_TO_NETDISK = 146;
    public final static  int FUNC_WIN_PHY_DUP_NETDISK = 147;
    public final static  int FUNC_WIN_PHY_SWITCH_TO_LOCALDISK = 148;

    public final static  int FUNC_WIN_SWITCH_TO_NETDISK = 149;
    public final static  int FUNC_WIN_DUP_NETDISK = 150;
    public final static  int FUNC_WIN_SWITCH_TO_LOCALDISK = 151;
    public final static  int FUNC_PHY_START_AUTO_CRT_SNAP = 152;
    public final static  int FUNC_AMS_REBUILD_MIRROR = 153;

    //ams
    public final static  int FUNC_PHY_QUERY_AMS_INIT_PROGRESS = 163;

    // support cluster
    public final static int FUNC_CLUSTER_PHY_WIN_INIT = 160;
    public final static int FUNC_CLUSTER_PHY_WIN_FAILOVER = 161;

    // misc.
    public final static int FUNC_CANCEL_JOB = 298;
    public final static int FUNC_SHUTDOWN = 299;
    public final static int FUNC_SNAP_TREE = 300;
    public final static  int FUNC_USER_MGR  = 297;
    public final static  int FUNC_USER_AUDIT = 296;
    public final static int FUNC_USER_MJOB = 302;
    public final static int FUNC_REFRESH = 301;

    public final static int FUNC_CRT_VMHOST = 400;
    public final static int FUNC_MOD_VMHOST = 401;
    public final static int FUNC_DEL_VMHOST = 402;
    public final static int FUNC_POWERON_VMHOST = 403;
    public final static int FUNC_POWEROFF_VMHOST = 404;
    public final static int FUNC_WIN_VMHOST_SWITCHNETDISK = 405;
    public final static int FUNC_ENABLE_IBOOT_VMHOST_DISK = 406;
    public final static int FUNC_DEL_VMHOST_IP = 407;
    public final static int FUNC_RECOVER_VMHOST_IP = 408;
    public final static int FUNC_SEE_VMHOST_IPCONFIG = 409;
    public final static int FUNC_CRT_VM_MACHINE = 410;
    public final static int FUNC_WIN_VMHOST_QUICK_LOGON_DATA_DISK = 411;
    public final static int FUNC_SET_VMSERVICE_INFO = 412;
    // menu
    public final static int INDEX_FILE   = 0;
    public final static int INDEX_LOGICAL_PROTECT = 1;
    public final static int INDEX_PHYSICAL_PROTECT = 2;
    public final static int INDEX_DEV    = 3;
    public final static int INDEX_REMOTE_DR = 4;
    public final static int INDEX_SETUP  = 5;
    public final static int INDEX_HELP   = 6;

    JMenu[] mainMenu = new JMenu[]{
        new JMenu(ResourceCenter.MENU_FILE),  // file
        new JMenu(ResourceCenter.MENU_LOGICAL_PROTECT),  // logical protect
        new JMenu(ResourceCenter.MENU_PHYSICAL_PROTECT), // physical protect
        new JMenu(ResourceCenter.MENU_DEV),   // device
        new JMenu(ResourceCenter.MENU_RDR),   // remote DR
        new JMenu(ResourceCenter.MENU_SETUP), // setup
        new JMenu(ResourceCenter.MENU_HELP)   // help
    };

    // file menu
    CustomMenuItem jMenuExit;
    CustomMenuItem jMenuLogin;
    CustomMenuItem jMenuProperty;
    BrowserToolMenuItem jMenuLogout;

    // win op menu(logical protect)
    JMenu jMenuWinPro = new JMenu();
    CustomMenuItem jMenuInit;
    JMenu jMenuDR = new JMenu();
    CustomMenuItem  jMenuFailover;    // data disater from local disk to iscsi
    CustomMenuItem  jMenuSwitchToNetDisk; //switch disk from local disk to iscsi
    CustomMenuItem  jMenuFailback;    // data disater from iscsi to local disk
    CustomMenuItem  jMenuSwitchToLocalDisk; //switch disk from iscsi to local disk
    CustomMenuItem  jMenuRestoreData; // migrate data to orginal disk
    CustomMenuItem  jMenuDuplicateDataFromIscsiToLocaldisk; // duplicate data from iscsi to local disk

    // non-win op menu(logical protect)
    JMenu jMenuNonWinPro = new JMenu();
    CustomMenuItem  jMenuInitNWinHost;
    CustomMenuItem  jMenuInitNWinAmsHost;
    CustomMenuItem  jMenuInitNWinLvmHost;
    CustomMenuItem  jMenuSelBootVer;
    CustomMenuItem  jMenuRestDataForNWinHost;
    CustomMenuItem  jMenuIBootForLinux;
    CustomMenuItem  jMenuSelBootVerams;
    CustomMenuItem  jMenuRestDataForNWinHostams;
    CustomMenuItem  jMenuIBootForLinuxams;

    // win op menu(physical protect)
    JMenu jMenuWinPhyPro = new JMenu();
    JMenu jMenuNonWinPhyPro = new JMenu();
    CustomMenuItem jMenuWinPhyInit;
    JMenu jMenuWinPhyDR = new JMenu();
    CustomMenuItem  jMenuWinPhyFailover;    // data disater from local disk to iscsi
    CustomMenuItem  jMenuWinPhySwitchToNetDisk;  // switch disk from local disk to iscsi
    CustomMenuItem  jMenuWinPhyFailback;    // data disater from iscsi to local disk
    CustomMenuItem  jMenuWinPhySwitchToLocalDisk; // switch disk from iscsi to local disk
    CustomMenuItem  jMenuWinPhyRestoreData; // migrate data to orginal disk
    CustomMenuItem  jMenuWinPhyDuplicateDataFromIscsiToLocaldisk; // duplicate data from iscsi to local disk
    CustomMenuItem  jMenuWinPhyQuerySyncState; // query volume's sync state
    CustomMenuItem  jMenuWinPhyQueryInitState; // query initializing volume's progress
    CustomMenuItem  jMenuWinPhyStartAutoCrtSnap; // start auto-crt-snap process( start_mirror )
    CustomMenuItem  jMenuAmsRebuildMirror ;     //rebuild linux data mirror

    //ams
    CustomMenuItem jMenuNotWinPhyQueryInitState;

    // support cluster
    CustomMenuItem jMenuClusterWinPhyInit;
    CustomMenuItem jMenuClusterWinPhyFailover;

    // device menu
    JMenu jMenuVol = new JMenu();  // volume submenu
    CustomMenuItem  jMenuCrtVol;
    CustomMenuItem  jMenuDelVol;
    CustomMenuItem  jMenuOnlineVol;
    CustomMenuItem  jMenuOfflineVol;

    JMenu jMenuView = new JMenu(); // view submenu
    CustomMenuItem jMenuCrtView;
    CustomMenuItem jMenuDelView;
    CustomMenuItem jMenuMntView;
    CustomMenuItem jMenuUMntView;

    JMenu jMenuSnap = new JMenu(); // snapshot submenu
    CustomMenuItem  jMenuCrtSnap;
    CustomMenuItem  jMenuCrtSyncSnap;
    CustomMenuItem  jMenuCrtAsyncSnap;
    CustomMenuItem  jMenuDelSnap;
    CustomMenuItem  jMenuMntSnap;
    CustomMenuItem  jMenuEvrSnap;
    CustomMenuItem  jMenuCrtUcsSnap;
        
    JMenu jMenuLunMap = new JMenu(); // lunmap submenu
    CustomMenuItem jMenuCrtLm;
    CustomMenuItem jMenuDelLm;

    CustomMenuItem jMenuExpView;

    JMenu jMenuPool = new JMenu(); // pool submenu
    CustomMenuItem jMenuCrtPool;
    CustomMenuItem jMenuDelPool;

    // setup Menu
    JMenu jMenuHost = new JMenu(); // host submenu
    CustomMenuItem jMenuCrtHost;
    CustomMenuItem jMenuDelHost;
    CustomMenuItem jMenuModHost;
    CustomMenuItem jMenuModHostBoot;
    CustomMenuItem jMenuDelCluster;
    CustomMenuItem jMenuModCluster;

    CustomMenuItem jMenuModSrcUWSrv; // 修改源端UWS server
    BrowserToolMenuItem jMenuDhcpSet;
    BrowserToolMenuItem jMenuMsgReport;
    BrowserToolMenuItem jMenuModPass;
    BrowserToolMenuItem jMenuAdminOpt;
    CustomMenuItem jMenuDelDstAgnt;

    JMenu jMenuUser = new JMenu(); // user submenu
    BrowserToolMenuItem jMenuCrtUser;
    BrowserToolMenuItem jMenuDelUser;
    BrowserToolMenuItem jMenuModUser;
    BrowserToolMenuItem jMenuModPasswd;

    //add start odysys qiaojian 20091012
    JMenu jMenuSDHM = new JMenu(); //SDHM submenu
    CustomMenuItem jMenuSDHMInfo;
    CustomMenuItem jMenuSDHMSet;
    CustomMenuItem jMenuSDHMReport;
    CustomMenuItem jMenuSDHMMirror;
    //add end odysys qiaojian 20091012

    BrowserToolMenuItem jMenuUserMgr;
    BrowserToolMenuItem jMenuAudit;
    BrowserToolMenuItem jMenuMjob;
    BrowserToolMenuItem shutdown; // shutdown uws server

    // data dup
    JMenu jMenuProf = new JMenu(); // profile
    CustomMenuItem addProfile;
    CustomMenuItem modProfile;
    CustomMenuItem modPhyProfile; // modify profile for physical protect
    CustomMenuItem delProfile;
    CustomMenuItem runProfile;
    CustomMenuItem verifyProfile;
    CustomMenuItem renameProfile;

    JMenu jMenuSch = new JMenu();  // schedule
    CustomMenuItem addSch;
    CustomMenuItem addADSch;
    CustomMenuItem modSch;
    CustomMenuItem delSch;
    BrowserToolMenuItem dupLog;   // browse log
    BrowserToolMenuItem monitorDup; // monitor task

    CustomMenuItem jMenuSnapTree; // snapshot tree operation

    // about menu
    BrowserToolMenuItem jMenuAboutBackup;

    ////////////////////////////////////////////
    //
    //    Remote Data Disaster Management
    //
    ////////////////////////////////////////////
    // remote mirror server
    CustomMenuItem jMenuCrtUWS;
    CustomMenuItem jMenuDelUWS;
    CustomMenuItem jMenuModUWS;

    // mirror job
    CustomMenuItem jMenuCrtMj;
    CustomMenuItem jMenuCrtMj1;
    CustomMenuItem jMenuDelMj;
    CustomMenuItem jMenuModMj;
    CustomMenuItem jMenuStartMj;
    CustomMenuItem jMenuStopMj;
    CustomMenuItem jMenuMonMj;
    CustomMenuItem jMenuQStartMj;
    CustomMenuItem jMenuBatchedStartStopMj;
    CustomMenuItem jMenuBatchedCrtMj;
    CustomMenuItem jMenuBatchedModMj;
    CustomMenuItem jMenuSetupMjSch;
    CustomMenuItem jMenuCrtLMj;

    // copy job
    CustomMenuItem jMenuCrtCj;
    CustomMenuItem jMenuDelCj;
    CustomMenuItem jMenuModCj;
    CustomMenuItem jMenuStartCj;
    CustomMenuItem jMenuQStartCj;
    CustomMenuItem jMenuStopCj;
    CustomMenuItem jMenuMonCj;

    // roll back
    CustomMenuItem  jMenuRollbackVol;
    CustomMenuItem  jMenuRollbackForNWin;   // rollback from linux SourceAgent to BootHost
    CustomMenuItem  jMenuRollback;    // rollback from windows SourceAgent to BootHost
    CustomMenuItem  jMenuDelSrcAgnt;

    // unlimited incremental snapshot
    CustomMenuItem jMenuCloneDisk;
    CustomMenuItem jMenuDelUiMirrorVol;
    CustomMenuItem jMenuDelCloneDisk;
    CustomMenuItem jMenuQueryUISnap; // QueryUISnapAction

    // mirror job scheduler
    CustomMenuItem jMenuCrtMjSch;
    CustomMenuItem jMenuModMjSch;
    CustomMenuItem jMenuDelMjSch;

    //VMHostManager
    CustomMenuItem addVMHost;
    CustomMenuItem modVMHost;
    CustomMenuItem delVMHost;
    CustomMenuItem powerOnVMHost;
    CustomMenuItem powerOffVMHost;
    CustomMenuItem switchNetDisk;
    CustomMenuItem enableIbootView;
    CustomMenuItem delVMHostIP;
    CustomMenuItem recoverVMHostIP;
    CustomMenuItem showVMHostIPInfo;
    CustomMenuItem crtVMMachine;
    CustomMenuItem quickLogonDataDisk;
    BrowserToolMenuItem jMenuSetVMServiceInfo;
    
    BrowserToolButton btnLogin;
    BrowserToolButton btnLogout;
    BrowserToolButton btnInit;
    BrowserToolButton btnFailover;
    BrowserToolButton btnRstOriData;
    BrowserToolButton btnUWSRpt;
    BrowserToolButton btnMonitor;
    BrowserToolButton btnExit;
    BrowserToolButton btnDupLog;
    BrowserToolButton btnMon;
    BrowserToolButton btnRefresh;

    SanBootView view;
    BrowserTree tree;
    Map<Integer,Object> funcMap = new HashMap<Integer,Object>(); // 右键功能的HashMap

    public MenuAndBtnCenterForMainUi( SanBootView _view ) {
        view = _view;
        tree = view.getTree();
    }

    boolean isFirst = true;
    public void languageSet(){
        view.setTitle( SanBootView.res.getString( "View.frameTitle" ) );

        mainMenu[INDEX_FILE].setText(SanBootView.res.getString("View.Menu.file"));
        jMenuExit.setText(SanBootView.res.getString("View.MenuItem.exit"));
        jMenuLogin.setText(SanBootView.res.getString("View.MenuItem.connect"));
        jMenuLogout.setText(SanBootView.res.getString("View.MenuItem.disConnect"));
        jMenuProperty.setText(SanBootView.res.getString("View.MenuItem.property"));
        mainMenu[INDEX_LOGICAL_PROTECT].setText(SanBootView.res.getString("View.Menu.logicalPro"));
        mainMenu[INDEX_PHYSICAL_PROTECT].setText(SanBootView.res.getString("View.Menu.physicalPro"));
        jMenuWinPro.setText(SanBootView.res.getString("View.Menu.winop"));
        jMenuWinPhyPro.setText(SanBootView.res.getString("View.Menu.winop"));
        jMenuNonWinPro.setText(SanBootView.res.getString("View.Menu.nwinop"));
        jMenuNonWinPhyPro.setText(SanBootView.res.getString("View.Menu.nwinop"));
        jMenuInit.setText( SanBootView.res.getString("View.MenuItem.init") );
        jMenuWinPhyInit.setText( SanBootView.res.getString("View.MenuItem.init") );
        jMenuInit.setPopMenuText( SanBootView.res.getString("View.MenuItem.init1") );
        jMenuWinPhyInit.setPopMenuText( SanBootView.res.getString("View.MenuItem.init3") );
        jMenuDR.setText( SanBootView.res.getString("View.MenuItem.dataDisater") );
        jMenuWinPhyDR.setText( SanBootView.res.getString("View.MenuItem.dataDisater") );
        jMenuFailover.setText( SanBootView.res.getString("View.MenuItem.failover") );
        jMenuSwitchToNetDisk.setText( SanBootView.res.getString("View.MenuItem.switchDisk"));
        jMenuWinPhyFailover.setText( SanBootView.res.getString("View.MenuItem.failover") );
        jMenuWinPhySwitchToNetDisk.setText( SanBootView.res.getString("View.MenuItem.switchDisk"));
        jMenuWinPhySwitchToLocalDisk.setText( SanBootView.res.getString("View.MenuItem.switchDisk1"));
        jMenuSwitchToLocalDisk.setText( SanBootView.res.getString("View.MenuItem.switchDisk1"));
        jMenuWinPhyDuplicateDataFromIscsiToLocaldisk.setText(SanBootView.res.getString("View.MenuItem.cpnetdisk"));
        jMenuDuplicateDataFromIscsiToLocaldisk.setText( SanBootView.res.getString("View.MenuItem.cpnetdisk"));
        jMenuFailback.setText( SanBootView.res.getString("View.MenuItem.failback") );
        jMenuWinPhyFailback.setText( SanBootView.res.getString("View.MenuItem.failback") );
        jMenuRestoreData.setText( SanBootView.res.getString("View.MenuItem.rstOriData"));
        jMenuWinPhyRestoreData.setText( SanBootView.res.getString("View.MenuItem.rstOriData"));
        jMenuWinPhyQuerySyncState.setText( SanBootView.res.getString("View.MenuItem.querySyncState") );
        jMenuWinPhyStartAutoCrtSnap.setText( SanBootView.res.getString("View.MenuItem.startAutoCrtSnap") );
        jMenuWinPhyQueryInitState.setText( SanBootView.res.getString("View.MenuItem.queryInitState") );
        jMenuNotWinPhyQueryInitState.setText( SanBootView.res.getString("View.MenuItem.queryInitState") );
        jMenuClusterWinPhyInit.setText( SanBootView.res.getString("View.MenuItem.init4") );
        jMenuClusterWinPhyFailover.setText( SanBootView.res.getString("View.MenuItem.failover"));
        jMenuRollback.setText( SanBootView.res.getString("View.MenuItem.rollback") );
        jMenuInitNWinHost.setText( SanBootView.res.getString("View.MenuItem.init") );
        jMenuInitNWinHost.setPopMenuText( SanBootView.res.getString("View.MenuItem.init2") );
        jMenuInitNWinAmsHost.setText( SanBootView.res.getString("View.MenuItem.init") );
        jMenuInitNWinAmsHost.setPopMenuText( SanBootView.res.getString("View.MenuItem.init5") );
        jMenuInitNWinLvmHost.setText( SanBootView.res.getString("View.MenuItem.init") );
        jMenuInitNWinLvmHost.setPopMenuText( SanBootView.res.getString("View.MenuItem.init6") );
        jMenuSelBootVer.setText( SanBootView.res.getString("View.MenuItem.selBootVer") );
        jMenuIBootForLinux.setText( SanBootView.res.getString("View.MenuItem.ibootForLinux") );
        jMenuRestDataForNWinHost.setText( SanBootView.res.getString("View.MenuItem.rstOriData") );
        jMenuSelBootVerams.setText( SanBootView.res.getString("View.MenuItem.selBootVer") );
        jMenuIBootForLinuxams.setText( SanBootView.res.getString("View.MenuItem.ibootForLinux") );
        jMenuRestDataForNWinHostams.setText( SanBootView.res.getString("View.MenuItem.rstOriData") );
        jMenuRollbackForNWin.setText( SanBootView.res.getString("View.MenuItem.rollbacknwin") );
        jMenuProf.setText(SanBootView.res.getString("View.Menu.prof"));
        jMenuProf.setIcon( ResourceCenter.ICON_PROFILE );
        jMenuAmsRebuildMirror.setText(SanBootView.res.getString("View.MenuItem.rebuildMirror"));
        addProfile.setText(SanBootView.res.getString("View.MenuItem.addProf"));
        modProfile.setText(SanBootView.res.getString("View.MenuItem.modProf"));
        modPhyProfile.setText(SanBootView.res.getString("View.MenuItem.modProf"));
        delProfile.setText(SanBootView.res.getString("View.MenuItem.delProf"));
        runProfile.setText( SanBootView.res.getString("View.MenuItem.runProf"));
        verifyProfile.setText( SanBootView.res.getString("View.MenuItem.verifyProf"));
        renameProfile.setText( SanBootView.res.getString("View.MenuItem.renameProf"));
        jMenuSch.setText(SanBootView.res.getString("View.Menu.sch"));
        jMenuSch.setIcon( ResourceCenter.SMALL_SCH );
        addSch.setText(SanBootView.res.getString("View.MenuItem.addSch"));
        addADSch.setText(SanBootView.res.getString("View.MenuItem.addADSch"));
        modSch.setText(SanBootView.res.getString("View.MenuItem.modSch"));
        delSch.setText(SanBootView.res.getString("View.MenuItem.delSch"));
        dupLog.setText(SanBootView.res.getString("View.MenuItem.duplog"));
        monitorDup.setText(SanBootView.res.getString("View.MenuItem.mon"));
        jMenuSnapTree.setText(SanBootView.res.getString("View.MenuItem.snaptree"));
        mainMenu[INDEX_DEV].setText( SanBootView.res.getString("View.Menu.device") );
        jMenuVol.setText( SanBootView.res.getString("View.Menu.vol"));
        jMenuCrtVol.setText( SanBootView.res.getString("View.MenuItem.crtVol") );
        jMenuDelVol.setText( SanBootView.res.getString("View.MenuItem.delVol") );
        jMenuOnlineVol.setText( SanBootView.res.getString("View.MenuItem.onlineDev"));
        jMenuOfflineVol.setText( SanBootView.res.getString("View.MenuItem.offlineDev"));
        jMenuRollbackVol.setText( SanBootView.res.getString("View.MenuItem.rollbackvol") );
        jMenuView.setText( SanBootView.res.getString("View.Menu.view"));
        jMenuCrtView.setText( SanBootView.res.getString("View.MenuItem.crtView") );
        jMenuDelView.setText( SanBootView.res.getString("View.MenuItem.delView") );
        jMenuMntView.setText( SanBootView.res.getString("View.MenuItem.mntView") );
        jMenuUMntView.setText( SanBootView.res.getString("View.MenuItem.umntView") );
        jMenuSnap.setText( SanBootView.res.getString("View.Menu.snap") );
        jMenuCrtSnap.setText( SanBootView.res.getString("View.MenuItem.crtSnap") );
        jMenuCrtSyncSnap.setText( SanBootView.res.getString("View.MenuItem.crtSnap1"));
        jMenuCrtAsyncSnap.setText( SanBootView.res.getString("View.MenuItem.crtSnap2"));
        jMenuDelSnap.setText( SanBootView.res.getString("View.MenuItem.delSnap") );
        jMenuEvrSnap.setText( SanBootView.res.getString("View.MenuItem.modSnap") );
        jMenuMntSnap.setText( SanBootView.res.getString("View.MenuItem.mntSnap"));
        jMenuLunMap.setText( SanBootView.res.getString("View.Menu.lm") );
        jMenuCrtLm.setText( SanBootView.res.getString("View.MenuItem.crtLm") );
        jMenuExpView.setText( SanBootView.res.getString("View.MenuItem.ExportView") );
        jMenuDelLm.setText( SanBootView.res.getString("View.MenuItem.delLm") );
        jMenuCrtMj.setText( SanBootView.res.getString("View.MenuItem.crtMj"));
        jMenuCrtLMj.setText( SanBootView.res.getString("View.MenuItem.crtLMj"));
        jMenuCrtMj1.setText( SanBootView.res.getString("View.MenuItem.crtIncMj"));
        jMenuDelMj.setText( SanBootView.res.getString("View.MenuItem.delMj"));
        jMenuModMj.setText( SanBootView.res.getString("View.MenuItem.modMj"));
        jMenuStartMj.setText( SanBootView.res.getString("View.MenuItem.startMj"));
        jMenuQStartMj.setText( SanBootView.res.getString("View.MenuItem.qstartMj"));
        jMenuBatchedStartStopMj.setText( SanBootView.res.getString("View.MenuItem.startStopMjInBatch") );
        jMenuBatchedCrtMj.setText( SanBootView.res.getString("View.MenuItem.crtMjInBatch") );
        jMenuBatchedModMj.setText( SanBootView.res.getString("View.MenuItem.modMjInBatch") );
        jMenuSetupMjSch.setText( SanBootView.res.getString("View.MenuItem.setupMjSch") );
        jMenuStopMj.setText( SanBootView.res.getString("View.MenuItem.stopMj"));
        jMenuMonMj.setText( SanBootView.res.getString("View.MenuItem.monMj"));
        jMenuCrtCj.setText( SanBootView.res.getString("View.MenuItem.crtCj"));
        jMenuDelCj.setText( SanBootView.res.getString("View.MenuItem.delCj"));
        jMenuModCj.setText( SanBootView.res.getString("View.MenuItem.modCj"));
        jMenuStartCj.setText( SanBootView.res.getString("View.MenuItem.startCj"));
        jMenuQStartCj.setText( SanBootView.res.getString("View.MenuItem.qstartCj"));
        jMenuStopCj.setText( SanBootView.res.getString("View.MenuItem.stopCj"));
        jMenuMonCj.setText( SanBootView.res.getString("View.MenuItem.monCj"));
        jMenuPool.setText( SanBootView.res.getString("View.MenuItem.pool"));
        jMenuCrtPool.setText( SanBootView.res.getString("View.MenuItem.crtPool"));
        jMenuDelPool.setText( SanBootView.res.getString("View.MenuItem.delPool"));
        jMenuCrtMjSch.setText( SanBootView.res.getString("View.MenuItem.crtMjSch"));
        jMenuModMjSch.setText( SanBootView.res.getString("View.MenuItem.modMjSch"));
        jMenuDelMjSch.setText( SanBootView.res.getString("View.MenuItem.delMjSch"));
        mainMenu[INDEX_REMOTE_DR].setText( SanBootView.res.getString("View.Menu.rdr") );
        mainMenu[INDEX_SETUP].setText(SanBootView.res.getString("View.Menu.setup"));
        jMenuHost.setText( SanBootView.res.getString("View.Menu.host") );
        jMenuHost.setIcon( ResourceCenter.SMALL_HOST );
        jMenuCrtHost.setText( SanBootView.res.getString("View.MenuItem.crtHost") );
        jMenuDelHost.setText( SanBootView.res.getString("View.MenuItem.delHost") );
        jMenuModHost.setText( SanBootView.res.getString("View.MenuItem.modHost"));
        jMenuModHostBoot.setText( SanBootView.res.getString("View.MenuItem.modHostBoot"));
        jMenuDelCluster.setText( SanBootView.res.getString("View.MenuItem.delCluster") );
        jMenuModCluster.setText( SanBootView.res.getString("View.MenuItem.modCluster") );
        jMenuCrtUWS.setText( SanBootView.res.getString("View.MenuItem.crtSWU") );
        jMenuDelUWS.setText( SanBootView.res.getString("View.MenuItem.delSWU") );
        jMenuModUWS.setText( SanBootView.res.getString("View.MenuItem.modSWU") );
        jMenuModSrcUWSrv.setText( SanBootView.res.getString("View.MenuItem.modSrcSWU"));
        jMenuDhcpSet.setText( SanBootView.res.getString("View.MenuItem.dhcp") );
        jMenuMsgReport.setText( SanBootView.res.getString("View.MenuItem.msgReport"));
        jMenuModPass.setText(SanBootView.res.getString("View.MenuItem.modPass"));
        jMenuAdminOpt.setText( SanBootView.res.getString("View.MenuItem.adminOpt"));
        jMenuDelDstAgnt.setText( SanBootView.res.getString("View.MenuItem.delDstAgnt"));
        jMenuDelDstAgnt.setPopMenuText( SanBootView.res.getString("View.MenuItem.delDstAgnt") );
        jMenuDelSrcAgnt.setText( SanBootView.res.getString("View.MenuItem.delRHost") );
        jMenuUser.setText( SanBootView.res.getString("View.Menu.user"));
        jMenuCrtUser.setText( SanBootView.res.getString("View.MenuItem.crtUser"));
        jMenuDelUser.setText( SanBootView.res.getString("View.MenuItem.delUser"));
        jMenuModUser.setText( SanBootView.res.getString("View.MenuItem.modUser"));
        jMenuModPasswd.setText( SanBootView.res.getString("View.MenuItem.modPasswd"));
		//add start odysys qiaojian 20091012
        jMenuSDHM.setText( SanBootView.res.getString("View.Menu.sdhm") );
        jMenuSDHM.setIcon( ResourceCenter.SMALL_HOST );
        jMenuSDHMInfo.setText( SanBootView.res.getString("View.MenuItem.sdhmInfo") );
        jMenuSDHMSet.setText( SanBootView.res.getString("View.MenuItem.sdhmSet") );
        jMenuSDHMReport.setText( SanBootView.res.getString("View.MenuItem.sdhmReport"));
        jMenuSDHMMirror.setText( SanBootView.res.getString("View.MenuItem.sdhmMirror"));
        //add end odysys qiaojian 20091012

        jMenuUserMgr.setText( SanBootView.res.getString("View.MenuItem.userMgr"));
        jMenuAudit.setText( SanBootView.res.getString("View.MenuItem.audit"));
        jMenuMjob.setText( SanBootView.res.getString("View.MenuItem.mjob"));
        shutdown.setText( SanBootView.res.getString("RightCustomDialog.checkbox.shutdown") );

        mainMenu[INDEX_HELP].setText(SanBootView.res.getString("View.Menu.help"));
        jMenuAboutBackup.setText(SanBootView.res.getString("View.MenuItem.about"));

        jMenuCloneDisk.setText( SanBootView.res.getString("View.MenuItem.clonedisk"));
        jMenuDelUiMirrorVol.setText(SanBootView.res.getString("View.MenuItem.deluimirvol"));
        jMenuDelCloneDisk.setText(SanBootView.res.getString("View.MenuItem.delCloneDisk"));
        jMenuQueryUISnap.setText( SanBootView.res.getString("View.MenuItem.queryUISnap"));

        btnLogin.setToolTipText(SanBootView.res.getString("View.tooltip.connect"));
        btnLogout.setToolTipText(SanBootView.res.getString("View.tooltip.disconnect"));
        btnExit.setToolTipText(SanBootView.res.getString("View.tooltip.exit"));
        btnInit.setToolTipText( SanBootView.res.getString("View.tooltip.initHost") );
        btnFailover.setToolTipText( SanBootView.res.getString("View.tooltip.failover"));
        btnRstOriData.setToolTipText( SanBootView.res.getString("View.tooltip.rstOriData") );
        btnUWSRpt.setToolTipText( SanBootView.res.getString("View.tooltip.productReport") );
        btnDupLog.setToolTipText( SanBootView.res.getString("View.tooltip.duplog"));
        btnMon.setToolTipText( SanBootView.res.getString("View.tooltip.mon"));
        btnRefresh.setToolTipText(SanBootView.res.getString("View.tooltip.refresh"));
        
        jMenuCrtUcsSnap.setText(SanBootView.res.getString("View.menuItem.UcsForword"));
        
        //vmmanager
        addVMHost.setText( SanBootView.res.getString( "View.MenuItem.addVMHost" ) );
        modVMHost.setText( SanBootView.res.getString( "View.MenuItem.modVMHost" ) );
        delVMHost.setText( SanBootView.res.getString( "View.MenuItem.delVMHost" ) );
        powerOnVMHost.setText( SanBootView.res.getString( "View.MenuItem.powerOnVMHost" ) );
        powerOffVMHost.setText( SanBootView.res.getString( "View.MenuItem.powerOffVMHost" ) );
        switchNetDisk.setText( SanBootView.res.getString( "View.MenuItem.switchDisk" ) );
        enableIbootView.setText( SanBootView.res.getString( "View.MenuItem.enableIboot" ) );
        delVMHostIP.setText( SanBootView.res.getString( "View.MenuItem.delVMHostIP" ) );
        recoverVMHostIP.setText( SanBootView.res.getString( "View.MenuItem.recoverVMHostIP" ) );
        showVMHostIPInfo.setText( SanBootView.res.getString( "View.MenuItem.seeVMHostIP" ) );
        crtVMMachine.setText( SanBootView.res.getString( "View.MenuItem.crtVMMachine" ) );
        quickLogonDataDisk.setText( SanBootView.res.getString( "View.MenuItem.quickLogonDataDisk") );
        jMenuSetVMServiceInfo.setText( SanBootView.res.getString( "View.MenuItem.setVMServiceInfo") );
        if( isFirst ){
            view.setTreeRootNode( tree ); // initialize root node for tree
            isFirst = false;
        }
    }

    public void init(){
        generateMenuAndBtn();
        addMenuToView();
        addButtonToView();
    }

    private void generateMenuAndBtn(){
        LoginAction loginAction = new LoginAction();
        loginAction.setView( view );
        jMenuLogin = new CustomMenuItem(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_C,Event.ALT_MASK
            ),
            loginAction
        );
        btnLogin = new BrowserToolButton(loginAction);

        LogoutAction logoutAction = new LogoutAction();
        logoutAction.setView( view );
        jMenuLogout = new BrowserToolMenuItem( logoutAction );
        btnLogout = new BrowserToolButton( logoutAction );

        PropertyAction propertyAction = new PropertyAction();
        propertyAction.setView( view );
        jMenuProperty = new CustomMenuItem( propertyAction );

        ExitAction exitAction = new ExitAction();
        exitAction.setView( view );
        jMenuExit = new CustomMenuItem(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_X,Event.ALT_MASK
            ),
            exitAction
        );
        btnExit = new BrowserToolButton(exitAction);

        InitHostAction initAct = new InitHostAction();
        initAct.setView( view );
        jMenuInit = new CustomMenuItem( initAct );
        btnInit =  new BrowserToolButton( initAct );

        InitHostByPhyProtectAction phyInitAct = new InitHostByPhyProtectAction();
        phyInitAct.setView( view );
        this.jMenuWinPhyInit = new CustomMenuItem( phyInitAct );

        InitNWinHostAction initNWinAct = new InitNWinHostAction();
        initNWinAct.setView( view );
        jMenuInitNWinHost = new CustomMenuItem( initNWinAct );
        
        InitHostByAmsAction initNWinAmsAct = new InitHostByAmsAction();
        initNWinAmsAct.setView( view );
        jMenuInitNWinAmsHost = new CustomMenuItem( initNWinAmsAct );

        InitHostByLvmAction inithostByLvmAct = new InitHostByLvmAction();
        inithostByLvmAct.setView(view);
        jMenuInitNWinLvmHost = new CustomMenuItem(inithostByLvmAct);

        SelectBootVerAction selAct = new SelectBootVerAction();
        selAct.setView( view );
        jMenuSelBootVer = new CustomMenuItem( selAct );
        jMenuSelBootVerams = new CustomMenuItem( selAct );

        IBootForLinuxAction ibootForLnx = new IBootForLinuxAction();
        ibootForLnx.setView(  view );
        jMenuIBootForLinux = new CustomMenuItem( ibootForLnx );
        jMenuIBootForLinuxams = new CustomMenuItem( ibootForLnx );

        // data disater from local disk to iscsi
        FailoverAction failoverAction = new FailoverAction();
        failoverAction.setView( view );
        jMenuFailover =  new CustomMenuItem( failoverAction );
        btnFailover = new BrowserToolButton( failoverAction );

        FailoverAction switchDiskAction = new FailoverAction( true );
        switchDiskAction.setView( view );
        this.jMenuSwitchToNetDisk =  new CustomMenuItem( switchDiskAction );

        FailoverByPhyProtectAction phyFailoverAction = new FailoverByPhyProtectAction();
        phyFailoverAction.setView( view );
        this.jMenuWinPhyFailover = new CustomMenuItem( phyFailoverAction );

        FailoverByPhyProtectAction phySwitchDiskAction = new FailoverByPhyProtectAction( true );
        phySwitchDiskAction.setView( view );
        this.jMenuWinPhySwitchToNetDisk = new CustomMenuItem( phySwitchDiskAction );

        // data disater from iscsi to local disk
        FailbackAction failbackAction = new FailbackAction();
        failbackAction.setView( view );
        jMenuFailback =  new CustomMenuItem( failbackAction );

        FailbackAction switchToLocalDiskAction = new FailbackAction( true );
        switchToLocalDiskAction.setView( view );
        this.jMenuSwitchToLocalDisk =  new CustomMenuItem( switchToLocalDiskAction );

        FailbackByPhyProtectAction phyFailbackAction = new FailbackByPhyProtectAction();
        phyFailbackAction.setView( view );
        this.jMenuWinPhyFailback =  new CustomMenuItem( phyFailbackAction );

        FailbackByPhyProtectAction phySwitchToLocalDiskAction = new FailbackByPhyProtectAction( true );
        phySwitchToLocalDiskAction.setView( view );
        this.jMenuWinPhySwitchToLocalDisk =  new CustomMenuItem( phySwitchToLocalDiskAction );

        RestoreOriginalData restOriData = new RestoreOriginalData();
        restOriData.setView( view );
        jMenuRestoreData =  new CustomMenuItem( restOriData );
        btnRstOriData = new BrowserToolButton( restOriData );

        RestoreOriginalData dupDataToLocalDisk = new RestoreOriginalData( true );
        dupDataToLocalDisk.setView( view );
        jMenuDuplicateDataFromIscsiToLocaldisk =  new CustomMenuItem( dupDataToLocalDisk );

        RestoreOriginalDataByPhyProtect phyRestOriData = new RestoreOriginalDataByPhyProtect();
        phyRestOriData.setView( view );
        jMenuWinPhyRestoreData =  new CustomMenuItem( phyRestOriData );

        RestoreOriginalDataByPhyProtect phyDupDataToLocalDisk = new RestoreOriginalDataByPhyProtect( true );
        phyDupDataToLocalDisk.setView( view );
        jMenuWinPhyDuplicateDataFromIscsiToLocaldisk =  new CustomMenuItem( phyDupDataToLocalDisk );

        QuerySyncStateAction querySyncState = new QuerySyncStateAction();
        querySyncState.setView( view );
        jMenuWinPhyQuerySyncState = new CustomMenuItem( querySyncState );

        StartAutoCrtSnapshotAction winPhyStartAutoCrtSnap = new StartAutoCrtSnapshotAction();
        winPhyStartAutoCrtSnap.setView( view );
        jMenuWinPhyStartAutoCrtSnap = new CustomMenuItem( winPhyStartAutoCrtSnap );

        RebuildMirrorAction rebuildMirrorAction = new RebuildMirrorAction();
        rebuildMirrorAction.setView(view);
        jMenuAmsRebuildMirror = new CustomMenuItem( rebuildMirrorAction );

        QueryInitProgressAction queryInitProgress = new QueryInitProgressAction();
        queryInitProgress.setView( view );
        jMenuWinPhyQueryInitState = new CustomMenuItem( queryInitProgress );

        QueryAmsInitProgressAction querInitAmsProgress = new QueryAmsInitProgressAction();
        querInitAmsProgress.setView( view );
        jMenuNotWinPhyQueryInitState = new CustomMenuItem( querInitAmsProgress );

        InitClusterForWinByPhyProtectAction initClusterForWin = new InitClusterForWinByPhyProtectAction();
        initClusterForWin.setView( view );
        jMenuClusterWinPhyInit = new CustomMenuItem( initClusterForWin );

        FailoverClusterForWinByPhyProtectAction failoverClusterForPhyWin = new FailoverClusterForWinByPhyProtectAction();
        failoverClusterForPhyWin.setView( view );
        jMenuClusterWinPhyFailover = new CustomMenuItem( failoverClusterForPhyWin );

        RollbackAction rollback = new RollbackAction();
        rollback.setView( view );
        jMenuRollback =  new CustomMenuItem( rollback );

        RestoreOriginalDataForNWin restOriDataForNWin = new RestoreOriginalDataForNWin();
        restOriDataForNWin.setView( view );
        jMenuRestDataForNWinHost =  new CustomMenuItem( restOriDataForNWin );
        jMenuRestDataForNWinHostams =  new CustomMenuItem( restOriDataForNWin );

        RollbackForNWinAction rollback1 = new RollbackForNWinAction();
        rollback1.setView( view );
        jMenuRollbackForNWin =  new CustomMenuItem( rollback1 );

        CrtVolAction crtVolAction = new CrtVolAction();
        crtVolAction.setView( view );
        jMenuCrtVol =  new CustomMenuItem(
            crtVolAction
        );

        DelVolAction delVolAction = new DelVolAction();
        delVolAction.setView( view );
        jMenuDelVol =  new CustomMenuItem(
            delVolAction
        );

        OnlineDevAction onlineVol = new OnlineDevAction();
        onlineVol.setView( view );
        jMenuOnlineVol = new CustomMenuItem(
            onlineVol
        );

        OfflineDevAction offlineVol = new OfflineDevAction();
        offlineVol.setView( view );
        jMenuOfflineVol = new CustomMenuItem(
            offlineVol
        );

        RollbackVolAction rollbackvol = new RollbackVolAction();
        rollbackvol.setView( view);
        jMenuRollbackVol = new CustomMenuItem( rollbackvol );

        CrtViewAction crtViewAction = new CrtViewAction();
        crtViewAction.setView( view );
        jMenuCrtView =  new CustomMenuItem(
            crtViewAction
        );

        DelViewAction delViewAction = new DelViewAction();
        delViewAction.setView( view );
        jMenuDelView =  new CustomMenuItem(
            delViewAction
        );
        
        CrtUcsSnapAction crtUcsSnapAction = new CrtUcsSnapAction();
        crtUcsSnapAction.setView( view );
        jMenuCrtUcsSnap = new CustomMenuItem(
          crtUcsSnapAction      
        );

        MntViewAction mntViewAction = new MntViewAction();
        mntViewAction.setView( view );
        jMenuMntView =  new CustomMenuItem(
            mntViewAction
        );

        UMntViewAction umntViewAction = new UMntViewAction();
        umntViewAction.setView( view );
        jMenuUMntView =  new CustomMenuItem(
            umntViewAction
        );

        CrtSnapAction crtSnapAction = new CrtSnapAction( 1 );
        crtSnapAction.setView( view );
        jMenuCrtSnap =  new CustomMenuItem(
            crtSnapAction
        );
        jMenuCrtSyncSnap =  new CustomMenuItem(
            crtSnapAction
        );

        CrtSnapAction crtSnapAction2 = new CrtSnapAction( 0 );
        crtSnapAction2.setView( view );
        jMenuCrtAsyncSnap =  new CustomMenuItem(
            crtSnapAction2
        );

        DelSnapAction delSnapAction = new DelSnapAction();
        delSnapAction.setView( view );
        jMenuDelSnap =  new CustomMenuItem(
            delSnapAction
        );

        SnapForEverAction everSnapAction = new SnapForEverAction();
        everSnapAction.setView( view );
        jMenuEvrSnap = new CustomMenuItem(
                everSnapAction
        );

        MntSnapAction mntSnapAction = new MntSnapAction();
        mntSnapAction.setView( view );
        jMenuMntSnap =  new CustomMenuItem(
            mntSnapAction
        );

        CrtlmAction crtlmAction = new CrtlmAction();
        crtlmAction.setView( view );
        jMenuCrtLm =  new CustomMenuItem(
            crtlmAction
        );

        ExportViewAction exportViewAction = new ExportViewAction();
        exportViewAction.setView(view);
        jMenuExpView = new CustomMenuItem(
            exportViewAction
        );

        DellmAction dellmAction = new DellmAction();
        dellmAction.setView( view );
        jMenuDelLm =  new CustomMenuItem(
            dellmAction
        );

        CrtMjAction crtMjAction = new CrtMjAction();
        crtMjAction.setView( view );
        jMenuCrtMj =  new CustomMenuItem(
            crtMjAction
        );

        CrtLMjAction crtLMjAction = new CrtLMjAction();
        crtLMjAction.setView( view );
        jMenuCrtLMj = new CustomMenuItem( crtLMjAction );
        
        CrtIncrementMjAction crtIncMjAction = new CrtIncrementMjAction();
        crtIncMjAction.setView( view );
        jMenuCrtMj1 =  new CustomMenuItem(
            crtIncMjAction
        );

        DelMjAction delMjAction = new DelMjAction();
        delMjAction.setView( view );
        jMenuDelMj =  new CustomMenuItem(
            delMjAction
        );

        ModMjAction modMjAction = new ModMjAction();
        modMjAction.setView( view );
        jMenuModMj =  new CustomMenuItem(
            modMjAction
        );

        CrtMjSchAction crtMjSchAction = new CrtMjSchAction();
        crtMjSchAction.setView( view );
        jMenuCrtMjSch = new CustomMenuItem(
            crtMjSchAction
        );

        ModMjSchAction modMjSchAction = new ModMjSchAction();
        modMjSchAction.setView( view );
        jMenuModMjSch = new CustomMenuItem(
            modMjSchAction
        );

        DelMjSchAction delMjSchAction = new DelMjSchAction();
        delMjSchAction.setView( view );
        jMenuDelMjSch = new CustomMenuItem(
            delMjSchAction
        );

        StartMjAction startMjAction = new StartMjAction( false );
        startMjAction.setView( view );
        jMenuStartMj =  new CustomMenuItem(
            startMjAction
        );

        QuickStartMjAction qStartMjAction = new QuickStartMjAction();
        qStartMjAction.setView( view );
        jMenuQStartMj = new CustomMenuItem(
            qStartMjAction
        );

        CreateMjInBatchAction batchedCrtMjAction = new CreateMjInBatchAction();
        batchedCrtMjAction.setView( view );
        this.jMenuBatchedCrtMj = new CustomMenuItem(
            batchedCrtMjAction
        );

        ModifyMjInBatchAction batchedModMjAction = new ModifyMjInBatchAction();
        batchedModMjAction.setView( view );
        this.jMenuBatchedModMj = new CustomMenuItem(
            batchedModMjAction
        );

        SetupMjSchAction setupMjSchAction = new SetupMjSchAction();
        setupMjSchAction.setView( view );
        this.jMenuSetupMjSch = new CustomMenuItem(
            setupMjSchAction
        );

        StartOrStopMjInBatchAction batchedStartStopMjAction = new StartOrStopMjInBatchAction();
        batchedStartStopMjAction.setView( view );
        this.jMenuBatchedStartStopMj = new CustomMenuItem(
            batchedStartStopMjAction
        );

        StopMjAction stopMjAction = new StopMjAction();
        stopMjAction.setView( view );
        jMenuStopMj =  new CustomMenuItem(
            stopMjAction
        );

        MonitorMjAction monMjAction = new MonitorMjAction();
        monMjAction.setView( view );
        jMenuMonMj = new CustomMenuItem(
            monMjAction
        );

        CrtCjAction crtCjAction = new CrtCjAction();
        crtCjAction.setView( view );
        jMenuCrtCj =  new CustomMenuItem(
            crtCjAction
        );

        jMenuDelCj =  new CustomMenuItem(
            delMjAction
        );

        jMenuModCj =  new CustomMenuItem(
            modMjAction
        );

        jMenuStartCj =  new CustomMenuItem(
            startMjAction
        );
        jMenuQStartCj = new CustomMenuItem(
            qStartMjAction
        );

        jMenuStopCj =  new CustomMenuItem(
            stopMjAction
        );
        jMenuMonCj = new CustomMenuItem(
            monMjAction
        );

        CrtPoolAction crtPoolAction = new CrtPoolAction();
        crtPoolAction.setView( view );
        jMenuCrtPool =  new CustomMenuItem(
            crtPoolAction
        );

        DelPoolAction delPoolAction = new DelPoolAction();
        delPoolAction.setView( view );
        jMenuDelPool =  new CustomMenuItem(
            delPoolAction
        );

        CrtHostAction crtHostAction = new CrtHostAction();
        crtHostAction.setView( view );
        jMenuCrtHost = new CustomMenuItem( crtHostAction );

        DelHostAction delHostAction = new DelHostAction();
        delHostAction.setView( view );
        jMenuDelHost = new CustomMenuItem( delHostAction );

        DelHostAction delCluster = new DelHostAction();
        delCluster.setView( view );
        this.jMenuDelCluster = new CustomMenuItem( delCluster );

        ModClusterAction modClusterAction = new ModClusterAction();
        modClusterAction.setView( view );
        jMenuModCluster = new CustomMenuItem( modClusterAction );

        ModHostAction modHostAction = new ModHostAction();
        modHostAction.setView( view );
        jMenuModHost = new CustomMenuItem( modHostAction );

        ModHostBootAction modHostBootAction = new ModHostBootAction();
        modHostBootAction.setView( view );
        jMenuModHostBoot = new CustomMenuItem(modHostBootAction);

	//add start odysys qiaojian 20091012
        SDHMInfoAction sdhmInfoAction= new SDHMInfoAction();
        sdhmInfoAction.setView( view );
        jMenuSDHMInfo = new CustomMenuItem( sdhmInfoAction );

        SDHMSetAction sdhmSetAction = new SDHMSetAction();
        sdhmSetAction.setView( view );
        jMenuSDHMSet = new CustomMenuItem( sdhmSetAction );

        SDHMReportAction sdhmReportAction = new SDHMReportAction();
        sdhmReportAction.setView( view );
        jMenuSDHMReport = new CustomMenuItem( sdhmReportAction );

        SDHMMirrorAction sdhmMirrorAction = new SDHMMirrorAction();
        sdhmMirrorAction.setView( view );
        jMenuSDHMMirror = new CustomMenuItem( sdhmMirrorAction );
        //add end odysys qiaojian 20091012

        UserMgrAction userMgrAction = new UserMgrAction();
        userMgrAction.setView( view );
        jMenuUserMgr = new BrowserToolMenuItem( userMgrAction );

        AuditAction auditAction = new AuditAction();
        auditAction.setView( view );
        jMenuAudit = new BrowserToolMenuItem( auditAction );

        MjobAction mjobAction = new MjobAction();
        mjobAction.setView( view );
        jMenuMjob = new BrowserToolMenuItem( mjobAction );

        ShutdownAction powerdown = new ShutdownAction();
        powerdown.setView( view );
        shutdown = new BrowserToolMenuItem( powerdown );

        CrtUWSAction crtUWSAction = new CrtUWSAction();
        crtUWSAction.setView( view );
        jMenuCrtUWS = new CustomMenuItem( crtUWSAction );

        DelUWSAction delUWSAction = new DelUWSAction();
        delUWSAction.setView( view );
        jMenuDelUWS = new CustomMenuItem( delUWSAction );

        ModUWSAction modUWSAction = new ModUWSAction();
        modUWSAction.setView( view );
        jMenuModUWS = new CustomMenuItem( modUWSAction );

        ModSrcUWSAction modSrcUWSAction = new ModSrcUWSAction();
        modSrcUWSAction.setView( view );
        jMenuModSrcUWSrv = new CustomMenuItem( modSrcUWSAction );

        MsgReportAction msgReportAction = new MsgReportAction();
        msgReportAction.setView( view );
        jMenuMsgReport = new BrowserToolMenuItem( msgReportAction );
        btnUWSRpt = new BrowserToolButton( msgReportAction );

        LoginPass loginPass = new LoginPass();
        loginPass.setView( view );
        jMenuModPass = new BrowserToolMenuItem( loginPass );

        LoginSetup loginSetup = new LoginSetup();
        loginSetup.setView( view );
        jMenuAdminOpt = new BrowserToolMenuItem( loginSetup );

        DelDstAgent delNBH = new DelDstAgent();
        delNBH.setView( view );
        jMenuDelDstAgnt = new CustomMenuItem( delNBH );

        DelSrcAgntAction delSrcAgnt = new DelSrcAgntAction();
        delSrcAgnt.setView( view );
        jMenuDelSrcAgnt = new CustomMenuItem( delSrcAgnt );

        DhcpSet dhcpAct = new DhcpSet( );
        dhcpAct.setView( view );
        jMenuDhcpSet = new BrowserToolMenuItem( dhcpAct );

        CrtUserAction crtUserAction = new CrtUserAction();
        crtUserAction.setView( view );
        jMenuCrtUser = new BrowserToolMenuItem( crtUserAction );

        DelUserAction delUserAction = new DelUserAction();
        delUserAction.setView( view );
        jMenuDelUser = new BrowserToolMenuItem( delUserAction );

        ModUserAction modUserAction = new ModUserAction();
        modUserAction.setView( view );
        jMenuModUser = new BrowserToolMenuItem( modUserAction );

        ModPasswdAction modPasswdAction = new ModPasswdAction();
        modPasswdAction.setView( view );
        jMenuModPasswd = new BrowserToolMenuItem( modPasswdAction );

        AddProfAction addProfAct = new AddProfAction();
        addProfAct.setView( view );
        addProfile = new CustomMenuItem( addProfAct );

        ModProfAction modProfAct = new ModProfAction();
        modProfAct.setView( view );
        modProfile = new CustomMenuItem( modProfAct );

        ModPhyProfAction modPhyProfAct = new ModPhyProfAction();
        modPhyProfAct.setView( view );
        modPhyProfile = new CustomMenuItem( modPhyProfAct );

        DelProfAction delProfAct = new DelProfAction();
        delProfAct.setView( view );
        delProfile = new CustomMenuItem( delProfAct );

        RunProfAction runProfAct = new RunProfAction();
        runProfAct.setView( view );
        runProfile = new CustomMenuItem( runProfAct );

        VerifyProfAction verifyProfAct = new VerifyProfAction();
        verifyProfAct.setView( view );
        verifyProfile = new CustomMenuItem( verifyProfAct );

        RenameProfAction renameProf = new RenameProfAction();
        renameProf.setView( view );
        renameProfile = new CustomMenuItem( renameProf );

        AddSchAction addSchAct = new AddSchAction();
        addSchAct.setView( view );
        addSch = new CustomMenuItem( addSchAct );

        AddADSchAction addADSchAct = new AddADSchAction();
        addADSchAct.setView(view);
        addADSch = new CustomMenuItem( addADSchAct );

        ModSchAction modSchAct = new ModSchAction();
        modSchAct.setView( view );
        modSch = new CustomMenuItem( modSchAct );

        DelSchAction delSchAct = new DelSchAction();
        delSchAct.setView( view );
        delSch = new CustomMenuItem( delSchAct );

        DupLogAction dupLogAct = new DupLogAction();
        dupLogAct.setView( view );
        dupLog = new BrowserToolMenuItem( dupLogAct );
        btnDupLog = new BrowserToolButton( dupLogAct );

        MonitorAction monitorAct = new MonitorAction();
        monitorAct.setView( view );
        monitorDup = new BrowserToolMenuItem( monitorAct );
        btnMon = new BrowserToolButton( monitorAct );

        SnapTreeAction snapTreeAct = new SnapTreeAction();
        snapTreeAct.setView( view );
        jMenuSnapTree = new CustomMenuItem( snapTreeAct );

        AboutAction aboutAction = new AboutAction();
        aboutAction.setView( view );
        jMenuAboutBackup = new BrowserToolMenuItem( aboutAction );

        CloneDiskAction cloneDiskAction = new CloneDiskAction();
        cloneDiskAction.setView( view );
        jMenuCloneDisk = new CustomMenuItem( cloneDiskAction );

        DeleteUiMirrorVolAction delUiMirVol = new DeleteUiMirrorVolAction();
        delUiMirVol.setView( view );
        jMenuDelUiMirrorVol = new CustomMenuItem( delUiMirVol );

        DeleteCloneDiskAction delCloneDisk = new DeleteCloneDiskAction();
        delCloneDisk.setView( view );
        jMenuDelCloneDisk = new CustomMenuItem( delCloneDisk );

        QueryUISnapAction queryUiSnap = new QueryUISnapAction();
        queryUiSnap.setView( view );
        jMenuQueryUISnap = new CustomMenuItem( queryUiSnap );

        RefreshAction refreshAct = new RefreshAction();
        refreshAct.setView(view);
        btnRefresh = new BrowserToolButton(refreshAct);
        
        //vmManager
        CreateVMHostAction crtVMHost = new CreateVMHostAction();
        crtVMHost.setView(view);
        addVMHost = new CustomMenuItem( crtVMHost );
        
        ModifyVMHostAction modiVMHost = new ModifyVMHostAction();
        modiVMHost.setView(view);
        modVMHost = new CustomMenuItem( modiVMHost );
        
        DeleteVMHostAction deleteVMHost = new DeleteVMHostAction();
        deleteVMHost.setView(view);
        delVMHost = new CustomMenuItem( deleteVMHost );
        
        PowerOnVMHostAction poweronVMHost = new PowerOnVMHostAction();
        poweronVMHost.setView(view);
        powerOnVMHost = new CustomMenuItem( poweronVMHost );
        
        PowerOffVMHostAction poweroffVMHost = new PowerOffVMHostAction();
        poweroffVMHost.setView(view);
        powerOffVMHost = new CustomMenuItem( poweroffVMHost );
        
        SwitchDiskForVMHostAction sdForVMHostAction = new SwitchDiskForVMHostAction( true );
        sdForVMHostAction.setView(view);
        switchNetDisk = new CustomMenuItem( sdForVMHostAction );
        
        EnableIbootVMhostDiskAction enableIbootAction = new EnableIbootVMhostDiskAction();
        enableIbootAction.setView(view);
        enableIbootView = new CustomMenuItem( enableIbootAction );
        
        DeleteVMHostAllIPAction delIP = new DeleteVMHostAllIPAction();
        delIP.setView(view);
        delVMHostIP = new CustomMenuItem( delIP );
        
        RecoverVMHostAllIPAction recoverIP = new RecoverVMHostAllIPAction();
        recoverIP.setView(view);
        recoverVMHostIP = new CustomMenuItem( recoverIP );
        
        SeeVMHostIPConfigAction seeIP = new SeeVMHostIPConfigAction();
        seeIP.setView(view);
        showVMHostIPInfo = new CustomMenuItem( seeIP );
        
        CreateVMMachineAction crtVMMachineAct = new CreateVMMachineAction();
        crtVMMachineAct.setView(view);
        crtVMMachine = new CustomMenuItem( crtVMMachineAct );
        
        QuickLogonDataDiskAction quickLDDAct = new QuickLogonDataDiskAction();
        quickLDDAct.setView(view);
        quickLogonDataDisk = new CustomMenuItem( quickLDDAct );

        SetVMServiceInfoAction setVMService = new SetVMServiceInfoAction();
        setVMService.setView(view);
        jMenuSetVMServiceInfo = new BrowserToolMenuItem( setVMService );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ), new Object()
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_PROPERTY ),this.jMenuProperty
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT ),this.jMenuInit
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_INIT ),this.jMenuWinPhyInit
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ),this.jMenuFailover
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER ),this.jMenuWinPhyFailover
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_NETDISK ),this.jMenuWinPhySwitchToNetDisk
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_NETDISK ),this.jMenuSwitchToNetDisk
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILBACK ),this.jMenuFailback
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILBACK ),this.jMenuWinPhyFailback
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_LOCALDISK ),this.jMenuWinPhySwitchToLocalDisk
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_LOCALDISK ),this.jMenuSwitchToLocalDisk
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_RESTORE_DISK ),this.jMenuRestoreData
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_RESTORE_DISK ),this.jMenuWinPhyRestoreData
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_DUP_NETDISK ),this.jMenuWinPhyDuplicateDataFromIscsiToLocaldisk
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_DUP_NETDISK ),this.jMenuDuplicateDataFromIscsiToLocaldisk
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_SYNC_STATE ),this.jMenuWinPhyQuerySyncState
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_START_AUTO_CRT_SNAP ),this.jMenuWinPhyStartAutoCrtSnap
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_AMS_REBUILD_MIRROR ), this.jMenuAmsRebuildMirror );
        
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_INIT_PROGRESS ),this.jMenuWinPhyQueryInitState
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_AMS_INIT_PROGRESS ),this.jMenuNotWinPhyQueryInitState
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CLUSTER_PHY_WIN_INIT ),this.jMenuClusterWinPhyInit
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CLUSTER_PHY_WIN_FAILOVER),this.jMenuClusterWinPhyFailover
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK ),this.jMenuRollback
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_VOL ),this.jMenuCrtVol
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_VOL ),this.jMenuDelVol
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_ONLINE ),this.jMenuOnlineVol
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_OFFLINE ),this.jMenuOfflineVol
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK_VOL ),this.jMenuRollbackVol
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_SNAP ),this.jMenuCrtSnap
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_UCS_SNAP ), this.jMenuCrtUcsSnap
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_SYNC_SNAP ),this.jMenuCrtSyncSnap
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_ASYNC_SNAP ),this.jMenuCrtAsyncSnap
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SNAP ),this.jMenuDelSnap
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MNT_SNAP ),this.jMenuMntSnap
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_SNAP_EVER ), this.jMenuEvrSnap
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_LUNMAP ),this.jMenuCrtLm
        );

        funcMap.put(
            new Integer(MenuAndBtnCenterForMainUi.FUNC_EXPORTLM) ,this.jMenuExpView
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CANCEL_LM ),this.jMenuDelLm
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_HOST ),this.jMenuCrtHost
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_HOST ),this.jMenuDelHost
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_HOST ),this.jMenuModHost
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_HOSTBOOT ),this.jMenuModHostBoot
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_CLUSTER ),this.jMenuDelCluster
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_CLUSTER ),this.jMenuModCluster
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_DSTAGNT ),this.jMenuDelDstAgnt
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SRCAGNT ),this.jMenuDelSrcAgnt
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_UWS_SRV ),this.jMenuCrtUWS
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_UWS_SRV ),this.jMenuDelUWS
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_UWS_SRV ),this.jMenuModUWS
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN ),this.jMenuInitNWinHost
        );
        funcMap.put(
        	new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN_AMS ), this.jMenuInitNWinAmsHost
        );

        funcMap.put(
        	new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN_LVM ), this.jMenuInitNWinLvmHost
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN ),this.jMenuRestDataForNWinHost
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_SELECT_BOOT_VER ),this.jMenuSelBootVer
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ),this.jMenuIBootForLinux
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK_NWIN ),this.jMenuRollbackForNWin
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_VIEW ),this.jMenuCrtView
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_VIEW ),this.jMenuDelView
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MNT_VIEW ),this.jMenuMntView
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_UMNT_VIEW ),this.jMenuUMntView
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_POOL ),this.jMenuCrtPool
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_POOL ),this.jMenuDelPool
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_MJ ),this.jMenuCrtMj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_LMJ ),this.jMenuCrtLMj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_MJ1 ),this.jMenuCrtMj1
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_MJ ),this.jMenuDelMj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_MJ ),this.jMenuModMj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_MJ_SCH ), this.jMenuCrtMjSch
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_MJ_SCH ), this.jMenuModMjSch
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_MJ_SCH ), this.jMenuDelMjSch
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_START_MJ ),this.jMenuStartMj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_QUICK_START_MJ ),this.jMenuQStartMj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_BATCH_START_STOP_MJ ),this.jMenuBatchedStartStopMj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_BATCH_CREATE_MJ ),this.jMenuBatchedCrtMj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_BATCH_MOD_MJ ),this.jMenuBatchedModMj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_SETUP_MJ_SCH ),this.jMenuSetupMjSch
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_STOP_MJ ),this.jMenuStopMj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MONITOR_MJ ),this.jMenuMonMj
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_CJ ),this.jMenuCrtCj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_CJ ),this.jMenuDelCj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_CJ ),this.jMenuModCj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_START_CJ ),this.jMenuStartCj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_STOP_CJ ),this.jMenuStopCj
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MONITOR_CJ ),this.jMenuMonCj
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CLONE_DISK ),this.jMenuCloneDisk
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_UI_MIRROR_VOL ),this.jMenuDelUiMirrorVol
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_CLONE_DISK ),this.jMenuDelCloneDisk
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_QUEYR_UISNAP ),this.jMenuQueryUISnap
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_SNAP_TREE ),this.jMenuSnapTree
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_SRC_UWS_SRV ),this.jMenuModSrcUWSrv
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_ADD_PROF ),this.addProfile
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_PROF ),this.modProfile
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_MOD_PROFILE ),this.modPhyProfile
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_PROF ),this.delProfile
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_RENAME_PROF ),this.renameProfile
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_RUN_PROF ),this.runProfile
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_VERIFY_PROF ),this.verifyProfile
        );

        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_ADD_SCH ),this.addSch
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_ADD_ADSCH ),this.addADSch
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_SCH ),this.modSch
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SCH ),this.delSch
        );
//        funcMap.put(
//            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_VMHOST ), this.addVMHost
//        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_VMHOST ), this.modVMHost
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_VMHOST ), this.delVMHost
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_POWERON_VMHOST ), this.powerOnVMHost
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_POWEROFF_VMHOST ), this.powerOffVMHost
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_VMHOST_SWITCHNETDISK ), this.switchNetDisk
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_ENABLE_IBOOT_VMHOST_DISK ), this.enableIbootView
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_VMHOST_IP ), this.delVMHostIP
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_RECOVER_VMHOST_IP ), this.recoverVMHostIP
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_SEE_VMHOST_IPCONFIG ), this.showVMHostIPInfo
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_VM_MACHINE ), this.crtVMMachine
        );
        funcMap.put(
            new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_VMHOST_QUICK_LOGON_DATA_DISK ), this.quickLogonDataDisk
        );
    }

    public void setEnabledOnMainMenu( int index,boolean val ){
        mainMenu[index].setEnabled( val );
    }

    private void addMenuToView(){
        mainMenu[INDEX_FILE].setMnemonic('F');
        mainMenu[INDEX_LOGICAL_PROTECT].setMnemonic('L');
        mainMenu[INDEX_PHYSICAL_PROTECT].setMnemonic('P');
        mainMenu[INDEX_DEV].setMnemonic('D');
        mainMenu[INDEX_REMOTE_DR].setMnemonic('E');
        mainMenu[INDEX_SETUP].setMnemonic('S');
        mainMenu[INDEX_HELP].setMnemonic('H');

        view.addMenu( mainMenu[INDEX_FILE] );
        view.addMenu( mainMenu[INDEX_LOGICAL_PROTECT] );
        view.addMenu( mainMenu[INDEX_PHYSICAL_PROTECT] );
        mainMenu[INDEX_LOGICAL_PROTECT].add( this.jMenuWinPro );
        if( ResourceCenter.isRelease( view.mode ) ){
            mainMenu[INDEX_LOGICAL_PROTECT].add( this.jMenuNonWinPro );
        }
        mainMenu[INDEX_LOGICAL_PROTECT].addSeparator();

        if( view.hasDP ){
            mainMenu[INDEX_LOGICAL_PROTECT].add( this.jMenuProf );
            jMenuProf.add( runProfile );
            jMenuProf.add( verifyProfile );
            jMenuProf.addSeparator();
            jMenuProf.add( addProfile );
            jMenuProf.add( modProfile );
            jMenuProf.add( delProfile );
            jMenuProf.add( renameProfile );
            mainMenu[INDEX_LOGICAL_PROTECT].addSeparator();
            mainMenu[INDEX_LOGICAL_PROTECT].add( this.jMenuSch );
            jMenuSch.add( addSch );
            jMenuSch.add( addADSch);
            jMenuSch.add( modSch );
            jMenuSch.add( delSch );
            mainMenu[INDEX_LOGICAL_PROTECT].addSeparator();
            mainMenu[INDEX_LOGICAL_PROTECT].add( this.dupLog );
            mainMenu[INDEX_LOGICAL_PROTECT].addSeparator();
            mainMenu[INDEX_LOGICAL_PROTECT].add( this.monitorDup );
        }
        view.addMenu( mainMenu[INDEX_DEV] );
        view.addMenu( mainMenu[INDEX_REMOTE_DR] );
        view.addMenu( mainMenu[INDEX_SETUP] );
        view.addMenu( mainMenu[INDEX_HELP] );

        mainMenu[INDEX_FILE].add(jMenuLogin);
        mainMenu[INDEX_FILE].add(jMenuLogout);
        mainMenu[INDEX_FILE].add(jMenuProperty );
        mainMenu[INDEX_FILE].addSeparator();
        mainMenu[INDEX_FILE].add(jMenuExit);

        jMenuWinPro.add( jMenuInit );
        jMenuWinPro.add( jMenuDR );
        jMenuDR.setIcon( ResourceCenter.MENU_ICON_BLANK );
        jMenuDR.add( jMenuFailover );
        jMenuDR.add( jMenuFailback );
        jMenuWinPro.add( jMenuRestoreData );

        this.jMenuNonWinPro.add(jMenuInitNWinHost );
        this.jMenuNonWinPro.add(jMenuSelBootVer);
        this.jMenuNonWinPro.add(jMenuIBootForLinux);
        this.jMenuNonWinPro.add(jMenuRestDataForNWinHost );
        
        mainMenu[INDEX_PHYSICAL_PROTECT].add(this.jMenuWinPhyPro);
        mainMenu[INDEX_PHYSICAL_PROTECT].add(this.jMenuNonWinPhyPro);
        this.jMenuWinPhyPro.add( this.jMenuWinPhyInit );
        this.jMenuWinPhyPro.add( this.jMenuWinPhyDR );
        this.jMenuWinPhyDR.add( this.jMenuWinPhyFailover );
        this.jMenuWinPhyDR.add(this.jMenuWinPhyFailback );
        this.jMenuWinPhyPro.add( this.jMenuWinPhyRestoreData );
        
        this.jMenuNonWinPhyPro.add(jMenuInitNWinAmsHost );
        this.jMenuNonWinPhyPro.add(jMenuSelBootVerams);
        this.jMenuNonWinPhyPro.add(jMenuIBootForLinuxams);
        this.jMenuNonWinPhyPro.add(jMenuRestDataForNWinHostams );

        mainMenu[INDEX_PHYSICAL_PROTECT].addSeparator();
        mainMenu[INDEX_PHYSICAL_PROTECT].add( modPhyProfile );

        mainMenu[INDEX_DEV].add( jMenuVol );
        jMenuVol.add( jMenuCrtVol );
        jMenuVol.add( jMenuDelVol );
        mainMenu[INDEX_DEV].add( jMenuSnap );
        jMenuSnap.add( jMenuCrtSnap );
        jMenuSnap.add(jMenuDelSnap );
        jMenuSnap.add(jMenuEvrSnap );
        mainMenu[INDEX_DEV].add( jMenuView );
        jMenuView.add( jMenuCrtView );
        jMenuView.add( jMenuDelView );
        jMenuView.add( jMenuCrtUcsSnap );
        mainMenu[INDEX_DEV].addSeparator();
        mainMenu[INDEX_DEV].add(jMenuLunMap);
        jMenuLunMap.add(jMenuCrtLm);
        jMenuLunMap.add(jMenuDelLm );
        mainMenu[INDEX_DEV].addSeparator();
        mainMenu[INDEX_DEV].add( jMenuOnlineVol );
//        mainMenu[INDEX_DEV].add( jMenuOfflineVol );
        mainMenu[INDEX_DEV].addSeparator();
        mainMenu[INDEX_DEV].add(jMenuPool);
        jMenuPool.add( jMenuCrtPool );
        jMenuPool.add( jMenuDelPool );

        mainMenu[INDEX_REMOTE_DR].add( this.jMenuCrtUWS );
        mainMenu[INDEX_REMOTE_DR].add( this.jMenuModUWS );
        mainMenu[INDEX_REMOTE_DR].add( this.jMenuDelUWS );
        mainMenu[INDEX_REMOTE_DR].addSeparator();
        mainMenu[INDEX_REMOTE_DR].add( this.jMenuCrtMj );
        mainMenu[INDEX_REMOTE_DR].add( this.jMenuCrtMj1 );
        mainMenu[INDEX_REMOTE_DR].add( jMenuDelMj );
        mainMenu[INDEX_REMOTE_DR].add( jMenuModMj );
        mainMenu[INDEX_REMOTE_DR].add( this.jMenuStartMj );
        mainMenu[INDEX_REMOTE_DR].add( jMenuStopMj );
        mainMenu[INDEX_REMOTE_DR].add( jMenuMonMj );
        mainMenu[INDEX_REMOTE_DR].addSeparator();
        mainMenu[INDEX_REMOTE_DR].add( jMenuRollback );
        mainMenu[INDEX_REMOTE_DR].add( jMenuRollbackForNWin );
        mainMenu[INDEX_REMOTE_DR].add( jMenuRollbackVol );
        mainMenu[INDEX_REMOTE_DR].addSeparator();
        mainMenu[INDEX_REMOTE_DR].add( this.jMenuDelSrcAgnt );
        mainMenu[INDEX_REMOTE_DR].addSeparator();
        mainMenu[INDEX_REMOTE_DR].add( jMenuQueryUISnap );
        mainMenu[INDEX_REMOTE_DR].add( jMenuCloneDisk );
        mainMenu[INDEX_REMOTE_DR].add( jMenuDelUiMirrorVol );
        mainMenu[INDEX_REMOTE_DR].add( jMenuDelCloneDisk );

        //mainMenu[INDEX_SETUP].add( jMenuHost );
        jMenuHost.setIcon( ResourceCenter.MENU_ICON_BLANK );
        jMenuHost.add( jMenuCrtHost );
        jMenuHost.add( jMenuModHost );
        jMenuHost.add( jMenuDelHost );
        mainMenu[INDEX_SETUP].add( jMenuDhcpSet );
        mainMenu[INDEX_SETUP].add( jMenuModPass );
        mainMenu[INDEX_SETUP].add( jMenuAdminOpt );
        //mainMenu[INDEX_SETUP].add( jMenuUserMgr );
        mainMenu[INDEX_SETUP].add( jMenuAudit );
        mainMenu[INDEX_SETUP].add( jMenuMjob );
        mainMenu[INDEX_SETUP].add( jMenuSetVMServiceInfo );

        // 暂时没有用户管理
        /*
        mainMenu[INDEX_SETUP].add( jMenuUser  );
        jMenuUser.add( jMenuCrtUser );
        jMenuUser.add( jMenuDelUser );
        jMenuUser.add( jMenuModUser );
        jMenuUser.add( jMenuModPasswd );
         */

        //add start odysys qiaojian 20091012
//        mainMenu[INDEX_SETUP].add( jMenuSDHM );
//        jMenuSDHM.setIcon( ResourceCenter.MENU_ICON_BLANK );
//        jMenuSDHM.add( jMenuSDHMInfo );
//        jMenuSDHM.add( jMenuSDHMSet );
//        jMenuSDHM.add( jMenuSDHMReport );
//        jMenuSDHM.add( jMenuSDHMMirror );
        //add end odysys qiaojian 20091012

        mainMenu[INDEX_HELP].add(jMenuAboutBackup);
    }

    private void addButtonToView(){
        CustomSeparator jSeparator0 = new CustomSeparator( JSeparator.VERTICAL );
        view.addSeparator( jSeparator0 );

        view.addToolButton( btnLogin );
        view.addToolButton( btnLogout );
        CustomSeparator jSeparator1 = new CustomSeparator( JSeparator.VERTICAL );
        view.addSeparator( jSeparator1 );

        view.addToolButton( btnInit );
        view.addToolButton( btnFailover );
        view.addToolButton( btnRstOriData );

        // 废掉这个功能（2010.3.18）
        //view.addToolButton( btnUWSRpt );
        CustomSeparator jSeparator2 = new CustomSeparator( JSeparator.VERTICAL );
        view.addSeparator( jSeparator2 );

        if( view.hasDP ){
            view.addToolButton( btnDupLog );
            view.addToolButton( btnMon );
            CustomSeparator jSeparator3 = new CustomSeparator( JSeparator.VERTICAL );
            view.addSeparator( jSeparator3 );
        }

        view.addToolButton(btnRefresh);
        CustomSeparator jSeparator4 = new CustomSeparator( JSeparator.VERTICAL );
        view.addSeparator( jSeparator4 );

        view.addToolButton(btnExit);
    }

    public void setupConnectButtonStatus(boolean isConnected){
        if( isConnected ){
            jMenuLogin.setEnabled(false);
            btnLogin.setEnabled(false);
            jMenuLogout.setEnabled(true);
            btnLogout.setEnabled(true);
            jMenuProperty.setEnabled( false );
            btnRefresh.setEnabled(true);

            jMenuInit.setEnabled( true );
            btnInit.setEnabled( true );
            jMenuInitNWinHost.setEnabled( true );
            jMenuSelBootVer.setEnabled( false );
            jMenuSelBootVerams.setEnabled( false );
            jMenuIBootForLinux.setEnabled( false );
            jMenuIBootForLinuxams.setEnabled( false );
            jMenuFailover.setEnabled( false );
            jMenuFailback.setEnabled( false );
            btnFailover.setEnabled( false );
            jMenuRestoreData.setEnabled( false );
            jMenuRollback.setEnabled( false );
            btnRstOriData.setEnabled( false );
            jMenuRestDataForNWinHost.setEnabled( false );
            jMenuRestDataForNWinHostams.setEnabled( false );
            jMenuRollbackForNWin.setEnabled( false );
            btnUWSRpt.setEnabled( true );

            jMenuCrtVol.setEnabled( false );
            jMenuDelVol.setEnabled( false );

            jMenuOnlineVol.setEnabled( false );

            jMenuCrtView.setEnabled( false );
            jMenuDelView.setEnabled( false );
            jMenuCrtUcsSnap.setEnabled( false );

            jMenuPool.setEnabled( true );
            jMenuCrtPool.setEnabled( true );
            jMenuDelPool.setEnabled( false );

            jMenuCrtSnap.setEnabled( false );
            jMenuDelSnap.setEnabled( false );
            jMenuEvrSnap.setEnabled( false );

            jMenuCrtLm.setEnabled( false );
            jMenuDelLm.setEnabled( false );

            jMenuCrtHost.setEnabled( true );
            jMenuDelHost.setEnabled( false );
            jMenuModHost.setEnabled( false );
            jMenuDhcpSet.setEnabled( true );
            jMenuMsgReport.setEnabled( true );
            jMenuModPass.setEnabled( true );
            jMenuSDHM.setEnabled(true);
            jMenuSetVMServiceInfo.setEnabled( true );

        }else{
            setDisableForMenuAndBtn();

            jMenuLogin.setEnabled( true );
            btnLogin.setEnabled( true );
            jMenuLogout.setEnabled(false);
            btnLogout.setEnabled(false);
        }
    }

    public static String getAuditOPType( int type ){
        String str;

        switch( type ){
            case FUNC_INIT:
                str = SanBootView.res.getString("View.MenuItem.init");
                break;
            case FUNC_FAILOVER:
                str = SanBootView.res.getString("View.MenuItem.failover");
                break;
            case FUNC_RESTORE_DISK:
                str = SanBootView.res.getString("View.MenuItem.rstOriData");
                break;
            case FUNC_FAILBACK:
                str = SanBootView.res.getString("View.MenuItem.failback");
                break;
            case FUNC_MOD_HOST:
                str = ResourceCenter.getCmdString(  ResourceCenter.CMD_MOD_CLIENT );
                break;
            case FUNC_DEL_CLUSTER:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_CLUSTER );
                break;
            case FUNC_DEL_HOST:
                str = ResourceCenter.getCmdString(  ResourceCenter.CMD_DEL_CLIENT );
                break;
            case FUNC_CRT_VOL:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_VOL );
                break;
            case FUNC_DEL_VOL:
                str = ResourceCenter.getCmdString(  ResourceCenter.CMD_DEL_VOL );
                break;
            case FUNC_LUNMAP:
                str = ResourceCenter.getCmdString(  ResourceCenter.CMD_ADD_LUNMAP);
                break;
            case FUNC_CANCEL_LM:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_LUNMAP );
                break;
            case FUNC_CRT_SNAP:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_SNAP );
                break;
            case FUNC_CRT_UCS_SNAP:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_UCS_SNAP );
            case FUNC_DEL_SNAP:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_SNAP );
                break;
            case FUNC_CRT_VIEW:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_VIEW );
                break;
            case  FUNC_DEL_VIEW:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_VIEW );
                break;
            case FUNC_ADD_PROF:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_PROFILE );
                break;
            case FUNC_DEL_PROF:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_PROFILE );
                break;
            case FUNC_MOD_PROF:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_PROFILE );
                break;
            case FUNC_RUN_PROF:
                str = SanBootView.res.getString("View.MenuItem.runProf");
                break;
            case FUNC_VERIFY_PROF:
                str = SanBootView.res.getString("View.MenuItem.verifyProf");;
                break;
            case FUNC_RENAME_PROF:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_RENAME_PROFILE );
                break;
            case FUNC_ADD_SCH:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_DB_SCHEDULER );
                break;
            case FUNC_ADD_ADSCH:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_DB_ADSCHEDULER );
                break;
            case FUNC_MOD_SCH:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_DB_SCHEDULER );
                break;
            case FUNC_DEL_SCH:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_DB_SCHEDULER );
                break;
            case FUNC_DHCP:
                str = SanBootView.res.getString("View.MenuItem.dhcp");
                break;
            case FUNC_ADMINOPT:
                str = SanBootView.res.getString("View.MenuItem.adminOpt");
                break;
            case FUNC_USER_AUDIT:
                str = SanBootView.res.getString("View.MenuItem.audit");
                break;
            case FUNC_USER_MJOB:
                str = SanBootView.res.getString("View.MenuItem.mjob");
                break;
            case FUNC_USER_MGR:
                str = SanBootView.res.getString("View.MenuItem.userMgr");
                break;
            case FUNC_CRT_POOL:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_DHCP_CHG_OPT );
                break;
            case FUNC_DEL_POOL:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_DHCP_CHG_OPT );
                break;
            case FUNC_CANCEL_JOB:
                str = ResourceCenter.getCmdString( ResourceCenter.CMD_KILL_TASK );
                break;
            case FUNC_MOD_PASS:
                str = SanBootView.res.getString("View.MenuItem.modPasswd");
                break;
            case FUNC_DUP_LOG:
                str = SanBootView.res.getString("common.cmd.delTskLog");
                break;
            case FUNC_LOGIN:
                str = SanBootView.res.getString("common.cmd.login");
                break;
            case FUNC_LOGOUT:
                str = SanBootView.res.getString("common.cmd.logout");
                break;
            case FUNC_SHUTDOWN:
                str = SanBootView.res.getString("RightCustomDialog.checkbox.shutdown");
                break;
            case FUNC_WIN_PHY_INIT:
                str = SanBootView.res.getString("RightCustomDialog.checkbox.phy-init");
                break;
            case FUNC_WIN_PHY_FAILOVER:
                str = SanBootView.res.getString("RightCustomDialog.checkbox.phy-netboot");
                break;
            case FUNC_WIN_PHY_FAILBACK:
                str = SanBootView.res.getString("RightCustomDialog.checkbox.phy-localdiskboot");
                break;
            case FUNC_WIN_PHY_RESTORE_DISK:
                str = SanBootView.res.getString("RightCustomDialog.checkbox.phy-rstOriDisk");
                break;
            default:
                str = SanBootView.res.getString( "common.cmd.unknown" );
                break;
        }

        return str;
    }

    public static boolean isRelatedToUser( int eid ){
        switch( eid ){
            // 如下操作与client有关系
            case FUNC_INIT:
            case FUNC_FAILOVER:
            case FUNC_MOD_HOST:
            case FUNC_DEL_HOST:
            case FUNC_ADD_PROF:
            case FUNC_DEL_PROF:
            case FUNC_MOD_PROF:
            case FUNC_RUN_PROF:
            case FUNC_VERIFY_PROF:
            case FUNC_RENAME_PROF:
            case FUNC_ADD_SCH:
            case FUNC_ADD_ADSCH:
            case FUNC_MOD_SCH:
            case FUNC_DEL_SCH:
            case FUNC_CANCEL_JOB:

            case FUNC_WIN_PHY_INIT:
            case FUNC_WIN_PHY_FAILOVER:
                return true;
            // 如下操作与client没有关系
            case FUNC_CRT_VOL:
            case FUNC_DEL_VOL:
            case FUNC_LUNMAP:
            case FUNC_CANCEL_LM:
            case FUNC_CRT_SNAP:
            case FUNC_DEL_SNAP:
            case FUNC_CRT_VIEW:
            case FUNC_DEL_VIEW:
            case FUNC_DHCP:
            case FUNC_ADMINOPT:
            case FUNC_USER_AUDIT:
            case FUNC_USER_MJOB:
            case FUNC_USER_MGR:
            case FUNC_CRT_POOL:
            case FUNC_DEL_POOL:
            case FUNC_MOD_PASS:
            case FUNC_DUP_LOG:
            case FUNC_LOGIN:
            case FUNC_LOGOUT:
            case FUNC_RESTORE_DISK:
            case FUNC_FAILBACK:
            case FUNC_SHUTDOWN:

            case FUNC_WIN_PHY_FAILBACK:
            case FUNC_WIN_PHY_RESTORE_DISK:
            case FUNC_SET_VMSERVICE_INFO:
                return false;
            default:
                return false;
        }
    }

    public int getLevelFromType( int type ){
        int level;
SanBootView.log.debug( getClass().getName(), "type ======> : " + type  );

        switch( type ){
            case ResourceCenter.TYPE_ROOT_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_ROOT;
                break;
            case ResourceCenter.TYPE_CHIEF_HOST_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_HOSTLIST;
                break;
            case ResourceCenter.TYPE_HOST_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_BOOT_HOST;
                break;
            case ResourceCenter.TYPE_CHIEF_DEST_UWS:
                level = MenuAndBtnCenterForMainUi.LEVEL_UWSLIST;
                break;
            case ResourceCenter.TYPE_DEST_UWS:
                level = MenuAndBtnCenterForMainUi.LEVEL_DEST_UWS_SRV; // 目的端UWS server
                break;
            case ResourceCenter.TYPE_SRC_UWSSRV:
                level = MenuAndBtnCenterForMainUi.LEVEL_SRC_UWS_SRV;  // 源端UWS server
                break;
            case ResourceCenter.TYPE_VOLUME_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_VOL;
                break;
            case ResourceCenter.TYPE_LV_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_LV;
                break;
            case ResourceCenter.TYPE_ACTLINK_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_ACTLINK;
                break;
            case ResourceCenter.TYPE_CHIEF_LUNMAP_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_LMLIST;
                break;
            case ResourceCenter.TYPE_LUNMAP_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_LM;
                break;
            case ResourceCenter.TYPE_CHIEF_SNAP_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_SNAPLIST;
                break;
            case ResourceCenter.TYPE_SNAP_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_SNAP;
                break;
            case ResourceCenter.TYPE_MIRROR_SNAP:
                level = MenuAndBtnCenterForMainUi.LEVEL_MIRROR_SNAP;
                break;
            case ResourceCenter.TYPE_MIRROR_SNAP_VIEW:
                level = MenuAndBtnCenterForMainUi.LEVEL_MIRROR_SNAP_VIEW;
                break;
            case ResourceCenter.TYPE_VIEW_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_VIEW;
                break;
            case ResourceCenter.TYPE_CHIEF_ORPHAN_VOL:
                level = MenuAndBtnCenterForMainUi.LEVEL_ORPH_VOLLIST;
                break;
            case ResourceCenter.TYPE_ORPHAN_VOL:
                level = MenuAndBtnCenterForMainUi.LEVEL_ORPH_VOL;
                break;
            case ResourceCenter.TYPE_CHIEF_USER_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_USER_LIST;
                break;
            case ResourceCenter.TYPE_USER_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_USER;
                break;
            case ResourceCenter.TYPE_CHIEF_POOL:
                level = MenuAndBtnCenterForMainUi.LEVEL_POOLLIST;
                break;
            case ResourceCenter.TYPE_POOL:
                level = MenuAndBtnCenterForMainUi.LEVEL_POOL;
                break;
            case ResourceCenter.TYPE_CHIEF_MIRROR_JOB:
                level = MenuAndBtnCenterForMainUi.LEVEL_MJLIST;
                break;
            case ResourceCenter.TYPE_MIRROR_JOB:
                level = MenuAndBtnCenterForMainUi.LEVEL_MJ;
                break;
            case ResourceCenter.TYPE_REMOTE_HOST:
                level = MenuAndBtnCenterForMainUi.LEVEL_SRC_AGENT;
                break;
            case ResourceCenter.TYPE_DEL_SNAP_INDEX:
                level = MenuAndBtnCenterForMainUi.LEVEL_DEL_SNAP;
                break;
            case ResourceCenter.TYPE_DEST_AGENT:
                level = MenuAndBtnCenterForMainUi.LEVEL_NETBOOTED_HOST;
                break;
            case ResourceCenter.TYPE_CHIEF_PROF:
            case ResourceCenter.TYPE_CHIEF_PPPROF:
                level = MenuAndBtnCenterForMainUi.LEVEL_PROFLIST;
                break;
            case ResourceCenter.TYPE_PROF:
                level = MenuAndBtnCenterForMainUi.LEVEL_PROF;
                break;
            case ResourceCenter.TYPE_PPPROF:
                level = MenuAndBtnCenterForMainUi.LEVEL_PPPROF;
                break;
            case ResourceCenter.TYPE_CHIEF_SCH:
                level = MenuAndBtnCenterForMainUi.LEVEL_SCHLIST;
                break;
            case ResourceCenter.TYPE_SCH:
                level = MenuAndBtnCenterForMainUi.LEVEL_SCH;
                break;
            case ResourceCenter.TYPE_CHIEF_ADSCH:
                level = MenuAndBtnCenterForMainUi.LEVEL_ADSCHLIST;
                break;
            case ResourceCenter.TYPE_CLONE_DISK:
                level = MenuAndBtnCenterForMainUi.LEVEL_CLONE_DISK;
                break;
            case ResourceCenter.TYPE_MIRROR_VOL:
                level = MenuAndBtnCenterForMainUi.LEVEL_MIRROR_VOL;
                break;
            case ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP:
                level = MenuAndBtnCenterForMainUi.LEVEL_UIM_SNAP;
                break;
            case ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_VOL:
                level = MenuAndBtnCenterForMainUi.LEVEL_UIM_VOL;
                break;
            case ResourceCenter.TYPE_CHIEF_UIM_SNAP:
                level = MenuAndBtnCenterForMainUi.LEVEL_UIM_SNAP_LIST;
                break;
            default:
                level = MenuAndBtnCenterForMainUi.LEVEL_UNKNOWN;
                break;
        }
SanBootView.log.debug( getClass().getName(), "level ======> : " + level  );
        return level;
    }

    // 根据树上点中节点的种类,来设置不同 button 的状态
    public void setupSelectionButtonStatus(int level){
        BackupUser user = null;
SanBootView.log.debug( getClass().getName(), "select level: "+level );

        // 先进行全局性的控制
        setDisableForMenuAndBtn();
        setEnabledForGolbalMenuAndBtn();

        // 再进行局部上的控制
        switch( level ){
            case MenuAndBtnCenterForMainUi.LEVEL_HOSTLIST:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!= null ){
                    if( user.isAdminRight() ){
                        jMenuHost.setEnabled( true );
                        jMenuCrtHost.setEnabled( true );
                        jMenuDelHost.setEnabled( false );
                        jMenuModHost.setEnabled( false );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_BOOT_HOST:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        jMenuProperty.setEnabled( true );
                        jMenuHost.setEnabled( true );
                        jMenuCrtHost.setEnabled( false );
                        jMenuDelHost.setEnabled( true );
                        jMenuModHost.setEnabled( true );

                        this.jMenuWinPro.setEnabled(true);
                        this.jMenuNonWinPro.setEnabled(true);
                        this.jMenuWinPhyPro.setEnabled( true );
                        this.jMenuNonWinPhyPro.setEnabled( true );
                        jMenuDR.setEnabled( true );
                        jMenuSelBootVer.setEnabled( true );
                        jMenuSelBootVerams.setEnabled( true );
                        jMenuIBootForLinux.setEnabled( true );
                        jMenuIBootForLinuxams.setEnabled( true );
                        jMenuFailover.setEnabled( true );
                        btnFailover.setEnabled( true );
                        jMenuFailback.setEnabled( true );
                        jMenuRestoreData.setEnabled( true );
                        btnRstOriData.setEnabled( true );
                        jMenuRestDataForNWinHost.setEnabled( true );
                        jMenuRestDataForNWinHostams.setEnabled( true );

                        this.jMenuWinPhyInit.setEnabled( true );
                        this.jMenuWinPhyDR.setEnabled( true );
                        this.jMenuWinPhyFailover.setEnabled( true );
                        this.jMenuWinPhyFailback.setEnabled( true );
                        this.jMenuWinPhyRestoreData.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_UWSLIST:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!= null ){
                    if( user.isAdminRight() ){
                        jMenuCrtUWS.setEnabled( true );
                        jMenuDelUWS.setEnabled( false );
                        jMenuModUWS.setEnabled( false );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_DEST_UWS_SRV:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        jMenuCrtUWS.setEnabled( false );
                        jMenuDelUWS.setEnabled( true );
                        jMenuModUWS.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_DEL_SNAP:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        jMenuSnap.setEnabled( true );
                        jMenuDelSnap.setEnabled( true );
                        jMenuEvrSnap.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_SRC_UWS_SRV:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        jMenuModSrcUWSrv.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_ORPH_VOLLIST:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        jMenuVol.setEnabled( true );
                        jMenuCrtVol.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_ORPH_VOL:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        jMenuVol.setEnabled( true );
                        jMenuDelVol.setEnabled( true );
                        jMenuOnlineVol.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_SNAPLIST:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        jMenuSnap.setEnabled( true );
                        jMenuCrtSnap.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_SNAP:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        jMenuView.setEnabled( true );
                        jMenuCrtView.setEnabled( true );
                        jMenuSnap.setEnabled( true );
                        jMenuDelSnap.setEnabled( true );
                        jMenuEvrSnap.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_MIRROR_SNAP:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        jMenuView.setEnabled( true );
                        jMenuCrtView.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_VIEW:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        jMenuView.setEnabled( true );
                        jMenuDelView.setEnabled( true );
                        jMenuOnlineVol.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_LMLIST:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        jMenuLunMap.setEnabled( true );
                        jMenuCrtLm.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_LM:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        jMenuLunMap.setEnabled( true );
                        jMenuDelLm.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_POOL:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        jMenuDelPool.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_MJLIST:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        jMenuCrtMj.setEnabled( true );
                        jMenuCrtLMj.setEnabled(true);
                        jMenuCrtMj1.setEnabled( true );
                        jMenuDelMj.setEnabled( false );
                        jMenuModMj.setEnabled( false );
                        jMenuStartMj.setEnabled( false );
                        jMenuStopMj.setEnabled( false );
                        jMenuMonMj.setEnabled( false );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_MJ:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        Object selObj = view.getSelectedObjFromSanBoot();
                        MirrorJob mj = (MirrorJob)selObj;
                        if( mj != null ){
                            jMenuCrtMj.setEnabled( false );
                            jMenuCrtLMj.setEnabled(false);
                            jMenuCrtMj1.setEnabled( false );
                            jMenuDelMj.setEnabled( !mj.isCjType() );
                            jMenuModMj.setEnabled( !mj.isCjType() );
                            jMenuStartMj.setEnabled( !mj.isCjType() );
                            jMenuStopMj.setEnabled( !mj.isCjType() );
                            jMenuMonMj.setEnabled( !mj.isCjType() );
                        }
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_MIRROR_VOL:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        this.jMenuRollbackVol.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_SRC_AGENT:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        Object selObj = view.getSelectedObjFromSanBoot();
                        if( ( selObj == null ) || !( selObj instanceof SourceAgent ) ) return;
                        SourceAgent srcAgnt = (SourceAgent)selObj;
                        if( srcAgnt.isNormalHost() ){
                            if( srcAgnt.isWinHost() ){
                                jMenuDR.setEnabled( true );
                                jMenuFailover.setEnabled( true );
                                this.jMenuRollback.setEnabled( true );
                                 btnFailover.setEnabled( true );
                            }else{
                                jMenuSelBootVer.setEnabled( true );
                                this.jMenuIBootForLinux.setEnabled( true );
                                this.jMenuRollbackForNWin.setEnabled( true );
                            }
                        }else{
                            if( srcAgnt.isWinHost() ){
                                jMenuDR.setEnabled( true );
                                jMenuFailover.setEnabled( true );
                                 btnFailover.setEnabled( true );
                            }else{
                                jMenuSelBootVer.setEnabled( true );
                                this.jMenuIBootForLinux.setEnabled( true );
                            }
                            this.jMenuDelSrcAgnt.setEnabled( true );
                        }

                        /*
                        this.jMenuRollback.setEnabled( true );
                        this.jMenuRollbackForNWin.setEnabled( true );
                        this.jMenuDelSrcAgnt.setEnabled( true );

                        jMenuDR.setEnabled( true );
                        jMenuSelBootVer.setEnabled( true );
                        jMenuIBootForLinux.setEnabled( true );
                        jMenuFailover.setEnabled( true );
                        btnFailover.setEnabled( true );
                         */
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_NETBOOTED_HOST:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user != null ){
                    if( user.isAdminRight() ){
                        jMenuDR.setEnabled( true );
                        jMenuFailback.setEnabled( true );
                        jMenuRestoreData.setEnabled( true );
                        btnRstOriData.setEnabled( true );
                        jMenuRestDataForNWinHost.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_VOL:
            case MenuAndBtnCenterForMainUi.LEVEL_LV:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        jMenuOnlineVol.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_PROFLIST:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( ( user != null ) && user.isAdminRight() ){
                    Object selObj = view.getSelectedObjFromSanBoot();
                    if( ( selObj == null ) || !( selObj instanceof ChiefProfile ) ) return;

                    ChiefProfile chiefProf = (ChiefProfile)selObj;
                    if( chiefProf != null ){
                        BrowserTreeNode hostNode = chiefProf.getFatherNode();
                        if( hostNode != null ){
                            Object uObj = hostNode.getUserObject();
                            if( uObj instanceof BootHost ){
                                BootHost host = (BootHost)uObj;
                                if( ( host != null ) && host.isMTPPProtect() ){
                                    jMenuProf.setEnabled( true );
                                    addProfile.setEnabled( true );
                                }
                            }else{
                                Cluster cluster = (Cluster)uObj;
                                if( ( cluster != null ) && cluster.isMTPPProtect() ){
                                    jMenuProf.setEnabled( true );
                                    addProfile.setEnabled( true );
                                }
                            }
                        }
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_PROF:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        jMenuProf.setEnabled( true );
                        modProfile.setEnabled( true );
                        delProfile.setEnabled( true );
                        runProfile.setEnabled( true );
                        verifyProfile.setEnabled( true );
                        renameProfile.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_PPPROF:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        this.modPhyProfile.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_SCH:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        jMenuSch.setEnabled( true );
                        modSch.setEnabled( true );
                        delSch.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_UIM_VOL:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        this.jMenuQueryUISnap.setEnabled( true );
                        this.jMenuDelUiMirrorVol.setEnabled( true );
                        this.jMenuRollbackVol.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_UIM_SNAP_LIST:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        this.jMenuQueryUISnap.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_UIM_SNAP:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        this.jMenuCloneDisk.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_CLONE_DISK:
                user = view.initor.mdb.getBakUserOnName(
                    view.initor.user
                );
                if( user!=null ){
                    if( user.isAdminRight() ){
                        this.jMenuDelCloneDisk.setEnabled( true );
                    }
                }
                break;
            case MenuAndBtnCenterForMainUi.LEVEL_ROOT:
            case MenuAndBtnCenterForMainUi.LEVEL_ACTLINK:
            case MenuAndBtnCenterForMainUi.LEVEL_USER_LIST:
            case MenuAndBtnCenterForMainUi.LEVEL_POOLLIST:
            case MenuAndBtnCenterForMainUi.LEVEL_USER:
            case MenuAndBtnCenterForMainUi.LEVEL_MIRROR_SNAP_VIEW:
            case MenuAndBtnCenterForMainUi.LEVEL_SCHLIST:
            case MenuAndBtnCenterForMainUi.LEVEL_ADSCHLIST:
            case MenuAndBtnCenterForMainUi.LEVEL_UNKNOWN:
            default:
                break;
        }
    }

    public void ctrlMenuAndBtnOnRight( int right ){
        if( BackupUser.hasThisRight( right, BackupUser.RIGHT_ADMIN ) ){
            return;
        }

        this.jMenuInit.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_INIT_HOST ) );
        this.btnInit.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_INIT_HOST ) );
        this.jMenuFailover.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_NETBOOT ) );
        this.btnFailover.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_NETBOOT ) );
        this.jMenuRestoreData.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_RST_ORG_DISK ) );
        this.btnRstOriData.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_RST_ORG_DISK ) );
        this.jMenuFailback.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_LOCAL_DISK_BOOT) );
        this.addProfile.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_CRT_PROF) );
        this.modProfile.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_MOD_PROF) );
        this.delProfile.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_DEL_PROF) );
        this.runProfile.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_RUN_PROF) );
        this.verifyProfile.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_VER_PROF) );
        this.renameProfile.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_REN_PROF) );

        if(  BackupUser.hasThisRight( right, BackupUser.RIGHT_CRT_SCH) ||
             BackupUser.hasThisRight( right, BackupUser.RIGHT_MOD_SCH) ||
             BackupUser.hasThisRight( right, BackupUser.RIGHT_DEL_SCH)
        ){
             this.jMenuSch.setEnabled( true );
        }
        this.addSch.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_CRT_SCH)  );
        this.addADSch.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_CRT_SCH) );
        this.modSch.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_MOD_SCH) );
        this.delSch.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_DEL_SCH)  );

        this.jMenuUserMgr.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_MOD_PWD) );
        this.jMenuDhcpSet.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_DHCP) );
        this.shutdown.setEnabled( BackupUser.hasThisRight( right, BackupUser.RIGHT_SHUTDOWN) );
        this.jMenuAudit.setEnabled( false );
        this.jMenuMjob.setEnabled( false );
        this.jMenuSetVMServiceInfo.setEnabled( false );
    }

    private void setEnabledForGolbalMenuAndBtn(){
        //BackupUser user = view.initor.mdb.getBakUserOnName(
        //    view.initor.user
        //);

        //if( user!=null ){
          //  if( user.isAdminRight() ){
                if( view.audit.isLoginUsrIsAdmin() ){
                    this.jMenuWinPro.setEnabled( true );
                    this.jMenuNonWinPro.setEnabled( true );
                    this.jMenuWinPhyPro.setEnabled( true );
                    this.jMenuNonWinPhyPro.setEnabled( true );
                    jMenuInit.setEnabled(true);
                    btnInit.setEnabled(true);
                    jMenuInitNWinHost.setEnabled( true );
                    this.jMenuWinPhyInit.setEnabled( true );

                    jMenuHost.setEnabled( true );
                    jMenuCrtHost.setEnabled( true );
                    jMenuDelHost.setEnabled( false );
                    jMenuModHost.setEnabled( false );

                    jMenuCrtUWS.setEnabled( true );
                    jMenuDelUWS.setEnabled( false );
                    jMenuModUWS.setEnabled( false );

                    jMenuPool.setEnabled( true );
                    jMenuCrtPool.setEnabled( true );

                    jMenuDhcpSet.setEnabled( true );
                    jMenuMsgReport.setEnabled( true );
                    jMenuModPass.setEnabled( true );
                    btnUWSRpt.setEnabled( true );
                    jMenuSetVMServiceInfo.setEnabled( true );
                    
                    jMenuSch.setEnabled( true );
                    addSch.setEnabled( true );
                    addADSch.setEnabled(true);
                    dupLog.setEnabled( true );
                    btnDupLog.setEnabled( true );
                    monitorDup.setEnabled( true );
                    btnMon.setEnabled( true );
                    jMenuSDHM.setEnabled( true );
                    btnRefresh.setEnabled(true);

                    jMenuUserMgr.setEnabled( true );
                    jMenuAudit.setEnabled( true );
                    jMenuMjob.setEnabled( true );
                    shutdown.setEnabled( true );
                }
            //}
        //}
    }

    private void setDisableForMenuAndBtn( ){
        jMenuProperty.setEnabled( false );
        jMenuWinPro.setEnabled( false );
        jMenuNonWinPro.setEnabled( false );
        this.jMenuWinPhyPro.setEnabled( false );
        this.jMenuNonWinPhyPro.setEnabled( false );
        jMenuInit.setEnabled( false);
        jMenuWinPhyInit.setEnabled( false );
        btnInit.setEnabled( false );
        jMenuInitNWinHost.setEnabled( false );
        jMenuSelBootVer.setEnabled(false);
        jMenuSelBootVerams.setEnabled(false);
        jMenuIBootForLinux.setEnabled(false );
        jMenuIBootForLinuxams.setEnabled(false );
        jMenuDR.setEnabled( false );
        jMenuWinPhyDR.setEnabled( false );
        jMenuFailover.setEnabled( false );
        jMenuWinPhyFailover.setEnabled(false );
        jMenuFailback.setEnabled( false );
        jMenuWinPhyFailback.setEnabled(false);
        btnFailover.setEnabled( false );
        jMenuRestoreData.setEnabled( false );
        jMenuWinPhyRestoreData.setEnabled(false);
        jMenuRollback.setEnabled( false );
        btnRstOriData.setEnabled( false );
        jMenuRestDataForNWinHost.setEnabled( false );
        jMenuRestDataForNWinHostams.setEnabled( false );
        jMenuRollbackForNWin.setEnabled( false );

        jMenuVol.setEnabled( false );
        jMenuCrtVol.setEnabled( false );
        jMenuDelVol.setEnabled( false );
        jMenuOnlineVol.setEnabled( false );

        jMenuView.setEnabled( false );
        jMenuCrtView.setEnabled( false );
        jMenuDelView.setEnabled( false );
        jMenuCrtUcsSnap.setEnabled( false );

        jMenuPool.setEnabled( false );
        jMenuCrtPool.setEnabled( false );
        jMenuDelPool.setEnabled( false );

        jMenuSnap.setEnabled( false );
        jMenuCrtSnap.setEnabled( false );
        jMenuDelSnap.setEnabled( false );
        jMenuEvrSnap.setEnabled( false );

        jMenuLunMap.setEnabled( false );
        jMenuCrtLm.setEnabled( false );
        jMenuDelLm.setEnabled( false );

        jMenuHost.setEnabled( false );
        jMenuCrtHost.setEnabled( false );
        jMenuDelHost.setEnabled( false );
        jMenuModHost.setEnabled( false );

        jMenuCrtUWS.setEnabled( false );
        jMenuDelUWS.setEnabled( false );
        jMenuModUWS.setEnabled( false );

        jMenuCrtMj.setEnabled( false );
        jMenuCrtLMj.setEnabled(false);
        jMenuDelMj.setEnabled( false );
        jMenuModMj.setEnabled( false );
        jMenuStartMj.setEnabled( false );
        jMenuStopMj.setEnabled( false );
        jMenuMonMj.setEnabled( false );

        jMenuProf.setEnabled( false );
        addProfile.setEnabled( false );
        modProfile.setEnabled( false );
        modPhyProfile.setEnabled( false );
        delProfile.setEnabled( false );
        runProfile.setEnabled( false );
        verifyProfile.setEnabled( false );
        renameProfile.setEnabled( false );
        jMenuSch.setEnabled( false );
        addADSch.setEnabled( false );
        addSch.setEnabled( false );
        delSch.setEnabled( false );
        modSch.setEnabled( false );
        dupLog.setEnabled( false );
        btnDupLog.setEnabled( false );
        monitorDup.setEnabled( false );
        btnMon.setEnabled( false );
        jMenuModSrcUWSrv.setEnabled( false );
        jMenuDhcpSet.setEnabled( false );
        jMenuMsgReport.setEnabled( false );
        jMenuModPass.setEnabled( false );
        btnUWSRpt.setEnabled( false );
        jMenuSetVMServiceInfo.setEnabled( false );
        
        jMenuCrtUWS.setEnabled( false );
        jMenuDelUWS.setEnabled( false );
        jMenuModUWS.setEnabled( false );

        jMenuCrtMj.setEnabled( false );
        jMenuCrtLMj.setEnabled(false);
        jMenuCrtMj1.setEnabled( false );
        jMenuSDHM.setEnabled( false );
	jMenuDelMj.setEnabled( false );
        jMenuModMj.setEnabled( false );
        jMenuStartMj.setEnabled( false );
        jMenuStopMj.setEnabled( false );
        jMenuMonMj.setEnabled( false );

        jMenuRollbackVol.setEnabled( false );
        jMenuRollbackForNWin.setEnabled( false );
        jMenuRollback.setEnabled( false );
        jMenuDelSrcAgnt.setEnabled( false );

        jMenuCloneDisk.setEnabled( false );
        jMenuDelUiMirrorVol.setEnabled( false );
        jMenuDelCloneDisk.setEnabled( false );
        jMenuQueryUISnap.setEnabled( false );

        jMenuUserMgr.setEnabled( false );
        jMenuAudit.setEnabled( false );
        jMenuMjob.setEnabled( false );
        shutdown.setEnabled( false );
        btnRefresh.setEnabled(false);
    }

    public void setEnabledOnLogin( boolean val ){
        jMenuLogin.setEnabled( val );
    }

    // 判断是否为logical protection function
    private boolean isLPFunction( int funcID ){
        int[] funList = new int[]{
            MenuAndBtnCenterForMainUi.FUNC_INIT,
            MenuAndBtnCenterForMainUi.FUNC_FAILOVER,
            MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_NETDISK,
            MenuAndBtnCenterForMainUi.FUNC_RESTORE_DISK,
            MenuAndBtnCenterForMainUi.FUNC_WIN_DUP_NETDISK,
            MenuAndBtnCenterForMainUi.FUNC_FAILBACK,
            MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_LOCALDISK,
            MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN,
            MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN,
            MenuAndBtnCenterForMainUi.FUNC_SELECT_BOOT_VER,
            MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD,
            MenuAndBtnCenterForMainUi.FUNC_ADD_PROF,
            MenuAndBtnCenterForMainUi.FUNC_MOD_PROF,
            MenuAndBtnCenterForMainUi.FUNC_DEL_PROF,
            MenuAndBtnCenterForMainUi.FUNC_RENAME_PROF,
            MenuAndBtnCenterForMainUi.FUNC_RUN_PROF,
            MenuAndBtnCenterForMainUi.FUNC_VERIFY_PROF,
            MenuAndBtnCenterForMainUi.FUNC_ADD_SCH,
            MenuAndBtnCenterForMainUi.FUNC_MOD_SCH,
            MenuAndBtnCenterForMainUi.FUNC_DEL_SCH,
            MenuAndBtnCenterForMainUi.FUNC_DUP_LOG,
            MenuAndBtnCenterForMainUi.FUNC_MONITOR_DD
        };
        for( int i=0; i<funList.length; i++ ){
            if( funList[i] == funcID ){
                return true;
            }
        }
        return false;
    }

    // 判断是否为physical protection function
    private boolean isPPFunction( int funcID ){
        int[] funList = new int[]{
            MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_INIT,
            MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER,
            MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_NETDISK,
            MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_DUP_NETDISK,
            MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_LOCALDISK,
            MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILBACK,
            MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_RESTORE_DISK,
            MenuAndBtnCenterForMainUi.FUNC_PHY_MOD_PROFILE,
            MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_INIT_PROGRESS,
            MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_SYNC_STATE,
            MenuAndBtnCenterForMainUi.FUNC_PHY_START_AUTO_CRT_SNAP
        };
        for( int i=0; i<funList.length; i++ ){
            if( funList[i] == funcID ){
                return true;
            }
        }
        return false;
    }

    //////////////////////////////////////////////////////////////
    //
    //            ��下 面 是 对 右 键 的 支 持
    //
    //     下面的 selectedObj 是PopMenu上选中的菜单项,
    //     右键的行为应该针对它展开
    //
    //////////////////////////////////////////////////////////////
    private JPopupMenu popmenu = new JPopupMenu();
    PopupMenuListener popupMenuListener;

    private Object selectedObj = null;
    public Object getSelectedObjForPopupMenu(){
        return selectedObj;
    }

    public int addPopMenuItem( Object selectedObj,int type ){
        int i,fsize,okcnt,funcId;
        boolean addSeparator = true;

        popmenu.removeAll();
        okcnt = 0;

        ArrayList funcSet = ((GeneralInfo)selectedObj).getFunctionList( type );
        if( funcSet == null ) return okcnt;

        fsize = funcSet.size();
        for( i=0; i<fsize; i++ ){
            Object func = funcMap.get( (Integer)funcSet.get(i) );
            if( func == null ) {
                continue;
            }
            funcId = ( (Integer)funcSet.get(i) ).intValue();

            if( func instanceof CustomMenuItem ){
                CustomMenuItem oldItem = (CustomMenuItem)func;
                CustomMenuItem item = new CustomMenuItem(
                    oldItem.getPopMenuText(),
                    oldItem.getIcon(),
                    oldItem.getAccelerator(),
                    oldItem.getActionObj()
                );
                if( this.isLPFunction( funcId ) ){
                    item.setEnabled( view.initor.mdb.isSupportMTPP() );
                }else if( this.isPPFunction( funcId ) ){
                    item.setEnabled( view.initor.mdb.isSupportCMDP() );
                }
                okcnt++;
                popmenu.add( item );
            }else{
                if( addSeparator ){
                    popmenu.addSeparator();
                }
            }
        }

        return okcnt;
    }

    /*
    private boolean checkPerm( int func ){
        if( !view.initor.isConnected ) return false;
        VssUser user = view.initor.mdb.getVssUserOnUserName(
            view.initor.user
        );
        if( user.isAdminRight() ){
            return true;
        }else{
            if( func == MenuAndBtnCenter.FUNC_MODPASSWD ){

                return true;
            }else{
                return false;
            }
        }
    }
    */

    public void showPopupMenu( Object _selectedObj,Component fatherComp,int type,int x,int y ){
        selectedObj = _selectedObj;
        if( addPopMenuItem( selectedObj,type ) > 0 ){
            popmenu.removePopupMenuListener( popupMenuListener );
            if( type == Browser.POPMENU_TREE_TYPE ){
                popupMenuListener = new PopupMenuListenerForTree();
            }else if( type == Browser.POPMENU_TABLE_ITEM_TYPE ){
                popupMenuListener = new PopupMenuListenerForTable();
            }
            popmenu.addPopupMenuListener( popupMenuListener );

            if( view.initor.isLogined() ) {
                popmenu.show( fatherComp,x,y );
            }
        }
    }

    class PopupMenuListenerForTree implements PopupMenuListener{
        public void popupMenuCanceled(PopupMenuEvent e){
        }

        public void popupMenuWillBecomeVisible(PopupMenuEvent e){
            view.setIsRightClicked(true);
            view.setTempPath();
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e){
            view.setIsRightClicked(false);
        }
    }

    class PopupMenuListenerForTable implements PopupMenuListener{
        public void popupMenuCanceled(PopupMenuEvent e){
        }

        public void popupMenuWillBecomeVisible(PopupMenuEvent e){
            view.setTempPathOnTable();
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e){
        }
    }
}
