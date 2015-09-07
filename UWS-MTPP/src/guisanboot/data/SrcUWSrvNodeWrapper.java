/*
 * SrcUWSrvNodeWrapper.java
 *
 * Created on 2008/6/17, AM��9:27
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

/**
 *
 * @author zjj
 */
public class SrcUWSrvNodeWrapper extends BasicUIObject {
    private UWSrvNode uws;
    
    /** Creates a new instance of UWSrvNodeWrapper */
    public SrcUWSrvNodeWrapper(){
        super( ResourceCenter.TYPE_SRC_UWSSRV );
    }
    
    public SrcUWSrvNodeWrapper( UWSrvNode uws ) {
        super( ResourceCenter.TYPE_SRC_UWSSRV );
        this.uws = uws;
    }
    
    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 0 );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            // Դ��UWS�ǲ��������޸ĺ�ɾ���
            fsForTable = new ArrayList<Integer>( 0 );
        }
    }
    
    public void setMetaData( UWSrvNode uws ){
        this.uws = uws;
    }
    
    public UWSrvNode getMetaData(){
        return this.uws;
    }
    
    @Override public String getComment(){
        return uws.getUws_id()+"";
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
        return uws.getUws_id()+"";
    }
    
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_REMOTE_UWS;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_REMOTE_UWS;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        //return this.uws.getUws_psn();
        return this.uws.getUws_ip();
    }
    public String toTipString(){
        return uws.getUws_psn()+" [ "+this.uws.getUws_ip()+" ] ";
    }
}
