/*
 * GetLastSyncTime.java
 *
 * Created on August 23, 2005, 1:21 PM
 */

package guisanboot.data;
import guisanboot.ui.SanBootView;

/**
 *
 * @author  Administrator
 */
public class GetLastSyncTime extends NetworkRunning {
    private String syncTime = "";
    
    /** Creates a new instance of GetLastSyncTime */
    public GetLastSyncTime( String cmd ) {
        super( cmd );
    }
    
    public void parser( String line ){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ line );       
        
        int index = s1.indexOf("=");
        if( index>0 ){
            String value = s1.substring( index+1 ).trim();
            
            // 2006/12/18 has problem
            if( s1.startsWith( BootHost.CLNTMACHINE ) ){
                syncTime = value;
            }
        }
    }
    
    /*
    private String getLastSyncTimeStr(){
        if( syncTime == null || syncTime.equals("") ){
            return "";
        }else{
            try{
                return getHour()+":"+getMinute()+":"+getSecond()+"  "+
                        getYear()+"/"+getMonth()+"/"+getDay()+"/";
            }catch(Exception ex){
                return "";
            }
        }
    }
  
    private int getYear(){
        String _year = syncTime.substring(0,4);
        try{
            return Integer.parseInt(_year);
        }catch(Exception ex){
            return -1;
        }
    }
    
    private int getMonth(){
        String _month = syncTime.substring(4,6);
        try{
            return Integer.parseInt( _month );
        }catch(Exception ex){
            return -1;
        }
    }
    
    private int getDay(){
        String day = syncTime.substring(6,8);
        try{
            return Integer.parseInt( day );
        }catch(Exception ex){
            return -1;
        }
    }
    
    private int getHour(){
        String hour = syncTime.substring(8,10);
        try{
            return Integer.parseInt(hour);
        }catch(Exception ex){
            return -1;
        }
    }
    
    private int getMinute(){
        String minute = syncTime.substring(10,12);
        try{
            return Integer.parseInt(minute);
        }catch(Exception ex){
            return -1;
        }
    }
    
    private int getSecond(){
        String second = syncTime.substring(12);
        try{
            return Integer.parseInt( second );
        }catch(Exception ex){
            return -1;
        }
    }*/
}
