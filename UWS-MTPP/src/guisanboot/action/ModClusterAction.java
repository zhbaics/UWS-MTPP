/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.entity.SubCluster;
import guisanboot.cluster.ui.ModifyClusterDialog;
import guisanboot.data.BindofVolAndSnap;
import guisanboot.data.BootHost;
import guisanboot.data.MirrorDiskInfoWrapper;
import guisanboot.data.ViewWrapper;
import guisanboot.data.VolumeMap;
import guisanboot.data.VolumeMapWrapper;
import guisanboot.datadup.data.BackupClient;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.GetHostInfoThread;
import guisanboot.ui.GetRstVersion;
import guisanboot.ui.ProcessEventOnChiefHost;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class ModClusterAction extends GeneralActionForMainUi{
    public ModClusterAction(){
        super(
            ResourceCenter.SMALL_MOD_HOST,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modCluster",
            MenuAndBtnCenterForMainUi.FUNC_MOD_CLUSTER
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering modify cluster action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null || !(selObj instanceof Cluster) ) return;

        try{
            Cluster selCluster = (Cluster)selObj;
            Cluster cloneCluster = selCluster.cloneCluster();
            BrowserTreeNode selClusterNode = selCluster.getTreeNode();
            BrowserTreeNode chiefHostNode = (BrowserTreeNode)selClusterNode.getParent();

            // 检查可否修改该主机

            ModifyClusterDialog dialog = new ModifyClusterDialog( view,cloneCluster );
            int width  = 540+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 345+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );

            Object[] ret  = dialog.getValue();
            if( ret == null || ret.length <= 0 ) return;

            // 只修改真实物理主机的信息，对于共享卷的虚拟主机的信息要在初始化向导中进行（因为需要“卷访问路径”来决定
            // 虚拟主机的ip情况） 2011.8.8 17:31
            Cluster newCluster = (Cluster)ret[0];
            ArrayList<SubCluster> subcList = newCluster.getRealSubCluster();
            int size = subcList.size();
            for( int i=0; i<size; i++ ){
                SubCluster newSubc = subcList.get(i);
                if( !modOneHost( newSubc.getHost(), selCluster ) ){
                    return;
                }
            }

            if( !modifyLunMap( selCluster ) ){
                return;
            }

            if( view.initor.mdb.modOneCluster2( selCluster.getCluster_id(), newCluster.getCluster_name() ) ){
                selCluster.setCluster_name( newCluster.getCluster_name() );
            }else{
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_CLUSTER )+":"+
                        view.initor.mdb.getErrorMessage()
                );
                return;
            }

            view.getTreeModel().reload( chiefHostNode );

            // 显示点击 chiefHostNode 后的右边tabpane中的内容��
            view.setCurNode( chiefHostNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefHost peOnChiefHost = new ProcessEventOnChiefHost( view );
            TreePath path = new TreePath( chiefHostNode.getPath() );
            peOnChiefHost.processTreeSelection( path );
            peOnChiefHost.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }catch(Exception ex){
            ex.printStackTrace();
        }
