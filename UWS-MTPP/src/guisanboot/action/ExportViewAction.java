/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.Audit;
import guisanboot.data.GetAgentInfo;
import guisanboot.data.LogicalVol;
import guisanboot.data.MirrorDiskInfo;
import guisanboot.data.View;
import guisanboot.data.Volume;
import guisanboot.data.VolumeMap;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.ChiefLunMap;
import guisanboot.ui.ExportViewDialog;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ProcessEventOnChiefLunMap;
import guisanboot.ui.ProgressDialog;
import guisanboot.ui.SanBootView;
import guisanboot.unlimitedIncMj.model.table.CloneDisk;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.tree.TreePath;
import mylib.UI.Browser;
import mylib.UI.BrowserTreeNode;

import guisanboot.data.GetFreePhyVol;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import guisanboot.cmdp.service.GetFreePhyVol1;
import guisanboot.data.SystemPartitionForWin;

/**
 *
 * @author zourishun
 */
public class ExportViewAction extends GeneralActionForMainUi{
    public ExportViewAction(){
        super(
            ResourceCenter.MENU_ICON_BLANK,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.ExportView",
            MenuAndBtnCenterForMainUi.FUNC_EXPORTLM
        );
    }

    @Override public void doAction(ActionEvent evt){
        int tid;
        ChiefLunMap chiefLm;
        BrowserTreeNode chiefLMNode=null,diskNode=null;
        Object volObj;

SanBootView.log.info(getClass().getName(),"########### Entering export lunmap action." );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        if( !( selObj instanceof ChiefLunMap ) &&
            !( selObj instanceof View ) &&
            !( selObj instanceof VolumeMap ) &&
            !( selObj instanceof Volume ) &&
            !( selObj instanceof LogicalVol ) &&
            !( selObj instanceof MirrorDiskInfo ) &&
            !( selObj instanceof CloneDisk  )
        ){
            return;
        }

        if( selObj instanceof ChiefLunMap ){
            chiefLm = (ChiefLunMap)selObj;
            chiefLMNode = chiefLm.getTreeNode();
            BrowserTreeNode volNode = chiefLm.getFatherNode();
            volObj = volNode.getUserObject();
        }else if( selObj instanceof View ){
            View viewObj = (View)selObj;
            diskNode = viewObj.getTreeNode();
            volObj = selObj;
        }else if( selObj instanceof VolumeMap ){
            VolumeMap volMapObj = (VolumeMap)selObj;
            diskNode = volMapObj.getTreeNode();
            volObj = selObj;
        }else if( selObj instanceof Volume ){
            Volume vObj = (Volume)selObj;
            diskNode = vObj.getTreeNode();
            volObj = selObj;
        }else if( selObj instanceof LogicalVol ){
            LogicalVol lvObj = (LogicalVol)selObj;
            diskNode = lvObj.getTreeNode();
            volObj = selObj;
        }else if( selObj instanceof MirrorDiskInfo ){
            MirrorDiskInfo mdi =(MirrorDiskInfo)selObj;
            diskNode = mdi.getTreeNode();
            volObj = selObj;
        }else{ // CloneDisk
            CloneDisk cd  = (CloneDisk)selObj;
            diskNode = cd.getTreeNode();
            volObj = selObj;
        }

        if( diskNode != null ){
            chiefLMNode = view.getChiefLunMapNodeOnViewNode( diskNode );
        }

        ExportViewDialog dialog = new ExportViewDialog( view );
        int width = 400+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 320+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object[] ret = dialog.getValues();
        if( ret == null || ret.length <= 0 ) return;

        if( volObj instanceof Volume ){
            Volume vol = (Volume)volObj;
            tid = vol.getTargetID();
        }else if( volObj instanceof LogicalVol ){
            LogicalVol lv = (LogicalVol)volObj;
            tid = view.initor.mdb.getTargetIDOnLV( lv );
        }else if( volObj instanceof View){
            View viewObj = (View)volObj;
            tid = viewObj.getTargetID();
        }else if( volObj instanceof MirrorDiskInfo ){
            MirrorDiskInfo mdi = (MirrorDiskInfo)volObj;
            tid = mdi.getTargetID();
        }else if( volObj instanceof CloneDisk ){
            CloneDisk cloneDisk =(CloneDisk)volObj;
            tid = cloneDisk.getTarget_id();
        }else{
            VolumeMap volMap = (VolumeMap)volObj;
            tid = volMap.getVolTargetID();
        }

        Audit audit = view.audit.registerAuditRecord( 0,MenuAndBtnCenterForMainUi.FUNC_LUNMAP );

        boolean isOk = view.initor.mdb.addLunMap(
            tid,
            (String)ret[0], // ip
            (String)ret[1], // mask
            (String)ret[2], // rwset
            (String)ret[3], // sev user
            (String)ret[4], // ser passwd
            " ", //(String)ret[5], //client user
            " " //(String)ret[6] // client passwd
        );

        if( isOk ){
            audit.setEventDesc( "Add lunmap for target:" + tid +" on " + ret[0] + " successfully." );
            view.audit.addAuditRecord( audit );

            // 显示点击 chiefLMNode 后的右边tabpane中的内容
            if( chiefLMNode != null ){
                view.setCurNode( chiefLMNode );
                view.setCurBrowserEventType( Browser.TREE_SELECTED_EVENT );
                ProcessEventOnChiefLunMap peOnChiefLM= new ProcessEventOnChiefLunMap( view );
                TreePath path = new TreePath( chiefLMNode.getPath() );
                peOnChiefLM.processTreeSelection( path );
                peOnChiefLM.controlMenuAndBtnForTreeEvent();
                view.getTree().setSelectionPath( path );
                view.getTree().requestFocus();
            }
        }else{
            audit.setEventDesc( "Failed to add lunmap for target: " + tid +" on " + ret[0] );
            view.audit.addAuditRecord( audit );

            view.showError1(
                ResourceCenter.CMD_ADD_LUNMAP,
                view.initor.mdb.getErrorCode(),
                //ResourceCenter.getGeneralErr( view.initor.mdb.getErrorCode() )
                view.initor.mdb.getErrorMessage()
            );
            return;
        }

        //添加新门户
        String svrIP = view.initor.getTxIP((String)ret[0]); //view.initor.mdb.getUWSDefaultIp();
        isOk = view.initor.mdb.addPortal((String)ret[0], ResourceCenter.CMDP_AGENT_PORT, svrIP, ResourceCenter.ISCSI_LOGIN_PORT, ResourceCenter.CMD_TYPE_CMDP);
        
        if (!isOk) {
            view.showError1(
                ResourceCenter.CMD_ADD_LUNMAP,
                view.initor.mdb.getErrorCode(),
                view.initor.mdb.getErrorMessage()
            );
            return;
        }
        // Collect host info before initing it
        ProgressDialog initDiag = new ProgressDialog( 
            view,
            SanBootView.res.getString("View.pdiagTitle.getInitInfo"),
            SanBootView.res.getString("View.pdiagTip.getInitInfo")
        );
        GetInitInfoForPhyThread getInitInfo = new GetInitInfoForPhyThread(
            initDiag, (String)ret[0], ResourceCenter.CMDP_AGENT_PORT, true, view, true
        );
        getInitInfo.start();
        initDiag.mySetSize();
        initDiag.setLocation( view.getCenterPoint( initDiag.getDefWidth(),initDiag.getDefHeight() ) );
        initDiag.setVisible( true );
        
        if( !getInitInfo.getAgtInfoIsOK() ){ return; }
        if( !getInitInfo.getOSServiceIsOK() ){ return; }

        Vector partList = getInitInfo.getSysPartList();
        for( int i=0; i<partList.size(); i++ ){
            SystemPartitionForWin part = (SystemPartitionForWin)partList.elementAt(i);
            if( ((String)ret[5]).substring(0,1).toUpperCase().equals( part.getSingleDiskLetter() ) ){
                view.showError1(
                    ResourceCenter.CMD_ADD_LUNMAP,
                    1,
                    SanBootView.res.getString("SelectIdleDiskDialog.table.disk.existname")
                );
                return;
            }
        }

 
       //挂载数据盘
       String targetSrvName = view.initor.mdb.getHostName();
       String iscsiVar = ResourceCenter.ISCSI_PREFIX + targetSrvName + ":" + tid;
       isOk = view.initor.mdb.assignDriver1((String)ret[0], 2015,
                                    svrIP, ResourceCenter.ISCSI_LOGIN_PORT + "", iscsiVar,
                                    (String)ret[5], ResourceCenter.CMD_TYPE_CMDP);
       if (!isOk) {
            view.showError1(
                ResourceCenter.CMD_ADD_LUNMAP,
                view.initor.mdb.getErrorCode(),
                view.initor.mdb.getErrorMessage()
            );
            return;
        }

SanBootView.log.info(getClass().getName(),"########### End of export lunmap action. " );
    }
}


