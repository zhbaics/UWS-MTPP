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
public class GetAgentJobInfo extends NetworkRunning {

    private AgentJobInfo agentJobInfo = new AgentJobInfo();
    private SanBootView view;

    /** Creates a new instance of GetAgentJobInfo */
    public GetAgentJobInfo(SanBootView _view, String cmd) throws IOException {
        super(cmd, _view.getSocket());
        view = _view;
    }

    public GetAgentJobInfo(String cmd) {
        super(cmd);
    }

    public AgentJobInfo AgentTaskInfo() {
        return agentJobInfo;
    }

    public void parser(String line) {
        if (agentJobInfo == null) {
            agentJobInfo = new AgentJobInfo();
        }
        String s1 = line.trim();

        SanBootView.log.debug(getClass().getName(), "output:" + s1);

        int index = s1.indexOf("=");

        if (index > 0) {
            String value = s1.substring(index + 1).trim();

            if (s1.startsWith(AgentJobInfo.AGENT_TASK_SUM)) {
                BigDecimal valueTemp;
                try {
                    valueTemp = new BigDecimal(value);
                } catch (Exception ex) {
                    valueTemp = BigDecimal.ZERO;
                }
                agentJobInfo.setSumtask(valueTemp);
            } else if (s1.startsWith(AgentJobInfo.AGENT_TASK_ERR)) {
                BigDecimal valueTemp;
                try {
                    valueTemp = new BigDecimal(value);
                } catch (Exception ex) {
                    valueTemp = BigDecimal.ZERO;
                }
                agentJobInfo.setErrortask(valueTemp);
            } else{
                String[] tempValueList = s1.split("=");
                if (tempValueList.length > 4){
                    agentJobInfo.addAgentTaskStartTime(tempValueList[0].toString());
                    agentJobInfo.addAgentTaskEndTime(tempValueList[1].toString());
                    agentJobInfo.addAgentTaskType(tempValueList[2].toString());
                    agentJobInfo.addAgentTaskResult(tempValueList[3].toString());
                    agentJobInfo.addAgentTaskMessage(tempValueList[4].toString());
                }
            }
        } else {
        }
    }
}
