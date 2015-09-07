/*
 * AddBootHostNetworkRunning.java
 *
 * Created on April 11, 2005, 3:04 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import mylib.tool.*;

/**
 *
 * @author  Administrator
 */
public class AddBootHostNetworkRunning extends AddCmdNetworkRunning{
    private String ipConf ="";
    
    @Override public void parser( String line ){
        String s1 = line.trim();
SanBootView.log.debug( getClass().getName(), "=====> "+ s1 );

        SplitString sp = new SplitString( s1 );
        String idStr = sp.getNextToken();
        ipConf = sp.getNextToken();
        
        try{
            newId = Integer.parseInt( idStr );
        }catch(Exception ex){}
    }
     
    /**
     * Creates a new instance of AddBootHostNetworkRunning
     */
    public AddBootHostNetworkRunning() {
    }
    
    public String getIPConf(){
        return ipConf;
    }
}