class GetInitInfoForPhyThread extends Thread{
    ProgressDialog pdiag;
    String ip;
    int port;
    boolean isGetAgtInfo;
    boolean isOK = true;
    SanBootView view;
    boolean isFirst;

    private boolean getAgtInfo = false;
    private boolean getOrphanVol = false;
    private boolean getSystemPart = false;
    private boolean getDiskPartForWin = false;
    private boolean isStartupfromSAN = false;
    private boolean getOsService = false;

    private GetFreePhyVol getOrVol;
    private String partitionContent;
    private Vector osService;
    private Vector sysPartList;
    private String diskDetailInfo;
    private GetAgentInfo getAgentInfo;
    private String uuid;
    private boolean startNet = false;

    Runnable close = new Runnable(){
        public void run(){
            pdiag.dispose();
        }
    };

    public GetInitInfoForPhyThread(  ProgressDialog pdiag, String ip,int port,boolean isGetAgtInfo,SanBootView view,boolean isFirst ){
        this.pdiag = pdiag;
        this.ip = ip;
        this.port = port;
        this.isGetAgtInfo = isGetAgtInfo;
        this.view = view;
        this.isFirst = isFirst;
    }

    @Override public void run(){
        boolean isOk;

        if( isGetAgtInfo ){
            try{
                getAgentInfo = new GetAgentInfo(
                    ResourceCenter.getCmdpS2A_CmdPath1(ip, port) + "getsysinfo 2>/dev/null",
                    view.getSocket()
                );
                getAgentInfo.setCmdType( ResourceCenter.CMD_TYPE_CMDP );
                isOk =  getAgentInfo.getAgentInfo();
                if( isOk ){
                    if( !getAgentInfo.getOSName().toUpperCase().startsWith("WIN") ){
                        JOptionPane.showMessageDialog( pdiag,
                            SanBootView.res.getString("InitBootHostWizardDialog.error.notWinHost")
                        );
                        isOK = false;
                    }else{
                        view.initor.mdb.getHostUUID( ip,port,true,ResourceCenter.CMD_TYPE_CMDP );
                        if( view.initor.mdb.getUUID().equals("") ){
SanBootView.log.error( getClass().getName(),"errmsg of getuuid:  " + view.initor.mdb.getErrorMessage() );
                            if( view.initor.mdb.getErrorMessage().trim().toUpperCase().equals("CAN NOT GET AGENT INSTALL PATH")||
                                view.initor.mdb.getErrorMessage().trim().toUpperCase().equals("CAN NOT OPEN SOFTWARE\\ODYSYS\\AGENT")
                            ){
SanBootView.log.error( getClass().getName(),"need to recreate new client's UUID : " + ip  );
                                if( !view.initor.mdb.reCrtUUID( ip, port, ResourceCenter.CMD_TYPE_CMDP,true ) ){
SanBootView.log.error( getClass().getName(),"failed to recreate uuid for client: " + ip );
                                    JOptionPane.showMessageDialog( pdiag,
                                        SanBootView.res.getString("InitNWinHostWizardDialog.log.getHostInfo")+
                                            SanBootView.res.getString("common.failed")
                                    );
                                    isOK = false;
                                }else{
                                    view.initor.mdb.getHostUUID( ip,port,true,ResourceCenter.CMD_TYPE_CMDP );
                                    if( view.initor.mdb.getUUID().equals("") ){
SanBootView.log.error( getClass().getName(),"Client's UUID is none: " + ip );
                                        JOptionPane.showMessageDialog( pdiag,
                                            SanBootView.res.getString("InitNWinHostWizardDialog.log.getHostInfo")+
                                                SanBootView.res.getString("common.failed")
                                        );
                                        isOK = false;
                                    }
                                }
                            }else{
SanBootView.log.error( getClass().getName(),"Client's UUID is none: " + ip );
                                JOptionPane.showMessageDialog( pdiag,
                                    SanBootView.res.getString("InitNWinHostWizardDialog.log.getHostInfo")+
                                        SanBootView.res.getString("common.failed")
                                );
                                isOK = false;
                            }
                        }else{
                            uuid = view.initor.mdb.getUUID();

                            // 判断是否存在相同的uuid（当网络启动在另外一台机器上后，源盘恢复后该机器的uuid就和源机器一样了）
                            // 下面的判断方法不对,还需要更多的依据（2008/10/31）
                            //if( view.initor.mdb.getHostFromCacheOnUUID( uuid ) != null ){
                            if( false ){
                                JOptionPane.showMessageDialog(view,
                                    SanBootView.res.getString("MenuAndBtnCenter.error.sameUUID")
                                );
                                isOK = false;
                            }else{
                                isOK = true;
                                getAgtInfo = true;
                            }
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog( pdiag,
                        SanBootView.res.getString("InitBootHostWizardDialog.log.getHostInfo")+
                            SanBootView.res.getString("common.failed")
                    );
                    isOK = false;
                }
            }catch(Exception ex){
                JOptionPane.showMessageDialog( pdiag,
                    SanBootView.res.getString("InitBootHostWizardDialog.log.getHostInfo")+
                        SanBootView.res.getString("common.failed")
                );
                isOK = false;
            }
        }else{
            getAgtInfo = true;
        }

        if( isFirst ){
            if( isOK ){
                try{
                    getOrVol = new GetFreePhyVol1(
                        ResourceCenter.getCmd(
                            ResourceCenter.CMD_GET_VOL
                        ),
                        view.getSocket(),
                        view
                    );
                    getOrVol.setAddCacheFlag( true );
                    // 获取所有的disk,不仅是free disk
                    getOrVol.setFilterFlag( false );

                    isOk = getOrVol.realDo();
                    if( !isOk ){
                        JOptionPane.showMessageDialog( pdiag,
                            SanBootView.res.getString("InitBootHostWizardDialog.error.getOrphanVol")
                        );
                        isOK = false;
                    }else{
                        getOrphanVol = true;
                    }
                }catch( Exception exp ){
                    JOptionPane.showMessageDialog( pdiag,
                        SanBootView.res.getString("InitBootHostWizardDialog.error.getOrphanVol")
                    );
                    isOK = false;
                }
            }
        }

        if( isOK ){
            isOk = view.initor.mdb.getVolInfoForCMDP( ip,port );
            if( !isOk ){
                JOptionPane.showMessageDialog(pdiag,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_PARTITION )+" : "+
                        view.initor.mdb.getErrorMessage()
                );
                isOK = false;
            }else{
                partitionContent = view.initor.mdb.getVolInfoContentsForCMDP();
                sysPartList = new Vector();
                Object[] partList = view.initor.mdb.getLocakDiskFromVolInfoForCMDP();
                for( int i=0; i<partList.length; i++ ){
                    sysPartList.add( partList[i] );
                }

                if( partitionContent.equals("") ){
SanBootView.log.error( this.getClass().getName(),"There is no valid local volume( no file system )");
                    JOptionPane.showMessageDialog( pdiag,
                        SanBootView.res.getString("InitBootHostWizardDialog.error.volInfoIsNull")
                    );
                    getSystemPart = false;
                }else{
                    getSystemPart = true;
                }
            }
        }

        if( isOK ){
            isOk = view.initor.mdb.getOSService( ip,port,"",ResourceCenter.CMD_TYPE_CMDP );
            if( isOk ){
                this.osService = view.initor.mdb.getOSService();
                getOsService = true;
            }else{
                JOptionPane.showMessageDialog(pdiag,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_OS_SERVICE )+" : "+
                    SanBootView.res.getString("common.failed")
                );
                isOK = false;
            }
        }

        if( isOK ){
            isOk = view.initor.mdb.getDiskPartForWin( ip,port,"ib_save_partition.exe -list_local",ResourceCenter.CMD_TYPE_CMDP );
            if( !isOk ){
                JOptionPane.showMessageDialog(pdiag,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_GET_PARTITION )+" : "+
                        view.initor.mdb.getErrorMessage()
                );
                isOK = false;
            }else{
                diskDetailInfo = view.initor.mdb.getDiskPartForWin();
                if( diskDetailInfo.equals("") ){
SanBootView.log.error( getClass().getName(),"Can't get disk partition info,so will not auto-restore-partition later. But we will tolerate this error.");
                    diskDetailInfo = "Can't get disk partition when initiating the client.";
                }
                getDiskPartForWin = true;
            }
        }

