/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cluster.entity;

import guisanboot.res.ResourceCenter;

/**
 * ClusterConfigInfo.java
 *
 * Created on 2011-8-25, 10:06:49
 */
public class ClusterConfigInfo {
    public final static String CCI_ClusterDiskLength="ClusterDiskLength";
    public final static String CCI_diskinterval="diskinterval";
    public final static String CCI_sharediskguid="sharediskguid";
    public final static String CCI_ErrorCode="ErrorCode";
    public final static String CCI_isSocketStarted="isSocketStarted";
    public final static String CCI_ret="ret";
    public final static String CCI_OtherStatus="OtherStatus";
    public final static String CCI_isEnd="isEnd";
    public final static String CCI_OtherClusterDiskStartoffset="OtherClusterDiskStartoffset";
    public final static String CCI_port= "port";
    public final static String CCI_isNetWorkError="isNetWorkError";
    public final static String CCI_ClusterType="ClusterType";
    public final static String CCI_HasReadDiskTag="HasReadDiskTag";
    public final static String CCI_ifCluster="ifCluster";
    public final static String CCI_WRDiskFailedTimes="WRDiskFailedTimes";
    public final static String CCI_SendErrorTimes="SendErrorTimes";
    public final static String CCI_thisip="thisip";
    public final static String CCI_ThisClusterDiskStartoffset="ThisClusterDiskStartoffset";
    public final static String CCI_hostguid="hostguid";
    public final static String CCI_OtherClusterDiskLength="OtherClusterDiskLength";
    public final static String CCI_ClusterDiskStartoffset="ClusterDiskStartoffset";
    public final static String CCI_otherip="otherip";
    public final static String CCI_return="return";
    public final static String CCI_maxlisten="maxlisten";
    public final static String CCI_ErrorType="ErrorType";
    public final static String CCI_socketinterval="socketinterval";
    public final static String CCI_isMaster="isMaster"; // 1:is master    0:is not master
    public final static String CCI_isExit="isExit";
    public final static String CCI_ThisClusterDiskLength="ThisClusterDiskLength";

    private String ClusterDiskLength="";
    private String diskinterval="";
    private String sharediskguid="";
    private String ErrorCode="";
    private String isSocketStarted="";
    private String ret="";
    private String OtherStatus="";
    private String isEnd="";
    private String OtherClusterDiskStartoffset="";
    private String port= ResourceCenter.PORT_CLUSTER_CMDP+"";
    private String isNetWorkError="";
    private String ClusterType="";
    private String HasReadDiskTag="";
    private String ifCluster="";
    private String WRDiskFailedTimes="";
    private String SendErrorTimes="";
    private String thisip="";
    private String ThisClusterDiskStartoffset="";
    private String hostguid="";
    private String OtherClusterDiskLength="";
    private String ClusterDiskStartoffset="";
    private String otherip="";
    private String aReturn="";
    private String maxlisten="";
    private String ErrorType="";
    private String socketinterval="";
    private String isMaster="0"; // 1: is master    0: is not master
    private String isExit="";
    private String ThisClusterDiskLength="";

    public ClusterConfigInfo(){
    }

    /**
     * @return the ClusterDiskLength
     */
    public String getClusterDiskLength() {
        return ClusterDiskLength;
    }

    /**
     * @param ClusterDiskLength the ClusterDiskLength to set
     */
    public void setClusterDiskLength(String ClusterDiskLength) {
        this.ClusterDiskLength = ClusterDiskLength;
    }

    /**
     * @return the diskinterval
     */
    public String getDiskinterval() {
        return diskinterval;
    }

    /**
     * @param diskinterval the diskinterval to set
     */
    public void setDiskinterval(String diskinterval) {
        this.diskinterval = diskinterval;
    }

    /**
     * @return the sharediskguid
     */
    public String getSharediskguid() {
        return sharediskguid;
    }

    /**
     * @param sharediskguid the sharediskguid to set
     */
    public void setSharediskguid(String sharediskguid) {
        this.sharediskguid = sharediskguid;
    }

    /**
     * @return the ErrorCode
     */
    public String getErrorCode() {
        return ErrorCode;
    }

