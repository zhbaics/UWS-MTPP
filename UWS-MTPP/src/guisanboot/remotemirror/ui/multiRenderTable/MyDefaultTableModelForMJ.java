/*
 * MyDefaultTableModelForMJ.java
 *
 * Created on 2010/08/18, �PM�14:47
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
public class MyDefaultTableModelForMJ extends DefaultTableModel{
    public MyDefaultTableModelForMJ(Object[] header,int colNum ){
        super( header,colNum );
    }
    
    public MyDefaultTableModelForMJ( Object[][] data,Object[] header ){
        super( data,header );
    }
    
    @Override public Object getValueAt( int row, int col ){
        return super.getValueAt( row,col );
    }
    
    @Override public boolean isCellEditable(int row, int col){
        if( col == 0 )
            return true;
        else
            return false;
    }   
}
