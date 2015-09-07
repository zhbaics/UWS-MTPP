/*
 * ListProfile.java
 *
 * Created on Aug 6, 2008, 3:15 PM
 */

package guisanboot.datadup.cmd;

import guisanboot.ui.SanBootView;
import guisanboot.data.NotParseOutputCmd;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.*;

/**
 *
 * @author  Administrator
 */
public class ListProfile extends NotParseOutputCmd {
    private ArrayList<String> profList = new ArrayList<String>();
    
    /** Creates a new instance of ListProfile */
    public ListProfile( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public ListProfile( String cmd ){
        super( cmd );
    }
    
    public ListProfile( Socket socket ) throws IOException{
        super( socket );
    }
      
    public boolean listProfile(){
SanBootView.log.info( getClass().getName(), " list profile cmd: "+ getCmdLine() ); 
        profList.clear();
        
        boolean ret = this.exec();
SanBootView.log.info( getClass().getName(), " list profile recode: "+ this.getRetCode() ); 
        if( !ret ){
SanBootView.log.error( getClass().getName(), " list profile errmsg: "+ this.getErrMsg() );             
        }else{
            parseResult();
        }
        return ret;
    }
    
    public ArrayList<String> getFileList(){
        int size = profList.size();
        ArrayList<String> ret = new ArrayList<String>( size );
        for( int i=0; i<size; i++ ){
            ret.add( profList.get(i) );
        }
        return ret;
    }
    
    public void parseResult( ){ 
        String result = this.getExecResult();
        String[] lines = Pattern.compile("\n").split( result,-1 );
        if( lines == null || lines.length<=0 ) return;
        
        for( int i=0; i<lines.length; i++ ){
            if( !lines[i].equals("") ){
                profList.add( lines[i] );
            }
        }
    }
}
