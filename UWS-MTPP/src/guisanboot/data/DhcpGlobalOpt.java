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

/**
 *
 * @author Administrator
 */
public class DhcpGlobalOpt {
    public String dns = "";
    public String defgw = "";
    public String defStartOS = "";
    
    /** Creates a new instance of DhcpClientInfo */
    public DhcpGlobalOpt() {
    }
    
    public String getDNS(){
        if( dns.equals("unspecified") ){
            return "";
        }else{
            return dns;
        }
    }
    public void setDNS( String val ){
        dns = val;
    }
    
    public String getDefGw(){
        if( defgw.equals("unspecified") ){
            return "";
        }else{
            return defgw;
        }
    }
    public void setDefGw( String val ){
        defgw = val;
    }
    
    public String getDefStartOS(){
        return defStartOS;
    }
    public void setDefStartOS( String val ){
        defStartOS = val;
    }
}
