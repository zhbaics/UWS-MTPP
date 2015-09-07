/*
 * DiskForWin.java
 *
 * Created on 2008/1/9,�AM 11:41
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
public class DiskForWin implements Comparable {
    private int   diskno  = -1;
    private long  diskSize = 0L; // 字节
    private int   pcnt = -1;
    private long  signature = 0L;

    private ArrayList<SystemPartitionForWin> partitions = new ArrayList<SystemPartitionForWin>(); // 里面放的是 SystemPartitionForWin
    private int unit; // 0: mega  1: giga

    /*
    diskno=0;disk_size=4293596160;PartitionCount=4;signature=3101837393
        StartingOffset=32256;PartitionLength=4293563904;HiddenSectors=63;Partit
onNumber=1;PartitionType=7;BootIndicator=1;RecognizedPartition=1;RewritePartiti
n=0;letter=C;label=iscsi-61-C;filesystem=NTFS;
        StartingOffset=0;PartitionLength=0;HiddenSectors=0;PartitionNumber=0;Pa
titionType=0;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
        StartingOffset=0;PartitionLength=0;HiddenSectors=0;PartitionNumber=0;Pa
titionType=0;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
        StartingOffset=0;PartitionLength=0;HiddenSectors=0;PartitionNumber=0;Pa
titionType=0;BootIndicator=0;RecognizedPartition=0;RewritePartition=0;
diskno=1;disk_size=4293596160;PartitionCount=4;signature=3279668091
    */
    
    
    /** Creates a new instance of DiskForWin */
    public DiskForWin() {
    }
    
    public DiskForWin( int diskno,long diskSize ){
        this.diskno = diskno;
        this.diskSize = diskSize;
    }
    
    @Override public String toString(){
        return SanBootView.res.getString("common.disk")+ diskno +" [ " + getSizeInBigUnit() + (unit>0? "GB":"MB") + " ]";
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
            
    public int getDiskno() {
        return diskno;
    }
    
    public void setDiskno(int diskno) {
        this.diskno = diskno;
    }

    public long getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(long diskSize) {
        this.diskSize = diskSize;
    }
    
    public void addPartiton( SystemPartitionForWin partition ){
        partitions.add( partition );
    }
    
    public boolean containOSPartition(){
        return containPartition("C");
    }
    
    public boolean containPartition( String letter ){
        int size = partitions.size();
        for( int i=0; i<size; i++ ){
            SystemPartitionForWin part = partitions.get(i);
            if( part.getDiskLabel().toUpperCase().equals( letter ) ){
                return true;
            }
        }
        
        return false;
    }
    
    public List<SystemPartitionForWin> getPartitionList(){
        int size = partitions.size();
        return partitions.subList( 0,size );
    }
    
    public int compareTo( Object _o){
        DiskForWin disk = (DiskForWin)_o;

        if( this.diskSize == disk.diskSize ){
            return 0;
        }
        
        if( this.diskSize <disk.diskSize ){
            return -1;
        }else{
            return 1;
        }
    }
     
    public String prtMe(){
        StringBuffer buf = new StringBuffer();
        buf.append("\n==========================================");
        buf.append("\ndiskno="+this.diskno);
        buf.append("\ndisksize="+this.diskSize);
        Iterator<SystemPartitionForWin> iterator = partitions.iterator();
        while( iterator.hasNext() ){
            buf.append("\npartition="+ iterator.next().prtMe() );
        }
        buf.append("\n===========================================");
        return buf.toString();
    }
}
