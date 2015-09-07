/*
 * UniProDynamic.java
 *
 * Created on November 30, 2004, 8:29 PM
 */

package guisanboot.datadup.data;

import java.io.*;
import java.util.*;

/**
 *
 * @author  Administrator
 */
public class UniProDynamic extends AbstractUniProfile {
    private String sn;
    private String backup_level;
    private String last_time;
    private String begin_restore_level;
    
    /** Creates a new instance of UniProDynamic */
    public UniProDynamic() {
        this("","","","");
    }
    
    public UniProDynamic(String _sn,String _bakLevel,String _lastTime,String _begin_rst_level){
        sn = _sn;
        backup_level = _bakLevel;
        last_time = _lastTime;
        begin_restore_level = _begin_rst_level;
    }
    
    public String getSn(){
        return sn;
    }
    public void setSn(String _sn){
        sn = _sn;
    }
    
    public String getBakLevel(){
        return backup_level;
    }
    public void setBakLevel(String _bakLevel){
        backup_level = _bakLevel;
    }
    
    public String getLastTime(){
        return last_time;
    }
    public void setLastTime(String _lastTime){
        last_time = _lastTime;
    }
    
    public String getBeginRstLevel(){
        return begin_restore_level;
    }
    public void setBeginRstLevel( String val ){
        begin_restore_level = val;
    }
    
    public void outputProfile(OutputStreamWriter out) throws IOException{
        out.write("[dynamic]\n");
        out.write("sn="+getSn()+"\n");
        out.write("backup-level="+getBakLevel()+"\n");
        out.write("last-time="+getLastTime()+"\n");
        out.write("begin-restore-level="+getBeginRstLevel()+"\n");
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        
        buf.append("[dynamic]\n");
        buf.append("sn="+getSn()+"\n");
        buf.append("backup-level="+getBakLevel()+"\n");
        buf.append("last-time="+getLastTime()+"\n");
        buf.append("begin-restore-level="+getBeginRstLevel()+"\n");
        
        return buf.toString();
    }
    
    public int parserProfile( int begin,String[] lines ) throws IllegalArgumentException{
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
          if( v.elementAt(0).equals("sn") )
            setSn((String)v.elementAt(1));
          else if( v.elementAt(0).equals("backup-level") )
            setBakLevel( (String)v.elementAt(1) );
          else if( v.elementAt(0).equals("last-time") )
            setLastTime( (String)v.elementAt(1) );
          else if( v.elementAt(0).equals("begin-restore-level") )
            setBeginRstLevel( (String)v.elementAt(1) );
          else
            throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [dynamic]");
         
          begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
