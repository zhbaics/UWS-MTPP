/*
 * BootHost.java
 *
 * Created on July 8, 2005, 5:43 PM
 */

package guisanboot.data;

import javax.swing.*;
import java.util.ArrayList;
import mylib.UI.BasicUIObject;
import guisanboot.res.*;
import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.ui.SanBootView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author  Administrator
 */

public class BootHost extends BasicUIObject {  
    public final static String W_2000 = "2000";
    public final static String W_XP   = "XP";
    public final static String W_2003 = "2003";
    public final static String W_VISTA  = "VISTA";
    public final static String W_2008 = "2008";
    public final static String W_2008R2 = "2008R2";
    public final static String W_WIN7   = "WIN7";
    public final static String W_HIGH = "HIGH"; // > win2003
    public final static String W_LOW  = "LOW";  // < win2000
    
    public final static int PROTECT_TYPE_MTPP = 1;
    public final static int PROTECT_TYPE_CMDP = 2;
    public final static int PROTECT_TYPE_UNKNOWN = 99;
    
    public final static int CLNT_OPT_BIT_CLUSTER = 0x1;
    
    public final static int BOOT_MODE_EM = 1;
    public final static int BOOT_MODE_IBOOT = 2;
    public final static int BOOT_MODE_ISCSI_HBA = 3;
    public final static int BOOT_MODE_WINDOWSPE = 4;
    
    public final static String CLNTRECFLAG ="Record:";
    public final static String CMDPRECFLAG ="---index";
    public final static String CLNTID ="clnt_id";
    public final static String CLNTNAME ="clnt_name";
    public final static String CLNTIP ="clnt_ip";
    public final static String CLNTMACHINE ="clnt_machine";
    public final static String CLNTPORT = "clnt_port";
    public final static String CLNTPORT1 = "clnt_port1";
    public final static String CLNTOS ="clnt_os";
    public final static String CLNTSTATUS = "clnt_status";
    public final static String CLNT_CONF_IP = "clnt_conf";
    public final static String CLNT_INIT_FLAG="clnt_inited_flag";
    public final static String CLNT_AUTO_DR_FLAG="clnt_auto_dr_flag";
    public final static String CLNT_AUTO_REBOOT_FLAG="clnt_auto_reboot_flag";
    public final static String CLNT_STOP_ALL_BASE_SERV="clnt_stop_base_service";
    public final static String CLNT_BOOT_MAC="clnt_mac_address";
    public final static String CLNT_BOOT_MODE="clnt_boot_mode";
    public final static String CLNT_MODE = "clnt_mode";
    public final static String CLNT_BOOT_PORTNAME = "clnt_portname";
    public final static String CLNT_PROTECT_TYPE = "clnt_protect_type";
    public final static String CLNT_PRI_IP = "clnt_pri_ip";
    public final static String CLNT_VIP = "clnt_vip";
    public final static String CLNT_CLUSTER_ID = "clnt_cluster_id";
    public final static String CLNT_OPT = "clnt_opt";
    public final static String CLNT_D2D_CID = "clnt_d2d_cid";
    
    protected int clnt_id;
    protected String clnt_name;//60
    protected String clnt_ip;// 32
    
    // 当主机类型为CLNT_MODE_ROLLBACK时，这里存放了rollback的来源，即某个SourceAgent的ID。
    // 根据这个ID值，在做linux的网络启动时，在tftproot_path/remir目录中可以得到对应的网络启动信息
    protected String clnt_machine=""; // 16
    
    protected int clnt_port  = ResourceCenter.CMDP_AGENT_PORT;  // cmdp port
    protected int clnt_port1 = ResourceCenter.MTPP_AGENT_PORT;  // mtpp port
    protected String clnt_os;// 30
    protected String clnt_status="Online"; //10
    
    // 存放UUID
    protected String clnt_conf_ip; // 4096 
    
    protected int clnt_inited_flag;
    protected int clnt_auto_dr_flag;
    protected int clnt_auto_reboot_flag; 
    
    // clnt_stop_base_service是一个标志位，目前定义如下：
    public static final int BIT0_DHCP = 0x1;   // 使用uws服务器上的dhcp服务
    public static final int BIT1_BASE_SERVICE = 0x2;  // 停止所有的基本服务
    
   // 对于windows来说两个标志位都要用；而对于linux来说只需要第一个标志位
    protected int clnt_stop_base_service;
    
