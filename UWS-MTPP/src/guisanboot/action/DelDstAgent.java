/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.DestAgent;
import guisanboot.data.SnapUsage;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefNetBootHost;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class DelDstAgent extends GeneralActionForMainUi {
    public DelDstAgent(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.SMALL_EDIT_PASSWORD,
            "View.MenuItem.delDstAgnt",
            MenuAndBtnCenterForMainUi.FUNC_DEL_DSTAGNT
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering delete dst-agent(netboot host) action ");
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !(selObj instanceof DestAgent) )  return;

        int ret = JOptionPane.showConfirmDialog(
            view,
            SanBootView.res.getString("MenuAndBtnCenter.confirm18"),
            SanBootView.res.getString("common.confirm"),  //"Confirm",
            JOptionPane.OK_CANCEL_OPTION
        );
        if(  ( ret == JOptionPane.CANCEL_OPTION ) || (  ret == JOptionPane.CLOSED_OPTION) ){
            return;
        }

        DestAgent da = (DestAgent)selObj;
        DelNetBootHost thread = new DelNetBootHost( view,da );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.delNBHost"),
            SanBootView.res.getString("View.pdiagTip.delNBHost"),
            thread
        );
SanBootView.log.info(getClass().getName(),"########### End of delete dst-agent(netboot host) action. " );
    }

    class DelNetBootHost extends BasicGetSomethingThread{
        DestAgent da;
        BrowserTreeNode selHostNode;
        BrowserTreeNode fNode;

        public DelNetBootHost( SanBootView view,DestAgent da ){
            super( view );
            this.da = da;
            this.selHostNode   = da.getTreeNode();
            this.fNode = da.getFatherNode();
        }

        public boolean realRun(){
             if( !delNetBootHost() ){ return false; }
             return this.realDelHost( fNode,selHostNode );
        }

        private boolean delNetBootHost(){
            int i,size;
            SnapUsage su;
            boolean aIsOk;

            int da_id = da.getDst_agent_id();
            aIsOk = view.delConfigFileUsingByNetBootHost( da_id );
            if( !aIsOk ){
                errMsg = view.getErrMsg();
                return false;
            }

            ArrayList list = view.initor.mdb.getMSUFromCacheOnDstAgntID( da_id );
            size = list.size();
            for( i=0; i<size; i++ ){
                su = (SnapUsage)list.get(i);
                aIsOk = view.initor.mdb.delMSU( su.getUsage_id() );
                if( !aIsOk ){
                    errMsg = view.initor.mdb.getErrorMessage();
                    return false;
                }else{
                    view.initor.mdb.removeMSUFromCache( su );
                }
            }

            aIsOk = view.initor.mdb.delNBH( da_id );
            if( !aIsOk ){
                errMsg = view.initor.mdb.getErrorMessage();
                return false;
            }

            if( da.isWinHost() ){
                view.initor.mdb.delIboot( da.getDst_agent_mac() );
            }

            return true;
        }

        private boolean realDelHost( BrowserTreeNode chiefHostNode,BrowserTreeNode selHostNode ){
            view.initor.mdb.removeNBHFromCache( da );
            if( selHostNode != null ){
                view.removeNodeFromTree( chiefHostNode, selHostNode );
            }

            // 显示点击 chiefHostNode 后的右边tabpane中的内容�
            view.setCurNode( chiefHostNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefNetBootHost peOnChiefHost = new ProcessEventOnChiefNetBootHost( view );
            TreePath path = new TreePath( chiefHostNode.getPath() );
            peOnChiefHost.processTreeSelection( path );
            peOnChiefHost.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();

            return isOk;
        }
    }
}
