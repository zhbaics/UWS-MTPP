/*
 * MirrorJobSchPaneEditor.java
 *
 * Created on Seq 9, 2010, 11:34 AM
 */

package guisanboot.remotemirror.ui.multiRenderTable;

import guisanboot.remotemirror.entity.MirrorJobSch;
import guisanboot.remotemirror.entity.PesudoMJSch;
import guisanboot.remotemirror.ui.MjSchDetailDialog;
import guisanboot.remotemirror.ui.SetupMjSchDialog;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */

public class MirrorJobSchPaneEditor extends AbstractCellEditor implements TableCellEditor {
    JComboBox mjSchComboBox = new JComboBox();
    JButton detailBtn = new JButton("...");
    SetupMjSchDialog fatherDialog;
    JPanel pane = new JPanel();
    
    public MirrorJobSchPaneEditor( ArrayList mjSchList,SetupMjSchDialog fatherDialog ) {
        this.fatherDialog = fatherDialog;
        setupMjSch( mjSchList );

        mjSchComboBox.setBorder( javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1) );
        int height = mjSchComboBox.getPreferredSize().height;
        mjSchComboBox.setPreferredSize( new Dimension(150,height ) );

        detailBtn.setPreferredSize( new Dimension( 25,20) );
        
        pane.setLayout( new FlowLayout( java.awt.FlowLayout.CENTER, 5, 0 ) );
        pane.add( mjSchComboBox,0 );
        pane.add( detailBtn,1 );
        pane.setBackground( Color.white );

        detailBtn.setToolTipText( SanBootView.res.getString("common.tooltip.mjsch") );
        detailBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailBtnActionPerformed( evt );
            }
        });
    }

    private void setupMjSch( ArrayList list ){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            MirrorJobSch mjSch = (MirrorJobSch)list.get( i );
            this.mjSchComboBox.addItem( mjSch );
        }
    }

    private void detailBtnActionPerformed(java.awt.event.ActionEvent evt){
        Object selObj = this.mjSchComboBox.getSelectedItem();
        if( selObj instanceof PesudoMJSch ) return;
        
        MirrorJobSch mjSch = (MirrorJobSch)selObj;
        MjSchDetailDialog dialog = new MjSchDetailDialog( fatherDialog,mjSch );
        int width  = 435+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 215+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( fatherDialog.getCenterPoint( width,height ) );
        dialog.setVisible( true );
    }

    public Object getCellEditorValue() {
        return this.mjSchComboBox.getSelectedItem();
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int r, int c
    ) {
        if( value != null ){
//System.out.println("MirrorJobSchPaneEditor(getTableCellEditorComponent) Value not null : "+value.toString() );
            this.mjSchComboBox.setSelectedItem( value );
        }
        return pane;
    }
}