/*
 * BindOfUnixPartandLV.java
 *
 * Created on 2007/1/4,��PM�7:46
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;

/**
 *
 * @author Administrator
 */
public class BindOfUnixPartandLV {
    public SystemPartitionForUnix part = null;
    public boolean isProtected = true ;
    public boolean isFormatted = false;
    public int action=0;      // 0: crt 1: sel
    public boolean isRealLV = false;
    public String lvName="";  // Logical volume的名字
    public VolumeMap lv = null;      // lv对象
    public String tgtName=""; //组成vg的tgt的名字������
    public Volume rawTgt = null;  // 即将成为pv的原始target信息
    public String tgtSize="";  // 这里为原始target的大小，不知道实际lv的大小
    public int blkSize=17;    // block size
    public int poolid=-999;   // pool id
    public int targetID=-1;    // targetID不为 -1表示创建成功
    public int rootID=-1;   // snap root id
    //public String maxSnap = "32"; 
    public String maxSnap = ResourceCenter.MAX_SNAP_NUM+"";
    public String lvType="";   // LVM type
    public String snapSapce="10";  // VG的快照空间的大小,大小为target的百分比,缺省为10%

    /**
     *  ams
     */
    public String desc=""; 
    public boolean isRealVol = false;
    public int ptype = BootHost.PROTECT_TYPE_MTPP;
    
     //ucs连续保护信息
    public boolean isucsprotected = false ;
    public int ucsLatestPoolid = -1 ;
    public int ucsOldestPoolid = -1 ;
    public int ucsLogPoolid = -1 ;
    public int ucsLogNumber = 10 ;
    public String ucsLogMaxSize = "" ;
    public String ucsLogMaxTime = "" ;

    
    /** Creates a new instance of BindOfPartandVol */
    public BindOfUnixPartandLV() {
    }
    
    public String getMaxSnap(){
        if( isRealLV ){
            return maxSnap;
        }else{
            if( ResourceCenter.MAX_SNAP_NUM <=32 ){
                return ResourceCenter.MAX_SNAP_NUM+"";
            }else{ // > 32
                return "32";
            }
        }
    }

    public boolean isProtectedByMTPP(){
        return ( this.ptype == BootHost.PROTECT_TYPE_MTPP );
    }

}
