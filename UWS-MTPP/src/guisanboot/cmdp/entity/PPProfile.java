/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.entity;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.MirrorGrp;
import guisanboot.data.VolumeMap;
import guisanboot.datadup.data.CronSchedule;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefNode;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.TableRevealable;
import mylib.UI.TreeRevealable;
import mylib.tool.SplitString;

/**
 * PPProfile.java
 *
 * Created on 2010-6-9, 16:08:44
 */
public class PPProfile extends ChiefNode implements TreeRevealable,TableRevealable{
    private ArrayList<PPProfileItem> elements = new ArrayList<PPProfileItem>();

    // 当PPProfile表示一个drivesgroup时，dg_name为drivesgroup的名字，master_disk_rootid为“dg主盘”的rootid
    private String dg_name = "";
    private int master_disk_rootid  = -1;
    
    public  boolean isSel = false;
    private boolean delFlag = false;
    
    // 存放界面上设置的信息，这些信息本来应该属于所谓的“master disk”的，但是
    // 由于还没有创建真正的ppprofile(drive mg)，所以也不知道master disk是谁，故而放在了这里
    private int temp_min_size = MirrorGrp.MG_DEFAULT_MIN_INCREMENT_SIZE;
    private int temp_max_snap = 0;
    private int temp_interval_time = MirrorGrp.MG_DEFAULT_INTERVAL_TIME;
    private int temp_db_type = 0;
    private String temp_db_instance ="";
    private String temp_services = "";
    private int temp_mg_schedule_type = 0;
    private String temp_mg_schedule_minute="0";
    private String temp_mg_schedule_hour="0";
    private String temp_mg_schedule_day="*";
    private String temp_mg_schedule_month="*";
    private String temp_mg_schedule_week="*";
    private String temp_mg_schedule_clock_zone="";
    private String temp_mg_schedule_hour1="0";
    private String temp_mg_schedule_clock_set="";
    private boolean temp_miniOverloadMode = false;
    
    private int display_mode = 0; // 缺省为0
    
    // for supporting cluster
    private String host_ip = "";
    private int host_port = -1 ;
    private int host_id = 0 ;
    private String host_uuid = "";

    public PPProfile(){
        super( ResourceCenter.TYPE_PPPROF );
    }

