/*
 * VolumeMap.java
 *
 * Created on 2006/12/22, AM 10:34
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.MenuAndBtnCenterForMainUi;
import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.BasicUIObject;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class VolumeMap extends BasicUIObject implements Comparable {
    public final static String VOLMAP_RECFLAG ="Record:";
    public final static String VOLMAP_NAME ="vol_name";
    public final static String VOLMAP_CLNT_ID ="vol_clntid";
    public final static String VOLMAP_DISK_LABEL = "vol_disklabel";
    public final static String VOLMAP_TARGET_ID = "vol_target_id";
    public final static String VOLMAP_MAX_SNAP = "vol_max_snap";
    public final static String VOLMAP_DESC = "vol_desc";
    public final static String VOLMAP_MGID ="vol_mgid";
    public final static String VOLMAP_ROOTID="vol_rootid";
    public final static String VOLMAP_UUID="vol_uuid";
    public final static String VOLMAP_CUR_LOCALID="vol_current_localid";
    public final static String VOLMAP_INFO="vol_info";
    public final static String VOLMAP_CAP="vol_capacity";
    public final static String VOLMAP_VIEW_TID="vol_view_targetid";
    public final static String VOLMAP_CRSNAP_SERVICES = "crsnap_services";
    public final static String VOLMAP_CHANGEVER_SERVICES = "changever_services";
    public final static String VOLMAP_Database_Instances = "database_instances";
    public final static String VOLMAP_Others ="others";
    public final static String VOLMAP_GroupInfo ="groupinfo";
    public final static String VOLMAP_GroupInfoDetail = "groupinfodetail";
    public final static String VOLMAP_GroupName = "groupname";
    public final static String VOLMAP_SetVersion = "setversion";
    public final static String VOLMAP_LAST_SEL_VER="vol_last_sel_boot_version";
    public final static String VOLMAP_LAST_SEL_VER_INFO="vol_last_sel_boot_info";

    public final static String VOLMAP_LAST_SEl_BOOT_DISK_TYPE = "last_sel_boot_disk_type";
    public final static String VOLMAP_LAST_GOOD_BOOT_DISK_TYPE ="last_good_boot_disk_type";
    public final static String VOLMAP_LAST_GOOD_BOOT_INFO ="vol_last_good_boot_info";
    public final static String VOLMAP_ROLLBACK_ORGI_VOLNAME = "rollback_orgi_volname";
    public final static String VOLMAP_ROLLBACK_ORGI_UWS = "rollback_orgi_uws";

    public final static String VOLMAP_VOL_LUN = "vol_lun";
    public final static String VOLMAP_VOL_PERMIT = "vol_permit";

    public final static String VOLMAP_SWITCH_LAST_SEL_VER = "vol_switch_last_sel_version";
    public final static String VOLMAP_SWITCH_LAST_SEL_INFO ="vol_switch_last_sel_info";
    public final static String VOLMAP_SWITCH_LAST_SEL_DISK_TYPE = "vol_switch_last_sel_disk_type";
    public final static String VOLMAP_SWITCH_LAST_GOOD_VER = "vol_switch_last_good_version";
    public final static String VOLMAP_SWITCH_LAST_GOOD_INFO ="vol_switch_last_good_info";
    public final static String VOLMAP_SWITCH_LAST_GOOD_DISK_TYPE = "vol_switch_last_good_disk_type";
    public final static String VOLMAP_PROTECT_TYPE = "vol_protect_type";
    public final static String VOLMAP_CLUSTER_ID = "vol_cluster_id";
    public final static String VOLMAP_OPT = "vol_opt";

    public final static String DB_TYPE_NONE  = SanBootView.res.getString("common.nodb");
    public final static String DB_TYPE_ORA   = "Oracle";
    public final static String DB_TYPE_SYB   = "Sybase";
    public final static String DB_TYPE_MSSQL = "MS SQL Server";
    
    public final static int CMDP_DB_TYPE_NONE   = 0;
    public final static int CMDP_DB_TYPE_ORACLE = 1;
    public final static int CMDP_DB_TYPE_MSSQL  = 2;
    public final static int CMDP_DB_TYPE_SYBASE = 3;
    
    public final static String TYPE_VG  = "VG";
    public final static String TYPE_TGT = "TGT";

    public final static int DISK_TYPE_VOLUME = 0;
    public final static int DISK_TYPE_CLONE_DISK = 1;

    public final static int BIT_WORK_STATE = 0x10;
    public final static int BIT_DBTYPE_ORA = 0x20;
    public final static int BIT_DBTYPE_SQL = 0x40;
    public final static int BIT_DBTYPE_SYB = 0x60;

    public final static int BIT_AUTO_ASYNC_AFT_CRT_SNAP = 0x800;
    public final static int VOL_OPT_BIT_SHARED_DISK = 0x1;
    
    private String vol_name = ""; // 32
    private int vol_clntid;
    private String vol_disklabel = ""; //32
    private int vol_target_id; 
    
    // 当VolumeMap表示VG时,vol_max_snap表示快照空间的大小,为target大小的百分比,缺省为10%
    private int vol_max_snap = 0;
    
    private String vol_desc = ""; // 255;
    private int vol_mgid = -1;
    private int vol_rootid;
    private String vol_uuid;
    private int vol_current_localid;
    
    // 由于MTPP没有使用vol_info，所以该字段被用来区分卷保护的类型
    // 在cmdp中，vol_info被按位使用了，所以一定要用陈连武的接口命令进行解释
    // 1) db type由vol_info解释,vol_info的意义如下：
/*
    	"issystem"  0
        "isdisk"  1
        "iscp"  2
        "cptype"  3
        "workstate"  4
        =================
        "db0"   5
        "db1"  6
	第5和6位联合起来表示数据库的类型：
        6bit  5bit
        0      0   ： 无数据库
	0      1   ： Oracle
	1      0   :  ms sql
	1      1   :  sybase
        =================
        "netstart0"  8
        "netstart1"  9
        "localstart0"  10
*/
    private String vol_info = "";
    private int db_type = 0;
    
    private long vol_capacity;
    
    //该字段表示“最后一次成功启动所用版本”
    //-1: 这个volumemap没有被选择用来网络启动
    //>0: 这个volumemap被用来网络启动
    //=0: Linux: 这个volumemap是swap   windows: 这个volumemap没有被选择用来网络启动
    private int vol_view_targetid = 0;

    // 存放网启成功版本对应的crt时间
    private String vol_last_good_boot_info = ""; // 是一个表示时间的字符串

    // (rollback_orgi_volname,rollback_orgi_uws)“二元对”表示一个rollback回来的卷,rollback_orgi_volname
    // 记录了该卷的原始名字,rollback_orgi_uws记录了对应的uws psn;而vol_name则是一个随机数，这样才能避免
    // VolMap结构中vol_name出现重复现象
    private String rollback_orgi_volname = "";   // 256 ( 以前对应VolMap结构中的 crsnap_services )
    private String rollback_orgi_uws = "";       // 256 ( 以前对应VolMap结构中的 changever_services )

    private String changever_services = "";

    private String database_instances = ""; //
    private String others = "";
    private String groupinfo = "";
    private String groupinfodetail = "";
    private String groupname = "";
    private String setversion = "";

    // 该字段表示“最后一次用户所选版本”，
    // 这些版本也许跟“最后一次成功启动所用版本”一致，
    // 也许还没有进行过网络启动，只是记录用户的选择
    private int vol_last_sel_boot_version = 0;    // 是一个target id

    // 该字段表明“最后一次用户所选版本”对应的创建时间
    private String vol_last_sel_boot_info="";  // 是一个表示时间的字符串，比如20101011142117

    // 当last_sel_boot_info对应的是一个盘（disk）时，由该值指示具体的disk type
    // 0: 分配给主机的原卷     1: 一个克隆盘
    // -1: 表示该字段没有意义
    private int  last_sel_boot_disk_type = -1;    // ( 以前对应VolMap结构中的vol_lun )

    // 当last_good_boot_info(vol_view_targetid)对应的是一个盘（disk）时，由该值指示具体的disk type
    // 0: 分配给主机的原卷     1: 一个克隆盘
    // -1: 表示该字段没有意义
    private int  last_good_boot_disk_type = -1;   // （以前对应VolMap结构中的vol_permit ）
    
    // 以下6个字段表示“不做网启，只做数据切换”时的版本选择信息
    private int switch_last_sel_version = 0;    // 是一个target id
    private String switch_last_sel_info = "";   // 是一个表示时间的字符串，比如20101011142117
    private int switch_last_sel_disk_type = -1;
    private int switch_last_good_verison = 0;   // 是一个target id
    private String switch_last_good_info = "";  // 是一个表示时间的字符串，比如20101011142117
    private int switch_last_good_disk_type = -1;

    // 保护的类型; 1: mtpp  2:cmdp  0:未知（当解析getBootHost命令的clnt_protect_type出错时，就赋值为0）
    private int vol_protect_type = BootHost.PROTECT_TYPE_CMDP; // 缺省为cmdp，为了兼容前面的版本（2011.4.19）

    private int vol_cluster_id = 0;
    private int vol_opt = 0;
    private String vol_cluster_identity = "";
    
    /**
     * Creates a new instance of VolumeMap 
     */
    public VolumeMap() {
        super( ResourceCenter.TYPE_VOLUME_INDEX );
    }

    public VolumeMap( String name,String mp ){
        super( ResourceCenter.TYPE_VOLUME_INDEX );

        this.vol_name = name;
        this.vol_disklabel = mp;
    }

    public VolumeMap( String name,String mp,boolean isLocalDisk ){
        super( ResourceCenter.TYPE_VOLUME_INDEX );

        this.vol_name = name;
        this.vol_disklabel = mp;
        if( isLocalDisk ){
            this.clearSharedDiskBit();
        }else{
            this.setSharedDiskBit();
        }
    }

    public VolumeMap( String mp ){
        this("", -1, mp, -1, 0, "",-1,BootHost.PROTECT_TYPE_CMDP );
    }
    
    public VolumeMap( String mp,int ptype ){
        this("", -1, mp, -1, 0, "",-1,ptype );
    }

    public VolumeMap( String name,int clntid,String mp,int rootid,String orgi_volname,String uws_psn ){
        this( name,clntid,mp,0,32,"",rootid,orgi_volname,uws_psn );
    }
    
    public VolumeMap( String name,int clntid,String mp,String vol_desc,int rootid,String orgi_volname,String uws_psn ){
        this( name,clntid,mp,0,32,vol_desc,rootid,orgi_volname,uws_psn );
    }
     
    public VolumeMap(
        String _vol_name,
        int _vol_clntid,
        String _vol_disklabel,
        int _vol_target_id,
        int _vol_max_snap,
        String _vol_desc,
        int _root_id,
        String _orgi_vol_name,
        String _uws_psn
    ){
        super( ResourceCenter.TYPE_VOLUME_INDEX );
        
        vol_name = _vol_name;
        vol_clntid = _vol_clntid;
        vol_disklabel = _vol_disklabel;
        vol_target_id = _vol_target_id;
        vol_max_snap = _vol_max_snap;
        vol_desc = _vol_desc;
        vol_rootid = _root_id;
        rollback_orgi_volname = _orgi_vol_name;
        rollback_orgi_uws = _uws_psn;
    }
    
    public VolumeMap(
        String _vol_name,
        int _vol_clntid,
        String _vol_disklabel,
        int _vol_target_id,
        int _vol_max_snap,
        String _vol_desc,
        int _root_id,
        int _vol_protect_type
    ){
        super( ResourceCenter.TYPE_VOLUME_INDEX );
        
        vol_name = _vol_name;
        vol_clntid = _vol_clntid;
        vol_disklabel = _vol_disklabel;
        vol_target_id = _vol_target_id;
        vol_max_snap = _vol_max_snap;
        vol_desc = _vol_desc;
        vol_rootid = _root_id;
        this.vol_protect_type = _vol_protect_type;
    }

       public VolumeMap(
        String _vol_name,
        int _vol_clntid,
        String _vol_disklabel,
        int _vol_target_id,
        int _vol_max_snap,
        String _vol_desc,
        int _root_id,
        int _vol_protect_type,
        int _vol_mgid
    ){
        super( ResourceCenter.TYPE_VOLUME_INDEX );

        vol_name = _vol_name;
        vol_clntid = _vol_clntid;
        vol_disklabel = _vol_disklabel;
        vol_target_id = _vol_target_id;
        vol_max_snap = _vol_max_snap;
        vol_desc = _vol_desc;
        vol_rootid = _root_id;
        this.vol_protect_type = _vol_protect_type;
        vol_mgid = _vol_mgid;
    }
    
    public VolumeMap(
        String _vol_name,
        int _vol_clntid,
        String _vol_disklabel,
        int _vol_target_id,
        int _vol_max_snap,
        String _vol_desc,
        int _vol_mgid,
        int _vol_rootid,
        String _vol_uuid,
        int _vol_current_localid,
        String _vol_info,
        long _vol_capacity,
        int _vol_view_targetid,
        int _vol_last_sel_boot_version,
        String _vol_last_sel_boot_info
    ){
        super( ResourceCenter.TYPE_VOLUME_INDEX );
        
        vol_name = _vol_name;
        vol_clntid = _vol_clntid;
        vol_disklabel = _vol_disklabel;
        vol_target_id = _vol_target_id;
        vol_max_snap = _vol_max_snap;
        vol_desc = _vol_desc;
        vol_mgid = _vol_mgid;
        vol_rootid = _vol_rootid;
        vol_uuid = _vol_uuid;
        vol_current_localid = _vol_current_localid;
        vol_info = _vol_info;
        vol_capacity = _vol_capacity;
        vol_view_targetid = _vol_view_targetid;
        vol_last_sel_boot_version = _vol_last_sel_boot_version;
        vol_last_sel_boot_info = _vol_last_sel_boot_info;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 5 );
            //fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SNAP_TREE ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_LUNMAP) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ONLINE ) );
            if( this.isCMDPProtect() ){
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_SYNC_STATE ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_START_AUTO_CRT_SNAP ) );
//                if(this.getVolDiskLabel().startsWith("/")){
//                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_AMS_REBUILD_MIRROR ) );
//                }

            }
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 5 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_LUNMAP ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ONLINE ) );
            if( this.isCMDPProtect() ){
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_SYNC_STATE ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_START_AUTO_CRT_SNAP ) );
//                if(this.getVolDiskLabel().startsWith("/")){
//                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_AMS_REBUILD_MIRROR ) );
//                }
            }
        }
    }
      
    public String getVolName(){
        //if( rollback_orgi_volname.equals("") ){
        //    return vol_name;
        //}else{
        //    return rollback_orgi_volname;
        //}
        return vol_name;
    }
    
    //public String getRealVolName(){
    //    return vol_name;
    //}
    public void setVolName( String val ){
        vol_name = val;
    }
    
    public int getVolClntID(){
        return vol_clntid;
    }
    public void setVolClntID( int val ){
        vol_clntid = val;
    }
    
    public String getVolDiskLabel(){
        return vol_disklabel;
    }
    public String getLetter(){
        return vol_disklabel.substring( 0,1 );
    }
    public void setVolDiskLabel( String val ){
        vol_disklabel = val;
    }
    // 如果label代表一个文件系统，则调用此函数之前最好在label后面加上“:\\”
    public boolean isSameLetter( String label ){
        if( (vol_disklabel.indexOf(":\\") >= 0) && (label.indexOf(":\\") >= 0) ){
            String header = vol_disklabel.substring( 0,1 );
            return header.toUpperCase().equals( label.substring( 0,1 ).toUpperCase() );
        }else{ // 否则volmap根本就不是一个ntfs文件系统，没有所谓的disk label
            return false;
        }
    }
    public boolean isOsVolMap(){
        if( vol_disklabel != null && !vol_disklabel.equals("") ){
            return vol_disklabel.substring(0,1).toUpperCase().equals("C");
        }else{
            return false;
        }
    }
    
    public int getVolTargetID(){
        return vol_target_id;
    }
    public void setVolTargetID( int val ){
        vol_target_id = val;
    }
    
    public int getMaxSnapNum(){
        return vol_max_snap;
    }
    public void setMaxSnapNum( int val ){
        vol_max_snap = val;
    }
    
    public String getVolDesc(){
        return vol_desc;
    }
    public void setVolDesc( String val ){
        vol_desc = val;
    }
    
    public int getVol_mgid(){
        return vol_mgid;
    }
    public void setVol_mgid( int vol_mgid ){
        this.vol_mgid = vol_mgid;
    }
    private boolean isMgIdValid(){
        return ( this.vol_mgid > 0 );
    }
    
    public int getVol_rootid(){
        return vol_rootid;
    }
    public void setVol_rootid( int vol_rootid ){
        this.vol_rootid = vol_rootid;
    }
    
    public String getVol_uuid(){
        return vol_uuid;
    }
    public void setVol_uuid( String vol_uuid ){
        this.vol_uuid = vol_uuid;
    }
    
    public int getVol_current_localid(){
        return vol_current_localid;
    }
    public void setVol_current_localid( int vol_current_localid ){
        this.vol_current_localid = vol_current_localid;
    }
    
    public String getVol_info(){
        return vol_info;
    }
    public void setVol_info( String vol_info ){
        this.vol_info = vol_info;
    }
    public boolean isProtectedByCMDP(){
        return !vol_info.equals("");
    }
    public boolean isWorkStateASync(){
        try{
            int vol_info_val = Integer.parseInt( vol_info );
            return ( vol_info_val & VolumeMap.BIT_WORK_STATE ) != 0;
        }catch( Exception ex ){
            return false;
        }
    }
    public void setWorkStateAsync( boolean isSync ){
        if( isSync ){
            try{
                int vol_info_val = Integer.parseInt( vol_info );
                vol_info_val = ( vol_info_val & ~VolumeMap.BIT_WORK_STATE );
                vol_info = vol_info_val+"";
            }catch( Exception ex ){
            }
        }else{
            try{
                int vol_info_val = Integer.parseInt( vol_info );
                vol_info_val = ( vol_info_val | VolumeMap.BIT_WORK_STATE );
                vol_info = vol_info_val+"";
            }catch( Exception ex ){
            }
        }
    }
    
    public int parseDBType(){
        try{
            int vol_info_val = Integer.parseInt( vol_info );
SanBootView.log.debug( getClass().getName(),"vol_info: " + vol_info_val +" for " + this.getVolName() );
            if( ( vol_info_val & VolumeMap.BIT_DBTYPE_SYB )  == VolumeMap.BIT_DBTYPE_SYB ){
                return VolumeMap.CMDP_DB_TYPE_SYBASE;
            }else if( ( vol_info_val & VolumeMap.BIT_DBTYPE_SQL ) == VolumeMap.BIT_DBTYPE_SQL ){
                return VolumeMap.CMDP_DB_TYPE_MSSQL;
            }else if( ( vol_info_val & VolumeMap.BIT_DBTYPE_ORA ) == VolumeMap.BIT_DBTYPE_ORA ){
                return VolumeMap.CMDP_DB_TYPE_ORACLE;
            }else{
                return VolumeMap.CMDP_DB_TYPE_NONE;
            }
        }catch( Exception ex ){
            return VolumeMap.CMDP_DB_TYPE_NONE;
        }
    }

    public boolean isAutoAsyncAftCrtSnap(){
        try{
            int vol_info_val = Integer.parseInt( vol_info );
            return ( vol_info_val & VolumeMap.BIT_AUTO_ASYNC_AFT_CRT_SNAP ) != 0;
        }catch( Exception ex ){
            return false;
        }
    }
    public void setAutoAsyncAftCrtSnap( boolean val ){
        if( val ){
            try{
                int vol_info_val = Integer.parseInt( vol_info );
                vol_info_val = ( vol_info_val | VolumeMap.BIT_AUTO_ASYNC_AFT_CRT_SNAP );
                vol_info = vol_info_val+"";
            }catch( Exception ex ){
            }
        }else{
            try{
                int vol_info_val = Integer.parseInt( vol_info );
                vol_info_val = ( vol_info_val & ~VolumeMap.BIT_AUTO_ASYNC_AFT_CRT_SNAP );
                vol_info = vol_info_val+"";
            }catch( Exception ex ){
            }
        }
    }

    public long getVol_capacity(){
        return vol_capacity;
    }
    public void setVol_capacity( long vol_capacity ){
        this.vol_capacity = vol_capacity;
    }
    
    public int getVol_view_targetid(){
        return vol_view_targetid; 
    }
    public void setVol_view_targetid( int vol_view_targetid ){
        this.vol_view_targetid = vol_view_targetid;
    }
    
    public String getLastGoodBootInfo(){
        return this.vol_last_good_boot_info;
    }
    public void setLastGoodBootInfo( String val ){
        this.vol_last_good_boot_info = val;
    }

    public String getRollbackOrgiVolname(){
        return rollback_orgi_volname;
    }
    public void setRollbackOrgiVolname( String val ){
        rollback_orgi_volname = val;
    }
    
    public String getRollbackOrgiUws(){
        return rollback_orgi_uws;
    }
    public void setRollbackOrgiUws( String val ){
        rollback_orgi_uws = val;
    }

    public String[] getServices(){
        return getServices( this.changever_services );
    }
    public static String[] getServices( String services ){
        return Pattern.compile(";").split( services,-1 );
    }
    public String getChangeVerService(){
        return this.changever_services;
    }
    public void setChangeVerService(String val ){
        this.changever_services = val;
    }
    public boolean isContainThisService( String service ){
        String[] list = this.getServices();
        for( int i=0; i<list.length; i++ ){
            if( list[i].equals("") ) continue;
            
            if( list[i].equals( service ) ){
                return true;
            }
        }
        return false;
    }
    public void appendService(String serv_name){
        this.setChangeVerService( this.getChangeVerService() +serv_name+";" );
    }
    public int getVol_last_sel_boot_version(){
        return this.vol_last_sel_boot_version;
    }
    public void setVol_last_sel_boot_version( int aversion ){
        this.vol_last_sel_boot_version = aversion;
    }
    
    public String getVol_last_sel_boot_info(){
        return vol_last_sel_boot_info;
    }
    public void setVol_last_sel_boot_info( String ainfo ){
        vol_last_sel_boot_info = ainfo;
    }

    /**
     * @return the Last_sel_boot_disk_type
     */
    public int getLast_sel_boot_disk_type() {
        return last_sel_boot_disk_type;
    }
    public void setLast_sel_boot_disk_type( int val ){
        this.last_sel_boot_disk_type = val;
    }
    public boolean isVolumeForLastSelBoot(){
        return ( this.last_sel_boot_disk_type == VolumeMap.DISK_TYPE_VOLUME );
    }
    public boolean isCloneDiskForLastSelBoot(){
        return ( this.last_sel_boot_disk_type == VolumeMap.DISK_TYPE_CLONE_DISK );
    }

    /**
     * @return the last_good_boot_disk_type
     */
    public int getLast_good_boot_disk_type() {
        return last_good_boot_disk_type;
    }
    public void setLast_goog_boot_disk_type( int val ){
        this.last_good_boot_disk_type = val;
    }
    public boolean isVolumeForLastGoodBoot(){
        return ( this.last_good_boot_disk_type == VolumeMap.DISK_TYPE_VOLUME );
    }
    public boolean isCloneDiskForLastGoodBoot(){
        return ( this.last_good_boot_disk_type == VolumeMap.DISK_TYPE_CLONE_DISK );
    }

    // 下列方法只在非windows主机上使用，比如Linux
    public boolean isVG(){
        return vol_desc.equals( VolumeMap.TYPE_VG );
    }
    public boolean isTGT(){
        return vol_desc.equals( VolumeMap.TYPE_TGT );
    }
    public boolean isLV(){
        return !isVG() && !isTGT();
    }
    
    @Override public String toString(){
        return getVolName();
    }
    
    @Override public String getComment(){
        return vol_target_id+"";
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
        return ResourceCenter.ICON_VOL;
    }
    public String toTableString(){
        if( this.vol_cluster_identity.equals("") ){
            return vol_disklabel;
        }else{
            return vol_disklabel+" [ " + this.vol_cluster_identity +" ]";
        }
    }
    
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_VOL;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_VOL;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        if( this.vol_cluster_identity.equals("") ){
            return vol_disklabel;
        }else{
            return vol_disklabel+" [ " + this.vol_cluster_identity +" ]";
        }
    }
    public String toTipString(){
        // 对于cmdp的卷来说，vol_max_snap的值不对；以后再考虑这个问题（2010.11.8）
        if( this.isMgIdValid() ){
            return vol_disklabel+" [ "+this.vol_target_id+" | "+ this.vol_rootid +" | " + this.vol_mgid+" | " + this.vol_max_snap+" ]";
        }else{
            return vol_disklabel+" [ "+this.vol_target_id+" | "+ this.vol_rootid +" | "+this.vol_max_snap+" ]";
        }
    }

    /**
     * @return the database_instances
     */
    public String getDatabase_instances() {
        return database_instances;
    }
    public String[] getInstanceList(){
        return getInstanceList( database_instances );
    }
    public static String[] getInstanceList( String database_instances ){
        String[] lines = Pattern.compile("\\s+").split( database_instances,-1 );
        return lines;
    }
    
    /**
     * @param database_instances the database_instances to set
     */
    public void setDatabase_instances(String database_instances) {
        this.database_instances = database_instances;
    }

    /**
     * @return the others
     */
    public String getOthers() {
        return others;
    }

    /**
     * @param others the others to set
     */
    public void setOthers(String others) {
        this.others = others;
    }

    /**
     * @return the groupinfo
     */
    public String getGroupinfo() {
        return groupinfo;
    }

    /**
     * @param groupinfo the groupinfo to set
     */
    public void setGroupinfo(String groupinfo) {
        this.groupinfo = groupinfo;
    }

    /**
     * @return the groupinfodetail
     */
    public String getGroupinfodetail() {
        return groupinfodetail;
    }

    /**
     * @param groupinfodetail the groupinfodetail to set
     */
    public void setGroupinfodetail(String groupinfodetail) {
        this.groupinfodetail = groupinfodetail;
    }

    /**
     * @return the groupname
     */
    public String getGroupname() {
        return groupname;
    }

    public boolean isBelongDG(){
        return !groupname.equals("");
    }

    /**
     * @param groupname the groupname to set
     */
    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    /**
     * @return the setversion
     */
    public String getSetversion() {
        return setversion;
    }

    /**
     * @param setversion the setversion to set
     */
    public void setSetversion(String setversion) {
        this.setversion = setversion;
    }

    public int getDBType(){
        return this.db_type;
    }
    public void setDBType( int type ){
        this.db_type = type;
    }
    
    public static int getDbType( String type ){
        int ret;

        if( type.equals( VolumeMap.DB_TYPE_NONE ) ){
            ret = VolumeMap.CMDP_DB_TYPE_NONE;
        }else if( type.equals( VolumeMap.DB_TYPE_ORA ) ){
            ret = VolumeMap.CMDP_DB_TYPE_ORACLE;
        }else if( type.equals( VolumeMap.DB_TYPE_MSSQL ) ){
            ret = VolumeMap.CMDP_DB_TYPE_MSSQL;
        }else if( type.equals( VolumeMap.DB_TYPE_SYB ) ){
            ret = VolumeMap.CMDP_DB_TYPE_SYBASE;
        }else{
            ret = VolumeMap.CMDP_DB_TYPE_NONE;
        }
        return ret;
    }

    public static String getDbString( int type ){
        String ret = "";

        switch ( type ){
            case VolumeMap.CMDP_DB_TYPE_NONE:
                ret = VolumeMap.DB_TYPE_NONE;
                break;
            case VolumeMap.CMDP_DB_TYPE_ORACLE:
                ret = VolumeMap.DB_TYPE_ORA;
                break;
            case VolumeMap.CMDP_DB_TYPE_MSSQL:
                ret = VolumeMap.DB_TYPE_MSSQL;
                break;
            case VolumeMap.CMDP_DB_TYPE_SYBASE:
                ret = VolumeMap.DB_TYPE_SYB;
                break;
            default:
                ret = VolumeMap.DB_TYPE_NONE;
                break;
        }

        return ret;
    }

    public String getSortedServices(){
        return VolumeMap.getServices( getServices() );
    }
    
    public static String getServices( String[] services ){
        StringBuffer buf = new StringBuffer();
        boolean isFirst = true;

        // 对services进行排序
        Arrays.sort( services );

        for( int i=0; i<services.length; i++ ){
            String letter = services[i].toUpperCase();
            if( isFirst ){
                buf.append( letter );
                isFirst = false;
            }else{
                buf.append( ";" + letter );
            }
        }
        return buf.toString();
    }

    public String getSortedDBInstance(){
        return VolumeMap.getDBInstance( getInstanceList() );
    }

    public static String getDBInstance( String[] db_instances ){
        StringBuffer buf = new StringBuffer();
        boolean isFirst = true;

        // 对db instances进行排序
        Arrays.sort( db_instances );

        for( int i=0; i<db_instances.length; i++ ){
            String letter = db_instances[i].toUpperCase();
            if( isFirst ){
                buf.append( letter );
                isFirst = false;
            }else{
                buf.append( "^" + letter );
            }
        }
        return buf.toString();
    }

    public int compareTo( Object volMapObj ){
        VolumeMap volMap = (VolumeMap)volMapObj;
        return this.vol_disklabel.substring(0,1).toUpperCase().compareTo( volMap.getVolDiskLabel().substring(0,1).toUpperCase() );
    }

    /**
     * @return the switch_last_sel_version
     */
    public int getSwitch_last_sel_version() {
        return switch_last_sel_version;
    }

    /**
     * @param switch_last_sel_version the switch_last_sel_version to set
     */
    public void setSwitch_last_sel_version(int switch_last_sel_version) {
        this.switch_last_sel_version = switch_last_sel_version;
    }

    /**
     * @return the switch_last_sel_info
     */
    public String getSwitch_last_sel_info() {
        return switch_last_sel_info;
    }

    /**
     * @param switch_last_sel_info the switch_last_sel_info to set
     */
    public void setSwitch_last_sel_info(String switch_last_sel_info) {
        this.switch_last_sel_info = switch_last_sel_info;
    }

    /**
     * @return the switch_last_sel_disk_type
     */
    public int getSwitch_last_sel_disk_type() {
        return switch_last_sel_disk_type;
    }

    /**
     * @param switch_last_sel_disk_type the switch_last_sel_disk_type to set
     */
    public void setSwitch_last_sel_disk_type(int switch_last_sel_disk_type) {
        this.switch_last_sel_disk_type = switch_last_sel_disk_type;
    }

    /**
     * @return the switch_last_good_verison
     */
    public int getSwitch_last_good_verison() {
        return switch_last_good_verison;
    }

    /**
     * @param switch_last_good_verison the switch_last_good_verison to set
     */
    public void setSwitch_last_good_verison(int switch_last_good_verison) {
        this.switch_last_good_verison = switch_last_good_verison;
    }

    /**
     * @return the switch_last_good_info
     */
    public String getSwitch_last_good_info() {
        return switch_last_good_info;
    }

    /**
     * @param switch_last_good_info the switch_last_good_info to set
     */
    public void setSwitch_last_good_info(String switch_last_good_info) {
        this.switch_last_good_info = switch_last_good_info;
    }

    /**
     * @return the switch_last_good_disk_type
     */
    public int getSwitch_last_good_disk_type() {
        return switch_last_good_disk_type;
    }

    /**
     * @param switch_last_good_disk_type the switch_last_good_disk_type to set
     */
    public void setSwitch_last_good_disk_type(int switch_last_good_disk_type) {
        this.switch_last_good_disk_type = switch_last_good_disk_type;
    }

    /**
     * @return the vol_protect_type
     */
    public int getVol_protect_type() {
        return vol_protect_type;
    }

    /**
     * @param vol_protect_type the vol_protect_type to set
     */
    public void setVol_protect_type(int vol_protect_type) {
        this.vol_protect_type = vol_protect_type;
    }

    public boolean isMtppProtect(){
        return ( this.vol_protect_type == BootHost.PROTECT_TYPE_MTPP );
    }

    public boolean isCMDPProtect(){
        return ( this.vol_protect_type == BootHost.PROTECT_TYPE_CMDP );
    }

    public boolean isUnknownProtect(){
        return !this.isMtppProtect() && !this.isCMDPProtect();
    }
    
    public String getProtectString(){
        if( this.isMtppProtect() ){
            return SanBootView.res.getString("common.logical");
        }else if( this.isCMDPProtect() ){
            return SanBootView.res.getString("common.physical");
        }else{
            return SanBootView.res.getString("common.unknown");
        }
    }
    
    /**
     * @return the vol_opt
     */
    public int getVol_opt() {
        return vol_opt;
    }

    /**
     * @param vol_opt the vol_opt to set
     */
    public void setVol_opt(int vol_opt) {
        this.vol_opt = vol_opt;
    }
    
    public void setVol_Cluster_Identity( String val ){
        this.vol_cluster_identity = val;
    }
    
    public boolean isClusterSharedDisk(){
        return ( ( vol_opt & VolumeMap.VOL_OPT_BIT_SHARED_DISK ) != 0 );
    }

    public void setSharedDiskBit(){
        vol_opt = vol_opt|VolumeMap.VOL_OPT_BIT_SHARED_DISK;
    }
    
    public void clearSharedDiskBit(){
        vol_opt = vol_opt&~VolumeMap.VOL_OPT_BIT_SHARED_DISK;
    }

    /**
     * @return the vol_cluster_id
     */
    public int getVol_cluster_id() {
        return vol_cluster_id;
    }

    /**
     * @param vol_cluster_id the vol_cluster_id to set
     */
    public void setVol_cluster_id(int vol_cluster_id) {
        this.vol_cluster_id = vol_cluster_id;
    }

    public VolumeMap cloneMe(){
        VolumeMap newOne = new VolumeMap();
        newOne.setVolName( vol_name );
        newOne.setVolClntID( vol_clntid );
        newOne.setVolDiskLabel( vol_disklabel );
        newOne.setVolTargetID( vol_target_id );
        newOne.setMaxSnapNum( vol_max_snap );
        newOne.setVolDesc( vol_desc );
        newOne.setVol_mgid( vol_mgid );
        newOne.setVol_rootid( vol_rootid );
        newOne.setVol_uuid( vol_uuid );
        newOne.setVol_current_localid( vol_current_localid );
        newOne.setVol_info( vol_info );
        newOne.setVol_capacity( vol_capacity );
        newOne.setVol_view_targetid( vol_view_targetid );
        newOne.setDBType( db_type );
        newOne.setLastGoodBootInfo( vol_last_good_boot_info );
        newOne.setRollbackOrgiVolname( rollback_orgi_volname );
        newOne.setRollbackOrgiUws( rollback_orgi_uws );
        newOne.setChangeVerService( changever_services );
        newOne.setDatabase_instances( database_instances );
        newOne.setOthers( others );
        newOne.setGroupinfo( groupinfo );
        newOne.setGroupinfodetail( groupinfodetail );
        newOne.setGroupname( groupname );
        newOne.setSetversion( setversion );

        newOne.setVol_last_sel_boot_version( vol_last_sel_boot_version  );
        newOne.setVol_last_sel_boot_info( vol_last_sel_boot_info );
        newOne.setLast_sel_boot_disk_type( last_sel_boot_disk_type );
        newOne.setLast_goog_boot_disk_type( last_good_boot_disk_type );

        newOne.setSwitch_last_good_verison( switch_last_good_verison );
        newOne.setSwitch_last_good_info( switch_last_good_info );
        newOne.setSwitch_last_good_disk_type( switch_last_good_disk_type );
        newOne.setSwitch_last_sel_disk_type( switch_last_sel_disk_type );
        newOne.setSwitch_last_sel_version( switch_last_sel_version );
        newOne.setSwitch_last_sel_info (switch_last_sel_info );
        
        newOne.setVol_cluster_id( vol_cluster_id );
        newOne.setVol_opt( vol_opt );
        newOne.setVol_Cluster_Identity( vol_cluster_identity );
        return newOne;
    }
}
