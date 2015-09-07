/*
 * WizardDialogSample.java
 *
 * Created on December 13, 2004, 11:39 AM
 */

package guisanboot.ui;

import guisanboot.data.DestAgent;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.util.regex.*;

import guisanboot.res.*;
import guisanboot.tool.Tool;
import mylib.UI.ImagePanel;

/**
 *
 * @author  Administrator
 */
public class WizardDialogSample extends javax.swing.JDialog {
    
    /** Creates new form WizardDialogSample */
    public WizardDialogSample(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public WizardDialogSample(SanBootView view){
        this( view,true );
        myInit( 0 );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel5 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jSeparator1, java.awt.BorderLayout.NORTH);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 1, 5, 1));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton1.setText("Back");
        jButton1.setPreferredSize(new java.awt.Dimension(75, 24));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton2.setText("Next");
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

        jPanel6.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 1, 5, 1));
        jPanel7.setPreferredSize(new java.awt.Dimension(120, 10));

        jButton4.setText("Delete");
        jButton4.setPreferredSize(new java.awt.Dimension(75, 24));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton4);

        jPanel6.add(jPanel7, java.awt.BorderLayout.WEST);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 100));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane1.setViewportView(jTextPane1);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setPreferredSize(new java.awt.Dimension(165, 10));
        jPanel5.setLayout(new java.awt.BorderLayout());
        jPanel3.add(jPanel5, java.awt.BorderLayout.WEST);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jButton4Process();
    }//GEN-LAST:event_jButton4ActionPerformed
    
    // cancel
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        cancelButtonProcess();
    }//GEN-LAST:event_jButton3ActionPerformed
    
    // next
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        nextButtonProcess();
    }//GEN-LAST:event_jButton2ActionPerformed
    
    // back
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        backButtonProcess();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        formWinClosing();
    }//GEN-LAST:event_formWindowClosing
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new WizardDialogSample(new javax.swing.JFrame(), true).setVisible( true );
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
    
    // JPanel3是整个dialog的主panel

    ////////////////////////////////////////////////////////
    //
    //  Content Wizard 布局(除了首尾的其他幅)
    //
    /////////////////////////////////////////////////////////
    JPanel topPane = new JPanel();
    JScrollPane jScrollPane2 = new JScrollPane();
    //jTextPane2是jScrollPane2上的 TextPane
    JTextPane jTextPane2 = new javax.swing.JTextPane();
    
    JPanel iconPane = new JPanel();
    JLabel iconLabel = new JLabel();
    
    JPanel bigContentPane = new JPanel();
    JPanel contentPane = new JPanel();
    
    ////////////////////////////////////////////////////////
    //
    //    Start-end Wizard tip panel的布局(第一幅和最后一幅)
    //
    /////////////////////////////////////////////////////////
    //jPanel5 是imagePane( 位于WEST )
    // imagepanel1是jPanel5上的image panel
    ImagePanel imagepanel1 = new ImagePanel( null, ImagePanel.TILED ); // left component of splitpane

    //jPanel4 是start-end wizard tip text panel( 位于CENTER )
    //jScrollPane1是jPanel4上的滚动pane
    //jTextArea1是jScrollPane1上的TextArea
     
    protected boolean isWrFirst = true;
    protected StringBuffer logBuf = new StringBuffer();
    public void writeLogBuf( String errMsg,int which ){
    }

    private void myInit( int mode ){
        // 首先生成 content wizard所需要的部件
        topPane.setLayout( new java.awt.BorderLayout() );
        topPane.setPreferredSize(new java.awt.Dimension(10,58));
        jScrollPane2.setBorder( null );
        topPane.add( jScrollPane2,BorderLayout.CENTER );
        topPane.add( iconPane,BorderLayout.EAST );
        jScrollPane2.setViewportView( jTextPane2 );
        iconPane.setLayout( new java.awt.BorderLayout() );
        iconPane.setBackground( Color.white );
        iconLabel.setIcon( ResourceCenter.BTN_ICON_LOGO );
        iconPane.add( iconLabel,BorderLayout.CENTER );
        iconPane.add( new JLabel("  "),BorderLayout.EAST );
        bigContentPane.setLayout( new java.awt.BorderLayout() );
        bigContentPane.add(new javax.swing.JSeparator(),BorderLayout.NORTH );
        bigContentPane.add( contentPane,BorderLayout.CENTER );
        contentPane.setLayout( new java.awt.BorderLayout() );
        
        jTextPane1.setBackground( Color.white );
        jTextPane1.setEnabled( false );
        jTextPane2.setEnabled( false );
        jTextPane2.setBackground( Color.white );
        jTextPane1.setDisabledTextColor( Color.BLACK );
        jTextPane2.setDisabledTextColor( Color.BLACK );
        
        imagepanel1.setImageIcon( ((ImageIcon)ResourceCenter.VerBlue).getImage() );
        jPanel5.add( imagepanel1,BorderLayout.CENTER );
        
        // mode:0 ====> without jbutton4 mode:1 ====> with jbutton4
        if( mode ==0 ){
            jPanel7.remove( jButton4 );
        }
        setupLanguage();
    }

    public void setupLanguage(){
        jButton1.setText(SanBootView.res.getString("WizardDialogSample.button.back"));
        jButton2.setText(SanBootView.res.getString("WizardDialogSample.button.next"));
        jButton3.setText(SanBootView.res.getString("WizardDialogSample.button.cancel"));
        jButton4.setText(SanBootView.res.getString("common.button.del"));
    }

    public void  removeAllContent(JTextPane jTextPane){
        try{
            int len = jTextPane.getDocument().getLength();
            jTextPane.getDocument().remove(0,len); //去掉所有的内容
        }catch(BadLocationException ex){ }
    }
    
    
    public JTextPane getTextPane1(){
        return jTextPane1;
    }
    
    public void setFinishWizardTipText(String str1){
        SimpleAttributeSet set = new SimpleAttributeSet();
    
        removeAllContent( jTextPane1 );
        
        StyleConstants.setFontSize(set, 22);
        StyleConstants.setFontFamily(set, "Dialog");
        StyleConstants.setBold(set, true);
        
        try{
            jTextPane1.getDocument().insertString( 0,str1+"\n",set );
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void setWizardTipText( String str1,String str2 ){
        SimpleAttributeSet set = new SimpleAttributeSet();
        SimpleAttributeSet set1 = new SimpleAttributeSet();
        
        removeAllContent( jTextPane1 );
        
        // 先设置开头的一行
        StyleConstants.setFontSize(set, 14);
        StyleConstants.setFontFamily(set, "Dialog");
        StyleConstants.setBold(set, true);
        
        try{
            jTextPane1.getDocument().insertString( 0,str1+"\n",set );
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        // 再设置下面的内容
        StyleConstants.setFontSize( set1, 14);
        StyleConstants.setFontFamily(set1, "Dialog");
        StyleConstants.setBold(set1, true);
  
        try{
            int offset = jTextPane1.getDocument().getLength();
            jTextPane1.getDocument().insertString(offset,str2+"\n",set1);
        }catch(Exception ex1){
            ex1.printStackTrace();
        }
    }
    
    public String getTipTitle(){
        String title = "";
        
        try{
            int length = jTextPane2.getDocument().getLength();
            title =  jTextPane2.getDocument().getText( 0,length ).trim();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return title;
    }
    
    public void backButtonProcess(){   
    }
    
    public void formWinClosing(){
        this.dispose();
    }

    public void nextButtonProcess(){
    }
    
    public void cancelButtonProcess(){
        this.dispose();
    }
    
    public void jButton4Process(){
    }
    
    public void removeBackButton(){
        jPanel2.remove( jButton1 );
    }
    
    public void removeNextButton(){
        jPanel2.remove( jButton2 );
    }
    
    public void enableBackButton( boolean val ){
        jButton1.setEnabled( val );
    }
    
    public void enableNextButton( boolean val ){
        jButton2.setEnabled( val );
    }
    
    public void enableCancelButton( boolean val ){
        jButton3.setEnabled( val );
    }
    
    public void enableJButton4( boolean val ){
        jButton4.setEnabled( val );
    }
    
    public void setFocusOnNextBtn(){
        jButton2.requestFocus();
    }
    
    public void setFocusOnJButton4(){
        jButton4.requestFocus();
    }
    
    public void setTextOnNextButton(String str){
        jButton2.setText( str );
    }
    
    public void addJButton4( ){
        jPanel7.add( jButton4 );
        jPanel7.validate();
        jPanel7.repaint();
    }
    
    public void removeCancelBtn(){
        jPanel2.remove( jButton3 );
        jPanel2.validate();
        jPanel2.repaint();
    }
    
    public void removeJButton4(){
        jPanel7.remove( jButton4 );
        jPanel7.validate();
        jPanel7.repaint();
    }
    
    public void setTextOnJButton4( String str ){
        jButton4.setText( str );
    }
    
    public void setTextOnCanelButton( String str ){
        jButton3.setText( str );
    }
    
    public void setSizeOnNextBtn( int width ){
        jButton2.setPreferredSize(new java.awt.Dimension(width, 24));
    }
    
    public void removeCenterPane(){
        jPanel3.removeAll();
    }
    
    public void addTopPaneOntoCenterPane(){
        jPanel3.add( topPane,BorderLayout.NORTH );
    }
    
    public void addContentPaneOntoCenterPane(){
        jPanel3.add( bigContentPane,BorderLayout.CENTER );
    }
    
    public void addPaneOntoContentPane(JPanel pane){
        contentPane.add( pane,BorderLayout.CENTER );
    }
    
    public void removePaneFromContentPane( JPanel pane ){
        contentPane.remove( pane );
    } 
    
    public void addImagePaneOntoCenterPane(){
        jPanel3.add( jPanel5,BorderLayout.WEST );
    }
    
    public void addWizardTipTextPane(){
        jPanel3.add( jPanel4,BorderLayout.CENTER );
    }
    
    public void setTipTextOnTopPane( String str1 ){
        removeAllContent( jTextPane2 );
        
        StyleContext context = new StyleContext();
        StyledDocument document = new DefaultStyledDocument(context);
        
        Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);//align left
        StyleConstants.setFontSize(style, 22);
        StyleConstants.setFontFamily( style, "Dialog" );
        StyleConstants.setBold( style, true );
        StyleConstants.setSpaceAbove(style, 4);
        StyleConstants.setSpaceBelow(style, 4);
        StyleConstants.setLeftIndent(style,8);
        
        // Insert content
        try {
            document.insertString(document.getLength(), str1, style);
        } catch ( BadLocationException  badLocationException ) {
            Tool.prtExceptionMsg( badLocationException );
        }
        
        jTextPane2.setDocument(document);
    }
    
    public void refreshCenterPane(){
        jPanel3.validate();
        jPanel3.repaint();
    }
    
    public void refreshBtnPane(){
        jPanel2.validate();
        jPanel2.repaint();
    }
    
    boolean first = true;
    public void setFirstFlag( boolean val ){
        first = val;
    }
    public boolean getFirstFlag(){
        return first;
    }
    
    boolean first1 = true;
    public void setFirstFlag1( boolean val ){
        first1 = val;
    }
    
    public long getMaxBitMapCacheSize(){
        // must be overloaded
        return -1L;
    }
    
    public String replaceLine( String msg ){
        boolean isFirst = true;
        String tmp;
        
        StringBuffer buf = new StringBuffer();
        
        String[] list = Pattern.compile("\n").split( msg ); 
        for( int i=0; i<list.length; i++ ){
            tmp = list[i].trim();
            if( !tmp.equals("") ){
                if( isFirst ){
                    buf.append( tmp );
                    isFirst = false;
                }else{
                    buf.append( " " + tmp );
                }
            }
        }
        
        return buf.toString();
    }
    
    public Point getCenterPoint(int width,int height){
        int x = ( getSize().width - width ) / 2 + getX();
        int y = ( getSize().height - height ) / 2 + getY();
        return new Point(x,y);
    }

    // uws sdhm report
    protected int getTaskParentClntID( DestAgent host ){
        if( host.getDst_agent_id() > 0 ){ // 是一个真正的DestAgent
            return host.getSrc_Agnt_id();
        }else{ // 是一个BootHost
            return -1;
        }
    }

    protected int getTaskParentClntType( DestAgent host ){
        if( host.getDst_agent_id() > 0 ){ // 是一个真正的DestAgent
            if( host.getHostType() == DestAgent.TYPE_DST_AGNT ){
                return DestAgent.TYPE_ORI_HOST;
            }else if( host.getHostType() == DestAgent.TYPE_SRC_AGNT ){
                return DestAgent.TYPE_SRC_AGNT;
            }else{
                return DestAgent.TYPE_INVALID_HOST_TYPE;
            }
        }else{ // 是一个BootHost
            return DestAgent.TYPE_INVALID_HOST_TYPE;
        }
    }
}
