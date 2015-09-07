/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

import guisanboot.res.ResourceCenter;

import guisanboot.data.View;
import guisanboot.data.Snapshot;
import guisanboot.datadup.ui.viewobj.ChiefScheduler;
import guisanboot.datadup.ui.viewobj.ChiefADScheduler;
import guisanboot.datadup.ui.viewobj.ChiefProfile;
import guisanboot.datadup.data.UniProfile;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnChiefSch;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnChiefADSch;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnChiefProfile;
import guisanboot.datadup.ui.ProcessEvent.ProcessEventOnProfile;
import guisanboot.remotemirror.ChiefMJScheduler;
import guisanboot.remotemirror.service.ProcessEventOnChiefMjScheduler;
import guisanboot.remotemirror.ChiefRollbackHost;
import guisanboot.remotemirror.ProcessEventOnChiefRollbackHost;
import guisanboot.ui.*;
import guisanboot.cmdp.entity.ChiefPPProfile;
import guisanboot.cmdp.service.ProcessEventOnChiefPPProfile;
import guisanboot.unlimitedIncMj.entity.ChiefLocalUnLimitedIncMirrorVolList;
import guisanboot.unlimitedIncMj.entity.ChiefCloneDiskList;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefUnlimitedIncMirrorVol;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefCloneDisk;
/**
 * 刷新功能
 * @author Administrator
 */
