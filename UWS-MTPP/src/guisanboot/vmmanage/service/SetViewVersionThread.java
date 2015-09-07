/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.service;

import guisanboot.data.VMHostInfo;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.ChiefVMHost;
import guisanboot.ui.ProcessEventOnChiefVMHost;
import guisanboot.ui.SanBootView;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author Administrator
 */
public class SetViewVersionThread extends BasicGetSomethingThread {

    private VMHostInfo vmhost;
    private ChiefVMHost cvmhost;

    public SetViewVersionThread(SanBootView _view, VMHostInfo _vmhost, ChiefVMHost _cvmhost) {
        super(_view);
        this.vmhost = _vmhost;
        this.cvmhost = _cvmhost;
    }

    @Override
    public boolean realRun() {

        boolean isOk = view.initor.mdb.changeDiskVMHost(vmhost);
        if (isOk) {
            isOk = view.initor.mdb.addVMHost(vmhost);
            if (!isOk) {
//                JOptionPane.showMessageDialog(view,
//                        SanBootView.res.getString("CreateVMHost.error.createfailed"));
                this.errMsg = SanBootView.res.getString("CreateVMHost.error.createfailed");
                return false;
            } else {
                updateUI();
                return true;
            }
        } else {
//            JOptionPane.showMessageDialog(view,
//                    SanBootView.res.getString("CreateVMHost.error.createfailed"));
            this.errMsg = SanBootView.res.getString("CreateVMHost.error.createfailed");
            return false;
        }
    }

    public void updateUI() {
        BrowserTreeNode cvmhostNode = cvmhost.getTreeNode();
        view.setCurNode(cvmhostNode);
        view.setCurBrowserEventType(Browser.TREE_SELECTED_EVENT);
        TreePath path = new TreePath(cvmhostNode.getPath());
        ProcessEventOnChiefVMHost processEvent = new ProcessEventOnChiefVMHost(view);
        processEvent.processTreeSelection(path);
        processEvent.processTreeExpand(path);
        processEvent.controlMenuAndBtnForTreeEvent();
        view.getTree().setSelectionPath(path);
        view.getTree().requestFocus();
    }
}
