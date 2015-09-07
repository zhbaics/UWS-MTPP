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

public class JDK14LoggingTest { 
  /**
 日志记录器
  */

  Logger log = Logger.getLogger(getClass().getName());

  /**
  日志输出对象
   */

  Handler fileHandler = null;

  /**
   * 构造方法
   */

  public JDK14LoggingTest() {

    //设置输出到文件
    log.setLevel(Level.INFO);//this set the log level to info. that means you can only pring info message to log file.
    Handler[] handlers = log.getHandlers();
    for (int i = 0; i < handlers.length; i++) {
        log.removeHandler(handlers[i]);
    }

    try {
       
        fileHandler = new DefaultHandler("logtest");
        
        //log.addHandler(); //class "DefaultHandler" is the default handler for log records. It currently only logs to a file in the format offered by TextFormatter.

 //     fileHandler = new FileHandler("%h/java%u.log");

//      fileHandler = new FileHandler("%h/java%u.log",10,5,true);

//      设置记录级别

//     fileHandler.setLevel(Level.WARNING);
      fileHandler.setLevel(Level.INFO);

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

 

  public void testLog(){

    log.info(getClass().getName());
    log.severe( "this a eror");
    log.log(Level.WARNING,getClass().getName(),"testLog");

  }

 
  public static void main(String[] args) {

    JDK14LoggingTest test = new JDK14LoggingTest();

    test.testLog();

  }
}

