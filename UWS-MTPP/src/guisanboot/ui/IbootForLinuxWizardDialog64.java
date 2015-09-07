/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.ui;

import guisanboot.data.*;
import guisanboot.datadup.data.NetWork;
import guisanboot.res.ResourceCenter;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;


/**
 *
 * @author Administrator
 */
public class IbootForLinuxWizardDialog64 extends javax.swing.JDialog {

    /**
     * Creates new form IbootForLinux6WizardDialog
     */
    public IbootForLinuxWizardDialog64(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public IbootForLinuxWizardDialog64(SanBootView _view, BootHost _host, String _targetSrvName, Vector _partList, String original_boot_MAC) {
        this(_view, true);
        myInit(_view, _host, _targetSrvName, _partList, original_boot_MAC);
    }
    SanBootView view;
    BootHost host;
    Vector partList;
    String targetSrvName;
    String original_boot_MAC ;

    public void myInit(SanBootView _view, BootHost _host, String _targetSrvName, Vector _partList, String _original_boot_MAC) {
        this.view = _view;
        this.host = _host ;
        this.targetSrvName = _targetSrvName;
        this.partList = _partList ;
        this.original_boot_MAC = _original_boot_MAC ;
        jRadioButton1.setSelected( true );
        setLanguage();
    }

    public void setLanguage() {
        this.setTitle(SanBootView.res.getString("IbootForLinuxWizardDialog64.title"));
        this.jLabel1.setText(SanBootView.res.getString("IbootForLinuxWizardDialog64.label.already"));
        this.jRadioButton1.setText(SanBootView.res.getString("IbootForLinuxWizardDialog64.label.other"));
        this.jRadioButton2.setText(SanBootView.res.getString("IbootForLinuxWizardDialog64.label.new"));
        this.jButton1.setText(SanBootView.res.getString("common.button.ok"));
        this.jButton2.setText(SanBootView.res.getString("common.button.cancel"));
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
        jPanel4 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jTextField1 = new javax.swing.JTextField();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(null);

        jPanel4.setLayout(null);

        jRadioButton1.setText("jRadioButton1");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jRadioButton1);
        jRadioButton1.setBounds(30, 30, 103, 23);

        jTextField1.setPreferredSize(new java.awt.Dimension(280, 25));
        jPanel4.add(jTextField1);
        jTextField1.setBounds(60, 60, 240, 25);

        jRadioButton2.setText("jRadioButton2");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jRadioButton2);
        jRadioButton2.setBounds(30, 100, 240, 20);

        jPanel1.add(jPanel4);
        jPanel4.setBounds(20, 50, 350, 160);

        jLabel1.setText("Path");
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel1.add(jLabel1);
        jLabel1.setBounds(130, 10, 100, 25);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 50));
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.add(jSeparator1, java.awt.BorderLayout.CENTER);

        jPanel3.setPreferredSize(new java.awt.Dimension(400, 48));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        jButton1.setText("OK");
        jButton1.setPreferredSize(new java.awt.Dimension(80, 25));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        jButton2.setText("CANCEL");
        jButton2.setPreferredSize(new java.awt.Dimension(80, 25));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2);

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ok_button_act();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        jRadioButton2.setSelected( false );
        jTextField1.setEnabled( true );
        jTextField1.setOpaque( true );
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        jTextField1.setEnabled( false );
        jTextField1.setOpaque( false );
        jRadioButton1.setSelected( false );
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    public void ok_button_act() {
        if (jRadioButton1.isSelected()) {

            String macStr = this.jTextField1.getText().trim();

            if("".equals(macStr)){
                JOptionPane.showMessageDialog( view,
                                SanBootView.res.getString("IbootForLinuxWizardDialog64.error.nopath")
                            );
                return;
            }

            if( !DhcpClientInfo.isValidMAC( macStr ) ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("EditSubnetDialog.errMsg.invalidMAC")
                );
                return;
            }

            Vector list = view.initor.dhcpdb.getSubnetListFromDhcp();
            String serverIp = view.initor.serverIp ;
            String subnet = ((SubNetInDHCPConf)list.elementAt(0)).subnet ;
            String hostIp = host.getIP() ;
            String cmd = "-i "+hostIp+" -p "+DhcpClientInfo.getSimpleMac( macStr )+" -s "+subnet+" -x "+serverIp+" -ostype linux" ;

            boolean isOk = view.initor.dhcpdb.dhcpOperation(
                ResourceCenter.BIN_DIR + "dhcp_set.sh addcli " + cmd
            );

            if( !isOk ){
                JOptionPane.showMessageDialog( this,
                    SanBootView.res.getString("EditSubnetDialog.errMsg.addClntFail")
                );
            }

            String tftpRoot = view.initor.mdb.getTftpRootPath();
            view.initor.mdb.copyFiles(tftpRoot+"pxelinux.cfg/01-"+DhcpClientInfo.getMacStrForWin(original_boot_MAC).toLowerCase(), tftpRoot+"pxelinux.cfg/default");

        }
        if (jRadioButton2.isSelected()) {
            IbootForLinux6WizardDialog dialog1 = new IbootForLinux6WizardDialog(view, host, targetSrvName, partList, original_boot_MAC) ;
            int width1 = 400 + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height1 = 200 + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; // 380;
            dialog1.setSize(width1, height1);
            dialog1.setLocation(view.getCenterPoint(width1, height1));
            dialog1.setVisible(true);
        }
        this.dispose() ;
    }

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
            java.util.logging.Logger.getLogger(IbootForLinux6WizardDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IbootForLinux6WizardDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IbootForLinux6WizardDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IbootForLinux6WizardDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                IbootForLinux6WizardDialog dialog = new IbootForLinux6WizardDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
