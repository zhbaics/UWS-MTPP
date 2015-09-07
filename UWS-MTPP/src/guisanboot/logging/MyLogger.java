/*
 * Main.java
 *
 * Created on 2007/10/25, PM�8:15
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.logging;

import java.util.logging.*;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;

public class MyLogger {
  /**

 日志记录器

  */
    public String logFilelocation;

    private int curLevel;

    Logger log = Logger.getLogger(getClass().getName());

 

  /**

  日志输出对象

   */

    Handler fileHandler = null;

 

  /**

   * 构造方法

   */

  public MyLogger( int level ) {

    //设置输出到文件
    Level le = MyLogger.getLogLevel( level );
    log.setLevel( le );//this set the log level to info. that means you can only pring info message to log file.
    curLevel = level;

    Handler[] handlers = log.getHandlers();
    for (int i = 0; i < handlers.length; i++) {
        log.removeHandler(handlers[i]);
    }

    try {
       
        fileHandler = new DefaultHandler("UWS-GUI");
        logFilelocation = ((DefaultHandler)fileHandler).getLogFileLocation();
        //log.addHandler(); //class "DefaultHandler" is the default handler for log records. It currently only logs to a file in the format offered by TextFormatter.

 //     fileHandler = new FileHandler("%h/java%u.log");

//      fileHandler = new FileHandler("%h/java%u.log",10,5,true);

//      设置记录级别

//     fileHandler.setLevel(Level.WARNING);
      fileHandler.setLevel( le );

//     设置过滤器,已达到更细粒度的记录

//      fileHandler.setFilter();

 

//      设置输出格式,有两种类型:SimpleFormatter和XMLFormatter,

//      默认是XMLFormatter,当然自己也可以扩张,只要继承Formatter类就可以了

     //fileHandler.setFormatter(new SimpleFormatter());

//      指定日志编码格式

//      fileHandler.setEncoding("");

//    ���设置错误处理

//      fileHandler.setErrorManager(new ErrorManager());

    }

    catch (Exception ex) {

      ex.printStackTrace();

    }

   log.addHandler(fileHandler);

  }

   public void changeLevel( int level ){
        if( curLevel == level ) return;
        
        synchronized( this ){
            Level le = MyLogger.getLogLevel( level );
            log.setLevel( le);
            fileHandler.setLevel( le );
            curLevel = level;
        }
    }

  public void debug( String classname,String contents ){
        if( this.fileHandler != null ){
            try{
                log.log( Level.ALL,"["+classname+"] "+contents );
            }catch(Exception ex){
            }
        }
    }

    public void info( String classname,String contents ){
        if( this.fileHandler != null ){
            try{
                log.log( Level.INFO,"["+classname+"] "+contents );
            }catch(Exception ex){
            }
        }
    }

    public void warning(String classname,String contents ){
        if( this.fileHandler != null ){
            try{
                log.log( Level.WARNING,"["+classname+"] "+contents );
            }catch(Exception ex){
            }
        }
    }

    public void error(String classname,String contents ){
        if( this.fileHandler != null ){
            try{
                log.log( Level.SEVERE,"["+classname+"] "+contents );
            }catch(Exception ex){
            }
        }
    }

  public void log( String classname,String msg ){
      log.info( msg );
  }

  public void testLog(){
    log.info(getClass().getName());
    log.severe( "this a eror");
    log.log(Level.WARNING,getClass().getName(),"testLog");
  }

  public void prtLog( int level,String classname,String contents ){
        switch( level ){
            case ResourceCenter.LOG_LEVEL_DEBUG:
                this.debug(classname, contents );
                break;
            case ResourceCenter.LOG_LEVEL_INFO:
                this.info(classname, contents );
                break;
            case ResourceCenter.LOG_LEVEL_WARNING:
                this.warning( classname,contents );
                break;
            case ResourceCenter.LOG_LEVEL_SEVERE:
                this.error(classname, contents);
                break;
            default:
                this.info(classname, contents);
                break;
        }
    }

    public boolean isDebugLevel(){
        return ( this.curLevel == ResourceCenter.LOG_LEVEL_DEBUG );
    }
    
    public static Level getLogLevel( int intLevel ){
        Level level;

        switch( intLevel ){
            case ResourceCenter.LOG_LEVEL_DEBUG: // debug level,打印全部日志（细节）
                level = Level.ALL;
                break;
            case ResourceCenter.LOG_LEVEL_INFO: // info level,过程日志
                level = Level.INFO;
                break;
            case ResourceCenter.LOG_LEVEL_WARNING: // warning level,肯定发生错误，但是还可以容忍
                level = Level.WARNING;
                break;
            case ResourceCenter.LOG_LEVEL_SEVERE: // error level,发生不可容忍的错误，程序可能需要退出
                level = Level.SEVERE;
                break;
            default:
                level = Level.INFO;
                break;
        }

        return level;
    }

    public static String getLogLevelStr( int intLevel ){
        String str;

        switch( intLevel ){
            case ResourceCenter.LOG_LEVEL_DEBUG: // debug level,打印全部日志（细节）
                str = SanBootView.res.getString("common.loglevel.debug");
                break;
            case ResourceCenter.LOG_LEVEL_INFO: // info level,过程日志
                str = SanBootView.res.getString("common.loglevel.info");
                break;
            case ResourceCenter.LOG_LEVEL_WARNING: // warning level,肯定发生错误，但是还可以容忍
                str = SanBootView.res.getString("common.loglevel.warning");
                break;
            case ResourceCenter.LOG_LEVEL_SEVERE: // error level,发生不可容忍的错误，程序可能需要退出
                str = SanBootView.res.getString("common.loglevel.error");
                break;
            default:
                str = SanBootView.res.getString("common.loglevel.info");
                break;
        }

        return str;
    }

    public static int getLogLevel( String levelStr ){
        int level;

        if( levelStr.equals(SanBootView.res.getString("common.loglevel.debug") )){
            level = ResourceCenter.LOG_LEVEL_DEBUG;
        }else if( levelStr.equals(SanBootView.res.getString("common.loglevel.info") )){
            level = ResourceCenter.LOG_LEVEL_INFO;
        }else if( levelStr.equals(SanBootView.res.getString("common.loglevel.warning"))){
            level = ResourceCenter.LOG_LEVEL_WARNING;
        }else if( levelStr.equals(SanBootView.res.getString("common.loglevel.error"))){
            level = ResourceCenter.LOG_LEVEL_SEVERE;
        }else{
            level = ResourceCenter.LOG_LEVEL_INFO;
        }

        return level;
    }
 

  public static void main(String[] args) {

    MyLogger test = new MyLogger(1);

    test.testLog();

  }
}

