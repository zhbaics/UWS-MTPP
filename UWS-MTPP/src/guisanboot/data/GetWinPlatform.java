/*
 * GetAgentInfo.java
 *
 * Created on January 28, 2005, 10:35 AM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;

/**
 *
 * @author  Administrator
 */
public class GetWinPlatform extends NetworkRunning {
    public final String WIN_2000 = "WIN2000";
    public final String WIN_XP = "WINXP";
    public final String WIN_2003 ="WIN2003";
    public final String WIN_HIGH="WINHIGH"; // > win2003
    public final String WIN_LOW ="WINLOW";  // < win2000
    
    private String platform = "";
    
    /** Creates a new instance of GetAgentInfo */
    public GetWinPlatform(String cmd, Socket socket) throws IOException {
        super( cmd,socket );
    }
    
    public GetWinPlatform(){
    }
    
    public void parser(String line){
        platform = line.trim();
        //platform ="win2000";
SanBootView.log.debug(getClass().getName(),"platform: "+platform );
    }
        
    public boolean supportVss(){
        String type = platform.toUpperCase();
        return type.equals( WIN_XP ) || type.equals( WIN_2003 ) || type.equals( WIN_HIGH );
    }
    
    public boolean supportSysCopy(){
        String type = platform.toUpperCase();
        return type.equals( WIN_2000 );
    }
    
    public boolean isWin2000(){
        return platform.toUpperCase().equals( WIN_2000 );
    }
}
