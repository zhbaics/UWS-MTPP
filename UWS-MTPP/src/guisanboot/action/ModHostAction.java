/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.data.BindofVolAndSnap;
import guisanboot.data.BootHost;
import guisanboot.data.MirrorDiskInfoWrapper;
import guisanboot.data.ViewWrapper;
import guisanboot.data.VolumeMap;
import guisanboot.data.VolumeMapWrapper;
import guisanboot.datadup.data.BackupClient;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.AddHostDialog;
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
public class ModHostAction extends GeneralActionForMainUi{
    public ModHostAction(){
        super(
            ResourceCenter.SMALL_MOD_HOST,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modHost",
            MenuAndBtnCenterForMainUi.FUNC_MOD_HOST
        );
    }

    @Override public void doAction(ActionEvent evt){
        BindofVolAndSnap bind;
        Object volObj;
        ArrayList snapList;
        int i,j,size,size1,tid;
        boolean isOK;

SanBootView.log.info(getClass().getName(),"########### Entering modify host action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !(selObj instanceof BootHost) ) return;

        try{
            BootHost selHost = (BootHost)selObj;
            BrowserTreeNode selHostNode = selHost.getTreeNode();
            BrowserTreeNode chiefHostNode = (BrowserTreeNode)selHostNode.getParent();
            BackupClient selClnt = view.initor.mdb.getBkClntOnUUID( selHost.getUUID() );

            // 检查可否修改该主机

            AddHostDialog dialog = new AddHostDialog( selHost,view );
            int width  = 340 + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 145 + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );

            Object[] ret  = dialog.getValue();
            if( ret == null || ret.length <= 0 ) return;

            String ip =(String)ret[0];
            int port = ((Integer)ret[1]).intValue();  // cmdp port
            int port1 = ((Integer)ret[2]).intValue(); // mtpp port

            GetHostInfoThread thread = new GetHostInfoThread(
                view,
                ip,
                port,
                selHost.isWinHost(),
                selHost.getProtectType()
            );
            view.startupProcessDiag(
                SanBootView.res.getString("MenuAndBtnCenter.pdiagTitle.gettingAgentInfo"),
                SanBootView.res.getString("MenuAndBtnCenter.pdiagTip.gettingAgentInfo"),
                thread
            );

            isOK = thread.isOk();
            if( isOK ){
                String uuid = thread.getUUID();
                if( uuid.equals("") ){
SanBootView.log.error( getClass().getName(),"UUID is null for host: "+ ip + "/"+ port );
                    JOptionPane.showMessageDialog(view,
                        ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_BOOT_HOST )+":"+
                            view.initor.mdb.getErrorMessage()
                    );
                    return;
                }

                // 检查平台是否冲突
                if( ( selHost.isWinHost() && !thread.isWinHost() )||
                    ( !selHost.isWinHost() && thread.isWinHost() )
                ){
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.mismatchedOS")
                    );
                    return;
                }else{
                    if( selHost.isWinHost() && thread.isWinHost() ){
                        if( !selHost.getWinPlatForm().equals( thread.getWinPlatForm() ) ){
                            JOptionPane.showMessageDialog(view,
                                SanBootView.res.getString("MenuAndBtnCenter.error.mismatchedOS")
                            );
                            return;
                        }
                    }
                }

                // check uuid
                if( !uuid.equals( selHost.getUUID() ) ){
                    int retval = JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm17"),
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if( ( retval == JOptionPane.CANCEL_OPTION ) || ( retval == JOptionPane.CLOSED_OPTION) ){
                        return;
                    }
                }

                BootHost newHost = new BootHost(
                    selHost.getID(),
                    thread.getHostName(),
                    ip,
                    thread.getMachine(),
                    port,
                    port1,
                    thread.getOSName(),
                    "Online",
                    uuid, // uuid
                    selHost.getInitFlag(),  // inited ?
                    selHost.getAutoDRFlag(),
                    selHost.getAutoRebootFlag(),
                    selHost.getStopAllBaseServFlag(),
                    selHost.getBootMac(),
                    selHost.getBootMode(),
                    selHost.getProtectType()
                );

                Audit audit = view.audit.registerAuditRecord( selHost.getID(), MenuAndBtnCenterForMainUi.FUNC_MOD_HOST );

                isOK = view.initor.mdb.modOneBootHost( newHost );
                if( isOK ){
                    audit.setEventDesc("Modify host ( id: " + selHost.getID() + " ) successfully." );
                    view.audit.addAuditRecord( audit );

                    // 修改d2d client,不管结果
                    if( !changeD2DClient( selClnt,uuid, thread, ip, port1 ) ){
SanBootView.log.error( getClass().getName()," Modify d2d client failed: "+ip +"/" + port );
                        JOptionPane.showMessageDialog(view,
                            ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_BOOT_HOST )+" : "+
                                view.initor.mdb.getErrorMessage()
                        );
                        return;
                    }

                    // dhcp和profile的修改先不管��Ȳ���

                    // 收集该机器上所有相关的vol,snap,view等,以便修改lunmap
                    ProgressDialog pdiag = new ProgressDialog(
                        view,
                        SanBootView.res.getString("View.pdiagTitle.modifyHost"),
                        SanBootView.res.getString("View.pdiagTip.modifyHost")
                    );

                    // 通过第六个参数，强行指定返回值中包含volmap本身，这样就可以在下面修正volmap的lunmap了
                    GetRstVersion getRstVer = new GetRstVersion( pdiag,view,selHost.getID(),0,selHost.isCMDPProtect(),true,true );
                    getRstVer.start();
                    pdiag.mySetSize();
                    pdiag.setLocation( view.getCenterPoint( pdiag.getDefWidth(),pdiag.getDefHeight() ) );
                    pdiag.setVisible( true );

                    Vector bindList = getRstVer.getBindList();
                    size = bindList.size();
                    for( i=0; i<size; i++ ){
                        bind = (BindofVolAndSnap)bindList.elementAt(i);
                        snapList = bind.getSnapList();
                        volObj = bind.getVolObj();
                        if( volObj instanceof VolumeMap ){
                            size1 = snapList.size();
                            for( j=0; j<size1; j++ ){
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

                                if( tid != -1 ){
                                    isOK = view.initor.mdb.addLunMap( tid, ip, "255.255.255.255", "rw", "", "", "", "" );
                                    if( !isOK ){
                                        JOptionPane.showMessageDialog(view,
                                           SanBootView.res.getString("InitBootHostWizardDialog.log.lunmap") + " [ " + tid + " " + ip + " 255.255.255.255 rw ]" + " " +
                                                  SanBootView.res.getString("common.failed")
                                        );
                                        return;
                                    }
                                }
                            }
                        }
                    }

                    // 用新值替换旧值.这样GUI上所有地方都该改过来了
                    selHost.setName( newHost.getName() );
                    selHost.setIP( newHost.getIP() );
                    selHost.setMachine( newHost.getMachine() );
                    selHost.setPort( newHost.getPort() );
                    selHost.setOS( newHost.getOS() );
                    selHost.setStatus( newHost.getStatus() );
                    selHost.setUUID( newHost.getUUID() );

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
                }else{
                    audit.setEventDesc("Failed to modify host ( id: " + selHost.getID() + " )" );
                    view.audit.addAuditRecord( audit );

                    JOptionPane.showMessageDialog(view,
                        ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_BOOT_HOST )+" : "+
                            view.initor.mdb.getErrorMessage()
                    );
                }
            }else{
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_BOOT_HOST )+":"+
                        view.initor.mdb.getErrorMessage()
                );
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
SanBootView.log.info(getClass().getName(),"########### End of modify host action. " );
    }

    private boolean changeD2DClient( BackupClient selClnt,String uuid,GetHostInfoThread thread,String ip,int port ){
        if( selClnt != null ){
            BackupClient newClnt = new BackupClient(
                selClnt.getID(),
                thread.getHostName(),
                ip,
                thread.getMachine(),
                port,
                thread.getOSName(),
                "Online",
                uuid, // uuid
                selClnt.getAcctID()
            );

            boolean isOK = view.initor.mdb.ModOneClient( newClnt );
            if( isOK ){
                // 用新值替换旧值.这样GUI上所有地方都该改过来了
                selClnt.setHostName( newClnt.getHostName() );
                selClnt.setIP( newClnt.getIP() );
                selClnt.setMachineType( newClnt.getMachineType() );
                selClnt.setPort( newClnt.getPort() );
                selClnt.setOsType( newClnt.getOsType() );
                selClnt.setStatus( newClnt.getStatus() );
                selClnt.setUUID( newClnt.getUUID() );
            }
            return isOK;
        }

        return true;
    }
}
