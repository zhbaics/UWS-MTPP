/*
 * GetDatabaseInfoOnVolume.java
 *
 * Created on 2010/6/11,�AM�11:00
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.cmdp.service;

import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.net.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public class GetDatabaseInfoOnVolume extends NetworkRunning {
    public final static String DB_INFO_TYPE = "dbtype";

    private int dbtype = 0;

    /** Creates a new instance of GetDatabaseInfoOnVolume */
    public GetDatabaseInfoOnVolume( String cmd, Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetDatabaseInfoOnVolume( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"#####: " + s1 );

        int index = s1.indexOf("=");
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
SanBootView.log.debug(getClass().getName(),"@@@@@: "+value );

            if( s1.startsWith( DB_INFO_TYPE )){
                try{
                    dbtype = Integer.parseInt( value );
                }catch( Exception ex ){
                    dbtype = 0;
                }
            }
        }
    }

    public boolean realDo( String volname ){
        this.setCmdLine( ResourceCenter.getCmd( ResourceCenter.CMD_CMDP_GET_DBINFO ) + volname + ",0" );
SanBootView.log.info( getClass().getName(), " get database info in volume cmd: "+ getCmdLine() );
        try{
            this.dbtype = 0;

            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get database info in volume retcode: "+ getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get database info in volume errmsg: "+ getErrMsg() );
        }
        return isOk;
    }

    public int getDBType(){
        return this.dbtype;
    }
}
