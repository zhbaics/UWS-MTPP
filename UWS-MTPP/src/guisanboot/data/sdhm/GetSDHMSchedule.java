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
public class GetSDHMSchedule extends NetworkRunning {

    private Vector sdhmUWSDiskTimeList = new Vector();
    private Vector sdhmUWSDiskInfoList = new Vector();
    private SanBootView view;

    /** Creates a new instance of GetSDHMSchedule */
    public GetSDHMSchedule(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetSDHMSchedule(String cmd) {
        super(cmd);
    }

    public Vector SDHMUWSDiskInfoList() {
        return sdhmUWSDiskInfoList;
    }

    public Vector SDHMUWSDiskTimeList() {
        return sdhmUWSDiskTimeList;
    }

    @Override
    public void parser(String line) {
        if (sdhmUWSDiskTimeList == null) {
            sdhmUWSDiskTimeList = new Vector();
        }
        if (sdhmUWSDiskInfoList == null) {
            sdhmUWSDiskInfoList = new Vector();
        }
        String s1 = line.trim();

        SanBootView.log.debug(getClass().getName(), "output:" + s1);

        int index = s1.indexOf(":");

        if (index > 0) {
            String timeValue = s1.substring(0, index - 1).trim();
            String diskValue = s1.substring(index + 1).trim();
//            String value = s1.substring( index+1 ).trim();
            sdhmUWSDiskTimeList.add(timeValue);
            sdhmUWSDiskInfoList.add(diskValue);
        }
    }
}
