/*
 * CreateSnapDialog.java
 *
 * Created on 2006/12/28, 4:17 PM
 */

package guisanboot.ui;

import guisanboot.data.IdleDisk;
import guisanboot.data.IdleDiskWrapper;
import guisanboot.res.ResourceCenter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;
import mylib.tool.Check;


/**
 *
 * @author  Administrator
 */
public class CreatePoolDialog extends javax.swing.JDialog {
    
    /** Creates new form CreateSnapDialog */
    public CreatePoolDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public CreatePoolDialog( SanBootView view,float vgSize,ArrayList nameList ,ArrayList idleDiskList){
        this( view,true );
        myInit( view,vgSize,nameList ,idleDiskList );
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
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Name :");
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

        jLabel2.setText("Size :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);

        jTextField2.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel1.add(jTextField2, gridBagConstraints);

        jLabel3.setText("Password :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        jPasswordField1.setPreferredSize(new java.awt.Dimension(81, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel1.add(jPasswordField1, gridBagConstraints);

        jLabel4.setText("Password Again :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        jPasswordField2.setPreferredSize(new java.awt.Dimension(81, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel1.add(jPasswordField2, gridBagConstraints);

        jLabel5.setText("Pool Type");
        jLabel5.setMaximumSize(new java.awt.Dimension(96, 15));
        jLabel5.setMinimumSize(new java.awt.Dimension(96, 15));
        jLabel5.setPreferredSize(new java.awt.Dimension(96, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel1.add(jLabel5, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel1.add(jComboBox1, gridBagConstraints);

        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jCheckBox1, gridBagConstraints);

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jButton3, gridBagConstraints);

        jTextField3.setText("jTextField3");
        jTextField3.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jTextField3, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 1, 2, 1));
        jPanel3.setPreferredSize(new java.awt.Dimension(173, 40));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 35, 5));

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
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        values = null;
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String _name = jTextField1.getText().trim();
        if( _name.equals("") ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("CreateSnapDialog.error.nonePoolName")
            );
            return;
        }
        
        if( Check.checkInput( _name ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("CreateSnapDialog.error.badPoolName")
            );
            return;
        }
        
