/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.ui.sdhm;

import guisanboot.data.sdhm.GetSDHMReportHostList;
import javax.swing.*;

import guisanboot.res.*;
import guisanboot.ui.*;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class SDHMReportHostListGetter extends BasicGetSomethingThread {

    private SDHMReportDialog diagReport;
    private SDHMSetDialog diagSet;
    private SanBootView view;
    private Object lock = new Object();
    private boolean overed = false;
    private int getCount;
    private Vector sdhmReportHostList;
    private int diagType; //0 is diagReport; 1 is diagSet
    public boolean errorFlag = false;

    /** Creates a new instance of SDHMReportHostListGetter */
    public SDHMReportHostListGetter(
            SanBootView _view,
            SDHMReportDialog _diag) {
        view = _view;
        diagReport = _diag;
        diagType = 0;
    }

    public SDHMReportHostListGetter(
            SanBootView _view,
            SDHMSetDialog _diag) {
        view = _view;
        diagSet = _diag;
        diagType = 1;
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
                if (diagType == 0) {
                    cmd = ResourceCenter.getCmd(ResourceCenter.CMD_GET_REPORT_HOST_LIST) + "monitor";
                } else {
                    cmd = ResourceCenter.getCmd(ResourceCenter.CMD_GET_REPORT_HOST_LIST) + "all";
                }
                GetSDHMReportHostList getSDHMReportHostList = new GetSDHMReportHostList(
                        view,
                        cmd);
                getSDHMReportHostList.run();
                sdhmReportHostList = getSDHMReportHostList.SDHMReportHostList();
                if (diagType == 0) {
                    diagReport.setHostList(sdhmReportHostList);
                } else {
                    diagSet.setHostList(sdhmReportHostList);
                }
                setOver(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                SanBootView.log.error(getClass().getName(), " sdhmGetHostListError: " + ex.getMessage());
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
                SanBootView.log.error(getClass().getName(), " sdhmGetHostListError: " + e.getMessage());
                errorFlag = true;
                errMsg = e.getMessage();
            }
        }
        if (diagType == 0) {
            diagReport.setBeforeGet(false);
        } else {
            diagSet.setBeforeGet(false);
        }
        try {
            SwingUtilities.invokeAndWait(setBtn);
            SwingUtilities.invokeAndWait(addListener);
        } catch (Exception ex) {
            ex.printStackTrace();
            SanBootView.log.error(getClass().getName(), " sdhmGetHostListError: " + ex.getMessage());
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
