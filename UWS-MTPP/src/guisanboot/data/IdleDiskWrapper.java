/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data;

/**
 *
 * @author Administrator
 */
public class IdleDiskWrapper {
    public IdleDisk iDisk;
    
    public IdleDiskWrapper( IdleDisk disk ){
        iDisk = disk ;
    }
    
    public String toString(){
        return this.iDisk.getIdleDiskName()+"("+this.iDisk.getIdleDiskSize()+")";
    }
}
