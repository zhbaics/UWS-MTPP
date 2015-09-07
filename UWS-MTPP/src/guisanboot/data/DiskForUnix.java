/*
 * DiskForUnix.java
 *
 * Created on 2008/1/9, AM�11:41
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mylib.tool.Check;

/**
 *
 * @author Administrator
 */
public class DiskForUnix implements Comparable {
    private String disk = "";
    private long diskSize = 0L; // 字节
    private String toString="";
    private ArrayList<SystemPartitionForUnix> partitions = new ArrayList<SystemPartitionForUnix>(); // 里面放的是 SystemPartitionForUnix
    private int unit; // 0: mega  1: giga
    
    /*
[sda]  TotalSize(B)=400088457216
/dev/sda1      y         386644356      none           Linux          unknown        none
/dev/sda2      n         3984120        none           Linux          unknown        none
    */
    
    /** Creates a new instance of DiskForUnix */
    public DiskForUnix() {
    }
    
    public DiskForUnix( String disk,long diskSize ){
        this.disk = disk;
        this.diskSize = diskSize;
        toString = SanBootView.res.getString("common.disk")+ disk +" [ " + getSizeInBigUnit() + (unit>0? "GB":"MB") + " ]";
    }
    
    @Override public String toString(){
        return toString;
    }
    
    public float getSizeInMega(){
        float val = (float)( diskSize/(1024.0*1024.0) );
        return Check.getSimpleFloat( val );
    }
            
    public float getSizeInGiga(){
        float val = (float)( diskSize/(1024.0*1024.0*1024.0) );
        return Check.getSimpleFloat( val );
    }
    
    public float getSizeInBigUnit(){
        float size = getSizeInGiga();
        if( size >= 1.0 ){
            unit = 1;
            return size;
        }{
            unit = 0;
            return getSizeInMega();
        }
    }
            
    public String getDiskDevName() {
        return disk;
    }
    
    public void setDiskDevName( String val ) {
        this.disk = val;
    }

    public long getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(long diskSize) {
        this.diskSize = diskSize;
    }
    
    public void addPartiton( SystemPartitionForUnix partition ){
        partitions.add( partition );
    }
    
    public boolean containOSPartition(){
        return containPartition("/");
    }
    
    public boolean containPartition( String mp ){
        int size = partitions.size();
        for( int i=0; i<size; i++ ){
            SystemPartitionForUnix part = partitions.get(i);
            if( part.mp.toUpperCase().equals( mp.toUpperCase() ) ){
                return true;
            }
        }
        
        return false;
    }
    
    public List getPartitionList(){
        int size = partitions.size();
        return partitions.subList( 0,size );
    }
    
    public int compareTo( Object _o){
        DiskForUnix aDisk = (DiskForUnix)_o;
        
        if( this.diskSize == aDisk.diskSize ){
            return 0;
        }
        
        if( this.diskSize <aDisk.diskSize ){
            return -1;
        }else{
            return 1;
        }
    }
     
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append("\ndisk dev name="+this.disk);
        buf.append("\ndisksize="+this.diskSize);
        Iterator<SystemPartitionForUnix> iterator = partitions.iterator();
        while( iterator.hasNext() ){
            buf.append("\npartition="+ iterator.next().prtMe() );
        }
        return buf.toString();
    }
}
