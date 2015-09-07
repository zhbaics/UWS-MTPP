/*
 * MyDefaultTableModelForTabX.java
 *
 * Created on 2006/12/29,��PM 8:56
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import javax.swing.table.*;

/**
 *
 * @author Administrator
 */
public class MyDefaultTableModelForTabU extends DefaultTableModel{    
    public MyDefaultTableModelForTabU( Object[][] data,Object[] header ){
        super( data,header );
    }
        
    @Override public Object getValueAt(int row, int col){
        return super.getValueAt(row,col);
    }
    
    @Override public boolean isCellEditable(int row, int col){
        if(col==0)
            return false;
        if( col==1 ){
            if( row == 7 )
                return false;
            else
                return true;
        }
        return false;
    }   
}
