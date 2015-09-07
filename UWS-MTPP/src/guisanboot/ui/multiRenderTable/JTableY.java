/*
 * JTableY.java
 *
 * Created on May 25, 2005, 3:29 PM
 */

package guisanboot.ui.multiRenderTable;

import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  Administrator
 */
public class JTableY extends JTable{
    RowEditorModel rm2; // 第2列的editor
    RowEditorModel rm0; // 第2列的editor
    RowEditorModel rm4; // 第4列的editor
    
    public MyDefaultTableModelForTabY model;
    
    public JTableY() {
        super(); 
    }
    
    public JTableY( MyDefaultTableModelForTabY _model ){
        super( _model );
        model = _model;
    }
    
    public void setCol2EditorModel(RowEditorModel rm2){
        this.rm2 = rm2;
    }
    
    public void setCol0EditorModel( RowEditorModel rm0 ){
        this.rm0 = rm0;
    }
     
    public void setCol4EditorModel( RowEditorModel rm4 ){
        this.rm4 = rm4;
    }

    @Override public TableCellEditor getCellEditor(int row, int col){  
        TableCellEditor tmpEditor = null;
        
        if( col == 0 ){
            if( rm0 != null )
                tmpEditor = rm0.getEditor( col );
            if( tmpEditor!=null )
                return tmpEditor;
            return super.getCellEditor(row,col);
        }else if( col == 2 ){      
            if ( rm2 != null )
                tmpEditor = rm2.getEditor(row);
            if ( tmpEditor!=null )
                return tmpEditor;
            return super.getCellEditor(row,col);
        }else if( col == 4 ){
            if ( rm4 != null )
                tmpEditor = rm4.getEditor( col );
            if ( tmpEditor!=null )
                return tmpEditor;
            return super.getCellEditor(row,col);
        }else{
            return super.getCellEditor(row,col);
        }
    }
     
    @Override public TableCellRenderer getCellRenderer( int row,int col ){
        if( col == 0 || col == 4 ){
            return new CheckBoxRender( );
        }else{
            return super.getCellRenderer(row,col);
        }
     }
}
