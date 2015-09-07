/*
 * DestAgent.java
 *
 * Created on 2008/6/16,�AM 11:02
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.BasicUIObject;

/**
 *
 * @author zjj
 */

// 一个初始化过的主机可以对应多个DestAgent,但是这些DestAgent的dst_agent_mac是不一样的
public class DestAgent extends BasicUIObject {
    public final static int   TYPE_ORI_HOST = 0; // 用户在界面上选择了boothost
    public final static int   TYPE_DST_AGNT = 1; // 用户在界面上选择了boothost下的“网络启动主机”
    public final static int   TYPE_SRC_AGNT = 2; // 用户在界面上选择了srcagent下的“网络启动主机”
    public final static int   TYPE_INVALID_HOST_TYPE = -1;
    
    public final static String DST_REC_FLAG    = "Record:";
    public final static String DST_AGNT_ID     = "dst_agent_id";
    public final static String DST_AGNT_IP     = "dst_agent_ip";
    public final static String DST_AGNT_PORT   = "dst_agent_port";
    public final static String DST_AGNT_OSTYPE = "dst_agent_ostype";
    public final static String DST_AGNT_MAC    = "dst_agent_mac";
    public final static String DST_AGNT_DESC   = "dst_agent_desc";
    public final static String DST_PROTECT_TYPE = "dst_agent_protect_type";
    
    private int dst_agent_id;
    private String dst_agent_ip;      // length: 32
    private int dst_agent_port;       // inherit from boothost or srcagnt
    private int dst_agent_port1;      // mtpp port,assigned by programming(not a field in MDB)
    private String dst_agent_ostype;  // length: 30
    private String dst_agent_mac;     // length: 100,采用简略格式，比如：000C29A51BE6
    private String dst_agent_desc;    // length: 255
    
    //保护的类型; 1: mtpp  2:cmdp  0:未知（当解析GetNetBootedHost命令的dst_agent_protect_type出错时，就赋值为0）
    private int dst_protect_type = BootHost.PROTECT_TYPE_MTPP;
    
    private int src_host_id;  // 对应的BootHost或SourceAgnet的id
    private boolean isBootHostObj = true;  // 区分src_host_id是BootHost的还是SourceAgent的; true: is BootHost / false: is SourceAgent
    private boolean stopAllServ = false;  // 缺省必须在界面上选择服务

    private int host_type = 0 ;
    private String host_uuid ="";
    
    private int boot_mode;
    private String machine;
    private boolean isCluster=false;  // 该desgagnt是否代表了一个cluster的成员节点（或者说跟cluster有关）
    private int cluster_id = -1;

    /** Creates a new instance of DestAgent */
    public DestAgent() {
        super( ResourceCenter.TYPE_DEST_AGENT );
    }

