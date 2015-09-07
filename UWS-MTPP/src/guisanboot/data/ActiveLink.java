/*
 * ActiveLink.java
 *
 * Created on 2006/12/18,�PM�4:57
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import javax.swing.*;
import java.util.ArrayList;
import mylib.UI.*;
import guisanboot.res.*;


/**
 *
 * @author Administrator
 */
public class ActiveLink implements GeneralInfo,TableRevealable{
    private String link_ip;
    private String link_initor;
    private int link_target_id;
    private String link_send;  //not used now
    private String link_rec; // not used now
    private String link_pid;
    
    private int type = ResourceCenter.TYPE_ACTLINK_INDEX;
    
    /** Creates a new instance of ActiveLink */
    public ActiveLink() {
    }
    
    public ActiveLink(
        String _link_ip,
        String _link_initor,
        int _link_target_id,
        String _link_send,
        String _link_rec,
        String _link_pid
    ){
        link_ip = _link_ip;
        link_initor=_link_initor;
        link_target_id = _link_target_id;
        link_send = _link_send;
        link_rec = _link_rec;
        link_pid = _link_pid;
    }
    
    public String getIP(){
        return link_ip;
    }
    public void setIP( String val ){
        link_ip = val;
    }
    
    public String getInitor(){
        return link_initor;
    }
    public void setInitor(String val){
        link_initor = val;
    }
    
    public int getTargetID(){
        return link_target_id;
    }
    public void setTargetID( int val ){
        link_target_id = val;
    }
    
    public String getSend(){
        return link_send;
    }
    public void setSend( String val ){
        link_send = val;
    }
    
    public String getRec(){
        return link_rec;
    }
    public void setRec( String val ){
        link_rec = val;
    }
    
    public String getPid(){
        return link_pid;
    }
    public void setPid( String val ){
        link_pid = val;
    }
    
    @Override public String toString(){
        return link_ip;
    }
    
    //** GeneralInfo的实现**/
    public int getType(){
        return type;
    }
    public int getIpIndex(){
        return -1;
    }
    public ArrayList getFunctionList(int type){
        return null;
    }
    public String getComment(){
        return toString();
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
        return ResourceCenter.ICON_LINK_16;
    }
    public String toTableString(){
        return link_ip;
    }
}
