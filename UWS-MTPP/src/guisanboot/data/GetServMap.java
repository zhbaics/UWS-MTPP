/*
 * GetServMap.java
 *
 * Created on 2006/12/22,�AM�10:54
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
public class GetServMap extends NetworkRunning {
    private Vector<ServiceMap> mapList = new Vector<ServiceMap>();
    private ServiceMap curMap = null;
    
    public void parser(String line){
        String s1 = line.trim();

        int index = s1.indexOf("=");
SanBootView.log.debug(getClass().getName(),"========> "+ s1 ); 

        if( index>0 ){
            String value = s1.substring( index+1 );

            if( s1.startsWith( ServiceMap.SERVMAP_ID ) ){
                try{
                    int serv_id = Integer.parseInt( value );
                    curMap.setServID( serv_id );
                }catch(Exception ex){
                    curMap.setServID( -1 );
                }
            }else if( s1.startsWith( ServiceMap.SERVMAP_CLNT_ID )){
                try{
                    int clnt_id = Integer.parseInt( value );
                    curMap.setClntID( clnt_id );
                }catch(Exception ex){
                    curMap.setClntID( -1 );
                }
            }else if( s1.startsWith( ServiceMap.SERVMAP_SERV_NAME ) ){
                curMap.setServName( value );
            }else if( s1.startsWith( ServiceMap.SERVMAP_SERV_DESC ) ){
                curMap.setServDesc( value );
                mapList.addElement( curMap );
            }
        }else{
            if( s1.startsWith( ServiceMap.SERVMAP_RECFLAG ) ){
                curMap = new ServiceMap();
            }
        }
    }
    
    public GetServMap( String cmd,Socket socket ) throws IOException{
        super( cmd ,socket );
    }
    
    public GetServMap( String cmd ){
        super( cmd );
    }
    
    public boolean updateServMap() {
SanBootView.log.info( getClass().getName(), " get servicemap cmd: "+ getCmdLine() );         
        try{
            mapList.removeAllElements();
            curMap= null;
            
            run();
        }catch(Exception ex){
            ex.printStackTrace();
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get servicemap retcode: "+ getRetCode() );   
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get servicemap errmsg: "+ this.getErrMsg() );              
        }
        return isOk;
    }
    
    public void addServMapToVector( ServiceMap map ){
        mapList.addElement( map );
    }

    public void removeServMapFromVector(ServiceMap map ){
        mapList.removeElement( map );
    }
    
    public Vector getAllServMapOnClntID( int id ){
        Vector<ServiceMap> list = new Vector<ServiceMap>();
        int size = mapList.size();
        for( int i=0;i<size;i++ ){
            ServiceMap map = mapList.elementAt(i);
            if( map.getClntID() == id ){
                list.addElement( map );
            }
        }
        return list;
    }
    
    public ServiceMap getServiceMap( int cid,String name,String desc ){
        int size = mapList.size();
        for( int i=0; i<size; i++ ){
            ServiceMap map = mapList.elementAt(i);
            if( ( map.getClntID() == cid )&&
                 ( map.getServName().equals( name ) ) &&
                 ( map.getServDesc().equals( desc ) )
            ){
                return map;
            }
        }
        return null;
    }
    
    public ServiceMap getServiceMap( String name,String desc ){
        int size = mapList.size();
        for( int i=0; i<size; i++ ){
            ServiceMap map = mapList.elementAt(i);
            if( ( map.getServName().equals( name ) ) &&
                 ( map.getServDesc().equals( desc ) )
            ){
                return map;
            }
        }
        return null;
    }
    
    public Vector<ServiceMap> getAllServMap(){
        Vector<ServiceMap> list  = new Vector<ServiceMap>();
        int size = mapList.size();
        for( int i=0;i<size;i++ ){
            list.addElement( mapList.elementAt(i) );
        }
        return list;
    }
}
