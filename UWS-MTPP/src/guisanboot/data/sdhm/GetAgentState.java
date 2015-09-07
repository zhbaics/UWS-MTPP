/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.data.sdhm;

import guisanboot.data.NetworkRunning;
import java.io.IOException;
import guisanboot.ui.*;
import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class GetAgentState extends NetworkRunning {

    private SDHMAgentState sdhmAgentState = new SDHMAgentState();
    private SanBootView view;

    /** Creates a new instance of GetAgentState */
    public GetAgentState(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetAgentState(String cmd) {
        super(cmd);
    }

    public SDHMAgentState SDHMAgentState() {
        return sdhmAgentState;
    }

    public void parser(String line) {
        if (sdhmAgentState == null) {
            sdhmAgentState = new SDHMAgentState();
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
            if (s1.startsWith(SDHMAgentState.AGENT_HOST_NAME)) {
                sdhmAgentState.setHostname(valueTemp);
            } else if (s1.startsWith(SDHMAgentState.AGENT_HOST_MACHINE)) {
                sdhmAgentState.setHostmachine(valueTemp);
            } else if (s1.startsWith(SDHMAgentState.AGENT_HOST_OS)) {
                sdhmAgentState.setHostos(valueTemp);
            } else if (s1.startsWith(SDHMAgentState.AGENT_HOST_MAC)) {
                sdhmAgentState.setHostmac(valueTemp);
            } else if (s1.startsWith(SDHMAgentState.AGENT_MEMORY_USED)) {
                sdhmAgentState.setMemoryused(value);
            } else if (s1.startsWith(SDHMAgentState.AGENT_DISK_READ_IO)) {
                sdhmAgentState.setDiskreadio(value);
            } else if (s1.startsWith(SDHMAgentState.AGENT_DISK_WRITE_IO)) {
                sdhmAgentState.setDiskwriteio(value);
            } else if (s1.startsWith(SDHMAgentState.AGENT_NET_READ_IO)) {
                sdhmAgentState.setNetreadio(value);
            } else if (s1.startsWith(SDHMAgentState.AGENT_NET_WRITE_IO)) {
                sdhmAgentState.setNetwriteio(value);
            } else if (s1.startsWith(SDHMAgentState.AGENT_TOTAL_DISK)) {
                sdhmAgentState.setTotaldisk(value);
            } else if (s1.startsWith(SDHMAgentState.AGENT_FREE_DISK)) {
                sdhmAgentState.setFreedisk(value);
            } else if (s1.startsWith(SDHMAgentState.AGENT_FREE_DISK)) {
                sdhmAgentState.setFreedisk(value);
            } else if (s1.startsWith(SDHMAgentState.AGENT_MAX_SNAP)) {
                sdhmAgentState.setMaxsnap(value);
            } else if (s1.startsWith(SDHMAgentState.AGENT_USED_SNAP)) {
                sdhmAgentState.setUsedsnap(value);
            }
        } else {
        }
    }
}
