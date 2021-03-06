/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * QueryInitProgressDialog.java
 *
 * Created on 2010-6-22, 17:38:30
 */

package guisanboot.cmdp.ui;

import guisanboot.cluster.entity.Cluster;
import guisanboot.cmdp.entity.InitProgressRecord;
import guisanboot.cmdp.service.InitProgressInfoReceiver;
import guisanboot.data.BootHost;
import guisanboot.ui.SanBootView;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import mylib.UI.BrowserTable;
import mylib.UI.GeneralBrowserTableCell;

/**
 *
 * @author zjj
 */
public class QueryInitProgressDialog extends javax.swing.JDialog implements PropertyChangeListener{

    /** Creates new form QueryInitProgressDialog */
    public QueryInitProgressDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public QueryInitProgressDialog( SanBootView view,BootHost host,Cluster cluster ) {
        this( view, true );
        myInit( view,host,cluster );
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
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitForm1(){
        // InitProgressGeter还没有调用enableButton(true),所以不能关闭窗口
        if( receiver == null ) return;
        
        if( receiver != null ){
            receiver.removePropertyChangeListener( (PropertyChangeListener)this );
            // 在退出之前,将receiver给阻塞掉
            receiver.requestSuspend();

            this.receiver = null;
        }

        this.dispose();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if( receiver != null ){
            receiver.removePropertyChangeListener( (PropertyChangeListener)this );
            // 在退出之前,将receiver给阻塞掉
            receiver.requestSuspend();
            
            this.receiver = null;
        }

        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QueryInitProgressDialog dialog = new QueryInitProgressDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    SanBootView view;
    BootHost host;
    Cluster cluster;
    BrowserTable taskTable = new BrowserTable();
    InitProgressInfoReceiver receiver = null;

    private void myInit( SanBootView view,BootHost host,Cluster cluster ){
        this.view = view;
        this.host = host;
        this.cluster = cluster;

        setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        jButton1.setEnabled( false );
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                exitForm1();
            }
            @Override public void windowClosed(java.awt.event.WindowEvent e) {
                exitForm1();
            }
        });
        jScrollPane1.getViewport().setBackground( Color.white );
        jScrollPane1.getViewport().add( taskTable, null );
        this.setupTaskTable();
        setupLanguage();   
    }

    private void setupLanguage(){
        this.setTitle( SanBootView.res.getString("QueryInitProgressDialog.title") );
        this.jButton1.setText( SanBootView.res.getString("common.button.close"));
    }

    public BrowserTable getTable(){
        return taskTable;
    }

    private void setupTaskTable(){
        Object[] title = new Object[]{
            SanBootView.res.getString("QueryInitProgressDialog.table.disk"),
            SanBootView.res.getString("QueryInitProgressDialog.table.percent"),
            SanBootView.res.getString("QueryInitProgressDialog.table.speed"),
            SanBootView.res.getString("QueryInitProgressDialog.table.restTime"),
            SanBootView.res.getString("QueryInitProgressDialog.table.elapsedTime"),
            SanBootView.res.getString("QueryInitProgressDialog.table.handled")
        };
        taskTable.setupTitle( title );
        
        // 由InitProgressGeter来获取第一次的init-progress record list
        
        int[][] widthList = new int[][]{
            {0,55}, {1,130},{2,130},{3,130},{4,130},{5,130}
        };
        taskTable.setupTableColumnWidth( widthList );
        taskTable.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        taskTable.getTableHeader().setReorderingAllowed( false );
    }

    public void enableButton( boolean val ){
        this.jButton1.setEnabled( val );
        if( !val)
            setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        else
            setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );

        // 在第一次获取了Init progress 信息之后,才注册监听器
        // 这一点非常重要
        receiver = view.initor.mdb.getInitProgressMonitor();
        if( receiver != null ){
            receiver.addPropertyChangeListener( this );
            receiver.setHost( host );
            receiver.setCluster( cluster );
            // 唤醒receiver, 使之继续工作
            receiver.setIsFirstFlag( true ); // 重新发现一下谁是master in cluster
            receiver.requestResume();
        }
    }

    public void propertyChange(PropertyChangeEvent evt){
        String msg = evt.getPropertyName();
        // 必须得到跟监控的主机ip一致的消息，否则就不显示.
        // 当在两台机器之间切换查询状态时，会暂时查询出关于上一台机器的信息，
        // 所以要加上ip来区分
        String ip = (this.cluster!=null)?this.cluster.getClusterIP():host.getIP();
        if( msg.equals( "INITPROGRESSINFO" + ip ) ){
            updateTable( true,"" );
        }
    }

    private void updateTable( boolean normal,String msg ){
        int i,size;

        if( this.receiver == null ) return;

        if( normal ){
            ArrayList<InitProgressRecord> recList = receiver.getInitProgress();
            size = recList.size();
            for( i=0; i<size; i++ ){
                updateRecItemOnTable( recList.get( i ) );
            }
        }else{
            // 在GUI上显示错误
            DefaultTableModel model = (DefaultTableModel)taskTable.getModel();
            int diskCol = taskTable.getColumn(
                SanBootView.res.getString("QueryInitProgressDialog.table.disk")
            ).getModelIndex();
            int percentCol = taskTable.getColumn(
                SanBootView.res.getString("QueryInitProgressDialog.table.percent")
            ).getModelIndex();
            size = model.getRowCount();
            for( i=0;i<size;i++ ){
                taskTable.setValueAt(
                    new GeneralBrowserTableCell(
                        -1, msg, JLabel.LEFT
                    ),
                    i,percentCol
                );
            }
        }
    }

    private void updateRecItemOnTable( InitProgressRecord _rec ){
        boolean finded = false;

        DefaultTableModel model = (DefaultTableModel)taskTable.getModel();
        int diskCol = taskTable.getColumn(
            SanBootView.res.getString("QueryInitProgressDialog.table.disk")
        ).getModelIndex();

        int cnt = model.getRowCount();
        for( int i=0;i<cnt;i++ ){
            InitProgressRecord rec = (InitProgressRecord)taskTable.getValueAt( i,diskCol );
            if( rec.getDisk().equals( _rec.getDisk() ) ){
                if( !isSameRecord( rec,_rec ) ){
                    updataLineOnTable( i,_rec );
                }
                finded = true;
                break;
            }
        }

        if( !finded ){
//System.out.println("insert new record into 0 row...........");
            this.inserOneLine( _rec,0 );
        }
    }

    private boolean isSameRecord( InitProgressRecord oold,InitProgressRecord nnew){
        try{
            if( (oold.getDisk().equals( nnew.getDisk() ) )&&
                (oold.getPercent().equals( nnew.getPercent() ))&&
                (oold.getSpeed().equals( nnew.getSpeed() ) ) &&
                (oold.getRemainTime().equals( nnew.getRemainTime() ) ) &&
                (oold.getElapsedTime().equals( nnew.getElapsedTime() ) ) &&
                (oold.getFinished().equals( nnew.getFinished() ) )
            ){
//System.out.println(" the record is same ...........");
                return true;
            }else{
//System.out.println(" the record is not same ...........");
                return false;
            }
        }catch(Exception ex){
            return false;
        }
    }

    private void updataLineOnTable( int line,InitProgressRecord rec ){
        int percentCol = taskTable.getColumn(
            SanBootView.res.getString("QueryInitProgressDialog.table.percent")
        ).getModelIndex();
        int leftCol = taskTable.getColumn(
             SanBootView.res.getString("QueryInitProgressDialog.table.restTime")
        ).getModelIndex();
        if( !rec.isInitState() ){
            if( !rec.isNotConnectToClient() ){
                taskTable.setValueAt(
                    new GeneralBrowserTableCell(
                        -1,"100%",JLabel.LEFT
                    ),
                    line, percentCol
                );

                taskTable.setValueAt(
                    new GeneralBrowserTableCell(
                        -1,"0",JLabel.LEFT
                    ),
                    line,leftCol
                );
            }else{
                taskTable.setValueAt(
                    new GeneralBrowserTableCell(
                        -1,SanBootView.res.getString("common.conError"),JLabel.LEFT
                    ),
                    line, percentCol
                );
            }

            // 不更新其他的东东
            return;
        }
        
        int diskCol = taskTable.getColumn(
            SanBootView.res.getString("QueryInitProgressDialog.table.disk")
        ).getModelIndex();
        taskTable.setValueAt(
            rec,
            line,
            diskCol
        );

        taskTable.setValueAt(
            new GeneralBrowserTableCell(
                -1,rec.getPercent(),JLabel.LEFT
            ),
            line, percentCol
        );

        int speedCol = taskTable.getColumn(
            SanBootView.res.getString("QueryInitProgressDialog.table.speed")
        ).getModelIndex();
        taskTable.setValueAt(
            new GeneralBrowserTableCell(
                -1,rec.getSpeed(),JLabel.LEFT
            ),
            line,speedCol
        );

        taskTable.setValueAt(
            new GeneralBrowserTableCell(
                //-1,rec.getRemainTime(),JLabel.LEFT
                -1,rec.getFormattedRemainTime(),JLabel.LEFT
            ),
            line,leftCol
        );

        int usedTimeCol = taskTable.getColumn(
            SanBootView.res.getString("QueryInitProgressDialog.table.elapsedTime")
        ).getModelIndex();
        taskTable.setValueAt(
            new GeneralBrowserTableCell(
                //-1,rec.getElapsedTime(),JLabel.LEFT
                -1,rec.getFormattedElapsedTime(),JLabel.LEFT
            ),
            line,usedTimeCol
        );

        int finishedCol = taskTable.getColumn(
            SanBootView.res.getString("QueryInitProgressDialog.table.handled")
        ).getModelIndex();
        taskTable.setValueAt(
            new GeneralBrowserTableCell(
                -1,rec.getFinished(),JLabel.LEFT
            ),
            line,finishedCol
        );
    }
    
    private void inserOneLine( InitProgressRecord rec,int line ){
        Object[] one = new Object[6];

        one[0] = rec;

        one[1] = new GeneralBrowserTableCell(
            -1,rec.getPercent(),JLabel.LEFT
        );

        one[2] = new GeneralBrowserTableCell(
            -1,rec.getSpeed(),JLabel.LEFT
        );

        one[3] = new GeneralBrowserTableCell(
            //-1,rec.getRemainTime(),JLabel.LEFT
            -1,rec.getFormattedRemainTime(),JLabel.LEFT
        );

        one[4] = new GeneralBrowserTableCell(
            //-1,rec.getElapsedTime(),JLabel.LEFT
            -1,rec.getFormattedElapsedTime(),JLabel.LEFT
        );

        one[5] = new GeneralBrowserTableCell(
            -1,rec.getFinished(),JLabel.LEFT
        );
        
        if( line>=0 ){
            DefaultTableModel model = (DefaultTableModel)taskTable.getModel();
            model.insertRow( line ,one );
        }else{
            taskTable.insertRow( one );
        }
    }
}
