/*
 * MyDefaultTableModelForTabX1.java
 *
 * Created on 2008/8�/�20, ��morning��11:46
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui.multiRenderTable;

import javax.swing.table.*;

/**
 *
 * @author Administrator
 */
public class MyDefaultTableModelForTabX1 extends DefaultTableModel{
    Object[] label = null;
    
    public MyDefaultTableModelForTabX1(Object[] header,int colNum,Object[] _label ){
        super( header,colNum );
        label = _label;
    }
    
    public MyDefaultTableModelForTabX1( Object[][] data,Object[] header,Object[] _label ){
        super( data,header );
        label = _label;
    }
    
    @Override public Object getValueAt(int row, int col){
        return super.getValueAt(row,col);
    }
    
    @Override public boolean isCellEditable(int row, int col){
        if( col==0 ){
            return true;
        }else{
            return false;
        }
    }
}
