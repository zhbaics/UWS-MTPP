/*
 * SelectBootHostPane.java
 *
 * Created on 2006/12/29,�AM 9:52
 */

package guisanboot.ui;

import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author  Administrator
 */
public class InputSwapDevPane extends javax.swing.JPanel {
    
    /** Creates new form SelectBootHostPane */
    public InputSwapDevPane() {
        initComponents();
    }
    
    public InputSwapDevPane( SanBootView view ){
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
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(20, 10));
        add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel2.setPreferredSize(new java.awt.Dimension(20, 10));
        add(jPanel2, java.awt.BorderLayout.EAST);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 70));
        jScrollPane1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        jTextArea1.setDisabledTextColor(java.awt.Color.black);
        jTextArea1.setEnabled(false);
        jTextArea1.setOpaque(false);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBorder(new javax.swing.border.TitledBorder("Swap"));
        jLabel1.setText("Swap Device:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel5.add(jLabel1, gridBagConstraints);

        jTextField1.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        jPanel5.add(jTextField1, gridBagConstraints);

        jButton1.setText("Select");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel5.add(jButton1, gridBagConstraints);

        jPanel3.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel6.setPreferredSize(new java.awt.Dimension(10, 45));
        jPanel3.add(jPanel6, java.awt.BorderLayout.SOUTH);

        add(jPanel3, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        /*
        SelectUnInitedHostDialog dialog = new SelectUnInitedHostDialog( wizardDiag,view,osType );
        int width  = 300+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 150+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setResizable( false );
        dialog.show();   
        
        Object[] ret = dialog.getValues();
        if( ret == null || ret.length <=0 ) return;
        
        boolean isFromMDB = ((Boolean)ret[0]).booleanValue();
        if( isFromMDB ){
            BootHost selHost = (BootHost)ret[1];
            jTextField1.setText( selHost.getIP() );
        }else{
            jTextField1.setText( "" );
        }
        */
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    ////GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    ////GEN-END:variables
    
    WizardDialogSample wizardDiag;
    SanBootView view;
    
    private void myInit( SanBootView _view){
        view = _view;
        
        jPanel5.remove( jButton1 );
        setupLanguage();
        jTextArea1.setText(
            SanBootView.res.getString("RestoreOriginalDiskWizardDialog.tip4")
        );
        regKeyboardAction();
    }
    
    private void setupLanguage(){
        ((TitledBorder)jPanel5.getBorder()).setTitle(
            SanBootView.res.getString("RestoreOriginalDiskWizardDialog.borderTitle.swap")
        );
        jLabel1.setText(SanBootView.res.getString("RestoreOriginalDiskWizardDialog.label.swap"));
        jButton1.setText( SanBootView.res.getString("SelectBootHostPane.button.select"));
    }
    
    private void regKeyboardAction(){
        
    }
    
    public void setWizardDialogSample( WizardDialogSample wdiag ){
        wizardDiag = wdiag;
    }
    
    String swapDev;
    public boolean checkClntInfoValidity(){
        swapDev = jTextField1.getText().trim();
        if( !swapDev.equals("") ){
            if( !swapDev.startsWith("/")){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.badSwapDev")
                );
                return false;
            }
        }

       
        return true;
    }
    
    public void setFocusOnTextField1(){
        jTextField1.requestFocus();
    }
    
    public String getSwapDevice(){
        return  swapDev;
    }
}
