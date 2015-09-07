/*
 * BakTask.java
 *
 * Created on Aug 11, 2008, 14:55 PM
 */

package guisanboot.datadup.data;

import javax.swing.*;

import mylib.UI.TableRevealable;
import mylib.tool.*;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author  Administrator
 */
public class BakTask implements TableRevealable{
    public static final String TASK_TYPE_CLEAN  = "CLEAN";
    public static final String TASK_TYPE_BKDATA = "BKDATA";
    public static final String TASK_TYPE_DELOLD = "DELOLD";
    
    public static final String BAKTASKRECFLAG = "Record:";
    public static final String BAKTASKID      = "task_id";
    public static final String BAKTASKPID     = "task_pid";
    public static final String BAKTASKTYPE    = "task_type";
    public static final String BAKTASKBAKSET  = "task_backupset";
    public static final String BAKTASKSTIME   = "task_starttime";
    public static final String BAKTASKSTATUS  = "task_status";
    public static final String BAKTASKMSG     = "task_msg";
    public static final String BAKTASKLOGFILE = "task_logfile";
    public static final String BAKTASKSCHID   = "task_schid";
    public static final String BAKTASKPROFID  = "task_profid";
    public static final String BAKTASKCLINAME = "task_client";
    public static final String BAKTASKPROFILE = "task_profile";
    
    public static final String TSK_SUMMARY_TskID ="TaskerID";
    public static final String TSK_SUMMARY_AGENTID = "AgentID";
    public static final String TSK_SUMMARY_Type = "Type";
    public static final String TSK_SUMMARY_CltSta ="Client Status";
    public static final String TSK_SUMMARY_HName = "HostName";
    public static final String TSK_SUMMARY_OS = "Os";
    public static final String TSK_SUMMARY_Arch ="Arch";
    public static final String TSK_SUMMARY_BakSrc = "Mirror Source";
    public static final String TSK_SUMMARY_STime = "Start Time";
    public static final String TSK_SUMMARY_STime1 = "Agent Start Time";
    public static final String TSK_SUMMARY_ETime = "Elapsed Time";
    public static final String TSK_SUMMARY_Files = "Files";
    public static final String TSK_SUMMARY_TData = "Total Data";
    public static final String TSK_SUMMARY_SVol = "Splited Volumes";
    public static final String TSK_SUMMARY_RDays = "Retention Days";
    public static final String TSK_SUMMARY_AvSpeed="Average Speed";
    
    private long id = -1L;
    private int pid = -1;
    private String type ="";
    private long bakSet = -1;
    private String starttime="";
    private String status="";
    private String msg ="";
    private String logfile="";
    private long  schid = -1L;
    private long  profid = -1L;
    private String cliName = SanBootView.res.getString("common.unknown");
    private String profileName="";
    
    /** Creates a new instance of BakTask */
    public BakTask(){
    }
    
    public BakTask(
        long _id,
        int _pid,
        String _type,
        long _bakSet,
        String _starttime,
        String _status,
        String _msg,
        String _logfile,
        long _schid,
        long _profid,
        String _cliName,
        String _profileName
    ) {
        id = _id;
        pid = _pid;
        type = _type;
        bakSet = _bakSet;
        starttime = _starttime;
        status = _status;
        msg = _msg;
        logfile = _logfile;
        schid = _schid;
        profid = _profid;
        cliName = _cliName;
        profileName = _profileName;
    }
    
    public long getID(){
        return id;
    }
    public void setID(long _id){
        id = _id;
    }
    
    public int getPid(){
        return pid;
    }
    public void setPid(int _pid){
        pid = _pid;
    }
    
    public String getTaskType(){
        return type;
    }
    public void setTaskType(String _type){
        type = _type;
    }
    public boolean isNormalTask(){
        return type.toUpperCase().startsWith("BACKUP") || 
                type.toUpperCase().startsWith("RESTORE") ||
                type.equals("");
    }
    public String getTaskTypeStr(){
        if( type.toUpperCase().startsWith("BACKUP") ){
            return SanBootView.res.getString("common.cmd.dup");
        }else if( type.toUpperCase().startsWith("RESTORE") ){
            return SanBootView.res.getString("common.cmd.anti-dup");
        }else{
            return type;
        }
    }
    
    public long getBakSet(){
        return bakSet;
    }
    public void setBakSet(long _bakSet){
        bakSet = _bakSet;
    }
    
    public String getStarttime(){
        return starttime;
    }
    public void setStarttime(String _stime){
        starttime = _stime;
    }
    
    public String getStarttimeStr(){
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
    
    public Day getDayObj(){
        int year  = this.getYear();
        int month = this.getMonth();
        int day   = this.getDay();
        
        if( year == -1 || month == -1 || day == -1 ){
            return null;
        }else{
            return new Day( year,month,day );
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
    
    public boolean isStatusEnd(){
        return status.equals("END");
    }
    
    public boolean isEndStatus(){
        Pattern pattern = Pattern.compile( "MIRROR\\s*JOB\\s*END" );
        Matcher matcher = pattern.matcher( msg.toUpperCase() );
        return matcher.find();
    }
    
    public boolean isQueueStatus(){
        return status.equals("QUEUED");
    }
    
    public boolean isFailedStatus(){
        return status.equals("FAILED");
    }
    
    public boolean isAbortStatus(){
        return status.equals("ABORT");
    }
    
    public boolean isActiveStatus(){
        return status.toUpperCase().equals("ACTIVE");
    }
    
    public boolean isFinished(){
        return isEndStatus() || isFailedStatus() || isAbortStatus();
    }
    
    public String getMsg(){
        return msg;
    }
    public void setMsg(String _msg){
        msg = _msg;
    }
  
    public String getLogFile(){
        return logfile;
    }
    public void setLogFile(String _logfile){
        logfile = _logfile;
    } 
    
    public long getSchID(){
        return schid;
    }
    public void setSchID(long _schid){
        schid = _schid;
    }
    
    public long getProfID(){
        return profid;
    }
    public void setProfID(long _profid){
        profid = _profid;
    }
    
    public String getClientName(){
        return cliName;
    }
    public void setClientName(String _cliName){
        cliName = _cliName;
    }
    
    public String getProfileName(){
        return profileName;
    }
    public void setProfileName( String profileName ){
        this.profileName = profileName;
    }
    
    @Override public String toString(){
        return id+"";
    }
    
    // TableRevealable的实现
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
        return ResourceCenter.ICON_ORPHAN_VOL;
    }
    public String toTableString(){
        return id+"";
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append("task_id="+this.getID()+"\n" );
        buf.append("task_msg="+this.getMsg()+"\n");
        buf.append("task_status="+this.getStatus()+"\n");
        buf.append("task_profile="+this.getProfileName()+"\n");
        return buf.toString();
    }
}