        if( _name.getBytes().length >=255 ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("CreateSnapDialog.error.poolNametoolong")
            );
            return;
        }
        
        if( isSamePoolName( _name ) ){
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("CreateOrphanVol.error.sameName")
            );
            return;
        }
        
        int type =0 ;
        String strTmp = jComboBox1.getSelectedItem().toString();
        if(strTmp.equals(SanBootView.res.getString("CreatePoolDialog.label.normalPool"))){
            type = 0 ;
        } else {
            type = 1 ;
        }
        
        float fval = -1;
        if( type == 0) {
            if(!jCheckBox1.isSelected()){
                String _val = jTextField2.getText().trim();
                if( _val.equals("") ){
                    JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("CreateOrphanVol.error.nonePoolVal")
                    );
                    return;
                }


                try{
                    fval = Float.parseFloat( _val );
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("CreateOrphanVol.error.invalidPoolVal")
                    );
                    return;
                }

                if( fval <= 0 || fval > vgSize ){
                    JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("CreateOrphanVol.error.invalidPoolVal")
                    );
                    return;
                }
            } else { //利用空闲盘创建普通存储池
                if(selList != null && selList.size() > 0 ) {
                    String _val = jTextField2.getText().trim();
                    if( _val.equals("") ){
                        int ret = JOptionPane.showConfirmDialog(
                            view,
                            SanBootView.res.getString("CreateNormalPool.confirm"),
                            SanBootView.res.getString("common.confirm"),  //"Confirm",
                            JOptionPane.OK_CANCEL_OPTION
                        );
                        if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
                            return;
                        }
                    } else {
                        try{
                            fval = Float.parseFloat( _val );
                        }catch(Exception ex){
                            JOptionPane.showMessageDialog(this,
                                SanBootView.res.getString("CreateOrphanVol.error.invalidPoolVal")
                            );
                            return;
                        }

//                        if( fval <= 0 || fval > vgSize ){
//                            JOptionPane.showMessageDialog(this,
//                                SanBootView.res.getString("CreateOrphanVol.error.invalidPoolVal")
//                            );
//                            return;
//                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("CreateOrphanVol.error.noselectIdleDisk")
                    );
                    return;
                }
            }
        } else {
            if(!( selList != null && selList.size() > 0 )){
                JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("CreateOrphanVol.error.noselectIdleDisk")
                    );
                    return;
            }
        }
        
        String pwd1 = new String ( jPasswordField1.getPassword() );
        if( !pwd1.equals("") ){
            String pwd2 = new String( jPasswordField2.getPassword() );
            if( !pwd1.equals( pwd2 ) ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("CreateOrphanVol.error.pwdMisMatched")
                );
                return;
            }
        }
        
        
        
        
        
        values = new Object[5];
        values[0] = _name;
        values[1] = new Float( fval );
        values[2] = pwd1;
        values[3] = new Integer( type );
        values[4] = selList;
        
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        changeAction();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        select_process();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        checked_process();
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    
    public void checked_process(){
        if(jCheckBox1.isSelected()){
            jButton3.setEnabled(true);
        } else {
            jButton3.setEnabled(false);
            selList.clear();
            jTextField3.setText("");
        }
    }
    
    private ArrayList selList = new ArrayList() ;
    public void select_process(){
        SelectIdleDiskDialog dialog = new SelectIdleDiskDialog(view , idleDiskList);
        int width = 275+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 300+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
        
        Object[] ret = dialog.getValues();
        if( ret == null || ret.length <= 0 ) return;
        
        jTextField3.setText("");
        selList = (ArrayList)ret[0];
        int count = selList.size();
        StringBuffer tmpStr = new StringBuffer();
        if( count > 0 ){
            for ( int i = 0 ; i < count ; i++ ){
                IdleDisk tmpDisk = (IdleDisk)selList.get(i);
                tmpStr.append(tmpDisk.getIdleDiskName()).append(";");
            }
            jTextField3.setText(tmpStr.toString());
        } else {
            jTextField3.setText("");
        }
        
    }
    
    public void changeAction(){
        Object tmpObj = jComboBox1.getSelectedItem();
        String tmp = "";
        if(tmpObj != null){
            tmp = tmpObj.toString();
        }
        if( tmp.equals( SanBootView.res.getString("CreatePoolDialog.label.normalPool") ) ){
            jCheckBox1.setSelected(false);
            jButton3.setEnabled(false);
            jTextField2.setEnabled(true);
            jTextField3.setText("");
            selList.clear();
        } else {
            jCheckBox1.setSelected(true);
            jButton3.setEnabled(true);
            jTextField2.setEnabled(false);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreateSnapDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
    
    private Object[] values=null;
    private SanBootView view;
    private float vgSize;
    private ArrayList nameList;
    private ArrayList idleDiskList;
    
    private void myInit( SanBootView view,float vgSize,ArrayList nameList ,ArrayList idleDiskList){
        this.view = view;
        this.vgSize = vgSize;
        this.nameList = nameList;
        this.idleDiskList = idleDiskList ;
        
        initJComboBox();
        
        if( vgSize == -1 ){
            jLabel2.setText(
                SanBootView.res.getString("CreatePoolDialog.label.size") 
            );
        }else{
            jLabel2.setText(
                SanBootView.res.getString("CreatePoolDialog.label.size") + 
                SanBootView.res.getString("CreateOrphanVol.label.max") + 
                vgSize + "GB)" 
            );
        }
        jTextField3.setEnabled(false);
        jTextField3.setText("");
        regKeyboardAction();
        setupLanguage();
    }
    
    public void initJComboBox(){
        jComboBox1.removeAllItems();
        jComboBox1.addItem(SanBootView.res.getString("CreatePoolDialog.label.normalPool"));
        jComboBox1.addItem(SanBootView.res.getString("CreatePoolDialog.label.UCSPool"));
        
    }
    
    
    private void setupLanguage(){
        setTitle( SanBootView.res.getString("CreatePoolDialog.title"));
        jLabel1.setText( SanBootView.res.getString("CreatePoolDialog.label.name"));
        jLabel3.setText( SanBootView.res.getString("CreatePoolDialog.label.pwd"));
        jLabel4.setText( SanBootView.res.getString("CreatePoolDialog.label.pwd1")); 
        jLabel5.setText(SanBootView.res.getString("CreatePoolDialog.label.poolType"));
        jCheckBox1.setText(SanBootView.res.getString("CreatePoolDialog.label.idleDisk"));
        jButton3.setText(SanBootView.res.getString("CreatePoolDialog.label.select"));
        jButton1.setText( SanBootView.res.getString("common.button.ok"));
        jButton2.setText( SanBootView.res.getString("common.button.cancel"));
    }
    
    private void regKeyboardAction(){            
        jTextField1.registerKeyboardAction(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    jTextField2.requestFocusInWindow();
                }
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
        
        jTextField2.registerKeyboardAction(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    jPasswordField1.requestFocus();
                }
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true),
            //JComponent.WHEN_IN_FOCUSED_WINDOW
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
        
        jPasswordField1.registerKeyboardAction(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    jPasswordField2.requestFocus();
                }
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true),
            //JComponent.WHEN_IN_FOCUSED_WINDOW
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
        
        jPasswordField2.registerKeyboardAction(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    jButton1.requestFocus();
                }
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true),
            //JComponent.WHEN_IN_FOCUSED_WINDOW
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }
    
    private boolean isSamePoolName( String name ){
        int size = nameList.size();
        for( int i=0; i<size; i++ ){
            String n = (String)nameList.get(i);
            if( n.equals( name ) ){
                return true;
            }
        }
        return false;
    }
    
    public Object[] getValues(){
        return values;
    }
}
