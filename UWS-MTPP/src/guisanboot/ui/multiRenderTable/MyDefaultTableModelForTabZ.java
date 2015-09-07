/*
 * MyDefaultTableModelForTabZ.java
 *
 * Created on 2006/12/29, �afternoon�8:56
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui.multiRenderTable;

import guisanboot.data.NetCard;
import guisanboot.data.UnixNetCard;
import javax.swing.table.*;

/**
 *
 * @author Administrator
 */
public class MyDefaultTableModelForTabZ extends DefaultTableModel{
    Object[] label = null;
    
    public MyDefaultTableModelForTabZ(Object[] header,int colNum,Object[] _label ){
        super( header,colNum );
        label = _label;
    }
    
    public MyDefaultTableModelForTabZ( Object[][] data,Object[] header,Object[] _label ){
        super( data,header );
        label = _label;
    }
        
    @Override public Object getValueAt(int row, int col){
        return super.getValueAt(row,col);
    }
    
    @Override public boolean isCellEditable(int row, int col){
        if( col==0 ) return true;
        if( col == 1 || col == 3 ) return false;
        
        Object oneObj = getValueAt(row,0);
        if( oneObj != null ){
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
    
    public void updateNetCard( int selectedRow ){
        int r,rowCnt;
        Boolean isSel;
        Object one;
        NetCard nc,selnc=null;
        UnixNetCard unc,selunc=null;
        rowCnt = getRowCount();

        Object selOne = super.getValueAt( selectedRow, 1 );
        if( selOne instanceof NetCard ){
            selnc  = (NetCard)selOne;
        }else{
            selunc = (UnixNetCard)selOne;
        }
        for( r=0; r<rowCnt; ++r ){
            one = super.getValueAt( r, 1 );
            isSel = (Boolean)super.getValueAt( r, 0 );
            if( one instanceof NetCard ){
                nc = (NetCard)one;
                if( !nc.host_ip.equals( selnc.host_ip ) ){
                    super.setValueAt( isSel, r, 0 );
                    continue;
                }
            }else{
                unc =(UnixNetCard)one;
                if( !unc.host_ip.equals( selunc.host_ip ) ){
                    super.setValueAt( isSel, r, 0 );
                    continue;
                }
            }

            super.setValueAt( new Boolean( r == selectedRow ), r, 0 ); 
        }

        for( r=0; r<rowCnt; ++r ){
            fireTableCellUpdated( r, 0 );
        }
    }
}
