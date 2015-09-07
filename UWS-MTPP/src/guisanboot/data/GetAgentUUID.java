/*
 * GetAgentUUID.java
 *
 * Created on January 28, 2005, 10:35 AM
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author  Administrator
 */
public class GetAgentUUID extends NetworkRunning {   
    private String uuid = "" ;
    
    /** Creates a new instance of GetAgentUUID */
    public GetAgentUUID( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetAgentUUID(){
        super();
    }

    public void parser(String line ){
        if( this.isMTPPCmd() ){
            parserForMTPP( line );
        }else{
            parserForCMDP( line );
        }
    }

    public void parserForMTPP(String line){
        String s1 = line.trim();
SanBootView.log.debug( getClass().getName(),s1 );
        if( this.isResveredForCmdp( s1 ) ) return;

        if( !s1.equals("") ){
            uuid = s1;
            Pattern pattern = Pattern.compile(
                "^(\\w+)-(\\w+)-(\\w+)-(\\w+)-(\\w+)$"
            );
            Matcher matcher = pattern.matcher( uuid );
            if( !matcher.find() ){
SanBootView.log.warning(getClass().getName(),"uuid is invalid: "+ uuid ); 
                uuid = "";
            }
        }
    }

    public void parserForCMDP( String line ){
        if( !this.isContinueToParserRetValueForCMDP( line ) ) return;

        if( this.isEqZero() ){
            this.parserForMTPP( line );
        }else{
            this.errMsg += line + "\n";
        }
    }

    public boolean getAgentUUID(){
SanBootView.log.info(getClass().getName()," get agt uuid cmd: "+ this.getCmdLine() ); 
        try{
            uuid = "";
            run();
        }catch(Exception ex){
            setErrMsg( this.getExceptionErrMsg( ex ) );
            setRetCode( this.getExceptionErrCode( ex ) );
        }
SanBootView.log.info(getClass().getName()," get agt uuid cmd retcode: "+ this.getRetCode() ); 
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error(getClass().getName()," get agt uuid cmd errmsg: "+ this.getErrMsg() );             
        }
        return isOk;
    }
    
    public String getUUID(){
        return uuid;
    }
}
