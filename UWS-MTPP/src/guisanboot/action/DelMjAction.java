/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.AbstractNetworkRunning;
import guisanboot.data.DeleteMjThread;
import guisanboot.data.MirrorGrp;
import guisanboot.data.MirrorJob;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefLogMirrorJobList;
import guisanboot.ui.ChiefMirrorJobList;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefLMj;
import guisanboot.ui.ProcessEventOnChiefMj;
import guisanboot.ui.ProcessEventOnChiefMjMg;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.entity.ChiefCopyJobList;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefCj;
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
public class DelMjAction extends GeneralActionForMainUi{
    public DelMjAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delMj",
            MenuAndBtnCenterForMainUi.FUNC_DEL_MJ
        );
    }

    @Override public void doAction(ActionEvent evt){
        boolean isOk;

SanBootView.log.info(getClass().getName(),"########### Entering delete mirror job action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ){
SanBootView.log.info(getClass().getName(),"selobj is null! \n########### End of delete mirror job action. " );
            return;
        }
        if( !( selObj instanceof MirrorJob ) ){
SanBootView.log.info(getClass().getName(),"bad type !\n ########### End of delete mirror job action. " );
            return;
        }

        MirrorJob mj = (MirrorJob)selObj;
        if( mj.isMJStart() ){
            JOptionPane.showMessageDialog(view,
                SanBootView.res.getString("MenuAndBtnCenter.error.mjisrunning")
            );
            return;
        }

        int retVal =  JOptionPane.showConfirmDialog(
            view,
            mj.isCjType()?SanBootView.res.getString("MenuAndBtnCenter.confirm23"):SanBootView.res.getString("MenuAndBtnCenter.confirm13"),
            SanBootView.res.getString("common.confirm")+"[" + mj.getMj_job_name() +"]",  //"Confirm",
            JOptionPane.OK_CANCEL_OPTION
        );
        if( ( retVal == JOptionPane.CANCEL_OPTION ) || ( retVal == JOptionPane.CLOSED_OPTION ) ){
            return;
        }

        Object[] ret = view.getSomtthingOnTreeFromMjObj( mj );
        if( ret == null ) return;

        Object hostObj = ret[0];
        int rootid =((Integer)ret[1]).intValue();
        String vol_name = (String)ret[2];
        String vol_mp = (String)ret[3];
        int ptype = ((Integer)ret[4]).intValue();

        if( hostObj != null ){
            boolean delHostInfo = false;

            // delete the following codes because user is difficulty to understand "删除该镜像任务对应的主机信息吗?"
            // 的含义 （2010.5.27 ）
            /*
            if( mj.isRemoteMjType() ){
                delHostInfo = true;
                retVal =  JOptionPane.showConfirmDialog(
                    view,
                    SanBootView.res.getString("MenuAndBtnCenter.confirm20"),
                    SanBootView.res.getString("common.confirm"),  //"Confirm",
                    JOptionPane.OK_CANCEL_OPTION
                );
                if( ( retVal == JOptionPane.CANCEL_OPTION ) || ( retVal == JOptionPane.CLOSED_OPTION ) ){
                    delHostInfo = false;
                }
            }
            */

            if( mj.isCjType() ){
                isOk = view.initor.mdb.delMj( mj.getMj_id() );
            }else{
SanBootView.log.info(getClass().getName(), "test whether destination swu server can be connected.");
                view.initor.mdb.targetSrvName = "";
                view.initor.mdb.getHostName(
                    mj.getMj_dest_ip(),
                    mj.getMj_dest_port(),
                    mj.getMj_dest_pool(),
                    mj.getMj_dest_pool_passwd()
                );
                if( view.initor.mdb.getErrorCode() != AbstractNetworkRunning.OK ){
                    retVal =  JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm24"),
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if( ( retVal == JOptionPane.CANCEL_OPTION ) || ( retVal == JOptionPane.CLOSED_OPTION ) ){
                        return;
                    }else{
                        isOk = view.initor.mdb.delMj( mj.getMj_id() );
                    }
                }else{
                    // 清除targetSrvName,免得影响其他代码（宁肯重新获取）
                    view.initor.mdb.targetSrvName = "";
                    DeleteMjThread thread = new DeleteMjThread( view, mj, 0, hostObj, rootid, vol_name,vol_mp,delHostInfo,ptype );
                    view.startupProcessDiag(
                        SanBootView.res.getString("View.pdiagTitle.delMj"),
                        SanBootView.res.getString("View.pdiagTip.delMj"),
                        thread
                    );
                    isOk = thread.isOK();
                }
            }
        }else{
            isOk = view.initor.mdb.delMj( mj.getMj_id() );
        }

        // 将对应的mg也删除掉，否则有可能服务器端还有对应的进程( start_mirror )
        MirrorGrp mg = view.initor.mdb.getMGFromVectorOnRootID( rootid );
        if( mg != null && ( mg.getMg_type() != MirrorGrp.MG_TYPE_RADISK ) ){
SanBootView.log.info(getClass().getName(), "Try to delete corresponding mirror group object.if failed, do not care.");

            // find mj list related to this mg. one mg can be shared by many mj
            ArrayList mjList = view.initor.mdb.getMjListFromVecOnMgID( mg.getMg_id() );
            if( mjList.size() ==1 ){
                if( view.initor.mdb.delMg( mg.getMg_id() ) ){
                    view.initor.mdb.removeMGFromVector( mg );
                }
            }
        }

        if( isOk ){
            view.initor.mdb.removeMJFromVector( mj );

            BrowserTreeNode mjFNode = mj.getFatherNode();
            Object fMjObj = mjFNode.getUserObject();
            view.setCurNode( mjFNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            TreePath path = new TreePath( mjFNode.getPath() );
            if( fMjObj instanceof ChiefMirrorJobList ){
                BrowserTreeNode selMjNode = view.getMjNodeOnMjName( mjFNode,mj.toTreeString() );
                if( selMjNode!= null ){
                    view.removeNodeFromTree( mjFNode, selMjNode );
                }

                ProcessEventOnChiefMj peOnChiefMj = new ProcessEventOnChiefMj( view );
                peOnChiefMj.processTreeSelection( path );
                peOnChiefMj.controlMenuAndBtnForTreeEvent();
            }else if( fMjObj instanceof ChiefLogMirrorJobList ){
                BrowserTreeNode selMjNode = view.getMjNodeOnMjName( mjFNode,mj.toTreeString() );
                if( selMjNode!= null ){
                    view.removeNodeFromTree( mjFNode, selMjNode );
                }
                ProcessEventOnChiefLMj peOnChiefLMj = new ProcessEventOnChiefLMj( view );
                peOnChiefLMj.processTreeSelection( path );
                peOnChiefLMj.controlMenuAndBtnForTreeEvent();
            }else if( fMjObj instanceof ChiefCopyJobList ){
                BrowserTreeNode selMjNode = view.getMjNodeOnMjName( mjFNode,mj.toTreeString() );
                if( selMjNode!= null ){
                    view.removeNodeFromTree( mjFNode, selMjNode );
                }
                ProcessEventOnChiefCj peOnChiefCj = new ProcessEventOnChiefCj( view );
                peOnChiefCj.processTreeSelection( path );
                peOnChiefCj.controlMenuAndBtnForTreeEvent();
            }else{
                ProcessEventOnChiefMjMg peOnChiefMjMg = new ProcessEventOnChiefMjMg( view );
                peOnChiefMjMg.processTreeSelection( path );
                peOnChiefMjMg.controlMenuAndBtnForTreeEvent();
            }
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }else{
            if( hostObj == null ){
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_DEL_MJ ) +" : "+view.initor.mdb.getErrorMessage()
                );
                return;
            }
        }
SanBootView.log.info(getClass().getName(),"########### End of delete mirror job action. " );
    }
}
