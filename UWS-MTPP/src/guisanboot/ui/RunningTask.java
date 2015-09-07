/*
 * RunningTask.java
 *
 * Created on 2007/2/3,�PM 4:24
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.ui;

/**
 *
 * @author Administrator
 */
public interface RunningTask {
    public void setTaskStatus( String val,int row,int col );
    public void setLogOnTabpane( String log,int row );
    public void freshCopyLogOnTabpane( String prefix,String log,int row,boolean isAutoScroll );
    public String getCopyLogOnTabpane( int row );
}
