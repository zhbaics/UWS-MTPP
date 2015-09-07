/*
 * RowEditorModel.java
 *
 * Created on May 25, 2005, 3:31 PM
 */

package guisanboot.ui.multiRenderTable;

import javax.swing.table.*;
import java.util.*;

/**
 *
 * @author  Administrator
 */
public class RowEditorModel {
    private Hashtable<Integer,TableCellEditor> data;
    
    public RowEditorModel(){
        data = new Hashtable<Integer,TableCellEditor>();
    }
    public void addEditorForRow(int row,TableCellEditor e ){
        data.put(new Integer(row), e);
    }
    public void removeEditorForRow( int row ){
        data.remove(new Integer(row));
    }
    public TableCellEditor getEditor(int row ){
        return data.get(new Integer(row));
    }
}
