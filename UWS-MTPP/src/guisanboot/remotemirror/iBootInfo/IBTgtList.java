/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.iBootInfo;

import java.io.*;
import java.util.*;
/**
 *
 * @author Administrator
 */
public class IBTgtList extends AbstractIB {
    private String tgtList;
    
    public IBTgtList(){
        this("");
    }
    
    public IBTgtList( String list ){
        this.tgtList = list;
    }
    
    public String getTgtList(){
        return tgtList;
    }
    public void setTgtList( String val ){
        this.tgtList = val;
    }
    
    public  void outputConf(OutputStreamWriter out) throws IOException {     
        out.write("[targetlist]\n");
        out.write("targetlist=" + this.getTgtList() + "\n" );      
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append("[targetlist]\n");
        buf.append("targetlist="+this.getTgtList() +"\n");
        return buf.toString();
    }
    
    public int parserConf(int begin,String[] lines ){
        String line;
        
        do {
            line = lines[begin];
            if( line.length() <= 0 ){
                begin++;
                continue;
            }

            if( isTitle(line) )
                break;

//System.out.println("to be parsed: "+line);
            Vector v = splitLine(line);

            if( v.elementAt(0).equals("targetlist") )
                setTgtList( (String)v.elementAt(1).toString() );
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [targetlist]");
                
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
