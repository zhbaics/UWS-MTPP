/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.ui;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.entity.SubCluster;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.cmdp.entity.PPProfileItem;
import guisanboot.data.*;
import guisanboot.datadup.data.*;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;
import guisanboot.res.ResourceCenter;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;

/**
 *
 * @author Administrator
 */
public class DelHost extends BasicGetSomethingThread{
    protected Object selHost;
    protected Cluster cluster;
    boolean isConnect;
    
    public DelHost( SanBootView view,Object selHost,boolean isConnect,Cluster cluster ){
        super( view );

        this.selHost   = selHost;
        this.cluster = cluster;
        this.isConnect = isConnect;
    }

    protected int getHostID(){
        if( selHost instanceof BootHost ){
            return ((BootHost)selHost).getID();
        }else{
            return ((SourceAgent)selHost).getSrc_agnt_id();
        }
    }
    
    protected String getHostIP(){
        if( selHost instanceof BootHost ){
            return ((BootHost)selHost).getIP();
        }else{
            return ((SourceAgent)selHost).getSrc_agnt_ip();
        }
    }
    
    protected int getHostPort(){
        if( selHost instanceof BootHost ){
            return ((BootHost)selHost).getPort();
        }else{
            return ((SourceAgent)selHost).getSrc_agnt_port();
        }
    }

    protected String getHostName(){
        if( selHost instanceof BootHost ){
            return ((BootHost)selHost).getName();
        }else{
            return ((SourceAgent)selHost).getSrc_agnt_id()+"";
        }
    }
    
    protected BrowserTreeNode getTreeNode(){
        if( this.cluster != null ){
            return cluster.getTreeNode();
        }else{
            if( selHost instanceof BootHost ){
                return ((BootHost)selHost).getTreeNode();
            }else{
                return ((SourceAgent)selHost).getTreeNode();
            }
        }
    }
    
    protected boolean isWin(){
        if( this.cluster != null ){
            return this.cluster.isWinHost();
        }else{
            return ( selHost instanceof BootHost )? ((BootHost)selHost).isWinHost():((SourceAgent)selHost).isWinHost();
        }
    }

    protected boolean isMTPP(){
        return ( selHost instanceof BootHost )? ((BootHost)selHost).isMTPPProtect(): true;
    }
    
    protected String getHostUUID(){
        if( selHost instanceof BootHost ){
            return ((BootHost)selHost).getUUID();
        }else{
            return ((SourceAgent)selHost).getSrc_agnt_desc();
        }
    }


    private boolean delServMap(){
        if( this.cluster != null ){
            ArrayList<SubCluster> subcList = this.cluster.getSubClusterList();
            int size = subcList.size();
            for( int i=0; i<size; i++ ){
                BootHost aHost = subcList.get(i).getHost();
                if( !this.delServMapOnHost( aHost.getID() ) ){
                    return false;
                }
            }
            return true;
        }else{
            return this.delServMapOnHost( this.getHostID() );
        }
    }

