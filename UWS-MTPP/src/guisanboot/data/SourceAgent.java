/*
 * SourceAgent.java
 *
 * Created on 2008/6/16, �AM�11:02
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package guisanboot.data;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.BasicUIObject;

/**
 *
 * @author zjj
 */
public class SourceAgent extends BasicUIObject {
    public final static String SRCAGNT_FLAG ="Record:";
    public final static String SRCAGNT_ID ="src_agnt_id";
    public final static String SRCAGNT_IP ="src_agnt_ip";
    public final static String SRCAGNT_OS ="src_agnt_os";
    public final static String SRCAGNT_MACHINE="src_agnt_machine";
    public final static String SRCAGNT_DESC = "src_agnt_desc";
    public final static String SRCAGNT_UWS_ID ="src_agnt_uws_id";
    public final static String SRCAGNT_PORT = "src_agnt_port";
    public final static String SRCAGNT_PORT1 = "src_agnt_port1";
    public final static String SRCAGNT_PSN ="src_agnt_psn";
    public final static String SRCAGNT_BOOT_MODE = "src_agnt_boot_mode";
    public final static String SRCAGNT_PROTECT_MODE = "src_agnt_protect_type";
    public final static String SRCAGNT_CLNT_OPT = "src_agnt_clust_opt";
    public final static String SRCAGNT_CLUSTER_ID ="src_agnt_cluster_id";
    public final static String SRCAGNT_PRI_IP = "src_agnt_priv_ip";
    public final static String SRCAGNT_VIP = "src_agnt_vip";

    private int src_agnt_id;
    
    // 当为源端UWS服务器时，src_agnt_ip里放UWS的ip
    private String src_agnt_ip;    // length: 32

    // 当为源端UWS服务器时，src_agnt_os里放UWS的psn
    private String src_agnt_os;    // length: 30
    
    private String src_agnt_machine=""; //length 32

    // 这里放agent的UUID,如果UUID为空,则该对象表示一个源端UWS服务器。
    // 所有来自于源端UWS的空闲卷都对应一个这样的srcagnt结构。
    private String src_agnt_desc;  // length: 255

    // src_agnt_uws_id表示该sa对应哪个镜像服务器（uws表），这样sa和对应的uws就完整地
    // 在远端构成了一个主机间的关联结构，这样每台sa对都应一个uws，如果sa被回滚了，那么
    // 该sa的uws就不关联uws了。
    // src_agnt_uws_id为负数或0时，表示该srcagnt是个rollback主机。否则就是一个正常的主机
    private int src_agnt_uws_id; 
    
    private int src_agnt_port = -1;  // ResourceCenter.CMDP_AGENT_PORT;  // cmdp port
    private int src_agnt_port1 = -1; // ResourceCenter.MTPP_AGENT_PORT;  // mtpp port
    
    private String src_agnt_psn="";    
    private int src_agnt_boot_mode = 0;

    private int src_agnt_protect_type = BootHost.PROTECT_TYPE_MTPP;

    // for cluster
    private String src_agnt_pri_ip="";
    private String src_agnt_vip="";
    private int src_agnt_clnt_opt = 0;
    private int src_agnt_cluster_id = 0;

    /** Creates a new instance of SourceAgent */
    public SourceAgent() {
        super( ResourceCenter.TYPE_REMOTE_HOST );
    }
    
    public SourceAgent(
        int src_agnt_id,
        String src_agnt_ip,
        String src_agnt_os,
        String src_agnt_machine,
        String src_agnt_desc,
        int src_agnt_uws_id,
        int src_agnt_port,
        int src_agnt_boot_mode,
        int src_agnt_protect_type
    ) {
        this( src_agnt_id,src_agnt_ip,src_agnt_os,src_agnt_machine,src_agnt_desc,src_agnt_uws_id,src_agnt_port,
            -1,src_agnt_boot_mode,src_agnt_protect_type 
        );
    }

