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
public class GetAgentVolumeList extends NetworkRunning {

    private Vector agentVolumeList = new Vector();
    private SanBootView view;
    private static final String VOLUME_NAME = "volname";

    /** Creates a new instance of GetAgentVolumeList */
    public GetAgentVolumeList(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetAgentVolumeList(String cmd) {
        super(cmd);
    }

    public Vector AgentVolumeList() {
        return agentVolumeList;
    }

    @Override
    public void parser(String line) {
        if (agentVolumeList == null) {
            agentVolumeList = new Vector();
        }
        String s1 = line.trim();

        SanBootView.log.debug(getClass().getName(), "output:" + s1);

        int index = s1.indexOf("=");

        if (index > 0) {
            String value = s1.substring(index + 1).trim();

            if (s1.startsWith(VOLUME_NAME)) {
                agentVolumeList.add(value);
            }
        }
    }
}
