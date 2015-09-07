/*
 * MirrorJobService.java
 *
 * Created on 2008/6/20, PM�2:34
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

/**
 *
 * @author Administrator
 */
public class MirrorJobService extends ServiceItem{
    
    /** Creates a new instance of MirrorJobService */
    public MirrorJobService() {
    }
    
    @Override public String toTableString(){
        return SanBootView.res.getString("common.mjlist");
    }
}
