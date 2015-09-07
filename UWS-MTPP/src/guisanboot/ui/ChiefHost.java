/*
 * ChiefHost.java
 *
 * Created on December 8, 2004, 6:10 PM
 */

package guisanboot.ui;

import guisanboot.MenuAndBtnCenterForMainUi;
import javax.swing.*;
import java.util.*;

import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class ChiefHost extends ChiefNode implements TreeRevealable,TableRevealable{    
    /** Creates a new instance of ChiefHost */
    public ChiefHost( ) {
            super( ResourceCenter.TYPE_CHIEF_HOST_INDEX );
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 6 ); //fsForTree = new ArrayList<Integer>( 4 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_INIT ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN_AMS) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN_LVM) ); //linux lvm保护

            //fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            //fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CLUSTER_PHY_WIN_INIT));
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 0 );
        }
    }
    
    /////////////////////////////////////////////////////////////////
    //
    //         下 面 的 方 法 跟 UI 有 关��
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
        return SanBootView.res.getString("common.hostlist");
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
        return SanBootView.res.getString("common.hostlist");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.hostList");
    }
}
