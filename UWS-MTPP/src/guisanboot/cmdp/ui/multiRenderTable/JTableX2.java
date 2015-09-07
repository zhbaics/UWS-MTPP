/*
 * JTableX2.java
 *
 * Created on June 8, 2010, 1:42 PM
 */

package guisanboot.cmdp.ui.multiRenderTable;

import guisanboot.ui.multiRenderTable.*;
import javax.swing.*;
import javax.swing.table.*;
import guisanboot.ui.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class JTableX2 extends JTable implements PaneEditorable{
    public MyDefaultTableModelForTabX2 model;
    protected RowEditorModel rm;
    protected RowEditorModel rmFor4Col;
    private boolean isCluster;
    private ArrayList vipList;
    private ArrayList pubIPList;

    public JTableX2() {
        super(); 
    }
    
    public JTableX2( MyDefaultTableModelForTabX2 _model,SanBootView _view, WizardDialogSample _wizardDiag ){
        super( _model );
        model = _model;
    }

    public void setIsCluster( boolean val ){
        this.isCluster = val;
    }

    public void setVipList( ArrayList vipList ){
        this.vipList = vipList;
    }

    public void setPubIPList( ArrayList pubIPList ){
        this.pubIPList = pubIPList;
    }
    
    public void setRowEditorModel( RowEditorModel rm ){
         this.rm = rm;
    }

    public void set4ColRowEditorModel( RowEditorModel rm ){
        this.rmFor4Col = rm;
    }
    
    @Override public TableCellEditor getCellEditor(int row, int col){
        TableCellEditor tmpEditor = null;
        if( col != 4 ){
            if ( rm != null )
                tmpEditor = rm.getEditor( col );
        }else{
            if( this.rmFor4Col != null )
                tmpEditor = rmFor4Col.getEditor( row );
        }
        if ( tmpEditor != null )
            return tmpEditor;
        return super.getCellEditor(row,col);
    }
     
    @Override public TableCellRenderer getCellRenderer( int row,int col ){
        if( this.isCluster ){
            if( col == 0 || col == 14 ){
                return new CheckBoxRender();
            }else if( col == 4 ){
                return new PaneRenderer2( vipList,pubIPList );
            }else{
                return super.getCellRenderer(row,col);
            }
        }else{
            if( col == 0 || col == 13 ){
                return new CheckBoxRender();
            }else{
                return super.getCellRenderer(row,col);
            }
        }
    }

    public void setSizeCol( Object obj,int row ){
    }

    public void setFormatFlagCol( boolean val,int row ){
    }

    public Object getSizeCol( int row ){
        return null;
    }

    public void cleanSomething(int row ){
        model.setValueAt( "", row, 2 );
    }
    
    public void setNameCol( Object obj,int row ){
        model.setValueAt( obj,row, 2 );
    }
    
    public Object getNameCol( int row ){
        return model.getValueAt( row, 2 );
    }

    public Object getPartitionCol( int row ){
        return model.getValueAt( row, 1 );
    }
    
    public void setLVMTypeCol( Object lvmType,int row ){
    }
    public void setSnapSpaceCol( Object snapSpace,int row ){
    }
    
    public void setBlkSizeCol( Object blk,int row ){
        model.setValueAt( blk,row, 3 );
    }
    
    public void setPoolCol( Object pool,int row ){
        model.setValueAt( pool,row, 4 );
    }
    
    public boolean isRightLVMType( String type ){
        return true;
    }
}
