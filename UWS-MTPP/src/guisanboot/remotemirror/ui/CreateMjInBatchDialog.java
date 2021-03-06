/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CreateMjInBatchDialog.java
 *
 * Created on 2010-8-27, 16:31:01
 */

package guisanboot.remotemirror.ui;

import guisanboot.data.BootHost;
import guisanboot.data.MirrorGrp;
import guisanboot.data.MirrorJob;
import guisanboot.data.UWSrvNode;
import guisanboot.data.VolumeMap;
import guisanboot.remotemirror.ui.multiRenderTable.MirrorJobOptionPaneEditor;
import guisanboot.remotemirror.ui.multiRenderTable.MirrorJobRemoteUWSPaneEditor;
import guisanboot.remotemirror.ui.multiRenderTable.MjOption;
import guisanboot.remotemirror.ui.multiRenderTable.MyDefaultTableModelForTableForCreateMj;
import guisanboot.remotemirror.ui.multiRenderTable.TableForCreateMj;
import guisanboot.remotemirror.ui.multiRenderTable.WrapperOfRemoteUWSAndPool;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.InitProgramDialog;
import guisanboot.ui.InputPasswordDiag;
import guisanboot.ui.SanBootView;
import guisanboot.ui.SlowerLaunch;
import guisanboot.ui.multiRenderTable.CheckBoxEditor;
import guisanboot.ui.multiRenderTable.MyComboBoxEditor;
import guisanboot.ui.multiRenderTable.RowEditorModel;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author zjj
 */
public class CreateMjInBatchDialog extends javax.swing.JDialog implements SlowerLaunch{

