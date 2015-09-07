/*
 * ChiefVMHost.java
 *
 * Created on July 28, 2008, 1:10 PM
 */

package guisanboot.ui;

import guisanboot.MenuAndBtnCenterForMainUi;
import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ChiefVMHost extends ChiefNode implements TreeRevealable,TableRevealable{
    /** Creates a new instance of ChiefVMHost */
    public ChiefVMHost( ) {
        super( ResourceCenter.TYPE_CHIEF_VM_HOST );
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 2 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_VMHOST ));
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_VM_MACHINE ));
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 2 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_VMHOST ));
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_VMHOST ));
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
        return SanBootView.res.getString("common.vmhostlist");
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
        return SanBootView.res.getString("common.vmhostlist");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.vmhostlist");
    }
}
