/*
 * ChiefPPProfile.java
 *
 * Created on June 10, 2010, 10:09 AM
 */

package guisanboot.cmdp.entity;

import java.util.ArrayList;
import guisanboot.res.*;
import guisanboot.datadup.ui.viewobj.ChiefProfile;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class ChiefPPProfile extends ChiefProfile {
    /** Creates a new instance of ChiefPPProfile */
    public ChiefPPProfile( int type ) {
        super( type );
    }
    
    public ChiefPPProfile() {
        super( ResourceCenter.TYPE_CHIEF_PPPROF );
    }
    
    @Override public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 0 );
        }
    }
    
    @Override public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 0 );
        }
    }

    @Override public String toTableString(){
        return SanBootView.res.getString("common.copypproflist");
    }
    @Override public String toTreeString(){
        return SanBootView.res.getString("common.copypproflist");
    }
    @Override public String toTipString(){
        return SanBootView.res.getString("common.desc.pprofList");
    }
}