    /** Creates new form CreateMjInBatchDialog */
    public CreateMjInBatchDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public CreateMjInBatchDialog( SanBootView view ) {
        this( view,true );
        myInit( view );
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 65));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setPreferredSize(new java.awt.Dimension(400, 25));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 3));

        jCheckBox1.setText("Select All");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateMjInBatchDialog.this.actionPerformed(evt);
            }
        });
        jPanel3.add(jCheckBox1);

        jLabel1.setText("Control Opt :");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1));
        jPanel3.add(jLabel1);

        jCheckBox2.setText("Encry");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jPanel3.add(jCheckBox2);

        jCheckBox3.setText("Compress");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        jPanel3.add(jCheckBox3);

        jCheckBox4.setText("Copy Branch");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });
        jPanel3.add(jCheckBox4);

        jCheckBox5.setText("Auto Delete View");
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });
        jPanel3.add(jCheckBox5);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel4.setPreferredSize(new java.awt.Dimension(400, 33));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 35, 5));

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1);

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        retVal = new Integer(0);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionPerformed
        // TODO add your handling code here:
        do_selCheckBox();
    }//GEN-LAST:event_actionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        do_ok();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
        do_selEncryOpt();
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
        do_selCompressOpt();
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        // TODO add your handling code here:
        do_selCopyBranch();
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        // TODO add your handling code here:
        do_selAutoDelView();
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CreateMjInBatchDialog dialog = new CreateMjInBatchDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    SanBootView view;
    TableForCreateMj table;

    private String status = "";
    private int cnt = 0;
    private int totalStep = 0;
    private String errormsg = "";
    private Object retVal = null;

    private void myInit( SanBootView view ){
        this.view = view;
        setupLanguage();
        setupTable();
    }

    private void setupLanguage(){
        this.setTitle( SanBootView.res.getString("CreateMjInBatchDialog.title") );
        this.jButton1.setText( SanBootView.res.getString("common.button.ok"));
        this.jButton2.setText( SanBootView.res.getString("common.button.cancel"));
        this.jCheckBox1.setText( SanBootView.res.getString("common.selectAll") );
        this.jLabel1.setText( SanBootView.res.getString("ModifyMjInBatchDialog.label.crtOpt") );
        this.jCheckBox2.setText( SanBootView.res.getString("EditMirrorJobDialog.checkbox.des") );
        this.jCheckBox3.setText( SanBootView.res.getString("EditMirrorJobDialog.checkbox.compress") );
        this.jCheckBox4.setText( SanBootView.res.getString("EditMirrorJobDialog.checkbox.copybranch") );
        this.jCheckBox5.setText( SanBootView.res.getString("EditMirrorJobDialog.checkbox.delview") );
    }

    private void do_selCheckBox(){
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            model.setValueAt( new Boolean( this.jCheckBox1.isSelected() ), row, 0 );
        }
        fireEditingStopMsg();
    }

    private void do_selEncryOpt(){
        boolean isSel = this.jCheckBox2.isSelected();

        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            MjOption mjOpt = (MjOption)model.getValueAt( row,6 );
            mjOpt.isEncrySeled = isSel;
            model.setValueAt( mjOpt, row, 6 );
        }
        fireEditingStopMsg();
    }

    private void do_selCompressOpt(){
        boolean isSel = this.jCheckBox3.isSelected();

        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            MjOption mjOpt = (MjOption)model.getValueAt( row,6 );
            mjOpt.isCompressedSeled = isSel;
            model.setValueAt( mjOpt, row, 6 );
        }
        fireEditingStopMsg();
    }

    private void do_selCopyBranch(){
        boolean isSel = this.jCheckBox4.isSelected();

        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            MjOption mjOpt = (MjOption)model.getValueAt( row,6 );
            mjOpt.isCopyBranchSeled = isSel;
            model.setValueAt( mjOpt, row, 6 );
        }
        fireEditingStopMsg();
    }

    private void do_selAutoDelView(){
        boolean isSel = this.jCheckBox5.isSelected();

        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            MjOption mjOpt = (MjOption)model.getValueAt( row,6 );
            mjOpt.isDeleteViewSeled = isSel;
            model.setValueAt( mjOpt, row, 6 );
        }
        fireEditingStopMsg();
    }

    public void fireEditingStopMsg(){
        TableCellEditor dce;

        AbstractTableModel model = (AbstractTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int i=0; i<lineNum; i++  ){
            for( int j=0; j<8; j++ ){
                if( j == 1 || j == 2 ) continue;
                
                dce = table.getCellEditor( i,j );
                if( dce != null ){
                    try{
                        while( !dce.stopCellEditing() ){}
                    }catch(Exception ex){}
                }
            }
        }
    }
    
    private void setupTable(){
        Vector list;
        String fs;

        Vector hostList = view.initor.mdb.getAllBootHost();
        ArrayList<VolumeMap> volMapList = new ArrayList<VolumeMap>();
        int size = hostList.size();
        for( int i=0; i<size; i++ ){
            BootHost host = (BootHost)hostList.elementAt( i );
            if( host.isWinHost() ){
                list = view.initor.mdb.getVolMapOnClntID( host.getID() );
            }else{
                list = view.initor.mdb.getRealLVListOnClntID( host.getID() );
            }
            int size1 = list.size();
            for( int j=0; j<size1; j++ ){
                volMapList.add( (VolumeMap)list.elementAt( j ) );
            }
        }
        ArrayList uwsSrvList = view.initor.mdb.getAllUWSrv();
        int num = volMapList.size();
        int colNum = 8;

        Object[][] data = new Object[num][colNum];
        Object[] header = new Object[colNum];
        Object[] label = new Object[num];

        header[0] = SanBootView.res.getString("common.sel");
        header[1] = SanBootView.res.getString("common.host");
        header[2] = SanBootView.res.getString("SelectProtectedSysVolPane.table.vol.disk");
        header[3] = SanBootView.res.getString("View.table.mj.name");
        header[4] = SanBootView.res.getString("View.table.mj.type");
        header[5] = SanBootView.res.getString("View.table.mj.swu");
        header[6] = SanBootView.res.getString("View.table.mj.opt");
        header[7] = SanBootView.res.getString("View.table.mj.desc");

        for( int i=0; i<num; i++ ){
            VolumeMap volMap = (VolumeMap)volMapList.get(i);

            data[i][0] = new Boolean( false );
            BootHost host = view.initor.mdb.getBootHostFromVector( volMap.getVolClntID() );
            if( host != null ){
                data[i][1] = host;
            }else{
                // 找不到volMap对应的主机对象，放弃为这个volMap创建mj
                continue;
            }
            
            data[i][2] = volMap.getVolDiskLabel();
            if( host.isWinHost() ){
                fs = volMap.getVolDiskLabel().substring(0,1).toUpperCase();
            }else{
                fs = volMap.getVolDiskLabel();
            }
            if( host != null ){
                data[i][3] = "MJ_"+fs+"_"+host.getIP();
            }else{
                data[i][3] = "MJ_"+fs+"_"+volMap.getVolClntID();
            }
            data[i][4] = SanBootView.res.getString("common.mjtype.remote");
            if( uwsSrvList.size() > 0 ){
                data[i][5] = new WrapperOfRemoteUWSAndPool( (UWSrvNode)uwsSrvList.get(0),null );
            }else{
                data[i][5] = new WrapperOfRemoteUWSAndPool( null,null );
            }

            if( volMap.isMtppProtect() ){
                data[i][6] = new MjOption( true,false,false,false,false );
            }else{
                if( host.isWinHost() && volMap.getVolDiskLabel().substring(0,1).toUpperCase().equals("C") ){
                    data[i][6] = new MjOption( true,false,false,false,true );
                }else{
                    data[i][6] = new MjOption( true,false,false,false,false );
                }
            }
            data[i][7] = "";

            label[i]= volMap;
        }

        MyDefaultTableModelForTableForCreateMj model = new MyDefaultTableModelForTableForCreateMj( data,header,label );

        table = new TableForCreateMj( model,uwsSrvList );
        table.setRowHeight( 20 );
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        RowEditorModel rm = new RowEditorModel();
        ((TableForCreateMj)table).setRowEditorModel( rm );

        CheckBoxEditor cb = new CheckBoxEditor();
        rm.addEditorForRow( 0, cb );

        ArrayList mjTypeList = new ArrayList(3);
        mjTypeList.add( SanBootView.res.getString("common.mjtype.remote") );
        mjTypeList.add( SanBootView.res.getString("common.mjtype.localtrack") );
        mjTypeList.add( SanBootView.res.getString("common.mjtype.remotetrack") );
        MyComboBoxEditor mcb = new MyComboBoxEditor( mjTypeList );
        rm.addEditorForRow( 4, mcb );

        MirrorJobRemoteUWSPaneEditor remoteUwsEditor = new MirrorJobRemoteUWSPaneEditor( uwsSrvList,view );
        rm.addEditorForRow( 5,remoteUwsEditor );

        MirrorJobOptionPaneEditor mjOptEditor = new MirrorJobOptionPaneEditor();
        rm.addEditorForRow( 6,mjOptEditor );

        TableColumnModel tableColumnModel = table.getColumnModel();
        tableColumnModel.getColumn(0).setWidth( 45 );
        tableColumnModel.getColumn(1).setWidth( 140 );
        tableColumnModel.getColumn(2).setWidth( 40 );
        tableColumnModel.getColumn(3).setWidth( 150 );
        tableColumnModel.getColumn(4).setWidth( 100 );
        tableColumnModel.getColumn(5).setWidth( 300 );
        tableColumnModel.getColumn(6).setWidth( 385 );
        tableColumnModel.getColumn(7).setWidth( 175 );
        for( int j=0;j<colNum;j++ )
            table.sizeColumnsToFit(j);

        table.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        table.getTableHeader().setReorderingAllowed(false);

        jScrollPane1.getViewport().add( table,null );
        jScrollPane1.getViewport().setBackground( Color.white );
    }

    private int getMjOption( MjOption mjOpt ){
        int opt = 0;
        if( mjOpt.isContinueTransferSeled){
            opt |= MirrorJob.MJ_OPT_CONTINUE;
        }
        if( mjOpt.isEncrySeled ){
            opt |= MirrorJob.MJ_OPT_ENCRY;
        }
        if( mjOpt.isCompressedSeled ){
            opt |= MirrorJob.MJ_OPT_ZIP;
        }
        if( mjOpt.isCopyBranchSeled ){
            opt |= MirrorJob.MJ_OPT_BRANCH;
        }
        if( mjOpt.isDeleteViewSeled ){
            opt |= MirrorJob.MJ_OPT_DEL_VIEW;
        }
        return opt;
    }

    ArrayList<MirrorJob> mjList = new ArrayList<MirrorJob>();
    private void do_ok(){
        int root_id,clntId;
        VolumeMap volMap;
        MirrorGrp mg;
        String local_hostName="",remote_hostName;
        boolean hasErr = false,isOk;
        HashMap<Integer,String> map = new HashMap<Integer,String>();
        HashMap<String,Integer> pwdMap = new HashMap<String,Integer>();

        this.fireEditingStopMsg();

        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int lineNum = model.getRowCount();
        for( int row=0; row<lineNum; row++ ){
            if( !((Boolean)model.getValueAt( row, 0 )).booleanValue() ) continue;

            Object host = model.getValueAt( row,1 );
            String disk = (String)model.getValueAt( row, 2 );
            String job_name= (String)model.getValueAt( row,3 );
            String job_type = (String)model.getValueAt(row,4 );
            WrapperOfRemoteUWSAndPool wrapper = (WrapperOfRemoteUWSAndPool)model.getValueAt( row,5 );
            String job_desc = (String)model.getValueAt( row,7 );
            MjOption mjOpt = (MjOption)model.getValueAt( row,6 );

            if( job_name.equals("") ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("EditMirrorJobDialog.error.noneName")
                );
                hasErr = true;
                break;
            }

            if( job_name.getBytes().length > 254 ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("EditMirrorJobDialog.error.tooLongName") + " : " + job_name
                );
                hasErr = true;
                break;
            }
            
            if( !job_desc.equals("") ){
                if( job_desc.getBytes().length > 254 ){
                    JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("EditMirrorJobDialog.error.tooLongDesc") +" : " + job_desc
                    );
                    hasErr = true;
                    break;
                }
            }

            int type;
            if( job_type.equals( SanBootView.res.getString("common.mjtype.remote") ) ){
                type = MirrorJob.MJ_TYPE_REMOTE;
            }else if( job_type.equals( SanBootView.res.getString("common.mjtype.localtrack") ) ){
                type = MirrorJob.MJ_TYPE_LOCAL_TRACK_JOB;
            }else{
                type = MirrorJob.MJ_TYPE_REMOTE_TRACK_JOB;
            }
