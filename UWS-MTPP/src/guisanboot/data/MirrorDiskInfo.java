/*
 * MirrorDiskInfo.java
 *
 * Created on 2008/6/18, 16:02 AM
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
import mylib.UI.BrowserTreeNode;
import guisanboot.ui.ChiefRemoteFreeVolume;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.entity.ChiefLocalUnLimitedIncMirrorVolList;

/**
 *
 * @author zjj
 */
public class MirrorDiskInfo extends BasicUIObject {
    public final static String MDI_FLAG ="Record:";
    public final static String MDI_SNAP_ROOTID = "snap_rootid";
    public final static String MDI_SRCAGNT_ID = "src_agnt_id";
    public final static String MDI_SRC_SNAP_ROOTID = "src_snap_rootid";
    public final static String MDI_SRC_VOL_NAME = "src_snap_name";
    public final static String MDI_SRC_DISK_LABEL  = "src_agent_mp";
    public final static String MDI_SRCAGNT_FLAG = "src_agent_info";
    public final static String MDI_SRC_GRPINFO = "src_groupinfo";
    public final static String MDI_SRC_GRPDETAIL = "src_groupinfodetail";
    public final static String MDI_SRC_VOL_INFO = "src_vol_info";
    public final static String MDI_SRC_VOL_PROTECT_TYPE = "src_vol_protect_type";
    
    private int snap_rootid;     // 该卷在目的端uws上的 rootid
    private int src_agnt_id;     // 远程主机的id
    private int src_snap_rootid;  // 该卷在源端UWS上的 rootid
    private String src_snap_name; // 该卷在源端UWS上的卷名 ( length: 255 )
    private String src_agent_mp;  // 该卷在源端UWS上的disklabel(盘符或mountpoint) ( lentth: 32 )
    private int src_agent_info;   // 该卷在源端UWS上的一些特征，比如是否为启动盘等

    // 当MirrorDiskInfo属于 rollbacked srcagnt时，则src_groupinfo表示对应的Target ID.
    private int src_groupinfo = -1 ;

    // 表示该远程镜像卷的类型，类型取值跟MirrorJob.mj_job_type一样。
    // 有6种取值，缺省为2（普通远程镜像）
    private int src_vol_info = 2;
    
    // UWS-MTPP 不需要的域，CMDP需要这些东东
    private String crsnap_services = ""; // 256
    private String changever_services = ""; // 256
    private String database_instances = ""; // 256    
    private int src_vol_capacity = 0; 
    private String others = ""; // 256    
    private String src_groupinfodetail = ""; //256
    private String src_groupname = ""; // 32;
    private String vol_lun="";
    private String vol_permit="";
    
    private int src_vol_protect_type = BootHost.PROTECT_TYPE_MTPP;

    /** Creates a new instance of MirrorDiskInfo */
    public MirrorDiskInfo() {
        // type is TYPE_MIRROR_VOL or TYPE_UNLIMITED_INC_MIRROR_VOL
        super( ResourceCenter.TYPE_MIRROR_VOL );
    }
    
