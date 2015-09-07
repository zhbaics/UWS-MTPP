/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.ui.sdhm;

import guisanboot.data.sdhm.GetSDHMUWSState;
import guisanboot.data.sdhm.SDHMUWSState;
import javax.swing.*;

import guisanboot.res.*;
import guisanboot.ui.*;

/**
 *
 * @author Administrator
 */
public class SDHMUWSStateGetter extends BasicGetSomethingThread {

    private SDHMMirrorReportDialog dialog;
    private SanBootView view;
    private Object lock = new Object();
    private boolean overed = false;
    private int getCount;
    private String hostip;
    private SDHMUWSState sdhmUWSState;
    private int retcode;
    public boolean errorFlag = false;

    /** Creates a new instance of SDHMUWSStateGetter */
    public SDHMUWSStateGetter(
            SanBootView _view,
            SDHMMirrorReportDialog _diag) {
        view = _view;
        dialog = _diag;
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
                GetSDHMUWSState getSDHMUWSState = new GetSDHMUWSState(
                        view,
                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_STATE));
                getSDHMUWSState.run();
                sdhmUWSState = getSDHMUWSState.SDHMUWSState();
                retcode = getSDHMUWSState.getRetCode();
                if (retcode != 0) {
                    errorFlag = true;
                    errMsg = getSDHMUWSState.getErrMsg();
                    SanBootView.log.error(getClass().getName(), " sdhmUWSStateError: " + errMsg);
                    setOver(true);
                    return;
                }
                dialog.setUWSState(sdhmUWSState);
                setOver(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmUWSStateError: " + ex.getMessage());
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
                SanBootView.log.error(getClass().getName(), " sdhmUWSStateError: " + e.getMessage());
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
            SanBootView.log.error(getClass().getName(), " sdhmUWSStateError: " + ex.getMessage());
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
