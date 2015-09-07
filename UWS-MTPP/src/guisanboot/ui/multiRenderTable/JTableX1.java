/*
 * JTableX1.java
 *
 * Created on Aug 20, 2008, 11:41 AM
 */

package guisanboot.ui.multiRenderTable;

import javax.swing.*;
import javax.swing.table.*;
import guisanboot.ui.*;

/**
 *
 * @author  Administrator
 */
public class JTableX1 extends JTable{
    public MyDefaultTableModelForTabX1 model;
    private SanBootView view;
    private WizardDialogSample wizardDiag;
    protected RowEditorModel rm;
    
    public JTableX1() {
        super(); 
    }
    
    public JTableX1( MyDefaultTableModelForTabX1 _model,SanBootView _view, WizardDialogSample _wizardDiag ){
        super( _model );
        model = _model;
        view = _view;
        wizardDiag = _wizardDiag;
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
    
    public void cleanSomething(int row ){
        model.setValueAt("", row, 4);
    }
}
