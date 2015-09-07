/*
 * CloneDiskWrapper.java
 *
 * Created on 2009/12/31,PM 2:42
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.unlimitedIncMj.entity;

import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;

/**
 *
 * @author Administrator
 */
public class CloneDiskWrapper {
    private int type;
    public CloneDisk cloneDisk;
    
    @Override public String toString(){
        if( type == 0 ){
            return cloneDisk.getLabel();
        }else{
            return SanBootView.res.getString("common.cdisk") + " : " + cloneDisk.getCrtTimeString()+
                    " ["+cloneDisk.getTarget_id()+"]";
        }
    }
    
    /** Creates a new instance of CloneDiskWrapper */
    public CloneDiskWrapper() {
        this( 0 );
    }
    
    public CloneDiskWrapper( int type ){
        this.type = type;
    }
}
