/*
 * DhcpClientInfo.java
 *
 * Created on 2007/8/13,�AM�10:28
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.TableRevealable;

/**
 *
 * @author Administrator
 */
public class DhcpClientInfo implements TableRevealable{
    public String ip ="";
    public String mac="";
    public String subnet="";
    public String nextServer="";
    public String dns ="";
    public String defgw ="";
    public String ostype="";
    public String boottype="";
    
    // dhcp info if the 3rd dhcp server is adopted.
    public String ip_3rd = "";  // ip assigned  by dhcp server
    public String nextServer_3rd = ""; // tftp server ip assigned by dhcp server

    public String os_tid = ""; // 对于iboot来说需要这个数据，其他类型的快速启动不需要这个东东
    
    /** Creates a new instance of DhcpClientInfo */
    public DhcpClientInfo() {
    }
    
    public DhcpClientInfo(
        String ip,
        String mac,
        String subnet,
        String nextServer,
        String dns,
        String defgw,
        String ostype
    ){
       this.ip = ip;
       this.mac = mac;
       this.subnet = subnet;
       this.nextServer = nextServer;
       this.dns = dns;
       this.defgw = defgw;
       this.ostype = ostype;
    }
    
    public DhcpClientInfo(
        String ip,
        String mac,
        String subnet,
        String nextServer,
        String dns,
        String defgw,
        String ostype,
        String os_tid
    ){
       this.ip = ip;
       this.mac = mac;
       this.subnet = subnet;
       this.nextServer = nextServer;
       this.dns = dns;
       this.defgw = defgw;
       this.ostype = ostype;
       this.os_tid = os_tid;
    }
    
    public DhcpClientInfo(
        String ip,
        String mac,
        String subnet,
        String nextServer,
        String dns,
        String defgw,
        String ostype,
        String ip_3rd,
        String nextServer_3rd
    ){
       this.ip = ip;
       this.mac = mac;
       this.subnet = subnet;
       this.nextServer = nextServer;
       this.dns = dns;
       this.defgw = defgw;
       this.ostype = ostype;
       this.ip_3rd = ip_3rd;
       this.nextServer_3rd = nextServer_3rd;
    }
    
    public String getDNS(){
        if( dns.equals("unspecified") ){
            return "";
        }else{
            return dns;
        }
    }
    
    public String getDefGw(){
        if( defgw.equals("unspecified") ){
            return "";
        }else{
            return defgw;
        }
    }
    
    public String getOSType(){
        if( ostype.equals("unspecified") ){
            return "windows";
        }else{
            return ostype;
        }
    }
    
    
    //** TableRevealable的实现**/
    public boolean enableTableEditable(){
        return false;
    }
    public boolean enableSelected(){
        return true;
    }
    public int  getAlignType(){
        return JLabel.LEFT;
    }
    public Icon getTableIcon(){
        return ResourceCenter.MENU_ICON_BLANK;
    }
    public String toTableString(){
        return mac;
    }
    
    public static boolean isValidMAC( String mac ){
        Pattern pattern1 = Pattern.compile(
            "^([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2})$"
        );
        Pattern pattern2 = Pattern.compile(
            "^([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})$"
        );
        Pattern pattern3 = Pattern.compile(
            "^([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})$"
        );
        
        Matcher matcher1 = pattern1.matcher( mac );
        Matcher matcher2 = pattern2.matcher( mac );
        Matcher matcher3 = pattern3.matcher( mac );
        
        return ( matcher1.find() || matcher2.find() || matcher3.find() );
    }

    public static ArrayList<String> getMacItem( String mac ){
        ArrayList<String> ret = new ArrayList<String>();

        Pattern simplePattern = Pattern.compile(
            "^([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})$"
        );
        Matcher simpleMatcher = simplePattern.matcher( mac );
        if( simpleMatcher.find() ){
            for( int i=1; i<7; i++ ){
                ret.add( simpleMatcher.group(i) );
            }
            return ret;
        }

        Pattern pattern = Pattern.compile(
            "^([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2})$"
        );
        Matcher matcher = pattern.matcher( mac );
        if( matcher.find() ){
            for( int i=1; i<7; i++ ){
                ret.add( matcher.group(i) );
            }
            return ret;
        }else{
            pattern = Pattern.compile(
                "^([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})$"
            );
            Matcher matcher1 = pattern.matcher( mac );
            if( matcher1.find() ){
                for( int i=1; i<7; i++ ){
                    ret.add( matcher1.group(i) );
                }
                return ret;
            }
        }
        
        return null;
    }