SanBootView.log.debug(this.getClass().getName(), "mirror job type to create: " + type );

            if( wrapper.uwsNode == null ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("EditMirrorJobDialog.error.noneSWUSrvNode")
                );
                hasErr = true;
                break;
            }

            if( wrapper.pool == null ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("EditMirrorJobDialog.error.nonePool") +" : "+wrapper.uwsNode.getUws_ip()
                );
                hasErr = true;
                break;
            }
            
            if( wrapper.pool.pool.getRealFreeSize() <= 0 ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("EditMirrorJobDialog.error.insufficientPool") + " : " +
                    wrapper.uwsNode.getUws_ip()+"/"+wrapper.pool.pool.getPool_name()
                );
                hasErr = true;
                break;
            }

            if( pwdMap.get( wrapper.uwsNode.getUws_id()+wrapper.pool.pool.getPool_id()+"" ) == null ){
                if( !wrapper.pool.pool.getPool_passwd().equals("") ){
                    InputPasswordDiag inputPass = new InputPasswordDiag( view,wrapper.uwsNode,wrapper.pool );
                    int width  = 270+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                    int height = 155+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                    inputPass.setSize( width,height );
                    inputPass.setLocation( getCenterPoint( width,height ) );
                    inputPass.setVisible( true );

                    String passwd = inputPass.getPasswd();
                    if( passwd == null ) {
                        hasErr = true;
                        break;
                    }else{
                        pwdMap.put( wrapper.uwsNode.getUws_id()+wrapper.pool.pool.getPool_id()+"", new Integer( wrapper.pool.pool.getPool_id() ) ) ;
                    }
                }else{
SanBootView.log.error(getClass().getName(), "there is no password for pool on destinated swu server: " + wrapper.uwsNode.getUws_ip() );
                    JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("EditMirrorJobDialog.error.nopasswd")+" : " +
                        wrapper.uwsNode.getUws_ip()+"/"+wrapper.pool.pool.getPool_name()
                    );
                    hasErr = true;
                    break;
                }
            }
            
            if( type == MirrorJob.MJ_TYPE_LOCAL_TRACK_JOB ){
                if( local_hostName.equals("") ){
                    view.initor.mdb.targetSrvName = null;
                    local_hostName = view.initor.mdb.getHostName();
                    if( local_hostName.equals("") ){
                        JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("EditMirrorJobDialog.error.getSWUHostName")
                        );
                        hasErr = true;
                        break;
                    }
                }

                view.initor.mdb.targetSrvName = null;
                remote_hostName = map.get( new Integer( wrapper.uwsNode.getUws_id() ) );
                if( remote_hostName == null ){
                    remote_hostName = view.initor.mdb.getHostName( wrapper.uwsNode.getUws_ip(), wrapper.uwsNode.getUws_port(),
                        wrapper.pool.pool.getPool_id(), wrapper.pool.pool.getPool_passwd()
                    );
                    if( remote_hostName.equals("") ){
                        JOptionPane.showMessageDialog(this,
                            SanBootView.res.getString("EditMirrorJobDialog.error.getSWUHostName")
                        );
                        hasErr = true;
                        break;
                    }else{
                        map.put( new Integer( wrapper.uwsNode.getUws_id()), remote_hostName );
                    }
                }

                if( !local_hostName.equals( remote_hostName ) ){
                    JOptionPane.showMessageDialog(this,
                        SanBootView.res.getString("EditMirrorJobDialog.error.mustLocalSWU")
                    );
                    hasErr = true;
                    break;
                }
            }

            MirrorJob mj  = new MirrorJob(
                job_name,
                wrapper.uwsNode.getUws_ip(),
                wrapper.uwsNode.getUws_port(),
                this.getMjOption( mjOpt ),
                wrapper.pool.pool.getPool_id(),
                wrapper.pool.pool.getPool_passwd(),
                job_desc
            );

            volMap = null;
            clntId = -1;
            if( host instanceof BootHost ){
                BootHost bootHost = (BootHost)host;
                clntId = bootHost.getID();
                volMap = view.initor.mdb.generalGetVolMapFromVecOnClntandLabel( clntId, disk );
            }else{
                String _clntId = (String)host;
                try{
                    clntId = Integer.parseInt( _clntId );
                    volMap = view.initor.mdb.generalGetVolMapFromVecOnClntandLabel( clntId, disk );
                }catch(Exception ex){}
            }

            if( volMap != null ){
                root_id = volMap.getVol_rootid();
                mj.setMj_current_rootid( root_id );
            }else{
SanBootView.log.error( getClass().getName(),"Can't find volmap from mdb according boot host id : " + clntId +" and disk label: " + disk );
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("EditMirrorJobDialog.error.noVolInfo")+" : " +
                    host.toString()+"/"+disk
                );
                hasErr = true;
                break;
            }
            
            if( type != MirrorJob.MJ_TYPE_REMOTE ){
                mj.setMj_job_type( type );
                mj.setMj_track_src_rootid( root_id );
                mj.setMj_track_mode( MirrorJob.MJ_TRACK_MODE_AFT_CRT_SNAP );
                mj.setMj_track_src_type( MirrorJob.MJ_TRACK_SRC_TYPE_LOCALDISK );
            }

            mjList.add( mj );
        }
        
        if( hasErr ) return;

        // 图形化地显示创建过程
        status = "";
        errormsg = "";
        cnt = 0;
        totalStep = mjList.size();
        if( totalStep <=0 ) {
            JOptionPane.showMessageDialog( this,
                SanBootView.res.getString("CreateMjInBatchDialog.error.noSel")
            );
            return;
        }
        
        this.dispose();

        InitProgramDialog initDiag = new InitProgramDialog(
            view,
            SanBootView.res.getString("CreateMjInBatchDialog.title.crtMj"),
            SanBootView.res.getString("CreateMjInBatchDialog.label.tip.crtMj")
        );
        Thread initThread = new Thread( new BatchedOpForMj( initDiag,this ) );
        initThread.start();
        int width  = 300+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 120+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        initDiag.setSize( width, height );
        initDiag.setLocation( view.getCenterPoint(width,height) );
        initDiag.setVisible( true );

        retVal = new Integer(1);
    }

    public boolean init(){
        int rootid;
        MirrorGrp mg;
        boolean isFirst=true,retValue =true,isOk;

        StringBuffer errBuf = new StringBuffer();

        int size = this.mjList.size();
        for( int i=0; i<size; i++ ){
            MirrorJob mj = this.mjList.get( i );
            cnt++;
            rootid = mj.getMj_current_rootid();
            status = SanBootView.res.getString("CreateMjInBatchDialog.loadstatus.create") + " " + mj.getMj_job_name();

            isOk = true;
            if( mj.isNormalMjType() ){
SanBootView.log.info(getClass().getName(), "ready to crt mg for this mirror job(a normal job): " + mj.getMj_job_name() );
                mg = view.initor.mdb.getMGFromVectorOnRootID( rootid );
                if( mg == null ){
                    // generate a mirror group object for this mirror job
                    mg = new MirrorGrp(
                        "MG_"+rootid,
                         1,
                         rootid,
                        "MG_"+rootid
                    );
                    isOk = view.initor.mdb.addMg( mg );
                    if( !isOk ){
SanBootView.log.error( this.getClass().getName(), " failed to add mg for mj : " + mj.getMj_job_name() +", the errmsg: " +view.initor.mdb.getErrorMessage() );
                    }else{
                        mg.setMg_id( view.initor.mdb.getNewId() );
                        view.initor.mdb.addMGToVector( mg );
                        mj.setMj_mg_id( mg.getMg_id() );
                    }
                }else{
                    mj.setMj_mg_id( mg.getMg_id() );
                }
            }

            if( isOk ){
                isOk = view.initor.mdb.addMj( mj );
                if( isOk ){
SanBootView.log.info( getClass().getName(),"new mj id is: " + view.initor.mdb.getNewId() );
                    mj.setMj_id( view.initor.mdb.getNewId() );
                    view.initor.mdb.monitorMJ( mj.getMj_id() );
                    MirrorJob newMj = view.initor.mdb.getMonedMj( mj.getMj_id() );
                    if( newMj == null ){
SanBootView.log.error( this.getClass().getName(),"Can't find mj object on mj_id: "+ mj.getMj_id() );
                        isOk = false;
                    }else{
                        view.initor.mdb.addMJToVector( newMj );
                    }
                }
            }

            if( !isOk ){
                retValue = false;
                if( isFirst ){
                    errBuf.append( mj.getMj_job_name() );
                    isFirst = false;
                }else{
                    errBuf.append(","+mj.getMj_job_name() );
                }
            }
        } // for end
        
        if( !retValue ){
            errormsg = SanBootView.res.getString("CreateMjInBatchDialog.error.create") + errBuf.toString();
        }else{
            errormsg = "";
        }

        return retValue;
    }
    
    public String getLoadingStatus(){
        return status;
    }

    public int getLoadingProcessVal(){
        return ( cnt * 100 )/totalStep;
    }

    public String getInitErrMsg(){
        return errormsg;
    }

    public boolean isCrtVG(){
        return false;
    }

    public String getSrvIP(){
        return "";
    }

    public Object getRetVal(){
        return this.retVal;
    }

    public Point getCenterPoint(int width,int height){
        int x = ( getSize().width - width ) / 2 + getX();
        int y = ( getSize().height - height ) / 2 + getY();
        return new Point(x,y);
    }
}
