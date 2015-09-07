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
public class LVWrapper {
    public VolumeMap lv;
    public Volume rawTgt;
    public String lvmType;
    public String snapSpace;
    
    /** Creates a new instance of LVWrapper */
    public LVWrapper( VolumeMap _lv,Volume _rawTgt,String _lvmType,String _snapSpace ) {
        lv = _lv;
        rawTgt = _rawTgt;
        lvmType = _lvmType;
        snapSpace = _snapSpace;
    }
    
    @Override public String toString(){
        return lv.getVolName();
    }
}