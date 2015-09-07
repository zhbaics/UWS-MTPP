/*
 * UWSReport.java
 *
 * Created on 2007/5/25, AM 10:50
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

/**
 *
 * @author Administrator
 */
public class UWSReport {
    public final static String RPT_RECFLAG = "Record:";
    public final static String RPT_ID      = "task_id";
    public final static String RPT_STIME   = "start_time";
    public final static String RPT_CLNT_NAME = "clnt_name";
    public final static String RPT_TTYPE     = "task_type";
    public final static String RPT_TRESULT   = "task_result";
    public final static String RPT_MSG       = "task_msg";
    public final static String RPT_CLNT_ID   = "task_clnt_id";
    public final static String RPT_CLNT_TYPE = "task_clnt_type";
    public final static String RPT_CLNT_PARENT_ID = "task_parent_clnt_id";
    public final static String RPT_CLNT_PARENT_TYPE = "task_parent_clnt_type";
    public final static String RPT_CLNT_MAC = "task_clnt_mac";
    public final static String RPT_CLNT_DHCP_IP = "task_clnt_dhcp_ip";

    public final static int TASK_TYPE_INIT    = 1;
    public final static int TASK_TYPE_L2I     = 2;
    public final static int TASK_TYPE_I2L     = 3;
    public final static int TASK_TYPE_RECOVER_LOCALDISK = 4;
    public final static int TASK_TYPE_PHY_DUP = 5;   // 对应cmdp自动创建快照的动作
    public final static int TASK_TYPE_SWITCH_L2I = 6;
    public final static int TASK_TYPE_SWITCH_I2L = 7;
    public final static int TASK_TYPE_SWITCH_RECOVER_LOCALDISK = 8;
    
    public final static String TASK_RESULT_END  = "END";
    public final static String TASK_RESULT_FAIL = "FAILED";
    
    private int task_id;
    private String start_time;  // format: 20070729111111
    private String clnt_name;   // len: 60
    private int task_type;      // 1: init 2: local disk-->iscsi 3: iscsi-->local disk 4: restore original disk
    private String task_result; // END,FAILED
    private String task_msg;    // len: 1024
    private int task_clnt_id;
    private int task_clnt_type = 0;
    private int task_parent_clnt_id = -1;
    private int task_parent_clnt_type = DestAgent.TYPE_INVALID_HOST_TYPE;
    private String task_clnt_mac = "";
    private String task_clnt_dhcp_ip = "";

    /** Creates a new instance of UWSReport */
    public UWSReport() {
    }
    
    public int getTaskID(){
        return task_id;
    }
    public void setTaskID( int val ){
        task_id = val;
    }
    
    public String getSTime(){
        return start_time;
    }
    public void setSTime( String val ){
        start_time = val;
    }
    
    public String getClntName(){
        return clnt_name;
    }
    public void setClntName( String val ){
        clnt_name = val;
    }
    
    public int getTType(){
        return task_type;
    }
    public void setTType( int val ){
        task_type = val;
    }
    
    public String getTResult(){
        return task_result;
    }
    public void setTResult( String val ){
        task_result = val;
    }
    
    public String getTaskMsg(){
        return task_msg;
    }
    public void setTaskMsg( String val ){
        task_msg = val;
    }

    public int getTaskClntId(){
        return this.task_clnt_id;
    }
    public void setTaskClntId( int cid ){
        this.task_clnt_id = cid;
    }

    /**
     * @return the task_clnt_type
     */
    public int getTask_clnt_type() {
        return task_clnt_type;
    }

    /**
     * @param task_clnt_type the task_clnt_type to set
     */
    public void setTask_clnt_type(int task_clnt_type) {
        this.task_clnt_type = task_clnt_type;
    }

    /**
     * @return the task_parent_clnt_id
     */
    public int getTask_parent_clnt_id() {
        return task_parent_clnt_id;
    }

    /**
     * @param task_parent_clnt_id the task_parent_clnt_id to set
     */
    public void setTask_parent_clnt_id(int task_parent_clnt_id) {
        this.task_parent_clnt_id = task_parent_clnt_id;
    }

    /**
     * @return the task_parent_clnt_type
     */
    public int getTask_parent_clnt_type() {
        return task_parent_clnt_type;
    }

    /**
     * @param task_parent_clnt_type the task_parent_clnt_type to set
     */
    public void setTask_parent_clnt_type(int task_parent_clnt_type) {
        this.task_parent_clnt_type = task_parent_clnt_type;
    }

    /**
     * @return the task_clnt_mac
     */
    public String getTask_clnt_mac() {
        return task_clnt_mac;
    }

    /**
     * @param task_clnt_mac the task_clnt_mac to set
     */
    public void setTask_clnt_mac(String task_clnt_mac) {
        this.task_clnt_mac = task_clnt_mac;
    }

    public String prtMe(){
        StringBuffer buf = new StringBuffer();

        buf.append( "start_time=" + start_time );
        buf.append( "\n" + "clnt_name=" + clnt_name );
        buf.append( "\n" + "task_type=" + task_type );
        buf.append( "\n" + "task_result=" + task_result );
        buf.append( "\n" + "task_msg=" + task_msg );
        buf.append( "\n" + "task_clnt_id=" + task_clnt_id );
        buf.append( "\n" + "task_clnt_type=" + task_clnt_type );
        buf.append( "\n" + "task_parent_clnt_id=" + task_parent_clnt_id );
        buf.append( "\n" + "task_parent_clnt_type=" + task_parent_clnt_type );
        buf.append( "\n" + "task_clnt_mac=" + task_clnt_mac );
        buf.append( "\n" + "task_clnt_dhcp_ip="+task_clnt_dhcp_ip +"\n" );
        
        return buf.toString();
    }

    /**
     * @return the task_clnt_dhcp_ip
     */
    public String getTask_clnt_dhcp_ip() {
        return task_clnt_dhcp_ip;
    }

    /**
     * @param task_clnt_dhcp_ip the task_clnt_dhcp_ip to set
     */
    public void setTask_clnt_dhcp_ip(String task_clnt_dhcp_ip) {
        this.task_clnt_dhcp_ip = task_clnt_dhcp_ip;
    }
}
