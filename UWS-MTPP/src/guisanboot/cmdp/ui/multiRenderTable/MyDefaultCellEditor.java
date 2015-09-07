/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.ui.multiRenderTable;

import guisanboot.cluster.entity.Cluster;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.cmdp.entity.PPProfileItem;
import guisanboot.data.BootHost;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.MirrorDiskInfoWrapper;
import guisanboot.data.VolumeMap;
import guisanboot.data.VolumeMapWrapper;
import guisanboot.ui.SanBootView;
import guisanboot.ui.SelectSnapshotPane;
import guisanboot.ui.multiRenderTable.JTableY;
import guisanboot.ui.multiRenderTable.MyDefaultTableModelForTabY;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 * MyDefaultCellEditor.java
 *
 * Created on 2010-6-30, 14:25:53
 */
public class MyDefaultCellEditor extends DefaultCellEditor{
    private Object selObj = null;
    private JTableY tab;
    private SanBootView view;
    private SelectSnapshotPane pane;
    private BootHost host;
    private Cluster cluster;

    private void actPerformed(){
        VolumeMap volMap;
        String diskLabel;
        MirrorDiskInfo mdi;
        PPProfile prof;

        int selRow = tab.getSelectedRow();
        if( selRow < 0 ) return;
        
        MyDefaultTableModelForTabY model = (MyDefaultTableModelForTabY)tab.getModel();
        Object volObj = model.getValueAt( selRow, 1 );
        if( volObj instanceof VolumeMapWrapper ){
            volMap = ((VolumeMapWrapper)volObj).volMap;
            if( volMap.isMtppProtect() ) return;
            diskLabel = volMap.getVolDiskLabel();
        }else{
            mdi = ((MirrorDiskInfoWrapper)volObj).mdi;
            diskLabel = mdi.getSrc_agent_mp();
        }
        
        if( cluster != null ){
            prof = view.initor.mdb.getBelongedDg1( cluster.getCluster_id(),diskLabel );
        }else{
            prof = view.initor.mdb.getBelongedDg( host.getID(),diskLabel );
        }
        if( !prof.isValidDriveGrp() ) return;
        
        JComboBox comboBox = (JComboBox)this.getComponent();
        selObj = comboBox.getSelectedItem();

        ArrayList<PPProfileItem> elements = prof.getElements();
        int size = elements.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = elements.get( i );
            if( item.getVolMap().getVolDiskLabel().equals( diskLabel ) ){ continue; }

            int row = pane.getValueOnDiskLabel( item.getVolMap().getVolDiskLabel() );
            if( row == -1 ) continue;

            pane.setVersionObjOfSameTime( row,selObj );
        }
    }

    public MyDefaultCellEditor( final JComboBox comboBox,BootHost host,SelectSnapshotPane pane,JTableY tab,SanBootView view ) {
        this( comboBox,host,null,pane,tab,view );
    }
    
    public MyDefaultCellEditor( final JComboBox comboBox,BootHost host,Cluster cluster,SelectSnapshotPane pane,JTableY tab,SanBootView view ) {
        super( comboBox );

        this.tab = tab;
        this.view = view;
        this.pane = pane;
        this.host = host;
        this.cluster = cluster;

        comboBox.addItemListener( new java.awt.event.ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                actPerformed();
            }
        });
    }
}
