/*
 * GetBakTaskIntoTableModel.java
 *
 * Created on Aug 11, 2008, 11:26 AM
 */

package guisanboot.datadup.cmd;

import guisanboot.data.SuspendNetworkRunning;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;

import mylib.UI.*;
import guisanboot.ui.*;
import guisanboot.datadup.data.*;
import guisanboot.datadup.ui.MonitorDialog;

/**
 *
 * @author  Administrator
 */
public class GetBakTaskIntoTableModel extends SuspendNetworkRunning{
    private  BakTask curTask  = null;
    private  SanBootView view;
    private  DefaultTableModel model;
    
    /** Creates a new instance of GetBakTaskIntoTableModel */
    public GetBakTaskIntoTableModel(SanBootView _view,String cmd) throws IOException {
        super( cmd, _view.getSocket(),true, 5 );
        view = _view;
    }
    
    public void parser(String line){
        String s1 = line.trim();
//System.out.println("#####(GetBakTask): "+s1);
        
        int index = s1.indexOf("=");
        if( index>0){
            String value = s1.substring( index+1 ).trim();
// System.out.println("@@@@@: "+value);
            
            if( s1.startsWith( BakTask.BAKTASKID) ){
                try{
                    int id = Integer.parseInt( value );
                    curTask.setID( id );
                }catch(Exception ex){
                    curTask.setID( -1 );
                }
            }else if( s1.startsWith( BakTask.BAKTASKPID )){
                try{
                    int pid = Integer.parseInt( value );
                    curTask.setPid( pid );
                }catch(Exception ex){
                    curTask.setPid( -1 );
                }
            }else if( s1.startsWith( BakTask.BAKTASKTYPE)){
                curTask.setTaskType( value );
            }else if( s1.startsWith( BakTask.BAKTASKBAKSET )){
//System.out.println("task bakset id: "+value);
                try{
                    long baksetid = Long.parseLong( value );
                    curTask.setBakSet( baksetid );
                }catch(Exception ex){
                    curTask.setBakSet( -1L );
                }
            }else if(s1.startsWith( BakTask.BAKTASKSTIME )){
                curTask.setStarttime( value );
            }else if(s1.startsWith( BakTask.BAKTASKSTATUS )){
                curTask.setStatus( value );
            }else if(s1.startsWith( BakTask.BAKTASKMSG )){
                curTask.setMsg( value );
            }else if(s1.startsWith( BakTask.BAKTASKLOGFILE)){
                curTask.setLogFile( value );
            }else if(s1.startsWith( BakTask.BAKTASKSCHID )){
                try{
                    long schid = Long.parseLong( value );
                    curTask.setSchID( schid );
                }catch(Exception ex){
                    curTask.setSchID( -1L );
                }
            }else if(s1.startsWith( BakTask.BAKTASKPROFID )){
                try{
                    long profid = Long.parseLong( value );
                    curTask.setProfID( profid );
                }catch(Exception ex){
                    curTask.setProfID( -1L );
                }
            }else if(s1.startsWith( BakTask.BAKTASKCLINAME ) ){
//System.out.println( "task client name: "+value );
                curTask.setClientName( value );
            }else if( s1.startsWith( BakTask.BAKTASKPROFILE ) ){
                curTask.setProfileName( value );
                try{
                    SwingUtilities.invokeAndWait( insertModel );
                }catch(Exception ex){}   
            }
        }else{
// System.out.println("begin..............");
            if( s1.startsWith( BakTask.BAKTASKRECFLAG )){
                curTask = new BakTask();
            }
        }
    }
    
    public void updateTaskList(){
        curTask = null;
        
        // 先将符合要求的按逆序显示
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
    }
    
    Runnable insertModel = new Runnable(){
        public void run(){
            Object[] one = new Object[8];
            
            one[0] = curTask;
            
            try{
                int cliId = Integer.parseInt( curTask.getClientName() );
                BackupClient cli = view.initor.mdb.getClientFromVector( cliId );
                if( cli!=null ){
                    one[1] = new GeneralBrowserTableCell(
                        -1,cli.getHostName(),JLabel.LEFT
                    ); 
                }else{
                    one[1] = new GeneralBrowserTableCell(
                        -1,SanBootView.res.getString("common.unknown"),JLabel.LEFT
                    ); 
                }
            }catch(Exception ex){
                one[1] = new GeneralBrowserTableCell(
                    -1,curTask.getClientName(),JLabel.LEFT
                );
            }
            
            one[2] = new GeneralBrowserTableCell(
                -1,MonitorDialog.getTaskType( curTask.getTaskType() ),JLabel.LEFT
            );

            one[3] = new GeneralBrowserTableCell(
                -1,curTask.getProfileName(),JLabel.LEFT
            );
            
            one[4] = new GeneralBrowserTableCell(
                -1,curTask.getMsg(),JLabel.LEFT
            );

            one[5] = new GeneralBrowserTableCell(
                -1,curTask.getStatus(),JLabel.RIGHT
            );

            one[6] = new GeneralBrowserTableCell(
                -1,curTask.getStarttimeStr(),JLabel.RIGHT
            );

            one[7] = new GeneralBrowserTableCell(
                -1,curTask.getPid()+"",JLabel.RIGHT
            );
            
            model.addRow( one );
        }
    };
    
    public void setTaskTable(BrowserTable _taskTable){
        model = (DefaultTableModel)_taskTable.getModel();
    }
}
