/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JOptionPane;
import mylib.UI.BrowserTreeNode;
import guisanboot.res.ResourceCenter;
import guisanboot.data.*;
import guisanboot.ui.*;
import guisanboot.datadup.data.UniProfile;
import guisanboot.datadup.ui.viewobj.ChiefProfile;
import guisanboot.cluster.entity.Cluster;
import guisanboot.audit.data.Audit;
import guisanboot.datadup.data.BackupClient;
import guisanboot.datadup.data.UniProIdentity;
import guisanboot.datadup.data.BakObject;
import guisanboot.datadup.cmd.RunBackup;
import guisanboot.datadup.ui.MonitorDialog;
import guisanboot.datadup.ui.TaskListGeter;
/**
 *
 * @author zourishun
 */
public class RunProfAction extends GeneralActionForMainUi{
    public RunProfAction(){
        super(
            ResourceCenter.ICON_RUN_PROFILE,
            ResourceCenter.MENU_ICON_BLANK,
            "View.MenuItem.runProf",
            MenuAndBtnCenterForMainUi.FUNC_RUN_PROF
        );
    }

    @Override public void doAction(ActionEvent evt){
        BootHost host = null;
        Cluster cluster = null;
SanBootView.log.info(getClass().getName(),"########### Entering run profile action. " );
        Object selObj = view.getSelectedObjFromSanBoot();
        if( selObj == null ) return;

        boolean isProf = ( selObj instanceof UniProfile );
        if( isProf ){
            UniProfile _prof = (UniProfile)selObj;
            BrowserTreeNode fNode = _prof.getFatherNode();
            ChiefProfile chiefProf = (ChiefProfile)fNode.getUserObject();
            fNode = chiefProf.getFatherNode();
            Object obj = fNode.getUserObject();
            if( obj instanceof BootHost ){
                host = (BootHost)obj;
            }else{
                cluster = (Cluster)obj;
            }
            // get real profile from cache. contents of profile on GUI maybe older.
            UniProfile prof = view.initor.mdb.getOneProfile( _prof.getProfileName() );
SanBootView.log.info( getClass().getName(),"profile to run: " + _prof.getProfileName() );
            if( prof == null ) {
SanBootView.log.error( getClass().getName(), "not found profile from memory cache.");
                return;
            }

            // 必须是初始化好的，否则profile中的target可能不对��ܲ
            if( cluster == null ){
                if( !host.isInited() ){
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notInited")
                    );
                    return;
                }
            }else{
                if( !cluster.isInited() ){
                    JOptionPane.showMessageDialog( view,
                        SanBootView.res.getString("MenuAndBtnCenter.error.notInited")
                    );
                    return;
                }
            }

            String bkClntId = prof.getUniProIdentity().getClntID();
            if( cluster != null ){
                host = view.initor.mdb.getHostFromVectOnD2DClntID1( bkClntId );
                if( host == null ){
SanBootView.log.error( getClass().getName()," not find boot host according to d2d_clnt_id:"+ prof.getUniProIdentity().getClntID() );
                    JOptionPane.showMessageDialog(view,
                        SanBootView.res.getString("common.errcode.inconsistentProf")
                    );
                    return;
                }
            }

            BackupClient bkClnt = view.initor.mdb.getClientFromVectorOnID( bkClntId );
            if( bkClnt.getPort() != host.getMtppPort() ){
                // 容错性代码。2011.5.24
                bkClnt.setPort( host.getMtppPort() );
            }

            if( !view.initor.mdb.checkProfile( prof ) ){
                JOptionPane.showMessageDialog( view,
                    view.initor.mdb.getProfErrMsg()
                );
                return;
            }

            UniProIdentity identity = prof.getUniProIdentity();
            String bkobjId = prof.getUniProIdentity().getBkObj_ID();
            // 进去之前，先重新获取一下backup object list,
            // 因为备份的时候，bkobj会变化，所以只能
            // update all bk obj
            view.initor.mdb.updateBakObjList( bkobjId );
            BakObject bkObj = view.initor.mdb.getOneBakObject();
            if( bkObj == null ){
SanBootView.log.error( getClass().getName()," missing bkobj in profile: "+prof.getProfileName() );
                JOptionPane.showMessageDialog( view,
                    SanBootView.res.getString("EditProfileDialog.error.lostBakObj")
                );
                return;
            }
            identity.setBkObj_SN( bkObj.getBakObjSN() +"" );

            File tmpFile = view.initor.mdb.createTmpFile( ResourceCenter.PREFIX,ResourceCenter.SUFFIX_PROF );
            if( tmpFile == null ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("common.errmsg.crtTmpFileLocallyFailed")
                );
                return;
            }

            // 发送profile的内容
            if( !view.initor.mdb.sendFileToServer( tmpFile.getName(),prof.prtMe() ) ){
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("common.errmsg.sendFileFailed")+" : "+
                    view.initor.mdb.getErrorMessage()
                );
                tmpFile.delete();
                return;
            }
            tmpFile.delete();

            // 将tmpFile move to profile dir
            boolean ok = view.initor.mdb.moveFile(
                ResourceCenter.TMP_DIR + tmpFile.getName(),
                ResourceCenter.PROFILE_DIR + prof.toString()
            );
            if( !ok ){
                JOptionPane.showMessageDialog(view,
                    ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_PROFILE )+
                    ": "+
                    SanBootView.res.getString("common.failed")
                );
                return;
            }

            // 开始备份
            RunBackup dup = null;
            try{
SanBootView.log.info( getClass().getName(),"data-dup profile: \n" + prof.prtMe() );
                dup = new RunBackup(
                    ResourceCenter.getCmd( ResourceCenter.CMD_DATA_DUP )+
                    prof.getProfileName() +
                    " -clnt=" +
                    bkClnt.getID(),
                    view.getSocket()
                );

SanBootView.log.info( getClass().getName(), " data-duplication cmd: " + dup.getCmdLine() );

                dup.run();
            }catch( Exception ex ){
                dup.setExceptionErrMsg( ex);
                dup.setExceptionRetCode( ex );
            }

SanBootView.log.info( getClass().getName(), " data-duplication cmd retcode: " + dup.getRetCode() );

            Audit audit = view.audit.registerAuditRecord( host.getID(), MenuAndBtnCenterForMainUi.FUNC_RUN_PROF );

            if( dup.isOk() ){
                audit.setEventDesc( "Run profile: " + prof.getProfileNameWithoutExtName() + " successfully." );
                view.audit.addAuditRecord( audit );

                MonitorDialog dialog = new MonitorDialog( view );
                TaskListGeter geter = new TaskListGeter(
                    dialog,
                    view
                );
                geter.start();

                int width  = 585+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
                int height = 335+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
                dialog.setSize( width,height );
                dialog.setLocation( view.getCenterPoint( width,height ) );

                dialog.setVisible( true );
            }else{
                audit.setEventDesc( "Failed to run profile: " + prof.getProfileNameWithoutExtName() );
                view.audit.addAuditRecord( audit );

SanBootView.log.error( getClass().getName(), " data-duplication cmd errmsg: " + dup.getErrMsg() );
                JOptionPane.showMessageDialog(view,
                    SanBootView.res.getString("EditProfileDialog.error.bakCmdFail")
                );
            }
        }
SanBootView.log.info(getClass().getName(),"########### End of run profile action. " );
    }
}
