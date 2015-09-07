/*
 * SchedDialog.java
 *
 * Created on July 28, 2008, 17:14 PM
 */

package guisanboot.datadup.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import guisanboot.datadup.data.*;
import guisanboot.data.BootHost;
import guisanboot.ui.SanBootView;
import mylib.tool.Check;

/**
 *
 * @author  Administrator
 */
public class SchedDialog extends javax.swing.JDialog {
    public static final String TIME_UNIT_HOUR = "SchedDialog.unit.hour";
    public static final String TIME_UNIT_MIN  = "SchedDialog.unit.min";
    public static final String TYPE_SCHE_FREQ = "FREQ";
    public static final String TYPE_SCHE_ONCE = "ONCE";
    
    MonthlyPane monthPane = new MonthlyPane(); 
    WeeklyPane weekPane = new WeeklyPane(); 
    
    Schedulerable diag;
    SanBootView view;
    boolean displayProf = true;
    Object[] values = null;
    DBSchedule sched = null;
    
    ButtonGroup group1 = new ButtonGroup();
    ButtonGroup group2 = new ButtonGroup();
    
    /** Creates new form SchedDialog */
    public SchedDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /** Creates new form SchedDialog */
    public SchedDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public SchedDialog( SanBootView view, DBSchedule sche,UniProfile prof ) {
        this( view,true );
        myInit( null,view,sche,prof );
    }
    
    public SchedDialog( Schedulerable diag,SanBootView view, DBSchedule sche ) {
        this( (Dialog)diag,true );
        myInit( diag,view,sche,null );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jSpinner3 = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setPreferredSize(new java.awt.Dimension(0, 80));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Profile :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel3.add(jLabel2, gridBagConstraints);

        jTextField1.setPreferredSize(new java.awt.Dimension(390, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel3.add(jTextField1, gridBagConstraints);

        jComboBox1.setPreferredSize(new java.awt.Dimension(190, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel3.add(jComboBox1, gridBagConstraints);

        jLabel5.setText("Backup Level :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel3.add(jLabel5, gridBagConstraints);

        jComboBox6.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel3.add(jComboBox6, gridBagConstraints);

        jCheckBox1.setText("Enable");
        jCheckBox1.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 8, 0, 0);
        jPanel3.add(jCheckBox1, gridBagConstraints);

        jLabel6.setText("Host :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel3.add(jLabel6, gridBagConstraints);

        jTextField2.setEditable(false);
        jTextField2.setPreferredSize(new java.awt.Dimension(110, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        jPanel3.add(jTextField2, gridBagConstraints);

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Contents"));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Occur Freq"));
        jPanel7.setPreferredSize(new java.awt.Dimension(80, 10));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jRadioButton3.setText("Daily");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jRadioButton3, gridBagConstraints);

        jRadioButton4.setText("Weekly");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jRadioButton4, gridBagConstraints);

        jRadioButton5.setText("Monthly");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jRadioButton5, gridBagConstraints);

        jPanel5.add(jPanel7, java.awt.BorderLayout.WEST);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new java.awt.BorderLayout());
        jPanel5.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Daily Freq"));
        jPanel6.setPreferredSize(new java.awt.Dimension(10, 90));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jRadioButton1.setText("Once Occur");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel6.add(jRadioButton1, gridBagConstraints);

        jRadioButton2.setText("Freq Occur");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel6.add(jRadioButton2, gridBagConstraints);

        jLabel3.setText("Hour");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        jPanel6.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Minute");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        jPanel6.add(jLabel4, gridBagConstraints);

        jComboBox5.setPreferredSize(new java.awt.Dimension(29, 20));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        jPanel6.add(jComboBox5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel6.add(jSpinner1, gridBagConstraints);

        jSpinner2.setPreferredSize(new java.awt.Dimension(45, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel6.add(jSpinner2, gridBagConstraints);

        jSpinner3.setPreferredSize(new java.awt.Dimension(45, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel6.add(jSpinner3, gridBagConstraints);

        jPanel4.add(jPanel6, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 1, 5, 1));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 25, 5));

        jButton1.setText("OK");
        jButton1.setPreferredSize(new java.awt.Dimension(75, 24));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton2.setText("Cancel");
        jButton2.setPreferredSize(new java.awt.Dimension(75, 24));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        unitComboBox_actionPerformed( evt );
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        freqOccurRadioButton_actionPerformed( evt );
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        onceOccurRadioButton_actionPerformed( evt );
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        monthlyRadioButton_actionPerformed();
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        weeklyRadioButton_actionPerformed();
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        dailyRadioButton_actionPerformed();
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        values = null;
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        okButton_actionPerformed( evt );
    }//GEN-LAST:event_jButton1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new SchedDialog(new javax.swing.JFrame(), true).setVisible( true );
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
    
    private void myInit( Schedulerable diag,SanBootView view,DBSchedule sched,UniProfile prof ){
        this.diag = diag;
        this.view = view;
        this.sched = sched;
        this.displayProf = (this.diag == null );
        
        group1.add( jRadioButton1 );
        group1.add( jRadioButton2 );
        
        group2.add( jRadioButton3 );
        group2.add( jRadioButton4 );
        group2.add( jRadioButton5 );
        
        if( !displayProf ){
            hideProfUI();
        }
        
        setupLanguage();
        setupTime();
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                profileComboBox_actionPerformed();
            }
        });
        setupProfileComboBox( prof );
        setupBakLevelComboBox();
        
        regKeyboardAction();
        setupDefault();
    }
    
