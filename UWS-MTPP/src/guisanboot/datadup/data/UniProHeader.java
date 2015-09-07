package guisanboot.datadup.data;

import java.io.*;
import java.util.*;

public class UniProHeader extends AbstractUniProfile {
    protected String profileName;
    protected String act;
    protected String drive1;
    protected String drive2;
    protected String parallel;
    protected String size;
    protected String split;
    protected String zip_level;
    protected String preCmd;
    protected String postCmd;
    protected String snap_bf_script;
    protected String snap_af_script;
    protected String postArgument;
    protected String verifyLevel;
    protected String enableCrypt;
    protected String verbose;
    protected String shvol;
    protected String bkobj_desc;
    private String mirror_flag;
    private String source_dest_reference;
    private String linux_lvm_snap; // 是否做lvm的快照。一般情况下不做，只在数据库情况下做。取值为1/0
    
    // linux_lvm_reference的格式：
    // serverip-?20.20.1.144-?tgtid-?32799-?lvmtype-?NONE-?vgname-?vg_11_32799-?lvname-?vol101.boot-?fstype-?ext3;
    private String linux_lvm_reference;
    
    public UniProHeader(){
        this("","backup-file","","","1","4096","YES","0","","","","","","","NO","YES","YES","","1","", "0","" );
    }

    public UniProHeader(
        String _profileName,
        String _act,
        String _drive1,
        String _drive2,
        String _parallel,
        String _size,
        String _split,
        String _zip_level,
        String _preCmd,
        String _postCmd,
        String snapPreCmd,
        String snapPostCmd,
        String _postArgument,
        String _verifyLevel,
        String _enableCrypt,
        String _verbose,
        String _shvol,
        String _bkobj_desc,
        String _mirror_flag,
        String _source_dest_reference,
        String _linux_lvm_snap,
        String _linux_lvm_reference
    )
    {
        profileName  = _profileName;
        act          = _act;
        drive1       = _drive1;
        drive2       = _drive2;
        parallel     = _parallel;
        size         = _size;
        split        = _split;
        zip_level    = _zip_level;
        preCmd       = _preCmd;
        postCmd      = _postCmd;
        snap_bf_script = snapPreCmd;
        snap_af_script = snapPostCmd;
        postArgument= _postArgument;
        verifyLevel  = _verifyLevel;
        enableCrypt = _enableCrypt;
        verbose      =_verbose;
        shvol        = _shvol;
        bkobj_desc  = _bkobj_desc;
        mirror_flag = _mirror_flag;
        source_dest_reference  =_source_dest_reference;
        linux_lvm_snap = _linux_lvm_snap;
        linux_lvm_reference = _linux_lvm_reference;
    }
    
    public String getProfileName() {
        return profileName;
    }
    public void setProfileName(String _profileName){
        profileName = _profileName;
    }

    public String getAct(){
        return act;
    }
    public void setAct(String _act){
        act = _act;
    }
  
    public String getDrive1(){
        return drive1;
    }
    public void setDrive1(String _drive1){
        drive1 = _drive1;
    }
 
    public String getDrive2(){
        return drive2;
    }
    public void setDrive2(String _drive2){
        drive2 = _drive2;
    }
  
    public String getParallel(){
        return parallel;
    }
    public void setParallel(String _parallel){
        parallel = _parallel;
    }
  
    public String getSize(){
        return size;
    }
    public void setSize(String _size){
        size = _size;
    }
  
    public String getSplit(){
        return split;
    }
    public void setSplit(String _split){
        split = _split;
    }
    public boolean isSplit(){
        return split.equals("YES");
    }
  
    public String getZipLevel(){
        return zip_level;
    }
    public void setZipLevel(String _zip_level){
        zip_level =_zip_level;
    }
 
    public String getPreCmd(){
        return preCmd;
    }
    public void setPreCmd(String _preCmd){
        preCmd = _preCmd;
    }
  
    public String getPostCmd(){
        return postCmd;
    }
    public void setPostCmd(String _postCmd){
        postCmd = _postCmd;
    }
 
    public String getSnapPreCmd(){
        return snap_bf_script;
    }
    public void setSnapPreCmd( String val ){
        snap_bf_script = val;
    }
    
    public String getSnapPostCmd(){
        return snap_af_script;
    }
    public void setSnapPostCmd( String val ){
        snap_af_script = val;
    }
    
    public String getPostArgument(){
        return postArgument;
    }
    public void setPostArgument(String _postArgument ){
        postArgument = _postArgument;
    }
    
    public String getVerifyLevel(){
        return verifyLevel;
    }
    public void setVerifyLevel(String _vlevel){
        verifyLevel = _vlevel;
    }
  
    public String getEnableCrypt(){
        return enableCrypt;
    }
    public void setEnableCrypt(String _enableCrypt){
        enableCrypt = _enableCrypt;
    }
    
    public String getVerbose(){
        return verbose;
    }
    public void setVerbose(String _verbose){
        verbose = _verbose;
    }
    public boolean isVerbose(){
        return verbose.equals("YES");
    }
  
    public String getShvol(){
        return shvol;
    }
    public void setShvol(String _shvol){
        shvol = _shvol;
    }
    public boolean isShvol(){
        return shvol.equals("YES");
    }
    
    public String getBkObjDesc(){
        return bkobj_desc;
    }
    public void setBkObjDesc(String _desc){
        bkobj_desc = _desc;
    }
    
    public String getMirror_flag() {
        return mirror_flag;
    }

    public void setMirror_flag(String mirror_flag) {
        this.mirror_flag = mirror_flag;
    }

