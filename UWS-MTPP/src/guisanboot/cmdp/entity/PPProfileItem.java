/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.entity;

import guisanboot.data.MirrorGrp;
import guisanboot.data.VolumeMap;

/**
 * PPProfileItem.java
 * physical protect strategy for one volume
 * Created on 2010-6-9, 10:18:44
 */
public class PPProfileItem {
    private MirrorGrp mg = null;
    private VolumeMap volMap = null;

    public PPProfileItem(){
    }

    public PPProfileItem(
        MirrorGrp mg,
        VolumeMap volMap
    ){
        this.mg = mg;
        this.volMap = volMap;
    }

    /**
     * @return the mg
     */
    public MirrorGrp getMg() {
        return mg;
    }

    /**
     * @param mg the mg to set
     */
    public void setMg(MirrorGrp mg) {
        this.mg = mg;
    }

    /**
     * @return the volMap
     */
    public VolumeMap getVolMap() {
        return volMap;
    }

    /**
     * @param volMap the volMap to set
     */
    public void setVolMap(VolumeMap volMap) {
        this.volMap = volMap;
    }
    
    @Override public String toString(){
        return volMap.getVolDiskLabel();
    }

    public boolean isSameProfileItem( PPProfileItem item ){
        return ( mg.getMg_interval_time() == item.getMg().getMg_interval_time() ) &&
               ( mg.getMg_max_snapshot() == item.getMg().getMg_max_snapshot() ) &&
               ( mg.getMg_min_snap_size() == item.getMg().getMg_min_snap_size() ) &&
               ( volMap.getVol_info().equals( item.getVolMap().getVol_info() ) ) &&
               ( volMap.getSortedDBInstance().equals( item.getVolMap().getSortedDBInstance() ) ) &&
               ( volMap.getSortedServices().equals( item.getVolMap().getSortedServices() ) );
    }

    public boolean isSameProfileItem( int interval_time,int max_snap,int min_size,
            int db_type,String db_instances,String services,
            int sch_type, String sch_minute,String sch_hour,
            String sch_day,String sch_month,String sch_week,
            String sch_clock_zone,String sch_hour1,String sch_clock_set
    ){
        return ( mg.getMg_interval_time() == interval_time ) &&
               ( mg.getMg_max_snapshot()  == max_snap ) &&
               ( mg.getMg_min_snap_size() == min_size ) &&
               ( volMap.getDBType() == db_type ) &&
               ( volMap.getSortedDBInstance().equals( VolumeMap.getDBInstance( VolumeMap.getInstanceList(db_instances))) ) &&
               ( volMap.getSortedServices().equals( VolumeMap.getServices( VolumeMap.getServices(services ) ) ) ) &&
               ( mg.getMg_schedule_type() == sch_type ) &&
               ( mg.getMg_schedule_minute().equals( sch_minute ) ) &&
               ( mg.getMg_schedule_hour().equals( sch_hour ) ) &&
               ( mg.getMg_schedule_day().equals( sch_day ) ) &&
               ( mg.getMg_schedule_month().equals( sch_month ) ) &&
               ( mg.getMg_schedule_week().equals( sch_week ) ) &&
               ( mg.getMg_schedule_clock_zone().equals( sch_clock_zone ) ) &&
               ( mg.getMg_schedule_hour1().equals( sch_hour1 ) ) &&
               ( mg.getMg_schedule_clock_set().equals( sch_clock_set ) );
    }
}
