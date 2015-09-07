package guisanboot.datadup.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.beans.*;
import javax.swing.border.*;
import java.util.ArrayList;

import mylib.UI.*;
import guisanboot.audit.data.*;
import guisanboot.datadup.data.BackupClient;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;
import guisanboot.datadup.data.BakTask;

/**
 * Title:        Odysys UWS
 * Description:
 * Copyright:    Copyright (c) 2008
 * Company:      odysys
 * @author
 * @version 3.0
 */

public class MonitorDialog extends JDialog implements PropertyChangeListener{
    JPanel panel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    JButton killJobButton = new JButton();
    JButton activeJobButton = new JButton();
    JButton closeButton = new JButton();
    JPanel jPanel4 = new JPanel();
    Border border1;
    TitledBorder titledBorder1;
    JTextArea messageTextArea = new JTextArea();
    BorderLayout borderLayout4 = new BorderLayout();
    JScrollPane jScrollPane1 = new JScrollPane();
    BorderLayout borderLayout2 = new BorderLayout();
    BorderLayout borderLayout3 = new BorderLayout();
    private Border border2;
    JPanel jPanel3 = new JPanel();
    Border border3;

    BrowserTable taskTable = new BrowserTable();
    SanBootView view;
    ArrayList<BakTask> killJob = new ArrayList<BakTask>();
    
