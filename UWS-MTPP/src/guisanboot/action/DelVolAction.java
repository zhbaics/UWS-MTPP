/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.data.LunMap;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.MirrorGrp;
import guisanboot.data.MirrorJob;
import guisanboot.data.View;
import guisanboot.data.Volume;
import guisanboot.datadup.data.DBSchedule;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefOrphanVolume;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefOrphanVol;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class DelVolAction extends GeneralActionForMainUi{
    public DelVolAction(){
        super(
            ResourceCenter.ICON_DELETE,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delVol",
            MenuAndBtnCenterForMainUi.FUNC_DEL_VOL
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering delete vol action. " );
        Object[] selObj = view.getMulSelectedObjFromSanBoot();
        if( selObj == null || selObj.length <= 0 ) return;

        int ret =  JOptionPane.showConfirmDialog(
            view,
            SanBootView.res.getString("MenuAndBtnCenter.confirm1"),
            SanBootView.res.getString("common.confirm"),  //"Confirm",
            JOptionPane.OK_CANCEL_OPTION
        );
        if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION ) ){
            return;
        }

        if(  !view.initor.mdb.updateMDI() ){
            view.showError1(
                ResourceCenter.CMD_GET_MIRROR_DISK,
                view.initor.mdb.getErrorCode(),
                view.initor.mdb.getErrorMessage()
            );
SanBootView.log.info(getClass().getName(),"failed to get MDI.\n########### End of delete vol action." );
            return;
        }

        ProgressDialog pdiag = new ProgressDialog(
            view,
            SanBootView.res.getString("View.pdiagTitle.delvol"),
            SanBootView.res.getString("View.pdiagTip.delvol")
        );
        DelVol delvol = new DelVol( pdiag,selObj,view );
        delvol.start();
        pdiag.mySetSize();
        pdiag.setLocation( view.getCenterPoint( pdiag.getDefWidth(),pdiag.getDefHeight() ) );
        pdiag.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of delete vol action." );
    }

    class DelVol extends Thread{
        ProgressDialog pdiag;
        Object[] vols;
        SanBootView view;

        public DelVol( ProgressDialog pdiag,Object[] vols,SanBootView view ){
            this.pdiag = pdiag;
            this.vols = vols;
            this.view = view;
        }

        Runnable close = new Runnable(){
            public void run(){
                pdiag.dispose();
            }
        };

        @Override public void run(){
            Volume vol;
            MirrorGrp mg;
            MirrorJob mj;
            boolean isOk,hasActiveMj,isGoing;
            BrowserTreeNode fNode,chiefOrphVolNode;
            ChiefOrphanVolume chiefOrphVol;
            Vector<LunMap> lmList;
            ArrayList<Volume> activeMj = new ArrayList<Volume>();
            ArrayList<MirrorJob> mjList;
            int size1,size2,j,tid,k,m,size3,i,size,size4,mg_id;
            LunMap lm;
            Audit audit;

            vol = (Volume)vols[0];
            fNode = vol.getFatherNode();
            chiefOrphVol = (ChiefOrphanVolume)fNode.getUserObject();
            chiefOrphVolNode = chiefOrphVol.getTreeNode();

            size = vols.length;
            for( i=0; i<size; i++ ){
                if( !( vols[i] instanceof Volume) ) continue;

                audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_DEL_VOL );

                hasActiveMj = false;
                vol = (Volume)vols[i];
                fNode = vol.getFatherNode();
                tid = vol.getTargetID();

                mg = view.initor.mdb.getMGFromVectorOnRootID( vol.getSnap_root_id() );
                if( mg != null ){
                    mjList = view.initor.mdb.getIncMjListFromVecOnSrcRootIdOrMgID( vol.getSnap_root_id(),mg.getMg_id() );
                }else{
                    mjList = view.initor.mdb.getIncMjListFromVecOnSrcRootId( vol.getSnap_root_id() );
                }

                size3 = mjList.size();
                for( m=0; m<size3; m++ ){
                    mj = (MirrorJob)mjList.get( m );
                    if( mj.isMJStart() ){
                        activeMj.add( vol );
                        hasActiveMj = true;
                        break;
                    }
                }

                if( hasActiveMj ){
                    continue;
                }

                // 首先删除老的、没用的lunmap,不管是否成功删除
                isOk = view.initor.mdb.getLunMapForTID( tid );
                if( isOk ){
                    lmList = view.initor.mdb.getAllLunMapForTid();
                    size1 = lmList.size();
                    for( j=0;j<size1;j++ ){
                        lm = (LunMap)lmList.elementAt( j );
                        view.initor.mdb.delLunMap( tid, lm.getIP(),lm.getMask(), lm.getAccessMode() );
                    }
                }

                //删除虚拟磁盘卷对应的提前预删除快照调度
                ArrayList schList = view.initor.mdb.getAutoDelSch();
                if(schList != null && schList.size() > 0) {
                    int ischsize = schList.size();
                    for( int is = 0 ; is < ischsize ; is++){
                        DBSchedule tmpADSch = ( DBSchedule )schList.get(is);
                        if( tmpADSch.getProfName().split(" ").length>1 && tmpADSch.getProfName().split(" ")[1].equals(String.valueOf(vol.getSnap_root_id())) ){
                            view.initor.mdb.deleteOneScheduler(tmpADSch);
                        }
                    }
                    view.initor.mdb.updateCronScheduler();
                }

                // 再删除disk上所有快照的view的lunmap
                isOk = view.initor.mdb.getAllView( vol.getSnap_root_id() );
                if( isOk ){
                    ArrayList viewList = view.initor.mdb.getViewList();
                    size2 = viewList.size();
                    for( k=0; k<size2; k++ ){
                        View viewObj = (View)viewList.get( k );
                        isOk = view.initor.mdb.getLunMapForTID( viewObj.getSnap_target_id() );
                        if( isOk ){
                            lmList = view.initor.mdb.getAllLunMapForTid();
                            size1 = lmList.size();
                            for( j=0;j<size1;j++ ){
                                lm = (LunMap)lmList.elementAt(j);
                                isOk = view.initor.mdb.delLunMap( viewObj.getSnap_target_id(), lm.getIP(),lm.getMask(), lm.getAccessMode() );
                                /*
                                if( !isOk ){
                                    JOptionPane.showMessageDialog(view,
                                        ResourceCenter.getCmd( ResourceCenter.CMD_DEL_LUNMAP) + " "+ viewObj.getSnap_target_id() +" "+lm.getIP() +" "+lm.getMask() +" "+lm.getAccessMode() +" "+
                                            view.initor.mdb.getErrorMessage()
                                    );
                                    return;
                                }
                                 */
                            }
                        }else{
                            /*
                            JOptionPane.showMessageDialog(view,
                                ResourceCenter.getCmd( ResourceCenter.CMD_GET_LUNMAP) + " [ " +viewObj.getSnap_target_id() + " ] "+
                                    view.initor.mdb.getErrorMessage()
                            );
                            return;
                             */
                        }
                    }
                }else{
                    /*
                    JOptionPane.showMessageDialog(view,
                        ResourceCenter.getCmd( ResourceCenter.CMD_GET_VIEW) + " "+
                            view.initor.mdb.getErrorMessage()
                    );
                    return;
                     */
                }

                isGoing = true;

                // delete mj(mg) on vol
                if( mg != null ){
                    // 如果mg上有active的mj，那么就不会到达这里（前面检查了是否存在active mj),
                    // 只能先把mj停止才行。但是，对于cmdp来说，即使mg上面没有mj，start_mirror也
                    // 可能开启（用于自动创建快照）。由于这里不知道是否为CMDP还是mtpp，所以统一
                    // 检查mg的状态。
                    if( !view.initor.mdb.checkMg( mg.getMg_id() ) ){
                        // mg 已经停止了
                        isOk = true;
                    }else{
                        isOk = view.initor.mdb.stopMg( mg.getMg_id() );
                    }

                    if( isOk ){
                        if( !view.initor.mdb.delMg( mg.getMg_id() ) ){
                            view.showError1(
                                ResourceCenter.CMD_DEL_MG,
                                view.initor.mdb.getErrorCode(),
                                view.initor.mdb.getErrorMessage()
                            );
                            isGoing = false;
                        }else{
                            view.initor.mdb.removeMGFromVector( mg );
                            // 成功删除mg后系统会自动将mdb中相关的mj(普通镜像任务)删除
                        }
                    }else{
                        isGoing = false;
                    }
                }

                if( isGoing ){
                    // 删除该mj对应的无限增量镜像卷
                    if( !delUIMVol( vol.getSnap_root_id() ) ){
                        isGoing = false;
                    }
                }

                if( isGoing ){
                    // 删除mj对应的克隆盘
                    if( !delCloneDisk( vol.getSnap_root_id() ) ){
                        isGoing = false;
                    }
                }

                if( isGoing ){
                    size3 = mjList.size();
                    for( m=0; m<size3; m++ ){
                        mj = (MirrorJob)mjList.get( m );
                        if( mj.getMj_mg_id() == -1 ){ // 无限增量镜像卷
                            if( !view.initor.mdb.delMj( mj.getMj_id() ) ){
                                view.showError1(
                                    ResourceCenter.CMD_DEL_MJ,
                                    view.initor.mdb.getErrorCode(),
                                    view.initor.mdb.getErrorMessage()
                                );
                                isGoing = false;
                                break;
                            }else{
                                view.initor.mdb.removeMJFromVector( mj );
                            }
                        }else{ // 普通镜像卷
                            view.initor.mdb.removeMJFromVector( mj );
                        }
                    }
                }

                if( isGoing ){
                    view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
                    isOk = view.initor.mdb.delVolume( vol );
                    view.initor.mdb.restoreOldTimeOut();
                    if( isOk ){
                        audit.setEventDesc( "Delete volume: " + vol.getSnap_name() + " successfully.");
                        view.audit.addAuditRecord( audit );

                        BrowserTreeNode selVolNode = view.getOrphVolNodeOnChiefOrphVol( chiefOrphVolNode,vol.getTargetID() );
                        if( selVolNode !=null ){
                            view.removeNodeFromTree( chiefOrphVolNode, selVolNode );
                        }
                    }else{
                        audit.setEventDesc( "Failed to delete volume: " + vol.getSnap_name() );
                        view.audit.addAuditRecord( audit );

                        view.showError1(
                            ResourceCenter.CMD_DEL_VOL,
                            view.initor.mdb.getErrorCode(),
                            view.initor.mdb.getErrorMessage()
                        );
                    }
                }
            }

            try{
                SwingUtilities.invokeAndWait( close );
            }catch( Exception ex ){
                ex.printStackTrace();
            }

            size4 = activeMj.size();
            if( size4 >0 ){
                String errStr = SanBootView.res.getString("MenuAndBtnCenter.error.hasActiveMj")+":\n";
                boolean isFirst = true;
                for( i=0; i<size4; i++ ){
                    vol = (Volume)activeMj.get(i);
                    if( isFirst ){
                        errStr += vol.getSnap_name()+"["+vol.getSnap_target_id()+"]";
                        isFirst = false;
                    }else{
                        errStr += ", " + vol.getSnap_name()+"["+vol.getSnap_target_id()+"]";
                    }
                }
                JOptionPane.showMessageDialog( view, errStr );
            }

            if( size4 != size ){
                // 显示点击 chiefOrphVolNode 后的右边tabpane中的内容
                view.setCurNode( chiefOrphVolNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                ProcessEventOnChiefOrphanVol peOnChiefOrphanVol = new ProcessEventOnChiefOrphanVol( view );
                TreePath path = new TreePath( chiefOrphVolNode.getPath() );
                peOnChiefOrphanVol.processTreeSelection( path );
                peOnChiefOrphanVol.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }
        }

        private boolean delCloneDisk( int rootid ){
            boolean isOk = true, destroyOk;
            CloneDisk cd;

            isOk = view.initor.mdb.getCloneDiskList( -1,CloneDisk.IS_FREEVOL,rootid );
            if( isOk ){
                ArrayList list = view.initor.mdb.getCloneDiskList();
                int size = list.size();
                for( int i=0; i<size; i++ ){
                    cd = (CloneDisk)list.get(i);

                    delLunMapOnDisk( cd.getTarget_id() );
                    delLunMapOnView( cd.getRoot_id() );

                    view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
                    destroyOk = view.initor.mdb.destroyDisk( cd.getRoot_id() );
                    view.initor.mdb.restoreOldTimeOut();
                    if( !destroyOk && ( view.initor.mdb.getErrorCode() == 100 )  ){
                        // cd.getRoot_id()代表的disk本身就不存在
                        destroyOk = true;
                    }

                    if( !destroyOk ){
                        view.showError1(
                            ResourceCenter.CMD_DESTORY_DISK,
                            view.initor.mdb.getErrorCode(),
                            view.initor.mdb.getErrorMessage()
                        );
                        isOk = false;
                        break;
                    }else{
                        if( !view.initor.mdb.delCloneDisk( "", 5010, 1, "", cd.getId() ) ){
                            view.showError1(
                                ResourceCenter.CMD_DEL_CLONE_DISK,
                                view.initor.mdb.getErrorCode(),
                                view.initor.mdb.getErrorMessage()
                            );
                            isOk = false;
                            break;
                        }
                    }
                }
            }else{
                view.showError1(
                    ResourceCenter.CMD_GET_CLONE_DISK,
                    view.initor.mdb.getErrorCode(),
                    view.initor.mdb.getErrorMessage()
                );
            }

            return isOk;
        }

        private boolean delUIMVol( int rootid ){
            MirrorDiskInfo mdi;
            boolean isOk = true,destroyOk;

            ArrayList list = view.initor.mdb.getMDIFromCacheOnHostIDandRootID( -1,rootid );
            int size = list.size();
            for( int i=0; i<size; i++ ){
                mdi = (MirrorDiskInfo)list.get( i );
                if( !mdi.isLocalMirrorVol() ) continue;

                view.initor.mdb.setNewTimeOut( ResourceCenter.MAX_TIMEOUT );
                destroyOk = view.initor.mdb.destroyDisk( mdi.getSnap_rootid() );
                view.initor.mdb.restoreOldTimeOut();
                if( !destroyOk && ( view.initor.mdb.getErrorCode() == 100 )  ){
                    // mdi.getSnap_rootid()代表的disk本身就不存在
                    destroyOk = true;
                }

                if(  destroyOk ){
                    if( view.initor.mdb.delMDI( mdi.getSnap_rootid() ) ){
                        view.initor.mdb.removeMDIFromCache( mdi );
                    }else{
                        view.showError1(
                            ResourceCenter.CMD_DEL_MIRROR_DISK,
                            view.initor.mdb.getErrorCode(),
                            view.initor.mdb.getErrorMessage()
                        );
                        isOk = false;
                        break;
                    }
                }else{
                    view.showError1(
                        ResourceCenter.CMD_DESTORY_DISK,
                        view.initor.mdb.getErrorCode(),
                        view.initor.mdb.getErrorMessage()
                    );
                    isOk = false;
                    break;
                }
            }

            return isOk;
        }

        private void delLunMapOnDisk( int tid ){
            // 首先删除老的、没用的lunmap,不管是否成功删除
            boolean isOk = view.initor.mdb.getLunMapForTID( tid );
            if( isOk ){
                Vector<LunMap> lmList = view.initor.mdb.getAllLunMapForTid();
                int size = lmList.size();
                for( int j=0;j<size;j++ ){
                    LunMap lm = (LunMap)lmList.elementAt( j );
                    view.initor.mdb.delLunMap( tid, lm.getIP(),lm.getMask(), lm.getAccessMode() );
                }
            }
        }

        private void delLunMapOnView( int root_id ){
            // 再删除disk上所有快照的view的lunmap
            boolean isOk = view.initor.mdb.getAllView( root_id );
            if( isOk ){
                ArrayList<View> viewList = view.initor.mdb.getViewList();
                int size2 = viewList.size();
                for( int k=0; k<size2; k++ ){
                    View viewObj = (View)viewList.get( k );
                    isOk = view.initor.mdb.getLunMapForTID( viewObj.getSnap_target_id() );
                    if( isOk ){
                        Vector<LunMap> lmList = view.initor.mdb.getAllLunMapForTid();
                        int size1 = lmList.size();
                        for( int j=0;j<size1;j++ ){
                            LunMap lm = (LunMap)lmList.elementAt(j);
                            isOk = view.initor.mdb.delLunMap( viewObj.getSnap_target_id(), lm.getIP(),lm.getMask(), lm.getAccessMode() );
                        }
                    }
                }
            }
        }
    }
}
