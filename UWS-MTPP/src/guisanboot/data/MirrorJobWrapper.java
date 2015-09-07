/*
 * MirrorJobWrapper.java
 *
 * Created on 2008/6/12, PM�1:16
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

/**
 *
 * @author zjj
 */
public class MirrorJobWrapper {
    public MirrorJob mj;
    public String pool_info;
    
    /** Creates a new instance of MirrorJobWrapper */
    public MirrorJobWrapper(
        MirrorJob mj,
        String pool_info
    ) {
        this.mj = mj;
        this.pool_info = pool_info;
    }
}
