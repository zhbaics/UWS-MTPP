/*
 * JTableForSetupMjSch.java
 *
 * Created on Seq 9, 2010, 12:53 PM
 */

package guisanboot.remotemirror.ui.multiRenderTable;

import guisanboot.ui.multiRenderTable.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  Administrator
 */
public class JTableForSetupMjSch extends JTable {
    public MyDefaultTableModelForTableForSetupMjSch model;
    protected RowEditorModel rm;
    private ArrayList mjSchList;
    
    public JTableForSetupMjSch() {
        super(); 
    }
    
    public JTableForSetupMjSch( ArrayList mjSchList,MyDefaultTableModelForTableForSetupMjSch _model ){
        super( _model );
        model = _model;
        this.mjSchList = mjSchList;
    }
    
    public void setRowEditorModel(RowEditorModel rm){
         this.rm = rm;
    }
     
    @Override public TableCellEditor getCellEditor(int row, int col){
        TableCellEditor tmpEditor = null;
        if ( rm != null )
            tmpEditor = rm.getEditor(col);
        if ( tmpEditor != null )
            return tmpEditor;
        return super.getCellEditor(row,col);
    }

    @Override public TableCellRenderer getCellRenderer( int row,int col ){
        if( col == 1 ){
            return super.getCellRenderer(row,col);
        }else if( col == 0 ){
            return new CheckBoxRender();
        }else{
            return new MirrorJobSchPaneRenderer( mjSchList );
        }
    }
}
