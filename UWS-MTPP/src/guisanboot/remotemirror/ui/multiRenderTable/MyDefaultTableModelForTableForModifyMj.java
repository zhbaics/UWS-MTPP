/*
 * MyDefaultTableModelForTableForModifyMj.java
 *
 * Created on 2010/09/01,11:12 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.remotemirror.ui.multiRenderTable;

import javax.swing.table.*;

/**
 *
 * @author Administrator
 */
public class MyDefaultTableModelForTableForModifyMj extends DefaultTableModel{
    Object[] label = null;
    
    public MyDefaultTableModelForTableForModifyMj(Object[] header,int colNum,Object[] _label ){
        super( header,colNum );
        label = _label;
    }
    
    public MyDefaultTableModelForTableForModifyMj( Object[][] data,Object[] header,Object[] _label ){
        super( data,header );
        label = _label;
    }

    public MyDefaultTableModelForTableForModifyMj( Object[][] data,Object[] header ){
        super( data,header );
    }
        
    @Override public Object getValueAt(int row, int col){
        return super.getValueAt(row,col);
    }
    
    @Override public boolean isCellEditable(int row, int col){
        if( col == 1 || col == 3 ) return false;
        return true;
    }
}
