/*
 * MirrorJob.java
 *
 * Created on 2008/6/5,�PM 5:37
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import javax.swing.Icon;
import javax.swing.JLabel;
import java.util.ArrayList;
import mylib.UI.BasicUIObject;
import guisanboot.MenuAndBtnCenterForMainUi;

/**
 *
 * @author zjj
 */
public class MirrorJob extends BasicUIObject {
    public final static int MJ_TYPE_LOCAL  = 1;
    public final static int MJ_TYPE_REMOTE = 2;
    public final static int MJ_TYPE_LOCAL_TRACK_JOB = 3;
    public final static int MJ_TYPE_REMOTE_TRACK_JOB = 4;
    public final static int MJ_TYPE_LOCAL_COPY_JOB  = 5;
    public final static int MJ_TYPE_REMOTE_COPY_JOB = 6;
    public final static int MJ_TYPE_REMOTE_LOG_JOB = 7;

    public final static String MJ_TYPE_LOCAL_STRING = "common.local";
    public final static String MJ_TYPE_REMOTE_STRING= "common.remote";
    
    public final static int MJ_TRACK_MODE_AFT_CRT_SNAP = 1;
    public final static int MJ_TRACK_MODE_BEF_MELT_SNAP = 2;

    public final static int MJ_TRACK_SRC_TYPE_LOCALDISK = 1;
    public final static int MJ_TRACK_SRC_TYPE_SGDISK = 2;

    public final static int MJ_COPY_SRC_TYPE_LOCALDISK = 1;
    public final static int MJ_COPY_SRC_TYPE_SGDISK = 2;

    public final static int MJ_OPT_CONTINUE = 1;
    public final static int MJ_OPT_BRANCH   = 2;
    public final static int MJ_OPT_ENCRY    = 4;
    public final static int MJ_OPT_ZIP      = 8;
    public final static int MJ_OPT_DEL_VIEW = 16;
    
    public final static int MJ_STATUS_START = 1;
    public final static int MJ_STATUS_STOP  = 2;
    
    public static final String MJ_RECFLAG="Record:";
    public static final String MJ_mj_id ="mj_id";
    public static final String MJ_mj_mg_id ="mj_mg_id";
    public static final String MJ_mj_job_type = "mj_job_type";
    public static final String MJ_mj_job_name = "mj_job_name";
    public static final String MJ_mj_dest_ip="mj_dest_ip";
    public static final String MJ_mj_dest_port = "mj_dest_port";
    public static final String MJ_mj_transfer_option="mj_transfer_option";
    public static final String MJ_mj_dest_pool="mj_dest_pool";
    public static final String MJ_mj_dest_root_id ="mj_dest_root_id";
    public static final String MJ_mj_dest_pool_passwd = "mj_dest_pool_passwd";
    public static final String MJ_mj_done_snap_id ="mj_done_snap_id";
    public static final String MJ_mj_current_rootid ="mj_current_rootid";
    public static final String MJ_mj_current_snap_id="mj_current_snap_id";
    public static final String MJ_mj_current_process="mj_current_process";
    public static final String MJ_mj_job_status="mj_job_status";
    public static final String MJ_mj_job_pid="mj_job_pid";
    public static final String MJ_mj_desc = "mj_desc";
    public static final String MJ_mj_current_block = "mj_current_block";
    public static final String MJ_mj_info = "mj_info";
    public static final String MJ_mj_track_src_rootid="mj_track_src_rootid";
    public static final String MJ_mj_track_mode="mj_track_mode";
    public static final String MJ_mj_track_src_type="mj_track_src_type";
    public static final String MJ_mj_copy_src_rootid="mj_copy_src_rootid";
    public static final String MJ_mj_copy_src_snapid="mj_copy_src_snapid";
    public static final String MJ_mj_copy_src_type ="mj_copy_src_type";
    public static final String MJ_mj_scheduler = "mj_scheduler";
    public static final String MJ_mg_name = "mg_name";
    public static final String MJ_mg_interval_time = "mg_interval_time";
    public static final String MJ_mg_log_max_time  = "mg_log_max_time";
    public static final String MJ_mg_max_snapshot = "mg_max_snapshot";

