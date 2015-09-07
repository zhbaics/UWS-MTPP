/*
 * UniProDatabase.java
 *
 * Created on 2006/5/8,��PM�3:07
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.datadup.data;

import java.io.*;
import java.util.*;

/**
 *
 * @author Administrator
 */
public class UniProDatabase extends AbstractUniProfile {
    private String rst_conf;
    private String bak_conf;
    private String db_type;
    private String goingon;  // 1: 表示后续还有版本要恢复; 0:表示本次为最后一次恢复
    private String onlyRstData;// 1: 只恢复备份数据，不自动进行DB的恢复； 0： 不仅恢复备份数据，而且自动进行DB的恢复
    private String use_cur_ctlfile; // 1：使用系统当前最新的控制文件; 0: 使用备份的控制文件 该标志只对oracle恢复时有效
    
    
    public UniProDatabase(){
        this("","","","","","");
    }
    
    /** Creates a new instance of UniProDatabase */
    public UniProDatabase( 
        String _rst_conf,
        String _bak_conf,
        String _db_type,
        String _goingon,
        String _onlyRstData,
        String _use_cur_ctlfile
    ) {
        rst_conf = _rst_conf;
        bak_conf = _bak_conf;
        db_type = _db_type;
        goingon = _goingon;
        onlyRstData = _onlyRstData;
        use_cur_ctlfile = _use_cur_ctlfile;
    }
    
    public String getRstConf(){
        return rst_conf;
    }
    public void setRstConf( String val ){
        rst_conf = val;
    }
    
    public String getBakConf(){
        return bak_conf;
    }
    public void setBakConf( String val ){
        bak_conf = val;
    }
    
    public String getDBType(){
        return db_type;
    }
    public void setDBType( String val ){
        db_type = val;
    }
    
    public String getGoingOnFlag(){
        return goingon;
    }
    public void setGoingOnFlag( String val ){
        goingon = val;
    }
    
    public String getOnlyRstFlag(){
        return onlyRstData;
    }
    public void setOnlyRstFlag( String val ){
        onlyRstData = val;
    }
    
    public String getUseCurCtlFileFlag(){
        return use_cur_ctlfile;
    }
    public void setUseCurCtlFileFlag( String val ){
        use_cur_ctlfile = val;
    }
    
    public void outputProfile(OutputStreamWriter out) throws IOException{
        out.write("[database]\n");
        out.write("restore_conf="+getRstConf()+"\n");
        out.write("backup_conf="+getBakConf()+"\n");
        out.write("database_type="+getDBType()+"\n");
        out.write("goingon="+getGoingOnFlag()+"\n");
        out.write("only_rst_data="+getOnlyRstFlag()+"\n");
        out.write("use_cur_ctlfile="+getUseCurCtlFileFlag()+"\n");
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        
        buf.append("[database]\n");
        buf.append("restore_conf="+getRstConf()+"\n");
        buf.append("backup_conf="+getBakConf()+"\n");
        buf.append("database_type="+getDBType()+"\n");
        buf.append("goingon="+getGoingOnFlag()+"\n");
        buf.append("only_rst_data="+getOnlyRstFlag()+"\n");
        buf.append("use_cur_ctlfile="+getUseCurCtlFileFlag()+"\n");
        
        return buf.toString();
    }
    
    public int parserProfile(int begin,String[] lines ) throws IllegalArgumentException{
        String line;

        do {
            line = lines[begin];

            if( line.length() == 0 ){
                begin++;
                continue;
            }

            if( isTitle(line) )
                break;

            Vector v = splitLine(line);
            if( v.elementAt(0).equals("restore_conf") )
                setRstConf( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("backup_conf") )
                setBakConf( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("database_type") )
                setDBType( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("goingon") )
                setGoingOnFlag( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("only_rst_data") )
                setOnlyRstFlag( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("use_cur_ctlfile") )
                setUseCurCtlFileFlag( (String)v.elementAt(1) );
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [database]");
            
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
