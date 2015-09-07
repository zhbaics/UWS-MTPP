/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.MirrorJob;
import guisanboot.data.SourceAgent;
import guisanboot.data.StartorStopMjThread;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefMirrorJobList;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefMj;
import guisanboot.ui.ProcessEventOnChiefMjMg;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.entity.ChiefCopyJobList;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefCj;
import guisanboot.unlimitedIncMj.service.StartorStopCjThread;
import guisanboot.unlimitedIncMj.service.StartorStopUnlimitedIncMjThread;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class StartMjAction extends GeneralActionForMainUi{
    boolean quickStart;

    public StartMjAction( Icon icon1,Icon icon2,String title,int funcID,boolean quickStart ){
        super(
            icon1,
            icon2,
            title,
            funcID
        );
        this.quickStart = quickStart;
    }

    public StartMjAction( boolean quickStart ){
        super(
            ResourceCenter.startIcon,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.startMj",
            MenuAndBtnCenterForMainUi.FUNC_START_MJ
        );
        this.quickStart = quickStart;
    }

    @Override public void doAction(ActionEvent evt){
        Object hostObj = null;
        int rootid,ptype;
        String vol_name="";
        String vol_mp="";

SanBootView.log.info(getClass().getName(),"########### Entering start mirror job action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;
        if( !( selObj instanceof MirrorJob ) ) return;

        MirrorJob mj = (MirrorJob)selObj;
        Object[] ret = view.getSomtthingOnTreeFromMjObj( mj );
        if( ret == null ) return;

        hostObj = ret[0];
        rootid =((Integer)ret[1]).intValue();
        vol_name = (String)ret[2];
        vol_mp = (String)ret[3];
        ptype = ((Integer)ret[4]).intValue();

        String bootMac = "";
        if( hostObj != null ){
            if( hostObj instanceof BootHost ){
                BootHost bh = (BootHost)hostObj;
                if( !bh.isWinHost() ){
                    boolean ok = view.initor.mdb.getUnixNetCardInfo( ResourceCenter.CLT_IP_CONF + "/" + bh.getID() + ResourceCenter.CONF_IP );
                    if( ok ){
                        bootMac = view.initor.mdb.getUnixBootMac();
                        if( bootMac.equals( "" ) ){
                            JOptionPane.showMessageDialog( view,
                                SanBootView.res.getString("EditProfileDialog.error.getBootMac")
                            );
                            return;
                        }
                    }
                }
            }else if( hostObj instanceof SourceAgent ){
                SourceAgent sa = (SourceAgent)hostObj;
                if( !sa.isWinHost() ){
                    boolean ok = view.initor.mdb.getUnixNetCardInfo( ResourceCenter.CLT_IP_CONF + "/" + ResourceCenter.PREFIX_SRC_AGNT + sa.getSrc_agnt_id() + ResourceCenter.CONF_IP );
                    if( ok ){
                        bootMac = view.initor.mdb.getUnixBootMac();
                        if( bootMac.equals( "" ) ){
                            JOptionPane.showMessageDialog( view,
                                SanBootView.res.getString("EditProfileDialog.error.getBootMac")
                            );
                            return;
                        }
                    }
                }
            }
        }

        boolean isOk;
        if( mj.isNormalMjType() ){
SanBootView.log.info(getClass().getName(), "this mirror job is a normal job.");
            StartorStopMjThread thread1 = new StartorStopMjThread( view, mj, 0,hostObj, rootid, vol_name,vol_mp,bootMac,quickStart,ptype );
            view.startupProcessDiag(
                SanBootView.res.getString("View.pdiagTitle.startMj")+" [ "+mj.getMj_job_name() +" ] ",
                SanBootView.res.getString("View.pdiagTip.startMj"),
                thread1
            );
            isOk = thread1.isOk();
        }else if( mj.isIncMjType() ){
SanBootView.log.info(getClass().getName(), "this mirror job is an incremental job.");
            StartorStopUnlimitedIncMjThread thread2 = new StartorStopUnlimitedIncMjThread( view,mj,0,hostObj,rootid,vol_name,vol_mp,bootMac,quickStart,ptype );
            view.startupProcessDiag(
                SanBootView.res.getString("View.pdiagTitle.startMj")+" [ "+mj.getMj_job_name() +" ] ",
                SanBootView.res.getString("View.pdiagTip.startMj"),
                thread2
            );
            isOk = thread2.isOk();
        }else{
SanBootView.log.info(getClass().getName(), "this mirror job is a copy job.");
            StartorStopCjThread thread3 = new StartorStopCjThread( view,mj,0,hostObj,rootid,vol_name,vol_mp,bootMac,quickStart,ptype );
            view.startupProcessDiag(
                SanBootView.res.getString("View.pdiagTitle.startCj")+" [ "+mj.getMj_job_name() +" ] ",
                SanBootView.res.getString("View.pdiagTip.startCj"),
                thread3
            );
            isOk = thread3.isOk();
        }

        if( isOk ){
            mj.setMj_job_status( MirrorJob.MJ_STATUS_START );

            BrowserTreeNode mjFNode = mj.getFatherNode();
            Object fObj = mjFNode.getUserObject();
            view.setCurNode( mjFNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            TreePath path = new TreePath( mjFNode.getPath() );
            if( fObj instanceof ChiefCopyJobList ){
                ProcessEventOnChiefCj peOnChiefCj = new ProcessEventOnChiefCj( view );
                peOnChiefCj.processTreeSelection( path );
                peOnChiefCj.controlMenuAndBtnForTreeEvent();
            }else if( fObj instanceof ChiefMirrorJobList  ){
                ProcessEventOnChiefMj peOnChiefMj = new ProcessEventOnChiefMj( view );
                peOnChiefMj.processTreeSelection( path );
                peOnChiefMj.controlMenuAndBtnForTreeEvent();
            }else{
                ProcessEventOnChiefMjMg peOnChiefMjMg = new ProcessEventOnChiefMjMg( view );
                peOnChiefMjMg.processTreeSelection( path );
                peOnChiefMjMg.controlMenuAndBtnForTreeEvent();
            }
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }
SanBootView.log.info(getClass().getName(),"########### End of start mirror job action. " );
    }
}
