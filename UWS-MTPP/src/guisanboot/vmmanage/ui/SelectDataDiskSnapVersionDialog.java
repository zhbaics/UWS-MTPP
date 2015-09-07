/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.ui;

import guisanboot.data.BootHost;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class SelectDataDiskSnapVersionDialog extends javax.swing.JDialog {

    /**
     * Creates new form SelectDataDiskSnapVersionDialog
     */
    public SelectDataDiskSnapVersionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public SelectDataDiskSnapVersionDialog(SanBootView _view, BootHost _host) {
        this(_view, true);
        myInit(_view, _host);
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
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 50));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 15));

        jButton1.setText("OK");
        jButton1.setPreferredSize(new java.awt.Dimension(80, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        jButton2.setText("CANCEL");
        jButton2.setPreferredSize(new java.awt.Dimension(80, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);
        jPanel1.add(jSeparator1, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 250));
        jPanel2.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        selectSnapPane.fireEditingStopMsg();
        ArrayList ret = selectSnapPane.getSelectedSnap();
        values = new Object[1];
        values[0] = ret;
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        values = null;
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed
    public SanBootView view;
    public BootHost host;
    public SelectSnapshotPane selectSnapPane;
    BootVerList oldSwitchVerList; // 上次网络磁盘切换的版本（里面只包含版本列表）
    Vector bindList = null;
    private Object[] values;

    public void setLanguage(){
        this.jButton1.setText(SanBootView.res.getString("common.button.ok"));
        this.jButton2.setText(SanBootView.res.getString("common.button.cancel"));
        this.setTitle( SanBootView.res.getString("SelectDataDiskSnapVersionDialog.title") );
    }
    public void myInit(SanBootView _view, BootHost _host) {
        this.view = _view;
        this.host = _host;
        setLanguage();
        selectSnapPane = new SelectSnapshotPane(view, ResourceCenter.CMD_TYPE_CMDP);
        selectSnapPane.setHost(host);
        
        oldSwitchVerList = view.getSwitchVer( host.getID() );
        
        ProgressDialog initDiag = new ProgressDialog(
                view,
                SanBootView.res.getString("View.pdiagTitle.getSnapVer"),
                SanBootView.res.getString("View.pdiagTip.getSnapVer"));

        GetRstVersion getRstVer = new GetRstVersion(initDiag, view, host.getID(), true, false);
        getRstVer.start();
        initDiag.mySetSize();
        initDiag.setLocation(view.getCenterPoint(initDiag.getDefWidth(), initDiag.getDefHeight()));
        initDiag.setVisible(true);

        bindList = getRstVer.getBindList();

        //selectSnapPane.setupTable( bindList, oldBootVerList.bootVerList );
        selectSnapPane.setupTable(bindList, oldSwitchVerList.bootVerList);
        this.jPanel2.add(selectSnapPane ,BorderLayout.CENTER);

    }
    public Object[] getValues(){
        return values;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}