    private void setupLanguage(){
        setTitle( SanBootView.res.getString("SchedDialog.dialogTitle"));
        ((TitledBorder)jPanel4.getBorder()).setTitle(
            SanBootView.res.getString("SchedDialog.borderTitle.content")
        );
        ((TitledBorder)jPanel7.getBorder()).setTitle(
            SanBootView.res.getString("SchedDialog.borderTitle.frequence")
        );
        ((TitledBorder)jPanel6.getBorder()).setTitle(
            SanBootView.res.getString("SchedDialog.borderTitle.dailyfreq")
        );
        jButton2.setText( SanBootView.res.getString("common.button.cancel"));
        jButton1.setText( SanBootView.res.getString("common.button.ok"));
        jLabel1.setText( SanBootView.res.getString("SchedDialog.label.name"));
        jLabel2.setText( SanBootView.res.getString("SchedDialog.label.profile"));
        jLabel3.setText( SanBootView.res.getString("SchedDialog.label.hour"));
        jLabel4.setText( SanBootView.res.getString("SchedDialog.label.min"));
        jLabel5.setText( SanBootView.res.getString("SchedDialog.label.level"));
        jLabel6.setText( SanBootView.res.getString("SchedDialog.label.client"));
        jRadioButton5.setText( SanBootView.res.getString("SchedDialog.radioButton.monthly"));
        jRadioButton4.setText( SanBootView.res.getString("SchedDialog.radioButton.weekly"));
        jRadioButton3.setText(SanBootView.res.getString("SchedDialog.radioButton.daily"));
        jRadioButton1.setText( SanBootView.res.getString("SchedDialog.radioButton.onceOccur"));
        jRadioButton2.setText( SanBootView.res.getString("SchedDialog.radioButton.freqOccur"));
        jCheckBox1.setText( SanBootView.res.getString("SchedDialog.check.active") );
    }

    private void regKeyboardAction(){
        jTextField1.registerKeyboardAction(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    jComboBox1.requestFocusInWindow();
                }
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
        
        jComboBox1.registerKeyboardAction(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    jRadioButton3.requestFocusInWindow();
                }
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }
    
    private void hideProfUI(){
        jPanel3.remove( this.jLabel2 );
        jPanel3.remove( this.jComboBox1 );
        jPanel3.remove( this.jLabel5 );
        jPanel3.remove( this.jComboBox6 );
        jPanel3.remove( this.jLabel6 );
        jPanel3.remove( this.jTextField2 );
    }
    
    UniProfile fixedProf = null;// 只为该profile增加调度
    private void setupProfileComboBox( UniProfile prof ){
        ArrayList list = view.initor.mdb.getAllProfile();
        int size = list.size();
        
        Vector<UniProfile> tmp = new Vector<UniProfile>( size );
        for( int i=0; i<size; i++ ){
            UniProfile profile =(UniProfile)list.get(i); 
//System.out.println(" profile name : \n"+profile.toString()+" indx: "+i);                      
            if( prof != null ){
                if( prof.toString().equals( profile.toString() ) ){              
                    fixedProf = profile;             
                }
            }
            
            // 不检查复制策略的合法性了。因为要依次获取每个windows agent的windows dir,如果该机器没开机，则非常的慢(2009/6/22)
            //if( view.initor.mdb.checkProfile( profile ) ){
                // 只加合格的 profile
                tmp.addElement( profile );
            //}else{
//System.out.println(" NOT quilified profile: "+profile.toString() );
            //}
        }
        
        ComboBoxModel model = new DefaultComboBoxModel( tmp );
        jComboBox1.setModel( model );
    }
    
