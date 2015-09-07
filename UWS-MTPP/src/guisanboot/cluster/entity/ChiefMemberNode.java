/*
 * ChiefMemberNode.java
 *
 * Created on Aug 4, 2011, 16:13 PM
 */

package guisanboot.cluster.entity;

import java.util.ArrayList;
import guisanboot.res.*;
import guisanboot.datadup.ui.viewobj.ChiefProfile;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class ChiefMemberNode extends ChiefProfile {
    /** Creates a new instance of ChiefMemberNode */
    public ChiefMemberNode( int type ) {
        super( type );
    }
    
    public ChiefMemberNode() {
        super( ResourceCenter.TYPE_CLUSTER_CHIEF_MEMBER_NODE );
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
        return SanBootView.res.getString("common.memberNodelist");
    }
    @Override public String toTreeString(){
        return SanBootView.res.getString("common.memberNodelist");
    }
    @Override public String toTipString(){
        return SanBootView.res.getString("common.desc.memberNodeList");
    }
}
