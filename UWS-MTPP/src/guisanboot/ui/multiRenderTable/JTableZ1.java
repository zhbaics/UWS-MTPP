/*
 * JTableX.java
 *
 * Created on May 25, 2005, 3:29 PM
 */

package guisanboot.ui.multiRenderTable;

import javax.swing.*;
import javax.swing.table.*;
import guisanboot.ui.*;

/**
 *
 * @author  Administrator
 */
public class JTableZ1 extends JTable{
    public MyDefaultTableModelForTabZ1 model;
    private SanBootView view;
    protected RowEditorModel rm;
    
    public JTableZ1() {
        super(); 
    }
    
    public JTableZ1(
        MyDefaultTableModelForTabZ1 _model,
        SanBootView _view
    ){
        super( _model );
        model = _model;
        view = _view;
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
        if( col == 0 ){
            return new CheckBoxRender();
        }else{
            return super.getCellRenderer(row,col);
        }
    }
}
