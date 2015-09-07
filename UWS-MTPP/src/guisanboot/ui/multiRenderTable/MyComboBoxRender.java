/*
 * MyComboBoxRender.java
 *
 * Created on 2007/8/9��
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui.multiRenderTable;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author Administrator
 */
public class MyComboBoxRender extends JPanel implements TableCellRenderer {
    JTextField jTextField1;
    
    /** Creates a new instance of MyComboBoxRender */
    public MyComboBoxRender( String defaultText ){
        jTextField1 = new JTextField();
        jTextField1.setText( defaultText );
        setLayout( new BorderLayout() );
        jTextField1.setHorizontalAlignment( JLabel.LEFT );
        jTextField1.setOpaque( false );
        jTextField1.setBorder(null);
        setBackground( Color.white );
        add( jTextField1,BorderLayout.CENTER );
    }
    
    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus,int row, int column
    ) { 
        if( value !=null ){
            jTextField1.setText( value.toString() );
        }else{
            jTextField1.setText("");
        }
        
        return this;
    }
}
