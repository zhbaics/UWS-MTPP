/*
 * UniProIdentity.java
 *
 * Created on November 17, 2004, 11:55 AM
 */

package guisanboot.datadup.data;

import java.io.*;
import java.util.*;

/**
 *
 * @author  Administrator
 */
public class UniProIdentity extends AbstractUniProfile {
    private String bkobj_id;
    private String bkobj_sn;
    private String dev_id;
    private String sch_id;
    private String sch_level;
    private String clnt_id;
    private String prof_id;
    private String rstrobj_id;
    private String action;
    private String filename;
    private String target;
    private String bks_retention;
    private String auto_rst_obj_id;
    private String rst_obj_id;
    private String addQueue;
    private String user_info;
    private String mirror_flag; // (mirror_flag=1 表示mirror版本,允许dev_id=0,且不会产生详细文件列表的trace文件)
    
    /** Creates a new instance of UniProIdentity */
    public UniProIdentity() {
        // 故意将dev_id置为2(2为当前真实的dev id)
        this("0","0","0","0","0","0","0","0","0","0","0","","","","0","","1");
    }
    
    public UniProIdentity(
        String _bkobj_id,
        String _bkobj_sn,
        String _dev_id,
        String _sch_id,
        String _sch_level,
        String _clnt_id,
        String _prof_id,
        String _rstrobj_id,
        String _action,
        String _filename,
        String _target,
        String _bks_retention,
        String _auto_rst_obj_id,
        String _rst_obj_id,
        String _addQueue,
        String _user_info,
        String _mirror_flag
    ){
        bkobj_id   = _bkobj_id;
        bkobj_sn   = _bkobj_sn;
        dev_id     = _dev_id;
        sch_id     = _sch_id;
        sch_level  = _sch_level;
        clnt_id    = _clnt_id;
        prof_id    = _prof_id;
        rstrobj_id = _rstrobj_id;
        action     = _action;
        filename   = _filename;
        target     = _target;
        bks_retention = _bks_retention;
        auto_rst_obj_id = _auto_rst_obj_id;
        rst_obj_id = _rst_obj_id;
        addQueue = _addQueue;
        user_info = _user_info;
        mirror_flag = _mirror_flag;
    }
    
    public String getBkObj_ID(){
        return bkobj_id;
    }
    public void setBkObj_ID(String _bkobj_id){
        bkobj_id = _bkobj_id;
    }
    public int getIntBkObj_ID(){
        try{
            int id = Integer.parseInt( bkobj_id );
            return id;
        }catch( Exception ex ){
            return -1;
        }
    }
    
    public String getBkObj_SN(){
        return bkobj_sn;
    }
    public void setBkObj_SN(String _bkobj_sn){
        bkobj_sn = _bkobj_sn;
    }
    
    public String getDevID(){
        return dev_id;
    }
    public void setDevID(String _dev_id){
        dev_id = _dev_id;
    }
   
    public String getSchID(){
        return sch_id;
    }
    public void setSchID(String _sch_id){
        sch_id = _sch_id;
    }
    
    public String getSchLevel(){
        return sch_level;
    }
    public void setSchLevel(String _sch_level){
        sch_level = _sch_level;
    }
    
    public String getClntID(){
        return clnt_id;
    }
    public void setClntID(String _clnt_id){
        clnt_id = _clnt_id;
    }
    
    public String getProfID(){
        return prof_id;
    }
    public void setProfID(String _prof_id){
        prof_id = _prof_id;
    }
   
    // 这个值(rstrobj_id)将填成要恢复的bkset id
    public String getRstrObjID(){
       return rstrobj_id;
    }
    public void setRstrObjID(String _rstrobj_id){
       rstrobj_id = _rstrobj_id;
    }
   
    public String getAction(){
        return action;
    }
    public void setAction(String _action){
        action = _action;
    }
    
    public String getFileName(){
        return filename;
    }
    public void setFileName(String _filename){
        filename = _filename;
    }
    
    public String getTarget(){
       return target;
    }
    public void setTarget(String _target){
       target = _target;
    }
    
    public String getBksRetention(){
        return bks_retention;
    }
    public void setBksRetention(String _bks_retention){
        bks_retention = _bks_retention;
    }
   
