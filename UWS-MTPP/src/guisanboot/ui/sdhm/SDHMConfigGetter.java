/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.ui.sdhm;

import guisanboot.data.sdhm.GetSDHMConfig;
import guisanboot.data.sdhm.SDHMConfig;
import javax.swing.*;

import guisanboot.res.*;
import guisanboot.ui.*;

/**
 *
 * @author Administrator
 */
public class SDHMConfigGetter extends BasicGetSomethingThread {

    private SDHMSetDialog diag;
    private SanBootView view;
    private Object lock = new Object();
    private boolean overed = false;
    private int getCount;
    private String hostip;
    private SDHMConfig sdhmConfig;
    private int retcode;
    public boolean errorFlag = false;

    /** Creates a new instance of SDHMConfigGetter */
    public SDHMConfigGetter(
            SanBootView _view,
            SDHMSetDialog _diag,
            String _hostip) {
        view = _view;
        diag = _diag;
        hostip = _hostip;
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
    Runnable getConfigRun = new Runnable() {

        public void run() {
            try {
                GetSDHMConfig getSDHMConfig = new GetSDHMConfig(
                        view,
                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_SDHM_CONFIG) + hostip + " all");
                getSDHMConfig.run();
                sdhmConfig = getSDHMConfig.SDHMConfig();
                diag.setConfig(sdhmConfig);
                setOver(true);
                retcode = getSDHMConfig.getRetCode();
                if (retcode != 0) {
                    errorFlag = true;
                    errMsg = getSDHMConfig.getErrMsg();
                    SanBootView.log.error(getClass().getName(), " sdhmGetError: " + errMsg);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmGetError: " + ex.getMessage());
                errorFlag = true;
                errMsg = ex.getMessage();
                setOver(true);
            }
        }
    };

    public boolean realRun() {
        Thread getInfoThread = new Thread(getConfigRun);
        getInfoThread.start();

        while (!isOver()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmGetError: " + e.getMessage());
                errorFlag = true;
                errMsg = e.getMessage();
            }
        }

//        diag.setBeforeGet( false );
        try {
            SwingUtilities.invokeAndWait(setBtn);
            SwingUtilities.invokeAndWait(addListener);
        } catch (Exception ex) {
            ex.printStackTrace();
            SanBootView.log.error(getClass().getName(), " sdhmGetError: " + ex.getMessage());
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
