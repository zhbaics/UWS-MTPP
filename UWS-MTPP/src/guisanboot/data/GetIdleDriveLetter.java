/*
 * GetIdleDriveLetter.java
 *
 * Created on 3 14, 2008, 1:12 PM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import java.util.regex.*;


/**
 *
 * @author  Administrator
 */
public class GetIdleDriveLetter extends NetworkRunning{
    private ArrayList<String> list = new ArrayList<String>();
    
    /** Creates a new instance of GetSystemTime */
    public GetIdleDriveLetter( String cmd ){
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
        // 命令输出:  ZYXWVUTSRQPONMLKJ
        String letter;
        Pattern pattern;
        Matcher matcher;
        
        pattern = Pattern.compile( "[A-Z]" );
        
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );  

        String tmp = line.trim();
        int len = tmp.length();
        for( int i=0;i<len; i++ ){
            letter = tmp.substring( i, i+1 );
            matcher = pattern.matcher(  letter );
            if( matcher.find() ){
                list.add( letter );
            }
        }  
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line );
        }else{
            this.errMsg += line +"\n";
        }
    }
    
    public void cleanList(){
        list.clear();
    }
    
    public String getIdelDriveLetter(){
        if( list.size() > 0 ){
            return (String)list.get(0);
        }else{
            return "";
        }
    }

    public ArrayList<String> getAllIdelDrvLetter(){
        int size = list.size();
        if( size >= 3 ){
            return (ArrayList)list.subList( 0, 3 );
        }else if( size == 2 ) {
            ArrayList<String> ret = new ArrayList<String>();
            ret.add( list.get(0) );
            ret.add( list.get(1) );
            ret.add( list.get(0) );
            return ret;
        }else if ( size == 1 ){
            ArrayList<String> ret = new ArrayList<String>();
            ret.add( list.get(0) );
            ret.add( list.get(0) );
            ret.add( list.get(0) );
            return ret;
        }else{
            ArrayList<String> ret = new ArrayList<String>();
            ret.add( "");
            ret.add( "" );
            ret.add( "" );
            return ret;
        }
    }
}
