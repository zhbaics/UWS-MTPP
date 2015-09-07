/*
 * MirrorGrp.java
 *
 * Created on 2008/6/5,��PM 12:14
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.datadup.data.Clock;
import guisanboot.datadup.data.CronSchedule;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Pattern;
import mylib.tool.SplitString;

/**
 *
 * @author zjj
 */
public class MirrorGrp {
    public final static int SIG_SIGHUP   = 1;  // 重新读mirror group配置
    public final static int SIG_SIGINT   = 2 ; // 终止
    public final static int SIG_SIGUSR1  = 10; // 强制创建快照
    public final static int SIG_SIGUSR2  = 12; // 强制传送
    public final static int SIG_SIGALRM  = 26; // 睡眠唤醒
    public final static int SIG_SIGRTMIN = 34; // 根据本地快照强制删除远处快照，使远程快照与本地相同

    public final static int MG_DEFAULT_INTERVAL_TIME = 3600; // unit: seconds
    public final static int MG_DEFAULT_MIN_INCREMENT_SIZE = 65536; //unit: bytes
    public final static String MG_DEFAULT_BEF_CMD = "/www/cgi-bin/mg_snapbf";
    public final static String MG_DEFAULT_AFT_CMD = "/www/cgi-bin/mg_snapaf";
    public final static int MAX_SNAP_SYSTEM_DISK = 30;
    public final static int MAX_SNAP_DATA_DISK = 60;

    public final static int MG_TYPE_LOCALDISK = 1; // local disk
    public final static int MG_TYPE_RADISK = 2;  // remote application disk
    public final static int MG_TYPE_SG = 3;  // snapshot group
    public final static int MG_TYPE_UCS = 4; // ucs snapshot

    public final static int MG_SCH_TYPE_ROTATE = 0;  // linear
    public final static int MG_SCH_TYPE_CROND = 1;   // non-linear
    public final static int MG_SCH_TYPE_MCLOCK = 2;  // multi occur
    
    public static final String MG_RECFLAG="Record:";
    public static final String MG_mg_id ="mg_id";
    public static final String MG_mg_name ="mg_name";
    public static final String MG_mg_type="mg_type";
    public static final String MG_mg_src_ip="mg_src_ip";
    public static final String MG_mg_src_port="mg_src_port";
    public static final String MG_mg_src_disk_uuid="mg_src_disk_uuid";
    public static final String MG_mg_src_root_id="mg_src_root_id";
    public static final String MG_mg_interval_time ="mg_interval_time";
    public static final String MG_mg_min_snap_size ="mg_min_snap_size";
    public static final String MG_mg_max_snapshot="mg_max_snapshot";
    public static final String MG_mg_pid="mg_pid";
    public static final String MG_mg_before_cmd="mg_before_cmd";
    public static final String MG_mg_after_cmd="mg_after_cmd";
    public static final String MG_mg_desc = "mg_desc";
    public static final String MG_mg_schedule_type = "mg_schedule_type";
    public static final String MG_mg_schedule_minute = "mg_schedule_minute";
    public static final String MG_mg_schedule_hour="mg_schedule_hour";
    public static final String MG_mg_schedule_day="mg_schedule_day";
    public static final String MG_mg_schedule_month="mg_schedule_month";
    public static final String MG_mg_schedule_week="mg_schedule_week";
    public static final String MG_mg_schedule_clock_zone="mg_schedule_clock_zone";
    public static final String MG_mg_schedule_hour1 = "mg_schedule_hour1";
    public static final String MG_mg_schedule_clock_set = "mg_schedule_clock_set";
    public static final String MG_mg_log_max_size = "mg_log_max_size";
    public static final String MG_mg_log_max_time = "mg_log_max_time";

    private int mg_id = -1;
    private String mg_name; // 255
    private int mg_type; //# 1 is localdisk, 2 remote application disk, 3 sg disk
    private String mg_src_ip; // 255
    private int mg_src_port; 
    private String mg_src_disk_uuid; // 100,
    private int mg_src_root_id;
    private int mg_interval_time;
    private int mg_min_snap_size;
    
    // 当删除卷上快照时，其上的mg也会删除目的端的快照。如果mg_max_snapshot为0，则mg会删除所有的快照，只保留最新快照，
    // 所以要将此值设置为最大值，防止该情况发生
    private int mg_max_snapshot = 255; 
    
