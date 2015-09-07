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
public class GetSDHMReportHostList extends NetworkRunning {

    private Vector sdhmReportHostList;
    private SanBootView view;
    private static final String HOST_IP = "hostip";

    /** Creates a new instance of GetSDHMReportHostList */
    public GetSDHMReportHostList(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetSDHMReportHostList(String cmd) {
        super(cmd);
    }

    public Vector SDHMReportHostList() {
        return sdhmReportHostList;
    }

    @Override
    public void parser(String line) {
        if (sdhmReportHostList == null) {
            sdhmReportHostList = new Vector();
        }
        String s1 = line.trim();

        SanBootView.log.debug(getClass().getName(), "output:" + s1);

        int index = s1.indexOf("=");

        if (index > 0) {
            String value = s1.substring(index + 1).trim();

            if (s1.startsWith(HOST_IP)) {
                sdhmReportHostList.add(value);
            }
        }
    }
}
