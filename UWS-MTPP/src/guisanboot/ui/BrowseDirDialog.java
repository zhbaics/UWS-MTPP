/*
 * BrowseServerDialog.java
 *
 * Created on April 6, 2005, 10:37 AM
 */

package guisanboot.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import guisanboot.res.*;

/**
 *
 * @author  Administrator
 */
public class BrowseDirDialog extends javax.swing.JDialog {
    private String curPath = "/";
    private String rootPath ="/";
     
    /** Creates new form BrowseServerDialog */
    public BrowseDirDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public BrowseDirDialog(
        SanBootView view, 
        boolean onlyDir, 
        String curPath 
    ){
        this( view,onlyDir,-1L,false, "/",curPath );
    }
    
    public BrowseDirDialog(
        SanBootView view, 
        boolean onlyDir, 
        long agentId,
        boolean isSyncNode,
        String rootPath,
        String curPath
    ){
        this( view,true );
        myInit( view, onlyDir, agentId, isSyncNode,rootPath,curPath );
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
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel6.setPreferredSize(new java.awt.Dimension(15, 10));
        jPanel4.add(jPanel6, java.awt.BorderLayout.WEST);

        jPanel7.setPreferredSize(new java.awt.Dimension(15, 10));
        jPanel4.add(jPanel7, java.awt.BorderLayout.EAST);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 1, 5, 1)));
        jPanel8.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 1, 5, 1)));
        jPanel5.setPreferredSize(new java.awt.Dimension(375, 55));
        jLabel1.setText("Enter Path :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel5.add(jLabel1, gridBagConstraints);

        jTextField1.setPreferredSize(new java.awt.Dimension(355, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel5.add(jTextField1, gridBagConstraints);

        jPanel1.add(jPanel5, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(3, 1, 3, 1)));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 45, 5));

        jPanel2.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(3, 1, 3, 1)));
        jButton1.setText("Done");
        jButton1.setPreferredSize(new java.awt.Dimension(75, 24));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton1);

        jButton2.setText("Stop");
        jButton2.setPreferredSize(new java.awt.Dimension(75, 24));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton2);

        jButton3.setText("Cancel");
        jButton3.setPreferredSize(new java.awt.Dimension(75, 24));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton3);

        jPanel3.add(jPanel2, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jSeparator1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
          values = null;
          this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if( geter!=null ){
            // 设置destroy标志,使得运行cmd的sunprocess被destroied.
            geter.setDestroyFlag( true );
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int index = this.fileList.getSelectedIndex();
        if( index < 0 ) {
            JOptionPane.showMessageDialog(this,
                SanBootView.res.getString("SelectSrcDialog.errmsg.notSel")
            );
            return;
        }
        
        values = new Object[1];
        values[0] = curPath+(String)fileModel.getElementAt(index);
        
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new BrowseDirDialog(new javax.swing.JFrame(), true).setVisible( true );
    }
    
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    
    SanBootView view;
    Object[] values;
    DefaultListModel fileModel = new DefaultListModel();
    JList fileList = new JList( fileModel );
    Threadable geter;
    boolean onlyDir;
    long agentId;
    boolean isSyncNode;
    
    private void myInit( 
        SanBootView _view,
        boolean _onlyDir,
        long _agentId,
        boolean _isSyncNode,
        String _rootPath,
        String _curPath
    ){
        view = _view;
        onlyDir = _onlyDir;
        agentId = _agentId;
        isSyncNode = _isSyncNode;
        rootPath = _rootPath;
        curPath = _curPath;
        
        setupLanguage();
        
        jTextField1.setText( curPath );
        
        this.jScrollPane1.getViewport().add( fileList,null );
        this.fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        this.fileList.setCellRenderer( new FileDirRenderer() );
        
        this.fileList.addMouseListener( new MouseAdapter(){
            @Override public void mouseClicked( MouseEvent e){
                doubleClickPrcess(e);
            }
        });
        
        jTextField1.setEnabled( false );
        jButton2.setEnabled( false );
        
        regKeyboardAction();
    }
    
    private void setupLanguage(){
        this.setTitle(SanBootView.res.getString("BrowseServerDirDialog.title"));
        this.jLabel1.setText(SanBootView.res.getString("BrowseServerDirDialog.label.enterpath"));
        this.jButton1.setText(SanBootView.res.getString("BrowseServerDirDialog.button.done"));
        this.jButton2.setText(SanBootView.res.getString("BrowseServerDirDialog.button.stop"));
        this.jButton3.setText(SanBootView.res.getString("common.button.cancel"));
    }
    
    public void setEnabledTF1(){
        this.jTextField1.setEnabled( true );
        this.jButton2.setEnabled( true );
    }
     
    private void doubleClickPrcess(  MouseEvent  e){
        if( e.getClickCount() >= 2 ){
            if( this.geter!=null && !geter.isOver() ){
                return;
            }
            
            int index = fileList.locationToIndex( e.getPoint() );
            String file = (String)fileModel.getElementAt( index );
                    
//System.out.println("cur sel file: "+file);
//System.out.println("cur path: "+curPath);

            if( file.equals("../") ){
                if( curPath.equals("/") ){
                    return;
                }else{
                    curPath = this.getFatherPath();
                }
            }else if( this.isDir( file ) ){
                curPath = curPath+file;
            }else{
                return;
            }
            
            fileModel.removeAllElements();
            jTextField1.setText( curPath );
            
            geter = new BrowseDirGeter(
                curPath, 
                fileModel, 
                this,
                view.getSocket(), 
                false,
                0,
                onlyDir,
                agentId,
                isSyncNode, 
                curPath.equals( rootPath )?true:false
            );
            geter.startRun();
        }
    }
    
    private void regKeyboardAction(){
        jTextField1.registerKeyboardAction(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    // 开始传输输入路径的文件
                    keyboardPress();
                }
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private void keyboardPress(){
        if( this.geter!=null && !geter.isOver() ){
            return;
        }
        
        // 不检查path是否存在.如果不存在,则服务器会报错
        // 错误消息会出现在GUI上
        String path = jTextField1.getText().trim();
        if( path.equals("") ){
            path = "/";
        }
        
        if( !this.isDir( path ) ){
            path = path +"/";
        }
        
        curPath = path;
        
        fileModel.removeAllElements();
        jTextField1.setText( curPath );
        
        geter = new BrowseDirGeter(
            curPath, 
            fileModel, 
            this,
            view.getSocket(), 
            false,
            0,
            onlyDir,
            agentId,
            isSyncNode, 
            curPath.equals( rootPath )?true:false
        );
        geter.startRun();
    }
    
    public DefaultListModel getFileModel( ){
        return this.fileModel;
    }
    
    public void setBrowseDirGeter( BrowseDirGeter _geter ){
        geter = _geter;
    }
    
    public Object[] getValues(){
        return values;
    }
    
    private boolean isDir(String file){        
        int length = file.length();
        if( file.charAt(length-1) == '/' )
            return true;
        else
            return false;
    }
    
    private String getFatherPath(){
        String ret = "/";
        
        int index = curPath.lastIndexOf("/");
        if( index >=0 ){
            try{
                ret = curPath.substring( 0,index );
                index = ret.lastIndexOf("/");
                if( index >=0 ){
                    ret = ret.substring(0,index)+"/";
                }
            }catch(Exception ex){
                ret = "/";
            }
        }
        
        return ret;
    }
}

class FileDirRenderer extends DefaultListCellRenderer{ 
    public Component getListCellRendererComponent( JList list,
                                                 Object value,
                                                 int index,
                                                 boolean isSelected,
                                                 boolean cellHasFocus){
        super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
        String file =(String)value;
        if( file.length() >0 ){
            if( file.charAt( file.length()-1 ) == '/' ){
                setIcon( ResourceCenter.SMALL_ICON_FOLDER );
            }else{
                setIcon( ResourceCenter.SMALL_ICON_FILE );
            }
        }else{
            setIcon( ResourceCenter.SMALL_ICON_FILE );
        }
        setText( file );
        return this;
    }
}