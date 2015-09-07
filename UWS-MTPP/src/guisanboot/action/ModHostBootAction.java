/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BindofVolAndSnap;
import guisanboot.data.BootHost;
import guisanboot.data.DhcpClientInfo;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.DhcpBootDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;
import java.util.Vector;

/**
 *
 * @author zourishun
 */
public class ModHostBootAction extends GeneralActionForMainUi{
    public ModHostBootAction(){
        super(
            ResourceCenter.SMALL_MOD_HOST,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modHostBoot",
            MenuAndBtnCenterForMainUi.FUNC_MOD_HOSTBOOT
        );
    }

    @Override public void doAction(ActionEvent evt){
        BindofVolAndSnap bind;
        Object volObj;
        ArrayList snapList;
        int i,j,size,size1,tid;
        boolean isOK;

SanBootView.log.info(getClass().getName(),"########### Entering modify hostBoot action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !(selObj instanceof BootHost) ) return;

        try{
            BootHost selHost = (BootHost)selObj;
            BrowserTreeNode selHostNode = selHost.getTreeNode();

            DhcpBootDialog dialog = new DhcpBootDialog( selHost,view );
            int width  = 340 + ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 145 + ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );

            Object[] ret  = dialog.getValue();
            if( ret == null || ret.length <= 0 ) return;

            String boottype =(String)ret[0];

            String cmd = ResourceCenter.BIN_DIR + "dhcp_set.sh addcli -p " + selHost.getBootMac() + " -boottype " + boottype;
            isOK = view.initor.mdb.modyHostBoot(cmd);

            if( isOK ){
                Vector list = view.initor.dhcpdb.getAllClient();
                int num = list.size();
                for(int i_clnt=0; i_clnt<num; i_clnt++ ){
                    DhcpClientInfo clnt = (DhcpClientInfo)list.elementAt(i_clnt);
                    if(selHost.getIP().equals(clnt.ip))
                        clnt.boottype = boottype;
                }
            }else{
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_BOOT_HOST )+":"+
                        view.initor.mdb.getErrorMessage()
                );
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SanBootView.log.info(getClass().getName(),"########### End of modify hostBoot action. " );
    }

}
