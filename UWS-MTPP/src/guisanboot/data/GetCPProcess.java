/*
 * GetCPProcess.java
 *
 * Created on 2007/2/3,�PM�4:33
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.net.*;
import java.io.*;
import java.util.regex.*;

/**
 *
 * @author Administrator
 */
public class GetCPProcess extends NetworkRunning {
    private String output = "";
    private String process="";
    private String src="";
    private String dest="";
    private boolean isOK = false;
    
    /** Creates a new instance of GetCPProcess */
    public GetCPProcess( String cmd, Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetCPProcess(){    
    }
    
    public void parser( String line ){
        //output format:###78.63%(D:>y:)
        if( line == null || line.equals("") ) return;
        output = line.trim();
SanBootView.log.info(getClass().getName(),"(GetCPProcess)output: " + output );
    }
    
    public void clear(){
        output = "";
        process="";
        src="";
        dest="";
        isOK = false;
    }
    
    public boolean isFinished(){
        if( output.startsWith("ret=") ){
            if(output.equals("ret=0") ){
                isOK = true;
            }else{
                isOK = false;
            }
            return true;
        }else{
            return false;
        }
    }
    
    public boolean isCopyOK(){
        return isOK;
    }
    
    public String getProcess(){
        return process;
    }
    
    public String getSrc(){
        return src;
    }
    public String getDest(){
        return dest;
    }
    
    public boolean parseProcess(){
        Pattern pattern = Pattern.compile(
            "^###(\\d{1,2}\\.\\d{1,2}%|100%)\\(([a-zA-Z]:)>([a-zA-Z]:)\\)$"
        );
        Matcher matcher = pattern.matcher( output );
    
        if( matcher.find() ){
            int grpNum = matcher.groupCount();
            if( grpNum != 3 ){
                return false;
            }else{
                process = matcher.group(1);
                src = matcher.group(2);
                dest = matcher.group(3);
SanBootView.log.info(getClass().getName()," process: " + process +" src: " + src + " dest: " + dest  );               
                return true;
            }
        }else{
            return false;
        }  
    }
}
