/*
 * TableForModifyMj.java
 *
 * Created on Seq 1, 2010, 11:11 AM
 */

package guisanboot.remotemirror.ui.multiRenderTable;

import guisanboot.data.UWSrvNode;
import guisanboot.ui.multiRenderTable.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  Administrator
 */
public class TableForModifyMj extends JTable{
    public MyDefaultTableModelForTableForModifyMj model;
    protected RowEditorModel rm;
    protected ArrayList<UWSrvNode> list;

    public TableForModifyMj() {
        super(); 
    }
    
    public TableForModifyMj(
        MyDefaultTableModelForTableForModifyMj _model,
        ArrayList<UWSrvNode> list
    ){
        super( _model );
        model = _model;
        this.list = list;
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
        if( col == 0  ){
            return new CheckBoxRender();
        }else if( col == 4 ){
            return new MirrorJobRemoteUWSPaneRenderer( list );
        }else if( col == 5 ){
            return new MirrorJobOptionPaneRenderer();
        }else{
            return super.getCellRenderer( row,col );
        }
    }
}
