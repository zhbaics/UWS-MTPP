/*
 * VolumeWrapper.java
 *
 * Created on 2007/8/15, PM�1:11
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
public class VolumeWrapper {
    public Volume vol;
    
    /** Creates a new instance of VolumeWrapper */
    public VolumeWrapper( Volume _vol ) {
        vol = _vol;
    }
    
    @Override public String toString(){
        return vol.getCapStr1();
    }
}
