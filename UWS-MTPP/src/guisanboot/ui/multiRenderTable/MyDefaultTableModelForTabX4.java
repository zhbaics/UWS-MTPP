/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.ui.multiRenderTable;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class MyDefaultTableModelForTabX4 extends DefaultTableModel{
    Object[] label = null;
    
    public MyDefaultTableModelForTabX4(Object[] header,int colNum,Object[] _label ){
        super( header,colNum );
        label = _label;
    }
    
    public MyDefaultTableModelForTabX4( Object[][] data,Object[] header,Object[] _label ){
        super( data,header );
        label = _label;
    }
        
    @Override public Object getValueAt(int row, int col){
        return super.getValueAt(row,col);
    }
    
    @Override public boolean isCellEditable(int row, int col){
        if( col == 0 ){
            return true;
        } else {
            return false;
        }
    }  
}