    public SourceAgent(
        int src_agnt_id,
        String src_agnt_ip,
        String src_agnt_os,
        String src_agnt_machine,
        String src_agnt_desc,
        int src_agnt_uws_id,
        int src_agnt_port,
        int src_agnt_port1,
        int src_agnt_boot_mode,
        int src_agnt_protect_type
    ) {
        super( ResourceCenter.TYPE_REMOTE_HOST  );

        this.src_agnt_id = src_agnt_id;
        this.src_agnt_ip = src_agnt_ip;
        this.src_agnt_os = src_agnt_os;
        this.src_agnt_desc = src_agnt_desc;
        this.src_agnt_uws_id = src_agnt_uws_id;
        this.src_agnt_port = src_agnt_port;
        this.src_agnt_port1 = src_agnt_port1;
        this.src_agnt_boot_mode = src_agnt_boot_mode;
        this.src_agnt_machine = src_agnt_machine;
        this.src_agnt_protect_type = src_agnt_protect_type;
    }

    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>( 2 );
            if( this.isNormalHost() ){
                if( this.isWinHost() ){
                    if( this.isCMDPProtect() ){
                        fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER ) );
                    }else{
                        fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ) );
                    }
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK ) );
                }else{                   
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ) );
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK_NWIN ) );
                }
            }else{
                if( this.isWinHost() ){
                    if( this.isCMDPProtect() ){
                        fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER ) );
                    }else{
                        fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ) );
                    }
                }else{
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ) );
                }
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SRCAGNT ) );
            }
        }
    }
    
    public void resetFuncForTree(){
        fsForTree = new ArrayList<Integer>( 2 );         
        if( this.isNormalHost() ){
            if( this.isWinHost() ){
                if( this.isCMDPProtect() ){
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER ) );
                }else{
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ) );
                }
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK ) );
            }else{
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ) );
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK_NWIN ) );
            }
        }else{
            if( this.isWinHost() ){
                if( this.isCMDPProtect() ){
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER ) );
                }else{
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ) );
                }
            }else{
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ) );
            }
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SRCAGNT ) );
        }
    }
    
    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>( 2 );
            if( this.isNormalHost() ){
                if( this.isWinHost() ){
                    if( this.isCMDPProtect() ){
                        fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER ) );
                    }else{
                        fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ) );
                    }
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK ) );
                }else{
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ) );
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK ) );
                }
            }else{
                 if( this.isWinHost() ){
                     if( this.isCMDPProtect() ){
                        fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER ) );
                     }else{
                        fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ) );
                     }
                }else{
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ) );
                }                 
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SRCAGNT ) );
            }
        }
    }
    
    public void resetFuncForTable(){
        fsForTable = new ArrayList<Integer>( 2 );
        if( this.isNormalHost() ){
            if( this.isWinHost() ){
                if( this.isCMDPProtect() ){
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER ) );
                }else{
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ) );
                }
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK ) );
            }else{
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ) );
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_ROLLBACK ) );
            }
        }else{
             if( this.isWinHost() ){
                 if( this.isCMDPProtect() ){
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILOVER ) );
                 }else{
                    fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ) );
                 }
            }else{
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ) );
            }
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_SRCAGNT ) );
        }
    }
    
    public int getSrc_agnt_id() {
        return src_agnt_id;
    }

    public void setSrc_agnt_id(int src_agnt_id) {
        this.src_agnt_id = src_agnt_id;
    }

    public String getSrc_agnt_ip() {
        return src_agnt_ip;
    }

    public void setSrc_agnt_ip(String src_agnt_ip) {
        this.src_agnt_ip = src_agnt_ip;
    }

    public String getSrc_agnt_os() {
        return src_agnt_os;
    }
    
    public void setSrc_agnt_os(String src_agnt_os) {
        this.src_agnt_os = src_agnt_os;
    }
    
    public boolean isWinHost(){
        return BootHost.isWinHost( src_agnt_os );
    }
    
    public String getWinPlatForm(){ 
        try{
            String release = src_agnt_os.substring(3);
            return release.toUpperCase();
        }catch(Exception ex){
            return "2003";
        }
    }
    
    public boolean isLargerThanVista(){
        String type = getWinPlatForm();
        return BootHost.isWinVista( type.toUpperCase() ) || BootHost.isWin2008( type );
    }
    
    public String getMachine(){
        return this.src_agnt_machine;
    }
    public void setMachine( String machine ){
        this.src_agnt_machine = machine;
    }
    public boolean is64BitOS(){
        return ( src_agnt_machine.indexOf("_64") >= 0 );
    }

    public String getUUID(){
        return src_agnt_desc;
    }
    
    public String getSrc_agnt_desc() {
        return src_agnt_desc;
    }
    
    public void setSrc_agnt_desc(String src_agnt_desc) {
        this.src_agnt_desc = src_agnt_desc;
    }

    public boolean isRepresentAUWS(){
        return src_agnt_desc.equals( "" );
    }
    
    public int getSrc_agnt_uws_id() {
        return src_agnt_uws_id;
    }

    public void setSrc_agnt_uws_id(int src_agnt_uws_id) {
        this.src_agnt_uws_id = src_agnt_uws_id;
    }
    
    public int getSrc_agnt_port(){
        return src_agnt_port;
    }
    public void setSrc_agnt_port( int src_agnt_port ){
        this.src_agnt_port = src_agnt_port;
    }
    
    public int getSrc_agnt_port1(){
        return src_agnt_port1;
    }
    public void setSrc_agnt_port1( int src_agnt_port1 ){
        this.src_agnt_port1 = src_agnt_port1;
    }
    
    public String getSrc_agnt_psn(){
        return src_agnt_psn;
    }
    public void setSrc_agnt_psn( String val ){
        src_agnt_psn = val;        
    }    
    
    public boolean isNormalHost(){
        return ( src_agnt_uws_id > 0 );
    }
    public boolean isRollbackedHost(){
        return ( src_agnt_uws_id <= 0 );
    }
    
    public int getSrc_agnt_boot_mode(){
        return this.src_agnt_boot_mode;
    }
    public void setSrc_agnt_boot_mode( int src_agnt_boot_mode ){
        this.src_agnt_boot_mode = src_agnt_boot_mode;
    }
    
    public String getBootModeString(){
        if( this.isWinHost() ){
            if( this.isEmBoot() ){
                return SanBootView.res.getString("common.boottype.boot");
            }else if( this.isIBoot() ){
                return SanBootView.res.getString("common.boottype.iboot");
            }else{
                return SanBootView.res.getString("common.boottype.iscsihba");
            }
        }else{
            return "";
        }
    }
    
    public boolean isEmBoot(){
        return ( src_agnt_boot_mode == BootHost.BOOT_MODE_EM );
    }   
    public boolean isIBoot(){
        return ( src_agnt_boot_mode == BootHost.BOOT_MODE_IBOOT );
    }
    public boolean isISCSIHBABoot(){
        return ( src_agnt_boot_mode == BootHost.BOOT_MODE_ISCSI_HBA );
    }

    public boolean isIA(){
        return BootHost.isIA( this.src_agnt_machine );
    }

    @Override public String getComment(){
        return src_agnt_id + "";
    }

    public int getProtectType(){
        return this.src_agnt_protect_type;
    }
    public void setProtectType( int val ){
        this.src_agnt_protect_type = val;
    }
    public boolean isMTPPProtect(){
        return ( src_agnt_protect_type == BootHost.PROTECT_TYPE_MTPP );
    }
    public boolean isCMDPProtect(){
        return ( src_agnt_protect_type == BootHost.PROTECT_TYPE_CMDP );
    }
    public String getProtectString(){
        if( this.isMTPPProtect() ){
            return SanBootView.res.getString("common.logical");
        }else if( this.isCMDPProtect() ){
            return SanBootView.res.getString("common.physical");
        }else{
            return SanBootView.res.getString("common.unknown");
        }
    }

     /**
     * @return the clnt_opt
     */
    public int getClnt_opt() {
        return this.src_agnt_clnt_opt;
    }

    /**
     * @param clnt_opt the clnt_opt to set
     */
    public void setClnt_opt(int clnt_opt) {
        this.src_agnt_clnt_opt = clnt_opt;
    }

    public boolean isCluster(){
        return ( ( src_agnt_clnt_opt & BootHost.CLNT_OPT_BIT_CLUSTER )  != 0 );
    }
    public void setClusterBit(){
        src_agnt_clnt_opt = src_agnt_clnt_opt | BootHost.CLNT_OPT_BIT_CLUSTER;
    }

    /**
     * @return the clnt_cluster_id
     */
    public int getClnt_cluster_id() {
        return this.src_agnt_cluster_id;
    }
    
    public void setClnt_cluster_id( int val ){
        src_agnt_cluster_id = val;
    }
    
    //** TableRevealable的实现**/
    public boolean enableTableEditable(){
        return false;
    }
    public boolean enableSelected(){
        return true;
    }
    public int  getAlignType(){
        return JLabel.LEFT;
    }
    public Icon getTableIcon(){
        return ResourceCenter.ICON_NODE_16;
    }
    public String toTableString(){
        return src_agnt_id+"";
    }
    
    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.ICON_NODE_16;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.ICON_NODE_16;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return this.src_agnt_ip;
    }   
    public String toTipString(){
        return this.src_agnt_ip +" [ " + src_agnt_os + " | "+ src_agnt_id + " ] ";
    }

    /**
     * @return the src_agnt_pri_ip
     */
    public String getSrc_agnt_pri_ip() {
        return src_agnt_pri_ip;
    }

    /**
     * @param src_agnt_pri_ip the src_agnt_pri_ip to set
     */
    public void setSrc_agnt_pri_ip(String src_agnt_pri_ip) {
        this.src_agnt_pri_ip = src_agnt_pri_ip;
    }

    /**
     * @return the src_agnt_vip
     */
    public String getSrc_agnt_vip() {
        return src_agnt_vip;
    }

    /**
     * @param src_agnt_vip the src_agnt_vip to set
     */
    public void setSrc_agnt_vip(String src_agnt_vip) {
        this.src_agnt_vip = src_agnt_vip;
    }
}