    protected String boot_mac = "";
    protected int boot_mode = BOOT_MODE_EM;  // 1: emboot 2:iboot 3: iscsi hba
    
    protected int clnt_mode;
    protected String clnt_portname = ""; // 30

    // 保护的类型; 1: mtpp  2:cmdp  0:未知（当解析getBootHost命令的clnt_protect_type出错时，就赋值为0）
    protected int clnt_protect_type = PROTECT_TYPE_MTPP;
    protected int clnt_cluster_id = 0;
    private int clnt_d2d_cid = 0 ;
    protected String clnt_pri_ip = "";
    protected String clnt_vip = "";  // 可能含有用;隔开的一系列vip
    protected int clnt_opt = 0;

    /**
     * Creates a new instance of BootHost
     */
    public BootHost() {
        super( ResourceCenter.TYPE_HOST_INDEX );
    }
    
    public BootHost( String name,String ip,int port,int port1,String machine,String os,String uuid ){
        this( -1,name, ip, machine, port, port1,os, "Online", uuid, 0,0,0,0,"",BOOT_MODE_EM,0,"",PROTECT_TYPE_MTPP );
    }
    
    public BootHost( String name,String ip,String src_agnt_id,int port,int port1,String os, String uuid,int boot_mode ){
        this( -1,name,ip,src_agnt_id,port,port1,os,"Online",uuid,1,0,0,0,"",boot_mode,0,"",PROTECT_TYPE_MTPP );
    }
    
    public BootHost(
        int _clnt_id,
        String _clnt_name,
        String _clnt_ip,
        String _clnt_machine,
        int _clnt_port,
        int _clnt_port1,
        String _clnt_os,
        String _clnt_status,
        String _clnt_conf_ip,
        int _clnt_inited_flag,
        int _clnt_auto_dr_flag,
        int _clnt_auto_reboot_flag,
        int _clnt_stop_base_service,
        String _clnt_boot_mac,
        int boot_mode,
        int protect_mode
    ){
        super( ResourceCenter.TYPE_HOST_INDEX  );
        
        clnt_id= _clnt_id;
        clnt_name= _clnt_name;
        clnt_ip = _clnt_ip;
        clnt_machine= _clnt_machine;
        clnt_port= _clnt_port;
        clnt_port1 = _clnt_port1;
        clnt_os= _clnt_os;
        clnt_status= _clnt_status;
        clnt_conf_ip = _clnt_conf_ip;
        clnt_inited_flag = _clnt_inited_flag;
        clnt_auto_dr_flag= _clnt_auto_dr_flag;
        clnt_auto_reboot_flag = _clnt_auto_reboot_flag;
        clnt_stop_base_service = _clnt_stop_base_service;
        boot_mac = _clnt_boot_mac;
        this.boot_mode = boot_mode;
        this.clnt_protect_type = protect_mode;
    }
    
