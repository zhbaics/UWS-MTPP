/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.CreatePoolDialog;
import guisanboot.ui.CreatePoolThread;
import guisanboot.ui.CreateUcsPoolThread;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefPool;
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
public class CrtPoolAction extends GeneralActionForMainUi{
     public CrtPoolAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtPool",
            MenuAndBtnCenterForMainUi.FUNC_CRT_POOL
        );
    }

    @Override public void doAction(ActionEvent evt){
        // get vg size
SanBootView.log.info(getClass().getName(),"########### Entering create pool action. " );
        boolean isOk = view.initor.mdb.getVolGrpSize();
        if( isOk ){
            float vgSize = view.initor.mdb.getRealVGSize();
            if( vgSize<=0 ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.noVgSpace")
                );
                //return;
            }

            float tmp = (float)(vgSize/1024.0);
SanBootView.log.info(getClass().getName(), " free size of VG : " + tmp );

            ArrayList nameList;
            ArrayList idleDiskList;
            isOk = view.initor.mdb.getShareName();
            if( isOk ){
                nameList = view.initor.mdb.getSharenameList();
            }else{
                nameList = view.initor.mdb.getPoolList();
            }
            
            isOk = view.initor.mdb.getIdelDisk();
            if( isOk ){
                idleDiskList = view.initor.mdb.getAllIdleDisk();
            } else {
                idleDiskList = new ArrayList();
            }

            CreatePoolDialog dialog = new CreatePoolDialog( view, tmp,nameList ,idleDiskList );
            int width = 275+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 425+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );

            Object[] ret = dialog.getValues();
            if( ret == null || ret.length <= 0 ) return;

            String name = (String)ret[0];
            float size = ((Float)ret[1]).floatValue();
            String password = (String)ret[2];
            int poolType = ((Integer)ret[3]).intValue();
            ArrayList selList = (ArrayList)ret[4];
            if( poolType == 0){
                CreatePoolThread thread = new CreatePoolThread(
                    view,
                    name,
                    (long)size*1024,
                    password,
                    poolType,
                    selList
                );
                view.startupProcessDiag(
                    SanBootView.res.getString("View.pdiagTitle.crtPool"),
                    SanBootView.res.getString("View.pdiagTip.crtPool"),
                    thread
                );
            
                Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_CRT_POOL );

                isOk = thread.isOK();

                if( isOk ){
                    audit.setEventDesc( "Create pool: " + name + " successfully." );
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
                    audit.setEventDesc( "Failed to create pool: " + name );
                    view.audit.addAuditRecord( audit );

                    JOptionPane.showMessageDialog( view,
                    thread.getErrMsg()
                    );
                    return;
                }
            } else { //创建UCS存储池
                CreateUcsPoolThread thread = new CreateUcsPoolThread(
                    view,
                    name,
                    0,
                    password,
                    poolType,
                    selList
                );
                view.startupProcessDiag(
                    SanBootView.res.getString("View.pdiagTitle.crtPool"),
                    SanBootView.res.getString("View.pdiagTip.crtPool"),
                    thread
                );
            
                Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_CRT_POOL );

                isOk = thread.isOK();
                if( isOk ){
                    audit.setEventDesc( "Create pool: " + name + " successfully." );
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
                    audit.setEventDesc( "Failed to create pool: " + name );
                    view.audit.addAuditRecord( audit );

                    JOptionPane.showMessageDialog( view,
                    thread.getErrMsg()
                    );
                    return;
                }
            }
        }else{
            JOptionPane.showMessageDialog( view,
               SanBootView.res.getString("MenuAndBtnCenter.error.getVgSize")
            );
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of create pool action. " );
    }
}
