/*
 * DhcpClientInfo.java
 *
 * Created on 2007/8/13, AM 10:28
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
public class DhcpIBootSrv implements TableRevealable{
    public String ip ="";
    public String defServer="";
    
    public DhcpIBootSrv(){
    }
    
    /** Creates a new instance of DhcpClientInfo */
    public DhcpIBootSrv( String _ip,String _defServer ) {
        ip = _ip;
        defServer = _defServer;
    }
    
    public String getIP(){
        return ip;
    }
    public void setIP( String val ){
        ip = val;
    }
    
    public String getDefServer(){
        return defServer;
    }
    public void setDefServer( String val ){
        defServer = val;
    }
    
    public boolean isDefaultServer(){
        return defServer.equals("1");
    }
    
    @Override public String toString(){
        return ip;
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
        return ip;
    }
}
