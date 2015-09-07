/*
 * JTableForMjSch.java
 *
 * Created on Seq 8, 2010, 11:49 AM
 */

package guisanboot.remotemirror.ui.multiRenderTable;

import guisanboot.ui.multiRenderTable.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  Administrator
 */
public class JTableForMjSch extends JTable {
    public MyDefaultTableModelForMJSch model;
    protected RowEditorModel rm;
    
    public JTableForMjSch() {
        super(); 
    }
    
    public JTableForMjSch( MyDefaultTableModelForMJSch _model ){
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
            return super.getCellRenderer(row,col);
        }else{
            return new CheckBoxRender();
        }
    }
}
