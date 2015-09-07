package guisanboot.datadup.data;

import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import mylib.tool.*;
import guisanboot.ui.SanBootView;

public abstract class AbstractSchedule {
  public final static String SCHED_TYPE_ONCE = "AbstractSchedule.once";
  public final static String SCHED_TYPE_FREQ = "AbstractSchedule.freq";

  public static String NAMEPREFIX = "#OdysysSchedule";
  protected String name = null;
  protected String cmd = null;
  protected String timeStr = null;
  protected Date time = null;
  protected boolean modified = false;
  protected boolean commited = true;
  
  public AbstractSchedule(String _name,String _cmd) {
    name = _name;
    cmd = _cmd;
  }

  public AbstractSchedule(String _name,String _timeStr,String _cmd)
  {
    name = _name;
    timeStr = _timeStr;
    cmd = _cmd;
  }

  abstract public void save(Writer writer) throws IOException,InvalidScheduleException;
  abstract public boolean delete();
  abstract public boolean isOnce();
  abstract public void setFile(File file);

  static public String[] splitLine(String line){
    ArrayList<String> list = new ArrayList<String>();
    int previos = -1,status = 0;
    StringBuffer strBuf = new StringBuffer();
    char ch;
    
    int i = 0;
    while( i<line.length() ){
      while( i<line.length() && Character.isSpaceChar(line.charAt(i)) ) i++;
      while( i<line.length() && !Character.isSpaceChar(line.charAt(i)) ){
        strBuf.append(line.charAt(i));
        i++;
      }
      list.add( strBuf.toString() );
      strBuf = new StringBuffer();
    }

    String[] splitLine = new String[list.size()];
    for( i=0;i<splitLine.length;i++ )
      splitLine[i] = (String)list.get(i);
    return splitLine;
  }

  static public boolean isComment(String line){
    return line.length()>0 && line.charAt(0) == '#';
  }

  static public boolean isScheduleTag(String line){
    return line.startsWith(NAMEPREFIX);
  }

  static public String getScheduleName(String comment){
    return comment.substring(NAMEPREFIX.length()).trim();
  }

  static public String readLine(Reader reader) throws IOException {
    StringBuffer strBuf = new StringBuffer();
    int ch = reader.read();

    while( ch >= 0 && ch != '\n' ){
      strBuf.append((char)ch);
      ch = reader.read();
    }

    if( ch < 0 && strBuf.length() == 0 )
      return null;
    return strBuf.toString().trim();
  }

  public void saveScheduleTag(Writer writer) throws IOException {
    writer.write(NAMEPREFIX+" "+name);
    writer.write("\n");
  }

  synchronized public String getName(){
    return name;
  }

  synchronized public void setName(String _name){
    name = _name;
    modified = true;
    commited = false;
  }

  synchronized public String getTimeStr(){
    return timeStr;
  }

  public void setTimeStr(String _timeStr){
    timeStr = _timeStr;
    modified = true;
    commited = false;
  }

  public Date getTime(){
    return time;
  }

  public void setTime(Date date){
    time = date;
  }

  synchronized public String getCmd(){
    return cmd;
  }

  synchronized public void setCmd(String _cmd){
    cmd = _cmd;
    modified = true;
    commited = false;
  }

  public boolean isModify(){
    return modified;
  }

  public void setModify(boolean mod){
    modified = mod;
  }

  public boolean isCommit(){
    return commited;
  }

  public void setCommit(boolean com){
    commited = com;
  }

  ///////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////
  abstract public String getStartTime();

  public String toString(){
    return this.name;
  }

  public String getBakNodeIP(){
    if( cmd == null || cmd.equals("") ){
        return "";
    }
    
    try{
        SplitString sp = new SplitString( cmd );
            
        // /usr/mesrv/bin/sync <sdir_id> <sync_type> <node_id>
        // <node_ip> <sync_source> <sync_dest>
        String ip = sp.getNextTokenN(4);
        if( ip!=null ){
            return ip;
        }else{
            return "";
        }
    }catch(Exception ex){
        return "";
    }
  }
  
  public String getSyncSrc(){
    if( cmd == null || cmd.equals("") ){
        return "";
    }
    
    try{
        SplitString sp = new SplitString( cmd );
        String src = sp.getNextTokenN(5);
        if( src!=null ){
            return src;
        }else{
            return "";
        }
    }catch(Exception ex){
        return "";
    }
  }
  
  public String getSyncDest(){
    if( cmd == null || cmd.equals("") ){
        return "";
    }
    
    try{
        SplitString sp = new SplitString( cmd );
        String dest = sp.getNextTokenN(6);
        if( dest!=null ){
            return dest;
        }else{
            return "";
        }
    }catch(Exception ex){
        return "";
    }
  }

  public String getSchedulerType(){
    if( isOnce() ){
      return SanBootView.res.getString(
        AbstractSchedule.SCHED_TYPE_ONCE
      );
    }else{
      return SanBootView.res.getString(
        AbstractSchedule.SCHED_TYPE_FREQ
      );
    }
  }
}
