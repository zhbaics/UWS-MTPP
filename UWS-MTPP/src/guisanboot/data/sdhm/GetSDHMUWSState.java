/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;

import guisanboot.data.NetworkRunning;
import javax.swing.table.*;
import java.io.IOException;
import guisanboot.ui.*;
import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class GetSDHMUWSState extends NetworkRunning {

    private SDHMUWSState sdhmUWSState;
    private DefaultTableModel model;
    private SanBootView view;
    private int count;

    /** Creates a new instance of GetSDHMUWSState */
    public GetSDHMUWSState(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetSDHMUWSState(String cmd) {
        super(cmd);
    }

    public SDHMUWSState SDHMUWSState() {
        return sdhmUWSState;
    }

    public void parser(String line) {
        if (sdhmUWSState == null) {
            sdhmUWSState = new SDHMUWSState();
        }
        String s1 = line.trim();

        SanBootView.log.debug(getClass().getName(), "output:" + s1);

        int index = s1.indexOf("=");

        if (index > 0) {
            String valueTemp = s1.substring(index + 1).trim();
            BigDecimal value;
            try {
                value = new BigDecimal(valueTemp);
            } catch (Exception ex) {
                value = BigDecimal.ZERO;
            }
            if (s1.startsWith(SDHMUWSState.UWS_ALL_DISK)) {
                sdhmUWSState.setAllDisk(value);
            } else if (s1.startsWith(SDHMUWSState.UWS_FREE_DISK)) {
                sdhmUWSState.setFreeDisk(value);
            } else if (s1.startsWith(SDHMUWSState.UWS_VISUAL_DISK)) {
                sdhmUWSState.setVisualDisk(value);
            } else if (s1.startsWith(SDHMUWSState.UWS_USED_DISK)) {
                sdhmUWSState.setUsedDisk(value);
            } else if (s1.startsWith(SDHMUWSState.UWS_LOCAL_DISK)) {
                sdhmUWSState.setLocalDisk(value);
            } else if (s1.startsWith(SDHMUWSState.UWS_MIRROR_DISK)) {
                sdhmUWSState.setMirrorDisk(value);
            } else if (s1.startsWith(SDHMUWSState.UWS_IDLE_DISK)) {
                sdhmUWSState.setIdleDisk(value);
            } else if (s1.startsWith(SDHMUWSState.UWS_MEM_USED)) {
                sdhmUWSState.setMemoryUsed(value);
            } else if (s1.startsWith(SDHMUWSState.UWS_CPU_USED)) {
                sdhmUWSState.setCpuUsed(value);
            } else if (s1.startsWith(SDHMUWSState.UWS_NET_READ)) {
                sdhmUWSState.setNetReceive(value);
            } else if (s1.startsWith(SDHMUWSState.UWS_NET_WRITE)) {
                sdhmUWSState.setNetSend(value);
            } else if (s1.startsWith(SDHMUWSState.UWS_IP)) {
                sdhmUWSState.setUWSIP(valueTemp);
            } else if (s1.startsWith(SDHMUWSState.UWS_NAME)) {
                sdhmUWSState.setUWSName(valueTemp);
            }
        } else {
        }
    }
}