    private int mg_pid;
    private String mg_before_cmd; // 1024
    private String mg_after_cmd; // 1024
    private String mg_desc;  // 255

    private int mg_schedule_type = 0;    // 0: rotate   1：cron   2:multi-occur
    private String mg_schedule_minute="0";
    private String mg_schedule_hour="0";
    private String mg_schedule_day="*";
    private String mg_schedule_month="*";
    private String mg_schedule_week="*";

    // 由界面控制该字段如何使用，服务器端不需要理解该字段
    private String mg_schedule_clock_zone="";    // 对每日的小时区段进行控制，只有位于这里面的小时才能自动生成快照
    private String mg_schedule_hour1="0";   // 保存界面上以小时为单位重复发生的取值
    private String mg_schedule_clock_set="";

    private String mg_log_max_time="";
    private String mg_log_max_size="";
    
    /** Creates a new instance of MirrorGrp */
    public MirrorGrp() {
    }

    public MirrorGrp(
        int mg_schedule_type,
        String mg_schedule_minute,
        String mg_schedule_hour,
        String mg_schedule_day,
        String mg_schedule_month,
        String mg_schedule_week,
        String mg_schedule_clock_zone,
        String mg_schedule_hour1,
        String mg_schedule_clock_set
    ){
        this.mg_schedule_type = mg_schedule_type;
        this.mg_schedule_minute = mg_schedule_minute;
        this.mg_schedule_hour = mg_schedule_hour;
        this.mg_schedule_day = mg_schedule_day;
        this.mg_schedule_month = mg_schedule_month;
        this.mg_schedule_week = mg_schedule_week;
        this.mg_schedule_clock_zone = mg_schedule_clock_zone;
        this.mg_schedule_hour1 = mg_schedule_hour1;
        this.mg_schedule_clock_set = mg_schedule_clock_set;
    }

    public MirrorGrp(
        String mg_name,
        int mg_type,
        int root_id,
        String mg_desc
    ){
        this(-1, mg_name, mg_type, "", -1, "", root_id, 0, 0, 255, -1, "", "", mg_desc );
    }

    public MirrorGrp(
        int mg_type,
        String mg_src_ip,
        int mg_src_port,
        String mg_src_disk_uuid,
        int mg_src_root_id,
        int mg_interval_time,
        int mg_min_snap_size,
        int mg_max_snapshot,
        String bef_cmd,
        String aft_cmd
    ){
        this.mg_type = mg_type;
        this.mg_src_ip = mg_src_ip;
        this.mg_src_port = mg_src_port;
        this.mg_src_disk_uuid = mg_src_disk_uuid;
        this.mg_src_root_id = mg_src_root_id;
        this.mg_interval_time = mg_interval_time;
        this.mg_min_snap_size = mg_min_snap_size;
        this.mg_max_snapshot = mg_max_snapshot;
        this.mg_before_cmd  = bef_cmd;
        this.mg_after_cmd = aft_cmd;
    }

    public MirrorGrp( 
        int mg_id,
        String mg_name,
        int mg_type,
        String mg_src_ip,
        int mg_src_port,
        String mg_src_disk_uuid,
        int mg_src_root_id,
        int mg_interval_time,
        int mg_min_snap_size,
        int mg_max_snapshot,
        int mg_pid,
        String mg_before_cmd,
        String mg_after_cmd,
        String mg_desc
    ){
        this.mg_id = mg_id;
        this.mg_name = mg_name;
        this.mg_type = mg_type;
        this.mg_src_ip = mg_src_ip;
        this.mg_src_port = mg_src_port;
        this.mg_src_disk_uuid = mg_src_disk_uuid;
        this.mg_src_root_id = mg_src_root_id;
        this.mg_interval_time = mg_interval_time;
        this.mg_min_snap_size = mg_min_snap_size;
        this.mg_max_snapshot = mg_max_snapshot;
        this.mg_pid = mg_pid;
        this.mg_before_cmd = mg_before_cmd;
        this.mg_after_cmd = mg_after_cmd;
        this.mg_desc = mg_desc;
    }
    
