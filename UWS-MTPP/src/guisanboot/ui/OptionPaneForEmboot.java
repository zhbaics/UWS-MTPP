/*
 * SelectBootHostPane.java
 *
 * Created on 2006/12/29,��AM 9:52
 */

package guisanboot.ui;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import mylib.tool.*;

/**
 *
 * @author  Administrator
 */
public class OptionPaneForEmboot extends javax.swing.JPanel {
    
    /** Creates new form SelectBootHostPane */
    public OptionPaneForEmboot() {
        initComponents();
    }
    
    public OptionPaneForEmboot( SanBootView view ){
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
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();

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

        jPanel6.setPreferredSize(new java.awt.Dimension(10, 25));
        jPanel3.add(jPanel6, java.awt.BorderLayout.SOUTH);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBorder(new javax.swing.border.TitledBorder("emBoot"));
        jLabel1.setText("IP :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel5.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Port :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel5.add(jLabel2, gridBagConstraints);

        jTextField1.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel5.add(jTextField1, gridBagConstraints);

        jTextField2.setPreferredSize(new java.awt.Dimension(65, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        jPanel5.add(jTextField2, gridBagConstraints);

        jPanel7.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jCheckBox1.setText("Skip this step");
        jCheckBox1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 3, 1, 1)));
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jPanel8.add(jCheckBox1, java.awt.BorderLayout.WEST);

        jPanel7.add(jPanel8, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if( jCheckBox1.isSelected() ){
            jTextField1.setEnabled( false );
            jTextField1.setOpaque( false );
            jTextField2.setEnabled( false );
            jTextField2.setOpaque( false );
        }else{
            jTextField1.setEnabled( true );
            jTextField1.setOpaque( true );
            jTextField2.setEnabled( true );
            jTextField2.setOpaque( true );
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    
    
    ////GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
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
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    ////GEN-END:variables
    
    WizardDialogSample wizardDiag;
    SanBootView view;
    
    private void myInit( SanBootView _view ){
        view = _view;
        setupLanguage();
        
        jTextArea1.setText(
            SanBootView.res.getString("FailoverWizardDialog.tip6")
        );
        regKeyboardAction();
    }
    
    private void setupLanguage(){
        ((TitledBorder)jPanel5.getBorder()).setTitle(
            SanBootView.res.getString("OptionPaneForEmboot.borderTitle.emboot")
        );
        jCheckBox1.setText( SanBootView.res.getString("OptionPaneForEmboot.checkBox.skip"));
        jLabel1.setText(SanBootView.res.getString("OptionPaneForEmboot.label.ip"));
        jLabel2.setText(SanBootView.res.getString("OptionPaneForEmboot.label.port"));
    }
    
    public boolean isSkip(){
        return jCheckBox1.isSelected();
    }
    
    String _ip;
    int port;
    public boolean checkInfoValidity(){
        _ip = jTextField1.getText().trim();
        if( _ip.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.noneIP")
            );
            return false;
        }

        if( !Check.ipCheck( _ip ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.invalidIP")
            );
            return false;
        }

        String _port = jTextField2.getText().trim();
        if( _port.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.nonePort")
            );
            return false;
        }

        if( !Check.digitCheck( _port ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.invalidPort")
            );
            return false;
        }

        port = -1;
        try{
            port = Integer.parseInt( _port );
        }catch(Exception ex){}

        if( port <1 ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.badPort")
            );
            return false;
        }
        
        if( port >65535 ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.badPort")
            );
            return false;
        }
        
        return true;
    }
    
    public void setWizardDialogSample( WizardDialogSample wdiag ){
        wizardDiag = wdiag;
    }
    
    private void regKeyboardAction(){
        jTextField1.registerKeyboardAction(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    jTextField2.requestFocus();
                }
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
          
        jTextField2.registerKeyboardAction(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    wizardDiag.nextButtonProcess();
                }
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }
    
    public void setFocusOnTextField1(){
        jTextField1.requestFocus();
    }
            
    public String getIP(){
        return _ip;
    }
    public int getPort(){
        return port;
    }
}
