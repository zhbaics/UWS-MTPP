/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.Pool;
import guisanboot.data.UWSrvNode;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.AddUWSSrvDialog;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.ChiefDestUWS;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefDestUWS;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class CrtUWSAction extends GeneralActionForMainUi{
    private ChiefDestUWS chiefDestUWS = null;

    public CrtUWSAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtSWU",
            MenuAndBtnCenterForMainUi.FUNC_CRT_UWS_SRV
        );
    }

    @Override public void doAction(ActionEvent evt){
        AddUWSSrvDialog dialog =null;

SanBootView.log.info(getClass().getName(),"########### Entering create swu server action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( selObj instanceof ChiefDestUWS ){
            chiefDestUWS = (ChiefDestUWS)selObj;
            // 发生在root下的chief dest uws node上
            dialog = new AddUWSSrvDialog( null,view );
        }else{ // 发生在其他节点上
            dialog = new AddUWSSrvDialog( null,view );
        }

        int width  = 275+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 155+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object[] ret = dialog.getValue();
        if( ret == null || ret.length <= 0 ) return;

        String ip =(String)ret[0];
        int port = ((Integer)ret[1]).intValue();

        UWSrvNode newUWS = new UWSrvNode(
            -1,
            ip,
            port,
            ""
        );

        CreateUWS thread = new CreateUWS( view,newUWS );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.crtSWUSrvNode"),
            SanBootView.res.getString("View.pdiagTip.crtSWUSrvNode"),
            thread
        );
SanBootView.log.info(getClass().getName(),"########### End of create swu server action. " );
    }

    class CreateUWS extends BasicGetSomethingThread{
        UWSrvNode newUWS;

        public CreateUWS( SanBootView view, UWSrvNode uws ){
            super( view );
            this.newUWS  = uws;
        }

        public boolean realRun(){
            int dest_uws_port;
            String dest_uws_ip;
            boolean aIsOk;

            dest_uws_ip = newUWS.getUws_ip();
            dest_uws_port = newUWS.getUws_port();
            aIsOk = view.initor.mdb.updateRemotePool( dest_uws_ip,dest_uws_port );
            if( !aIsOk ){
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.getPool")+" : "+view.initor.mdb.getErrorMessage();
                return false;
            }

            ArrayList list = view.initor.mdb.getRemotePoolList();
            if( list.size() <=0 ){
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.noPool1");
                return false;
            }

            Pool pool = (Pool)list.get(0);

            view.initor.mdb.targetSrvName = null;
            String psn = view.initor.mdb.getHostName( dest_uws_ip, dest_uws_port, pool.getPool_id(),pool.getPool_passwd() );
            if( psn.equals("") ){
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.getHostName");
                view.initor.mdb.targetSrvName = null;
                return false;
            }
            view.initor.mdb.targetSrvName = null;
            newUWS.setUws_psn( psn.toUpperCase() );

            // 本地增加一台dest-end uws server
            aIsOk = view.initor.mdb.addUWSrv( "",-1, -1,"",newUWS );
            if( aIsOk ){
                newUWS.setUws_id( view.initor.mdb.getNewId() );
                view.initor.mdb.addUWSrvToVector( newUWS );
                processGUIWhenAddUWSrv( newUWS );
            }else{
                errMsg =  ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_UWS_SRV ) + " : " +
                    view.initor.mdb.getErrorMessage();
                return false;
            }

            return true;
        }

        private void processGUIWhenAddUWSrv( UWSrvNode srv ){
            BrowserTreeNode chiefDestUWSNode;

            // 在树上初始化新建的uwsrv node( 点击发生在root下的chiefDestUWS上或其他节点上 )
            if( chiefDestUWS != null ){ //点击发生在root下的chiefRemoteUWS上
                chiefDestUWSNode = (BrowserTreeNode)chiefDestUWS.getTreeNode();
            }else{ // 发生在其他节点上
                chiefDestUWSNode = view.getChiefNodeOnRoot(
                    ResourceCenter.TYPE_CHIEF_DEST_UWS
                );
            }

            if( chiefDestUWSNode != null ){
                view.setCurNode( chiefDestUWSNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                ProcessEventOnChiefDestUWS peOnChiefDestUWS = new ProcessEventOnChiefDestUWS( view );
                TreePath path = new TreePath( chiefDestUWSNode.getPath() );
                peOnChiefDestUWS.processTreeSelection( path );
                peOnChiefDestUWS.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }
        }
    }
}
