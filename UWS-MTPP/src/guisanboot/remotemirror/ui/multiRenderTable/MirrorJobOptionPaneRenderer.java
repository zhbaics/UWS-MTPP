/*
 * MirrorJobOptionPaneRenderer.java
 *
 * Created on Aug 27, 2010, 5:48 PM
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
public class MirrorJobOptionPaneRenderer extends JPanel implements TableCellRenderer {
    JCheckBox continueBox;
    JCheckBox encryBox;
    JCheckBox deleteViewBox;
    JCheckBox compressBox;
    JCheckBox copyBranchBox;

    /** Creates a new instance of MirrorJobOptionPaneRenderer */
    public MirrorJobOptionPaneRenderer(){
        continueBox = new JCheckBox( SanBootView.res.getString("EditMirrorJobDialog.checkbox.continue") );
        continueBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        continueBox.setOpaque( false );
        encryBox = new JCheckBox( SanBootView.res.getString( "EditMirrorJobDialog.checkbox.des") );
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
        
        setLayout( new FlowLayout(java.awt.FlowLayout.CENTER, 0, 0) );

        add( continueBox,0 );
        add( encryBox,1 );
        add( compressBox,2 );
        add( copyBranchBox,3 );
        add( deleteViewBox,4 );

        this.continueBox.setSelected( true );
        this.setBackground( Color.white );
    }
    
    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus,int row, int column
    ) { 
        if( value != null ){
            MjOption mjOpt = (MjOption)value;
            this.continueBox.setSelected( mjOpt.isContinueTransferSeled );
            this.encryBox.setSelected( mjOpt.isEncrySeled );
            this.deleteViewBox.setSelected( mjOpt.isDeleteViewSeled );
            this.compressBox.setSelected( mjOpt.isCompressedSeled );
            this.copyBranchBox.setSelected( mjOpt.isCopyBranchSeled );
        }

        return this;
    }
}
