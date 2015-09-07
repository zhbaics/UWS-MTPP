/*
 * GetFileList.java
 *
 * Created on January 27, 2005, 3:33 PM
 */

package guisanboot.datadup.cmd;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import guisanboot.res.*;
import guisanboot.exception.*;
import guisanboot.ui.SanBootView;
import guisanboot.data.SuspendNetworkRunning;
import guisanboot.datadup.data.BackupClient;

/**
 *
 * @author  Administrator
 */
public class GetFileList extends SuspendNetworkRunning {
    final public static String SVINFO = "System Volume Information/";
    
    // 这两个目录涉及到windows的垃圾回收，本来可以不备份的。但目前姑且允许备份它们
    final public static String Recy1  = "RECYCLER/";
    final public static String Recy2  = "Recycled/";
    
    private DefaultListModel model;
    private String curFile;
    private boolean filterFatherPath;
    private ArrayList<String> dirList  = new ArrayList<String>();
    private ArrayList<String> fileList = new ArrayList<String>();
    private Vector<String> list = null;
    
    /** Creates a new instance of GetFileList */
    public GetFileList(
        String cmd,
        Socket socket,
        boolean yield,
        int nestNum,
        boolean _filterFatherPath
    ) throws IOException{
        super( cmd,socket,yield,nestNum,ResourceCenter.DEFAULT_CHARACTER_SET );
        filterFatherPath = _filterFatherPath;
    }
    
    @Override public void parserErr( String line ) {
        String s1 = line.trim();
        
        if( s1!=null && !s1.equals("") ){
            curFile = s1;
            
            try{
                SwingUtilities.invokeAndWait( insertModel );
            }catch(Exception ex){
            }
        }
    }
    
    public void parser(String line) throws InterruptedException {
SanBootView.log.debug(getClass().getName(),  "=====> "+line); 
        String s1 = line.trim();
        if( s1 != null && !s1.equals("") ){
            if( s1.equals("./") ){
                return;
            }
            
            if( filterFatherPath ){
                if( s1.equals("../") ){
                    return;
                }
            }
            
            curFile = s1;
            if( list != null ){
                filter();
                list.addElement( curFile );
            }
            
            if( isDir( curFile ) ){
                dirList.add( curFile );
            }else{
                fileList.add( curFile );
            }
        }
    }
    
    public void updateFileList(BackupClient cli,String _beginPath )
                                                    throws IOException,
                                                            InterruptedException,
                                                            BadMagicException,
                                                            BadVersionException,
                                                            BadPackageLenException
    {
        curFile = null;
        fileList.clear();
        dirList.clear();
        
        String beginPath = _beginPath;
        
        /*
         * 当beginPath含有中文时，列目录就会出问题，即使转换了encoding还是这样. ( 2010.12.24 )
        if( this.isSpecifiedEncoding() ){
            beginPath = Check.getStringByEncoding( _beginPath,this.getEncoding() );
        }
        */
        
        if( cli.getID() > 0 ){
            setCmdLine(
                ResourceCenter.getCmd( ResourceCenter.CMD_GET_FILE_LIST ) + cli.getID() + " \""+beginPath+"\""
            );
        }else{
            setCmdLine(
                ResourceCenter.getCmd( ResourceCenter.CMD_GET_FILE_LIST ) + "-d " + cli.getIP() +" " + cli.getPort() + " \""+beginPath+"\""
            );
        }
SanBootView.log.info( getClass().getName(),  "get file cmd: " + getCmdLine() );
        
        run();
        
        // 将cache中的东西放到model中
        insertModelAftSort();
    }
    
    Runnable insertModel = new Runnable(){
        public void run(){
            model.addElement( curFile );
        }
    };
    
    Runnable insert2DotModel = new Runnable(){
        public void run(){
            model.add( 0, "../" );
        }
    };
    
    public void setListModel(DefaultListModel _model){
        model = _model;
    }
    
    public void setListVector( Vector<String> _list ){
        list = _list;
    }
    
    public Vector<String> getRootList(){
        return list;
    }
    
    private void insertModelAftSort(){
        Object[] aList;
        int i,size;
        
        // 先列目录(按字母顺序排序), "../"放在第一个位置
        if( has2Dot ){
            try{
                SwingUtilities.invokeAndWait( insert2DotModel );
            }catch(Exception ex){
            }
        }
        
        aList = dirList.toArray();
        Arrays.sort( aList );
        size = aList.length;
        for( i=0; i<size; i++ ){
            curFile = (String)aList[i];
//System.out.println(" dir: "+ curFile );            
            
            if( !curFile.equals("../") ){
                try{
                    SwingUtilities.invokeAndWait( insertModel );
                }catch(Exception ex){
                }
            }
        }
        
        // 再列文件(按字母顺序排序)
        aList = fileList.toArray();
        Arrays.sort( aList );
        size = aList.length;
        for( i=0; i<size; i++ ){
            curFile = (String)aList[i];
//System.out.println(" File: "+ curFile );              
            try{
                SwingUtilities.invokeAndWait( insertModel );
            }catch(Exception ex){
            }
        }
    }
    
    boolean has2Dot = false;
    private boolean isDir( String file ){
        int length = file.length();
        char tmp = file.charAt( length-1 );
        if( (tmp == '/')|| (tmp == '\\') ){
            if( file.equals("../") ){
                has2Dot = true;
            }
            return true;
        }else{
            return false;
        }
    }
      
    public void filter(){        
        String[] list1;
        String fileStr="";
        boolean isFirst;
        int j;
        
        Pattern pattern2 = Pattern.compile("[/|\\\\]");
        
        if( !curFile.equals("") ){
            if( !curFile.startsWith("ERROR") ){
                int ch = curFile.charAt( curFile.length()-1 );
                boolean isDir = ( ch == '\\') || ( ch == '/');
                list1 = pattern2.split( curFile );
                isFirst = true;
                for( j=0; j<list1.length; j++ ){
                    if( !list1[j].equals("") ){
                        if( isFirst ){
                            fileStr = "/"+list1[j];
                            isFirst = false;
                        }else{
                            fileStr += "/" + list1[j];
                        }
                    }
                }
SanBootView.log.debug(getClass().getName(), " fileStr: "+fileStr ); 
                if( isDir ){
                    fileStr +="/";
                }
                curFile = fileStr;
            }
        }
    }
}
