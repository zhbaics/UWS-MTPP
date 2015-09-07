/*
 * ChiefPool.java
 *
 * Created on December 8, 2004, 5:53 PM
 */

package guisanboot.ui;

import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.res.*;
import guisanboot.MenuAndBtnCenterForMainUi;

/**
 *
 * @author  Administrator
 */
public class ChiefPool extends ChiefNode implements TreeRevealable{  
    /** Creates a new instance of ChiefPool */
    public ChiefPool() {
        super( ResourceCenter.TYPE_CHIEF_POOL );
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 1 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_POOL ) );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 0 );
        }
    }
    
    /////////////////////////////////////////////////////////////////
    //
    //           下 面 的 方 法 跟 UI 有 关� ��
    //
    /////////////////////////////////////////////////////////////////
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_POOLLIST;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_POOLLIST;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return SanBootView.res.getString("common.poolList");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.poollist");
    }
}
