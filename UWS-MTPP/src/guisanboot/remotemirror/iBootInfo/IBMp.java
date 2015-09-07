/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.iBootInfo;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class IBMp extends AbstractIB{
    private ArrayList mpList = new ArrayList();
    
    public IBMp(){        
    }
    
    public  void outputConf( OutputStreamWriter out ) throws IOException {     
        out.write("[lv-mountpoint-fstype]\n");
        int size = mpList.size();
        for( int i=0; i<size; i++ ){
            out.write( mpList.get( i ) + "\n" );
        }
    }
     
    public  String prtMe() {     
        StringBuffer buf = new StringBuffer();
        
        buf.append("[lv-mountpoint-fstype]\n");
        int size = mpList.size();
        for( int i=0; i<size; i++ ){
            buf.append( mpList.get( i ) + "\n" );
        }
        
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
            if( line.indexOf( "swap" ) < 0 ){
                mpList.add( line );
            }
            
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