SanBootView.log.info(getClass().getName(),"########### End of modify cluster action. " );
    }

    private boolean modifyLunMap( Cluster selCluster ){
        int tid;
        boolean isOK;

        // 收集该集群上所有相关的vol,snap,view等,以便修改lunmap
        ProgressDialog pdiag = new ProgressDialog(
            view,
            SanBootView.res.getString("View.pdiagTitle.modifyHost"),
            SanBootView.res.getString("View.pdiagTip.modifyHost")
        );

        // 通过第六个参数，强行指定返回值中包含volmap本身，这样就可以在下面修正volmap的lunmap了
        GetRstVersion getRstVer = new GetRstVersion( pdiag,view,0,selCluster.getCluster_id(),selCluster.isCMDPProtect(),true,true );
        getRstVer.start();
        pdiag.mySetSize();
        pdiag.setLocation( view.getCenterPoint( pdiag.getDefWidth(),pdiag.getDefHeight() ) );
        pdiag.setVisible( true );

        Vector bindList = getRstVer.getBindList();
        int size = bindList.size();
        for( int i=0; i<size; i++ ){
            BindofVolAndSnap bind = (BindofVolAndSnap)bindList.elementAt(i);
            ArrayList snapList = bind.getSnapList();
            Object volObj = bind.getVolObj();
            if( !( volObj instanceof VolumeMap ) ) continue;

            int size1 = snapList.size();
            for( int j=0; j<size1; j++ ){
                Object obj = snapList.get(j);
                if( obj instanceof ViewWrapper ){
                    ViewWrapper vw = (ViewWrapper)obj;
                    tid = vw.view.getSnap_target_id();
                }else if( obj instanceof VolumeMapWrapper ){
                    VolumeMapWrapper vm =(VolumeMapWrapper)obj;
                    tid = vm.volMap.getVolTargetID();
                }else if( obj instanceof MirrorDiskInfoWrapper ){
                    tid = -1;
                }else{ // SnapWrapper
                    tid = -1;
                }
                if( tid == -1 ) continue;

                ArrayList<SubCluster> subcList = selCluster.getRealSubCluster();
                int size2 = subcList.size();
                for( int w=0; w<size2; w++ ){
                    String ip = subcList.get(w).getHost().getIP();
                    isOK = view.initor.mdb.addLunMap( tid, ip, "255.255.255.255", "rw", "", "", "", "" );
                    if( !isOK ){
                        JOptionPane.showMessageDialog(view,
                           SanBootView.res.getString("InitBootHostWizardDialog.log.lunmap") + " [ " + tid + " " + ip + " 255.255.255.255 rw ]" + " " +
                                  SanBootView.res.getString("common.failed")
                        );
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void replaceSomething( BootHost selHost,Cluster cluster ){
        BootHost real_host = cluster.getHostOnID( selHost.getID() );
        if( real_host != null ){
            real_host.setIP(  selHost.getIP() );
            real_host.setPort(  selHost.getPort() );
            real_host.setClnt_pri_ip(  selHost.getClnt_pri_ip() );
            real_host.setClnt_vip(  selHost.getClnt_vip() );
            real_host.setMtppPort(  selHost.getMtppPort() );
            real_host.setName( selHost.getName() );
            real_host.setOS(  selHost.getOS() );
            real_host.setMachine( selHost.getMachine() );
            real_host.setUUID( selHost.getUUID() );
        }
    }

    private boolean modOneHost( BootHost selHost,Cluster selCluster ){
        boolean isOK;

        String ip = selHost.getIP();
        int port = selHost.getPort();

        GetHostInfoThread thread = new GetHostInfoThread(
            view,
            ip,
            port,
            selCluster.isWinHost(),
            selCluster.getCluster_protect_type()
        );
        view.startupProcessDiag(
            SanBootView.res.getString("MenuAndBtnCenter.pdiagTitle.gettingAgentInfo")+" "+ ip,
            SanBootView.res.getString("MenuAndBtnCenter.pdiagTip.gettingAgentInfo") +" " +ip,
            thread
        );

        isOK = thread.isOk();
        if( isOK ){
            String uuid = thread.getUUID();
            if( uuid.equals("") ){
SanBootView.log.error( getClass().getName(),"UUID is null for host: "+ ip + "/"+ port );
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_BOOT_HOST )+":"+
                        view.initor.mdb.getErrorMessage() +" on " + ip
                );
                return false;
            }

            // 检查平台是否冲突
            if( ( selCluster.isWinHost() && !thread.isWinHost() )||
                ( !selCluster.isWinHost() && thread.isWinHost() )
            ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.mismatchedOS")
                );
                return false;
            }else{
                if( selCluster.isWinHost() && thread.isWinHost() ){
                    if( !selHost.getWinPlatForm().equals( thread.getWinPlatForm() ) ){
                        JOptionPane.showMessageDialog(view,
                            SanBootView.res.getString("MenuAndBtnCenter.error.mismatchedOS")
                        );
                        return false;
                    }
                }
            }

            // check uuid
            if( !uuid.equals( selHost.getUUID() ) ){
                int retval = JOptionPane.showConfirmDialog(
                    view,
                    SanBootView.res.getString("MenuAndBtnCenter.confirm17"),
                    SanBootView.res.getString("common.confirm"),  // "Confirm",
                    JOptionPane.OK_CANCEL_OPTION
                );
                if( ( retval == JOptionPane.CANCEL_OPTION ) || ( retval == JOptionPane.CLOSED_OPTION) ){
SanBootView.log.info( getClass().getName(), "select to cancel this op for conflicat uuid." );
                    return true;
                }
            }

            selHost.setName( thread.getHostName() );
            selHost.setMachine(  thread.getMachine() );
            selHost.setOS( thread.getOSName() );
            selHost.setUUID( uuid );

            Audit audit = view.audit.registerAuditRecord( selHost.getID(), MenuAndBtnCenterForMainUi.FUNC_MOD_HOST );

            isOK = view.initor.mdb.modOneBootHost5( selHost );
            if( isOK ){
                audit.setEventDesc("Modify host ( id: " + selHost.getID() + " ) successfully." );
                replaceSomething( selHost,selCluster );
                view.audit.addAuditRecord( audit );

                // 修改d2d client,不管结果
                if( !changeD2DClient( selHost ) ){
SanBootView.log.error( getClass().getName()," Modify d2d client failed: "+ip +"/" + port );
                    JOptionPane.showMessageDialog(view,
                        ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_BOOT_HOST )+" : "+
                            view.initor.mdb.getErrorMessage()
                    );
                    return false;
                }

                // dhcp和profile的修改先不管
            }else{
                audit.setEventDesc("Failed to modify host ( id: " + selHost.getID() + " )" );
                view.audit.addAuditRecord( audit );

                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_BOOT_HOST )+" : "+
                        view.initor.mdb.getErrorMessage()
                );
                return false;
            }
        }else{
            JOptionPane.showMessageDialog(view,
                ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_BOOT_HOST )+":"+
                    view.initor.mdb.getErrorMessage()
            );
            return false;
        }

        return true;
    }

    private boolean changeD2DClient( BootHost selHost ){
        BackupClient selClnt = view.initor.mdb.getClientFromVector( (long)selHost.getClnt_d2d_cid() );
        if( selClnt != null ){
            selClnt.setIP(  selHost.getIP() );
            selClnt.setPort( selHost.getMtppPort() );
            selClnt.setHostName( selHost.getName() );
            selClnt.setOsType( selHost.getOS() );
            selClnt.setMachineType( selHost.getMachine() );
            selClnt.setUUID( selHost.getUUID() );

            boolean isOK = view.initor.mdb.ModOneClient1( selClnt );
            if( isOK ){
                // 用新值替换旧值.这样GUI上所有地方都该改过来了
                selClnt.setHostName( selHost.getName() );
                selClnt.setIP( selHost.getIP() );
                selClnt.setMachineType( selHost.getMachine() );
                selClnt.setPort( selHost.getMtppPort() );
                selClnt.setOsType( selHost.getOS() );
                selClnt.setUUID( selHost.getUUID() );
            }
            return isOK;
        }

        return true;
    }
}
