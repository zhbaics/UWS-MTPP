/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.ui.sdhm;

import guisanboot.data.sdhm.SDHMConfig;
import guisanboot.data.sdhm.SetSDHMConfig;
import javax.swing.*;

import guisanboot.res.*;
import guisanboot.ui.*;

/**
 *
 * @author Administrator
 */
public class SDHMConfigSetter extends BasicGetSomethingThread {

    private SDHMSetDialog diag;
    private SanBootView view;
    private Object lock = new Object();
    private boolean overed = false;
    private int getCount;
    private SDHMConfig sdhmConfig;
    private int retcode;
    private String errmsg;
    public boolean errorFlag = false;

    /** Creates a new instance of SDHMConfigSetter */
    public SDHMConfigSetter(
            SanBootView _view,
            SDHMSetDialog _diag,
            SDHMConfig _sdhmConfig) {
        view = _view;
        diag = _diag;
        sdhmConfig = _sdhmConfig;
    }
    Runnable setBtn = new Runnable() {

        public void run() {
//            diag.enableButton( true );
        }
    };
    Runnable addListener = new Runnable() {

        public void run() {
//            diag.addListSelectionListener();
        }
    };
    Runnable setConfigRun = new Runnable() {

        public void run() {
            try {
                SetSDHMConfig setSDHMConfig = new SetSDHMConfig(
                        view,
                        ResourceCenter.getCmd(ResourceCenter.CMD_SET_SDHM_CONFIG) + sdhmConfig.getHostip() + " " + SDHMConfig.MONITOR_UWS + "=" + sdhmConfig.getMonitorUWS() + " " + SDHMConfig.THR_MEMORY + "=" + sdhmConfig.getThrmemory() + " " + SDHMConfig.THR_CPU + "=" + sdhmConfig.getThrcpu() + " " + SDHMConfig.THR_NETIO + "=" + sdhmConfig.getThrnetio() + " " + SDHMConfig.THR_DISKIO + "=" + sdhmConfig.getThrdiskio() + " " + SDHMConfig.THR_DISK + "=" + sdhmConfig.getThrdisk() + " " + SDHMConfig.INTERVAL + "=" + sdhmConfig.getInterval());
                setSDHMConfig.run();
                setOver(true);
                retcode = setSDHMConfig.getRetCode();
                if (retcode != 0) {
                    errorFlag = true;
                    errMsg = setSDHMConfig.getErrMsg();
                    SanBootView.log.error(getClass().getName(), " sdhmSetError: " + errMsg);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmSetError: " + ex.getMessage());
                errorFlag = true;
                errMsg = ex.getMessage();
                setOver(true);
            }
        }
    };

    public boolean realRun() {
        Thread getInfoThread = new Thread(setConfigRun);
        getInfoThread.start();

        while (!isOver()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmSetError: " + e.getMessage());
                errorFlag = true;
                errMsg = e.getMessage();
            }
        }

        diag.setBeforeGet(false);
        try {
            SwingUtilities.invokeAndWait(setBtn);
            SwingUtilities.invokeAndWait(addListener);
        } catch (Exception ex) {
            ex.printStackTrace();
            SanBootView.log.error(getClass().getName(), " sdhmSetError: " + ex.getMessage());
            errorFlag = true;
            errMsg = ex.getMessage();
        }

        return true;
    }

    boolean isOver() {
        synchronized (lock) {
            return overed;
        }
    }

    void setOver(boolean val) {
        synchronized (lock) {
            overed = val;
        }
    }

    public int getTskLogCount() {
        return getCount;
    }

    public boolean getRetVal() {
        return !errorFlag;
    }

    public String getErrorMsg() {
        return errMsg;
    }
}
