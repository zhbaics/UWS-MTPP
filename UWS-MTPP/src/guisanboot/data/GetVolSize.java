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
public class GetVolSize extends NetworkRunning{
    private float size = -1;
    
    /** Creates a new instance of GetSystemTime */
    public GetVolSize( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );       

        int idx = s1.indexOf("M");
        String val = s1.substring( 0,idx );
        try{
            size = Float.parseFloat( val );
        }catch(Exception ex){
            size = -1;
        }
    }
    
    public float getRealVolSize(){
        return size;
    }
}
