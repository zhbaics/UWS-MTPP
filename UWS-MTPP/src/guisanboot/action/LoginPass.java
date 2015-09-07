/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guisanboot.action;

import guisanboot.MenuAndBtnCenterForMainUi;
import guisanboot.audit.data.BackupUser;
import guisanboot.res.ResourceCenter;
import guisanboot.ui.BasicGetSomethingThread;
import guisanboot.ui.GeneralActionForMainUi;
import guisanboot.ui.ModifyProfileThread;
import guisanboot.ui.SanBootView;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author zourishun
 */
public class LoginPass extends GeneralActionForMainUi {
    public LoginPass() {
        super(
            ResourceCenter.SMALL_EDIT_PASSWORD,
            ResourceCenter.SMALL_EDIT_PASSWORD,
            "View.MenuItem.modPass",
            MenuAndBtnCenterForMainUi.FUNC_MOD_PASS
        );
    }

    @Override public void doAction(ActionEvent evt){
SanBootView.log.info(getClass().getName(),"########### Entering login password config action. " );
        BackupUser user = view.initor.mdb.getBakUserOnName( view.initor.user );
        if( user == null ) {
SanBootView.log.error( getClass().getName(),"Can't find user which name is : "+ view.initor.user );
            JOptionPane.showMessageDialog(view,
                SanBootView.res.getString("MenuAndBtnCenter.error.findUser")
            );
            return;
        }

        guisanboot.ui.InputPasswordDialog dialog = new guisanboot.ui.InputPasswordDialog( view,user );
        int width  = 285+ResourceCenter.GLOBAL_DELTA_WIDTH_SIZE;
        int height = 230+ResourceCenter.GLOBAL_DELTA_HIGH_SIZE;
        dialog.setSize( width,height );
        dialog.setLocation( view.getCenterPoint( width,height ) );
        dialog.setVisible( true );

        Object _name = dialog.getNewName();
        if( _name == null ) return;

        String  name = (String )_name;
        String  newPass = dialog.getNewPasswd();

        modUser thread = new modUser( view,user,name,newPass,view.initor.port,view.initor.txIp );
        view.startupProcessDiag(
            SanBootView.res.getString("View.pdiagTitle.modUser"),
            SanBootView.res.getString("View.pdiagTip.modUser"),
            thread
        );
SanBootView.log.info(getClass().getName(),"########### End of login password config action. " );
    }

    class modUser extends BasicGetSomethingThread {
        private BackupUser user;
        private String name;
        private String pass;
        private int port;
        private String txIp;

        public modUser( SanBootView view,BackupUser user,String name,String pass,int port,String txIp){
            super( view );
            this.user = user;
            this.name = name;
            this.pass = pass;
            this.port = port;
            this.txIp = txIp;
        }

        public boolean realRun( ){
            boolean isOK = view.initor.mdb.modBackupUser( user.getID(), name,pass );
            if( isOK ){
                user.setUserName( name );
                user.setPasswd( pass );
                view.initor.user = name;
                view.initor.passwd = pass;

                // modify dhcp user and dhcp password
                String tgt_srv = view.initor.mdb.getHostName();
                String dhcp_srv = view.initor.dhcpdb.getHostName();
SanBootView.log.debug( getClass().getName()," tgt_srv name: "+ tgt_srv );
SanBootView.log.debug( getClass().getName()," dhcp_srv name: "+ dhcp_srv );
                if( !tgt_srv.equals("") && !dhcp_srv.equals("") && tgt_srv.equals( dhcp_srv ) ){
                    // 被管理的uws server和指定的dhcp是同一台机器�
                    view.initor.dhcp_acct = name;
                    view.initor.dhcp_passwd = pass;
                    if( !saveDhcpConf( view.initor.serverIp,view.initor.port,name,pass ) ){
                        return false;
                    }
                }

                ModifyProfileThread modProf = new ModifyProfileThread( view, name, pass, txIp, port );
                if( !modProf.realRun() ){
                    errMsg = modProf.getErrMsg();
                    return false;
                }else{
                    return true;
                }
            }else{
                errMsg = SanBootView.res.getString("InputPasswordDialog.msg.modfail")+
                        view.initor.mdb.getErrorMessage();
                return false;
            }
        }

        boolean saveDhcpConf( String ip,int port,String user,String pass ){
            File tmpFile = view.initor.mdb.createTmpFile( ResourceCenter.PREFIX,ResourceCenter.SUFFIX_PROF );
            if( tmpFile == null ){
                errMsg = SanBootView.res.getString("common.errmsg.crtTmpFileLocallyFailed");
                return false;
            }

            if( !pass.equals("") ){
                pass = SanBootView.util.encUserKey( pass );
            }
            StringBuffer buf = new StringBuffer();
            buf.append( ResourceCenter.DHCP_IP +"="+ip );
            buf.append( "\n"+ResourceCenter.DHCP_PORT +"="+port);
            buf.append( "\n"+ResourceCenter.DHCP_ACCT +"="+user);
            buf.append( "\n"+ResourceCenter.DHCP_PASS +"="+pass);

            if( !view.initor.mdb.sendFileToServer( tmpFile.getName(),buf.toString() ) ){
                errMsg =  SanBootView.res.getString("common.errmsg.sendFileFailed")+" : "+
                    view.initor.mdb.getErrorMessage();
                return false;
            }

            // 将tmpFile move to profile dir
            if( !view.initor.mdb.moveFile( ResourceCenter.TMP_DIR + tmpFile.getName(), ResourceCenter.CLT_IP_CONF+ResourceCenter.DHCP_CONF_FILE ) ) {
                 errMsg = ResourceCenter.getCmdString( ResourceCenter.CMD_MOD_DHCP)+
                    ": "+
                    SanBootView.res.getString("common.failed");
                 return false;
            }

            return true;
        }
    }
}
