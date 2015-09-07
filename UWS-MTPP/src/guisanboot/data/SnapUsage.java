/*
 * SnapUsage.java
 *
 * Created on 2008/6/16,�AM 11:04
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.BasicUIObject;

/**
 *
 * @author zjj
 */
public class SnapUsage extends BasicUIObject {
    public final static String SU_FLAG       = "Record:";
    public final static String SU_ID         = "usage_id";
    public final static String SU_SRCAGNT_ID = "dst_agent_id";
    public final static String SU_SNAP_ROOTID = "snap_rootid";
    public final static String SU_SNAP_LOCALID = "snap_local_id";
    public final static String SU_VIEW_ID = "snap_view_local_id";
    public final static String SU_EXP_MP  = "export_mp";
    public final static String SU_TID      = "snap_target_id";
    public final static String SU_CRT_TIME = "snap_crt_time";
    
    private int usage_id; 
    private int dst_agent_id; 
    private int snap_rootid;     // 该snap在目的端uws上的 rootid
    private int snap_local_id;   // 该snap在目的端uws上的 local_id
    private int snap_view_local_id; // 该snap的view在目的端uws上的 local_id
    private String export_mp;  // length: 255
    private int snap_target_id; 
    private String snap_crt_time; // 14
    
    /** Creates a new instance of SnapUsage */
    public SnapUsage() {
        super( ResourceCenter.TYPE_SNAP_USAGE );
    }

    public SnapUsage(
        int usage_id,
        int dst_agent_id,
        int snap_rootid,
        int snap_local_id,
        int snap_view_local_id,
        String export_mp,
        int snap_target_id,
        String snap_crt_time
    ){
        super( ResourceCenter.TYPE_SNAP_USAGE );
        
        this.usage_id = usage_id;
        this.dst_agent_id = dst_agent_id;
        this.snap_rootid = snap_rootid;
        this.snap_local_id = snap_local_id;
        this.snap_view_local_id = snap_view_local_id;
        this.export_mp = export_mp;
        this.snap_target_id = snap_target_id;
        this.snap_crt_time = snap_crt_time;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 1 );
            //fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CANCEL_LM ) );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 1 );
            //fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CANCEL_LM ) );
        }
    }
    
    public int getUsage_id() {
        return usage_id;
    }

    public void setUsage_id(int usage_id) {
        this.usage_id = usage_id;
    }

    public int getDst_agent_id() {
        return dst_agent_id;
    }

    public void setDst_agent_id(int dst_agent_id) {
        this.dst_agent_id = dst_agent_id;
    }

    public int getSnap_rootid() {
        return snap_rootid;
    }

    public void setSnap_rootid(int snap_rootid) {
        this.snap_rootid = snap_rootid;
    }

    public int getSnap_local_id() {
        return snap_local_id;
    }

    public void setSnap_local_id(int snap_local_id) {
        this.snap_local_id = snap_local_id;
    }

    public int getSnap_view_local_id() {
        return snap_view_local_id;
    }

    public void setSnap_view_local_id(int snap_view_local_id) {
        this.snap_view_local_id = snap_view_local_id;
    }

    public String getExport_mp() {
        return export_mp;
    }

    public void setExport_mp(String export_mp) {
        this.export_mp = export_mp;
    }
    
    public int getSnapTid(){
        return this.snap_target_id;
    }
    public void setSnapTid( int tid ){
        this.snap_target_id = tid;
    }
    
    public String getCrtTime(){
        return this.snap_crt_time;
    }
    public void setCrtTime( String val ){
        this.snap_crt_time = val;
    }
    
    @Override public String getComment(){
        return toString();
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
        // 当存在多个相同的镜像盘时，某个盘（文件系统）对应的usage_id最大，那么它就是该盘当前所用的版本
        return this.export_mp+"  [ "+this.snap_rootid + "/" + this.usage_id +" ]";
    }
    
    //** TreeRevealable的实现�
    public Icon getExpandIcon(){
        return null;
    }
    public Icon getCollapseIcon(){
        return null;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return toString();
    }
    public String toTipString(){
        return toTreeString();
    }
}
