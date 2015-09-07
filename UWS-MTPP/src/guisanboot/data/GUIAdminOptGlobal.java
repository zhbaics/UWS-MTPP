/*
 * GUIAdminOptGlobal.java
 *
 * Created on 2008/7/8, PM�5:11
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 *
 * @author zjj
 */
public class GUIAdminOptGlobal extends AbstractConfIO{
    private String lang = "zh";
    private String loglevel = "1";
    private String logFile = "";
    private String cmdp_rst_mirror_type = ResourceCenter.CMDP_RST_MIRROR_INC+""; // 缺省为：增量（2）
    private String global_hight_delta = ResourceCenter.GLOBAL_DELTA_HIGH_SIZE+"";
    private String global_width_delta = ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE+"";

    /** Creates a new instance of GUIAdminOptGlobal */
    public GUIAdminOptGlobal() {
        this( "zh", "1", ResourceCenter.CMDP_RST_MIRROR_INC+"",
            ResourceCenter.GLOBAL_DELTA_HIGH_SIZE+"",
            ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE+""
        );
    }
    
    public GUIAdminOptGlobal(
        String lang,
        String loglevel,
        String rst_mirror_type,
        String global_high_delta,
        String global_width_delta
    ){
        this.lang = lang;
        this.loglevel = loglevel;
        this.cmdp_rst_mirror_type = rst_mirror_type;
        this.global_hight_delta = global_high_delta;
        this.global_width_delta = global_width_delta;
    }
    
    public String getLang() {
        return lang;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }
    
    public String getLoglevel() {
        return loglevel;
    }
    public void setLoglevel(String loglevel) {
        this.loglevel = loglevel;
    }
    public int getIntLogLevel(){
        try{
            int level = Integer.parseInt( loglevel );
            return level;
        }catch(Exception ex){
            return ResourceCenter.LOG_LEVEL_INFO;
        }
    }

    public String logFile(){
        return this.logFile;
    }

    public int getRstMirrorType(){
        try{
            int type = Integer.parseInt( this.cmdp_rst_mirror_type );
            if( ResourceCenter.isValidForRstMirrorType( type ) ){
                return type;
            }else{
                return ResourceCenter.CMDP_RST_MIRROR_FULL; // 保守方式
            }
        }catch(Exception ex){
            return ResourceCenter.CMDP_RST_MIRROR_FULL; // 保守方式
        }
    }
    public void setRstMirrorType( String type ){
        this.cmdp_rst_mirror_type = type;
    }

    /**
     * @return the global_hight_delta
     */
    public String getGlobal_hight_delta() {
        return global_hight_delta;
    }

    /**
     * @param global_hight_delta the global_hight_delta to set
     */
    public void setGlobal_hight_delta(String global_hight_delta) {
        this.global_hight_delta = global_hight_delta;
    }

    /**
     * @return the global_width_delta
     */
    public String getGlobal_width_delta() {
        return global_width_delta;
    }

    /**
     * @param global_width_delta the global_width_delta to set
     */
    public void setGlobal_width_delta(String global_width_delta) {
        this.global_width_delta = global_width_delta;
    }

    public void prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append("[global]\n");
        buf.append(GUIAdminOpt.CONF_LANG +"="+this.getLang()+"\n");
        buf.append("#LogLevel 0:debug  1:info  2:warning  3:error\n");
        buf.append(GUIAdminOpt.CONF_LOG_LEVEL +"="+this.getLoglevel()+"\n");
        buf.append("#Restore Mirror Type  0:full  2:incremental\n");
        buf.append(GUIAdminOpt.CONF_RST_MIRROR_TYPE +"="+this.getRstMirrorType()+"\n");
        buf.append("#global high size delta(maybe minus)\n");
        buf.append(GUIAdminOpt.CONF_GLOBAL_HITH_DELTA+"="+this.getGlobal_hight_delta()+"\n");
        buf.append("#global width size delta(maybe minus)\n");
        buf.append(GUIAdminOpt.CONF_GLOBAL_WIDTH_DELTA+"="+this.getGlobal_width_delta()+"\n");
        System.out.println(buf.toString());
    }
    
    public void outputConf(OutputStreamWriter out) throws IOException{
        out.write("[global]\n");
        out.write(GUIAdminOpt.CONF_LANG +"="+this.getLang()+"\n");
        out.write("#LogLevel 0:debug  1:info  2:warning  3:error\n");
        out.write(GUIAdminOpt.CONF_LOG_LEVEL +"="+this.getLoglevel()+"\n");
        out.write("#Restore Mirror Type 0:full 2:incremental\n");
        out.write(GUIAdminOpt.CONF_RST_MIRROR_TYPE+"="+this.getRstMirrorType()+"\n");
        out.write("#global high size delta(maybe minus)\n");
        out.write(GUIAdminOpt.CONF_GLOBAL_HITH_DELTA+"="+this.getGlobal_hight_delta()+"\n");
        out.write("#global width size delta(maybe minus)\n");
        out.write(GUIAdminOpt.CONF_GLOBAL_WIDTH_DELTA+"="+this.getGlobal_width_delta()+"\n\n");
    }
    
    public int parserConf(int begin,String[] lines ){
        String line;
        
        do {
            line = lines[begin];
//System.out.println( "[global]line: " + line );

            if( line.length() <= 0 ){
                begin++;
                continue;
            }
            
            if( line.startsWith("#") ){
                begin++;
                continue;
            }
            
            if( isTitle( line ) )
                break;
                
            Vector v = splitLine( line );
            if( v.elementAt(0).equals( GUIAdminOpt.CONF_LANG.toUpperCase() ) )
                this.setLang( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals(GUIAdminOpt.CONF_LOG_LEVEL.toUpperCase() ) )
                this.setLoglevel( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals(GUIAdminOpt.CONF_RST_MIRROR_TYPE.toUpperCase() ) )
                this.setRstMirrorType( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals(GUIAdminOpt.CONF_GLOBAL_HITH_DELTA.toUpperCase() ) )
                this.setGlobal_hight_delta( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals(GUIAdminOpt.CONF_GLOBAL_WIDTH_DELTA.toUpperCase() ) )
                this.setGlobal_width_delta( (String)v.elementAt(1) );
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [global]");
            
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
