package guisanboot.lvm.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.lvm.ui.LvmInitBootHostWizardDialog;
import guisanboot.data.BootHost;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.GetAllPoolThread;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * InitHostByPhyProtectAction.java
 *
 * Created on 2010-6-7, 13:43:55
 */
public class InitHostByLvmAction extends GeneralActionForMainUi{
    public InitHostByLvmAction(){
        super(
          ResourceCenter.BTN_ICON_INIT_16,
          ResourceCenter.BTN_ICON_INIT_50,
          "View.MenuItem.init",
          MenuAndBtnCenterForMainUi.FUNC_INIT_NWIN_LVM
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering not-window host'lvm-init process" );
        Object selObj = view.getSelectedObjFromSanBoot();
        BootHost host = null;
        if( (selObj != null) && (selObj instanceof BootHost)  ){
            host = (BootHost)selObj;
        }

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
            if( size <= 0 ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.noPool")
                );
                return;
            }else{
/*
                boolean hasEnoughSpace = false;
                for( int i=0; i<size; i++ ){
                    Pool pool = (Pool)list.get(i);
                    if( pool.getRealFreeSize() >0 ){
                        hasEnoughSpace = true;
                        break;
                    }
                }

                if( !hasEnoughSpace ){
                    try{
                        GetFreePhyVol getFreeVol = new GetFreePhyVol(
                            ResourceCenter.getCmd(
                                ResourceCenter.CMD_GET_VOL
                            ),
                            view.getSocket(),
                            view
                        );
                        getFreeVol.setAddCacheFlag( true );
                        if( !getFreeVol.realDo() ){
                            JOptionPane.showMessageDialog( view,
                                SanBootView.res.getString("MenuAndBtnCenter.error.getFreeVol") +
                                   view.initor.mdb.getErrorMessage()
                            );
                            return;
                        }

                        if( getFreeVol.getFreeVolNum() <= 0 ){
                            JOptionPane.showMessageDialog( view,
                                SanBootView.res.getString("MenuAndBtnCenter.error.noEnoughSpace")
                            );
                            return;
                        }
                    }catch(Exception ex){
SanBootView.log.error(getClass().getName(), ex.getMessage() );
                        return;
                    }
                }
*/
            }
        }

        LvmInitBootHostWizardDialog dialog = new LvmInitBootHostWizardDialog( view,host,true );
        int width  = 560+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 385+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of not-window host's lvm-init process action. " );
    }
}
