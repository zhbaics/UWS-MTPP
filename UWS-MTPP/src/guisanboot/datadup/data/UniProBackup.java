package guisanboot.datadup.data;

import java.io.*;
import java.util.*;

public class UniProBackup extends AbstractUniProfile {
    public final static int DEFAULT_VAL_DEL_REDUNDANT = 10;
    
    private String minDepth;
    private String maxDepth;
    private String backupLevel;
    private String beginRstLevel;
    private String lastTime;
    private String backupRaw;
    private String sn;
    private String src;
    private String srcType;
    private String srcOS;
    private String seek;
    private String bs;
    private String include;
    private String inclType;
    private String inclCase;
    private String exclude;
    private String exclType;
    private String exclCase;
    private String singleFs;
    private String target;
    private String forceOvwr;
    private String ovwrOld;
    private String curtime;
    private String restoreRaw;
    private String volMaxFile;
    private String zipFileList;
    private String kill_prescript;
    private String blk_inc_rst_mode;
    
    // 1：不管时间及文件大小是否改变，一律对其进行物理扫描 0：只有时间及大小变了，才扫描
    private String inc_no_judge_time;
    
    // 1：快照必须有效的情况下，才能备份；否则备份失败   0：快照一旦失败，按普通方式进行备份
    // 该参数只对有快照的系统有效（比如xp,win2003等）；若系统没有快照，则不管该参数
    private String bak_with_snap;
    
    // 从备份源中要排斥的内容,即这些内容不进行备份
    private String exclude_from_bak;
    
    private String delete_redundant_file; // default val: 10,多少次后将执行删除冗余文件操作,即源盘数据删除则在目的盘也删除此文件
    private String ignore_all_copy_error; // 是否忽略数据传输中的所有错误
    private String crt_snap; // 0: 不做快照   1:做快照
    
    public UniProBackup(){
        this("0","0","0","0","","NO","","","","","0","0","","","YES","","","YES","YES","","YES","","","","10000","","1","1","0","1","","10","0","1");
    }

    public UniProBackup(
        String _minDepth,
        String _maxDepth,
        String _backupLevel,
        String _beginRstLevel,
        String _lastTime,
        String _backupRaw,
        String _sn,
        String _src,
        String _srcType,
        String _srcOS,
        String _seek,
        String _bs,
        String _include,
        String _inclType,
        String _inclCase,
        String _exclude,
        String _exclType,
        String _exclCase,
        String _singleFs,
        String _target,
        String _forceOvwr,
        String _ovwrOld,
        String _curtime,
        String _restoreRaw,
        String _volMaxFile,
        String _zipFileList,
        String _kill_prescript,
        String _blk_inc_rst_mode,
        String _inc_no_judge_time,
        String _bak_with_snap,
        String _exclude_from_bak,
        String _delete_redundant_file,
        String _ignore_all_copy_error,
        String _crt_snap
    )
    {
        minDepth    = _minDepth;
        maxDepth    = _maxDepth;
        backupLevel = _backupLevel;
        beginRstLevel=_beginRstLevel;
        lastTime    = _lastTime;
        backupRaw   = _backupRaw;
        sn          = _sn;
        
        src         = _src;
        srcType     = _srcType;
        srcOS       = _srcOS;
        seek        = _seek;
        bs          = _bs;
        
        include     = _include;
        inclType    = _inclType;
        inclCase    = _inclCase;
        
        exclude     = _exclude;
        exclType    = _exclType;
        exclCase    = _exclCase;
        
        singleFs    = _singleFs;
        target      = _target;
        forceOvwr   = _forceOvwr;
        ovwrOld     = _ovwrOld;
        curtime     = _curtime;
        restoreRaw  = _restoreRaw;
        volMaxFile  = _volMaxFile;
        zipFileList = _zipFileList;
        kill_prescript= _kill_prescript;
        blk_inc_rst_mode = _blk_inc_rst_mode;
        inc_no_judge_time= _inc_no_judge_time; 
        bak_with_snap = _bak_with_snap;
        exclude_from_bak = _exclude_from_bak;
        delete_redundant_file = _delete_redundant_file;
        ignore_all_copy_error = _ignore_all_copy_error;
        crt_snap = _crt_snap;
    }

