/*
 * ChiefCopyJobList.java
 *
 * Created on Janu 25, 2010, 5:27 PM
 */

package guisanboot.unlimitedIncMj.entity;

import guisanboot.ui.*;
import javax.swing.*;
import java.util.ArrayList;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ChiefCopyJobList extends ChiefMirrorJobList{
    /** Creates a new instance of ChiefCopyJobList */
    public ChiefCopyJobList() {
        super( ResourceCenter.TYPE_CHIEF_COPY_JOB );
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
        return SanBootView.res.getString("common.cplist");
    }
    public String toTipString(){
        return toTreeString();
    }
}
