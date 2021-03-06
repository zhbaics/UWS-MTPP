/*
 * SelectServicePane.java
 *
 * Created on 2006/12/29,��AM 9:52
 */

package guisanboot.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.util.*;

import mylib.UI.*;
import guisanboot.data.*;

/**
 *
 * @author  Administrator
 */
public class SelectServicePane extends javax.swing.JPanel {
    
    /** Creates new form SelectBootHostPane */
    public SelectServicePane() {
        initComponents();
    }
    
    public SelectServicePane(SanBootView view ){
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
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(20, 10));
        add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel2.setPreferredSize(new java.awt.Dimension(20, 10));
        add(jPanel2, java.awt.BorderLayout.EAST);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 45));
        jScrollPane1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        jTextArea1.setLineWrap(true);
        jTextArea1.setDisabledTextColor(java.awt.Color.black);
        jTextArea1.setEnabled(false);
        jTextArea1.setOpaque(false);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(new javax.swing.border.TitledBorder("Service"));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel8.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jRadioButton1.setText("All non-kernel service");
        jRadioButton1.setBorder(null);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jRadioButton1, gridBagConstraints);

        jRadioButton2.setText("Select  Service");
        jRadioButton2.setBorder(null);
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jRadioButton2, gridBagConstraints);

        jPanel9.add(jPanel7, java.awt.BorderLayout.WEST);

        jPanel5.add(jPanel9, java.awt.BorderLayout.NORTH);

        jPanel3.add(jPanel5, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        clickJRadioBtn2();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        clickJRadioBtn1();
    }//GEN-LAST:event_jRadioButton1ActionPerformed
    
    
    ////GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    ////GEN-END:variables
    
    GeneralEditableTable table = new GeneralEditableTable();
    SanBootView view;
    ButtonGroup grp = new ButtonGroup();
    Color orgColor;
    
    private void myInit( SanBootView _view ){
        view = _view;
        setupLanguage();
        
        jScrollPane2.getViewport().add( table,null );
        orgColor = jScrollPane2.getViewport().getBackground();
        
        jTextArea1.setText(
            SanBootView.res.getString("FailoverWizardDialog.tip2")
        );
        grp.add( jRadioButton1 );
        grp.add( jRadioButton2 );
        
        //jRadioButton1.setSelected( true );
        //clickJRadioBtn1();
        jRadioButton2.setSelected( true );
        clickJRadioBtn2();
    }
    
    private void setupLanguage(){
        ((TitledBorder)jPanel5.getBorder()).setTitle(
            SanBootView.res.getString("SelectServicePane.borderTitle.serv")
        );
        jRadioButton1.setText( SanBootView.res.getString("SelectServicePane.non-kernel"));
        jRadioButton2.setText( SanBootView.res.getString("SelectServicePane.select"));
    }
    
    public void clickJRadioBtn1( ) {                                              
        jScrollPane2.getViewport().remove( table );
        jScrollPane2.getViewport().setBackground( orgColor );
    }       
     
    public void clickJRadioBtn2() {                                              
        jScrollPane2.getViewport().add( table,null );
        jScrollPane2.getViewport().setBackground( Color.white );
    }

    public void setupServiceTable( HashMap list  ){
        int i,colSize;
        Object[][] data;
        
        colSize = 3;
        Set keySet = list.keySet();
        Iterator iterator = keySet.iterator();
        int num = keySet.size();
        
        data = new Object[2+num][colSize]; //isSel?,name,desc
        data[0][0] = "" + num;    // row num
        data[0][1] = "" + colSize;      // col num

        data[1][0] = SanBootView.res.getString("SelectServicePane.table.service.isSel");
        data[1][1] = SanBootView.res.getString("SelectServicePane.table.service.name");
        data[1][2] = SanBootView.res.getString("SelectServicePane.table.service.desc");

        i=0;
        while( iterator.hasNext() ){
            String key = (String)iterator.next();
            Service serv = (Service)list.get( key );
            data[2+i][0] = new Boolean( serv.iSeled() );
            data[2+i][1] = serv;
            data[2+i][2] = serv.getServDesc();
            i++;
        }
        
        table.setTableModel( data );
        table.setDefaultLook();

        TableColumnModel tableColumnModel = table.getColumnModel();
        int colNum = tableColumnModel.getColumnCount();
        for( i=0;i<colNum;i++ ){
            if( i == 0  ) continue; //这一列要用系统缺省的渲染器，否则CheckBox的图形效果出不来
            tableColumnModel.getColumn(i).setCellRenderer(new BrowserTableCellRenderer());
        }

        tableColumnModel.getColumn(0).setWidth( 50 );
        tableColumnModel.getColumn(1).setWidth( 100 );
        tableColumnModel.getColumn(2).setWidth( 300 );
        for( i=0;i<colNum;i++ )
            table.sizeColumnsToFit(i);

        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed(false);
    }

    public void setupServiceTable( Vector list  ){
        int i;
        Object[][] data;

        int num = list.size();

        data = new Object[2+num][3]; //isSel?,name,desc
        data[0][0] = "" + num;    // row num
        data[0][1] = "" + 3;      // col num

        data[1][0] = SanBootView.res.getString("SelectServicePane.table.service.isSel");
        data[1][1] = SanBootView.res.getString("SelectServicePane.table.service.name");
        data[1][2] = SanBootView.res.getString("SelectServicePane.table.service.desc");
        
        for( i=0; i<num; i++ ){
            Service ser = (Service)list.elementAt(i);
            
            data[2+i][0] = new Boolean( ser.iSeled() );
            data[2+i][1] = ser;
            data[2+i][2] = ser.getServDesc();
        }

        table.setTableModel( data );
        table.setDefaultLook();

        TableColumnModel tableColumnModel = table.getColumnModel();
        int colNum = tableColumnModel.getColumnCount();
        for( i=0;i<colNum;i++ ){
            if( i == 0  ) continue; //这一列要用系统缺省的渲染器，否则CheckBox的图形效果出不来
            tableColumnModel.getColumn(i).setCellRenderer(new BrowserTableCellRenderer());
        }

        tableColumnModel.getColumn(0).setWidth( 50 );
        tableColumnModel.getColumn(1).setWidth( 100 );
        tableColumnModel.getColumn(2).setWidth( 300 );
        for( i=0;i<colNum;i++ )
            table.sizeColumnsToFit(i);

        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed(false);
    }
    
    public void setStopPartialService( boolean val ){
        jRadioButton2.setSelected( val );
    }
     
    public void setStopAllService( boolean val ){
        jRadioButton1.setSelected( val );
    }
    
    public boolean isStopAllService(){
        return jRadioButton1.isSelected();
    }
    
    public Vector getServiceInfo(){
        Service newServ,oldServ;
        boolean isSel;
        
        AbstractTableModel model = (AbstractTableModel)table.getModel();
        int lineNum = model.getRowCount();
        Vector ret = new Vector( lineNum );
        
        for( int row=0; row<lineNum; row++ ){
            newServ = new Service();
            isSel = ((Boolean)model.getValueAt( row, 0 )).booleanValue();
            newServ.setSeled( isSel );
            
            oldServ  = (Service)model.getValueAt(row, 1);
            newServ.setServID( oldServ.getServID() );
            newServ.setServName( oldServ.getServName() );
            newServ.setServDesc( oldServ.getServDesc() );
            newServ.setServPath( oldServ.getServPath() );
            
            ret.addElement( newServ );
        }
        
        return ret;
    }

    private boolean isSelectedOnDupPane( String servName,HashMap servNameList ){
        Set keyset = servNameList.keySet();
        Iterator iterator = keyset.iterator();
        while( iterator.hasNext() ){
            String tempServName = (String)iterator.next();
            if( tempServName.equals( servName ) ){
                return true;
            }
        }
        return false;
    }
    
    public void adjustTable( HashMap servNameList ){
        Service serv;
        boolean isSel;

        if( servNameList == null ) return;
        
        AbstractTableModel model = (AbstractTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            serv  = (Service)model.getValueAt(row, 1);
            isSel = ((Boolean)model.getValueAt( row, 0 )).booleanValue();

            if( isSel ){
                if( !this.isSelectedOnDupPane( serv.getServName(), servNameList ) ){
                    model.setValueAt( new Boolean( false ), row, 0 );
                }
            }else{
                if( this.isSelectedOnDupPane( serv.getServName(),  servNameList ) ){
                    model.setValueAt( new Boolean( true), row, 0 );
                }
            }
        }
    }

    public boolean hasMoreThanOneServ(){
        int cnt = 0;
        
        AbstractTableModel model = (AbstractTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            Boolean isSel= (Boolean)model.getValueAt(row, 0 );
            if( isSel.booleanValue() ){
                cnt++;
            }
        }
        
        return ( cnt > 0 );
    }
    
    public void fireEditingStopMsg(){
        TableCellEditor dce;
        
        AbstractTableModel model = (AbstractTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int i=0; i<lineNum; i++  ){
            dce = table.getCellEditor( i,0 );
            if( dce!=null ){
                try{
                    while(!dce.stopCellEditing()){}
                }catch(Exception ex){}
            }
        }
    }
}
