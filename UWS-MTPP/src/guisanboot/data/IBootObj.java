/*
 * IBootObj.java
 *
 * Created on 2008/1/4, PM�4:21
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;

/**
 *
 * @author Administrator
 */
public class IBootObj {
    public final static String IBOOT_TGTNAME = "tgt_name";
    public final static String IBOOT_PORTALIP = "portal_ip";
    
    public String mac=""; 
    public String ena="Yes";  // enable or disable
    public String hostIp="";
    public String hostName="";
    public String pip=""; // portal_ip
    public String pport="3260"; // pport
    public String options="000000a0";
    public String rams="128";  //RAMs
    public String unids="0"; //UNDIs
    public String uname="";
    public String password="";
    public String ini_name="";
    public String tgt_name="";
    public String living_days="";
    
    /** Creates a new instance of IBootObj */
    public IBootObj() {
    }
    
    public IBootObj( 
            String mac, 
            String ena,
            String hostIp,
            String hostName,
            String pip,
            String pport,
            String options,
            String rams,
            String unids,
            String uname,
            String password,
            String ini_name,
            String tgt_name,
            String living_days
    ){
        this.mac = mac;
        this.ena = ena;
        this.hostIp = hostIp;
        this.hostName = hostName;
        this.pip = pip;
        this.pport = pport;
        this.options = options;
        this.rams = rams;
        this.unids = unids;
        this.uname = uname;
        this.password = password;
        this.ini_name = ini_name;
        this.tgt_name = tgt_name;
        this.living_days = living_days;
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append(ResourceCenter.lineSeparator + " mac: " + mac );
        buf.append(ResourceCenter.lineSeparator + " ena: "+ena);
        buf.append(ResourceCenter.lineSeparator + " hostip: "+hostIp );
        buf.append(ResourceCenter.lineSeparator + " hostName: "+hostName);
        buf.append(ResourceCenter.lineSeparator + " pip: "+pip );
        buf.append(ResourceCenter.lineSeparator + " pport: "+pport );
        buf.append(ResourceCenter.lineSeparator + " options: "+options );
        buf.append(ResourceCenter.lineSeparator + " rams: "+rams );
        buf.append(ResourceCenter.lineSeparator + " unids: "+unids );
        buf.append(ResourceCenter.lineSeparator + " uname: "+uname );
        buf.append(ResourceCenter.lineSeparator + " password: "+password );
        buf.append(ResourceCenter.lineSeparator + " ini_name: "+ini_name );
        buf.append(ResourceCenter.lineSeparator + " tgt_name: "+tgt_name );
        buf.append(ResourceCenter.lineSeparator + " living_days: "+living_days );
        
        return buf.toString();
    }
}
