/*
 * GetDataDupProcess.java
 *
 * Created on 2008/8/27,��AM 11:23
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.datadup.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import guisanboot.datadup.data.BakTask;
import guisanboot.datadup.data.UniProfile;
import guisanboot.ui.RunningTask;
import guisanboot.ui.SanBootView;
import javax.swing.SwingUtilities;
import mylib.tool.Check;

/**
 *
 * @author Administrator
 */
public class GetDataDupProcess extends Thread {
    SanBootView view;
    RunningTask pane;
    String prefix = ""; // 从外面带进来的显示信息,要显示的信息为( prefix + new msg )
    String taskTitle="";
    String totoalSize="";
    ArrayList goodProfList;
    boolean hasCommitFail = false;
    int row;
    int act = 0; // 0: data dup  1: data restore
    
    ArrayList<BakTask> taskList;
    ArrayList<BakTask> taskHisList = new ArrayList<BakTask>();
    
    StringBuffer msgBuf;
    HashMap<Long,Long> active_task_map = new HashMap<Long,Long>();
    HashMap<Long,BakTask> finish_task_map = new HashMap<Long,BakTask>();
    HashMap<Long,BakTask> task_his_map = new HashMap<Long,BakTask>(); // 某些任务提交时就失败，所以只能在task his中找到
    
    boolean isFirst = true;
    boolean isFinished = false; // whether data-duplication is finished? 
    boolean lastResult = true;  // 表示所有步骤是否成功完成
    boolean toEnd = false;      // 表示所有步骤全部完成与否
    
    Runnable setVal = new Runnable(){
        public void run(){
            String log = "";
            if( msgBuf != null ){
                log = msgBuf.toString();
            }
            
            if( !log.equals("") || !prefix.equals("") ){
                if( found_num != goodProfList.size() ){
                    pane.freshCopyLogOnTabpane( prefix, log,row,true ); 
                }else{
                    if( isFirst ){
                        pane.freshCopyLogOnTabpane( prefix, log,row,true ); 
                        isFirst = false;
                    }else{
                        pane.freshCopyLogOnTabpane( prefix, log,row,false ); 
                    } 
                }
            }
        }
    };
    
    /**
     * Creates a new instance of GetDataDupProcess 
     */
    // 给复制使用的
    public GetDataDupProcess( 
        SanBootView _view,
        RunningTask _pane,
        ArrayList _goodProfList,
        boolean _hasCommitFail,
        String _prefix,
        int row
    ){
        view = _view;
        pane = _pane;
        goodProfList = _goodProfList;
        hasCommitFail = _hasCommitFail;
        prefix = _prefix;
        this.row = row;
        act = 0;
    }
    
    // 给恢复使用的
    public GetDataDupProcess( 
        SanBootView _view,
        RunningTask _pane,
        ArrayList _goodProfList,
        String _taskTitle,
        boolean _hasCommitFail,    
        int row
    ){
        view = _view;
        pane = _pane;
        goodProfList = _goodProfList;
        taskTitle = _taskTitle;
        hasCommitFail = _hasCommitFail;
        this.row = row;
        act = 1;
    }
    
    public GetDataDupProcess( 
        SanBootView view,
        RunningTask pane,
        ArrayList goodProfList,
        String taskTitle,
        int row
    ){
        this( view,pane,goodProfList,taskTitle,false,row );
    }
    
    int found_num=0;
    private void parseTask( ){
        int i,size;
        String temp_totalSize;
        long elapse_time;
        UniProfile prof;
        BakTask task;
        StringBuffer endTask;
        
        found_num = 0;
        size = goodProfList.size();
        msgBuf = new StringBuffer();
        isFirst = true;
        for( i=0; i<size; i++ ){
            prof = (UniProfile)goodProfList.get(i);
            task = parseProfie( taskList,prof,0 );
            if( task == null ){
                // 在task history中查找
                task = parseProfie( taskHisList,prof,1 );
            }
            
            if( task != null ){
                temp_totalSize = this.parseTransferSize( task.getMsg() );
                if( !temp_totalSize.equals("") ){
                    this.totoalSize = temp_totalSize;
                }

                found_num +=1;
SanBootView.log.debug( getClass().getName()," #### DISPLAY #### " + prof.toString() +" task: "+task.prtMe() );
                if( task.isFinished() ){
                    if( finish_task_map.get( new Long( task.getID() ) ) == null ){
                        finish_task_map.put( new Long( task.getID() ),task );
                        
                        // 结束的任务单独用endTask存放（最终放在prefix中），不混在msgBuf中
                        endTask = new StringBuffer();
                        
                        if( act == 0 ){
                            endTask.append("[  ");
                            endTask.append( prof.toString() );
                            endTask.append("  ]");
                            endTask.append( " :  " );
                        }else{
                            endTask.append("[  ");
                            endTask.append( taskTitle );
                            endTask.append("  ]");
                            endTask.append( " :\n  " );
                        }
                        if( this.totoalSize.equals("") ){
                            endTask.append( task.getMsg() );
                        }else{
                            endTask.append( task.getMsg() +" [ " +this.totoalSize +" ]" );
                        }
                        endTask.append( "   Status: " );
                        endTask.append( task.getStatus() );
                        elapse_time = getElapsedTime( task );
                        endTask.append( "   Elapsed Time: ");
                        endTask.append( Check.getFormatTime( elapse_time ) );
                        
                        if( finish_task_map.size() < size ){
                            endTask.append("\n");
                        }
SanBootView.log.info( getClass().getName(), "finished task info: \n" + endTask.toString() );                
                        prefix += endTask.toString();
                    }
                }else{
                    if( task.isActiveStatus() || task.isStatusEnd() ){
                        // status为END并不是真正结束，一定要等到msg中出现"mirror job end"才算结束了
                        elapse_time = getElapsedTime( task );
                    }else{
                        elapse_time = 0;
                    }
                    
                    if( isFirst ){
                        isFirst = false;
                    }else{
                        msgBuf.append("\n");
                    }
                    
                    if( act == 0 ){
                        msgBuf.append("[  ");
                        msgBuf.append( prof.toString() );
                        msgBuf.append("  ]");
                        msgBuf.append( " :  " );
                    }else{
                        msgBuf.append("[  ");
                        msgBuf.append( taskTitle );
                        msgBuf.append("  ]");
                        msgBuf.append( " : \n " );
                    }
                    msgBuf.append( task.getMsg() );
                    msgBuf.append( "   Status: " );
                    msgBuf.append( task.getStatus() );
                    msgBuf.append( "   Elapsed Time: ");
                    msgBuf.append( Check.getFormatTime( elapse_time ) );
                }
            }
        }
        
SanBootView.log.info( getClass().getName()," ####### finished task num: "+finish_task_map.size() +" [ monitored task total:  " + size + "  ]" );        
        if( finish_task_map.size() == size ){
            isFinished = true;
            
            msgBuf = new StringBuffer();
            // 全部结束后,最后再显示一下copy info
            try{
                SwingUtilities.invokeAndWait( setVal );
            } catch( Exception e){
                e.printStackTrace();
            }
        }
    }