    public MirrorDiskInfo(
        int snap_rootid,
        int src_agnt_id,
        int src_snap_rootid,
        String src_snap_name,
        String src_agent_mp,
        int src_agent_info,
        int src_vol_info,
        int ptype
    ) {
        super( ResourceCenter.TYPE_MIRROR_VOL );
        
        this.snap_rootid=snap_rootid;
        this.src_agnt_id = src_agnt_id;
        this.src_snap_rootid = src_snap_rootid;
        this.src_snap_name = src_snap_name;
        this.src_agent_mp = src_agent_mp;
        this.src_agent_info = src_agent_info;
        this.src_vol_info = src_vol_info;
        this.src_vol_protect_type = ptype;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            BrowserTreeNode fNode = this.getFatherNode();
            if( fNode == null ) {
                fsForTree = new ArrayList<Integer>( 0 );
                return;
            }
            if( fNode.getUserObject() instanceof ChiefRemoteFreeVolume ){
                fsForTree = new ArrayList<Integer>( 1 );
                if( !this.isUIM_Vol() && !this.isRemoteCjVol() ){
                    fsForTree.add(  new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK_VOL ) );
                }else{
                    if( this.isRemoteCjVol() ){
                        fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_UI_MIRROR_VOL ) );
                    }
                    if( this.isUIM_Vol() ){
                        fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_UI_MIRROR_VOL ) );
                    }
                }
            }else if( fNode.getUserObject() instanceof ChiefLocalUnLimitedIncMirrorVolList ){
                fsForTree = new ArrayList<Integer>( 2 );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_UI_MIRROR_VOL ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_QUEYR_UISNAP ) );
            }else{
                fsForTree = new ArrayList<Integer>( 1 );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_LUNMAP ) );
            }
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            BrowserTreeNode fNode = this.getFatherNode();
            if( fNode == null ){
                fsForTable = new ArrayList<Integer>( 0 );
                return;
            }
            if( fNode.getUserObject() instanceof ChiefRemoteFreeVolume ){
                fsForTable = new ArrayList<Integer>( 1 );
                if( !this.isUIM_Vol() && !this.isRemoteCjVol() ){
                    fsForTable.add(  new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK_VOL ) );
                }else{
                    if( this.isRemoteCjVol() ){
                        fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_UI_MIRROR_VOL ) );
                    }
                    if( this.isUIM_Vol() ){
                        fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_UI_MIRROR_VOL ) );
                    }
                }
            }else if( fNode.getUserObject() instanceof ChiefLocalUnLimitedIncMirrorVolList ){
                fsForTable = new ArrayList<Integer>( 2 );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_UI_MIRROR_VOL ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_QUEYR_UISNAP ) );
            }else{
                fsForTable = new ArrayList<Integer>( 1 );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_LUNMAP ) );
            }
        }
    }
    
    public int getSnap_rootid() {
        return snap_rootid;
    }
    
    public void setSnap_rootid(int snap_rootid) {
        this.snap_rootid = snap_rootid;
    }
    
    public int getSrc_agnt_id() {
        return src_agnt_id;
    }

    public void setSrc_agnt_id(int src_agnt_id) {
        this.src_agnt_id = src_agnt_id;
    }

    public int getSrc_snap_rootid() {
        return src_snap_rootid;
    }

    public void setSrc_snap_rootid(int src_snap_rootid) {
        this.src_snap_rootid = src_snap_rootid;
    }

    public String getSrc_snap_name() {
        return src_snap_name;
    }

    public void setSrc_snap_name(String src_snap_name) {
        this.src_snap_name = src_snap_name;
    }

    public String getSrc_agent_mp() {
        return src_agent_mp;
    }

    public void setSrc_agent_mp(String src_agent_mp) {
        this.src_agent_mp = src_agent_mp;
    }

    public int getSrc_agent_info() {
        return src_agent_info;
    }

    public void setSrc_agent_info(int src_agent_info) {
        this.src_agent_info = src_agent_info;
    }
    
    @Override public String getComment(){
        return snap_rootid+"";
    }
    
    public int getTargetID(){
        return src_groupinfo;
    }
    public void setTargetID( int val ){
        src_groupinfo = val;
    }

    public int getVolumeType(){
        return this.src_vol_info;
    }
    public void setVolumeType( int val ){
        this.src_vol_info = val;
    }
    public boolean isUIM_Vol( ){
        return ( this.src_vol_info == MirrorJob.MJ_TYPE_REMOTE_TRACK_JOB );
    }
    public boolean isLocalMirrorVol(){
        return ( this.src_vol_info == MirrorJob.MJ_TYPE_LOCAL ) || ( this.src_vol_info == MirrorJob.MJ_TYPE_LOCAL_TRACK_JOB );
    }
    public boolean isRemoteCjVol(){
        return ( this.src_vol_info == MirrorJob.MJ_TYPE_REMOTE_COPY_JOB );
    }
    
    public void setOthers( String val ){
        this.others = val;
    }
    public String getOthers(){
        return others;
    }
    
    // 下列方法只在非windows主机上使用，比如Linux
    public boolean isVG(){
        return others.equals( VolumeMap.TYPE_VG );
    }
    public boolean isTGT(){
        return others.equals( VolumeMap.TYPE_TGT );
    }
    public boolean isLV(){
        return !isVG() && !isTGT();
    }

    @Override public String toString(){
        if( src_agent_mp.equals("") ){
            return this.src_snap_name;
        }else{
            return this.src_agent_mp;
        }
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
        return snap_rootid+"";
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
        if( src_agent_mp.equals("") ){
            return src_snap_name;
        }else{
            return src_agent_mp;
        }
    }
    public String toTipString(){
        return toTreeString()+" | " +this.snap_rootid;
    }

    /**
     * @return the src_vol_protect_type
     */
    public int getSrc_vol_protect_type() {
        return src_vol_protect_type;
    }

    /**
     * @param src_vol_protect_type the src_vol_protect_type to set
     */
    public void setSrc_vol_protect_type(int src_vol_protect_type) {
        this.src_vol_protect_type = src_vol_protect_type;
    }

    public boolean isMTPPProtect(){
        return ( src_vol_protect_type == BootHost.PROTECT_TYPE_MTPP );
    }
    
    public boolean isCMDPProtect(){
        return ( src_vol_protect_type == BootHost.PROTECT_TYPE_CMDP );
    }

    public boolean isUnknownProtectType(){
        return !this.isMTPPProtect() && !this.isCMDPProtect();
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
}
