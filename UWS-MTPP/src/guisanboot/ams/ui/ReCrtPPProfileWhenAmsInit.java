package guisanboot.ams.ui;

import guisanboot.cluster.entity.Cluster;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.cmdp.entity.PPProfileItem;
import guisanboot.data.BindOfUnixPartandLV;
import guisanboot.data.BootHost;
import guisanboot.data.LunMap;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.MirrorGrp;
import guisanboot.data.View;
import guisanboot.data.VolumeMap;
import guisanboot.ui.RunningTaskPane;
import guisanboot.ui.SanBootView;
import guisanboot.ui.WizardDialogSample;
import guisanboot.res.ResourceCenter;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * ReCrtPPProfileWhenInit.java
 *
 * Created on 2010-6-25, PM 17:28
 */
public class ReCrtPPProfileWhenAmsInit {

    SanBootView view;
    BootHost host;
    Cluster cluster;
    RunningTaskPane runPane;
    WizardDialogSample initDiag;
    Vector<VolumeMap> curVolMapList;
    Vector<BindOfUnixPartandLV> toProtectDisks;
    ArrayList<PPProfile> curNewPPPList;
    ArrayList<PPProfile> curPPPList;
    private String errMsg;
    Map<String, Integer> mgidMap;

    public ReCrtPPProfileWhenAmsInit(
        SanBootView view,
        BootHost host,
        Cluster cluster,
        RunningTaskPane runPane,
        WizardDialogSample initDiag,
        Vector<VolumeMap> curVolMapList,
        Vector<BindOfUnixPartandLV> toProtectDisks,
        ArrayList<PPProfile> curNewPPPList,
        ArrayList<PPProfile> curPPPList,
        Map<String, Integer> mgidMap
    ) {
        this.view = view;
        this.host = host;
        this.cluster = cluster;
        this.runPane = runPane;
        this.initDiag = initDiag;
        this.curVolMapList = curVolMapList;
        this.toProtectDisks = toProtectDisks;
        this.curNewPPPList = curNewPPPList;
        this.curPPPList = curPPPList;
        this.mgidMap = mgidMap;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    private PPProfile findPPProfile(PPProfile _prof) {
        int size = this.curPPPList.size();
        for (int i = 0; i < size; i++) {
            PPProfile prof = curPPPList.get(i);
            if ( prof.getDiskList().equals( _prof.getDiskList() ) ) {
                return prof;
            }
        }
        return null;
    }

    private PPProfile findPPProfileAms(PPProfile _prof) {
        int size = this.curPPPList.size();
        for (int i = 0; i < size; i++) {
            PPProfile prof = curPPPList.get(i);
            if ( prof.getDiskListAms().equals( _prof.getDiskList() ) ) {
                return prof;
            }
        }
        return null;
    }

    private boolean containedInSet(String drive, Vector<BindOfUnixPartandLV> disks) {
        int size = disks.size();
        for ( int i=0; i<size; i++ ) {
            BindOfUnixPartandLV disk = disks.elementAt(i);
            if (!disk.isProtected) {
                continue;
            }

            int start = drive.indexOf("_");
            int end = drive.lastIndexOf("_");
            if(start >0 && end >0){
                drive = drive.substring(start+1, end);
            }
            //if ( disk.part.getDiskLabel().substring(0, 1).toUpperCase().equals(drive.toUpperCase() ) ) {
            if ( disk.part.mp.equals(drive) ) {
                return true;
            }
        }
        return false;
    }

    private BindOfUnixPartandLV containedIn(String drive, Vector<BindOfUnixPartandLV> disks) {
        int start = drive.indexOf("_");
        int end = drive.lastIndexOf("_");
        if(start >0 && end >0){
            drive = drive.substring(start+1, end);
        }
        
        int size = disks.size();
        for ( int i=0; i<size; i++ ) {
            BindOfUnixPartandLV disk = disks.elementAt(i);
            if ( disk.part.mp.equals(drive) ) {
                return disk;
            }
        }
        return null;
    }

    private ArrayList<VolumeMap> findDiskToDel() {
        ArrayList<VolumeMap> toDelList = new ArrayList<VolumeMap>();
        int size = this.curVolMapList.size();
        for (int i = 0; i < size; i++) {
            VolumeMap vol = this.curVolMapList.elementAt(i);
            if (!this.containedInSet( vol.getVolName(), toProtectDisks ) ) {
                toDelList.add(vol);
            }
        }
        return toDelList;
    }

    private boolean modMgInfo( String diskList,PPProfileItem oldItem, int sch_type,
            int interval_time,int max_snap,int min_size,String sch_min,String sch_hour,
            String sch_day,String sch_month,String sch_week,String clock_zone,String sch_hour1,
            String sch_clock_set
    ){
        boolean ok;

        if( sch_type == MirrorGrp.MG_SCH_TYPE_ROTATE ){
            ok = view.initor.mdb.modAutoCrtSnapParameter( oldItem.getMg().getMg_id(), sch_type, interval_time, max_snap, min_size );
        }else{
            ok = view.initor.mdb.modAutoCrtSnapParameter1( oldItem.getMg().getMg_id(),sch_type,
             sch_min,sch_hour,sch_day,sch_month,sch_week,clock_zone,sch_hour1,sch_clock_set,max_snap, min_size );
        }
        if( !ok ){
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf1")+" [ "+ diskList +" ]";
            runPane.setLogOnTabpane( errMsg,RunningTaskPane.SAVE_INFO_ROW );
            initDiag.writeLogBuf( errMsg, RunningTaskPane.SAVE_INFO_ROW );
        }else{
            oldItem.getMg().setMg_schedule_type( sch_type );
            oldItem.getMg().setMg_max_snapshot( max_snap );
            oldItem.getMg().setMg_min_snap_size( min_size );
            if( sch_type == MirrorGrp.MG_SCH_TYPE_ROTATE ){
                oldItem.getMg().setMg_interval_time( interval_time );
            }else{
                oldItem.getMg().setMg_schedule_minute( sch_min );
                oldItem.getMg().setMg_schedule_hour( sch_hour );
                oldItem.getMg().setMg_schedule_day( sch_day );
                oldItem.getMg().setMg_schedule_month( sch_month );
                oldItem.getMg().setMg_schedule_week( sch_week );
                oldItem.getMg().setMg_schedule_clock_zone( clock_zone );
                oldItem.getMg().setMg_schedule_hour1( sch_hour1 );
                oldItem.getMg().setMg_schedule_clock_set( sch_clock_set );
            }
        }
        return ok;
    }

    private boolean crtNewDg(ArrayList<PPProfileItem> selDisks) {
        int size = selDisks.size();
        boolean isFirst = true;
        StringBuffer rootids = new StringBuffer();

        for (int i = 0; i < size; i++) {
            PPProfileItem item = selDisks.get(i);
            if (isFirst) {
                rootids.append(item.getVolMap().getVol_rootid() + "");
                isFirst = false;
            } else {
                rootids.append("^" + item.getVolMap().getVol_rootid());
            }
        }

        StringBuffer dg_name = new StringBuffer();
        dg_name.append("dg-");
        Calendar c = new GregorianCalendar();
        int year = c.get(Calendar.YEAR);
        dg_name.append("" + year);
        int month = c.get(Calendar.MONTH) + 1;
        if (month < 10) {
            dg_name.append("0" + month);
        } else {
            dg_name.append("" + month);
        }
        int day_of_month = c.get(Calendar.DAY_OF_MONTH);
        if (day_of_month < 10) {
            dg_name.append("0" + day_of_month);
        } else {
            dg_name.append("" + day_of_month);
        }
        int hour_of_day = c.get(Calendar.HOUR_OF_DAY);
        if (hour_of_day < 10) {
            dg_name.append("0" + hour_of_day);
        } else {
            dg_name.append("" + hour_of_day);
        }
        int minute = c.get(Calendar.MINUTE);
        if (minute < 10) {
            dg_name.append("0" + minute);
        } else {
            dg_name.append("" + minute);
        }
        int sec = c.get(Calendar.SECOND);
        if (sec < 10) {
            dg_name.append("0" + sec);
        } else {
            dg_name.append("" + sec);
        }

        return view.initor.mdb.crtDG(dg_name.toString(), rootids.toString());
    }

    private boolean modInfo( String diskList,PPProfileItem oldProfItem,int interval_time,
        int max_snap,int min_size,int db_type,String db_instances,String services,
        int sch_type, String sch_minute,String sch_hour,String sch_day,String sch_month,
        String sch_week,String clock_zone,String sch_hour1,String sch_clock_set
    ){
        boolean isOk,taskOk = true;

        isOk = this.modMgInfo( diskList, oldProfItem, sch_type,interval_time,max_snap, min_size,
                sch_minute,sch_hour,sch_day,sch_month,sch_week,clock_zone,sch_hour1,sch_clock_set );
        if( !isOk ){
            taskOk = false;
        }

//        // services
//        isOk = view.initor.mdb.modServiceOnVolume(oldProfItem.getVolMap().getVolName(), services);
//        if (!isOk) {
//            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf2") + " [ " + diskList + " ]";
//            runPane.setLogOnTabpane(errMsg, RunningTaskPane.SAVE_INFO_ROW);
//            initDiag.writeLogBuf(errMsg, RunningTaskPane.SAVE_INFO_ROW);
//        } else {
//            oldProfItem.getVolMap().setChangeVerService(services);
//        }
//        if (!isOk) {
//            taskOk = false;
//        }
//
//        String[] lines = Pattern.compile("\\s+").split(db_instances, -1);
//        StringBuffer buf1 = new StringBuffer();
//        boolean isFirst = true;
//        for (int i = 0; i < lines.length; i++) {
//            if (lines[i].equals("")) {
//                continue;
//            }
//            if (isFirst) {
//                buf1.append(lines[i]);
//                isFirst = false;
//            } else {
//                buf1.append("^" + lines[i]);
//            }
//        }
//        // database info
//        isOk = view.initor.mdb.modDbinfoOnVolume(oldProfItem.getVolMap().getVolName(), db_type, buf1.toString());
//        if (!isOk) {
//            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf3") + " [ " + diskList + " ]";
//            runPane.setLogOnTabpane(errMsg, RunningTaskPane.SAVE_INFO_ROW);
//            initDiag.writeLogBuf(errMsg, RunningTaskPane.SAVE_INFO_ROW);
//        } else {
//            oldProfItem.getVolMap().setDBType(db_type);
//            oldProfItem.getVolMap().setDatabase_instances(db_instances);
//        }
//        if (!isOk) {
//            taskOk = false;
//        }

        return taskOk;
    }

    private void adjustProfileDiskElement(PPProfile newProf) {
        VolumeMap volMap;
        ArrayList<PPProfileItem> elements = newProf.getElements();
        int size = elements.size();
        for (int i = size - 1; i >= 0; i--) {
            PPProfileItem item = elements.get(i);
            if( host != null ){
                volMap = view.initor.mdb.getVolMapFromVecOnClntandLabel( host.getID(), item.getVolMap().getVolDiskLabel() );
            }else{
                volMap = view.initor.mdb.getVolMapFromVecOnClusterAndLabel( cluster.getCluster_id(), item.getVolMap().getVolDiskLabel() );
            }
            if (volMap == null) {
                elements.remove(i);
            } else {
                item.getVolMap().setVol_rootid(volMap.getVol_rootid());
            }
        }
    }

    public boolean realRun() {
        BootHost aHost;
        PPProfile newProf, oldProf,prof;
        PPProfileItem newProfItem, oldProfItem;
        int i, size, delDiskSize, delOldSize, crtNewSize;
        boolean isOk, taskOk = true;

        ArrayList<VolumeMap> toDelList = this.findDiskToDel();
        delDiskSize = toDelList.size();
SanBootView.log.info(getClass().getName(), "find volumes to delete, num# of del-vol is: " + delDiskSize );

        for ( i = 0; i < delDiskSize; i++ ) {
            VolumeMap toDelVol = toDelList.get( i );
            if( cluster != null ){
                aHost = view.initor.mdb.getBootHostFromVector( toDelVol.getVolClntID() );
            }else{
                aHost = host;
            }
            
            //toDelVol: vol_name=z7_/mnt/teset2_198.88.88.134 vol_target_id=32775 vol_desc=TGT vol_mgid=544 vol_rootid=1133
            String amsArg = " -I " + ResourceCenter.ISCSI_PREFIX + view.initor.mdb.getHostName() + " -S "+ view.initor.getTxIP(aHost.getIP()) + " -P "+ ResourceCenter.ISCSI_LOGIN_PORT + " -t " + toDelVol.getVolTargetID() ;
            isOk = view.initor.mdb.amsGetDiskPath(aHost.getIP(), aHost.getPort(), amsArg);
            if( isOk ){
                amsArg = view.initor.mdb.getDiskPath();
                isOk = view.initor.mdb.delAmsProtect( aHost.getIP(), aHost.getPort(), amsArg);
            }
            
            if (!isOk) {
                errMsg = SanBootView.res.getString("InitBootHostWizardDialog.log.delProtect") + " " + toDelVol.getVolDiskLabel() + " [ " + aHost.getIP() +"/" + aHost.getPort() +" ] " +SanBootView.res.getString("common.failed");
                runPane.setLogOnTabpane(errMsg, RunningTaskPane.SAVE_INFO_ROW);
                initDiag.writeLogBuf(errMsg, RunningTaskPane.SAVE_INFO_ROW);
                taskOk = false;
            } else {
                BindOfUnixPartandLV lv = this.containedIn( toDelVol.getVolName(), this.toProtectDisks );
                if( lv != null){
                        /**
                         * 删除lunmapping并使它挂在空闲卷
                         */
                        VolumeMap vol = new VolumeMap();
                        vol.setVolDiskLabel(lv.part.mp);
                        vol.setVolTargetID(toDelVol.getVolTargetID());
                        vol.setVol_protect_type( BootHost.PROTECT_TYPE_CMDP);
                        vol.setVol_rootid(vol.getVol_rootid());
                        vol.setVolClntID(host.getID());
                        vol.setVolName(lv.lvName);
                        lv.lv = vol;

                        isOk = delVol( host,  lv);
                        if( !isOk ){
                            SanBootView.log.error( getClass().getName(), "删除到空闲卷失败:" + "[" +  lv.part.mp + "-" + lv.part.dev_path + "]");
                        }
                }
                if (!isOk) {
                    errMsg = SanBootView.res.getString("InitBootHostWizardDialog.log.delProtect") + " " + toDelVol.getVolDiskLabel() + " [ " + aHost.getIP() +"/" + aHost.getPort() +" ] " + SanBootView.res.getString("common.failed");
                    runPane.setLogOnTabpane(errMsg, RunningTaskPane.SAVE_INFO_ROW);
                    initDiag.writeLogBuf(errMsg, RunningTaskPane.SAVE_INFO_ROW);
                    taskOk = false;
                } else {
                    runPane.setLogOnTabpane(
                        SanBootView.res.getString("InitBootHostWizardDialog.log.delProtect") + " " + toDelVol.getVolDiskLabel() + " [ " + aHost.getIP() +"/" + aHost.getPort() +" ] " +
                        SanBootView.res.getString("common.ok"),
                        RunningTaskPane.SAVE_INFO_ROW
                    );
                }
            }
        }

        // 按照新profile的disk list组合, 从旧的ppprofile中找出对应的profile，对其进行其他内容的修改
        size = this.curNewPPPList.size();
        for ( i = size - 1; i >= 0; i-- ) {
            newProf = this.curNewPPPList.get(i);
            oldProf = this.findPPProfileAms(newProf);
            if ( oldProf != null ) {
                oldProfItem = oldProf.getMainDiskItem();

                if( !oldProfItem.isSameProfileItem(
                    newProf.getTemp_interval_time(),
                    newProf.getTemp_max_snap(),
                    newProf.getTemp_min_size(),
                    newProf.getTemp_db_type(),
                    newProf.getTemp_db_instance(),
                    newProf.getTemp_services(),
                    newProf.getTemp_mg_schedule_type(),
                    newProf.getTemp_mg_schedule_minute(),
                    newProf.getTemp_mg_schedule_hour(),
                    newProf.getTemp_mg_schedule_day(),
                    newProf.getTemp_mg_schedule_month(),
                    newProf.getTemp_mg_schedule_week(),
                    newProf.getTemp_mg_schedule_clock_zone(),
                    newProf.getTemp_mg_schedule_hour1(),
                    newProf.getTemp_mg_schedule_clock_set()
                ) ){
                    // 修改其他的信息，比如 min_size,max_snap 等
                    isOk =  this.modInfo( oldProf.getDiskList(),
                        oldProfItem,
                        newProf.getTemp_interval_time(),
                        newProf.getTemp_max_snap(),
                        newProf.getTemp_min_size(),
                        newProf.getTemp_db_type(),
                        newProf.getTemp_db_instance(),
                        newProf.getTemp_services(),
                        newProf.getTemp_mg_schedule_type(),
                        newProf.getTemp_mg_schedule_minute(),
                        newProf.getTemp_mg_schedule_hour(),
                        newProf.getTemp_mg_schedule_day(),
                        newProf.getTemp_mg_schedule_month(),
                        newProf.getTemp_mg_schedule_week(),
                        newProf.getTemp_mg_schedule_clock_zone(),
                        newProf.getTemp_mg_schedule_hour1(),
                        newProf.getTemp_mg_schedule_clock_set()
                    );
                    if( !isOk ){
                        taskOk = false;
                    }

                    // 再修改其他成员盘的若干属性( max_snap,min_inc_data )
                    isOk = modifyForNonMainDisk( oldProf,newProf.getTemp_max_snap(),newProf.getTemp_min_size() );
                    if( !isOk ){
                        taskOk = false;
                    }

                    // 修改minioverload mode属性
                    isOk = this.modifyForMiniOverloadMode( oldProf,newProf.isMiniOverloadMode() );
                    if( !isOk ){
                        taskOk = false;
                    }
                }

                // 从cache中去掉这个profile
                this.curPPPList.remove(oldProf);
                this.curNewPPPList.remove(newProf);
            }else{   //第一次ams初始化时 add by:hwh
                newProf = this.curNewPPPList.get(i);
                oldProf = newProf;
                oldProfItem = oldProf.getMainDiskItem();
                //对mg_id进行赋值
                ArrayList<PPProfileItem> elements = oldProf.getElements();
                int elesize = elements.size();
                for( int elsei=0; elsei<elesize; elsei++ ){
                    String diskLabel = elements.get(elsei).getVolMap().getVolName();    
                    Set<String> set = mgidMap.keySet();
                    Iterator<String> it= set.iterator();
                    while( it.hasNext()){
                        String itkey = it.next();
                        int start = itkey.indexOf("_");
                        int end = itkey.lastIndexOf("_");
                        if(start >0 && end >0){
                            if( itkey.substring(start+1, end).equals(diskLabel) ){
                                oldProfItem.getMg().setMg_id(mgidMap.get(itkey));

                                isOk =  this.modInfo( oldProf.getDiskList(),
                                    oldProfItem,
                                    newProf.getTemp_interval_time(),
                                    newProf.getTemp_max_snap(),
                                    newProf.getTemp_min_size(),
                                    newProf.getTemp_db_type(),
                                    newProf.getTemp_db_instance(),
                                    newProf.getTemp_services(),
                                    newProf.getTemp_mg_schedule_type(),
                                    newProf.getTemp_mg_schedule_minute(),
                                    newProf.getTemp_mg_schedule_hour(),
                                    newProf.getTemp_mg_schedule_day(),
                                    newProf.getTemp_mg_schedule_month(),
                                    newProf.getTemp_mg_schedule_week(),
                                    newProf.getTemp_mg_schedule_clock_zone(),
                                    newProf.getTemp_mg_schedule_hour1(),
                                    newProf.getTemp_mg_schedule_clock_set()
                                );
                                if( !isOk ){
                                    taskOk = false;
                                }
                            }
                        }
                    }
                }
       
            }
        }

        // 删除剩下的old profile
        delOldSize = this.curPPPList.size();
        SanBootView.log.info(getClass().getName(), "find old profiles to delete, num of del-profile is: " + delOldSize);
        for (i = 0; i < delOldSize; i++) {
            oldProf = this.curPPPList.get(i);
            if (!oldProf.isValidDriveGrp()) {
                continue;
            }

            isOk = view.initor.mdb.delDG(
                    oldProf.getDriveGrpName(),
                    oldProf.getMainDiskItem().getVolMap().getVol_rootid() + "",
                    oldProf.getMainDiskItem().getVolMap().getGroupinfodetail());
            if (!isOk) {
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf4") + " [ " + oldProf.getDiskList() + " ]";
                runPane.setLogOnTabpane(errMsg, RunningTaskPane.SAVE_INFO_ROW);
                initDiag.writeLogBuf(errMsg, RunningTaskPane.SAVE_INFO_ROW);
            }

            if (!isOk) {
                taskOk = false;
            }
        }

        // 生成全新的profile
        crtNewSize = this.curNewPPPList.size();
        SanBootView.log.info(getClass().getName(), "find new profiles to create, size of crt-profile is: " + crtNewSize);

        for (i = 0; i < crtNewSize; i++) {
            newProf = this.curNewPPPList.get(i);
            // 根据build mirror的结果再对curNewPPPList中的profile的磁盘成员进行调整
            adjustProfileDiskElement(newProf);
            if (!newProf.isValidDriveGrp1()) {
                continue;
            }

            isOk = this.crtNewDg(newProf.getElements());
            if (!isOk) {
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf5") + " [ " + newProf.getDiskList() + " ]";
                runPane.setLogOnTabpane(errMsg, RunningTaskPane.SAVE_INFO_ROW);
                initDiag.writeLogBuf(errMsg, RunningTaskPane.SAVE_INFO_ROW);
                taskOk = false;
            }
        }

        // 不管发生什么磁盘变化，都更新一遍下面的东东。因为就算磁盘组成没有发生变化，其他属性(快照频率等)
        // 也可能变化，另外mg中的pid也随时会变化，所以统统更新一遍。(2010.11.23)
        //if(  delDiskSize>0 || delOldSize>0 || crtNewSize>0  ){
        // 刷新volume信息
        if (!view.initor.mdb.updateVolumeMap()) {
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf6");
            runPane.setLogOnTabpane(errMsg, RunningTaskPane.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPane.SAVE_INFO_ROW);
            taskOk = false;
        }
        // 刷新mg信息,因为mg里面记录了start_mirror进程的pid，这个pid有可能会变化
        if (!view.initor.mdb.updateMg()) {
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf9");
            runPane.setLogOnTabpane(errMsg, RunningTaskPane.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPane.SAVE_INFO_ROW);
            taskOk = false;
        }
        // 刷新dg信息
        if (!view.initor.mdb.updateDg()) {
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf7");
            runPane.setLogOnTabpane(errMsg, RunningTaskPane.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPane.SAVE_INFO_ROW);
            taskOk = false;
        }
        // 刷新ppprofile
        if (!view.initor.mdb.updatePPProfileList()) {
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf8");
            runPane.setLogOnTabpane(errMsg, RunningTaskPane.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPane.SAVE_INFO_ROW);
            taskOk = false;
        }
        //}

        if (crtNewSize > 0) {
            // 更新新创建的profile的其他信息
            for (i = 0; i < crtNewSize; i++) {
                newProf = this.curNewPPPList.get(i);

                if( host != null ){
                    prof = view.initor.mdb.getPPProfile( host.getID(), PPProfile.getDiskList(newProf.getElements()) );
                }else{
                    prof = view.initor.mdb.getPPProfile( cluster.getCluster_id(), PPProfile.getDiskList(newProf.getElements() ));
                }
                if (prof != null) {
                    newProfItem = prof.getMainDiskItem();
                    isOk = this.modInfo( prof.getDiskList(), newProfItem,
                        newProf.getTemp_interval_time(),
                        newProf.getTemp_max_snap(),
                        newProf.getTemp_min_size(),
                        newProf.getTemp_db_type(),
                        newProf.getTemp_db_instance(),
                        newProf.getTemp_services(),
                        newProf.getTemp_mg_schedule_type(),
                        newProf.getTemp_mg_schedule_minute(),
                        newProf.getTemp_mg_schedule_hour(),
                        newProf.getTemp_mg_schedule_day(),
                        newProf.getTemp_mg_schedule_month(),
                        newProf.getTemp_mg_schedule_week(),
                        newProf.getTemp_mg_schedule_clock_zone(),
                        newProf.getTemp_mg_schedule_hour1(),
                        newProf.getTemp_mg_schedule_clock_set()
                    );
                    if( !isOk ){
                        taskOk = false;
                    }

                    // 再修改其他成员盘的若干属性( max_snap,min_inc_data )
                    isOk = modifyForNonMainDisk( prof,newProf.getTemp_max_snap(),newProf.getTemp_min_size() );
                    if( !isOk ){
                        taskOk = false;
                    }

                    // 修改minioverload mode属性
                    isOk = this.modifyForMiniOverloadMode( prof,newProf.isMiniOverloadMode() );
                    if( !isOk ){
                        taskOk = false;
                    }
                }else{
SanBootView.log.warning(getClass().getName(),"can't find physical protect profile according clntid: " + host.getID()  + " and "+PPProfile.getDiskList( newProf.getElements() ) );
                }
            }
        }

        // 重新通知start mirror进程 (2010.11.23 )
        if( host != null ){
            isOk = view.initor.mdb.reloadMgConfig( host.getID() );
        }else{
            isOk = view.initor.mdb.reloadMgConfig( cluster.getCluster_id() );
        }
        if (!isOk) {
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf10");
            runPane.setLogOnTabpane(errMsg, RunningTaskPane.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPane.SAVE_INFO_ROW);
            taskOk = false;
        }

        return taskOk;
    }

    private boolean modifyForNonMainDisk(PPProfile oldProf, int temp_max_snap, int temp_min_size) {
        boolean isOk, taskOk = true;

        ArrayList<PPProfileItem> itemList = oldProf.getNonMainDiskItemList();
        int size = itemList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                PPProfileItem item = itemList.get(i);
                isOk = view.initor.mdb.modAutoCrtSnapParameter3(item.getMg().getMg_id(), temp_max_snap, temp_min_size);
                if (!isOk) {
                    taskOk = false;
                } else {
                    item.getMg().setMg_max_snapshot(temp_max_snap);
                    item.getMg().setMg_min_snap_size(temp_min_size);
                }
            }
            return taskOk;
        } else {
            return true;
        }
    }

    private boolean modifyForMiniOverloadMode( PPProfile prof,boolean minioverload_mode ){
        boolean aIsOk,taskOk = true;

        ArrayList<PPProfileItem> itemList = prof.getElements();
        int size = itemList.size();
        if( size > 0 ){
            for( int i=0; i<size; i++ ){
                PPProfileItem item = itemList.get( i );
                item.getVolMap().setAutoAsyncAftCrtSnap( minioverload_mode );
                aIsOk = view.initor.mdb.modOneVolumeMap7( item.getVolMap() );
                if( !aIsOk ){
                    taskOk = false;
                }
            }
            return taskOk;
        }else{
            return true;
        }
    }

    private boolean delVol(BootHost host,  BindOfUnixPartandLV binder){
        // 删除 tgt volume map
        boolean isOk = true;
        String tgtSrvName = view.initor.mdb.getHostName();
        if( tgtSrvName.equals("") )
            return false;
        String iscsiVar = ResourceCenter.ISCSI_PREFIX + tgtSrvName;

        String args = " -g " + binder.lv.getVolDiskLabel();
        // delete real vg
        isOk = view.initor.mdb.delVg( host.getIP(), host.getPort(), args );
        if( !isOk ){
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_VG ) + " : " + view.initor.mdb.getErrorMessage();
            SanBootView.log.error( getClass().getName(), errMsg);
            return false;
        }else{
            // 将target logout出去
            args = " -I "+iscsiVar +" -S "+ view.initor.getTxIP(host.getIP()) + " -P "+ ResourceCenter.ISCSI_LOGIN_PORT + " -t "+binder.targetID;
            isOk = view.initor.mdb.logoutUnixTarget(  host.getIP(), host.getPort(),args );
            if( !isOk ){
                errMsg =  ResourceCenter.getCmdString( ResourceCenter.CMD_LOGOUTTARGET ) + " : " + view.initor.mdb.getErrorMessage();
                SanBootView.log.error( getClass().getName(),errMsg);
                return false;
            }

            // del lunmap on tgt
            if( !delLunMapOnVol(  host.getIP(), host.getPort(),binder.lv ) ){ return false; }

            // 删除disk上所有快照的view的lunmap
            if( !delLunMapOnView( binder.lv ) ){ return false; }

            // 删除clonedisk及其view上的lunmap
            if( !delLunMapOnCloneDisk( host.getID(),binder.lv.getVol_rootid() ) ){ return false;}


            // 修改UIMVol的属性，使之归结到空闲卷上
            if( !this.modUIMVolOnVolMap(binder.lv.getVolClntID(),binder.lv.getVol_rootid(),1 ) ){ return false;}

            // 修改clonedisk的属性，使之归结到空闲卷上
            if( !this.modCloneDiskOnVolMap(binder.lv.getVol_rootid(), binder.lv.getVolClntID(), 1) ){ return false;}

            // del tgt volumemap object
            isOk = view.initor.mdb.delVolumeMap( binder.lv );
            if( !isOk ){
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_VOLMAP ) + "(TGT) : " +  view.initor.mdb.getErrorMessage();
                SanBootView.log.error( getClass().getName(),errMsg);
                return false;
            }else{
                view.initor.mdb.removeOneVolMapFromVec( binder.lv );
            }
        }
        return true;
    }

    private boolean delLunMapOnVol( String ip,int port,VolumeMap tgt ){
        LunMap lm;

        String iscsiVar = "";
        if(tgt.isCMDPProtect() ){
            String tgtSrvName = view.initor.mdb.getHostName();
            if( tgtSrvName.equals("") ) return false;
            iscsiVar = ResourceCenter.ISCSI_PREFIX + tgtSrvName + ":" + tgt.getVolTargetID();
        }

//        if( tgt.isCMDPProtect() ){
//            // 先把target logout出去,不管是否成功。反正下面还会del lunmap。之所以要先试图logout,
//            // 是因为clw说直接del lunmap会对操作系统有影响
//            view.initor.mdb.logoutTarget( ip, port, iscsiVar, tgt.getVolDiskLabel().substring( 0,1 ), ResourceCenter.CMD_TYPE_CMDP );
//        }

        boolean isOK = view.initor.mdb.getLunMapForTID( tgt.getVolTargetID() );
        if( isOK ){
            Vector lmList = view.initor.mdb.getAllLunMapForTid();
            int size1 = lmList.size();
            for( int j=0;j<size1;j++ ){
                lm = (LunMap)lmList.elementAt(j);
                isOK = view.initor.mdb.delLunMap( tgt.getVolTargetID(), lm.getIP(),lm.getMask(), lm.getAccessMode() );
                if( !isOK ){
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_LUNMAP) + " "+ tgt.getVolTargetID() +" "+lm.getIP() +" "+lm.getMask() +" "+lm.getAccessMode() +" "+ view.initor.mdb.getErrorMessage();
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_LUNMAP) + " [ " +tgt.getVolName() + " ] "+ view.initor.mdb.getErrorMessage();
            SanBootView.log.error( getClass().getName(),errMsg);
            return false;
        }

        return true;
    }

    private boolean delLunMapOnView( VolumeMap tgt ){
        int k,j,size2,size1;
        Vector lmList;
        LunMap lm;

        boolean isOK = view.initor.mdb.getAllView( tgt.getVol_rootid() );
        if( isOK ){
            ArrayList viewList = view.initor.mdb.getViewList();
            size2 = viewList.size();
            for( k=0; k<size2; k++ ){
                View viewObj = (View)viewList.get( k );
                isOK = view.initor.mdb.getLunMapForTID( viewObj.getSnap_target_id() );
                if( isOK ){
                    lmList = view.initor.mdb.getAllLunMapForTid();
                    size1 = lmList.size();
                    for( j=0;j<size1;j++ ){
                        lm = (LunMap)lmList.elementAt(j);
                        isOK = view.initor.mdb.delLunMap( viewObj.getSnap_target_id(), lm.getIP(),lm.getMask(), lm.getAccessMode() );
                        if( !isOK ){
                            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_LUNMAP) + " "+ viewObj.getSnap_target_id() +" "+lm.getIP() +" "+lm.getMask() +" "+lm.getAccessMode() +" "+ view.initor.mdb.getErrorMessage();
                            SanBootView.log.error( getClass().getName(),errMsg);
                            return false;
                        }
                    }
                }else{
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_LUNMAP) + " [ " +viewObj.getSnap_target_id() + " ] "+  view.initor.mdb.getErrorMessage();
                    SanBootView.log.error( getClass().getName(),errMsg);
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_VIEW) + " "+ view.initor.mdb.getErrorMessage();
            SanBootView.log.error( getClass().getName(),errMsg);
            return false;
        }

        return true;
    }

    private boolean delLunMapOnCloneDisk( int host_id,int rootid ){
        CloneDisk cd;

        boolean aIsOk = view.initor.mdb.getCloneDiskList( host_id ,CloneDisk.IS_BOOTHOST,rootid );
        if( aIsOk ){
            ArrayList list = view.initor.mdb.getCloneDiskList();
            int size = list.size();
            for( int i=0; i<size; i++ ){
                cd = (CloneDisk)list.get(i);
                this.delLunMapOnMDI( cd.getTarget_id() );
                this.delLunMapOnView( cd.getRoot_id() );

                // 修改clonedisk的属性，使之归于空闲盘的范畴
                if( !view.initor.mdb.modCloneDisk("",0,0,"",cd.getId(),-1,CloneDisk.IS_FREEVOL ) ){
                    errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_GET_CLONE_DISK ) +" : " + view.initor.mdb.getErrorMessage();
                    SanBootView.log.error( getClass().getName(),errMsg);
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_GET_CLONE_DISK ) +" : " + view.initor.mdb.getErrorMessage();
            SanBootView.log.error( getClass().getName(),errMsg);
        }

        return aIsOk;
    }

    private boolean modUIMVolOnVolMap( int cltID,int rootid,int mode ){
        ArrayList list;

        if( mode == 0 ){
            list = view.initor.mdb.getMDIFromCacheOnHostIDandRootID( -1,rootid );
        }else{
            list = view.initor.mdb.getMDIFromCacheOnHostIDandRootID( cltID,rootid );
        }
        int size = list.size();
        for( int i=0; i<size; i++ ){
            MirrorDiskInfo mdi = (MirrorDiskInfo)list.get( i );

            if( mode == 0 ){
                // 修改uim-vol的属性，使之归于boothost的范畴
                if( !view.initor.mdb.modMDI( "",0,0,"",mdi.getSnap_rootid(),cltID ) ){
                    errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_MOD_MIRROR_DISK ) +" : " + view.initor.mdb.getErrorMessage();
                    return false;
                }
            }else{
                // 修改uim-vol的属性，使之归于空闲卷的范畴
                if( !view.initor.mdb.modMDI( "",0,0,"",mdi.getSnap_rootid(),-1 ) ){
                    errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_MOD_MIRROR_DISK ) +" : " + view.initor.mdb.getErrorMessage();
                    return false;
                }
            }
        }

        return true;
    }

    private boolean modCloneDiskOnVolMap( int rootid,int clntID,int mode ){
        CloneDisk cd;
        boolean aIsOk;

        if( mode == 0 ){
            aIsOk = view.initor.mdb.getCloneDiskList( -1,CloneDisk.IS_FREEVOL,rootid );
        }else{
            aIsOk = view.initor.mdb.getCloneDiskList( clntID,CloneDisk.IS_BOOTHOST,rootid );
        }
        if( aIsOk ){
            ArrayList list = view.initor.mdb.getCloneDiskList();
            int size = list.size();
            for( int i=0; i<size; i++ ){
                cd = (CloneDisk)list.get(i);

                if( mode == 0 ){
                    // 修改clonedisk的属性，使之归于boothost的范畴
                    if( !view.initor.mdb.modCloneDisk( "",0,0,"",cd.getId(),clntID,CloneDisk.IS_BOOTHOST ) ){
                        errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_MOD_CLONE_DISK ) +" : " + view.initor.mdb.getErrorMessage();
                        return false;
                    }
                }else{
                    // 修改clonedisk的属性，使之归于空闲卷的范畴
                    if( !view.initor.mdb.modCloneDisk( "",0,0,"",cd.getId(),-1,CloneDisk.IS_FREEVOL) ){
                        errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_MOD_CLONE_DISK ) +" : " + view.initor.mdb.getErrorMessage();
                        return false;
                    }
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_GET_CLONE_DISK ) +" : " + view.initor.mdb.getErrorMessage();
        }

        return aIsOk;
    }

    private boolean delLunMapOnMDI( int tid ){
        LunMap lm;

        boolean isOK = view.initor.mdb.getLunMapForTID( tid );
        if( isOK ){
            Vector lmList = view.initor.mdb.getAllLunMapForTid();
            int size1 = lmList.size();
            for( int j=0;j<size1;j++ ){
                lm = (LunMap)lmList.elementAt(j);
                isOK = view.initor.mdb.delLunMap( tid, lm.getIP(),lm.getMask(), lm.getAccessMode() );
                if( !isOK ){
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_LUNMAP) + " "+ tid +" "+lm.getIP() +" "+lm.getMask() +" "+lm.getAccessMode() +" "+ view.initor.mdb.getErrorMessage();
                    SanBootView.log.error( getClass().getName(),errMsg);
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_LUNMAP) + " [ tid:" + tid + " ] "+ view.initor.mdb.getErrorMessage();
            SanBootView.log.error( getClass().getName(),errMsg);
            return false;
        }

        return true;
    }

    private boolean delLunMapOnView( int rootid ){
        int k,j,size2,size1;
        Vector lmList;
        LunMap lm;

        boolean isOK = view.initor.mdb.getAllView( rootid );
        if( isOK ){
            ArrayList viewList = view.initor.mdb.getViewList();
            size2 = viewList.size();
            for( k=0; k<size2; k++ ){
                View viewObj = (View)viewList.get( k );
                isOK = view.initor.mdb.getLunMapForTID( viewObj.getSnap_target_id() );
                if( isOK ){
                    lmList = view.initor.mdb.getAllLunMapForTid();
                    size1 = lmList.size();
                    for( j=0;j<size1;j++ ){
                        lm = (LunMap)lmList.elementAt(j);
                        isOK = view.initor.mdb.delLunMap( viewObj.getSnap_target_id(), lm.getIP(),lm.getMask(), lm.getAccessMode() );
                        if( !isOK ){
                            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_LUNMAP) + " "+ viewObj.getSnap_target_id() +" "+lm.getIP() +" "+lm.getMask() +" "+lm.getAccessMode() +" "+ view.initor.mdb.getErrorMessage();
                            SanBootView.log.error( getClass().getName(),errMsg);
                            return false;
                        }
                    }
                }else{
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_LUNMAP) + " [ " +viewObj.getSnap_target_id() + " ] "+  view.initor.mdb.getErrorMessage();
                    SanBootView.log.error( getClass().getName(),errMsg);
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_VIEW) + " "+ view.initor.mdb.getErrorMessage();
            SanBootView.log.error( getClass().getName(),errMsg);
            return false;
        }

        return true;
    }

}
