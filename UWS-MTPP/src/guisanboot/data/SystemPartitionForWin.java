/*
 * SystemPartitionForWin.java
 *
 * Created on 2007/1/4, PM�1:44
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import mylib.tool.Check;

/**
 *
 * @author Administrator
 */
public class SystemPartitionForWin implements Comparable{
    public final static String Partition_CMDP_NAME = "name";
    public final static String Partition_CMDP_UUID = "uuid";
    public final static String Partition_CMDP_VOLNAME = "volname";
    public final static String Partition_CMDP_ISCSI = "iscsi";
    public final static String Partition_CMDP_SIZE = "size";
    public final static String Partition_CMDP_LAYOUTCNT = "layoutcount";
    public final static String Partition_CMDP_LETTER = "letter";
    public final static String Partition_CMDP_HASFS = "hasfs";
    public final static String Partition_CMDP_LAYOUTS = "layouts";
    public final static String Partition_CMDP_FS = "fs";
    public final static String Partition_CMDP_IBOOT = "isboot";

    public final static String WINFS_NTFS  = "NTFS";
    public final static String WINFS_FAT   = "FAT";
    public final static String WINFS_FAT32 = "FAT32";
    public final static String WINFS_FAT16 = "FAT16";

    private  String host_ip;
    private int host_port;
    private String host_uuid;
    private int host_id;
    private String hostName;

    public String disklabel;
    public String isActive="";
    public String label;
    public String volInfo="";
    public String fsType;
    public String size; // MB or byte ( PartitionLength )
    public String available; // MB
    public String vender;
    public String iscsiVar="";
    public String uuid =""; // for cmdp, e.g. name=4809b724-099d-4685-8185-7a6d6c75f029_3225
    public String volname=""; // for cmdp, e.g. volname={1c21d34b-5791-11dc-aa10-000c29779761}
    public String layoutcount="";// for cmdp, e.g.
    public String layouts=""; // for cmdp, e.g. 2,32256,8578934784;
    public String iscsi="";   // for cmdp, e.g. iscsi=1 , 当为0时表示是本地盘，为1时表示是iscsi盘
    public String hasfs="";   // for cmdp, e.g. hasfs=1
    public String isboot=""; // for cmdp, e.g. isboot=0 与isActive等效

    public String part_sartOffet=""; // StartingOffset
    public String part_hiddenSec=""; // HiddenSectors
    public String part_number=""; // PartitonNumber
    public String part_type=""; // PartitionType
    public String part_Boot=""; // BootIndicator
    public String part_recog=""; // RecognizedPartition
    public String part_rewr=""; // RewritePartition
    public String part_oldLetter=""; // OldLetter
    public String part_volname=""; // VolName
    public String part_mntLetter=""; // MountLetter

    public int type = ResourceCenter.CMD_TYPE_MTPP ;

    public int displayMode = 0; // 0 is default

    /** Creates a new instance of SystemPartitionForWin */
    public SystemPartitionForWin() {
    }
    
    @Override public String toString(){
        if( displayMode == 0 ){
            if( !disklabel.equals("") ){
                return disklabel;
            }else{
                return this.uuid;
            }
        }else{
            if( !disklabel.equals("") ){
                return "[ "+ getHostName()+" ] "+disklabel;
            }else{
                return "[ "+ getHostName()+" ] "+this.uuid;
            }
        }
    }

    public String getSingleDiskLetter(){
        if( this.disklabel.equals("") ){
            return "";
        }else{
            return disklabel.substring(0,1).toUpperCase();
        }
    }

    public String getDiskLabel(){
        if( !this.disklabel.equals("") ){
            return disklabel;
        }else{
            return uuid;
        }
    }
    
    public String getDskUUID(){
        return uuid;
    }

    public void setDisplayMode( int mode ){
        this.displayMode = mode;
    }
    
    public long getAvailableInMega(){
        long lSize = -1L;
        try{
            lSize = Long.parseLong( available );
            return lSize;
        }catch( Exception ex ){
            return lSize;
        }
    }
    
