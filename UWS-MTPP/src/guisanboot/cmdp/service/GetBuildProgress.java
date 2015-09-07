/*
 * GetBuildProgress.java
 *
 * Created on 2010/7/8,��PM 17:31
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.cmdp.service;

import guisanboot.cmdp.entity.InitProgressRecord;
import guisanboot.data.BindOfSrcAndDest;
import java.util.ArrayList;
import java.util.HashMap;
import guisanboot.ui.RunningTask;
import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.util.Vector;
import javax.swing.SwingUtilities;

/**
 *
 * @author Administrator
 */
public class GetBuildProgress extends Thread {
    SanBootView view;
    RunningTask pane;
    String prefix = ""; // 从外面带进来的显示信息,要显示的信息为( prefix + new msg )
    Vector rstMapList;
    String ip;
    int port;
    int row;
    int act = 0; // 0: data dup  1: data restore

    ArrayList<InitProgressRecord> recList = new ArrayList<InitProgressRecord>();
    ArrayList<InitProgressRecord> save_recList = new ArrayList<InitProgressRecord>();

    private GetInitProgress geter;
    StringBuffer msgBuf;
    HashMap<String,InitProgressRecord> finish_task_map = new HashMap<String,InitProgressRecord>();
    
    boolean isFirst = true;
    boolean isFinished = false; // whether data-duplication is finished? 
    boolean lastResult = false;  // 表示所有步骤是否成功完成
    boolean toEnd = false;      // 表示所有步骤全部完成与否
    boolean hasFailedTask = false;  // 是否有失败的任务
    
    Runnable setVal = new Runnable(){
        public void run(){
            String log = "";
            if( msgBuf != null ){
                log = msgBuf.toString();
            }
            if( log.equals("") && prefix.equals("") ) return;

            if( isFirst ){
                pane.freshCopyLogOnTabpane( prefix, log,row,true );
                isFirst = false;
            }else{
                pane.freshCopyLogOnTabpane( prefix, log,row,false );
            }
        }
    };
    
    /**
     * Creates a new instance of GetBuildProgress
     */
    // 给复制使用的
    public GetBuildProgress( 
        SanBootView _view,
        RunningTask _pane,
        Vector rstMapList,
        String _prefix,
        int row
    ){
        view = _view;
        pane = _pane;
        this.rstMapList = rstMapList;
        prefix = _prefix;
        this.row = row;
        act = 0;
    }
    
    // 给恢复使用的
    public GetBuildProgress( 
        SanBootView _view,
        RunningTask _pane,
        Vector rstMapList,
        String ip,
        int port,
        int row
    ){
        view = _view;
        pane = _pane;
        this.rstMapList = rstMapList;
        this.ip = ip;
        this.port = port;
        this.row = row;
        act = 1;

        geter = new GetInitProgress("");
    }
    
