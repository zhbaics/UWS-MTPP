/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.LogicalVol;
import guisanboot.data.Snapshot;
import guisanboot.data.View;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.snapusage.MountSnapOrViewDialog;
import guisanboot.ui.ChiefHostVolume;
import guisanboot.ui.ChiefSnapshot;
import guisanboot.ui.GeneralActionForMainUi;
import java.awt.event.ActionEvent;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class MntSnapAction extends GeneralActionForMainUi {

    public MntSnapAction() {
        super(
                ResourceCenter.MENU_ICON_BLANK,
                ResourceCenter.MENU_ICON_BLANK,
                "View.MenuItem.mntSnap",
                MenuAndBtnCenterForMainUi.FUNC_MNT_SNAP);
    }

    @Override
    public void doAction(ActionEvent evt) {
        BrowserTreeNode chiefHostVolNode;

        Object selObj = view.getSelectedObjFromSanBoot();
        if (selObj == null) {
            return;
        }

        if (!(selObj instanceof Snapshot)) {
            return;
        }

        Snapshot snap = (Snapshot) selObj;
        BrowserTreeNode fNode = snap.getFatherNode();
        ChiefSnapshot chiefSnap = (ChiefSnapshot) fNode.getUserObject();
        BrowserTreeNode volNode = chiefSnap.getFatherNode();
        Object volObj = volNode.getUserObject();
        if (volObj instanceof Volume) {
            Volume vol = (Volume) volObj;
            chiefHostVolNode = vol.getFatherNode();
        } else if (volObj instanceof LogicalVol) {
            LogicalVol lv = (LogicalVol) volObj;
            chiefHostVolNode = lv.getFatherNode();
        } else if (volObj instanceof View) {
            View v = (View) volObj;
            chiefHostVolNode = v.getFatherNode();
        } else {
            VolumeMap volMap = (VolumeMap) volObj;
            chiefHostVolNode = volMap.getFatherNode();
        }
        ChiefHostVolume chiefHostVol = (ChiefHostVolume) chiefHostVolNode.getUserObject();
        BrowserTreeNode hostNode = chiefHostVol.getFatherNode();
        BootHost host = (BootHost) hostNode.getUserObject();

        MountSnapOrViewDialog dialog = new MountSnapOrViewDialog(view, host, false);
        int width = 355 + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 255 + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize(width, height);
        dialog.setLocation(view.getCenterPoint(width, height));
        dialog.setVisible(true);
    }
}
