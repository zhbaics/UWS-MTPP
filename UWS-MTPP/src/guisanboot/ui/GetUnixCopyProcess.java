/*
 * GetUnixCopyProcess.java
 *
 * Created on 2007/8/17,�PM 2:14
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import javax.swing.*;
import java.util.*;
import guisanboot.data.*;

/**
 *
 * @author Administrator
 */
public class GetUnixCopyProcess extends Thread {
    SanBootView view;
    Vector srcDestList; // src and dest binding list
    int pid;
    RunningTask pane;
    RecordUWSRptable recUWSRpt;
    int row;
    int col;
    String cltIP;
    int cltPort;
    //String osDest;
    BindOfSrcAndDest srcDest;
    String tipInfo;
    String saveInfo1;
    String saveInfo2;
    String saveInfo3;
    
    boolean isFinished = false; // whether copy is finished?
    boolean isCPOk = false; //whether copy is ok?
    boolean isVerifyOK = false; // whether verify is ok?
    
    boolean lastResult = true; // 表示所有步骤是否成功完成
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
    public GetUnixCopyProcess( 
        SanBootView _view,
        int _pid,
        Vector _srcDestList,
        RunningTask _pane,
        RecordUWSRptable _recUWSRpt,
        String _cltIP,
        int _cltPort,
        String _tipInfo,
        int _row,
        int _col,
        //String _osDest,
        BindOfSrcAndDest _srcDest,
        String _saveInfo1,
        String _saveInfo2,
        String _saveInfo3
    ){
        view = _view;
        srcDestList = _srcDestList;
        pid = _pid;
        pane = _pane;
        recUWSRpt = _recUWSRpt;
        cltIP = _cltIP;
        cltPort = _cltPort;
        tipInfo = _tipInfo;
        row  = _row;
        col  = _col;
        //osDest = _osDest;
        srcDest = _srcDest;
        saveInfo1 = _saveInfo1;
        saveInfo2 = _saveInfo2;
        saveInfo3 = _saveInfo3;
    }
    
    private void parseSrcDest( String src,String dest,long process ){
        BindOfSrcAndDest binder;
        long percent;
        
        int size = srcDestList.size();
        for( int i=0; i<size; i++ ){
            binder = ( BindOfSrcAndDest )srcDestList.elementAt(i);
            if( binder.src.equals( src ) && 
                binder.dest.equals( dest ) 
            ){
                percent=(long)(process*100/binder.totoal);
                if( percent>100 ) percent = 100;
                binder.process = percent+"";
SanBootView.log.info(getClass().getName(), " percent: " + binder.process + "%  total: " + binder.totoal );                
                break;
            }
        }
    }
    
