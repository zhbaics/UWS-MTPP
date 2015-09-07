/*
 * LocalUnlimitedIncMirroredSnap.java
 *
 * Created on 2010/1/27, PM�1:44
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.unlimitedIncMj.entity;

import guisanboot.ui.*;
import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.Snapshot;
import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author zjj
 */
public class LocalUnlimitedIncMirroredSnap extends UnlimitedIncMirroredSnap{    
    /** Creates a new instance of LocalUnlimitedIncMirroredSnap */
    public LocalUnlimitedIncMirroredSnap() {
        super( ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP1 );
    }
    
    public LocalUnlimitedIncMirroredSnap( Snapshot snap ){
        super( ResourceCenter.TYPE_UNLIMITED_INC_MIRROR_SNAP1 );
        this.snap = snap;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 1 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CLONE_DISK ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SNAP ) );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 1 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CLONE_DISK ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SNAP ) );
        }
    }
}
