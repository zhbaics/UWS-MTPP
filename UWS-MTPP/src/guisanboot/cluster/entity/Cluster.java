/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cluster.entity;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.data.BootHost;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.SanBootView;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.Icon;
import javax.swing.JLabel;
import mylib.UI.BasicUIObject;

/**
 * Cluster.java
 *
 * Created on 2011-7-11, 15:28:28
 */
public class Cluster extends BasicUIObject{
    public final static String CLUSTERECFLAG ="Record:";
    public final static String CMDPRECFLAG ="---index";
    public final static String CLUSTER_ID ="cluster_id";
    public final static String CLUSTER_NAME ="cluster_name";
    public final static String CLUSTER_INIT_FLAG="cluster_inited_flag";
    public final static String CLUSTER_AUTO_DR_FLAG="cluster_auto_dr_flag";
    public final static String CLUSTER_AUTO_REBOOT_FLAG="cluster_auto_reboot_flag";
    public final static String CLUSTER_STOP_ALL_BASE_SERV="cluster_stop_base_service";
    public final static String CLUSTER_BOOT_MAC="cluster_mac_address";
    public final static String CLUSTER_BOOT_MODE="cluster_boot_mode";
    public final static String CLUSTER_PROTECT_TYPE = "cluster_protect_type";
    public final static String CLUSTER_TYPE = "cluster_type";
    
    private int cluster_id ;
    private String cluster_name ; // len:60
    private int cluster_inited_flag;
    private int cluster_auto_dr_flag;
    private int cluster_auto_reboot_flag;
    private int cluster_stop_base_service;
    private String cluster_mac_address;
    private int cluster_boot_mode = BootHost.BOOT_MODE_EM;
    private int cluster_protect_type = BootHost.PROTECT_TYPE_MTPP;

    private int cluster_type = ResourceCenter.TYPE_CLUS_NOT_RAC_INT;

    private ArrayList<SubCluster> subList = new ArrayList<SubCluster>();

    public Cluster(){
        super( ResourceCenter.TYPE_CLUSTER );
    }

