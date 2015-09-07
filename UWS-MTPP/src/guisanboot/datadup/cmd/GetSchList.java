/*
 * GetSchList.java
 *
 * Created on November 15, 2004, 2:23 PM
 */

package guisanboot.datadup.cmd;

import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.NetworkRunning;
import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;
import java.util.Vector;
import guisanboot.datadup.data.DBSchedule;
import java.util.ArrayList;

/**
 *
 * @author  Administrator
 */
public class GetSchList extends NetworkRunning{ 
    private Vector<DBSchedule> schlist = new Vector<DBSchedule>();
    private DBSchedule curSch = null;
    
    /** Creates a new instance of GetSchList */
    public GetSchList(String cmd ) {
        super( cmd );
    }
    
    public void parser(String line){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 );
        
        int index = s1.indexOf("=");
        
        if( index>0 ){
          String value = s1.substring( index+1 ).trim();
          s1 = s1.substring( 0,index );          
          if( s1.startsWith( DBSchedule.SCHID) ){
            try{
                int id = Integer.parseInt( value );
                curSch.setID( id );
            }catch(Exception ex){
                curSch.setID( -1 );
            }
          }else if( s1.startsWith( DBSchedule.SCHNAME )){
            curSch.setName( value );
          }else if( s1.startsWith( DBSchedule.SCHTYPE ) ){
            try{
                int type = Integer.parseInt( value );
                curSch.setSchType( type );
            }catch(Exception ex){
                curSch.setSchType( -1 );
            }
          }else if( s1.startsWith( DBSchedule.SCHMIN)){
            curSch.setMin( value );
          }else if( s1.startsWith( DBSchedule.SCHHOUR)){
            curSch.setHour( value );
          }else if(s1.startsWith( DBSchedule.SCHDAY )){
            curSch.setDay( value );
          }else if(s1.startsWith( DBSchedule.SCHMONTH )){
            curSch.setMonth( value );
          }else if(s1.startsWith( DBSchedule.SCHWEEK )){
            curSch.setWeek( value );
          }else if(s1.startsWith( DBSchedule.SCHLEVEL)){
            curSch.setLevel( value );
          }else if( s1.equals( DBSchedule.SCHPROFID)){
            try{
                int pid = Integer.parseInt( value );
                curSch.setProfId( pid );
            }catch(Exception ex){   
                curSch.setProfId( -1 );
            }
          }else if(s1.startsWith( DBSchedule.SCHDEV )){
              try{
                  int devid = Integer.parseInt( value );
                  curSch.setDevId( devid );
              }catch(Exception ex){
              }
          }else if(s1.startsWith( DBSchedule.SCHOBJ )){
            try{
                int objid = Integer.parseInt( value );
                curSch.setObjId( objid );
            }catch(Exception ex){
                curSch.setObjId( -1 );
            }
          }else if( s1.equals( DBSchedule.SCHPROFNAME )){
              curSch.setProfName( value );
              schlist.addElement( curSch );
          }
        }else{
          if( s1.startsWith( DBSchedule.SCHRECFLAG )){
            curSch = new DBSchedule();
          }
        }
    }
    
    public GetSchList( String cmd,Socket socket ) throws IOException {
        super( cmd ,socket );
    }

    public boolean updateScheduleList( ){
SanBootView.log.info( getClass().getName(), " get sch list cmd: " + this.getCmdLine()  ); 
        try{
            schlist.removeAllElements();
            curSch = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get sch list cmd retcode: "+getRetCode()  );       
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get sch list cmd errmsg: "+getErrMsg()  );                   
        }
        return isOk;
    }
    
    public DBSchedule getScheduleFromVector(int id){
        int size = schlist.size();
        for( int i=0;i<size;i++ ){
            DBSchedule sch = schlist.elementAt(i);
            if( sch.getID() == id )
                return sch;
        }
        return null;
    }
    
    public Vector<DBSchedule> getNormalBakSchOnBkObj( long bkObjId ){
        Vector<DBSchedule> list = new Vector<DBSchedule>();
        int size = schlist.size();
        for( int i=0; i<size; i++ ){
            DBSchedule one = schlist.elementAt(i);
            if( one.isNormalBackupSch() ){
                if( one.getObjId() == bkObjId )
                    list.addElement( one );
            }
        }
        
        return list;
    }
    
  public ArrayList<DBSchedule> getAutoDelSch(){
        int size = schlist.size();
        ArrayList<DBSchedule> list = new ArrayList<DBSchedule>( size );
        for( int i=0;i<size;i++ ){
            DBSchedule one = schlist.elementAt(i);
            if( one.isAutoDelSch() ){
                list.add( one );
            }
        }
         return list;
    }
    
    public void addScheduleToCache( DBSchedule one ){
        schlist.add( one );
    }
    
    public void removeSchedulerFromCache( DBSchedule one ){
        schlist.remove( one );
    }
    
    public void removeSch( DBSchedule one ){
        int size = schlist.size();
        for( int i=0; i<size; i++ ){
            DBSchedule sch = schlist.elementAt(i);
            if( sch.getID() == one.getID() ){
                schlist.remove(i);
                break;
            }
        }
    }
    
    public ArrayList<DBSchedule> getNormalBackupSch(){
        int size = schlist.size();
        ArrayList<DBSchedule> list = new ArrayList<DBSchedule>( size );
        for( int i=0;i<size;i++ ){
            DBSchedule one = schlist.elementAt(i);
            if( one.isNormalBackupSch() ){
                list.add( one );
            }
        }
        
        return list;
    }
    
    public DBSchedule getMDBBackupSch(){
        int size = schlist.size();
        for( int i=0; i<size;i++ ){
            DBSchedule one = schlist.elementAt(i);
            if( one.isMDBBackupSch() )
                return one;
        }
        
        return null;
    }
    
    public DBSchedule getFivCleanSch(){
        int size = schlist.size();
        for( int i=0;i<size;i++ ){
            DBSchedule one = schlist.elementAt( i );
            if( one.isFivCleanSch() ){
                return one;
            }
        }
        
        return null;
    }
    
    public DBSchedule getChkTaskerSch(){
        int size = schlist.size();
        for( int i=0;i<size;i++ ){
            DBSchedule one = schlist.elementAt( i );
            if( one.isChkTaskerSch() ){
                return one;
            }
        }
        
        return null;
    }
    
    public ArrayList<DBSchedule> getAllSchedule(){
        int size = schlist.size();
        ArrayList<DBSchedule> list  = new ArrayList<DBSchedule>( size );
        for( int i=0;i<size;i++ ){
            list.add( schlist.elementAt(i) );
        }
        return list;
    }
    
    public ArrayList<DBSchedule> getSchOnProfName( String profName ){
        int size = schlist.size();
        ArrayList<DBSchedule> ret = new ArrayList<DBSchedule>( size );
        for( int i=0; i<size; i++ ){
            DBSchedule sch = schlist.elementAt(i);
            if( sch.getProfName().equals( profName ) ){
                ret.add( sch );
            }
        }
        return ret;
    }
    
    public int getSchNum(){
        return schlist.size();
    }
}
