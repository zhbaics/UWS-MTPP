/*
 * MirrorJobSchPaneRenderer.java
 *
 * Created on Seq 9, 2010, 11:40 AM
 */

package guisanboot.remotemirror.ui.multiRenderTable;

import guisanboot.remotemirror.entity.MirrorJobSch;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */

public class MirrorJobSchPaneRenderer extends JPanel implements TableCellRenderer {
    JComboBox mjSchComboBox = new JComboBox();
    JButton detailBtn = new JButton("...");
    
    public MirrorJobSchPaneRenderer( ArrayList mjSchList ) {
        setupMjSch( mjSchList );

        mjSchComboBox.setBorder( javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1) );
        int height = mjSchComboBox.getPreferredSize().height;
        mjSchComboBox.setPreferredSize( new Dimension(150,height ) );
        detailBtn.setPreferredSize( new Dimension( 25,20) );
        
        this.setLayout( new FlowLayout( java.awt.FlowLayout.CENTER, 5, 0 ) );
        this.add( mjSchComboBox,0 );
        this.add( detailBtn,1 );
        this.setBackground( Color.white );
    }

    private void setupMjSch( ArrayList list ){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            MirrorJobSch mjSch = (MirrorJobSch)list.get( i );
            this.mjSchComboBox.addItem( mjSch );
        }
    }

    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus,int row, int column
    ) {
        if( value != null ){
//System.out.println("MirrorJobSchPaneRenderer(getTableCellEditorComponent) Value not null : "+value.toString() );
            this.mjSchComboBox.setSelectedItem( value );
        }
        return this;
    }
}