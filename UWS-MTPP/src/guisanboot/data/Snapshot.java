/*
 * Snapshot.java
 *
 * Created on 2006/12/18, PM�4:56
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.res.*;
import guisanboot.MenuAndBtnCenterForMainUi;

/**
 *
 * @author Administrator
 */
// Snapshot表示正常的快照（type为0）,Snapshot不能继承BasicUIObject,因为java不支持多重继承
public class Snapshot extends BasicVDisk implements  GeneralInfo, 
                                                        TreeRevealable, 
                                                        TableRevealable,
                                                        Comparable{
    protected int type = ResourceCenter.TYPE_SNAP_INDEX;
    
    protected BrowserTreeNode treeNode   = null;
    protected BrowserTreeNode fatherNode = null;
    
    private ArrayList<Integer> fsForTree = null;
    private ArrayList<Integer> fsForTable = null;
    
    /** Creates a new instance of Snapshot */
    public Snapshot() {
    }
    
    public Snapshot( BasicVDisk disk ){
        this( disk.getSnap_root_id(), disk.getSnap_local_snapid(), 
                disk.getSnap_name(),disk.getSnap_pool_id(), 
                disk.getSnap_map_file(), disk.getSnap_dat_file(),
                disk.getSnap_orig_id(),disk.getSnap_block_size(),
                disk.getSnap_max_block_no(), disk.getSnap_opened_type(),
                disk.getSnap_target_id(),disk.getSnap_parent(),
                disk.getSnap_child_list(),disk.getSnap_create_time(),
                disk.getSnap_localid(), disk.getSnap_desc(),disk.getSnap_owner()
                );
    }
    
    public Snapshot(
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
    ){
        super( snap_root_id, snap_local_snapid, snap_name, snap_pool_id, snap_map_file, snap_dat_file, snap_orig_id, snap_block_size, 
            snap_max_block_no, snap_opened_type, snap_target_id, snap_parent, snap_child_list,snap_create_time, snap_localid, snap_desc, snap_owner
        );
    }    
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 5 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_VIEW ) );
            // 暂不实现mnt snap and view/ umount view
            //fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MNT_SNAP) );
            
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SNAP ) );

            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SNAP_EVER ) );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 3 );          
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SNAP ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SNAP_EVER ) );
        }
    }
    
    public void setTreeNode(BrowserTreeNode _treeNode){
        treeNode = _treeNode;
    }
    public BrowserTreeNode getTreeNode(){
        return treeNode;
    }
    
    public BrowserTreeNode getFatherNode(){
        return fatherNode;
    }
    public void setFatherNode( BrowserTreeNode _father ){
        fatherNode = _father;
    }
    
    @Override public String toString(){
        if( getSnap_desc().equals("") ){
            return "Snap-"+getCreateTimeStr();
        }else{
            return getSnap_desc();
        }
    }
    
    //** GeneralInfo的实现**/
    public int getType(){
        return type;
    }
    public int getIpIndex(){
        return -1;
    }
    public ArrayList<Integer> getFunctionList(int type){
        addFunctionsForTree();
        addFunctionsForTable();
        
        if( type == Browser.POPMENU_TREE_TYPE )
            return fsForTree;
        else 
            return fsForTable;
    }
    public String getComment(){
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
        return ResourceCenter.ICON_SNAP_16;
    }
    public String toTableString(){
        return this.getSnap_root_id()+"."+getSnap_local_snapid();
    }
  
    //** TreeRevealable的实现**/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_SNAP_16;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_SNAP_16;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return this.getSnap_root_id()+"."+getSnap_local_snapid();
    }
    public String toTipString(){
        return this.getSnap_root_id()+"."+getSnap_local_snapid()+" [ "+
                this.toString()+" | "+this.getCreateTimeStr()+" ]";
    }

    public int compareTo( Object snapObj ){
        Snapshot snap = (Snapshot)snapObj;
        return this.getCreateTimeStr().compareTo( snap.getCreateTimeStr() );
    }

    public String printMe(){
        return toString();
    }
}
