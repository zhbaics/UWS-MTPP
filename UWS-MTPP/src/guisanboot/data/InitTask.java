/*
 * InitTask.java
 *
 * Created on 2006/12/30, AM�11:52
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import java.util.*;
import guisanboot.ui.*;

/**
 *
 * @author Administrator
 */
public class InitTask {
    public final static int INIT_TASK_STA_READY   = 0;
    public final static int INIT_TASK_STA_RUNNING = 1;
    public final static int INIT_TASK_STA_END     = 2;
    public final static int INIT_TASK_STA_FAIL    = 3;
    public final static int INIT_TASK_STA_WARN    = 4;
    
    public int seq;
    public String task;
    public String cmd;
    public Vector parm = null ;
    public int ptype;   // mtpp:1  cmdp:2
    public String preSnapCmd="";
    public String postSnapCmd="";
    public int status = 0; // 0:ready   1:running    2:end    3:failed
    
    /** Creates a new instance of InitTask */
    public InitTask() {
        task = "";
        cmd ="";
        status = 0;
    }
    
    @Override public String toString(){
        return task;
    }
    
    public static String getTaskStatusStr( int status ){
        switch( status ){
            case INIT_TASK_STA_READY:
                return SanBootView.res.getString("common.taskSta.ready");
            case INIT_TASK_STA_RUNNING:
                return SanBootView.res.getString("common.taskSta.running");
            case INIT_TASK_STA_END:
                return SanBootView.res.getString("common.taskSta.end");
            case INIT_TASK_STA_FAIL:
                return SanBootView.res.getString("common.taskSta.failed");
            case INIT_TASK_STA_WARN:
                return SanBootView.res.getString("common.taskSta.warning");
            default:
                return SanBootView.res.getString("common.taskSta.unknown");
        }
    }
}