     public MirrorGrp( 
        int mg_id,
        String mg_name,
        int mg_type,
        String mg_src_ip,
        int mg_src_port,
        String mg_src_disk_uuid,
        int mg_src_root_id,
        int mg_interval_time,
        int mg_min_snap_size,
        int mg_max_snapshot,
        int mg_pid,
        String mg_before_cmd,
        String mg_after_cmd,
        String mg_desc,
        String mg_log_max_size,
        String mg_log_max_time
    ){
        this.mg_id = mg_id;
        this.mg_name = mg_name;
        this.mg_type = mg_type;
        this.mg_src_ip = mg_src_ip;
        this.mg_src_port = mg_src_port;
        this.mg_src_disk_uuid = mg_src_disk_uuid;
        this.mg_src_root_id = mg_src_root_id;
        this.mg_interval_time = mg_interval_time;
        this.mg_min_snap_size = mg_min_snap_size;
        this.mg_max_snapshot = mg_max_snapshot;
        this.mg_pid = mg_pid;
        this.mg_before_cmd = mg_before_cmd;
        this.mg_after_cmd = mg_after_cmd;
        this.mg_desc = mg_desc;
        this.mg_log_max_size = mg_log_max_size;
        this.mg_log_max_time = mg_log_max_time;
    }
    
    public int getMg_id() {
        return mg_id;
    }

    public void setMg_id(int mg_id) {
        this.mg_id = mg_id;
    }

    public boolean isValidMg(){
        return ( this.mg_id != -1 );
    }

    public String getMg_name() {
        return mg_name;
    }

    public void setMg_name(String mg_name) {
        this.mg_name = mg_name;
    }

    public int getMg_type() {
        return mg_type;
    }

    public void setMg_type(int mg_type) {
        this.mg_type = mg_type;
    }

    public String getMg_src_ip() {
        return mg_src_ip;
    }

    public void setMg_src_ip(String mg_src_ip) {
        this.mg_src_ip = mg_src_ip;
    }

    public int getMg_src_port() {
        return mg_src_port;
    }

    public void setMg_src_port(int mg_src_port) {
        this.mg_src_port = mg_src_port;
    }

    public String getMg_src_disk_uuid() {
        return mg_src_disk_uuid;
    }

    public void setMg_src_disk_uuid(String mg_src_disk_uuid) {
        this.mg_src_disk_uuid = mg_src_disk_uuid;
    }

    public int getMg_src_root_id() {
        return mg_src_root_id;
    }

    public void setMg_src_root_id(int mg_src_root_id) {
        this.mg_src_root_id = mg_src_root_id;
    }

    public int getMg_interval_time() {
        return mg_interval_time;
    }

    public void setMg_interval_time(int mg_interval_time) {
        this.mg_interval_time = mg_interval_time;
    }

    public static Integer[] getDayOfIntervalTime1( int interval ){
        int day=0,hour=0,min=0,sec=0;

        if( interval < 60 ){
            // 当interval<60时，就认为min=0
            min = 0;
            sec = interval;
System.out.println( interval + " {= [0,60],day="+day+" hour="+hour+" min="+min +" sec="+sec );
        }else{
            if( interval < 3600 ){
                min = (interval / 60);
                sec = interval-min*60;
System.out.println( interval +" {= [60,3600] day="+day+" hour="+hour+" min="+min +" sec=" +sec );
            }else{
                if( interval < 3600*24 ){
                    hour = (interval/3600);
                    min = (interval-hour*3600)/60;
                    sec = interval-hour*3600-min*60;
System.out.println( interval+" {= [3600,3600*24],day="+day+" hour="+hour+" min="+min +" sec=" +sec );
                }else{
                    day = (interval/(3600*24) );
                    hour = (interval-day*3600*24)/3600;
                    min = (interval-day*3600*24-hour*3600)/60;
                    sec = interval-day*3600*24-hour*3600-min*60;
System.out.println( interval +" {= [3600*24, 无穷大], day="+day+" hour="+hour+" min="+min + " sec=" +sec );
                }
            }
        }
        
        Integer[] items = new Integer[4];
        items[0] = new Integer( sec );
        items[1] = new Integer( min );
        items[2] = new Integer( hour );
        items[3] = new Integer( day );

        return items;
    }

