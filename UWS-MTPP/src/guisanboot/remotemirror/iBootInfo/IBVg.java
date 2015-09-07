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
public class IBVg extends AbstractIB{
    private String name; 
    private String pvs;
    private String lvs;
    private String lvmtype;
    private String partition;
    private String vgpartition;
    
    public IBVg(){
        this( "","","","","","" );
    }
    
    public IBVg( String name,String pvs,String lvs,String lvmtype,String partition,String vgpartition ){
        this.name = name;
        this.pvs = pvs;
        this.lvs = lvs;
        this.lvmtype = lvmtype;
        this.partition = partition;
        this.vgpartition = vgpartition;
    }
    
    public String getLvmtype() {
        return lvmtype;
    }

    public String getLvs() {
        return lvs;
    }

    public String getName() {
        return name;
    }

    public String getPvs() {
        return pvs;
    }
    
    public String getPartition(){
        return this.partition;
    }

    public String getVgPartition(){
        return this.vgpartition;
    }
    
    public void setLvmtype(String lvmtype) {
        this.lvmtype = lvmtype;
    }

    public void setLvs(String lvs) {
        this.lvs = lvs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPvs(String pvs) {
        this.pvs = pvs;
    }
    
    public void setPartition( String partition ){
        this.partition = partition;
    }
    
    public void setVgPartition( String vgpartition ){
        this.vgpartition = vgpartition;
    }
    
    public  void outputConf(OutputStreamWriter out) throws IOException{
        out.write("[vg]\n");
        out.write("name=" + this.getName() + "\n" );
        out.write("pvs=" + this.getPvs() + "\n" );
        out.write("lvs=" + this.getLvs() + "\n" );        
        out.write("lvmtype="+this.getLvmtype() +"\n");
        out.write("partition="+this.getPartition()+"\n");
        out.write("vgpartition="+this.getVgPartition()+"\n");
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        
        buf.append("[vg]\n");
        buf.append("name=" + this.getName() + "\n" );
        buf.append("pvs=" + this.getPvs() + "\n" );
        buf.append("lvs=" + this.getLvs() + "\n" );        
        buf.append("lvmtype="+this.getLvmtype() +"\n");
        buf.append("partition="+this.getPartition() +"\n" );
        buf.append("vgpartition="+this.getVgPartition()+"\n");
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
            else if( v.elementAt(0).equals("pvs") )
                this.setPvs( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("lvs") )
                this.setLvs( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("lvmtype") )
                this.setLvmtype( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("partition") )
                this.setPartition((String)v.elementAt(1) );
            else if( v.elementAt(0).equals("vgpartition") )
                this.setVgPartition((String)v.elementAt(1) );
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [vg]");
                
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
