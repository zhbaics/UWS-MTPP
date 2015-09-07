/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.unlimitedIncMj.model.table;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BasicVDisk;
import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.BasicUIObject;

/**
 * 2009.12.21 AM 11:37
 * @author zjj
 */
public class CloneDisk extends BasicUIObject{
    public final static String CloneDisk_FLAG ="Record:";
    public final static String CloneDisk_ID ="clone_disk_id";
    public final static String CloneDisk_SRC_HOST_TYPE ="src_host_type";
    public final static String CloneDisk_SRC_HOST_ID ="src_host_id";
    public final static String CloneDisk_SRC_DISK_ROOT_ID = "src_disk_root_id";
    public final static String CloneDisk_SRC_INC_MIRVOL_ROOT_ID ="src_inc_mirvol_root_id";
    public final static String CloneDisk_SRC_INC_MIRVOL_SNAP_LOCAL_ID = "src_inc_mirvol_snap_local_id";
    public final static String CloneDisk_CRT_TIME ="clone_disk_crt_time";
    public final static String CloneDisk_ROOT_ID = "clone_disk_root_id";
    public final static String CloneDisk_Label ="clone_disk_label";
    public final static String CloneDisk_Desc ="clone_disk_desc";
    public final static String CloneDisk_Tid = "clone_disk_target_id";

    // src_host_type definition
    public final static int IS_BOOTHOST  = 0;
    public final static int IS_SRCAGNT   = 1;
    public final static int IS_FREEVOL   = 2;
    public final static int IS_REMOTE_FREEVOL = 3;

    private int id;
    private int src_host_type = 0 ; // 0: Boothost/ 1: SrcAgent/ 2: FreeVol/ 3: RemoteFreeVol
    private int src_host_id;   // host id
    private int src_disk_root_id;  // 此克隆盘的原始盘的root id

    private int src_inc_mirvol_root_id;  // 属于哪个“无限增量镜像卷”的
    private int src_inc_mirvol_snap_local_id;  // 属于哪个“无限增量镜像快照”、

    private String crt_time;    // 对应的“无限增量镜像快照”的创建时间
    private int root_id;        // 在vsnap中对应的root_id;
    private String label;       // 对应的文件系统，可能是C:\，或者是/boot目录
    private String desc;
    private int target_id;

    public CloneDisk() {
        super( ResourceCenter.TYPE_CLONE_DISK );
    }

    public CloneDisk(
        int id,
        int src_host_type,
        int src_host_id,
        int src_disk_root_id,
        int src_inc_mirvol_root_id,
        int src_inc_mirvol_snap_local_id,
        String crt_time,
        int root_id,
        String label,
        String desc,
        int target_id
    ){
        super( ResourceCenter.TYPE_CLONE_DISK );

        this.id = id;
        this.src_host_type = src_host_type;
        this.src_host_id = src_host_id ;
        this.src_disk_root_id = src_disk_root_id;
        this.src_inc_mirvol_root_id = src_inc_mirvol_root_id;
        this.src_inc_mirvol_snap_local_id = src_inc_mirvol_snap_local_id;
        this.crt_time = crt_time;
        this.root_id = root_id;
        this.label = label;
        this.desc = desc;
        this.target_id = target_id;
    }

