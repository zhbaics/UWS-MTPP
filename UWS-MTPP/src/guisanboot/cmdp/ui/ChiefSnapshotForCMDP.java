/*
 * ChiefSnapshotForCMDP.java
 *
 * Created on July 2, 2010, 11:36 AM
 */

package guisanboot.cmdp.ui;

import guisanboot.ui.*;
import java.util.ArrayList;
import guisanboot.res.*;
import guisanboot.MenuAndBtnCenterForMainUi;

/**
 *
 * @author  Administrator
 */
public class ChiefSnapshotForCMDP extends ChiefSnapshot{
    boolean isOsVol = false;

    /** Creates a new instance of ChiefSnapshotForCMDP */
    public ChiefSnapshotForCMDP() {
        super( ResourceCenter.TYPE_CHIEF_SNAP_INDEX );
    }

    public ChiefSnapshotForCMDP( boolean isOsVol ) {
        this();
        this.isOsVol = isOsVol;
    }
    
    @Override public void addFunctionsForTree(){
        if( fsForTree == null ){
            if( this.isOsVol ){
                fsForTree = new ArrayList<Integer>( 1 );
            }else{
                fsForTree = new ArrayList<Integer>( 2 );
            }

            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_SYNC_SNAP ) );
            if( !this.isOsVol ){
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CRT_ASYNC_SNAP ) );
            }
        }
    }
}