    public String getMinDepth(){
        return minDepth;
    }
    public void setMinDepth(String _minDepth){
        minDepth = _minDepth;
    }
    
    public String getMaxDepth(){
        return maxDepth;
    }
    public void setMaxDepth(String _maxDepth){
        maxDepth = _maxDepth;
    }
    
    public String getBackupLevel(){
        return backupLevel;
    }
    public void setBackupLevel(String _bakLevel){
        backupLevel = _bakLevel;
    }
   
    public String getBeginRstLevel(){
        return beginRstLevel;
    }
    public void setBeginRstLevel( String val ){
        beginRstLevel = val;
    }
    
    public String getLastTime(){
        return lastTime;
    }
    public void setLastTime( String _lastTime){
        lastTime = _lastTime;
    }
    
    public String getBackupRaw(){
        return backupRaw;
    }
    public void setBackupRaw(String _backupRaw){
        backupRaw = _backupRaw;
    }
    
    public String getSN(){
        return sn;
    }
    public void setSN(String _sn){
        sn = _sn;
    }
    
    public String getSrc(){
        return src;
    }
    public void setSrc(String _src){
        src = _src;
    }
    
    public String getSrcType(){
        return srcType;
    }
    public void setSrcType(String _srcType){
        srcType = _srcType;
    }
    
    public String getSrcOS(){
        return srcOS;
    }
    public void setSrcOS( String val ){
        srcOS = val;
    }
    
    public String getSeek(){
        return seek;
    }
    public void setSeek(String _seek){
        seek = _seek;
    }
    
