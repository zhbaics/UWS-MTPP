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
public class GetSnapName extends NetworkRunning{
    private String name="";
    
    /** Creates a new instance of GetSystemTime */
    public GetSnapName( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        name = line.trim();
    }
    
    public String getName(){
        return name;
    }
}
