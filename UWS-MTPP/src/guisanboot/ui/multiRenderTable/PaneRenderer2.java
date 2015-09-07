/*
 * PaneRenderer2.java
 *
 * Created on July 12, 2011, 10:28 AM
 */

package guisanboot.ui.multiRenderTable;

import guisanboot.cluster.entity.AccessPath;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import guisanboot.ui.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class PaneRenderer2 extends JPanel implements TableCellRenderer {
    JRadioButton jBtn1;
    JRadioButton jBtn2;
    ButtonGroup grp;
    JComboBox comboBox;
    ArrayList vipList;
    ArrayList pubIPList;

    /** Creates a new instance of PaneRenderer2 */
    public PaneRenderer2( ArrayList vipList,ArrayList pubIPList ){
        this.vipList = vipList;
        this.pubIPList = pubIPList;

        jBtn1 = new JRadioButton( SanBootView.res.getString("common.localdisk") );
        jBtn2 = new JRadioButton( SanBootView.res.getString("common.sharedDisk") );
        grp = new ButtonGroup();
        grp.add( jBtn1 );
        grp.add( jBtn2 );
      
        comboBox = new JComboBox();
        comboBox.setPreferredSize( new Dimension(120,20));
        
        setLayout( new BorderLayout() );
        
        add( jBtn1,BorderLayout.WEST );
        add( jBtn2,BorderLayout.CENTER );
        add( comboBox,BorderLayout.EAST );

        jBtn1.setSelected( true );
        jBtn2.setSelected( false );
    }

    private void setupPubIPList(){
        DefaultComboBoxModel model = (DefaultComboBoxModel)comboBox.getModel();
        model.removeAllElements();
        int size = this.pubIPList.size();
        for( int i=0; i<size; i++ ){
            model.addElement( pubIPList.get(i) );
        }
    }

    private void setupVipList(){
        DefaultComboBoxModel model = (DefaultComboBoxModel)comboBox.getModel();
        model.removeAllElements();
        int size = vipList.size();
        for( int i=0; i<size; i++ ){
            model.addElement( vipList.get(i) );
        }
    }

    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus,int row, int column
    ) { 
        if( value != null ){
            AccessPath ap = (AccessPath)value;
//System.out.println("PaneRenderer(getTableCellRendererComponent) Value not null: "+ap.toString() );
            if( ap.isLocal ){
                jBtn1.setSelected( true );
                this.setupPubIPList();
            }else{
                jBtn2.setSelected( true );
                this.setupVipList();
            }
            comboBox.setSelectedItem( ap.ip );
        }
         
        return this;
    }
}
