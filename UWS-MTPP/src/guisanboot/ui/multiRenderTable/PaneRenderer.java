/*
 * PaneRenderer.java
 *
 * Created on May 25, 2005, 6:23 PM
 */

package guisanboot.ui.multiRenderTable;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import guisanboot.ui.*;

/**
 *
 * @author  Administrator
 */
public class PaneRenderer extends JPanel implements TableCellRenderer {
    JRadioButton jBtn1;
    JRadioButton jBtn2;
    ButtonGroup grp;
    JButton btn;
    boolean hasEnoughSpace;

    /** Creates a new instance of PaneRenderer */
    public PaneRenderer( boolean hasEnoughSpace ){
        this.hasEnoughSpace = hasEnoughSpace;

        jBtn1 = new JRadioButton( SanBootView.res.getString("common.crt") );
        jBtn2 = new JRadioButton( SanBootView.res.getString("common.sel") );
        grp = new ButtonGroup();
        
        grp.add( jBtn1 );
        grp.add( jBtn2 );
      
        btn = new JButton("...");
        btn.setMargin( new Insets(2,2,2,2));
        
        setLayout( new BorderLayout() );
        
        add( jBtn1,BorderLayout.WEST );
        add( jBtn2,BorderLayout.CENTER );
        add(this.btn,BorderLayout.EAST );
        
        this.jBtn1.setEnabled( this.hasEnoughSpace );
        if( hasEnoughSpace ){
            jBtn1.setSelected( true );
            jBtn2.setSelected( false );
            btn.setEnabled( false );
        }else{
            jBtn1.setSelected( false );
            jBtn2.setSelected( true );
            btn.setEnabled( true );
        }
        
        jBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn.setEnabled( false );
            }
        });
        
        jBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn.setEnabled( true );
            }
        });
        
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.showMessageDialog(null,
                    "a window is launched...."
                );
            }
        });
    }
    
    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus,int row, int column
    ) { 
        if( value !=null ){
System.out.println("PaneRenderer(getTableCellRendererComponent) Value not null: "+value.toString() );
            if( ((Boolean)value).booleanValue() ){
                jBtn1.setSelected( true );
                btn.setEnabled( false );
            }else{
                jBtn2.setSelected( true );
                btn.setEnabled( true);
            }
        }
         
        return this;
    }
}
