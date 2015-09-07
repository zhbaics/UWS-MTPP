/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.cmdp.ui;

import guisanboot.cluster.entity.Cluster;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.cmdp.entity.PPProfileItem;
import guisanboot.data.BindOfPartandVol;
import guisanboot.data.BootHost;
import guisanboot.data.MirrorGrp;
import guisanboot.data.VolumeMap;
import guisanboot.ui.SanBootView;
import guisanboot.ui.WizardDialogSample;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * ReCrtPPProfileWhenInit.java
 *
 * Created on 2010-6-25, PM 17:28
 */
public class ReCrtPPProfileWhenInit {

    SanBootView view;
    BootHost host;
    Cluster cluster;
    RunningTaskPaneForCMDP runPane;
    WizardDialogSample initDiag;
    Vector<VolumeMap> curVolMapList;
    Vector<BindOfPartandVol> toProtectDisks;
    ArrayList<PPProfile> curNewPPPList;
    ArrayList<PPProfile> curPPPList;
    private String errMsg;

    public ReCrtPPProfileWhenInit(
        SanBootView view,
        BootHost host,
        Cluster cluster,
        RunningTaskPaneForCMDP runPane,
        WizardDialogSample initDiag,
        Vector<VolumeMap> curVolMapList,
        Vector<BindOfPartandVol> toProtectDisks,
        ArrayList<PPProfile> curNewPPPList,
        ArrayList<PPProfile> curPPPList
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

    private boolean containedInSet(String drive, Vector<BindOfPartandVol> disks) {
        int size = disks.size();
        for ( int i=0; i<size; i++ ) {
            BindOfPartandVol disk = disks.elementAt(i);
            if (!disk.isProtected) {
                continue;
            }

            if ( disk.part.getDiskLabel().substring(0, 1).toUpperCase().equals(drive.toUpperCase() ) ) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<VolumeMap> findDiskToDel() {
        ArrayList<VolumeMap> toDelList = new ArrayList<VolumeMap>();
        int size = this.curVolMapList.size();
        for (int i = 0; i < size; i++) {
            VolumeMap vol = this.curVolMapList.elementAt(i);
            if (!this.containedInSet( vol.getVolDiskLabel().substring(0, 1).toUpperCase(), toProtectDisks ) ) {
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
            runPane.setLogOnTabpane( errMsg,RunningTaskPaneForCMDP.SAVE_INFO_ROW );
            initDiag.writeLogBuf( errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW );
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

        // services
        isOk = view.initor.mdb.modServiceOnVolume(oldProfItem.getVolMap().getVolName(), services);
        if (!isOk) {
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf2") + " [ " + diskList + " ]";
            runPane.setLogOnTabpane(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
        } else {
            oldProfItem.getVolMap().setChangeVerService(services);
        }
        if (!isOk) {
            taskOk = false;
        }

        String[] lines = Pattern.compile("\\s+").split(db_instances, -1);
        StringBuffer buf1 = new StringBuffer();
        boolean isFirst = true;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].equals("")) {
                continue;
            }
            if (isFirst) {
                buf1.append(lines[i]);
                isFirst = false;
            } else {
                buf1.append("^" + lines[i]);
            }
        }
        // database info
        isOk = view.initor.mdb.modDbinfoOnVolume(oldProfItem.getVolMap().getVolName(), db_type, buf1.toString());
        if (!isOk) {
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf3") + " [ " + diskList + " ]";
            runPane.setLogOnTabpane(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
        } else {
            oldProfItem.getVolMap().setDBType(db_type);
            oldProfItem.getVolMap().setDatabase_instances(db_instances);
        }
        if (!isOk) {
            taskOk = false;
        }

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
            isOk = view.initor.mdb.destoryMirror( aHost.getIP(), aHost.getPort(), toDelVol.getVolName());
            if (!isOk) {
                errMsg = SanBootView.res.getString("InitBootHostWizardDialog.log.delProtect") + " " + toDelVol.getVolDiskLabel() + " [ " + aHost.getIP() +"/" + aHost.getPort() +" ] " +
                        SanBootView.res.getString("common.failed");
                runPane.setLogOnTabpane(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
                initDiag.writeLogBuf(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
                taskOk = false;
            } else {
                // clw的destoryMirror应该会把volmap也删除。下面的代码为防止clw的程序没有删除volMap而准备的
                isOk = view.initor.mdb.delVolumeMap( toDelVol );
                if (!isOk) {
                    errMsg = SanBootView.res.getString("InitBootHostWizardDialog.log.delProtect") + " " + toDelVol.getVolDiskLabel() + " [ " + aHost.getIP() +"/" + aHost.getPort() +" ] " +
                            SanBootView.res.getString("common.failed");
                    runPane.setLogOnTabpane(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
                    initDiag.writeLogBuf(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
                    taskOk = false;
                } else {
                    runPane.setLogOnTabpane(
                        SanBootView.res.getString("InitBootHostWizardDialog.log.delProtect") + " " + toDelVol.getVolDiskLabel() + " [ " + aHost.getIP() +"/" + aHost.getPort() +" ] " +
                        SanBootView.res.getString("common.ok"),
                        RunningTaskPaneForCMDP.SAVE_INFO_ROW
                    );
                }
            }
        }

        // 按照新profile的disk list组合, 从旧的ppprofile中找出对应的profile，对其进行其他内容的修改
        size = this.curNewPPPList.size();
        for ( i = size - 1; i >= 0; i-- ) {
            newProf = this.curNewPPPList.get(i);
            oldProf = this.findPPProfile(newProf);
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
                runPane.setLogOnTabpane(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
                initDiag.writeLogBuf(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
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
                runPane.setLogOnTabpane(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
                initDiag.writeLogBuf(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
                taskOk = false;
            }
        }

        // 不管发生什么磁盘变化，都更新一遍下面的东东。因为就算磁盘组成没有发生变化，其他属性(快照频率等)
        // 也可能变化，另外mg中的pid也随时会变化，所以统统更新一遍。(2010.11.23)
        //if(  delDiskSize>0 || delOldSize>0 || crtNewSize>0  ){
        // 刷新volume信息
        if (!view.initor.mdb.updateVolumeMap()) {
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf6");
            runPane.setLogOnTabpane(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
            taskOk = false;
        }
        // 刷新mg信息,因为mg里面记录了start_mirror进程的pid，这个pid有可能会变化
        if (!view.initor.mdb.updateMg()) {
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf9");
            runPane.setLogOnTabpane(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
            taskOk = false;
        }
        // 刷新dg信息
        if (!view.initor.mdb.updateDg()) {
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf7");
            runPane.setLogOnTabpane(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
            taskOk = false;
        }
        // 刷新ppprofile
        if (!view.initor.mdb.updatePPProfileList()) {
            errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf8");
            runPane.setLogOnTabpane(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
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
            runPane.setLogOnTabpane(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
            initDiag.writeLogBuf(errMsg, RunningTaskPaneForCMDP.SAVE_INFO_ROW);
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
}
