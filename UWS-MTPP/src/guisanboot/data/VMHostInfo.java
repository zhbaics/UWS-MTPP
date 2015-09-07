/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Administrator
 */
public class VMHostInfo extends BasicUIObject{
    
    public final static String VM_RecordFlag="Record:";
    public final static String VM_name = "vm_name";
    public final static String VM_path = "vm_path";
    public final static String VM_ip = "vm_ip";
    public final static String VM_vip = "vm_vip";
    public final static String VM_port = "vm_port";
    public final static String VM_targetid = "vm_targetid";
    public final static String VM_snapid = "vm_snapid";
    public final static String VM_viewid = "vm_viewid";
    public final static String VM_letter = "vm_letter";
    public final static String VM_clntid = "vm_clntid";
    public final static String VM_recoverip = "vm_recoverip";
    public final static String VM_standby_maxtime = "vm_maxdisctime";
    public final static String VM_standbyCheckIP = "vm_pingip";
    public final static String VM_standbyIsCheck = "vm_ischeck";
    
    private String vm_clntid = "";
    private String vm_name ="";
    private String vm_path ="";
    private String vm_ip ="";
    private String vm_vip ="";
    private String vm_port ="";
    private String vm_targetid ="";
    private String vm_snapid ="";
    private String vm_snap_root_id = "";
    private String vm_viewid ="";
    private String vm_letter ="";
    private int vm_recoverip = 0 ;   //0标识不自动恢复，1表示自动恢复
    private String vm_maxdisctime = "";
    private String vm_pingip = "";
    private String vm_ischeck = "";
    
    public String getVm_ischeck() {
        return vm_ischeck;
    }

    public void setVm_ischeck(String vm_ischeck) {
        this.vm_ischeck = vm_ischeck;
    }

    public String getVm_maxdisctime() {
        return vm_maxdisctime;
    }

    public void setVm_maxdisctime(String vm_maxdisctime) {
        this.vm_maxdisctime = vm_maxdisctime;
    }

    public String getVm_pingip() {
        return vm_pingip;
    }

    public void setVm_pingip(String vm_pingip) {
        this.vm_pingip = vm_pingip;
    }

    
    public VMHostInfo() {
        super( ResourceCenter.TYPE_VM_HOST );
    }

    @Override
    public void addFunctionsForTable() {
        if( fsForTable == null ){
            fsForTable =  new ArrayList<Integer>( 0);
//            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_POWERON_VMHOST ));
//            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_POWEROFF_VMHOST ));
//            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_VMHOST_SWITCHNETDISK ));
//            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_VMHOST_IP ));
//            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_RECOVER_VMHOST_IP ));
//            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ));
//            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_VMHOST ));
//            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_VMHOST ));
        }
    }

    @Override
    public void addFunctionsForTree() {
        if( fsForTree == null ){
            fsForTree =  new ArrayList<Integer>( 10 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_POWERON_VMHOST ));
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_POWEROFF_VMHOST ));
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_VMHOST_SWITCHNETDISK ));
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_VMHOST_QUICK_LOGON_DATA_DISK ));
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_VMHOST_IP ));
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_RECOVER_VMHOST_IP ));
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEE_VMHOST_IPCONFIG ));
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ));
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_VMHOST ));
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_VMHOST ));
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
        return vm_name+"";
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

    public String toTipString() {
        return vm_name;
    }

    public String toTreeString() {
        return vm_name;
    }

    public String getVm_ip() {
        return vm_ip;
    }

    public void setVm_ip(String vm_ip) {
        this.vm_ip = vm_ip;
    }

    public String getVm_name() {
        return vm_name;
    }

    public void setVm_name(String vm_name) {
        this.vm_name = vm_name;
    }

    public String getVm_path() {
        return vm_path;
    }

    public void setVm_path(String vm_path) {
        this.vm_path = vm_path;
    }

    public String getVm_port() {
        return vm_port;
    }

    public void setVm_port(String vm_port) {
        this.vm_port = vm_port;
    }

    public String getVm_snapid() {
        return vm_snapid;
    }

    public void setVm_snapid(String vm_snapid) {
        this.vm_snapid = vm_snapid;
    }

    public String getVm_targetid() {
        return vm_targetid;
    }

    public void setVm_targetid(String vm_targetid) {
        this.vm_targetid = vm_targetid;
    }

    public String getVm_vip() {
        return vm_vip;
    }

    public void setVm_vip(String vm_vip) {
        this.vm_vip = vm_vip;
    }

    public String getVm_letter() {
        return vm_letter;
    }

    public void setVm_letter(String vm_letter) {
        this.vm_letter = vm_letter;
    }

    public String getVm_viewid() {
        return vm_viewid;
    }

    public void setVm_viewid(String vm_viewid) {
        this.vm_viewid = vm_viewid;
    }

    public String getVm_clntid() {
        return vm_clntid;
    }

    public void setVm_clntid(String vm_clntid) {
        this.vm_clntid = vm_clntid;
    }

    public int getVm_recoverip() {
        return vm_recoverip;
    }

    public void setVm_recoverip(int vm_recoverip) {
        this.vm_recoverip = vm_recoverip;
    }

    public String getVm_snap_root_id(){
        return vm_snap_root_id;
    }

    public void setVm_snap_root_id(String vm_snap_root_id){
        this.vm_snap_root_id = vm_snap_root_id;
    }
    
    
    
}
