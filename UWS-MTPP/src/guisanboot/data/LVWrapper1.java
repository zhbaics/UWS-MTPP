/*
 * LVWrapper.java
 *
 * Created on 2007/8/16, PM�12:45
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
public class LVWrapper1 {
    public VolumeMap lv = null;
    public String lvmType="";
    public String fsType="";

    /** Creates a new instance of LVWrapper1 */
    public LVWrapper1( VolumeMap _lv,String _lvmType ) {
        lv = _lv;
        lvmType = _lvmType;
    }

    public void setFsType( String fsType ){
        this.fsType = fsType;
    }

    public String getFsType(){
        return fsType;
    }

    @Override public String toString(){
        return lv.getVolDiskLabel(); //  表示 unix 的 mountpoint
    }
}