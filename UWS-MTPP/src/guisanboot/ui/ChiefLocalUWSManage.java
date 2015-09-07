/*
 * ChiefLocalUWSManage.java
 *
 * Created on 2008/6/18, 12:57 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.res.ResourceCenter;
import javax.swing.Icon;
import javax.swing.JLabel;
import java.util.ArrayList;
import mylib.UI.TableRevealable;
import mylib.UI.TreeRevealable;

/**
 *
 * @author zjj
 */
public class ChiefLocalUWSManage extends ChiefNode implements TreeRevealable,TableRevealable {
    /** Creates a new instance of ChiefLocalUWSManage */
    public ChiefLocalUWSManage() {
        super( ResourceCenter.TYPE_LOCAL_UWS_MANAGE );  
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>(0);
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>(0);
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
        return SanBootView.res.getString("common.localSWU");
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
        return SanBootView.res.getString("common.localSWU");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.localSWU");
    }
}
