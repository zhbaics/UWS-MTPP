

package guisanboot.datadup.ui.viewobj;

import javax.swing.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.res.*;
import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.ui.ChiefNode;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class ChiefADScheduler extends ChiefNode implements TreeRevealable,TableRevealable{     
    /** Creates a new instance of ChiefScheduler */
    public ChiefADScheduler( ) {
        super( ResourceCenter.TYPE_CHIEF_ADSCH );
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 1 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ADD_ADSCH ) );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 0 );
        }
    }

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
        return SanBootView.res.getString("common.adschlist");
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
        return SanBootView.res.getString("common.adschlist");
    }
    public String toTipString(){
        return SanBootView.res.getString("common.desc.adschList");
    }
}
