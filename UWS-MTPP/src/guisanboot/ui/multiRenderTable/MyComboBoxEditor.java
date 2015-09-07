/*
 * MyComboBoxEditor.java
 *
 * Created on Aug 25, 2007, 5:00 PM
 */

package guisanboot.ui.multiRenderTable;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

/**
 *
 * @author  Administrator
 */
public class MyComboBoxEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    JComboBox jComboBox1;
    JPanel pane = new JPanel();
    
    public MyComboBoxEditor( Vector items  ){
        this( items.toArray() );
    }
    public MyComboBoxEditor( ArrayList items ){
        this( items.toArray() );
    }
    
    public MyComboBoxEditor( Object[] items ) {
        jComboBox1 = new JComboBox( items );
        pane.setLayout( new BorderLayout() );
        jComboBox1.setOpaque( false );
        pane.setBackground( Color.white );
        //if( items.size() > 0 )
           // jComboBox1.setSelectedIndex( 0 );
        pane.add( jComboBox1, BorderLayout.CENTER );
    }
    
    public void actionPerformed(ActionEvent e) {
    }
    
    // 要想正确的得到用户输入,必须之前将编辑过程停止( 调用 stopCellEditing )
    public Object getCellEditorValue() { 
        return jComboBox1.getSelectedItem();
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int r, int c
    ) {         
        if( value !=null ){
            jComboBox1.setSelectedItem( value );
        }
        
        return pane;
    }
}
