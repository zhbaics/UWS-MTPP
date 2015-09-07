/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.ui.multiRenderTable;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * MyTableHeaderRenderer.java
 *
 * Created on 2010-9-10, 11:22:24
 */
public class MyTableHeaderRenderer extends JLabel implements TableCellRenderer{
    private boolean color = false;
    public MyTableHeaderRenderer( boolean color ){
        this.color = color;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column
    ) {
        if( color ){
            this.setForeground( Color.RED );
        }
        
        //this.setBorder( BorderFactory.createRaisedBevelBorder() );
        this.setBorder( UIManager.getBorder("TableHeader.cellBorder") );
        this.setHorizontalAlignment( JButton.CENTER );
        this.setText( value.toString() );
        return this;
    }
}