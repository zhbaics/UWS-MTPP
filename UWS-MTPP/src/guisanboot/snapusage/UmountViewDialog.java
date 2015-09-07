/*
 * UmountViewDialog.java
 *
 * Created on 2009/7/8,��PM��2:05
 */

package guisanboot.snapusage;

import guisanboot.data.BootHost;
import guisanboot.data.HostWrapper;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.util.Vector;

/**
 *
 * @author  Administrator
 */
public class UmountViewDialog extends javax.swing.JDialog {

    /** Creates new form UmountViewDialog */
    public UmountViewDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public UmountViewDialog( SanBootView view,BootHost host ){
        this( view,true );
        myInit( view,host );
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 1, 2, 1));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 25, 5));

        jButton1.setText("OK");
        jButton1.setPreferredSize(new java.awt.Dimension(75, 24));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        jButton2.setText("Cancel");
        jButton2.setPreferredSize(new java.awt.Dimension(75, 24));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);
        jPanel1.add(jSeparator1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(null);
        jTextArea1.setDisabledTextColor(java.awt.Color.black);
        jTextArea1.setEnabled(false);
        jTextArea1.setOpaque(false);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel6.setPreferredSize(new java.awt.Dimension(30, 10));
        jPanel4.add(jPanel6, java.awt.BorderLayout.WEST);

        jPanel7.setPreferredSize(new java.awt.Dimension(30, 10));
        jPanel4.add(jPanel7, java.awt.BorderLayout.EAST);
        jPanel4.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jPanel2.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel5.setMinimumSize(new java.awt.Dimension(42, 45));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Host IP :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel5.add(jLabel1, gridBagConstraints);

        jComboBox1.setPreferredSize(new java.awt.Dimension(140, 20));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel5.add(jComboBox1, gridBagConstraints);

        jLabel2.setText("Host Port :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel5.add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel5.add(jTextField1, gridBagConstraints);

        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel5.add(jCheckBox1, gridBagConstraints);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    this.jComboBox_process();
}//GEN-LAST:event_jComboBox1ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    okBtn_process();
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    cancelBtn_process();
}//GEN-LAST:event_jButton2ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UmountViewDialog dialog = new UmountViewDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override public void windowClosing(java.awt.event.WindowEvent e) {
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
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    SanBootView view;
    BootHost host;
    public void myInit( SanBootView view,BootHost host ){
        this.view = view;
        this.host = host;
        
        setupHostComboBox();
        setupLanguage();
        jTextArea1.setText( "\n" + SanBootView.res.getString("UmountViewDialog.tip") );
    }
    
    private void setupLanguage(){
        this.setTitle( SanBootView.res.getString("UmountViewDialog.title") );
        this.jLabel1.setText( SanBootView.res.getString("UmountViewDialog.label.ip") );
        this.jLabel2.setText( SanBootView.res.getString("UmountViewDialog.label.port") );
        this.jButton1.setText( SanBootView.res.getString("common.button.ok") );
        this.jButton2.setText( SanBootView.res.getString("common.button.cancel") );
        this.jCheckBox1.setText( SanBootView.res.getString("UmountViewDialog.check.delView") );
    }
    
    private void setupHostComboBox(){
        BootHost ahost;
        HostWrapper wrap = null;
        
        Vector list = view.initor.mdb.getAllBootHost();
        int size = list.size();
        for( int i=0; i<size; i++ ){
            ahost = ( BootHost )list.elementAt(i);
            wrap = new HostWrapper( ahost );
            if( host.isWinHost() ){
                if( ahost.isWinHost() ){
                    jComboBox1.addItem( wrap );
                }
            }else{
                if( !ahost.isWinHost() ){
                    jComboBox1.addItem( wrap );
                }
            }
        }
    }
    
    private void jComboBox_process(){
        Object obj = jComboBox1.getSelectedItem();
        if( obj == null ) return;
        
        if( obj instanceof HostWrapper ){
            jTextField1.setText( ((HostWrapper)obj).getPort()+"" );
        }else{
            jTextField1.setText( ResourceCenter.MTPP_AGENT_PORT+"" );
        }
    }
    
    private void okBtn_process(){
        
    }
    
    private void cancelBtn_process(){
        this.dispose();
    }
}
