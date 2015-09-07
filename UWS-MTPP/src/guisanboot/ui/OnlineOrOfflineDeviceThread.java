/*
 * OnlineOrOfflineDeviceThread.java
 *
 * Created on 2008/6/25,��PM 2:53
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;
import guisanboot.data.BasicVDisk;
import guisanboot.data.Volume;
import guisanboot.res.ResourceCenter;

/**
 *
 * @author zjj
 */
public class OnlineOrOfflineDeviceThread extends BasicGetSomethingThread{
    final public static int ACT_ONLINE    = 0;
    final public static int ACT_OFFLINE   = 1;
    
    int rootid;
    int snapid;
    
    // 0: online 1: offline
    int act;
    
    /** Creates a new instance of OnlineOrOfflineDeviceThread */
    public OnlineOrOfflineDeviceThread(
        SanBootView view,
        int rootid,
        int snapid,
        int act
    ) {
        super( view, false );
        
        this.rootid = rootid;
        this.snapid = snapid;
        this.act = act;
    }
    
    public boolean realRun(){        
        boolean isOK = true;
        
        if( snapid == -1  ){
            isOK = view.initor.mdb.queryVSnapDB(
                "select * from " + ResourceCenter.VSnap_DB +" where (" +BasicVDisk.BVDisk_Snap_OpenType +"="+
                    BasicVDisk.TYPE_OPENED_DISK +" or "+BasicVDisk.BVDisk_Snap_OpenType +"="+
                    BasicVDisk.TYPE_OPENED_AppMirr+" or "+BasicVDisk.BVDisk_Snap_OpenType +"="+
                    BasicVDisk.TYPE_OPENED_UCSDISK+") "+" and "+ BasicVDisk.BVDisk_Snap_Root_ID +"="+rootid+";"
            );
            
            if( !isOK ){
                this.cmd = ResourceCenter.CMD_SNAPDB_SQL_QUEUE;
                this.errcode = view.initor.mdb.getErrorCode();
                this.errMsg = view.initor.mdb.getErrorMessage();
            }else{
                Volume vol = view.initor.mdb.getQueryedVolume( rootid );
                if( vol == null ){
SanBootView.log.error( getClass().getName(),"Can't find volume(disk) from server on rootid: " + rootid ); 
                    if( act == ACT_ONLINE ){
                        this.cmd = ResourceCenter.CMD_ONLINE_DEV;
                    }else{
                        this.cmd = ResourceCenter.CMD_OFFLINE_DEV;
                    }
                    this.errcode = 255;
                    this.errMsg = SanBootView.res.getString("common.error.noVol");
                    isOK = false;
                }else{
                    snapid = vol.getSnap_local_snapid();
                }
            }
        }
        
        if( isOK ){
            if( act == ACT_ONLINE ){
                isOK = view.initor.mdb.onlineVolume( rootid, snapid );
                if( !isOK ){
                    this.cmd = ResourceCenter.CMD_ONLINE_DEV;
                    this.errcode = view.initor.mdb.getErrorCode();
                    this.errMsg = view.initor.mdb.getErrorMessage();
                }
            }else{
                isOK = view.initor.mdb.offlineVolume( rootid,snapid );
                if( !isOK ){
                    this.cmd = ResourceCenter.CMD_OFFLINE_DEV;
                    this.errcode = view.initor.mdb.getErrorCode();
                    this.errMsg = view.initor.mdb.getErrorMessage();
                }
            }
        }
        
        return isOK;
    }
}
