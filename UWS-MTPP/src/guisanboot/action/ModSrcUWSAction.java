/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.SrcUWSrvNodeWrapper;
import guisanboot.data.UWSrvNode;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.AddUWSSrvDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnRemoteUWSManage;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class ModSrcUWSAction extends GeneralActionForMainUi{
    public ModSrcUWSAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modSrcSWU",
            MenuAndBtnCenterForMainUi.FUNC_MOD_SRC_UWS_SRV
        );
    }

    @Override public void doAction(ActionEvent evt){
        boolean isOK;

SanBootView.log.info(getClass().getName(),"########### Entering modify source-end swu server action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;
        try{
            if( selObj instanceof SrcUWSrvNodeWrapper ){
                SrcUWSrvNodeWrapper selSrv = (SrcUWSrvNodeWrapper)selObj;

                // 检查可否修改该主机

                AddUWSSrvDialog dialog = new AddUWSSrvDialog( selSrv.getMetaData(),view );
                int width  = 275+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 175+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );
                dialog.setVisible( true );

                Object[] ret  = dialog.getValue();
                if( ret == null || ret.length <= 0) return;

                String ip =(String)ret[0];
                int port = ((Integer)ret[1]).intValue();

                UWSrvNode newSrv = new UWSrvNode(
                    selSrv.getMetaData().getUws_id(),
                    ip,
                    port,
                    selSrv.getMetaData().getUws_psn()
                );

                isOK = view.initor.mdb.modUWSrv( newSrv );
                if( isOK ){
                    // 用新值替换旧值.这样GUI上所有地方都该改过来了���
                    selSrv.getMetaData().setUws_ip( newSrv.getUws_ip() );
                    selSrv.getMetaData().setUws_port( newSrv.getUws_port() );

                    // 显示点击 remoteUWSMg 后的右边tabpane中的内容��
                    BrowserTreeNode remoteUWSmg = view.getChiefNodeOnSuperRoot( ResourceCenter.TYPE_REMOTE_UWS_MANAGE );
                    view.setCurNode( remoteUWSmg );
                    view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                    ProcessEventOnRemoteUWSManage peOnRemoteUWSmg = new ProcessEventOnRemoteUWSManage( view );
                    TreePath path = new TreePath( remoteUWSmg.getPath() );
                    peOnRemoteUWSmg.processTreeSelection( path );
                    peOnRemoteUWSmg.controlMenuAndBtnForTreeEvent();
                    view.getTree().setSelectionPath( path );
                    view.getTree().requestFocus();
                }else{
                    JOptionPane.showMessageDialog(view,
                        ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_UWS_SRV )+" : "+
                            view.initor.mdb.getErrorMessage()
                    );
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
SanBootView.log.info(getClass().getName(),"########### End of modify source-end swu server action. " );
    }
}
