/*
 * JTableX.java
 *
 * Created on May 25, 2005, 3:29 PM
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.table.*;
import guisanboot.ui.multiRenderTable.RowEditorModel;

/**
 *
 * @author  Administrator
 */
public class JTableU extends JTable{
    public MyDefaultTableModelForTabU model; 
    protected RowEditorModel rm; 

     public JTableU(){
         super();
         rm = null;
     }

    public JTableU( MyDefaultTableModelForTabU _model ){ 
        super( _model );
        model = _model;
    }

     public void setRowEditorModel(RowEditorModel rm){ 
         this.rm = rm;
     }

     public RowEditorModel getRowEditorModel(){
         return rm;
     }

     public TableCellEditor getCellEditor(int row, int col){
         TableCellEditor tmpEditor = null;
         if (rm!=null)
             tmpEditor = rm.getEditor(row);
         if (tmpEditor!=null)
             return tmpEditor;
         return super.getCellEditor(row,col);
     }
     
     public TableCellRenderer getCellRenderer( int row,int col ){
        return super.getCellRenderer(row,col);
     }
}
