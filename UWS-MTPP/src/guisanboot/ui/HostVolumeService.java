/*
 * HostVolumeService.java
 *
 * Created on 2008/6/30, PM 2:34
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.res.ResourceCenter;
import javax.swing.Icon;

/**
 *
 * @author Administrator
 */
public class HostVolumeService extends ServiceItem{
    
    /** Creates a new instance of HostVolumeService */
    public HostVolumeService() {
    }
    
    @Override public String toTableString(){
        return SanBootView.res.getString("common.hostvollist");
    }
    
    @Override public Icon getTableIcon(){
        return ResourceCenter.ICON_FIX;
    }
}
