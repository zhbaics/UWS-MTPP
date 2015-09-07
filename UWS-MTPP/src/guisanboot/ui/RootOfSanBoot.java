/*
 * RootOfSanBoot.java
 *
 * Created on February 25, 2005, 9:40 AM
 */

package guisanboot.ui;

import javax.swing.*;
import java.util.ArrayList;
import mylib.UI.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class RootOfSanBoot implements GeneralInfo, TreeRevealable{
    private int type = ResourceCenter.TYPE_ROOT_INDEX;
    
    /**
     * Creates a new instance of RootOfSanBoot 
     */
    public RootOfSanBoot() {
    }
    
    ///////////////////////////////////////////////////////
    //
    //           下面的方法跟UI有关
    //
    ////////////////////////////////////////////////////////
    
    //** GeneralInfo的实现**/
    public int getType(){
        return type;
    }
    public int getIpIndex(){
        return -1;
    }
    public ArrayList getFunctionList(int type){
        return null;
    }
    public String getComment(){
        return SanBootView.res.getString("View.comment1");
    }
    
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.SMALL_ICON_SANBOOT;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.SMALL_ICON_SANBOOT;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return SanBootView.res.getString("View.tree.userobject.topnode");
    }
    public String toTipString(){
        return toTreeString();
    }
}
