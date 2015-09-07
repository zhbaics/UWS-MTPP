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
public class IBLv extends AbstractIB{
    private String name;
    private String owner;
    private String fstype;
    private String size="";
    private String uuid="";
    private String tag="";
    
    public IBLv(){
        this( "","","" );
    }
    
    public IBLv( String name,String owner,String fstype ){
        this.name = name;
        this.owner = owner;
        this.fstype = fstype;
    }
    
    public String getFstype() {
        return fstype;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public void setFstype(String fstype) {
        this.fstype = fstype;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSize() {
        return size;
    }

    public String getTag() {
        return tag;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid( String uuid ){
        this.uuid = uuid;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
       
    public  void outputConf(OutputStreamWriter out) throws IOException{     
        out.write("[lv]\n");
        out.write("name=" + this.getName() + "\n" );
        out.write("owner=" + this.getOwner()+ "\n" );
        out.write("fstype=" + this.getFstype() + "\n" );      
        out.write("size=" + this.getSize() + "\n" );
        out.write("uuid=" + this.getUuid() + "\n" );
        out.write("tag=" + this.getTag()+ "\n" );         
    }
    
     public String prtMe(){     
        StringBuffer buf = new StringBuffer();
       
        buf.append("[lv]\n");
        buf.append("name=" + this.getName() + "\n" );
        buf.append("owner=" + this.getOwner()+ "\n" );
        buf.append("fstype=" + this.getFstype() + "\n" );      
        buf.append("size=" + this.getSize() + "\n" );
        buf.append("uuid=" + this.getUuid() + "\n" );
        buf.append("tag=" + this.getTag()+ "\n" );         
        
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

            if( v.elementAt(0).equals("name") )
                this.setName( (String)v.elementAt(1).toString());
            else if( v.elementAt(0).equals("owner") )
                this.setOwner( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("fstype") )
                this.setFstype( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("size") )
                this.setSize( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("uuid") )
                this.setUuid( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("tag") )
                this.setTag( (String)v.elementAt(1) );
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [lv]");
                
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
