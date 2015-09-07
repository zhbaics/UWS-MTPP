package guisanboot.datadup.data;

import guisanboot.res.ResourceCenter;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UniProIBoot extends AbstractUniProfile {
    private ArrayList<String> iboot_bf_cmd = new ArrayList<String>();
    private ArrayList<String> iboot_snap_bef_cmd = new ArrayList<String>();
    private ArrayList<String> iboot_af_cmd = new ArrayList<String>();
    private ArrayList<String> iboot_snap_bef_cmd1 = new ArrayList<String>();
    
    private String only_localboot_backup; // 只有本地启动才会执行此备份
    private String snap_flag; // 备份完毕做快照
    private String uws_username; // uws admin account
    private String uws_passwd; // uws admin account password
    private String uws_ip;
    private String uws_port;
    private String mgid_info; // < target_id : max snap : mgid >三元组
    private String logout_target; // whether logout target after duplicating is finished. 1: logout  non-1: not logout
    
    private int osTid = 0;
    
    public UniProIBoot() {
        this( "1","1",ResourceCenter.DEFAULT_ACCT,"admin","",ResourceCenter.C_S_PORT+"","","1" );
    }
    
    public UniProIBoot(
        String only_localboot_backup,
        String snap_flag,
        String uws_username,
        String uws_passwd,
        String uws_ip,
        String uws_port,
        String mgid_info,
        String logout_target
    ){
        this.setOnly_localboot_backup(only_localboot_backup);
        this.setSnap_flag(snap_flag);
        this.uws_username = uws_username;
        this.uws_passwd = uws_passwd;
        this.uws_ip = uws_ip;
        this.uws_port = uws_port;
        this.mgid_info = mgid_info;
        this.logout_target = logout_target;
    }
    
    public int getOsTid(){
        return osTid;
    }
    public void setOsTid( int val ){
        osTid = val;
    }
    
    public void cleanIboot_bf_cmd(){
        this.iboot_bf_cmd.clear();
    }
    
    public void addIboot_bf_cmd( String iboot_bf_cmd ) {
        this.iboot_bf_cmd.add( iboot_bf_cmd );
    }
     
    public String getIboot_bf_cmd() {
        StringBuffer buf = new StringBuffer();
        int size = iboot_bf_cmd.size();
        for( int i=0; i<size; i++ ){
            buf.append( (String)iboot_bf_cmd.get(i)+"\n" );
        }
        return buf.toString();
    }
    
    public String assebleIboot_bf_cmd() {
        StringBuffer buf = new StringBuffer();
        int size = iboot_bf_cmd.size();
        if( size >0 ){
            for( int i=0; i<size; i++ ){
                buf.append( "iboot_bf_cmd="+(String)iboot_bf_cmd.get(i)+"\n" );
            }
        }else{
            buf.append( "iboot_bf_cmd=\n" );
        }
        return buf.toString();
    }
    
    public void cleanIboot_af_cmd(){
        this.iboot_af_cmd.clear();
    }
    
    public String getIboot_af_cmd() {
        StringBuffer buf = new StringBuffer();
        int size = iboot_af_cmd.size();
        for( int i=0; i<size; i++ ){
            buf.append( (String)iboot_af_cmd.get(i)+"\n" );
        }
        return buf.toString();
    }
    
    public String assebleIboot_af_cmd() {
        StringBuffer buf = new StringBuffer();
        int size = iboot_af_cmd.size();
        if( size >0 ){
            for( int i=0; i<size; i++ ){
                buf.append( "iboot_af_cmd="+(String)iboot_af_cmd.get(i)+"\n" );
            }
        }else{
            buf.append( "iboot_af_cmd=\n" );
        }
        return buf.toString();
    }
    
    public void addIboot_af_cmd(String iboot_af_cmd) {
        this.iboot_af_cmd.add( iboot_af_cmd );
    }
    
    public void cleanIboot_snap_bf_cmd(){
        this.iboot_snap_bef_cmd.clear();
    }
    
    public void addIboot_snap_bf_cmd( String snap_bf_cmd ) {
        this.iboot_snap_bef_cmd.add( snap_bf_cmd );
    }
    
    public String getIboot_snap_bf_cmd() {
        StringBuffer buf = new StringBuffer();
        int size = iboot_snap_bef_cmd.size();
        for( int i=0; i<size; i++ ){
            buf.append( (String)iboot_snap_bef_cmd.get(i)+"\n" );
        }
        return buf.toString();
    }
    
    public String assebleIboot_snap_bf_cmd() {
        String iboot_snap_bf_cmd;
        
        StringBuffer buf = new StringBuffer();
        int size = iboot_snap_bef_cmd.size();
        if( size >0 ){
            for( int i=0; i<size; i++ ){
                iboot_snap_bf_cmd = (String)iboot_snap_bef_cmd.get(i);
                // 注掉一下代码，不改变iboot_snap_bf_cmd中的定义,当os_target_rootid变化了时，
                // 需要自己手动改动这个值 （2011.8.30 by zjj）
                //if( iboot_snap_bf_cmd.indexOf("<TARGET_ROOT_PATH=") >=0 ){
//System.out.println(" @@@@@@@@@@@@@@@@@@@@@@  assebleIboot_snap_bf_cmd ");
                 //   buf.append( "iboot_snap_bf_cmd=" + replaceOSTid( iboot_snap_bf_cmd ) +"\n" );
                //}else{
                    buf.append( "iboot_snap_bf_cmd=" + iboot_snap_bf_cmd + "\n" );
                //}
            }
        }else{
            buf.append( "iboot_snap_bf_cmd=\n" );
        }
        return buf.toString();
    }
    
    // 将<TARGET_ROOT_PATH=xxx>中的xxx替换掉
    private String replaceOSTid( String oldString ){
System.out.println(" ################# old String: "+ oldString );           
        Pattern pattern = Pattern.compile( "<TARGET_ROOT_PATH=\\d{1,9}>");
        Matcher matcher = pattern.matcher( oldString );
        String newString = matcher.replaceAll( "<TARGET_ROOT_PATH=" + osTid + ">" ); 
System.out.println(" ################# new String: "+ newString );                
        return newString;
    }
    
    public ArrayList getIboot_snap_bf_cmd_list(){
        return iboot_snap_bef_cmd;
    }
    
    public void cleanIboot_snap_bf_cmd1(){
        this.iboot_snap_bef_cmd1.clear();
    }
    
    public void addIboot_snap_bf_cmd1( String snap_bf_cmd1 ) {
        this.iboot_snap_bef_cmd1.add( snap_bf_cmd1 );
    }
    
    public String getIboot_snap_bf_cmd1() {
        StringBuffer buf = new StringBuffer();
        int size = iboot_snap_bef_cmd1.size();
        for( int i=0; i<size; i++ ){
            buf.append( (String)iboot_snap_bef_cmd1.get(i)+"\n" );
        }
        return buf.toString();
    }
    
    public String assebleIboot_snap_bf_cmd1() {
        String iboot_snap_bf_cmd1;
        
        StringBuffer buf = new StringBuffer();
        int size = iboot_snap_bef_cmd1.size();
        if( size >0 ){
            for( int i=0; i<size; i++ ){
                iboot_snap_bf_cmd1 = (String)iboot_snap_bef_cmd1.get( i );
                // 注掉一下代码，不改变iboot_snap_bf_cmd中的定义,当os_target_rootid变化了时，
                // 需要自己手动改动这个值 （2011.8.30 by zjj）
                //if( iboot_snap_bf_cmd1.indexOf("<TARGET_ROOT_PATH=")>=0 ){
//System.out.println(" @@@@@@@@@@@@@@@@@@@@@@  assebleIboot_snap_bf_cmd1 ");
                //    buf.append( "iboot_snap_bf_cmd1=" + replaceOSTid( iboot_snap_bf_cmd1 ) +"\n" );
                //}else{
                    buf.append( "iboot_snap_bf_cmd1=" + iboot_snap_bf_cmd1 + "\n" );
                //}
            }
        }else{
            buf.append( "iboot_snap_bf_cmd1=\n" );
        }
        return buf.toString();
    }
    
    public ArrayList getIboot_snap_bf_cmd1_list(){
        return iboot_snap_bef_cmd1;
    }
    
    public String getOnly_localboot_backup() {
        return only_localboot_backup;
    }

    public void setOnly_localboot_backup(String only_localboot_backup) {
        this.only_localboot_backup = only_localboot_backup;
    }
    
    public String getSnap_flag() {
        return snap_flag;
    }
    
    public void setSnap_flag(String snap_flag) {
        this.snap_flag = snap_flag;
    }
    
    public String getUws_username() {
        return uws_username;
    }

    public void setUws_username(String uws_username) {
        this.uws_username = uws_username;
    }

    public String getUws_passwd() {
        return uws_passwd;
    }

    public void setUws_passwd(String uws_passwd) {
        this.uws_passwd = uws_passwd;
    }

    public String getUws_ip() {
        return uws_ip;
    }

    public void setUws_ip(String uws_ip) {
        this.uws_ip = uws_ip;
    }

    public String getUws_port() {
        return uws_port;
    }
    
    public void setUws_port(String uws_port) {
        this.uws_port = uws_port;
    }

    public String getMgid_info() {
        return mgid_info;
    }

    public void setMgid_info(String mgid_info) {
        this.mgid_info = mgid_info;
    }
    
    public String getLogout_target(){
        return logout_target;
    }
    public void setLogout_target( String logout_target ){
        this.logout_target = logout_target;
    }
    public boolean isAutoLogout(){
        return logout_target.equals("1");
    }
    
    public void outputProfile(OutputStreamWriter out) throws IOException{
        out.write("[iboot]\n");
        out.write( this.assebleIboot_bf_cmd() );
        out.write( this.assebleIboot_snap_bf_cmd() );
        out.write( this.assebleIboot_af_cmd() );
        out.write( this.assebleIboot_snap_bf_cmd1() );
        out.write("only_localboot_backup="+this.getOnly_localboot_backup()+"\n");
        out.write("snap_flag="+this.getSnap_flag()+"\n");
        out.write("uws_username="+this.getUws_username() +"\n");
        out.write("uws_passwd="+this.getUws_passwd()+"\n");
        out.write("uws_ip="+this.getUws_ip()+"\n");
        out.write("uws_port="+this.getUws_port()+"\n");
        out.write("mgid_info="+this.getMgid_info()+"\n");
        out.write("logout_target="+this.getLogout_target() +"\n");
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        
        buf.append("[iboot]\n");
        buf.append( this.assebleIboot_bf_cmd()  );
        buf.append( this.assebleIboot_snap_bf_cmd() );
        buf.append( this.assebleIboot_af_cmd() );
        buf.append( this.assebleIboot_snap_bf_cmd1() );
        buf.append("only_localboot_backup="+this.getOnly_localboot_backup()+"\n");
        buf.append("snap_flag="+this.getSnap_flag()+"\n");
        buf.append("uws_username="+this.getUws_username() +"\n");
        buf.append("uws_passwd="+this.getUws_passwd()+"\n");
        buf.append("uws_ip="+this.getUws_ip()+"\n");
        buf.append("uws_port="+this.getUws_port()+"\n");
        buf.append("mgid_info="+this.getMgid_info()+"\n");
        buf.append("logout_target="+this.getLogout_target()+"\n");
        
        return buf.toString();
    }
    
    public int parserProfile( int begin,String[] lines ) throws IllegalArgumentException{
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
            if( v.elementAt(0).equals("iboot_bf_cmd") ){
                this.addIboot_bf_cmd( (String)v.elementAt(1) );
            }else if( v.elementAt(0).equals("iboot_snap_bf_cmd") ){
                this.addIboot_snap_bf_cmd( (String)v.elementAt(1) );
            }else if( v.elementAt(0).equals("iboot_af_cmd") ){
                this.addIboot_af_cmd( (String)v.elementAt(1) ); 
            }else if( v.elementAt(0).equals("iboot_snap_bf_cmd1") ){
                this.addIboot_snap_bf_cmd1( (String)v.elementAt(1) );
            }else if( v.elementAt(0).equals("only_localboot_backup") ){
                this.setOnly_localboot_backup( (String)v.elementAt(1) );
            }else if( v.elementAt(0).equals("snap_flag") ){
                this.setSnap_flag( (String)v.elementAt(1) );
            }else if( v.elementAt(0).equals("uws_username")){
                this.setUws_username( (String)v.elementAt(1) );
            }else if( v.elementAt(0).equals("uws_passwd")){
                this.setUws_passwd( (String)v.elementAt(1) );
            }else if( v.elementAt(0).equals("uws_ip") ){
                this.setUws_ip( (String)v.elementAt(1) );
            }else if( v.elementAt(0).equals("uws_port")){
                this.setUws_port( (String)v.elementAt(1));
            }else if( v.elementAt(0).equals("mgid_info")){
                this.setMgid_info( (String)v.elementAt(1) );
            }else if( v.elementAt(0).equals("logout_target") ){
                this.setLogout_target( (String)v.elementAt(1) );
            }else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [iboot]");
            
            begin++;
            
        } while( begin < lines.length );

        setLastLine( line );
        
        return begin;
    }
}