    public void run(){
        boolean isOk;
        long process;
        String srcdir,destdir,args,errMsg;
        String accident="";
          
        args = " process ";
        while( !isFinished ){
            // 判断是否结束
            isOk = view.initor.mdb.getCPProcess( pid );
            if( isOk ){
                if( view.initor.mdb.isCPFinished() ){
                    isFinished = true;
                    isCPOk = view.initor.mdb.isCPOK();
                    continue;
                }else{
                    // 还没有结束,继续获取进度��
                    isOk = view.initor.mdb.getUnixCPProcess( cltIP, cltPort, args );
                    if( isOk ){
                        if( view.initor.mdb.parseUnixCpProcess() ){
                            srcdir = view.initor.mdb.getUnixCpSrcDir();
                            destdir = view.initor.mdb.getUnixCpDestDir();
                            process = view.initor.mdb.getUnixCpProcess();
                            
                            parseSrcDest( srcdir,destdir,process );
                            
                            try{
                                SwingUtilities.invokeAndWait( setVal );
                            } catch( Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }else{
                isFinished = true;
                isCPOk = false;
                accident = view.initor.mdb.getGeneralErrorMsg( view.initor.mdb.getErrorCode() );
            }
            
            // sleep for 2 sec.
            try{
                Thread.sleep( 2*1000 ); // 2 sec.
            } catch( Exception e){
                e.printStackTrace();
            }
        }
        
        if( isCPOk ){ 
            if( srcDest != null ){
SanBootView.log.info( getClass().getName()," need to verify copying contents ..... " );
                // 校验特殊目录，避免拷贝时的一个诡异问题。本段代码可在问题解决后删除
                String diffpath = view.initor.mdb.getIscsiCmdPath( cltIP,cltPort );
                if( diffpath.equals("") ){
SanBootView.log.info(getClass().getName(),"Can't get verify directory, we think copying is failed.");                
                    pane.setLogOnTabpane(
                        SanBootView.res.getString("InitNWinHostWizardDialog.log.verify")+" "+
                            SanBootView.res.getString("common.failed"), 
                        row
                    );
                }else{
                    isOk = view.initor.mdb.diff( cltIP,cltPort,srcDest.src+"/"+diffpath, srcDest.dest+"/"+diffpath );
                    if( !isOk ){
                        if( ( view.initor.mdb.getErrorCode() == 2 ) &&
                                ( view.initor.mdb.getErrorMessage().indexOf(" No such file or directory") >=0 )
                        ){ // verify目录不存在，所以认为verify成功
                            isVerifyOK = true;
                        }else{
                            pane.setLogOnTabpane(
                                SanBootView.res.getString("InitNWinHostWizardDialog.log.verify")+" "+
                                    SanBootView.res.getString("common.failed"), 
                                row
                            );
                        }
                    }else{
                        isVerifyOK = true;
                    }
                }
            }else{
                isVerifyOK = true;
            }
            
            if( isVerifyOK ){
                pane.setLogOnTabpane(
                    tipInfo + " "+ SanBootView.res.getString("common.ok"), 
                    row
                );
                
                //if( osDest!= null ){ 
                if( (saveInfo1 != null) && (saveInfo2 != null) && ( saveInfo3 != null) ){
                    isOk = view.initor.mdb.modBootConf( cltIP,cltPort, saveInfo1 );
                    if( isOk ){
                        if( saveInfo2!=null ){
                            isOk = view.initor.mdb.modBootConf( cltIP,cltPort,saveInfo2 );
                            if( isOk ){
                                if( saveInfo3!=null ){
                                    isOk = view.initor.mdb.modBootConf( cltIP,cltPort,saveInfo3 ); 
                                    if( isOk ){
                                        pane.setLogOnTabpane(
                                            SanBootView.res.getString("InitNWinHostWizardDialog.log.modBootConf")+" "+
                                                SanBootView.res.getString("common.ok"), 
                                            row
                                        );
                                    }else{
                                        pane.setLogOnTabpane(
                                            SanBootView.res.getString("InitNWinHostWizardDialog.log.modBootConf")+" "+
                                                SanBootView.res.getString("common.failed"), 
                                            row
                                        );
                                        lastResult = false;
                                    }   
                                }else{
                                    pane.setLogOnTabpane(
                                        SanBootView.res.getString("InitNWinHostWizardDialog.log.modBootConf")+" "+
                                            SanBootView.res.getString("common.ok"), 
                                        row
                                    );
                                }
                            }else{
                                pane.setLogOnTabpane(
                                    SanBootView.res.getString("InitNWinHostWizardDialog.log.modBootConf")+" "+
                                        SanBootView.res.getString("common.failed"), 
                                    row
                                );
                                lastResult = false;
                            }
                        }else{
                            pane.setLogOnTabpane(
                                SanBootView.res.getString("InitNWinHostWizardDialog.log.modBootConf")+" "+
                                    SanBootView.res.getString("common.ok"), 
                                row
                            );
                        }
                    }else{
                        pane.setLogOnTabpane(
                            SanBootView.res.getString("InitNWinHostWizardDialog.log.modBootConf")+" "+
                                SanBootView.res.getString("common.failed"), 
                            row
                        );
                        lastResult = false;
                    }

                    if( saveInfo2!=null ){ // 反向恢复时，不在这里umount OS mp
                        //isOk = view.initor.mdb.umountFs( cltIP,cltPort,osDest );
                        isOk = view.initor.mdb.umountFs( cltIP,cltPort,srcDest.dest );
                        if( isOk ){
                            pane.setLogOnTabpane(
                                SanBootView.res.getString("InitNWinHostWizardDialog.log.umountOSMp")+" "+
                                    SanBootView.res.getString("common.ok"), 
                                row
                            );
                        }else{
                            pane.setLogOnTabpane(
                                SanBootView.res.getString("InitNWinHostWizardDialog.log.umountOSMp")+" "+
                                    SanBootView.res.getString("common.failed"), 
                                row
                            );
                        }  
                    }
                }
            }else{ // verify failed, we think copy has errors.
                errMsg = tipInfo + " "+ SanBootView.res.getString("common.failed");
                pane.setLogOnTabpane( errMsg, row );
                recUWSRpt.putIntoLogBuf( errMsg, row );
                lastResult = false;
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
}
