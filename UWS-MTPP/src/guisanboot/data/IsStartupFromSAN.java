/*
 * GetSystemTime.java
 *
 * Created on April 22, 2005, 1:12 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import mylib.tool.*;

/**
 *
 * @author  Administrator
 */
public class IsStartupFromSAN extends NetworkRunning{
    private String bus="";
    private String vendor="";
    
    /** Creates a new instance of GetSystemTime */
    public IsStartupFromSAN( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        if( this.isMTPPCmd() ){
            parserForMTPP( line );
        }else{
            parserForCMDP( line );
        }
    }

    public void parserForMTPP( String line ){
        // 命令输出:  Bus:ide Vendor:
        if( line == null || line.equals("") ) return;
        String tmp = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ tmp );

        SplitString sp = new SplitString( tmp );
        String _bus = sp.getNextToken();
        int idx1 = _bus.indexOf(":");
        try{
            bus = _bus.substring( idx1+1 );
        }catch( Exception ex ){
            bus = "";
        }

        String _vendor = sp.getNextToken();
        int idx2 = _vendor.indexOf(":");
        try{
            vendor = _vendor.substring( idx2+1 );
        }catch(Exception ex){
            vendor = "";
        }
    }

    public void parserForCMDP( String line ){
        if( !isContinueToParserRetValueForCMDP( line ) ) return;
        
        if( this.isEqZero() ){
           this.parserForMTPP( line );
        }else{
            this.errMsg += line +"\n";
        }
    }
    
    public boolean isStartupFromSAN(){
        if( vendor!= null ){
            return vendor.toUpperCase().equals("ODYSYS");
        }else{
            return false;
        }
    }
}
