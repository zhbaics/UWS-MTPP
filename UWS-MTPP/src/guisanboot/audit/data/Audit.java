/*
 * Audit.java
 *
 */

package guisanboot.audit.data;

import javax.swing.*;
import mylib.UI.TableRevealable;
import mylib.tool.*;
import guisanboot.res.ResourceCenter;

/**
 *
 * @author  Administrator
 */
public class Audit implements TableRevealable{
    public static final String AUDITRECFLAG   = "Record:";
    public static final String AUDITID        = "audit_id";
    public static final String AUDITUID       = "audit_user_id";
    public static final String AUDITTIME      = "audit_time";
    public static final String AUDITCID       = "audit_clnt_id";
    public static final String AUDITEID       = "audit_event_id";
    public static final String AUDITEVENTDESC = "audit_event_desc";
    
    private int id = -1;
    private int uid = -1;
    private String event_time ="";
    private int cid = -1;
    private int eid = -1;
    private String event_desc ="";
    
    /** Creates a new instance of Audit */
    public Audit(){
    }
    
    public Audit(
        int _uid,
        String _event_time,
        int _cid,
        int _eid,
        String _event_desc
    ) {
        uid = _uid;
        event_time = _event_time;
        cid = _cid;
        eid = _eid;
        event_desc = _event_desc;
    }
    
    public Audit(
        int _id,
        int _uid,
        String _event_time,
        int _cid,
        int _eid,
        String _event_desc
    ) {
        id = _id;
        uid = _uid;
        event_time = _event_time;
        cid = _cid;
        eid = _eid;
        event_desc = _event_desc;
    }
    
    public int getID(){
        return id;
    }
    public void setID( int _id){
        id = _id;
    }
    
    public int getUid(){
        return uid;
    }
    public void setUid(int _uid){
        uid = _uid;
    }
    
    public String getEventtime(){
        return event_time;
    }
    public void setEventtime(String _etime){
        event_time = _etime;
    }
    
    public String getEventtimeStr(){
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
        }catch( Exception ex ){
            ex.printStackTrace();
            return "";
        }
    }
    
    public int getYear(){
        String _year = event_time.substring(0,4);
        try{
            return Integer.parseInt(_year);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getMonth(){
        String _month = event_time.substring(4,6);
        try{
            return Integer.parseInt( _month );
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getDay(){
        String day = event_time.substring(6,8);
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
        String hour = event_time.substring(8,10);
        try{
            return Integer.parseInt(hour);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getMinute(){
        String minute = event_time.substring(10,12);
        try{
            return Integer.parseInt(minute);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getSecond(){
        String second = event_time.substring(12);
        try{
            return Integer.parseInt( second );
        }catch(Exception ex){
            return -1;
        }
    }
    
    public String getEventDesc(){
        return event_desc;
    }
    public void setEventDesc(String _event_desc){
        event_desc = _event_desc;
    }
    
    public int getCID(){
        return cid;
    }
    public void setCID( int _cid ){
        cid = _cid;
    }
    
    public int getEID(){
        return eid;
    }
    public void setEID( int _eid){
        eid = _eid;
    }
    
    @Override public String toString(){
        return id + "";
    }
    
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
        buf.append("event_id="+this.getID()+"\n" );
        buf.append("event_cid="+this.getCID() + "\n" );
        buf.append("event_msg="+this.getEventDesc()+"\n");
        buf.append("event_uid="+this.getUid()+"\n");
        buf.append("event_eid="+this.getEID()+"\n");
        return buf.toString();
    }
}
