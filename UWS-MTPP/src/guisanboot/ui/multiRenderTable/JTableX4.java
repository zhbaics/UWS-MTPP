/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.ui.multiRenderTable;

import guisanboot.ui.SanBootView;
import guisanboot.ui.multiRenderTable.CheckBoxRender;
import guisanboot.ui.multiRenderTable.PaneEditorable;
import guisanboot.ui.multiRenderTable.RowEditorModel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Administrator
 */
public class JTableX4 extends JTable implements PaneEditorable{
    public MyDefaultTableModelForTabX4 model ;
    protected RowEditorModel rm;
    public JTableX4(){
        super();
    }
    
    public JTableX4(MyDefaultTableModelForTabX4 _model ,SanBootView _view){
        super(_model);
        model = _model;
    }

    public void setRowEditorModel( RowEditorModel rm ){
         this.rm = rm;
    }
    
    @Override public TableCellEditor getCellEditor(int row, int col){
        TableCellEditor tmpEditor = null;
        if ( rm != null )
            tmpEditor = rm.getEditor( col );
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
    
     public void setSizeCol( Object obj,int row ){
    }

    public void setFormatFlagCol( boolean val,int row ){
    }

    public Object getSizeCol( int row ){
        return null;
    }

    public void cleanSomething(int row ){
       
    }
    
    public void setNameCol( Object obj,int row ){
        
    }
    
    public Object getNameCol( int row ){
        return null;
    }

    public Object getPartitionCol( int row ){
        return null;
    }
    
    public void setLVMTypeCol( Object lvmType,int row ){
    }
    public void setSnapSpaceCol( Object snapSpace,int row ){
    }
    
    public void setBlkSizeCol( Object blk,int row ){
       
    }
    
    public void setPoolCol( Object pool,int row ){
        
    }
    
    public boolean isRightLVMType( String type ){
        return true;
    }
    
}