    public String getAutoRstObjID(){
        return auto_rst_obj_id;
    }
    public void setAutoRstObjID( String val ){
        auto_rst_obj_id = val;
    }
    
    public String getRstObjID(){
        return rst_obj_id;
    }
    public void setRstObjID( String val ){
        rst_obj_id = val;
    }
    
    public String getAddQueue(){
        return addQueue;
    }
    public void setAddQueue( String val ){
        addQueue = val;
    }
    public boolean isAddQueue(){
        return addQueue.equals("1");
    }
    
    public String getUserInfo(){
        return user_info;
    }
    public void setUserInfo( String val ){
        user_info = val;
    }
    
    public String getMirror_flag(){
        return mirror_flag;
    }
    public void setMirror_flag( String val ){
        mirror_flag = val;
    }
    
    public void outputProfile(OutputStreamWriter out) throws IOException{
        out.write("[identity]\n");
        out.write("bkobj_id="+getBkObj_ID()+"\n");
        out.write("bkobj_sn="+getBkObj_SN()+"\n");
        out.write("dev_id="+getDevID()+"\n");
        out.write("sch_id="+getSchID()+"\n");
        out.write("sch_level="+getSchLevel()+"\n");
        out.write("clnt_id="+getClntID()+"\n");
        out.write("prof_id="+getProfID()+"\n");
        out.write("rstrobj_id="+getRstrObjID()+"\n");
        out.write("action="+getAction()+"\n");
        out.write("filename="+getFileName()+"\n");
        out.write("target="+getTarget()+"\n");
        out.write("bks_retention="+getBksRetention()+"\n");
        out.write("autoresobj_id="+getAutoRstObjID()+"\n");
        out.write("restore_id="+getRstObjID()+"\n");
        out.write("add_queue="+getAddQueue()+"\n");
        out.write("user_info="+getUserInfo()+"\n");
        out.write("mirror_flag="+getMirror_flag()+"\n");
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
       
        buf.append("[identity]\n");
        buf.append("bkobj_id="+getBkObj_ID()+"\n");
        buf.append("bkobj_sn="+getBkObj_SN()+"\n");
        buf.append("dev_id="+getDevID()+"\n");
        buf.append("sch_id="+getSchID()+"\n");
        buf.append("sch_level="+getSchLevel()+"\n");
        buf.append("clnt_id="+getClntID()+"\n");
        buf.append("prof_id="+getProfID()+"\n");
        buf.append("rstrobj_id="+getRstrObjID()+"\n");
        buf.append("action="+getAction()+"\n");
        buf.append("filename="+getFileName()+"\n");
        buf.append("target="+getTarget()+"\n");
        buf.append("bks_retention="+getBksRetention()+"\n");
        buf.append("autoresobj_id="+getAutoRstObjID()+"\n");
        buf.append("restore_id="+getRstObjID()+"\n");
        buf.append("add_queue="+getAddQueue()+"\n");
        buf.append("user_info="+getUserInfo()+"\n");
        buf.append("mirror_flag="+getMirror_flag()+"\n");
        
        return buf.toString();
    }
    
    public int parserProfile( int begin,String[] lines ) throws IllegalArgumentException{
        String line;

        do {
            line = lines[begin];
//System.out.println( "line[identity]: " + line );

            if( line.length() <= 0 ){
                begin++;
                continue;
            }

            if( isTitle( line ) )
                break;

            Vector v = splitLine(line);
            if( v.elementAt(0).equals("bkobj_id") )
                setBkObj_ID((String)v.elementAt(1));
            else if( v.elementAt(0).equals("bkobj_sn") )
                setBkObj_SN( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("dev_id") )
                setDevID( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("sch_id") )
                setSchID( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("sch_level") )
                setSchLevel( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("clnt_id") )
                setClntID( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("prof_id") )
                setProfID( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("rstrobj_id") )
                setRstrObjID( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("action") )
                setAction( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("filename") )
                setFileName( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("target") )
                setTarget( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("bks_retention") )
                setBksRetention( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("autoresobj_id"))
                setAutoRstObjID( (String)v.elementAt(1)  );
            else if( v.elementAt(0).equals("restore_id") )
                setRstObjID( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("add_queue") )
                setAddQueue( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("user_info") )
                setUserInfo( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("mirror_flag"))
                setMirror_flag( (String)v.elementAt(1) );
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [identity]");
            
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
