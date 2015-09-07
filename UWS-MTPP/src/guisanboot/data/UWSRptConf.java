/*
 * UWSRptConf.java
 *
 * Created on 2007/5/21, PM�5:11
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import java.util.regex.*;
import java.util.Vector;
/**
 *
 * @author Administrator
 */
public class UWSRptConf {
    public final static String RPT_MON_UWS      = "monitor_uws";
    public final static String RPT_SEND_MAIL    = "send_mail";
    public final static String RPT_MAILTO       = "mail_to";
    public final static String RPT_SEND_WHEN_ERR= "ok_email_send";
    public final static String RPT_SEND_TIME    = "mail_send_time";
    public final static String RPT_SEND_FREQ    = "report_email_period";
    public final static String RPT_REMAIN_DAYS  = "remain_email_days";
    public final static String RPT_OK_REC_NUM   = "successful_records";
    public final static String RPT_FAILED_REC_NUM = "failed_records";
    
    public final static int TYPE_FREQ_WEEKLY  = 1;
    public final static int TYPE_FREQ_MONTHLY = 2 ;
    public final static int TYPE_FREQ_DAILY   = 3;
    
    private boolean monitor_uws = true;
    private boolean send_mail = true;
    private String mailto="";
    private boolean sendWhenErrOcured=true;
    private String sendtime="170000";
    
    // 1 is week; 2 is month; 3 is day
    private String send_freq = "3";
    private int remain_days = 10;
    
    private int ok_rec_num = 20;
    private int failed_rec_num = 50;
    
    /** Creates a new instance of UWSRptConf */
    public UWSRptConf() {
    }
    
    public UWSRptConf(
        boolean _monitor_uws,
        boolean _send_mail,
        String _mailto,
        boolean _sendWhenErrOcured,
        String _sendtime,
        String _send_freq,
        int _remain_days,
        int _ok_rec_num,
        int _failed_rec_num
    ){
        monitor_uws = _monitor_uws;
        send_mail = _send_mail;
        mailto = _mailto;
        sendWhenErrOcured = _sendWhenErrOcured;
        sendtime = _sendtime;
        send_freq = _send_freq;
        remain_days =_remain_days;
        ok_rec_num = _ok_rec_num;
        failed_rec_num = _failed_rec_num;
    }
    
    public boolean isMonUWS(){
        return monitor_uws;
    }
    public void setMonUWSFlag( boolean val ){
        monitor_uws = val;
    }
    
    public boolean isSendMail(){
        return send_mail;
    }
    public void setSendMailFlag( boolean val ){
        send_mail = val;
    }
    
    public String getMailTo(){
        return mailto;
    }
    public void setMailTo( String val ){
        mailto = val;
    }
    
    public boolean isSendWhenErrOcured(){
        return sendWhenErrOcured;
    }
    public void setSendWhenErrOcuredFlag( boolean val ){
        sendWhenErrOcured = val;
    }
    
    public String getSendTime(){
        return sendtime;
    }
    public void setSendTime( String val ){
        sendtime = val;
    }
    public int getHourFromSendTime(){
        try{
            String _hour = sendtime.substring( 0, 2 );
            int hour = Integer.parseInt( _hour );
            return hour;
        }catch(Exception ex){
            return 17;
        }
    }
    public int getMinuteFromSendTime(){
        try{
            String _min = sendtime.substring(2, 4);
            int min = Integer.parseInt( _min );
            return min;
        }catch(Exception ex){
            return 0;
        }
    }
    
    public String getSendFreq(){
        return send_freq;
    }
    public void setSendFreq( String val ){
        send_freq = val;
    }
    public int getFreqType(){
        try{
            String _type = send_freq.substring( 0,1 );
            int type = Integer.parseInt( _type );
            return type;
        }catch(Exception ex){
            return UWSRptConf.TYPE_FREQ_DAILY;
        }
    }
    public Vector<Integer> getDayList(){
        Vector<Integer> ret = new Vector<Integer>();
        
        int indx = send_freq.indexOf(";");
        if( indx >=0 ){
            String dayList = send_freq.substring( indx+1 );
            String[] list = Pattern.compile(" ").split( dayList );
            for( int i=0; i<list.length; i++ ){
                try{
                    int day = Integer.parseInt( list[i].trim() );
                    ret.addElement( new Integer( day ) );
                }catch(Exception ex){}
            }
        }
        
        return ret;
    }
    
    public int getRemainDays(){
        return remain_days;
    }
    public void setRemainDays( int val ){
        remain_days = val;
    }
    
    public int getOkRecNum(){
        return ok_rec_num;
    }
    public void setOkRecNum( int val ){
        ok_rec_num = val;
    }
    
    public int getFailedRecNum(){
        return failed_rec_num;
    }
    public void setFailedRecNum( int val ){
        failed_rec_num = val;
    }    
    
    public String getContents(){
        StringBuffer buf = new StringBuffer();
        
        buf.append( UWSRptConf.RPT_MON_UWS + "=" + (isMonUWS()?"yes":"no"));
        buf.append( "\n"+ UWSRptConf.RPT_SEND_MAIL+"="+( isSendMail()?"yes":"no") );
        buf.append( "\n"+ UWSRptConf.RPT_MAILTO+"="+getMailTo() );
        buf.append( "\n"+"# send mail only when error occured");
        buf.append( "\n"+UWSRptConf.RPT_SEND_WHEN_ERR+"="+( isSendWhenErrOcured()?"no":"yes") );
        buf.append( "\n"+UWSRptConf.RPT_SEND_TIME+"="+getSendTime());
        buf.append( "\n\n"+"# 1 is week,2 is month,3 is dayily");
        buf.append( "\n"+"# For example,the following means every Tuesday Wednesday send email");
        buf.append( "\n"+"# report_email_period=1;2 3");
        buf.append( "\n"+UWSRptConf.RPT_SEND_FREQ+"="+getSendFreq() );
        buf.append( "\n"+UWSRptConf.RPT_REMAIN_DAYS+"="+getRemainDays());
        buf.append( "\n\n"+"# how many of fivstore records to be send");
        buf.append( "\n"+UWSRptConf.RPT_OK_REC_NUM+"="+getOkRecNum() );
        buf.append( "\n"+UWSRptConf.RPT_FAILED_REC_NUM +"="+getFailedRecNum()+"\n" ); 
        
        return buf.toString();
    }
}