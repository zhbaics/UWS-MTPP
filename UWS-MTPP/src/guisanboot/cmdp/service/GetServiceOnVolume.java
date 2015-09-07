package guisanboot.cmdp.service;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2011
 * Company:      odysys
 * @author
 * @version 1.0
 */
import guisanboot.cmdp.entity.ServicesOnVolume;
import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;


public class GetServiceOnVolume extends NetworkRunning {
    private ArrayList<ServicesOnVolume> list = new ArrayList<ServicesOnVolume>();
    
    public void parser( String line )  {
        if( line == null ) return;
        line = line.trim();
        if( line.equals("") ) return;
        
SanBootView.log.debug(getClass().getName(),"========> "+ line );

        String[] ret = Pattern.compile(":").split( line, -1 );
        if( ret.length > 2 ){
            ServicesOnVolume sv = new ServicesOnVolume( ret[0] );
            sv.setVolumeList( Pattern.compile(";").split( ret[1],-1 ) );
            sv.setServiceList( Pattern.compile(";").split( ret[2],-1 ) );
        }
    }

    public GetServiceOnVolume( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetServiceOnVolume( String cmd ){
        super( cmd,ResourceCenter.DEFAULT_CHARACTER_SET );
    }

    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " get services_on_volume cmd: "+ getCmdLine() );
        try{
            this.list.clear();
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get services_on_volume cmd retcode: "+ getRetCode() );
        boolean isOk = this.isOKForExcuteThisCmd();
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get services_on_volume cmd errmsg: "+ getErrMsg() );
        }
        return isOk;
    }
    
/*
dg-zxxxx:C;D:Oraclexxx;OracleConsolexxx
*/
    public String[] getServicesList( String letter ){
        int size = this.list.size();
        for( int i=0; i<size; i++ ){
            ServicesOnVolume sv = list.get(i);
            if( sv.isBelongedThisDg( letter ) ){
                return sv.getServiceList();
            }
        }
        return null;
    }

    public ArrayList<ServicesOnVolume> getAllServiceListOnVol(){
        return this.list;
    }
}