    private void  profileComboBox_actionPerformed(){
        Object obj = jComboBox1.getSelectedItem();
        if( obj == null ) return;
        
        UniProfile profile      = (UniProfile)obj;
        UniProIdentity identity = profile.getUniProIdentity();
        
        String level = identity.getSchLevel();
        if( level.equals("0") ){
            jComboBox6.setSelectedItem(
                SanBootView.res.getString(BakObject.BAKLEVEL_FULL)
            );
        }else if( level.equals("1")){
            jComboBox6.setSelectedItem(
                SanBootView.res.getString(BakObject.BAKLEVLE_INC)
            );
        }else{
            jComboBox6.setSelectedItem(
                BakObject.BAKLEVEL_UNKNOWN
            );
        }
        
        String clnt_id = identity.getClntID();
        BackupClient bkClnt = view.initor.mdb.getClientFromVectorOnID( clnt_id );
        if( bkClnt != null ){
            // 尽量使用BootHost的信息
            BootHost host = view.initor.mdb.getHostFromCacheOnUUID( bkClnt.getUUID() );
            if( host != null ){
                jTextField2.setText( host.getIP() );
                jTextField2.setToolTipText( host.getName() +"["+host.getIP()+"]" );
            }else{
                jTextField2.setText( bkClnt.getIP() );
                jTextField2.setToolTipText( bkClnt.getHostName() +"["+bkClnt.getIP()+"]" );
            }
        }else{
            jTextField2.setText("N/A");
            jTextField2.setToolTipText("N/A");
        }
    }
    
    private void setupBakLevelComboBox(){
        Object[] tmp = new Object[3];
        tmp[0] = SanBootView.res.getString(BakObject.BAKLEVEL_FULL);
        tmp[1] = SanBootView.res.getString(BakObject.BAKLEVLE_INC);
        tmp[2] = BakObject.BAKLEVEL_UNKNOWN;
        ComboBoxModel model = new DefaultComboBoxModel( tmp );
        jComboBox6.setModel( model );
        jComboBox6.setEnabled( false );
    }
     
    private void setupDefault(){
        if( sched == null ){ // new a scheduler
            jRadioButton3.setSelected( true );
            ((TitledBorder)jPanel8.getBorder()).setTitle(
                SanBootView.res.getString("SchedDialog.borderTitle.daily")
            );
            jRadioButton1.setSelected( true );
            enableFreqOccurAt( false );
            
            if( fixedProf != null ){
                jComboBox1.setSelectedItem( fixedProf );
                jComboBox1.setEnabled( false );
            }else{
                if( jComboBox1.getItemCount() >0 ){
                   jComboBox1.setSelectedIndex( 0 );
                }
            }
            jCheckBox1.setSelected( true );
        }else{  // modify a scheduler
            jTextField1.setText( sched.getName() );
            
            UniProfile profile = view.initor.mdb.getOneProfile( sched.getProfName() );
            if( profile !=null ){
                jComboBox1.setSelectedItem( profile );
            }
            if( fixedProf != null ){
                jComboBox1.setEnabled( false );
            }
            jCheckBox1.setSelected( sched.isEnable() );
            initCronScheduler( sched );
        }
    }
    
    private void initCronScheduler( DBSchedule sched ) {
        String time = sched.getTimeStr();

        int type = CronSchedule.getCronSchedulerType( time );
        if( type == CronSchedule.TYPE_DAILY ){
            jRadioButton3.setSelected( true );
            ((TitledBorder)jPanel8.getBorder()).setTitle(
                SanBootView.res.getString("SchedDialog.borderTitle.daily")
            );
        }else if( type == CronSchedule.TYPE_MONTHLY ){
            jRadioButton5.setSelected( true );
            monthlyRadioButton_actionPerformed();
            Vector mlist = sched.getMonthdayList();
            monthPane.initMonthPane( mlist );
        }else {
            jRadioButton4.setSelected( true );
            weeklyRadioButton_actionPerformed();
            Vector dlist = sched.getWeekdayList();
            weekPane.initWeekPane( dlist );
        }

        type = CronSchedule.getDailyFreqOfCronScheduler( time );
        if( type == CronSchedule.TYPE_DAILY_FREQ ){
            jRadioButton2.setSelected( true );
            enableOnceOccurAt( false );

            type = CronSchedule.getDailyFreqOccurType( time );
            if( type == CronSchedule.TYPE_DAILY_HOUR_OCR ){
                jComboBox5.setSelectedItem(
                    SanBootView.res.getString(SchedDialog.TIME_UNIT_HOUR)
                );
                jSpinner3.setValue( sched.getHourFreqVal() );
            }else{
                jComboBox5.setSelectedItem(
                    SanBootView.res.getString(SchedDialog.TIME_UNIT_MIN)
                );
                jSpinner3.setValue( sched.getMinuteFreqVal() );
            }
        }else{
            jRadioButton1.setSelected( true );
            enableFreqOccurAt( false );
            jSpinner1.setValue( sched.getIntHour() );
            jSpinner2.setValue( sched.getIntMin() );
        }
    }