        if( isOK ){
            isOk = view.initor.mdb.isStartupfromSAN( ip,port,"C",ResourceCenter.CMD_TYPE_CMDP );
            if( isOk ){
                startNet = view.initor.mdb.isStartupFromSAN();
                isStartupfromSAN = true;
            }else{
                JOptionPane.showMessageDialog(pdiag,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_IS_STARTUP_FROM_NET )+" : "+
                        view.initor.mdb.getErrorMessage()
                );
                isOK = false;
            }
        }

        try{
            SwingUtilities.invokeAndWait( close );
        }catch( Exception ex ){
            ex.printStackTrace();
        }
    }

    public boolean isOK(){
        return isOK;
    }

    public boolean getAgtInfoIsOK(){
        return getAgtInfo;
    }

    public GetAgentInfo getAgentInfoCmdObj(){
        return getAgentInfo;
    }

    public String getUUID(){
        return uuid;
    }

    public boolean getOrphanVolIsOK(){
        return getOrphanVol;
    }

    public boolean getSystemPartIsOK(){
        return getSystemPart;
    }

    public boolean getDiskPartForWinIsOK(){
        return getDiskPartForWin;
    }

    public boolean getOSServiceIsOK(){
        return this.getOsService;
    }

    public boolean isStartupFromSAN(){
        return isStartupfromSAN;
    }

    public ArrayList getOrphanVolList(){
        if( getOrVol != null ){
            return getOrVol.getAllVolAndFreeDiskFromCache();
        }else{
            return new ArrayList();
        }
    }

    public String getSystemPartContents(){
        return partitionContent;
    }

    public Vector getOSServicelist(){
        return this.osService;
    }

    public Vector getSysPartList(){
        return sysPartList;
    }

    public String getDiskDetailInfo(){
        return diskDetailInfo;
    }

    public boolean isStartFromNet(){
        return startNet;
    }
}
