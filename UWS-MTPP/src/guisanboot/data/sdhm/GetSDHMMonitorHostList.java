/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;

import guisanboot.data.NetworkRunning;
import java.io.IOException;
import guisanboot.ui.*;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class GetSDHMMonitorHostList extends NetworkRunning {

    private Vector sdhmMonitorHostList;
    private SanBootView view;
    private static final String MONITOR_HOST_IP = "ody_monitor_host_ip";

    /** Creates a new instance of GetSDHMReportHostList */
    public GetSDHMMonitorHostList(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetSDHMMonitorHostList(String cmd) {
        super(cmd);
    }

    public Vector SDHMMonitorHostList() {
        return sdhmMonitorHostList;
    }

    @Override
    public void parser(String line) {
        if (sdhmMonitorHostList == null) {
            sdhmMonitorHostList = new Vector();
        }
        String s1 = line.trim();

        SanBootView.log.debug(getClass().getName(), "output:" + s1);

        int index = s1.indexOf("=");

        if (index > 0) {
            String value = s1.substring(index + 1).trim();

            if (s1.startsWith(MONITOR_HOST_IP)) {
                sdhmMonitorHostList.add(value);
            }
        }
    }
}
