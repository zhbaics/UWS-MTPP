/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import guisanboot.vmmanage.service.ProcessEventOnVMHostInfo;
import guisanboot.vmmanage.ui.SelectDataDiskSnapVersionDialog;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author Administrator
 */
public class PowerOnVMHostAction extends GeneralActionForMainUi {

    public PowerOnVMHostAction() {
        super(
                ResourceCenter.MENU_ICON_BLANK,
                ResourceCenter.MENU_ICON_BLANK,
                "View.MenuItem.powerOnVMHost",
                MenuAndBtnCenterForMainUi.FUNC_POWERON_VMHOST);
    }

    @Override
    public void doAction(ActionEvent evt) {
        SanBootView.log.info(getClass().getName(), "########### Entering power on VMHost action.");
        Object selObj = view.getSelectedObjFromSanBoot();
        if (selObj == null) {
            SanBootView.log.info(getClass().getName(), "bad type or sel obj is null \n ########### End of power on VMHost action.");
            return;
        }
        if (selObj instanceof VMHostInfo) {
            VMHostInfo vmhost = (VMHostInfo) selObj;
            //是否现在挂载数据盘
            boolean isLogon = false;
            Map dataDisk = new HashMap();

            boolean isOk = view.initor.mdb.isRunningVMHost(vmhost);
            if (!isOk) {

                int ret = JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("VMHostPowerOnSelect.select.LongonDataDisk"),
                        SanBootView.res.getString("common.confirm"), //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION);
                if ((ret == JOptionPane.CANCEL_OPTION) || (ret == JOptionPane.CLOSED_OPTION)) {
                    isLogon = false;
                } else {
                    isLogon = true;
                    BootHost host = view.initor.mdb.getBootHostFromVector(Integer.parseInt(vmhost.getVm_clntid()));
                    SelectDataDiskSnapVersionDialog dialog = new SelectDataDiskSnapVersionDialog(view, host);
                    int width = 560 + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                    int height = 320 + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; //380;
                    dialog.setSize(width, height);
                    dialog.setLocation(view.getCenterPoint(width, height));
                    dialog.setVisible(true);

                    Object[] ret1 = dialog.getValues();
                    if (ret1 == null) { 
                        return;
                    }
                    ArrayList retlist = (ArrayList) ret1[0];
                    BindOfVolMapandSnap binder;
                    if (retlist != null && retlist.size() > 0) {
                        for (int i = 0; i < retlist.size(); i++) {
                            binder = (BindOfVolMapandSnap) retlist.get(i);
                            dataDisk.put(binder.getVolMap().getLetter(), binder.snap);
                        }
                    }
                }

                PowerOnVMHostThread thread = new PowerOnVMHostThread(view, vmhost);
                view.startupProcessDiag(
                        SanBootView.res.getString("View.pdiagTitle.powerOnVMHost"),
                        SanBootView.res.getString("View.pdiagTip.powerOnVMHost"),
                        thread);
                if (thread.isOk()) {
                    if ( isLogon ) {
                        LogonDataDiskThread thread1 = new LogonDataDiskThread(view, dataDisk, vmhost.getVm_vip(), 2015);
                        view.startupProcessDiag(
                                SanBootView.res.getString("View.pdiagTitle.quickLogonView"),
                                SanBootView.res.getString("View.pdiagTip.quickLogonView"),
                                thread1);
                    }
                }
            } else {

                JOptionPane.showMessageDialog(view, SanBootView.res.getString("VMHostInfo.mes.powerOn"));
                return;
            }
        }
        SanBootView.log.info(getClass().getName(), "########### End of power on VMHost action.");
    }
}

class LogonDataDiskThread extends BasicGetSomethingThread {

    Map dataDisk = new HashMap();
    private String ip;
    private int port = 2015;

    public LogonDataDiskThread(SanBootView _view, Map _map, String _ip, int _port) {
        super(_view);
        dataDisk = _map;
        ip = _ip;
        port = _port;
    }

