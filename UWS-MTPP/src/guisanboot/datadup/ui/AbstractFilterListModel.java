/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.datadup.ui;

import javax.swing.DefaultListModel;

/**
 * AbstractFilterListModel.java
 *
 * Created on 2011-5-17, 17:44:29
 */
public abstract class AbstractFilterListModel extends DefaultListModel {
    private boolean isFilter = false;
    public abstract boolean canAddInto( Object obj );
    public AbstractFilterListModel(){
        super();
    }

    public void setFilterFlag( boolean val ){
        this.isFilter = val;
    }
    public boolean isFilterable(){
        return this.isFilter;
    }
    
    @Override public void addElement( Object obj ){
        if( isFilter ){
            if( this.canAddInto( obj ) ){
                super.addElement( obj );
            }
        }else{
            super.addElement( obj );
        }
    }
}
