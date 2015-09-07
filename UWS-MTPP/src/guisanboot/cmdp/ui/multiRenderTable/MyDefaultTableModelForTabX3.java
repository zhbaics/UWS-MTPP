/*
 * MyDefaultTableModelForTabX3.java
 *
 * Created on 2010/06/24, �PM�13:55
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.cmdp.ui.multiRenderTable;

import javax.swing.table.*;

/**
 *
 * @author Administrator
 */
public class MyDefaultTableModelForTabX3 extends DefaultTableModel{
    Object[] label = null;
    
    public MyDefaultTableModelForTabX3(Object[] header,int colNum,Object[] _label ){
        super( header,colNum );
        label = _label;
    }
    
    public MyDefaultTableModelForTabX3( Object[][] data,Object[] header,Object[] _label ){
        super( data,header );
        label = _label;
    }
    
    @Override public Object getValueAt(int row, int col){
        return super.getValueAt(row,col);
    }
    
    @Override public boolean isCellEditable(int row, int col){
        return false;
    }
}
