/*
 * BasicVDisk.java
 *
 * Created on 2008/2/19,��AM 11:05
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import java.util.ArrayList;
import guisanboot.ui.*;
import java.util.regex.Pattern;
import mylib.tool.Check;

/**
 *
 * @author Administrator
 */
public class BasicVDisk implements Comparable{
    public final static String BVDisk_RECFLAG="Record:";
    public final static String BVDisk_Start ="vdisk info";
    public final static String BVDisk_Snap_Local_SnapId ="snap_local_snapid";
    public final static String BVDisk_Snap_Name="snap_name";
    public final static String BVDisk_Snap_Root_ID="snap_root_id";
    public final static String BVDisk_Snap_Pool_ID="snap_pool_id";
    public final static String BVDisk_Snap_Parent="snap_parent";
    public final static String BVDisk_Snap_Orig_ID = "snap_orig_id";
    public final static String BVDisk_Snap_BlkSize="snap_block_size";
    public final static String BVDisk_Snap_MaxBlkNo="snap_max_block_no";
    public final static String BVDisk_Snap_Type="snap_type";
    public final static String BVDisk_Snap_OpenType="snap_opened_type";
    public final static String BVDisk_Snap_Child_List="snap_child_list";
    public final static String BVDisk_Snap_TID="snap_target_id";
    public final static String BVDisk_Snap_LocalID="snap_localid";
    public final static String BVDisk_Snap_CrtTime="snap_create_time";
    public final static String BVDisk_Snap_Desc="snap_desc";
    public final static String BVDisk_Snap_Owner="snap_owner";
    
    public final static int TYPE_OPENED_SNAP = 0;
    public final static int TYPE_OPENED_VIEW = 1;
    public final static int TYPE_OPENED_DISK = 2;
    public final static int TYPE_OPENED_AppMirr = 3;
    public final static int TYPE_OPENED_UCSDISK = 4;
    public final static int TYPE_OPENED_MiirDest = -3;  // 镜像卷(由远程传输过来的disk生成的对象)
    public final static int TYPE_OPENED_CrtByMirr = -2; // 镜像卷的快照
    public final static int TYPE_OPEND_DEL_SNAP = -1;   // 正在被删除的快照
    public final static int TYPE_OPEND_UIMirDest = -4;  // 无限增量镜像卷（由远程传输过来的disk生成的对象）
    public final static int TYPE_OPEND_CrtByUIMirDest = -6; //无限增量镜像卷的快照
    

    public final static String BLK_SIZE_4K="4";
    public final static String BLK_SIZE_8K="8";
    public final static String BLK_SIZE_16K="16";
    public final static String BLK_SIZE_32K="32";
    public final static String BLK_SIZE_64K="64";
    public final static String BLK_SIZE_128K="128";
    public final static String BLK_SIZE_256K="256";
    public final static String BLK_SIZE_512K="512";
    public final static String BLK_SIZE_1M="1024";
    
    private int snap_root_id=-999;
    private int snap_local_snapid=-999;
    private String snap_name=""; // 255
    private int snap_pool_id=-999;
    private String snap_map_file; // 255
    private String snap_dat_file; // 255
    private int snap_orig_id=-999;
    private int snap_block_size;
    private int snap_max_block_no; 
    
    // 0 is closed, 1 is view ,2 is original disk or main root ,3 is application mirror,
    // -3 is mirror dest, -1 has been deleted , -2 created by mirror
    private int snap_opened_type = -999;
    
    private int snap_target_id = -999;
    private int snap_parent=-999;
    private String snap_child_list; //2048
    private String snap_create_time; //14
    private int snap_localid = -999;
    private String snap_desc = ""; //255
    private String snap_owner = ResourceCenter.OWNER_MTPP ; // 255
    
    /** Creates a new instance of BasicVDisk */
    public BasicVDisk() {
    }

    public BasicVDisk( int tid,String name ){
        this.snap_target_id = tid;
        this.snap_name = name;
    }
    
    public BasicVDisk(
        int _snap_root_id,
        int _snap_local_snapid,
        String _snap_name,
        int _snap_pool_id,
        String _snap_map_file,
        String _snap_dat_file,
        int _snap_orig_id,
        int _snap_block_size,
        int _snap_max_block_no, 
        int _snap_opened_type,
        int _snap_target_id,
        int _snap_parent,
        String _snap_child_list,
        String _snap_create_time,
        int _snap_localid,
        String _snap_desc,
        String _snap_owner
    ){
        snap_root_id = _snap_root_id;
        snap_local_snapid = _snap_local_snapid;
        snap_name = _snap_name;
        snap_pool_id = _snap_pool_id;
        snap_map_file = _snap_map_file;
        snap_dat_file = _snap_dat_file;
        snap_orig_id = _snap_orig_id;
        snap_block_size = _snap_block_size;
        snap_max_block_no = _snap_max_block_no;
        snap_opened_type = _snap_opened_type;
        snap_target_id = _snap_target_id;
        snap_parent = _snap_parent;
        snap_child_list = _snap_child_list;
        snap_create_time = _snap_create_time;
        snap_localid = _snap_localid;
        snap_desc = _snap_desc;
        snap_owner = _snap_owner;
    }
    
