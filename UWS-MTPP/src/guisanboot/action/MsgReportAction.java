/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.UWSRptConf;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ReportConfDialog;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;

/**
 *
 * @author zourishun
 */
public class MsgReportAction extends GeneralActionForMainUi{
    public MsgReportAction(){
        super(
            ResourceCenter.BTN_ICON_RPT_16,
            ResourceCenter.BTN_ICON_RPT_50,
            "View.MenuItem.msgReport",
            MenuAndBtnCenterForMainUi.FUNC_MSG_REPORT
        );
    }

    @Override public void doAction(ActionEvent evt){
        UWSRptConf conf;
SanBootView.log.info(getClass().getName(),"########### Entering message report action. " );
        boolean isOk = view.initor.mdb.getUWSRptConf();
        if( !isOk ){
            conf = new UWSRptConf();
        }else{
            conf = view.initor.mdb.getUWSRptConfContent();
        }

        ReportConfDialog dialog = new ReportConfDialog( view,conf );
        int width  = 540+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 455+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of message report action. " );
    }
}
