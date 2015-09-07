/*
 * PaneEditor.java
 *
 * Created on May 25, 2005, 5:00 PM
 */

package guisanboot.ui.multiRenderTable;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import guisanboot.ui.*;
import guisanboot.data.*;
import guisanboot.res.ResourceCenter;

/**
 *
 * @author  Administrator
 */
public class PaneEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    JRadioButton jBtn1; // crt radio button
    JRadioButton jBtn2; // sel radio button
    ButtonGroup grp;
    PaneEditorable tab;
    SanBootView view;
    WizardDialogSample wizardDiag;
    boolean isClickBtn1=true;
    boolean isClickBtn2=false;
    int selRow=-1;
    
    JButton btn;
    JPanel pane = new JPanel();
    
    public PaneEditor( PaneEditorable _tab,SanBootView _view,WizardDialogSample _wizardDiag,boolean hasEnoughSpace  ) {
        tab = _tab;
        view = _view;
        wizardDiag = _wizardDiag;
        
        jBtn1 = new JRadioButton( SanBootView.res.getString("common.crt") );
        jBtn2 = new JRadioButton( SanBootView.res.getString("common.sel") );
        grp = new ButtonGroup();

        grp.add( jBtn1 );
        grp.add( jBtn2 );
       
        btn = new JButton("...");
        btn.setMargin( new Insets(2,2,2,2));
        
        pane.setLayout( new BorderLayout() );

        pane.add( jBtn1,BorderLayout.WEST );
        pane.add( jBtn2,BorderLayout.CENTER );    
        pane.add(btn, BorderLayout.EAST );
        
        jBtn1.setEnabled( hasEnoughSpace );
        if( hasEnoughSpace ){
            jBtn1.setSelected( true );
            btn.setEnabled( false );
        }else{
            jBtn2.setSelected( true );
            btn.setEnabled( true);
        }

        jBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // 获取表中当前行的取值
                Object volObj = tab.getNameCol( selRow );
                if( volObj instanceof Volume ){
                    Volume vol = (Volume)volObj;
                    int tid = vol.getTargetID();
                    view.addVolumeToUnSelTabForTID( tid,vol );
                }
                
                btn.setEnabled( false );
                tab.cleanSomething( selRow );
                tab.setFormatFlagCol( true,selRow );
            }
        });
        
        jBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // 获取表中当前行的取值
                Object volObj = tab.getNameCol( selRow );
                if( volObj instanceof Volume ){
                    Volume vol = (Volume)volObj;
                    int tid = vol.getTargetID();
                    view.addVolumeToUnSelTabForTID( tid,vol );
                }
                
                btn.setEnabled( true );
                tab.cleanSomething( selRow );
                tab.setFormatFlagCol( false,selRow );
            }
        });
        
        pane.setBackground(Color.white);
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Volume vol;
                int tid;
                
                SelectVolDialog dialog = new SelectVolDialog( wizardDiag,view );
                int width  = 390+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 220+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );
                dialog.setVisible( true );
                
                Object ret = dialog.getValues();
                if( ret == null ) return;
                
                // 获取表中当前行的取值
                Object volObj = tab.getNameCol( selRow );
                if( volObj instanceof Volume ){
                    vol = (Volume)volObj;
                    tid = vol.getTargetID();
                    view.addVolumeToUnSelTabForTID( tid,vol );
                }
                
                // 用新值更新对应行
                vol = (Volume)ret;
                tid = vol.getTargetID();
                tab.setNameCol( vol, selRow );
                tab.setSizeCol( vol.getCapStr1(), selRow );
                                
                // set blk size
                tab.setBlkSizeCol( BasicVDisk.getBlkSizeStr( vol.getSnap_block_size() ),selRow );
                
                // set pool
                PoolWrapper pool = view.initor.mdb.getPoolWrapper( vol.getSnap_pool_id() );
                if( pool != null ){
                    tab.setPoolCol( pool,selRow );
                }else{
                    tab.setPoolCol( "",  selRow );
                }        
                view.removeVolumeFromUnSelTabForTID( tid );
            }
        });
    }
    
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
//System.out.println("@@@@@: "+obj.toString() );
    }
    
    public Object getCellEditorValue() {
        return new Boolean( jBtn1.isSelected() );
    }
    
     public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int r, int c
    ) { 
        if( value !=null ){
//System.out.println("PaneEditor(getTableCellEditorComponent) Value not null ");
            if( ((Boolean)value).booleanValue() ){
                jBtn1.setSelected( true );
                btn.setEnabled( false );
            }else{
                jBtn2.setSelected( true );
                btn.setEnabled( true );
            }
        }
        selRow = r;
         
        return pane;
    }
}
