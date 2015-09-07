/*
 * LunMap.java
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
import guisanboot.MenuAndBtnCenterForMainUi;

/**
 *
 * @author Administrator
 */
public class LunMap extends BasicUIObject{
    private String lm_ip;
    private String lm_mask;
    private String lm_access_mode;
    private String lm_srv_user;
    private String lm_srv_passwd;
    private String lm_clnt_user;
    private String lm_clnt_passwd;
    
    /** Creates a new instance of LunMap */
    public LunMap() {
        super( ResourceCenter.TYPE_LUNMAP_INDEX );
    }
    
    public LunMap(
        String _lm_ip,
        String _lm_mask,
        String _lm_access_mode,
        String _lm_srv_user,
        String _lm_srv_passwd,
        String _lm_clnt_user,
        String _lm_clnt_passwd
    ){
        super( ResourceCenter.TYPE_LUNMAP_INDEX );
        
        lm_ip = _lm_ip;
        lm_mask = _lm_mask;
        lm_access_mode = _lm_access_mode;
        lm_srv_user = _lm_srv_user;
        lm_clnt_user = _lm_clnt_user;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 0 );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 1 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CANCEL_LM ) );
        }
    }
    
    public String getIP(){
        return lm_ip;
    }
    public void setIP( String val ){
        lm_ip = val;
    }
    
    public String getMask(){
        return lm_mask;
    }
    public void setMask( String val ){
        lm_mask = val;
    }
    
    public String getAccessMode(){
        return lm_access_mode;
    }
    public void setAccessMode( String val ){
        lm_access_mode = val;
    }
    
    public String getSrvUser(){
        return lm_srv_user;
    }
    public void setSrvUser( String val ){
        lm_srv_user = val;
    }
    
    public String getClntUser(){
        return lm_clnt_user;
    }
    public void setClntUser( String val ){
        lm_clnt_user = val;
    }
    
    public String getSrvPasswd(){
        return lm_srv_passwd;
    }
    public void setSrvPasswd( String val ){
        lm_srv_passwd = val;
    }
    
    public String getClntPasswd(){
        return lm_clnt_passwd;
    }
    public void setClntPasswd( String val ){
        lm_clnt_passwd = val;
    }
    
    @Override public String toString(){
        return lm_ip;
    }
    
    @Override public String getComment(){
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
        return ResourceCenter.ICON_CHL_16;
    }
    public String toTableString(){
        return lm_ip;
    }
    
    //** TreeRevealable的实现��
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