    private void parseInitProgressRecord( ){
        int i,size;
        InitProgressRecord record,olderRec;
        StringBuffer endTask;
        String simple_src,simple_dest,title;

        size = this.rstMapList.size();
        msgBuf = new StringBuffer();
        isFirst = true;
        for( i=0; i<size; i++ ){
            BindOfSrcAndDest binder = (BindOfSrcAndDest)rstMapList.get(i);
            simple_src = binder.src.substring(0,1).toUpperCase();
            simple_dest = binder.dest.substring(0,1).toUpperCase();
            if( simple_src.toUpperCase().equals("I") || simple_dest.toUpperCase().equals("I") ){
                title = simple_src + "--->" + simple_dest;
            }else{
                title = simple_src + "->"+simple_dest;
            }

            record = parseRecord( this.recList,binder.getRealSrc() );
            if( record == null ) continue;

            olderRec = this.getRecFromOlderRecList( record.getDisk() );

            // notConnectError/sync/unknown这三种状态都算是恢复结束
            if( record.isNotConnectToClient() || record.isSyncState() || record.isUnknownState()  ){
                if( finish_task_map.get( binder.src ) == null ){
                    finish_task_map.put( binder.src,record );

                    // 结束的任务单独用endTask存放（最终放在prefix中），不混在msgBuf中
                    endTask = new StringBuffer();

                    if( act == 0 ){
                        endTask.append("[");
                        endTask.append( title );
                        endTask.append("]");
                        endTask.append( " :  " );
                    }else{
                        endTask.append("[");
                        endTask.append( title );
                        endTask.append("]: ");
                    }

                    if( record.isNotConnectToClient() ){
                        endTask.append( "  Percent: " + SanBootView.res.getString("common.conError") );
                        hasFailedTask = true;
                    }else if( record.isUnknownState() ){
                        endTask.append( "  Percent: " + SanBootView.res.getString("common.rstError") );
                        hasFailedTask = true;
                    }else{
                        endTask.append( "  Percent: 100% " );
                    }

                    endTask.append("   Speed: " + ( ( olderRec!=null )? olderRec.getSpeed():"0.0MB/s") );
                    endTask.append("   Left Time: 0 ");
                    String finished = ( ( olderRec!=null )? olderRec.getFinished():"0.0M");
                    try{
                        float ff = Float.parseFloat( finished.substring( 0,finished.length()-1 ) );
                        if( ff == 0 ){
                            endTask.append("   Elapsed Time: 0" );
                        }else{
                            endTask.append("   Elapsed Time: " +  ( ( olderRec!=null )? olderRec.getElapsedTime():"0") );
                        }
                    }catch(Exception ex){
                        endTask.append("   Elapsed Time: " +  ( ( olderRec!=null )? olderRec.getElapsedTime():"0") );
                    }
                    endTask.append("   Finished: " + finished  );

                    if( finish_task_map.size() < size ){
                        endTask.append("\n");
                    }
SanBootView.log.info( getClass().getName(), "finished task info: \n" + endTask.toString() );
                    prefix += endTask.toString();
                }
            }else{
                if( isFirst ){
                    isFirst = false;
                }else{
                    msgBuf.append("\n");
                }

                if( act == 0 ){
                    msgBuf.append("[");
                    msgBuf.append( title );
                    msgBuf.append("]");
                    msgBuf.append( " :  " );
                }else{
                    msgBuf.append("[");
                    msgBuf.append( title );
                    msgBuf.append("]: ");
                }
                
                if( record.getPercent().equals("") ){
                    if( olderRec == null || olderRec.getPercent().equals("") ){
                        msgBuf.append("   Percent: 0.0%" );
                    }else{
                        msgBuf.append("   Percent: " + olderRec.getPercent() );
                    }
                }else{
                    msgBuf.append("   Percent: " + record.getPercent() );
                }
                if( record.getSpeed().equals("") ){
                    if( olderRec == null || olderRec.getPercent().equals("") ){
                        msgBuf.append("   Speed: " );
                    }else{
                        msgBuf.append("   Speed: " + olderRec.getSpeed() );
                    }
                }else{
                    msgBuf.append("   Speed: " + record.getSpeed() );
                }
                if( record.getRemainTime().equals("") ){
                    if( olderRec == null || olderRec.getPercent().equals("") ){
                        msgBuf.append("   Left Time: " );
                    }else{
                        msgBuf.append("   Left Time: " + olderRec.getRemainTime() );
                    }
                }else{
                    msgBuf.append("   Left Time: " + record.getRemainTime());
                }
                if( record.getElapsedTime().equals("") ){
                    if( olderRec == null || olderRec.getPercent().equals("") ){
                        msgBuf.append("   Elapsed Time: " );
                    }else{
                        msgBuf.append("   Elapsed Time: " + olderRec.getElapsedTime() );
                    }
                }else{
                    msgBuf.append("   Elapsed Time: " + record.getElapsedTime() );
                }
                if( record.getFinished().equals("") ){
                    if( olderRec == null || olderRec.getPercent().equals("") ){
                        msgBuf.append("   Finished: " );
                    }else{
                        msgBuf.append("   Finished: " + olderRec.getFinished() );
                    }
                }else{
                    msgBuf.append("   Finished: " + record.getFinished() );
                }
            }
        }

SanBootView.log.info( getClass().getName()," ####### finished task num: "+finish_task_map.size() +" [ monitored task total:  " + size + "  ]" );
        if( finish_task_map.size() == size ){
            isFinished = true;
            if( !this.hasFailedTask ){
                this.lastResult = true;
            }

            msgBuf = new StringBuffer();
            // 全部结束后,最后再显示一下copy info
            try{
                SwingUtilities.invokeAndWait( setVal );
            } catch( Exception e){
                e.printStackTrace();
            }
        }
    }
    
