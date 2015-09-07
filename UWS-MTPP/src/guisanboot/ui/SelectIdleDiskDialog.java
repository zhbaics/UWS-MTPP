/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.ui;

import guisanboot.ui.multiRenderTable.JTableX4;
import guisanboot.ui.multiRenderTable.MyDefaultTableModelForTabX4;
import guisanboot.data.IdleDisk;
import guisanboot.ui.multiRenderTable.CheckBoxEditor;
import guisanboot.ui.multiRenderTable.RowEditorModel;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Administrator
 */
public class SelectIdleDiskDialog extends javax.swing.JDialog {

    /**
     * Creates new form SelectIdleDiskDialog
     */
    public SelectIdleDiskDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public SelectIdleDiskDialog(SanBootView _view, ArrayList _list){
        this(_view , true);
        myInit(_view ,_list);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(300, 300));
        setMinimumSize(new java.awt.Dimension(300, 300));

        jPanel1.setMaximumSize(new java.awt.Dimension(300, 200));
        jPanel1.setMinimumSize(new java.awt.Dimension(300, 200));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 200));
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setMaximumSize(new java.awt.Dimension(300, 50));
        jPanel2.setMinimumSize(new java.awt.Dimension(300, 50));
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 50));
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.add(jSeparator2, java.awt.BorderLayout.PAGE_END);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jTextField1.setBackground(new java.awt.Color(236, 233, 216));
        jTextField1.setText("jTextField1");
        jPanel5.add(jTextField1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setMaximumSize(new java.awt.Dimension(300, 50));
        jPanel3.setMinimumSize(new java.awt.Dimension(300, 50));
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 50));
        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.add(jSeparator1, java.awt.BorderLayout.PAGE_START);

        jPanel4.setPreferredSize(new java.awt.Dimension(300, 48));

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1);

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ok_process();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        cancel_process();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SelectIdleDiskDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SelectIdleDiskDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SelectIdleDiskDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SelectIdleDiskDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                SelectIdleDiskDialog dialog = new SelectIdleDiskDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
    
    Object[] header;
    Object[] label;
    JTable table;
    SanBootView view;
    public ArrayList idledisklist;
    private Object[] values = null ;
    public void myInit(SanBootView _view ,ArrayList _list){
        this.idledisklist = _list;
        this.view = _view;
        setupTable(view,this.idledisklist);
        setLanauage();
    }

    
    public void setupTable(SanBootView _view ,ArrayList list){
        Object[][] data;
        int i;
        int num = list.size();
        
        data = new Object[num][3]; 
        
        header = new Object[3];
        label = new Object[num];
        
        header[0] = SanBootView.res.getString("SelectIdleDiskDialog.table.disk.select");
        header[1] = SanBootView.res.getString("SelectIdleDiskDialog.table.disk.name");
        header[2] = SanBootView.res.getString("SelectIdleDiskDialog.table.disk.size");
        
        for( i=0; i<num; i++ ){
            IdleDisk tmpdisk = (IdleDisk)list.get(i);
            data[i][0] = new Boolean( false );
            data[i][1] = tmpdisk.getIdleDiskName();
            data[i][2] = tmpdisk.getIdleDiskSize();
            
            label[i]= tmpdisk;
        }
        
        MyDefaultTableModelForTabX4 model = new MyDefaultTableModelForTabX4( data,header,label );  
        
        table = new JTableX4( model,_view);
        table.setRowHeight( 20 );
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        RowEditorModel rm = new RowEditorModel();
        ((JTableX4)table).setRowEditorModel(rm);
        
        CheckBoxEditor cb = new CheckBoxEditor();
        rm.addEditorForRow( 0, cb );
        
        TableColumnModel tableColumnModel = table.getColumnModel();
        int colNum = tableColumnModel.getColumnCount();
        tableColumnModel.getColumn(0).setWidth( 45 );
        tableColumnModel.getColumn(1).setWidth( 100 );
        tableColumnModel.getColumn(2).setWidth( 100 );
        for( i=0;i<colNum;i++ )
            table.sizeColumnsToFit(i);
        
        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed(false);
        
        jScrollPane2.getViewport().add( table,null );
        jScrollPane2.getViewport().setBackground( Color.white );
        
    }
    
    public ArrayList getIdleDiskInfo(){
        ArrayList ret = new ArrayList();
        
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int lineNum = model.getRowCount();
        
        for( int i = 0 ; i < lineNum ; i++){
            boolean isSelected = ((Boolean)model.getValueAt(i, 0)).booleanValue();
            if( isSelected ){
                IdleDisk tmp = new IdleDisk();
                tmp.setIdleDiskName( model.getValueAt(i, 1).toString() );
                tmp.setIdleDiskSize( model.getValueAt(i, 2).toString() );
                ret.add(tmp);
            }
        }
        
        return ret;
    }
    
    public void setLanauage(){
       jTextField1.setText("空闲盘选择：可以选择一个或者多个");
       jButton1.setText( SanBootView.res.getString("common.button.ok"));
       jButton2.setText( SanBootView.res.getString("common.button.cancel"));
    }
    
    public void ok_process(){
        values = new Object[1];
        this.fireEditingStopMsg();
        values[0] = getIdleDiskInfo();
        dispose();
    }
    
    public void cancel_process(){
        values = null;
        dispose();
    }
    
    public Object[] getValues(){
        return values;
    }
    
    public void fireEditingStopMsg(){
        TableCellEditor dce;
        int colNum =3 ;
        AbstractTableModel model = (AbstractTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int i=0; i<lineNum; i++  ){
            for( int j=0;j<colNum;j++ ){ 
                if( j == 1 || j ==2 ) continue;

                dce = table.getCellEditor( i,j );
                if( dce!=null ){
                    try{
                        while(!dce.stopCellEditing()){}
                    }catch(Exception ex){}
                }
            }
        }
    }
}