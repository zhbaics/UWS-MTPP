package guisanboot.datadup.data;

import guisanboot.ui.SanBootView;
import java.io.*;
import java.util.Vector;
import java.util.regex.*;

import mylib.tool.*;

public class CronSchedule extends AbstractSchedule { 
    public final static int TYPE_DAILY   = 0;
    public final static int TYPE_WEEKLY  = 1;
    public final static int TYPE_MONTHLY = 2;

    public final static int TYPE_DAILY_FREQ = 0;
    public final static int TYPE_DAILY_ONCE = 1;

    public final static int TYPE_DAILY_MINUTE_OCR = 0;
    public final static int TYPE_DAILY_HOUR_OCR = 1;

    private static String lastScheduleName = null;
    static Reader reader = null;
    static Writer writer = null;

    public CronSchedule(String name,String time,String cmd) {
        super(name,time,cmd);
    }

    public static CronSchedule parse(String line) throws InvalidScheduleException {
        if( line == null || line.length() == 0 )
            return null;

        if( isComment(line) && isScheduleTag(line) ){
            lastScheduleName = getScheduleName(line);
            return null;
        } else if( isComment(line) )
            return null;

        String[] fields = splitLine(line);
        if( fields.length < 6 )
            throw new InvalidScheduleException("Invalid schedule line -> "+line);

        String tStr = fields[0]+" "+fields[1]+" "+fields[2]+" "+fields[3]+" "+fields[4];
        String cStr = fields[5];
        for( int i=6;i<fields.length;i++ )
            cStr += " "+fields[i];

        return new CronSchedule(lastScheduleName,tStr,cStr);
    }

    public void save(Writer writer) throws IOException,InvalidScheduleException {
        if( name == null )
            throw new InvalidScheduleException("There isn't a Schedule Name");
        if( timeStr == null )
            throw new InvalidScheduleException("There isn't a Time String");
        if( cmd == null )
            throw new InvalidScheduleException("There isn't a command");

        saveScheduleTag(writer);
        writer.write(timeStr+" "+cmd);
        writer.write("\n");
    }

    public boolean delete(){
        return true;
    }

    public void setFile(File _file){
    }

    public boolean isOnce(){
        return false;
    }

    //////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    public String getStartTime(){
        int type = CronSchedule.getDailyFreqOfCronScheduler( timeStr );
        if( type == CronSchedule.TYPE_DAILY_FREQ ){
            type = CronSchedule.getDailyFreqOccurType( timeStr );
        if( type == CronSchedule.TYPE_DAILY_HOUR_OCR ){
            return "Every "+ getHourFreqVal()+" Hour(s)";
        }else
            return "Every "+ getMinuteFreqVal()+" Minute(s)";
        }else{
            return getHourVal()+":"+getMinuteVal();
        }
    }

    public static int getCronSchedulerType(String time){
SanBootView.log.debug( "CronSchedule class", " time: "+ time );

        SplitString sp = new SplitString( time );
        sp.getNextToken();  // minute
        sp.getNextToken();  // hour

        String dayMon = sp.getNextToken();
        String mon = sp.getNextToken();
        String dayweek = sp.getNextToken();
        if( dayMon.equals("*") && mon.equals("*") && dayweek.equals("*")){
            return CronSchedule.TYPE_DAILY;
        }else if( dayMon.equals("*") && mon.equals("*")&&!dayweek.equals("") ){
            return CronSchedule.TYPE_WEEKLY;
        }else{
            return CronSchedule.TYPE_MONTHLY;
        }
    }
   
    public static int getDailyFreqOfCronScheduler( String time ){
        SplitString sp = new SplitString( time );
        String minute = sp.getNextToken();
        String hour   = sp.getNextToken();

        return CronSchedule.getDailyFreqOfCron( minute,hour );
    }

    public static int getDailyFreqOfCron( String minute,String hour ){
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher( minute );
        boolean isMDigital = matcher.matches();

        matcher = pattern.matcher( hour );
        boolean isHDigital = matcher.matches();

        if( isMDigital && isHDigital  ){
            return CronSchedule.TYPE_DAILY_ONCE;
        }else{
            return CronSchedule.TYPE_DAILY_FREQ;
        }
    }

    public static int getDailyFreqOccurType(String time){
        SplitString sp = new SplitString( time );
        String minute = sp.getNextToken();
        String hour = sp.getNextToken();

        if( (minute.indexOf("*")>=0 || minute.indexOf(",")>=0) && hour.indexOf("*")>=0 ){
            return CronSchedule.TYPE_DAILY_MINUTE_OCR;
        }else{
            return CronSchedule.TYPE_DAILY_HOUR_OCR;
        }
    }

    private String getFrequenceVal( String freq ){
        if( freq.indexOf(",")>=0 ){
            String[] list = Pattern.compile(",").split( freq );
            return list[1];
        }else{
            return 1+"";
        }
    }

    public String getHourFreqVal(){
        SplitString sp = new SplitString(timeStr);
        sp.getNextToken();
        return getFrequenceVal( sp.getNextToken() );
    }

    public String getMinuteFreqVal(){
        SplitString sp = new SplitString( timeStr );
        return getFrequenceVal( sp.getNextToken() );
    }

    public String getHourVal(){
        SplitString sp = new SplitString( timeStr );
        sp.getNextToken();
        return sp.getNextToken();
    }

    public String getMinuteVal(){
        SplitString sp = new SplitString( timeStr );
        return sp.getNextToken();
    }

    public Vector getMonthdayList(){
        SplitString sp = new SplitString( timeStr );
        sp.getNextToken();
        sp.getNextToken();
        String daymonth = sp.getNextToken();

        Vector<Integer> ret = new Vector<Integer>();

        String[] list = Pattern.compile(",").split( daymonth.trim() );
        for( int i=0;i<list.length;i++ ){
          try{
            int mday = Integer.parseInt( list[i] );
            ret.addElement( new Integer( mday ) );
          }catch(Exception ex){}
        }

        return ret;
    }

    public Vector getWeekdayList(){
        SplitString sp = new SplitString( timeStr );
        sp.getNextToken();
        sp.getNextToken();
        sp.getNextToken();
        sp.getNextToken();
        String dayweek = sp.getNextToken();
        
        Vector<Integer> ret  = new Vector<Integer>();
        String[] list = Pattern.compile(",").split( dayweek.trim() );
        for( int i=0;i<list.length;i++ ){
            try{
                int wday = Integer.parseInt( list[i] );
                ret.addElement( new Integer( wday ) );
            }catch(Exception ex){}
        }

        return ret;
    }
}