    private int mj_id;
    private int mj_mg_id=0;
    private int mj_job_type;  //  # 1 local mirror job   2 remote  mirror job
    private String mj_job_name; // 255
    private String mj_dest_ip; // 255
    private int mj_dest_port;
    
    // transport options��only effect on remote mirror job
    // 1 continue transfer option 
    // 2 transfer branch  
    // 4 transfer data after being encrypted by DES 
    // 8 compress flag
    // 16 force to delete views on snapshot
    private int mj_transfer_option = 0;
    
    private int mj_dest_pool;
    private int mj_dest_root_id;
    private String mj_dest_pool_passwd;//32
    private int mj_done_snap_id; // 
    private int mj_current_rootid;//
    private int mj_current_snap_id;// 
    private int mj_current_process;//
    private int mj_job_status;  // # 1 start   2 stop
    private int mj_job_pid;
    private String mj_desc; // 255
    private int mj_current_block;
    private String mj_info="";

    private int mj_track_src_rootid;
    private int mj_track_mode;  //  #1 is after createsnap or sg_crt_snap ,2 before meltsnap or sg_melt_snap                                                                                                                                                                                            is after createsnap or sg_crt_snap ,2 before meltsnap or sg_melt_snap
    private int mj_track_src_type ;     //  #1 is localdisk ,2 is sg disk

    private int mj_copy_src_rootid;
    private int mj_copy_src_snapid;
    private int mj_copy_src_type ;  // #1 is localdisk ,2 is sg disk

    private int mj_scheduler;
    
    private String mg_name;
    private int mg_interval_time;
    private int mg_log_max_time;
    private int mg_max_snapshot;

    /** Creates a new instance of MirrorJob */
    public MirrorJob() {
        super( ResourceCenter.TYPE_MIRROR_JOB );
    }
    
    public MirrorJob(
        int mj_mg_id,
        String mj_job_name,
        String mj_dest_ip,
        int mj_dest_port,
        int mj_transfer_opt,
        int mj_dest_pool,
        String mj_dest_pool_password,        
        String mj_desc
    ){
        this(-1,mj_mg_id,2, mj_job_name,mj_dest_ip,mj_dest_port,mj_transfer_opt,mj_dest_pool,-1,mj_dest_pool_password,-1,-1,-1,-1,2,-1,mj_desc,-1,"");
    }

    public MirrorJob(
        String mj_job_name,
        String mj_dest_ip,
        int mj_dest_port,
        int mj_transfer_opt,
        int mj_dest_pool,
        String mj_dest_pool_password,
        String mj_desc
    ){
        this(-1,mj_job_name,mj_dest_ip,mj_dest_port,mj_transfer_opt,mj_dest_pool,mj_dest_pool_password,mj_desc );
    }

