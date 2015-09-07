/*
 * GetBakTask.java
 *
 * Created on Aug 11, 2008, 9:47 AM
 */

package guisanboot.datadup.cmd;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import guisanboot.datadup.data.BakTask;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class GetBakTask extends NetworkRunning{
    private ArrayList<BakTask> taskList  = new ArrayList<BakTask>();
    private BakTask curTask  = null;
    
    public void parser(String line)  {
        String s1 = line.trim();
//System.out.debug("#####(GetBakTask): "+s1);

        int index = s1.indexOf("=");
        
        if( index>0){
            String value = s1.substring( index+1 ).trim();
//System.out.debug("@@@@@: "+value);
          
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
System.out.println(" =========== task msg: "+ value );                
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
                curTask.setClientName( value );
            }else if(s1.startsWith( BakTask.BAKTASKPROFILE ) ){ 
                curTask.setProfileName( value );
                if( curTask.isNormalTask() ){
                    taskList.add( curTask );
                }
            }
        }else{
            if( s1.startsWith( BakTask.BAKTASKRECFLAG )){
                curTask = new BakTask();
            }
        }
    }
    
    public GetBakTask( String cmd,Socket socket ) throws IOException{
        super( cmd,socket );
    }
    
    public GetBakTask( String cmd ){
        super( cmd );
    }
    
    public synchronized boolean updateTaskList(){
SanBootView.log.info( getClass().getName()," get task cmd: "+ this.getCmdLine() );
        try{
            taskList.clear();
            curTask = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," get task cmd retcode: "+ this.getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," get task cmd errmsg: "+ this.getErrMsg() );            
        }
        return isOk;
    }

    // mode == 1 ����
    // mode == 0 ����
    public synchronized ArrayList<BakTask> getAllTaskList( int mode ){
        int i;
        
        int size = taskList.size();
        ArrayList<BakTask> list = new ArrayList<BakTask>( size );
        if( size<=0 ) return list;
        
        if( mode == 1 ){
            for( i=size-1;i>=0;i--){
                list.add( taskList.get(i) );
            }
        }else{
            for( i=0;i<size;i++ ){
                list.add( taskList.get(i) );
            }
        }
        
        return list;
    }
    
    public synchronized void removeTask( BakTask one ){
        taskList.remove( one );
    }
}
