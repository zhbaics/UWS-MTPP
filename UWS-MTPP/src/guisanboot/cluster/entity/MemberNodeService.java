/*
 * MemberNodeService.java
 *
 * Created on 2011/8/4, PM 17:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.cluster.entity;

import guisanboot.ui.*;
import guisanboot.res.ResourceCenter;
import javax.swing.Icon;

/**
 *
 * @author Administrator
 */
public class MemberNodeService extends ServiceItem{
    
    /** Creates a new instance of MemberNodeService */
    public MemberNodeService() {
    }
    
    @Override public String toTableString(){
        return SanBootView.res.getString("common.memberNodelist");
    }
    
    @Override public Icon getTableIcon(){
        return ResourceCenter.ICON_FIX;
    }
}
