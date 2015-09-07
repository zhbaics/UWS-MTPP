/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.data.sdhm;

import java.math.BigDecimal;
import java.util.Vector;
/**
 *
 * @author Jesse
 */
public class AgentJobInfo {

    public static final String AGENT_TASK_SUM = "sumtask";
    public static final String AGENT_TASK_ERR = "errortask";

    BigDecimal sumtask = BigDecimal.ZERO;
    BigDecimal errortask = BigDecimal.ZERO;
    private Vector agentTaskStartTime = new Vector();
    private Vector agentTaskEndTime = new Vector();
    private Vector agentTaskType = new Vector();
    private Vector agentTaskResult = new Vector();
    private Vector agentTaskMessage = new Vector();
    
    /** Creates a new instance of AgentJobInfo */
    public AgentJobInfo() {
    }

    public BigDecimal getSumtask() {
        return sumtask;
    }

    public void setSumtask(BigDecimal _sumtask) {
        sumtask = _sumtask;
    }

    public BigDecimal getErrortask() {
        return errortask;
    }

    public void setErrortask(BigDecimal _errortask) {
        errortask = _errortask;
    }

    public Vector getAgentTaskStartTime() {
        return agentTaskStartTime;
    }

    public void addAgentTaskStartTime(String _agentTaskStartTime) {
        agentTaskStartTime.add(_agentTaskStartTime);
    }

    public Vector getAgentTaskEndTime() {
        return agentTaskEndTime;
    }

    public void addAgentTaskEndTime(String _agentTaskEndTime) {
        agentTaskEndTime.add(_agentTaskEndTime);
    }

    public Vector getAgentTaskType() {
        return agentTaskType;
    }

    public void addAgentTaskType(String _agentTaskType) {
        agentTaskType.add(_agentTaskType);
    }

    public Vector getAgentTaskResult() {
        return agentTaskResult;
    }

    public void addAgentTaskResult(String _agentTaskResult) {
        agentTaskResult.add(_agentTaskResult);
    }

    public Vector getAgentTaskMessage() {
        return agentTaskMessage;
    }

    public void addAgentTaskMessage(String _agentTaskMessage) {
        agentTaskMessage.add(_agentTaskMessage);
    }

}
