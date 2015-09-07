/*
 * NetworkRunningWithoutOutput.java
 *
 * Created on March 25, 2005, 2:18 PM
 */

package guisanboot.data;

import java.net.*;
import java.io.*;

/**
 *
 * @author  Administrator
 */
public class NetworkRunningWithoutOutput extends NetworkRunning{
    public void parser(String line){
        if( this.isCMDPCmd() ){
            parserForCMDP( line );
        }
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( !this.isEqZero() ){
            this.errMsg += line + "\n";
        }
    }

    public NetworkRunningWithoutOutput(){ 
    }
    
    public NetworkRunningWithoutOutput(Socket socket )throws IOException{
        super( socket );
    }
    
    /** Creates a new instance of NetworkRunningWithoutOutput */
    public NetworkRunningWithoutOutput(String cmdLine,Socket socket) throws IOException{
        super( cmdLine,socket );
    }
}
