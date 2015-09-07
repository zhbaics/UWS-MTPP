/*
 * JTableX3.java
 *
 * Created on June 24, 2010, 13:53 PM
 */

package guisanboot.cmdp.ui.multiRenderTable;

import guisanboot.ui.multiRenderTable.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  Administrator
 */
public class JTableX3 extends JTable{
    public MyDefaultTableModelForTabX3 model;
   
    protected RowEditorModel rm;
    
    public JTableX3() {
        super(); 
    }
    
    public JTableX3( MyDefaultTableModelForTabX3 _model){
        super( _model );
        model = _model;
    }
    
    public void setRowEditorModel(RowEditorModel rm){
         this.rm = rm;
    }
     
    @Override public TableCellEditor getCellEditor(int row, int col){
        TableCellEditor tmpEditor = null;
        if (rm!=null)
            tmpEditor = rm.getEditor(col);
        if (tmpEditor!=null)
            return tmpEditor;
        return super.getCellEditor(row,col);
     }
     
    @Override public TableCellRenderer getCellRenderer( int row,int col ){
        return super.getCellRenderer(row,col);
    }
}
