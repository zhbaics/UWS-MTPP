/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cluster.entity;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * AccessPath.java
 *
 * Created on 2011-7-12, 12:14:13
 */
public class AccessPath {
    public String ip;
    public boolean isLocal;
    public Hashtable<String,String> otherIPPath = new Hashtable<String,String>();

    public AccessPath(){
    }

    public AccessPath( String ip,boolean isLocal ){
        this.ip = ip;
        this.isLocal = isLocal;
    }

    public boolean isLocal(){
        return this.isLocal;
    }

    public boolean isShared(){
        return !this.isLocal();
    }

    public void addOtherPath( String ip ){
        String val = otherIPPath.get( ip );
        if( val == null ){
            otherIPPath.put( ip, ip );
        }
    }

    private Object[] sortIP(){
        InetAddress aIP = null;
        String ipp;
        long longIP;

        try{
            aIP = InetAddress.getByName( this.ip );
        }catch(Exception ex){
        }
        longIP = mylib.tool.Util.inetAddress2Long( aIP );
        Vector vec = new Vector();
        vec.add( new Long( longIP ) );

        Enumeration list =  this.otherIPPath.elements();
        while( list.hasMoreElements() ){
            ipp = (String)list.nextElement();
            try{
                aIP = InetAddress.getByName( ipp );
            }catch(Exception ex){
            }
            longIP = mylib.tool.Util.inetAddress2Long( aIP );
            vec.add( new Long( longIP ) );
        }
        Object[] objList = vec.toArray();
        Arrays.sort( objList );

        return objList;
    }
    
    public String getCombinedIP(){
        InetAddress aIP;

        Object[] objList = this.sortIP();
        StringBuffer buf = new StringBuffer();
        for( int i=0; i<objList.length; i++ ){
            long longIp = ((Long)objList[i]).longValue();
            aIP = mylib.tool.Util.long2InetAddress( longIp );
            buf.append( aIP );
        }
        return buf.toString();
    }

    @Override public String toString(){
        return ip+" "+isLocal;
    }
}