    private String parseTransferSize( String msg ){
        int index = msg.indexOf("transfer data size:");
        if( index >=0 ){
            index = msg.lastIndexOf(":");
            if( index >=0 ){
                return msg.substring( index + 1 );
            }else{
                return "";
            }
        }else{
            return "";
        }
    }

    private long getElapsedTime( BakTask task ){
        long elapse_time;
        
        Object val = active_task_map.get( new Long( task.getID() ) ); 
        if( val == null ){
            active_task_map.put( new Long( task.getID() ),new Long( System.currentTimeMillis()/1000 ) );
            elapse_time = 0;
        }else{
            Long start_time = (Long)val;
            elapse_time = System.currentTimeMillis()/1000 - start_time.longValue();
            if( elapse_time < 0 ){
                elapse_time = 0;
            }
        }
        return elapse_time;
    }
    
    private BakTask parseProfie( ArrayList<BakTask> taskList,UniProfile prof,int mode ){
        BakTask task;
        String pf,str;

        str = ( mode == 1 )?" from taskhis table.":" from task table.";

SanBootView.log.debug( getClass().getName(), "########## checked prof: "+ prof.toString() );
        int size = taskList.size();
        for( int i=0; i<size; i++ ){
            task = taskList.get(i);
            pf = task.getProfileName();
SanBootView.log.debug( getClass().getName(), "$$$$$$$$$$ prof in task: "+ pf );
            if( pf.equals( prof.toString() ) ){
                return task;
            }
        }
        
SanBootView.log.warning(getClass().getName(),"@@@ Can Not find matched task for profile: "+prof.toString() + str );
        
        return null;
    }
    
    @Override public void run(){
        boolean isok;
        
        while( !isFinished ){ // 判断是否结束
            isok = view.initor.mdb.updateTaskList();
            if( isok ){
                updateTaskHis();
                taskList = view.initor.mdb.getAllTaskList( 1 );
                
                parseTask();
                try{
                    SwingUtilities.invokeAndWait( setVal );
                } catch( Exception e){
                    e.printStackTrace();
                }
            }
            
            // sleep for 2 sec.
            try{
                Thread.sleep( 2*1000 );
            } catch( Exception e){
                e.printStackTrace();
            }
        }
        
        if( hasCommitFail ){
            lastResult = false;
        }else{
            Set set = finish_task_map.keySet();
            Iterator<Long> list = set.iterator();
            while( list.hasNext() ){
                BakTask task = (BakTask)finish_task_map.get( list.next() );
                if( task.isFailedStatus() || task.isAbortStatus() ){
                    lastResult = false;
                    break;
                }
            }
        }
        
        toEnd = true;
    }
    
    private void updateTaskHis(){
        UniProfile prof;
        boolean isOk;
        BakTask task;
        
        taskHisList.clear();
        
        int size = goodProfList.size();
        for( int i=0; i<size; i++ ){    
            prof = (UniProfile)goodProfList.get(i); 
            
            isOk = view.initor.mdb.getOneBakTask( prof.toString() );
            if( isOk ){
                task = view.initor.mdb.getQueryBakTask();
                if( task != null ){
                    taskHisList.add( task );
                }else{
SanBootView.log.warning( getClass().getName(),"can't find task in taskhis: "+ prof.toString()+". Maybe the task isn't yet finished " );                   
                }
            }
        }
    }
    
    public boolean getLastResult(){
        return lastResult;
    }
    
    public boolean isToEnd(){
        return toEnd;
    }
}