    public DestAgent(
        int dst_agent_id,
        String dst_agent_ip,
        int dst_agent_port,
        String dst_agent_ostype,
        String dst_agent_mac,
        String dst_agent_desc,
        int dst_protect_type
    ){
        super( ResourceCenter.TYPE_DEST_AGENT );
        
        this.dst_agent_id = dst_agent_id;
        this.dst_agent_ip = dst_agent_ip;
        this.dst_agent_port = dst_agent_port;
        this.dst_agent_ostype = dst_agent_ostype;
        this.dst_agent_mac = dst_agent_mac;
        this.dst_agent_desc = dst_agent_desc;
        this.dst_protect_type = dst_protect_type;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
             if( this.isWinHost() ){
                fsForTree = new ArrayList<Integer>( 4 );
                if( this.isMTPPProtect() ){
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_RESTORE_DISK ) );
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILBACK ) );
                }else{
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_RESTORE_DISK ) );
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILBACK ) );
                }
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_DSTAGNT ) );
             }else{
                fsForTree = new ArrayList<Integer>( 3 );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_DSTAGNT ) );
             }
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            if( this.isWinHost() ){
                fsForTable = new ArrayList<Integer>( 4 );
                if( this.isMTPPProtect() ){
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_RESTORE_DISK ) );
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILBACK ) );
                }else{
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_RESTORE_DISK ) );
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILBACK ) );
                }
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_DSTAGNT ) );
             }else{
                fsForTable = new ArrayList<Integer>( 3 );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_DSTAGNT ) );
             }
        }
    }
    
    public int getDst_agent_id() {
        return dst_agent_id;
    }

    public void setDst_agent_id(int dst_agent_id) {
        this.dst_agent_id = dst_agent_id;
    }

    public String getIP(){
        return dst_agent_ip;
    }
    public String getDst_agent_ip() {
        return dst_agent_ip;
    }

    public void setDst_agent_ip(String dst_agent_ip) {
        this.dst_agent_ip = dst_agent_ip;
    }

    public int getPort(){
        return dst_agent_port;
    }
    public int getDst_agent_port() {
        return dst_agent_port;
    }

    public int getPort1(){
        return this.dst_agent_port1;
    }
    public void setPort1( int port1 ){
        this.dst_agent_port1 = port1;
    }
    
    public void setDst_agent_port(int dst_agent_port) {
        this.dst_agent_port = dst_agent_port;
    }

    public String getDst_agent_ostype() {
        return dst_agent_ostype;
    }
    
    public String getWinPlatForm(){
        try{
            String release = dst_agent_ostype.substring(3);
            return release.toUpperCase();
        }catch(Exception ex){
            return "2003";
        }
    }
    
    public boolean supportVss(){
        String type = getWinPlatForm();
        return BootHost.supportVss( type );
    }
    
    public boolean supportSysCopy(){
        String type = getWinPlatForm();
        return BootHost.supportSysCopy( type );
    }
    
    public boolean isLargerThanVista(){
        String type = getWinPlatForm();
        return BootHost.isWinVista( type.toUpperCase() ) || BootHost.isWin2008( type );
    }
    
    public boolean isWin2000(){
        String type = getWinPlatForm();
        return BootHost.isWin2000( type );
    }
    
    public boolean isWinHost(){
        return BootHost.isWinHost( dst_agent_ostype );
    }
    
    public void setDst_agent_ostype(String dst_agent_ostype) {
        this.dst_agent_ostype = dst_agent_ostype;
    }
    
    public String getDst_agent_mac() {
        return dst_agent_mac;
    }
    public void setDst_agent_mac(String dst_agent_mac) {
        this.dst_agent_mac = dst_agent_mac;
    }
    public boolean isRealDestAgent(){
        return !dst_agent_mac.equals("");
    }
    
    public String getDst_agent_desc() {
        return dst_agent_desc;
    }

    public void setDst_agent_desc(String dst_agent_desc) {
        this.dst_agent_desc = dst_agent_desc;
    }
    
    public int getID(){
        return src_host_id;
    }
    public int getSrc_Agnt_id(){
        return src_host_id;
    }
    public void setSrc_Agnt_id( int src_host_id ){
        this.src_host_id = src_host_id;
    }
    
    public boolean isRealBootHost(){
        return isBootHostObj;
    }
    public void setRealBootHostFlag( boolean val ){
        this.isBootHostObj = val; 
    }
    
    public boolean isStopAllServ(){
        return stopAllServ;
    }
    public void setStopAllServFlag( boolean val ){
        stopAllServ = val;
    }
    
    public String getRstMapFileName(){
        // dst_agent_mac不为空,说明它是一个真正的DesAgent;否则它是从一个BootHost/SourceAgent转换来的，mdb中并不存在该dstagnt对象
        if( this.isRealDestAgent() ){
            return ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_DST_AGNT + this.dst_agent_id + ResourceCenter.CONF_RSTMAP;
        }else{
            return ResourceCenter.CLT_IP_CONF + "/" + getID() + ResourceCenter.CONF_RSTMAP;
        }
    }
    
    public String getNetbootDiskInfo(){
        // dst_agent_mac不为空,说明它是一个真正的DesAgent;否则它是从一个BootHost/SourceAgent转换来的，mdb中并不存在该dstagnt对象
        if( this.isRealDestAgent() ){
            return ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_DST_AGNT + this.dst_agent_id + ResourceCenter.CONF_NETBOOT_DISK;
        }else{
            return ResourceCenter.CLT_IP_CONF + "/" + getID() + ResourceCenter.CONF_NETBOOT_DISK;
        }
    }
    
    public String getOldDiskConfAbsPathForUnix(){
        if( this.isRealBootHost() ){
            return ResourceCenter.CLT_IP_CONF + "/" + getID() + ResourceCenter.CONF_OLDDISK;
        }else{
            return ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + getID() + ResourceCenter.CONF_OLDDISK;
        }
    }
    
    public String getOldDiskConfAbsPath(){
        if( this.isRealBootHost() ){
            return ResourceCenter.CLT_IP_CONF + "/" + getID() + ResourceCenter.CONF_OLDDISK;
        }else{
            return ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + getID() + ResourceCenter.CONF_OLDDISK;
        }
    }
    
    public String getOldDiskConfRelativePath(){
        if( this.isRealBootHost() ){
            return getID() + ResourceCenter.CONF_OLDDISK;
        }else{
            return ResourceCenter.PREFIX_SRC_AGNT + getID() + ResourceCenter.CONF_OLDDISK;
        }
    }
    
    public String getTmpPartInfoAbsPath( int new_diskno ){
        if( this.isRealBootHost() ){
            return ResourceCenter.CLT_IP_CONF + "/" + getID() +"."+ new_diskno +".partInfo.tmp";
        }else{
            return ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + getID() +"."+ new_diskno +".partInfo.tmp";
        }
    }
    
    public String getTmpPartInfoRelativePath( int new_diskno ){
        if( this.isRealBootHost() ){
            return  getID() +"."+ new_diskno +".partInfo.tmp";
        }else{
            return  ResourceCenter.PREFIX_SRC_AGNT + getID() +"."+ new_diskno +".partInfo.tmp";
        }
    }
    
    public String getOsloaderType( ){
        if( this.isRealBootHost() ){
            return  ResourceCenter.CLT_IP_CONF  + "/" + getID() + ResourceCenter.CONF_OS_LOADER;
        }else{
            return  ResourceCenter.CLT_IP_CONF  + "/" + ResourceCenter.PREFIX_SRC_AGNT + getID() + ResourceCenter.CONF_OS_LOADER;
        }
    }
    
    // 获取用cmdp方式保护的volume对应的服务  2011.4.21 - jason
    public String getServiceOnVolPath(){
        if( this.isRealBootHost() ){
            return ResourceCenter.CLT_IP_CONF  + "/" + getID() + ResourceCenter.CONF_SERVICE_RELATED_WITH_VOLUME;
        }else{
            return ResourceCenter.CLT_IP_CONF  + "/" + ResourceCenter.PREFIX_SRC_AGNT + getID() + ResourceCenter.CONF_SERVICE_RELATED_WITH_VOLUME;
        }
    }

    public int getHostType(){
        return host_type;
    }
    public void setHostType( int val ){
        host_type = val;
    }
    public boolean isOriginalBootHost(){
        return ( host_type == DestAgent.TYPE_ORI_HOST );
    }
    public boolean isDstAgntUnderBootHost(){
        return ( host_type == DestAgent.TYPE_DST_AGNT );
    }
    public boolean isDstAgntUnderSrcHost(){
        return ( host_type == DestAgent.TYPE_SRC_AGNT );
    }

    public String getHostUUID(){
        return this.host_uuid;
    }
    public void setHostUUID( String val ){
        this.host_uuid = val;
    }

    public int getProtectType(){
        return this.dst_protect_type;
    }
    public void setProtectType( int val ){
        this.dst_protect_type = val;
    }
    public boolean isMTPPProtect(){
        return ( dst_protect_type == BootHost.PROTECT_TYPE_MTPP );
    }
    public boolean isCMDPProtect(){
        return ( dst_protect_type == BootHost.PROTECT_TYPE_CMDP );
    }

    public void setIsCluster( boolean val ){
        this.isCluster = val;
    }

    public boolean isCluster(){
        return this.isCluster;
    }

    public void setClusterID( int val ){
        this.cluster_id = val;
    }

    public int getClusterID(){
        return this.cluster_id;
    }
    
    @Override public String getComment(){
        return this.dst_agent_id+"";
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
        return ResourceCenter.ICON_NODE_16;
    }
    public String toTableString(){
        return this.dst_agent_id+"";
    }
    
    //** TreeRevealable的实现**/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_NODE_16;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_NODE_16;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return this.dst_agent_ip;
    }
    public String toTipString(){
        return dst_agent_ip+" [ "+this.dst_agent_mac +" ]";
    }

    public void setBootMode( int mode ){
        boot_mode = mode;
    }
    public int getBootMode(){
        return boot_mode;
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

    public String getMachine(){
        return this.machine;
    }
    public void setMachine( String val ){
        this.machine = val;
    }
    public boolean is64BitOS(){
        return ( machine.indexOf("_64") >= 0 );
    }
}