    public static Integer[] getDayOfIntervalTime( int interval ){
        int day=0,hour=0,min=0;

        if( interval < 60 ){
            // 当interval<60时，就认为min=0
            min = 0;
System.out.println("[0,60],day="+day+" hour="+hour+" min="+min );
        }else{
            if( interval < 3600 ){
                min = ( interval / 60 );
System.out.println("[60,3600] day="+day+" hour="+hour+" min="+min );
            }else{
                if( interval < 3600*24 ){
                    hour = (interval/3600);
                    min = (interval-hour*3600)/60;
System.out.println("[3600,3600*24],day="+day+" hour="+hour+" min="+min );
                }else{
                    day = (interval/(3600*24) );
                    hour = (interval-day*3600*24)/3600;
                    min = (interval-day*3600*24-hour*3600)/60;
System.out.println("[3600*24, 无穷大], day="+day+" hour="+hour+" min="+min );
                }
            }
        }

        Integer[] items = new Integer[3];
        items[0] = new Integer( min );
        items[1] = new Integer( hour );
        items[2] = new Integer( day );

        return items;
    }

    public static String getIntervalString( int interval ){
        Integer[] items = MirrorGrp.getDayOfIntervalTime( interval );
        return  items[2] + SanBootView.res.getString("EditProfileDialog.label.day")+" "+
                items[1]+ SanBootView.res.getString("EditProfileDialog.label.hour")+" "+
                items[0] + SanBootView.res.getString("EditProfileDialog.label.min");
    }

    public String getSchString(){
        if( this.isRotateType() ){
            return MirrorGrp.getIntervalString( this.mg_interval_time );
        }else{
            return this.getCronSchString();
        }
    }

    public int getMg_min_snap_size() {
        return mg_min_snap_size;
    }

    public void setMg_min_snap_size(int mg_min_snap_size) {
        this.mg_min_snap_size = mg_min_snap_size;
    }

    public int getMg_max_snapshot() {
        return mg_max_snapshot;
    }

    public void setMg_max_snapshot(int mg_max_snapshot) {
        this.mg_max_snapshot = mg_max_snapshot;
    }

    public int getMg_pid() {
        return mg_pid;
    }

    public void setMg_pid(int mg_pid) {
        this.mg_pid = mg_pid;
    }

    public String getMg_before_cmd() {
        return mg_before_cmd;
    }

    public void setMg_before_cmd(String mg_before_cmd) {
        this.mg_before_cmd = mg_before_cmd;
    }

    public String getMg_after_cmd() {
        return mg_after_cmd;
    }

    public void setMg_after_cmd(String mg_after_cmd) {
        this.mg_after_cmd = mg_after_cmd;
    }

    public String getMg_desc() {
        return mg_desc;
    }

    public void setMg_desc(String mg_desc) {
        this.mg_desc = mg_desc;
    }

    /**
     * @return the mg_schedule_type
     */
    public int getMg_schedule_type() {
        return mg_schedule_type;
    }
    public boolean isRotateType(){
        return mg_schedule_type == MirrorGrp.MG_SCH_TYPE_ROTATE ;
    }
    public boolean isCronType(){
        return mg_schedule_type == MirrorGrp.MG_SCH_TYPE_CROND;
    }
    public boolean isMultiOccurType(){
        return mg_schedule_type == MirrorGrp.MG_SCH_TYPE_MCLOCK;
    }

    /**
     * @param mg_schedule_type the mg_schedule_type to set
     */
    public void setMg_schedule_type(int mg_schedule_type) {
        this.mg_schedule_type = mg_schedule_type;
    }

    /**
     * @return the mg_schedule_minute
     */
    public String getMg_schedule_minute() {
        return mg_schedule_minute;
    }
    public int getIntMg_schedule_minute(){
        try{
            return Integer.parseInt( mg_schedule_minute );
        }catch(Exception ex){
            return 0;
        }
    }

    /**
     * @param mg_schedule_minute the mg_schedule_minute to set
     */
    public void setMg_schedule_minute(String mg_schedule_minute) {
        this.mg_schedule_minute = mg_schedule_minute;
    }

    /**
     * @return the mg_schedule_hour
     */
    public String getMg_schedule_hour() {
        return mg_schedule_hour;
    }

