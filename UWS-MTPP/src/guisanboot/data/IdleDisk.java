/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data;

/**
 *
 * @author Administrator
 */
public class IdleDisk {
    public static final String IDLE_DISK_NAME = "name";
    public static final String IDLE_DISK_SIZE = "size";
    public static final String IDLE_DISK_RECFLAG = "---index";
    public static final String IDLE_DISK_COUNT = "count";
    
    private String idleDiskName = "";
    private String idleDiskSize = "";

    public IdleDisk() {
    }
    
    public IdleDisk( String name , String size ){
        this.idleDiskName = name ;
        this.idleDiskSize = size ;
    }
    
    public String getIdleDiskName() {
        return idleDiskName;
    }

    public void setIdleDiskName(String idleDiskName) {
        this.idleDiskName = idleDiskName;
    }

    public String getIdleDiskSize() {
        return idleDiskSize;
    }

    public void setIdleDiskSize(String idleDiskSize) {
        this.idleDiskSize = idleDiskSize;
    }
    
}
