/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.MirrorJob;
import guisanboot.data.Pool;
import guisanboot.data.UWSrvNode;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.AddUWSSrvDialog;
import guisanboot.ui.BasicGetSomethingThread;
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
public class ModUWSAction extends GeneralActionForMainUi{
    public ModUWSAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modSWU",
            MenuAndBtnCenterForMainUi.FUNC_MOD_UWS_SRV
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering modify swu server action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null || !(selObj instanceof UWSrvNode) ){
SanBootView.log.info(getClass().getName(),"########### End of modify swu server action. " );
            return;
        }

        try{
            UWSrvNode selSrv = (UWSrvNode)selObj;

            // 检查可否修改该UWS服务器

            AddUWSSrvDialog dialog = new AddUWSSrvDialog( selSrv,view );
            int width  = 275+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 175+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );

            Object[] ret  = dialog.getValue();
            if( ret == null || ret.length <= 0) return;

            String ip =(String)ret[0];
            int port = ((Integer)ret[1]).intValue();
System.out.println(" new ip: "+ ip +" new port: "+port );
            ModUWSSrvNode  thread = new ModUWSSrvNode( view,selSrv, ip, port );
            view.startupProcessDiag(
                SanBootView.res.getString("View.pdiagTitle.modSWUSrvNode"),
                SanBootView.res.getString("View.pdiagTip.modSWUSrvNode"),
                thread
            );
        }catch(Exception ex){
            ex.printStackTrace();
        }
SanBootView.log.info(getClass().getName(),"########### End of modify swu server action. " );
    }

    class ModUWSSrvNode extends BasicGetSomethingThread{
        UWSrvNode selSrv;
        String new_ip;
        int new_port;
        StringBuffer mod_mj_err_buf = new StringBuffer();
        StringBuffer start_mj_err_buf = new StringBuffer();

        public ModUWSSrvNode( SanBootView view,UWSrvNode selSrv,String new_ip,int new_port ){
            super( view );

            this.selSrv   = selSrv;
            this.new_ip   = new_ip;
            this.new_port = new_port;
        }

        public boolean realRun(){
            boolean ok;
            boolean mod_mj_isFirst = true;
            boolean start_mj_isFirst = true;
            String strMj_Name;

            ok = view.initor.mdb.updateRemotePool( new_ip,new_port );
            if( !ok ){
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
            String psn = view.initor.mdb.getHostName( new_ip, new_port, pool.getPool_id(),pool.getPool_passwd() );
            if( psn.equals("") ){
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.getHostName");
                view.initor.mdb.targetSrvName = null;
                return false;
            }
            view.initor.mdb.targetSrvName = null;

            if( !selSrv.getUws_psn().toUpperCase().equals( psn.toUpperCase() ) ){
                errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.notSameSWU");
                return false;
            }

            String old_dest_ip = selSrv.getUws_ip();
            if( !realModUWSrvNode( selSrv,new_ip,new_port,psn.toUpperCase() ) ) return false;

            ArrayList mjList = view.initor.mdb.getAllMJOnDestIP( old_dest_ip );
            int size =mjList.size();
System.err.println("mj size: "+size );

            for( int i=0; i<size; i++ ){
                MirrorJob mj = (MirrorJob)mjList.get( i );
                strMj_Name = mj.getMj_job_name();

                if( !view.initor.mdb.modMj2( mj.getMj_id(), new_ip, new_port ) ){
                    if( mod_mj_isFirst ){
                        mod_mj_err_buf.append( strMj_Name );
                        mod_mj_isFirst = false;
                    }else{
                        mod_mj_err_buf.append( "," + strMj_Name );
                    }
                }else{
                    mj.setMj_dest_ip( new_ip );
                    mj.setMj_dest_port( new_port );

                    if( mj.isMJStart() ){
                        ok = view.initor.mdb.checkMg( mj.getMj_mg_id() );
                        if( ok ){
                            ok = view.initor.mdb.startMj( mj.getMj_id() );
                            if( !ok ){
                                if( start_mj_isFirst ){
                                    start_mj_err_buf.append( strMj_Name );
                                    start_mj_isFirst = false;
                                }else{
                                    start_mj_err_buf.append( "," + strMj_Name );
                                }
                            }
                        }else{
                            ok = view.initor.mdb.startMg( mj.getMj_mg_id() );
                            if( ok ){
                                ok = view.initor.mdb.startMj( mj.getMj_id() );
                                if( !ok ){
                                    if( start_mj_isFirst ){
                                        start_mj_err_buf.append( strMj_Name );
                                        start_mj_isFirst = false;
                                    }else{
                                        start_mj_err_buf.append( "," + strMj_Name );
                                    }
                                }
                            }else{
                                start_mj_err_buf.append( "," + strMj_Name );
                            }
                        }
                    }
                }
            }

            boolean isRealOk = mod_mj_err_buf.toString().equals("") && start_mj_err_buf.toString().equals("") ;
            if( !isRealOk ){
                if( !mod_mj_err_buf.toString().equals("") ){
                    errMsg = SanBootView.res.getString("MenuAndBtnCenter.error.modmj") + mod_mj_err_buf.toString();
                }
                if( !start_mj_err_buf.toString().equals("") ){
                    errMsg += "\n" + SanBootView.res.getString("MenuAndBtnCenter.error.startmj") + start_mj_err_buf.toString();
                }
            }
            return isRealOk;
        }

        private boolean realModUWSrvNode( UWSrvNode selSrv,String new_ip,int new_port, String new_psn ){
            UWSrvNode newSrv = new UWSrvNode(
                selSrv.getUws_id(),
                new_ip,
                new_port,
                new_psn
            );
            boolean isOK = view.initor.mdb.modUWSrv( newSrv );
            if( isOK ){
                // 用新值替换旧值.这样GUI上所有地方都该改过来了
                selSrv.setUws_ip( newSrv.getUws_ip() );
                selSrv.setUws_port( newSrv.getUws_port() );
                selSrv.setUws_psn( new_psn );

                // 显示点击 chiefDestUWSrvNode 后的右边tabpane中的内容����
                BrowserTreeNode chiefDestUWSrvNode = view.getChiefNodeOnRoot( ResourceCenter.TYPE_CHIEF_DEST_UWS );
                view.setCurNode( chiefDestUWSrvNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                ProcessEventOnChiefDestUWS peOnChiefDestUWS = new ProcessEventOnChiefDestUWS( view );
                TreePath path = new TreePath( chiefDestUWSrvNode.getPath() );
                peOnChiefDestUWS.processTreeSelection( path );
                peOnChiefDestUWS.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }else{
                errMsg = ResourceCenter.getCmd( ResourceCenter.CMD_MOD_UWS_SRV )+":"+
                        view.initor.mdb.getErrorMessage();
            }

            return isOK;
        }
    }
}
