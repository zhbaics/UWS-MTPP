/*
 * GenerateUWSReport.java
 *
 * Created on 2007/5/22,�PM�2:37
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;

/**
 *
 * @author Administrator
 */
public class GenerateUWSReport extends NetworkRunning {
    private String rptFile="";
    
    public void parser(String line){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"=======> "+ line );         
        rptFile = line.trim();
    }
    
    /** Creates a new instance of GenerateUWSReport */
    public GenerateUWSReport(String cmd, Socket socket) throws IOException {
        super( cmd,socket );
    }
    
    public boolean generateRpt(){
SanBootView.log.info(getClass().getName(),"generate report cmd "+ this.getCmdLine() );         
        try{
            rptFile ="";
            run();
        }catch(Exception ex){
            setErrMsg( getExceptionErrMsg( ex ) );
            setRetCode( getExceptionErrCode( ex ) );
        }
SanBootView.log.info(getClass().getName(),"generate report retcode "+ this.getRetCode() );         
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error(getClass().getName(),"generate report errmsg "+ this.getErrMsg() );                
        }
        return isOk;
    }
    
    public String getTmpRptFile(){
        return rptFile;
    }
}
