/*
 * GetSystemTime.java
 *
 * Created on April 22, 2005, 1:12 PM
 */

package guisanboot.data;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class IsCreatedVg extends NetworkRunning{
    private boolean crted = false;
    
    /** Creates a new instance of GetSystemTime */
    public IsCreatedVg( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
        String val = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ val );         
        if( val.equals("1") ){
            crted = true;
        }else{
            crted = false;
        }
    }
    
    public boolean isCreatedVG(){
        return crted;
    }
}
