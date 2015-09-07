/*
 * BindOfVolMapandSnap.java
 *
 * Created on 2007/1/13,�AM 11:12
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
public class BindOfVolMapandSnap {
    public Object volObj; // VolumeMap or MirrorDiskInfo
    public Object snap; // snapwraper /viewwraper/ volumewrapper /mirroredsnap/clonediskWrapper
    public boolean isSel=false;
    
    /** Creates a new instance of BindOfVolMapandSnap */
    public BindOfVolMapandSnap() {
    }
    
    public VolumeMap getVolMap(){
        return (VolumeMap)volObj;
    }
    
    public MirrorDiskInfo getMDI( ){
        return (MirrorDiskInfo)volObj;
    }
}