    public int getSnap_root_id() {
        return snap_root_id;
    }

    public void setSnap_root_id(int snap_root_id) {
        this.snap_root_id = snap_root_id;
    }

    public int getSnap_local_snapid() {
        return snap_local_snapid;
    }

    public void setSnap_local_snapid(int snap_local_snapid) {
        this.snap_local_snapid = snap_local_snapid;
    }

    public String getSnap_name() {
        return snap_name;
    }

    public void setSnap_name(String snap_name) {
        this.snap_name = snap_name;
    }

    public int getSnap_pool_id() {
        return snap_pool_id;
    }

    public void setSnap_pool_id(int snap_pool_id) {
        this.snap_pool_id = snap_pool_id;
    }

    public String getSnap_map_file() {
        return snap_map_file;
    }

    public void setSnap_map_file(String snap_map_file) {
        this.snap_map_file = snap_map_file;
    }

    public String getSnap_dat_file() {
        return snap_dat_file;
    }

    public void setSnap_dat_file(String snap_dat_file) {
        this.snap_dat_file = snap_dat_file;
    }

    public int getSnap_orig_id() {
        return snap_orig_id;
    }

    public void setSnap_orig_id(int snap_orig_id) {
        this.snap_orig_id = snap_orig_id;
    }

    public int getSnap_block_size() {
        return snap_block_size;
    }

    public void setSnap_block_size(int snap_block_size) {
        this.snap_block_size = snap_block_size;
    }

    public int getSnap_max_block_no() {
        return snap_max_block_no;
    }

    public void setSnap_max_block_no(int snap_max_block_no) {
        this.snap_max_block_no = snap_max_block_no;
    }
    
    public long getCap(){
        long blk_size = ( 1 << snap_block_size );
        return  ( blk_size*snap_max_block_no ) ;
    }
    
    public static String getCapStr( long lSize ){
        float val;
        
        if( lSize <=0 ) return "0";
        
        try{
            val = (float)( lSize/(1024.0*1024.0*1024.0) );        
            if( val >=1.0 ){
                return  Check.getSimpleFloat( val )+"GB";
            }else{
                val = (float)( lSize/(1024.0*1024.0) );
                if( val >=1.0 ){
                    return Check.getSimpleFloat( val )+"MB";
                }else{
                    val = (float)(lSize/1024.0);
                    if( val >=1.0 ){
                        return Check.getSimpleFloat( val )+"KB";
                    }else{
                        return lSize + "B";
                    }
                }
            }
        }catch(Exception ex){
            return lSize+"B";
        }
    }
    
    public String getCapStr(){ 
        long lSize = getCap();
        return getCapStr( lSize );
    }
    
    public String getCapStr1(){
        float val;
        
        long lSize = getCap();
    
        try{
            val = (float)( lSize/(1024.0*1024.0*1024.0) );        
            if( val >=1.0 ){
                return  Check.getSimpleFloat( val )+"";
            }else{
                val = (float)( lSize/(1024.0*1024.0) );
                if( val >=1.0 ){
                    return Check.getSimpleFloat( val )+"";
                }else{
                    val = (float)(lSize/1024.0);
                    if( val >=1.0 ){
                        return Check.getSimpleFloat( val )+"";
                    }else{
                        return lSize+"";
                    }
                }
            }
        }catch(Exception ex){
            return lSize+"";
        }
    }
    
    public int getSnap_opened_type() {
        return snap_opened_type;
    }

    public void setSnap_opened_type(int snap_opened_type) {
        this.snap_opened_type = snap_opened_type;
    }

    public boolean isSnap(){
        return  ( snap_opened_type == BasicVDisk.TYPE_OPENED_SNAP );
    }
    public boolean isDeletingSnap(){
        return ( snap_opened_type == BasicVDisk.TYPE_OPEND_DEL_SNAP );
    }
    public boolean isView(){
        return ( snap_opened_type == BasicVDisk.TYPE_OPENED_VIEW );
    }
    
    public boolean isUcsDisk(){
        return ( snap_opened_type == BasicVDisk.TYPE_OPENED_UCSDISK );
    }
    
