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
public class JTableZ extends JTable{
    public MyDefaultTableModelForTabZ model;
    private SanBootView view;
    private WizardDialogSample wizardDiag;
    protected RowEditorModel rm;
    private MyComboBoxEditor[] comboEditorArr;
    private MyComboBoxRender[] comboRenderArr;
    
    public JTableZ() {
        super(); 
    }
    
    public JTableZ(
        MyDefaultTableModelForTabZ _model,
        SanBootView _view, 
        WizardDialogSample _wizardDiag, 
        MyComboBoxEditor[] _comboEditorArr,
        MyComboBoxRender[] _comboRenderArr
    ){
        super( _model );
        model = _model;
        view = _view;
        wizardDiag = _wizardDiag;
        comboEditorArr = _comboEditorArr;
        comboRenderArr = _comboRenderArr;
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
        if( col == 2 ){
            return comboEditorArr[row];
        }
        return super.getCellEditor(row,col);
    }
     
    @Override public TableCellRenderer getCellRenderer( int row,int col ){
        if( col == 0 ){
            return new CheckBoxRender();
        }else if( col == 2 ){
            return comboRenderArr[row];
        }else{
            return super.getCellRenderer(row,col);
        }
    }
     
    public void updateNetCardColumn( int selectedRow ){
        model.updateNetCard( selectedRow );
    }
    
    public void cleanVolInfo(int row ){
        model.setValueAt("", row, 3);
    }
    
    public void setIPCol( Object obj,int row ){
        model.setValueAt( obj,row, 3 );
    }
    
    public Object getIPCol( int row ){
        return model.getValueAt( row, 3 );
    }
}
