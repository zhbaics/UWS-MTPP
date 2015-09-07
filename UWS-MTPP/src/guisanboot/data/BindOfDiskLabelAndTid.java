/*
 * BindOfDiskLabelAndTid.java
 *
 * Created on 2008/3/24,�AM 10:31
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
public class BindOfDiskLabelAndTid {
    private String diskLabel;
    private int tid;     // 最后一次网启所用的版本
    private int fSelTid; // 最后一次所选的网络启动版本(linux不用这个域)
    
    public BindOfDiskLabelAndTid( String diskLabel,int tid,int aSelTid ){
        this.diskLabel = diskLabel;
        this.tid = tid;
        this.fSelTid = aSelTid;
    }
    
    public int getTid(){
        return tid;
    }
    
    public int getSelectedBootVer(){
        return fSelTid;
    }
    
    public String getDiskLabel(){
        return diskLabel;
    }
    
    public String prtMe(){
        return "BindOfDiskLabelAndTid: " + diskLabel + " booted version:" + tid +" selected version:"+fSelTid;
    }
}