    public boolean isOriDisk(){
        return ( snap_opened_type == BasicVDisk.TYPE_OPENED_DISK );
    }
    public boolean isCmdpDisk(){
        return ( snap_opened_type == BasicVDisk.TYPE_OPENED_AppMirr );
    }
    public boolean isMirroredSnap(){
        return ( snap_opened_type == BasicVDisk.TYPE_OPENED_CrtByMirr );
    }
    public boolean isMirroredSnapHeader(){
        return ( snap_opened_type == BasicVDisk.TYPE_OPENED_MiirDest );
    }
    public boolean isUIMirroredSnap(){
        return ( snap_opened_type == BasicVDisk.TYPE_OPEND_CrtByUIMirDest );
    }
    public boolean isUIMirroredSnapHeader(){
        return ( snap_opened_type == BasicVDisk.TYPE_OPEND_UIMirDest );
    }
    public int getSnap_target_id() {
        return snap_target_id;
    }
    public int getTargetID(){
        return snap_target_id;
    }
    
    public void setSnap_target_id(int snap_target_id) {
        this.snap_target_id = snap_target_id;
    }

    public int getSnap_parent() {
        return snap_parent;
    }

    public void setSnap_parent(int snap_parent) {
        this.snap_parent = snap_parent;
    }

    public String getSnap_child_list() {
        return snap_child_list;
    }

    public void setSnap_child_list(String snap_child_list) {
        this.snap_child_list = snap_child_list;
    }
    
    public ArrayList getChildList(){        
        ArrayList<Integer> ret = new ArrayList<Integer>();
        if( !snap_child_list.equals("") ){
            String[] list = Pattern.compile(":").split( snap_child_list,-1 );
            if( list.length > 0 ){
                for( int i=0; i<list.length; i++ ){
                    if( !list[i].equals("") ){
                        try{   
                            int snap_id = Integer.parseInt( list[i] );
SanBootView.log.debug( getClass().getName()," child local_snap_id : "+ snap_id +" father: "+this.snap_local_snapid );                            
                            ret.add( new Integer( snap_id ) );
                        }catch(Exception ex){
SanBootView.log.error( getClass().getName()," Bad local_snap_id in child list: "+ list[i] );                    
                        }
                    }
                }
            }
        }
        
        return ret;
    }
    
    //格式: yearmonthdayhourminsec
    //       4   2   2  2   2  2
    //e.g.  2004 11 23 13 27 45
    public String getSnap_create_time() {
        return snap_create_time;
    }

    public void setSnap_create_time(String snap_create_time) {
        this.snap_create_time = snap_create_time;
    }
    
    public int getSnap_localid() {
        return snap_localid;
    }

    public void setSnap_localid(int snap_localid) {
        this.snap_localid = snap_localid;
    }
    public String getStatus(){
        if( this.snap_localid > 0 ){
            return SanBootView.res.getString("common.status.online");
        }else{
            return SanBootView.res.getString("common.status.offline");
        }
    }
      
    public String getSnap_desc() {
        return snap_desc;
    }
    
    public void setSnap_desc(String snap_desc) {
        this.snap_desc = snap_desc;
    }
    
    public String getSnap_owner(){
        return snap_owner;
    }
    public boolean isUWSDisk(){
        return snap_owner.equals( ResourceCenter.OWNER_MTPP ) || snap_owner.equals( ResourceCenter.OWNER_CMDP );
    }
    public void setSnap_owner( String snap_owner ){
        this.snap_owner = snap_owner;
    }

    public static String getSimpleCrtTimeStr( String time ){
        if( time == null || time.equals("") ){
            return "";
        }else{
            try{
                int month = getMonth( time );
                String monStr = month+"";
                if( month <10 ){
                    monStr="0"+monStr;
                }
                int day = getDay( time );
                String dayStr = day+"";
                if( day <10 ){
                    dayStr ="0"+dayStr;
                }

                int hour = getHour( time );
                String hourStr = hour+"";
                if( hour<10 ){
                    hourStr = "0"+hourStr;
                }
                int min = getMinute( time );
                String minStr =min+"";
                if( min < 10 ){
                    minStr ="0"+minStr;
                }

                int sec = getSecond( time );
                String secStr = sec+"";
                if( sec < 10 ){
                    secStr = "0"+secStr;
                }
                return getYear( time )+monStr+dayStr+hourStr+minStr+secStr;
            }catch(Exception ex){
                return "";
            }
        }
    }

    public static String getCreateTimeStr( String time ){
        if( time == null || time.equals("") ){
            return "";
        }else{
            try{
                int month = getMonth( time );
                String monStr = month+"";
                if( month <10 ){
                    monStr="0"+monStr;
                }
                int day = getDay( time );
                String dayStr = day+"";
                if( day <10 ){
                    dayStr ="0"+dayStr;
                }
                
                int hour = getHour( time );
                String hourStr = hour+"";
                if( hour<10 ){
                    hourStr = "0"+hourStr;
                }
                int min = getMinute( time );
                String minStr =min+"";
                if( min < 10 ){
                    minStr ="0"+minStr;
                }
                
                int sec = getSecond( time );
                String secStr = sec+"";
                if( sec < 10 ){
                    secStr = "0"+secStr;
                }
                return getYear( time )+"/"+monStr+"/"+dayStr+" " + 
                        hourStr+":"+minStr+":"+secStr;
            }catch(Exception ex){
                return "";
            }
        }
    }
    
