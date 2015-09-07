/*
 * JTableForMJ.java
 *
 * Created on Aug 18, 2010, 14:44 PM
 */

package guisanboot.remotemirror.ui.multiRenderTable;

import guisanboot.ui.multiRenderTable.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  Administrator
 */
public class JTableForMJ extends JTable {
    public MyDefaultTableModelForMJ model;
    protected RowEditorModel rm;
    
    public JTableForMJ() {
        super(); 
    }
    
    public JTableForMJ( MyDefaultTableModelForMJ _model ){
        super( _model );
        model = _model;
    }
    
    public void setRowEditorModel(RowEditorModel rm){
         this.rm = rm;
    }
     
    @Override public TableCellEditor getCellEditor(int row, int col){
        TableCellEditor tmpEditor = null;
        if ( rm != null )
            tmpEditor = rm.getEditor(col);
        if ( tmpEditor != null )
            return tmpEditor;
        return super.getCellEditor(row,col);
    }
     
    @Override public TableCellRenderer getCellRenderer( int row,int col ){
        if( col == 0 ){
            return new CheckBoxRender();
        }else{
            return super.getCellRenderer(row,col);
        }
    }
}
