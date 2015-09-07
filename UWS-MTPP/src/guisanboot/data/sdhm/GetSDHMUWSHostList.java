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
public class GetSDHMUWSHostList extends NetworkRunning {

    private Vector sdhmUWSHostList;
    private SanBootView view;
    private static final String UWS_HOST_IP = "ody_sdhm_monitor_itemval_des1";

    /** Creates a new instance of GetSDHMUWSHostList */
    public GetSDHMUWSHostList(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetSDHMUWSHostList(String cmd) {
        super(cmd);
    }

    public Vector SDHMUWSHostList() {
        return sdhmUWSHostList;
    }

    @Override
    public void parser(String line) {
        if (sdhmUWSHostList == null) {
            sdhmUWSHostList = new Vector();
        }
        String s1 = line.trim();

        SanBootView.log.debug(getClass().getName(), "output:" + s1);

        int index = s1.indexOf("=");

        if (index > 0) {
            String value = s1.substring(index + 1).trim();

            if (s1.startsWith(UWS_HOST_IP)) {
                sdhmUWSHostList.add(value);
            }
        }
    }
}