    /**
     * @param mg_schedule_hour the mg_schedule_hour to set
     */
    public void setMg_schedule_hour(String mg_schedule_hour) {
        this.mg_schedule_hour = mg_schedule_hour;
    }

    /**
     * @return the mg_schedule_day
     */
    public String getMg_schedule_day() {
        return mg_schedule_day;
    }

    /**
     * @param mg_schedule_day the mg_schedule_day to set
     */
    public void setMg_schedule_day(String mg_schedule_day) {
        this.mg_schedule_day = mg_schedule_day;
    }

    /**
     * @return the mg_schedule_month
     */
    public String getMg_schedule_month() {
        return mg_schedule_month;
    }

    /**
     * @param mg_schedule_month the mg_schedule_month to set
     */
    public void setMg_schedule_month(String mg_schedule_month) {
        this.mg_schedule_month = mg_schedule_month;
    }

    /**
     * @return the mg_schedule_week
     */
    public String getMg_schedule_week() {
        return mg_schedule_week;
    }

    /**
     * @param mg_schedule_week the mg_schedule_week to set
     */
    public void setMg_schedule_week(String mg_schedule_week) {
        this.mg_schedule_week = mg_schedule_week;
    }

    public String getMg_log_max_size() {
        return mg_log_max_size;
    }

    public String getMg_log_max_time() {
        return mg_log_max_time;
    }
    
    

    public String getTimeStr(){
        StringBuffer buf = new StringBuffer();
        buf.append( this.mg_schedule_minute );
        buf.append( " " );
        buf.append( this.mg_schedule_hour1 );
        buf.append( " " );
        buf.append( this.mg_schedule_day );
        buf.append( " " );
        buf.append( this.mg_schedule_month  );
        buf.append( " " );
        buf.append( this.mg_schedule_week );
        return buf.toString();
    }

    public String getCronSchString(){
        StringBuffer buf = new StringBuffer();
        String cronSchString = this.getTimeStr();
        int freq_type = CronSchedule.getCronSchedulerType( cronSchString );         // day,week or month
        
        if( freq_type == CronSchedule.TYPE_DAILY ){
            buf.append( SanBootView.res.getString("Strategy.period.day") );
        }else if( freq_type == CronSchedule.TYPE_WEEKLY ){
            buf.append( SanBootView.res.getString("Strategy.period.week1") );
            buf.append( this.getWeekDay() );
        }else{
            buf.append( SanBootView.res.getString("Strategy.period.month1") );
            buf.append(" ");
            buf.append( this.mg_schedule_day );
            buf.append(" ");
            buf.append( SanBootView.res.getString("ReportConfDialog.label.days") );
        }
        buf.append(", ");

        if( this.isCronType() ){
            int daily_type = CronSchedule.getDailyFreqOfCronScheduler( cronSchString );   // once or freq
            int freq_daily_type = CronSchedule.getDailyFreqOccurType( cronSchString );  // freq unit is minute or hour
            String freq_daily_time = ( freq_daily_type == CronSchedule.TYPE_DAILY_MINUTE_OCR )?this.getMinuteFreqVal()+" "+
                    SanBootView.res.getString("common.minutes"):this.getHourFreqVal()+" "+SanBootView.res.getString("common.hours");
            String clock_zone = this.mg_schedule_clock_zone.equals("")?"":", "+SanBootView.res.getString("common.clockZoneCtrl")+" "+this.mg_schedule_clock_zone;
            
            if( daily_type == CronSchedule.TYPE_DAILY_ONCE ){
                buf.append( this.mg_schedule_hour1 );
                buf.append( ":" );
                buf.append( this.mg_schedule_minute );
            }else{
                buf.append( SanBootView.res.getString("Strategy.period.every") );
                buf.append(" ");
                buf.append( freq_daily_time );
                buf.append(" ");
                buf.append( clock_zone );
            }
        }else{ // multi occur
            buf.append( this.mg_schedule_clock_set );
        }
        return buf.toString();
    }

