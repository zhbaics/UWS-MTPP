/*
 * GUIAdminOptUWS.java
 *
 * Created on 2008/7/8,�PM 5:11
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 *
 * @author zjj
 */
public class GUIAdminOptUWS extends AbstractConfIO{
    private String serverIp;
    private String port;
    private String user;
    private String txIP;    
    private String desc;
    
    /** Creates a new instance of GUIAdminOptUWS */
    public GUIAdminOptUWS() {
        this( "", ResourceCenter.C_S_PORT+"", ResourceCenter.DEFAULT_ACCT, "" );
    }
    
    public GUIAdminOptUWS(
        String serverIp,
        String port,
        String user,
        String txIP
    ){
        this.serverIp = serverIp;
        this.port = port;
        this.user = user;
        this.txIP = txIP;
        this.desc = serverIp;
    }
    
    public String getServerIp() {
        return serverIp;
    }
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
    
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public int getIntPort(){
        try{
            int intPort = Integer.parseInt( port );
            return intPort;
        }catch(Exception ex){
            return ResourceCenter.C_S_PORT;
        }
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getTxIP(){
        return txIP;
    }
    public void setTxIP( String txIP ){
        this.txIP = txIP;
    }
    
    public void setDesc( String val ){
        this.desc = val;
    }
    
    @Override public String toString(){
        return this.desc;
    }
    
    public void prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append("["+this.getServerIp()+"]\n");
        buf.append(GUIAdminOpt.CONF_IP +"="+this.getServerIp()+"\n");
        buf.append(GUIAdminOpt.CONF_TX_IP +"="+this.getTxIP()+"\n");
        buf.append(GUIAdminOpt.CONF_PORT +"="+this.getPort()+"\n");
        buf.append(GUIAdminOpt.CONF_USER +"="+this.getUser()+"\n");
        System.out.println( buf.toString() );        
    }
    
    public void outputConf(OutputStreamWriter out) throws IOException{
        out.write("["+this.getServerIp()+"]\n");
        out.write(GUIAdminOpt.CONF_IP +"="+this.getServerIp()+"\n");
        out.write(GUIAdminOpt.CONF_TX_IP +"="+this.getTxIP()+"\n");
        out.write(GUIAdminOpt.CONF_PORT +"="+this.getPort()+"\n");
        out.write(GUIAdminOpt.CONF_USER +"="+this.getUser()+"\n");
    }
    
    public int parserConf(int begin,String[] lines ){
        String line;

        do {
            line = lines[begin];
//System.out.println( "line[uws]: " + line );

            if( line.length() <= 0 ){
                begin++;
                continue;
            }

            if( isTitle( line ) )
                break;

            Vector v = splitLine(line);
            if( v.elementAt(0).equals(GUIAdminOpt.CONF_IP.toUpperCase() ) ){
                this.setServerIp( (String)v.elementAt(1) );
                this.setDesc( this.getServerIp() );
            }else if(v.elementAt(0).equals( GUIAdminOpt.CONF_TX_IP.toUpperCase() ) ){
                this.setTxIP( (String)v.elementAt(1) );
            }else if( v.elementAt(0).equals( GUIAdminOpt.CONF_PORT.toUpperCase() ) )
                this.setPort( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals(GUIAdminOpt.CONF_USER.toUpperCase() ) )
                this.setUser( (String)v.elementAt(1) );
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [uws]");
                
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
