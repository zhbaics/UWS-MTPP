/*
 * DefaultHandler.java
 *
 * Created on 2007/10/25,��AM�9:25
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.logging;

import guisanboot.ui.InitApp;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.*;

public final class DefaultHandler extends Handler {
    private String fileName;
    private FileWriter fileWriter;
    private File file;
    
    /**
    * The constructor to use.
    * @throws Exception In case the log file could not be created/written to.
    */
    public DefaultHandler( String _fileName ) throws Exception {
        fileName = _fileName;
        
        String logdir = InitApp.getUserWorkDir() + File.separator + "log";
        File logDirFile = new File( logdir );
        if( !logDirFile.exists() ){
            logDirFile.mkdir();
        }
        
        String currentTime = this.getCurrentTime();
        file = new File( logdir + File.separator + fileName + "_" + currentTime + ".log" );
        if ( file.exists() ) {
            file.delete();
        }
        
        try {
            file.createNewFile();
        } catch (IOException e) {            
            throw new Exception("Log file \"" + fileName + "\" could not be created.");
        }
        
        if (!file.canWrite()) {
            throw new Exception("Can not write in log file \"" + fileName + "\".");
        }
        
        // We use TextFormatter that we build latter.
        setFormatter( new TextFormatter() );
    }
    
     /**
    * Closes this handler so that it will stop handling log records.
    */
    public void close() {
        try {
            if( fileWriter != null ){
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
    * Flushes the data that this handler has logged.
    */
    public void flush() {  
        try {
            if( fileWriter != null ){
                fileWriter.flush();
            }
        } catch (IOException e) {
           //  e.printStackTrace();
        }
    }

    public String getLogFileLocation(){
        if( file !=null )
            return file.getAbsolutePath();
        else
            return "";
    }

    /**
    * Publishes the given LogRecord by writing its data to a file using
    * a TextFormatter.
    *
    * @param record The log record to publish.
    */
    public void publish(LogRecord record) {
        if (record.getLevel().intValue() < getLevel().intValue()) {
            return;
        }
        String str = getFormatter().format(record);
        try {
            fileWriter = new FileWriter( file,true );
            fileWriter.write(str, 0, str.length());
        } catch (IOException e) {
            //e.printStackTrace();
        }
        
        flush();
        close();
    }
    
    private String getCurrentTime(){
        GregorianCalendar todaysDate = new GregorianCalendar();
        int year = todaysDate.get(Calendar.YEAR);
        int month = todaysDate.get(Calendar.MONTH) + 1;
        String monthStr;
        if( month<10 ){
            monthStr = "0"+month;
        }else{
            monthStr = ""+month;
        }
        int day = todaysDate.get(Calendar.DAY_OF_MONTH);
        String dayStr;
        if( day < 10 ){
            dayStr = "0"+day;
        }else{
            dayStr = ""+day;
        }
        int hour = todaysDate.get(Calendar.HOUR_OF_DAY );
        String hourStr;
        if( hour < 10 ){
            hourStr = "0"+hour;
        }else{
            hourStr = ""+hour;
        }
        int min = todaysDate.get( Calendar.MINUTE );
        String minStr;
        if( min<10 ){
            minStr ="0"+min;
        } else{
            minStr =""+min;
        }
        int sec = todaysDate.get( Calendar.SECOND );
        String secStr;
        if( sec < 10 ){
            secStr = "0"+sec;
        }else{
            secStr =""+sec;
        }
        String time = year+monthStr+dayStr+hourStr+minStr+secStr;
        return time;
    }
}
