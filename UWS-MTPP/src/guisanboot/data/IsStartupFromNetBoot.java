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
public class IsStartupFromNetBoot extends NetworkRunning{
    private String ret="";
    
    /** Creates a new instance of GetSystemTime */
    public IsStartupFromNetBoot( String cmd ){
        super( cmd );
    }
    
    public IsStartupFromNetBoot(){    
    }
    
    public void parser( String line ){
        // 命令输出: yes/no
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        ret = line.trim();
    }
    
    public boolean isStartupFromNetBoot(){
        if( ret!= null ){
            return ret.toUpperCase().equals("YES");
        }else{
            return false;
        }
    }
}
