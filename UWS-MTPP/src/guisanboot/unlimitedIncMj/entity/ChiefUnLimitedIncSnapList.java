/*
 * ChiefUnLimitedIncSnapList.java
 *
 * Created on Jan 12, 2010, 4:52 PM
 */

package guisanboot.unlimitedIncMj.entity;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.ui.*;
import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ChiefUnLimitedIncSnapList extends ChiefNode implements TreeRevealable{      
    /** Creates a new instance of ChiefUnLimitedIncSnapList */
    public ChiefUnLimitedIncSnapList( int type ) {
        super( type );
    }

    public ChiefUnLimitedIncSnapList() {
        super( ResourceCenter.TYPE_CHIEF_UIM_SNAP );
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 1 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_QUEYR_UISNAP ) );
        }
    }
    
    public void addFunctionsForTable(){
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
        return SanBootView.res.getString("common.unlimitedmirrorsnap");
    }
    public String toTipString(){
        return toTreeString();
    }
}
