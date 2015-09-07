/*
 * TableForCreateMj.java
 *
 * Created on Aug 27, 2010, 5:18 PM
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
public class TableForCreateMj extends JTable{
    public MyDefaultTableModelForTableForCreateMj model;
    protected RowEditorModel rm;
    protected ArrayList<UWSrvNode> list;

    public TableForCreateMj() {
        super(); 
    }
    
    public TableForCreateMj(
        MyDefaultTableModelForTableForCreateMj _model,
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
        }else if( col == 5 ){
            return new MirrorJobRemoteUWSPaneRenderer( list );
        }else if( col == 6 ){
            return new MirrorJobOptionPaneRenderer();
        }else{
            return super.getCellRenderer( row,col );
        }
    }
}
