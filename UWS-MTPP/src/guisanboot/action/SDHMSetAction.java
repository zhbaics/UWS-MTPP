/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import guisanboot.ui.sdhm.SDHMSetDialog;
import java.awt.event.ActionEvent;

/**
 *
 * @author zourishun
 */
public class SDHMSetAction extends GeneralActionForMainUi{
    public SDHMSetAction(){
        super(
            ResourceCenter.SMALL_ADD_HOST,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.sdhmInfo",
            MenuAndBtnCenterForMainUi.FUNC_SDHM_INFO
        );
    }

    @Override public void doAction(ActionEvent evt){
        SanBootView.log.info(getClass().getName(),"########### Entering SDHM set browser action");
        SDHMSetDialog dialog = new SDHMSetDialog( view );
        boolean isOK = false;

        int width  = 450+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 550+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );


////        isOK = view.initor.mdb.addUWSrv( "",-1, -1,"",newSrv );
//        if( isOK ){
////            newSrv.setUws_id( view.initor.mdb.getNewId() );
////            view.initor.mdb.addUWSrvToVector( newSrv );
////            processGUIWhenAddUWSrv( newSrv );
//        }else{
//            JOptionPane.showMessageDialog(view,
//                ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_UWS_SRV ) + " : " +
//                view.initor.mdb.getErrorMessage()
//            );
//        }

        SanBootView.log.info(getClass().getName(),"########### End of sdhm set action. " );
    }
}
