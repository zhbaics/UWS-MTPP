/*
 * ChiefDestUWS.java
 *
 * Created on June 10, 2008, 5:53 PM
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
public class ChiefDestUWS extends ChiefNode implements TreeRevealable{     
    /** Creates a new instance of ChiefDestUWS */
    public ChiefDestUWS() {
        super( ResourceCenter.TYPE_CHIEF_DEST_UWS );
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 1 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_UWS_SRV ) );
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
    //** TreeRevealable�的实现�*/
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
        return SanBootView.res.getString("common.swulist");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.destSWU");
    }
}
