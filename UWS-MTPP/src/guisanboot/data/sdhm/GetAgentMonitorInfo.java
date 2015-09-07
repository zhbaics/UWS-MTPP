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
 * @author Jesse
 */
public class GetAgentMonitorInfo extends NetworkRunning {

    private BigDecimal avg_monitor_info = BigDecimal.ZERO;
    private SanBootView view;
    private static final String AVG_MONITOR_INFO = "AVG(ody_sdhm_monitor_itemval_val)";

    /** Creates a new instance of GetAgentMonitorInfo */
    public GetAgentMonitorInfo(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetAgentMonitorInfo(String cmd) {
        super(cmd);
    }

    public BigDecimal AvgMonitorInfo() {
        return avg_monitor_info;
    }

    public void parser(String line) {

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

            if (s1.startsWith(AVG_MONITOR_INFO)) {
                avg_monitor_info = value;
            }
        } else {
        }
    }

}
