/*
 * TaskSummary.java
 *
 * Created on Aug 11, 2008, 11:48 AM
 */

package guisanboot.datadup.data;

/**
 *
 * @author  Administrator
 */
public class TaskSummary {
    public static final String TSK_SUMMARY_TskID ="TaskerID";
    public static final String TSK_SUMMARY_AGENTID = "AgentID";
    public static final String TSK_SUMMARY_Type = "Type";
    public static final String TSK_SUMMARY_CltSta ="Client Status";
    public static final String TSK_SUMMARY_HName = "HostName";
    public static final String TSK_SUMMARY_OS = "Os";
    public static final String TSK_SUMMARY_Arch ="Arch";
    public static final String TSK_SUMMARY_BakSrc = "Backup Source";
    public static final String TSK_SUMMARY_STime = "Start Time";
    public static final String TSK_SUMMARY_ETime = "Elapsed Time";
    public static final String TSK_SUMMARY_Files = "Files";
    public static final String TSK_SUMMARY_TData = "Total Data";
    public static final String TSK_SUMMARY_SVol = "Splited Volnums";
    public static final String TSK_SUMMARY_RDays = "Retention Days";
    public static final String TSK_SUMMARY_AvSpeed="Average Speed";
    
    public String taskerID ="";
    public String agentID="";
    public String type="";
    public String cltStatus="";
    public String hostname="";
    public String os="";
    public String arch="";
    public String bakSrc="";
    public String stime="";
    public String elapsedTime="";
    public String files="";
    public String totalData="";
    public String splitedVols="";
    public String retentionDays="";
    public String averageSpeed="";
    
    /** Creates a new instance of TaskSummary */
    public TaskSummary() {
    }
    
    public TaskSummary(
        String _taskerID,
        String _agentID,
        String _type,
        String _cltStatus,
        String _hostname,
        String _os,
        String _arch,
        String _bakSrc,
        String _stime,
        String _elapsedTime,
        String _files,
        String _totalData,
        String _splitedVols,
        String _retentionDays,
        String _averageSpeed
    ){
        taskerID = _taskerID;
        agentID = _agentID;
        type= _type;
        cltStatus = _cltStatus;
        hostname = _hostname;
        os= _os;
        arch = _arch;
        bakSrc= _bakSrc;
        stime= _stime;
        elapsedTime= _elapsedTime;
        files= _files;
        totalData =_totalData;
        splitedVols = _splitedVols;
        retentionDays = _retentionDays;
        averageSpeed = _averageSpeed;
    }   
}
