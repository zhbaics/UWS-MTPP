/*
 * LVInfo.java
 *
 * Created on 2008/9/10,�AM 11:50
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

/**
 *
 * @author zjj
 */
public class LVInfo {
    public String mp="";
    public String tid="";
    public String vg="";
    public String lv="";
    public String fstype="";
    public String lvmType="";
    
    /** Creates a new instance of LVInfo */
    public LVInfo() {
    }
    
    @Override public String toString(){
        StringBuffer buf = new StringBuffer();
        buf.append( mp );
        buf.append("   ");
        buf.append(tid);
        buf.append("   ");
        buf.append( vg );
        buf.append("   ");
        buf.append( lv );
        buf.append("   ");
        buf.append( fstype);
        buf.append("   ");
        buf.append( lvmType );
        
        return buf.toString();
    }
}
