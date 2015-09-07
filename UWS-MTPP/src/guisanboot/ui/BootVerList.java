/*
 * BootVerList.java
 *
 * Created on 2008/7/2, 5:32 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

import guisanboot.data.BindOfDiskLabelAndTid;
import java.util.ArrayList;

/**
 *
 * @author zjj
 */
public class BootVerList {
    public ArrayList bootVerList; // 网络启动或磁盘切换的版本列表
    public BindOfDiskLabelAndTid osBootVer; // 当为磁盘切换时，osBootVer应该为空
    
    /** Creates a new instance of BootVerList */
    public BootVerList( ArrayList list, BindOfDiskLabelAndTid osVer ){
        this.bootVerList = list;
        this.osBootVer = osVer;
    } 
}
