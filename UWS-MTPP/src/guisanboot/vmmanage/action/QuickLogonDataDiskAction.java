/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.*;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author zourishun
 */
public class QuickLogonDataDiskAction extends GeneralActionForMainUi {

    public QuickLogonDataDiskAction() {
        super(
                ResourceCenter.BTN_ICON_DR_16,
                ResourceCenter.BTN_ICON_DR_50,
                "View.MenuItem.failover",
                MenuAndBtnCenterForMainUi.FUNC_WIN_VMHOST_QUICK_LOGON_DATA_DISK);
    }

    @Override
    public void doAction(ActionEvent evt) {
        String ip;
        int port = 2015;
        boolean isOk;
        Vector bindlist = null;
        
        Object selObj = view.getSelectedObjFromSanBoot();
        if (selObj == null) {
            return;
        }
        boolean isVMHost = (selObj instanceof VMHostInfo);

        if (isVMHost) {
            VMHostInfo vmh = (VMHostInfo) selObj;
            ip = vmh.getVm_vip();
            BootHost host = view.initor.mdb.getBootHostFromVector(Integer.parseInt(vmh.getVm_clntid()));
            if (!host.isInited()) {
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notInited"));
                return;
            }

            isOk = view.initor.mdb.isRunningVMHost(vmh);
            if (!isOk) {
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("VMHostInfo.error.notpoweronVMHost"));
                return;
            }

            isOk = view.initor.mdb.isAlreadyStartSystem(ip, 2015);
            if (!isOk) {
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("QuickLogonDataDiskAction.error.notpingtohost"));
                return;
            }

            //添加新门户
            String svrIP = view.initor.mdb.getUWSDefaultIp();
            isOk = view.initor.mdb.addPortal(ip, port, svrIP, 3260, ResourceCenter.CMD_TYPE_CMDP);
            if (!isOk) {
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("QuickLogonDataDiskAction.error.addPortalFailed") + ":" + svrIP);
                return;
            }

            //准备快照
            ProgressDialog initDiag = new ProgressDialog(
                    view,
                    SanBootView.res.getString("View.pdiagTitle.getSnapVer"),
                    SanBootView.res.getString("View.pdiagTip.getSnapVer"));

            GetRstVersion getRstVer = new GetRstVersion(initDiag, view, Integer.valueOf(vmh.getVm_clntid()).intValue(), true, true);
            getRstVer.start();
            initDiag.mySetSize();
            initDiag.setLocation(view.getCenterPoint(initDiag.getDefWidth(), initDiag.getDefHeight()));
            initDiag.setVisible(true);
            bindlist = getRstVer.getBindList();

            QuickLogonViewThread thread = new QuickLogonViewThread(view ,bindlist ,ip ,port ,svrIP);
            view.startupProcessDiag(
                    SanBootView.res.getString("View.pdiagTitle.quickLogonView"),
                    SanBootView.res.getString("View.pdiagTip.quickLogonView"),
                    thread
                );
            
        } else {
            return;
        }
        SanBootView.log.info(getClass().getName(), "########### End of quicklogondatadisk action. ");

    }
}

class QuickLogonViewThread extends BasicGetSomethingThread{
    private Vector bindlist;
    private String ip;
    private String svrIP;
    private int port =2015;
    
    public QuickLogonViewThread(SanBootView _view , Vector _bindlist ,String _ip ,int _port ,String _svrIP){
        super(_view);
        this.bindlist = _bindlist;
        this.ip = _ip;
        this.port = _port;
        this.svrIP = _svrIP;
    }
    @Override
    public boolean realRun() {
        //获取数据盘的最新快照
         Map dataDisk = new HashMap();
         Object binderObj;
            for (int i = 0; i < bindlist.size(); i++) {
                binderObj = bindlist.elementAt(i);
                if (binderObj instanceof BindofVolAndSnap) {
                    VolumeMap vm = ((BindofVolAndSnap) binderObj).getVolMap();
                    if (!vm.isOsVolMap()) {
                        dataDisk.put(vm.getLetter(), ((BindofVolAndSnap) binderObj).getSnapList().get(0));
                    }
                }
            }

            Iterator it = dataDisk.entrySet().iterator();
            int targetid = 0;
            String iscsiVar ,errmsg = "";
            String targetSrvName = view.initor.mdb.getHostName();
            int size = dataDisk.size();
            int count = 0;
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String letter = entry.getKey().toString();
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
                isOk = view.initor.mdb.addLunMap(targetid, ip, "255.255.255.255", "rw", "", "", "", "");
                if (!isOk) {
                    errmsg = errmsg + SanBootView.res.getString("QuickLogonDataDiskAction.error.LunMapFailed") + ":" +targetid + "\n";
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
                count ++;
            }
            if(count == size){
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("QuickLogonDataDiskAction.success.successful") );
                return true;
            } else {
//                JOptionPane.showMessageDialog(view,
//                        SanBootView.res.getString("QuickLogonDataDiskAction.error.switchFailed") + errmsg );
                this.errMsg = SanBootView.res.getString("QuickLogonDataDiskAction.error.switchFailed") + errmsg;
                return false;
            }
    }
    
}