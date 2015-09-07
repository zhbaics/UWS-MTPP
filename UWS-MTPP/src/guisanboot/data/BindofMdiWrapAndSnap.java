/*
 * BindofMdiWrapAndSnap.java
 *
 * Created on 2009/09/22, PM�4:55
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
public class BindofMdiWrapAndSnap { 
    public MirrorDiskInfoWrapper mdi  = null;
    public ArrayList snapList = new ArrayList();
    
    /** Creates a new instance of BindofMdiWrapAndSnap */
    public BindofMdiWrapAndSnap() {
    }
    
    public BindofMdiWrapAndSnap( MirrorDiskInfoWrapper mdi, ArrayList list ){
        this.mdi = mdi;
        snapList = list;
    }
    
    public MirrorDiskInfoWrapper getMdi(){
        return mdi;
    }
    
    public ArrayList getSnapList(){
        return snapList;
    }
}
