/*
 * GetShadowCopyProcess.java
 *
 * Created on 2007/2/3, PM�4:14
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import javax.swing.*;
import java.util.*;

import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author Administrator
 */
public class GetShadowCopyProcess extends Thread {
    SanBootView view;
    Vector srcDestList; // src and dest binding list
    int targetID;
    int pid;
    RunningTask pane;
    RecordUWSRptable recUWSRpt;
    int row;
    int col;
    String cltIP;
    int cltPort;
    String realWinDir;
    String mac;
    String tipInfo;
    String saveInfo1;
    
    boolean isFinished = false; // whether copy is finished?
    boolean isCPOk = false; //whether copy is ok?
    
    boolean lastResult = false; // volMap本身也可以看成一种快照,而且是最新的
    boolean toEnd = false;   // 表示所有步骤全部完成与否
    
    Runnable setVal = new Runnable(){
        public void run(){
            int i,size,intprocess;
            BindOfSrcAndDest binder;
            String tmpstr,str = "";
            boolean isFirst = true;
            
            size = srcDestList.size();
            for( i=0; i<size; i++ ){
                binder = (BindOfSrcAndDest)srcDestList.elementAt( i );
                
                try{
                    intprocess = Integer.parseInt( binder.process );
                    tmpstr = intprocess + "%";
                }catch( Exception ex ){
                    tmpstr = binder.process;
                }
                
                if( isFirst ){
                    str = tmpstr;
                    isFirst = false;
                }else{
                    str +=" , " + tmpstr;
                }
            }        
            pane.setTaskStatus( str, row, col );
        }
    };
    
    /**
     * Creates a new instance of GetShadowCopyProcess 
     */
    public GetShadowCopyProcess( 
        SanBootView _view,
        int _pid,
        Vector _srcDestList,
        int _targetID,
        RunningTask _pane,
        RecordUWSRptable _recUWSRpt,
        String _cltIP,
        int _cltPort,
        String _realWinDir,
        String _mac,
        String _tipInfo,
        String _saveInfo1,
        int _row,
        int _col 
    ){
        view = _view;
        srcDestList = _srcDestList;
        targetID = _targetID;
        pid = _pid;
        pane = _pane;
        recUWSRpt = _recUWSRpt;
        cltIP = _cltIP;
        cltPort = _cltPort;
        realWinDir = _realWinDir;
        mac = _mac;
        tipInfo = _tipInfo;
        saveInfo1 = _saveInfo1;
        row  = _row;
        col  = _col;
    }
    
    private void parseSrcDest( String src,String dest, String process ){
        BindOfSrcAndDest binder;
        
        int size = srcDestList.size();
        for( int i=0; i<size; i++ ){
            binder = ( BindOfSrcAndDest )srcDestList.elementAt(i);
            if( binder.src.toUpperCase().equals(src.toUpperCase() ) && 
                binder.dest.toUpperCase().equals( dest.toUpperCase() ) 
            ){
//System.out.println("(parseSrcDest): " + binder.process );
                binder.process = process;
                break;
            }
        }
    }
    
