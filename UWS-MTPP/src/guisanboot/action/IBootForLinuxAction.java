/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.data.*;
import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.SourceAgent;
import guisanboot.data.SubNetInDHCPConf;
import guisanboot.remotemirror.ChiefRollbackHost;
import guisanboot.remotemirror.IbootForLinuxSourceAgentWizardDialog;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.IbootForLinux6WizardDialog;
import guisanboot.ui.IbootForLinuxWizardDialog;

import guisanboot.ui.IbootForLinuxWizardDialog64;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Vector;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class IBootForLinuxAction extends GeneralActionForMainUi {

    public IBootForLinuxAction() {
        super(
                ResourceCenter.MENU_ICON_BLANK,
                ResourceCenter.BTN_ICON_DR_50,
                "View.MenuItem.ibootForLinux",
                MenuAndBtnCenterForMainUi.FUNC_IBOOT_WIZARD);
    }

    @Override
    public void doAction(ActionEvent evt) {
        boolean isOk;
        String targetSrvName, original_boot_MAC;

        SanBootView.log.info(getClass().getName(), "########### Entering linux iboot wizard ......");
        Object selObj = view.getSelectedObjFromSanBoot();
        if (selObj == null) {
            SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
            return;
        }

        boolean isBootHost = (selObj instanceof BootHost);
        boolean isSrvAgent = (selObj instanceof SourceAgent);
        if (isBootHost) {
            BootHost host = (BootHost) selObj;

            if (host.isWinHost()) {
                SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.isWinHost"));
                return;
            }

            if (!host.isInited()) {
                SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notInited"));
                return;
            }

            String status = isStartFromNet(host.getIP(), host.getPort());
            if (!status.toUpperCase().equals("UNKNOW")) {
                if (status.toUpperCase().equals("YES")) {
                    SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                    JOptionPane.showMessageDialog(view,
                            SanBootView.res.getString("MenuAndBtnCenter.error.notChgVerWhenNetboot"));
                    return;
                }
            } else {
                int ret = JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm16"),
                        SanBootView.res.getString("common.confirm"), //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION);
                if ((ret == JOptionPane.CANCEL_OPTION) || (ret == JOptionPane.CLOSED_OPTION)) {
                    SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                    return;
                }
            }

            targetSrvName = view.initor.mdb.getHostName();
            if (targetSrvName.equals("")) {
                SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("InitBootHostWizardDialog.log.getHostNameFailed"));
                return;
            }

            isOk = view.initor.mdb.getUnixNetCardInfo(ResourceCenter.CLT_IP_CONF + "/" + host.getID() + ".conf");
            if (isOk) {
                original_boot_MAC = view.initor.mdb.getUnixBootMac();
                if (original_boot_MAC.equals("")) {
                    SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                    JOptionPane.showMessageDialog(view,
                            SanBootView.res.getString("FailoverWizardDialog.error.notFoundBootMac"));
                    return;
                }
            } else {
                SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("FailoverWizardDialog.error.notFoundBootMac"));
                return;
            }

            isOk = view.initor.mdb.getUnixPart1(ResourceCenter.CLT_IP_CONF + "/" + host.getID() + ResourceCenter.CONF_MP);
            if (!isOk) {
                SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                JOptionPane.showMessageDialog(view,
                        ResourceCenter.getCmdString(ResourceCenter.CMD_GET_UNIX_PART) + " : "
                        + view.initor.mdb.getErrorMessage());
                return;
            }
            Vector partList = view.initor.mdb.getUnixSysPart();
//            if ("x86_64".equals(host.getMachine().toLowerCase())) {
//
//                System.out.println(DhcpClientInfo.getMacItem(original_boot_MAC));
//                System.out.println(DhcpClientInfo.getMacStr(original_boot_MAC));
//                System.out.println(DhcpClientInfo.getMacStrForWin(original_boot_MAC));
//                System.out.println(DhcpClientInfo.getSimpleMac(original_boot_MAC));
//
//                boolean isokk = view.initor.mdb.findFile(host.getID()+"-network.conf") ;
//
//                if (isokk) {
//                    IbootForLinuxWizardDialog64 dialog64 = new IbootForLinuxWizardDialog64(view, host, targetSrvName, partList, original_boot_MAC);
//                    int width64 = 400 + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
//                    int height64 = 300 + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; // 380;
//                    dialog64.setSize(width64, height64);
//                    dialog64.setLocation(view.getCenterPoint(width64, height64));
//                    dialog64.setVisible(true);
//                }else {
//                    IbootForLinux6WizardDialog dialog1 = new IbootForLinux6WizardDialog(view, host, targetSrvName, partList, original_boot_MAC);
//                    int width1 = 400 + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
//                    int height1 = 200 + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; // 380;
//                    dialog1.setSize(width1, height1);
//                    dialog1.setLocation(view.getCenterPoint(width1, height1));
//                    dialog1.setVisible(true);
//                }
//
//            } else {

                IbootForLinuxWizardDialog dialog = new IbootForLinuxWizardDialog(view, host, targetSrvName, partList, original_boot_MAC);
                int width = 560 + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 400 + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; // 380;
                dialog.setSize(width, height);
                dialog.setLocation(view.getCenterPoint(width, height));
                dialog.setVisible(true);
//            }
            SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
        } else if (isSrvAgent) {
            boolean mustCheckDiskExist = false;
            SourceAgent host = (SourceAgent) selObj;
            BrowserTreeNode fNode = host.getFatherNode();
            Object fObj = fNode.getUserObject();
            if (fObj instanceof ChiefRollbackHost) {
                mustCheckDiskExist = true;
            }

            if (host.isWinHost()) {
                SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.isWinHost"));
                return;
            }

            int ret = JOptionPane.showConfirmDialog(
                    view,
                    SanBootView.res.getString("MenuAndBtnCenter.confirm6"),
                    SanBootView.res.getString("common.confirm"), //"Confirm",
                    JOptionPane.OK_CANCEL_OPTION);
            if ((ret == JOptionPane.CANCEL_OPTION) || (ret == JOptionPane.CLOSED_OPTION)) {
                SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                return;
            }

            targetSrvName = view.initor.mdb.getHostName();
            if (targetSrvName.equals("")) {
                SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("InitBootHostWizardDialog.log.getHostNameFailed"));
                return;
            }

            isOk = view.initor.mdb.getUnixNetCardInfo(ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host.getSrc_agnt_id() + ".conf");
            if (isOk) {
                original_boot_MAC = view.initor.mdb.getUnixBootMac();
                if (original_boot_MAC.equals("")) {
                    SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                    JOptionPane.showMessageDialog(view,
                            SanBootView.res.getString("FailoverWizardDialog.error.notFoundBootMac"));
                    return;
                }
            } else {
                SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("FailoverWizardDialog.error.notFoundBootMac"));
                return;
            }

            isOk = view.initor.mdb.getUnixPart1(ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + host.getSrc_agnt_id() + ResourceCenter.CONF_MP);
            if (!isOk) {
                SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
                JOptionPane.showMessageDialog(view,
                        ResourceCenter.getCmdString(ResourceCenter.CMD_GET_UNIX_PART) + " : "
                        + view.initor.mdb.getErrorMessage());
                return;
            }
            Vector partList = view.initor.mdb.getUnixSysPart();

            IbootForLinuxSourceAgentWizardDialog dialog = new IbootForLinuxSourceAgentWizardDialog(view, host, targetSrvName, partList, original_boot_MAC, mustCheckDiskExist);
            int width = 560 + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 400 + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE; // 380;
            dialog.setSize(width, height);
            dialog.setLocation(view.getCenterPoint(width, height));
            dialog.setVisible(true);
            SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
        } else {
            SanBootView.log.info(getClass().getName(), "########### End of linux iboot wizard. ");
            return;
        }
    }

    private String isStartFromNet(String ip, int port) {
        /// 判断是否从 network 启动
        boolean isOk = view.initor.mdb.isStartupfromNetBoot(ip, port);
        if (isOk) {
            String ret = view.initor.mdb.isStartupFromNetBoot() ? "yes" : "no";
            return ret;
        } else {
            return "unknow";
        }
    }
}
