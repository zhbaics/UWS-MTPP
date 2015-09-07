/*
 * ChiefMJScheduler.java
 *
 * Created on 2010/9/7, 4:31 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.remotemirror;

import guisanboot.ui.*;
import guisanboot.MenuAndBtnCenterForMainUi;
import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author Administrator
 */
public class ChiefMJScheduler extends ChiefNode implements TreeRevealable,TableRevealable{
    /** Creates a new instance of ChiefMJScheduler */
    public ChiefMJScheduler(){
        super( ResourceCenter.TYPE_CHIEF_MJ_SCHEDULER );
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 1 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_MJ_SCH ) );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 0 );
        }
    }
    
    /////////////////////////////////////////////////////////////////
    //
    //              下 面 的 方 法 跟 UI 有 关
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
        return SanBootView.res.getString("common.mjSchList");
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
        return SanBootView.res.getString("common.mjSchList");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.mjSchList");
    }
}
