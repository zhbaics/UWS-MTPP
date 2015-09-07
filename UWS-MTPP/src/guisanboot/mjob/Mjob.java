/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.mjob;

import java.util.ArrayList;
import javax.swing.Icon;
import mylib.UI.TableRevealable;

/**
 *
 * @author Administrator
 */
public class Mjob implements TableRevealable{

    public String beginTime = "";	// 开始时间
    public String endTime = "";	// 结束时间
    public int mjob = 0;            // 作业id
    public int rootid = 0;          // 备份源逻辑盘id
    public int destrootid = 0;      // 备份目的设备逻辑盘id
    public long size = 0;           // 文件size总计（byte）
    public long transfersize = 0;   // 实际传输size（byte）
    public String runtime = "";         // 运行时间（秒）
    private ArrayList myList = new ArrayList();

    public void setMyList(Mjob mjob) {
        myList.add(mjob);
    }

    public ArrayList getMyList() {
        return myList;
    }

    public void setBeginTime(String _beginTime) {
        beginTime = _beginTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setEndTime(String _endTime) {
        endTime = _endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setMjob(int _mjob) {
        mjob = _mjob;
    }

    public int getMjob() {
        return mjob;
    }

    public void setRootid(int _rootid) {
        rootid = _rootid;
    }

    public int getRootid() {
        return rootid;
    }

    public void setDestrootid(int _destrootid) {
        destrootid = _destrootid;
    }

    public int getDestrootid() {
        return destrootid;
    }

    public void setSize(long _size) {
        size = _size;
    }

    public long getSize() {
        return size;
    }

    public void setTransfersize(long _transfersize) {
        transfersize = _transfersize;
    }

    public long getTransfersize() {
        return transfersize;
    }

    public void setRuntime(String _runtime) {
        runtime = _runtime;
    }

    public String getRuntime() {
        return runtime;
    }
    
    static String tools(String input) {
        int input1 = Integer.parseInt(input);
        //小时
        int hh = input1 / 3600;
        //分钟
        int mm = (input1 - hh * 3600) / 60;
        //秒数
        int ss = (input1 - hh * 3600 - mm * 60);
        String time = hh + "h" + mm + "min" + ss + "s";
        return time;
    }

    public boolean enableTableEditable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean enableSelected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getAlignType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Icon getTableIcon() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String toTableString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
