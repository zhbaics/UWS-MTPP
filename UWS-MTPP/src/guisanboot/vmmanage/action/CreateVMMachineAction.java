/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.VMHostInfo;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.*;
import guisanboot.vmmanage.service.SetViewVersionThread;
import guisanboot.vmmanage.ui.CreateVMMachineDialog;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author Administrator
 */
public class CreateVMMachineAction extends GeneralActionForMainUi {

    public CreateVMMachineAction() {
        super(
                ResourceCenter.MENU_ICON_BLANK,
                ResourceCenter.MENU_ICON_BLANK,
                "View.MenuItem.crtVMMachine",
                MenuAndBtnCenterForMainUi.FUNC_CRT_VM_MACHINE);
    }

    @Override
    public void doAction(ActionEvent evt) {
        SanBootView.log.info(getClass().getName(), "########### Entering create VMHost action. ");
        Object selObj = view.getSelectedObjFromSanBoot();
        if (selObj == null) {
            return;
        }
        VMHostInfo vmhost = null;
        Vector bindlist = null;
        String tostype, ostype, arch;
        long disksize = 0;
        Object[] ret;
        boolean isOk;
        isOk = view.initor.mdb.hasVMServiceInfo();
        if( !isOk ){
            JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("CreateVMHost.error.noServiceInfo")
                    );
                    return;
        }
        if (selObj instanceof ChiefVMHost) {
            ChiefVMHost cvmhost = (ChiefVMHost) selObj;
            BrowserTreeNode fNode = cvmhost.getFatherNode();
            Object fObj = fNode.getUserObject();
            if (fObj instanceof BootHost) {
                BootHost bhost = (BootHost) fObj;
                //获取主机系统相关信息
                isOk = view.initor.mdb.updateVMinfoFromHost(bhost.getIP());
                if (isOk) {
                    tostype = view.initor.mdb.getOsType();
                    ostype = tostype.substring(tostype.indexOf("2"));
                    arch = view.initor.mdb.getArch();
                    disksize = view.initor.mdb.getDiskSize();
                } else {
                    JOptionPane.showMessageDialog(view,
                            SanBootView.res.getString("CreateVMMachine.error.getInfofailed"));
                    return;
                }

                //记录vmhost信息
                vmhost = new VMHostInfo();
                vmhost.setVm_clntid(bhost.getID() + "");
                isOk = view.initor.mdb.updateVMServerInfo();
                if (isOk) {
                    vmhost.setVm_ip(view.initor.mdb.getVMServerIP());
                    vmhost.setVm_port(view.initor.mdb.getVMServerPort() + "");
                } else {
                    JOptionPane.showMessageDialog(view,
                            SanBootView.res.getString("CreateVMHost.error.wrongServer"));
                    return;
                }
                String hostname = bhost.getName();
                vmhost.setVm_name("vm" + hostname);
                vmhost.setVm_path("C:\\vm" + hostname);
                // 正在准备快照版本
                ProgressDialog initDiag = new ProgressDialog(
                        view,
                        SanBootView.res.getString("View.pdiagTitle.getSnapVer"),
                        SanBootView.res.getString("View.pdiagTip.getSnapVer"));

                GetRstVersion getRstVer = new GetRstVersion(initDiag, view, bhost.getID(), true, true);
                getRstVer.start();
                initDiag.mySetSize();
                initDiag.setLocation(view.getCenterPoint(initDiag.getDefWidth(), initDiag.getDefHeight()));
                initDiag.setVisible(true);
                bindlist = getRstVer.getBindList();

                CreateVMMachineDialog dialog = new CreateVMMachineDialog(view, vmhost, bindlist, bhost);
                int width = 400 + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 460 + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize(width, height);
                dialog.setLocation(view.getCenterPoint(width, height));
                dialog.setVisible(true);

                ret = dialog.getValues();
                if (ret == null) {
                    SanBootView.log.info(getClass().getName(), "cancel to create VMHost.");
                    return;
                }
                vmhost = (VMHostInfo) ret[0];
                String vmpath = (String) ret[1];
                String networkNumber = (String) ret[2];
                String memzie = (String) ret[3];
                //拼接创建虚拟机的参数
                StringBuffer args = new StringBuffer();
                args.append("\"").append(vmhost.getVm_ip());
                args.append(",").append(vmhost.getVm_port());
                args.append(",").append(vmpath);
                args.append(",").append(vmhost.getVm_name());
                args.append(",").append(ostype);
                args.append(",").append(arch);
                args.append(",").append(networkNumber);
                args.append(",").append(disksize);
                args.append(",").append(vmhost.getVm_maxdisctime());
                args.append(",").append(vmhost.getVm_pingip());
                args.append(",").append(vmhost.getVm_ischeck()).append("\"");
                isOk = view.initor.mdb.createVMMachine(args.toString());

                //add netboot--bye hwh
                /**
                DestAgent  newDa = new DestAgent(
                                    -1,
                                    vmhost.getVm_vip(),
                                    Integer.parseInt(vmhost.getVm_port()), //2015,
                                    bhost.getOS(), //host.getOS(),
                                    "", //simpleMAC,
                                    "",
                                    BootHost.PROTECT_TYPE_CMDP
                                );
                int  newDaid = -1;
                if( view.initor.mdb.addNBH( newDa ) ){
                    newDaid  = view.initor.mdb.getNewId();
                    newDa.setDst_agent_id( newDaid );
                    view.initor.mdb.addNBHIntoCache( newDa );
                }

                SnapUsage msu = new SnapUsage(
                        -1, //int usage_id,
                        newDaid,    //int dst_agent_id,
                        Integer.parseInt(vmhost.getVm_snap_root_id()),    //int snap_rootid,
                        -1, //int snap_local_id,
                        -1, //int snap_view_local_id,
                        "C:\\vm" + hostname, //String export_mp,
                        -1, //int snap_target_id,
                        "" //String snap_crt_time
                        );
                view.initor.mdb.addMSU(msu);
                **/
                
                if (isOk) {
                    SetViewVersionThread thread = new SetViewVersionThread(view, vmhost, cvmhost);
                    view.startupProcessDiag(
                            SanBootView.res.getString("View.pdiagTitle.createVMHost"),
                            SanBootView.res.getString("View.pdiagTip.createVMHost"),
                            thread);
                } else {
                    view.initor.mdb.removeVMHost(vmhost);
                    JOptionPane.showMessageDialog(view,
                            SanBootView.res.getString("CreateVMHost.error.createfailed"));
                    return;
                }
            }
        }
        SanBootView.log.info(getClass().getName(), "########### End of create vmhost action. ");
    }
}
