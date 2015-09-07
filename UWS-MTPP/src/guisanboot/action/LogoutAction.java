/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author zourishun
 */
public class LogoutAction extends GeneralActionForMainUi{
    public LogoutAction(Icon menuIcon,Icon btnIcon,String text,int fID){
        super( menuIcon,btnIcon,text,fID);
    }

    public LogoutAction(){
        super(
            ResourceCenter.BTN_ICON_DISCON_16,
            ResourceCenter.BTN_ICON_DISCON_50,
            "View.MenuItem.disConnect",
            MenuAndBtnCenterForMainUi.FUNC_LOGOUT
        );
    }

    Runnable cleanTip = new Runnable(){
        public void run(){
            view.setConnectionTip( ResourceCenter.MENU_ICON_BLANK,"");
        }
    };

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering logout action. " );

        Audit audit = view.audit.registerAuditRecord( 0, MenuAndBtnCenterForMainUi.FUNC_LOGOUT );
        audit.setEventDesc( "Logout from system successfully.");
        view.audit.addAuditRecord( audit );

        view.initor.mdb.logout();
        view.initor.mdb.closeStreamOnCurSocket();

        view.getTable().setModel( new DefaultTableModel() );
        view.setTreeRootNode( view.getTree() );
        view.removeRightPane();

        view.setCurNode( null );
        view.initor.setLoginedFlag( false );
        view.initor.setInitedFlag(  false );
        view.initor.mdb.targetSrvName = null;
        view.isSupportCMDP = false;
        view.initor.clearCurUWS();
        view.mbCenter.setupConnectButtonStatus( view.initor.isLogined() );
        view.setTitle( SanBootView.res.getString( "View.frameTitle" ) );

        try{
            SwingUtilities.invokeLater( cleanTip );
        }catch(Exception ex){}
SanBootView.log.info(getClass().getName(),"########### End of logout action. " );
    }
}
