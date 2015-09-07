/*
 * AddVolumeNetworkRunning.java
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
public class AddVolumeNetworkRunning extends AddCmdNetworkRunning{
    private long cap = 0L;
    private String san_flag ="";
    
    @Override public void parser( String line ){
        String s1 = line.trim();
SanBootView.log.debug( getClass().getName(), "=====> "+ s1 );

        SplitString sp = new SplitString( s1 );
        String idStr = sp.getNextToken();
        String capStr = sp.getNextToken();
        //如果为非san device，则san_flag为空；否则不为空
        san_flag = sp.getNextToken();
        
        try{
            newId = Integer.parseInt( idStr );
        }catch(Exception ex){}
        
        try{
            cap = Long.parseLong( capStr );
        }catch(Exception ex){}
    }
     
    /**
     * Creates a new instance of AddVolumeNetworkRunning 
     */
    public AddVolumeNetworkRunning() {
    }
    
    public long getCapacity(){
        return cap;
    }
    
    public String getSanFlag(){
        return san_flag;
    }
}
