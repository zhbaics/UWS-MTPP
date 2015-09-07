/*
 * PaneEditor.java
 *
 * Created on May 25, 2005, 5:00 PM
 */

package guisanboot.ui.multiRenderTable;

import guisanboot.res.ResourceCenter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import guisanboot.ui.*;
import java.util.Vector;

/**
 *
 * @author  Administrator
 */
public class PaneEditor1 extends AbstractCellEditor implements TableCellEditor, ActionListener {
    JComboBox jComboBox1;
    SanBootView view;   
    JButton btn;
    JPanel pane = new JPanel();
    
    public PaneEditor1( Vector items,SanBootView _view ) {
        view = _view;
        
        jComboBox1 = new JComboBox( items );
        btn = new JButton("...");
        btn.setToolTipText( SanBootView.res.getString("RestoreOriginalDiskWizardDialog.btn.detail") );
        btn.setMargin( new Insets(2,2,2,2));
        pane.setLayout( new BorderLayout() );
        pane.add( jComboBox1,BorderLayout.CENTER );    
        pane.add(btn, BorderLayout.EAST );
        pane.setBackground(Color.white);
        
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { 
                Object disk = jComboBox1.getSelectedItem();
                if( disk != null ){
System.out.println("  seled obj: "+disk.toString() );                        
                    DiskPropertyDialog dialog = new DiskPropertyDialog( view,disk, false ); 
                    int width  = 340+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                    int height = 220+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                    dialog.setSize( width,height );
                    dialog.setLocation( view.getCenterPoint( width,height ) );
                    dialog.setVisible( true );
                }
            }
        });
    }
    
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
//System.out.println("@@@@@: "+obj.toString() );
    }
    
    public Object getCellEditorValue() {
        return jComboBox1.getSelectedItem();
    }
    
     public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int r, int c
    ) { 
        if( value !=null ){
//System.out.println("PaneEditor(getTableCellEditorComponent) Value not null : "+value.toString() );
            jComboBox1.setSelectedItem( value );
        } 
        return pane;
    }
}