    public long getOccupiedInMega(){
SanBootView.log.info(getClass().getName()," disk total size : "+size +" disk avaliable size: "+available );
        long lSize = -1L;
        long lASize = -1L;
        try{
            lSize = Long.parseLong( size );
            lASize = Long.parseLong( available );
            return ( lSize - lASize );
        }catch( Exception ex ){
            return -1L;
        }
    }
    
    // 比实际值多一点
    public long getSizeInGiga(){
        long lSize = -1L;
        
        try{
            if( type == ResourceCenter.CMD_TYPE_CMDP ){
                lSize = Long.parseLong( size );
                float val = (float)( lSize/(1024.0*1024.0) );
                return (long)( val + 1023 )/1024;
            }else{
                lSize = Long.parseLong( size );
                return (lSize+1023)/1024;
            }
        }catch(Exception ex){
            return lSize;
        }
    }
    
    public String getSizeInBytes(){
        return this.size;
    }

    public long getLongSizeInBytes(){
        try{
            long val = Long.parseLong( size );
            return val;
        }catch( Exception ex ){
            return -1L;
        }
    }
    
    public String getSizeStr(){
        try{
            long lSize = Long.parseLong( size );
            float val = (float)( lSize/(1024.0*1024.0*1024.0) );
            if( val >1.0 ){
                return  Check.getSimpleFloat( val )+"GB";
            }else{
                val = (float)( lSize/(1024.0*1024.0) );
                return Check.getSimpleFloat( val )+"MB";
            }
        }catch(Exception ex){
            return size +"B";
        }
    }
    
    public boolean isActivePart(){
        return isActive.equals("*") || isboot.equals("1");
    }
    
    public int getTargetID(){
        if( !iscsiVar.equals("") ){
            int idx = iscsiVar.indexOf(":");
            if( idx>=0 ){
                String _id = iscsiVar.substring( idx+1 );
                try{
                    return Integer.parseInt( _id );
                }catch(Exception ex){
                    return -1;
                }
            }else{
                return -1;
            }
        }{
            return -1;
        }
    }
    
    public boolean isSamePartition( String letter ){
        return disklabel.substring( 0,1 ).toUpperCase().equals( letter.substring( 0,1 ).toUpperCase() );
    }

    public boolean isMTPPType(){
        return ( this.type == ResourceCenter.CMD_TYPE_MTPP ) ;
    }

    public boolean isCMDPType(){
        return (this.type == ResourceCenter.CMD_TYPE_CMDP );
    }
    
    // 都统一成文剑要求的格式
    public String getVolInfo(){
        if( this.volInfo.length() > 0 ){
            if( this.volInfo.substring(0,1).equals("{") ){ // 使用uws_agt_list_volume（chenlianwu）命令得到的volinfo
                return "\\\\?\\Volume"+volInfo+"\\";
            }else{
                return this.volInfo;
            }
        }else{
            return this.volInfo;
        }
    }

    public String prtMe(){
        return this.disklabel +" "+this.label +" "+this.fsType+" "+getHost_uuid();
    }
    
    public int compareTo( Object partObj ){
        SystemPartitionForWin part = (SystemPartitionForWin)partObj;
        String comStr1 = this.toString();
        String comStr2 = part.toString();
        return comStr1.compareToIgnoreCase( comStr2 );
    }
    
    public static boolean isValidFS( String fsType ){
        String aFsType = fsType.toUpperCase();
        return aFsType.equals( WINFS_NTFS )|| aFsType.equals( WINFS_FAT ) || 
                aFsType.equals( WINFS_FAT32 ) || aFsType.equals( WINFS_FAT16 ) || aFsType.startsWith( WINFS_FAT );
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
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getDiskUUID(){
        if( !this.uuid.equals("") ){
            int index = uuid.indexOf("_");
            return uuid.substring( 0,index );
        }else{
            return "";
        }
    }
}
