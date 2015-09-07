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
public class JTableX extends JTable implements PaneEditorable{
    public MyDefaultTableModelForTabX model;
    private SanBootView view;
    private WizardDialogSample wizardDiag;
    protected RowEditorModel rm;
    private boolean hasEnoughSpace;

    public JTableX() {
        super(); 
    }
    
    public JTableX( MyDefaultTableModelForTabX _model,
        SanBootView _view, WizardDialogSample _wizardDiag,
        boolean hasEnoughSpace
    ){
        super( _model );
        model = _model;
        view = _view;
        wizardDiag = _wizardDiag;
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
        if( col == 0 || col == 9 ){
            return new CheckBoxRender();
        }else if( col == 3 ){
            return new PaneRenderer( this.hasEnoughSpace );
        }else{
            return super.getCellRenderer(row,col);
        }
     }
     
    public void cleanSomething(int row ){
        model.setValueAt("", row, 4);
      //  model.setValueAt("", row, 5);
    }
    
    public void setFormatFlagCol( boolean val,int row ){
        model.setValueAt( new Boolean(val),row,9 );
    }
    
    public void setNameCol( Object obj,int row ){
        model.setValueAt( obj,row, 4 );
    }
    
    public void setSizeCol( Object obj,int row ){
        model.setValueAt( obj,row, 5 );
    }
    
    public Object getNameCol( int row ){
        return model.getValueAt( row, 4 );
    }
    
    public Object getSizeCol( int row ){
        return model.getValueAt( row,5 );
    }
    
    public Object getPartitionCol( int row ){
        return model.getValueAt( row, 1 );
    }
    
    public void setLVMTypeCol( Object lvmType,int row ){
    }
    public void setSnapSpaceCol( Object snapSpace,int row ){
    }
    
    public void setBlkSizeCol( Object blk,int row ){
        model.setValueAt( blk,row, 6 );
    }
    
    public void setPoolCol( Object pool,int row ){
        model.setValueAt( pool,row, 7 );
    }
    
    public boolean isRightLVMType( String type ){
        return true;
    }
}
