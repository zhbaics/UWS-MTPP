/*
 * SubNetInDHCPConf.java
 *
 * Created on 2007/8/11,� PM�2:37
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.TableRevealable;

/**
 *
 * @author Administrator
 */
public class SubNetInDHCPConf implements TableRevealable{
    public String subnet="";
    public String netmask="";
    public String start="";
    public String end="";
    public int def_lease_time= 172800; //缺省的租赁时间[秒], 2天
    public boolean allUnknonClnt = false;
    
    /** Creates a new instance of SubNetInDHCPConf */
    public SubNetInDHCPConf() {
    }
    
    public SubNetInDHCPConf(
        String _subnet,
        String _netmask,
        String _start,
        String _end,
        int _lease,
        boolean allow
    ){
        subnet = _subnet;
        netmask = _netmask;
        start = _start;
        end = _end;
        def_lease_time = _lease;
        allUnknonClnt = allow;
    }
    
    @Override public String toString(){
        return subnet;
    }
    
    public int getLeaseTimeInDay(){
        try{
            int day = def_lease_time/(24*60*60);
            return day;
        }catch(Exception ex){
            return 1;
        }
    }
    
    public boolean isAllowUnknownHost(){
        return allUnknonClnt;
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
        return subnet;
    }

    public boolean isAllUnknonClnt() {
        return allUnknonClnt;
    }

    public int getDef_lease_time() {
        return def_lease_time;
    }

    public String getEnd() {
        return end;
    }

    public String getNetmask() {
        return netmask;
    }

    public String getStart() {
        return start;
    }

    public String getSubnet() {
        return subnet;
    }

}
