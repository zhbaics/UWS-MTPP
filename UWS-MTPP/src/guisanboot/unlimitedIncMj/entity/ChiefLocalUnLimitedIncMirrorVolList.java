/*
 * ChiefLocalUnLimitedIncMirrorVolList.java
 *
 * Created on Dec 14, 2009, 11:15 AM
 */

package guisanboot.unlimitedIncMj.entity;

import guisanboot.ui.*;
import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ChiefLocalUnLimitedIncMirrorVolList extends ChiefNode implements TreeRevealable{      
    /** Creates a new instance of ChiefLocalUnLimitedIncMirrorVolList */
    public ChiefLocalUnLimitedIncMirrorVolList() {
        super( ResourceCenter.TYPE_CHIEF_UNLIMITED_INC_MIRROR_VOL );
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
    //           下 面 的 方 法 跟 UI 有 关�
    //
    /////////////////////////////////////////////////////////////////
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.BTN_ICON_TASKLIST;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.BTN_ICON_TASKLIST;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return SanBootView.res.getString("common.unlimitedmirrorvol");
    }
    public String toTipString(){
        return toTreeString();
    }
}
