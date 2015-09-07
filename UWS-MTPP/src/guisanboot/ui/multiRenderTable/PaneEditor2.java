/*
 * PaneEditor2.java
 *
 * Created on July 12, 2011, 10:22 AM
 */

package guisanboot.ui.multiRenderTable;

import guisanboot.cluster.entity.AccessPath;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import guisanboot.ui.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class PaneEditor2 extends AbstractCellEditor implements TableCellEditor, ActionListener {
    JRadioButton jBtn1; // local disk radio button
    JRadioButton jBtn2; // shared disk radio button
    ButtonGroup grp;
    PaneEditorable tab;
    boolean isClickBtn1=true;
    boolean isClickBtn2=false;
    ArrayList<String> vipList;
    ArrayList<String> pubIPList;

    JComboBox comboBox;
    JPanel pane = new JPanel();
    
    public PaneEditor2(
        PaneEditorable _tab,
        ArrayList<String> _vipList,
        ArrayList<String> _pubIPList
    ) {
        tab = _tab;
        this.vipList = _vipList;
        this.pubIPList = _pubIPList;

        jBtn1 = new JRadioButton( SanBootView.res.getString("common.localdisk") );
        jBtn2 = new JRadioButton( SanBootView.res.getString("common.sharedDisk") );
        grp = new ButtonGroup();
        grp.add( jBtn1 );
        grp.add( jBtn2 );
        comboBox = new JComboBox();
        comboBox.setPreferredSize( new Dimension(120,20));
        pane.setLayout( new BorderLayout() );

        pane.add( jBtn1,BorderLayout.WEST );
        pane.add( jBtn2,BorderLayout.CENTER );    
        pane.add( comboBox, BorderLayout.EAST );
        
        jBtn1.setSelected( true );
        this.setupPubIPList();
        jBtn2.setSelected( false );
        
        jBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setupPubIPList();
            }
        });
        
        jBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setupVipList();
            }
        });
        
        pane.setBackground( Color.white );
    }

    private void setupPubIPList(){
        DefaultComboBoxModel model = (DefaultComboBoxModel)comboBox.getModel();
        model.removeAllElements();
        int size = pubIPList.size();
        for( int i=0; i<size;i++ ){
            model.addElement( pubIPList.get( i ) );
        }
    }

    private void setupVipList(){
        DefaultComboBoxModel model = (DefaultComboBoxModel)comboBox.getModel();
        model.removeAllElements();
        int size = vipList.size();
        for( int i=0; i<size;i++ ){
            model.addElement( vipList.get( i ) );
        }
    }
    
    public void actionPerformed(ActionEvent e) {
    }
    
    public Object getCellEditorValue() {
        AccessPath ap = new AccessPath();
        ap.isLocal = this.jBtn1.isSelected();
        ap.ip = (String)this.comboBox.getSelectedItem();
        return ap;
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int r, int c
    ) { 
        if( value != null ){
            AccessPath ap = (AccessPath)value;
            if( ap.isLocal ){
                jBtn1.setSelected( true );
            }else{
                jBtn2.setSelected( true );
            }
            comboBox.setSelectedItem( ap.ip );
        }
        return pane;
    }
}