    private void setupTime(){
        jSpinner1.setModel( new SpinnerNumberModel( 0,0,23,1 ) );
        jSpinner2.setModel( new SpinnerNumberModel( 0,0,59,1 ) );
        jComboBox5.addItem(
            SanBootView.res.getString(SchedDialog.TIME_UNIT_HOUR)
        );
        jComboBox5.addItem(
            SanBootView.res.getString(SchedDialog.TIME_UNIT_MIN)
        );
    }

    void dailyRadioButton_actionPerformed() {
        jPanel8.removeAll();
        ((TitledBorder)jPanel8.getBorder()).setTitle(
                SanBootView.res.getString("SchedDialog.borderTitle.daily")
        );
        jPanel8.validate();
        jPanel8.repaint();
    }

    void weeklyRadioButton_actionPerformed() {
        jPanel8.removeAll();
        ((TitledBorder)jPanel8.getBorder()).setTitle(
            SanBootView.res.getString("SchedDialog.borderTitle.weekly")
        );
        jPanel8.add(weekPane,BorderLayout.CENTER);
        jPanel8.validate();
        jPanel8.repaint();
    }

    void monthlyRadioButton_actionPerformed() {
        jPanel8.removeAll();
        ((TitledBorder)jPanel8.getBorder()).setTitle(
            SanBootView.res.getString("SchedDialog.borderTitle.monthly")  
        );
        jPanel8.add( monthPane,BorderLayout.CENTER );
        jPanel8.validate();
        jPanel8.repaint();
    }

    private void enableFreqPane(boolean val ){
        this.enableFreqTypePane( val );

        if (jPanel8.getComponentCount() >0 ){
            Component com = jPanel8.getComponent(0);
            if( com instanceof MonthlyPane ){
                monthPane.enableMonthlyPane( val );
            }else if ( com instanceof WeeklyPane ){
                weekPane.enableOwn( val );
            }
        }

        enableDailyFreqPane( val );
    }

    private void enableFreqTypePane(boolean val ){
        jRadioButton3.setEnabled( val );
        jRadioButton4.setEnabled( val );
        jRadioButton5.setEnabled( val );
    }

    private void enableDailyFreqPane(boolean val ){
        jRadioButton2.setEnabled( val );
        jRadioButton1.setEnabled( val );
        enableFreqOccurAt( val );
        enableOnceOccurAt( val );
        if( val ){
            if( jRadioButton2.isSelected() ){
                enableOnceOccurAt( false);
            }else{
                enableFreqOccurAt( false);
            }
        }
    }
    
    void onceOccurRadioButton_actionPerformed(ActionEvent e) {
        enableOnceOccurAt( true );
        enableFreqOccurAt( false );
    }

    private void enableOnceOccurAt(boolean val ){
        jSpinner1.setEnabled( val );
        jSpinner2.setEnabled( val );
        jLabel3.setEnabled( val );
        jLabel4.setEnabled( val );
    }

    private void enableFreqOccurAt(boolean val){
        jSpinner3.setEnabled( val );
        jComboBox5.setEnabled( val );
    }

    void freqOccurRadioButton_actionPerformed(ActionEvent e) {
        this.enableFreqOccurAt( true);
        this.enableOnceOccurAt( false );
    }
    
    public Object[] getValues(){
        return values;
    }
    
    void okButton_actionPerformed(ActionEvent e) {
        // 字符,<32,不含' " 空格,,非空
        String name = jTextField1.getText().trim();
        if( name.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("SchedDialog.errmsg.noneName")
            );
            return;
        }
        
        if( Check.checkInput( name ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("SchedDialog.errmsg.badName")
            );
            return;
        }
        
