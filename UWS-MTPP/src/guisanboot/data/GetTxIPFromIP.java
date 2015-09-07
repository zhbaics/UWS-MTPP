/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class GetTxIPFromIP extends NetworkRunning{
    
    private String txIp ;
    private String port;
    public static final String IP = "ip";
    public static final String PORT = "port";
    
    public GetTxIPFromIP(String cmd, Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetTxIPFromIP(String cmd){
        super( cmd );
    }
    
    public GetTxIPFromIP(){
        
    }
    @Override
    public void parser(String line) {
        String s1 = line.trim();
        int index = s1.indexOf("=");
        if( index>0){
        String value = s1.substring( index+1 ).trim();
        if( s1.startsWith( GetTxIPFromIP.IP) ){
                this.txIp = value;
            } else if( s1.startsWith(GetTxIPFromIP.PORT) ){
                this.port = value;
            }
        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTxIp() {
        return txIp;
    }

    public void setTxIp(String txIp) {
        this.txIp = txIp;
    }
    
    
    
}
