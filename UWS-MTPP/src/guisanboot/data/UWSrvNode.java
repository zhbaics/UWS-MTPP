/*
 * UWSrvNode.java
 *
 * Created on 2008/6/5, PM�6:24
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.BasicUIObject;
import guisanboot.MenuAndBtnCenterForMainUi;

/**
 *
 * @author zjj
 */
public class UWSrvNode extends BasicUIObject {
    public final static int MEDIA_SERVER_PORT = 6020;
    
    public static final String UWS_RECFLAG   = "Record:";
    public static final String UWS_uws_id    = "uws_id";
    public static final String UWS_uws_ip    = "uws_ip";
    public static final String UWS_uws_port  = "uws_port";
    public static final String UWS_uws_psn   = "uws_sn";
    
    private int uws_id;
    private String uws_ip; //  length: 60
    private int uws_port = MEDIA_SERVER_PORT;
    
    // uws_psn为空表示目的端UWS, uws_psn不为空表示源端UWS(实际上是PSN)
    private String uws_psn=""; // length: 60 
    
    /** Creates a new instance of UWSrvNode */
    public UWSrvNode() {
        super( ResourceCenter.TYPE_DEST_UWS );
    }

    public UWSrvNode(
        int uws_id,
        String uws_ip,
        int uws_port,
        String uws_psn 
    ){
        super( ResourceCenter.TYPE_DEST_UWS );
        
        this.uws_id = uws_id;
        this.uws_ip = uws_ip;
        this.uws_port = uws_port;
        this.uws_psn = uws_psn;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 0 );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 2 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_UWS_SRV ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_UWS_SRV ) );
        }
    }
    
    public int getUws_id() {
        return uws_id;
    }

    public void setUws_id(int uws_id) {
        this.uws_id = uws_id;
    }

    public String getUws_ip() {
        return uws_ip;
    }

    public void setUws_ip(String uws_ip) {
        this.uws_ip = uws_ip;
    }

    public int getUws_port() {
        return uws_port;
    }

    public void setUws_port(int uws_port) {
        this.uws_port = uws_port;
    }

    public String getUws_psn() {
        return uws_psn;
    }

    public void setUws_psn(String uws_psn) {
        this.uws_psn = uws_psn;
    }
 
    @Override public String toString(){
        return uws_ip;
    }
    
    @Override public String getComment(){
        return uws_id+"";
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
        return ResourceCenter.ICON_REMOTE_UWS;
    }
    public String toTableString(){
        return uws_id+"";
    }
    
    //** TreeRevealable的实现�
    public Icon getExpandIcon(){
        return null;
    }
    public Icon getCollapseIcon(){
        return null;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return toString();
    }
    public String toTipString(){
        return toTreeString();
    }
}