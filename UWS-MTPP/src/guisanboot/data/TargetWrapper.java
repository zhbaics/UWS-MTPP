/*
 * TargetWrapper.java
 *
 * Created on 2007/1/13, AM�11:10
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
public class TargetWrapper {
    // 该对象只用在linux平台上
    public String diskLabel;  //target 对应的 lv 的 mp( 比如/boot,/,/usr等 )
    public VolumeMap tgt;
    
    @Override public String toString(){
        return diskLabel;
    }
    
    /** Creates a new instance of VolumeMapWrapper */
    public TargetWrapper( String diskLabel,VolumeMap tgt ) {
        this.diskLabel = diskLabel;
        this.tgt = tgt;
    }
}