    public BootHost(
        int _clnt_id,
        String _clnt_name,
        String _clnt_ip,
        String _clnt_machine,
        int _clnt_port,
        int _clnt_port1,
        String _clnt_os,
        String _clnt_status,
        String _clnt_conf_ip,
        int _clnt_inited_flag,
        int _clnt_auto_dr_flag,
        int _clnt_auto_reboot_flag,
        int _clnt_stop_base_service,
        String _clnt_boot_mac,
        int _clnt_boot_mode,
        int _clnt_mode,
        String _clnt_portname,
        int _clnt_protect_type
    ){
        super( ResourceCenter.TYPE_HOST_INDEX  );
        
        clnt_id= _clnt_id;
        clnt_name= _clnt_name;
        clnt_ip = _clnt_ip;
        clnt_machine= _clnt_machine;
        clnt_port= _clnt_port;
        clnt_port1 = _clnt_port1;
        clnt_os= _clnt_os;
        clnt_status= _clnt_status;
        clnt_conf_ip = _clnt_conf_ip;
        clnt_inited_flag = _clnt_inited_flag;
        clnt_auto_dr_flag= _clnt_auto_dr_flag;
        clnt_auto_reboot_flag = _clnt_auto_reboot_flag;
        clnt_stop_base_service = _clnt_stop_base_service;
        boot_mac = _clnt_boot_mac;
        boot_mode = _clnt_boot_mode;
        clnt_mode = _clnt_mode;
        clnt_portname = _clnt_portname;
        clnt_protect_type = _clnt_protect_type;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>(9); 
            if( this.isWinHost() ){
                if( this.isMTPPProtect() ){
                    if( !this.isCluster() ){
                        fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT ) );
                        fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ));
                    }
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_RESTORE_DISK ));
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_FAILBACK ));
                    fsForTree.add( new Integer(  MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_NETDISK ));
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_DUP_NETDISK ));
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_LOCALDISK));
                }else{ // cmdp
                    if( !this.isCluster() ){
                        fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_INIT ) );
                        fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER ));
                    }
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_RESTORE_DISK ));
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILBACK ));
                    fsForTree.add( new Integer(  MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_NETDISK ));
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_DUP_NETDISK ));
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_LOCALDISK));
                    if( !this.isCluster() ){
                        fsForTree.add( new Integer(  MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                        fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_INIT_PROGRESS ) );
                    }
                }
            }else{ // Linux
                if( this.isMTPPProtect() ){
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN  ) );
                    if("x86_64".equals(this.getMachine().toLowerCase())){
                        fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ));
                    } else {
                        fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SELECT_BOOT_VER ));
                        fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ));
                    }
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN ) );
                }else if( this.isLVMProtect() ){
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN_LVM  ) );
                    if("x86_64".equals(this.getMachine().toLowerCase())){
                        fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ));
                    }
                }else{ //ams
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN_AMS  ) );
                    if("x86_64".equals(this.getMachine().toLowerCase())){
                        fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ));
                    } else {
                        fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SELECT_BOOT_VER ));
                        fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ));
                    }
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN ) );

                    fsForTree.add( new Integer(  MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_NETDISK ));
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_DUP_NETDISK ));
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_LOCALDISK));

                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_AMS_INIT_PROGRESS ) );
                }
            }
            if( !this.isCluster() ){
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_HOSTBOOT ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_HOST ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_HOST ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PROPERTY));
            }
        }
    }
    
    public void addFunctionsForTable(){        
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>(9);
            if( this.isWinHost() ){
                if( this.isMTPPProtect() ){
                    if( !this.isCluster() ){
                        fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_INIT ) );
                        fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ));
                    }
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_RESTORE_DISK ));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_FAILBACK ));
                    fsForTable.add( new Integer(  MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_NETDISK ));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_DUP_NETDISK ));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_LOCALDISK));
                }else{ // cmdp
                    if( !this.isCluster() ){
                        fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_INIT ) );
                        fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER ));
                    }
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_RESTORE_DISK ));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILBACK ));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_NETDISK ));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_DUP_NETDISK ));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_LOCALDISK));
                    if( !this.isCluster() ){
                        fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                        fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_INIT_PROGRESS ) );
                    }
                }
            }else{ // Linux
                if( this.isMTPPProtect() ){
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN  ) );
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SELECT_BOOT_VER ));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ));
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN ) );
                }else if( this.isLVMProtect() ){
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN_LVM  ) );
                    if("x86_64".equals(this.getMachine().toLowerCase())){
                        fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ));
                    }
                }else{
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN_AMS  ) );
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SELECT_BOOT_VER ));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ));
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN ) );

                    fsForTable.add( new Integer(  MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_NETDISK ));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_DUP_NETDISK ));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_LOCALDISK));

                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_AMS_INIT_PROGRESS ) );
                }
            }
            if( !this.isCluster() ){
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_HOSTBOOT ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_HOST ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_HOST ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PROPERTY)); 
            }
        }
    }
    
    public int getID(){
        return clnt_id;
    }
    public void setID( int _id ){
        clnt_id = _id;
    }
    
    public String getName(){
        return clnt_name;
    }
    public void setName( String val ){
        clnt_name = val;
    }
    
    public String getIP(){
        return clnt_ip;
    }
    public String getLastItemOfIp(){
        Pattern pattern = Pattern.compile(
            "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$"
        );
        Matcher matcher = pattern.matcher( clnt_ip );
        if( matcher.find() ){
            return matcher.group( 4 );
        }else{
            return "";
        }
    }
    public void setIP( String val ){
        clnt_ip = val;
    }
    
    public String getMachine(){
        return clnt_machine; 
    }
    public void setMachine( String val ){
        clnt_machine = val;
    }
    
    public boolean is64BitOS(){
        return ( clnt_machine.indexOf("_64") >= 0 );
    }

    public int getPort(){
        return clnt_port;
    }
    public void setPort( int val ){
        clnt_port = val;
    }
    public int getCmdpPort(){
        return this.getPort();
    }
    public void setCmdpPort( int val ){
        setPort( val );
    }

    public int getPort1(){
        return clnt_port1;
    }
    public void setPort1( int val ){
        clnt_port1 = val;
    }
    public int getMtppPort(){
        return this.getPort1();
    }
    public void setMtppPort( int val ){
        setPort1( val );
    }

    public String getOS(){
        return clnt_os;
    }
    public void setOS( String val ){
        clnt_os = val;
    }
    
    public static boolean isWinHost( String os ){
        return os.toUpperCase().startsWith("WIN");
    }
    public boolean isWinHost(){
        return isWinHost( clnt_os );
    }
    
    public String getWinPlatForm(){ 
        try{
            String release = clnt_os.substring(3);
            return release.toUpperCase();
        }catch(Exception ex){
            return W_2003;
        }
    }
    
    public static boolean supportVss( String type ){       
        return type.equals( W_XP ) ||
               type.equals( W_2003 ) ||
               type.equals( W_VISTA ) ||
               type.equals( W_2008 ) ||
               type.equals( W_2008R2 )||
               type.equals( W_WIN7 );
    }
    
    public boolean supportVss(){
        String type = getWinPlatForm();
        return supportVss( type );
    }
    
    public static boolean supportSysCopy( String type ){
        return type.equals( W_2000 );
    }
    
    public boolean supportSysCopy(){
        String type = getWinPlatForm();
        return supportSysCopy( type );
    }
    
    public static boolean isWin2000( String type ){
        return type.equals( W_2000 );
    }
    
    public static boolean isWin2003( String type ){
        return type.equals( W_2003 );
    }
    
    public boolean isWin2000(){
        String type = getWinPlatForm();
        return isWin2000( type );
    }
    
    public boolean isWin2003(){
        String type = getWinPlatForm();
        return isWin2003( type );
    }

    public boolean isIA(){
        return isIA( this.clnt_machine );
    }
    
    public static boolean isWinVista( String type ){
        return type.toUpperCase().equals( W_VISTA );
    }
    public static boolean isWin2008 ( String type ){
        return type.equals( W_2008 ) || type.toUpperCase().equals( W_2008R2 );
    }
    public static boolean isWin7( String type ){
        return type.toUpperCase().equals( W_WIN7 );
    }
    public boolean isLargerThanVista(){
        String type = getWinPlatForm();
        return isWinVista( type.toUpperCase() ) || isWin2008( type ) || isWin7( type );
    }
    public static boolean isIA( String machine ){
        return machine.toUpperCase().startsWith("IA");
    }
    
    public String getStatus(){
        return clnt_status; 
    }
    public void setStatus(String val ){
        clnt_status = val;
    }
    
    public String getUUID(){
        return clnt_conf_ip;
    }
    public void setUUID( String val ){
        clnt_conf_ip = val;
    }
    public boolean isRealHostInCluster(){
        return !this.getUUID().equals("");
    }

    public int getInitFlag(){
        return clnt_inited_flag;
    }
    public void setInitFlag( int val ){
        clnt_inited_flag = val;
    }
    public boolean isInited(){
        return ( clnt_inited_flag == 1 );
    }
    
    public int getAutoDRFlag(){
        return clnt_auto_dr_flag;
    }
    public void setAutoDRFlag( int val ){
        clnt_auto_dr_flag = val;
    }
    public boolean isAutoDR(){
        return (clnt_auto_dr_flag == 1);
    }
    
    public int getAutoRebootFlag(){
        return clnt_auto_reboot_flag;
    }
    public void setAutoRebootFlag( int val ){
        clnt_auto_reboot_flag = val;
    }
    public boolean isAutoReboot(){
        return ( clnt_auto_reboot_flag == 1);
    }
    
    public int getStopAllBaseServFlag(){
        return clnt_stop_base_service;
    }
    public void setStopAllBaseServFlag( int val ){
        clnt_stop_base_service = val;
    }
    public boolean isStopAllBaseServ(){
        return ( clnt_stop_base_service & BootHost.BIT1_BASE_SERVICE )!=0;
    }
    public boolean isUseOdyDhcp(){
        return ( clnt_stop_base_service & BootHost.BIT0_DHCP ) !=0 ;
    }
    public void  setStopAllBaseServ( ){
        clnt_stop_base_service |=  BootHost.BIT1_BASE_SERVICE;
    }
    public void setUseOdyDhcp(){
        clnt_stop_base_service |= BootHost.BIT0_DHCP;
    }
    public void clearStopAllBaseServ(){
        clnt_stop_base_service &=  ~BootHost.BIT1_BASE_SERVICE;
    }
    public void clearUseOdyDhcp(){
        clnt_stop_base_service &= ~BootHost.BIT0_DHCP;
    }
    
    public String getBootMac(){
        return boot_mac;
    }
    public void setBootMac( String mac ){
        boot_mac = mac;
    }
    
    public int getBootMode(){
        return boot_mode;
    }
    public void setBootMode( int mode ){
        boot_mode = mode;
    }
    public String getBootModeString(){
        if( this.isWinHost() ){
            if( this.isEmBoot() ){
                return SanBootView.res.getString("common.boottype.boot");
            }else if( this.isIBoot() ){
                return SanBootView.res.getString("common.boottype.iboot");
            }else{
                return SanBootView.res.getString("common.boottype.iscsihba");
            }
        }else{
            return "";
        }
    }
    
    public boolean isEmBoot(){
        return ( boot_mode == BootHost.BOOT_MODE_EM );
    }   
    public boolean isIBoot(){
        return ( boot_mode == BootHost.BOOT_MODE_IBOOT );
    }
    public boolean isISCSIHBABoot(){
        return ( boot_mode == BootHost.BOOT_MODE_ISCSI_HBA );
    }
    public boolean isWindowsPEBoot(){
        return ( boot_mode == BootHost.BOOT_MODE_WINDOWSPE );
    }
    
    public int getClntMode(){
        return clnt_mode;
    }
    public void setClntMode( int val ){
        clnt_mode = val;
    }
    
    public String getPortName(){
        return clnt_portname;
    }
    public void setPortName( String val ){
        clnt_portname = val;
    }

    public int getProtectType(){
        return clnt_protect_type;
    }
    public void setProtectType( int val ){
        this.clnt_protect_type = val;
    }
    public boolean isMTPPProtect(){
        return ( clnt_protect_type == BootHost.PROTECT_TYPE_MTPP );
    }
    public boolean isCMDPProtect(){
        return ( clnt_protect_type == BootHost.PROTECT_TYPE_CMDP );
    }

    public boolean isLVMProtect(){
        return clnt_name.endsWith("(LVM)");
    }

    public String getProtectString(){
        if( this.isMTPPProtect() ){
            return SanBootView.res.getString("common.logical");
        }else if( this.isCMDPProtect() ){
            return SanBootView.res.getString("common.physical");
        }else{
            return SanBootView.res.getString("common.unknown");
        }
    }

    public int getClntPort1(){
        return this.clnt_port1;
    }
    public void setClntPort1( int val ){
        this.clnt_port1 = val;
    }

    @Override public String toString(){
        return clnt_name;
    }
    @Override public String getComment(){
        return clnt_id+"";
    }
    
    public static boolean isSameHost( String name1,String name2,String os1,String os2,String mach1,String mach2,String uuid1,String uuid2 ){
        return  name1.equals( name2 ) && os1.equals( os2 ) && mach1.equals( mach2 ) && uuid1.equals( uuid2 );
    }
    
    public static boolean isSameHost1( String name1,String name2,String os1,String os2,String mach1,String mach2,String uuid1,String uuid2,String ip1,String ip2,int port1,int port2 ){
        return name1.equals( name2 ) && os1.equals( os2 ) && mach1.equals( mach2 ) && uuid1.equals( uuid2 ) && ip1.equals(ip2) &&( port1 == port2 );
    }

    public static boolean isSameHost2( String name1,String name2,String os1,String os2,String mach1,String mach2,
       String ip1,String ip2,String pri_ip1,String pri_ip2,String vip1,String vip2,int port1,int port2,int mtpp_port1,int mtpp_port2 ){
        return name1.equals( name2 ) && os1.equals( os2 ) && mach1.equals( mach2 ) &&
                ip1.equals(ip2) && pri_ip1.equals( pri_ip2 ) &&
                vip1.equals( vip2 ) && port1 == port2 && mtpp_port1 == mtpp_port2 ;
    }

    //** TableRevealable的实现**/
    public boolean enableTableEditable(){
        return false;
    }
    public boolean enableSelected(){
        return true;
    }
    public int  getAlignType(){
        return JLabel.LEFT;
    }
    public Icon getTableIcon(){
        if( this.isMTPPProtect() ){
            return ResourceCenter.ICON_NODE_16;
        }else{
            return ResourceCenter.ICON_PHY_NODE_16;
        }
    }
    public String toTableString(){
        return clnt_id+"";
    }
    
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        if( this.isMTPPProtect() ){
            return ResourceCenter.ICON_NODE_16;
        }else{
            return ResourceCenter.ICON_PHY_NODE_16;
        }
    }
    public Icon getCollapseIcon(){
        if( this.isMTPPProtect() ){
            return ResourceCenter.ICON_NODE_16;
        }else{
            return ResourceCenter.ICON_PHY_NODE_16;
        }
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return this.clnt_name;
    }
    public String toTipString(){
        return this.clnt_name+" [ "+clnt_ip+" | "+this.clnt_id + " ] ";
    }

    /**
     * @return the clnt_pri_ip
     */
    public String getClnt_pri_ip() {
        return clnt_pri_ip;
    }

    /**
     * @param clnt_pri_ip the clnt_pri_ip to set
     */
    public void setClnt_pri_ip(String clnt_pri_ip) {
        this.clnt_pri_ip = clnt_pri_ip;
    }

    /**
     * @return the clnt_vip
     */
    public String getClnt_vip() {
        return clnt_vip;
    }
    public boolean isContainedInVip( String one_vip ){
        String[] lines = Pattern.compile(";").split( clnt_vip );
        for( int i=0; i<lines.length; i++ ){
            if( lines[i].equals("") ) continue;
            
            if( lines[i].equals( one_vip ) ){
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param clnt_vip the clnt_vip to set
     */
    public void setClnt_vip(String clnt_vip) {
        this.clnt_vip = clnt_vip;
    }

    /**
     * @return the clnt_cluster_id
     */
    public int getClnt_cluster_id() {
        return clnt_cluster_id;
    }
    public boolean isBelongedCluster(){
        return clnt_cluster_id > 0 ;
    }

    /**
     * @param clnt_cluster_id the clnt_cluster_id to set
     */
    public void setClnt_cluster_id(int clnt_cluster_id) {
        this.clnt_cluster_id = clnt_cluster_id;
    }

    /**
     * @return the clnt_opt
     */
    public int getClnt_opt() {
        return clnt_opt;
    }

    /**
     * @param clnt_opt the clnt_opt to set
     */
    public void setClnt_opt(int clnt_opt) {
        this.clnt_opt = clnt_opt;
    }

    public boolean isCluster(){
        return ( ( clnt_opt & BootHost.CLNT_OPT_BIT_CLUSTER )  != 0 );
    }
    public void setClusterBit(){
        clnt_opt = clnt_opt | BootHost.CLNT_OPT_BIT_CLUSTER;
    }
    
    /**
     * @return the clnt_d2d_cid
     */
    public int getClnt_d2d_cid() {
        return clnt_d2d_cid;
    }

    /**
     * @param clnt_d2d_cid the clnt_d2d_cid to set
     */
    public void setClnt_d2d_cid(int clnt_d2d_cid) {
        this.clnt_d2d_cid = clnt_d2d_cid;
    }

    public BootHost cloneBootHost(){
        BootHost newHost = new BootHost();
        newHost.setID( clnt_id );
        newHost.setName( clnt_name );
        newHost.setIP( clnt_ip );
        newHost.setMachine( clnt_machine );
        newHost.setPort( clnt_port );
        newHost.setClntPort1( clnt_port1 );
        newHost.setOS( clnt_os );
        newHost.setStatus( clnt_status );
        newHost.setUUID( clnt_conf_ip );
        newHost.setInitFlag( clnt_inited_flag );
        newHost.setAutoDRFlag( clnt_auto_dr_flag );
        newHost.setAutoRebootFlag( clnt_auto_reboot_flag );
        newHost.setStopAllBaseServFlag( clnt_stop_base_service );
        newHost.setBootMac( boot_mac );
        newHost.setBootMode( boot_mode );
        newHost.setClntMode( clnt_mode );
        newHost.setProtectType( clnt_protect_type );
        newHost.setClnt_cluster_id( clnt_cluster_id );
        newHost.setClnt_d2d_cid( clnt_d2d_cid );
        newHost.setClnt_pri_ip( clnt_pri_ip );
        newHost.setClnt_vip( clnt_vip );
        newHost.setClnt_opt( clnt_opt );
        return newHost;
    }
}
