/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;

import guisanboot.data.NetworkRunning;
import java.io.IOException;
import guisanboot.ui.SanBootView;

/**
 *
 * @author Administrator
 */
public class GetSDHMConfig extends NetworkRunning {

    private SDHMConfig sdhmConfig;
    private SanBootView view;

    /** Creates a new instance of GetSDHMConfig */
    public GetSDHMConfig(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetSDHMConfig(String cmd) {
        super(cmd);
    }

    public SDHMConfig SDHMConfig() {
        return sdhmConfig;
    }

    public void parser(String line) {
        if (sdhmConfig == null) {
            sdhmConfig = new SDHMConfig();
        }
        String s1 = line.trim();

        SanBootView.log.debug(getClass().getName(), "output:" + s1);

        int index = s1.indexOf("=");

        if (index > 0) {
            String value = s1.substring(index + 1).trim();

            if (s1.startsWith(SDHMConfig.MONITOR_UWS)) {
                sdhmConfig.setMonitorUWS(value);
            } else if (s1.startsWith(SDHMConfig.HOST_PORT)) {
                sdhmConfig.setHostport(value);
            } else if (s1.startsWith(SDHMConfig.GET_DATA_TIME)) {
                sdhmConfig.setGetdataTime(value);
            } else if (s1.startsWith(SDHMConfig.REP_SAVE_DIR)) {
                sdhmConfig.setRepSaveDir(value);
            } else if (s1.startsWith(SDHMConfig.HOST_IP)) {
                sdhmConfig.setHostip(value);
            } else if (s1.startsWith(SDHMConfig.THR_CPU)) {
                sdhmConfig.setThrcpu(value);
            } else if (s1.startsWith(SDHMConfig.THR_DISK)) {
                sdhmConfig.setThrdisk(value);
            } else if (s1.startsWith(SDHMConfig.THR_MEMORY)) {
                sdhmConfig.setThrmemory(value);
            } else if (s1.startsWith(SDHMConfig.THR_DISKIO)) {
                sdhmConfig.setThrdiskio(value);
            } else if (s1.startsWith(SDHMConfig.THR_NETIO)) {
                sdhmConfig.setThrnetio(value);
            } else if (s1.startsWith(SDHMConfig.INTERVAL)) {
                sdhmConfig.setInterval(value);
            }
        } else {
        }
    }
}