    private InitProgressRecord parseRecord( ArrayList<InitProgressRecord> recList,String srcLetter ){
        int size = recList.size();
        for( int i=0; i<size; i++ ){
            InitProgressRecord record = recList.get( i );
            if( record.getDisk().substring(0,1).toUpperCase().equals( srcLetter.substring(0,1).toUpperCase() ) ){
                return record;
            }
        }
SanBootView.log.warning(getClass().getName(),"@@@ Can Not find matched InitProgressRecord for disk: "+ srcLetter );
        return null;
    }

    @Override public void run(){
        boolean isok;
        
        while( !isFinished ){ // 判断是否结束
            try{
                getInitProgressInfo();
                isok = true;
            }catch( Exception ex ){
                isok = false;
            }
            
            if( isok ){
                this.parseInitProgressRecord();
                try{
                    SwingUtilities.invokeAndWait( setVal );
                } catch( Exception e){
                    e.printStackTrace();
                }
            }

            // sleep for 1.5 sec.
            try{
                Thread.sleep( 1500 );
            } catch( Exception e){
                e.printStackTrace();
            }
        }
         
        toEnd = true;
    }

    private InitProgressRecord getRecFromOlderRecList( String diskLetter ){
        int size = this.save_recList.size();
        for( int i=0; i<size; i++ ){
            InitProgressRecord curRec = this.save_recList.get(i);
            if( curRec.getDisk().substring(0,1).toUpperCase().equals( diskLetter.substring(0,1).toUpperCase() ) ){
                return curRec;
            }
        }
        return null;
    }

    private void saveOlderRecord(){
        int size = this.recList.size();
        for( int i=0; i<size; i++ ){
            InitProgressRecord curRec = this.recList.get(i);
            InitProgressRecord olderRec = this.getRecFromOlderRecList( curRec.getDisk() );
            if( olderRec == null ){
                this.save_recList.add( curRec );
            }else{
                if( !curRec.getFinished().equals("") ){
                    olderRec.setElapsedTime( curRec.getElapsedTime() );
                    olderRec.setFinished( curRec.getFinished() );
                    olderRec.setPercent( curRec.getPercent() );
                    olderRec.setRemainTime( curRec.getRemainTime() );
                    olderRec.setSpeed( curRec.getSpeed() );
                }
            }
        }
    }

    private void getInitProgressInfo() throws IOException{
        InitProgressRecord curRec;

        this.geter.setSocket( view.getSocket() );
        this.saveOlderRecord();
        this.recList.clear();

        int size = this.rstMapList.size();
        for( int i=0; i<size; i++ ){
            BindOfSrcAndDest binder = (BindOfSrcAndDest)rstMapList.elementAt( i );
            boolean isOk = geter.updateBuildProgress( ip,port,binder.srcUUID,binder.getRealSrc() );
            if( isOk ){
                curRec = geter.getInitRecord();
                curRec.setIsNotConnectError( geter.isNotConnectToHost() );
            }else{
                curRec = new InitProgressRecord( binder.getRealSrc() );
            }
            recList.add( curRec );
        }
    }

    public boolean getLastResult(){
        return lastResult;
    }
    
    public boolean isToEnd(){
        return toEnd;
    }
}
