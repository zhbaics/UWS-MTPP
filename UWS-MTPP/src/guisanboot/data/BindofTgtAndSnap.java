/*
 * BindofVolAndSnap.java
 *
 * Created on 2006/12/30, PM�4:55
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
public class BindofTgtAndSnap { 
    public TargetWrapper tgt  = null;
    public Object snap = null;  //VolumeMapWrapper,SnapWrapper,ViewWrapper,CloneDiskWrapper
    public boolean isSel;
    
    public BindofTgtAndSnap(){   
    }
}
