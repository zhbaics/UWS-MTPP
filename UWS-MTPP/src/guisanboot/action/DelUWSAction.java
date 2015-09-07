/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.MirrorJob;
import guisanboot.data.SourceAgent;
import guisanboot.data.UWSrvNode;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefDestUWS;
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
public class DelUWSAction extends GeneralActionForMainUi{
    SourceAgent saForUWS = null;

    public DelUWSAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delSWU",
            MenuAndBtnCenterForMainUi.FUNC_DEL_UWS_SRV
        );
    }

    @Override public void doAction(ActionEvent evt){
        boolean isOk;

SanBootView.log.info(getClass().getName(),"########### Entering delete swu server action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !( selObj instanceof UWSrvNode ) ) {
SanBootView.log.info(getClass().getName(),"########### (Unknown type )End of delete swu server action. " );
            return;
        }

        try{
            UWSrvNode selUWS = (UWSrvNode)selObj;

            if( isUsed( selUWS.getUws_id() ) ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.delSrcSWU")
                );
                return;
            }

            int ret = JOptionPane.showConfirmDialog(
                view,
                SanBootView.res.getString("MenuAndBtnCenter.confirm12"),
                SanBootView.res.getString("common.confirm"),  //"Confirm",
                JOptionPane.OK_CANCEL_OPTION
            );
            if(  ( ret == JOptionPane.CANCEL_OPTION ) || (  ret == JOptionPane.CLOSED_OPTION) ){
                return;
            }

            if( saForUWS != null ){ //delete srcagnt represented src-end UWS
                isOk = view.initor.mdb.delSrcAgnt( "", 0, 0, "", saForUWS.getSrc_agnt_id() );
                if( isOk ){
                    view.initor.mdb.removeSrcAgntFromCache( saForUWS );
                }else{
                    JOptionPane.showMessageDialog( view,
                        ResourceCenter.getCmd( ResourceCenter.CMD_DEL_SRCAGNT )+":"+
                            view.initor.mdb.getErrorMessage()
                    );
                    return;
                }
            }

            isOk = view.initor.mdb.delUWSrv( selUWS.getUws_id() );
            if( isOk ){
                view.initor.mdb.removeUWSrvFromVector( selUWS );

                // 将相关的mj删除
                ArrayList mjs = view.initor.mdb.getAllMJOnDestIP( selUWS.getUws_ip() );
                int size = mjs.size();
                for( int i=0; i<size; i++ ){
                    MirrorJob mj = (MirrorJob)mjs.get(i);
                    if( view.initor.mdb.delMj( mj.getMj_id() ) ){
                        view.initor.mdb.removeMJFromVector( mj );
                    }
                }

                // 显示点击 chiefDestUWSrvNode 后的右边tabpane中的内容���
                BrowserTreeNode chiefDestUWSrvNode = view.getChiefNodeOnRoot( ResourceCenter.TYPE_CHIEF_DEST_UWS );
                view.setCurNode( chiefDestUWSrvNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                ProcessEventOnChiefDestUWS peOnChiefDestUWSrv = new ProcessEventOnChiefDestUWS( view );
                TreePath path = new TreePath( chiefDestUWSrvNode.getPath() );
                peOnChiefDestUWSrv.processTreeSelection( path );
                peOnChiefDestUWSrv.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }else{
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmd( ResourceCenter.CMD_DEL_UWS_SRV )+":"+
                        view.initor.mdb.getErrorMessage()
                );
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
SanBootView.log.info(getClass().getName(),"########### End of delete swu server action. " );
    }

    private boolean isUsed( int uws_id ){
        SourceAgent sa = null;
        int realSaCnt=0;

        ArrayList saList = view.initor.mdb.getSrcAgntFromVecOnUWSID( uws_id );
        if( saList.size() <=0 ) return false;

        int size = saList.size();
        for( int i=0; i<size; i++ ){
            sa = (SourceAgent)saList.get(i);
            if( !sa.isRepresentAUWS() ){
                realSaCnt++;
            }
        }

        if( realSaCnt >0 ) return true;

        this.saForUWS = sa;
        ArrayList mdiList = view.initor.mdb.getMDIFromCacheOnSrcAgntID( sa.getSrc_agnt_id() );
        return ( mdiList.size() > 0 );
    }
}
