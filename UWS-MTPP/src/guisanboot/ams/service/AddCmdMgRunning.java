/*
 * AddCmdNetworkRunning.java
 *
 * Created on April 11, 2005, 12:53 PM
 */

package guisanboot.ams.service;

import guisanboot.data.*;
import guisanboot.ui.SanBootView;
import java.net.*;
import java.io.*;

/**
 *
 * @author  Odysys
 */
public class AddCmdMgRunning extends NetworkRunning{
    protected int newId = -1;
    
    public void parser( String line ){
        String s1 = line.trim();
SanBootView.log.debug( getClass().getName(), "=====> "+ s1 );        
        try{
            newId = Integer.parseInt( s1 );
        }catch(Exception ex){}
    }
     
    /** Creates a new instance of AddCmdNetworkRunning */
    public AddCmdMgRunning() {
    }
    
    public AddCmdMgRunning(Socket socket )throws IOException{
        super( socket );
    }
    
    public AddCmdMgRunning(String cmdLine,Socket socket) throws IOException{
        super( cmdLine,socket );
    }
    
    public int getNewId(){
        return newId;
    }
}
