/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.data.Pool;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefPool;
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
public class DelPoolAction extends GeneralActionForMainUi{
     public DelPoolAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delPool",
            MenuAndBtnCenterForMainUi.FUNC_DEL_POOL
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering delete pool action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( selObj instanceof Pool ){
            Pool pool = (Pool)selObj;
            boolean isOk = view.initor.mdb.getPoolInfo( pool.getPool_id() );
            if( !isOk ){
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_POOLINFO )+" : "+
                        view.initor.mdb.getErrorMessage()
                );

                return;
            }else{
                long vused = view.initor.mdb.getPoolVUsed();
                if( vused <= 0 ){
                    int ret =  JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm8"),
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION ) ){
                        return;
                    }
                }else{
                    int ret =  JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm9"),
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION ) ){
                        return;
                    }

                    //删除使用该pool的volume/snapshot/view，然后再删除该pool
                    //但是这样要求遍历mdb找出所有要删除的obj

                }
            }

            Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_DEL_POOL );
            if( pool.getPool_Type() == 1){
                isOk = view.initor.mdb.delUcsPool(pool.getPool_name());
            } else {
                isOk = view.initor.mdb.delResVol( pool.getPool_name() );
            }
            if( isOk ){
                if( view.initor.mdb.delPool( pool.getPool_id() ) ){
                    audit.setEventDesc( "Delete pool: "+ pool.getPool_name() + " successfully." );
                    view.audit.addAuditRecord( audit );

                    // 显示点击 chiefPool 后的右边tabpane中的内容
                    BrowserTreeNode chiefPool = view.getChiefNodeOnRoot( ResourceCenter.TYPE_CHIEF_POOL );
                    if( chiefPool!= null ){
                        view.setCurNode( chiefPool );
                        view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                        ProcessEventOnChiefPool peOnChiefPool= new ProcessEventOnChiefPool( view );
                        TreePath path = new TreePath( chiefPool.getPath() );
                        peOnChiefPool.processTreeSelection( path );
                        peOnChiefPool.controlMenuAndBtnForTreeEvent();
                        view.getTree().setSelectionPath( path );
                        view.getTree().requestFocus();
                    }
                }else{
                    audit.setEventDesc( "Failed to delete pool: " + pool.getPool_name() );
                    view.audit.addAuditRecord( audit );

                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.delPool") +
                            view.initor.mdb.getErrorMessage()
                    );
                    return;
                }
            }else{
                audit.setEventDesc( "Failed to delete pool: " + pool.getPool_name() );
                view.audit.addAuditRecord( audit );

                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.delResVol")+
                        view.initor.mdb.getErrorMessage()
                );
                return;
            }
        }
SanBootView.log.info(getClass().getName(),"########### End of delete pool action. " );
    }
}
