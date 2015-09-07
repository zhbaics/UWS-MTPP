/*
 * ChiefMJManage.java
 *
 * Created on 2008/6/20, 3:35 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.MenuAndBtnCenterForMainUi;
import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author Administrator
 */
public class ChiefMJManage extends ChiefNode implements TreeRevealable,TableRevealable{
    /** Creates a new instance of ChiefMJManage */
    public ChiefMJManage(){
        super( ResourceCenter.TYPE_CHIEF_MJ_MG );
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            // 在树上点击ChiefMjManage,不便增加mj,因为不知道对哪个卷增加mj.
            // 以后考虑扩展"增加mg窗口"，可以在其上选择一个卷
            fsForTree = new ArrayList<Integer>( 5 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_BATCH_CREATE_MJ ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_BATCH_MOD_MJ ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_BATCH_START_STOP_MJ ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SETUP_MJ_SCH ) );
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
        return SanBootView.res.getString("common.mjlist");
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
        return SanBootView.res.getString("common.mjlist");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.mjlist");
    }
}
