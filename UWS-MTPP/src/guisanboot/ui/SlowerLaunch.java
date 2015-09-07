/*
 * SlowerLaunch.java
 *
 * Created on April 12, 2005, 11:37 AM
 */

package guisanboot.ui;

/**
 * 这个接口定义了所有启动比较慢的实体要实现的方法
 * @author  Administrator
 */
public interface SlowerLaunch {
    public boolean init();
    public String getLoadingStatus();
    public int getLoadingProcessVal();
    public String getInitErrMsg();
    public boolean isCrtVG();
    public String getSrvIP();
}
