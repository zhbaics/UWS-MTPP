/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.cluster.entity.Cluster;
import guisanboot.data.BootHost;
import guisanboot.datadup.data.BackupClient;
import guisanboot.datadup.data.BakObject;
import guisanboot.datadup.data.UniProfile;
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
public class ModProfAction extends GeneralActionForMainUi{
    public ModProfAction(){
        super(
            ResourceCenter.ICON_MOD_PROFILE,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modProf",
            MenuAndBtnCenterForMainUi.FUNC_MOD_PROF
        );
    }

    @Override public void doAction( ActionEvent evt ){
        int width,height;
        BootHost host = null;
        Cluster cluster = null;
        ArrayList hidenFs = null;

SanBootView.log.info(getClass().getName(),"########### Entering modify profile action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isProf = ( selObj instanceof UniProfile );
        if( !isProf ) return;

        UniProfile _prof = (UniProfile)selObj;
        BrowserTreeNode chiefProfNode = _prof.getFatherNode();
        ChiefProfile chiefProf = (ChiefProfile)chiefProfNode.getUserObject();
        BrowserTreeNode hostNode = chiefProf.getFatherNode();
        Object obj = hostNode.getUserObject();
        if( obj instanceof BootHost ){
            host =(BootHost)obj;
            if( host.isIA() ){
                boolean ok = view.initor.mdb.getOldDiskPartitionTableForUnix( ResourceCenter.CLT_IP_CONF + "/" + host.getID() + ResourceCenter.CONF_OLDDISK );
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
            cluster = (Cluster)obj;
        }

        // get real profile from cache. contents of profile on GUI maybe older.
        UniProfile prof = view.initor.mdb.getOneProfile( _prof.getProfileName() );
SanBootView.log.info( getClass().getName(),"profile to modify: "+_prof.getProfileName() );
        if( prof == null ) return;

        BackupClient bkClnt = view.initor.mdb.getBkClntOnUUID( host.getUUID() );
SanBootView.log.info( getClass().getName()," host uuid: "+ host.getUUID() );

        if( bkClnt == null ){
SanBootView.log.error( getClass().getName()," no d2d client for boot host "+ host.getName() );
            JOptionPane.showMessageDialog(view,
                SanBootView.res.getString("common.errcode.inconsistentProf")
            );
            return;
        }else{
            if( cluster != null ){
                host = view.initor.mdb.getHostFromVectOnD2DClntID( (int)bkClnt.getID() );
                if( host == null ){
SanBootView.log.error( getClass().getName()," not find boot host according to d2d_clnt_id:"+ prof.getUniProIdentity().getClntID() );
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("common.errcode.inconsistentProf")
                    );
                    return;
                }else{
                    host.setTreeNode( cluster.getTreeNode() );
                }
            }
            // 换成当前正在使用的ip和port比较保险
            bkClnt.setIP( host.getIP() );
            bkClnt.setPort( host.getPort() );
        }

        String bkobjId = prof.getUniProIdentity().getBkObj_ID();
        // 进去之前先重新获取一下backup object,因为备份的时候bkobj会变化，所以只能update bk obj
        view.initor.mdb.updateBakObjList( bkobjId );
        BakObject bkObj = view.initor.mdb.getOneBakObject();
        if( bkObj == null ){
SanBootView.log.error( getClass().getName()," no d2d bkobj, bkobj id: "+ bkobjId );
            JOptionPane.showMessageDialog(view,
                SanBootView.res.getString("common.errcode.inconsistentProf")
            );
            return;
        }else{
            // update bkboj in cache
            view.initor.mdb.removeBakObjFromVector( bkObj.getBakObjID() );
            view.initor.mdb.addBakObjIntoVector( bkObj );
        }

        String windir ="";
        if( host.isWinHost()  ){
            // if failed, re-get windir in EditProfileDialog
            windir = view.initor.mdb.getWinDir( host.getIP(),host.getMtppPort() );
SanBootView.log.info( getClass().getName(), "windir: " + windir );
        }

        ArrayList schList = view.initor.mdb.getSchOnProfName( prof.getProfileName() );

        EditProfileDialog dialog = new EditProfileDialog( windir,view, host, bkClnt, schList, bkObj,prof,hidenFs );
        if( cluster != null ){
            dialog.setRoot_vol_list( cluster.getMtppRootVolForCluster( host ) );
        }
        width  = 560+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        height = 480+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );
SanBootView.log.info(getClass().getName(),"########### End of modify profile action. " );
    }
}
