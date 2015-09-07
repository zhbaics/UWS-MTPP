/*
 * GetTaskLog.java
 *
 * Created on Aug 11, 2008, 12:00 AM
 */

package guisanboot.datadup.cmd;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import javax.swing.*;
import javax.swing.table.*;
import java.io.IOException;
import mylib.UI.*;
import guisanboot.datadup.data.*;
import guisanboot.ui.*;
import guisanboot.res.*;


/**
 *
 * @author  Administrator
 */
public class GetTaskLog extends NetworkRunning{
    private BakTask curTaskLog;
    private DefaultTableModel model;
    private SanBootView view;
    private int count;
        
    /** Creates a new instance of GetTaskLog */
    public GetTaskLog( SanBootView _view ,String cmd ) throws IOException {
        super( cmd,_view.getSocket() );
        view = _view;
    }
    
    public GetTaskLog( String cmd ) {
        super( cmd );
    }
    
    public void parser(String line){
        String s1 = line.trim();
//System.out.println("#####(GetTaskLog): "+s1);

        //indexOf(String str)  返回第一次出现的指定子字符串在此字符串中的索引。
        int index = s1.indexOf("=");
        
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();

            //startsWith(String prefix)  测试此字符串是否以指定的前缀开始
            if( s1.startsWith( BakTask.BAKTASKID) ){
                try{
                    int id = Integer.parseInt( value );
                    curTaskLog.setID( id );
                }catch(Exception ex){
                    curTaskLog.setID( -1 );
                }
            }else if( s1.startsWith( BakTask.BAKTASKPID )){
                // do nothing
            }else if( s1.startsWith( BakTask.BAKTASKTYPE)){
                curTaskLog.setTaskType( value );
            }else if( s1.startsWith( BakTask.BAKTASKBAKSET )){
                try{
                    long baksetid = Long.parseLong( value );
                    curTaskLog.setBakSet( baksetid );
                }catch(Exception ex){
                    curTaskLog.setBakSet( -1L );
                }
            }else if(s1.startsWith( BakTask.BAKTASKSTIME )){
                curTaskLog.setStarttime( value );
            }else if(s1.startsWith( BakTask.BAKTASKSTATUS )){
                curTaskLog.setStatus( value );
            }else if(s1.startsWith( BakTask.BAKTASKMSG )){
                curTaskLog.setMsg( value );
            }else if(s1.startsWith( BakTask.BAKTASKLOGFILE)){
            }else if(s1.startsWith( BakTask.BAKTASKSCHID )){
            }else if(s1.startsWith( BakTask.BAKTASKPROFID )){
            }else if(s1.startsWith( BakTask.BAKTASKCLINAME ) ){
                curTaskLog.setClientName( value );
            }else if(s1.startsWith( BakTask.BAKTASKPROFILE ) ){
                curTaskLog.setProfileName( value );
                System.out.println("curTaskLog:------" + curTaskLog.getProfileName());
                
                try{
                    SwingUtilities.invokeAndWait( insertModel );
                }catch(Exception ex){
                }
                count++;
            }
        }else{
            if( s1.startsWith( BakTask.BAKTASKRECFLAG )){
                curTaskLog = new BakTask();
            }
        }
    }
    
    public void setTableModel( DefaultTableModel _model ){
        model = _model;
    }
    
    Runnable insertModel = new Runnable(){
        public void run(){
            BackupClient cli;
            
            if( model == null ) return;
            
            Object[] one = new Object[6];
            
            one[0] = curTaskLog;
            
            one[1] = new GeneralBrowserTableCell(
                -1,curTaskLog.getTaskTypeStr(),JLabel.LEFT 
            ); 
            
            String taskType = curTaskLog.getTaskType().toUpperCase();
            if( !taskType.startsWith( BakTask.TASK_TYPE_BKDATA ) &&
                !taskType.startsWith( BakTask.TASK_TYPE_DELOLD ) &&
                !taskType.startsWith( BakTask.TASK_TYPE_CLEAN )
            ){
                try{
                    int cliId = Integer.parseInt( curTaskLog.getClientName() );
//                    System.out.println("cliId:-------" + cliId);
                    cli = view.initor.mdb.getClientFromVector( cliId );
//                    System.out.println("cli:-------" + cli);
                    if( cli!=null ){
                        one[2] = new GeneralBrowserTableCell(
                            -1,cli.getHostName() +" [ "+cli.getIP() +" ]",JLabel.LEFT
                        ); 
                    }else{
                        //common.unknown 未知
                        //BackupHistoryDialog.miss 可能已被删除
                        one[2] = new GeneralBrowserTableCell(
                            -1, SanBootView.res.getString("common.unknown")+" "+
                                SanBootView.res.getString("BackupHistoryDialog.miss"),
                            JLabel.LEFT
                        ); 
                    }
                }catch(Exception ex){
                    one[2] = new GeneralBrowserTableCell(
                        -1,curTaskLog.getClientName(),JLabel.LEFT
                    );
                }
            }else{
                one[2] = new GeneralBrowserTableCell(
                    -1,SanBootView.res.getString("common.server"),JLabel.LEFT
                );
            }
            
            one[3] = new GeneralBrowserTableCell(
                -1,curTaskLog.getProfileName(),JLabel.LEFT
            );
            System.out.println("curTaskLog:------" + curTaskLog.getProfileName());
            
            one[4] = new GeneralBrowserTableCell(
                -1,curTaskLog.getStatus(),JLabel.LEFT
            );
            
            one[5] = new GeneralBrowserTableCell(
                -1,curTaskLog.getStarttimeStr(),JLabel.LEFT
            );
            model.addRow( one );
        }
    };
     
    // 调用一次就只获取某个bakset的备份包
    public boolean updateTaskLog( int begin,int num ){
        try{
            curTaskLog = null;
            count = 0;
            setCmdLine(
                ResourceCenter.getCmd( ResourceCenter.CMD_GET_TSKLOG )+
                begin+" "+num
            );

SanBootView.log.info(getClass().getName(), " get tasklog cmd: " + getCmdLine() );

            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info(getClass().getName(), " get tasklog cmd retcode: " + getRetCode() );        
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error(getClass().getName(), " get tasklog cmd errmsg: " + getErrMsg() );            
        }
        return isOk;
    }
    
    public boolean updateTaskLog( String profName ){
        try{
            curTaskLog = null;
            setCmdLine(
                ResourceCenter.getCmd( ResourceCenter.CMD_QUERY_TSKLOG ) + profName
            );
            
SanBootView.log.info(getClass().getName(), " get one tasklog cmd: " + getCmdLine() );
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info(getClass().getName(), " get one tasklog cmd retcode: " + getRetCode() );        
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error(getClass().getName(), " get one tasklog cmd errmsg: " + getErrMsg() );            
        }
        return isOk;
    }
    
    public BakTask getQueryTask(){
        return curTaskLog;
    }
    
    public int getCount(){
        return count;
    }
}