    public static String getSimpleMac( String mac ){
        String[] list = null;
        
        Pattern simplePattern = Pattern.compile(
            "^([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})$"
        );
        Matcher simpleMatcher = simplePattern.matcher( mac );
        if( simpleMatcher.find() ){
            return mac;
        }
        
        Pattern pattern = Pattern.compile(
            "^([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2})$"
        );
        Matcher matcher = pattern.matcher( mac );
        if( matcher.find() ){
            list = Pattern.compile(":").split( mac,-1 );
        }else{
            pattern = Pattern.compile(
                "^([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})$"
            );
            Matcher matcher1 = pattern.matcher( mac ); 
            if( matcher1.find() ){
                list = Pattern.compile("-").split( mac,-1);
            }
        }
        
        if( list!= null ){
            StringBuffer buf = new StringBuffer();
            for( int i=0; i<list.length; i++ ){
                if( list[i].equals("") ) continue;
                buf.append( list[i]);
            }
            return buf.toString();
        }else{
            return "";
        }
    }
    
    public static String getMacStrForWin( String mac ){
        String[] list = null;
        boolean isFirst =true;
        
        Pattern pattern = Pattern.compile(
            "^([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})$"
        );
        Matcher matcher = pattern.matcher( mac );
        if( matcher.find() ){
            return mac;
        }else{
            pattern = Pattern.compile(
                "^([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2})$"
            );
            Matcher matcher1 = pattern.matcher( mac ); 
            if( matcher1.find() ){
                list = Pattern.compile(":").split( mac,-1);
            }
        }
        
        if( list!= null ){
            StringBuffer buf = new StringBuffer();
            for( int i=0; i<list.length; i++ ){
                if( list[i].equals("") ) continue;
                if( isFirst ){
                    buf.append( list[i]);
                    isFirst = false;
                }else{
                    buf.append("-"+list[i]);
                }
            }
            return buf.toString();
        }else{
            pattern = Pattern.compile(
                "^([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})$"
            );
            Matcher matcher2 = pattern.matcher( mac );
            if( matcher2.find() ){
                isFirst = true;
                StringBuffer buf1 = new StringBuffer();
                int grpNum = matcher2.groupCount();
                for( int j=1;j<=grpNum; j++ ){ 
                    if( isFirst ){
                        buf1.append( matcher2.group(j) );
                        isFirst = false;
                    }else{
                        buf1.append("-");
                        buf1.append( matcher2.group(j) );
                    }
                }
                return buf1.toString();
            }else{
                return "";
            }
        }
    }
    
    public static String getMacStr( String mac ){
        String[] list = null;
        boolean isFirst =true;
        
        Pattern pattern = Pattern.compile(
            "^([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2}):([0-9a-zA-Z]{2})$"
        );
        Matcher matcher = pattern.matcher( mac );
        if( matcher.find() ){
            return mac;
        }else{
            pattern = Pattern.compile(
                "^([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})-([0-9a-zA-Z]{2})$"
            );
            Matcher matcher1 = pattern.matcher( mac ); 
            if( matcher1.find() ){
                list = Pattern.compile("-").split( mac,-1);
            }
        }
        
        if( list != null ){
            StringBuffer buf = new StringBuffer();
            for( int i=0; i<list.length; i++ ){
                if( list[i].equals("") ) continue;
                if( isFirst ){
                    buf.append( list[i]);
                    isFirst = false;
                }else{
                    buf.append(":"+list[i]);
                }
            }
            return buf.toString();
        }else{
            pattern = Pattern.compile(
                "^([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})([0-9a-zA-Z]{2})$"
            );
System.out.println(" original mac: "+ mac );
            
            Matcher matcher2 = pattern.matcher( mac );
            if( matcher2.find() ){
                isFirst = true;
                StringBuffer buf1 = new StringBuffer();
                int grpNum = matcher2.groupCount();
                for( int j=1;j<=grpNum; j++ ){ 
                    if( isFirst ){
                        buf1.append( matcher2.group(j) );
                        isFirst = false;
                    }else{
                        buf1.append(":");
                        buf1.append( matcher2.group(j) );
                    }
                }
                return buf1.toString();
            }else{
                return "";
            }
        }
    }

    public String getBoottype() {
        return boottype;
    }

    public String getDefgw() {
        return defgw;
    }

    public String getDns() {
        return dns;
    }

    public String getIp() {
        return ip;
    }

    public String getIp_3rd() {
        return ip_3rd;
    }

    public String getMac() {
        return mac;
    }

    public String getNextServer() {
        return nextServer;
    }

    public String getNextServer_3rd() {
        return nextServer_3rd;
    }

    public String getOs_tid() {
        return os_tid;
    }

    public String getOstype() {
        return ostype;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }
    
    
}
