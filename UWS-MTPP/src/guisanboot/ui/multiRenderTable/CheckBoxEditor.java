/*
 * CheckBoxEditor.java
 *
 * Created on May 25, 2005, 5:00 PM
 */

package guisanboot.ui.multiRenderTable;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  Administrator
 */
public class CheckBoxEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    JCheckBox jCheckBox1;
    JPanel pane = new JPanel();
    
    public CheckBoxEditor() {        
        jCheckBox1 = new JCheckBox("");
        pane.setLayout( new BorderLayout() );
        jCheckBox1.setHorizontalAlignment( JLabel.CENTER );
        jCheckBox1.setOpaque( false );
        pane.setBackground( Color.white );
      //  jCheckBox1.setSelected( true );
        pane.add( jCheckBox1, BorderLayout.CENTER );
    }
    
    public void actionPerformed(ActionEvent e) {
    }
    
    // 要想正确的得到用户输入,必须之前将编辑过程停止( 调用 stopCellEditing )
    public Object getCellEditorValue() {
//System.out.println(" ((((((((((( : "+ jCheckBox1.isSelected() );        
        return new Boolean( jCheckBox1.isSelected() );
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int r, int c
    ) { 
        if( value !=null ){
//System.out.println(" ))))))): "+ value + " row: "+r+" col: "+c );
            if( ((Boolean)value).booleanValue() ){
                jCheckBox1.setSelected( true );
            }else{
                jCheckBox1.setSelected( false );
            }
        }
        return pane;
    }
}
