/*
 * AboutDialog.java
 *
 * Created on October 19, 2004, 1:58 PM
 */

package guisanboot.ui;

import javax.swing.*;
import guisanboot.res.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author  jjzhang
 */
public class AboutDialog extends javax.swing.JDialog {
    String name;
    Icon   icon;
    
    public AboutDialog(java.awt.Frame owner, String title, boolean modal, String name, Icon icon) {
        super( owner,title,modal );
        try {
            initComponents();
            myInit( (SanBootView)owner,name,icon );
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Creates new form AboutDialog */
    public AboutDialog( SanBootView view,String name,Icon icon) {
        this(view,"",true,name,icon);
    }
    
    void myInit(SanBootView view,String _name,Icon _icon){
        this.name = _name;
        this.icon = _icon;
        this.jLabel1.setIcon(this.icon);
        jLabel2.setText(this.name);
        setupLanguage( view );
        jButton1.requestFocus();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 22));
        jPanel5.add(jLabel2);
        jLabel2.setBounds(80, 10, 280, 30);

        jLabel3.setText("CopyRight (C) 2002-2004 Odysys inc.");
        jPanel5.add(jLabel3);
        jLabel3.setBounds(80, 70, 280, 18);

        jLabel4.setText("Version 1.0.0");
        jPanel5.add(jLabel4);
        jLabel4.setBounds(80, 50, 270, 18);

        jPanel5.add(jLabel1);
        jLabel1.setBounds(10, 10, 70, 90);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jButton1.setText("OK");
        jButton1.setName("okButton");
        jButton1.setPreferredSize(new java.awt.Dimension(79, 27));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel3.add(jButton1);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.add(jSeparator1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents
    
    void setupLanguage( SanBootView view ){
        String s1;
        String buildtime="";
        StringBuffer buf = new StringBuffer();
        
        try { 
            InputStream is = ResourceCenter.class.getResourceAsStream("buildtime");
            BufferedReader br = new BufferedReader( new InputStreamReader(is) );
            while( ( s1=br.readLine() ) != null ){
                if( s1.equals("") ) continue;
                s1 = s1.trim();
                buf.append( s1 );
            }
            buildtime = buf.toString();
        }catch(Exception ex){
SanBootView.log.error( getClass().getName(),"Can't get buildtime info from jar.");                  
            buildtime="";
        }
        
        setTitle( SanBootView.res.getString("AboutDialog.dialog_title") );
        jLabel3.setText( SanBootView.res.getString("AboutDialog.copyright") );
        if( view.getMode() == ResourceCenter.MODE_DEMO ){
            if( buildtime.equals("") ){
                jLabel4.setText( SanBootView.res.getString("AboutDialog.version")+" ( DEMO Version )");
            }else{
                jLabel4.setText( SanBootView.res.getString("AboutDialog.version")+" ( DEMO Version build:"+buildtime+" )");
            }
        }else{
            if( buildtime.equals("") ){
                jLabel4.setText( SanBootView.res.getString("AboutDialog.version"));
            }else{
                jLabel4.setText( SanBootView.res.getString("AboutDialog.version")+" ( build:"+buildtime+" )");
            }
        }
        jButton1.setText( SanBootView.res.getString("common.button.ok"));
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new AboutDialog(null,"",null).setVisible( true );
    }
    
    // 变量声明 - 不进行修改//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    // 变量声明结束//GEN-END:variables
  }
