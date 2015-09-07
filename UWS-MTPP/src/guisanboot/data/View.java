/*
 * View.java
 *
 * Created on 2008/2/19, AM 12:55
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
public class View extends BasicVDisk implements  GeneralInfo, 
                                                    TreeRevealable, 
                                                    TableRevealable{
    private int type = ResourceCenter.TYPE_VIEW_INDEX;
    
    BrowserTreeNode treeNode   = null;
    BrowserTreeNode fatherNode = null;
    
    private ArrayList<Integer> fsForTree = null;
    private ArrayList<Integer> fsForTable = null;
    
    /** Creates a new instance of View */
    public View() {
    }
    
    public View( BasicVDisk disk ){
        this(
            disk.getSnap_root_id(), disk.getSnap_local_snapid(),
            disk.getSnap_name(),disk.getSnap_pool_id(),
            disk.getSnap_map_file(), disk.getSnap_dat_file(),
            disk.getSnap_orig_id(),disk.getSnap_block_size(),
            disk.getSnap_max_block_no(), disk.getSnap_opened_type(),
            disk.getSnap_target_id(),disk.getSnap_parent(),
            disk.getSnap_child_list(),disk.getSnap_create_time(),
            disk.getSnap_localid(), disk.getSnap_desc(),disk.getSnap_owner()
        );
    }
    
    public View(
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
            snap_max_block_no, snap_opened_type, snap_target_id, snap_parent, snap_child_list,snap_create_time, snap_localid, snap_desc,snap_owner
        );
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 7 );
            //fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MNT_VIEW ) );
            //fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_UMNT_VIEW ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_LUNMAP ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_EXPORTLM ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_VIEW) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_UCS_SNAP ));
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ENABLE_IBOOT_VMHOST_DISK ));

        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 7 );
            //fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MNT_VIEW ) );
            //fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_UMNT_VIEW ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_LUNMAP ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_EXPORTLM ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_VIEW) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );

            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_UCS_SNAP ));
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ENABLE_IBOOT_VMHOST_DISK ));

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
        if( getSnap_name().equals("") ){
            return "View-"+getCreateTimeStr();
        }else{
            return getSnap_name();
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
        return ResourceCenter.ICON_VIEW; 
    }
    public String toTableString(){
        return this.getSnap_root_id()+"."+this.getSnap_local_snapid()+"("+this.getTargetID()+")";
    }
  
    //** TreeRevealable的实现**/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_VIEW; 
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_VIEW; 
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return this.getSnap_root_id()+"."+this.getSnap_local_snapid();
    }
    public String toTipString(){
        return this.getSnap_root_id()+"."+this.getSnap_local_snapid()+" [ "+this.getSnap_target_id()+" | "+
                this.toString()+" | "+this.getCreateTimeStr()+" ]";
    }
}
