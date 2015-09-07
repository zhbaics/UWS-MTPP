/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.ui;

import guisanboot.cmdp.entity.PPProfile;
import guisanboot.cmdp.entity.PPProfileItem;
import guisanboot.data.MirrorGrp;
import guisanboot.data.Service;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

/**
 * EditPPProfileThread.java
 *
 * Created on 2010-6-17, 9:49:38
 */
public class EditPPProfileThread extends BasicGetSomethingThread{
    private PPProfile orgPPProf;
    private ArrayList<PPProfileItem> selDisks;
    private int sch_type;
    private int day;
    private int hour;
    private int min;
    private MirrorGrp cronInfo_mg;
    private int minSize;
    private int maxSnap;
    private int db_type;
    private String instances;
    private ArrayList<Service> selServices;
    private boolean miniOverloadMode = false;

    public EditPPProfileThread(
        SanBootView view,
        PPProfile orgPPProf,
        ArrayList<PPProfileItem> selDisks,
        int sch_type,
        int day,
        int hour,
        int min,
        MirrorGrp cronInfo_mg,
        int minSize,
        int maxSnap,
        int db_type,
        String instances,
        ArrayList<Service> selServices,
        boolean miniOverloadMode
    ){
        super( view );
        this.orgPPProf = orgPPProf;
        this.selDisks = selDisks;
        this.sch_type = sch_type;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.cronInfo_mg = cronInfo_mg;
        this.minSize = minSize;
        this.maxSnap = maxSnap;
        this.db_type = db_type;
        this.instances = instances;
        this.selServices = selServices;
        this.miniOverloadMode = miniOverloadMode;
    }

    public EditPPProfileThread(
        SanBootView view,
        PPProfile orgPPProf,
        ArrayList<PPProfileItem> selDisks,
        int day,
        int hour,
        int min,
        int minSize,
        int maxSnap,
        int db_type,
        String instances,
        ArrayList<Service> selServices
    ){
        super( view );
        this.orgPPProf = orgPPProf;
        this.selDisks = selDisks;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.minSize = minSize;
        this.maxSnap = maxSnap;
        this.db_type = db_type;
        this.instances = instances;
        this.selServices = selServices;
    }

