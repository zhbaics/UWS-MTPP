/*
 * SchService.java
 *
 * Created on 2008/7/25, PM�2:34
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.datadup.ui;

import guisanboot.ui.SanBootView;
import guisanboot.ui.ServiceItem;

/**
 *
 * @author Administrator
 */
public class SchService extends ServiceItem{
    
    /** Creates a new instance of SchService */
    public SchService() {
    }
    
    @Override public String toTableString(){
        return SanBootView.res.getString("common.schlist");
    }
}