    @Override
    public boolean realRun() {
        String svrIP = view.initor.mdb.getUWSDefaultIp();
        boolean isOk1 = view.initor.mdb.addPortal(ip, port, svrIP, 3260, ResourceCenter.CMD_TYPE_CMDP);
        if (isOk1) {
            Iterator it = dataDisk.entrySet().iterator();
            int targetid = 0;
            String iscsiVar, errmsg = "";
            String targetSrvName = view.initor.mdb.getHostName();
            int size = dataDisk.size();
            int count = 0;
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String letter = entry.getKey().toString();
                if (entry.getValue() instanceof SnapWrapper) {
                    SnapWrapper objSnap = (SnapWrapper) entry.getValue();
                    String newViewName = ResourceCenter.NET_START_VIEW + letter.toUpperCase() + "_used_by_virtual_machine";
                    isOk = view.initor.mdb.addView(newViewName, objSnap.snap.getSnap_root_id(), objSnap.snap.getSnap_local_snapid());
                    if (isOk) {
                        View newView = view.initor.mdb.getCrtView();
                        targetid = newView.getTargetID();
                    } else {
                        errmsg = errmsg + SanBootView.res.getString("CreateVMHost.error.wrongcrtview") + ":" + letter.toUpperCase() + "\n";
                        continue;
                    }
                } else if (entry.getValue() instanceof ViewWrapper) {
                    ViewWrapper objView = (ViewWrapper) entry.getValue();
                    targetid = objView.view.getTargetID();
                } else {
                    errmsg = errmsg + SanBootView.res.getString("QuickLogonDataDiskAction.error.wrongDataDisk") + "\n";
                    continue;
                }
                isOk = view.initor.mdb.addLunMap(targetid, ip, "255.255.255.255", "rw", "", "", "", "");
                if (!isOk) {
                    errmsg = errmsg + SanBootView.res.getString("QuickLogonDataDiskAction.error.LunMapFailed") + ":" + targetid + "\n";
                    continue;
                }

                //挂载数据盘
                iscsiVar = ResourceCenter.ISCSI_PREFIX + targetSrvName + ":" + targetid;
                int retryCnt = 0;
                while (retryCnt < 3) {
                    isOk = view.initor.mdb.assignDriver1(ip, port,
                            svrIP, ResourceCenter.ISCSI_LOGIN_PORT + "", iscsiVar,
                            letter, ResourceCenter.CMD_TYPE_CMDP);
                    if (isOk) {
                        break;
                    } else {
                        SanBootView.log.info(getClass().getName(), "sleep 10 sec. and then retry set-partition again......");
                        try {
                            Thread.sleep(10 * 1000);
                        } catch (Exception ex) {
                        }
                        retryCnt++;
                    }
                }
                count++;
            }
            if (count == size) {
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("QuickLogonDataDiskAction.success.successful"));
                return true;
            } else {
//                JOptionPane.showMessageDialog(view,
//                        SanBootView.res.getString("QuickLogonDataDiskAction.error.switchFailed") + errmsg );
                this.errMsg = SanBootView.res.getString("QuickLogonDataDiskAction.error.switchFailed") + errmsg;
                return false;
            }
        } else {
            this.errMsg = SanBootView.res.getString("QuickLogonDataDiskAction.error.addPortalFailed") + ":" + svrIP;
            return false;
        }
    }
}

class PowerOnVMHostThread extends BasicGetSomethingThread {

    VMHostInfo vmhost;
    public final static int COUNT = 50;

    public PowerOnVMHostThread(SanBootView view, VMHostInfo _vmhost) {
        super(view);
        this.vmhost = _vmhost;
    }

    public boolean realRun() {
        boolean isOk, ret, isRunning = false;
        int count = 0;
        isOk = view.initor.mdb.changeDiskVMHost(vmhost);
        if (isOk) {
            isOk = view.initor.mdb.powerOnVMHost(this.vmhost);
            if (isOk) {
                try {
                    while (true) {
                        isOk = view.initor.mdb.isAlreadyStartSystem(vmhost.getVm_vip(), Integer.parseInt(vmhost.getVm_port()));
                        if (!isOk) {
                            isRunning = view.initor.mdb.isRunningVMHost(vmhost);
                            if (!isRunning) {
                                break;
                            }
                            Thread.sleep(5 * 1000);//等5s，等虚拟机系统完全启好
                            count++;
                            if (count > COUNT) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }

                    if (!isRunning) {
                        JOptionPane.showMessageDialog(view, SanBootView.res.getString("VMHostInfo.error.powerOnfailed-1"));
                        return false;
                    }

                    if (count > COUNT) {
                        ret = view.initor.mdb.isRunningVMHost(vmhost);
                        if (!ret) {
                            JOptionPane.showMessageDialog(view, SanBootView.res.getString("VMHostInfo.error.powerOnfailed-1"));
                            return false;
                        } else {
                            JOptionPane.showMessageDialog(view, SanBootView.res.getString("VMHostInfo.error.powerOnfailed-2"));
                            return false;
                        }
                    }


                } catch (Exception ex) {
                    return false;
                }
                updateUI();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("VMHostInfo.error.powerOnfailed"));
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(view, SanBootView.res.getString("VMHostInfo.error.powerOnfailed"));
            return false;
        }
        return isOk;
    }

    public void updateUI() {
        BrowserTreeNode vmHostNode = this.vmhost.getTreeNode();
        if (vmHostNode != null) {
            view.setCurNode(vmHostNode);
            view.setCurBrowserEventType(Browser.TREE_SELECTED_EVENT);
            ProcessEventOnVMHostInfo peOnVMHostInfo = new ProcessEventOnVMHostInfo(view);
            TreePath path = new TreePath(vmHostNode.getPath());
            peOnVMHostInfo.processTreeSelection(path);
            peOnVMHostInfo.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath(path);
            view.getTree().requestFocus();
        }
    }
}