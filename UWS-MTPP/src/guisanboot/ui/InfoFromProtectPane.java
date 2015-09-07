/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.ui;

import java.util.Vector;

/**
 *
 * @author Administrator
 */

public class InfoFromProtectPane{
    public boolean isCopyOS = true; // 在界面选择"复制操作系统"
    public boolean isOnlyModRegister = false; // 在界面选择"只修改注册表"
    public boolean isDonothing = false; // 在界面上选择"什么都不做"
    public boolean startNet = false; // 表明当前系统从net上启动的
    public Vector lvmTypeList = null; // lvm type list
    public Vector volList = null;
    public InfoFromProtectPane(){
    }
}
