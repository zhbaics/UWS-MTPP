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
import guisanboot.data.*;

/**
 *
 * @author Administrator
 */
public class MyDefaultTableModelForTabW extends DefaultTableModel{
    Object[] label = null;
    
    public MyDefaultTableModelForTabW(Object[] header,int colNum,Object[] _label ){
        super( header,colNum );
        label = _label;
    }
    
    public MyDefaultTableModelForTabW( Object[][] data,Object[] header,Object[] _label ){
        super( data,header );
        label = _label;
    }
        
    @Override public Object getValueAt(int row, int col){
        return super.getValueAt(row,col);
    }
    
    @Override public boolean isCellEditable(int row, int col){
        if(col==0)
            return true;
        if(col==1 || col==2 )
            return false;
            
        Object oneObj = getValueAt(row,0);  //isProtected?
        if( oneObj !=null ){
//System.out.println("0000000000000000  HAVE VALUESSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            Boolean oneColSel = (Boolean)oneObj;
            if( oneColSel.booleanValue() ){       
                if( col == 4 || col == 5 || col == 6 || col == 7){
                    Object threeObj = getValueAt(row,3);
                    if( threeObj !=null ){
//System.out.println("2222222222222     HAVE VALUESSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                        Boolean threeColSel = (Boolean)threeObj;
                        if( threeColSel.booleanValue() ){
                            return true;
                        }else{
                            Object fourObj = getValueAt(row, 4);
                            if( fourObj instanceof LVWrapper ){
                                return false;
                            }else{
                                if( col == 4 ){
                                    return true;
                                }else{
                                    return false;
                                }
                            }
                        }
                    }else{ // impossible to coming here 
                        return false;
                    }
                }else if( col == 8 || col == 9 ){
                    Object fourObj = getValueAt(row, 4);
                    if( fourObj instanceof LVWrapper ){
                        return false;
                    }else{
                        return true; 
                    }
                }        
                return true;
            }else{
                if( col==0 ) 
                    return true;
                return false;
            }
        }else{ // impossible to coming here 
            return false;
        }
    }   
}
