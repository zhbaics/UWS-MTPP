/*
 * VolumeMapWrapper.java
 *
 * Created on 2007/1/13,AM 11:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;

/**
 *
 * @author Administrator
 */
public class VolumeMapWrapper {
    private int type;
    public VolumeMap volMap;
    public String host_ip="";
    
    @Override public String toString(){
        if( type == 0 ){
            return volMap.getVolDiskLabel();
        }else if( type == 1 ){
            return SanBootView.res.getString("common.vol") + " : " + volMap.toString();
        }else if( type == 2 ){
            return SanBootView.res.getString("common.selectLatestVer");
        }else if( type == 3 ){
            return volMap.getVolDiskLabel() +" [ " +volMap.getProtectString() +" ]";
        }else { // type == 4
            return volMap.getVolDiskLabel()+" [ " + host_ip + " ]";
        }
    }
    
    /** Creates a new instance of VolumeMapWrapper */
    public VolumeMapWrapper() {
        this( 0 );
    }
    
    public VolumeMapWrapper( String host_ip ) {
        this.type = 4;
        this.host_ip = host_ip;
    }

    public VolumeMapWrapper( int type ){
        this.type = type;
    }

    public void setType( int val ){
        this.type = val;
    }
}
