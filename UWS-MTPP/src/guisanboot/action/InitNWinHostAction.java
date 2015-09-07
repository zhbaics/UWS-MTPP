/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.data.Pool;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.GetAllPoolThread;
import guisanboot.ui.InitNWinHostWizardDialog;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author zourishun
 */
public class InitNWinHostAction extends GeneralActionForMainUi {
    public InitNWinHostAction(){
        super(
          ResourceCenter.BTN_ICON_INIT_16,
          ResourceCenter.MENU_ICON_BLANK,
          "View.MenuItem.init",
          MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN
        );
    }

    @Override public void doAction(ActionEvent evt){
        boolean hasEnoughSpace = false;

SanBootView.log.info(getClass().getName(),"########### Entering Linux host's init action. " );
        GetAllPoolThread thread = new GetAllPoolThread(
            view
        );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.getPool1"),
            SanBootView.res.getString("View.pdiagTip.getPool1"),
            thread
        );

        ArrayList list = thread.getRet();
        if( list == null ){
            JOptionPane.showMessageDialog( view,
                SanBootView.res.getString("MenuAndBtnCenter.error.getPool") +
                   view.initor.mdb.getErrorMessage()
            );
            return;
        }else{
            int size = list.size();
            if( size <=0 ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.noPool")
                );
                return;
            }else{
                for( int i=0; i<size; i++ ){
                    Pool pool = (Pool)list.get(i);
                    if( pool.getRealFreeSize() >0 ){
                        hasEnoughSpace = true;
                        break;
                    }
                }

                // 存储池空间已被预分配完了，只能使用之前分配的target或者空闲卷了
                if( !hasEnoughSpace ){
                    int ret = JOptionPane.showConfirmDialog(
                        view,
                        SanBootView.res.getString("MenuAndBtnCenter.confirm26"),
                        SanBootView.res.getString("common.confirm"),  //"Confirm",
                        JOptionPane.OK_CANCEL_OPTION
                    );
                    if( ( ret == JOptionPane.CANCEL_OPTION ) || ( ret == JOptionPane.CLOSED_OPTION) ){
                        return;
                    }
                }

            }
        }

        Object selObj = view.getSelectedObjFromSanBoot();
        BootHost host = null;
        if( (selObj != null) && (selObj instanceof BootHost)  ){
            host = (BootHost)selObj;
        }

        InitNWinHostWizardDialog dialog = new InitNWinHostWizardDialog( view,host,hasEnoughSpace );
        int width  = 560+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 385+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of linux's host init action. " );
    }
}
