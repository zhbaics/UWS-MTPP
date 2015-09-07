/*
 * TextFormatter.java
 *
 * Created on 2007//10/25, PM 9:27
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.logging;

import java.text.MessageFormat;
import java.util.logging.*;
import java.util.*;

final class TextFormatter extends java.util.logging.Formatter {
    Date dat = new Date();
    private final static String format = "{0,date} {0,time}";
    private MessageFormat formatter;
    private Object args[] = new Object[1];
    
    final public static String lineSeparator = System.getProperties().getProperty("line.separator");

    /**
    * The constructor to use.
    */
    public TextFormatter() {
    }

    /**
    * Formats the given log record's data into human-readable text.
    *
    * @param record The log record whose data needs to be formatted.
    * @return The log record's data as a string.
    */
    public synchronized String format(LogRecord record) {
        String level;
        if (record.getLevel() == Level.INFO) {
            level = " INFO";
        } else if (record.getLevel() == Level.ALL) {
            level = " DEBUG";
        } else if (record.getLevel() == Level.SEVERE) {
            level = " ERROR";
        } else if (record.getLevel() == Level.WARNING) {
            level = " WARNING";
        } else {
            level = " UNKNOWN";
        }
        
        dat.setTime( record.getMillis() );
	args[0] = dat;
	StringBuffer time = new StringBuffer();
	if ( formatter == null ) {
	    formatter = new MessageFormat( format );
	}
	formatter.format( args, time, null );
        String message = formatMessage( record );
        String result = time.toString() + level + ":" + message + lineSeparator;
        return result;
    }
}
