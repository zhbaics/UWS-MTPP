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
public class GetVGSize extends NetworkRunning{
    private float size = -1; // 单位为MB
    
    /** Creates a new instance of GetSystemTime */
    public GetVGSize( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
        String val = line.trim();
SanBootView.log.info(getClass().getName()," getVgSize output: "+val  );
        
        try{
            size = Float.parseFloat( val );
        }catch(Exception ex){
            if( val.equals("0.00bytes") ){
                size = 0;
            }else{
                size = -1;
            }
        }
    }
    
    public float getRealVGSize(){
        return size;
    }
}
