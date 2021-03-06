/*
 * InputNameDialog.java
 *
 * Created on 2008/8/13, PM�1:38
 */

package guisanboot.datadup.ui;

import javax.swing.*;
import guisanboot.ui.SanBootView;
import java.awt.event.KeyEvent;

/**
 *
 * @author  Administrator
 */
public class InputNameDialog extends javax.swing.JDialog {
    
    /** Creates new form InputNameDialog */
    public InputNameDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public InputNameDialog( SanBootView view,String oldName ){
        this( view,true );
        myInit( view,oldName );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" ��ɵĴ��� ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("New Profile Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jLabel1, gridBagConstraints);

        jTextField1.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel1.add(jTextField1, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 35, 5));

        jPanel3.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 1, 2, 1)));
        jPanel3.setPreferredSize(new java.awt.Dimension(173, 40));
        jButton1.setText("OK");
        jButton1.setPreferredSize(new java.awt.Dimension(75, 27));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel3.add(jButton1);

        jButton2.setText("Cancel");
        jButton2.setPreferredSize(new java.awt.Dimension(75, 27));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel3.add(jButton2);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.add(jSeparator1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        values = null;
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String _name = jTextField1.getText().trim();
        if( _name.equals("") ){
            JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("InputNameDialog.error.noneName")
            );
            return;
        }
        
        if( _name.indexOf("\"")>=0 || _name.indexOf("'")>=0|| 
            _name.indexOf(' ')>=0 || _name.indexOf('\t')>=0 ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("InputNameDialog.error.badName")
            );
            return;
        }
        
        if( _name.getBytes().length >=255 ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("InputNameDialog.error.nametoolong")
            );
            return;
        }
        
        String name = _name + ".prf";
        if( name.equals( oldName ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("InputNameDialog.error.notChg")
            );
            return;
        }
        
        if( view.initor.mdb.isSameProfileName( name ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("InputNameDialog.error.sameName")
            );
            return;
        }
        
        values = new Object[1];
        values[0] = name;
        
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InputNameDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    // �������� - �������޸�//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    // ������������//GEN-END:variables
    
    SanBootView view;
    String oldName;
    private Object[] values=null;
    
    private void myInit( SanBootView view,String oldName ){
        this.view = view;
        this.oldName = oldName;
        setupLanguage();
        
        try{
            int len = oldName.length();
            String name = oldName.substring( 0, len-4 );
            jTextField1.setText( name );
        }catch(Exception ex){
            jTextField1.setText( oldName );
        }
        
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter(){
            @Override public void keyPressed( KeyEvent e ){
                if( e.getKeyCode() == KeyEvent.VK_ENTER ){
                    // 在jCheckBox2上回车将导致直接开始 add host
                    jButton1ActionPerformed( null );
                }
            }
        });
    }
    
    private void setupLanguage(){
        setTitle( SanBootView.res.getString("InputNameDialog.title"));
        jLabel1.setText( SanBootView.res.getString("InputNameDialog.label.name"));
        jButton1.setText( SanBootView.res.getString("common.button.ok"));
        jButton2.setText( SanBootView.res.getString("common.button.cancel"));
    }
    
    public Object[] getValues(){
        return values;
    }
}
