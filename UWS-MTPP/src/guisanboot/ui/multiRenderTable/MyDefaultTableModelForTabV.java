/*
 * MyDefaultTableModelForTabX.java
 *
 * Created on 2006/12/29, afternoon 8:56
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
public class MyDefaultTableModelForTabV extends DefaultTableModel{
    Object[] label = null;
    
    public MyDefaultTableModelForTabV(Object[] header,int colNum,Object[] _label ){
        super( header,colNum );
        label = _label;
    }
    
    public MyDefaultTableModelForTabV( Object[][] data,Object[] header,Object[] _label ){
        super( data,header );
        label = _label;
    }
        
    @Override public Object getValueAt(int row, int col){
        return super.getValueAt(row,col);
    }
    
    @Override public boolean isCellEditable(int row, int col){
        if(col==0)
            return true;
        if(col==1)
            return false;
                
        Object oneObj = getValueAt(row,0);
        if( oneObj !=null ){
            Boolean oneColSel = (Boolean)oneObj;
            if( oneColSel.booleanValue() ){
                return true;
            }else{
                return false;
            }
        }else{ // impossible to coming here 
            return false;
        }
    }   
}
