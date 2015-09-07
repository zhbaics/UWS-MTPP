/*
 * MirrorJobRemoteUWSPaneRenderer.java
 *
 * Created on Aug 30, 2010, 10:54 AM
 */

package guisanboot.remotemirror.ui.multiRenderTable;

import guisanboot.data.PoolWrapper;
import guisanboot.data.UWSrvNode;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class MirrorJobRemoteUWSPaneRenderer extends JPanel implements TableCellRenderer {
    JComboBox serverComboBox = new JComboBox();
    JComboBox poolComboBox = new JComboBox();
    
    public MirrorJobRemoteUWSPaneRenderer( ArrayList uwsNodeList ) {
        this.setupDestUWSrv( uwsNodeList );

        serverComboBox.setBorder( javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1) );
        int height = serverComboBox.getPreferredSize().height;
        serverComboBox.setPreferredSize( new Dimension(100,height ) );
        poolComboBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1) );
        height = poolComboBox.getPreferredSize().height;
        poolComboBox.setPreferredSize( new Dimension( 180, height ) );
        this.setLayout( new FlowLayout( java.awt.FlowLayout.CENTER, 5, 0 ) );
        this.add( serverComboBox,0 );
        this.add( poolComboBox,1 );
        this.setBackground( Color.white );
    }
    
    private void setupDestUWSrv( ArrayList list ){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            UWSrvNode uws = (UWSrvNode)list.get(i);
            this.serverComboBox.addItem( uws );
        }
    }

    private void setupPool( ArrayList<PoolWrapper> list ){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            PoolWrapper pool = list.get(i);
            this.poolComboBox.addItem( pool );
        }
    }
    
    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus,int row, int column
    ) {
        if( value != null ){
//System.out.println("MirrorJobRemoteUWSPaneRenderer(getTableCellRendererComponent) Value not null "+value.toString() );
            WrapperOfRemoteUWSAndPool wrapper = (WrapperOfRemoteUWSAndPool)value;
            this.poolComboBox.removeAllItems();
            this.setupPool( wrapper.poolList );
            
            this.serverComboBox.setSelectedItem( wrapper.uwsNode );
            this.poolComboBox.setSelectedItem( wrapper.pool );
        }

        return this;
    }
}
