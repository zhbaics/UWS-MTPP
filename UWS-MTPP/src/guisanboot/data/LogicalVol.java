/*
 * LogicalVol.java
 *
 * Created on 2007/8/2, PM�1:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

/**
 *
 * @author Administrator
 */
public class LogicalVol extends VolumeMap { 
    /** Creates a new instance of LogicalVol */
    public LogicalVol(
        String lv_name,
        int lv_clntid,
        String mp,
        int target_id,
        int max_snap,
        String vg
    ) {
        super( lv_name,lv_clntid,mp,target_id,max_snap,vg,-1,BootHost.PROTECT_TYPE_MTPP );
    }
    
    public LogicalVol(
        String lv_name,
        int lv_clntid,
        String mp,
        int target_id,
        int max_snap,
        String vg,
        int rootid
    ) {
        super( lv_name,lv_clntid,mp,target_id,max_snap,vg,rootid,BootHost.PROTECT_TYPE_MTPP );
    }

     public LogicalVol(
        String lv_name,
        int lv_clntid,
        String mp,
        int target_id,
        int max_snap,
        String vg,
        int rootid,
        int protect_type
    ) {
        super( lv_name,lv_clntid,mp,target_id,max_snap,vg,rootid,protect_type );
    }

    public LogicalVol(
        String lv_name,
        int lv_clntid,
        String mp,
        int target_id,
        int max_snap,
        String vg,
        int rootid,
        int protect_type,
        int mgid
    ) {
        super( lv_name,lv_clntid,mp,target_id,max_snap,vg,rootid,protect_type, mgid );
    }
    
    // 当vol_target_id为0时,表示这个lv是使用真正的卷软件创建出来的（比如Linux上的LVM）;
    // 当vol_target_id为非0时，表示这个lv是个假的卷(即它实际上是个target)
    public boolean isRealLV(){
        return ( getVolTargetID() == 0 );
    }
    
    public String getLVName(){
        return getVolName();
    }
    public String getVgName(){
        return getVolDesc();
    }
    
    // 重载 GeneralInfo的getComment()
    @Override public String getComment(){
        return getVolDiskLabel();
    }
}
