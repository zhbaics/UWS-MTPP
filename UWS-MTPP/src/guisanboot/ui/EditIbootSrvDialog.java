/*
 * EditSubnetDialog.java
 *
 * Created on 2007/12/14, AM 11:36
 */

package guisanboot.ui;

import guisanboot.data.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author  Administrator
 */
public class EditIbootSrvDialog extends javax.swing.JDialog {
    
    /** Creates new form EditSubnetDialog */
    public EditIbootSrvDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public EditIbootSrvDialog( SanBootView view,DhcpDialog diag,DhcpIBootSrv ibootSrv,int selRow ){
        this( view,true );
        myInit( view,diag,ibootSrv,selRow );
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
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("IBoot Server :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jLabel1, gridBagConstraints);

        jTextField1.setPreferredSize(new java.awt.Dimension(195, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanel1.add(jTextField1, gridBagConstraints);

        jCheckBox1.setText("Default Iboot Server");
        jCheckBox1.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        jPanel1.add(jCheckBox1, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.add(jSeparator1, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        jPanel3.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 1, 5, 1)));
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

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        okButton();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        values = null;
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditSubnetDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    ////GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    ////GEN-END:variables
 
    DhcpIBootSrv ibootSrv;
    SanBootView view;
    DhcpDialog diag;
    int selRow;
    Object[] values;
    
    private void myInit( SanBootView _view,DhcpDialog _diag,DhcpIBootSrv _ibootSrv,int _selRow){
        view   = _view;
        diag   = _diag;
        ibootSrv = _ibootSrv;
        selRow = _selRow;
        
        if( ibootSrv != null ){
            jTextField1.setText( ibootSrv.ip );
            jCheckBox1.setSelected( ibootSrv.isDefaultServer() );
        }
        
        regKeyboardAction();
        setupLanguage();
    }
    
    void setupLanguage(){
        this.setTitle( SanBootView.res.getString("EditSubnetDialog.title.edit1"));
        this.jLabel1.setText( SanBootView.res.getString("EditSubnetDialog.label.ibootSrv"));
        this.jCheckBox1.setText( SanBootView.res.getString("EditSubnetDialog.check.default"));
        jButton1.setText(SanBootView.res.getString("common.button.ok"));
        jButton2.setText( SanBootView.res.getString("common.button.cancel"));
    }
    
    private void regKeyboardAction(){  
        jTextField1.registerKeyboardAction(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    jCheckBox1.requestFocus();
                }
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }
    
    void okButton(){   
        String ipStr = jTextField1.getText().trim();
        if( !checkOneIbootSrv( ipStr ) ){
            return;
        }
        
        if( diag.isSameIbootSrv( ipStr ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("EditSubnetDialog.errMsg.sameIbootSrv")
            );
            return;
        }
        
        boolean isDefIbootSrv = jCheckBox1.isSelected();
        if( !isDefIbootSrv ){
            // 检查是否为最后的default iboot server
            if( !view.initor.dhcpdb.hasDefIbootSrv( ipStr ) ){ 
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("EditSubnetDialog.errMsg.atLeastOneIbootSrv")
                );
                return;
            }
        }
        
        values = new Object[2];
        values[0] = ipStr;
        values[1] = isDefIbootSrv?"1":"0";
        
        this.dispose();
    }
    
    public Object[] getValues(){
        return values;
    }
    
    private boolean checkOneIbootSrv ( String ipStr ){
        InetAddress ip;
        
        if( ipStr.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("EditSubnetDialog.errMsg.noneIbootSrv")
            );
            return false;
        }
        
        try{
            ip = InetAddress.getByName( ipStr );
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("EditSubnetDialog.errMsg.invalidIbootSrv") +" : "+ipStr
            );
            return false;
        }
        
        return true;
    }
}
