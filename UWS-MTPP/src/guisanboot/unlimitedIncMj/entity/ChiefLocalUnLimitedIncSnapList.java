/*
 * ChiefLocalUnLimitedIncSnapList.java
 *
 * Created on Jan 12, 2010, 4:52 PM
 */

package guisanboot.unlimitedIncMj.entity;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.ui.*;
import javax.swing.*;
import java.util.ArrayList;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ChiefLocalUnLimitedIncSnapList extends ChiefUnLimitedIncSnapList{
    /** Creates a new instance of ChiefLocalUnLimitedIncSnapList */
    public ChiefLocalUnLimitedIncSnapList() {
        super( ResourceCenter.TYPE_CHIEF_UIM_SNAP1 );
    }
    
    @Override public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 1 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_QUEYR_UISNAP ) );
        }
    }
    
    @Override public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 1 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_QUEYR_UISNAP ) );
        }
    }
    
    /////////////////////////////////////////////////////////////////
    //
    //           下 面 的 方 法 跟 UI 有 关�
    //
    /////////////////////////////////////////////////////////////////
    //** TreeRevealable的实现*/
    @Override public Icon getExpandIcon(){
        return ResourceCenter.BTN_ICON_TASKLIST;
    }
    @Override public Icon getCollapseIcon(){
        return ResourceCenter.BTN_ICON_TASKLIST;
    }
    @Override public boolean enableTreeEditable(){
        return false;
    }
    @Override public String toTreeString(){
        return SanBootView.res.getString("common.unlimitedmirrorsnap");
    }
    @Override public String toTipString(){
        return toTreeString();
    }
}
