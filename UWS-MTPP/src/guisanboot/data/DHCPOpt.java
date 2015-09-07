/*
 * DHCPOpt.java
 *
 * Created on 2011/4/28,�AM 12:02
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;
import mylib.tool.Check;

/**
 *
 * @author zjj
 */
public class DHCPOpt extends AbstractConfIO{
    public final static String CONF_IP       = "dhcp_ip";
    public final static String CONF_PORT     = "dhcp_port";
    public final static String CONF_USER     = "dhcp_acct";
    public final static String CONF_PASSWORD = "dhcp_pass";

    private String title="";
    private String serverIp="";
    private String port="";
    private String user="";
    private String password="";

    /** Creates a new instance of DHCPOpt */
    public DHCPOpt() {
        this( "","", ResourceCenter.C_S_PORT+"", ResourceCenter.DEFAULT_ACCT, "" );
    }
    
    public DHCPOpt(
        String title,
        String serverIp,
        String port,
        String user,
        String password
    ){
        this.title = title;
        this.serverIp = serverIp;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle( String title ){
        this.title = title;
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
    
    public String getPassword(){
        return this.password;
    }
    public void setPassword( String password ){
        this.password = password;
    }
    
    @Override public String toString(){
        return this.serverIp;
    }

    public void prtMe(){}
    
    public String prtMe1(){
        StringBuffer buf = new StringBuffer();
        buf.append("["+this.getTitle()+"]\n");
        buf.append(DHCPOpt.CONF_IP +"="+this.getServerIp()+"\n");
        buf.append(DHCPOpt.CONF_PORT +"="+this.getPort()+"\n");
        buf.append(DHCPOpt.CONF_USER +"="+this.getUser()+"\n");
        if( !this.password.equals("") ){
            buf.append(DHCPOpt.CONF_PASSWORD +"="+SanBootView.util.encUserKey( password )+"\n");
        }else{
            buf.append(DHCPOpt.CONF_PASSWORD +"="+this.getPassword()+"\n");
        }
        return buf.toString();
    }
    
    public void outputConf(OutputStreamWriter out) throws IOException{
        out.write("["+this.getTitle()+"]\n");
        out.write(DHCPOpt.CONF_IP +"="+this.getServerIp()+"\n");
        out.write(DHCPOpt.CONF_PORT +"="+this.getPort()+"\n");
        out.write(DHCPOpt.CONF_USER +"="+this.getUser()+"\n");
        out.write(DHCPOpt.CONF_PASSWORD+"="+this.getPassword()+"\n");
    }
    
    public int parserConf(int begin,String[] lines ){
        String line;

        // set title field
        this.parseTitle( lines[begin-1] );
        
        do {
            line = lines[begin];
//SanBootView.log.debug(getClass().getName(), "line[dhcp]: " + line );

            if( line.length() <= 0 ){
                begin++;
                continue;
            }

            if( isTitle( line ) ){
//SanBootView.log.debug(getClass().getName(), "=================\n" + this.prtMe1() );
                break;
            }   

            Vector v = splitLine(line);
            if( v.elementAt(0).equals(DHCPOpt.CONF_IP.toUpperCase() ) )
                this.setServerIp( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals( DHCPOpt.CONF_PORT.toUpperCase() ) )
                this.setPort( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals( DHCPOpt.CONF_USER.toUpperCase() ) )
                this.setUser( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals( DHCPOpt.CONF_PASSWORD.toUpperCase() ) ){
                String aPassword = (String)v.elementAt(1);
                if( !aPassword.equals("") ){
                    this.setPassword( SanBootView.util.dencUserKey( aPassword ) );
//SanBootView.log.debug( getClass().getName(), "password: " + this.getPassword() );
                }
            }else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [DHCP]");
                
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }

    private void parseTitle( String line ){
        try{
            String str = line.trim();
            int indx1 = str.indexOf("[");
            int indx2 = str.indexOf("]");
            String ip = str.substring( indx1+1, indx2 ).trim();
            if( Check.ipCheck( ip ) ){
                this.setTitle( ip );
            }
        }catch(Exception ex){
        }
    }
}
