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
import guisanboot.ui.ChiefMirrorJobList;
import guisanboot.ui.EditMirrorJobDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefMj;
import guisanboot.ui.ProcessEventOnChiefMjMg;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.entity.ChiefCopyJobList;
import guisanboot.unlimitedIncMj.service.ProcessEventOnChiefCj;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class ModMjAction extends GeneralActionForMainUi{
    public ModMjAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modMj",
            MenuAndBtnCenterForMainUi.FUNC_MOD_MJ
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering modify mirror job action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !( selObj instanceof MirrorJob ) ){
            return;
        }

        MirrorJob mj = (MirrorJob)selObj;

        if( mj.isMJStart() ){
            JOptionPane.showMessageDialog(view,
                SanBootView.res.getString("MenuAndBtnCenter.error.mjisrunning1")
            );
            return;
        }

        int func;
        if( mj.isNormalMjType() ){
            func = MenuAndBtnCenterForMainUi.FUNC_CRT_MJ;
        }else if( mj.isIncMjType() ){
            func = MenuAndBtnCenterForMainUi.FUNC_CRT_MJ1;
        }else if( mj.isRemoteLjType()){
            func = MenuAndBtnCenterForMainUi.FUNC_CRT_LMJ;
        }else{
            func = MenuAndBtnCenterForMainUi.FUNC_CRT_CJ;
        }

        EditMirrorJobDialog dialog = new EditMirrorJobDialog( view,mj, "",func,false );
        int width = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 380+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object[] ret = dialog.getValues();
        if( ret == null ) return;

        String job_name = (String)ret[0];
        String job_desc = (String)ret[1];
        UWSrvNode uws = (UWSrvNode)ret[2];
        Pool pool = (Pool)ret[3];
        int opt = ((Integer)ret[4]).intValue();

        MirrorJob newMJ = new MirrorJob(
            mj.getMj_mg_id(),
            job_name,
            uws.getUws_ip(),
            uws.getUws_port(),
            opt,
            pool.getPool_id(),
            pool.getPool_passwd(),
            job_desc
        );
        newMJ.setMj_id( mj.getMj_id() );

        boolean isOk = view.initor.mdb.modMj1( newMJ );
        if( isOk ){
            mj.setMj_job_name( job_name );
            mj.setMj_dest_ip( uws.getUws_ip() );
            mj.setMj_dest_port( uws.getUws_port() );
            mj.setMj_transfer_option( opt );
            mj.setMj_dest_pool( pool.getPool_id() );
            mj.setMj_dest_pool_passwd( pool.getPool_passwd() );
            mj.setMj_desc( job_desc );

            BrowserTreeNode mjFNode = mj.getFatherNode();
            Object fMjObj = mjFNode.getUserObject();
            view.setCurNode( mjFNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            TreePath path = new TreePath( mjFNode.getPath() );
            if( fMjObj instanceof ChiefMirrorJobList ){
                ProcessEventOnChiefMj peOnChiefMj = new ProcessEventOnChiefMj( view );
                peOnChiefMj.processTreeSelection( path );
                peOnChiefMj.controlMenuAndBtnForTreeEvent();
            }else if( fMjObj instanceof ChiefCopyJobList ){
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
            JOptionPane.showMessageDialog(view,
                ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_MJ ) +" : "+view.initor.mdb.getErrorMessage()
            );
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of modify mirror job action. " );
    }
}