public class RefreshAction extends GeneralActionForMainUi{
    public RefreshAction(){
        super(
          ResourceCenter.ICON_REFRESH_20,
          ResourceCenter.ICON_REFRESH_50,
          "View.MenuItem.refresh",
          MenuAndBtnCenterForMainUi.FUNC_REFRESH
        );
    }
    @Override public void doAction( ActionEvent evt ){
        SanBootView.log.info(getClass().getName(), "########### Entering of Refresh Action :");
        ChiefNode chiefNode ;
        GeneralProcessEventForSanBoot peOnSelObj;
        Object selObj = view.getSelectedObjFromSanBoot();

        if(selObj instanceof ChiefOrphanVolume){
            if(view.initor.mdb.updateOrphanVol()){  //刷新空闲卷列表
                chiefNode   = (ChiefOrphanVolume)selObj;
                peOnSelObj = new ProcessEventOnChiefOrphanVol( view );
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")
                        +
                        " :" + view.initor.mdb.getErrorMessage() );
            }

        } else if(selObj instanceof ChiefScheduler){    //刷新逻辑保护调度
            if(view.initor.mdb.updateCronScheduler()){
                chiefNode = (ChiefScheduler)selObj;
                peOnSelObj = new ProcessEventOnChiefSch(view);
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if( selObj instanceof ChiefADScheduler){     //刷新提前预删除快照调度
            if(view.initor.mdb.updateCronScheduler()){
                chiefNode = (ChiefADScheduler)selObj;
                peOnSelObj = new ProcessEventOnChiefADSch(view);
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if( selObj instanceof ChiefMJManage ){       //刷新镜像任务管理
            if(view.initor.mdb.updateMj()){
                chiefNode = (ChiefMJManage)selObj;
                peOnSelObj = new ProcessEventOnChiefMjMg(view);
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if( selObj instanceof ChiefMJScheduler ){    //刷新镜像时间计划
            if( view.initor.mdb.updateMjSch() ) {
                chiefNode = (ChiefMJScheduler)selObj;
                peOnSelObj = new ProcessEventOnChiefMjScheduler(view);
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if ( selObj instanceof ChiefDestUWS ){
            if( view.initor.mdb.updateUWSrv() ){        //刷新镜像服务器
                chiefNode = (ChiefDestUWS)selObj;
                peOnSelObj = new ProcessEventOnChiefDestUWS(view);
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if ( selObj instanceof ChiefRollbackHost ){
            if( view.initor.mdb.updateSrcAgnt() ){          //刷新回滚主机列表
                chiefNode = ( ChiefRollbackHost )selObj;
                peOnSelObj = new ProcessEventOnChiefRollbackHost(view);
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if ( selObj instanceof ChiefPool ){
            if( view.initor.mdb.updatePool() ){         //刷新存储池列表
                chiefNode = ( ChiefPool )selObj;
                peOnSelObj = new ProcessEventOnChiefPool(view);
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if ( selObj instanceof ChiefHost ){
            if ( view.initor.mdb.updateBootHost() ){        //刷新主机列表
                chiefNode = ( ChiefHost )selObj;
                peOnSelObj = new ProcessEventOnChiefHost( view );
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if ( selObj instanceof ChiefHostVolume ) {
            if ( view.initor.mdb.updateVolumeMap() ){           //刷新主机卷列表
                chiefNode = ( ChiefHostVolume )selObj;
                peOnSelObj = new ProcessEventOnChiefHostVol( view ) ;
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if ( selObj instanceof ChiefPPProfile) {
            if ( view.initor.mdb.updatePPProfileList() ) {          //刷新物理保护策略
                chiefNode = ( ChiefPPProfile )selObj;
                peOnSelObj = new ProcessEventOnChiefPPProfile( view );
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if ( selObj instanceof ChiefProfile ) {
            if ( view.initor.mdb.updateProfile() ) {                //刷新逻辑保护策略
                chiefNode = ( ChiefProfile ) selObj;
                peOnSelObj = new ProcessEventOnChiefProfile( view );
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if ( selObj instanceof ChiefNetBootHost ) {
            if ( view.initor.mdb.updateNBH() ) {                    //刷新网络启动主机列表
                chiefNode = ( ChiefNetBootHost ) selObj;
                peOnSelObj = new ProcessEventOnChiefNetBootHost( view );
                this.realDoRefresh(peOnSelObj, chiefNode);
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if ( selObj instanceof ChiefLunMap ) {               //刷新主机映射列表
            chiefNode = ( ChiefLunMap ) selObj;
            peOnSelObj = new ProcessEventOnChiefLunMap ( view ) ;
            this.realDoRefresh(peOnSelObj, chiefNode);
//            this.testForSuccess();

        } else if ( selObj instanceof ChiefSnapshot ) {             //刷新快照列表
            chiefNode = ( ChiefSnapshot ) selObj;
            peOnSelObj = new ProcessEventOnChiefSnap ( view ) ;
            this.realDoRefresh(peOnSelObj, chiefNode);
//            this.testForSuccess();

        } else if ( selObj instanceof ChiefMirrorJobList ) {         //刷新镜像任务列表
            chiefNode = ( ChiefMirrorJobList ) selObj;
            peOnSelObj = new ProcessEventOnChiefMj( view ) ;
            this.realDoRefresh(peOnSelObj, chiefNode);
//            this.testForSuccess();

        } else if ( selObj instanceof ChiefLocalUnLimitedIncMirrorVolList ) {
            chiefNode = ( ChiefLocalUnLimitedIncMirrorVolList ) selObj;     //刷新无限增量镜像卷列表
            peOnSelObj = new ProcessEventOnChiefUnlimitedIncMirrorVol ( view );
            this.realDoRefresh(peOnSelObj, chiefNode);
//            this.testForSuccess();

        } else if ( selObj instanceof ChiefCloneDiskList ) {
            chiefNode = ( ChiefCloneDiskList ) selObj;                      //刷新克隆盘列表
            peOnSelObj = new ProcessEventOnChiefCloneDisk ( view );
            this.realDoRefresh(peOnSelObj, chiefNode);
//            this.testForSuccess();

        } else if ( selObj instanceof Snapshot ) {
            Snapshot snapshot = ( Snapshot ) selObj;                //刷新快照节点
            peOnSelObj = new ProcessEventOnSnapshot( view );
            BrowserTreeNode snapshotNode = snapshot.getTreeNode();
            TreePath path = new TreePath( snapshotNode.getPath() );
            view.setCurNode(snapshotNode);
            view.setCurBrowserEventType(Browser.TREE_SELECTED_EVENT);
            peOnSelObj.processTreeExpand(path);
            peOnSelObj.processTreeSelection( path );
            peOnSelObj.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
//            this.testForSuccess();

        } else if ( selObj instanceof View ) {                      //刷新快照副本
            View objView = ( View )selObj;
            peOnSelObj = new ProcessEventOnView ( view );
            BrowserTreeNode viewNode = objView.getTreeNode();
            TreePath path = new TreePath( viewNode.getPath() );
            view.setCurNode(viewNode);
            view.setCurBrowserEventType(Browser.TREE_SELECTED_EVENT);
            peOnSelObj.processTreeExpand(path);
            peOnSelObj.processTreeSelection( path );
            peOnSelObj.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
//            this.testForSuccess();

        } else if ( selObj instanceof UniProfile ) {
            if ( view.initor.mdb.updateCronScheduler() ){               //刷新策略节点
                UniProfile uniprofile = ( UniProfile )selObj;
                BrowserTreeNode uniprofileNode = uniprofile.getTreeNode();
                peOnSelObj = new ProcessEventOnProfile( view );
                TreePath path = new TreePath( uniprofileNode.getPath() );
                view.setCurNode(uniprofileNode);
                view.setCurBrowserEventType(Browser.TREE_SELECTED_EVENT);
                peOnSelObj.processTreeExpand(path);
                peOnSelObj.processTreeSelection( path );
                peOnSelObj.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
//                this.testForSuccess();
            } else {
                JOptionPane.showMessageDialog(view, SanBootView.res.getString("common.error.refreshfailed")+
                        " :" +
                        view.initor.mdb.getErrorMessage() );
            }

        } else if ( selObj instanceof ChiefRemoteUWSManage ) {              //刷新镜像到本地的UWS
            ChiefRemoteUWSManage chiefReUWSM = ( ChiefRemoteUWSManage )selObj;
            BrowserTreeNode chiefReUWSMNode = chiefReUWSM.getTreeNode();
            peOnSelObj = new ProcessEventOnRemoteUWSManage( view );
            TreePath path = new TreePath( chiefReUWSMNode.getPath() );
            view.setCurNode(chiefReUWSMNode);
            view.setCurBrowserEventType(Browser.TREE_SELECTED_EVENT);
            peOnSelObj.processTreeExpand(path);
            peOnSelObj.processTreeSelection( path );
            peOnSelObj.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
//            this.testForSuccess();

        }


        SanBootView.log.info(getClass().getName(), "########### End of Refresh Action");
    }

    /**
     * 具体的刷新操作方法
     * @param peOnSelObj
     * @param chiefNode
     */
    private void realDoRefresh(GeneralProcessEventForSanBoot peOnSelObj ,ChiefNode chiefNode){
        TreePath path ;
        BrowserTreeNode selObjNode ;
        selObjNode = chiefNode.getTreeNode();
        path = new TreePath( selObjNode.getPath() );
        view.setCurNode( selObjNode );
        view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
        peOnSelObj.processTreeExpand(path);
        peOnSelObj.processTreeSelection( path );
        peOnSelObj.controlMenuAndBtnForTreeEvent();
        view.getTree().setSelectionPath( path );
        view.getTree().requestFocus();
    }

}
