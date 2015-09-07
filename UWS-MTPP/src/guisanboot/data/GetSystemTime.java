/*
 * GetSystemTime.java
 *
 * Created on April 22, 2005, 1:12 PM
 */

package guisanboot.data;

import mylib.tool.*;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class GetSystemTime extends NetworkRunning{
    private Day day = null ;
    
    /** Creates a new instance of GetSystemTime */
    public GetSystemTime( String cmd ){
        super( cmd );
    }
    
    public void parser( String line ){
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line ); 
        
        // 日期格式: Fri Apr 22 13:17:13 CST 2005
        SplitString sp = new SplitString( line );
        sp.getNextToken();
        String _mon = sp.getNextToken();
SanBootView.log.debug(getClass().getName()," bak server month: "+_mon ); 
        String _day = sp.getNextToken();
SanBootView.log.debug(getClass().getName()," bak server day: "+_day );
        String _year = sp.getNextTokenN(2);
SanBootView.log.debug(getClass().getName()," bak server year: "+_year ); 
        
        int monInt = ResourceCenter.getMonthInt( _mon );
                
        int dayInt = -1;
        try{
            dayInt = Integer.parseInt( _day );
        }catch(Exception ex){
            dayInt = -1;
        }
        
        int yearInt = -1;
        try{
            yearInt = Integer.parseInt( _year );
        }catch(Exception ex){
            yearInt = -1;
        }
        
        if( monInt != -1 && dayInt != -1 && yearInt != -1 ){
            day = new Day( yearInt, monInt, dayInt );
        }   
    }
    
    public Day getCurSystemTime(){
SanBootView.log.info( getClass().getName(), " get current system time cmd: "+ getCmdLine() );         
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode(ex );
        }
SanBootView.log.info( getClass().getName(), " get current system time retcode: "+ getRetCode() );            
        if( getRetCode() != AbstractNetworkRunning.OK ){
SanBootView.log.error( getClass().getName(), " get current system time errmsg: "+ this.getErrMsg() );       
            return null;
        }else{
            return day;
        }
    }
}
