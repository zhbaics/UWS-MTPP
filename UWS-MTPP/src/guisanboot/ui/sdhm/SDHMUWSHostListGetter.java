/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.ui.sdhm;

import guisanboot.data.sdhm.GetSDHMUWSHostList;
import javax.swing.*;

import guisanboot.res.*;
import guisanboot.ui.*;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class SDHMUWSHostListGetter extends BasicGetSomethingThread {

    private SDHMMirrorReportDialog dialog;
    private SanBootView view;
    private Object lock = new Object();
    private boolean overed = false;
    private int getCount;
    private Vector sdhmUWSHostList;
    public boolean errorFlag = false;

    /** Creates a new instance of SDHMUWSHostListGetter */
    public SDHMUWSHostListGetter(
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
    Runnable getHostListRun = new Runnable() {

        public void run() {
            try {
                String cmd = "";
                cmd = ResourceCenter.getCmd(ResourceCenter.CMD_GET_UWS_HOST_LIST);
                GetSDHMUWSHostList getSDHMUWSHostList = new GetSDHMUWSHostList(
                        view,
                        cmd);
                getSDHMUWSHostList.run();
                sdhmUWSHostList = getSDHMUWSHostList.SDHMUWSHostList();
                if ((sdhmUWSHostList == null) || sdhmUWSHostList.isEmpty()) {
                    errorFlag = true;
                    errMsg = "Mirror UWS is none";
                    setOver(true);
                    return;
                }
//                dialog.setMirrorUWSHostList(sdhmUWSHostList);
                setOver(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmUWSHostListError: " + ex.getMessage());
                errorFlag = true;
                errMsg = ex.getMessage();
                setOver(true);
            }
        }
    };

    public boolean realRun() {
        Thread getInfoThread = new Thread(getHostListRun);
        getInfoThread.start();

        while (!isOver()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmUWSHostListError: " + e.getMessage());
                errorFlag = true;
                errMsg = e.getMessage();
            }
        }
        dialog.setBeforeGet(false);
        try {
            SwingUtilities.invokeAndWait(setBtn);
            SwingUtilities.invokeAndWait(addListener);
        } catch (Exception ex) {
            ex.printStackTrace();
            SanBootView.log.error(getClass().getName(), " sdhmUWSHostListError: " + ex.getMessage());
            errorFlag = true;
            errMsg = ex.getMessage();
        }

        return true;
    }

    public boolean isOver() {
        synchronized (lock) {
            return overed;
        }
    }

    public void setOver(boolean val) {
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
