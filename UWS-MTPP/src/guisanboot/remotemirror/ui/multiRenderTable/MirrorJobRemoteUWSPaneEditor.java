/*
 * MirrorJobRemoteUWSPaneEditor.java
 *
 * Created on Aug 30, 2010, 10:21 AM
 */

package guisanboot.remotemirror.ui.multiRenderTable;

import guisanboot.data.Pool;
import guisanboot.data.PoolWrapper;
import guisanboot.data.UWSrvNode;
import guisanboot.res.ResourceCenter;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import guisanboot.ui.*;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */

// 点击poolComboBox也会引发获取pool的线程，不知为何，暂且这样！（2010.8.30）

public class MirrorJobRemoteUWSPaneEditor extends AbstractCellEditor implements TableCellEditor {
    JComboBox serverComboBox = new JComboBox();
    JComboBox poolComboBox = new JComboBox();
    ArrayList cache = new ArrayList();
    
    SanBootView view; 
    JPanel pane = new JPanel();
    
    public MirrorJobRemoteUWSPaneEditor( ArrayList uwsNodeList,SanBootView _view ) {
        view = _view;
        setupDestUWSrv( uwsNodeList );

        serverComboBox.setBorder( javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1) );
        int height = serverComboBox.getPreferredSize().height;
        serverComboBox.setPreferredSize( new Dimension(100,height ) );
        poolComboBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1) );
        height = poolComboBox.getPreferredSize().height;
        poolComboBox.setPreferredSize( new Dimension( 180, height ) );
        pane.setLayout( new FlowLayout( java.awt.FlowLayout.CENTER, 5, 0 ) );
        pane.add( serverComboBox,0 );
        pane.add( poolComboBox,1 );
        pane.setBackground( Color.white );

        serverComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverComboBoxActionPerformed( evt );
            }
        });
    }

    private void setupDestUWSrv( ArrayList list ){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            UWSrvNode uws = (UWSrvNode)list.get( i );
            this.serverComboBox.addItem( uws );
        }
    }

    private void serverComboBoxActionPerformed(java.awt.event.ActionEvent evt){ 
        GetAllPoolThread thread;
        int i,size,uws_port;
        String uws_ip;
        PoolListItem poolListItem;
        
        Object obj = this.serverComboBox.getSelectedItem();
        if( obj == null ) return;

        UWSrvNode uws = (UWSrvNode)obj;
        uws_ip = uws.getUws_ip();
        uws_port = uws.getUws_port();

        ArrayList pools = hasOne( uws_ip,uws_port );
        if( pools == null ){
            thread = new GetAllPoolThread(
                view,
                uws_ip,
                uws_port
            );
            view.startupProcessDiag(
                SanBootView.res.getString("View.pdiagTitle.getPool2"),
                SanBootView.res.getString("View.pdiagTip.getPool2"),
                thread
            );

            this.poolComboBox.removeAllItems();
            ArrayList list = thread.getRet();
            if( list == null ){
                JOptionPane.showMessageDialog( view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_POOL_REMOTE_UWS ) +" : "+view.initor.mdb.getErrorMessage()
                );
                return;
            }else{
                size = list.size();
                if( size <= 0 ){
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.noPool1")
                    );
                    return;
                }else{
                    boolean hasEnoughSpace = false;
                    for( i=0; i<size; i++ ){
                        Pool pool = (Pool)list.get(i);
                        if( pool.getRealFreeSize() > 0 ){
                            hasEnoughSpace = true;
                            break;
                        }
                    }

                    if( !hasEnoughSpace ){
                        JOptionPane.showMessageDialog( view,
                            SanBootView.res.getString("MenuAndBtnCenter.error.noEnoughSpace1")
                        );
                        return;
                    }

                    pools = thread.getWraperRet();
                    size = pools.size();
                    for( i=0; i<size; i++ ){
                        this.poolComboBox.addItem( pools.get(i) );
                    }

                    poolListItem = new PoolListItem( uws_ip,uws_port, pools );
                    cache.add( poolListItem );
                }
            }
        }else{
            this.poolComboBox.removeAllItems();
            size = pools.size();
            for( i=0; i<size; i++ ){
                this.poolComboBox.addItem( pools.get(i) );
            }
        }
        pane.validate();
        pane.repaint();
    }

    private ArrayList hasOne( String uwsIP,int uwsPort ){
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            PoolListItem item =(PoolListItem)cache.get(i);
            if( item.uws_ip.equals( uwsIP ) && ( item.uws_port == uwsPort ) ){
                return item.pools;
            }
        }
        return null;
    }

    private void getPoolList( WrapperOfRemoteUWSAndPool wrapper ){
        ComboBoxModel model = this.poolComboBox.getModel();
        int size = model.getSize();
        for( int i=0; i<size; i++ ){
            PoolWrapper pool = (PoolWrapper)model.getElementAt(i);
            wrapper.addPoolItem( pool );
        }
    }

    public Object getCellEditorValue() {
        if( this.poolComboBox.getSelectedItem() == null ){
            if( this.poolComboBox.getModel().getSize() > 0 ){
                this.poolComboBox.setSelectedIndex( 0 );
            }
        }
        
        WrapperOfRemoteUWSAndPool wrapper = new WrapperOfRemoteUWSAndPool(
           (UWSrvNode)this.serverComboBox.getSelectedItem(),
           (PoolWrapper)this.poolComboBox.getSelectedItem()
        );
        this.getPoolList( wrapper );
        return wrapper;
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int r, int c
    ) {
        if( value != null ){
//System.out.println("MirrorJobRemoteUWSPaneEditor(getTableCellEditorComponent) Value not null : "+value.toString() );
            WrapperOfRemoteUWSAndPool wrapper = (WrapperOfRemoteUWSAndPool)value;
            this.serverComboBox.setSelectedItem( wrapper.uwsNode );
            this.poolComboBox.setSelectedItem( wrapper.pool );
        }
        return pane;
    }
}

class PoolListItem {
    public String uws_ip;
    public int uws_port;
    public ArrayList pools;

    public PoolListItem( String uws_ip,int uws_port,ArrayList pools ){
        this.uws_ip = uws_ip;
        this.uws_port = uws_port;
        this.pools = pools;
    }
}