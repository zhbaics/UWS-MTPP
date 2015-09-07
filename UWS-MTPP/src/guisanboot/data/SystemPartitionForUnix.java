/*
 * SystemPartitionForUnix.java
 *
 * Created on 2007/8/7, PM�3:15
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class SystemPartitionForUnix {
    public String mp="";
    public String dev_path="";
    public String fsType="";
    public String size=""; //MB,float
    public String used=""; //MB
    public String devType=""; // ide,scsi,etc.
    public String vender=""; // odysys etc
    
    public boolean isBootable = false; // is bootable?
    public String new_mp=""; // new mount point
    public String partType="";  // Partition Type
    public String label ="";    // label;
    public String flag ="";     // 分区的flag，只适用于 HP安腾linux

    public String uuid =""; // for ams, e.g. name=4809b724-099d-4685-8185-7a6d6c75f029_3225
    
    /** Creates a new instance of SystemPartitionForUnix */
    public SystemPartitionForUnix() {
    }
    
    @Override public String toString(){
        if( mp.equals("none") ){
            return "";
        }else{
            return mp;
        }
    }
    
    public boolean isSwap(){
        return fsType.toUpperCase().equals("SWAP") || partType.toUpperCase().equals("82");
    }
    
    private boolean isPureDigital( String val ){
        try{
            Long.parseLong( val );
            return true;
        }catch(Exception ex){
            return false;
        }
    }
    
    private long getSizeInMega( String val ){
        // val的单位是字节
        long lSize = -1L;
        try{
            lSize = Long.parseLong( val );
            return lSize/(1024*1024);
        }catch(Exception ex){
            return -1L;
        }
    }
    
    private long getSizeInMega( String unit,String number ){
        try{
            long lSize = Long.parseLong( number );
            if( unit.equals("T") ){
                return lSize*1024*1024;
            }else if( unit.equals("G") ){
                return lSize*1024;
            }else {
                return lSize;
            }
        }catch(Exception ex){
            return -1L;
        }
    }
    
    private long getValInMega( String val ){
        try{
            if( isPureDigital( val ) ){
                return getSizeInMega( val );
            }else{
                int len = val.length();
                String unit = val.substring(len-1);
                String number = val.substring(0,len-1);
                return getSizeInMega( unit,number );
            }
        }catch( Exception ex ){
            return -1L;
        }
    }
    
    public long getUsedInMega(){
        return getValInMega( used );
    }
    
    public long getAvailableInMega(){
SanBootView.log.info(getClass().getName()," disk total size : "+size +" disk used size: "+used ); 
        long lSize = getValInMega( size );
        long lNSize = getValInMega( used );
        return ( lSize-lNSize );
    }
    
    public String getSizeStr(){
        if( isPureDigital( size ) ){
            return size+"B";
        }else{
            return size;
        }
    }
    
    // 比实际值多一点
    public long getSizeInGiga(){
        long lSize = getValInMega( size );
        if( lSize > 0 ){
            return ( lSize+1023 )/1024;
        }else{
            return lSize;
        }
    }
    
    public boolean isSamePartition( String aMp ){
        return mp.equals( aMp );
    }

    public boolean isEfiFlag(){
        return ( (this.flag!=null) && !this.flag.equals("") && !flag.equals("none") );
    }

    public String getDeviceName(){
        Pattern pattern = Pattern.compile( "^(/dev/\\D+)(\\d{1,3})$" );
        Matcher matcher = pattern.matcher( this.dev_path );
        if( matcher.find() ){
            int grpNum = matcher.groupCount();
            if( grpNum!=2 )
                return "";
            else{
                return matcher.group(1);
            }
        }else{
            return "";
        }
    }

     public String getPartitionNumber(){
        Pattern pattern = Pattern.compile( "^(/dev/\\D+)(\\d{1,3})$" );
        Matcher matcher = pattern.matcher( this.dev_path );
        if( matcher.find() ){
            int grpNum = matcher.groupCount();
            if( grpNum!=2 )
                return "";
            else{
                return matcher.group(2);
            }
        }else{
            return "";
        }
    }

    public String getDiskLabel(){
        if( !this.label.equals("") ){
            return label;
        }else{
            return uuid;
        }
    }

    public String prtMe(){
        return this.dev_path +" "+this.mp +" "+this.partType+" "+this.fsType + " "+this.label;
    }
}