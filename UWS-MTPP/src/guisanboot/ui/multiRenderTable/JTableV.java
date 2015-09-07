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
public class JTableV extends JTable{
    RowEditorModel rm2; // 第2列的editor
    RowEditorModel rm0; // 第0列的editor
    RowEditorModel rm5; // 第5列的editor
    
    public MyDefaultTableModelForTabV model;
    
    public JTableV() {
        super(); 
    }
    
    public JTableV( MyDefaultTableModelForTabV _model ){
        super( _model );
        model = _model;
    }
    
    public void setCol2EditorModel(RowEditorModel rm2){
        this.rm2 = rm2;
    }
    
    public void setCol0EditorModel( RowEditorModel rm0 ){
        this.rm0 = rm0;
    }
     
    public void setCol5EditorModel( RowEditorModel rm5 ){
        this.rm5 = rm5;
    }
    
    public TableCellEditor getCellEditor(int row, int col){  
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
        }else if( col == 5 ){
            if ( rm5 != null )
                tmpEditor = rm5.getEditor( col );
            if ( tmpEditor!=null )
                return tmpEditor;
            return super.getCellEditor(row,col);
        }else{
            return super.getCellEditor(row,col);
        }
    }
     
    public TableCellRenderer getCellRenderer( int row,int col ){
        if( col == 0 || col == 5 ){
            return new CheckBoxRender( );
        }else{
            return super.getCellRenderer(row,col);
        }
     }
}
