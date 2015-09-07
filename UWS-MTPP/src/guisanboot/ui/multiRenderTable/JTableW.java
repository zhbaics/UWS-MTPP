/*
 * JTableX.java
 *
 * Created on May 25, 2005, 3:29 PM
 */

package guisanboot.ui.multiRenderTable;

import javax.swing.*;
import javax.swing.table.*;
import java.util.Vector;

import guisanboot.ui.*;

/**
 *
 * @author  Administrator
 */
public class JTableW extends JTable implements PaneEditorable{
    public MyDefaultTableModelForTabW model;
    private SanBootView view;
    private WizardDialogSample wizardDiag;
    protected RowEditorModel rm;
    private String text;
    private String[] capList;
    private Vector lvTypeList;
    private boolean hasEnoughSpace;

    public JTableW() {
        super(); 
    }
    
    public JTableW( 
        MyDefaultTableModelForTabW _model,
        SanBootView _view, 
        WizardDialogSample _wizardDiag,
        String _text,
        String[] _capList,
        Vector _lvTypeList,
        boolean hasEnoughSpace
    ){
        super( _model );
        model = _model;
        view = _view;
        text = _text;
        wizardDiag = _wizardDiag;
        capList = _capList;
        lvTypeList = _lvTypeList;
        this.hasEnoughSpace = hasEnoughSpace;
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
        if( col == 0 || col == 11 ){
            return new CheckBoxRender();
        }else if( col == 3 ){
            return new PaneRenderer( hasEnoughSpace );
        }else if( col == 8 ){
            return new MyComboBoxRender( text );
        }else{
            return super.getCellRenderer(row,col);
        }
     }
     
    public void cleanSomething(int row ){
        model.setValueAt("", row, 4);
        model.setValueAt(capList[row], row, 5);
    }
    
    public void setFormatFlagCol(boolean val,int row ){
        model.setValueAt( new Boolean(val),row,11 );
    }
    
    public void setNameCol( Object obj,int row ){
        model.setValueAt( obj,row, 4 );
    }
    
    public void setSizeCol( Object obj,int row ){
        model.setValueAt( obj,row, 5 );
    }
    
    public Object getSizeCol( int row ){
        return model.getValueAt( row,5 );
    }
      
    public Object getNameCol( int row ){
        return model.getValueAt( row, 4 );
    }
    public Object getPartitionCol( int row ){
        return model.getValueAt( row, 1 );
    }
    
    public void setBlkSizeCol( Object blk,int row ){
        model.setValueAt( blk,row,6);
    }
    
    public void setPoolCol( Object pool,int row ){
        model.setValueAt( pool,row,7 );
    }
     
    public void setLVMTypeCol( Object lvmType,int row ){
        model.setValueAt( lvmType,row,8 );
    }
    public void setSnapSpaceCol( Object snapSpace,int row ){
        model.setValueAt( snapSpace,row, 9 );
    }
    
    public boolean isRightLVMType( String type ){
        return true;
    }
}