    private boolean delServMapOnHost( int host_id){
        int i,size;
        ServiceMap servMap;

        Vector servList = view.initor.mdb.getAllServMapOnClntID( host_id );
        size = servList.size();
        for( i=0; i<size; i++ ){
            servMap = (ServiceMap)servList.elementAt( i );
            isOk = view.initor.mdb.delServMap( servMap.getServID() );
            if( !isOk ){
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_SERVMAP ) + " : " +
                           view.initor.mdb.getErrorMessage();
                return false;
            }else{
                view.initor.mdb.removeOneServMapFromVect( servMap );
            }
        }
        return true;
    }

    private boolean delProfileForCluster(){
        ArrayList<BackupClient> d2d_clnt_list = view.initor.mdb.getD2DMemberForCluster( this.cluster.getCluster_id() );
        int size = d2d_clnt_list.size();
        for( int i=0; i<size; i++ ){
            BackupClient d2d_clnt = d2d_clnt_list.get(i);
            if( d2d_clnt != null ){
                if( !this.delProfile( d2d_clnt ) ){ return false;}
            }
        }
        return true;
    }

    private boolean isConnected( int host_id ){
        ArrayList<SubCluster> subcList = this.cluster.getSubClusterList();
        int size = subcList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subcList.get(i);
            if( subc.getHost().getID() == host_id ){
                return subc.isConnected();
            }
        }
        return false;
    }

    private BootHost getMemberHostFromCluster( int host_id ){
        ArrayList<SubCluster> subcList = this.cluster.getSubClusterList();
        int size = subcList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subcList.get(i);
            if( subc.getHost().getID() == host_id ){
                return subc.getHost();
            }
        }
        return null;
    }

    public boolean realRun(){
        int i,size,port;
        VolumeMap volMap,tgt;
        String args;
        boolean isCon;
        String ip;

        BrowserTreeNode selHostNode = getTreeNode();
        BrowserTreeNode chiefHostNode = (BrowserTreeNode)selHostNode.getParent();

        if( !view.initor.mdb.updateMDI() ){
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_MIRROR_DISK ) + " : " +
                    view.initor.mdb.getErrorMessage();
            return false;
        }

        if( isWin() ) {
            // 0. 删除netboot host( destagent )
            if( !delNetBoot( selHostNode ) ){ return false; }
            
            // 1. 首先删除 service list
            if( !this.delServMap() ){ return false;}

            // 2. 删除profile
            if( this.cluster != null ){
                if( this.cluster.isMTPPProtect() ){
                    if( !this.delProfileForCluster() ){ return false; }
                }else{
                    if( !this.delProfileForCluster() ){ return false; }
                    ArrayList<PPProfile> ppprofList = view.initor.mdb.getAllPPProfileForCluster( this.cluster.getCluster_id() );
                    if( !delPPProfile( ppprofList ) ) { return false; }
                }
            }else{
                BackupClient bkClnt = view.initor.mdb.getBkClntOnUUID( this.getHostUUID() );
                if( this.isMTPP() ){        
                    if( bkClnt != null ){
                        if( !delProfile( bkClnt ) ){ return false; }
                    }
                }else{
                    if( bkClnt != null ){
                        if( !delProfile( bkClnt ) ){ return false;}
                    }
                    ArrayList<PPProfile> ppprofList = view.initor.mdb.getPPProfile( this.getHostID() );
                    if( !delPPProfile( ppprofList ) ) { return false; }
                }
            }
            
            // 3. 删除config
            if( this.cluster != null ){
                if( !this.delConfigFileForCluster() ){ return false; }
            }else{
                if( !delConfigFile( this.getHostID() ) ){ return false; }
            }
            
            // 4. 删除启动配置( 目前不考虑删除dhcp和iboot的配置信息 )
            delIbootConfForWin();

            // 5. 删除 volume map
            Vector volList;
            if( this.cluster != null ){
                volList = view.initor.mdb.getVolFromCluster1( this.cluster.getCluster_id() ); 
            }else{
                volList = view.initor.mdb.getVolMapOnClntID( this.getHostID() );
            }
            size = volList.size();
            for( i=0; i<size; i++ ){
                volMap = (VolumeMap)volList.elementAt(i);
                
                if( this.cluster != null ){
                    BootHost aHost = getMemberHostFromCluster( volMap.getVolClntID() );
                    if( aHost == null ) continue;
                    ip = aHost.getIP();
                    port = aHost.getPort();
                }else{
                    ip = this.getHostIP();
                    port = this.getHostPort();
                }
                
                if( this.cluster != null ){
                    isCon = isConnected( volMap.getVolClntID() );
                }else{
                    isCon = this.isConnect;
                }
                // 如果可以连接上该client端，则删除client端上的mirrorring
                if( volMap.isCMDPProtect() && isCon ){
                    if( !view.initor.mdb.delMirrorOnClnt( ip, port, volMap.getVolDiskLabel().substring(0,1) ) ){
                        errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.delMirror") +
                              view.initor.mdb.getErrorMessage();
                        return false;
                    }
                }

                // 先把mg自动创建快照的参数清除，使之不能自动创建快照；然后如果不存在mj，就把对应的mg给stop掉
                if( volMap.isCMDPProtect() ){
                    if( !this.stop_mg( volMap ) ) return false;
                }
                
                // 删除disk上的lunmap
                if( !delLunMapOnVol( ip,port,volMap ) ){ return false; }

                // 删除disk上所有快照的view的lunmap
                if( !delLunMapOnView( volMap ) ) { return false; }

                // 删除clonedisk及其view上的lunmap
                if( this.cluster != null ){
                    if( !this.delLunMapOnCloneDisk( volMap.getVolClntID(),volMap.getVol_rootid() ) ){ return false; }
                }else{
                    if( !this.delLunMapOnCloneDisk( this.getHostID(),volMap.getVol_rootid() ) ){ return false; }
                }
                
                // 修改UIMVol的属性，使之归结到空闲卷上
                if( !this.modUIMVolOnVolMap( volMap.getVolClntID(),volMap.getVol_rootid(),1 ) ){ return false;}

                // 修改clonedisk的属性，使之归结到空闲卷上
                if( !this.modCloneDiskOnVolMap( volMap.getVol_rootid(), volMap.getVolClntID(), 1 ) ){ return false;}

                isOk = view.initor.mdb.delVolumeMap( volMap ); 
                if( !isOk ){
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_VOLMAP ) + " : " +
                              view.initor.mdb.getErrorMessage();
                    return false;
                }else{
                    // 删除volmap对应卷的lunmap
                    view.initor.mdb.removeOneVolMapFromVec( volMap );
                }
            }
            
            if( this.cluster != null ){
                if( this.cluster.isCMDPProtect() ){
                    // 不管结果（因为也许某些机器已经不在了）
                    // 为了测试，临时注释掉 2011.8.24 zjj
                    //delPersistentTargetsForCluster();
                }
            }else{
                if( !this.isMTPP() ){
                    // 不管结果（因为也许这台机器已经不在了）
                    // 为了测试，临时注释掉 2011.8.24 zjj
                   // this.delPersistentTargets( this.getHostIP(),this.getHostPort() );
                    System.out.println("----") ;
                }
            }
            
            if( this.cluster != null ) {
                isOk = clearClusterInfo();
                if( !isOk ){
                    errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.delMirrorInfo") +
                              view.initor.mdb.getErrorMessage();
                    return false;
                }
            }
            
            // 6. 删除host
            return realDelHost( chiefHostNode, selHostNode );
        }else{ // del unix host

            // 0. 删除netboot host
            if( !delNetBoot( selHostNode ) ){ return false; }

            // 1. 删除profile
            if( this.cluster != null ){
                if( !delProfileForCluster() ){ return false; }
            }else{
                BackupClient bkClnt = view.initor.mdb.getBkClntOnUUID( this.getHostUUID() );
                if( !delProfile( bkClnt ) ){ return false; }
            }

            // 2. 删除配置
            if( this.cluster != null ){
                if( !this.delConfigFileForCluster() ){ return false; }
            }else{
                if( !delConfigFile( this.getHostID() ) ){ return false; }
            }
            
            // 3. del vg volumeMap
            if( !delVGVolMap() ){ return false; }

            // 4. del lv volMap
            if( !delLVVolMap() ){ return false; }

            // 5. 删除 tgt volume map
            String tgtSrvName = view.initor.mdb.getHostName();
            if( tgtSrvName.equals("") ) return false;

            String iscsiVar = ResourceCenter.ISCSI_PREFIX + tgtSrvName;
            Vector tgtList = view.initor.mdb.getTgtListOnClntID( this.getHostID() );
            size = tgtList.size();
            if( isConnect ){
                /**
                 * 删除保护时，回退相关ams操作，如：重命名，重新挂载原盘
                 */
                if ( !this.isMTPP() ){
                    args = "";
                    isOk = view.initor.mdb.delAmsProtect( this.getHostIP(),this.getHostPort(),args );
                    if( isOk ){
                        errMsg = "delete host fail about ams of :" + view.initor.mdb.getErrorMessage();
                        SanBootView.log.error( getClass().getName(), errMsg);
                    }
                }

                for( i=0; i<size; i++ ){
                    tgt = (VolumeMap)tgtList.elementAt(i);
                    args = " -I "+iscsiVar +" -S "+ view.initor.getTxIP(this.getHostIP()) + " -P "+ ResourceCenter.ISCSI_LOGIN_PORT + " -t "+tgt.getVolTargetID();        

                    isOk = view.initor.mdb.loginUnixTarget( this.getHostIP(),this.getHostPort(),args );
                    if( isOk ){
                        args = " -g " + tgt.getVolDiskLabel();
                        // delete real vg
                        isOk = view.initor.mdb.delVg( this.getHostIP(), this.getHostPort(), args );
                        if( !isOk ){
                            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_VG ) + " : " +
                                      view.initor.mdb.getErrorMessage(); 
                            return false; 
                        }else{
                            // 将target logout出去
                            args = " -I "+iscsiVar +" -S "+ view.initor.getTxIP(this.getHostIP()) + " -P "+ ResourceCenter.ISCSI_LOGIN_PORT + " -t "+tgt.getVolTargetID(); 
                            isOk = view.initor.mdb.logoutUnixTarget( this.getHostIP(),this.getHostPort(),args );
                            if( !isOk ){
                                errMsg =  ResourceCenter.getCmdString( ResourceCenter.CMD_LOGOUTTARGET ) + " : " +
                                          view.initor.mdb.getErrorMessage();
                                return false; 
                            }

                            // del lunmap on tgt
                            if( !delLunMapOnVol( this.getHostIP(),this.getHostPort(),tgt ) ){ return false; }

                            // 删除disk上所有快照的view的lunmap
                            if( !delLunMapOnView( tgt ) ){ return false; }

                            // 删除clonedisk及其view上的lunmap
                            if( this.cluster != null ){
                                if( !delLunMapOnCloneDisk( tgt.getVolClntID(),tgt.getVol_rootid() ) ){ return false;}
                            }else{
                                if( !delLunMapOnCloneDisk( this.getHostID(),tgt.getVol_rootid() ) ){ return false;}
                            }

                            // 修改UIMVol的属性，使之归结到空闲卷上
                            if( !this.modUIMVolOnVolMap(tgt.getVolClntID(),tgt.getVol_rootid(),1 ) ){ return false;}

                            // 修改clonedisk的属性，使之归结到空闲卷上
                            if( !this.modCloneDiskOnVolMap(tgt.getVol_rootid(), tgt.getVolClntID(), 1) ){ return false;}

                            // del tgt volumemap object
                            isOk = view.initor.mdb.delVolumeMap( tgt );
                            if( !isOk ){
                                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_VOLMAP ) + "(TGT) : " +
                                          view.initor.mdb.getErrorMessage(); 
                                return false;
                            }else{
                                view.initor.mdb.removeOneVolMapFromVec( tgt );
                            }
                        }
                    }else{
                        errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_LOGINTARGET ) + " : " +
                                view.initor.mdb.getErrorMessage(); 
                        return false; 
                    }
                }
            }else{ // not connect
                for( i=0; i<size; i++ ){
                    tgt = (VolumeMap)tgtList.elementAt(i);

                    // del lunmap on tgt
                    if( !delLunMapOnVol( this.getHostIP(),this.getHostPort(),tgt ) ){ return false; }

                    // 删除disk上所有快照的view的lunmap
                    if( !delLunMapOnView( tgt ) ){ return false; }

                    // 删除clonedisk及其view上的lunmap
                    if( this.cluster != null ){
                        if( !delLunMapOnCloneDisk( tgt.getVolClntID(),tgt.getVol_rootid() ) ){ return false; }
                    }else{
                        if( !delLunMapOnCloneDisk( this.getHostID(),tgt.getVol_rootid() ) ){ return false; }
                    }

                    // 修改UIMVol的属性，使之归结到空闲卷上
                    if( !this.modUIMVolOnVolMap( tgt.getVolClntID(),tgt.getVol_rootid(),1 ) ){ return false;}

                    // 修改clonedisk的属性，使之归结到空闲卷上
                    if( !this.modCloneDiskOnVolMap(tgt.getVol_rootid(), tgt.getVolClntID(), 1) ){ return false;}

                    // del tgt volumemap object
                    isOk = view.initor.mdb.delVolumeMap( tgt );
                    if( !isOk ){
                        errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_VOLMAP ) + "(TGT) : " +
                                view.initor.mdb.getErrorMessage();
                        return false;
                    }else{
                        view.initor.mdb.removeOneVolMapFromVec( tgt );
                    }
                }
            }

            // 6. 最后删除 host
            return realDelHost( chiefHostNode, selHostNode );
        }
    }
    
    private boolean stop_mg( VolumeMap vol ){
        boolean hasActiveMj = false;
        ArrayList<MirrorJob> mjList = new ArrayList<MirrorJob>(0);
        
        // 只检查跟mg相关的mj(无限增量mj不在考察之列)
        MirrorGrp mg = view.initor.mdb.getMGFromVectorOnRootID( vol.getVol_rootid() );
        if( mg != null ){
            // 清除掉mg上有关自动生成快照的调度,并把mg_src_ip和mg_src_disk_uuid清空,
            // 否则干扰下次对该机器的初始化
            if( !view.initor.mdb.modAutoCrtSnapParameter2( mg.getMg_id(), MirrorGrp.MG_SCH_TYPE_ROTATE,0 ) ){
                return false;
            }
            mjList = view.initor.mdb.getMjListFromVecOnMgID( mg.getMg_id() );
        }

        int size = mjList.size();
        for( int m=0; m<size; m++ ){
            MirrorJob mj = (MirrorJob)mjList.get( m );
            if( mj.isMJStart() ){
                hasActiveMj = true;
                break;
            }
        }

        if( hasActiveMj ){
SanBootView.log.info(getClass().getName(),"have active mjs on this mg,so can't stop this mg.");
            return true;
        }else{
            if( mg != null ){
                boolean ok = view.initor.mdb.stopMg( mg.getMg_id() );
                if( !ok ){
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_STOP_MG ) + " : "+
                              view.initor.mdb.getErrorMessage();
                }
                return ok;
            }else{
                return true;
            }
        }
    }

    private boolean delLunMapOnVol( String ip,int port,VolumeMap tgt ){
        LunMap lm;
        
        String iscsiVar = "";
        if( this.isWin() && tgt.isCMDPProtect() ){
            String tgtSrvName = view.initor.mdb.getHostName();
            if( tgtSrvName.equals("") ) return false;
            iscsiVar = ResourceCenter.ISCSI_PREFIX + tgtSrvName + ":" + tgt.getVolTargetID();
        }

        if( this.isWin() && tgt.isCMDPProtect() ){
            // 先把target logout出去,不管是否成功。反正下面还会del lunmap。之所以要先试图logout,
            // 是因为clw说直接del lunmap会对操作系统有影响
            view.initor.mdb.logoutTarget( ip, port, iscsiVar, tgt.getVolDiskLabel().substring( 0,1 ), ResourceCenter.CMD_TYPE_CMDP );
        }

        boolean isOK = view.initor.mdb.getLunMapForTID( tgt.getVolTargetID() );
        if( isOK ){
            Vector lmList = view.initor.mdb.getAllLunMapForTid();
            int size1 = lmList.size();
            for( int j=0;j<size1;j++ ){
                lm = (LunMap)lmList.elementAt(j);
                isOK = view.initor.mdb.delLunMap( tgt.getVolTargetID(), lm.getIP(),lm.getMask(), lm.getAccessMode() );
                if( !isOK ){
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_LUNMAP) + " "+ tgt.getVolTargetID() +" "+lm.getIP() +" "+lm.getMask() +" "+lm.getAccessMode() +" "+
                              view.initor.mdb.getErrorMessage(); 
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_LUNMAP) + " [ " +tgt.getVolName() + " ] "+
                      view.initor.mdb.getErrorMessage(); 
            return false;
        }

        return true;
    }

    private boolean delLunMapOnView( VolumeMap tgt ){
        int k,j,size2,size1;
        Vector lmList;
        LunMap lm;

        boolean isOK = view.initor.mdb.getAllView( tgt.getVol_rootid() );
        if( isOK ){
            ArrayList viewList = view.initor.mdb.getViewList();
            size2 = viewList.size();
            for( k=0; k<size2; k++ ){
                View viewObj = (View)viewList.get( k );
                isOK = view.initor.mdb.getLunMapForTID( viewObj.getSnap_target_id() ); 
                if( isOK ){
                    lmList = view.initor.mdb.getAllLunMapForTid();
                    size1 = lmList.size();
                    for( j=0;j<size1;j++ ){
                        lm = (LunMap)lmList.elementAt(j);
                        isOK = view.initor.mdb.delLunMap( viewObj.getSnap_target_id(), lm.getIP(),lm.getMask(), lm.getAccessMode() );
                        if( !isOK ){
                            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_LUNMAP) + " "+ viewObj.getSnap_target_id() +" "+lm.getIP() +" "+lm.getMask() +" "+lm.getAccessMode() +" "+
                                    view.initor.mdb.getErrorMessage();
                            return false;
                        }
                    }
                }else{
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_LUNMAP) + " [ " +viewObj.getSnap_target_id() + " ] "+
                            view.initor.mdb.getErrorMessage();
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_VIEW) + " "+
                    view.initor.mdb.getErrorMessage();
            return false;
        }

        return true;
    }

    private boolean delVGVolMap(){
        VolumeMap vg;
        boolean isOK;

        Vector vgList = view.initor.mdb.getVgListOnClntID( this.getHostID() );
        int size = vgList.size();
        for( int i=0; i<size; i++ ){
            vg = (VolumeMap)vgList.elementAt(i);
            isOK = view.initor.mdb.delVolumeMap( vg );
            if( !isOK ){
                errMsg =  ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_VOLMAP ) + "(VG) : " +
                        view.initor.mdb.getErrorMessage();
                return false;
            }else{
                view.initor.mdb.removeOneVolMapFromVec( vg );
            }
        }

        return true;
    }

    private boolean delLVVolMap(){
        VolumeMap lv;
        boolean ok;

        Vector lvList = view.initor.mdb.getLVListOnClntID( this.getHostID() );
        int size = lvList.size();
        for( int i=0; i<size; i++ ){
            lv = (VolumeMap)lvList.elementAt(i);
            ok = view.initor.mdb.delVolumeMap( lv );
            if( !ok ){
                errMsg =  ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_VOLMAP ) + "(LV) : " +
                        view.initor.mdb.getErrorMessage();
                return false;
            }else{
                view.initor.mdb.removeOneVolMapFromVec( lv );
            }
        }

        return true;
    }
    
    private void removeClusterIncludingMemberHostInCache(){
        ArrayList<SubCluster> subcList = this.cluster.getSubClusterList();
        int size = subcList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subcList.get(i);
            BootHost host = subc.getHost();
            view.initor.mdb.removeBootHostFromVector( host );
        }
        view.initor.mdb.removeClusterFromVector( this.cluster );
    }
    
    private boolean delClusterIncludingMemberHost(){
        ArrayList<SubCluster> subcList = this.cluster.getSubClusterList();
        int size = subcList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subcList.get(i);
            if( !view.initor.mdb.delBootHost( subc.getHost().getID() ) ){ return false; }
        }
        return view.initor.mdb.delCluster( this.cluster );
    }
    
    private boolean clearClusterInfo(){
        ArrayList<SubCluster> subcList = this.cluster.getRealSubCluster();
        int size = subcList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subcList.get(i);
            if( !view.initor.mdb.delMirrorCluster( subc.getHost().getIP(),subc.getHost().getPort() ) ){ return false; }
        }
        return true;
    }
    
    private boolean realDelHost( BrowserTreeNode chiefHostNode,BrowserTreeNode selHostNode ){
        boolean isOK;
        Audit audit;

        if( cluster != null ){
            audit = view.audit.registerAuditRecord( this.cluster.getCluster_id(), MenuAndBtnCenterForMainUi.FUNC_DEL_CLUSTER );
        }else{
            audit = view.audit.registerAuditRecord( this.getHostID(), MenuAndBtnCenterForMainUi.FUNC_DEL_HOST );
        }

        if( this.cluster != null ){
            isOK = this.delClusterIncludingMemberHost();
        }else{
            isOK = view.initor.mdb.delBootHost( this.getHostID() );
        }
        if( isOK ){
            if( this.cluster != null ){
                audit.setEventDesc( "Delete cluster: [ " + this.cluster.getCluster_name() + " ] successfully." );
            }else{
                audit.setEventDesc( "Delete host: [ " + this.getHostName() + "," + this.getHostIP() + " ] successfully." );
            }
            view.audit.addAuditRecord( audit );

            if( this.cluster != null ){
                removeClusterIncludingMemberHostInCache();
            }else{
                BootHost host = (BootHost)selHost;
                view.initor.mdb.removeBootHostFromVector( host );
            }
            view.removeNodeFromTree( chiefHostNode, selHostNode );
            
            // 显示点击 chiefHostNode 后的右边tabpane中的内容
            view.setCurNode( chiefHostNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefHost peOnChiefHost = new ProcessEventOnChiefHost( view );
            TreePath path = new TreePath( chiefHostNode.getPath() );
            peOnChiefHost.processTreeSelection( path );
            peOnChiefHost.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }else{
            if( this.cluster != null ){
                audit.setEventDesc( "Failed to delete cluster: [ " + this.cluster.getCluster_name() + " ]" );
            }else{
                audit.setEventDesc( "Failed to delete host: [ " + this.getHostName() + "," + this.getHostIP() +" ]" );
            }
            view.audit.addAuditRecord( audit );

            if( this.cluster != null ){
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_CLUSTER )+" : "+
                    view.initor.mdb.getErrorMessage();
            }else{
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_BOOT_HOST )+" : "+
                    view.initor.mdb.getErrorMessage();
            }
        }

        return isOk;
    }

    private boolean delProfile( BackupClient bkClnt ){
        UniProfile prof;
        int i,j,size,size1;
        String bkobjId;
        BakObject bkObj;
        ArrayList schList;
        DBSchedule sch;

        //BackupClient bkClnt = view.initor.mdb.getBkClntOnUUID( this.getHostUUID() );
        if( bkClnt != null ) {
            ArrayList profList = view.initor.mdb.getAllProfileOnClntID( bkClnt.getID() ); 
            size = profList.size();     
            for( i=0; i<size; i++ ){
                prof = (UniProfile)profList.get( i );

                bkobjId = prof.getUniProIdentity().getBkObj_ID();
                bkObj = view.initor.mdb.getBakObjFromVector( bkobjId );
                if( bkObj != null ){
                    if( !view.initor.mdb.deleteBakObj( bkObj.getBakObjID() ) ){
                        errMsg =  ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_BAKOBJECT )+
                                    ": "+
                                  view.initor.mdb.getErrorMessage();
                        return false;
                    }else{
                        // 这里不从mdb中删除bkobj，从而在下面的delObj中再删除一次，
                        // 因为有时这里的删除并不能真正从sqllite数据库中删除。这样做
                        // 就有两次机会来删除bkobj，从而增加了成功的几率。 2011.5.22 zjj
                        //view.initor.mdb.removeBakObjFromVector( bkObj );
                    }
                }

                schList = view.initor.mdb.getSchOnProfName( prof.getProfileName() );
                size1 = schList.size();
                for( j=0; j<size1; j++ ){
                    sch = (DBSchedule)schList.get( j );
                    if( !view.initor.mdb.deleteOneScheduler( sch ) ){
                        errMsg =  ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_DB_SCHEDULER ) +
                                    ": "+
                                    view.initor.mdb.getErrorMessage();
                        return false;
                    }else{
                        view.initor.mdb.removeSch( sch );
                    }
                }

                if( !view.initor.mdb.delFile( prof.getProfileName()  ) ){
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_FILE ) +
                                ": "+
                            view.initor.mdb.getErrorMessage(); 
                    return false;
                }else{
                    view.initor.mdb.removeProfFromCache( prof );
                }
            }

            // 删除不属于profile中的bkobj(孤立的bkobj)
            // 会再次删除上面已经删过的bkobj(因为有时上面的删除为“假删除”) 2011.5.22
            ArrayList bkobjList = view.initor.mdb.getBakObjOnClient( bkClnt.getID() );
            size = bkobjList.size();
            for( i=0; i<size; i++ ){
                bkObj = (BakObject)bkobjList.get(i);
                if( !view.initor.mdb.deleteBakObj( bkObj.getBakObjID() ) ){
                    errMsg =  ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_BAKOBJECT )+
                                ": "+
                              view.initor.mdb.getErrorMessage();
                    return false;
                }else{
                    view.initor.mdb.removeBakObjFromVector( bkObj );
                }
            }

            // 删除d2d client
            if( !view.initor.mdb.delClient(  bkClnt ) ){
                errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_CLIENT ) +
                            ": "+
                        view.initor.mdb.getErrorMessage(); 
                return false;
            }else{
                view.initor.mdb.removeClientFromVector( bkClnt );
            }
        }

        return true;
    }

    private boolean delPPProfile( ArrayList<PPProfile> ppprofList ){
        int size = ppprofList.size();
        for( int i=0; i<size; i++ ){
            PPProfile prof = ppprofList.get(i);
            if( prof.isValidDriveGrp() ){
                PPProfileItem master_disk = prof.getMainDiskItem();
                if( !view.initor.mdb.delDG( prof.getDriveGrpName(), master_disk.getVolMap().getVol_rootid()+"",master_disk.getVolMap().getGroupinfodetail() ) ){
                    errMsg =  SanBootView.res.getString("MenuAndBtnCenter.error.delPPProf")+
                              view.initor.mdb.getErrorMessage();
                    return false;
                }
            }

            // 不能随便删除mg，在delPPProfile后面将有mg的处理。（2010.12.17）
/*
            ArrayList<PPProfileItem> items = prof.getElements();
            int size1 = items.size();
            for( int j=0; j<size1; j++ ){
                PPProfileItem item = items.get( j );
                if( !view.initor.mdb.delMg( item.getMg().getMg_id() ) ){
                    errMsg =  ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_MG ) +
                               ": "+
                              view.initor.mdb.getErrorMessage();
                    return false;
                }
            }
 */
        }
        return true;
    }

    private String getErrorMsgForDelFile( String conf ){
        return ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_FILE ) +"[ "+conf+" ]" + " : " +
                  view.initor.mdb.getErrorMessage(); 
    }

    private boolean delConfigFileForCluster1( int cluster_id ){
        String[] commonConf = new String[]{
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_IP,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_NETBOOT_DISK,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_OLDDISK,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_RSTMAP,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_3RD_DHCP,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_HeartBeat_Disk
        };
        String[] winConf = new String[]{
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_NORMAL_DISK,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_SERVICE
        };
        String[] unixConf = new String[]{
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_MP,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_LVMINFO,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_OS_LOADER,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_FSTAB,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_NETWORK_CMD,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_KILLALL_CMD,
            ResourceCenter.CLT_IP_CONF + "/cluster-" + cluster_id + ResourceCenter.CONF_HALT_CMD
        };

        // 删除公共的配置
        for( int i=0; i<commonConf.length; i++ ){
            isOk = view.initor.mdb.delFile( commonConf[i] );
            if( !isOk ){
                errMsg = getErrorMsgForDelFile( commonConf[i] );
                return false;
            }
        }

        if( this.isWin() ){
            // 删除windows配置
            for( int i=0; i<winConf.length; i++ ){
                isOk = view.initor.mdb.delFile( winConf[i] );
                if( !isOk ){
                    errMsg = getErrorMsgForDelFile( winConf[i] );
                    return false;
                }
            }
        }else{
            for( int i=0; i<unixConf.length; i++ ){
                isOk = view.initor.mdb.delFile( unixConf[i] );
                if( !isOk ){
                    errMsg = getErrorMsgForDelFile( unixConf[i] );
                    return false;
                }
            }

            // del remir ( to be continued.....)
        }

        return true;
    }

    private boolean delConfigFileForCluster(){
        ArrayList<SubCluster> subcList = cluster.getSubClusterList();
        int size = subcList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subcList.get(i);
            BootHost aHost = subc.getHost();
            if( !this.delConfigFile( aHost.getID() ) ) { return false; }
        }
        return this.delConfigFileForCluster1( this.cluster.getCluster_id() );
    }

    private boolean delConfigFile( int host_id ){
        String[] commonConf = new String[]{
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_IP,
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_NETBOOT_DISK,
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_OLDDISK,
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_RSTMAP,
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_3RD_DHCP
        };
        String[] winConf = new String[]{
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_NORMAL_DISK,
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_SERVICE
        };
        String[] unixConf = new String[]{
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_MP,
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_LVMINFO,
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_OS_LOADER,
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_FSTAB,
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_NETWORK_CMD,
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_KILLALL_CMD,
            ResourceCenter.CLT_IP_CONF + "/" + host_id + ResourceCenter.CONF_HALT_CMD                
        };
        
        // 删除公共的配置
        for( int i=0; i<commonConf.length; i++ ){
            isOk = view.initor.mdb.delFile( commonConf[i] );
            if( !isOk ){
                errMsg = getErrorMsgForDelFile( commonConf[i] ); 
                return false;
            }
        }

        if( this.isWin() ){
            // 删除windows配置
            for( int i=0; i<winConf.length; i++ ){
                isOk = view.initor.mdb.delFile( winConf[i] );
                if( !isOk ){
                    errMsg = getErrorMsgForDelFile( winConf[i] );
                    return false;
                }
            }
        }else{
            for( int i=0; i<unixConf.length; i++ ){
                isOk = view.initor.mdb.delFile( unixConf[i] );
                if( !isOk ){
                    errMsg = getErrorMsgForDelFile( unixConf[i] );
                    return false;
                }
            }

            // del remir ( to be continued.....)
        }

        return true;
    }
    
    private void delIbootConfForWin(  ){
        String oldmac = "";
        if( this.cluster != null ){
            oldmac = cluster.getCluster_mac_address();
        }else{
            if( selHost instanceof BootHost ){
                BootHost host = (BootHost)selHost;
                oldmac = host.getBootMac();
            }
        }

        if( !oldmac.equals("") ){
            String simpleMac = NetCard.getSimpleMac( oldmac );
            view.initor.mdb.delIboot( simpleMac );
        }
    }

    protected boolean delNetBoot( BrowserTreeNode selHostNode ){
        int i,j,size,size1,da_id;
        DestAgent da;
        SnapUsage su;
        boolean isOK;

        ArrayList daList = view.getNetbootedHostOnHost( selHostNode ); 
        size = daList.size();
SanBootView.log.debug(getClass().getName(), "da size: "+ size );
        for( i=0; i<size; i++ ){
            da = (DestAgent) daList.get(i);
SanBootView.log.debug(getClass().getName(), "da ip: "+ da.getDst_agent_ip() );
            da_id = da.getDst_agent_id();

            isOK = view.delConfigFileUsingByNetBootHost( da_id );
            if( !isOK ){
                errMsg = view.getErrMsg();
                return false;
            }

            ArrayList list = view.initor.mdb.getMSUFromCacheOnDstAgntID( da_id );
            size1 = list.size();
            for( j=0; j<size1; j++ ){
                su = (SnapUsage)list.get(j);
                isOK = view.initor.mdb.delMSU( su.getUsage_id() );
                if( !isOK ){
                    errMsg = view.initor.mdb.getErrorMessage();
                    return false;
                }else{
                    view.initor.mdb.removeMSUFromCache( su );
                }
            }

            isOK = view.initor.mdb.delNBH( da_id );
            if( !isOK ){
                errMsg = view.initor.mdb.getErrorMessage();
                return false;
            }else{
                if( da.isWinHost() ){
                    // delete iboot config info
                    view.initor.mdb.delIboot( da.getDst_agent_mac() );
                }
                view.initor.mdb.removeNBHFromCache( da );
            }
        }
        
        return true;
    }
    
    private boolean delLunMapOnCloneDisk( int host_id,int rootid ){
        CloneDisk cd;

        boolean aIsOk = view.initor.mdb.getCloneDiskList( host_id ,CloneDisk.IS_BOOTHOST,rootid );
        if( aIsOk ){
            ArrayList list = view.initor.mdb.getCloneDiskList();
            int size = list.size();
            for( int i=0; i<size; i++ ){
                cd = (CloneDisk)list.get(i);
                this.delLunMapOnMDI( cd.getTarget_id() );
                this.delLunMapOnView( cd.getRoot_id() );

                // 修改clonedisk的属性，使之归于空闲盘的范畴
                if( !view.initor.mdb.modCloneDisk("",0,0,"",cd.getId(),-1,CloneDisk.IS_FREEVOL ) ){
                    errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_GET_CLONE_DISK ) +" : " + view.initor.mdb.getErrorMessage();
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_GET_CLONE_DISK ) +" : " + view.initor.mdb.getErrorMessage();
        }

        return aIsOk;
    }

    private boolean delLunMapOnMDI( int tid ){
        LunMap lm;

        boolean isOK = view.initor.mdb.getLunMapForTID( tid );
        if( isOK ){
            Vector lmList = view.initor.mdb.getAllLunMapForTid();
            int size1 = lmList.size();
            for( int j=0;j<size1;j++ ){
                lm = (LunMap)lmList.elementAt(j);
                isOK = view.initor.mdb.delLunMap( tid, lm.getIP(),lm.getMask(), lm.getAccessMode() );
                if( !isOK ){
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_LUNMAP) + " "+ tid +" "+lm.getIP() +" "+lm.getMask() +" "+lm.getAccessMode() +" "+
                              view.initor.mdb.getErrorMessage();
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_LUNMAP) + " [ tid:" + tid + " ] "+
                      view.initor.mdb.getErrorMessage();
            return false;
        }

        return true;
    }

    private boolean delLunMapOnView( int rootid ){
        int k,j,size2,size1;
        Vector lmList;
        LunMap lm;

        boolean isOK = view.initor.mdb.getAllView( rootid );
        if( isOK ){
            ArrayList viewList = view.initor.mdb.getViewList();
            size2 = viewList.size();
            for( k=0; k<size2; k++ ){
                View viewObj = (View)viewList.get( k );
                isOK = view.initor.mdb.getLunMapForTID( viewObj.getSnap_target_id() );
                if( isOK ){
                    lmList = view.initor.mdb.getAllLunMapForTid();
                    size1 = lmList.size();
                    for( j=0;j<size1;j++ ){
                        lm = (LunMap)lmList.elementAt(j);
                        isOK = view.initor.mdb.delLunMap( viewObj.getSnap_target_id(), lm.getIP(),lm.getMask(), lm.getAccessMode() );
                        if( !isOK ){
                            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_LUNMAP) + " "+ viewObj.getSnap_target_id() +" "+lm.getIP() +" "+lm.getMask() +" "+lm.getAccessMode() +" "+
                                    view.initor.mdb.getErrorMessage();
                            return false;
                        }
                    }
                }else{
                    errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_LUNMAP) + " [ " +viewObj.getSnap_target_id() + " ] "+
                            view.initor.mdb.getErrorMessage();
                    return false;
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_GET_VIEW) + " "+
                    view.initor.mdb.getErrorMessage();
            return false;
        }

        return true;
    }

    private boolean modCloneDiskOnVolMap( int rootid,int clntID,int mode ){
        CloneDisk cd;
        boolean aIsOk;

        if( mode == 0 ){
            aIsOk = view.initor.mdb.getCloneDiskList( -1,CloneDisk.IS_FREEVOL,rootid );
        }else{
            aIsOk = view.initor.mdb.getCloneDiskList( clntID,CloneDisk.IS_BOOTHOST,rootid );
        }
        if( aIsOk ){
            ArrayList list = view.initor.mdb.getCloneDiskList();
            int size = list.size();
            for( int i=0; i<size; i++ ){
                cd = (CloneDisk)list.get(i);

                if( mode == 0 ){
                    // 修改clonedisk的属性，使之归于boothost的范畴
                    if( !view.initor.mdb.modCloneDisk( "",0,0,"",cd.getId(),clntID,CloneDisk.IS_BOOTHOST ) ){
                        errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_MOD_CLONE_DISK ) +" : " + view.initor.mdb.getErrorMessage();
                        return false;
                    }
                }else{
                    // 修改clonedisk的属性，使之归于空闲卷的范畴
                    if( !view.initor.mdb.modCloneDisk( "",0,0,"",cd.getId(),-1,CloneDisk.IS_FREEVOL) ){
                        errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_MOD_CLONE_DISK ) +" : " + view.initor.mdb.getErrorMessage();
                        return false;
                    }
                }
            }
        }else{
            errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_GET_CLONE_DISK ) +" : " + view.initor.mdb.getErrorMessage();
        }

        return aIsOk;
    }

    private boolean modUIMVolOnVolMap( int cltID,int rootid,int mode ){
        ArrayList list;

        if( mode == 0 ){
            list = view.initor.mdb.getMDIFromCacheOnHostIDandRootID( -1,rootid );
        }else{
            list = view.initor.mdb.getMDIFromCacheOnHostIDandRootID( cltID,rootid );
        }
        int size = list.size();
        for( int i=0; i<size; i++ ){
            MirrorDiskInfo mdi = (MirrorDiskInfo)list.get( i );

            if( mode == 0 ){
                // 修改uim-vol的属性，使之归于boothost的范畴
                if( !view.initor.mdb.modMDI( "",0,0,"",mdi.getSnap_rootid(),cltID ) ){
                    errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_MOD_MIRROR_DISK ) +" : " + view.initor.mdb.getErrorMessage();
                    return false;
                }
            }else{
                // 修改uim-vol的属性，使之归于空闲卷的范畴
                if( !view.initor.mdb.modMDI( "",0,0,"",mdi.getSnap_rootid(),-1 ) ){
                    errMsg = ResourceCenter.getCmdString(  ResourceCenter.CMD_MOD_MIRROR_DISK ) +" : " + view.initor.mdb.getErrorMessage();
                    return false;
                }
            }
        }

        return true;
    }

    private boolean delPersistentTargetsForCluster(){
        ArrayList<SubCluster> subcList = this.cluster.getSubClusterList();
        int size = subcList.size();
        for( int i=0; i<size; i++ ){
            SubCluster subc = subcList.get(i);
            BootHost host = subc.getHost();
            if( !this.delPersistentTargets( host.getIP(),host.getPort() ) ) { return false; }
        }
        return true;
    }

    private boolean delPersistentTargets( String ip,int port ){
        String tgtSrvName = view.initor.mdb.getHostName();
        if( tgtSrvName.equals("") ) return false;

        boolean ok = view.initor.mdb.getPersistentTarget( ip, port,ResourceCenter.CMD_TYPE_CMDP );
        if( !ok ) return false;

        ArrayList persistentTargetList = view.initor.mdb.getPersistentTgtList();
        int size = persistentTargetList.size();
        for( int i=0; i<size; i++ ){
            Integer perInt = (Integer)persistentTargetList.get(i);
            String persistent = ResourceCenter.ISCSI_PREFIX + tgtSrvName + ":" +perInt.intValue();
            ok = view.initor.mdb.delPersistentTarget( ip,port,persistent,ResourceCenter.CMD_TYPE_CMDP );
            if( !isOk ) return false;
        }
        return true;
    }
}
