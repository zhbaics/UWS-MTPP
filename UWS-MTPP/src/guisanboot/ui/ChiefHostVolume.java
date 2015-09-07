/*
 * ChiefHostVolume.java
 *
 * Created on July 28, 2008, 1:10 PM
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
public class ChiefHostVolume extends ChiefNode implements TreeRevealable,TableRevealable{
    /** Creates a new instance of ChiefHostVolume */
    public ChiefHostVolume( ) {
        super( ResourceCenter.TYPE_CHIEF_HOST_VOL );
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
    
    /////////////////////////////////////////////////////////////////
    //
    //          下 面 的 方 法 跟 UI 有 关
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
        return SanBootView.res.getString("common.hostvollist");
    }
    
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_ORPHAN_VOL_LIST;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_ORPHAN_VOL_LIST;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return SanBootView.res.getString("common.hostvollist");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.hostVolList");
    }
}
