/*
 * DBSchedule.java
 *
 * Created on July 25, 2008, 12:46 PM
 */

package guisanboot.datadup.data;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.*;

import mylib.UI.BasicUIObject;
import guisanboot.res.*;
import guisanboot.MenuAndBtnCenterForMainUi;

/**
 *
 * @author  Administrator
 */
public class DBSchedule extends BasicUIObject{
    public static final String SCHRECFLAG = "Record:";
    public static final String SCHID      = "sch_id";
    public static final String SCHNAME    = "sch_name";
    public static final String SCHTYPE    = "sch_type";
    public static final String SCHMIN     = "sch_min";
    public static final String SCHHOUR    = "sch_hour";
    public static final String SCHDAY     = "sch_day";
    public static final String SCHMONTH   = "sch_month";
    public static final String SCHWEEK    = "sch_week";
    public static final String SCHLEVEL   = "sch_level";
    public static final String SCHPROFID  = "sch_prof";
    public static final String SCHDEV     = "sch_dev";
    public static final String SCHOBJ     = "sch_obj";
    public static final String SCHPROFNAME = "sch_profname";
    
    public static final int SCH_NORMAL_BACKUP = 1;
    public static final int SCH_MDB_BACKUP    = 2;
    public static final int SCH_FIVCLEAN      = 3;
    public static final int SCH_CHECK_TASKER  = 4;
    public static final int SCH_AUTO_DEL_SNAP = 5; //定义sch_type=5为提前自动删除快照类型的调度
    
    private long id = -1L;
    private String name="";
    private int schType=-1;   // 1: backup 2: mdb backup 3: fiv_clean 4: check tasker
    private String min="";
    private String hour="";
    private String day="";
    private String month="";
    private String week="";
    private String level="";
    private int profId = -1;  // enable or disable backup scheduler
    private int devId = -1; 
    private long objId = -1;
    private String profName="";
    
    /** Creates a new instance of DBSchedule */
    public DBSchedule() {
        super( ResourceCenter.TYPE_SCH );
    }
    
