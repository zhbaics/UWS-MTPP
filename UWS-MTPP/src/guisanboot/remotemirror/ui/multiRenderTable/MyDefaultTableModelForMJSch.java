/*
 * MyDefaultTableModelForMJSch.java
 *
 * Created on 2010/09/8, �AM�11:50
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
public class MyDefaultTableModelForMJSch extends DefaultTableModel{
    public MyDefaultTableModelForMJSch(Object[] header,int colNum ){
        super( header,colNum );
    }
    
    public MyDefaultTableModelForMJSch( Object[][] data,Object[] header ){
        super( data,header );
    }
    
    @Override public Object getValueAt( int row, int col ){
        return super.getValueAt( row,col );
    }
    
    @Override public boolean isCellEditable(int row, int col){
        if( col == 0 )
            return false;
        else
            return true;
    }   
}
