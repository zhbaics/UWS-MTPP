/*
 * BindOfPartandVol.java
 *
 * Created on 2007/1/4,��PM 7:46
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.cluster.entity.AccessPath;
import guisanboot.res.ResourceCenter;

/**
 *
 * @author Administrator
 */
public class BindOfPartandVol {
    public SystemPartitionForWin part = null;
    public boolean isProtected = true ;
    public boolean isFormatted = false;
    public int action = 0; // 0: crt 1: sel
    public boolean isRealVol = false;
    public String volName="";
    public Volume vol = null;
    public String volSize="";
    public int blkSize=17;
    public int poolid=-999;
    public int targetID=-1;  // targetID不为 -1表示创建成功
    public int rootID=-1; 
    //public String maxSnap = "32"; 
    public String maxSnap = ResourceCenter.MAX_SNAP_NUM+"";
    public String desc=""; 
    public int ptype = BootHost.PROTECT_TYPE_CMDP;
    public AccessPath ap = null;
    
        //ucs连续保护信息
    public boolean isucsprotected = false ;
    public int ucsLatestPoolid = -1 ;
    public int ucsOldestPoolid = -1 ;
    public int ucsLogPoolid = -1 ;
    public int ucsLogNumber = 10 ;
    public String ucsLogMaxSize = "" ;
    public String ucsLogMaxTime = "" ;
    public boolean isclusger = false;

    /** Creates a new instance of BindOfPartandVol */
    public BindOfPartandVol() {
    }
    
    public String getMaxSnap(){
        if( isRealVol ){
            return maxSnap;
        }else{
            if( ResourceCenter.MAX_SNAP_NUM <=32 ){
                return ResourceCenter.MAX_SNAP_NUM+"";
            }else{ // > 32
                return "4";   //return "32";
            }
        }
    }

    public boolean isProtectedByCMDP(){
        return ( this.ptype == BootHost.PROTECT_TYPE_CMDP );
    }
    
    public boolean isProtectedByMTPP(){
        return ( this.ptype == BootHost.PROTECT_TYPE_MTPP );
    }

    public boolean isLocalDisk(){
        return this.ap.isLocal;
    }
    public boolean isSharedDisk(){
        return !this.ap.isLocal;
    }

    public BindOfPartandVol cloneMe(){
        BindOfPartandVol newOne = new BindOfPartandVol();
        newOne.part = this.part;
        newOne.isProtected = this.isProtected;
        newOne.isFormatted = this.isFormatted;
        newOne.action = this.action;
        newOne.isRealVol = this.isRealVol;
        newOne.volName = this.volName;
        newOne.vol = this.vol;
        newOne.volSize = this.volSize;
        newOne.blkSize = this.blkSize;
        newOne.poolid = this.poolid;
        newOne.targetID = this.targetID;
        newOne.rootID = this.rootID;
        newOne.maxSnap = this.maxSnap;
        newOne.desc = this.desc;
        newOne.ptype = this.ptype;
        newOne.ap = this.ap;
        return newOne;
    }
}
