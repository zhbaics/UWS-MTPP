/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ModifyClusterDialog.java
 *
 * Created on 2011-7-25, 15:05:25
 */

package guisanboot.cluster.ui;

import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.entity.SubCluster;
import guisanboot.data.BootHost;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import mylib.tool.Check;

/**
 *
 * @author zjj
 */
public class ModifyClusterDialog extends javax.swing.JDialog {

    /** Creates new form ModifyClusterDialog */
    public ModifyClusterDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public ModifyClusterDialog( SanBootView view,Cluster cluster ){
        this( view,true );
        myInit( cluster) ;
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
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Server Pair"));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Private IP（HeartBeat） :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel5.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Public IP（VIP） :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel5.add(jLabel2, gridBagConstraints);

        jTextField2.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        jPanel5.add(jTextField2, gridBagConstraints);

        jLabel3.setText("Virtual IP :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel5.add(jLabel3, gridBagConstraints);

        jTextField3.setPreferredSize(new java.awt.Dimension(65, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 0, 0);
        jPanel5.add(jTextField3, gridBagConstraints);

        jTextField4.setPreferredSize(new java.awt.Dimension(83, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        jPanel5.add(jTextField4, gridBagConstraints);

        jTextField5.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 0, 0);
        jPanel5.add(jTextField5, gridBagConstraints);

        jTextField6.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 0, 0);
        jPanel5.add(jTextField6, gridBagConstraints);

        jTextField7.setPreferredSize(new java.awt.Dimension(65, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        jPanel5.add(jTextField7, gridBagConstraints);

        jLabel6.setText("CMDP Port :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel5.add(jLabel6, gridBagConstraints);

        jLabel7.setText("MTPP Port :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel5.add(jLabel7, gridBagConstraints);

        jTextField8.setPreferredSize(new java.awt.Dimension(6, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        jPanel5.add(jTextField8, gridBagConstraints);

        jTextField9.setPreferredSize(new java.awt.Dimension(6, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 0, 0);
        jPanel5.add(jTextField9, gridBagConstraints);

        jTextField10.setPreferredSize(new java.awt.Dimension(6, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        jPanel5.add(jTextField10, gridBagConstraints);

        jTextField11.setPreferredSize(new java.awt.Dimension(6, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 0, 0);
        jPanel5.add(jTextField11, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("宋体", 1, 20));
        jLabel8.setText("Node1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel5.add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("宋体", 1, 20));
        jLabel9.setText("Node2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel5.add(jLabel9, gridBagConstraints);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("General"));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Cluster Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel8.add(jLabel4, gridBagConstraints);

        jTextField1.setPreferredSize(new java.awt.Dimension(300, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel8.add(jTextField1, gridBagConstraints);

        jPanel1.add(jPanel8, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 35, 5));

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        cancel_process();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ok_process();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ModifyClusterDialog dialog = new ModifyClusterDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables

    private Object[] ret;
    private Cluster cluster;
    private int osType = 0;

    private void myInit( Cluster cluster ){
        this.cluster = cluster;
        if( cluster.isCMDPProtect() && cluster.isWinHost() ){
            this.osType = 2;
        }
        setupUI();
        setupLanguage();
    }

    private void setupUI(){
        this.jTextField1.setText( cluster.getCluster_name() );
        ArrayList<SubCluster> subcList = cluster.getRealSubCluster();
        SubCluster sub = subcList.get( 0 );
        this.jTextField4.setText( sub.getHost().getClnt_pri_ip() );
        this.jTextField2.setText( sub.getHost().getIP() );
        this.jTextField7.setText( sub.getHost().getClnt_vip() );
        this.jTextField8.setText( sub.getHost().getPort()+"" );
        this.jTextField10.setText( sub.getHost().getMtppPort()+"" );
        sub = subcList.get( 1 );
        this.jTextField6.setText( sub.getHost().getClnt_pri_ip() );
        this.jTextField5.setText( sub.getHost().getIP() );
        this.jTextField3.setText( sub.getHost().getClnt_vip() );
        this.jTextField9.setText( sub.getHost().getPort()+"" );
        this.jTextField11.setText( sub.getHost().getMtppPort()+"" );
    }
    
    private void setupLanguage(){
        this.setTitle( SanBootView.res.getString("ClusterSetupPane.title"));
        ((TitledBorder)jPanel5.getBorder()).setTitle(
            SanBootView.res.getString("ClusterSetupPane.borderTitle.serverPair")
        );
        ((TitledBorder)jPanel8.getBorder()).setTitle(
            SanBootView.res.getString("ClusterSetupPane.borderTitle.general")
        );
        jLabel1.setText(SanBootView.res.getString("ClusterSetupPane.label.priip"));
        jLabel2.setText(SanBootView.res.getString("ClusterSetupPane.label.pubip"));
        jLabel3.setText(SanBootView.res.getString("ClusterSetupPane.label.vip"));
        jLabel4.setText(SanBootView.res.getString("ClusterSetupPane.label.clusterName"));
        jLabel6.setText( SanBootView.res.getString("SelectProtectedSysVolPane.combox.pp") +
                SanBootView.res.getString("SelectBootHostPane.label.port"));
        jLabel7.setText( SanBootView.res.getString("SelectProtectedSysVolPane.combox.lp") +
                SanBootView.res.getString("SelectBootHostPane.label.port"));
        jLabel8.setText( SanBootView.res.getString("ClusterSetupPane.label.node")+"1" );
        jLabel9.setText( SanBootView.res.getString("ClusterSetupPane.label.node")+"2" );
        this.jButton1.setText(SanBootView.res.getString("common.button.ok"));
        this.jButton2.setText(SanBootView.res.getString("common.button.cancel"));
    }

    String clusterName;
    String pri_ip1,pri_ip2,pub_ip1,pub_ip2,vip1,vip2;
    int port1,port2;
    int mtpp_port1,mtpp_port2;
    public boolean checkClntInfoValidity(){
        clusterName = this.jTextField1.getText().trim();
        if( clusterName.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("PhyInitWinClusterWizardDialog.error.clusterNameNone")
            );
            return false;
        }

        if( clusterName.getBytes().length > 60 ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("PhyInitWinClusterWizardDialog.error.clusterNameTooLong")
            );
            return false;
        }

        pri_ip1 = this.jTextField4.getText().trim();
        if( pri_ip1.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.noneIP")
            );
            return false;
        }
        if( !Check.ipCheck( pri_ip1 ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.invalidIP") + " : " + pri_ip1
            );
            return false;
        }

        pri_ip2 = this.jTextField6.getText().trim();
        if( pri_ip2.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.noneIP")
            );
            return false;
        }
        if( !Check.ipCheck( pri_ip2 ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.invalidIP") + " : " + pri_ip2
            );
            return false;
        }

        pub_ip1 = this.jTextField2.getText().trim();
        if( pub_ip1.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.noneIP")
            );
            return false;
        }
        if( !Check.ipCheck( pub_ip1 ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.invalidIP") + " : " + pub_ip1
            );
            return false;
        }

        pub_ip2 = this.jTextField5.getText().trim();
        if( pub_ip2.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.noneIP")
            );
            return false;
        }
        if( !Check.ipCheck( pub_ip2 ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.invalidIP") + " : " + pub_ip2
            );
            return false;
        }

        vip1 = this.jTextField7.getText().trim();
        if( vip1.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.noneIP")
            );
            return false;
        }
        if( !Check.ipCheck( vip1 ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.invalidIP") + " : " + vip1
            );
            return false;
        }

        vip2 = this.jTextField3.getText().trim();
        if( vip2.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.noneIP")
            );
            return false;
        }
        if( !Check.ipCheck( vip2 ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.invalidIP") + " : " + vip2
            );
            return false;
        }

        if( osType == 2 ){ // check cmdp port validity
            String _port1 = jTextField8.getText().trim();
            if( _port1.equals("") ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("AddHostDialog.errMsg.nonePort")
                );
                return false;
            }

            if( !Check.digitCheck( _port1 ) ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("AddHostDialog.errMsg.invalidPort")
                );
                return false;
            }

            port1 = -1;
            try{
                port1 = Integer.parseInt( _port1 );
            }catch(Exception ex){}

            if( port1 < 1 ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("AddHostDialog.errMsg.badPort")
                );
                return false;
            }

            if( port1 > 65535 ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("AddHostDialog.errMsg.badPort")
                );
                return false;
            }

            String _port2 = jTextField9.getText().trim();
            if( _port2.equals("") ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("AddHostDialog.errMsg.nonePort")
                );
                return false;
            }

            if( !Check.digitCheck( _port2 ) ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("AddHostDialog.errMsg.invalidPort")
                );
                return false;
            }

            port2 = -1;
            try{
                port2 = Integer.parseInt( _port2 );
            }catch(Exception ex){}

            if( port2 < 1 ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("AddHostDialog.errMsg.badPort")
                );
                return false;
            }

            if( port2 > 65535 ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("AddHostDialog.errMsg.badPort")
                );
                return false;
            }
        }

        String _m_port1 = jTextField10.getText().trim();
        if( _m_port1.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.nonePort")
            );
            return false;
        }

