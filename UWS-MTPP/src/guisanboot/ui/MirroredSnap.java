/*
 * MirroredSnap.java
 *
 * Created on 2008/6/19, PM�9:54
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.Snapshot;
import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.BasicUIObject;

/**
 *
 * @author zjj
 */
public class MirroredSnap extends BasicUIObject{
    public Snapshot snap;
    
    /** Creates a new instance of MirroredSnap */
    public MirroredSnap() {
        super( ResourceCenter.TYPE_MIRROR_SNAP );
    }
    
    public MirroredSnap( Snapshot snap ){
        super( ResourceCenter.TYPE_MIRROR_SNAP );
        this.snap = snap;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 1 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_VIEW ) );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 1 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_VIEW ) );
        }
    }
    
    @Override public String toString(){
        return SanBootView.res.getString("common.snap") + " : " + snap.getCreateTimeStr();
    }
    
    @Override public String getComment(){
        return toString();
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
        return ResourceCenter.ICON_SNAP_16;
    }
    public String toTableString(){
        return snap.getSnap_root_id()+"."+snap.getSnap_local_snapid();
    }
  
    //** TreeRevealable的实现**/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_SNAP_16;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_SNAP_16;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return snap.getSnap_root_id()+"."+snap.getSnap_local_snapid();
    }
    public String toTipString(){
        return toTreeString()+" [ "+this.snap.toString()+" | "+this.snap.getCreateTimeStr()+" ]";
    }

    public String printMe(){
        return toString();
    }
}