    public String getCreateTimeStr(){
        return BasicVDisk.getCreateTimeStr( snap_create_time );
    }

    public String getSimpleCrtTimeStr(){
        return BasicVDisk.getSimpleCrtTimeStr( snap_create_time );
    }

    public static int getYear( String time ){
        String _year = time.substring(0,4);
        try{
            return Integer.parseInt(_year);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getYear(){
        return BasicVDisk.getYear( snap_create_time );
    }
    
    public static int getMonth( String time ){
        String _month = time.substring(4,6);
        try{
            return Integer.parseInt( _month );
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getMonth(){
        return BasicVDisk.getMonth( snap_create_time );
    }
    
    public static int getDay( String time ){
        String day = time.substring(6,8);
        try{
            return Integer.parseInt( day );
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getDay(){
        return BasicVDisk.getDay( snap_create_time );
    }
    
    public static int getHour( String time ){
        String hour = time.substring(8,10);
        try{
            return Integer.parseInt(hour);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getHour(){
        return BasicVDisk.getHour( snap_create_time );
    }
    
    public static int getMinute( String time ){
        String minute = time.substring(10,12);
        try{
            return Integer.parseInt(minute);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getMinute(){
        return BasicVDisk.getMinute( snap_create_time );
    }
    
    public static int getSecond( String time ){
        String second = time.substring(12);
        try{
            return Integer.parseInt( second );
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getSecond(){
        return BasicVDisk.getSecond( snap_create_time );
    }
    
    public static ArrayList getBlkSizeList(){
        ArrayList<String> ret = new ArrayList<String>(9);
        ret.add( BasicVDisk.BLK_SIZE_4K );
        ret.add( BasicVDisk.BLK_SIZE_8K );
        ret.add( BasicVDisk.BLK_SIZE_16K );
        ret.add( BasicVDisk.BLK_SIZE_32K );
        ret.add( BasicVDisk.BLK_SIZE_64K );
        ret.add( BasicVDisk.BLK_SIZE_128K );
        ret.add( BasicVDisk.BLK_SIZE_256K );
        ret.add( BasicVDisk.BLK_SIZE_512K );
        ret.add( BasicVDisk.BLK_SIZE_1M );
        return ret;
    }
    
    public static int getBlkSizeFromStr( String bkSize ){
        if( BasicVDisk.BLK_SIZE_4K.equals( bkSize ) ){
            return 12;
        }else if( BasicVDisk.BLK_SIZE_8K.equals( bkSize ) ){
            return 13;
        }else if( BasicVDisk.BLK_SIZE_16K.equals( bkSize ) ){
            return 14;
        }else if( BasicVDisk.BLK_SIZE_32K.equals( bkSize ) ){
            return 15;
        }else if (BasicVDisk.BLK_SIZE_64K.equals( bkSize ) ){
            return 16;
        }else if( BasicVDisk.BLK_SIZE_128K.equals( bkSize ) ){
            return 17;
        }else if( BasicVDisk.BLK_SIZE_256K.equals( bkSize ) ){
            return 18;
        }else if( BasicVDisk.BLK_SIZE_512K.equals( bkSize ) ){
            return 19;
        }else if( BasicVDisk.BLK_SIZE_1M.equals( bkSize ) ){
            return 20;
        }else {
            return 17;
        }
    }
    
    public static String getBlkSizeStr( int blkSize ){
        if( blkSize == 12 ){
            return BasicVDisk.BLK_SIZE_4K;
        }else if( blkSize == 13 ){
            return BasicVDisk.BLK_SIZE_8K;
        }else if( blkSize == 14 ){
            return BasicVDisk.BLK_SIZE_16K;
        }else if( blkSize == 15 ){
            return BasicVDisk.BLK_SIZE_32K;
        }else if( blkSize == 16 ){
            return BasicVDisk.BLK_SIZE_64K;
        }else if( blkSize == 17 ){
            return BasicVDisk.BLK_SIZE_128K;
        }else if( blkSize == 18 ){
            return BasicVDisk.BLK_SIZE_256K;
        }else if( blkSize == 19 ){
            return BasicVDisk.BLK_SIZE_512K;
        }else if( blkSize == 20 ){
            return BasicVDisk.BLK_SIZE_1M;
        }else {
            return BasicVDisk.BLK_SIZE_128K;
        }
    }

    public int compareTo( Object diskObj ){
        BasicVDisk disk = (BasicVDisk)diskObj;
        return this.getCreateTimeStr().compareTo( disk.getCreateTimeStr() );
    }
}
