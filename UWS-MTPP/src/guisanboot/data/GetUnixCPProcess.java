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


/**
 *
 * @author Administrator
 */
public class GetUnixCPProcess extends NetworkRunning {
    private String src_dir="";
    private String snap_dir="";
    private String dest_dir=""; 
    private String process="";
    private String output="";
    
    private boolean isOK = false;
    
    /** Creates a new instance of GetCPProcess */
    public GetUnixCPProcess( String cmd, Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetUnixCPProcess(){    
    }
    
    public void parser( String line ){
        //output format: source_dir=/mnt/rst/home/1 snap_dir=/mnt/rst/home/1 dest_dir=/mnt/rst/home/2 process=11
        if( line == null || line.equals("") ) return;
        output = line.trim();
SanBootView.log.info(getClass().getName(),"(GetUnixCPProcess::parser)output: " + output  ); 
    }
    
    public void clear(){
        output="";
        process="";
        src_dir="";
        snap_dir="";
        dest_dir="";
        isOK = false;
    }
    
    public long getProcess(){
        try{
            return Long.parseLong( process );
        }catch(Exception ex){
            return 0;
        }
    }
    public long getTotal(){
        try{
            return Long.parseLong( output );
        }catch(Exception ex){
            return 0;
        }
    }
    public String getSrcDir(){
        return src_dir;
    }
    public String getDestDir(){
        return dest_dir;
    }
    public String getSnapDir(){
        return snap_dir;
    }
    
    public boolean parseTotal(){
        try{
            long val = Long.parseLong( output );
            return ( val > 0) ;
        }catch(Exception ex){
            return false;
        }
    }
            
    public boolean parseProcess(){
        int indx1,indx2;
        String tmp;
        
        try{
            indx1 = output.indexOf("=");
            indx2 = output.indexOf("snap_dir");
            src_dir = output.substring(indx1+1,indx2).trim();
SanBootView.log.info(getClass().getName()," src dir: "+src_dir  );             

            tmp = output.substring(indx2);
            indx1 = tmp.indexOf("=");
            indx2 = tmp.indexOf("dest_dir");
            snap_dir= tmp.substring( indx1+1, indx2).trim();
SanBootView.log.info(getClass().getName()," snap dir: "+snap_dir ); 

            tmp = tmp.substring( indx2 );
            indx1 = tmp.indexOf("=");
            indx2 = tmp.indexOf("process");
            dest_dir = tmp.substring(indx1+1,indx2).trim();
SanBootView.log.info(getClass().getName()," dest dir: "+dest_dir ); 

            tmp = tmp.substring( indx2 );
            indx1 = tmp.indexOf("=");            
            process = tmp.substring(indx1+1);
SanBootView.log.info(getClass().getName()," process: "+ process ); 
            
            // ���process�Ƿ�Ϊһ������
            long val = Long.parseLong( process );
            if( val < 0 ) return false;
            
            return true;
        }catch(Exception ex){
            return false;
        }
    }
}