    public MirrorJob(
        int mj_id,
        int mj_mg_id,
        int mj_job_type,
        String mj_job_name,
        String mj_dest_ip,
        int mj_dest_port,
        int mj_transfer_option,
        int mj_dest_pool,
        int mj_dest_root_id,
        String mj_dest_pool_passwd,
        int mj_done_snap_id,
        int mj_current_rootid,
        int mj_current_snap_id, 
        int mj_current_process,
        int mj_job_status,
        int mj_job_pid,
        String mj_desc,
        int mj_current_block,
        String mj_info
    ){
        super( ResourceCenter.TYPE_MIRROR_JOB );
        
        this.mj_id = mj_id;
        this.setMj_mg_id(mj_mg_id);
        this.setMj_job_type(mj_job_type);
        this.setMj_job_name(mj_job_name);
        this.setMj_dest_ip(mj_dest_ip);
        this.setMj_dest_port(mj_dest_port);
        this.setMj_transfer_option(mj_transfer_option);
        this.setMj_dest_pool(mj_dest_pool);
        this.setMj_dest_root_id(mj_dest_root_id);
        this.setMj_dest_pool_passwd(mj_dest_pool_passwd);
        this.setMj_done_snap_id(mj_done_snap_id);
        this.setMj_current_rootid(mj_current_rootid);
        this.setMj_current_snap_id(mj_current_snap_id);
        this.setMj_current_process(mj_current_process);
        this.setMj_job_status(mj_job_status);
        this.setMj_job_pid(mj_job_pid);
        this.setMj_desc(mj_desc);
        this.setMj_current_block(mj_current_block);
        this.mj_info = mj_info;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 4 );
            if( this.isCjType() ){
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_CJ ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_CJ ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_START_CJ ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_QUICK_START_CJ ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_STOP_CJ ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MONITOR_CJ ) );
            }else{
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_MJ ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_MJ ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_START_MJ ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_QUICK_START_MJ ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_STOP_MJ ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MONITOR_MJ ) );
            }
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 4 );
            if( this.isCjType() ){
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_CJ ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_CJ ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_START_CJ ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_QUICK_START_CJ ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_STOP_CJ ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MONITOR_CJ ) );
            }else{
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_MJ ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_MJ ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_START_MJ ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_QUICK_START_MJ ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_STOP_MJ ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MONITOR_MJ ) );
            }
        }
    }
    
    public int getMj_current_block() {
        return mj_current_block;
    }

    public void setMj_current_block(int mj_current_block) {
        this.mj_current_block = mj_current_block;
    }

    public int getMj_id() {
        return mj_id;
    }

    public void setMj_id(int mj_id) {
        this.mj_id = mj_id;
    }

    public int getMj_mg_id() {
        return mj_mg_id;
    }

    public void setMj_mg_id(int mj_mg_id) {
        this.mj_mg_id = mj_mg_id;
    }

    public int getMj_job_type() {
        return mj_job_type;
    }

    public void setMj_job_type(int mj_job_type) {
        this.mj_job_type = mj_job_type;
    }

    public boolean isLocalIncMjType(){
        return ( this.mj_job_type == MirrorJob.MJ_TYPE_LOCAL_TRACK_JOB );
    }
    public boolean isRemoteIncMjType(){
        return ( this.mj_job_type == MirrorJob.MJ_TYPE_REMOTE_TRACK_JOB );
    }
    public boolean isLocalNormalMjType(){
        return ( this.mj_job_type == MirrorJob.MJ_TYPE_LOCAL );
    }
    public boolean isRemoteNormalMjType(){
        return ( this.mj_job_type == MirrorJob.MJ_TYPE_REMOTE );
    }
    public boolean isLocalCjType(){
        return ( this.mj_job_type == MirrorJob.MJ_TYPE_LOCAL_COPY_JOB );
    }
    public boolean isRemoteCjType(){
        return ( this.mj_job_type == MirrorJob.MJ_TYPE_REMOTE_COPY_JOB );
    }
    public boolean isRemoteLjType(){
        return ( this.mj_job_type == MirrorJob.MJ_TYPE_REMOTE_LOG_JOB );
    }
    public boolean isNormalMjType(){
        return ( this.isLocalNormalMjType() || this.isRemoteNormalMjType() );
    }
    public boolean isIncMjType(){
        return (this.isLocalIncMjType() || this.isRemoteIncMjType() );
    }
    public boolean isLocalMjType(){
        return ( this.isLocalNormalMjType() || this.isLocalIncMjType() );
    }
    public boolean isRemoteMjType(){
        return ( this.isRemoteIncMjType() || this.isRemoteNormalMjType() );
    }
    public boolean isCjType(){
        return ( this.isLocalCjType() || this.isRemoteCjType() );
    }
    
    public String getMj_job_name() {
        return mj_job_name;
    }

    public void setMj_job_name(String mj_job_name) {
        this.mj_job_name = mj_job_name;
    }

    public String getMj_dest_ip() {
        return mj_dest_ip;
    }

    public void setMj_dest_ip(String mj_dest_ip) {
        this.mj_dest_ip = mj_dest_ip;
    }

    public int getMj_dest_port() {
        return mj_dest_port;
    }

    public void setMj_dest_port(int mj_dest_port) {
        this.mj_dest_port = mj_dest_port;
    }

    public int getMj_transfer_option() {
        return mj_transfer_option;
    }

    public String getMj_trans_optStr(){
        String optStr="";
        boolean isFirst = true;
        if( ( mj_transfer_option & MirrorJob.MJ_OPT_CONTINUE) !=0  ){
            if( isFirst ){
                optStr = SanBootView.res.getString("EditMirrorJobDialog.checkbox.continue");
                isFirst = false;
            }else{
                optStr +="&"+SanBootView.res.getString("EditMirrorJobDialog.checkbox.continue");
            }
        }else{
            if( isFirst ){
                optStr = SanBootView.res.getString("EditMirrorJobDialog.checkbox.continue1");
                isFirst = false;
            }else{
                optStr +="&"+SanBootView.res.getString("EditMirrorJobDialog.checkbox.continue1");
            }
        }
        
        if( ( mj_transfer_option & MirrorJob.MJ_OPT_ENCRY) != 0 ){
            if( isFirst ){
                optStr = SanBootView.res.getString("EditMirrorJobDialog.checkbox.des");
                isFirst = false;
            }else{
                optStr += "&"+ SanBootView.res.getString("EditMirrorJobDialog.checkbox.des");
            }
        }else{
            if( isFirst ){
                optStr = SanBootView.res.getString("EditMirrorJobDialog.checkbox.des1");
                isFirst = false;
            }else{
                optStr += "&"+ SanBootView.res.getString("EditMirrorJobDialog.checkbox.des1");
            }
        }
        
        if( ( mj_transfer_option & MirrorJob.MJ_OPT_ZIP) != 0 ){
            if( isFirst ){
                optStr = SanBootView.res.getString("EditMirrorJobDialog.checkbox.compress");
                isFirst = false;
            }else{
                optStr += "&"+ SanBootView.res.getString("EditMirrorJobDialog.checkbox.compress");
            }
        }else{
            if( isFirst ){
                optStr = SanBootView.res.getString("EditMirrorJobDialog.checkbox.compress1");
                isFirst = false;
            }else{
                optStr += "&"+ SanBootView.res.getString("EditMirrorJobDialog.checkbox.compress1");
            }
        }
        
        if( ( mj_transfer_option & MirrorJob.MJ_OPT_BRANCH ) != 0 ){
            if( isFirst ){
                optStr = SanBootView.res.getString("EditMirrorJobDialog.checkbox.copybranch");
                isFirst = false;
            }else{
                optStr += "&"+ SanBootView.res.getString("EditMirrorJobDialog.checkbox.copybranch");
            }
        }else{
            if( isFirst ){
                optStr = SanBootView.res.getString("EditMirrorJobDialog.checkbox.copybranch1");
                isFirst = false;
            }else{
                optStr += "&"+ SanBootView.res.getString("EditMirrorJobDialog.checkbox.copybranch1");
            }
        }
        
        if( ( mj_transfer_option & MirrorJob.MJ_OPT_DEL_VIEW ) != 0 ){
            if( isFirst ){
                optStr = SanBootView.res.getString("EditMirrorJobDialog.checkbox.delview");
                isFirst = false;
            }else{
                optStr += "&"+ SanBootView.res.getString("EditMirrorJobDialog.checkbox.delview");
            }
        }else{
            if( isFirst ){
                optStr = SanBootView.res.getString("EditMirrorJobDialog.checkbox.delview1");
                isFirst = false;
            }else{
                optStr += "&"+ SanBootView.res.getString("EditMirrorJobDialog.checkbox.delview1");
            }
        }
        
        return optStr;
    }
    
    public boolean isContinue(){
        return ( mj_transfer_option&MirrorJob.MJ_OPT_CONTINUE ) != 0;
    }
    public boolean isEncrypt(){
        return ( mj_transfer_option&MirrorJob.MJ_OPT_ENCRY ) != 0;
    }
    public boolean isZip(){
        return ( mj_transfer_option&MirrorJob.MJ_OPT_ZIP ) != 0;
    }
    public boolean isBranch(){
        return ( mj_transfer_option&MirrorJob.MJ_OPT_BRANCH ) != 0;
    }
    public boolean isDelView(){
        return ( mj_transfer_option&MirrorJob.MJ_OPT_DEL_VIEW ) != 0;
    }
    
    public void setMj_transfer_option(int mj_transfer_option) {
        this.mj_transfer_option = mj_transfer_option;
    }

    public int getMj_dest_pool() {
        return mj_dest_pool;
    }

    public void setMj_dest_pool(int mj_dest_pool) {
        this.mj_dest_pool = mj_dest_pool;
    }

    public int getMj_dest_root_id() {
        return mj_dest_root_id;
    }

    public void setMj_dest_root_id(int mj_dest_root_id) {
        this.mj_dest_root_id = mj_dest_root_id;
    }

    public String getMj_dest_pool_passwd() {
        return mj_dest_pool_passwd;
    }

    public void setMj_dest_pool_passwd(String mj_dest_pool_passwd) {
        this.mj_dest_pool_passwd = mj_dest_pool_passwd;
    }

    public int getMj_done_snap_id() {
        return mj_done_snap_id;
    }

    public void setMj_done_snap_id(int mj_done_snap_id) {
        this.mj_done_snap_id = mj_done_snap_id;
    }

    public int getMj_current_rootid() {
        return mj_current_rootid;
    }

    public void setMj_current_rootid(int mj_current_rootid) {
        this.mj_current_rootid = mj_current_rootid;
    }

    public int getMj_current_snap_id() {
        return mj_current_snap_id;
    }

    public void setMj_current_snap_id(int mj_current_snap_id) {
        this.mj_current_snap_id = mj_current_snap_id;
    }

    public int getMj_current_process() {
        return mj_current_process;
    }

    public void setMj_current_process(int mj_current_process) {
        this.mj_current_process = mj_current_process;
    }

    public int getMj_job_status() {
        return mj_job_status;
    }
    public String getMjStatusStr(){
        if( mj_job_status == MirrorJob.MJ_STATUS_STOP ){
            return SanBootView.res.getString("DhcpDialog.label.stop");
        }else{
            return SanBootView.res.getString("DhcpDialog.label.start");
        }
    }
    public String getMjStatusStr( boolean isMgStart ){
        if( isMgStart ){
            return getMjStatusStr();
        }else{
            return SanBootView.res.getString("DhcpDialog.label.stop");
        }
    }

    public boolean isMJStart(){
        return ( mj_job_status == MirrorJob.MJ_STATUS_START ); 
    }
    public boolean isMJStop(){
        return ( mj_job_status == MirrorJob.MJ_STATUS_STOP );
    }
    
    public void setMj_job_status(int mj_job_status) {
        this.mj_job_status = mj_job_status;
    }

    public int getMj_job_pid() {
        return mj_job_pid;
    }

    public void setMj_job_pid(int mj_job_pid) {
        this.mj_job_pid = mj_job_pid;
    }

    public String getMj_desc() {
        return mj_desc;
    }

    public void setMj_desc(String mj_desc) {
        this.mj_desc = mj_desc;
    }
    
    public String getMj_info(){
        return this.mj_info;
    }
    public void setMj_info( String mj_info ){
        this.mj_info = mj_info;
    }

    public int getMj_track_mode() {
        return mj_track_mode;
    }

    public void setMj_track_mode(int mj_track_mode) {
        this.mj_track_mode = mj_track_mode;
    }

    public boolean isTrackAftCrtSnap(){
        return  ( this.getMj_track_mode() == MirrorJob.MJ_TRACK_MODE_AFT_CRT_SNAP );
    }
    public boolean isTrackBefMeltSnap(){
        return ( this.getMj_track_mode() == MirrorJob.MJ_TRACK_MODE_BEF_MELT_SNAP );
    }

    public int getMj_track_src_rootid() {
        return mj_track_src_rootid;
    }

    public void setMj_track_src_rootid(int mj_track_src_rootid) {
        this.mj_track_src_rootid = mj_track_src_rootid;
    }

    public int getMj_track_src_type() {
        return mj_track_src_type;
    }

    public void setMj_track_src_type(int mj_track_src_type) {
        this.mj_track_src_type = mj_track_src_type;
    }

    public boolean isLocalDiskSrcType(){
        return ( getMj_track_src_type() == MirrorJob.MJ_TRACK_SRC_TYPE_LOCALDISK );
    }
    public boolean isSgDiskSrcType(){
        return ( getMj_track_src_type() == MirrorJob.MJ_TRACK_SRC_TYPE_SGDISK );
    }

    /**
     * @return the mj_copy_src_rootid
     */
    public int getMj_copy_src_rootid() {
        return mj_copy_src_rootid;
    }
    public void setMj_copy_src_rootid( int val ){
        mj_copy_src_rootid = val;
    }

    /**
     * @return the mj_copy_src_snapid
     */
    public int getMj_copy_src_snapid() {
        return mj_copy_src_snapid;
    }
    public void setMj_copy_src_snapid( int val ){
        mj_copy_src_snapid = val;
    }

    /**
     * @return the mj_copy_src_type
     */
    public int getMj_copy_src_type() {
        return mj_copy_src_type;
    }
    public void setMj_copy_src_type( int val ){
        mj_copy_src_type = val;
    }
    public boolean isCopyLocalDiskSrcType(){
        return ( getMj_copy_src_type() == MirrorJob.MJ_COPY_SRC_TYPE_LOCALDISK );
    }
    public boolean isCopySgDiskSrcType(){
        return ( getMj_copy_src_type() == MirrorJob.MJ_COPY_SRC_TYPE_SGDISK );
    }
    
    public int getMj_scheduler(){
        return mj_scheduler;
    }
    public void setMj_scheduler( int mj_scheduler ){
        this.mj_scheduler = mj_scheduler;
    }

    @Override public String getComment(){
        return mj_id+"";
    }
    
    @Override public String toString(){
        return toTableString();
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
        return ResourceCenter.BTN_ICON_TASK;
    }
    public String toTableString(){
        return mj_id+"_"+mj_mg_id;
    }
    
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.BTN_ICON_TASK;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.BTN_ICON_TASK;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return this.mj_id+"_"+mj_mg_id ;
    }
    public String toTipString(){
        return toTreeString();
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append( "mj_mg_id: "+this.getMj_mg_id() );
        buf.append( "mj_track_src_rootid:"+this.getMj_track_src_rootid() );
        buf.append( "dest_pool: "+this.mj_dest_pool );
        return buf.toString();
    }

    public static String getTypeString( int type ){
        switch( type ){
            case MirrorJob.MJ_TYPE_LOCAL:
                return SanBootView.res.getString("common.mjtype.local");
            case MirrorJob.MJ_TYPE_LOCAL_TRACK_JOB:
                return SanBootView.res.getString("common.mjtype.localtrack");
            case MirrorJob.MJ_TYPE_REMOTE:
                return SanBootView.res.getString("common.mjtype.remote");
            case MirrorJob.MJ_TYPE_REMOTE_COPY_JOB:
                return SanBootView.res.getString("common.mjtype.remotecopy");
            case MirrorJob.MJ_TYPE_REMOTE_LOG_JOB:
                return SanBootView.res.getString("common.mjtype.log");
            default:
                return SanBootView.res.getString("common.mjtype.remotetrack");
        }
    }

    public int getMg_interval_time() {
        return mg_interval_time;
    }

    public void setMg_interval_time(int mg_interval_tiime) {
        this.mg_interval_time = mg_interval_tiime;
    }

    public int getMg_log_max_time() {
        return mg_log_max_time;
    }

    public void setMg_log_max_time(int mg_log_max_time) {
        this.mg_log_max_time = mg_log_max_time;
    }

    public String getMg_name() {
        return mg_name;
    }

    public void setMg_name(String mg_name) {
        this.mg_name = mg_name;
    }

    public int getMg_max_snapshot() {
        return mg_max_snapshot;
    }

    public void setMg_max_snapshot(int mg_max_snapshot) {
        this.mg_max_snapshot = mg_max_snapshot;
    }
    
    
}