    /**
     * @param ErrorCode the ErrorCode to set
     */
    public void setErrorCode(String ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    /**
     * @return the isSocketStarted
     */
    public String getIsSocketStarted() {
        return isSocketStarted;
    }

    /**
     * @param isSocketStarted the isSocketStarted to set
     */
    public void setIsSocketStarted(String isSocketStarted) {
        this.isSocketStarted = isSocketStarted;
    }

    /**
     * @return the ret
     */
    public String getRet() {
        return ret;
    }

    /**
     * @param ret the ret to set
     */
    public void setRet(String ret) {
        this.ret = ret;
    }

    /**
     * @return the OtherStatus
     */
    public String getOtherStatus() {
        return OtherStatus;
    }

    /**
     * @param OtherStatus the OtherStatus to set
     */
    public void setOtherStatus(String OtherStatus) {
        this.OtherStatus = OtherStatus;
    }

    /**
     * @return the isEnd
     */
    public String getIsEnd() {
        return isEnd;
    }

    /**
     * @param isEnd the isEnd to set
     */
    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    /**
     * @return the OtherClusterDiskStartoffset
     */
    public String getOtherClusterDiskStartoffset() {
        return OtherClusterDiskStartoffset;
    }

    /**
     * @param OtherClusterDiskStartoffset the OtherClusterDiskStartoffset to set
     */
    public void setOtherClusterDiskStartoffset(String OtherClusterDiskStartoffset) {
        this.OtherClusterDiskStartoffset = OtherClusterDiskStartoffset;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the isNetWorkError
     */
    public String getIsNetWorkError() {
        return isNetWorkError;
    }

    /**
     * @param isNetWorkError the isNetWorkError to set
     */
    public void setIsNetWorkError(String isNetWorkError) {
        this.isNetWorkError = isNetWorkError;
    }

    /**
     * @return the ClusterType
     */
    public String getClusterType() {
        return ClusterType;
    }

    /**
     * @param ClusterType the ClusterType to set
     */
    public void setClusterType(String ClusterType) {
        this.ClusterType = ClusterType;
    }

    /**
     * @return the HasReadDiskTag
     */
    public String getHasReadDiskTag() {
        return HasReadDiskTag;
    }

    /**
     * @param HasReadDiskTag the HasReadDiskTag to set
     */
    public void setHasReadDiskTag(String HasReadDiskTag) {
        this.HasReadDiskTag = HasReadDiskTag;
    }

    /**
     * @return the ifCluster
     */
    public String getIfCluster() {
        return ifCluster;
    }

    /**
     * @param ifCluster the ifCluster to set
     */
    public void setIfCluster(String ifCluster) {
        this.ifCluster = ifCluster;
    }

    /**
     * @return the WRDiskFailedTimes
     */
    public String getWRDiskFailedTimes() {
        return WRDiskFailedTimes;
    }

    /**
     * @param WRDiskFailedTimes the WRDiskFailedTimes to set
     */
    public void setWRDiskFailedTimes(String WRDiskFailedTimes) {
        this.WRDiskFailedTimes = WRDiskFailedTimes;
    }

    /**
     * @return the SendErrorTimes
     */
    public String getSendErrorTimes() {
        return SendErrorTimes;
    }

    /**
     * @param SendErrorTimes the SendErrorTimes to set
     */
    public void setSendErrorTimes(String SendErrorTimes) {
        this.SendErrorTimes = SendErrorTimes;
    }

    /**
     * @return the thisip
     */
    public String getThisip() {
        return thisip;
    }

    /**
     * @param thisip the thisip to set
     */
    public void setThisip(String thisip) {
        this.thisip = thisip;
    }

    /**
     * @return the ThisClusterDiskStartoffset
     */
    public String getThisClusterDiskStartoffset() {
        return ThisClusterDiskStartoffset;
    }

    /**
     * @param ThisClusterDiskStartoffset the ThisClusterDiskStartoffset to set
     */
    public void setThisClusterDiskStartoffset(String ThisClusterDiskStartoffset) {
        this.ThisClusterDiskStartoffset = ThisClusterDiskStartoffset;
    }

    /**
     * @return the hostguid
     */
    public String getHostguid() {
        return hostguid;
    }

    /**
     * @param hostguid the hostguid to set
     */
    public void setHostguid(String hostguid) {
        this.hostguid = hostguid;
    }

    /**
     * @return the OtherClusterDiskLength
     */
    public String getOtherClusterDiskLength() {
        return OtherClusterDiskLength;
    }

    /**
     * @param OtherClusterDiskLength the OtherClusterDiskLength to set
     */
    public void setOtherClusterDiskLength(String OtherClusterDiskLength) {
        this.OtherClusterDiskLength = OtherClusterDiskLength;
    }

    /**
     * @return the ClusterDiskStartoffset
     */
    public String getClusterDiskStartoffset() {
        return ClusterDiskStartoffset;
    }

    /**
     * @param ClusterDiskStartoffset the ClusterDiskStartoffset to set
     */
    public void setClusterDiskStartoffset(String ClusterDiskStartoffset) {
        this.ClusterDiskStartoffset = ClusterDiskStartoffset;
    }

    /**
     * @return the otherip
     */
    public String getOtherip() {
        return otherip;
    }

    /**
     * @param otherip the otherip to set
     */
    public void setOtherip(String otherip) {
        this.otherip = otherip;
    }

    /**
     * @return the aReturn
     */
    public String getReturn() {
        return aReturn;
    }

    /**
     * @param aReturn the aReturn to set
     */
    public void setReturn(String aReturn) {
        this.aReturn = aReturn;
    }

    /**
     * @return the maxlisten
     */
    public String getMaxlisten() {
        return maxlisten;
    }

    /**
     * @param maxlisten the maxlisten to set
     */
    public void setMaxlisten(String maxlisten) {
        this.maxlisten = maxlisten;
    }

    /**
     * @return the ErrorType
     */
    public String getErrorType() {
        return ErrorType;
    }

    /**
     * @param ErrorType the ErrorType to set
     */
    public void setErrorType(String ErrorType) {
        this.ErrorType = ErrorType;
    }

    /**
     * @return the socketinterval
     */
    public String getSocketinterval() {
        return socketinterval;
    }

    /**
     * @param socketinterval the socketinterval to set
     */
    public void setSocketinterval(String socketinterval) {
        this.socketinterval = socketinterval;
    }

    /**
     * @return the isMaster
     */
    public String getIsMaster() {
        return isMaster;
    }
    public boolean isMasterNode(){
        return isMaster.equals("1");
    }
    
    /**
     * @param isMaster the isMaster to set
     */
    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }

    /**
     * @return the isExit
     */
    public String getIsExit() {
        return isExit;
    }

    /**
     * @param isExit the isExit to set
     */
    public void setIsExit(String isExit) {
        this.isExit = isExit;
    }

    /**
     * @return the ThisClusterDiskLength
     */
    public String getThisClusterDiskLength() {
        return ThisClusterDiskLength;
    }

    /**
     * @param ThisClusterDiskLength the ThisClusterDiskLength to set
     */
    public void setThisClusterDiskLength(String ThisClusterDiskLength) {
        this.ThisClusterDiskLength = ThisClusterDiskLength;
    }
    
}