        if( name.getBytes().length>=32 ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("SchedDialog.errmsg.tooLargeName")
            );
            return;
        }
        
        UniProfile profile = null;
        if( this.displayProf ){
            Object _profile = jComboBox1.getSelectedItem();
            if( _profile == null ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("SchedDialog.errmsg.noneProfile")
                );
                return;
            }
            profile = (UniProfile)_profile;
        }
          
        String[] info = getDailyFreqInfo();
        String minute = info[0];
        String hour   = info[1];

        String daymonth = "";
        String month = "";
        String dayweek = "";
        if( jRadioButton3.isSelected() ){
            daymonth = "*"; // 天/月
            month = "*";    // 月
            dayweek = "*";  // 天/周
        }else if ( jRadioButton4.isSelected() ){
            daymonth = "*";
            month = "*";
            dayweek = this.weekPane.getInfo();
            if( dayweek.equals("")){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("SchedDialog.errmsg.noDayofWeek")
                );
                return;
            }
        }else{
            daymonth = this.monthPane.getInfo();
            if( daymonth.equals("") ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("SchedDialog.errmsg.noDayofMonth")
                );
                return;
            }
            month = "*";
            dayweek = "*";
        }
        
        String schLevel="";
        String profName="";
        int bkobj_id = -1;
        if( this.displayProf ){
            UniProIdentity identity = profile.getUniProIdentity();
            schLevel = identity.getSchLevel();
            profName = profile.getProfileName();
            bkobj_id = identity.getIntBkObj_ID();
            if( bkobj_id < 0 ){
SanBootView.log.error( getClass().getName(),"bkobj id is invalid in profile: "+ profile.toString() ); 
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("SchedDialog.errmsg.invalidObj")
                );
                return;
            }
        }
        
        DBSchedule dbScheduler = new DBSchedule(
            -1L,
            name,
            DBSchedule.SCH_NORMAL_BACKUP, // normal backup
            minute,
            hour,
            daymonth,
            month,
            dayweek,
            schLevel,
            ( jCheckBox1.isSelected()? 1 : -1 ),
            -1,
            bkobj_id,
            profName
        );
        
        if( diag!= null ){
            if( diag.isSameScheduler( dbScheduler ) ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("SchedDialog.errmsg.sameSched")
                );
                return;
            }
        }else{
            if( this.sched != null ){
                dbScheduler.setID( sched.getID() );
            }
            if( view.isSameScheduler( dbScheduler ) ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("SchedDialog.errmsg.sameSched")
                );
                return;
            }
        }
        
        values = new Object[1];
        values[0] = dbScheduler;
        this.dispose();
    }

    private String[] getDailyFreqInfo(){
        String[] aValues = new String[2];

        if( jRadioButton1.isSelected() ){
            aValues[0] = ((Integer)jSpinner2.getValue()).toString();   // 分
            aValues[1] = ((Integer)jSpinner1.getValue()).toString();   // 钟
        }else{ //每日周期发生
            int num  = ((Integer)jSpinner3.getValue()).intValue();
            String unit = (String)jComboBox5.getSelectedItem();
            if( unit.equals( SanBootView.res.getString(SchedDialog.TIME_UNIT_HOUR))){
                aValues[0] = "0";
                if( num == 1  ){
                    aValues[1] = "*";
                }else{
                    //每num小时发生一次
                    aValues[1] = getHourFreq( num );
                }
            }else{
                if( num == 1 ){
                    aValues[0] = "*";
                }else{
                    //每num分钟发生一次 
                    aValues[0] = getMinuteFreq( num );
                }
                aValues[1] = "*";
            }
        }

        return aValues;
    }
    
    void unitComboBox_actionPerformed(ActionEvent e) {   
        String unit = (String)jComboBox5.getSelectedItem();
        if( unit.equals( SanBootView.res.getString(SchedDialog.TIME_UNIT_HOUR))){
            jSpinner3.setModel( new SpinnerNumberModel(1,1,23,1) );
        }else{
            jSpinner3.setModel( new SpinnerNumberModel(1,1,59,1) );
        }
    }
  
    private String getHourFreq(int step){
        String ret="";
        boolean isFirst = true;

        for(int i=0;i<24;i+=step){
            if( isFirst ){
                ret = i+"";
                isFirst = false;
            }else{
                ret += ","+i;
            }
        }
        return ret;
    }
  
    private String getMinuteFreq(int step){
        String ret="";
        boolean isFirst = true;

        for(int i=0;i<60;i+=step){
            if( isFirst ){
                ret = i+"";
                isFirst = false;
            }else{
                ret += ","+i;
            }
        }

        return ret;
    }
}
