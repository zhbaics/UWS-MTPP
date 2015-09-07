/*
 * SetPartitionActive.java
 *
 * Created on 2007/2/9,�PM�5:16
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.net.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public class SetPartitionActive extends NetworkRunning{
    private boolean isRight = false;

    public void parser( String line ){
        if( this.isMTPPCmd() ){
            parserForMTPP( line );
        }else{
            parserForCMDP( line );
        }
    }

    public void parserForMTPP( String line ){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );         
        isRight = s1.equals("0");
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line );
        }else{
            this.errMsg += line +"\n";
        }
    }

    /** Creates a new instance of SetPartitionActive */
    public SetPartitionActive() {
    }
    
    public SetPartitionActive(Socket socket )throws IOException{
        super( socket );
    }
    
    public SetPartitionActive(String cmdLine,Socket socket) throws IOException{
        super( cmdLine,socket );
    }
    
    public boolean isRight(){
        return isRight;
    }
}