    public PPProfile(
        String dg_name,
        int master_disk_rootid
    ){
        super( ResourceCenter.TYPE_PPPROF );
        this.dg_name = dg_name;
        this.master_disk_rootid = master_disk_rootid;
    }

    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 1 );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_MOD_PROFILE) );
        }
    }

    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 1 );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_MOD_PROFILE) );
        }
    }

    public void clearAll(){
        this.elements.clear();
    }

    public void setDisplayMode( int val ){
        this.display_mode = val;
    }

    public int getDisplayMode(){
        return this.display_mode;
    }

    public void addItem( PPProfileItem one ){
        elements.add( one );
    }

    public void removeItem( int rootid ){
        int size = elements.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = elements.get(i);
            if( item.getVolMap().getVol_rootid() == rootid ){
                elements.remove( i );
                break;
            }
        }
    }

    public void removeItem( String diskLabel ){
        int size = elements.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = elements.get(i);
            if( item.getVolMap().getVolDiskLabel().equals( diskLabel ) || item.getVolMap().isSameLetter( diskLabel ) ){
                elements.remove( i );
                break;
            }
        }
    }
    
    public ArrayList<PPProfileItem>  getElements(){
        return this.elements;
    }

    public int getDiskSize(){
        return this.elements.size();
    }
    
    public void setDriveGrpName( String name ){
        this.dg_name = name;
    }
    public String getDriveGrpName(){
        return dg_name;
    }

    public int getMasterDiskRootid(){
        return this.master_disk_rootid;
    }
    public void setMasterDiskRootid( int rootid ){
        this.master_disk_rootid = rootid;
    }

    public boolean isValidDriveGrp(){
        return !this.dg_name.equals("") && ( this.master_disk_rootid != -1 );
    }

    public boolean isValidDriveGrp1(){
        return ( this.elements.size() > 1 );
    }

    public boolean isDeled(){
        return this.delFlag;
    }

    public int getHostID(){
        int size = this.elements.size();
        if( size > 0 ){
            return elements.get( 0 ).getVolMap().getVolClntID();
        }else{
            return -1;
        }
    }
    
    public String getMainDiskLabel(){
        PPProfileItem item = this.getMainDiskItem();
        return item.getVolMap().getVolDiskLabel();
    }

    public PPProfileItem getMainDiskItem(){
        if( this.isValidDriveGrp() ){
            return this.getMainDiskItem( this.master_disk_rootid );
        }else{
            if( this.elements.size() > 0 ){
                return this.elements.get( 0 );
            }else{
                return null;
            }
        }
    }

    public ArrayList<PPProfileItem> getNonMainDiskItemList(){
        ArrayList<PPProfileItem> ret = new ArrayList<PPProfileItem>();

        if( this.isValidDriveGrp() ){
            int size = this.elements.size();
            for( int i=0; i<size; i++ ){
                PPProfileItem ele = elements.get(i);
                if( ele.getVolMap().getVol_rootid() != this.master_disk_rootid ){
                    ret.add( ele );
                }
            }
        }
        return ret;
    }

    public PPProfileItem getItemNotPointed( String label ){
        int size = this.elements.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = elements.get( i );
            if( !item.getVolMap().getVolDiskLabel().equals( label ) ){
                return item;
            }
        }
        return null;
    }

    private PPProfileItem getMainDiskItem( int rootid ){
        int size = this.elements.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = elements.get(i);
            if( item.getVolMap().getVol_rootid() == rootid ){
                return item;
            }
        }
        return null;
    }
    
    public String getIntervalString(){
        if( this.isValidDriveGrp() ){
            PPProfileItem item = this.getMainDiskItem( this.getMasterDiskRootid() );
            if( item != null ){
SanBootView.log.debug( getClass().getName(),"interval time: "+item.getMg().getMg_interval_time()+"" );
                //return MirrorGrp.getIntervalString( item.getMg().getMg_interval_time() );
                return item.getMg().getSchString();
            }else{
                return "";
            }
        }else{
            int size = elements.size();
            if( size > 0 ){
                PPProfileItem item  = elements.get( 0 );
SanBootView.log.debug( getClass().getName(),"interval time: "+item.getMg().getMg_interval_time() );
                //return MirrorGrp.getIntervalString( item.getMg().getMg_interval_time() );
                return item.getMg().getSchString();
            }else{
                return "";
            }
        }
    }
    
    public Integer[] getItemValOfIntervalTime(){
        if( this.isValidDriveGrp() ){
            PPProfileItem item = this.getMainDiskItem( this.getMasterDiskRootid() );
            if( item != null ){
SanBootView.log.debug( getClass().getName(),"interval time: "+item.getMg().getMg_interval_time()+"" );
                return MirrorGrp.getDayOfIntervalTime( item.getMg().getMg_interval_time() );
            }else{
                return null;
            }
        }else{
            int size = elements.size();
            if( size > 0 ){
                PPProfileItem item  = elements.get( 0 );
SanBootView.log.debug( getClass().getName(),"interval time: "+item.getMg().getMg_interval_time()+"" );
                return MirrorGrp.getDayOfIntervalTime( item.getMg().getMg_interval_time() );
            }else{
                return null;
            }
        }
    }

    public void PrtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append("\ndrivegroup name: "+ this.dg_name );
        buf.append("\nmaster disk rootid: "+this.master_disk_rootid );
        buf.append("\nelement size: "+this.elements.size() );
        System.out.println( buf.toString() );
    }

    public static String getDiskList( ArrayList<PPProfileItem> elements ){
        StringBuffer buf = new StringBuffer();
        boolean isFirst = true;
        
        // 对disklabel进行排序
        int size = elements.size();
        String[] list = new String[size];
        for( int i=0; i<size; i++ ){
            list[i] = elements.get(i).getVolMap().getVolDiskLabel();
        }
        Arrays.sort( list );
        
        for( int i=0; i<list.length; i++ ){
            String letter = list[i];
            if( isFirst ){
                buf.append( letter );
                isFirst = false;
            }else{
                buf.append( ";" + letter );
            }
        }
        return buf.toString();
    }

    public static String getDiskListAms( ArrayList<PPProfileItem> elements ){
        StringBuffer buf = new StringBuffer();
        boolean isFirst = true;

        // 对disklabel进行排序
        int size = elements.size();
        String[] list = new String[size];
        for( int i=0; i<size; i++ ){
            String diskLabel = elements.get(i).getVolMap().getVolName();
            int start = diskLabel.indexOf("_");
            int end = diskLabel.lastIndexOf("_");
            if(start >0 && end >0){
                diskLabel = diskLabel.substring(start+1, end);
            }
            list[i] = diskLabel;
        }
        Arrays.sort( list );

        for( int i=0; i<list.length; i++ ){
            String letter = list[i];
            if( isFirst ){
                buf.append( letter );
                isFirst = false;
            }else{
                buf.append( ";" + letter );
            }
        }
        return buf.toString();
    }

    public String getDiskList(){
        return PPProfile.getDiskList( this.elements );
    }

    public String getDiskListAms(){
        return PPProfile.getDiskListAms( this.elements );
    }

    public PPProfileItem getItem( String diskLabel ){
        int size = this.elements.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = this.elements.get(i);
            if( item.getVolMap().getVolDiskLabel().equals( diskLabel ) ||
                item.getVolMap().isSameLetter( diskLabel )
            ){
                return item;
            }
        }
        return null;
    }

    public PPProfileItem getItem( String diskLabel,String host_uuid,String host_ip ){
        if( host_uuid.equals( this.getHost_uuid() ) || host_ip.equals( this.getHost_ip() ) ){
            int size = this.elements.size();
            for( int i=0; i<size; i++ ){
                PPProfileItem item = this.elements.get(i);
                if( item.getVolMap().getVolDiskLabel().equals( diskLabel ) ||
                    item.getVolMap().isSameLetter( diskLabel )
                ){
                    return item;
                }
            }
        }

        return null;
    }

    public boolean isMasterDisk( String diskLabel ){
        PPProfileItem item = this.getItem( diskLabel );
        if( item != null && this.master_disk_rootid >0 ){
            return ( item.getVolMap().getVol_rootid() == this.master_disk_rootid );
        }else{
            return false;
        }
    }

    public boolean isBelongedToThisDG( String diskLabel ){
        return ( getItem( diskLabel ) != null );
    }

    private boolean containOneDisk( String diskLabel,ArrayList<PPProfileItem> diskSet ){
        int size = diskSet.size();
        for( int i=0; i<size; i++ ){
            if( diskLabel.equals( diskSet.get(i).getVolMap().getVolDiskLabel() ) ) {
                return true;
            }
        }
        return false;
    }

    public boolean containDiskSet( ArrayList<PPProfileItem> diskSet ){
        int size = diskSet.size();
        for( int i=0; i<size; i++ ){
           PPProfileItem item = diskSet.get(i);
           if( !this.containOneDisk( item.getVolMap().getVolDiskLabel(),this.elements ) ){
              return false;
           }
        }
        return true;
    }

    public boolean beContainedInThisSet( ArrayList<PPProfileItem> diskSet ){
        int size = this.elements.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = elements.get(i);
            if( !this.containOneDisk( item.getVolMap().getVolDiskLabel(),diskSet ) ){
                return false;
            }
        }
        return true;
    }

    public PPProfile getOverlapDisksOnThisSet( ArrayList<PPProfileItem> diskSet ){
        PPProfile overlap_prof = new PPProfile( this.dg_name,this.master_disk_rootid );

        int size = diskSet.size();
        for( int i=0; i<size; i++ ){
            PPProfileItem item = diskSet.get(i);
            PPProfileItem overlap_item_in_dg = this.getItem( item.getVolMap().getVolDiskLabel() );
            if( overlap_item_in_dg != null ){
                overlap_prof.addItem( overlap_item_in_dg );
            }
        }
        
        overlap_prof.delFlag = ( overlap_prof.getDiskSize() == this.getDiskSize() );
        return overlap_prof;
    }

    public PPProfile cloneMyself(){
        PPProfile newProf = new PPProfile();
        newProf.setDriveGrpName( this.dg_name );
        newProf.setMasterDiskRootid( this.getMasterDiskRootid() );

        PPProfileItem master_disk = this.getMainDiskItem();
        newProf.setTemp_db_instance( master_disk.getVolMap().getDatabase_instances() );
        newProf.setTemp_db_type( master_disk.getVolMap().getDBType() );
        newProf.setTemp_interval_time( master_disk.getMg().getMg_interval_time() );
        newProf.setTemp_max_snap( master_disk.getMg().getMg_max_snapshot() );
        newProf.setTemp_min_size( master_disk.getMg().getMg_min_snap_size() );
        newProf.setTemp_services( master_disk.getVolMap().getChangeVerService() );
        newProf.setTemp_mg_schedule_type( master_disk.getMg().getMg_schedule_type() );
        newProf.setTemp_mg_schedule_clock_zone( master_disk.getMg().getMg_schedule_clock_zone() );
        newProf.setTemp_mg_schedule_day( master_disk.getMg().getMg_schedule_day() );
        newProf.setTemp_mg_schedule_hour( master_disk.getMg().getMg_schedule_hour() );
        newProf.setTemp_mg_schedule_hour1( master_disk.getMg().getMg_schedule_hour1() );
        newProf.setTemp_mg_schedule_minute( master_disk.getMg().getMg_schedule_minute() );
        newProf.setTemp_mg_schedule_month( master_disk.getMg().getMg_schedule_month() );
        newProf.setTemp_mg_schedule_week( master_disk.getMg().getMg_schedule_week() );
        newProf.setTemp_mg_schedule_clock_set( master_disk.getMg().getMg_schedule_clock_set() );
        
        int size = this.elements.size();
        for( int i=0; i<size; i++ ){
            newProf.addItem( this.elements.get(i) );
        }
        return newProf;
    }
    
    @Override public String toString(){
        if( this.display_mode == 0 ){
            return this.getDiskList();
        }else{
            return this.getDiskList()+"  [ " + this.getHost_ip() +" ]";
        }
    }
    
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_PROFILE;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_PROFILE;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return toString();
    }
    public String toTipString(){
        String src = this.getDiskList();
        if( src.length() > 50 ){
            src = src.substring(0, 50)+"...";
        }
        return toTreeString()+" [ "+src +" ] ";
    }

    //** TableRevealable的实现**/
    public boolean enableTableEditable(){
        return false;
    }
    public boolean enableSelected(){
        return true;
    }
    public int  getAlignType(){
        return JLabel.LEFT;
    }
    public Icon getTableIcon(){
        return ResourceCenter.ICON_PROFILE;
    }
    public String toTableString(){
        return toString();
    }

    /**
     * @return the temp_min_size
     */
    public int getTemp_min_size() {
        return temp_min_size;
    }

    /**
     * @param temp_min_size the temp_min_size to set
     */
    public void setTemp_min_size(int temp_min_size) {
        this.temp_min_size = temp_min_size;
    }

    /**
     * @return the temp_max_snap
     */
    public int getTemp_max_snap() {
        return temp_max_snap;
    }

    /**
     * @param temp_max_snap the temp_max_snap to set
     */
    public void setTemp_max_snap(int temp_max_snap) {
        this.temp_max_snap = temp_max_snap;
    }

    /**
     * @return the temp_interval_time
     */
    public int getTemp_interval_time() {
        return temp_interval_time;
    }

    /**
     * @param temp_interval_time the temp_interval_time to set
     */
    public void setTemp_interval_time(int temp_interval_time) {
        this.temp_interval_time = temp_interval_time;
    }

    /**
     * @return the temp_db_type
     */
    public int getTemp_db_type() {
        return temp_db_type;
    }

    /**
     * @param temp_db_type the temp_db_type to set
     */
    public void setTemp_db_type(int temp_db_type) {
        this.temp_db_type = temp_db_type;
    }

    /**
     * @return the temp_db_instance
     */
    public String getTemp_db_instance() {
        return temp_db_instance;
    }

    /**
     * @param temp_db_instance the temp_db_instance to set
     */
    public void setTemp_db_instance(String temp_db_instance) {
        this.temp_db_instance = temp_db_instance;
    }

    /**
     * @return the temp_services
     */
    public String getTemp_services() {
        return temp_services;
    }

    /**
     * @param temp_services the temp_services to set
     */
    public void setTemp_services(String temp_services) {
        this.temp_services = temp_services;
    }

    public void appendService(String serv_name){
        this.setTemp_services( this.getTemp_services()+serv_name+";" );
    }

    public void removeService( String serv_name ){
        String[] list = this.getServices();
        StringBuffer buf = new StringBuffer();
        int size = list.length;
        for( int i=0; i<size; i++ ){
            if( !list[i].equals( serv_name ) ){
                buf.append( list[i]+";");
            }
        }
        this.setTemp_services( buf.toString() );
    }

    public String[] getServices(){
        return VolumeMap.getServices( this.getTemp_services() );
    }

    public String getSchString(){
        if( this.temp_mg_schedule_type == MirrorGrp.MG_SCH_TYPE_ROTATE  ){
            return MirrorGrp.getIntervalString( this.temp_interval_time );
        }else{
            return this.getTempCronSchString();
        }
    }

    /**
     * @return the temp_mg_schedule_type
     */
    public int getTemp_mg_schedule_type() {
        return temp_mg_schedule_type;
    }

    public boolean isCronType(){
        return temp_mg_schedule_type == MirrorGrp.MG_SCH_TYPE_CROND;
    }

    /**
     * @param temp_mg_schedule_type the temp_mg_schedule_type to set
     */
    public void setTemp_mg_schedule_type(int temp_mg_schedule_type) {
        this.temp_mg_schedule_type = temp_mg_schedule_type;
    }

    /**
     * @return the temp_mg_schedule_minute
     */
    public String getTemp_mg_schedule_minute() {
        return temp_mg_schedule_minute;
    }

    /**
     * @param temp_mg_schedule_minute the temp_mg_schedule_minute to set
     */
    public void setTemp_mg_schedule_minute(String temp_mg_schedule_minute) {
        this.temp_mg_schedule_minute = temp_mg_schedule_minute;
    }

    /**
     * @return the temp_mg_schedule_hour
     */
    public String getTemp_mg_schedule_hour() {
        return temp_mg_schedule_hour;
    }

    /**
     * @param temp_mg_schedule_hour the temp_mg_schedule_hour to set
     */
    public void setTemp_mg_schedule_hour(String temp_mg_schedule_hour) {
        this.temp_mg_schedule_hour = temp_mg_schedule_hour;
    }

    /**
     * @return the temp_mg_schedule_day
     */
    public String getTemp_mg_schedule_day() {
        return temp_mg_schedule_day;
    }

    /**
     * @param temp_mg_schedule_day the temp_mg_schedule_day to set
     */
    public void setTemp_mg_schedule_day(String temp_mg_schedule_day) {
        this.temp_mg_schedule_day = temp_mg_schedule_day;
    }

    /**
     * @return the temp_mg_schedule_month
     */
    public String getTemp_mg_schedule_month() {
        return temp_mg_schedule_month;
    }

    /**
     * @param temp_mg_schedule_month the temp_mg_schedule_month to set
     */
    public void setTemp_mg_schedule_month(String temp_mg_schedule_month) {
        this.temp_mg_schedule_month = temp_mg_schedule_month;
    }

    /**
     * @return the temp_mg_schedule_week
     */
    public String getTemp_mg_schedule_week() {
        return temp_mg_schedule_week;
    }

    /**
     * @param temp_mg_schedule_week the temp_mg_schedule_week to set
     */
    public void setTemp_mg_schedule_week(String temp_mg_schedule_week) {
        this.temp_mg_schedule_week = temp_mg_schedule_week;
    }

    /**
     * @return the temp_mg_schedule_clock_zone
     */
    public String getTemp_mg_schedule_clock_zone() {
        return temp_mg_schedule_clock_zone;
    }

    /**
     * @param temp_mg_schedule_clock_zone the temp_mg_schedule_clock_zone to set
     */
    public void setTemp_mg_schedule_clock_zone(String temp_mg_schedule_clock_zone) {
        this.temp_mg_schedule_clock_zone = temp_mg_schedule_clock_zone;
    }

    /**
     * @return the temp_mg_schedule_hour1
     */
    public String getTemp_mg_schedule_hour1() {
        return temp_mg_schedule_hour1;
    }

    public String getTemp_mg_schedule_clock_set(){
        return temp_mg_schedule_clock_set;
    }

    public boolean isMiniOverloadMode(){
        return this.temp_miniOverloadMode;
    }

    public void setMiniOverloadMode( boolean val ){
        this.temp_miniOverloadMode = val;
    }
    
    /**
     * @param temp_mg_schedule_hour1 the temp_mg_schedule_hour1 to set
     */
    public void setTemp_mg_schedule_hour1(String temp_mg_schedule_hour1) {
        this.temp_mg_schedule_hour1 = temp_mg_schedule_hour1;
    }

    public void setTemp_mg_schedule_clock_set( String val ){
        this.temp_mg_schedule_clock_set = val;
    }

    public String getTimeStr(){
        StringBuffer buf = new StringBuffer();
        buf.append( this.temp_mg_schedule_minute );
        buf.append( " " );
        buf.append( this.temp_mg_schedule_hour1 );
        buf.append( " " );
        buf.append( this.temp_mg_schedule_day );
        buf.append( " " );
        buf.append( this.temp_mg_schedule_month  );
        buf.append( " " );
        buf.append( this.temp_mg_schedule_week );
        return buf.toString();
    }

    public String getTempCronSchString(){
        StringBuffer buf = new StringBuffer();
        String cronSchString = this.getTimeStr();
        int freq_type = CronSchedule.getCronSchedulerType( cronSchString );         // day,week or month

        if( freq_type == CronSchedule.TYPE_DAILY ){
            buf.append( SanBootView.res.getString("Strategy.period.day") );
        }else if( freq_type == CronSchedule.TYPE_WEEKLY ){
            buf.append( SanBootView.res.getString("Strategy.period.week1") );
            buf.append( this.getWeekDay() );
        }else{
            buf.append( SanBootView.res.getString("Strategy.period.month1") );
            buf.append(" ");
            buf.append( this.temp_mg_schedule_day );
            buf.append(" ");
            buf.append( SanBootView.res.getString("ReportConfDialog.label.days") );
        }
        buf.append(", ");

        if( this.isCronType() ){
            int daily_type = CronSchedule.getDailyFreqOfCronScheduler( cronSchString );   // once or freq
            int freq_daily_type = CronSchedule.getDailyFreqOccurType( cronSchString );  // freq unit is minute or hour
            String freq_daily_time = ( freq_daily_type == CronSchedule.TYPE_DAILY_MINUTE_OCR )?this.getMinuteFreqVal()+" "+
                    SanBootView.res.getString("common.minutes"):this.getHourFreqVal()+" "+SanBootView.res.getString("common.hours");
            String clock_zone = this.temp_mg_schedule_clock_zone.equals("")?"":", "+SanBootView.res.getString("common.clockZoneCtrl")+" "+this.temp_mg_schedule_clock_zone;

            if( daily_type == CronSchedule.TYPE_DAILY_ONCE ){
                buf.append( this.temp_mg_schedule_hour1 );
                buf.append( ":" );
                buf.append( this.temp_mg_schedule_minute );
            }else{
                buf.append( SanBootView.res.getString("Strategy.period.every") );
                buf.append(" ");
                buf.append( freq_daily_time );
                buf.append(" ");
                buf.append( clock_zone );
            }
        }else{ // multi occur
            buf.append( this.temp_mg_schedule_clock_set );
        }
        return buf.toString();
    }

    private int getFrequenceVal( String freq ){
        if( freq.indexOf(",")>=0 ){
            String[] list = Pattern.compile(",").split( freq );
            try{
                return Integer.parseInt( list[1] );
            }catch(Exception ex){
                return 1;
            }
        }else{
            return 1;
        }
    }

    public int getHourFreqVal(){
        SplitString sp = new SplitString( this.getTimeStr() );
        sp.getNextToken();
        return getFrequenceVal( sp.getNextToken() );
    }

    public int getMinuteFreqVal(){
        SplitString sp = new SplitString( this.getTimeStr() );
        return getFrequenceVal( sp.getNextToken() );
    }

    private String getWeekDay(){
        StringBuffer buf = new StringBuffer();
        boolean isFirst = true,hasSunDay=false;

        String[] list = Pattern.compile(",").split( this.temp_mg_schedule_week.trim() );
        for( int i=0;i<list.length;i++ ){
            try{
                int wday = Integer.parseInt( list[i] );
                if( wday == 1 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.monday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.monday") );
                    }
                }
                if( wday == 2 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.tuesday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.tuesday") );
                    }
                }
                if( wday == 3 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.wednesday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.wednesday") );
                    }
                }
                if( wday == 4 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.thursday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.thursday") );
                    }
                }
                if( wday == 5 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.friday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.friday") );
                    }
                }
                if( wday == 6 ){
                    if( isFirst ){
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.saturday") );
                        isFirst = false;
                    }else{
                        buf.append(", ");
                        buf.append( SanBootView.res.getString("WeeklyPane.checkBox.saturday") );
                    }
                }
                if( wday == 0 ){
                    hasSunDay = true;
                }
            }catch(Exception ex){}
        }
        if( hasSunDay ){
            if( isFirst ){
                buf.append( SanBootView.res.getString("WeeklyPane.checkBox.sunday") );
                isFirst = false;
            }else{
                buf.append(", ");
                buf.append( SanBootView.res.getString("WeeklyPane.checkBox.sunday") );
            }
        }
        return buf.toString();
    }

    public ServicesOnVolume convertToServicesOnVolume(){        
        int size = this.elements.size();
        String[] letter_list = new String[size];
        for( int i=0; i<size; i++ ){
            PPProfileItem item = this.elements.get( i );
            letter_list[i] = item.getVolMap().getVolDiskLabel().substring( 0,1 ).toUpperCase();
        }

        String dgName = this.dg_name;
        if( dgName == null || dgName.equals("") ){
            dgName = "dg-" + letter_list[ 0 ];
        }

        ServicesOnVolume sv = new ServicesOnVolume( dgName );
        sv.setVolumeList( letter_list );
        sv.setServiceList( this.getMainDiskItem().getVolMap().getServices() );
        return sv;
    }

    /**
     * @return the host_ip
     */
    public String getHost_ip() {
        return host_ip;
    }

    /**
     * @param host_ip the host_ip to set
     */
    public void setHost_ip(String host_ip) {
        this.host_ip = host_ip;
    }

    /**
     * @return the host_port
     */
    public int getHost_port() {
        return host_port;
    }

    /**
     * @param host_port the host_port to set
     */
    public void setHost_port(int host_port) {
        this.host_port = host_port;
    }

    /**
     * @return the host_id
     */
    public int getHost_id() {
        return host_id;
    }

    /**
     * @param host_id the host_id to set
     */
    public void setHost_id(int host_id) {
        this.host_id = host_id;
    }

    /**
     * @return the host_uuid
     */
    public String getHost_uuid() {
        return host_uuid;
    }

    /**
     * @param host_uuid the host_uuid to set
     */
    public void setHost_uuid(String host_uuid) {
        this.host_uuid = host_uuid;
    }
}
