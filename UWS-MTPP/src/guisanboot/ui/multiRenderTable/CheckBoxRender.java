/*
 * CheckBoxRender.java
 *
 * Created on 2006/12/29, afternoon 6:06
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
public class CheckBoxRender extends JPanel implements TableCellRenderer {
    JCheckBox jCheckBox1;
    
    /** Creates a new instance of CheckBoxRender */
    public CheckBoxRender() {        
        jCheckBox1 = new JCheckBox("");
        setLayout( new BorderLayout() );
        jCheckBox1.setHorizontalAlignment( JLabel.CENTER );
        jCheckBox1.setOpaque( false );
        //jCheckBox1.setSelected( true );
        setBackground( Color.white );
        add( jCheckBox1,BorderLayout.CENTER );
    }
    
    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus,int row, int column
    ) { 
        if( value !=null ){          
            if( ((Boolean)value).booleanValue() ){
                jCheckBox1.setSelected( true );
            }else{
                jCheckBox1.setSelected( false );
            }
        }
        
        return this;
    }
}
