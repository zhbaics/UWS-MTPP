/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.cluster.entity.Cluster;
import guisanboot.cluster.ui.SelectMTPPHostDialog;
import guisanboot.data.BootHost;
import guisanboot.data.HostWrapper;
import guisanboot.datadup.data.BackupClient;
import guisanboot.datadup.ui.EditProfileDialog;
import guisanboot.datadup.ui.viewobj.ChiefProfile;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class AddProfAction extends GeneralActionForMainUi{
    public AddProfAction(){
        super(
            ResourceCenter.ICON_ADD_PROFILE,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.addProf",
            MenuAndBtnCenterForMainUi.FUNC_ADD_PROF
        );
    }

    @Override public void doAction(ActionEvent evt){
        int width,height;
        boolean ok;
        BootHost host = null;
        Cluster cluster = null;
        ArrayList hidenFs = null;
        BackupClient bkClnt = null;

SanBootView.log.info(getClass().getName(),"########### Entering add lp-profile action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isChiefProf = ( selObj instanceof ChiefProfile );
        if( !isChiefProf ) return;

        ChiefProfile chiefProf = (ChiefProfile)selObj;
        BrowserTreeNode hostNode = chiefProf.getFatherNode();
        Object obj = hostNode.getUserObject();
        if( obj instanceof BootHost ){
            host = (BootHost)obj;
            if( !host.isInited() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notInited")
                );
                return;
            }
            if( host.isIA() ){
                ok = view.initor.mdb.getOldDiskPartitionTableForUnix( ResourceCenter.CLT_IP_CONF + "/" + host.getID() + ResourceCenter.CONF_OLDDISK );
                if( ok ){
                    hidenFs = view.initor.mdb.getIAHidenFsFromOldDiskPartForUnix();
                }else{
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.getHidenFs")
                    );
                    return;
                }
            }
        }else{
            cluster =(Cluster)obj;
            if( !cluster.isInited() ){
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.notInited1")
                );
                return;
            }

            ArrayList<BootHost> mtpp_host = cluster.getHostProtectedByMTPP();
            SelectMTPPHostDialog diag = new SelectMTPPHostDialog( view,mtpp_host );
            width  = 330+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
            height = 140+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
            diag.setSize( width,height );
            diag.setLocation( view.getCenterPoint( width,height ) );
            diag.setVisible( true );

            Object ret = diag.getSelectedHost();
            if( ret == null ) return;

            host = ((HostWrapper)ret).host;
            host.setTreeNode( cluster.getTreeNode() );
        }

        if( cluster == null ){
            bkClnt = view.initor.mdb.getBkClntOnUUID( host.getUUID() );
SanBootView.log.info( getClass().getName()," host uuid: "+ host.getUUID() );
        }else{
            bkClnt = view.initor.mdb.getClientFromVector( (long)host.getClnt_d2d_cid() );
        }

        if( bkClnt == null ){
SanBootView.log.warning( getClass().getName()," no d2d client for boot host "+ host.getName() );
            bkClnt = new BackupClient( host.getName(), host.getIP(), host.getMachine(), host.getMtppPort(), host.getOS(), host.getUUID() );
            ok = view.initor.mdb.addOneClient( bkClnt );
            if( !ok ){
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_CLIENT )+
                    ": "+
                    view.initor.mdb.getErrorMessage()
                );
                return;
            }else{
                bkClnt.setID( view.initor.mdb.getNewId() );
                view.initor.mdb.addBakClntIntoCache( bkClnt );
            }
        }else{
            // 换成当前正在使用的ip和port比较保险
            bkClnt.setIP( host.getIP() );
            bkClnt.setPort( host.getMtppPort() );
        }

        String windir="";
        if( host.isWinHost()  ){
            // if failed, re-get windir in EditProfileDialog
            windir = view.initor.mdb.getWinDir( host.getIP(),host.getMtppPort() );
SanBootView.log.info(getClass().getName(), "windir: " + windir );
        }

        EditProfileDialog dialog = new EditProfileDialog( windir,view,host,bkClnt,new ArrayList(0),null,null,hidenFs );
        if( cluster != null ){
            dialog.setRoot_vol_list( cluster.getMtppRootVolForCluster( host ) );
        }
        width  = 560+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        height = 480+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of add profile action. " );
    }
}