    public String getBS(){
        return bs;
    }
    public void setBS(String _bs){
        bs = _bs;
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
    
    public String getInclCase(){
        return inclCase;
    }
    public void setInclCase(String _inclCase){
        inclCase = _inclCase;
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
    
    public String getExclCase(){
        return exclCase;
    }
    public void setExclCase(String _exclCase){
        exclCase = _exclCase;
    }
   
    public String getSingleFs(){
        return singleFs;
    }
    public void setSingleFs(String _singleFs){
        singleFs = _singleFs;
    }
   
    public String getTarget(){
        return target;
    }
    public void setTarget(String _target){
        target = _target;
    }
   
    public String getForceOvwr(){
        return forceOvwr;
    }
    public void setForceOvwr(String _forceOvwr){
        forceOvwr = _forceOvwr;
    }
 
    public String getOvwrOld(){
        return ovwrOld;
    }
    public void setOvwrOld(String _ovwrOld){
        ovwrOld = _ovwrOld;
    }
    
    public String getCurTime(){
        return curtime;
    }
    public void setCurTime(String _curtime){
        curtime = _curtime;
    }
   
    public String getRestoreRaw(){
        return restoreRaw;
    }
    public void setRestoreRaw(String _restoreRaw){
        restoreRaw = _restoreRaw;
    }
    
    public String getVolMaxFile(){
        return volMaxFile;
    }
    public void setVolMaxFile(String _volMaxFile){
        volMaxFile = _volMaxFile;
    }
    
    public String getZipFileList(){
        return zipFileList;
    }
    public void setZipFileList(String _zipFileList){
        zipFileList = _zipFileList;
    }
    
    public String getKillPreCmd(){ 
        return kill_prescript;
    }
    public void setKillPreCmd( String val ){
        kill_prescript = val;
    }
    
    public String getBlkIncRstMode(){
        return blk_inc_rst_mode;
    }
    public void setBlkIncRstMode( String val ){
        blk_inc_rst_mode = val;
    }
    
    public String getPhyIncJudgeTimeFlag(){
        return inc_no_judge_time;
    }
    public void setPhyIncJudgeTimeFlag( String val ){
        inc_no_judge_time = val;
    }
    public boolean isPhyIncJudgeTime(){
        return inc_no_judge_time.equals("1");
    }
    
    public String getBakWithSnapFlag(){
        return this.bak_with_snap;
    }
    public void setBakWithSnapFlag( String val ){
        bak_with_snap = val;
    }
    public boolean isBakWithSnap(){
        return bak_with_snap.equals("1");
    }
    
    public String getExcludeFromBak(){
        return this.exclude_from_bak;
    }
    public void setExcludeFromBak( String val ){
        exclude_from_bak = val;
    }
    
    public String getDeleteRedundantFile(){
        return delete_redundant_file;
    }
    public void setDeleteRedundantFile( String val ){
        delete_redundant_file = val;
    }
    public boolean isDeleteRedundantFileNumValid(){
        try{
            int num = Integer.parseInt( delete_redundant_file );
            if( num >=0 ){
                return true;
            }else{
                return false;
            }
        }catch(Exception ex){
            return false;
        }
    }
    
    public String getIgnore_all_copy_error(){
        return ignore_all_copy_error;
    }
    public void setIgnore_all_copy_error( String val ){
        ignore_all_copy_error = val;
    }
    public boolean isIgnore_all_copy_error(){
        return !ignore_all_copy_error.equals("0");
    }
    
    public String getCrt_Snap(){
        return crt_snap;
    }
    public void setCrt_Snap( String val ){
        crt_snap = val;
    }
    public boolean isCrtSnap(){
        return crt_snap.equals("1");
    }
    
    public void outputProfile(OutputStreamWriter out) throws IOException{
        out.write("[backup]\n");
        out.write("min-depth="+getMinDepth()+"\n");
        out.write("max-depth="+getMaxDepth()+"\n");
        out.write("backup-level="+getBackupLevel()+"\n");
        out.write("begin-restore-level="+getBeginRstLevel()+"\n");
        out.write("last-time="+getLastTime()+"\n");
        out.write("backup-raw="+getBackupRaw()+"\n");
        out.write("sn="+getSN()+"\n");
        out.write("src="+getSrc()+"\n");
        out.write("src-type="+getSrcType()+"\n");
        out.write("src-os="+getSrcOS()+"\n");
        out.write("seek="+getSeek()+"\n");
        out.write("bs="+getBS()+"\n");
        out.write("include="+getInclude()+"\n");
        out.write("incl-type="+getInclType()+"\n");
        out.write("incl-case="+getInclCase()+"\n");
        out.write("exclude="+getExclude()+"\n");
        out.write("excl-type="+getExclType()+"\n");
        out.write("excl-case="+getExclCase()+"\n");
        out.write("single-fs="+getSingleFs()+"\n");
        out.write("target="+getTarget()+"\n");
        out.write("force-ovwr="+getForceOvwr()+"\n");
        out.write("ovwr-old="+getOvwrOld()+"\n");
        out.write("curtime="+getCurTime()+"\n");
        out.write("restore-raw="+getRestoreRaw()+"\n");
        out.write("vol_max_file="+getVolMaxFile()+"\n");
        out.write("zip_file_list="+getZipFileList()+"\n");
        out.write("kill_prescript="+getKillPreCmd()+"\n");
        out.write("blk_inc_rst_mode="+getBlkIncRstMode()+"\n");
        out.write("inc_no_judge_time="+getPhyIncJudgeTimeFlag()+"\n");
        out.write("bak_with_snap="+getBakWithSnapFlag()+"\n");
        out.write("exclude_from_bak="+getExcludeFromBak()+"\n");
        out.write("delete_redundant_file="+getDeleteRedundantFile()+"\n");
        out.write("ignore_all_copy_error="+getIgnore_all_copy_error()+"\n");
        out.write("crt_snap="+getCrt_Snap()+"\n");
    }

    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        
        buf.append("[backup]\n");
        buf.append("min-depth="+getMinDepth()+"\n");
        buf.append("max-depth="+getMaxDepth()+"\n");
        buf.append("backup-level="+getBackupLevel()+"\n");
        buf.append("begin-restore-level="+getBeginRstLevel()+"\n");
        buf.append("last-time="+getLastTime()+"\n");
        buf.append("backup-raw="+getBackupRaw()+"\n");
        buf.append("sn="+getSN()+"\n");
        buf.append("src="+getSrc()+"\n");
        buf.append("src-type="+getSrcType()+"\n");
        buf.append("src-os="+getSrcOS()+"\n");
        buf.append("seek="+getSeek()+"\n");
        buf.append("bs="+getBS()+"\n");
        buf.append("include="+getInclude()+"\n");
        buf.append("incl-type="+getInclType()+"\n");
        buf.append("incl-case="+getInclCase()+"\n");
        buf.append("exclude="+getExclude()+"\n");
        buf.append("excl-type="+getExclType()+"\n");
        buf.append("excl-case="+getExclCase()+"\n");
        buf.append("single-fs="+getSingleFs()+"\n");
        buf.append("target="+getTarget()+"\n");
        buf.append("force-ovwr="+getForceOvwr()+"\n");
        buf.append("ovwr-old="+getOvwrOld()+"\n");
        buf.append("curtime="+getCurTime()+"\n");
        buf.append("restore-raw="+getRestoreRaw()+"\n");
        buf.append("vol_max_file="+getVolMaxFile()+"\n");
        buf.append("zip_file_list="+getZipFileList()+"\n");
        buf.append("kill_prescript="+getKillPreCmd()+"\n");
        buf.append("blk_inc_rst_mode="+getBlkIncRstMode()+"\n");
        buf.append("inc_no_judge_time="+getPhyIncJudgeTimeFlag()+"\n");
        buf.append("bak_with_snap="+getBakWithSnapFlag()+"\n");
        buf.append("exclude_from_bak="+getExcludeFromBak()+"\n");
        buf.append("delete_redundant_file="+getDeleteRedundantFile()+"\n");
        buf.append("ignore_all_copy_error="+getIgnore_all_copy_error()+"\n");
        buf.append("crt_snap="+getCrt_Snap()+"\n");
        
        return buf.toString();
    }
    
    public int parserProfile(int begin,String[] lines ) throws IllegalArgumentException{
        String line;

        do{
            line = lines[begin];
            
//System.out.println("line[backup]: "+line+" begin: "+begin);
            
            if( line.length() == 0 ){
                begin++;
                continue;
            }

            if( isTitle(line) )
                break;

            Vector v = splitLine(line);
            if( v.elementAt(0).equals("min-depth") )
                setMinDepth((String)v.elementAt(1));
            else if( v.elementAt(0).equals("max-depth") )
                setMaxDepth( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("backup-level") )
                setBackupLevel( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("begin-restore-level") )
                setBeginRstLevel( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("last-time") )
                setLastTime( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("backup-raw") )
                setBackupRaw( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("sn") )
                setSN( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("src") )
                setSrc( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("src-type") )
                setSrcType( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("src-os") )
                setSrcOS( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("seek") )
                setSeek( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("bs") )
                setBS( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("include") )
                setInclude( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("incl-type") )
                setInclType( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("incl-case") )
                setInclCase( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("exclude") )
                setExclude( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("excl-type") )
                setExclType( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("excl-case") )
                setExclCase( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("single-fs") )
                setSingleFs( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("target") )
                setTarget( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("force-ovwr") )
                setForceOvwr( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("ovwr-old") )
                setOvwrOld( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("curtime") )
                setCurTime( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("restore-raw") )
                setRestoreRaw( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("vol_max_file") )
                setVolMaxFile( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("zip_file_list") )
                setZipFileList( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("kill_prescript"))
                setKillPreCmd( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("blk_inc_rst_mode"))
                setBlkIncRstMode( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("inc_no_judge_time"))
                setPhyIncJudgeTimeFlag( (String)v.elementAt(1) ) ;
            else if( v.elementAt(0).equals("bak_with_snap") )
                setBakWithSnapFlag( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("exclude_from_bak") )
                setExcludeFromBak( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("delete_redundant_file"))
                setDeleteRedundantFile( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("ignore_all_copy_error"))
                setIgnore_all_copy_error((String)v.elementAt(1) );
            else if( v.elementAt(0).equals("crt_snap"))
                setCrt_Snap( (String)v.elementAt(1) );
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [Backup]");
        
            begin++;    
        }while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
