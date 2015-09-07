/*
 * GetLicence.java
 *
 * Created on 2008/3/25, PM 18:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.net.*;
import java.util.regex.Pattern;
import java.io.*;
import java.util.ArrayList;


/**
 *
 * @author Administrator
 */
public class GetLicence extends NetworkRunning {
    private ArrayList<LicObj> list = new ArrayList();
    private LicObj lic = null;
    
    /** Creates a new instance of GetLicence */
    public GetLicence( String cmd, Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetLicence( String cmd ){
        super( cmd );
    }
     
    public GetLicence(){
    }
    
    public void parser( String line ){
        //output:
        /*
        MTPP:16 255 0
        CMDP:8 255 0
        DEFAULT:2 2 255
        */
                
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );       

        int index = line.indexOf(":");
        if( index>0 ){
            if( line.toUpperCase().startsWith( LicObj.LIC_MTPP ) ){
                this.parseLicense( index,line );
            }else if( line.toUpperCase().startsWith( LicObj.LIC_CMDP )){
                this.parseLicense( index, line );
            }else if( line.toUpperCase().startsWith( LicObj.LIC_DEFAULT )){
                this.parseLicense( index, line);
            }
        }
    }
    
    public boolean getLicence() {
SanBootView.log.info( getClass().getName(), " get license cmd: "+ getCmdLine() );         
        try{
            list.clear();
            lic = null;
            run();
        }catch( Exception ex ){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get license retcode: "+ getRetCode() );     
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get license errmsg: "+ getErrMsg() );                 
        }
        return isOk;
    }

    private void parseLicense( int index,String line ){
        String product = line.substring( 0,index );
        String value = line.substring( index+1 );
        String[] lines = Pattern.compile("\\s+").split( value,-1 );

        lic = new LicObj();
        lic.setProduct( product );
SanBootView.log.debug( getClass().getName(), " product of license: "+ product );
        try{
            int point = Integer.parseInt( lines[0] );
SanBootView.log.debug( getClass().getName(), " point of license: "+ point +" for "+product );
            lic.setPoint( point );
            int maxsnap = Integer.parseInt( lines[1] );
SanBootView.log.debug( getClass().getName(), " maxsnap of license: "+ maxsnap +" for "+product );
            lic.setSnapNum( maxsnap );
            long cap = Long.parseLong( lines[2] );
            lic.setCap( cap );
        }catch(Exception ex){
            ex.printStackTrace();
        }
        this.list.add( lic );
    }
    
    public int getMTPPSnapInLic(){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            LicObj licObj = list.get(i);
            if( licObj.isMTPPLic() ){
                return licObj.getSnapNum();
            }
        }
        return -1;
    }

    public int getMTPPPointInLic(){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            LicObj licObj = list.get(i);
            if( licObj.isMTPPLic() ){
                return licObj.getPoint();
            }
        }
        return -1;
    }

    public boolean isSupportMTPP(){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            LicObj licObj = list.get(i);
            if( licObj.isMTPPLic() ){
                return ( licObj.getPoint() > 0 );
            }
        }
        return false;
    }

    public int getCMDPSnapInLic(){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            LicObj licObj = list.get(i);
            if( licObj.isCMDPLic() ){
                return licObj.getSnapNum();
            }
        }
        return -1;
    }

    public int getCMDPPointInLic(){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            LicObj licObj = list.get(i);
            if( licObj.isCMDPLic() ){
                return licObj.getPoint();
            }
        }
        return -1;
    }

    public boolean isSupportCMDP(){
        int size = list.size();
        for( int i=0; i<size; i++ ){
            LicObj licObj = list.get(i);
            if( licObj.isCMDPLic() ){
                return ( licObj.getPoint() > 0 );
            }
        }
        return false;
    }
}
