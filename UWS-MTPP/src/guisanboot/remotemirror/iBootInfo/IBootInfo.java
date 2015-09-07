/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.remotemirror.iBootInfo;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class IBootInfo extends AbstractIB {
    private IBHeader header = new IBHeader();
    private IBTgtList tgtList = new IBTgtList();
    private ArrayList vgList = new ArrayList();
    private ArrayList lvList = new ArrayList();
    private IBMp mp = new IBMp();
    
    public IBootInfo(){        
    }
    
    public IBHeader getIBHeader(){
        return header;
    }
    public void setIBHeader( IBHeader val ){
        this.header = val;
    }
    
    public IBTgtList getIBTgtList(){
        return this.tgtList;
    }
    public void setIBTgtList( IBTgtList val ){
        this.tgtList = val;
    }
    
    public IBMp getIBMp(){
        return this.mp;
    }
    public void setIBMp( IBMp val ){
        this.mp = val;
    }
    
    public void addVg( IBVg vg ){
        this.vgList.add( vg );
    }
    
    public void addLv( IBLv lv ){
        this.lvList.add( lv );
    }
    
    public void outputConf( OutputStreamWriter out ) throws IOException {
    }
    
    public String prtMe(){     
        StringBuffer buf = new StringBuffer();
        
        buf.append( header.prtMe() );
        buf.append( "\n" + tgtList.prtMe() );
        int size = vgList.size();
        for( int i=0; i<size; i++ ){
            buf.append( "\n" + ((IBVg)vgList.get(i)).prtMe() );
        }
        size = lvList.size();
        for( int i=0; i<size; i++ ){
            buf.append( "\n" + ((IBLv)lvList.get(i)).prtMe() );
        }
        
        buf.append( "\n" + mp.prtMe());
        return buf.toString();
    }
    
    public void parserConf( StringBuffer buf ) throws IllegalArgumentException{
        int begin;
        
        String[] lines = Pattern.compile("\n").split( buf.toString(),-1 );
        if( lines!=null && lines.length>0 ){
            begin = 0;
        }else{
            begin = -1;
        }
        
        parserConf( begin,lines );
    }
    
    public IBVg getVg( String  name ){
        int size = vgList.size();
        for( int i=0; i<size; i++ ){
            IBVg vg = (IBVg)vgList.get(i);
            if( vg.getName().equals( name ) ){
                return vg;
            }
        }
        return null;
    }
    
    public IBVg getVgOnLVName( String lv_name ){
        int size = vgList.size();
        for( int i=0; i<size; i++ ){
            IBVg vg = (IBVg)vgList.get(i);
            if( vg.getLvs().equals( lv_name ) ){
                return vg;
            }
        }
        return null;
    }
    
    public IBLv getLv( String  name ){
        int size = lvList.size();
        for( int i=0; i<size; i++ ){
            IBLv lv = (IBLv)lvList.get(i);
            if( lv.getName().equals( name ) ){
                return lv;
            }
        }
        return null;
    }
    
    public int parserConf(int begin,String[] lines ){
        String line;
        AbstractIB conf;
        IBVg vg;
        IBLv lv;
        
        if( begin == -1 ){
            throw new IllegalArgumentException("None any lines in config file.");
        }
                    
        boolean alreadyHeader = false;
        boolean alreadyTgtList = false;
        boolean alreadyMp = false;
                
        line = lines[begin];
        begin++;
        
        do{
//System.out.println(" admin conf line: "+line+" begin: "+begin );
            
            if( line.length() == 0 ||  !isTitle( line )  ){
                begin++;
                continue;
            }
            
            if( line.equals("[header]") ){
                if( alreadyHeader )
                    throw new IllegalArgumentException("Duplication [header] ->"+line );
                this.header = new IBHeader();
                conf = this.header;
                alreadyHeader = true;
            }else if( line.equals("[targetlist]") ){
                if( alreadyTgtList )
                    throw new IllegalArgumentException("Duplication [targetlist] ->"+line );
                this.tgtList = new IBTgtList();
                conf = this.tgtList;
                alreadyTgtList = true;
            }else if( line.equals("[vg]") ){
                vg = new IBVg();
                conf = vg;
            }else if( line.equals("[lv]") ){
                lv = new IBLv();
                conf = lv;
            }else if( line.equals("[lv-mountpoint-fstype]") ){
                if( alreadyMp )
                    throw new IllegalArgumentException("Duplication [lv-mountpoint-fstype] ->"+line );
                this.mp = new IBMp();
                conf = this.mp;
                alreadyMp = true;
            }else
                throw new IllegalArgumentException("Invalid configure line ->"+line );
                
            begin = conf.parserConf( begin,lines );
            line  = conf.getLastLine();
            
            if( conf instanceof IBVg ){
                vg = (IBVg)conf;
                if( getVg( vg.getName() )  == null ){
                    vgList.add( vg );
                }
            }
            
            if( conf instanceof IBLv ){
                lv = (IBLv)conf;
                if( getLv( lv.getName() )  == null ){
                    lvList.add( lv );
                }
            }
            
            
            begin++;
        }while( begin <lines.length );
        
        return begin;
    }
    
    public void replaceTipOnHeader( String new_tip ){
        this.header.setTip( new_tip );
    }
    
    public void replacePSNOnHeader( String psn ){
        this.header.replacePsn( psn );
    }
    
    public void replaceTgtList( String new_tgtList ){
        this.tgtList.setTgtList( new_tgtList );
    }
}
