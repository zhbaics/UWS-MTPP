/*
 * ChiefRollbackHost.java
 *
 * Created on Aug 15, 2009, 4:20 PM
 */

package guisanboot.remotemirror;

import guisanboot.ui.*;
import javax.swing.*;
import java.util.*;

import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ChiefRollbackHost extends ChiefNode implements TreeRevealable,TableRevealable{    
    /** Creates a new instance of ChiefRollbackHost */
    public ChiefRollbackHost( ) {
        super( ResourceCenter.TYPE_CHIEF_ROLLBACK_HOST );
    }
    
    public void addFunctionsForTree(){       
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 0 );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 0 );
        }
    }

    //** TableRevealable的实现**/
    public boolean enableTableEditable(){
        return false;
    }
    public boolean enableSelected(){
        return true;
    }
    public int  getAlignType(){
        return JLabel.LEFT;
    }
    public Icon getTableIcon(){
        return ResourceCenter.ICON_SERVICE;
    }
    public String toTableString(){
        return SanBootView.res.getString("common.rbhostlist");
    }
    
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_HOSTLIST;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_HOSTLIST;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return SanBootView.res.getString("common.rbhostlist");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.rbhostList");
    }
}
