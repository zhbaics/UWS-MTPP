/*
 * RestoreNewDiskForUnixThread.java
 *
 * Created on 2007/12/13,��PM 3:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;
import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class RestoreNewDiskForUnixThread implements SlowerLaunch{
    String status="";
    String errormsg="";
    boolean isOK;
    int cnt=0;
    int total;
    RestoreOrigiDiskForUnixWizardDialog dialog; 
    SanBootView view;
    ArrayList diskList;// 新旧盘的恢复关系
    DestAgent host; 
    HashMap map = new HashMap();
    
    /** Creates a new instance of FreshDhcpInfoThread */
    public RestoreNewDiskForUnixThread( 
            RestoreOrigiDiskForUnixWizardDialog _dialog, 
            SanBootView _view,
            ArrayList _diskList,
            DestAgent _host 
    ) {
        dialog = _dialog;
        view = _view;
        diskList = _diskList;
        host = _host;        
        total = diskList.size();
    }
    
    public boolean init(){
        String oldDevName,newDevName;
        String conf;
        DiskForUnix newDisk;
        boolean isOk,ret = true;
        
        StringBuffer errBuf = new StringBuffer();
        StringBuffer diskParm = new StringBuffer();
        
        String ip = host.getIP();
        int port = host.getPort();
        int hid = host.getID();
        
        Iterator iterator = diskList.iterator();
        while ( iterator.hasNext() ){
            DiskBinderForUnix binder = (DiskBinderForUnix)iterator.next();
            oldDevName = binder.old_disk.getDiskDevName();
            newDevName = binder.new_disk.getDiskDevName();
            
            diskParm.append( " " + oldDevName + "=" + newDevName + " " );
            
            cnt++;
            status = SanBootView.res.getString("RestoreOriginalDiskWizardDialog.loadstatus.rstPart")+
                    "( " + SanBootView.res.getString("common.disk") + newDevName + " )";
            //conf = hid + ResourceCenter.CONF_OLDDISK;
            conf = host.getOldDiskConfRelativePath();
            
            isOk = view.initor.mdb.sendNetConf( ip, port, conf );
            if( !isOk ){
                errBuf.append(
                  "\n" + SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.rstPart1")+" : "+
                  view.initor.mdb.getErrorMessage()
                );
                ret = false;
                break;
            }
            
            // 由于partition的时间不好确定，故而一律将超时时间设置为10 hours
            view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
            isOk = view.initor.mdb.rstPartition( ip, port, conf, oldDevName, newDevName ); 
            view.initor.mdb.restoreOldTimeOut();
            
            if( !isOk ){
                if( view.initor.mdb.getErrorCode() == FormatPartitionOnDiskForUnix.Error_99 ){
SanBootView.log.error( getClass().getName(), "Retcode is 99 when restoring partition. User must reboot host and then retry this operation.");                    
                    errBuf.append(
                      "\n"+SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.rstPart2")
                    );
                }else{
                    errBuf.append(
                      "\n"+SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.rstPart")+" : "+
                      view.initor.mdb.getErrorMessage()
                    );
                }
                
                ret = false;
                break;
            }
            
            cnt++;
            status = SanBootView.res.getString("RestoreOriginalDiskWizardDialog.loadstatus.formatPart")+
                     "( " + SanBootView.res.getString("common.disk") + newDevName + " )";
            
            // 开始格式化本地硬盘
            // 由于format的时间不好确定，故而一律将超时时间设置为10 hours
            view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
            isOk = view.initor.mdb.formatDiskPartForUnix( ip, port, conf, oldDevName, newDevName ); 
            view.initor.mdb.restoreOldTimeOut();
            
            if( !isOk ){
                if( view.initor.mdb.getErrorCode() == FormatPartitionOnDiskForUnix.Error_99 ){
SanBootView.log.error( getClass().getName(), "Retcode is 99 when formatting partition. User must reboot host and then retry this operation.");                    
                    errBuf.append(
                      "\n"+SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.formatPart3")
                    );
                }else{
                    errBuf.append(
                      "\n" + SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.formatPart")+" : "+
                      view.initor.mdb.getErrorMessage()
                    );
                }
                ret = false;
                break;
            }
            
            newDisk = view.initor.mdb.getMpFromFormatPart1();
            if( newDisk != null ){
SanBootView.log.debug(getClass().getName(), " New Disk(Partition): "+newDisk.prtMe() );
                genRstMaper( newDisk );
            }
        }
        
        // 开始格式化lv ( 格式化lv不需要在循环中，只要一次即可 )
        // 由于format的时间不好确定，故而一律将超时时间设置为10 hours
        view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
        //conf = hid + ResourceCenter.CONF_OLDDISK;
        conf = host.getOldDiskConfRelativePath();
        isOk = view.initor.mdb.formatDiskPartForUnix1( ip, port, conf, diskParm.toString() ); 
        view.initor.mdb.restoreOldTimeOut();
        
        if( !isOk ){
            if( view.initor.mdb.getErrorCode() == FormatPartitionOnDiskForUnix.Error_99 ){
SanBootView.log.error( getClass().getName(), "Retcode is 99 when formatting lv. User must reboot host and then retry this operation.");                    
                    errBuf.append(
                      "\n"+SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.formatPart4")
                    );
            }else{
                errBuf.append(
                  "\n" + SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.formatPart")+" : "+
                  view.initor.mdb.getErrorMessage()
                );
            }
            ret = false;
        }else{
            newDisk = view.initor.mdb.getMpFromFormatPart2();
            if( newDisk != null ){
SanBootView.log.debug(getClass().getName()," New LV: " + newDisk.prtMe() );
                genRstMaper( newDisk );
            }
        }
        
        //errormsg = SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.rstDisk") + errBuf.toString();
        errormsg = SanBootView.res.getString("RestoreOriginalDiskWizardDialog.error.rstDisk1");
         
        isOK = ret;
        return ret;
    }
    
    public String getLoadingStatus(){
        return status;
    }
    
    public int getLoadingProcessVal(){
        int val = (cnt*100)/(total*2);
        if( val >=100 ){
            val = 99;
        }
        return val;
    }
     
    public String getInitErrMsg(){
        return errormsg;
    }
    public boolean isSuccessfully(){
        return isOK;
    }
    
    public String getSrvIP(){
        return "";
    }
    public boolean isCrtVG(){
        return true;
    }
    
    private void genRstMaper( DiskForUnix newDisk ){
        String oldMp,newMp,devName,label;
        RestoreMapper mapper;
        DestDevice dest;
        
        List list = newDisk.getPartitionList();
        Iterator iterator = list.iterator();
        while( iterator.hasNext() ){
            SystemPartitionForUnix part = (SystemPartitionForUnix)iterator.next();
            if( part.isSwap() ){
                oldMp = ResourceCenter.SWAP_MP;
                newMp = part.dev_path;
                devName = part.dev_path;
                label = "";
            }else{
                oldMp = part.mp;
                newMp = part.new_mp;
                devName = part.dev_path;
                label = part.label;
            }
            
            if( !oldMp.equals("") ){
                if( map.get( oldMp ) == null ){
                    dest = new DestDevice( newMp, devName );
                    mapper = new RestoreMapper( oldMp,dest,label );
                    map.put( oldMp, mapper );
                }
            }
        }
    }
    
    public ArrayList getNewRstRelationship(){
        return diskList;
    }
    
    public HashMap getRstMapper(){
        return map;
    }
}