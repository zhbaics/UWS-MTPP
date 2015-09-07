/*
 * DeletingSnapshot.java
 *
 * Created on 2008/7/11,�PM�4:07
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import javax.swing.Icon;

/**
 *
 * @author zjj
 */
public class DeletingSnapshot extends Snapshot{
    
    public DeletingSnapshot(){ 
        type = ResourceCenter.TYPE_DEL_SNAP_INDEX;
    }
    
    public DeletingSnapshot( BasicVDisk disk ){
        this( disk.getSnap_root_id(), disk.getSnap_local_snapid(), 
            disk.getSnap_name(),disk.getSnap_pool_id(), 
            disk.getSnap_map_file(), disk.getSnap_dat_file(),
            disk.getSnap_orig_id(),disk.getSnap_block_size(),
            disk.getSnap_max_block_no(), disk.getSnap_opened_type(),
            disk.getSnap_target_id(),disk.getSnap_parent(),
            disk.getSnap_child_list(),disk.getSnap_create_time(),
            disk.getSnap_localid(), disk.getSnap_desc(),disk.getSnap_owner()
        );
        type = ResourceCenter.TYPE_DEL_SNAP_INDEX;
    }
    
    /** Creates a new instance of DeletingSnapshot */
    public DeletingSnapshot(
         int snap_root_id,
        int snap_local_snapid,
        String snap_name,
        int snap_pool_id,
        String snap_map_file,
        String snap_dat_file,
        int snap_orig_id,
        int snap_block_size,
        int snap_max_block_no, 
        int snap_opened_type,
        int snap_target_id,
        int snap_parent,
        String snap_child_list,
        String snap_create_time,
        int snap_localid,
        String snap_desc,
        String snap_owner
    ) {
        super( snap_root_id, snap_local_snapid, snap_name, snap_pool_id, snap_map_file, snap_dat_file, snap_orig_id, snap_block_size, 
            snap_max_block_no, snap_opened_type, snap_target_id, snap_parent, snap_child_list,snap_create_time, snap_localid, snap_desc,snap_owner
        );
        type = ResourceCenter.TYPE_DEL_SNAP_INDEX;
    }
    
    // 将不一样的函数重载
    @Override public Icon getTableIcon(){
        return ResourceCenter.ICON_DEL_SNAP_16;
    } 
    @Override public Icon getExpandIcon(){
        return ResourceCenter.ICON_DEL_SNAP_16;
    }
    @Override public Icon getCollapseIcon(){
        return ResourceCenter.ICON_DEL_SNAP_16;
    } 
}
