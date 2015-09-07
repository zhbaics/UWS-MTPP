/*
 * UniOracle.java
 *
 * Created on June 27, 2005, 9:55 AM
 */

package guisanboot.datadup.data;

import java.io.*;
import java.util.*;

/**
 *
 * @author  Administrator
 */
public class UniOracle extends AbstractUniProfile{ 
    private String ora_bak_dir;
    
    public UniOracle(){
        this("");
    }
    
    public UniOracle( String bak_dir ) {
        ora_bak_dir = bak_dir;
    }
    
    public String getOraBakDir(){
        return ora_bak_dir;
    }
    public void setOraBakDir( String _bak_dir ){
        ora_bak_dir = _bak_dir;
    }
    
    public void outputProfile(OutputStreamWriter out) throws IOException{
        out.write("[oracle]\n");
        out.write("oracle_backup_dir=" + getOraBakDir() + "\n" );
    }
    
    public int parserProfile( int begin,String[] lines ) throws IllegalArgumentException{
        String line;

        do {
            line = lines[begin];
            
            if( line.length() == 0 ){
                begin++;
                continue;
            }

            if( isTitle( line ) )
                break;

            Vector v = splitLine(line);
            if( v.elementAt(0).equals("oracle_backup_dir") )
                setOraBakDir( (String)v.elementAt(1));
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [oracle]");
         
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        
        buf.append("[oracle]\n");
        buf.append("oracle_backup_dir="+getOraBakDir()+"\n");
        
        return buf.toString();
    }
}
