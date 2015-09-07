/*
 * BakObject.java
 *
 * Created on July 25, 2008, 11:43 AM
 */

package guisanboot.datadup.data;

import guisanboot.res.ResourceCenter;

/**
 *
 * @author  Administrator
 */
public class BakObject {
    public final static String BAKOBJ_RECFLAG  = "Record:";
    public final static String BAKOBJ_ID       = "bkobj_id";
    public final static String BAKOBJ_FNAME    = "bkobj_filename";
    public final static String BAKOBJ_FTYPE    = "bkobj_filetype";
    public final static String BAKOBJ_INCLUDE  = "bkobj_include";
    public final static String BAKOBJ_INCLTYPE = "bkobj_incl_type";
    public final static String BAKOBJ_INCLCASE = "bkobj_incl_case";
    public final static String BAKOBJ_EXCLUDE  = "bkobj_exclude";
    public final static String BAKOBJ_EXCLTYPE = "bkobj_excl_type";
    public final static String BAKOBJ_EXCLCASE = "bkobj_excl_case";
    public final static String BAKOBJ_SINGLEFS = "bkobj_single_fs";
    public final static String BAKOBJ_BS       = "bkobj_bs";
    public final static String BAKOBJ_SEEK     = "bkobj_seek";
    public final static String BAKOBJ_COUNT    = "bkobj_count";
    public final static String BAKOBJ_LASTBK   = "bkobj_last_bk";
    public final static String BAKOBJ_RAWDEV   = "bkobj_raw";
    public final static String BAKOBJ_SN       = "bkobj_sn";
    public final static String BAKOBJ_CLIENTID = "bkobj_client";
    public final static String BAKOBJ_BAKLEVEL = "bkobj_level";
    public final static String BAKOBJ_DESC      = "bkobj_desc";
    public final static String BAKOBJ_ACCTID    = "bkobj_account_id";
    public final static String BAKOBJ_MAX_BKNUM = "bkobj_max_backuplevel";
    
    public final static String BAKLEVEL_FULL = "common.bakMode.full";
    public final static String BAKLEVLE_INC  = "common.bakMode.incremental";
    public final static String BAKLEVLE_NONE = "";
    public final static String BAKLEVEL_UNKNOWN = "Unknown";
    
    public final static int BAKLEVLE_0 = 0;
    public final static int BAKLEVLE_1 = 1;
    
    private long id = -1L;
    private String fileName="";
    private String fileType="";  //4: file 5: database 6: raw dev
    
    // 只要bkobj中的include域不为空,那么就表示真正要备份的是include所表示的内容,
    // 这适用于UWS上的Linux的基于LVM快照的物理增量的后处理
    private String include="";
    
    private String inclType="";
    private boolean isInclCase=true;
    private String exclude="";
    private String exclType="";
    private boolean isExclCase=true;
    private boolean isSingleFs=true;
    private int bs=0;
    private int seek = -1;
    private int count=0;
    private String last_bk="";
    private boolean isRawDev=false;
    private int bakObjSN=-1;
    private long clientId=-1;
    private int bklevel=0;
    private String desc="";
    private int uid = 0;
    private int bkobj_max_backuplevel = 7; //缺省值
    
    public BakObject(){
    }
    
    /** Creates a new instance of BakObject */
    public BakObject(
        long _id,
        String _fileName,
        String _fileType,
        String _include,
        String _inclType,
        boolean _isInclCase,
        String _exclude,
        String _exclType,
        boolean _isExclCase,
        boolean _isSingleFs,
        int _bs,
        int _seek,
        int _count,
        String _last_bk,
        boolean _isRawDev,
        int _bakObjSN,
        long _clientId,
        int _bklevel,
        String _desc,
        int _uid,
        int _bkobj_max_bklevel
    ) {
        id = _id;
        fileName = _fileName;
        fileType = _fileType;
        include  = _include;
        inclType = _inclType;
        isInclCase = _isInclCase;
        exclude = _exclude;
        exclType = _exclType;
        isExclCase = _isExclCase;
        isSingleFs = _isSingleFs;
        bs = _bs;
        seek = _seek;
        count = _count;
        last_bk = _last_bk;
        isRawDev = _isRawDev;
        bakObjSN = _bakObjSN;
        clientId = _clientId;
        bklevel = _bklevel;
        desc = _desc;
        uid = _uid;
        bkobj_max_backuplevel = _bkobj_max_bklevel;
    }
    
    public long getBakObjID(){
        return id;
    }
    public void setBakObjID(long _id){
        id = _id;
    }
    
    public String getFileName(){
        return fileName;
    }
    public void setFileName(String _fileName){
        fileName = _fileName;
    }
    
    public String getFileType(){
        return fileType;
    }
    public void setFileType(String _fileType){
        fileType = _fileType;
    }
    
    public int getBakType(){
        try{
            int ftype = Integer.parseInt( fileType );
            return (ftype&ResourceCenter.BAK_TYPE_MASK);
        }catch( Exception ex ){
            return ResourceCenter.BAK_TYPE_UNKNOWN ;
        }
    }
    
    public boolean isSupportPhyInc(){
        try{
            int ftype = Integer.parseInt( fileType );
            return (ftype&ResourceCenter.BAK_TYPE_PHY_INC) !=0;
        }catch(Exception ex){
            return false;
        }
    }
    
