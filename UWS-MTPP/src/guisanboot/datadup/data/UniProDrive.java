package guisanboot.datadup.data;

import java.io.*;
import java.util.*;

public class UniProDrive extends AbstractUniProfile {
    private String title;
    private String path;
    private String user;
    private String passwd;
    private String size;
    private String san_flag;
    private String san_port;
    private String ms_flag; // 1=network; 2=san target
    private String ms_port; // 缺省值:�6020
    
    public UniProDrive(String title) {
        this(title,"","","","","","","","");
    }

    public UniProDrive(
        String _title,
        String _path,
        String _user,
        String _passwd,
        String _size,
        String _san_flag,
        String _san_port,
        String _ms_flag,
        String _ms_port
    ){
        title  = _title;
        path   = _path;
        user   = _user;
        passwd = _passwd;
        size   = _size;
        san_flag = _san_flag;
        san_port = _san_port;
        ms_flag = _ms_flag;
        ms_port = _ms_port;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String _title){
        title = _title;
    }
    
    public String getPath(){
        return path;
    }
    public void setPath(String _path){
        path = _path;
    }
    
    public String getUser(){
        return user;
    }
    public void setUser(String _user){
        user = _user;
    }
  
    public String getPasswd(){
        return passwd;
    }
    public void setPasswd(String _passwd){
        passwd = _passwd;
    }
   
    public String getSize(){
        return size;
    }
    public void setSize(String _size){
        size = _size;
    }
    
    public String getSanFlag(){
        return san_flag;
    }
    public void setSanFlag( String val ){
        san_flag = val;
    }
    
    public String getSanPort(){
        return san_port;
    }
    public void setSanPort( String val ){
        san_port = val;
    }
    
    public String getMSFlag(){
        return ms_flag; 
    }
    public void setMSFlag( String val ){
        ms_flag = val;
    }
    
    public String getMSPort(){
        return ms_port;
    }
    public void setMSPort( String val ){
        ms_port = val;
    }
    
    public void outputProfile(OutputStreamWriter out) throws IOException{
        out.write("["+title+"]\n");
        out.write("path="+getPath()+"\n");
        out.write("user="+getUser()+"\n");
        out.write("password="+getPasswd()+"\n");
        out.write("size="+getSize()+"\n");
        out.write("san_flag="+getSanFlag()+"\n");
        out.write("san_port="+getSanPort()+"\n");
        out.write("ms_flag="+getMSFlag()+"\n");
        out.write("ms_port="+getMSPort()+"\n");
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        
        buf.append("["+title+"]\n");
        buf.append("path="+getPath()+"\n");
        buf.append("user="+getUser()+"\n");
        buf.append("password="+getPasswd()+"\n");
        buf.append("size="+getSize()+"\n");
        buf.append("san_flag="+getSanFlag()+"\n");
        buf.append("san_port="+getSanPort()+"\n");
        buf.append("ms_flag="+getMSFlag()+"\n");
        buf.append("ms_port="+getMSPort()+"\n");
        
        return buf.toString();
    }
    
    public int parserProfile(int begin,String[] lines ) throws IllegalArgumentException{
        String line;

        do {
            line = lines[begin];

            if( line.length() == 0 ){
                begin++;
                continue;
            }

            if( isTitle(line) )
                break;

            Vector v = splitLine(line);
            if( v.elementAt(0).equals("path") )
                setPath((String)v.elementAt(1));
            else if( v.elementAt(0).equals("user") )
                setUser( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("password") )
                setPasswd( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("size") )
                setSize( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("san_flag") )
                setSanFlag( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("san_port") )
                setSanPort( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("ms_flag") )
                setMSFlag( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("ms_port") )
                setMSPort( (String)v.elementAt(1) );
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at ["+title+"]");
            
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
