/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.ui;

import guisanboot.cmdp.entity.PPProfile;
import guisanboot.cmdp.entity.PPProfileItem;
import guisanboot.data.MirrorGrp;
import guisanboot.data.Service;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;

/**
 * EditPPProfileWhenInit.java
 *
 * Created on 2010-6-17, 9:49:38
 */
public class EditPPProfileWhenInit {
    private PPProfile orgPPProf;
    private ArrayList<PPProfileItem> selDisks;
    private int sch_type;
    private int day;
    private int hour;
    private int min;
    private MirrorGrp schInfo_mg;
    private int minSize;
    private int maxSnap;
    private int db_type;
    private String instances;
    private ArrayList<Service> selServices;
    private ArrayList<PPProfile> curProfList;
    private ArrayList<PPProfileItem> allDisks;
    private String errMsg;
    private boolean miniOverloadMode = false;

    public EditPPProfileWhenInit(
        PPProfile orgPPProf,
        ArrayList<PPProfileItem> selDisks,
        int sch_type,
        int day,
        int hour,
        int min,
        MirrorGrp schInfo_mg,
        int minSize,
        int maxSnap,
        int db_type,
        String instances,
        ArrayList<Service> selServices,
        ArrayList<PPProfile> curProfList,
        ArrayList<PPProfileItem> allDisks,
        boolean miniOverloadMode
    ){
        this.orgPPProf = orgPPProf;
        this.selDisks = selDisks;
        this.sch_type = sch_type;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.schInfo_mg = schInfo_mg;
        this.minSize = minSize;
        this.maxSnap = maxSnap;
        this.db_type = db_type;
        this.instances = instances;
        this.selServices = selServices;
        this.curProfList = curProfList;
        this.allDisks = allDisks;
        this.miniOverloadMode = miniOverloadMode;
    }

    public String getErrMsg(){
        return this.errMsg;
    }

    private boolean hasContainInPPProfile( String singleDiskLabel,ArrayList<PPProfile> diskSet ){
        int size = diskSet.size();
        for( int i=0; i<size; i++ ){
            PPProfile prof = diskSet.get(i);
            if( this.hasContainOne( singleDiskLabel, prof.getElements() ) ){
                return true;
            }
        }
        return false;
    }

    private void combind( ArrayList<PPProfileItem> curItemList,ArrayList<PPProfile> oldProfList ){
        int max_snap;

        ArrayList<PPProfile> newSingleProfList = new ArrayList<PPProfile>();
        int size = curItemList.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = curItemList.get(i);
            if( !this.hasContainInPPProfile( item.getVolMap().getVolDiskLabel(), oldProfList ) ){
                PPProfile newProf = new PPProfile();

                if( item.getMg().getMg_max_snapshot() > 0 ){
                    max_snap = item.getMg().getMg_max_snapshot();
                }else{
                    if( item.getVolMap().getLetter().toUpperCase().startsWith("C") ){
                        max_snap = MirrorGrp.MAX_SNAP_SYSTEM_DISK;
                    }else{
                        max_snap = MirrorGrp.MAX_SNAP_DATA_DISK;
                    }
                }
                newProf.setTemp_max_snap( max_snap );

                if( item.getMg().getMg_min_snap_size() > 0 ){
                    newProf.setTemp_min_size( item.getMg().getMg_min_snap_size() );
                }
                if( item.getMg().getMg_interval_time() >0 ){
                    newProf.setTemp_interval_time( item.getMg().getMg_interval_time() );
                }
                if( item.getVolMap().getDBType() != 0 ){
                    newProf.setTemp_db_type( item.getVolMap().getDBType() );
                }
                if( !item.getVolMap().getChangeVerService().equals("") ){
                    newProf.setTemp_services( item.getVolMap().getChangeVerService() );
                }
                if( !item.getVolMap().getDatabase_instances().equals("") ){
                    newProf.setTemp_db_instance(item.getVolMap().getDatabase_instances() );
                }

                newProf.addItem( item );
                newSingleProfList.add( newProf );
            }
        }

