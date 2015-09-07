/*
 * DHCPConf.java
 *
 * Created on 2011/4/28, AM�11:49
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;
import mylib.tool.Check;

/**
 *
 * @author zjj
 */
public class DHCPConf extends AbstractConfIO{   
    private ArrayList<DHCPOpt> dhcpList = new ArrayList<DHCPOpt>();
    
    /** Creates a new instance of DHCPConf */
    public DHCPConf() {
    }
     
    public DHCPOpt getOneDHCP( String title ){
        int size = dhcpList.size();
        for( int i=0; i<size; i++ ){
            DHCPOpt dhcp = (DHCPOpt)dhcpList.get(i);
            if( dhcp.getTitle().equals( title ) ){
                return dhcp;
            }
        }
        return null;
    }
    
    public void addOneDhcp( DHCPOpt dhcp ){
        dhcpList.add( dhcp );
    }
    
    public void removeOneDhcp( DHCPOpt dhcp ){
        dhcpList.remove( dhcp );
    }
    
    public DHCPOpt getFirstDhcp( ){
        int size = dhcpList.size();
        if( size > 0 ){
            return (DHCPOpt)dhcpList.get( 0 );
        }else{
            return null;
        }
    }

    public void clearAllDHCP(){
        this.dhcpList.clear();
    }
    
    public ArrayList<DHCPOpt> getAllDHCP(){
        int size = dhcpList.size();
        ArrayList<DHCPOpt> ret = new ArrayList<DHCPOpt>( size );
        for( int i=0; i<size; i++ ){
            ret.add( dhcpList.get(i) );
        }
        return ret;
    }
    
    public void outputConf( String conf ) throws IOException{
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream( conf ) );
        outputConf(out);
        out.close();
    }

    public void prtMe(){}
    public String prtMe1(){
        StringBuffer buf = new StringBuffer();
        boolean isFirst = true;
        int size = dhcpList.size();
        for( int i=0; i<size; i++ ){
            if( isFirst ){
                buf.append( ((DHCPOpt)dhcpList.get(i)).prtMe1() );
                isFirst = false;
            }else{
                buf.append("\n"+((DHCPOpt)dhcpList.get(i)).prtMe1() );
            }
        }
        return buf.toString();
    }
    
    public void outputConf(OutputStreamWriter out) throws IOException,IllegalArgumentException {
        int size = dhcpList.size();
        for( int i=0; i<size; i++ ){
            ((DHCPOpt)dhcpList.get(i)).outputConf( out );
        }
    }
    
    public void parserConf( StringBuffer buf ) throws IllegalArgumentException{
        int begin;
        
        String[] lines = Pattern.compile("\n").split( buf.toString(),-1 );
        if( lines != null && lines.length > 0 ){
            begin = 0;
        }else{
            begin = -1;
        }
        
        parserConf( begin,lines );
    }
    
    private boolean isDHCPSection( String line ){
        try{
            String str = line.trim();
            int indx1 = str.indexOf("[");
            int indx2 = str.indexOf("]");
            String ip = str.substring( indx1+1, indx2 ).trim();
            return Check.ipCheck( ip );
        }catch(Exception ex){
            return false;
        }
    }
    
    public int parserConf(int begin,String[] lines ){
        String line;
        AbstractConfIO conf = null;
        DHCPOpt dhcp;
        
        if( begin == -1 ){
            throw new IllegalArgumentException("None any lines in config file.");
        }

        line = lines[begin];
        begin++;
        
        do{
//SanBootView.log.debug(getClass().getName(), " admin conf line: "+line+" begin: "+begin );
            if( line.length() == 0 ||  !isTitle( line )  ){
                begin++;
                continue;
            }
            
            if( isDHCPSection( line ) ){
                dhcp = new DHCPOpt();
                conf = dhcp;
            }else
                throw new IllegalArgumentException("Invalid configure line ->"+line );
                
            begin = conf.parserConf( begin,lines );
            line  = conf.getLastLine();
            
            if( conf instanceof DHCPOpt ){
                dhcp = (DHCPOpt)conf;
                if( getOneDHCP( dhcp.getTitle() )  == null ){
                    dhcpList.add( dhcp );
                }
            }
            
            begin++;
        }while( begin <lines.length );
        
        return begin;
    }
}
