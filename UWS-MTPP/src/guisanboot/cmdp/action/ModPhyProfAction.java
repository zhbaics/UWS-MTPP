/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.cmdp.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.cmdp.entity.PPProfile;
import guisanboot.cmdp.service.ProcessEventOnChiefPPProfile;
import guisanboot.data.BootHost;
import guisanboot.data.Service;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 * ModPhyProfAction.java
 *
 * Created on 2010-6-7, 13:50:44
 */
public class ModPhyProfAction extends GeneralActionForMainUi{
    public ModPhyProfAction(){
        super(
            ResourceCenter.ICON_MOD_PROFILE,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.modProf",
            MenuAndBtnCenterForMainUi.FUNC_PHY_MOD_PROFILE
        );
    }

    @Override public void doAction( ActionEvent evt ){
SanBootView.log.info(getClass().getName(),"########### Entering modify ppprofile action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isProf = ( selObj instanceof PPProfile );
        if( !isProf ) return;

        PPProfile ppprof = (PPProfile)selObj;
        int clntId = ppprof.getHostID();
        BootHost host = view.initor.mdb.getBootHostFromVector( (long)clntId );
        Vector serviceList;
        if( host != null ){
            if( host.isWinHost() ){
            if( !view.initor.mdb.getOSService( host.getIP(),host.getPort(),"",ResourceCenter.CMD_TYPE_CMDP ) ){
                // 创建一个空的service list,使程序继续运行（2010.11.8）
SanBootView.log.warning(getClass().getName(), "failed to get os service from client, so create empty service list to continue this process.");
                serviceList = new Vector();
            }else{
                serviceList = view.initor.mdb.getOSService();
            }
            
            int size = serviceList.size();
            int cnt = size;

            boolean founded;
            Vector servMapList = view.initor.mdb.getServiceOfVolMapOnClntID( host.getID() );
            int size1 = servMapList.size();
            for( int j=0; j<size1; j++ ){
                String servMap =(String)servMapList.elementAt( j );

                founded = false;
                for( int i=0; i<size; i++ ){
                    Service service = (Service)serviceList.elementAt(i);
                    if( service.getServName().equals( servMap ) ){
                        founded = true;
                        break;
                    }
                }

                if( !founded ){
SanBootView.log.debug(getClass().getName(), " add servermap onto GUI: " + servMap );
                    serviceList.addElement( new Service( cnt, servMap, "" ) );
                    cnt++;
                }
            }
            } else {
                serviceList = new Vector();
            }
        }else{
            // impossible to happen.
SanBootView.log.error( getClass().getName(), "can not find host obj for this ppprofile.");
            return;
        }

        guisanboot.cmdp.ui.EditPhyProfileDialog dialog = new guisanboot.cmdp.ui.EditPhyProfileDialog( view,ppprof,serviceList );
        int width  = 520+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 380+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        BrowserTreeNode fNode = ppprof.getFatherNode();
        if( fNode != null ){
            view.setCurNode( fNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefPPProfile peOnChiefPPProf = new ProcessEventOnChiefPPProfile( view );
            TreePath path = new TreePath( fNode.getPath() );
            peOnChiefPPProf.processTreeSelection( path );
            peOnChiefPPProf.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }
SanBootView.log.info(getClass().getName(),"########### End of modify ppprofile action. " );
    }
}