    public String getSource_dest_reference() {
        return source_dest_reference;
    }
    
    public void setSource_dest_reference(String source_dest_reference) {
        this.source_dest_reference = source_dest_reference;
    }
    
    public String getLinux_lvm_snap() {
        return linux_lvm_snap;
    }

    public void setLinux_lvm_snap(String linux_lvm_snap) {
        this.linux_lvm_snap = linux_lvm_snap;
    }
    
    public boolean isLinuxLVMSnap(){
        return ( linux_lvm_snap.equals("1") );
    }
    
    public String getLinux_lvm_reference() {
        return linux_lvm_reference;
    }

    public void setLinux_lvm_reference(String linux_lvm_reference) {
        this.linux_lvm_reference = linux_lvm_reference;
    }
    
    public void outputProfile(OutputStreamWriter out) throws IOException{
        out.write("[header]\n");
        out.write("profile-name="+getProfileName()+"\n");
        out.write("act="+getAct()+"\n");
        out.write("drive1="+getDrive1()+"\n");
        out.write("drive2="+getDrive2()+"\n");
        out.write("parallel="+getParallel()+"\n");
        out.write("size="+getSize()+"\n");
        out.write("split="+getSplit()+"\n");
        out.write("zip-level="+getZipLevel()+"\n");
        out.write("pre-script="+getPreCmd()+"\n");
        out.write("post-script="+getPostCmd()+"\n");
        out.write("post_argument="+getPostArgument()+"\n");
        out.write("snap_bf_script="+getSnapPreCmd()+"\n");
        out.write("snap_af_script="+getSnapPostCmd()+"\n");
        out.write("verify-level="+getVerifyLevel()+"\n");
        out.write("enable-crypt="+getEnableCrypt()+"\n");
        out.write("verbose="+getVerbose()+"\n");
        out.write("shvol="+getShvol()+"\n");
        out.write("bkobj_desc="+getBkObjDesc()+"\n");
        out.write("mirror_flag="+getMirror_flag()+"\n");
        out.write("source_dest_reference="+getSource_dest_reference()+"\n");
        out.write("linux_lvm_snap="+getLinux_lvm_snap()+"\n");
        out.write("linux_lvm_reference="+getLinux_lvm_reference() +"\n" );
    }
    
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        
        buf.append("[header]\n");
        buf.append("profile-name="+getProfileName()+"\n");
        buf.append("act="+getAct()+"\n");
        buf.append("drive1="+getDrive1()+"\n");
        buf.append("drive2="+getDrive2()+"\n");
        buf.append("parallel="+getParallel()+"\n");
        buf.append("size="+getSize()+"\n");
        buf.append("split="+getSplit()+"\n");
        buf.append("zip-level="+getZipLevel()+"\n");
        buf.append("pre-script="+getPreCmd()+"\n");
        buf.append("post-script="+getPostCmd()+"\n");
        buf.append("snap_bf_script="+getSnapPreCmd()+"\n");
        buf.append("snap_af_script="+getSnapPostCmd()+"\n");
        buf.append("post_argument="+getPostArgument()+"\n");
        buf.append("verify-level="+getVerifyLevel()+"\n");
        buf.append("enable-crypt="+getEnableCrypt()+"\n");
        buf.append("verbose="+getVerbose()+"\n");
        buf.append("shvol="+getShvol()+"\n");
        buf.append("bkobj_desc="+getBkObjDesc()+"\n");
        buf.append("mirror_flag="+getMirror_flag()+"\n");
        buf.append("source_dest_reference="+getSource_dest_reference()+"\n");
        buf.append("linux_lvm_snap="+getLinux_lvm_snap()+"\n");
        buf.append("linux_lvm_reference="+getLinux_lvm_reference() +"\n" );
        
	return buf.toString();
    }
    
    public int parserProfile( int begin,String[] lines ) throws IllegalArgumentException {
        String line;

        do {
            line = lines[begin];
//System.out.println("line[header]: "+line+" begin: "+begin+
//    "line bytes:  "+line.getBytes().length
//);

            if( line.length() <= 0 ){
                begin++;
                continue;
            }

            if( isTitle(line) )
                break;

//System.out.println("to be parsed: "+line);
            Vector v = splitLine(line);

            if( v.elementAt(0).equals("profile-name") )
                setProfileName((String)v.elementAt(1).toString());
            else if( v.elementAt(0).equals("act") )
                setAct( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("drive1") )
                setDrive1( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("drive2") )
                setDrive2( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("parallel") )
                setParallel( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("size") )
                setSize( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("split") )
                setSplit( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("zip-level") )
                setZipLevel( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("pre-script") )
                setPreCmd( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("post-script") )
                setPostCmd( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("snap_bf_script") )
                setSnapPreCmd( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("snap_af_script") )
                setSnapPostCmd( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("post_argument"))
                setPostArgument( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("verify-level") )
                setVerifyLevel( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("enable-crypt") )
                setEnableCrypt( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("verbose") )
                setVerbose( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("shvol") )
                setShvol( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("bkobj_desc") )
                setBkObjDesc( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("mirror_flag"))
                setMirror_flag( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("source_dest_reference"))
                setSource_dest_reference( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("linux_lvm_snap") )
                setLinux_lvm_snap( (String)v.elementAt(1) );
            else if( v.elementAt(0).equals("linux_lvm_reference") )
                setLinux_lvm_reference( (String)v.elementAt(1) );
            else
                throw new IllegalArgumentException("Invalid Configure Line -> "+line+" at [header]");
                
            begin++;
            
        } while( begin<lines.length );

        setLastLine(line);
        
        return begin;
    }
}
