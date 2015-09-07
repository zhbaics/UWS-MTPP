/*
 * MirrorDiskInfoWrapper.java
 *
 * Created on 2008/7/3,�PM�6:51
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;

/**
 *
 * @author zjj
 */
public class MirrorDiskInfoWrapper {
    private int type;
    public MirrorDiskInfo mdi;
    private String host_ip="";
    
    @Override public String toString(){
        if( type == 0 ){
            return mdi.getSrc_agent_mp();
        }else if( type == 1 ){
            return SanBootView.res.getString("common.vol") + " : " + mdi.toString();
        }else { // type ==2
            return mdi.getSrc_agent_mp()+" [ "+host_ip+" ]";
        }
    }
    
    /** Creates a new instance of MirrorDiskInfoWrapper */
    public MirrorDiskInfoWrapper() {
        this(0);
    }
    
    public MirrorDiskInfoWrapper( int type) {
        this.type = type;
    }

    public void setHost_IP( String val ){
        this.host_ip = val;
    }

    public void setType( int type ){
        this.type = type;
    }
}
