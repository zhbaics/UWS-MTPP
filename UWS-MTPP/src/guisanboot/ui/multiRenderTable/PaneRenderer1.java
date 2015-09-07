/*
 * PaneRenderer1.java
 *
 * Created on May 25, 2005, 6:23 PM
 */

package guisanboot.ui.multiRenderTable;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.Vector;

/**
 *
 * @author  Administrator
 */
public class PaneRenderer1 extends JPanel implements TableCellRenderer {
    JComboBox jComboBox1;
    JButton btn;
    
    /** Creates a new instance of PaneRenderer1 */
    public PaneRenderer1( Vector item ){    
        jComboBox1 = new JComboBox( item );
        btn = new JButton("...");
        btn.setMargin( new Insets(2,2,2,2));
        setLayout( new BorderLayout() );
        add( jComboBox1,BorderLayout.CENTER );
        add(this.btn,BorderLayout.EAST );
    }
    
    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus,int row, int column
    ) { 
        if( value !=null ){
System.out.println("PaneRenderer(getTableCellRendererComponent) Value not null "+value.toString() );
            jComboBox1.setSelectedItem( value );
        }
        
        return this;
    }
}
