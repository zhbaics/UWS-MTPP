/*
 * ChiefNetBootHost.java
 *
 * Created on July 28, 2008, 1:55 PM
 */

package guisanboot.ui;

import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ChiefNetBootHost extends ChiefNode implements TreeRevealable,TableRevealable{   
    /** Creates a new instance of ChiefNetBootHost */
    public ChiefNetBootHost( ) {
        super( ResourceCenter.TYPE_CHIEF_NETBOOT_HOST );
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>(0);
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>(0);
        }
    }
            
    /////////////////////////////////////////////////////////////////
    //
    //         下 面 的 方 法 跟 UI 有 关
    //
    /////////////////////////////////////////////////////////////////
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
        return SanBootView.res.getString("common.netboothostlist");
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
        return SanBootView.res.getString("common.netboothostlist");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.netbootedHostList");
    }
}
