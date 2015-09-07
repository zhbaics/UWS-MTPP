/*
 * ChiefCopyJobService.java
 *
 * Created on 2010/1/26, PM 2:00
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.unlimitedIncMj.ui;

import guisanboot.ui.*;
import guisanboot.res.ResourceCenter;
import javax.swing.Icon;

/**
 *
 * @author Administrator
 */
public class ChiefCopyJobService extends ServiceItem{
    
    /** Creates a new instance of ChiefCopyJobService */
    public ChiefCopyJobService() {
    }
    
    @Override public String toTableString(){
        return SanBootView.res.getString("common.cjList");
    }
    
    @Override public Icon getTableIcon(){
        return ResourceCenter.ICON_FIX;
    }
}
