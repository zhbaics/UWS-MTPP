/*
 * ListDir.java
 *
 * Created on March 31, 2005, 3:15 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.util.Vector;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.*;

import mylib.tool.*;

/**
 *
 * @author  Administrator
 */
public class ListDir extends NetworkRunning {
    private String basePath = "";
    private Vector<UnixFileObj> fileList = new Vector<UnixFileObj>();
    private UnixFileObj curFile;
    
    /** Creates a new instance of ListDir */
    public ListDir( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public ListDir( String cmd ){
        super( cmd );
    }
    
    public ListDir( Socket socket ) throws IOException{
        super( socket );
    }
    
    public void parser(String line){
        if( line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        
        String s1 = line.trim();
        SplitString sp = new SplitString( s1 );
        String type = sp.getNextToken();
        String month = sp.getNextTokenN(4);
        String day = sp.getNextToken();
        String time = sp.getNextToken();
        String name = sp.getNextToken();

        if( !name.equals("") && !name.equals(".") && !name.equals("..") ){
            curFile = new UnixFileObj( type,basePath+name,name );
            fileList.addElement( curFile );
        }
    }
    
    public void setBasePath( String _basePath ){
        basePath = _basePath;
    }
    
    public boolean listDirectory(){
SanBootView.log.info( getClass().getName(), " listdir cmd: "+ getCmdLine() );         
        try{
            fileList.removeAllElements();
            curFile = null;
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " listdir retcode: "+ getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " listdir errmsg: "+ this.getErrMsg() );            
        }
        return isOk;
    }
    
    public boolean isExist( String file ){
        int size = fileList.size();
System.out.println("found file size: "+ size ) ;
        for( int i=0; i<size; i++ ){
            UnixFileObj f = (UnixFileObj)fileList.elementAt(i);
System.out.println("found file name: "+f.getName() );
            if( f.getName().equals( file ) ){
                return true;
            }
        }
        return false;
    }
    
    public boolean isFile( String file ){
        if( fileList.size() == 1 ){
            UnixFileObj one = fileList.elementAt(0);
            if( one.getName().equals( file ) ){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    public ArrayList<UnixFileObj> getFileList(){
        int size = fileList.size();
        ArrayList<UnixFileObj> ret = new ArrayList<UnixFileObj>( size );
        for( int i=0; i<size; i++ ){
            ret.add( fileList.elementAt(i) );
        }
        return ret;
    }
    
    public Vector<UnixFileObj> getFileList( String filter ){
        Pattern pattern;
        Matcher matcher;
        String name;
        
        Vector<UnixFileObj> ret = new Vector<UnixFileObj>();
        
        int size = fileList.size();
        for( int i=0;i<size;i++ ){
            UnixFileObj one = fileList.elementAt(i);
SanBootView.log.debug( getClass().getName(), "name: "+one.getName()+" abspath: "+one.getAbsPath() );                
            
            name = one.getName(); 
            if( name == null || name.equals("") )
                continue;
            
            if( !one.isDir() ){
                if( filter!=null ){
SanBootView.log.debug( getClass().getName(), " file name: "+one.getName()  );                
                    pattern = Pattern.compile( filter );
                    matcher = pattern.matcher( one.getName() );
                    if ( matcher.find() ){
                        ret.addElement( one );
                    }
                }else{
                    ret.addElement( one );
                }
            }
        }
        
        return ret;
    }
}
