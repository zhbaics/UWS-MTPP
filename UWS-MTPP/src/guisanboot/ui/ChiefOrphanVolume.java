/*
 * ChiefOrphanVolume.java
 *
 * Created on 2006/12/22, 3:35 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.res.*;
import guisanboot.MenuAndBtnCenterForMainUi;

/**
 *
 * @author Administrator
 */
public class ChiefOrphanVolume extends ChiefNode implements TreeRevealable,TableRevealable{
    /** Creates a new instance of ChiefOrphanVolume */
    public ChiefOrphanVolume(){
        super( ResourceCenter.TYPE_CHIEF_ORPHAN_VOL );
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 1 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_VOL ) );
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
        return SanBootView.res.getString("common.orphanvollist");
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
        return SanBootView.res.getString("common.orphanvollist");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.orphVolList");
    }
}
