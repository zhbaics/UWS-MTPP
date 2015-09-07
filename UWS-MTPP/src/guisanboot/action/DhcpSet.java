/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.DhcpDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.InitProgramDialog;
import guisanboot.ui.LaunchSomething;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;

/**
 *
 * @author zourishun
 */
public class DhcpSet extends GeneralActionForMainUi{
    public DhcpSet(){
        super(
            ResourceCenter.SMALL_DHCP,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.dhcp",
            MenuAndBtnCenterForMainUi.FUNC_DHCP
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering dhcp operation action. " );
        DhcpDialog dialog = new DhcpDialog( view );

        // 图形化的显示初始化过程
        InitProgramDialog initDiag = new InitProgramDialog(
            view,
            SanBootView.res.getString("DhcpDialog.initTitle"),
            SanBootView.res.getString("DhcpDialog.initLabel")
        );
        Thread initThread = new Thread( new LaunchSomething( initDiag,dialog ) );
        initThread.start();
        int width  = 300+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 120+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        initDiag.setSize( width, height );
        initDiag.setLocation( view.getCenterPoint(width,height) );
        initDiag.setVisible( true );

        width  = 480+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        height = 465+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of dhcp operation acion. " );
    }
}
