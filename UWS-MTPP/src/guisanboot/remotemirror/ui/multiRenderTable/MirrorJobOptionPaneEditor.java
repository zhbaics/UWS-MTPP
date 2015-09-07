/*
 * MirrorJobOptionPaneEditor.java
 *
 * Created on Aug 27, 2010, 4:44 PM
 */

package guisanboot.remotemirror.ui.multiRenderTable;

import guisanboot.ui.SanBootView;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  Administrator
 */
public class MirrorJobOptionPaneEditor extends AbstractCellEditor implements TableCellEditor {
    JCheckBox continueBox;
    JCheckBox encryBox;
    JCheckBox deleteViewBox;
    JCheckBox compressBox;
    JCheckBox copyBranchBox;

    JPanel pane = new JPanel();
    
    public MirrorJobOptionPaneEditor() {        
        continueBox = new JCheckBox( SanBootView.res.getString("EditMirrorJobDialog.checkbox.continue") );
        continueBox.setSelected( true );
        continueBox.setOpaque( false );
        continueBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        encryBox = new JCheckBox( SanBootView.res.getString("EditMirrorJobDialog.checkbox.des") );
        encryBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        encryBox.setOpaque( false );
        deleteViewBox = new JCheckBox( SanBootView.res.getString("EditMirrorJobDialog.checkbox.delview") );
        deleteViewBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        deleteViewBox.setOpaque( false );
        compressBox = new JCheckBox( SanBootView.res.getString("EditMirrorJobDialog.checkbox.compress") );
        compressBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        compressBox.setOpaque( false );
        copyBranchBox = new JCheckBox( SanBootView.res.getString("EditMirrorJobDialog.checkbox.copybranch") );
        copyBranchBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        copyBranchBox.setOpaque( false );
        
        pane.setLayout( new FlowLayout(java.awt.FlowLayout.CENTER, 0, 0 ) );
        pane.add( continueBox,0 );
        pane.add( encryBox,1 );
        pane.add( compressBox,2 );
        pane.add( copyBranchBox,3 );
        pane.add( deleteViewBox,4 );
        pane.setBackground( Color.white );
    }
    
    public Object getCellEditorValue() {
        MjOption mjOpt = new MjOption(
            this.continueBox.isSelected(),
            this.encryBox.isSelected(),
            this.deleteViewBox.isSelected(),
            this.compressBox.isSelected(),
            this.copyBranchBox.isSelected()
        );

        return mjOpt;
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int r, int c
    ) {
        if( value != null ){
//System.out.println("MirrorJobOptionPaneEditor(getTableCellEditorComponent) Value not null : "+value.toString() );
            MjOption mjOpt = (MjOption)value;
            this.continueBox.setSelected( mjOpt.isContinueTransferSeled );
            this.encryBox.setSelected( mjOpt.isEncrySeled );
            this.deleteViewBox.setSelected( mjOpt.isDeleteViewSeled );
            this.compressBox.setSelected(mjOpt.isCompressedSeled );
            this.copyBranchBox.setSelected( mjOpt.isCopyBranchSeled );
        }
        return pane;
    }
}
