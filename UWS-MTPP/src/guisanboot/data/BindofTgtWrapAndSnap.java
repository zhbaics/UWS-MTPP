/*
 * BindofVolAndSnap.java
 *
 * Created on 2006/12/30,�PM�4:55
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
public class BindofTgtWrapAndSnap { 
    public TargetWrapper tgt  = null;
    public ArrayList snapList = new ArrayList();
    
    /** Creates a new instance of BindofVolAndSnap */
    public BindofTgtWrapAndSnap() {
    }
    
    public BindofTgtWrapAndSnap( TargetWrapper tgt,ArrayList list ){
        this.tgt = tgt;
        snapList = list;
    }
    
    public TargetWrapper getTarget(){
        return tgt;
    }
    
    public ArrayList getSnapList(){
        return snapList;
    }
}
