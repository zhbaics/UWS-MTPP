/*
 * HostProfileService.java
 *
 * Created on 2008/8/27,�PM�5:57
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.datadup.ui.viewobj;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import guisanboot.ui.ServiceItem;
import javax.swing.Icon;

/**
 *
 * @author Administrator
 */
public class HostProfileService extends ServiceItem{
    private int mode; // 0: mtpp 1: cmdp

    /** Creates a new instance of HostProfileService */
    public HostProfileService() {
        this( 0 );
    }

    public HostProfileService( int mode ) {
        this.mode = mode;
    }

    @Override public String toTableString(){
        if( mode == 0 ){
            return SanBootView.res.getString("common.copyproflist");
        }else{
            return SanBootView.res.getString("common.copypproflist");
        }
    }
    
    @Override public Icon getTableIcon(){
        return ResourceCenter.ICON_FIX;
    }
}