    public void setSupportPhyIncFlag( boolean val ){
        try{
            int ftype = Integer.parseInt( fileType );
            if( val ){
                ftype = ftype | ResourceCenter.BAK_TYPE_PHY_INC;
            }else{
                ftype = ftype | ~ResourceCenter.BAK_TYPE_PHY_INC;
            }
            
            fileType = ftype+"";
        }catch( Exception ex ){
            // impossible happen
        }
    }
    
    public String getInclude(){
        return include;
    }
    public void setInclude(String _include){
        include = _include;
    }
    
    public String getInclType(){
        return inclType;
    }
    public void setInclType(String _inclType){
        inclType = _inclType;
    }
    
    public boolean isInclCase(){
        return isInclCase;
    }
    public void setInclCase(boolean _val){
        isInclCase = _val;
    }
    public String getExclude(){
        return exclude;
    }
    public void setExclude(String _exclude){
        exclude = _exclude;
    }
    
    public String getExclType(){
       return exclType;
    }
    public void setExclType(String _exclType){
        exclType = _exclType;
    }
    
    public boolean isExclCase(){
        return isExclCase;
    }
    public void setExclCase(boolean _val){
        isExclCase = _val;
    }
    
    public boolean isSingleFs(){
        return isSingleFs;
    }
    public void setSingleFs(boolean _val){
        isSingleFs = _val;
    }
  
    public int getBS(){
        return bs;
    }
    public void setBS(int _bs){
        bs = _bs;
    }
    
    public int getSeek(){
        return seek;
    }
    public void setSeek(int _seek){
        seek = _seek;
    }
    
    public int getCount(){
        return count;
    }
    public void setCount(int _count){
        count = _count;
    }
    
    public String getLastBk(){
        return last_bk;
    }
    public void setLastBk(String _lastBk){
        last_bk = _lastBk;
    }
    
    public String getLastBkTimeStr(){
        if( last_bk == null || last_bk.equals("") ){
            return "";
        }else{
            try{
                return getYear()+"-"+getMonth()+"-"+getDay()+"   "+
                getHour()+" :"+getMinute()+" :"+getSecond();
            }catch(Exception ex){
                return "";
            }
        }
    }
  
    public int getYear(){
        String _year = last_bk.substring(0,4);
        try{
            return Integer.parseInt(_year);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getMonth(){
        String _month = last_bk.substring(4,6);
        try{
            return Integer.parseInt( _month );
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getDay(){
        String day = last_bk.substring(6,8);
        try{
            return Integer.parseInt( day );
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getHour(){
        String hour = last_bk.substring(8,10);
        try{
            return Integer.parseInt(hour);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getMinute(){
        String minute = last_bk.substring(10,12);
        try{
            return Integer.parseInt(minute);
        }catch(Exception ex){
            return -1;
        }
    }
    
    public int getSecond(){
        String second = last_bk.substring(12);
        try{
            return Integer.parseInt( second );
        }catch(Exception ex){
            return -1;
        }
    }
    
    public boolean isRawDev(){
        return isRawDev;
    }
    public void setRawDev(boolean _val){
        isRawDev = _val;
    }
   
    public int getBakObjSN(){
        return bakObjSN;
    }
    public void setBakObjSN(int _bakObjSN){
        bakObjSN = _bakObjSN;
    }
    
    public long getClientId(){
        return clientId;
    }
    public void setClientId(long _cliId){
        clientId = _cliId;
    }   
    
    public int getBakLevel(){
        return bklevel;
    }
    public void setBakLevel(int _bklevel){
        bklevel = _bklevel;
    }
    
    public String getDesc(){
        return desc;
    }
    public void setDesc(String _desc){
        desc = _desc;
    }
    
    public int getUid(){
        return uid;
    }
    public void setUid( int val ){
        uid = val;
    }
    
    public int getMaxBkNum(){
        return bkobj_max_backuplevel;
    }
    public void setMaxBkNum( int val ){
        bkobj_max_backuplevel = val;
    }
    
    @Override public String toString(){
        return id+"";
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        
        if( getBakObjID() >=0 ){ 
            buf.append("bkobj_id="+getBakObjID()+"\n");
        }
      
        buf.append("bkobj_filename="+getFileName()+"\n");
        buf.append("bkobj_filetype="+getFileType()+"\n");
        buf.append("bkobj_include="+getInclude()+"\n");
        buf.append("bkobj_incl_type="+getInclType()+"\n");
        buf.append("bkobj_incl_case="+
            (isInclCase()?"YES":"NO")+"\n"
        );
        buf.append("bkobj_exclude="+getExclude()+"\n");
        buf.append("bkobj_excl_type="+getExclType()+"\n");
        buf.append("bkobj_excl_case="+
            (isExclCase()?"YES":"NO")+"\n"
        );
        buf.append("bkobj_single_fs="+
            (isSingleFs()?"YES":"NO")+"\n"
        );
        buf.append("bkobj_bs="+getBS()+"\n");
        buf.append("bkobj_seek="+getSeek()+"\n");
        buf.append("bkobj_count="+getCount()+"\n");
        buf.append("bkobj_last_bk="+getLastBk()+"\n");
        buf.append("bkobj_raw="+
            (isRawDev()?"YES":"NO")+"\n"
        );
        buf.append("bkobj_sn="+getBakObjSN()+"\n");
        buf.append("bkobj_client="+getClientId()+"\n");
        buf.append("bkobj_level="+getBakLevel()+"\n");
        buf.append("bkobj_desc="+getDesc()+"\n");
        buf.append("bkobj_account_id="+getUid()+"\n");
        buf.append("bkobj_max_backuplevel="+getMaxBkNum()+"\n");
        
        return buf.toString();
    }
}