    public MonitorDialog(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        try {
            jbInit();
            myInit( (SanBootView)frame );
            pack();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public MonitorDialog( SanBootView view ) {
        this( view, "", true );
    }

    void jbInit() throws Exception {
        border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
        titledBorder1 = new TitledBorder(border1,"Task Message");
        border2 = BorderFactory.createEtchedBorder(Color.white,new Color(178, 178, 178));
        border3 = BorderFactory.createEmptyBorder(5,0,5,0);
        panel1.setLayout(borderLayout1);
        jPanel2.setLayout(flowLayout1);
        closeButton.setMaximumSize(new Dimension(85, 27));
        closeButton.setMinimumSize(new Dimension(85, 27));
        closeButton.setPreferredSize(new Dimension(85, 27));
        killJobButton.setMaximumSize(new Dimension(85, 27));
        killJobButton.setMinimumSize(new Dimension(85, 27));
        killJobButton.setPreferredSize(new Dimension(85, 27));
        killJobButton.setMargin(new java.awt.Insets(2, 1, 2, 1));
        activeJobButton.setMaximumSize(new Dimension(85, 27));
        activeJobButton.setMinimumSize(new Dimension(85, 27));
        activeJobButton.setPreferredSize(new Dimension(85, 27));
        activeJobButton.setMargin(new java.awt.Insets(2, 1, 2, 1));
        
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(ActionEvent e) {
            closeButton_actionPerformed(e);
          }
        });
        killJobButton.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(ActionEvent e) {
            killJobButton_actionPerformed(e);
          }
        });
        activeJobButton.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(ActionEvent e) {
            activeJobButton_actionPerformed(e);
          }
        });
        jPanel1.setLayout(borderLayout2);
        messageTextArea.setBorder(border2);
        messageTextArea.setMinimumSize(new Dimension(65, 22));
        messageTextArea.setPreferredSize(new Dimension(200, 200));
        messageTextArea.setEditable(false);
        jPanel3.setLayout(borderLayout4);
        jScrollPane1.getViewport().setBackground(Color.white);
        jScrollPane1.setPreferredSize(new Dimension(300, 200));
        borderLayout3.setHgap(40);
        jPanel2.setBorder(border3);
        getContentPane().add(panel1);
        panel1.add(jPanel1,  BorderLayout.CENTER);
        panel1.add(jPanel2,  BorderLayout.SOUTH);
        jPanel2.add(killJobButton,null);
        jPanel2.add(activeJobButton,null );
        jPanel2.add(closeButton, null);
        flowLayout1.setHgap( 35 );
        jPanel1.add(jPanel3, BorderLayout.CENTER);
        jPanel3.add(jScrollPane1,  BorderLayout.CENTER);
        jScrollPane1.getViewport().add(taskTable, null);
    }

    MonitorInfoReceiver receiver = null;

    private void myInit(SanBootView _view){
        view = _view;

        setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );

        setPermOnUser();
        setupTaskTable();

        closeButton.setEnabled( false );
        killJobButton.setEnabled( false );
        activeJobButton.setEnabled( false );
        
        setupLanguage();
    }

    private void setPermOnUser(){
        BackupUser user = view.initor.mdb.getBakUserOnName(
                                    view.initor.user
                            );
        if( user!=null ){
            if( !user.isAdminRight() ){
                killJobButton.setEnabled( false );
                activeJobButton.setEnabled( false );
            }
        }
    }
  
    public void propertyChange(PropertyChangeEvent evt){
        String msg = evt.getPropertyName();
        if( msg.equals("TASKINFO") ){
            updateTaskTable( true,"" );
        }else{
            updateTaskTable( false,msg );
        }
    }

    @Override protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if ( e.getID() == WindowEvent.WINDOW_CLOSING ) {
            if( receiver == null ) return;

            if( receiver != null ){
                receiver.removePropertyChangeListener( (PropertyChangeListener)this );
                // 在退出之前,将receiver给阻塞掉
                receiver.requestSuspend();
            }

            killJob.clear();
            dispose();
        }
    }
    
    private void updateTaskTable( boolean normal,String msg ){
        BakTask task;
        int i,size;

        if( normal ){
            ArrayList taskList = view.initor.mdb.getAllTaskList( 1 ); 
            size = taskList.size();
//System.out.println("*******************1111111111111111111111111111111: size "+size );              
            for( i=0;i<size;i++ ){
                task = ( BakTask )taskList.get(i);
                updateTaskItemOnTable( task );
            }

            size = killJob.size();
            for( i=0;i<size;i++ ){
                task = (BakTask)killJob.get(i);
                updateTaskItemOnTable( task );
            }
        }else{
            // 在GUI上显示错误
            DefaultTableModel model = (DefaultTableModel)taskTable.getModel();
            int taskIdCol = taskTable.getColumn(
                SanBootView.res.getString("MonitorDialog.table.task.id")
            ).getModelIndex();
            int msgCol = taskTable.getColumn(
                SanBootView.res.getString("MonitorDialog.table.task.msg")
            ).getModelIndex();
            size = model.getRowCount();
            for( i=0;i<size;i++ ){
                task = (BakTask)taskTable.getValueAt( i,taskIdCol );
                taskTable.setValueAt(
                    new GeneralBrowserTableCell(
                        -1, msg, JLabel.LEFT
                    ),
                    i,msgCol
                );
            }
        }
    }

    private void updateTaskItemOnTable(BakTask _task){
        boolean finded = false;
        
        DefaultTableModel model = (DefaultTableModel)taskTable.getModel();
        int taskIdCol = taskTable.getColumn(
            SanBootView.res.getString("MonitorDialog.table.task.id")
        ).getModelIndex();
        
        int cnt = model.getRowCount();
//System.out.println("*******************22222222222222222222222: "+ cnt ); 
        for( int i=0;i<cnt;i++ ){
            BakTask task = (BakTask)taskTable.getValueAt( i,taskIdCol );
            if( task.getID() == _task.getID()){
                if( !isSameTask(task,_task) ){
                    updataLineOnTable( i,_task );
                }
                finded = true;
                break;
            }
        }
        
        if( !finded ){
//System.out.println("insert new task into 0 row...........");
            this.inserOneLine( _task,0 );
        }
    }
    
    private boolean isSameTask( BakTask oold,BakTask nnew){
        try{
            if( (oold.getID() == nnew.getID())&&
                (oold.getStatus().equals( nnew.getStatus() ))&&
                (oold.getMsg().equals( nnew.getMsg() ) &&
                (oold.getProfileName().equals( nnew.getProfileName() ) ))
            ){
//System.out.println(" the task is same ...........");                
                return true;
            }else{
//System.out.println(" the task is not same ...........");                  
                return false;
            }
        }catch(Exception ex){
            return false;
        }
    }

    private void updataLineOnTable( int line,BakTask task ){
        BackupClient bkClnt;
        
        int taskIdCol = taskTable.getColumn(
            SanBootView.res.getString("MonitorDialog.table.task.id")
        ).getModelIndex();
        taskTable.setValueAt(
            task,
            line,
            taskIdCol
        );
        
        int cliCol = taskTable.getColumn(
            SanBootView.res.getString("MonitorDialog.table.task.client")
        ).getModelIndex();
        String taskType = task.getTaskType().toUpperCase();
        if( taskType.startsWith( BakTask.TASK_TYPE_BKDATA ) ||
            taskType.startsWith( BakTask.TASK_TYPE_DELOLD ) ||
            taskType.startsWith( BakTask.TASK_TYPE_CLEAN )
        ){
            taskTable.setValueAt(
                new GeneralBrowserTableCell(
                    -1,SanBootView.res.getString("common.server"),JLabel.LEFT
                ),
                line, cliCol
            );
        }else{
            try{
                int cliId = Integer.parseInt( task.getClientName() );
                bkClnt = view.initor.mdb.getClientFromVector( cliId );
                if( bkClnt != null ){
                    taskTable.setValueAt(
                        new GeneralBrowserTableCell(
                            -1,bkClnt.getHostName(),JLabel.LEFT
                        ),
                        line, cliCol
                    );
                }else{          
                    taskTable.setValueAt(
                        new GeneralBrowserTableCell(
                            -1,SanBootView.res.getString("common.unknown") ,JLabel.LEFT
                        ),
                        line, cliCol
                    );
                }
            }catch(Exception ex){
                taskTable.setValueAt(
                    new GeneralBrowserTableCell(
                        -1,task.getClientName(),JLabel.LEFT
                    ),
                    line, cliCol
                );
            }
        }
        
        int typeCol = taskTable.getColumn(
            SanBootView.res.getString("MonitorDialog.table.task.type")
        ).getModelIndex();
        taskTable.setValueAt(
            new GeneralBrowserTableCell(
                -1,getTaskType( task.getTaskType() ),JLabel.LEFT
            ),
            line,typeCol
        );
        
        int profCol = taskTable.getColumn(
            SanBootView.res.getString("MonitorDialog.table.task.prof")
        ).getModelIndex();
        taskTable.setValueAt(
            new GeneralBrowserTableCell(
                -1,task.getProfileName(),JLabel.LEFT
            ),
            line,profCol
        );
        
        int msgCol = taskTable.getColumn(
            SanBootView.res.getString("MonitorDialog.table.task.msg")
        ).getModelIndex();
        taskTable.setValueAt(
            new GeneralBrowserTableCell(
                -1,task.getMsg(),JLabel.LEFT
            ),
            line,msgCol
        );
        
        int statusCol = taskTable.getColumn(
            SanBootView.res.getString("MonitorDialog.table.task.status")
        ).getModelIndex();
        taskTable.setValueAt(
            new GeneralBrowserTableCell(
                -1,task.getStatus(),JLabel.RIGHT
            ),
            line,statusCol
        );

        int stimeCol = taskTable.getColumn(
            SanBootView.res.getString("MonitorDialog.table.task.stime")
        ).getModelIndex();
        taskTable.setValueAt(
            new GeneralBrowserTableCell(
                -1,task.getStarttimeStr(),JLabel.RIGHT
            ),
            line, stimeCol
        );

        int pidCol = taskTable.getColumn(
            SanBootView.res.getString("MonitorDialog.table.task.pid")
        ).getModelIndex();
        taskTable.setValueAt(
            new GeneralBrowserTableCell(
                -1,task.getPid()+"",JLabel.RIGHT
            ),
            line,pidCol
        );
    }

    private void setupTaskTable(){
        Object[] title = new Object[]{
            SanBootView.res.getString("MonitorDialog.table.task.id"),
            SanBootView.res.getString("MonitorDialog.table.task.client"),
            SanBootView.res.getString("MonitorDialog.table.task.type"),
            SanBootView.res.getString("MonitorDialog.table.task.prof"),        
            SanBootView.res.getString("MonitorDialog.table.task.msg"),
            SanBootView.res.getString("MonitorDialog.table.task.status"),
            SanBootView.res.getString("MonitorDialog.table.task.stime"),
            SanBootView.res.getString("MonitorDialog.table.task.pid")
        };
        taskTable.setupTitle( title );

        // 由TaskListGeter来获取第一次的task list
        
        int[][] widthList = new int[][]{
            {0,55}, {1,70},{2,75},{3,90},{4,195},
            {5,70}, {6,130},{7,60}
        };
        taskTable.setupTableColumnWidth( widthList );
        taskTable.getTableHeader().setBorder( BorderFactory.createRaisedBevelBorder() );
        taskTable.getTableHeader().setReorderingAllowed( false );
    }

    private void inserOneLine(BakTask task,int line){
        BackupClient bkClnt;
        
        Object[] one = new Object[8];
        
        one[0] = task;
        
        String taskType = task.getTaskType().toUpperCase();
        if( taskType.startsWith( BakTask.TASK_TYPE_BKDATA ) ||
            taskType.startsWith( BakTask.TASK_TYPE_DELOLD ) ||
            taskType.startsWith( BakTask.TASK_TYPE_CLEAN )
        ){
            one[1] = new GeneralBrowserTableCell(
                -1,SanBootView.res.getString("common.server"),JLabel.LEFT
            );
        }else{
            try{
                int cliId = Integer.parseInt( task.getClientName() );
                bkClnt = view.initor.mdb.getClientFromVector( cliId );
                if( bkClnt != null ){
                    one[1] = new GeneralBrowserTableCell(
                        -1,bkClnt.getHostName(),JLabel.LEFT
                    ); 
                }else{
                    one[1] = new GeneralBrowserTableCell(
                        -1,SanBootView.res.getString("common.unknown"),JLabel.LEFT
                    ); 
                }
            }catch(Exception ex){
                one[1] = new GeneralBrowserTableCell(
                    -1,task.getClientName(),JLabel.LEFT
                );
            }
        }
        
        one[2] = new GeneralBrowserTableCell(
          -1,getTaskType( task.getTaskType() ),JLabel.LEFT
        ); 
        
        one[3] = new GeneralBrowserTableCell(
          -1,task.getProfileName(),JLabel.LEFT
        ); 
        
        one[4] = new GeneralBrowserTableCell(
          -1,task.getMsg(),JLabel.LEFT
        );

        one[5] = new GeneralBrowserTableCell(
          -1,task.getStatus(),JLabel.RIGHT
        );

        one[6] = new GeneralBrowserTableCell(
            -1,task.getStarttimeStr(),JLabel.RIGHT
        );

        one[7] = new GeneralBrowserTableCell(
          -1,task.getPid()+"",JLabel.RIGHT
        );
        
        if( line>=0 ){
            DefaultTableModel model = (DefaultTableModel)taskTable.getModel();
            model.insertRow( line ,one );
        }else{
            taskTable.insertRow( one );
        }
    }

    public static String getTaskType( String type ){
        if( type.toUpperCase().equals("BACKUP-FILE") ){
            return SanBootView.res.getString("common.cmd.dup");
        }else if(type.equals("RESTORE-FILE") ){
            return SanBootView.res.getString("common.cmd.anti-dup");
        }else{
            return type;
        }
    }
    private void setupLanguage(){
        setTitle( SanBootView.res.getString("MonitorDialog.diagTitle.monitor"));
        closeButton.setText( SanBootView.res.getString("MonitorDialog.button.close"));
        killJobButton.setText(SanBootView.res.getString("MonitorDialog.button.killJob"));
        activeJobButton.setText( SanBootView.res.getString("MonitorDialog.button.activeJob"));
        titledBorder1.setTitle( SanBootView.res.getString("MonitorDialog.border.message"));
    }

    void closeButton_actionPerformed(ActionEvent e) {
        if( receiver != null ){
            receiver.removePropertyChangeListener( (PropertyChangeListener)this );
            // 在退出之前,将receiver给阻塞掉
            receiver.requestSuspend();
        }
        
        killJob.clear();

        this.dispose();
    }
  
    public BrowserTable getTaskTable(){
        return taskTable;
    }
  
    public void enableButton( boolean val ){
        closeButton.setEnabled( val );
        killJobButton.setEnabled( val );
        activeJobButton.setEnabled( val );
        this.setPermOnUser();

        if( !val)
            setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        else
            setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );

        // 在第一次获取了task list之后,才注册监听器
        // 这一点非常重要
        receiver = view.initor.mdb.getMonitor(); 
        if( receiver != null ){
            receiver.addPropertyChangeListener( this );
            // 唤醒receiver, 使之继续工作
            receiver.requestResume();
        }
    }
  
    void activeJobButton_actionPerformed(ActionEvent e){
        String cliname = null;
        
        int selLine = taskTable.getSelectedRow();
        if( selLine < 0 ) return;

        DefaultTableModel model = (DefaultTableModel)taskTable.getModel();
        int rowCnt = model.getRowCount();
        if( selLine>=0 && selLine< rowCnt ){ 
            int taskIdCol = taskTable.getColumn(
                SanBootView.res.getString("MonitorDialog.table.task.id")
            ).getModelIndex();
            
            BakTask one = (BakTask)taskTable.getValueAt(selLine, taskIdCol);
            String status = one.getStatus();        
            if( !status.equals("QUEUED") ){
                return;
            }
            
            if( !view.initor.mdb.viewFileContents( ResourceCenter.PID_PATH ) ){ 
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("MonitorDialog.errMsg.getTaskerPid")+" : "+ 
                        view.initor.mdb.getErrorMessage()
                    );
                return;
            }
            
            StringBuffer strBuf = view.initor.mdb.getContentBuf();
 //System.out.println(" pid: "+strBuf.toString().trim() );
            int pid = -1;
            try{
                pid = Integer.parseInt( strBuf.toString().trim() );
            }catch(Exception ex){
                ex.printStackTrace();
            }
            if( pid <= 0 ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("MonitorDialog.errMsg.badTaskerPid")+" : "+ pid
                );
                return;
            }

            if( !view.initor.mdb.activeTask( pid ) ){
                JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("MonitorDialog.errMsg.activeJobFailed")+" : "+
                    view.initor.mdb.getErrorMessage()
                );
            }else{
                /*
                BakTask task = new BakTask(
                    one.getID(), 
                    one.getPid(),
                    one.getTaskType(), 
                    one.getBakSet(),
                    one.getStarttime(),
                    "ABORT",
                    "Job Stopped", 
                    one.getLogFile(),
                    one.getSchID(),
                    one.getProfID(),
                    cliname 
                );
                killJob.addElement( task );
                 */
            }
        }
    }
    
    void killJobButton_actionPerformed(ActionEvent e) {
        String cliname = null;
        BackupClient bkClnt;
        
        int selLine = taskTable.getSelectedRow();
        if( selLine < 0 ) return;
        
        DefaultTableModel model = (DefaultTableModel)taskTable.getModel();
        int rowCnt = model.getRowCount();
        if( selLine >= 0 && selLine< rowCnt ){ 
            int taskIdCol = taskTable.getColumn(
                SanBootView.res.getString("MonitorDialog.table.task.id")
            ).getModelIndex();
            
            BakTask one = (BakTask)taskTable.getValueAt(selLine, taskIdCol);
//System.out.println("selected Baktask type: "+one.getTaskType());
//System.out.println("selected Baktask id: "+one.getID() );
//System.out.println("selected Baktask baksetid: "+one.getBakSet() );
            
            String status = one.getStatus();        
            if( status.equals("ABORT") || status.equals("END") || status.equals("FAILED") ){
                return;
            }
            
            String taskType = one.getTaskType().toUpperCase();
            if( taskType.startsWith( BakTask.TASK_TYPE_BKDATA ) ||
                taskType.startsWith( BakTask.TASK_TYPE_DELOLD ) ||
                taskType.startsWith( BakTask.TASK_TYPE_CLEAN )
            ){
                return;
            }
            
            String cliId = "-1";
            try{
                long id = Long.parseLong( one.getClientName() );
                bkClnt = view.initor.mdb.getClientFromVector( id );
                if( bkClnt != null ){
                    cliname = bkClnt.getHostName();
                }else{
                    cliname = id+"";
                }
                cliId = id+"";
            }catch(Exception ex){
                bkClnt = view.initor.mdb.getClientFromVectorOnName( one.getClientName() ); 
                cliname = one.getClientName();
                if( bkClnt != null ){
                    cliId = bkClnt.getID()+"";
                }
            }
            
            if( JOptionPane.showConfirmDialog(
                this,
                SanBootView.res.getString("MonitorDialog.confirm"),
                SanBootView.res.getString("common.confirm"),  //"Confirm",
                JOptionPane.OK_CANCEL_OPTION
            ) == JOptionPane.CANCEL_OPTION ){
                return;
            }

            if( !view.initor.mdb.killTask( cliId, one.getID() ) ){
                 JOptionPane.showMessageDialog(this,
                    SanBootView.res.getString("MonitorDialog.errMsg.killJobFailed")+" : "+
                    view.initor.mdb.getErrorMessage()
                );
            }else{
                if( taskType.startsWith("backup-") ){
                    long bkstId = one.getBakSet();
                    if( bkstId != -1 ){
                        // 将bkst的状态改为ABORT
                        view.initor.mdb.modBakSet( bkstId ); 
                    }
                }

                BakTask task = new BakTask(
                    one.getID(), 
                    one.getPid(),
                    one.getTaskType(), 
                    one.getBakSet(),
                    one.getStarttime(),
                    "ABORT",
                    "Job Stopped", 
                    one.getLogFile(),
                    one.getSchID(),
                    one.getProfID(),
                    cliname,
                    one.getProfileName()
                );
                killJob.add( task );
            }
        }
    }
}
