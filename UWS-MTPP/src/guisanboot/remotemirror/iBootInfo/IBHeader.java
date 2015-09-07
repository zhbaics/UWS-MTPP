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
public class IBHeader extends AbstractIB{
    private String tgt_prefix;
    private String tip;
    private String strict;
    
    public IBHeader(){
        this("","","");
    }
    
    public IBHeader( String tgt_prefix,String tip,String strict ){
        this.tgt_prefix = tgt_prefix;
        this.tip = tip;
        this.strict = strict;
    }
    
    public String getTgtPrefix(){
        return this.tgt_prefix;
    }
    public void setTgtPrefix( String val ){
        this.tgt_prefix = val;
    }
    public void replacePsn( String new_psn ){
        int index  = tgt_prefix.lastIndexOf( "." );
        tgt_prefix = tgt_prefix.substring( 0,  index )+"."+new_psn;
    }
    
    public String getTip(){
        return tip;
    }
    public void setTip( String val ){
        this.tip = val;
    }
    
    public String getStrict(){
        return this.strict;
    }
    public void setStrict( String val ){
        this.strict = val;
    }
    
    public  void outputConf(OutputStreamWriter out) throws IOException{
        out.write("[header]\n");
        out.write("tgt-prefix=" + this.getTgtPrefix() + "\n" );
        out.write("TIP=" + this.getTip() + "\n" );
        out.write("STRICT=" + this.getStrict() + "\n" );        
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        
        buf.append("[header]\n");
        buf.append("tgt-prefix="+getTgtPrefix()+"\n");
        buf.append("TIP=" + getTip()+"\n");
        buf.append("STRICT="+ getStrict()+"\n");        
        
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

            if( v.elementAt(0).equals("tgt-prefix") )
                setTgtPrefix( (String)v.elementAt(1).toString());
            else if( v.elementAt(0).equals("TIP") )
                setTip( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("STRICT") )
                setStrict( (String)v.elementAt(1) );
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [header]");
                
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
