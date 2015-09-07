/*
 * GUIAdminOpt.java
 *
 * Created on 2008/7/8, PM�5:11
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
public class GUIAdminOpt extends AbstractConfIO{
    public final static String CONF_IP    = "server_ip";  // 管理ip
    public final static String CONF_TX_IP = "tx_ip";      // 数据传输IP
    public final static String CONF_PORT  = "port";
    public final static String CONF_USER  = "user";
    public final static String CONF_LANG  = "lang";
    public final static String CONF_LOG_LEVEL ="loglevel";
    public final static String CONF_RST_MIRROR_TYPE = "rst_mirror_type";
    public final static String CONF_GLOBAL_HITH_DELTA = "global_hight_delta";
    public final static String CONF_GLOBAL_WIDTH_DELTA = "global_width_delta";
    
    private GUIAdminOptGlobal  global   = new GUIAdminOptGlobal();
    private ArrayList<GUIAdminOptUWS> uwsList = new ArrayList<GUIAdminOptUWS>();
    
    /** Creates a new instance of GUIAdminOpt */
    public GUIAdminOpt() {
    }
    
    public GUIAdminOptGlobal getGlobal(){
        return global;
    }
    public void setGlobal( GUIAdminOptGlobal newGlobal ){
        global = newGlobal;
    }
    
    public GUIAdminOptUWS getUWS( String uws_ip ){
        int size = uwsList.size();
        for( int i=0; i<size; i++ ){
            GUIAdminOptUWS uws = (GUIAdminOptUWS)uwsList.get(i);
            if( uws.getServerIp().equals( uws_ip ) ){
                return uws;
            }
        }
        return null;
    }
    
    public void addUWS( GUIAdminOptUWS uws ){
        uwsList.add( uws );
    }
    
    public void removeUWS( GUIAdminOptUWS uws ){
        uwsList.remove( uws );
    }
    
    public GUIAdminOptUWS getFirstUWS( ){
        int size = uwsList.size();
        if( size > 0 ){
            return (GUIAdminOptUWS)uwsList.get(0);
        }else{
            return null;
        }
    }
    
    public ArrayList<GUIAdminOptUWS> getAllUWS(){
        int size = uwsList.size();
        ArrayList<GUIAdminOptUWS> ret = new ArrayList<GUIAdminOptUWS>( size );
        for( int i=0; i<size; i++ ){
            ret.add( uwsList.get(i) );
        }
        return ret;
    }
    
    public void outputConf( String conf ) throws IOException{
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream( conf ) );
        outputConf(out);
        out.close();
    }
    
    public void prtMe(){
        global.prtMe();
        int size = uwsList.size();
        for( int i=0; i<size; i++ ){
            ((GUIAdminOptUWS)uwsList.get(i)).prtMe();
        }
    }
    
    public void outputConf(OutputStreamWriter out) throws IOException,IllegalArgumentException {
        global.outputConf( out );
        int size = uwsList.size();
        for( int i=0; i<size; i++ ){
            ((GUIAdminOptUWS)uwsList.get(i)).outputConf( out );
        }
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
    
    private boolean isUWSSection( String line ){
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
        GUIAdminOptUWS uws;
        
        if( begin == -1 ){
            throw new IllegalArgumentException("None any lines in config file.");
        }
                    
        boolean alreadyGlobal = false;
        
        line = lines[begin];
        begin++;
        
        do{
//System.out.println(" admin conf line: "+line+" begin: "+begin );
            
            if( line.length() == 0 ||  !isTitle( line )  ){
                begin++;
                continue;
            }
            
            if( line.equals("[global]") ){
                if( alreadyGlobal )
                    throw new IllegalArgumentException("Duplication [global] ->"+line );
                this.global = new GUIAdminOptGlobal();
                conf = this.global;
                alreadyGlobal = true;
            } else if( isUWSSection( line ) ){
                uws = new GUIAdminOptUWS();
                conf = uws;
            }else
                throw new IllegalArgumentException("Invalid configure line ->"+line );
                
            begin = conf.parserConf( begin,lines );
            line  = conf.getLastLine();
            
            if( conf instanceof GUIAdminOptUWS ){
                uws = (GUIAdminOptUWS)conf;
                if( getUWS( uws.getServerIp() )  == null ){
                    uwsList.add( uws );
                }
            }
            
            begin++;
        }while( begin <lines.length );
        
        return begin;
    }
    
}