    public void addFunctionsForTree(){
        if( fsForTree == null ){
            fsForTree = new ArrayList<Integer>(9);
            if( this.isWinHost() ){
                if( this.isMTPPProtect() ){
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_INIT ) );
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ));
                    //fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_RESTORE_DISK ));
                    //fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_FAILBACK ));
                    //fsForTree.add( new Integer(  MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    //fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_NETDISK ));
                    //fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_DUP_NETDISK ));
                    //fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_LOCALDISK));
                }else{ // cmdp
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_CLUSTER_PHY_WIN_INIT ) );
                    fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_CLUSTER_PHY_WIN_FAILOVER ));
                    //fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_CLUSTER_PHY_WIN_RESTORE_DISK ));
                    //fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILBACK ));
                    //fsForTree.add( new Integer(  MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    //fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_NETDISK ));
                    //fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_DUP_NETDISK ));
                    //fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_LOCALDISK));
                    fsForTree.add( new Integer(  MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_INIT_PROGRESS ) );
                }
            }else{
                fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN  ) );
                fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SELECT_BOOT_VER ));
                fsForTree.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ));
                //fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN ) );
            }
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_CLUSTER ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_CLUSTER ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTree.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PROPERTY));
        }
    }

    public void addFunctionsForTable(){
        if( fsForTable == null ){
            fsForTable = new ArrayList<Integer>(9);
            if( this.isWinHost() ){
                if( this.isMTPPProtect() ){
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_INIT ) );
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_FAILOVER ));
                    //fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_RESTORE_DISK ));
                    //fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_FAILBACK ));
                    //fsForTable.add( new Integer(  MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    //fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_NETDISK ));
                    //fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_DUP_NETDISK ));
                    //fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_SWITCH_TO_LOCALDISK));
                }else{ // cmdp
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_CLUSTER_PHY_WIN_INIT ) );
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_CLUSTER_PHY_WIN_FAILOVER ));
                    //fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_CLUSTER_PHY_WIN_RESTORE_DISK ));
                    //fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_FAILBACK ));
                    //fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    //fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_NETDISK ));
                    //fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_DUP_NETDISK ));
                    //fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_WIN_PHY_SWITCH_TO_LOCALDISK));
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
                    fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_PHY_QUERY_INIT_PROGRESS ) );
                }
            }else{
                fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN  ) );
                fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_SELECT_BOOT_VER ));
                fsForTable.add( new Integer ( MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD ));
                //fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_REST_DISK_NWIN ) );
            }
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_MOD_CLUSTER ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_DEL_CLUSTER ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_SEPARATOR ) );
            fsForTable.add( new Integer( MenuAndBtnCenterForMainUi.FUNC_PROPERTY));
        }
    }

    /**
     * @return the cluster_id
     */
    public int getCluster_id() {
        return cluster_id;
    }

    /**
     * @return the cluster_name
     */
    public String getCluster_name() {
        return cluster_name;
    }

    /**
     * @return the cluster_inited_flag
     */
    public int getCluster_inited_flag() {
        return cluster_inited_flag;
    }
    public boolean isInited(){
        return ( cluster_inited_flag == 1 );
    }

    /**
     * @return the cluster_auto_dr_flag
     */
    public int getCluster_auto_dr_flag() {
        return cluster_auto_dr_flag;
    }
    public boolean isAutoDR(){
        return ( cluster_auto_dr_flag == 1 );
    }
    
    /**
     * @return the cluster_auto_reboot_flag
     */
    public int getCluster_auto_reboot_flag() {
        return cluster_auto_reboot_flag;
    }
    public boolean isAutoReboot(){
        return ( cluster_auto_reboot_flag == 1);
    }

    /**
     * @return the cluster_stop_base_service
     */
    public int getCluster_stop_base_service() {
        return cluster_stop_base_service;
    }
    public boolean isUseOdyDhcp(){
        return ( cluster_stop_base_service & BootHost.BIT0_DHCP ) !=0 ;
    }
    public void clearUseOdyDhcp(){
        cluster_stop_base_service &= ~BootHost.BIT0_DHCP;
    }
    public void setUseOdyDhcp(){
        cluster_stop_base_service |= BootHost.BIT0_DHCP;
    }
    public void  setStopAllBaseServ( ){
        cluster_stop_base_service |=  BootHost.BIT1_BASE_SERVICE;
    }
    public void clearStopAllBaseServ(){
        cluster_stop_base_service &=  ~BootHost.BIT1_BASE_SERVICE;
    }
    public boolean isStopAllBaseServ(){
        return ( cluster_stop_base_service & BootHost.BIT1_BASE_SERVICE )!=0;
    }

    /**
     * @return the cluster_mac_address
     */
    public String getCluster_mac_address() {
        return cluster_mac_address;
    }

    /**
     * @return the cluster_boot_mode
     */
    public int getCluster_boot_mode() {
        return cluster_boot_mode;
    }
    public boolean isEmBoot(){
        return ( cluster_boot_mode == BootHost.BOOT_MODE_EM );
    }
    public boolean isIBoot(){
        return ( cluster_boot_mode == BootHost.BOOT_MODE_IBOOT );
    }
    public boolean isISCSIHBABoot(){
        return ( cluster_boot_mode == BootHost.BOOT_MODE_ISCSI_HBA );
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

    /**
     * @return the cluster_protect_type
     */
    public int getCluster_protect_type() {
        return cluster_protect_type;
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
    public boolean isMTPPProtect(){
        return ( cluster_protect_type == BootHost.PROTECT_TYPE_MTPP );
    }
    public boolean isCMDPProtect(){
        return ( cluster_protect_type == BootHost.PROTECT_TYPE_CMDP );
    }
    
    /**
     * @param cluster_id the cluster_id to set
     */
    public void setCluster_id(int cluster_id) {
        this.cluster_id = cluster_id;
    }

    /**
     * @param cluster_name the cluster_name to set
     */
    public void setCluster_name(String cluster_name) {
        this.cluster_name = cluster_name;
    }

    /**
     * @param cluster_inited_flag the cluster_inited_flag to set
     */
    public void setCluster_inited_flag(int cluster_inited_flag) {
        this.cluster_inited_flag = cluster_inited_flag;
    }

    /**
     * @param cluster_auto_dr_flag the cluster_auto_dr_flag to set
     */
    public void setCluster_auto_dr_flag(int cluster_auto_dr_flag) {
        this.cluster_auto_dr_flag = cluster_auto_dr_flag;
    }

    /**
     * @param cluster_auto_reboot_flag the cluster_auto_reboot_flag to set
     */
    public void setCluster_auto_reboot_flag(int cluster_auto_reboot_flag) {
        this.cluster_auto_reboot_flag = cluster_auto_reboot_flag;
    }

    /**
     * @param cluster_stop_base_service the cluster_stop_base_service to set
     */
    public void setCluster_stop_base_service(int cluster_stop_base_service) {
        this.cluster_stop_base_service = cluster_stop_base_service;
    }

    /**
     * @param cluster_mac_address the cluster_mac_address to set
     */
    public void setCluster_mac_address(String cluster_mac_address) {
        this.cluster_mac_address = cluster_mac_address;
    }

    /**
     * @param cluster_boot_mode the cluster_boot_mode to set
     */
    public void setCluster_boot_mode(int cluster_boot_mode) {
        this.cluster_boot_mode = cluster_boot_mode;
    }

    /**
     * @param cluster_protect_type the cluster_protect_type to set
     */
    public void setCluster_protect_type(int cluster_protect_type) {
        this.cluster_protect_type = cluster_protect_type;
    }

    public void addSubCluster( SubCluster subc ){
        boolean isOk = checkUnique( subc );
        assert isOk;
        this.subList.add( subc );
    }

    private boolean checkUnique( SubCluster one ){
SanBootView.log.debug( getClass().getName(), "to check host: " + one.getHost().getID() );
        int size = subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster a = subList.get(i);
SanBootView.log.debug( getClass().getName(), "host item: " + a.getHost().getID() );
            if( a.getHost().getIP().equals( one.getHost().getIP() ) && ( a.getHost().getPort() == one.getHost().getPort() ) ){
                return false;
            }
        }
        return true;
    }

    public boolean checkUnique(){
        Hashtable<String,SubCluster> hash = new Hashtable<String,SubCluster>();
        int size = subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            String key = subc.getHost().getIP()+subc.getHost().getPort();
            Object obj = hash.get( key );
            if( obj == null ){
                hash.put( key, subc );
            }else{
                return false;
            }
        }
        return true;
    }

    public void removeSubCluster( SubCluster subc ){
        this.subList.remove( subc );
    }

    public void removeAllSubCluster(){
        this.subList.clear();
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
        return ResourceCenter.SMALL_ICON_SANBOOT;
    }
    public String toTableString(){
        return this.cluster_id+"";
    }

    //** TreeRevealable的实现*/
    public Icon getExpandIcon(){
        return ResourceCenter.SMALL_ICON_SANBOOT;
    }
    public Icon getCollapseIcon(){
        return ResourceCenter.SMALL_ICON_SANBOOT;
    }
    public boolean enableTreeEditable(){
        return false;
    }
    public String toTreeString(){
        return this.cluster_name;
    }
    public String toTipString(){
        return this.cluster_name+" [ " + this.cluster_id + " ] ";
    }

    public boolean isWinHost(){
        int size = this.subList.size();
        if( size > 0 ){
            return subList.get(0).getHost().isWinHost();
        }else{
SanBootView.log.error(this.getClass().getName(), "There is no member in cluster: " + this.cluster_id );
            return false;
        }
    }

    public String getClusterIP(){
        boolean isFirst = true;

        StringBuffer ret = new StringBuffer();
        int size = this.subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            if( !subc.getHost().getUUID().equals("") ){
                if( isFirst ){
                    ret.append( subc.getHost().getIP() );
                    isFirst = false;
                }else{
                    ret.append("/");
                    ret.append( subc.getHost().getIP() );
                }
            }
        }

        return ret.toString();
    }

    public String getClusterPort(){
        boolean isFirst = true;

        StringBuffer ret = new StringBuffer();
        int size = this.subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            if( !subc.getHost().getUUID().equals("") ){
                if( isFirst ){
                    ret.append( subc.getHost().getPort()+"" );
                    isFirst = false;
                }else{
                    ret.append("/");
                    ret.append( subc.getHost().getPort()+"");
                }
            }
        }

        return ret.toString();
    }

    public String getClusterUUID(){
        boolean isFirst = true;

        StringBuffer ret = new StringBuffer();
        int size = this.subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            if( !subc.getHost().getUUID().equals("") ){
                if( isFirst ){
                    ret.append( subc.getHost().getUUID() );
                    isFirst = false;
                }else{
                    ret.append(",");
                    ret.append( subc.getHost().getUUID() );
                }
            }
        }

        return ret.toString();
    }

    public boolean isWin2000(){
        int size = this.subList.size();
        if( size > 0 ){
            return this.subList.get(0).getHost().isWin2000();
        }else{
            return false;
        }
    }
    
    public String getClusterOS(){
        int size = this.subList.size();
        if( size > 0 ){
            return this.subList.get(0).getHost().getOS();
        }else{
            return SanBootView.res.getString("common.unknown");
        }
    }

    public String getClusterMachine(){
        int size = this.subList.size();
        if( size > 0 ){
            return this.subList.get(0).getHost().getMachine();
        }else{
            return SanBootView.res.getString("common.unknown");
        }
    }

    // 返回代表真实主机的信息(BootHost的uuid不为空表示它是一个真机)
    public ArrayList<SubCluster> getRealSubCluster(){
        int size = this.subList.size();
        ArrayList<SubCluster> ret = new ArrayList<SubCluster>();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            if( subc.isRealHost() ){
                ret.add( subc );
            }
        }
        return ret;
    }

    public BootHost getOneRealHost(){
        ArrayList<SubCluster> subcList = this.getRealSubCluster();
        if( subcList.size() > 0 ){
            return subcList.get( 0 ).getHost();
        }else{
            return null;
        }
    }

    public BootHost getOtherRealHost(){
        ArrayList<SubCluster> subcList = this.getRealSubCluster();
        if( subcList.size() > 1 ){
            return subcList.get( 1 ).getHost();
        }else{
            return null;
        }
    }

    public ArrayList getUUIDList(){
        ArrayList<String> ret  = new ArrayList<String>();
        int size = this.subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            if( !subc.getHost().getUUID().equals("") ){
                ret.add( subc.getHost().getUUID() );
            }
        }
        return ret;
    }

    public VolumeMap getVolMapFromClusterOnUUIDandLabel( String uuid,String label ){
        int size = this.subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            if( subc.getHost().getUUID().equals( uuid ) ){
                VolumeMap volMap = subc.getVolMapFromSubCluster( label );
                if( volMap != null )
                    return volMap;
            }
        }
        return null;
    }

    // 对于本地卷资源，通过不为空的uuid肯定可以找到；对于共享卷，通过为空的uuid肯定可以找到
    public VolumeMap getVolMapFromClusterOnUUIDandLabel1( String uuid,String label ){
        VolumeMap vol = this.getVolMapFromClusterOnUUIDandLabel( uuid, label );
        if( vol == null ){
            return this.getVolMapFromClusterOnUUIDandLabel( "", label );
        }else{
            return vol;
        }
    }

    public SubCluster getSubCluster( PPProfile prof ){
        int size = this.subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster sbc = subList.get(i);
            if( sbc.getHost().getUUID().equals( prof.getHost_uuid() ) ){
                return sbc;
            }
        }
        return null;
    }

    public ArrayList<SubCluster> getSubClusterList(){
        return this.subList;
    }
    
    public ArrayList<BootHost> getHostProtectedByMTPP(){
        int i,j,size,size1;

        ArrayList<BootHost> ret = new ArrayList<BootHost>();
        size = this.subList.size();
        for( i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            BootHost host = subc.getHost();
            ArrayList<ClusterVolume> cvs = subc.getDisks();
            size1 = cvs.size();
            for( j=0; j<size1; j++ ){
                ClusterVolume cv = cvs.get(j);
                if( cv.isMtppProtect() ){
                    ret.add( host );
                    break;
                }
            }
        }
        return ret;
    }

    public ArrayList<String> getMtppRootVolForCluster( BootHost host ){
        ArrayList<String> ret = new ArrayList<String>();

        int size = this.subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            if( subc.getHost().getIP().equals( host.getIP() ) ){
                ArrayList<ClusterVolume> cvs = subc.getDisks();
                int size1 = cvs.size();
                for( int j=0; j<size1; j++ ){
                    ClusterVolume cv = cvs.get(j);
                    if( cv.isMtppProtect() ){
                        ret.add( cv.getDiskLabel() );
                    }
                }
                break;
            }
        }
        return ret;
    }
    
    public String[] getIPFromRealHost(){
        ArrayList<SubCluster> subcList = this.getRealSubCluster();
        int size = subcList.size();
        String[] ret = new String[size];
        for( int i=0; i<size; i++ ){
            ret[i] = subcList.get(i).getHost().getIP();
        }
        return ret;
    }
    
    /**
     * @return the cluster_type
     */
    public int getCluster_type() {
        return cluster_type;
    }

    /**
     * @param cluster_type the cluster_type to set
     */
    public void setCluster_type(int cluster_type) {
        this.cluster_type = cluster_type;
    }

    public boolean isBelongedThisCluster( BootHost host ){
        int size = this.subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            if( !subc.getHost().isRealHostInCluster() ) continue;
            if( subc.getHost().getUUID().equals( host.getUUID() ) ){
                return true;
            }
        }
        return false;
    }

    public boolean isBelongedThisCluster1( BootHost host ){
        int size = this.subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            if( !subc.getHost().isRealHostInCluster() ) continue;
            if( subc.getHost().getID() == host.getID() ){
                return true;
            }
        }
        return false;
    }

    public Cluster cloneCluster(){
        Cluster newClus = new Cluster();
        newClus.setCluster_id( cluster_id );
        newClus.setCluster_name( cluster_name );
        newClus.setCluster_inited_flag(cluster_inited_flag );
        newClus.setCluster_auto_dr_flag( cluster_auto_dr_flag );
        newClus.setCluster_auto_reboot_flag( cluster_auto_reboot_flag );
        newClus.setCluster_stop_base_service( cluster_stop_base_service );
        newClus.setCluster_mac_address( cluster_mac_address );
        newClus.setCluster_boot_mode( cluster_boot_mode );
        newClus.setCluster_protect_type( cluster_protect_type );
        newClus.setCluster_type( cluster_type );
        int size = subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster one = subList.get(i);
            newClus.addSubCluster( one.cloneSubCluster() );
        }
        return newClus;
    }

    public BootHost getHostOnID( int id ){
        int size = this.subList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subList.get(i);
            if( subc.getHost().getID() == id ){
                return subc.getHost();
            }
        }
        return null;
    }
}
