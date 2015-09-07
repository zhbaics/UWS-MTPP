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

import java.util.*;

/**
 *
 * @author Administrator
 */
public class BindofVolAndSnap {
    private Object volObj = null;
    private ArrayList snapList = new ArrayList();
    
    /** Creates a new instance of BindofVolAndSnap */
    public BindofVolAndSnap() {
    }
    
    public BindofVolAndSnap( Object volObj,ArrayList list ){
        this.volObj = volObj;
        snapList = list;
    }
    
    public VolumeMap getVolMap(){
        return (VolumeMap)volObj;
    }
    
    public MirrorDiskInfo getMDI(){
        return (MirrorDiskInfo)volObj;
    }
    
    public Object getVolObj(){
        return volObj;
    }
    
    public ArrayList getSnapList(){
        return snapList;
    }
}
