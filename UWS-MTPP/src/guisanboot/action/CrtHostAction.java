/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.data.BootHost;
import guisanboot.datadup.ui.viewobj.ChiefProfile;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.AddHostDialog;
import guisanboot.ui.ChiefHost;
import guisanboot.ui.ChiefHostVolume;
import guisanboot.ui.ChiefNetBootHost;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.GetHostInfoThread;
import guisanboot.ui.ProcessEventOnChiefHost;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

/**
 *
 * @author zourishun
 */
public class CrtHostAction extends GeneralActionForMainUi{
    private ChiefHost chiefHost = null;

    public CrtHostAction(){
        super(
            ResourceCenter.SMALL_ADD_HOST,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.crtHost",
            MenuAndBtnCenterForMainUi.FUNC_CRT_HOST
        );
    }

    @Override public void doAction(ActionEvent evt){
        AddHostDialog dialog =null;
        boolean isOK;
SanBootView.log.info(getClass().getName(),"########### Entering create host action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( selObj instanceof ChiefHost ){
            chiefHost = (ChiefHost)selObj;
            // 发生在root下的chief host node上
            dialog = new AddHostDialog( null,view );
        }else{ // 发生在其他节点上
            dialog = new AddHostDialog( null,view );
        }

        int width  = 275+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 145+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object[] ret = dialog.getValue();
        if( ret == null || ret.length <= 0 ) return;

        String ip =(String)ret[0];
        int port = ((Integer)ret[1]).intValue();

        GetHostInfoThread thread = new GetHostInfoThread(
            view,
            ip,
            port
        );
        view.startupProcessDiag(
            SanBootView.res.getString("MenuAndBtnCenter.pdiagTitle.gettingAgentInfo"),
            SanBootView.res.getString("MenuAndBtnCenter.pdiagTip.gettingAgentInfo"),
            thread
        );

        isOK = thread.isOk();
        if( isOK  ){
            String uuid = thread.getUUID();
            if( uuid.equals("") ){
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_UUID_WIN )+ " : "+ thread.getErrMsgFromGetAgentInfo()
                );
                return;
            }

            // 判断是否存在相同的uuid（当网络启动在另外一台机器上后，源盘恢复后该机器的uuid就和源机器一样了）
            if( view.initor.mdb.getHostFromCacheOnUUID( uuid ) != null ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("MenuAndBtnCenter.error.sameUUID")
                );
                return;
            }

            BootHost newHost = new BootHost(
                -1,
                thread.getHostName(),
                ip,
                thread.getMachine(),
                port,
                port,
                thread.getOSName(),
                "Online",
                uuid,    // host uuid
                0,  // inited ?
                0,
                0,
                1,
                "",
                1,
                1
            );

            isOK = view.initor.mdb.addOneBootHost( newHost );
            if( isOK ){
                newHost.setID( view.initor.mdb.getNewId() );
                view.initor.mdb.addBootHostToVector( newHost );

                processGUIWhenAddHost( newHost );
            }else{
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_ADD_BOOT_HOST ) + " : " +
                    view.initor.mdb.getErrorMessage()
                );
            }
        }else{
            JOptionPane.showMessageDialog(view,
                thread.getErrMsgFromGetAgentInfo()
            );
            return;
        }
SanBootView.log.info(getClass().getName(),"########### End of create host action. " );
    }

    private void processGUIWhenAddHost( BootHost host ){
        BrowserTreeNode hostNode,chiefHostNode;

        // 在树上初始化新建的host( 点击发生在root下的chiefhost上或其他节点上 )
        if( chiefHost != null ){ //点击发生在root下的chiefhost上
            setupHostNodeOnRoot( chiefHost.getTreeNode(),host );
            chiefHostNode = (BrowserTreeNode)chiefHost.getTreeNode();
        }else{ // 发生在其他节点上
            chiefHostNode = view.getChiefNodeOnRoot(
                ResourceCenter.TYPE_CHIEF_HOST_INDEX
            );
            setupHostNodeOnRoot( chiefHostNode,host );
        }

        if( chiefHostNode!=null ){
            view.setCurNode( chiefHostNode );
            view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
            ProcessEventOnChiefHost peOnChiefHost = new ProcessEventOnChiefHost( view );
            TreePath path = new TreePath( chiefHostNode.getPath() );
            peOnChiefHost.processTreeSelection( path );
            peOnChiefHost.controlMenuAndBtnForTreeEvent();
            view.getTree().setSelectionPath( path );
            view.getTree().requestFocus();
        }
    }

    private void setupHostNodeOnRoot( BrowserTreeNode chiefHostNode,BootHost host ){
        BrowserTreeNode hostNode = new BrowserTreeNode( host,false );
        host.setTreeNode( hostNode );
        host.setFatherNode( chiefHostNode );

        ChiefHostVolume chiefHostVol = new ChiefHostVolume();
        BrowserTreeNode chiefHVolNode = new BrowserTreeNode( chiefHostVol,false );
        chiefHostVol.setTreeNode( chiefHVolNode );
        chiefHostVol.setFatherNode( hostNode );

        ChiefProfile chiefProfile = new ChiefProfile();
        BrowserTreeNode chiefProfNode = new BrowserTreeNode( chiefProfile,false );
        chiefProfile.setTreeNode( chiefProfNode );
        chiefProfile.setFatherNode( hostNode );

        ChiefNetBootHost chiefNBootHost = new ChiefNetBootHost();
        BrowserTreeNode chiefNBHNode = new BrowserTreeNode( chiefNBootHost,false );
        chiefNBootHost.setTreeNode( chiefNBHNode );
        chiefNBootHost.setFatherNode( hostNode );

        hostNode.add( chiefHVolNode );
        hostNode.add( chiefProfNode );
        hostNode.add( chiefNBHNode );

        view.insertNodeToTree( chiefHostNode,hostNode );
    }
}
