/*
 * DestDevice.java
 *
 * Created on 2008/1/11, PM�4:26
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

/**
 *
 * @author Administrator
 */
public class DestDevice {
    private String mp;
    // 对windows来说 volName为 \\?\Volume{d0faa43b-bddd-11dc-a882-000c29d3c475}
    // 对linux来说 volName为 /dev/sda1
    private String volName; 
    
    /** Creates a new instance of DestDevice */
    public DestDevice() {
    }
    
    public DestDevice( String mp ){
        this.setMp( mp );
    }
    
    public DestDevice( String mp,String volName ){
        this.setMp(mp);
        this.setVolName(volName);
    }

    public String getMp() {
        return mp;
    }

    public void setMp(String mp) {
        this.mp = mp;
    }

    public String getVolName() {
        return volName;
    }

    public void setVolName(String volName) {
        this.volName = volName;
    }
    
    @Override public String toString(){
        return this.mp;
    }
}
