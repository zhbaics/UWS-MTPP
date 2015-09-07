/*
 * SelectBootHostPane.java
 *
 * Created on 2006/12/29, 9:52 AM
 */

package guisanboot.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.List;
import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.multiRenderTable.*;

/**
 *
 * @author  Administrator
 */
public class DefineRstRelationshipForUnixPane extends javax.swing.JPanel {
    
    /** Creates new form SelectBootHostPane */
    public DefineRstRelationshipForUnixPane() {
        initComponents();
    }
    
    public DefineRstRelationshipForUnixPane( SanBootView view ) {
        this();
        myInit( view );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(20, 10));
        add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel2.setPreferredSize(new java.awt.Dimension(20, 10));
        add(jPanel2, java.awt.BorderLayout.EAST);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 70));
        jScrollPane1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        jTextArea1.setLineWrap(true);
        jTextArea1.setDisabledTextColor(java.awt.Color.black);
        jTextArea1.setEnabled(false);
        jTextArea1.setOpaque(false);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel7.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 1, 30, 1)));
        jPanel5.add(jPanel7, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jPanel5, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    ////GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    ////GEN-END:variables
    
    ArrayList newList;
    JTableZ1 table;
    SanBootView view;
    Object[] header;
    Object[] label;
    WizardDialogSample wizardDiag;
    
    private void myInit( SanBootView _view ){
        view = _view;    
        jTextArea1.setText(
            SanBootView.res.getString("RestoreOriginalDiskWizardDialog.tip6")
        );
    }
    
    public void setupTable( ArrayList oldList,ArrayList _newList ){
        int i;
        long cap;
        Object[][] data;
        
        newList = _newList;
        
        Vector newDisk = new Vector();
        int size = newList.size();
        for( i=0; i<size; i++ ){
            newDisk.addElement( newList.get(i) );
        }
        
        ArrayList fit_list = matchDisk( oldList );
        if( fit_list.size() == 0 ){
SanBootView.log.error( getClass().getName(),"Can't find any matched disk-peer. the following are old and new disk.");
            size = newList.size();
            for( i=0; i<size; i++ ){
SanBootView.log.info( getClass().getName()," New Disk: \n " + ((DiskForUnix)newList.get(i)).prtMe() );                
            }
            
            size = oldList.size();
            for( i=0; i<size; i++ ){
SanBootView.log.info( getClass().getName()," Old Disk: \n " + ((DiskForUnix)oldList.get(i)).prtMe() );                 
            }
        }
        
        int num = fit_list.size();
        data = new Object[num][3]; 
        
        header = new Object[3]; // restore?,old-disk,new-disk
        label = new Object[num];
        
        header[0] = SanBootView.res.getString("RestoreOriginalDiskWizardDialog.table.rst");
        header[1] = SanBootView.res.getString("RestoreOriginalDiskWizardDialog.table.olddisk");
        header[2] = SanBootView.res.getString("RestoreOriginalDiskWizardDialog.table.newdisk");
        
        for( i=0; i<num; i++ ){
            DiskBinderForUnix binder = (DiskBinderForUnix)fit_list.get(i);
            data[i][0] = Boolean.TRUE;
            data[i][1] = binder.old_disk;
            if( binder.new_disk != null ){
                data[i][2] = binder.new_disk;
            }
            label[i]= binder.old_disk.toString();
        }
        
        MyDefaultTableModelForTabZ1 model = new MyDefaultTableModelForTabZ1( data,header,label );
        
        table = new JTableZ1( model,view );   
        table.setRowHeight( 20 );
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        RowEditorModel rm = new RowEditorModel();
        table.setRowEditorModel(rm); 
        
        CheckBoxEditor cb = new CheckBoxEditor();
        rm.addEditorForRow( 0, cb );
        
        PaneEditor1 pe = new PaneEditor1( newDisk,view ); 
        rm.addEditorForRow( 2, pe );
        
        TableColumnModel tableColumnModel = table.getColumnModel();
        int colNum = tableColumnModel.getColumnCount();
        tableColumnModel.getColumn(0).setWidth( 45 );
        tableColumnModel.getColumn(1).setWidth( 202 );
        tableColumnModel.getColumn(2).setWidth( 202 );
        for( i=0;i<colNum;i++ )
            table.sizeColumnsToFit(i);
            
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed(false);
        
        jScrollPane2.getViewport().add( table,null );
        jScrollPane2.getViewport().setBackground( Color.white );
        
        table.addMouseListener( new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                int clicks = e.getClickCount();
                if( clicks >=2 ){
                    int col = table.getSelectedColumn();
                    if( col == 1 ){
                        int row = table.getSelectedRow();
                        Object disk = table.getValueAt( row,col );
                        if( disk != null ){
SanBootView.log.debug(getClass().getName(), "  selected disk: " + disk.toString() );
                            DiskPropertyDialog dialog = new DiskPropertyDialog( view,disk, col==1 ); 
                            int width  = 340+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                            int height = 220+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                            dialog.setSize( width,height );
                            dialog.setLocation( view.getCenterPoint( width,height ) );
                            dialog.setVisible( true );
                        }
                    }
                }
            }
        });
    }
    
    private ArrayList<DiskBinderForUnix> matchDisk( List<DiskForUnix> oldList ){
        Collections.sort( newList );
        
        ArrayList<DiskBinderForUnix> ret = new ArrayList<DiskBinderForUnix>( oldList.size() );
        
        Iterator<DiskForUnix> iterator = oldList.iterator();
        while( iterator.hasNext() ){
            DiskBinderForUnix binder = new DiskBinderForUnix();
            binder.old_disk = iterator.next();
            DiskForUnix new_disk = findFitNewDisk( binder.old_disk.getDiskSize(),newList );
            if( new_disk != null ){
                binder.new_disk = new_disk;
            }
            ret.add( binder );
        }
        
        return ret;
    }
    
    private DiskForUnix findFitNewDisk( long size,List list ){
        Iterator iterator = newList.iterator();
        while( iterator.hasNext() ){
            DiskForUnix disk = (DiskForUnix)iterator.next();
            if( size <= disk.getDiskSize() ){
                list.remove( disk );
                return disk;
            }
        }
        return null;
    }
    
    public void setWizardDialogSample( WizardDialogSample wdiag ){
        wizardDiag = wdiag;
    }
    
    public boolean hasRestoreOSDisk(){
        DiskForUnix disk;
        
        MyDefaultTableModelForTabZ1 model = (MyDefaultTableModelForTabZ1)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            disk = (DiskForUnix)model.getValueAt( row,1);
            if( disk.containOSPartition()){
                return ((Boolean)model.getValueAt(row, 0 )).booleanValue();
            }
        }
        
        return false;
    }
    
    public boolean hasRestoreDisk(){
        int cnt = 0;
        
        MyDefaultTableModelForTabZ1 model = (MyDefaultTableModelForTabZ1)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            Boolean isSel = (Boolean)model.getValueAt(row, 0 );
            if( isSel.booleanValue() ){
                cnt++;
            }
        }
        
        return ( cnt > 0 );
    }
    
    public boolean hasNoSeledDisk(){
        MyDefaultTableModelForTabZ1 model = (MyDefaultTableModelForTabZ1)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            Boolean isSel = (Boolean)model.getValueAt(row, 0 );
            if( !isSel.booleanValue() ){
                return true;
            }
        }
        return false;
    }
    
    public boolean checkVolInfoValidity(){
        HashSet set = new HashSet();
        
        MyDefaultTableModelForTabZ1 model = (MyDefaultTableModelForTabZ1)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            Boolean isSel = (Boolean)model.getValueAt( row, 0 );
            if( isSel.booleanValue() ){
                DiskForUnix old_disk = (DiskForUnix)model.getValueAt( row, 1 );
                DiskForUnix new_disk = (DiskForUnix)model.getValueAt( row, 2 );
                if( new_disk == null ){
                    JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.noNewDisk") + "["+(row+1)+",3]"
                    );
                    return false;
                }else{
                    if( set.contains( new_disk.getDiskDevName() ) ){
                        JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.sameNewDisk") + "["+(row+1)+",3]"
                        );
                        return false;
                    }else{
                        set.add( new_disk.getDiskDevName() );
                        //if( old_disk.getDiskSize() > new_disk.getDiskSize() ){
                        if( new_disk.getDiskSize() < ( old_disk.getDiskSize()-1024*1024*104 ) ){
                            JOptionPane.showMessageDialog(this,
                                SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.notMatch") + "["+(row+1)+",3]"
                            );
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    public ArrayList getDiskInfo(){
        DiskBinderForUnix binder;
        boolean isSel;
        
        MyDefaultTableModelForTabZ1 model = (MyDefaultTableModelForTabZ1)table.getModel();
        int lineNum = model.getRowCount();
        ArrayList ret = new ArrayList( lineNum );
        for( int row=0; row<lineNum; row++ ){
            isSel = ((Boolean)model.getValueAt( row, 0 )).booleanValue();
            if( isSel ){
                binder = new DiskBinderForUnix();
                binder.old_disk = (DiskForUnix)model.getValueAt(row,1);
                binder.new_disk = (DiskForUnix)model.getValueAt(row,2);
                
                ret.add( binder );
            }
        }
        
        return ret;
    }
    
    public void fireEditingStopMsg(){
        TableCellEditor dce;
        
        AbstractTableModel model = (AbstractTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int i=0; i<lineNum; i++  ){
            for( int j=0;j<3;j++ ){ //专门停止0,1，2列
                if( j == 1 ) continue;
                
                dce = table.getCellEditor( i,j );
                if( dce!=null ){
                    try{
                        while(!dce.stopCellEditing()){};
                    }catch(Exception ex){}
                }
            }
        }
    }
}

class DiskBinderForUnix{
    public DiskForUnix old_disk;
    public DiskForUnix new_disk = null;
    
    DiskBinderForUnix(){
    }
}
