/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.data.LunMap;
import guisanboot.data.Snapshot;
import guisanboot.data.View;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnMirroredSnap;
import guisanboot.ui.ProcessEventOnSnapshot;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
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
public class DelViewAction extends GeneralActionForMainUi{
    public DelViewAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.delView",
            MenuAndBtnCenterForMainUi.FUNC_DEL_VIEW
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering delete view action. " );
        Object[] selObj = view.getMulSelectedObjFromSanBoot();
        if( selObj == null || selObj.length<=0 ) return;

        int ret =  JOptionPane.showConfirmDialog(
            view,
            SanBootView.res.getString("MenuAndBtnCenter.confirm7"),
            SanBootView.res.getString("common.confirm"),  //"Confirm",
            JOptionPane.OK_CANCEL_OPTION
        );
        if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION ) ){
            return;
        }

        ProgressDialog initDiag = new ProgressDialog(
            view,
            SanBootView.res.getString("View.pdiagTitle.delview"),
            SanBootView.res.getString("View.pdiagTip.delview")
        );
        DelView delvol = new DelView( initDiag,selObj,view );
        delvol.start();
        initDiag.mySetSize();
        initDiag.setLocation( view.getCenterPoint( initDiag.getDefWidth(),initDiag.getDefHeight() ) );
        initDiag.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of delete view action. " );
    }

    class DelView extends Thread{
        ProgressDialog pdiag;
        Object[] views;
        SanBootView view;

        public DelView( ProgressDialog pdiag,Object[] views,SanBootView view ){
            this.pdiag = pdiag;
            this.views = views;
            this.view = view;
        }

        Runnable close = new Runnable(){
            public void run(){
                pdiag.dispose();
            }
        };

        @Override public void run(){
            View v;
            boolean isOk;
            BrowserTreeNode fNode;
            Vector lmList;
            int size1,j,tid;
            LunMap lm;
            Audit audit;

            v = (View)views[0];

            int size = views.length;
            for( int i=0; i<size; i++ ){
                if( views[i] instanceof View ){
                    v = (View)views[i];
                    fNode = v.getFatherNode();
                    tid = v.getTargetID();

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
                    audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_DEL_VIEW );
                    isOk = view.initor.mdb.delView( v );
                    if( isOk ){
                        audit.setEventDesc( "Delete view: " + v.getSnap_name() + " successfully." );
                        view.audit.addAuditRecord( audit );

                        BrowserTreeNode selViewNode = view.getViewNodeOnSnapshot( fNode,v.getTargetID() );
                        if( selViewNode !=null ){
                            view.removeNodeFromTree( fNode,selViewNode );
                        }
                    }else{
                        audit.setEventDesc( "Failed to delete view: " + v.getSnap_name() );
                        view.audit.addAuditRecord( audit );

                        view.showError1(
                            ResourceCenter.CMD_DEL_VIEW,
                            view.initor.mdb.getErrorCode(),
                            //ResourceCenter.getDelVolErrStr( view.initor.mdb.getErrorCode() )
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

            // 显示点击 snapNode 后的右边tabpane中的内容
            fNode = v.getFatherNode();
            Object snapObj = fNode.getUserObject();
            if( snapObj instanceof Snapshot ){
                view.setCurNode( fNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                ProcessEventOnSnapshot peOnSnap = new ProcessEventOnSnapshot( view );
                TreePath path = new TreePath( fNode.getPath() );
                peOnSnap.processTreeSelection( path );
                peOnSnap.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }else{
                view.setCurNode( fNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                ProcessEventOnMirroredSnap peOnMSnap = new ProcessEventOnMirroredSnap( view );
                TreePath path = new TreePath( fNode.getPath() );
                peOnMSnap.processTreeSelection( path );
                peOnMSnap.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }
        }
    }
}