        size = newSingleProfList.size();
        for( int i=0; i<size; i++ ){
            PPProfile prof = newSingleProfList.get(i);
            this.curProfList.add( prof );
        }
    }

    private void removeOneProfile1( String diskList ){
        int size = this.curProfList.size();
        for( int i=0; i<size; i++ ){
            PPProfile prof = curProfList.get(i);
            if( prof.getDiskList().equals( diskList ) ){
                curProfList.remove( i );
                break;
            }
        }
    }

    public PPProfile getPPProfile( String hostIp,String diskList ){
        ArrayList<PPProfile> profList = this.curProfList;
        int size = profList.size();
        for( int i=0; i<size; i++ ){
            PPProfile prof = profList.get(i);
            if( prof == null ) continue;
            if( prof.getHost_ip().equals( hostIp ) && prof.getDiskList().equals( diskList ) ){
                return prof;
            }
        }
        return null;
    }
    
    public PPProfile getPPProfile( String diskList ){
        ArrayList<PPProfile> profList = this.curProfList;
        int size = profList.size();
        for( int i=0; i<size; i++ ){
            PPProfile prof = profList.get(i);
            if( prof.getDiskList().equals( diskList ) ){
                return prof;
            }
        }
        return null;
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
        ArrayList<PPProfile> ppprofList = this.curProfList;
        int size = ppprofList.size();
        for( int i=0; i<size; i++ ){
            PPProfile ppprof1 = ppprofList.get(i);
            if( ppprof1.containDiskSet( selDisks ) ){
                return ppprof1;
            }
        }
        return null;
    }

    private PPProfile getMaxSubPPProfile( ArrayList<PPProfileItem> selDisks ){
        ArrayList<PPProfile> subPPPList = new ArrayList<PPProfile>();

        ArrayList<PPProfile> ppprofList = this.curProfList;
        int size = ppprofList.size();
        for( int i=0; i<size; i++ ){
            PPProfile ppprof1 = ppprofList.get(i);
            if( ppprof1.beContainedInThisSet( selDisks )  ){
                subPPPList.add( ppprof1 );
            }
        }

        if( subPPPList.size() == 0 ) return null;

        // 找出符合要求的最大的一个
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
        ArrayList<PPProfile> ppprofList = this.curProfList;
        int size = ppprofList.size();
        ArrayList<PPProfile> ret = new ArrayList<PPProfile>();

        for( int i=0; i<size; i++ ){
            PPProfile ppprof1 = ppprofList.get(i);
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
SanBootView.log.debug(getClass().getName(), "1111111111111111111111111111111");
                return this.delDiskFromDg1( selDisks, orgPPProf );
            }else{
                fatherPPP = this.getBelongedPPProfile( selDisks );
                if( fatherPPP != null ){
SanBootView.log.debug(getClass().getName(), "222222222222222222222222222222");
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
                if( delDiskFromDg2( overLapedPPProfList ) ){
                    return this.addDiskIntoDg( selDisks, subPPP );
                }
            }else{
                // 创建一个dg
                ArrayList overLapedPPProfList = this.getRelatedPPProfile( selDisks,null );
                if( this.delDiskFromDg2( overLapedPPProfList ) ){
                    return this.crtNewDg( selDisks );
                }
            }
        }

        return true;
    }

    private boolean crtNewDg( ArrayList<PPProfileItem> selDisks ){
        PPProfile newDg = new PPProfile();
        int size = selDisks.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = selDisks.get(i);
            newDg.addItem( item );
        }
        this.curProfList.add( newDg );
        
        return true;
    }

    private boolean delDiskFromDg2( ArrayList<PPProfile> overlapProf ){
        int size = overlapProf.size();
        for( int i=0; i<size; i++ ){
            PPProfile prof = overlapProf.get( i );
            if( prof.isDeled() ){
                this.removeOneProfile1( prof.getDiskList() );
            }else{
                PPProfile fatherPPP = this.getBelongedPPProfile( prof.getElements() );
                if( fatherPPP != null ){
                    this.delDiskFromDg1( prof.getElements(), fatherPPP );
                }else{
System.out.println("impossible happen.");
                }
            }
        }
        
        return true;
    }
    
    private boolean addDiskIntoDg( ArrayList<PPProfileItem> selDisks,PPProfile dg ){
        int size = selDisks.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = selDisks.get(i);
            if( dg.getItem( item.getVolMap().getVolDiskLabel() ) == null ){
                dg.addItem( item );
            }
        }
        return true;
    }

    private boolean delDiskFromDg( ArrayList<PPProfileItem> selDisks,PPProfile dg ){
        ArrayList<PPProfileItem> elements = dg.getElements();
        int size = elements.size();
        for( int i=size-1; i>=0; i-- ){
            PPProfileItem item = elements.get(i);
            if( !this.hasContainOne( item.getVolMap().getVolDiskLabel(), selDisks ) ){
                dg.removeItem( item.getVolMap().getVol_rootid() );
            }
        }
        return true;
    }

    private boolean delDiskFromDg1( ArrayList<PPProfileItem> selDisks,PPProfile dg ){
        int size = selDisks.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = selDisks.get(i);
            dg.removeItem( item.getVolMap().getVol_rootid() );
        }
        return true;
    }

    public boolean realRun(){
        boolean ok = this.modifyDiskComponent( selDisks );
        if( !ok ){
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }

        // 合并，得到最后的profile list
        this.combind( this.allDisks, curProfList );

        PPProfile prof =  this.getPPProfile( this.orgPPProf.getHost_ip(),PPProfile.getDiskList( this.selDisks ) );
        if( prof == null ){
SanBootView.log.error(getClass().getName(),"can't find physical protect profile according clntid: " + this.orgPPProf.getHostID()  + " and "+PPProfile.getDiskList( this.selDisks ) );
            this.errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.editPPProf");
            return false;
        }

        // 修改其他东西
        // mg_interval_time/mg_min_snap_size/mg_max_snapshot
        prof.setTemp_mg_schedule_type( sch_type );
        if( sch_type == MirrorGrp.MG_SCH_TYPE_ROTATE ){
            int mg_interval_time = this.day*24*60*60 + this.hour*60*60 + this.min*60;
            prof.setTemp_interval_time( mg_interval_time );
        }else{
            prof.setTemp_mg_schedule_minute( schInfo_mg.getMg_schedule_minute() );
            prof.setTemp_mg_schedule_hour( schInfo_mg.getMg_schedule_hour() );
            prof.setTemp_mg_schedule_day( schInfo_mg.getMg_schedule_day() );
            prof.setTemp_mg_schedule_month( schInfo_mg.getMg_schedule_month() );
            prof.setTemp_mg_schedule_week( schInfo_mg.getMg_schedule_week() );
            prof.setTemp_mg_schedule_clock_zone( schInfo_mg.getMg_schedule_clock_zone() );
            prof.setTemp_mg_schedule_hour1( schInfo_mg.getMg_schedule_hour1() );
            prof.setTemp_mg_schedule_clock_set( schInfo_mg.getMg_schedule_clock_set() );
        }
        
        prof.setTemp_min_size( this.minSize );
        prof.setTemp_max_snap( this.maxSnap );

        // services
        int size = this.selServices.size();
        StringBuffer buf = new StringBuffer();
        for( int i=0; i<size; i++ ){
            buf.append( selServices.get(i).getServName()+";");
        }
        prof.setTemp_services( buf.toString() );

        // database info       
        prof.setTemp_db_type( this.db_type );
        prof.setTemp_db_instance( this.instances );

        // mini-overload mode
        prof.setMiniOverloadMode( this.miniOverloadMode );

        return true;
     }
}
