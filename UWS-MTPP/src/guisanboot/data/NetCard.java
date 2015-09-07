/*
 * NetCard.java
 *
 * Created on 2006/12/29,�PM 2:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import java.util.Vector;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class NetCard {
    public boolean isSel = false;
    public String info =""; // adapter info
    public String mac =""; 
    public Vector<BindIPAndMask> ipList = new Vector<BindIPAndMask>(); //主机原来的ip/mask组
    public boolean isGiga = false; // is a giga netcard?

    public String host_ip="";  // 用于区分不同主机的网卡
    
    /** Creates a new instance of NetCard */
    public NetCard() {
    }
    
    public NetCard( String mac ){
        this.mac = mac;
    }
    
    @Override public String toString(){
        return info;
    }
    
    public void addIpBinder( BindIPAndMask binder ){
        ipList.add( binder );
    }
    
    public Vector<BindIPAndMask> getIPList(){
        int size = ipList.size();
        if( size > 0 ){
            return ipList;
        }else{
            BindIPAndMask binder = new BindIPAndMask();
            Vector<BindIPAndMask> ret = new Vector<BindIPAndMask>(1);
            ret.addElement( binder );
            return ret;
        }
    }
    
    public boolean isBelongedThisNc( String ip ){
        int size = ipList.size();
        for( int i=0; i<size; i++ ){
            BindIPAndMask one = ipList.get(i);
            if( one.ip.equals( ip ) ){
                return true;
            }
        }
        return false;
    }
    
    public String getDefaultIP(){
        int size = ipList.size();
        if( size > 0 ){
            BindIPAndMask binder = ipList.elementAt(0);
            return binder.ip;
        }else{
            return "";
        }
    }
    
    public String prtMe(){
        BindIPAndMask binder;
        String ret;
        
        ret = info;
        ret += ";"+ mac+" ";
        int size = ipList.size();
        for( int i=0; i<size; i++ ){
            binder = ipList.elementAt(i);
            ret+=binder.ip+";"+binder.mask+";";
        }
        
        return ret;
    }
    
    public static String getSimpleMac( String mac ){
        String ret="";
        String[] items = Pattern.compile("-").split( mac,-1 );
        for( int i=0; i<items.length; i++ ){
            if( items[i].equals("") )continue;
            ret +=items[i];
        }
        return ret;
    }
    
    public static String getSimpleMac1( String mac ){
        String ret="";
        String[] items = Pattern.compile(":").split( mac,-1 );
        for( int i=0; i<items.length; i++ ){
            if( items[i].equals("") )continue;
            ret +=items[i];
        }
        return ret;
    }
    
    public static String getUnixMac( String mac ){
        String ret="";
        boolean isFirst = true;
        
        String[] items = Pattern.compile("-").split( mac,-1 );
        for( int i=0; i<items.length; i++ ){
            if( items[i].equals("") )continue;
            if( isFirst ){
                ret = items[i];
                isFirst = false;
            }else{
                ret +=":"+items[i];
            }
        }
        return ret;
    }
}
