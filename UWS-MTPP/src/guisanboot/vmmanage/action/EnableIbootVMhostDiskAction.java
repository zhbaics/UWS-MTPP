/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.vmmanage.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.View;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import guisanboot.vmmanage.ui.EnableIbootDialog;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author zourishun
 */
public class EnableIbootVMhostDiskAction extends GeneralActionForMainUi{
    public EnableIbootVMhostDiskAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.enableIboot",
            MenuAndBtnCenterForMainUi.FUNC_ENABLE_IBOOT_VMHOST_DISK
        );
    }
    
    @Override public void doAction(ActionEvent evt){
    SanBootView.log.info(getClass().getName(),"########### Entering create ucs_snap action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ){
            return ;
        }
        View objView ;
        Object[] ret;
        if(selObj instanceof View){
            objView = (View)selObj;
            int rootid = objView.getSnap_root_id() ;
            VolumeMap volmap = view.initor.mdb.getVolMapOnRootID(rootid);
            int targetid = objView.getSnap_target_id();
            String letter = "";
            if( volmap != null ){
                letter = volmap.getVolDiskLabel().substring(0,1).toLowerCase();
            }
            if( !"c".equals(letter) ){
                JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("EnableIboot.error.wrongOSletter")
                    );
                return;
            }
            
            String ip = "";
            int port = 0 ;
            boolean isOk = view.initor.mdb.updateVMServerInfo();
            if( isOk ){
                ip = view.initor.mdb.getVMServerIP();
                port = view.initor.mdb.getVMServerPort();
            }
            
            EnableIbootDialog dialog = new EnableIbootDialog( view ,ip ,port );
            int width = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            int height = 150+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            dialog.setSize( width,height );
            dialog.setLocation( view.getCenterPoint( width,height ) );
            dialog.setVisible( true );
                
            ret = dialog.getValues();
            if( ret == null ) {
                SanBootView.log.info(getClass().getName(), "cancel to enable Iboot.");
                return;
            }
            String retip = ret[0].toString();
            int retport = ((Integer)ret[1]).intValue();
            String args = ip + "," + port + "," + letter + "," + targetid;
            
            EnableIbootThread thread = new EnableIbootThread(view , args);
                view.startupProcessDiag(
                    SanBootView.res.getString("View.pdiagTitle.enableIboot"),
                    SanBootView.res.getString("View.pdiagTip.enableIboot"),
                    thread
                );
        } else {
            return;
        }
    }
}

class EnableIbootThread extends BasicGetSomethingThread{
    private String args ;
    public EnableIbootThread( SanBootView _view ,String _args){
        super( _view );
        this.args = _args;
    }

    @Override
    public boolean realRun() {
        
        boolean isOk = view.initor.mdb.enableIbootDiskView(args);
        if( isOk ){
             JOptionPane.showMessageDialog(view, SanBootView.res.getString( "EnableIboot.mes.success" ) );
        } else {
             JOptionPane.showMessageDialog(view, SanBootView.res.getString( "EnableIboot.mes.failed" ) );
        }
        return isOk;
    }
}