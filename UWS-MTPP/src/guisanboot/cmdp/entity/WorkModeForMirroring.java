/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.entity;

/**
 * WorkModeForMirroring.java
 *
 * Created on 2010-11-16, 15:31:16
 */
public class WorkModeForMirroring {
    private String  disk;
    private boolean isMgStart = false;

    public WorkModeForMirroring(String disk,boolean isMgStart ){
        this.disk = disk;
        this.isMgStart = isMgStart;
    }

    /**
     * @return the disk
     */
    public String getDisk() {
        return disk;
    }

    /**
     * @param disk the disk to set
     */
    public void setDisk(String disk) {
        this.disk = disk;
    }

    /**
     * @return the isMgStart
     */
    public boolean isIsMgStart() {
        return isMgStart;
    }

    /**
     * @param isMgStart the isMgStart to set
     */
    public void setIsMgStart(boolean isMgStart) {
        this.isMgStart = isMgStart;
    }
}
