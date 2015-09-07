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
public class PaneUnixEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
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
    
    public PaneUnixEditor( PaneEditorable _tab,SanBootView _view,WizardDialogSample _wizardDiag,boolean hasEnoughSpace  ) {
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
        pane.add( btn, BorderLayout.EAST );

        jBtn1.setEnabled( hasEnoughSpace );

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
                int tid;
                
                // 获取表中当前行的取值
                Object volObj =tab.getSizeCol( selRow );
                Object lvObj = tab.getNameCol( selRow );
                if( lvObj instanceof String ){ 
                    if( volObj instanceof VolumeWrapper ){
                        // 说明当前这个对象是个tgt(可能有表示vg或lv的VolumeMap表记录跟它对应)
                        VolumeWrapper volWraper = (VolumeWrapper)volObj;
                        tid = volWraper.vol.getTargetID();
                        view.addVolumeToUnSelTabForTID( tid,volWraper.vol );
                    }else{
                        // 说明用户要创建一个新的lv( name col和size col都是输入的字符串)
                    }
                }else if( lvObj instanceof LVWrapper ){ 
                    // 说明当前这个对象是lv(一定有表示vg/lv/tgt的VolumeMap表记录跟它对应)
                    LVWrapper lv = (LVWrapper)lvObj;
                    tid = lv.rawTgt.getTargetID();
                    view.addVolumeToUnSelTabForTID( tid, lv.rawTgt );
                }else{
                }
                btn.setEnabled( false );
                tab.cleanSomething( selRow );
                tab.setFormatFlagCol( true, selRow );
            }
        });
        
        jBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int tid;
                
                // 获取表中当前行的取值
                Object volObj =tab.getSizeCol( selRow );
                Object lvObj = tab.getNameCol( selRow );
                if( lvObj instanceof String ){
                    if( volObj instanceof VolumeWrapper ){
                        // 说明当前这个对象是个tgt(可能有表示vg或lv的VolumeMap表记录跟它对应)
                        VolumeWrapper volWraper = (VolumeWrapper)volObj;
                        tid = volWraper.vol.getTargetID();
                        view.addVolumeToUnSelTabForTID( tid,volWraper.vol );
                    }else{
                        // 说明用户要创建一个新的lv( name col和size col都是输入的字符串)
                    }
                }else if( lvObj instanceof LVWrapper ){
                    // 说明当前这个对象是lv(一定有表示vg/lv/tgt的VolumeMap表记录跟它对应)
                    LVWrapper lv = (LVWrapper)lvObj;
                    tid = lv.rawTgt.getTargetID();
                    view.addVolumeToUnSelTabForTID( tid, lv.rawTgt );
                }else{
                }

                btn.setEnabled( true );
                tab.cleanSomething( selRow );
                tab.setFormatFlagCol( false, selRow );
            }
        });
        
        pane.setBackground(Color.white);
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Volume vol;
                LVWrapper lv;
                VolumeWrapper volWraper;
                int tid;
                
                SelectUnixVolDialog dialog = new SelectUnixVolDialog( wizardDiag,view,tab );
                int width  = 390+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 220+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );
                dialog.setVisible( true );
                
                Object ret[] = dialog.getValues();
                if( ret == null ) return;
                
                // 获取表中当前行的取值
                Object volObj = tab.getSizeCol( selRow );
                Object lvObj = tab.getNameCol( selRow );
                if( lvObj instanceof LVWrapper ){
                    lv = (LVWrapper)lvObj;
                    tid = lv.rawTgt.getTargetID();
                    view.addVolumeToUnSelTabForTID( tid, lv.rawTgt );
                }else if( lvObj instanceof String ){
                    if( volObj instanceof VolumeWrapper ){
                        volWraper = (VolumeWrapper)volObj;
                        tid = volWraper.vol.getTargetID();
                        view.addVolumeToUnSelTabForTID( tid,volWraper.vol );
                    }
                }
                
                // 用新值更新对应行
                if( ret.length == 4 ){
                    vol = (Volume)ret[0];
                    VolumeMap lvVolMap = (VolumeMap)ret[1];
                    
                    lv = new LVWrapper( lvVolMap,vol, (String)ret[2], (String)ret[3] );
                    tid = vol.getTargetID();
                    tab.setNameCol( lv, selRow );
                    tab.setSizeCol( vol.getCapStr(), selRow );
                    tab.setLVMTypeCol( lv.lvmType, selRow );
                    tab.setSnapSpaceCol( lv.snapSpace,selRow );
//System.out.println(" blk size:"+ vol.getSnap_block_size() +" pool: "+vol.getSnap_pool_id() );                    
                    tab.setBlkSizeCol( BasicVDisk.getBlkSizeStr( vol.getSnap_block_size() ),selRow );
                    PoolWrapper pool = view.initor.mdb.getPoolWrapper( vol.getSnap_pool_id() );
                    if( pool != null ){
                        tab.setPoolCol( pool,selRow );
                    }else{
                        tab.setPoolCol( "",  selRow );
                    }        
                    
                    view.removeVolumeFromUnSelTabForTID( tid );
                }else{
                    vol = (Volume)ret[0];
                    tid = vol.getTargetID();
                    tab.setNameCol( "", selRow );
                    volWraper = new VolumeWrapper( vol );
                    tab.setSizeCol( volWraper, selRow );
                    
//System.out.println(" blk size:"+ vol.getSnap_block_size() +" pool: "+vol.getSnap_pool_id() );                    
                    tab.setBlkSizeCol( BasicVDisk.getBlkSizeStr( vol.getSnap_block_size() ),selRow );
                    PoolWrapper pool = view.initor.mdb.getPoolWrapper( vol.getSnap_pool_id() );
                    if( pool != null ){
                        tab.setPoolCol( pool,selRow );
                    }else{
                        tab.setPoolCol( "",  selRow );
                    }        

                    view.removeVolumeFromUnSelTabForTID( tid ); 
                }
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
