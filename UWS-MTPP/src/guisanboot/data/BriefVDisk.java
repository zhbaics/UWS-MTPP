/*
 * BriefVDisk.java
 *
 * Created on 2008/2/19,�AM 11:05
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

/**
 *
 * @author Administrator
 */
public class BriefVDisk implements Comparable{
    public final static String BRVDisk_ID ="id";
    public final static String BRVDisk_PoolID ="poolid";
    public final static String BRVDisk_Status="status";
    public final static String BRVDisk_CRTime="create_time";
    public final static String BRVDisk_Bytes="bytes";
    
    private int local_snap_id = -999;
    private int pool_id = -999;
    
    // 0 is closed, 1 is view ,2 is original disk or main root ,3 is application mirror,
    // -3 is mirror dest, -1 has been deleted , -2 created by mirror,
    // -4 is unlimited incremental mirror dest, -6 created by -4
    private int opened_type=-999;
    
    private String create_time; //14
    private long size = 0L;
    private String unit = "bytes";
    
    /** Creates a new instance of VSnap */
    public BriefVDisk() {
    }
    
    public BriefVDisk(
        int _local_snap_id,
        int _pool_id,
        int _opened_type,
        String _create_time,
        long _size,
        String _unit
    ){
        setLocal_snap_id(_local_snap_id);
        setPool_id(_pool_id);
        setOpened_type(_opened_type);
        create_time = _create_time;
        setSize(_size);
        setUnit(_unit);
    }
    
    //格式:yearmonthdayhourminsec
    //       4   2   2  2   2  2
    //e.g.  2004 11 23 13 27 45
    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreateTimeStr(){
        return BasicVDisk.getCreateTimeStr( create_time );
    }

    public int getLocal_snap_id() {
        return local_snap_id;
    }

    public void setLocal_snap_id(int local_snap_id) {
        this.local_snap_id = local_snap_id;
    }

    public int getPool_id() {
        return pool_id;
    }

    public void setPool_id(int pool_id) {
        this.pool_id = pool_id;
    }

    public int getOpened_type() {
        return opened_type;
    }

    public boolean isSnap(){
        return ( opened_type == BasicVDisk.TYPE_OPENED_SNAP );
    }
    public boolean isDeletingSnap(){
        return ( opened_type == BasicVDisk.TYPE_OPEND_DEL_SNAP );
    }
    public boolean isDisk(){
        return ( opened_type == BasicVDisk.TYPE_OPENED_DISK );
    }
    public boolean isView(){
        return ( opened_type == BasicVDisk.TYPE_OPENED_VIEW );
    }
    public boolean isMirroredSnap(){
        return ( opened_type == BasicVDisk.TYPE_OPENED_CrtByMirr );
    }
    public boolean isMirroredSnapHeader(){
        return ( opened_type == BasicVDisk.TYPE_OPENED_MiirDest );
    }
    public boolean isUIMirroredSnap(){
        return ( opened_type == BasicVDisk.TYPE_OPEND_CrtByUIMirDest );
    }
    public boolean isUIMirroredSnapHeader(){
        return ( opened_type == BasicVDisk.TYPE_OPEND_UIMirDest );
    }
    
    public void setOpened_type(int opened_type) {
        this.opened_type = opened_type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int compareTo( Object diskObj ){
        BriefVDisk disk = (BriefVDisk)diskObj;
        return this.getCreateTimeStr().compareTo( disk.getCreateTimeStr() );
    }
}
