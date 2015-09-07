/*
 * AuditLogGeter.java
 *
 * Created on Aug 11, 2008, 11:58 AM
 */
package guisanboot.mjob.ui;

import guisanboot.mjob.ui.*;
import guisanboot.audit.cmd.*;
import guisanboot.mjob.Mjob;
import guisanboot.mjob.cmd.GetMjobLog;
import javax.swing.*;
import guisanboot.res.*;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import mylib.UI.BrowserTable;

/**
 *
 * @author  Administrator
 */
public class MjobLogGeter extends Thread {

    private MjobLogDialog diag;
    private SanBootView view;
    private int begin;
    private int num;
    private Object lock = new Object();
    private boolean overed = false;
    private int getCount;
    BrowserTable table = new BrowserTable();

    /** Creates a new instance of AuditLogGeter */
    public MjobLogGeter(
            SanBootView _view,
            MjobLogDialog _diag,
            int _begin,
            int _num) {
        view = _view;
        diag = _diag;
        begin = _begin;
        num = _num;
    }
    Runnable setBtn = new Runnable() {

        public void run() {
            diag.enableButton(true);
        }
    };
    Runnable addListener = new Runnable() {

        public void run() {
            diag.addListSelectionListener();
        }
    };
    Runnable getInfoRun = new Runnable() {

        public void run() {
            try {
                GetMjobLog getMjobLog = new GetMjobLog(
                        view,
                        ResourceCenter.getCmd(ResourceCenter.CMD_GET_MJOB));
                getMjobLog.setTableModel(diag.getTableModel());
                
                getMjobLog.updateMjobLog(begin, num);

                getCount = getMjobLog.getCount();

                setOver(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    @Override
    public void run() {
        Thread getInfoThread = new Thread(getInfoRun);
        getInfoThread.start();

        while (!isOver()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        diag.setBeforeGet(false);
        try {
            SwingUtilities.invokeAndWait(setBtn);
            SwingUtilities.invokeAndWait(addListener);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

}