    public void addFunctionsForTree(){
        if( fsForTree == null ){
            // 不允许直接从界面上删除volume map对象
            fsForTree = new ArrayList<Integer>( 2 );
            fsForTree.add(  new Integer( MenuAndBtnCenterForMainUi.FUNC_LUNMAP ) );
            fsForTree.add(  new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_CLONE_DISK ) );
        }
    }

    public void addFunctionsForTable(){
        if( fsForTable == null ){
            // 不允许直接从界面上删除volume map对象
            fsForTable = new ArrayList<Integer>( 2 );
            fsForTable.add(  new Integer( MenuAndBtnCenterForMainUi.FUNC_LUNMAP ) );
            fsForTable.add(  new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_CLONE_DISK ) );
        }
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    /**
     * @return the src_host_type
     */
    public int getSrc_host_type() {
        return src_host_type;
    }

    /**
     * @param src_host_type the src_host_type to set
     */
    public void setSrc_host_type(int src_host_type) {
        this.src_host_type = src_host_type;
    }

    public boolean isBelongedBootHost(){
        return ( this.src_host_type == CloneDisk.IS_BOOTHOST );
    }
    public boolean isBelongedSrcAgent(){
        return ( this.src_host_type == CloneDisk.IS_SRCAGNT );
    }
    public boolean isBelongedFreeVol(){
        return ( this.src_host_type == CloneDisk.IS_FREEVOL );
    }
    public boolean isBelongedRemoteFreeVol(){
        return ( this.src_host_type == CloneDisk.IS_REMOTE_FREEVOL );
    }
    
    /**
     * @return the src_host_id
     */
    public int getSrc_host_id() {
        return src_host_id;
    }

    /**
     * @param src_host_id the src_host_id to set
     */
    public void setSrc_host_id(int src_host_id) {
        this.src_host_id = src_host_id;
    }

    /**
     * @return the src_disk_root_id
     */
    public int getSrc_disk_root_id() {
        return src_disk_root_id;
    }

    /**
     * @param src_disk_root_id the src_disk_root_id to set
     */
    public void setSrc_disk_root_id(int src_disk_root_id) {
        this.src_disk_root_id = src_disk_root_id;
    }

    public int getSrc_inc_mirvol_root_id() {
        return this.src_inc_mirvol_root_id;
    }

    public void setSrc_inc_mirvol_root_id( int src_inc_mirvol_root_id  ) {
        this.src_inc_mirvol_root_id = src_inc_mirvol_root_id;
    }

    public int getSrc_inc_mirvol_snap_local_id(){
        return src_inc_mirvol_snap_local_id;
    }

    public void setSrc_inc_mirvol_snap_local_id( int src_inc_mirvol_snap_local_id ){
        this.src_inc_mirvol_snap_local_id = src_inc_mirvol_snap_local_id;
    }

    /**
     * @return the crt_time
     */
    public String getCrt_time() {
        return crt_time;
    }

    public String getCrtTimeString(){
        return BasicVDisk.getCreateTimeStr( crt_time );
    }

    /**
     * @param crt_time the crt_time to set
     */
    public void setCrt_time(String crt_time) {
        this.crt_time = crt_time;
    }

    /**
     * @return the root_id
     */
    public int getRoot_id() {
        return root_id;
    }

    /**
     * @param root_id the root_id to set
     */
    public void setRoot_id(int root_id) {
        this.root_id = root_id;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDefaultDesc(){
        this.desc = "clone_from_"+this.getSrc_inc_mirvol_root_id() +"."+this.getSrc_inc_mirvol_snap_local_id();
    }

    /**
     * @return the target_id
     */
    public int getTarget_id() {
        return target_id;
    }

    /**
     * @param target_id the target_id to set
     */
    public void setTarget_id(int target_id) {
        this.target_id = target_id;
    }

    @Override public String toString(){
        return this.label;
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
        return this.id+"";
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
        return this.label;
    }
    public String toTipString(){
        return label+" [ "+this.target_id +" | " + this.root_id + " ]";
    }

    public void prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append(" \nid: "+ id );
        buf.append(" \nsrc_host_type: "+ src_host_type );
        buf.append(" \nsrc_host_id: "+ this.src_host_id );
        buf.append(" \nsrc_disk_root_id: "+this.src_disk_root_id );
        buf.append(" \nsrc_inc_mirvol_root_id: "+ this.getSrc_inc_mirvol_root_id() );
        buf.append(" \nsrc_inc_mirvol_snap_local_id: "+this.getSrc_inc_mirvol_snap_local_id() );
        buf.append(" \ncrt_time: "+ this.getCrt_time() );
        buf.append(" \nroot_id: "+this.getRoot_id() );
        buf.append(" \nlabel; "+this.getLabel() );
        buf.append(" \ndesc; "+this.getDesc() );
        buf.append(" \ntarget_id: "+this.getTarget_id() );
        System.out.println( buf.toString() );
    }
}