    private boolean hasContainOne( String singleDiskLabel,ArrayList<PPProfileItem> diskSet){
        int size = diskSet.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = diskSet.get(i);
            if( item.getVolMap().getVolDiskLabel().equals( singleDiskLabel ) ){
                return true;
            }
        }
        return false;
    }

    private PPProfile getBelongedPPProfile( ArrayList<PPProfileItem> selDisks ){
        ArrayList<PPProfile> ppprofList = view.initor.mdb.getPPProfile( orgPPProf.getHostID() );
        int size = ppprofList.size();
        for( int i=0; i<size; i++ ){
            PPProfile ppprof1 = ppprofList.get(i);
            if( !ppprof1.isValidDriveGrp() ) continue;

            if( ppprof1.containDiskSet( selDisks ) ){
                return ppprof1;
            }
        }
        return null;
    }

    private PPProfile getMaxSubPPProfile( ArrayList<PPProfileItem> selDisks ){
        ArrayList<PPProfile> subPPPList = new ArrayList<PPProfile>();

        ArrayList<PPProfile> ppprofList = view.initor.mdb.getPPProfile( orgPPProf.getHostID() );
        int size = ppprofList.size();
        for( int i=0; i<size; i++ ){
            PPProfile ppprof1 = ppprofList.get(i);
            if( !ppprof1.isValidDriveGrp() ) continue;

            if( ppprof1.beContainedInThisSet( selDisks )  ){
                subPPPList.add( ppprof1 );
            }
        }

        if( subPPPList.size() == 0 ) return null;

        PPProfile ret = new PPProfile();
        size = subPPPList.size();
        for( int i=0; i<size; i++  ){
            if( subPPPList.get(i).getDiskSize() > ret.getDiskSize() ){
                ret = subPPPList.get(i);
            }
        }

        return ret;
    }

    private ArrayList<PPProfile> getRelatedPPProfile( ArrayList<PPProfileItem> selDisks,PPProfile excludedPPP ){
        ArrayList<PPProfile> ppprofList = view.initor.mdb.getPPProfile( orgPPProf.getHostID() );
        int size = ppprofList.size();
        ArrayList<PPProfile> ret = new ArrayList<PPProfile>();

        for( int i=0; i<size; i++ ){
            PPProfile ppprof1 = ppprofList.get(i);
            if( !ppprof1.isValidDriveGrp() ) continue;
            if( (excludedPPP!=null) && ppprof1.getDiskList().equals( excludedPPP.getDiskList() ) ) continue;

            PPProfile only_has_overlap_disks_prof = ppprof1.getOverlapDisksOnThisSet( selDisks );
            if( only_has_overlap_disks_prof.getDiskSize() > 0 ){
                ret.add( only_has_overlap_disks_prof );
            }
        }
        return ret;
    }

    private boolean modifyDiskComponent( ArrayList<PPProfileItem> selDisks ){
        PPProfile fatherPPP;

        int orgDiskNum = orgPPProf.getDiskSize();
        int selDiskNum = selDisks.size();

        if( ( orgDiskNum == 1 ) && ( selDiskNum > 1 ) ){ // single disk ===> drivegroup
            return this.crtNewDriveGroup( selDisks );
        }else if( ( orgDiskNum == 1 ) && ( selDiskNum ==1 ) ) { // single disk === > single disk
            if( orgPPProf.getDiskList().equals( PPProfile.getDiskList( selDisks ) ) ){
                // 所选的卷跟原来的卷一样
                return true;
            }else{
                fatherPPP = this.getBelongedPPProfile( selDisks );
                if( fatherPPP != null ){
                    // 将selDisks从fatherPPP中退出
                    return this.delDiskFromDg1( selDisks, fatherPPP );
                }else{
                    // impossible to happen
SanBootView.log.warning(getClass().getName(), "can't find drivegroup of the disk: "+ selDisks.get(0).getVolMap().getVolDiskLabel());
                    return false;
                }
            }
        }else if( ( orgDiskNum >1 ) && ( selDiskNum ==1 ) ) { // drivegroup ===> single disk
            if( this.hasContainOne( selDisks.get(0).getVolMap().getVolDiskLabel(), orgPPProf.getElements() ) ){
                // 所选volume就在原来的dg中
                // 将selDisks从ppprof中退出
                return this.delDiskFromDg1( selDisks, orgPPProf );
            }else{
                fatherPPP = this.getBelongedPPProfile( selDisks );
                if( fatherPPP != null ){
                    // 将selDisks的盘从fatherPPP中退出
                    return this.delDiskFromDg1( selDisks, fatherPPP );
                }else{
                    // impossible to happen
SanBootView.log.warning(getClass().getName(), "can't find drivegroup of the disk: "+ selDisks.get(0).getVolMap().getVolDiskLabel());
                    return false;
                }
            }
        }else{ // drivegroup ====> drivegroup
            if( this.orgPPProf.getDiskList().equals( PPProfile.getDiskList( selDisks ) ) ){
                // 所选的卷跟原来的卷一样
                return true;
            }else{
                return this.crtNewDriveGroup( selDisks );
            }
        }
    }

    // 1. 先找一个包含seldisks的dg,对它做退盘操作，否则转2
    // 2. 在selDisks中找一个最大的完整的dg,然后把selDisks剩余的盘加入到这个dg中，同时做“退盘”操作，否则转3
    // 3. 创建一个dg,然后把selDisks加入到这个dg中，同时做“退盘”操作
    private boolean crtNewDriveGroup( ArrayList<PPProfileItem> selDisks ){
        PPProfile fatherPPP = this.getBelongedPPProfile( selDisks );
        if( fatherPPP != null ){
            // 将不属于selDisks的盘从这个fatherPPP中退盘
            return this.delDiskFromDg( selDisks, fatherPPP );
        }else{
            PPProfile subPPP = getMaxSubPPProfile ( selDisks );
            if( subPPP != null ){
                // 对这个subPPP做加盘操作
                ArrayList overLapedPPProfList = this.getRelatedPPProfile( selDisks,subPPP );
                if( delDiskFromDg( overLapedPPProfList ) ){
                    return this.addDiskIntoDg( selDisks, subPPP );
                }
            }else{
                // 创建一个dg
                ArrayList overLapedPPProfList = this.getRelatedPPProfile( selDisks,null );
                if( this.delDiskFromDg( overLapedPPProfList ) ){
                    return this.crtNewDg( selDisks );
                }
            }
        }

        return true;
    }

    private boolean crtNewDg( ArrayList<PPProfileItem> selDisks ){
        int size = selDisks.size();
        boolean isFirst = true;
        StringBuffer rootids = new StringBuffer();

        for( int i=0; i<size; i++ ){
            PPProfileItem item = selDisks.get(i);
            if( isFirst ){
                rootids.append( item.getVolMap().getVol_rootid() + "" );
                isFirst = false;
            }else{
                rootids.append( "^" + item.getVolMap().getVol_rootid() );
            }
        }

        StringBuffer dg_name = new StringBuffer();
        dg_name.append("dg-");
        Calendar  c = new  GregorianCalendar();
        int year = c.get( Calendar.YEAR );
        dg_name.append(""+year);
        int month = c.get( Calendar.MONTH )+1;
        if( month < 10 ){
            dg_name.append("0"+month);
        }else{
            dg_name.append(""+month);
        }
        int day_of_month = c.get( Calendar.DAY_OF_MONTH );
        if( day_of_month < 10 ){
            dg_name.append("0"+day_of_month);
        }else{
            dg_name.append(""+day_of_month);
        }
        int hour_of_day = c.get( Calendar.HOUR_OF_DAY );
        if( hour_of_day < 10 ){
            dg_name.append("0"+hour_of_day);
        }else{
            dg_name.append(""+hour_of_day);
        }
        int minute = c.get( Calendar.MINUTE );
        if( minute < 10 ){
            dg_name.append("0"+minute);
        }else{
            dg_name.append(""+minute);
        }
        int sec = c.get(Calendar.SECOND );
        if( sec < 10 ){
            dg_name.append("0"+sec);
        }else{
            dg_name.append(""+sec);
        }

        return view.initor.mdb.crtDG( dg_name.toString(), rootids.toString() );
    }

    private boolean delDiskFromDg( ArrayList<PPProfile> overlapProf ){
        int i,j,size,size1;
        boolean ok;

        size = overlapProf.size();
        for( i=0; i<size; i++ ){
            PPProfile prof = overlapProf.get(i);
            if( prof.isDeled() ){
                PPProfileItem master_disk = prof.getMainDiskItem();
                ok = view.initor.mdb.delDG( prof.getDriveGrpName(), master_disk.getVolMap().getVol_rootid()+"",master_disk.getVolMap().getGroupinfodetail() );
                if( !ok ){
                    return false;
                }
            }else{
                ArrayList<PPProfileItem> delItemList = prof.getElements();
                size1 = delItemList.size();
                for( j=0; j<size1; j++ ){
                    PPProfileItem delItem = delItemList.get(j);
                    ok = view.initor.mdb.delDiskFromDG(
                        delItem.getVolMap().getVolName(),
                        delItem.getVolMap().getVol_rootid()+"",
                        delItem.getVolMap().getGroupinfo(),
                        delItem.getVolMap().getGroupinfodetail(),
                        prof.getDriveGrpName()
                    );
                    if( !ok ){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean addDiskIntoDg( ArrayList<PPProfileItem> selDisks,PPProfile dg ){
        boolean ok;

        int size = selDisks.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = selDisks.get(i);
            if( dg.getItem( item.getVolMap().getVolDiskLabel() ) == null ){
                ok = view.initor.mdb.addDiskIntoDG( dg.getDriveGrpName(), item.getVolMap().getVol_rootid()+"" );
                if( !ok ){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean delDiskFromDg(ArrayList<PPProfileItem> selDisks,PPProfile dg ){
        boolean ok;

        ArrayList<PPProfileItem> elements = dg.getElements();
        int size = elements.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = elements.get(i);
            if( !this.hasContainOne( item.getVolMap().getVolDiskLabel(), selDisks ) ){
                ok = view.initor.mdb.delDiskFromDG(
                    item.getVolMap().getVolName(),
                    item.getVolMap().getVol_rootid()+"",
                    item.getVolMap().getGroupinfo(),
                    item.getVolMap().getGroupinfodetail(),
                    dg.getDriveGrpName()
                );
                if( !ok ){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean delDiskFromDg1( ArrayList<PPProfileItem> selDisks,PPProfile dg ){
        boolean ok;

        int size = selDisks.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = selDisks.get(i);
            ok = view.initor.mdb.delDiskFromDG(
                item.getVolMap().getVolName(),
                item.getVolMap().getVol_rootid()+"",
                item.getVolMap().getGroupinfo(),
                item.getVolMap().getGroupinfodetail(),
                dg.getDriveGrpName()
            );
            if( !ok ){
                return false;
            }
        }
        return true;
    }

    public boolean realRun(){
        boolean ok = this.modifyDiskComponent( selDisks );
        if( !ok ){
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }

        // 不管是否发生磁盘变化，都更新一遍下面的东东,因为就算磁盘组成没有发生变化，其他属性(快照频率等)
        // 也可能变化，另外mg中的pid也随时会变化，所以统统更新一遍。(2010.11.23)
        // 1)刷新volume信息
        if( !view.initor.mdb.updateVolumeMap() ){
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }
        // 2)刷新mg信息,因为mg里面记录了start_mirror进程的pid，这个pid有可能会变化
        if( !view.initor.mdb.updateMg() ){
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }
        // 3)刷新dg信息
        if( !view.initor.mdb.updateDg()){
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;

        }
        // 4)刷新ppprofile
        if( !view.initor.mdb.updatePPProfileList()){
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }

        PPProfile prof = view.initor.mdb.getPPProfile( this.orgPPProf.getHostID(),PPProfile.getDiskList( this.selDisks ) );
        if( prof == null ){
SanBootView.log.error(getClass().getName(),"can't find physical protect profile according clntid: " + this.orgPPProf.getHostID()  + " and "+PPProfile.getDiskList( this.selDisks ) );
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }

        PPProfileItem master_disk = prof.getMainDiskItem();

        // 修改其他东西
        // mg_interval_time / mg_min_snap_size / mg_max_snapshot
        if( sch_type == MirrorGrp.MG_SCH_TYPE_ROTATE ){
            int mg_interval_time = this.day*24*60*60 + this.hour*60*60 + this.min*60;
            ok = view.initor.mdb.modAutoCrtSnapParameter( master_disk.getMg().getMg_id(), sch_type,mg_interval_time,this.maxSnap, this.minSize );
            if( !ok ){
                this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
                return false;
            }else{
                master_disk.getMg().setMg_schedule_type( sch_type );
                master_disk.getMg().setMg_interval_time( mg_interval_time );
                master_disk.getMg().setMg_max_snapshot( this.maxSnap );
                master_disk.getMg().setMg_min_snap_size( this.minSize );
            }
        }else{ // sch is crond or clock type
            String mg_sch_minute = this.cronInfo_mg.getMg_schedule_minute();
            String mg_sch_hour   = this.cronInfo_mg.getMg_schedule_hour();
            String mg_sch_day    = this.cronInfo_mg.getMg_schedule_day();
            String mg_sch_month  = this.cronInfo_mg.getMg_schedule_month();
            String mg_sch_week   = this.cronInfo_mg.getMg_schedule_week();
            String mg_sch_clock_zone = this.cronInfo_mg.getMg_schedule_clock_zone();
            String mg_sch_hour1  = this.cronInfo_mg.getMg_schedule_hour1();
            String mg_sch_clock_set = this.cronInfo_mg.getMg_schedule_clock_set();

            ok = view.initor.mdb.modAutoCrtSnapParameter1( master_disk.getMg().getMg_id(), sch_type,mg_sch_minute,
                    mg_sch_hour,mg_sch_day,mg_sch_month,mg_sch_week,mg_sch_clock_zone,mg_sch_hour1,mg_sch_clock_set,
                    this.maxSnap, this.minSize );
            if( !ok ){
                this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
                return false;
            }else{
                master_disk.getMg().setMg_schedule_type( sch_type );
                master_disk.getMg().setMg_schedule_minute( mg_sch_minute );
                master_disk.getMg().setMg_schedule_hour( mg_sch_hour );
                master_disk.getMg().setMg_schedule_day( mg_sch_day );
                master_disk.getMg().setMg_schedule_month( mg_sch_month );
                master_disk.getMg().setMg_schedule_week( mg_sch_week );
                master_disk.getMg().setMg_schedule_clock_zone( mg_sch_clock_zone );
                master_disk.getMg().setMg_schedule_hour1( mg_sch_hour1 );
                master_disk.getMg().setMg_schedule_clock_set( mg_sch_clock_set );
                master_disk.getMg().setMg_max_snapshot( this.maxSnap );
                master_disk.getMg().setMg_min_snap_size( this.minSize );
            }
        }

        // 再修改其他成员盘的若干属性( max_snap,min_inc_data )
        ok = modifyForNonMainDisk( prof,this.maxSnap,this.minSize );
        if( !ok ){
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }
        
        // services
        int size = this.selServices.size();
        StringBuffer buf = new StringBuffer();
        for( int i=0; i<size; i++ ){
            buf.append( selServices.get(i).getServName()+";");
        }
        ok = view.initor.mdb.modServiceOnVolume( master_disk.getVolMap().getVolName(),buf.toString() );
        if( !ok ){
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }else{
            master_disk.getVolMap().setChangeVerService( buf.toString() );
        }

        // 修改xxx-service.conf配置文件

        String[] lines = Pattern.compile("\\s+").split( instances,-1 );
        StringBuffer buf1 = new StringBuffer();
        boolean isFirst = true;
        for( int i=0; i<lines.length; i++ ){
            if( lines[i].equals("") ) continue;
            if( isFirst ){
                buf1.append(lines[i]);
                isFirst = false;
            }else{
                buf1.append("^"+lines[i]);
            }
        }
        
        // database info
        ok = view.initor.mdb.modDbinfoOnVolume( master_disk.getVolMap().getVolName(),this.db_type,buf1.toString() );
        if( !ok ){
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }else{
            master_disk.getVolMap().setDBType( this.db_type );
            master_disk.getVolMap().setDatabase_instances( this.instances );
        }

        // mini-overload mode( mini-overload mode )
        //ok = this.modifyForMiniOverloadMode( prof,this.miniOverloadMode );
        ok = this.modifyForMiniOverloadMode( prof,this.miniOverloadMode, this.db_type );
        if( !ok ){
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }
        
        // send signal to every mg
        ok = view.initor.mdb.reloadMgConfig( prof.getHostID() );
        if( !ok ){
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }
        return true;
    }

    private boolean modifyForNonMainDisk( PPProfile prof,int temp_max_snap,int temp_min_size ){
        boolean aIsOk,taskOk = true;

        ArrayList<PPProfileItem> itemList = prof.getNonMainDiskItemList();
        int size = itemList.size();
        if( size > 0 ){
            for( int i=0; i<size; i++ ){
                PPProfileItem item = itemList.get( i );
                aIsOk = view.initor.mdb.modAutoCrtSnapParameter3( item.getMg().getMg_id(),temp_max_snap,temp_min_size );
                if( !aIsOk ){
                    taskOk = false;
                }else{
                    item.getMg().setMg_max_snapshot( temp_max_snap );
                    item.getMg().setMg_min_snap_size( temp_min_size );
                }
            }
            return taskOk;
        }else{
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

    private boolean modifyForMiniOverloadMode( PPProfile prof,boolean minioverload_mode,int dbtype ){
        boolean aIsOk,taskOk = true;

        ArrayList<PPProfileItem> itemList = prof.getElements();
        int size = itemList.size();
        if( size > 0 ){
            for( int i=0; i<size; i++ ){
                PPProfileItem item = itemList.get( i );
                item.getVolMap().setAutoAsyncAftCrtSnap( minioverload_mode );
                //aIsOk = view.initor.mdb.modOneVolumeMap7( item.getVolMap() );
                view.initor.mdb.modVolInfo(item.getVolMap().getVol_info(), dbtype);
                String volinfo = view.initor.mdb.getVolInfo();
                aIsOk = view.initor.mdb.modOneVolumeMap7( item.getVolMap(), volinfo );
                
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