        if( !Check.digitCheck( _m_port1 ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.invalidPort")
            );
            return false;
        }

        mtpp_port1 = -1;
        try{
            mtpp_port1 = Integer.parseInt( _m_port1 );
        }catch(Exception ex){}

        if( mtpp_port1 < 1 ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.badPort")
            );
            return false;
        }

        if( mtpp_port1 > 65535 ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.badPort")
            );
            return false;
        }

        String _m_port2 = jTextField11.getText().trim();
        if( _m_port2.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.nonePort")
            );
            return false;
        }

        if( !Check.digitCheck( _m_port2 ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.invalidPort")
            );
            return false;
        }

        mtpp_port2 = -1;
        try{
            mtpp_port2 = Integer.parseInt( _m_port2 );
        }catch(Exception ex){}

        if( mtpp_port2 < 1 ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.badPort")
            );
            return false;
        }

        if( mtpp_port2 > 65535 ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("AddHostDialog.errMsg.badPort")
            );
            return false;
        }

        return true;
    }
    
    private void ok_process(){
        if( !this.checkClntInfoValidity() )return;
        
        cluster.setCluster_name( this.clusterName );
        ArrayList<SubCluster> subcList = cluster.getRealSubCluster();
        SubCluster sub = subcList.get( 0 );
        sub.getHost().setClnt_pri_ip( pri_ip1 );
        sub.getHost().setIP( pub_ip1 );
        sub.getHost().setClnt_vip( vip1 );
        sub.getHost().setPort( port1 );
        sub.getHost().setMtppPort( mtpp_port1 );

        sub = subcList.get(1);
        sub.getHost().setClnt_pri_ip( pri_ip2 );
        sub.getHost().setIP( pub_ip2 );
        sub.getHost().setClnt_vip( vip2 );
        sub.getHost().setPort( port2 );
        sub.getHost().setMtppPort( mtpp_port2 );

        ret = new Object[1];
        ret[0] = cluster;

        this.dispose();
    }

    private void cancel_process(){
        this.ret = null;
        this.dispose();
    }

    public Object[] getValue(){
        return ret;
    }
}
