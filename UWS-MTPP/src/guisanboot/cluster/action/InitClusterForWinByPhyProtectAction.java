/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cluster.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.ui.PhyInitWinClusterWizardDialog;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.GetAllPoolThread;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * InitClusterForWinByPhyProtectAction.java
 *
 * Created on 2011-7-6, 13:34:55
 */
public class InitClusterForWinByPhyProtectAction extends GeneralActionForMainUi{
    public InitClusterForWinByPhyProtectAction(){
        super(
          ResourceCenter.BTN_ICON_INIT_16,
          ResourceCenter.BTN_ICON_INIT_50,
          "View.MenuItem.init",
          MenuAndBtnCenterForMainUi.FUNC_CLUSTER_PHY_WIN_INIT
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering window cluster's phy-init process." );
        Object selObj = view.getSelectedObjFromSanBoot();
        Cluster cluster = null;
        if( (selObj != null) && (selObj instanceof Cluster)  ){
            cluster = (Cluster)selObj;
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
            }
        }

        PhyInitWinClusterWizardDialog dialog = new PhyInitWinClusterWizardDialog( view,cluster );
        int width  = 580+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 435+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of window cluster's phy-init process action." );
    }
}
