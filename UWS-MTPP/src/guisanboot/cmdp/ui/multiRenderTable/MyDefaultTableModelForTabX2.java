/*
 * MyDefaultTableModelForTabX2.java
 *
 * Created on 2010/06/08, �PM�1:52
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.cmdp.ui.multiRenderTable;

import guisanboot.data.Volume;
import guisanboot.ui.SanBootView;
import javax.swing.table.*;

/**
 *
 * @author Administrator
 */
public class MyDefaultTableModelForTabX2 extends DefaultTableModel{
    Object[] label = null;
    
    public MyDefaultTableModelForTabX2(Object[] header,int colNum,Object[] _label ){
        super( header,colNum );
        label = _label;
    }
    
    public MyDefaultTableModelForTabX2( Object[][] data,Object[] header,Object[] _label ){
        super( data,header );
        label = _label;
    }
        
    @Override public Object getValueAt(int row, int col){
        return super.getValueAt(row,col);
    }
    
    @Override public boolean isCellEditable(int row, int col){
        if( col == 0 )
            return true;
        if( col == 1 || col == 2 )
            return false;
        
        Object oneObj = getValueAt( row,0 );  //isProtected?
        if( oneObj != null ){
            Boolean oneColSel = (Boolean)oneObj;
            if( oneColSel.booleanValue() ){
                if( col == 3 || col == 4 ){
                    Object twoObj = getValueAt( row,2 );
                    if( twoObj != null ){
                        if( twoObj instanceof Volume ){
                            return false;
                        }else{
                            return true;
                        }
                    }else{ // impossible to coming here
                        return false;
                    }
                }
                if( col == 7 || col == 8 || col == 9 || col == 10 || col ==11 || col == 12){
                    Object threeObj = getValueAt( row,3 );
                    if(threeObj != null ){
                        String aThreeObj = (String)threeObj;
                        return aThreeObj.equals( SanBootView.res.getString("SelectProtectedSysVolPane.combox.up") );
                    }  else {
                        return false ;
                    }
                }
                if( col == 13 ){
                    Object threeObj = getValueAt( row,3 );
                    if( threeObj != null ){
                        String aThreeObj = (String)threeObj;
                        return  aThreeObj.equals( SanBootView.res.getString("SelectProtectedSysVolPane.combox.lp") );
                    }else{ // impossible to coming here
                        return false;
                    }
                }
                return true;
            }else{
                if( col==0 ) 
                    return true;
                return false;
            }
        }else{ // impossible to come here 
            return false;
        }
    }   
}
