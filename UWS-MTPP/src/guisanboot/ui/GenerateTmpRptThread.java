/*
 * GenerateTmpRptThread.java
 *
 * Created on 2007/5/18, PM 4:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import java.io.*;
import guisanboot.data.*;
import guisanboot.res.*;

/**
 *
 * @author Administrator
 */
public class GenerateTmpRptThread extends BasicGetSomethingThread{
    private GenerateUWSReport action = null;
    private boolean retval;
    private String beginDate;
    private String endDate;
    
    /** Creates a new instance of GenerateTmpRptThread */
    public GenerateTmpRptThread(
        SanBootView view,
        String _beginDate,
        String _endDate
    ) {
        super( view );
        
        beginDate = _beginDate;
        endDate = _endDate;
    }
    
    public boolean realRun(){ 
        try{
            action = new GenerateUWSReport(
                ResourceCenter.getCmd( ResourceCenter.CMD_GENERATE_UWS_RPT )+" "+beginDate +" "+endDate + " 0",
                view.getSocket()
            );            
            retval = action.generateRpt();
            if( retval ){
                // 获得当前的运行环境
                Runtime rt = Runtime.getRuntime();
                // 真正运行命令�����
                Process pr = rt.exec( "cmd /c start http://" + view.initor.serverIp+"/tmpreport/" + action.getTmpRptFile() );
                
                InputStreamReader reader1 = new InputStreamReader( pr.getInputStream() );
                String line1 = "";
                while( ( line1 = readLine(reader1) )!=null ){
                    //parser(line1);
                }
                reader1.close();
                
                int retCode = pr.waitFor();
                if( retCode != 0 ){
                    InputStreamReader reader2 = new InputStreamReader(pr.getErrorStream());
                    String line2 = "";
                    while((line2=readLine(reader2))!=null){
                        errMsg += line2+"\n";
                    }
                    reader2.close();
                    retval = false;
                }else{
                    retval = true;
                }
            }else{
                errMsg = action.getErrMsg();
            }
        }catch(Exception ex){
            pdiag.dispose();
            ex.printStackTrace();
        }
        
        return true; // 这样就不会直接报告错误了
    }
    
    public static String readLine(InputStreamReader in) throws IOException{
        StringBuffer strBuf = new StringBuffer();
        int c;
        c = in.read();
        while( c>=0 && c!='\n'){
            strBuf.append((char)c);
            c = in.read();
        }
        if( c<0 && strBuf.length() == 0 )
            return null;
        return strBuf.toString();
    }   
    
    public boolean getRetVal(){
        return retval;
    }
    public String getErrorMsg(){
        return errMsg;
    }
}