    public DBSchedule(
        long _id,
        String _name,
        int _schType,
        String _min,
        String _hour,
        String _day,
        String _month,
        String _week,
        String _level,
        int _profId,
        int _devId,
        long _objId,
        String _profName
    ){
        super( ResourceCenter.TYPE_SCH );
        
        id = _id;
        name = _name;
        schType = _schType;
        min = _min;
        hour = _hour;
        day = _day;
        month = _month;
        week = _week;
        level = _level;
        profId = _profId;
        devId =_devId;
        objId = _objId;
        profName = _profName;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 0 );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 2 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_SCH ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SCH ) );
        }
    }
    
    public long getID(){
        return id;
    }
    public void setID(long _id){
        id = _id;
    }
    
    public String getName(){
        return name;
    }
    public void setName(String _name){
        name = _name;
    }
    
    public int getSchType(){
        return schType;
    }
    public void setSchType( int _schType ){
        schType = _schType;
    }
    
    public boolean isNormalBackupSch(){
        return ( schType == DBSchedule.SCH_NORMAL_BACKUP );
    }
    
    public boolean isMDBBackupSch(){
        return ( schType == DBSchedule.SCH_MDB_BACKUP );
    }
    
    public boolean isFivCleanSch(){
        return ( schType == DBSchedule.SCH_FIVCLEAN );
    }
    
    public boolean isChkTaskerSch(){
        return ( schType == DBSchedule.SCH_CHECK_TASKER );
    }
    
    public String getMin(){
        return min;
    }
    public int getIntMin(){
        try{
            return Integer.parseInt( min );
        }catch(Exception ex){
            return 0;
        }
    }
    public void setMin(String _min){
        min = _min;
    }
    
    public  String getHour(){
        return hour;
    }
    public int getIntHour(){
        try{
            return Integer.parseInt( hour );
        }catch(Exception ex){
            return 0;
        }
    }
    public void setHour(String _hour){
        hour = _hour;
    }
    
    public String getTimeStr(){
        return min + " " + hour + " " + day + " " + month + " " + week;
    }
    
    public Vector<Integer> getMonthdayList(){
        Vector<Integer> ret = new Vector<Integer>();
    
        String[] list = Pattern.compile(",").split( day.trim() );
        for( int i=0; i<list.length; i++ ){
            try{
                int mday = Integer.parseInt( list[i] );
                ret.addElement( new Integer( mday ) );
            }catch(Exception ex){}
        }

        return ret;
    }
    
    public Vector<Integer> getWeekdayList(){
        Vector<Integer> ret  = new Vector<Integer>();

        String[] list = Pattern.compile(",").split( week.trim() );
        for( int i=0;i<list.length;i++ ){
            try{
                int wday = Integer.parseInt( list[i] );
                ret.addElement( new Integer( wday ) );
            }catch(Exception ex){}
        }

        return ret;
    }
    
    private int getDailyFreqOfScheduler(){
        return CronSchedule.getDailyFreqOfCron( min,hour );
    }
    
    private int getDailyFreqOccurType(){
        if( (min.indexOf("*") >=0 || min.indexOf(",")>=0) && hour.indexOf("*")>=0 ){
            return CronSchedule.TYPE_DAILY_MINUTE_OCR;
        }else{
            return CronSchedule.TYPE_DAILY_HOUR_OCR;
        }
    }
    
    public String getStartTime(){
        int type = getDailyFreqOfScheduler( );
        
        if( type == CronSchedule.TYPE_DAILY_FREQ ){
            type = getDailyFreqOccurType( );
            if( type == CronSchedule.TYPE_DAILY_HOUR_OCR ){
                return "Every "+ getHourFreqVal()+" Hour(s)";
            }else{
                return "Every "+ getMinuteFreqVal()+" Minute(s)";
            }
        }else{
            return hour+":"+min;
        }
    }
    
    public int getHourFreqVal(){
        return getFrequenceVal( hour );
    }

    public int getMinuteFreqVal(){
        return getFrequenceVal( min );
    }

    private int getFrequenceVal(String freq){
        if( freq.indexOf(",")>=0 ){
            String[] list = Pattern.compile(",").split( freq );
            try{
                return Integer.parseInt( list[1] );
            }catch(Exception ex){
                return 1;
            }
        }else{
            return 1;
        }
    }
    
    public String getDay(){
        return day;
    }
    public void setDay(String _day){
        day = _day;
    }
    
    public String getMonth(){
        return month;
    }
    public void setMonth(String _month){
        month = _month;
    }
    
    public String getWeek(){
        return week;
    }
    public void setWeek(String _week){
        week = _week;
    }
    
    public String getLevel(){
        return level;
    }
    public void setLevel(String _level){
        level = _level;
    }
    
    public int getProfId(){
        return profId;
    }
    public void setProfId(int _pid){
        profId = _pid;
    }
    public boolean isEnable(){
        return ( profId == 1 );
    }
    
    public int getDevId(){
        return devId;
    }
    public void setDevId(int _devId){
        devId = _devId;
    }
    
    public long getObjId(){
        return objId;
    }
    public void setObjId(long _objId){
        objId = _objId;
    }
    
    public String getProfName(){
        return profName;
    }
    public void setProfName(String _profName){
        profName = _profName;
    }
    public String getSimpleProfName(){
        if( profName != null ){
            int index = profName.lastIndexOf("/");
            try{
                return profName.substring(index+1).trim();
            }catch(Exception ex){
                return profName;
            }
        }else{
            return "";
        }
    }
    
    @Override public String toString(){
        return id+"";
    }
    
    @Override public String getComment(){
        return id+"";
    }
    
    //** TableRevealable的实现**/
    public boolean enableTableEditable(){
        return false;
    }
    public boolean enableSelected(){
        return true;
    }
    public int  getAlignType(){
        return JLabel.LEFT;
    }
    public Icon getTableIcon(){
        return ResourceCenter.SMALL_SCH;
    }
    public String toTableString(){
        return  name;
    }
    
    //** TreeRevealable的实现
    public Icon getExpandIcon(){
        return null;
    }
    public Icon getCollapseIcon(){
        return null;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return toString();
    }
    public String toTipString(){
        return toTreeString();
    }

    /**
     * 判断是否为提前预删除快照的调度
     * @return
     */
    public boolean isAutoDelSch(){
        return ( schType == DBSchedule.SCH_AUTO_DEL_SNAP );
    }
}
