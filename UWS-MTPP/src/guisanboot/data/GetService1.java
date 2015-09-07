/*
 * GetService.java
 *
 * Created on 2006/12/22, AM 11:12
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.util.Vector;
import java.net.*;
import java.io.*;


/**
 *
 * @author Administrator
 */
public class GetService1 extends NetworkRunning{
    private Vector<Service> servList = new Vector<Service>();
    private Service curServ = null;

    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );  
        int index = s1.indexOf("=");
        if( index>0 ){
            String value = s1.substring( index+1 );

            if( s1.startsWith( Service.SERV_ID ) ){
                try{
                    int serv_id = Integer.parseInt( value );
                    curServ.setServID( serv_id);
                }catch(Exception ex){
                    curServ.setServID( -1 );
                } 
            }else if( s1.startsWith( Service.SERV_NAME )){
                curServ.setServName( value );
            }else if( s1.startsWith( Service.SERV_DESC )){
                curServ.setServDesc( value );
                servList.addElement( curServ );
            }
        }else{
            if( s1.startsWith( Service.SERV_RECFLAG ) ){
                curServ = new Service();
            }
        }
    }
    
    public GetService1( String cmd,Socket socket ) throws IOException{
        super( cmd ,socket );
    }
    
    public GetService1( String cmd ){
        super( cmd );
    }
    
    public boolean updateService() {
SanBootView.log.info( getClass().getName(), " get service cmd: "+ getCmdLine() );         
        try{
            servList.removeAllElements();
            curServ= null;
            
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get service retcode: "+ getCmdLine() );      
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get service errmsg: "+ getErrMsg() );                 
        }
        return isOk;
    }
    
    public void addServToVector( Service serv ){
        servList.addElement( serv );
    }

    public void removeServFromVector(Service serv ){
        servList.removeElement( serv );
    }
    
    public Vector<Service> getAllService(){
        Vector<Service> list  = new Vector<Service>();
        int size = servList.size();
        for( int i=0;i<size;i++ ){
            list.addElement( servList.elementAt(i) );
        }
        return list;
    }
}
