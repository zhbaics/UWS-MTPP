/*
 * BakSet.java
 *
 * Created on November 15, 2004, 12:36 PM
 */

package guisanboot.datadup.data;

/**
 *
 * @author  Administrator
 */
public class BakSet {
    public final static String BAKSET_RECFLAG = "Record:";
    public final static String BAKSET_ID      = "bks_id";
    public final static String BAKSET_OBJ     = "bks_object";
    public final static String BAKSET_OBJ_SN  = "bks_object_sn";
    public final static String BAKSET_DEVID   = "bks_device";
    public final static String BAKSET_SCHID   = "bks_schedule";
    public final static String BAKSET_LEVEL   = "bks_level";
    public final static String BAKSET_STIME   = "bks_starttime";
    public final static String BAKSET_STATUS  = "bks_status";
    public final static String BAKSET_DEP     = "bks_dependency";
    public final static String BAKSET_EXPIRE  = "bks_expire";
    
    private long id = -1L;
    private int obj = -1;
    private long obj_sn = -1L;
    private long devId = -1L;
    private long sched = -1L;
    private String level ="";
    private String starttime=""; //20041201123445
    // 当备份数据被移走后,其状态变成offline
    private String status="";
    private long dependency=-1L;
    private String expire="";   //20041201092314
    
    public BakSet(){   
    }
    
    /** Creates a new instance of BakSet */
    public BakSet(
        long _id,
        int _obj,
        long _obj_sn,
        long _devId,
        long _sched,
        String _level,
        String _starttime,
        String _status,
        long _dependency,
        String _expire
    ) {
        id = _id;
        obj = _obj;
        obj_sn = _obj_sn;
        devId = _devId;
        sched = _sched;
        level = _level;
        starttime = _starttime;
        status = _status;
        dependency = _dependency;
        expire = _expire;
    }
    
    public long getID(){
        return id;
    }
    public void setID(long _id){
        id = _id;
    }
    
    public int getObj(){
        return obj;
    }
    public void setObj( int _obj ){
        obj = _obj;
    }
    
    public long getObjSN(){
        return obj_sn;
    }
    public void setObjSN(long _obj_sn){
        obj_sn = _obj_sn;
    }
    
    public long getDevId(){
        return devId;
    }
    public void setDevId(long _devId){
        devId = _devId;
    }
 
    public long getSched(){
        return sched;
    }
    public void setSched(long _sched){
        sched = _sched;
    }
    
    public String getLevel(){
        return level;
    }
    public void setLevel(String _level){
        level = _level;
    }
    
    //格是:yearmonthdayhourminsec
    //       4   2   2  2   2  2
    //e.g.  2004 11 23 13 27 45
    public String getStarttime(){
        return starttime;
    }
    public void setStarttime(String _starttime){
        starttime = _starttime;
    }
    
    public String getStarttimeStr(){
        if( starttime == null || starttime.equals("") ){
            return "";
        }else{
            try{
                int month = getMonth(  );
                String monStr = month+"";
                if( month <10 ){
                    monStr="0"+monStr;
                }
                int day = getDay(  );
                String dayStr = day+"";
                if( day <10 ){
                    dayStr ="0"+dayStr;
                }
                int hour = getHour( );
                String hourStr = hour+"";
                if( hour<10 ){
                    hourStr = "0"+hourStr;
                }
                int min = getMinute(  );
                String minStr =min+"";
                if( min < 10 ){
                    minStr ="0"+minStr;
                }
                int sec = getSecond(  );
                String secStr = sec+"";
                if( sec < 10 ){
                    secStr = "0"+secStr;
                }
                return getYear( )+"/"+monStr+"/"+dayStr+" " + 
                        hourStr+":"+minStr+":"+secStr;
            }catch(Exception ex){
                return "";
            }
        }
    }
  
    public int getYear(){
        String _year = starttime.substring(0,4);
        try{
            return Integer.parseInt(_year);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getMonth(){
        String _month = starttime.substring(4,6);
        try{
            return Integer.parseInt( _month );
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getDay(){
        String day = starttime.substring(6,8);
        try{
            return Integer.parseInt( day );
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getHour(){
        String hour = starttime.substring(8,10);
        try{
            return Integer.parseInt(hour);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getMinute(){
        String minute = starttime.substring(10,12);
        try{
            return Integer.parseInt(minute);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getSecond(){
        String second = starttime.substring(12);
        try{
            return Integer.parseInt( second );
        }catch(Exception ex){
            return -1;
        }
    }
    
    public String getStatus(){
        return status;
    }
    public void setStatus(String _status){
        status = _status;
    }
    public boolean isFailed(){
        return !status.equals("END");
    }
    
    public long getDependency(){
        return dependency;
    }
    public void setDependency(long _dep){
        dependency = _dep;
    }
    
    public String getExpire(){
        return expire;
    }
    public void setExpire(String _expire){
        expire = _expire;
    }
    
    @Override public String toString(){
        return id+"";
    }
}