    public Vector<Integer> getMonthdayList(){
        Vector<Integer> ret = new Vector<Integer>();

        String[] list = Pattern.compile(",").split( this.mg_schedule_day.trim() );
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

        String[] list = Pattern.compile(",").split( this.mg_schedule_week.trim() );
        for( int i=0;i<list.length;i++ ){
            try{
                int wday = Integer.parseInt( list[i] );
                ret.addElement( new Integer( wday ) );
            }catch(Exception ex){}
        }

        return ret;
    }

    public Vector<Integer> getClockZoneList(){
        Vector<Integer> ret  = new Vector<Integer>();

        String[] list = Pattern.compile(",").split( this.mg_schedule_clock_zone.trim() );
        for( int i=0;i<list.length;i++ ){
            try{
                int clock = Integer.parseInt( list[i] );
                ret.addElement( new Integer( clock ) );
            }catch(Exception ex){}
        }

        return ret;
    }

    private String getWeekDay(){
        StringBuffer buf = new StringBuffer();
        boolean isFirst = true,hasSunDay=false;

        String[] list = Pattern.compile(",").split( this.mg_schedule_week.trim() );
        for( int i=0;i<list.length;i++ ){
            try{
                int wday = Integer.parseInt( list[i] );
                if( wday == 1 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.monday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.monday") );
                    }
                }
                if( wday == 2 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.tuesday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.tuesday") );
                    }
                }
                if( wday == 3 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.wednesday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.wednesday") );
                    }
                }
                if( wday == 4 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.thursday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.thursday") );
                    }
                }
                if( wday == 5 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.friday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.friday") );
                    }
                }
                if( wday == 6 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.saturday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.saturday") );
                    }
                }
                if( wday == 0 ){
                    hasSunDay = true;
                }
            }catch(Exception ex){}
        }
        if( hasSunDay ){
            if( isFirst ){
                buf.append( SanBootView.res.getString("WeeklyPane.checkBox.sunday") );
                isFirst = false;
            }else{
                buf.append(", ");
                buf.append( SanBootView.res.getString("WeeklyPane.checkBox.sunday") );
            }
        }
        return buf.toString();
    }

    private int getFrequenceVal( String freq ){
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

    public int getHourFreqVal(){
        SplitString sp = new SplitString( this.getTimeStr() );
        sp.getNextToken();
        return getFrequenceVal( sp.getNextToken() );
    }

    public int getMinuteFreqVal(){
        SplitString sp = new SplitString( this.getTimeStr() );
        return getFrequenceVal( sp.getNextToken() );
    }
    
    /**
     * @return the mg_schedule_clock_zone
     */
    public String getMg_schedule_clock_zone() {
        return mg_schedule_clock_zone;
    }
    public boolean hasClockZone(){
        return !mg_schedule_clock_zone.equals("");
    }

    /**
     * @param mg_schedule_clock_zone the mg_schedule_clock_zone to set
     */
    public void setMg_schedule_clock_zone(String mg_schedule_clock_zone) {
        this.mg_schedule_clock_zone = mg_schedule_clock_zone;
    }

    /**
     * @return the mg_schedule_hour1
     */
    public String getMg_schedule_hour1() {
        return mg_schedule_hour1;
    }
    public int getIntMg_schedule_hour1(){
        try{
            return Integer.parseInt( mg_schedule_hour1 );
        }catch(Exception ex){
            return 0;
        }
    }

    public String getMg_schedule_clock_set(){
        return this.mg_schedule_clock_set;
    }
    public void setMg_schedule_clock_set( String val ){
        this.mg_schedule_clock_set = val;
    }
    
    public ArrayList<Clock> getMultiClock(){
        ArrayList<Clock> ret = new ArrayList<Clock>();
        String[] lines = Pattern.compile(";").split( mg_schedule_clock_set,-1 );
        for( int i=0; i<lines.length; i++ ){
            if( !lines[i].equals("") ){
                int indx = lines[i].indexOf(":");
                if( indx >= 0 ){
                    Clock clock = new Clock();
                    clock.setHour( lines[i].substring( 0,indx ) );
                    clock.setMin( lines[i].substring( indx+1));
                    ret.add( clock );
                }
            }
        }
        return ret;
    }
    
    /**
     * @param mg_schedule_hour1 the mg_schedule_hour1 to set
     */
    public void setMg_schedule_hour1(String mg_schedule_hour1) {
        this.mg_schedule_hour1 = mg_schedule_hour1;
    }
}