    @Override public void run(){
        boolean isOk,isNumber;
        int index,intProcess,size,cnt;
        String process,src,dest,errMsg;
        String accident="";
                
        while( !isFinished ){
            isOk = view.initor.mdb.getCPProcess( pid );
            if( isOk ){
                if( view.initor.mdb.isCPFinished() ){
                    isFinished = true;
                    isCPOk = view.initor.mdb.isCPOK();
                    continue;
                }else{
                    if( view.initor.mdb.parseProcess() ){
                        src = view.initor.mdb.getCPSrc();
                        dest = view.initor.mdb.getCPDest();
                        process = view.initor.mdb.getProcess();
                        
                        index = process.indexOf("%");
                        if( index>=0 ){
                            isNumber = true;
                            try{
                                intProcess =(int)Float.parseFloat( process.substring( 0, index ) );
                            }catch( Exception ex ){
                                isNumber = false;
                                intProcess = 0;
                            }

                            if( isNumber ){
                                parseSrcDest( src, dest, intProcess+"" );
                                
                                try{
                                    SwingUtilities.invokeAndWait( setVal );
                                } catch( Exception e){
                                    e.printStackTrace();
                                }
                            }   
                        }
                    }
                }
            }else{
                isFinished = true;
                isCPOk = false;
                accident = view.initor.mdb.getGeneralErrorMsg( view.initor.mdb.getErrorCode() );
            }
            
            // sleep for 4 sec.
            try{
                Thread.sleep( 2*1000 ); // 4 sec.
            } catch( Exception e){
                e.printStackTrace();
            }
        }
        
        if( isCPOk ){
            
            // 强行将进度设置为100%
            
            if( !mac.equals("") ){
                if( !mac.toUpperCase().equals("LOCALDISK") ){ // copy os from localdisk to target volume
                    pane.setLogOnTabpane( // report copying os is ok
                        tipInfo + " "+ SanBootView.res.getString("common.ok"), 
                        row
                    );
                    
                    cnt=0;
                    isOk = false;
                    while( cnt<3 ){
                        isOk = view.initor.mdb.regOS( cltIP,cltPort, ResourceCenter.REG_MODE_ISCSI_DISK, realWinDir,mac );
                        if( isOk ){
                            break;
                        }else{
                            cnt++;
                        }
                    }
                    
                    if( isOk ){ // reg is ok 
                        if( saveInfo1 !=null ){
                            isOk = view.initor.mdb.modBootConf( cltIP,cltPort,saveInfo1 );
                            if( isOk ){
                                pane.setLogOnTabpane(
                                    SanBootView.res.getString("InitBootHostWizardDialog.log.regOS")+" "+
                                        SanBootView.res.getString("common.ok"), 
                                    row
                                );
                                lastResult = true;
                            }else{
                                errMsg = SanBootView.res.getString("InitBootHostWizardDialog.log.regOS")+" "+
                                        SanBootView.res.getString("common.failed");
                                pane.setLogOnTabpane( errMsg, row );
                                recUWSRpt.putIntoLogBuf( errMsg, row );
                                lastResult = false;
                            }
                        }else{
                            pane.setLogOnTabpane(
                                SanBootView.res.getString("InitBootHostWizardDialog.log.regOS")+" "+
                                    SanBootView.res.getString("common.ok"), 
                                row
                            );
                            lastResult = true;
                        }
                    }else{
                        errMsg = SanBootView.res.getString("InitBootHostWizardDialog.log.regOS")+" "+
                                SanBootView.res.getString("common.failed");
                        pane.setLogOnTabpane( errMsg, row );
                        recUWSRpt.putIntoLogBuf( errMsg, row );
                        lastResult = false;
                    }
                }else{ // copy os from target volume to local disk
                    pane.setLogOnTabpane(  // report copying os is ok
                        tipInfo + " " + SanBootView.res.getString("common.ok"),
                        row
                    );
                    
                    if( realWinDir.equals("") ){
                        errMsg = SanBootView.res.getString("InitBootHostWizardDialog.log.notFoundWindir"); 
                        pane.setLogOnTabpane( errMsg, row );
                        recUWSRpt.putIntoLogBuf( errMsg, row );
                        pane.setTaskStatus(
                            InitTask.getTaskStatusStr( InitTask.INIT_TASK_STA_FAIL), row, 2
                        );
                        
                        lastResult = false;
                    }else{
                        if( saveInfo1 !=null ){
                            // 将ody_iboot.bat和ib_ip_auto_set.conf删除,不检查结果����
                            view.initor.mdb.modBootConf( cltIP,cltPort,saveInfo1 );
                        }
                        
                        cnt =0;
                        isOk = false;
                        while( cnt<3 ){
                            isOk = view.initor.mdb.regOS( cltIP,cltPort, ResourceCenter.REG_MODE_LOCAL_DISK, realWinDir,mac );
                            if( isOk ){
                                break;
                            }else{
                                cnt++;
                            }
                        }
                        
                        if( isOk ){     
                            pane.setLogOnTabpane(
                                SanBootView.res.getString("InitBootHostWizardDialog.log.regOS1")+" "+
                                    SanBootView.res.getString("common.ok"), 
                                row
                            );
                            
                            // 检查恢复盘是否为active的
                            isOk = view.initor.mdb.setPartitionActive( cltIP,cltPort, realWinDir.substring(0,2),"Active" );
                            if( isOk ){
                                // 分析返回值ֵ
                                if( view.initor.mdb.isRight() ){// 目的分区是Active的
                                    pane.setTaskStatus(
                                        InitTask.getTaskStatusStr( InitTask.INIT_TASK_STA_END ), row, 2
                                    );
                                    
                                    lastResult = true;
                                }else{ // 目的分区不是Active的，提示用户手工修改成active
                                    errMsg = SanBootView.res.getString("InitBootHostWizardDialog.log.needSetActive") +" [ " + realWinDir.substring(0,2)+" ]"; 
                                    pane.setLogOnTabpane( errMsg, row );
                                    recUWSRpt.putIntoLogBuf( errMsg,row );

                                    pane.setTaskStatus(
                                        InitTask.getTaskStatusStr( InitTask.INIT_TASK_STA_FAIL ), row, 2
                                    );
                                    
                                    lastResult = false;
                                }
                            }else{
                                errMsg =  SanBootView.res.getString("InitBootHostWizardDialog.log.setactive") +" " + realWinDir.substring(0,2) +" "+
                                           SanBootView.res.getString("common.failed");
                                pane.setLogOnTabpane( errMsg, row ); 
                                recUWSRpt.putIntoLogBuf( errMsg,row );
                                pane.setTaskStatus(
                                    InitTask.getTaskStatusStr( InitTask.INIT_TASK_STA_FAIL), row, 2
                                );
                                lastResult = false;
                            }
                        }else{
                            errMsg = SanBootView.res.getString("InitBootHostWizardDialog.log.regOS1")+" "+
                                      SanBootView.res.getString("common.failed");
                            pane.setLogOnTabpane( errMsg, row );
                            recUWSRpt.putIntoLogBuf( errMsg,row );
                            pane.setTaskStatus(
                                InitTask.getTaskStatusStr( InitTask.INIT_TASK_STA_FAIL), row, 2
                            );

                            lastResult = false;
                        }
                    }
                }
            }else{
                pane.setLogOnTabpane(
                    tipInfo + " "+ SanBootView.res.getString("common.ok"), 
                    row
                );
                lastResult = true;
            }
        }else{
            if( accident.equals("") ){
                errMsg = tipInfo + " "+ SanBootView.res.getString("common.failed");
            }else{
                errMsg = accident;
            }
            pane.setLogOnTabpane( errMsg, row );
            recUWSRpt.putIntoLogBuf( errMsg, row );
            lastResult = false;
        }
        
        toEnd = true;
    }
    
    public boolean getLastResult(){
        return lastResult;
    }
    
    public boolean isToEnd(){
        return toEnd;
    }

// if pid = -1, then display the following
/*
    private boolean overed = false;
    synchronized boolean isOver(){
        return overed;
    }
    synchronized void setOver(boolean val){
        overed = val;
    }
    
    private int phrase = 0;
    public String getPhraseIcon(){
        String str = "";
        switch( phrase ){
            case 0:
                str = "|";
                break;
            case 1:
                str = "/";
                break;
            case 2:
                str = "--";
                break;
            case 3:
                str = "\\";
                break;
            default:
                str = "|";
                break;
        }

        phrase = (phrase+1)%4;
        return str;
    }

    Runnable setVal = new Runnable(){
        public void run(){
            runningTaskPane.setTaskStatus( getPhraseIcon() +"", RunningTaskPane.COPY_OS_ROW,2 );
        }
    };
    
    Runnable process = new Runnable(){
        public void run(){
            while( !isOver() ){
                try{
                    SwingUtilities.invokeAndWait( setVal );

                    Thread.sleep(200);
                } catch( Exception e){
                    e.printStackTrace();
                }
            }
        }
    };
*/
}
